/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.*;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.visual.ILayoutPolicyFactory;

import org.eclipse.ve.internal.propertysheet.INeedData;
/**
 * Default PropertySourceAdapter for org.eclipse.swt.widgets.Control
 * 
 * If the composite's layout manager is null then bounds/size and location are shown,
 * otherwise they are read only
 */

public class ControlPropertySourceAdapter extends WidgetPropertySourceAdapter {
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		
		IPropertyDescriptor[] descriptors = super.getPropertyDescriptors();

		IJavaObjectInstance compositeJavaObjectInstance = null;
		if(getEObject().eResource() != null) {
			compositeJavaObjectInstance = (IJavaObjectInstance) InverseMaintenanceAdapter.getFirstReferencedBy(
					getEObject(),
					JavaInstantiation.getReference(
							Utilities.getJavaClass("org.eclipse.swt.widgets.Composite",getEObject().eResource().getResourceSet()), //$NON-NLS-1$
							SWTConstants.SF_COMPOSITE_CONTROLS));
		}
		
		boolean explicitUserSizing = false;
		// Top level things like Shells don't have a parent
		EReference sfLayout = null; 
		if (compositeJavaObjectInstance == null) {
			explicitUserSizing = true;
		} else {
			sfLayout = JavaInstantiation.getReference(compositeJavaObjectInstance, SWTConstants.SF_COMPOSITE_LAYOUT);
			if (!compositeJavaObjectInstance.eIsSet(sfLayout)) {
				CompositeProxyAdapter compositeProxyAdapter = (CompositeProxyAdapter) BeanProxyUtilities
						.getBeanProxyHost(compositeJavaObjectInstance);
				if (compositeProxyAdapter.isBeanProxyInstantiated()) {
					IBeanProxy layoutBeanProxy = BeanSWTUtilities.invoke_getLayout(compositeProxyAdapter.getBeanProxy());
					// null layout will require explicit sizing.
					if (layoutBeanProxy == null)
						explicitUserSizing = true;
				}
			} else if (compositeJavaObjectInstance.eGet(sfLayout) == null)
				explicitUserSizing = true;	// Explicitly set, and set to null.
		}
		
		List descriptorList = new ArrayList(descriptors.length);			
		loop: for (int i = 0; i<descriptors.length; i++) {
			IPropertyDescriptor pd = descriptors[i];
			if (pd.getId() instanceof EStructuralFeature) {
				EStructuralFeature sf = (EStructuralFeature)pd.getId();
				String fn = sf.getName();				
				if(explicitUserSizing) {
					// exclude the layoutData property
					if ("layoutData".equals(fn)) //$NON-NLS-1$
						continue loop;
				} else {
					// exclude bounds/size/location because we have a layout manager
					if ("bounds".equals(fn) || "size".equals(fn) || "location".equals(fn)) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						continue loop;
				}

				// LayoutData is wrappered so it is treated differently to allow values on un-set layoutData instances to be set
				if("layoutData".equals(fn)){ //$NON-NLS-1$
					// We need the class of the layoutData to set.  This comes from looking at the policyFactory
					// for our container's layoutManager
					CompositeProxyAdapter compositeProxyAdapter = (CompositeProxyAdapter)BeanProxyUtilities.getBeanProxyHost(compositeJavaObjectInstance);
					EditDomain domain = compositeProxyAdapter.getBeanProxyDomain().getEditDomain();
					// If instantiated we can get the true layout, else we have to guess by using local setting.
					IPropertyDescriptor layoutPD = null;
					if (compositeProxyAdapter.isBeanProxyInstantiated()) {
						ILayoutPolicyFactory factory = BeanSWTUtilities.getLayoutPolicyFactory(compositeProxyAdapter.getBeanProxy(), domain);
						layoutPD = factory.getConstraintPropertyDescriptor(sf);
					} else {
						EObject layoutSetting = (EObject) compositeJavaObjectInstance.eGet(sfLayout);
						ILayoutPolicyFactory factory = BeanSWTUtilities.getLayoutPolicyFactoryFromLayout(layoutSetting != null ? layoutSetting.eClass() : null, domain);
						layoutPD = factory.getConstraintPropertyDescriptor(sf);
					}
					if (layoutPD != null) {
						if (layoutPD instanceof INeedData) 
							((INeedData)layoutPD).setData(domain);
						descriptorList.add(layoutPD);
					}
				} else {
					descriptorList.add(pd);
				}
			} else {
				descriptorList.add(pd);  // Without a structural feature it is included as required by constructor style bits
			}
		}
		return (IPropertyDescriptor[]) descriptorList.toArray(new IPropertyDescriptor[descriptorList.size()]);
	}
}

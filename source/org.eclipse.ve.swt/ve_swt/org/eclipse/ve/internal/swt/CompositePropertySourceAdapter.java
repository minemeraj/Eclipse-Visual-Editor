package org.eclipse.ve.internal.swt;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CompositePropertySourceAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2004-03-04 17:00:21 $ 
 */
import java.util.*;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.jem.internal.beaninfo.adapters.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.java.JavaClass;
/**
 * Default PropertySourceAdapter for org.eclipse.swt.widgets.Control
 * 
 * If the composite's layout manager is null then bounds/size and location are shown,
 * otherwise they are read only
 */

public class CompositePropertySourceAdapter extends BeanPropertySourceAdapter {
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		
		IPropertyDescriptor[] descriptors = super.getPropertyDescriptors();
		
		IJavaObjectInstance compositeJavaObjectInstance = (IJavaObjectInstance) InverseMaintenanceAdapter.getFirstReferencedBy(
				getEObject(),
				JavaInstantiation.getReference(
						(JavaClass) Utilities.getJavaClass("org.eclipse.swt.widgets.Composite",getEObject().eResource().getResourceSet()),
						SWTConstants.SF_COMPOSITE_CONTROLS));
		
		boolean explicitUserSizing = false;
		// Top level things like Shells don't have a parent
		if (compositeJavaObjectInstance == null) {
			explicitUserSizing = true;
		} else {
			CompositeProxyAdapter compositeProxyAdapter = (CompositeProxyAdapter) BeanProxyUtilities.getBeanProxyHost(compositeJavaObjectInstance);
			IBeanProxy layoutBeanProxy = compositeProxyAdapter.getLayoutBeanProxy();
			if(layoutBeanProxy == null) explicitUserSizing = true;
		}
		
		List descriptorList = new ArrayList(descriptors.length);			
		loop: for (int i = 0; i<descriptors.length; i++) {
			IPropertyDescriptor pd = descriptors[i];
			if (pd.getId() instanceof EStructuralFeature) {
				String fn = ((EStructuralFeature) pd.getId()).getName();				
				if(explicitUserSizing) {
					// exclude the layoutData property
					if ("layoutData".equals(fn))
						continue loop;
				} else {
					// exclude bounds/size/location because we have a layout manager
					if ("bounds".equals(fn) || "size".equals(fn) || "location".equals(fn)) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						continue loop;
				}
				descriptorList.add(pd);
			}
		}
		return (IPropertyDescriptor[]) descriptorList.toArray(new IPropertyDescriptor[descriptorList.size()]);
	}
}
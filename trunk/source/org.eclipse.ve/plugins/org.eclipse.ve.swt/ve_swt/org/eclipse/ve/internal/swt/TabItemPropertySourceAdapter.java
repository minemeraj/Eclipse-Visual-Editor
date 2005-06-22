/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TabItemPropertySourceAdapter.java,v $
 *  $Revision: 1.8 $  $Date: 2005-06-22 16:24:10 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.ve.internal.cde.properties.PropertySourceAdapter;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;
import org.eclipse.ve.internal.java.rules.RuledWrapperedPropertyDescriptor;
 

/**
 * 
 * @since 1.0.0
 */
public class TabItemPropertySourceAdapter extends PropertySourceAdapter {

	protected IPropertySource controlPS;
	protected List myDescriptors;
	private EReference sf_items;
	/*
	 * If one of ours, send it on up, else send it to the control.
	 */
	public void setPropertyValue(Object feature, Object val) {
		if (myDescriptors.contains(feature))
			super.setPropertyValue(feature, val);
		else
			controlPS.setPropertyValue(feature, val);
	}
	/*
	 * If one of ours, send it on up, else send it to the control.
	 */
	public void resetPropertyValue(Object feature) {
		if (myDescriptors.contains(feature))
			super.resetPropertyValue(feature);
		else
			controlPS.resetPropertyValue(feature);
	}
	/*
	 * If one of ours, send it on up, else send it to the control.
	 */
	public boolean isPropertySet(Object feature) {
		if (myDescriptors.contains(feature))
			return super.isPropertySet(feature);
	
		return controlPS.isPropertySet(feature);
	}
	/*
	 * If one of ours, send it on up, else send it to the control.
	 */
	public Object getPropertyValue(Object feature) {
		if (myDescriptors == null)
			getPropertyDescriptors();
		if (myDescriptors.contains(feature))
			return super.getPropertyValue(feature);
	
		return controlPS.getPropertyValue(feature);
	}
	/*
	 * Need to merge in the properties of the control with the TabItem properties.
	 * We will have to wrapper any ISourced or ICommand descriptors because
	 * those will have the wrong source passed into them. We need to intercept
	 * and have the correct source (the control) passed into them.
	 * 
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		Resource res = getEObject().eResource();
		if (res == null)
			return new IPropertyDescriptor[0]; // We don't have a resource, we are in anything, really can't go on.

		ResourceSet rset = (getEObject()).eResource().getResourceSet();
		sf_items = JavaInstantiation.getReference(rset, SWTConstants.SF_TABFOLDER_ITEMS);
		EObject tabFolder = InverseMaintenanceAdapter.getFirstReferencedBy(getEObject(), sf_items);
		IBeanProxyHost tabFolderProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) tabFolder);

		IJavaObjectInstance component = (IJavaObjectInstance) getEObject().eGet(
				JavaInstantiation.getSFeature(getEObject().eResource().getResourceSet(), SWTConstants.SF_TABITEM_CONTROL));
		controlPS = (IPropertySource) EcoreUtil.getRegisteredAdapter(component, IPropertySource.class);

		EditDomain domain = tabFolderProxyHost.getBeanProxyDomain().getEditDomain();
		IPropertyDescriptor[] mine = super.getPropertyDescriptors();
		IPropertyDescriptor[] wrappedMine = new IPropertyDescriptor[mine.length];
		int wi = 0;
		for (int i = 0; i < mine.length; i++) {
			IPropertyDescriptor pd = mine[i];
			if (pd.getId() instanceof EStructuralFeature) {
				// We need to wrapper them to change the names so that don't collide with the properties of the control
				String fn = ((EStructuralFeature) pd.getId()).getName();
				if ("text".equals(fn)) { //$NON-NLS-1$
					String tabtext = SWTMessages.TabItemPropertySourceAdapter_tabText; 
					wrappedMine[wi++] = new RuledWrapperedPropertyDescriptor(domain, tabtext, this, pd);
					continue;
				} else if ("image".equals(fn)) { //$NON-NLS-1$
					String tabImage = SWTMessages.TabItemPropertySourceAdapter_tabImage; 
					wrappedMine[wi++] = new RuledWrapperedPropertyDescriptor(domain, tabImage, this, pd);
					continue;
				} else if ("toolTipText".equals(fn)) { //$NON-NLS-1$
					String tabToolTipText = SWTMessages.TabItemPropertySourceAdapter_tabToolTipText; 
					wrappedMine[wi++] = new RuledWrapperedPropertyDescriptor(domain, tabToolTipText, this, pd);
					continue;
				} else if ("control".equals(fn)) //$NON-NLS-1$
					continue;
			}
			wrappedMine[wi++] = new RuledWrapperedPropertyDescriptor(tabFolderProxyHost.getBeanProxyDomain().getEditDomain(), controlPS, pd);
		}
		IPropertyDescriptor[] theirs = controlPS.getPropertyDescriptors();
		IPropertyDescriptor[] wrappedTheirs = new IPropertyDescriptor[theirs.length];
		for (int i = 0; i < theirs.length; i++)
			wrappedTheirs[i] = new RuledWrapperedPropertyDescriptor(tabFolderProxyHost.getBeanProxyDomain().getEditDomain(), controlPS, theirs[i]);
		//Save the ID's for later compares.
		myDescriptors = new ArrayList(wi);
		for (int i = 0; i < wi; i++) {
			myDescriptors.add(wrappedMine[i].getId());
		}
		// Finally build the complete list.
		IPropertyDescriptor[] finalList = new IPropertyDescriptor[theirs.length + wi];
		System.arraycopy(wrappedTheirs, 0, finalList, 0, wrappedTheirs.length);
		System.arraycopy(wrappedMine, 0, finalList, wrappedTheirs.length, wi);
		return finalList;
	}

}

/*****************************************************************************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms
 * of the Common Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************************************************************************/
/*
 * $RCSfile: CoolItemPropertySourceAdapter.java,v $ $Revision: 1.1 $ $Date: 2004-08-20 22:39:14 $
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

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.ve.internal.cde.properties.PropertySourceAdapter;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;
import org.eclipse.ve.internal.java.rules.RuledWrapperedPropertyDescriptor;

import org.eclipse.ve.internal.propertysheet.command.WrapperedPropertyDescriptor;

public class CoolItemPropertySourceAdapter extends PropertySourceAdapter {

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
		if (myDescriptors.contains(feature))
			return super.getPropertyValue(feature);

		return controlPS.getPropertyValue(feature);
	}

	/*
	 * Need to merge in the properties of the control with the TabItem properties. We will have to wrapper any ISourced or ICommand descriptors
	 * because those will have the wrong source passed into them. We need to intercept and have the correct source (the control) passed into them.
	 *  
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {

		ResourceSet rset = (getEObject()).eResource().getResourceSet();
		sf_items = JavaInstantiation.getReference(rset, SWTConstants.SF_COOLBAR_ITEMS);
		EObject tabFolder = InverseMaintenanceAdapter.getFirstReferencedBy(getEObject(), sf_items);
		IBeanProxyHost tabFolderProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) tabFolder);
		Resource res = getEObject().eResource();
		if (res == null)
			return new IPropertyDescriptor[0]; // We don't have a resource, we are in anything, really can't go on.

		IJavaObjectInstance component = (IJavaObjectInstance) getEObject().eGet(
				JavaInstantiation.getSFeature(getEObject().eResource().getResourceSet(), SWTConstants.SF_COOLITEM_CONTROL));
		controlPS = (IPropertySource) EcoreUtil.getRegisteredAdapter(component, IPropertySource.class);

		IPropertyDescriptor[] mine = super.getPropertyDescriptors();
		IPropertyDescriptor[] wrappedMine = new IPropertyDescriptor[mine.length];
		int wi = 0;
		for (int i = 0; i < mine.length; i++) {
			IPropertyDescriptor pd = mine[i];
			if (pd.getId() instanceof EStructuralFeature) {
				String displayname;
				String fn = ((EStructuralFeature) pd.getId()).getName();
				if ("text".equals(fn)) {
					displayname = "coolItemText";
					wrappedMine[wi++] = new WrapperedPropertyDescriptor(pd.getId(), displayname, this, pd);
					continue;
				} else if ("image".equals(fn)) {
					displayname = "coolItemImage";
					wrappedMine[wi++] = new WrapperedPropertyDescriptor(pd.getId(), displayname, this, pd);
					continue;
				} else if ("toolTipText".equals(fn)) {
					displayname = "coolItemToolTipText";
					wrappedMine[wi++] = new WrapperedPropertyDescriptor(pd.getId(), displayname, this, pd);
					continue;
				} else if ("preferredSize".equals(fn)) {
					displayname = "coolItemPreferredSize";
					wrappedMine[wi++] = new WrapperedPropertyDescriptor(pd.getId(), displayname, this, pd);
					continue;
				} else if ("minimumSize".equals(fn)) {
					displayname = "coolItemMinimumSize";
					wrappedMine[wi++] = new WrapperedPropertyDescriptor(pd.getId(), displayname, this, pd);
					continue;
				} else if ("size".equals(fn)) {
					displayname = "coolItemSize";
					wrappedMine[wi++] = new WrapperedPropertyDescriptor(pd.getId(), displayname, this, pd);
					continue;
				} else if ("bounds".equals(fn)) {
					displayname = "coolItemBounds";
					wrappedMine[wi++] = new WrapperedPropertyDescriptor(pd.getId(), displayname, this, pd);
					continue;
				} else if ("control".equals(fn))
					// don't show control property
					continue;
			}
			wrappedMine[wi++] = new RuledWrapperedPropertyDescriptor(tabFolderProxyHost.getBeanProxyDomain().getEditDomain(), controlPS, pd);
		}
		IPropertyDescriptor[] theirs = controlPS.getPropertyDescriptors();
		//Save the ID's for later compares.
		myDescriptors = new ArrayList(wi);
		for (int i = 0; i < wi; i++) {
			myDescriptors.add(wrappedMine[i].getId());
		}
		// Finally build the complete list.
		IPropertyDescriptor[] finalList = new IPropertyDescriptor[theirs.length + wi];
		System.arraycopy(theirs, 0, finalList, 0, theirs.length);
		System.arraycopy(wrappedMine, 0, finalList, theirs.length, wi);
		return finalList;
	}
}
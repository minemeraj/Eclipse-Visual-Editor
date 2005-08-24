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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: JTabComponentPropertySourceAdapter.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:09 $ 
 */
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.properties.PropertySourceAdapter;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;
import org.eclipse.ve.internal.java.rules.RuledWrapperedPropertyDescriptor;

/**
 * Property source adapter for the special features for components added to a JTabbedPane
 */
public class JTabComponentPropertySourceAdapter extends PropertySourceAdapter {

	protected IPropertySource componentPS;
	protected List myDescriptors;

	/*
	 * Need to merge in the properties of the component.
	 * We will have to wrapper any ISourced or ICommand descriptors because
	 * those will have the wrong source passed into them. We need to intercept
	 * and have the correct source (the component) passed into them.
	 *
	 * 
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IBeanProxyHost containerProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) getEObject().eContainer());
		
		Resource res = getEObject().eResource();
		if (res == null)
			return new IPropertyDescriptor[0];	// We don't have a resource, we are in anything, really can't go on.
			
		IJavaObjectInstance component =
			(IJavaObjectInstance) getEObject().eGet(JavaInstantiation.getSFeature(getEObject().eResource().getResourceSet(), JFCConstants.SF_JTABCOMPONENT_COMPONENT));
		componentPS = (IPropertySource) EcoreUtil.getRegisteredAdapter(component,IPropertySource.class);
		IPropertyDescriptor[] theirs = componentPS.getPropertyDescriptors();

		IPropertyDescriptor[] wrappedTheirs = new IPropertyDescriptor[theirs.length];
		int wi = 0;
		for (int i = 0; i < theirs.length; i++) {
			IPropertyDescriptor pd = theirs[i];
			if (pd.getId() instanceof EStructuralFeature) {
				// exclude bounds/size/location if 
				String fn = ((EStructuralFeature) pd.getId()).getName();
				if ("bounds".equals(fn) || "size".equals(fn) || "location".equals(fn)) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					continue;
			}
			wrappedTheirs[wi++] = new RuledWrapperedPropertyDescriptor(containerProxyHost.getBeanProxyDomain().getEditDomain(), componentPS, pd);
		}

		// Finally build the complete list.
		IPropertyDescriptor[] mine = super.getPropertyDescriptors();
		//Save the ID's for later compares.
		myDescriptors = new ArrayList(mine.length);
		for (int i = 0; i < mine.length; i++) {
			myDescriptors.add(mine[i].getId());
		}
		IPropertyDescriptor[] finalList = new IPropertyDescriptor[mine.length + wi];
		System.arraycopy(wrappedTheirs, 0, finalList, 0, wi);
		System.arraycopy(mine, 0, finalList, wi, mine.length);
		return finalList;
	}

	/*
	 * If one of ours, send it on up, else send it to the component.
	 */
	public Object getPropertyValue(Object feature) {
		if (myDescriptors.contains(feature))
			return super.getPropertyValue(feature);

		return componentPS.getPropertyValue(feature);
	}

	/*
	 * If one of ours, send it on up, else send it to the component.
	 */
	public boolean isPropertySet(Object feature) {
		if (myDescriptors.contains(feature))
			return super.isPropertySet(feature);

		return componentPS.isPropertySet(feature);
	}

	/*
	 * If one of ours, send it on up, else send it to the component.
	 */
	public void resetPropertyValue(Object feature) {
		if (myDescriptors.contains(feature))
			super.resetPropertyValue(feature);
		else
			componentPS.resetPropertyValue(feature);
	}

	/*
	 * If one of ours, send it on up, else send it to the component.
	 */
	public void setPropertyValue(Object feature, Object val) {
		if (myDescriptors.contains(feature))
			super.setPropertyValue(feature, val);
		else
			componentPS.setPropertyValue(feature, val);
	}
}

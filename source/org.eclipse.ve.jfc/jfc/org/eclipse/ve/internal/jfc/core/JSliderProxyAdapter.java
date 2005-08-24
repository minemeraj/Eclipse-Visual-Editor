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
/*
 *  $RCSfile: JSliderProxyAdapter.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:10 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.IExpression;
import org.eclipse.jem.internal.proxy.core.IProxy;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;

/**
 * JSlider proxy adapter.
 * 
 * @since 1.1.0
 */
public class JSliderProxyAdapter extends ComponentProxyAdapter {

	protected EStructuralFeature sfMajorTicks, sfLabelTable;

	/**
	 * Constructor for JSliderProxyAdapter.
	 * 
	 * @param domain
	 */
	public JSliderProxyAdapter(IBeanProxyDomain domain) {
		super(domain);

		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sfMajorTicks = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSLIDER_MAJORTICKS);
		sfLabelTable = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSLIDER_LABELTABLE);
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.jfc.core.ComponentProxyAdapter#applyBeanProperty(org.eclipse.jem.internal.beaninfo.PropertyDecorator, org.eclipse.jem.internal.proxy.core.IProxy, org.eclipse.jem.internal.proxy.core.IExpression, boolean)
	 */
	protected IProxy applyBeanProperty(PropertyDecorator propertyDecorator, IProxy settingProxy, IExpression expression, boolean getOriginalValue)
			throws NoSuchMethodException, NoSuchFieldException {
		// Because of the way the labels work on JSlider, once you set the majorTicks it creates
		// the labels, but if you then change the majorTicks again, the labels are not changed again.
		// So we need to test to see if it is major ticks, and if it is, and label table NOT set, then we need to ALSO apply null
		// to the label table first so that it will always recreate the labels on each major tick change.
		if (!inInstantiation() && propertyDecorator.getEModelElement() == sfMajorTicks && !getEObject().eIsSet(sfLabelTable)) {
			super.applyBeanProperty(Utilities.getPropertyDecorator(sfLabelTable), null, expression, false);
		}

		return super.applyBeanProperty(propertyDecorator, settingProxy, expression, getOriginalValue);
	}

}

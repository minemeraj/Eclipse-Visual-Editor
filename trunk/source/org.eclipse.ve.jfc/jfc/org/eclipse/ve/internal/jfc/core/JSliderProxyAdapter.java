package org.eclipse.ve.internal.jfc.core;
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
 *  $RCSfile: JSliderProxyAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;

/**
 * @author richkulp
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class JSliderProxyAdapter extends ComponentProxyAdapter {

	protected EStructuralFeature sfMajorTicks, sfLabelTable;
	/**
	 * Constructor for JSliderProxyAdapter.
	 * @param domain
	 */
	public JSliderProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sfMajorTicks = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSLIDER_MAJORTICKS);		
		sfLabelTable = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSLIDER_LABELTABLE);		
	}
	

	/**
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#applied(EStructuralFeature, Object, int)
	 */
	protected void applied(EStructuralFeature sf, Object newValue, int position) {
		// Because of the way the labels work on JSlider, once you set the majorTicks it creates
		// the labels, but if you then change the majorTicks again, the labels are not changed again.
		// So we need to test to see if it is major ticks, and if it is, then request reinstantiation
		// if not in instantiation.
		if (sf == sfMajorTicks && !inInstantiation() && !getEObject().eIsSet(sfLabelTable))
			throw new ReinstantiationNeeded();
		super.applied(sf, newValue, position);
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#canceled(EStructuralFeature, Object, int)
	 */
	protected void canceled(EStructuralFeature sf, Object oldValue, int position) {
		// Because of the way the labels work on JSlider, once you set the majorTicks it creates
		// the labels, but if you then change the majorTicks again, the labels are not changed again.
		// So we need to test to see if it is major ticks, and if it is, then request reinstantiation
		// if not in instantiation.
		if (sf == sfMajorTicks && !inInstantiation() && !getEObject().eIsSet(sfLabelTable))
			throw new ReinstantiationNeeded();		
		super.canceled(sf, oldValue, position);
	}

}

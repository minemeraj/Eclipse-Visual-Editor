/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

/*
 *  $RCSfile: BoxLayoutEditPolicy.java,v $
 *  $Revision: 1.4 $  $Date: 2004-08-27 15:34:48 $ 
 */

public class BoxLayoutEditPolicy extends FlowLayoutEditPolicy {
	
	private boolean	orientation;	// true is HORIZONTAL

	EStructuralFeature sfLayout = null;
	
	public BoxLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
	   super(containerPolicy);
	}
	/**
	 * @see org.eclipse.gef.editpolicies.FlowLayoutEditPolicy#isHorizontal()
	 */
	protected boolean isHorizontal() {
		return orientation;
	}
			
	/**
	 * Determine the BoxLayout's orientation... horizontal or vertical,
	 * based on the box layout manager's initialzation string
	 */
	private void determineOrientation() {
		orientation = true;	// Default.
		Object layout = helper.getContainer().eGet(sfLayout);
		if (layout != null && layout instanceof IJavaObjectInstance) {
			// KLUDGE Get live object and delve into the privates to get the axis. This
			// isn't exposed by BoxLayout.
			IBeanProxyHost boxlayoutProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) layout);
			IBeanProxy boxlayoutProxy = boxlayoutProxyHost.getBeanProxy();			
			orientation = BeanAwtUtilities.getBoxLayoutAxis(boxlayoutProxy);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.EditPolicy#activate()
	 */
	public void activate() {
		super.activate();
		// Get the layout structural feature from the target container
		sfLayout = JavaInstantiation.getSFeature(helper.getContainer(),JFCConstants.SF_CONTAINER_LAYOUT);
		determineOrientation();
	}

}

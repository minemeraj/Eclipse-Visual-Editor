package org.eclipse.ve.internal.jfc.core;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

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
 *  $RCSfile: BoxLayoutEditPolicy.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

public class BoxLayoutEditPolicy extends FlowLayoutEditPolicy {
	
	private int 
		HORIZONTAL = 0,
		VERTICAL = 1,
		orientation = HORIZONTAL;	// default

	EStructuralFeature sfLayout = null;
	
	public BoxLayoutEditPolicy(ContainerPolicy containerPolicy) {
	   super(containerPolicy);
	}
	/**
	 * @see org.eclipse.gef.editpolicies.FlowLayoutEditPolicy#isHorizontal()
	 */
	protected boolean isHorizontal() {
		return orientation == HORIZONTAL;
	}
			
	/**
	 * Determine the BoxLayout's orientation... horizontal or vertical,
	 * based on the box layout manager's initialzation string
	 */
	private void determineOrientation() {
		Object layout = helper.getContainer().eGet(sfLayout);
		if (layout != null && layout instanceof IJavaObjectInstance) {
			// Get the layout's initialization string
			String initString =
				((IJavaObjectInstance) layout).getInitializationString();

            // Search for the BoxLayout's axis constraint
			int lastCommaIndex = initString.lastIndexOf(","); //$NON-NLS-1$
			if (lastCommaIndex != -1) {
				int lastParenthesisIndex = initString.lastIndexOf(")"); //$NON-NLS-1$
				if (lastParenthesisIndex != -1) {
					String axisString = initString.substring(
								lastCommaIndex + 1,
								lastParenthesisIndex).trim();
					axisString.trim();
					if (axisString.equals("0") //$NON-NLS-1$
					|| axisString.equals("X_AXIS") //$NON-NLS-1$
					|| axisString.equals("javax.swing.BoxLayout.X_AXIS")) //$NON-NLS-1$
						orientation = HORIZONTAL;
					else if (axisString.equals("1") //$NON-NLS-1$
					|| axisString.equals("Y_AXIS") //$NON-NLS-1$
					|| axisString.equals("javax.swing.BoxLayout.Y_AXIS")) //$NON-NLS-1$
						orientation = VERTICAL;
				}
			}
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
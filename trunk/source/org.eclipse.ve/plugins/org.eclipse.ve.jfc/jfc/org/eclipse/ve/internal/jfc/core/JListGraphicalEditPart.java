/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: JListGraphicalEditPart.java,v $ $Revision: 1.2 $ $Date: 2004-03-26 23:07:38 $
 */
package org.eclipse.ve.internal.jfc.core;

/**
 * @author sri
 * 
 * To change the template for this generated type comment go to Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JListGraphicalEditPart extends ComponentGraphicalEditPart {

	/**
	 * @param model
	 */
	public JListGraphicalEditPart(Object model) {
		super(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	//	protected void createEditPolicies() {
	//		installEditPolicy(EditPolicy.LAYOUT_ROLE, new VisualModelEditPolicy("javax.swing.JList")); //$NON-NLS-1$
	//		super.createEditPolicies();
	//	}
}

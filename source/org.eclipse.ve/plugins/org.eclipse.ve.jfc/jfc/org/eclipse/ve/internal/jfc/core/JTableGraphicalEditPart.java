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
 *  $RCSfile: JTableGraphicalEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import java.util.Collections;
import java.util.List;



public class JTableGraphicalEditPart extends ComponentGraphicalEditPart {

public JTableGraphicalEditPart(Object aModel){
	super(aModel);
}

//protected void createEditPolicies(){
//	installEditPolicy(EditPolicy.LAYOUT_ROLE, new VisualModelEditPolicy("javax.swing.JTable")); //$NON-NLS-1$
//	super.createEditPolicies();
//}

protected List getModelChildren() {
	// visually there are no children. Though the beans viewer will have columns.
	return Collections.EMPTY_LIST;
}

}
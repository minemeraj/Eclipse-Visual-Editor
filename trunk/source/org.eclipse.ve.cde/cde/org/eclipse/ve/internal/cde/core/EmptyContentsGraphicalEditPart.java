package org.eclipse.ve.internal.cde.core;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EmptyContentsGraphicalEditPart.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:17:59 $ 
 */


/*
 * A simple contents graphical edit part. Used while there is no model.
 * Displays just an empty canvas.
 */
public class EmptyContentsGraphicalEditPart extends ContentsGraphicalEditPart {
	protected void createEditPolicies() {
	}		
}
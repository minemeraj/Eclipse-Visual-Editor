/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: EmptyContentsGraphicalEditPart.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:49 $ 
 */


/*
 * A simple contents graphical edit part. Used while there is no model.
 * Displays just an empty canvas.
 */
public class EmptyContentsGraphicalEditPart extends ContentsGraphicalEditPart {
	protected void createEditPolicies() {
	}		
}

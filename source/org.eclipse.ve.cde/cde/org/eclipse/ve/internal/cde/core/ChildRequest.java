package org.eclipse.ve.internal.cde.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ChildRequest.java,v $
 *  $Revision: 1.1 $  $Date: 2005-03-28 14:14:29 $ 
 */

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;

public class ChildRequest extends Request {

	protected EditPart fEditPart;
	
	public ChildRequest(Object type, EditPart child) {
		super(type);
		fEditPart = child;
	}
	
	public EditPart getChildEditPart() {
		return fEditPart;
	}
}
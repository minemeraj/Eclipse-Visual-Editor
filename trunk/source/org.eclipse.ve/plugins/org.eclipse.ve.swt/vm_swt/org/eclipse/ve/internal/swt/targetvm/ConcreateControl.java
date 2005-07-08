/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile$
 *  $Revision$  $Date$ 
 */
package org.eclipse.ve.internal.swt.targetvm;

import org.eclipse.swt.widgets.*;
 

/**
 * This is the swt.control used when for the "this" when all of the supertypes of the true "this"
 * are abstract. Use this guy in its place.
 * 
 * @since 1.1.0
 */
public class ConcreateControl extends Canvas {

	/**
	 * @param parent
	 * @param style
	 * 
	 * @since 1.1.0
	 */
	public ConcreateControl(Composite parent, int style) {
		super(parent, style);
		setBackground(Environment.getEnvironment(Display.getCurrent()).lightGray);
	}

}

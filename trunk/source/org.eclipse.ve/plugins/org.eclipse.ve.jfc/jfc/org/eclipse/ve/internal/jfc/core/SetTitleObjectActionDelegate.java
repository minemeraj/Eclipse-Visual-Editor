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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: SetTitleObjectActionDelegate.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:10 $ 
 */

/**
 * Object action delegate that brings up a dialog to change the 'title' property for
 * a component.
 */
public class SetTitleObjectActionDelegate extends SetTextPropertyObjectActionDelegate {
	public SetTitleObjectActionDelegate() {
		super("title"); //$NON-NLS-1$
	}
}

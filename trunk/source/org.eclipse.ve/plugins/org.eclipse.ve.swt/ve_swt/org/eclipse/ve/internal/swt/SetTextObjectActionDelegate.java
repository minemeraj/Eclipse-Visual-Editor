package org.eclipse.ve.internal.swt;
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
 *  $RCSfile: SetTextObjectActionDelegate.java,v $
 *  $Revision: 1.1 $  $Date: 2004-04-23 19:49:07 $ 
 */

/**
 * Object action delegate that brings up a dialog to change the 'text' property for
 * a control.
 */
public class SetTextObjectActionDelegate extends SetTextPropertyObjectActionDelegate {
    public SetTextObjectActionDelegate() {
    	super("text"); //$NON-NLS-1$
    }
}
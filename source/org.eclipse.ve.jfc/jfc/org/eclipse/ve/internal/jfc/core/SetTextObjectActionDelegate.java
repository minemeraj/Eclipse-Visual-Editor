package org.eclipse.ve.internal.jfc.core;
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
 *  $RCSfile: SetTextObjectActionDelegate.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:42:05 $ 
 */

/**
 * Object action delegate that brings up a dialog to change the 'text' property for
 * a component.
 */
public class SetTextObjectActionDelegate extends SetTextPropertyObjectActionDelegate {
    public SetTextObjectActionDelegate() {
    	super("text"); //$NON-NLS-1$
    }
}
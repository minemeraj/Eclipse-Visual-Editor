/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;
/*
 *  $RCSfile: SetTextObjectActionDelegate.java,v $
 *  $Revision: 1.2 $  $Date: 2004-08-27 15:35:50 $ 
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

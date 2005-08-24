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
package org.eclipse.ve.internal.swt;
/*
 *  $RCSfile: SetTextObjectActionDelegate.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:52:56 $ 
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

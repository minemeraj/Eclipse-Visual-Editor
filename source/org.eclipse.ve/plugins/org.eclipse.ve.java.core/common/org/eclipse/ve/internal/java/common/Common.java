/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.common;
/*
 *  $RCSfile: Common.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:49 $ 
 */


public interface Common {

int WIN_OPENED = 1; // Window is opened
int WIN_CLOSED = 2;	// Explicitly closed
int DLG_OK = 3;		// Edits accepted closing dialog
int DLG_CANCEL = 4;	// All edits unacceptable closing dialog
int DLG_APPLY = 5;	// Edits accepted keeping dialog open
int DLG_REVERT = 6;	// All edits unacceptable keeping dialog open
int PROP_CHANGED = 7; // PropertyChangeEvent was signalled

}

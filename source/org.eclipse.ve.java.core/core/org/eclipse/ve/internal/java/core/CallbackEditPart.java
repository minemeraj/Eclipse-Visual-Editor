/*
 * Edit parts that have a callback implement this.  It allows polymorphic selection of an edit part
 * by its viewer if it is for the argument callback
 * This could be the edit part itself or one of its children, and a true result to selectCallback(Callback aCallback)
 * means that an edit part was found and successfully selected
 */
package org.eclipse.ve.internal.java.core;
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
 *  $RCSfile: CallbackEditPart.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:23:54 $ 
 */

import org.eclipse.ve.internal.jcm.Callback;

public interface CallbackEditPart {
	
public boolean selectCallback(Callback aCallback);

}

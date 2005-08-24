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
package org.eclipse.ve.internal.java.remotevm;
/*
 *  $RCSfile: IPropertyEditorDialogListener.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:49 $ 
 */

/**
 * This is a listener for either a property editor dialog
 * or a customizer dialog.
 * 
 * The property editor dialog,
 * which is an internal VCE class, uses this to know
 * which button has been hit and to do the right thing.
 *
 * If a user customizer/Property dialog implements this
 * interface, then it will be added as a listener to the
 * dialog so that it can know if cancel or save has been
 * hit. In that case:
 *   1) On Save it must make all of its changes, if it has
 *      not yet made them.
 *   2) On Revert it must undo all of its changes, if it has
 *      made any yet.
 *
 * Creation date: (11/24/99 3:55:27 PM)
 * @author: Joe Winchester
 */
public interface IPropertyEditorDialogListener {
/**
 * If a user customizer or Propery Dialog implements this
 * interface then on revert it must undo all of its changes
 * back to the last save, if it has not yet made them.
 * Creation date: (11/24/99 3:55:40 PM)
 */
void revertPropertyValue();
/**
 * If a user customizer or Propery Dialog implements this
 * interface then on Save it must make all of its changes, 
 * if it has not yet made them.
 * Creation date: (11/24/99 3:55:40 PM)
 */
void savePropertyValue();
}

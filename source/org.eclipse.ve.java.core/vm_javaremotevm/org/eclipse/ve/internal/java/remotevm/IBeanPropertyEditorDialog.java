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
 *  $RCSfile: IBeanPropertyEditorDialog.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:30:49 $ 
 */

import java.awt.Component;

/**
 * This interface is for a JavaBeanPropertyEditor.
 * It is an interface because the dialog could be
 * either an AWT dialog or a Swing dialog.
 *
 * The implementer must be either an AWT or a Swing
 * dialog because Dialog methods will also be called,
 * but since Dialog is not an interface, that can't
 * be added in here.
 * Creation date: (07/26/00 1:55:26 PM)
 * @author: Administrator
 */
public interface IBeanPropertyEditorDialog {
/**
 * Add the argument to the listener list for actions in this dialog
 */
public void addListener(IPropertyEditorDialogListener aListener);
/**
 * Remove the argument from the listener list for actions in this dialog
 */
public void removeListener(IPropertyEditorDialogListener aListener);
/**
 * Set the property editor
 */
public void setPropertyEditor(Component aComponent);
/**
 * Specify whether OK and Cancel buttons should be put onto the dialog
 * true unless explicitly set otherwise
 */
public void decorateWithButtons(boolean abool);
}

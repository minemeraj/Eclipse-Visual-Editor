package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: BeanDialogCellEditor.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.ve.internal.java.common.Common;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
/**
 * Dialog Editor for working with Bean Custom Dialogs.
 * This will invoke a property editor on a remote VM
 * and maintain a pseudo-modal conversation with it
 */
public class BeanDialogCellEditor extends DialogCellEditor implements BeanFeatureEditor.IWrappedCellEditor {
	protected PropertyEditorBeanProxyWrapper fEditorProxy;
	protected String fPropertyName;

/**
 * BeanDialogCellEditor constructor comment.
 */
protected BeanDialogCellEditor(Composite aComposite , PropertyEditorBeanProxyWrapper anEditorProxy, String propertyName) {
	super(aComposite);
	fEditorProxy = anEditorProxy;
	fPropertyName = propertyName;
}


/**
 * openDialogBox method comment.
 */
protected Object openDialogBox(Control cellEditorWindow) {
	
	// Because the custom editor will be in another VM, we need to disable the
	// shell containing the cell editor so that we simulate showModel so that
	// no changes can be made while this is up.
	// The editor proxy has all the magic to do this for us
	// so that the current thread is suspended while the AWT window is open
	IBeanProxy currentValue = fEditorProxy.getValue();
	int returnCode = fEditorProxy.launchCustomEditor(cellEditorWindow.getShell());
	// The return code says whether or not OK was pressed on the property editor
	if ( returnCode == Common.DLG_OK || returnCode == Common.DLG_APPLY ) {
		return fEditorProxy.getValue();	
	} else {
		// Kludge: need to reapply the current value because if canceled, 
		// many property editors don't reinitialize the custom editor when
		// it is requested. So they use whatever was selected at cancel (such
		// as change to a new font instead of the current font). 
		fEditorProxy.setValue(currentValue);
		return null;
	}
}

/**
 * @see IWrappedCellEditor#newValue(String)
 */
public void newValue(String text) {
	setValue(text);
}

}

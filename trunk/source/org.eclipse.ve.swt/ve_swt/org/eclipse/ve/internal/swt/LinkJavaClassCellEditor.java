/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: LinkJavaClassCellEditor.java,v $
 *  $Revision: 1.7 $  $Date: 2005-06-23 01:48:08 $ 
 */
package org.eclipse.ve.internal.swt;

import java.text.MessageFormat;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

import org.eclipse.ve.internal.java.core.StringJavaClassCellEditor;

/**
 * CellEditor for Link. It verifies that the text is valid for a link.
 * 
 * @since 1.1.0
 */
public class LinkJavaClassCellEditor extends StringJavaClassCellEditor {

	private boolean infoMessageDisplayed = false;
	private Link workingLink;

	public LinkJavaClassCellEditor(Composite aComposite) {
		super(aComposite);
	}

	public void dispose() {
		super.dispose();
		if (workingLink != null) {
			Composite parent = workingLink.getParent();	// This is the phony shell we created.
			workingLink = null;
			parent.dispose();	// This will dispose both the phony shell and the link.
		}
	}
	
	protected String isCorrectString(String value) {
		this.infoMessageDisplayed = false;
		if (workingLink == null) 
			workingLink = new Link(new Shell(Display.getCurrent()), SWT.NONE);
		try {
			workingLink.setText(value);
		} catch (Exception e) {
			this.infoMessageDisplayed = true;
			return SWTMessages.LinkJavaClassCellEditor_NoLink_ERROR_; 
		} finally {
			
		}
		return null;
	}

	protected void fireApplyEditorValue() {
		super.fireApplyEditorValue();
		final String value = doGetEditorString();
		if (value != null) {
			String lowCaseValue = value.toLowerCase();
			int openA = lowCaseValue.indexOf("<a>"); //$NON-NLS-1$
			int closeA = lowCaseValue.indexOf("</a>"); //$NON-NLS-1$
			if (((openA == -1) || (closeA == -1) || (openA > closeA)) && (!infoMessageDisplayed && isDirty())) {
				infoMessageDisplayed = true;
				final Display display = Display.getCurrent();
				display.asyncExec(new Runnable() {

					public void run() {
						Shell shell = display.getActiveShell();
						if (shell != null) {
							String message = MessageFormat.format(
									SWTMessages.LinkJavaClassCellEditor_NoLink_INFO_, new Object[] { value}); 
							MessageDialog.openInformation(shell, SWTMessages.LinkJavaClassCellEditor_NoLinkInfoTitle, message); 
						}
					}
				});
			}
		}
	}
}

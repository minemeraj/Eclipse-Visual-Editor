package org.eclipse.ve.internal.jfc.core;
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
 *  $RCSfile: ImageCellEditor.java,v $
 *  $Revision: 1.2 $  $Date: 2004-02-06 20:19:47 $ 
 */
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IFileEditorInput;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.propertysheet.INeedData;

/**
 * Cell editor for javax.swing.ImageIcon.
 * @version 	1.0
 * @author
 */
public class ImageCellEditor extends DialogCellEditor implements IJavaCellEditor, INeedData {

	protected EditDomain fEditDomain;
	private String path = ""; //$NON-NLS-1$

	public ImageCellEditor(Composite parent) {
		super(parent);
	}
	/* 
	 * Create an instance of the MOF BeanType with string specified
	 */
	private Object createValue(String path) {
		if (path == null || path.equals("")) { //$NON-NLS-1$
			return null;
		}

		this.path = path;
		return BeanUtilities.createJavaObject("java.awt.Image", //$NON-NLS-1$
		JavaEditDomainHelper.getResourceSet(fEditDomain), getJavaInitializationString());
	}

	private static final String IMAGE_INITSTRING_START = "java.awt.Toolkit.getDefaultToolkit().getImage("; //$NON-NLS-1$
	/**
	 * Parse through the initialization string and strip out the path information.
	 */
	protected String getPathFromInitializationString(String initStr) {
		if (initStr == null || !initStr.startsWith(IMAGE_INITSTRING_START))
			return ""; // Not valid format //$NON-NLS-1$

		int lastParen = initStr.lastIndexOf(')');
		if (lastParen == -1)
			return ""; // Not valid format //$NON-NLS-1$

		return initStr.substring(IMAGE_INITSTRING_START.length(), lastParen).trim(); // Get the arg
	}
	/**
	 * getJavaInitializationString method comment.
	 */
	public String getJavaInitializationString() {
		return IMAGE_INITSTRING_START + path + ")"; //$NON-NLS-1$
	}

	protected void updateContents(Object aValue) {
		Label lbl = getDefaultLabel();
		if (lbl == null)
			return;

		String text = ""; //$NON-NLS-1$
		if (aValue != null) {
			// TODO need to fix compile error
//			String initString = getPathFromInitializationString(((IJavaObjectInstance) aValue).getInitializationString());
//			if (initString != "") //$NON-NLS-1$
//				text = initString;
		}
		lbl.setText(text);
	}

	public Object openDialogBox(Control cellEditorWindow) {
		IconDialog iconDialog =
			new IconDialog(
				cellEditorWindow.getShell(),
				((IFileEditorInput) fEditDomain.getEditorPart().getEditorInput()).getFile().getProject());

		IJavaObjectInstance aValue = (IJavaObjectInstance) getValue();
		if (aValue != null) {
			// TODO need to fix compile error
//			path = getPathFromInitializationString(aValue.getInitializationString());
//			if (path != "") { //$NON-NLS-1$
//				iconDialog.setValue(path);
//			}
		}
		int returnCode = iconDialog.open();
		// The return code says whether or not OK was pressed on the property editor
		if (returnCode == Window.OK) {
			return createValue(iconDialog.getValue());
		} else
			return null;
	}
	public void setData(Object data) {
		fEditDomain = (EditDomain) data;
	}
}
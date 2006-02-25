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
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IFileEditorInput;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.impl.NaiveExpressionFlattener;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.*;

import org.eclipse.ve.internal.propertysheet.INeedData;

import com.ibm.icu.util.StringTokenizer;

/**
 * Cell editor for javax.swing.ImageIcon.
 * 
 * @version 1.0
 * @author
 */
public class ImageCellEditor extends DialogCellEditor implements IJavaCellEditor2, INeedData {

	protected EditDomain fEditDomain;
	private String path = ""; //$NON-NLS-1$
	private static final String IMAGE_INITSTRING_START = "java.awt.Toolkit.getDefaultToolkit().getImage("; //$NON-NLS-1$
	private static final String IMAGE_CLASSNAME = "java.awt.Toolkit"; //$NON-NLS-1$

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
		return BeanUtilities.createJavaObject(IMAGE_CLASSNAME, JavaEditDomainHelper.getResourceSet(fEditDomain), getJavaAllocation());
	}

	/**
	 * Return the JavaAllocation for the current value.
	 * @return 
	 * @since 1.0.0
	 */
	public JavaAllocation getJavaAllocation() {
		return BeanPropertyDescriptorAdapter.createAllocation(getJavaInitializationString(), fEditDomain);
	}

	/*
	 * Find the argument to the new ImageIcon. Turn it into a string.
	 */
	public static String getPathFromInitializationAllocation(JavaAllocation allocation) {
		if (allocation.isParseTree()) {
			PTExpression exp = ((ParseTreeAllocation) allocation).getExpression();
			if (exp instanceof PTMethodInvocation && (IMAGE_INITSTRING_START.indexOf(((PTMethodInvocation) exp).getName()) != 1)
					&& ((PTMethodInvocation) exp).getArguments().size() == 1) {
				NaiveExpressionFlattener flattener = new NaiveExpressionFlattener();
				((PTExpression) ((PTMethodInvocation) exp).getArguments().get(0)).accept(flattener);
				return flattener.getResult();
			}
		}

		return ""; // Don't know how to handle if not a ParseTree allocation. //$NON-NLS-1$
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

		if (aValue != null) {
			String initStr = getPathFromInitializationAllocation(((IJavaObjectInstance) aValue)
					.getAllocation());
			int ind_first = initStr.indexOf("\""); //$NON-NLS-1$
			int ind_last = initStr.lastIndexOf("\""); //$NON-NLS-1$
			if ((ind_first != -1) && (ind_last != -1)) {
				initStr = initStr.substring(ind_first + 1, ind_last);
				StringTokenizer tokenizer = new StringTokenizer(initStr,"\"\\/"); //$NON-NLS-1$
				String fname = ""; //$NON-NLS-1$
				while (tokenizer.hasMoreTokens()) {
					fname = tokenizer.nextToken();
				}
				lbl.setText(fname);
				return;
			}
		}
		lbl.setText(""); //$NON-NLS-1$
	}
	public Object openDialogBox(Control cellEditorWindow) {
		IconDialog iconDialog = new IconDialog(cellEditorWindow.getShell(), ((IFileEditorInput) fEditDomain.getEditorPart().getEditorInput())
				.getFile().getProject());

		IJavaObjectInstance aValue = (IJavaObjectInstance) getValue();
		if (aValue != null) {
			iconDialog.setValue(getPathFromInitializationAllocation(aValue.getAllocation()));
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

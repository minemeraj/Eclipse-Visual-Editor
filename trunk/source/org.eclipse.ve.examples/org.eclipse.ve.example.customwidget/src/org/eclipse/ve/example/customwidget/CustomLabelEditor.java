/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  Created Feb 28, 2005 by Gili Mendel
 * 
 */
package org.eclipse.ve.example.customwidget;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.IStringBeanProxy;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.*;

import org.eclipse.ve.internal.propertysheet.INeedData;

/**
 * 
 * @since 1.1.0
 */
public class CustomLabelEditor extends DialogCellEditor implements INeedData {

	private EditDomain fEditDomain;	
	
	protected String stringValue = "";

	public CustomLabelEditor(Composite aComposite) {
		super(aComposite);
	}

	private IJavaInstance createStringJavaObject(String aString) {
		return BeanUtilities.createString(JavaEditDomainHelper.getResourceSet(fEditDomain), aString);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
	 */
	protected Object openDialogBox(Control cellEditorWindow) {
		Display display = cellEditorWindow.getDisplay();
		TitleAreaDialog dialog = new TitleAreaDialog(display.getActiveShell()) {
			LabelDialogContent content;
			protected Control createContents(Composite parent) {
				Control result = super.createContents(parent);
				setTitle("Prompter's text property editor");
				setMessage("Enter the text property, or select a default one by checking the Hello or GoodBye",IMessageProvider.INFORMATION);
//				setTitleImage(CustomwidgetPlugin.getSmileyFace());								
				return result;
			}
			protected Control createDialogArea(Composite parent) {
				content = new LabelDialogContent(parent, SWT.NONE);
				content.setString(stringValue);
				return content;
			}
			
			public String toString() {
				return content.getString();
			}
		};	
		
	
		if (dialog.open() != Window.CANCEL) 
			return createStringJavaObject(dialog.toString());
		else
			return getValue();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.propertysheet.INeedData#setData(java.lang.Object)
	 */
	public void setData(Object data) {
		// TODO Auto-generated method stub
		fEditDomain = (EditDomain) data;
	}
	
	protected void doSetValue(Object value) {
		if (value != null){
			IStringBeanProxy stringBeanProxy = (IStringBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) value);
			stringValue = stringBeanProxy.stringValue();
		}
		super.doSetValue(value);
	}
	protected void updateContents(Object value) {
		super.updateContents(stringValue);
	}

}
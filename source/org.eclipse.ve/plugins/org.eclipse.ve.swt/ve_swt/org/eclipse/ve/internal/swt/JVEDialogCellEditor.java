package org.eclipse.ve.internal.swt;
/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2002 - All Rights Reserved. 
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 */

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IFileEditorInput;

import org.eclipse.ve.internal.cde.core.*;

import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.propertysheet.INeedData;

/**
 * Cell editor for javax.swing.ImageIcon.
 * @version 	1.0
 * @author
 */
public class JVEDialogCellEditor extends DialogCellEditor implements IJavaCellEditor, INeedData , IExecutableExtension {

	protected EditDomain fEditDomain;
	private String initString = ""; //$NON-NLS-1$
	private Class chooserClass;
	PropertyEditor chooser = null;
	private Label label;	

	public JVEDialogCellEditor(Composite parent) {
		super(parent);
	}
	/* 
	 * Create an instance of the MOF BeanType with string specified
	 */
	private Object createValue(Object value) {
		
		// Get the string of the type class
		String qualifiedClassName = value.getClass().getName();		
	
		return BeanUtilities.createJavaObject(
			qualifiedClassName, //$NON-NLS-1$
			JavaEditDomainHelper.getResourceSet(fEditDomain), getJavaInitializationString());
	}

	public Object openDialogBox(Control cellEditorWindow) {
		IJavaObjectInstance aValue = (IJavaObjectInstance) getValue();	
		chooser.setValue(aValue);
		PropertyDialogEditor chooseDialog =
			new PropertyDialogEditor(
				cellEditorWindow.getShell(),
				chooser,
				((IFileEditorInput) fEditDomain.getEditorPart().getEditorInput()).getFile().getProject());
		int returnCode = chooseDialog.open();
		// The return code says whether or not OK was pressed on the property editor
		if (returnCode == Window.OK) {
			initString = chooseDialog.getJavaInitializationString();
			return createValue(chooseDialog.getValue());
		} else
			return null;
	}
	public void setData(Object data) {
		fEditDomain = (EditDomain) data;
	}
	/* (non-Javadoc)
	 * @see com.ibm.etools.jbcf.IJBCFCellEditor#getJavaInitializationString()
	 */
	public String getJavaInitializationString() {
		return initString;
	}	
	public void setInitializationData(IConfigurationElement ce, String pName, Object initData) {
		if (initData instanceof String){
			try{
				chooserClass = CDEPlugin.getClassFromString((String)initData);	
				try{
					chooser = (PropertyEditor)chooserClass.newInstance();					
				} catch (Exception exc){
					JavaVEPlugin.log(exc, MsgLogger.LOG_WARNING);					
				}	
			} catch (ClassNotFoundException exc){
				JavaVEPlugin.log(exc, MsgLogger.LOG_WARNING);				
			}
		}
	}
	protected Label getDefaultLabel() {
		return label;
	}	
	protected Control createContents(Composite cell) {
		label = new Label(cell, SWT.LEFT);
		label.setFont(cell.getFont());
		label.setBackground(cell.getBackground());
		return label;
	}	
	protected void updateContents(Object value) {
		if (label == null)
			return;
		if (value instanceof IJavaObjectInstance){
			chooser.setJavaObjectInstanceValue((IJavaObjectInstance)value);
			label.setText(chooser.getText());
		} else {
			label.setText("");
		}
	}	
}

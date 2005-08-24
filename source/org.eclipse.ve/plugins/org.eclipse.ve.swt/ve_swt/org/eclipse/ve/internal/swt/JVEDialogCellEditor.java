/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;

import java.util.logging.Level;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IFileEditorInput;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.workbench.utility.NoASTResolver;
import org.eclipse.jem.workbench.utility.ParseTreeCreationFromAST;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.propertysheet.INeedData;

/**
 * Cell editor for SWT property editors.
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
		return BeanUtilities.createJavaObject(qualifiedClassName, JavaEditDomainHelper.getResourceSet(fEditDomain), getJavaAllocation());
	}

	/*
	 * Create a Parse tree allocation from the initialization string returned from the property editor.
	 * 
	 */
	private JavaAllocation getJavaAllocation() {
		ASTParser parser = ASTParser.newParser(AST.JLS2);
		String initString = getJavaInitializationString();
		parser.setSource(initString.toCharArray());
		parser.setSourceRange(0, initString.length());
		parser.setKind(ASTParser.K_EXPRESSION);
		ASTNode ast = parser.createAST(null);
		if (ast == null)
			return null; // It didn't parse.
	
		ParseTreeCreationFromAST.Resolver res = new NoASTResolver() {
			public PTExpression resolveName(Name name) {
				PTExpression exp = null;
				if (name instanceof QualifiedName &&
						(name.getFullyQualifiedName().startsWith("org.eclipse.swt.SWT.") || //$NON-NLS-1$
						name.getFullyQualifiedName().startsWith("org.eclipse.jface.resource.JFaceResources.") || //$NON-NLS-1$
						name.getFullyQualifiedName().startsWith("org.eclipse.jface.preference.JFacePreferences."))) { //$NON-NLS-1$
					PTExpression receiver = InstantiationFactory.eINSTANCE.createPTName(((QualifiedName)name).getQualifier().getFullyQualifiedName());
					exp = InstantiationFactory.eINSTANCE.createPTFieldAccess(receiver, ((QualifiedName) name).getName().getIdentifier());
					return exp;
				}
				return super.resolveName(name);			
			}
		};
		ParseTreeAllocation alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation();
		alloc.setExpression(new ParseTreeCreationFromAST(res).createExpression((Expression) ast));
		return alloc;
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
		// For property editors that need the EditDomain
		if (chooser != null && chooser instanceof INeedData)
			((INeedData) chooser).setData(data);
	}

	public String getJavaInitializationString() {
		return initString;
	}	
	public void setInitializationData(IConfigurationElement ce, String pName, Object initData) {
		if (initData instanceof String){
			try{
				chooserClass = CDEPlugin.getClassFromString((String)initData);	
				try{
					chooser = (PropertyEditor)chooserClass.newInstance();
					if (chooser instanceof INeedData)
						((INeedData) chooser).setData(fEditDomain);
				} catch (Exception exc){
					JavaVEPlugin.log(exc, Level.WARNING);					
				}	
			} catch (ClassNotFoundException exc){
				JavaVEPlugin.log(exc, Level.WARNING);				
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
			label.setText(""); //$NON-NLS-1$
		}
	}	
}

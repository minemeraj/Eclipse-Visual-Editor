package org.eclipse.ve.internal.swt;
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
 *  $Revision: 1.2 $  $Date: 2004-05-19 23:04:11 $ 
 */

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IFileEditorInput;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.impl.NaiveExpressionFlattener;
import org.eclipse.jem.workbench.utility.NoASTResolver;
import org.eclipse.jem.workbench.utility.ParseTreeCreationFromAST;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.*;

import org.eclipse.ve.internal.propertysheet.INeedData;

/**
 * Cell editor for javax.swing.ImageIcon.
 * TODO Needs to be changed to use parse tree all of the way through to Icon dialog and beyond.
 * @version 	1.0
 * @author
 */
public class ImageCellEditor extends DialogCellEditor implements IJavaCellEditor, INeedData {

	protected EditDomain fEditDomain;
	private String path = ""; //$NON-NLS-1$
	private static final String IMAGE_INITSTRING_START = "org.eclipse.swt.graphics.Image(org.eclipse.swt.widgets.Display.getCurrent()"; //$NON-NLS-1$
	private static final String IMAGE_CLASSNAME = "org.eclipse.swt.graphics.Image";

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
		
		
		return BeanUtilities.createJavaObject(IMAGE_CLASSNAME,
			JavaEditDomainHelper.getResourceSet(fEditDomain), getJavaAllocation());
	}
	
	/**
	 * Return the JavaAllocation for the current value.
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public JavaAllocation getJavaAllocation() {

		ASTParser parser = ASTParser.newParser(AST.LEVEL_2_0);
		String initString = getJavaInitializationString(); 
		parser.setSource(initString.toCharArray());
		parser.setSourceRange(0,initString.length());
		parser.setKind(ASTParser.K_EXPRESSION) ;		
        ASTNode ast = parser.createAST(null);
        if (ast == null)
        	return null;	// It didn't parse.
	
        ParseTreeAllocation alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation();
        alloc.setExpression(new ParseTreeCreationFromAST(new NoASTResolver()).createExpression((Expression) ast));
		return alloc;
	}
	
	/*
	 * Find the argument to the new Image Turn it into a string.
	 */
	public static String getPathFromInitializationAllocation(JavaAllocation allocation) {
		if (allocation instanceof ParseTreeAllocation) {
			PTExpression exp = ((ParseTreeAllocation) allocation).getExpression();
			if (exp instanceof PTClassInstanceCreation && ((PTClassInstanceCreation) exp).getType().equals(IMAGE_CLASSNAME) && ((PTClassInstanceCreation) exp).getArguments().size() == 2) {
				NaiveExpressionFlattener flattener = new NaiveExpressionFlattener();
				((PTExpression) ((PTClassInstanceCreation) exp).getArguments().get(1)).accept(flattener);
				return flattener.getResult();
			}
		}
		
		return "";	// Don't know how to handle if not an ParseTree allocation.
	}
	
	/**
	 * getJavaInitializationString method comment.
	 */
	public String getJavaInitializationString() {
		//return path != null ? "" : "";
		if ((path==null)||(path.equals("")))return "";
		return "new " + IMAGE_INITSTRING_START + ',' + path + ")"; //$NON-NLS-1$
	}

	protected void updateContents(Object aValue) {
		Label lbl = getDefaultLabel(); 
		if (lbl == null)
			return;

		if (aValue != null)
			lbl.setText(getPathFromInitializationAllocation(((IJavaObjectInstance) aValue).getAllocation()));
		else
			lbl.setText("");
	}

	public Object openDialogBox(Control cellEditorWindow) {
		ImageDialog iconDialog =
			new ImageDialog(
				cellEditorWindow.getShell(),
				((IFileEditorInput) fEditDomain.getEditorPart().getEditorInput()).getFile().getProject());

		IJavaObjectInstance aValue = (IJavaObjectInstance) getValue();
		if (aValue != null) {
				iconDialog.setValue(getPathFromInitializationAllocation(((IJavaObjectInstance) aValue).getAllocation()));
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

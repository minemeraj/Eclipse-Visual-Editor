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
 *  $RCSfile: LayoutCellEditor.java,v $
 *  $Revision: 1.5 $  $Date: 2004-03-04 02:13:17 $ 
 */

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.java.*;
import org.eclipse.jem.java.impl.JavaClassImpl;
import org.eclipse.jem.java.impl.JavaRefFactoryImpl;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.IJavaCellEditor;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.ve.internal.propertysheet.ObjectComboBoxCellEditor;
/**
 * The method createItems shows a list of available layout manager classes from which the 
 * user can pick one.
 */
public class LayoutCellEditor extends ObjectComboBoxCellEditor implements IJavaCellEditor, INeedData {
	protected EditDomain fEditDomain;
	
	protected static String[] fItems = new String[]{
		"null" , "FillLayout" , "FormLayout" , "GridLayout" , "RowLayout"
	};
	protected static String[] fClassNames = new String[] {
		"","org.eclipse.swt.layout.FillLayout",  "org.eclipse.swt.layout.FormLayout" , "org.eclipse.swt.layout.GridLayout", "org.eclipse.swt.layout.RowLayout"
	};
/**
 * This method shows a list of available layout manager classes from which the 
 * user can pick one.
 * For now the list is hard coded
 */
public LayoutCellEditor(Composite aComposite){
	super(aComposite,fItems);
}
/**
 * Return an EMF class that represents the constraint bean
 */
protected Object doGetObject(int index) {
	if (index == sNoSelection || index == 0)
		return null;
	String layoutManagerClassName = fClassNames[index];
	ResourceSet rset = JavaEditDomainHelper.getResourceSet(fEditDomain);
	JavaHelpers javaClass = JavaRefFactory.eINSTANCE.reflectType(layoutManagerClassName, rset);
	return BeanUtilities.createJavaObject(javaClass, rset, (String)null);
}

protected int doGetIndex(Object anObject){
	// The argument is an IJavaInstance.  Get its bean proxy and compare it against
	// the values stored by us
	if (anObject == null) {
		return 0;
	} else if (anObject instanceof IJavaObjectInstance) {
		String qualifiedClassName = ((IJavaObjectInstance)anObject).getJavaType().getQualifiedName();
		// Look for this class name in our known list 
		for(int i=1 ; i<fClassNames.length ; i++){
			if (qualifiedClassName.equals(fClassNames[i])){
				return i;
			}
		}
	}
	return sNoSelection;
}

public String getJavaInitializationString() {
	// TODO Are there some managers that aren't default ctor'd. If so, we need a different way 
	// of getting the initialization string. This is only used for the customizer stuff.
	Object v = doGetValue();
	if (v == null)
		return "null"; //$NON-NLS-1$
	else {
		IJavaObjectInstance jv = (IJavaObjectInstance) v;
		return "new " + jv.getJavaType().getQualifiedName() + "()"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
public static String getDisplayName(String className){
	String dispName = null;
	for(int i=0;i<fClassNames.length;i++){
		if(fClassNames[i].equals(className)){
			dispName = fItems[i];
			break;
		}
	}
	if(dispName==null)
		return ""; //$NON-NLS-1$
	else
		return dispName;
}

public String isCorrectObject(Object anObject){
	return null;
}
public void setData(Object data){
	fEditDomain = (EditDomain) data;
}
}

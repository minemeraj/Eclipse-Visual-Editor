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
 *  $Revision: 1.7 $  $Date: 2004-04-01 21:25:06 $ 
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.*;

import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.ve.internal.propertysheet.ObjectComboBoxCellEditor;
/**
 * The method createItems shows a list of available layout manager classes from which the 
 * user can pick one.
 */
public class LayoutCellEditor extends ObjectComboBoxCellEditor implements IJavaCellEditor, INeedData {
	public static final String EDITDOMAINKEY_ITEMS_LIST = "org.eclipse.ve.internal.swt.LayoutCellEditor";
	public static final int CLASSNAMES_INDEX = 0;
	public static final int DISPLAYNAMES_INDEX = 1;
	protected EditDomain fEditDomain;
	
/**
 * This method shows a list of available layout manager classes from which the 
 * user can pick one.
 * The list is determined from overrides.
 */
public LayoutCellEditor(Composite aComposite){
	super(aComposite);
}
/**
 * Return an EMF class that represents the constraint bean
 */
protected Object doGetObject(int index) {
	if (index == NO_SELECTION || index == 0)
		return null;
	String layoutClassName = getLayoutItems(fEditDomain)[CLASSNAMES_INDEX][index];
	ResourceSet rset = JavaEditDomainHelper.getResourceSet(fEditDomain);
	JavaHelpers javaClass = JavaRefFactory.eINSTANCE.reflectType(layoutClassName, rset);
	return BeanUtilities.createJavaObject(javaClass, rset, (String)null);
}

protected int doGetIndex(Object anObject){
	// The argument is an IJavaInstance.  Get its bean proxy and compare it against
	// the values stored by us
	if (anObject == null) {
		return 0;
	} else if (anObject instanceof IJavaObjectInstance) {
		String [] classNames = getLayoutItems(fEditDomain)[CLASSNAMES_INDEX];
		String qualifiedClassName = ((IJavaObjectInstance)anObject).getJavaType().getQualifiedName();
		// Look for this class name in our known list 
		for(int i=1 ; i<classNames.length ; i++){
			if (qualifiedClassName.equals(classNames[i])){
				return i;
			}
		}
	}
	return NO_SELECTION;
}

public String getJavaInitializationString() {
	Object v = doGetValue();
	if (v == null)
		return "null"; //$NON-NLS-1$
	else {
		IJavaObjectInstance jv = (IJavaObjectInstance) v;
		return "new " + jv.getJavaType().getQualifiedName() + "()"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
public static String getDisplayName(EditDomain editDomain, String className){
	String dispName = null;
	String [] [] layoutinfo = getLayoutItems(editDomain);
	for(int i=0;i<layoutinfo[CLASSNAMES_INDEX].length;i++){
		if(layoutinfo[CLASSNAMES_INDEX][i].equals(className)){
			dispName = layoutinfo[DISPLAYNAMES_INDEX][i];
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
	setItems(getLayoutItems(fEditDomain)[DISPLAYNAMES_INDEX]);
}
/**
 * Get the SWT layout class names and display names from the annotations for the
 * SWT interface org.eclipse.swt.widgets.Layout (Layout.override). Look for only those
 * annotations with a source value of "org.eclipse.ve.LayoutInfo"
 * 
 * Each annotation contains two key/value pairs: key =
 * org.eclipse.ve.internal.swt.layoutClass value = the actual layout class
 * name (e.g. org.eclipse.swt.widgets.RowLayout)
 * 
 * key = org.eclipse.ve.internal.swt.layoutDisplayName value = the display name to
 * be one of the items in the combo box list (e.g. RowLayout)
 * 
 * Populate the combo box items with the display names.
 */
public static String [][] getLayoutItems(EditDomain editDomain) {
 // TODO - need to figure out how we NLS all the xmi override information for the layout display names
	if (editDomain == null)
		return null;
	String[][] layoutLists = (String[][]) editDomain.getData(EDITDOMAINKEY_ITEMS_LIST);
	if (layoutLists == null) {
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(editDomain);
		JavaHelpers javaClass = JavaRefFactory.eINSTANCE.reflectType("org.eclipse.swt.widgets.Layout", rset);
		if (javaClass != null) {
			List classNames = new ArrayList();
			List displayNames = new ArrayList();
			List annotations = ((JavaClass) javaClass).getEAnnotations();
			for (int i = 0; i < annotations.size(); i++) {
				if (((EAnnotation) annotations.get(i)).getSource().equals("org.eclipse.ve.LayoutInfo")) {
					EMap details = ((EAnnotation) annotations.get(i)).getDetails();
					String layoutClassName = (String) details.get("org.eclipse.ve.internal.swt.layoutClass");
					String layoutDisplayName = (String) details.get("org.eclipse.ve.internal.swt.layoutDisplayName");
					if (layoutClassName != null && layoutDisplayName != null) {
						classNames.add(layoutClassName);
						displayNames.add(layoutDisplayName);
					}
				}
			}
			if (classNames.size() > 0) {
				layoutLists = new String [2][];
				layoutLists[CLASSNAMES_INDEX] = new String[classNames.size() + 1];
				layoutLists[CLASSNAMES_INDEX][0] = "";
				layoutLists[DISPLAYNAMES_INDEX]  = new String[displayNames.size() + 1];
				layoutLists[DISPLAYNAMES_INDEX][0] = "null";
				System.arraycopy(classNames.toArray(new String [classNames.size()]), 0, layoutLists[CLASSNAMES_INDEX], 1, classNames.size());
				System.arraycopy(displayNames.toArray(new String [displayNames.size()]), 0, layoutLists[DISPLAYNAMES_INDEX], 1, displayNames.size());
				editDomain.setData(EDITDOMAINKEY_ITEMS_LIST, layoutLists);
			}
		}
	}
	return layoutLists;
}
}

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
 *  $RCSfile: LayoutManagerCellEditor.java,v $
 *  $Revision: 1.6 $  $Date: 2004-03-04 23:37:32 $ 
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.IJavaCellEditor;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.visual.ILayoutPolicyFactory;

import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.ve.internal.propertysheet.ObjectComboBoxCellEditor;
/**
 * The method createItems shows a list of available layout manager classes from which the 
 * user can pick one.
 */
public class LayoutManagerCellEditor extends ObjectComboBoxCellEditor implements IJavaCellEditor, INeedData {
	public static final String EDITDOMAINKEY_ITEMS_LIST = "org.eclipse.ve.internal.jfc.core.LayoutManagerCellEditor";
	public static final int CLASSNAMES_INDEX = 0;
	public static final int DISPLAYNAMES_INDEX = 1;
	protected EditDomain fEditDomain;
	
	
/**
 * This method shows a list of available layout manager classes from which the 
 * user can pick one.
 * For now the list is hard coded because there is no way yet ( until we get the VA2000 Java IDE ) 
 * to see all classes that implement a specific interface.
 */
public LayoutManagerCellEditor(Composite aComposite){
	super(aComposite, null);
}
/**
 * Return a MOF class that represents the constraint bean
 */
protected Object doGetObject(int index) {
	if (index == sNoSelection || index == 0)
		return null;
	String layoutManagerClassName = getLayoutManagerItems(fEditDomain)[CLASSNAMES_INDEX][index];
	ResourceSet rset = JavaEditDomainHelper.getResourceSet(fEditDomain);
	JavaHelpers javaClass = JavaRefFactory.eINSTANCE.reflectType(layoutManagerClassName, rset);
	ILayoutPolicyFactory factory =
		BeanAwtUtilities.getLayoutPolicyFactoryFromLayoutManger((EClassifier) javaClass, fEditDomain);
	return factory.getLayoutManagerInstance(javaClass, rset);
}

protected int doGetIndex(Object anObject){
	// The argument is an IJavaInstance.  Get its bean proxy and compare it against
	// the values stored by us
	if (anObject == null) {
		return 0;
	} else if (anObject instanceof IJavaObjectInstance) {
		String [] classNames = getLayoutManagerItems(fEditDomain)[CLASSNAMES_INDEX];
		String className = LayoutManagerLabelProvider.getQualifiedName((IJavaObjectInstance)anObject);
		// Look for this class name in our known list 
		for(int i=1 ; i<classNames.length ; i++){
			if (className.equals(classNames[i])){
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
public static String getDisplayName(EditDomain editDomain, String className){
	String dispName = null;
	String [] [] layoutinfo = getLayoutManagerItems(editDomain);
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
	// populate the combo box items with the layout manager display names
	setItems(getLayoutManagerItems(fEditDomain)[DISPLAYNAMES_INDEX]);
}

	/**
	 * Get the AWT/Swing layout manager class names and display names from the annotations for the
	 * AWT/Swing interface java.awt.LayoutManager (LayoutManager.override). Look for only those
	 * annotations with a source value of "org.eclipse.ve.LayoutInfo"
	 * 
	 * Each annotation contains two key/value pairs: key =
	 * org.eclipse.ve.internal.jfc.core.layoutManagerClass value = the actual layout manager class
	 * name (e.g. java.awt.BorderLayout)
	 * 
	 * key = org.eclipse.ve.internal.jfc.core.layoutManagerDisplayName value = the display name to
	 * be one of the items in the combo box list (e.g. BorderLayout)
	 * 
	 * Populate the combo box items with the display names.
	 */
	public static String [][] getLayoutManagerItems(EditDomain editDomain) {
	 // TODO - need to figure out how we NLS all the xmi override information for the layout manager display names
		if (editDomain == null)
			return null;
		String[][] layoutManagerLists = (String[][]) editDomain.getData(EDITDOMAINKEY_ITEMS_LIST);
		if (layoutManagerLists == null) {
			ResourceSet rset = JavaEditDomainHelper.getResourceSet(editDomain);
			JavaHelpers javaClass = JavaRefFactory.eINSTANCE.reflectType("java.awt.LayoutManager", rset);
			if (javaClass != null) {
				List classNames = new ArrayList();
				List displayNames = new ArrayList();
				List annotations = ((JavaClass) javaClass).getEAnnotations();
				for (int i = 0; i < annotations.size(); i++) {
					if (((EAnnotation) annotations.get(i)).getSource().equals("org.eclipse.ve.LayoutInfo")) {
						EMap details = ((EAnnotation) annotations.get(i)).getDetails();
						String layoutClassName = (String) details.get("org.eclipse.ve.internal.jfc.core.layoutManagerClass");
						String layoutDisplayName = (String) details.get("org.eclipse.ve.internal.jfc.core.layoutManagerDisplayName");
						if (layoutClassName != null && layoutDisplayName != null) {
							classNames.add(layoutClassName);
							displayNames.add(layoutDisplayName);
						}
					}
				}
				if (classNames.size() > 0) {
					layoutManagerLists = new String [2][];
					layoutManagerLists[CLASSNAMES_INDEX] = new String[classNames.size() + 1];
					layoutManagerLists[CLASSNAMES_INDEX][0] = "";
					layoutManagerLists[DISPLAYNAMES_INDEX]  = new String[displayNames.size() + 1];
					layoutManagerLists[DISPLAYNAMES_INDEX][0] = "null";
					System.arraycopy(classNames.toArray(new String [classNames.size()]), 0, layoutManagerLists[CLASSNAMES_INDEX], 1, classNames.size());
					System.arraycopy(displayNames.toArray(new String [displayNames.size()]), 0, layoutManagerLists[DISPLAYNAMES_INDEX], 1, displayNames.size());
					editDomain.setData(EDITDOMAINKEY_ITEMS_LIST, layoutManagerLists);
				}
			}
		}
		return layoutManagerLists;
	}
}

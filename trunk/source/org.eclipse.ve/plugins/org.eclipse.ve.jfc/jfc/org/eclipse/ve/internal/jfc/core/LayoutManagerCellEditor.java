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
 *  $Revision: 1.3 $  $Date: 2004-01-12 21:44:36 $ 
 */

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.java.JavaClass;
import org.eclipse.jem.internal.java.JavaHelpers;
import org.eclipse.jem.internal.java.impl.JavaClassImpl;

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
	protected EditDomain fEditDomain;
	
	protected static String[] fItems = new String[]{
		VisualMessages.getString("Layout.NullLayout") , VisualMessages.getString("Layout.BorderLayout") ,VisualMessages.getString("Layout.BoxLayoutX_AXIS"), VisualMessages.getString("Layout.BoxLayoutY_AXIS"), VisualMessages.getString("Layout.CardLayout"), VisualMessages.getString("Layout.FlowLayout"), VisualMessages.getString("Layout.GridBagLayout"), VisualMessages.getString("Layout.GridLayout") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
	};
	protected static String[] fClassNames = new String[] {
		"","java.awt.BorderLayout", "javax.swing.BoxLayoutX_Axis", "javax.swing.BoxLayoutY_Axis", "java.awt.CardLayout","java.awt.FlowLayout","java.awt.GridBagLayout","java.awt.GridLayout" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
	};
/**
 * This method shows a list of available layout manager classes from which the 
 * user can pick one.
 * For now the list is hard coded because there is no way yet ( until we get the VA2000 Java IDE ) 
 * to see all classes that implement a specific interface.
 */
public LayoutManagerCellEditor(Composite aComposite){
	super(aComposite,fItems);
}
/**
 * Return a MOF class that represents the constraint bean
 */
protected Object doGetObject(int index) {
	if (index == sNoSelection || index == 0)
		return null;
	String layoutManagerClassName = fClassNames[index];
	ResourceSet rset = JavaEditDomainHelper.getResourceSet(fEditDomain);
	// If this is one of the BoxLayout's, we need force reflection
	// on the actual BoxLayout so it will find these special dummy classes.
	if (layoutManagerClassName.equals("javax.swing.BoxLayoutX_Axis") //$NON-NLS-1$
		|| layoutManagerClassName.equals("javax.swing.BoxLayoutY_Axis")) { //$NON-NLS-1$
		JavaHelpers javaClass = JavaClassImpl.reflect("javax.swing.BoxLayout", rset); //$NON-NLS-1$
		if (javaClass != null)
			 ((JavaClass) javaClass).getEAnnotations();
	}
	JavaHelpers javaClass = JavaClassImpl.reflect(layoutManagerClassName, rset);
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
		String className = LayoutManagerLabelProvider.getQualifiedName((IJavaObjectInstance)anObject);
		// Look for this class name in our known list 
		for(int i=1 ; i<fClassNames.length ; i++){
			if (className.equals(fClassNames[i])){
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

/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: DefaultJavaClassCellEditor.java,v $
 *  $Revision: 1.5 $  $Date: 2005-06-22 15:21:39 $ 
 */

import java.text.MessageFormat;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.ve.internal.propertysheet.*;
/**
 * Default cell editor for simple java classes. It takes the typed
 * in string lets the subclass turn that into an initialization string
 * and creates the object from it. It has a simple test
 * for correctness in that it is an instance of the class entered.
 * It asks the subclass to turn a value into a string for editing.
 *
 * It is abstract, subclasses will do the actual retrieval of the
 * initialization string and string value. They must also set the
 * java class by calling setJavaType before it is needed. The approriate
 * time is by overriding setData and setting the class at that
 * time.
 */
public abstract class DefaultJavaClassCellEditor extends ObjectCellEditor implements INeedData , IJavaCellEditor {
	protected Object[] fSources;
	protected JavaHelpers fJavaType;
	protected EditDomain fEditDomain;
	
public DefaultJavaClassCellEditor(Composite aComposite){
	super(aComposite);
}

/**
 * Create an instance of the MOF IJavaInstance of the type String.
 */
protected Object doGetObject(String value){
	return (value != null) ?
		BeanUtilities.createJavaObject(fJavaType, JavaEditDomainHelper.getResourceSet(fEditDomain), getJavaInitializationString(value)) : null;
}
public void setData(Object data){
	fEditDomain = (EditDomain) data;
}
/**
 * getJavaInitializationString: Return the initialization string given the string from the text cell.
 */
protected abstract String getJavaInitializationString(String aString);

/**
 * getJavaInitializationString method comment.
 */
public String getJavaInitializationString() {
	if (isValueValid()) {
		Object value = getSetValue();
		return (value != null) ? 
			getJavaInitializationString(doGetString(value)) : 
			null;
	}
	return null;
}	

/**
 * This can be overridden and replaced to have a more complete
 * test with a more complete error msg. In that case it is
 * not necessary to call super.isCorrectObject.
 */
protected String isCorrectObject(Object value) {
	if (value == null || isInstance(value))
		return null;	// Null is valid object, or it is a java object of the same type.
	else
		return getNotValidMsg(value);
}

/**
 * Helper method to test if the value is an instance of the Java Type we are handling.
 */
protected boolean isInstance(Object value) {
	return ((EClassifier) fJavaType).isInstance(value);
}

/**
 * Override this to return that this is not a valid type.
 * For example for String, if not "java.lang.String" it would
 * return a not String error message. The default message is
 * Not valid plus the toString on the object.
 */
protected String getNotValidMsg(Object value) {
	return MessageFormat.format(PropertysheetMessages.getString("not_valid_WARN_"), new Object[] {value});
}

protected JavaHelpers getJavaType() {
	return fJavaType;
}

protected void setJavaType(JavaHelpers javaType) {
	fJavaType = javaType;
}

}


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
/*
 *  $RCSfile: RectangleBeanInfo.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:11 $ 
 */
package org.eclipse.ve.internal.jfc.beaninfo;

import java.awt.Rectangle;



public class RectangleBeanInfo extends IvjBeanInfo {
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return java.awt.Rectangle.class;
}
/**
 * @return java.beans.PropertyDescriptor[] for the x,y, width and height
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		return new java.beans.PropertyDescriptor[] {
				createFieldPropertyDescriptor("x", Rectangle.class.getField("x"), EMPTY_ARGS), //$NON-NLS-1$ //$NON-NLS-2$
				createFieldPropertyDescriptor("y", Rectangle.class.getField("y"), EMPTY_ARGS), //$NON-NLS-1$ //$NON-NLS-2$
				createFieldPropertyDescriptor("width", Rectangle.class.getField("width"), EMPTY_ARGS), //$NON-NLS-1$ //$NON-NLS-2$
				createFieldPropertyDescriptor("height", Rectangle.class.getField("height"), EMPTY_ARGS) //$NON-NLS-1$ //$NON-NLS-2$
		};
	} catch (SecurityException e) {
		handleException(e);
	} catch (NoSuchFieldException e) {
		handleException(e);
	}
	return null;
}
}



package org.eclipse.ve.internal.jfc.beaninfo;

import java.awt.Point;

/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: PointBeanInfo.java,v $
 *  $Revision: 1.4 $  $Date: 2005-04-05 21:52:42 $ 
 */


public class PointBeanInfo extends IvjBeanInfo {
/**
 * Gets the bean class.
 */
public Class getBeanClass() {
	return java.awt.Point.class;
}
/**
 * @return java.beans.PropertyDescriptor[] for the x and y
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		return new java.beans.PropertyDescriptor[] {
				createFieldPropertyDescriptor("x", Point.class.getField("x"), EMPTY_ARGS), //$NON-NLS-1$ //$NON-NLS-2$
				createFieldPropertyDescriptor("y", Point.class.getField("y"), EMPTY_ARGS) //$NON-NLS-1$ //$NON-NLS-2$
		};
	} catch (SecurityException e) {
		handleException(e);
	} catch (NoSuchFieldException e) {
		handleException(e);
	}
	return null;
}
}



package org.eclipse.ve.internal.jfc.beaninfo;

import java.awt.Dimension;

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
 *  $RCSfile: DimensionBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-02-16 00:43:34 $ 
 */


public class DimensionBeanInfo extends IvjBeanInfo {
public Class getBeanClass() {
	return java.awt.Dimension.class;
}
/**
 * @return java.beans.PropertyDescriptor[] for the width and height fields
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		return new java.beans.PropertyDescriptor[] {
				createFieldPropertyDescriptor("width", Dimension.class.getField("width"), EMPTY_ARGS),
				createFieldPropertyDescriptor("height", Dimension.class.getField("height"), EMPTY_ARGS)
		};
	} catch (SecurityException e) {
		handleException(e);
	} catch (NoSuchFieldException e) {
		handleException(e);
	}
	return null;
}
}
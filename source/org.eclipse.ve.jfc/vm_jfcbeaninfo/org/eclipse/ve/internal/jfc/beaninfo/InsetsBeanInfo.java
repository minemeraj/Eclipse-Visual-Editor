package org.eclipse.ve.internal.jfc.beaninfo;

import java.awt.Insets;

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
 *  $RCSfile: InsetsBeanInfo.java,v $
 *  $Revision: 1.1 $  $Date: 2005-02-16 00:43:34 $ 
 */


public class InsetsBeanInfo extends IvjBeanInfo {
/**
 * Gets the bean class.
 */
public Class getBeanClass() {
	return java.awt.Insets.class;
}
/**
 * @return java.beans.PropertyDescriptor[] for the top, bottom, left and right
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		return new java.beans.PropertyDescriptor[] {
				createFieldPropertyDescriptor("top", Insets.class.getField("top"), EMPTY_ARGS),
				createFieldPropertyDescriptor("bottom", Insets.class.getField("bottom"), EMPTY_ARGS),
				createFieldPropertyDescriptor("left", Insets.class.getField("left"), EMPTY_ARGS),
				createFieldPropertyDescriptor("right", Insets.class.getField("right"), EMPTY_ARGS),				
		};
	} catch (
			SecurityException e) {
		handleException(e);
	} catch (NoSuchFieldException e) {
		handleException(e);
	}
	return null;
}
}



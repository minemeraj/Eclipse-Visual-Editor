package org.eclipse.ve.internal.jfc.beaninfo;
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
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:44:12 $ 
 */


public class PointBeanInfo extends IvjBeanInfo {
/**
 * Gets the bean class.
 */
public Class getBeanClass() {
	return java.awt.Point.class;
}
/**
 * There should be no properties for point
 * Returning an empty array means the introspector will not use defualt
 * introspection
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	return new java.beans.PropertyDescriptor[0];
}
}



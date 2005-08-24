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
package org.eclipse.ve.internal.jfc.beaninfo;
/*
 *  $RCSfile: Point2DBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:11 $ 
 */


public class Point2DBeanInfo extends IvjBeanInfo {
/**
 * Gets the bean class.
 */
public Class getBeanClass() {
	return java.awt.geom.Point2D.class;
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



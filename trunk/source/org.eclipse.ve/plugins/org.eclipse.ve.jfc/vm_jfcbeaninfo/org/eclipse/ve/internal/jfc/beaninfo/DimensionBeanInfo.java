package org.eclipse.ve.internal.jfc.beaninfo;
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
 *  $RCSfile: DimensionBeanInfo.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:33 $ 
 */


public class DimensionBeanInfo extends IvjBeanInfo {
public Class getBeanClass() {
	return java.awt.Dimension.class;
}
/**
 * There should be no properties for dimension
 * Returning an empty array means the introspector will not use defualt
 * introspection
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	return new java.beans.PropertyDescriptor[0];
}
}



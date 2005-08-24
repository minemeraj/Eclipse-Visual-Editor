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

import java.awt.Insets;

/*
 *  $RCSfile: InsetsBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:10 $ 
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
				createFieldPropertyDescriptor("top", Insets.class.getField("top"), EMPTY_ARGS), //$NON-NLS-1$ //$NON-NLS-2$
				createFieldPropertyDescriptor("bottom", Insets.class.getField("bottom"), EMPTY_ARGS), //$NON-NLS-1$ //$NON-NLS-2$
				createFieldPropertyDescriptor("left", Insets.class.getField("left"), EMPTY_ARGS), //$NON-NLS-1$ //$NON-NLS-2$
				createFieldPropertyDescriptor("right", Insets.class.getField("right"), EMPTY_ARGS),				 //$NON-NLS-1$ //$NON-NLS-2$
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



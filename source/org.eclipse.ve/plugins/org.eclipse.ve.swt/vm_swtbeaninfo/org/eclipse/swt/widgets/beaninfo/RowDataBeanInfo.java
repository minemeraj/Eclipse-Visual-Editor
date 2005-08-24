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
package org.eclipse.swt.widgets.beaninfo;

import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.beaninfo.IvjBeanInfo;

/*
 *  $RCSfile: RowDataBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:52:53 $ 
 */


public class RowDataBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle resbundle = java.util.ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.rowdata");  //$NON-NLS-1$	
/**
 * Gets the bean class.
 */
public Class getBeanClass() {
	return RowData.class;
}
/**
 * @return java.beans.PropertyDescriptor[] for properties
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		return new java.beans.PropertyDescriptor[] {
				createFieldPropertyDescriptor("width", RowData.class.getField("width"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("rowdata.width"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("rowdata.width.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("height", RowData.class.getField("height"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("rowdata.height"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("rowdata.height.Desc"), //$NON-NLS-1$							
				}),				
		};
	} catch (SecurityException e) {
		handleException(e);
	} catch (NoSuchFieldException e) {
		handleException(e);
	}
	return null;
}
}

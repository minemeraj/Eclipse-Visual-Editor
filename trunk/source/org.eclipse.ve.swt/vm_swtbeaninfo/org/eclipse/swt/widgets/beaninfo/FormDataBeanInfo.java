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

import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.beaninfo.IvjBeanInfo;

/*
 *  $RCSfile: FormDataBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:52:54 $ 
 */


public class FormDataBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle resbundle = java.util.ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.formdata");  //$NON-NLS-1$	
/**
 * Gets the bean class.
 */
public Class getBeanClass() {
	return FormData.class;
}
/**
 * @return java.beans.PropertyDescriptor[] for height, width, bottom, top, left and right
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		return new java.beans.PropertyDescriptor[] {
				createFieldPropertyDescriptor("height", FormData.class.getField("height"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("formdata.height"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("formdata.height.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("width", FormData.class.getField("width"), new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("formdata.width"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("formdata.width.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("bottom", FormData.class.getField("bottom"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("formdata.bottom"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("formdata.bottom.Desc"), //$NON-NLS-1$
				}),
				createFieldPropertyDescriptor("top", FormData.class.getField("top"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("formdata.top"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("formdata.top.Desc"), //$NON-NLS-1$								
				}),
				createFieldPropertyDescriptor("left", FormData.class.getField("left"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("formdata.left"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("formdata.left.Desc"), //$NON-NLS-1$
				}),
				createFieldPropertyDescriptor("right", FormData.class.getField("right"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("formdata.right"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("formdata.right.Desc"), //$NON-NLS-1$								
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

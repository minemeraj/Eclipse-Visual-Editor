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

import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.beaninfo.IvjBeanInfo;

/*
 *  $RCSfile: FormLayoutBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:52:54 $ 
 */


public class FormLayoutBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle resbundle = java.util.ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.formlayout");  //$NON-NLS-1$	
/**
 * Gets the bean class.
 */
public Class getBeanClass() {
	return FormLayout.class;
}
/**
 * @return java.beans.PropertyDescriptor[] for marginWidth, marginHeight and spacing
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		return new java.beans.PropertyDescriptor[] {
				createFieldPropertyDescriptor("marginWidth", FormLayout.class.getField("marginWidth"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("formlayout.marginwidth"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("formlayout.marginwidth.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("marginHeight", FormLayout.class.getField("marginHeight"), new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("formlayout.marginheight"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("formlayout.marginheight.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("spacing", FormLayout.class.getField("spacing"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("formlayout.spacing"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("formlayout.spacing.Desc"), //$NON-NLS-1$							
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

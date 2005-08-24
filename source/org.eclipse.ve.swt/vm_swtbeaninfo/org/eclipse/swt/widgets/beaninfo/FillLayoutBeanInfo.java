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

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.beaninfo.IvjBeanInfo;

/*
 *  $RCSfile: FillLayoutBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:52:53 $ 
 */


public class FillLayoutBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle resbundle = java.util.ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.filllayout");  //$NON-NLS-1$	
/**
 * Gets the bean class.
 */
public Class getBeanClass() {
	return FillLayout.class;
}
/**
 * @return java.beans.PropertyDescriptor[] for the type, marginWidth, marginHeight and spacing 
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		return new java.beans.PropertyDescriptor[] {
				createFieldPropertyDescriptor("type", FillLayout.class.getField("type"), new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("filllayout.type"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("filllayout.type.Desc"), //$NON-NLS-1$						
				}),
				createFieldPropertyDescriptor("marginWidth", FillLayout.class.getField("marginWidth"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("filllayout.marginwidth"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("filllayout.marginwidth.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("marginHeight", FillLayout.class.getField("marginHeight"), new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("filllayout.marginheight"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("filllayout.marginheight.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("spacing", FillLayout.class.getField("spacing"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("filllayout.spacing"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("filllayout.spacing.Desc"), //$NON-NLS-1$							
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

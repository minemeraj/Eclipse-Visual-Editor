package org.eclipse.swt.widgets.beaninfo;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.beaninfo.IvjBeanInfo;

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
 *  $RCSfile: GridLayoutBeanInfo.java,v $
 *  $Revision: 1.2 $  $Date: 2005-03-01 19:33:55 $ 
 */


public class GridLayoutBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle resbundle = java.util.ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.gridlayout");  //$NON-NLS-1$	
/**
 * Gets the bean class.
 */
public Class getBeanClass() {
	return GridLayout.class;
}
/**
 * @return java.beans.PropertyDescriptor[] for numColumns, makeColumnsEqualWidth, marginWidth, marginHeight,
 * horizontalSpacing and verticalSpacing
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		return new java.beans.PropertyDescriptor[] {
				createFieldPropertyDescriptor("numColumns", GridLayout.class.getField("numColumns"), new Object[]{
						DISPLAYNAME, resbundle.getString("gridlayout.numcolumns"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("gridlayout.numColumns.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("makeColumnsEqualWidth", GridLayout.class.getField("makeColumnsEqualWidth"), new Object[]{
						DISPLAYNAME, resbundle.getString("gridlayout.makecolumnsequalwidth"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("gridlayout.makeColumnsEqualWidth.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("marginWidth", GridLayout.class.getField("marginWidth"), new Object[]{
						DISPLAYNAME, resbundle.getString("gridlayout.marginwidth"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("gridlayout.marginWidth.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("marginHeight", GridLayout.class.getField("marginHeight"), new Object[]{
						DISPLAYNAME, resbundle.getString("gridlayout.marginheight"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("gridlayout.marginHeight.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("horizontalSpacing", GridLayout.class.getField("horizontalSpacing"), new Object[]{
						DISPLAYNAME, resbundle.getString("gridlayout.horizontalspacing"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("gridlayout.horizontalSpacing.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("verticalSpacing", GridLayout.class.getField("verticalSpacing"), new Object[]{
						DISPLAYNAME, resbundle.getString("gridlayout.verticalspacing"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("gridlayout.verticalSpacing.Desc"), //$NON-NLS-1$							
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
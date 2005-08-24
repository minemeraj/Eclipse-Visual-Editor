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

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.beaninfo.IvjBeanInfo;

/*
 *  $RCSfile: GridDataBeanInfo.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:52:54 $ 
 */


public class GridDataBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle resbundle = java.util.ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.griddata");  //$NON-NLS-1$	
/**
 * Gets the bean class.
 */
public Class getBeanClass() {
	return GridData.class;
}
/**
 * @return java.beans.PropertyDescriptor[] for field properties
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		return new java.beans.PropertyDescriptor[] {
				createFieldPropertyDescriptor("grabExcessHorizontalSpace", GridData.class.getField("grabExcessHorizontalSpace"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("griddata.grabExcessHorizontalSpace"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("griddata.grabExcessHorizontalSpace.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("grabExcessVerticalSpace", GridData.class.getField("grabExcessVerticalSpace"), new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("griddata.grabExcessVerticalSpace"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("griddata.grabExcessVerticalSpace.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("heightHint", GridData.class.getField("heightHint"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("griddata.heightHint"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("griddata.heightHint.Desc"), //$NON-NLS-1$
				}),
				createFieldPropertyDescriptor("widthHint", GridData.class.getField("widthHint"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("griddata.widthHint"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("griddata.widthHint.Desc"), //$NON-NLS-1$
				}),
				createFieldPropertyDescriptor("horizontalAlignment", GridData.class.getField("horizontalAlignment"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("griddata.horizontalAlignment"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("griddata.horizontalAlignment.Desc"), //$NON-NLS-1$
				}),
				createFieldPropertyDescriptor("verticalAlignment", GridData.class.getField("verticalAlignment"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("griddata.verticalAlignment"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("griddata.verticalAlignment.Desc"), //$NON-NLS-1$
				}),				
				createFieldPropertyDescriptor("horizontalIndent", GridData.class.getField("horizontalIndent"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("griddata.horizontalIndent"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("griddata.horizontalIndent.Desc"), //$NON-NLS-1$
				}),
				createFieldPropertyDescriptor("horizontalSpan", GridData.class.getField("horizontalSpan"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("griddata.horizontalSpan"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("griddata.horizontalSpan.Desc"), //$NON-NLS-1$
				}),
				createFieldPropertyDescriptor("verticalSpan", GridData.class.getField("verticalSpan"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("griddata.verticalSpan"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("griddata.verticalSpan.Desc"), //$NON-NLS-1$
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

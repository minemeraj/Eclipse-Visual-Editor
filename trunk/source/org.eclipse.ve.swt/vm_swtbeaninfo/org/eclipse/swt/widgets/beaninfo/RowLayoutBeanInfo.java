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

import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.beaninfo.IvjBeanInfo;

/*
 *  $RCSfile: RowLayoutBeanInfo.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:52:54 $ 
 */


public class RowLayoutBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle resbundle = java.util.ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.rowlayout");  //$NON-NLS-1$	
/**
 * Gets the bean class.
 */
public Class getBeanClass() {
	return RowLayout.class;
}
/**
 * @return java.beans.PropertyDescriptor[] for properties
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		return new java.beans.PropertyDescriptor[] {
				createFieldPropertyDescriptor("type", RowLayout.class.getField("type"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("rowlayout.type"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("rowlayout.type.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("marginWidth", RowLayout.class.getField("marginWidth"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("rowlayout.marginwidth"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("rowlayout.marginWidth.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("marginHeight", RowLayout.class.getField("marginHeight"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("rowlayout.marginheight"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("rowlayout.marginHeight.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("marginLeft", RowLayout.class.getField("marginLeft"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("rowlayout.marginleft"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("rowlayout.marginLeft.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("marginRight", RowLayout.class.getField("marginRight"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("rowlayout.marginright"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("rowlayout.marginRight.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("marginTop", RowLayout.class.getField("marginTop"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("rowlayout.margintop"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("rowlayout.marginTop.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("marginBottom", RowLayout.class.getField("marginBottom"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("rowlayout.marginbottom"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("rowlayout.marginBottom.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("spacing", RowLayout.class.getField("spacing"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("rowlayout.spacing"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("rowlayout.spacing.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("wrap", RowLayout.class.getField("wrap"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("rowlayout.wrap"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("rowlayout.wrap.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("fill", RowLayout.class.getField("fill"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("rowlayout.fill"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("rowlayout.fill.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("justify", RowLayout.class.getField("justify"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("rowlayout.justify"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("rowlayout.justify.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("pack", RowLayout.class.getField("pack"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("rowlayout.pack"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("rowlayout.pack.Desc"), //$NON-NLS-1$							
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

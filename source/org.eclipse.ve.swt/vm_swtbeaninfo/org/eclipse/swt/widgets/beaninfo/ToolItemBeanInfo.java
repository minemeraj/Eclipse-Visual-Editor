/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;

import org.eclipse.swt.SWT;

public class ToolItemBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle ToolItemMessages = java.util.ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.toolitem");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return org.eclipse.swt.widgets.ToolItem.class;
}

public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
	    new Object[] [] {
			{ "style" , ToolItemMessages.getString("ToolItemBeanInfo.StyleBits.Style.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
			    ToolItemMessages.getString("ToolItemBeanInfo.StyleBits.Style.Value.Push") , "org.eclipse.swt.SWT.PUSH" , new Integer(SWT.PUSH) , //$NON-NLS-1$ //$NON-NLS-2$
				ToolItemMessages.getString("ToolItemBeanInfo.StyleBits.Style.Value.Check") , "org.eclipse.swt.SWT.CHECK" ,  new Integer(SWT.CHECK), 	 //$NON-NLS-1$ //$NON-NLS-2$
				ToolItemMessages.getString("ToolItemBeanInfo.StyleBits.Style.Value.Radio") , "org.eclipse.swt.SWT.RADIO" , new Integer(SWT.RADIO) , //$NON-NLS-1$ //$NON-NLS-2$
				ToolItemMessages.getString("ToolItemBeanInfo.StyleBits.Style.Value.Separator") , "org.eclipse.swt.SWT.SEPARATOR" , new Integer(SWT.SEPARATOR) , //$NON-NLS-1$ //$NON-NLS-2$
				ToolItemMessages.getString("ToolItemBeanInfo.StyleBits.Style.Value.Dropdown") , "org.eclipse.swt.SWT.DROP_DOWN" , new Integer(SWT.DROP_DOWN)	 //$NON-NLS-1$ //$NON-NLS-2$
			} }
		}
	);
	SweetHelper.mergeSuperclassStyleBits(descriptor);
	return descriptor;
}

/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
			// bounds
			super.createPropertyDescriptor(getBeanClass(),"bounds", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ToolItemMessages.getString("boundsDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ToolItemMessages.getString("boundsSD"), //$NON-NLS-1$
			}
			),
//			// control
//			super.createPropertyDescriptor(getBeanClass(),"control", new Object[] { //$NON-NLS-1$
//				DISPLAYNAME, ToolItemMessages.getString("controlDN"), //$NON-NLS-1$
//				SHORTDESCRIPTION, ToolItemMessages.getString("controlSD"), //$NON-NLS-1$
//			}
//			),
			// disabledImage
			super.createPropertyDescriptor(getBeanClass(),"disabledImage", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ToolItemMessages.getString("disabledImageDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ToolItemMessages.getString("disabledImageSD"), //$NON-NLS-1$
			}
			),
			// enabled
			super.createPropertyDescriptor(getBeanClass(),"enabled", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ToolItemMessages.getString("enabledDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ToolItemMessages.getString("enabledSD"), //$NON-NLS-1$
			}
			),
			// hotImage
			super.createPropertyDescriptor(getBeanClass(),"hotImage", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ToolItemMessages.getString("hotImageDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ToolItemMessages.getString("hotImageSD"), //$NON-NLS-1$
			}
			),
			// selection
			super.createPropertyDescriptor(getBeanClass(),"selection", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ToolItemMessages.getString("selectionDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ToolItemMessages.getString("selectionSD"), //$NON-NLS-1$
			}
			),
			// toolTipText
			super.createPropertyDescriptor(getBeanClass(),"toolTipText", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ToolItemMessages.getString("toolTipTextDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ToolItemMessages.getString("toolTipTextSD"), //$NON-NLS-1$
			}
			),
			// width
			super.createPropertyDescriptor(getBeanClass(),"width", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ToolItemMessages.getString("widthDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ToolItemMessages.getString("widthSD"), //$NON-NLS-1$
			}
			),
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}

}

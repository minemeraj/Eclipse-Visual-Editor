/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ButtonBeanInfo.java,v $
 *  $Revision: 1.7 $  $Date: 2004-06-25 18:40:10 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;

import org.eclipse.swt.SWT;
 
/**
 * 
 * @since 1.0.0
 */
public class ButtonBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Button.class;
	}
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
	    new Object[] [] {
			{ "style" , ButtonMessages.getString("ButtonBeanInfo.StyleBits.Style.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
			    ButtonMessages.getString("ButtonBeanInfo.StyleBits.Style.Value.Push") , "org.eclipse.swt.SWT.PUSH" , new Integer(SWT.PUSH) , //$NON-NLS-1$ //$NON-NLS-2$
				ButtonMessages.getString("ButtonBeanInfo.StyleBits.Style.Value.Check") , "org.eclipse.swt.SWT.CHECK" ,  new Integer(SWT.CHECK), 				 //$NON-NLS-1$ //$NON-NLS-2$
				ButtonMessages.getString("ButtonBeanInfo.StyleBits.Style.Value.Radio") , "org.eclipse.swt.SWT.RADIO" , new Integer(SWT.RADIO) , //$NON-NLS-1$ //$NON-NLS-2$
				ButtonMessages.getString("ButtonBeanInfo.StyleBits.Style.Value.Arrow") , "org.eclipse.swt.SWT.ARROW" , new Integer(SWT.ARROW) , //$NON-NLS-1$ //$NON-NLS-2$
				ButtonMessages.getString("ButtonBeanInfo.StyleBits.Style.Value.Toggle") , "org.eclipse.swt.SWT.TOGGLE" , new Integer(SWT.TOGGLE)				 //$NON-NLS-1$ //$NON-NLS-2$
			} } ,
			{ "textAlignment" , ButtonMessages.getString("ButtonBeanInfo.StyleBits.TextAlignment.Name"), Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				ButtonMessages.getString("ButtonBeanInfo.StyleBits.TextAlignment.Value.Left") , "org.eclipse.swt.SWT.LEFT" , new Integer(SWT.LEFT) ,					 //$NON-NLS-1$ //$NON-NLS-2$
				ButtonMessages.getString("ButtonBeanInfo.StyleBits.TextAlignment.Value.Right") , "org.eclipse.swt.SWT.RIGHT" , new Integer(SWT.RIGHT) , //$NON-NLS-1$ //$NON-NLS-2$
				ButtonMessages.getString("ButtonBeanInfo.StyleBits.TextAlignment.Value.Center") , "org.eclipse.swt.SWT.CENTER" , new Integer(SWT.CENTER)				 //$NON-NLS-1$ //$NON-NLS-2$
			} },
			{ "arrowStyle" , ButtonMessages.getString("ButtonBeanInfo.StyleBits.ArrowStyle.Name"), Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				ButtonMessages.getString("ButtonBeanInfo.StyleBits.ArrowStyle.Value.Up") , "org.eclipse.swt.SWT.UP" , new Integer(SWT.UP) ,					 //$NON-NLS-1$ //$NON-NLS-2$
				ButtonMessages.getString("ButtonBeanInfo.StyleBits.ArrowStyle.Value.Down") , "org.eclipse.swt.SWT.DOWN" , new Integer(SWT.DOWN) , //$NON-NLS-1$ //$NON-NLS-2$
				ButtonMessages.getString("ButtonBeanInfo.StyleBits.ArrowStyle.Value.Left") , "org.eclipse.swt.SWT.LEFT" , new Integer(SWT.LEFT) , //$NON-NLS-1$ //$NON-NLS-2$
				ButtonMessages.getString("ButtonBeanInfo.StyleBits.ArrowStyle.Value.Right") , "org.eclipse.swt.SWT.RIGHT" , new Integer(SWT.RIGHT) 				 //$NON-NLS-1$ //$NON-NLS-2$
			} }
		}
	);
	SweetHelper.mergeSuperclassStyleBits(descriptor);
	return descriptor;
}
/* (non-Javadoc)
 * @see java.beans.BeanInfo#getEventSetDescriptors()
 */
public EventSetDescriptor[] getEventSetDescriptors() {
	return new EventSetDescriptor[] {
			SelectionListenerEventSet.getEventSetDescriptor(getBeanClass())
	};
}

/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
			// alignment
			super.createPropertyDescriptor(getBeanClass(),"alignment", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ButtonMessages.getString("alignmentDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ButtonMessages.getString("alignmentSD"), //$NON-NLS-1$
			}
			),
			// image
			super.createPropertyDescriptor(getBeanClass(),"image", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ButtonMessages.getString("imageDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ButtonMessages.getString("imageSD"), //$NON-NLS-1$
			}
			),
			// selection
			super.createPropertyDescriptor(getBeanClass(),"selection", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ButtonMessages.getString("selectionDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ButtonMessages.getString("selectionSD"), //$NON-NLS-1$
			}
			),
			// text
			super.createPropertyDescriptor(getBeanClass(),"text", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ButtonMessages.getString("textDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ButtonMessages.getString("textSD"), //$NON-NLS-1$
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

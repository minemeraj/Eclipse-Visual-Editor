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
/*
 *  $RCSfile: DecorationsBeanInfo.java,v $
 *  $Revision: 1.10 $  $Date: 2005-11-17 22:45:28 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Scrollable;
 
/**
 * 
 * @since 1.0.0
 */
public class DecorationsBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Decorations.class;
	}
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
	    new Object[] [] {
			{ "trim" , DecorationsMessages.getString("DecorationsBeanInfo.StyleBits.Trim.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
			    DecorationsMessages.getString("DecorationsBeanInfo.StyleBits.Trim.Value.ShellTrim") , "org.eclipse.swt.SWT.SHELL_TRIM" , new Integer(SWT.SHELL_TRIM) ,				 //$NON-NLS-1$ //$NON-NLS-2$
			    DecorationsMessages.getString("DecorationsBeanInfo.StyleBits.Trim.Value.DialogTrim") , "org.eclipse.swt.SWT.DIALOG_TRIM" , new Integer(SWT.DIALOG_TRIM) , //$NON-NLS-1$ //$NON-NLS-2$
				DecorationsMessages.getString("DecorationsBeanInfo.StyleBits.Trim.Value.NoTrim") , "org.eclipse.swt.SWT.NO_TRIM" , new Integer(SWT.NO_TRIM)								 //$NON-NLS-1$ //$NON-NLS-2$
			} } ,
			{ "on_top" , DecorationsMessages.getString("DecorationsBeanInfo.StyleBits.OnTop.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				DecorationsMessages.getString("DecorationsBeanInfo.StyleBits.OnTop.Value.OnTop") , "org.eclipse.swt.SWT.ON_TOP" , new Integer(SWT.ON_TOP)				 //$NON-NLS-1$ //$NON-NLS-2$
			} } ,			
			{ "close" , DecorationsMessages.getString("DecorationsBeanInfo.StyleBits.Close.Name") , Boolean.TRUE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
			    DecorationsMessages.getString("DecorationsBeanInfo.StyleBits.Close.Value.Close") , "org.eclipse.swt.SWT.CLOSE" , new Integer(SWT.CLOSE)				 //$NON-NLS-1$ //$NON-NLS-2$
			} } ,
			{ "min" , DecorationsMessages.getString("DecorationsBeanInfo.StyleBits.Min.Name") , Boolean.TRUE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				DecorationsMessages.getString("DecorationsBeanInfo.StyleBits.Min.Value.Min") , "org.eclipse.swt.SWT.MIN" , new Integer(SWT.MIN)				 //$NON-NLS-1$ //$NON-NLS-2$
			} } ,
			{ "max" , DecorationsMessages.getString("DecorationsBeanInfo.StyleBits.Max.Name") , Boolean.TRUE, new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				DecorationsMessages.getString("DecorationsBeanInfo.StyleBits.Max.Value.Max") , "org.eclipse.swt.SWT.MAX" , new Integer(SWT.MAX)				 //$NON-NLS-1$ //$NON-NLS-2$
			} } ,
			{ "resize" , DecorationsMessages.getString("DecorationsBeanInfo.StyleBits.Resize.Name") , Boolean.TRUE, new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				DecorationsMessages.getString("DecorationsBeanInfo.StyleBits.Resize.Value.Resize") , "org.eclipse.swt.SWT.RESIZE" , new Integer(SWT.RESIZE)				 //$NON-NLS-1$ //$NON-NLS-2$
			} } ,
			{ "title" , DecorationsMessages.getString("DecorationsBeanInfo.StyleBits.Title.Name") , Boolean.TRUE, new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				DecorationsMessages.getString("DecorationsBeanInfo.StyleBits.Title.Value.Title") , "org.eclipse.swt.SWT.TITLE" , new Integer(SWT.TITLE)				 //$NON-NLS-1$ //$NON-NLS-2$
			} } 
		}
	);
	// Do not inherit from Composite otherwise we will pick up things like noMergePaintEvents and noBackground
	SweetHelper.mergeStyleBits(descriptor, new Class[] {Scrollable.class,});
	return descriptor;
}

/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
			// defaultButton
			super.createPropertyDescriptor(getBeanClass(),"defaultButton", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, DecorationsMessages.getString("defaultButtonDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, DecorationsMessages.getString("defaultButtonSD"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
			}
			),
			// image
			super.createPropertyDescriptor(getBeanClass(),"image", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, DecorationsMessages.getString("imageDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, DecorationsMessages.getString("imageSD"), //$NON-NLS-1$
			}
			),
			// images
			super.createPropertyDescriptor(getBeanClass(),"images", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, DecorationsMessages.getString("imagesDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, DecorationsMessages.getString("imagesSD"), //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,
			}
			),
			// maximized
			super.createPropertyDescriptor(getBeanClass(),"maximized", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, DecorationsMessages.getString("maximizedDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, DecorationsMessages.getString("maximizedSD"), //$NON-NLS-1$
			}
			),
			// menuBar
			super.createPropertyDescriptor(getBeanClass(),"menuBar", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, DecorationsMessages.getString("menuBarDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, DecorationsMessages.getString("menuBarSD"), //$NON-NLS-1$
				HIDDEN, Boolean.TRUE,		// show hidden - can't set from PS, only palette
			}
			),
			// minimized
			super.createPropertyDescriptor(getBeanClass(),"minimized", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, DecorationsMessages.getString("minimizedDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, DecorationsMessages.getString("minimizedSD"), //$NON-NLS-1$
			}
			),
			// text
			super.createPropertyDescriptor(getBeanClass(),"text", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, DecorationsMessages.getString("textDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, DecorationsMessages.getString("textSD"), //$NON-NLS-1$
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

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
 *  $RCSfile: ShellBeanInfo.java,v $
 *  $Revision: 1.9 $  $Date: 2005-08-24 23:52:53 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;

import org.eclipse.swt.SWT;
 

/**
 * @since 1.0.0
 *
 */
public class ShellBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Shell.class;
	}

	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
		descriptor.setValue(
			SweetHelper.STYLE_BITS_ID,
		    new Object[] [] {
				{ "modality" , ShellMessages.getString("ShellBeanInfo.StyleBits.Modality.Name") , Boolean.FALSE, new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				    ShellMessages.getString("ShellBeanInfo.StyleBits.Modality.Value.ApplicationModal") , "org.eclipse.swt.SWT.APPLICATION_MODAL" , new Integer(SWT.APPLICATION_MODAL) ,				 //$NON-NLS-1$ //$NON-NLS-2$
				    ShellMessages.getString("ShellBeanInfo.StyleBits.Modality.Value.Modeless") , "org.eclipse.swt.SWT.MODELESS" , new Integer(SWT.MODELESS) , //$NON-NLS-1$ //$NON-NLS-2$
					ShellMessages.getString("ShellBeanInfo.StyleBits.Modality.Value.PrimaryModal") , "org.eclipse.swt.SWT.PRIMARY_MODAL" , new Integer(SWT.PRIMARY_MODAL), //$NON-NLS-1$ //$NON-NLS-2$
					ShellMessages.getString("ShellBeanInfo.StyleBits.Modality.Value.SystemModal") , "org.eclipse.swt.SWT.SYSTEM_MODAL" , new Integer(SWT.SYSTEM_MODAL) //$NON-NLS-1$ //$NON-NLS-2$
				} }
			}
		);
		// Do not inherit from Composite otherwise we will pick up things like noMergePaintEvents and noBackground
		SweetHelper.mergeSuperclassStyleBits(descriptor);
		return descriptor;
	}
	
	
	/* (non-Javadoc)
	 * @see java.beans.BeanInfo#getEventSetDescriptors()
	 */
	public EventSetDescriptor[] getEventSetDescriptors() {
		return new EventSetDescriptor[] {
				ShellListenerEventSet.getEventSetDescriptor(getBeanClass())
		};
	}
	
	/**
	 * Return the property descriptors for this bean.
	 * @return java.beans.PropertyDescriptor[]
	 */
	public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor aDescriptorList[] = {
				// imeInputMode
				super.createPropertyDescriptor(getBeanClass(),"imeInputMode", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, ShellMessages.getString("imeInputModeDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, ShellMessages.getString("imeInputModeSD"), //$NON-NLS-1$
					EXPERT, Boolean.TRUE,
				}
				),
				// region
				super.createPropertyDescriptor(getBeanClass(),"region", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, ShellMessages.getString("regionDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, ShellMessages.getString("regionSD"), //$NON-NLS-1$
					EXPERT, Boolean.TRUE,
				}
				),
				// shells
				super.createPropertyDescriptor(getBeanClass(),"shells", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, ShellMessages.getString("shellsDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, ShellMessages.getString("shellsSD"), //$NON-NLS-1$
					DESIGNTIMEPROPERTY, Boolean.FALSE
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

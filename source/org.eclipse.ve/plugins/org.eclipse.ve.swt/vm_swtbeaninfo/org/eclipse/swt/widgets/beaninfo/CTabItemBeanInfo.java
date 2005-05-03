package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;

import org.eclipse.swt.SWT;


public class CTabItemBeanInfo extends IvjBeanInfo {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.custom.CTabItem.class;
	}
	
	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
		descriptor.setValue(
			SweetHelper.STYLE_BITS_ID,
		    new Object[] [] {
				{ "close" , CTabItemMessages.getString("CTabItemBeanInfo.StyleBits.Close.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				    CTabItemMessages.getString("CTabItemBeanInfo.StyleBits.Close.Value.CLOSE") , "org.eclipse.swt.SWT.CLOSE" , new Integer(SWT.CLOSE) , //$NON-NLS-1$ //$NON-NLS-2$
				} }
			}
		);
		return descriptor;
	}	

	/**
	 * Return the property descriptors for this bean.
	 * 
	 * @return java.beans.PropertyDescriptor[]
	 */
	public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor aDescriptorList[] = {
				// font
				super.createPropertyDescriptor(getBeanClass(),"font", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, CTabItemMessages.getString("fontDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, CTabItemMessages.getString("fontSD"), //$NON-NLS-1$
				}
				),
				// toolTipText
				super.createPropertyDescriptor(getBeanClass(),"toolTipText", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, CTabItemMessages.getString("toolTipTextDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, CTabItemMessages.getString("toolTipTextSD"), //$NON-NLS-1$
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

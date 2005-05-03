package org.eclipse.swt.widgets.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;

import org.eclipse.swt.SWT;


public class ViewFormBeanInfo extends IvjBeanInfo {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.custom.ViewForm.class;
	}
	
	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
		descriptor.setValue(
			SweetHelper.STYLE_BITS_ID,
		    new Object[] [] {
				{ "border" , ViewFormMessages.getString("ViewFormBeanInfo.StyleBits.Border.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				    ViewFormMessages.getString("ViewFormBeanInfo.StyleBits.Border.Value.Border") , "org.eclipse.swt.SWT.BORDER" , new Integer(SWT.BORDER) , //$NON-NLS-1$ //$NON-NLS-2$
				} },
				{ "flat" , ViewFormMessages.getString("ViewFormBeanInfo.StyleBits.Flat.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				    ViewFormMessages.getString("ViewFormBeanInfo.StyleBits.Flat.Value.Flat") , "org.eclipse.swt.SWT.FLAT" , new Integer(SWT.FLAT) , //$NON-NLS-1$ //$NON-NLS-2$
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
				// topCenterSeparate
				super.createPropertyDescriptor(getBeanClass(),"topCenterSeparate", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, ViewFormMessages.getString("topCenterSeparateDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, ViewFormMessages.getString("topCenterSeparateSD"), //$NON-NLS-1$
				}
				)
			};
			return aDescriptorList;
		} catch (Throwable exception) {
			handleException(exception);
		};
		return null;
	}
	
	protected PropertyDescriptor[] overridePropertyDescriptors(PropertyDescriptor[] pds) {
		PropertyDescriptor[] newPDs = (PropertyDescriptor[]) pds.clone();

		replacePropertyDescriptor(newPDs, "layout", null, new Object[] { //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,});

		return newPDs;
	}
}

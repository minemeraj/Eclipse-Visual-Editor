package org.eclipse.swt.widgets.beaninfo;

import java.beans.PropertyDescriptor;

public class CBannerBeanInfo extends IvjBeanInfo {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.custom.CBanner.class;
	}

	/**
	 * Return the property descriptors for this bean.
	 * 
	 * @return java.beans.PropertyDescriptor[]
	 */
	public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor aDescriptorList[] = {
				// simple
				super.createPropertyDescriptor(getBeanClass(), "simple", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, CBannerMessages.getString("simpleDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, CBannerMessages.getString("simpleSD"), //$NON-NLS-1$
				}),
				// right minimum size
				super.createPropertyDescriptor(getBeanClass(), "rightMinimumSize", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, CBannerMessages.getString("rightMinimumSizeDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, CBannerMessages.getString("rightMinimumSizeSD"), //$NON-NLS-1$
				}),
				// right width
				super.createPropertyDescriptor(getBeanClass(), "rightWidth", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, CBannerMessages.getString("rightWidthDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, CBannerMessages.getString("rightWidthSD"), //$NON-NLS-1$
				}),
			};
			return aDescriptorList;
		} catch (Throwable exception) {
			handleException(exception);
		}
		;
		return null;
	}

	protected PropertyDescriptor[] overridePropertyDescriptors(PropertyDescriptor[] pds) {
		PropertyDescriptor[] newPDs = (PropertyDescriptor[]) pds.clone();

		replacePropertyDescriptor(newPDs, "layout", null, new Object[] { //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,});

		return newPDs;
	}

}

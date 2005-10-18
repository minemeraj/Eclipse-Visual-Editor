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
 *  $RCSfile: SashFormBeanInfo.java,v $
 *  $Revision: 1.8 $  $Date: 2005-10-18 15:32:18 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.PropertyDescriptor;

import org.eclipse.jem.beaninfo.common.IBaseBeanInfoConstants;

/**
 * 
 * @since 1.0.0
 */
public class SashFormBeanInfo extends IvjBeanInfo {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.custom.SashForm.class;
	}

	/**
	 * Return the property descriptors for this bean.
	 * 
	 * @return java.beans.PropertyDescriptor[]
	 */
	public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor aDescriptorList[] = {
					super.createPropertyDescriptor(getBeanClass(), "orientation", new Object[] { //$NON-NLS-1$
							DISPLAYNAME, SashFormMessages.getString("orientationDN"), //$NON-NLS-1$
									SHORTDESCRIPTION, SashFormMessages.getString("orientationSD"), //$NON-NLS-1$
									IBaseBeanInfoConstants.ENUMERATIONVALUES,
									new Object[] { SashFormMessages.getString("orientation.vertical"), new Integer(org.eclipse.swt.SWT.VERTICAL), //$NON-NLS-1$
											"org.eclipse.swt.SWT.VERTICAL", //$NON-NLS-1$
											SashFormMessages.getString("orientation.horizontal"), new Integer(org.eclipse.swt.SWT.HORIZONTAL), //$NON-NLS-1$
											"org.eclipse.swt.SWT.HORIZONTAL", //$NON-NLS-1$
									},}),
					super.createPropertyDescriptor(getBeanClass(), "weights", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, SashFormMessages.getString("weightsDN"),  //$NON-NLS-1$
						SHORTDESCRIPTION, SashFormMessages.getString("weightsSD"),						 //$NON-NLS-1$
						DESIGNTIMEPROPERTY, Boolean.FALSE,	// Because we don't have a good property editor yet.
									})};
			// aDescriptorList[0].setHidden(true);
			return aDescriptorList;
		} catch (Throwable exception) {
			handleException(exception);
		}
		;
		return null;
	}

	protected PropertyDescriptor[] overridePropertyDescriptors(
			PropertyDescriptor[] pds) {
		PropertyDescriptor[] newPDs = (PropertyDescriptor[]) pds.clone();

		replacePropertyDescriptor(newPDs, "layout", null, new Object[] { //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE, });
		replacePropertyDescriptor(newPDs, "border", null, new Object[] { //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE, });
		replacePropertyDescriptor(newPDs, "verticalScroll", null, new Object[] { //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE, });
		replacePropertyDescriptor(newPDs, "horizontalScroll", null, //$NON-NLS-1$
				new Object[] { DESIGNTIMEPROPERTY, Boolean.FALSE, });

		return newPDs;
	}

}

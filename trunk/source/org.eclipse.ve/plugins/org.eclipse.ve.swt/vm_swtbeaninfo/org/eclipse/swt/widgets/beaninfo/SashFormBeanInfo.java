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
 *  $RCSfile: SashFormBeanInfo.java,v $
 *  $Revision: 1.1 $  $Date: 2004-07-28 16:29:39 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.PropertyDescriptor;

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
			PropertyDescriptor aDescriptorList[] = { super
					.createPropertyDescriptor(
							getBeanClass(),
							"orientation", new Object[] { //$NON-NLS-1$
									DISPLAYNAME,
									SashFormMessages.getString("orientationDN"), //$NON-NLS-1$
									SHORTDESCRIPTION,
									SashFormMessages.getString("orientationSD"), //$NON-NLS-1$
									ENUMERATIONVALUES,
									new Object[] {
											SashFormMessages
													.getString("orientation.vertical"), new Integer(org.eclipse.swt.SWT.VERTICAL), //$NON-NLS-1$
											"org.eclipse.swt.SWT.VERTICAL", //$NON-NLS-1$
											SashFormMessages
													.getString("orientation.horizontal"), new Integer(org.eclipse.swt.SWT.HORIZONTAL), //$NON-NLS-1$
											"org.eclipse.swt.SWT.HORIZONTAL", //$NON-NLS-1$
									}, }), };
			//aDescriptorList[0].setHidden(true);
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

		replacePropertyDescriptor(newPDs, "layout", null, new Object[] {
				DESIGNTIMEPROPERTY, Boolean.FALSE, });
		replacePropertyDescriptor(newPDs, "border", null, new Object[] {
				DESIGNTIMEPROPERTY, Boolean.FALSE, });
		replacePropertyDescriptor(newPDs, "verticalScroll", null, new Object[] {
				DESIGNTIMEPROPERTY, Boolean.FALSE, });
		replacePropertyDescriptor(newPDs, "horizontalScroll", null,
				new Object[] { DESIGNTIMEPROPERTY, Boolean.FALSE, });

		return newPDs;
	}

}

/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ScrolledCompositeBeanInfo.java,v $
 *  $Revision: 1.2 $  $Date: 2005-05-11 21:33:55 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.PropertyDescriptor;


public class ScrolledCompositeBeanInfo extends IvjBeanInfo {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.custom.ScrolledComposite.class;
	}
	
	/**
	 * Return the property descriptors for this bean.
	 * 
	 * @return java.beans.PropertyDescriptor[]
	 */
	public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor aDescriptorList[] = {
				// alwaysShowScrollBars
				super.createPropertyDescriptor(getBeanClass(),"alwaysShowScrollBars", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, ScrolledCompositeMessages.getString("alwaysShowScrollBarsDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, ScrolledCompositeMessages.getString("alwaysShowScrollBarsSD"), //$NON-NLS-1$
				}
				),
				// content
				super.createPropertyDescriptor(getBeanClass(),"content", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, ScrolledCompositeMessages.getString("contentDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, ScrolledCompositeMessages.getString("contentSD"), //$NON-NLS-1$
					DESIGNTIMEPROPERTY, Boolean.FALSE,
				}
				),
				// expandHorizontal
				super.createPropertyDescriptor(getBeanClass(),"expandHorizontal", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, ScrolledCompositeMessages.getString("expandHorizontalDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, ScrolledCompositeMessages.getString("expandHorizontalSD"), //$NON-NLS-1$
				}
				),
				// expandVertical
				super.createPropertyDescriptor(getBeanClass(),"expandVertical", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, ScrolledCompositeMessages.getString("expandVerticalDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, ScrolledCompositeMessages.getString("expandVerticalSD"), //$NON-NLS-1$
				}
				),
				// minHeight
				super.createPropertyDescriptor(getBeanClass(),"minHeight", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, ScrolledCompositeMessages.getString("minHeightDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, ScrolledCompositeMessages.getString("minHeightSD"), //$NON-NLS-1$
						DESIGNTIMEPROPERTY, Boolean.FALSE, //$NON_NLS-1$
				}
				),
				// minWidth
				super.createPropertyDescriptor(getBeanClass(),"minWidth", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, ScrolledCompositeMessages.getString("minWidthDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, ScrolledCompositeMessages.getString("minWidthSD"), //$NON-NLS-1$
						DESIGNTIMEPROPERTY, Boolean.FALSE, //$NON_NLS-1$
				}
				),
				// minSize
				super.createPropertyDescriptor(getBeanClass(),"minSize", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, ScrolledCompositeMessages.getString("minSizeDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, ScrolledCompositeMessages.getString("minSizeSD"), //$NON-NLS-1$
				}
				),
				// origin
				super.createPropertyDescriptor(getBeanClass(),"origin", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, ScrolledCompositeMessages.getString("originDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, ScrolledCompositeMessages.getString("originSD"), //$NON-NLS-1$
						DESIGNTIMEPROPERTY, Boolean.FALSE, //$NON_NLS-1$
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

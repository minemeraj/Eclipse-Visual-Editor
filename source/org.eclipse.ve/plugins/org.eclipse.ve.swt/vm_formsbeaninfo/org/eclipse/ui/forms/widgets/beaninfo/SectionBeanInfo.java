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
 *  $RCSfile: SectionBeanInfo.java,v $
 *  $Revision: 1.1 $  $Date: 2006-02-03 15:22:02 $ 
 */
package org.eclipse.ui.forms.widgets.beaninfo;

import java.beans.PropertyDescriptor;

import org.eclipse.swt.widgets.beaninfo.IvjBeanInfo;
import org.eclipse.ui.forms.widgets.Section;

import org.eclipse.jem.beaninfo.common.IBaseBeanInfoConstants;

public class SectionBeanInfo extends IvjBeanInfo {

	public Class getBeanClass() {
		return Section.class;
	}
	
	public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor aDescriptorList[] = {
				// style bit
				super.createPropertyDescriptor(getBeanClass(),"style", new Object[] { //$NON-NLS-1$
					IBaseBeanInfoConstants.FACTORY_CREATION , new Object[] { 
							new Object[] { "org.eclipse.ui.forms.widgets.FormToolkit" , "createSection" , new Integer(1) ,  //$NON-NLS-1$ //$NON-NLS-2$
									new String[] { "org.eclipse.swt.widgets.Composite" , "int"} } }				 //$NON-NLS-1$ //$NON-NLS-2$
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
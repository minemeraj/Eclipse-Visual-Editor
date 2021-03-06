/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: FormBeanInfo.java,v $
 *  $Revision: 1.8 $  $Date: 2006-05-17 20:15:54 $ 
 */
package org.eclipse.ui.forms.widgets.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.beaninfo.IvjBeanInfo;
import org.eclipse.ui.forms.widgets.Form;

import org.eclipse.jem.beaninfo.common.IBaseBeanInfoConstants;
import org.eclipse.jem.beaninfo.vm.BaseBeanInfo;

import org.eclipse.ve.swt.common.SWTBeanInfoConstants;
 
/**
 * 
 * @since 1.0.0
 */
public class FormBeanInfo extends IvjBeanInfo {

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return Form.class;
	}
	
	public BeanDescriptor getBeanDescriptor() {
		return createBeanDescriptor(getBeanClass(), new Object[] {
			BaseBeanInfo.REQUIRED_IMPLICIT_PROPERTIES, "body",		//$NON-NLS-1$
			SWTBeanInfoConstants.DEFAULT_LAYOUT, Boolean.FALSE
		}); 
	}
	
/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
			// text
			super.createPropertyDescriptor(getBeanClass(),"text", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, FormMessages.getString("textDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, FormMessages.getString("textSD"), //$NON-NLS-1$
			}
			),					
			// background image
			super.createPropertyDescriptor(getBeanClass(),"backgroundImage", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, FormMessages.getString("backgroundImageDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, FormMessages.getString("backgroundImageSD"), //$NON-NLS-1$
			}
			),		
			// 	background image tiled
			super.createPropertyDescriptor(getBeanClass(),"backgroundImageTiled", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, FormMessages.getString("backgroundImageTiledDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, FormMessages.getString("backgroundImageTiledSD"), //$NON-NLS-1$
			}
			),
			// background image clipped
			super.createPropertyDescriptor(getBeanClass(),"backgroundImageClipped", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, FormMessages.getString("backgroundImageClippedDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, FormMessages.getString("backgroundImageClippedSD"), //$NON-NLS-1$
			}
			),			
			// 	background image alignment
			super.createPropertyDescriptor(getBeanClass(),"backgroundImageAlignment", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, FormMessages.getString("backgroundImageAlignmentDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, FormMessages.getString("backgroundImageAlignmentSD"), //$NON-NLS-1$
		      	IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
					FormMessages.getString("LEFTnumDN"), new Integer(SWT.LEFT), //$NON-NLS-1$
      				"org.eclipse.swt.SWT.LEFT",//$NON-NLS-1$
      				FormMessages.getString("RIGHTnumDN"), new Integer(SWT.RIGHT), //$NON-NLS-1$
      				"org.eclipse.swt.SWT.RIGHT"//$NON-NLS-1$
    			}				
			}
			),
			// body
			super.createPropertyDescriptor(getBeanClass(),"body", new Object[] { //$NON-NLS-1$
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

/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets.beaninfo;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.beaninfo.IvjBeanInfo;

/*
 *  $RCSfile: FormAttachmentBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:52:53 $ 
 */


public class FormAttachmentBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle resbundle = java.util.ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.formattachment");  //$NON-NLS-1$	
/**
 * Gets the bean class.
 */
public Class getBeanClass() {
	return FormAttachment.class;
}
/**
 * @return java.beans.PropertyDescriptor[] for alignment, denominator, numerator, offset
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		return new java.beans.PropertyDescriptor[] {
				createFieldPropertyDescriptor("alignment", FormAttachment.class.getField("alignment"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("formattachment.alignment"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("formattachment.alignment.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("denominator", FormAttachment.class.getField("denominator"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("formattachment.denominator"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("formattachment.denominator.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("numerator", FormAttachment.class.getField("numerator"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("formattachment.numerator"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("formattachment.numerator.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("offset", FormAttachment.class.getField("offset"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resbundle.getString("formattachment.offset"), //$NON-NLS-1$
						SHORTDESCRIPTION, resbundle.getString("formattachment.offset.Desc"), //$NON-NLS-1$							
				}),		
		};
	} catch (SecurityException e) {
		handleException(e);
	} catch (NoSuchFieldException e) {
		handleException(e);
	}
	return null;
}
}

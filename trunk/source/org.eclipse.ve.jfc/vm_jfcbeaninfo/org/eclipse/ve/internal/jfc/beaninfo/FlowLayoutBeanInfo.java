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
package org.eclipse.ve.internal.jfc.beaninfo;
/*
 *  $RCSfile: FlowLayoutBeanInfo.java,v $
 *  $Revision: 1.4 $  $Date: 2005-10-06 15:18:44 $ 
 */

import java.awt.FlowLayout;

import org.eclipse.jem.beaninfo.vm.IBaseBeanInfoConstants;

public class FlowLayoutBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle FlowLayoutMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.flowlayout");  //$NON-NLS-1$

public Class getBeanClass() {
	return FlowLayout.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	return new java.beans.BeanDescriptor(FlowLayout.class);
}
/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	return( new java.beans.EventSetDescriptor[0]);
}
/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {

	try {
		java.beans.PropertyDescriptor aDescriptorList[] = {
			// hgap
	   	super.createPropertyDescriptor(getBeanClass(),"Hgap", new Object[] {//$NON-NLS-1$
		   	DISPLAYNAME, FlowLayoutMessages.getString("hgap.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, FlowLayoutMessages.getString("hgap.Desc"), //$NON-NLS-1$
	    		}
	    	),				
			// vgap
	   	super.createPropertyDescriptor(getBeanClass(),"Vgap", new Object[] {//$NON-NLS-1$
		   	DISPLAYNAME, FlowLayoutMessages.getString("vgap.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, FlowLayoutMessages.getString("vgap.Desc"), //$NON-NLS-1$
	    		}
			),
		super.createPropertyDescriptor(getBeanClass(),"alignment" ,new Object[] { //$NON-NLS-1$
			DISPLAYNAME , FlowLayoutMessages.getString("alignment.Name"), //$NON-NLS-1$
			SHORTDESCRIPTION, FlowLayoutMessages.getString("alignment.Desc"), //$NON-NLS-1$
			IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      		FlowLayoutMessages.getString("LEFT"), new Integer(FlowLayout.LEFT), "java.awt.FlowLayout.LEFT", //$NON-NLS-1$ //$NON-NLS-2$
	           	FlowLayoutMessages.getString("CENTER"), new Integer(FlowLayout.CENTER), "java.awt.FlowLayout.CENTER", //$NON-NLS-1$ //$NON-NLS-2$
	          	FlowLayoutMessages.getString("RIGHT"),new Integer(FlowLayout.RIGHT), "java.awt.FlowLayout.RIGHT", //$NON-NLS-1$ //$NON-NLS-2$
	    		}
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

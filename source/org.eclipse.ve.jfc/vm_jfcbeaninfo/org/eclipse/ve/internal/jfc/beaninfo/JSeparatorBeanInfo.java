package org.eclipse.ve.internal.jfc.beaninfo;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JSeparatorBeanInfo.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:33 $ 
 */

import java.beans.*;

public class JSeparatorBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JSeparatorMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jseparator");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JSeparator.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JSeparatorMessages.getString("JSeparator.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JSeparatorMessages.getString("JSeparator.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/msepar32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/msepar16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
	} catch (Throwable exception) {
		handleException(exception);
	};
	return aDescriptor;
}
/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	return ( new EventSetDescriptor[0]);
}
	/**
	 * @return an icon of the specified kind for JButton
	 */
	public java.awt.Image getIcon(int kind) {
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("msepar32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("msepar16.gif"); //$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
		  	// getAccessibleContext()
			super.createMethodDescriptor(getBeanClass(),"getAccessibleContext",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSeparatorMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessible context",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	)
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}
/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
	    	// orientation
			super.createPropertyDescriptor(getBeanClass(),"orientation", new Object[] { //$NON-NLS-1$
	      			DISPLAYNAME, JSeparatorMessages.getString("orientation.Name"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, JSeparatorMessages.getString("orientation.Desc"), //$NON-NLS-1$
	      			ENUMERATIONVALUES, new Object[] {
	      				JSeparatorMessages.getString("orientation.HORIZONTAL"), new Integer(javax.swing.SwingConstants.HORIZONTAL), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.HORIZONTAL", //$NON-NLS-1$
	      				JSeparatorMessages.getString("orientation.VERTICAL"), new Integer(javax.swing.SwingConstants.VERTICAL), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.VERTICAL" //$NON-NLS-1$
	    			}
	    		}
	    	)
		
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}
}

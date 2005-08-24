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
 *  $RCSfile: JCheckBoxBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.beans.*;

public class JCheckBoxBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JCheckBoxMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jcheckbox");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JCheckBox.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JCheckBoxMessages.getString("JCheckBox.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JCheckBoxMessages.getString("JCheckBox.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/cbox32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/cbox16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
	    return loadImage("cbox32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("cbox16.gif"); //$NON-NLS-1$
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
	   			DISPLAYNAME, JCheckBoxMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessible context",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isBorderPaintedFlat()
			super.createMethodDescriptor(getBeanClass(),"isBorderPaintedFlat",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JCheckBoxMessages.getString("isBorderPaintedFlat.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JCheckBoxMessages.getString("isBorderPaintedFlat.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}
		  	),
		  	// setBorderPaintedFlat(boolean)
			super.createMethodDescriptor(getBeanClass(),"setBorderPaintedFlat",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JCheckBoxMessages.getString("setBorderPaintedFlat.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JCheckBoxMessages.getString("setBorderPaintedFlat.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("borderPaintedFlat", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JCheckBoxMessages.getString("setDefaultCapable.borderPaintedFlat.Name"), //$NON-NLS-1$
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// updateUI()
			super.createMethodDescriptor(getBeanClass(),"updateUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JCheckBoxMessages.getString("updateUI().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "update the UI",
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
	    	// borderPaintedFlat
			super.createPropertyDescriptor(getBeanClass(),"borderPaintedFlat", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JCheckBoxMessages.getString("borderPaintedFlat.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JCheckBoxMessages.getString("borderPaintedFlat.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
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

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
 *  $RCSfile: JButtonBeanInfo.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.beans.*;

public class JButtonBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle JButtonMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jbutton");  //$NON-NLS-1$

/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	return new EventSetDescriptor[0];
}
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JButton.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JButtonMessages.getString("JButton.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JButtonMessages.getString("JButton.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/butt32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/butt16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
	} catch (Throwable exception) {
		handleException(exception);
	};
	return aDescriptor;
}
	/**
	 * @return an icon of the specified kind for JButton
	 */
	public java.awt.Image getIcon(int kind) {
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("butt32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("butt16.gif"); //$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
		  	// isDefaultButton
			super.createMethodDescriptor(getBeanClass(),"isDefaultButton",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JButtonMessages.getString("isDefaultButton.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JButtonMessages.getString("isDefaultButton.Desc")	    		},  //$NON-NLS-1$
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isDefaultCapable()
			super.createMethodDescriptor(getBeanClass(),"isDefaultCapable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JButtonMessages.getString("isDefaultCapable.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JButtonMessages.getString("isDefaultCapable.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}
		  	),
		  	// setDefaultCapable(boolean)
			super.createMethodDescriptor(getBeanClass(),"setDefaultCapable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JButtonMessages.getString("setDefaultCapable.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JButtonMessages.getString("setDefaultCapable.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("defaultCapable", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JButtonMessages.getString("setDefaultCapable.defaultCapable.Name"), //$NON-NLS-1$
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
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
	    	// defaultButton
			super.createPropertyDescriptor(getBeanClass(),"defaultButton", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JButtonMessages.getString("defaultButton.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JButtonMessages.getString("defaultButton.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// defaultCapable
			super.createPropertyDescriptor(getBeanClass(),"defaultCapable", new Object[] { //$NON-NLS-1$
			    DISPLAYNAME, JButtonMessages.getString("defaultCapable.Name"), //$NON-NLS-1$
	      	    SHORTDESCRIPTION, JButtonMessages.getString("defaultCapable.Desc"), //$NON-NLS-1$
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



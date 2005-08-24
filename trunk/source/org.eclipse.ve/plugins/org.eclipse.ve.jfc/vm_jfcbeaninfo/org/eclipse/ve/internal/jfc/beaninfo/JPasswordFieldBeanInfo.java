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
 *  $RCSfile: JPasswordFieldBeanInfo.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.beans.*;

public class JPasswordFieldBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JPasswordFieldMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jpasswordfield");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JPasswordField.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JPasswordFieldMessages.getString("JPasswordField.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JPasswordFieldMessages.getString("JPasswordField.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/jpwfld32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/jpwfld16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
	    return loadImage("jpwfld32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("jpwfld16.gif"); //$NON-NLS-1$
   	return super.getIcon(kind);
}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
			// echoCharIsSet()
			super.createMethodDescriptor(getBeanClass(),"echoCharIsSet",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPasswordFieldMessages.getString("echoCharIsSet().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JPasswordFieldMessages.getString("echoCharIsSet().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getEchoChar()
			super.createMethodDescriptor(getBeanClass(),"getEchoChar",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPasswordFieldMessages.getString("getEchoChar().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JPasswordFieldMessages.getString("getEchoChar().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setEchoChar(char)
			super.createMethodDescriptor(getBeanClass(),"setEchoChar",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPasswordFieldMessages.getString("setEchoChar(char).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JPasswordFieldMessages.getString("setEchoChar(char).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("c", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPasswordFieldMessages.getString("setEchoChar(char).echoCharacter.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "echo character",
	      				})
	      		},
	      		new Class[] {
	      			char.class
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
	    	// echoChar
			super.createPropertyDescriptor(getBeanClass(),"echoChar", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JPasswordFieldMessages.getString("echoChar.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JPasswordFieldMessages.getString("echoChar.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE	      	
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

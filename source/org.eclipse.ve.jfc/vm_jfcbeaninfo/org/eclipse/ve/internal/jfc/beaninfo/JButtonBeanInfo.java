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
 *  $RCSfile: JButtonBeanInfo.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:33 $ 
 */

import java.beans.*;

public class JButtonBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle JButtonMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jbutton");  //$NON-NLS-1$
private static java.util.ResourceBundle resAbstractButton = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.abstractbutton");  //$NON-NLS-1$

/**
 * Gets the actionevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public java.beans.EventSetDescriptor actionEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.ActionEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.ActionListener.class,
				"actionPerformed",  //$NON-NLS-1$
				new Object[] {
				DISPLAYNAME, resAbstractButton.getString("MthdDesc.ActionPerformed.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, resAbstractButton.getString("MthdDesc.ActionPerformed.Desc"), //$NON-NLS-1$
				PREFERRED, Boolean.TRUE
				}, 
				new ParameterDescriptor[] {
					createParameterDescriptor("actionEvent", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, resAbstractButton.getString("ParamDesc.ActionPerformed.actionEvent.Name"), //$NON-NLS-1$
					// SHORTDESCRIPTION, "Event on clicking the button",
					}
				)
			},
			paramTypes
			)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"action", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, resAbstractButton.getString("EventSetDesc.ActionPerformed.Action.name"), //$NON-NLS-1$
						SHORTDESCRIPTION, resAbstractButton.getString("EventSetDesc.ActionPerformed.Action.Desc"), //$NON-NLS-1$
						INDEFAULTEVENTSET, Boolean.TRUE,
						}, 
						aDescriptorList, java.awt.event.ActionListener.class,
						"addActionListener", "removeActionListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}

/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	try {
		EventSetDescriptor aDescriptorList[] = {
			actionEventSetDescriptor(),
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
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
		  	// getAccessibleContext()
			super.createMethodDescriptor(getBeanClass(),"getAccessibleContext",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JButtonMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessible context",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
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



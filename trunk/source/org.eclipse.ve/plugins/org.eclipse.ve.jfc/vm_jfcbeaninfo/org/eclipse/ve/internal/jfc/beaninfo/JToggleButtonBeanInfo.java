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
 *  $RCSfile: JToggleButtonBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.beans.*;

public class JToggleButtonBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JToggleButtonMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jtogglebutton");  //$NON-NLS-1$
private static java.util.ResourceBundle resAbstractButton = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.abstractbutton");  //$NON-NLS-1$


/**
 * Gets the componentevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor changeEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { javax.swing.event.ChangeEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(javax.swing.event.ChangeListener.class,
				"stateChanged",  //$NON-NLS-1$
				new Object[] {
				DISPLAYNAME, resAbstractButton.getString("MthdDesc.StateChanged.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, resAbstractButton.getString("MthdDesc.StateChanged.Desc"), //$NON-NLS-1$
				PREFERRED, Boolean.TRUE
				}, 
				new ParameterDescriptor[] {
					createParameterDescriptor("stateChangeEvent", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, resAbstractButton.getString("ParamDesc.StateChanged.StateChangedEvent.Name"), //$NON-NLS-1$
					// SHORTDESCRIPTION, "Event fired on state change",
					}
				)
			},
			paramTypes
			)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"change", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, resAbstractButton.getString("EventSetDesc.Change.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, resAbstractButton.getString("EventSetDesc.Change.Desc"), //$NON-NLS-1$
					}, 
						aDescriptorList, javax.swing.event.ChangeListener.class,
						"addChangeListener", "removeChangeListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}
/**
 * Gets the itemevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public java.beans.EventSetDescriptor itemEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.ItemEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.ItemListener.class,
				"itemStateChanged",  //$NON-NLS-1$
				new Object[] {
				DISPLAYNAME, resAbstractButton.getString("MthdDesc.ItemStateChanged.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, resAbstractButton.getString("MthdDesc.ItemStateChanged.Desc"), //$NON-NLS-1$
				PREFERRED, Boolean.TRUE
				}, 
				new ParameterDescriptor[] {
					createParameterDescriptor("itemEvent", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, resAbstractButton.getString("ParamDesc.ItemStateChanged.itemEvent.Name"), //$NON-NLS-1$
					// SHORTDESCRIPTION, "Item state changed event",
					}
				)
			},
			paramTypes
			)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"item", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, resAbstractButton.getString("EventSetDesc.ItemStateChanged.item.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, resAbstractButton.getString("EventSetDesc.ItemStateChanged.item.Desc"), //$NON-NLS-1$
						}, 
						aDescriptorList, java.awt.event.ItemListener.class,
						"addItemListener", "removeItemListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}

/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	try {
		EventSetDescriptor aDescriptorList[] = {
			changeEventSetDescriptor(),
			itemEventSetDescriptor()
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
	return javax.swing.JToggleButton.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JToggleButtonMessages.getString("JToggleButton.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JToggleButtonMessages.getString("JToggleButton.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/jtbutn32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/jtbutn16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
	    return loadImage("jtbutn32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("jtbutn16.gif"); //$NON-NLS-1$
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
	   			DISPLAYNAME, JToggleButtonMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessible context",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// updateUI()
			super.createMethodDescriptor(getBeanClass(),"updateUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JToggleButtonMessages.getString("updateUI().Name"), //$NON-NLS-1$
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
	return (new PropertyDescriptor[0]);
}
}

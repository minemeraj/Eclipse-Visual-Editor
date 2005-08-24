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
 *  $RCSfile: CheckboxMenuItemBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:12 $ 
 */

import java.beans.*;
public class CheckboxMenuItemBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle rescheckboxmenuitem = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.checkboxmenuitem");  //$NON-NLS-1$
	
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return java.awt.CheckboxMenuItem.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		/* Create and return the CheckboxMenuItemBeanInfobean descriptor. */
		aDescriptor = new java.beans.BeanDescriptor(java.awt.CheckboxMenuItem.class);
		aDescriptor.setDisplayName(rescheckboxmenuitem.getString("CheckboxMenuItemDN")); //$NON-NLS-1$
		aDescriptor.setShortDescription(rescheckboxmenuitem.getString("CheckboxMenuItemSD")); //$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/cboxmi32.gif");//$NON-NLS-2$//$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/cboxmi16.gif");//$NON-NLS-2$//$NON-NLS-1$
	} catch (Throwable exception) {
	};
	return aDescriptor;
}
/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	try {
		java.beans.EventSetDescriptor aDescriptorList[] = {
			itemEventSetDescriptor()
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}
	/**
	 * @return an icon of the specified kind for JButton
	 */
	public java.awt.Image getIcon(int kind) {
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("cboxmi32.gif");//$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("cboxmi16.gif");//$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		java.beans.MethodDescriptor aDescriptorList[] = {
			// addNotify()
			super.createMethodDescriptor(getBeanClass(),"addNotify", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "addNotify()",  //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Create the peer of the checkbox and notify",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),		
			// getSelectedObjects()
			super.createMethodDescriptor(getBeanClass(),"getSelectedObjects", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getSelectedObjects()",  //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Return array with checkbox menu item label",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getState()
			super.createMethodDescriptor(getBeanClass(),"getState", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getState()",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Return the boolean state of the checkbox",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),		
			// setState(boolean)
			super.createMethodDescriptor(getBeanClass(),"setState", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setState(boolean)",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the menu item check box's state",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescheckboxmenuitem.getString("stateParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Boolean state",
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
		java.beans.PropertyDescriptor aDescriptorList[] = {
			// selectedObjects
	   	super.createPropertyDescriptor(getBeanClass(),"selectedObjects", new Object[] {//$NON-NLS-1$
		   	DISPLAYNAME, rescheckboxmenuitem.getString("selectedObjectsDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescheckboxmenuitem.getString("selectedObjectsSD"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
			// state
			super.createPropertyDescriptor(getBeanClass(),"state", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, rescheckboxmenuitem.getString("stateDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescheckboxmenuitem.getString("stateSD"), //$NON-NLS-1$
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
/**
 * Gets the itemevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public java.beans.EventSetDescriptor itemEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.ItemEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.ItemListener.class,
				"itemStateChanged", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescheckboxmenuitem.getString("itemStateChangedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescheckboxmenuitem.getString("itemStateChangedSD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("itemEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescheckboxmenuitem.getString("itemEventDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "item state changed event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"item", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, rescheckboxmenuitem.getString("itemDN"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, rescheckboxmenuitem.getString("itemSD"), //$NON-NLS-1$
	      			}, 
						aDescriptorList, java.awt.event.ItemListener.class,
						"addItemListener", "removeItemListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}
}

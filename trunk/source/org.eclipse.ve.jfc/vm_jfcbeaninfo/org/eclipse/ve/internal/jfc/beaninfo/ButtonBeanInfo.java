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
 *  $RCSfile: ButtonBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.beans.*;
public class ButtonBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle resbutton = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.button");  //$NON-NLS-1$
	
/**
 * Gets the actionevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public java.beans.EventSetDescriptor actionEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.ActionEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.ActionListener.class,
				"actionPerformed", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resbutton.getString("actionPerformedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resbutton.getString("actionPerformedSD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("actionEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resbutton.getString("actionEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Event on clicking button",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"action", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, resbutton.getString("actionEventsDN"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, resbutton.getString("actionEventsSD"), //$NON-NLS-1$
	      				INDEFAULTEVENTSET, Boolean.TRUE,
	      				PREFERRED, Boolean.TRUE,	      				
	      			}, 
						aDescriptorList, java.awt.event.ActionListener.class,
						"addActionListener", "removeActionListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return java.awt.Button.class;
}
/**
 * This method was created by a SmartGuide.
 * @return java.beans.BeanDescriptor
 */
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		/* Create and return the Button bean descriptor. */
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               	DISPLAYNAME, resbutton.getString("ButtonDN"), //$NON-NLS-1$
	        		SHORTDESCRIPTION, resbutton.getString("ButtonSD") //$NON-NLS-1$
							}			    
						);
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/butt32.gif");//$NON-NLS-2$//$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/butt16.gif");//$NON-NLS-2$//$NON-NLS-1$
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
	try {
		java.beans.EventSetDescriptor aDescriptorList[] = {
			actionEventSetDescriptor()
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
	    return loadImage("butt32.gif");//$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("butt16.gif");//$NON-NLS-1$
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
	   			DISPLAYNAME, "addNotify()", //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Create the peer for the button",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getActionCommand()
			super.createMethodDescriptor(getBeanClass(),"getActionCommand", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getActionCommand()", //$NON-NLS-1$
	      		SHORTDESCRIPTION, resbutton.getString("getActionCommand()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getLabel()
			super.createMethodDescriptor(getBeanClass(),"getLabel", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getLabel()", //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the button label",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),	
			// setActionCommand(String)
			super.createMethodDescriptor(getBeanClass(),"setActionCommand", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setActionCommand(String)", //$NON-NLS-1$
	      		SHORTDESCRIPTION, resbutton.getString("setActionCommand(String)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resbutton.getString("commandParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "String for command name",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		String.class 
	      	}	    		
		  	),	
			// setLabel(String)
			super.createMethodDescriptor(getBeanClass(),"setLabel", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setLabel(String)", //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set this button's label",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resbutton.getString("labelParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Button label",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		java.lang.String.class 
	      	}	    		
		  	),
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
			// actionCommand
	   	super.createPropertyDescriptor(getBeanClass(),"actionCommand", new Object[] {//$NON-NLS-1$
		   	DISPLAYNAME, resbutton.getString("actionCommandDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resbutton.getString("actionCommandSD"), //$NON-NLS-1$	      		      	

	    		}
	    	),				
			// label
	   	super.createPropertyDescriptor(getBeanClass(),"label", new Object[] {//$NON-NLS-1$
		   	DISPLAYNAME, resbutton.getString("labelDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resbutton.getString("labelSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE,	      		      	

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

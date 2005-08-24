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
 *  $RCSfile: MenuComponentBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.beans.*;

public class MenuComponentBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle resmenucomponent = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.menucomponent");  //$NON-NLS-1$
	
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return java.awt.MenuComponent.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		/* Create and return the MenuComponentBeanInfobean descriptor. */
		aDescriptor = new java.beans.BeanDescriptor(java.awt.MenuComponent.class);
		aDescriptor.setDisplayName(resmenucomponent.getString("MenuComponentDN")); //$NON-NLS-1$
		aDescriptor.setShortDescription(resmenucomponent.getString("MenuComponentSD")); //$NON-NLS-1$
	} catch (Throwable exception) {
	};
	return aDescriptor;
}
/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	return (new java.beans.EventSetDescriptor[0]);
}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		java.beans.MethodDescriptor aDescriptorList[] = {
			// dispatchEvent(AWTEvent)
			super.createMethodDescriptor(getBeanClass(),"dispatchEvent", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "dispatchEvent(AWTEvent)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Deliver the event",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenucomponent.getString("eventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Event to dispatch",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		java.awt.AWTEvent.class 
	      	}	    		
		  	),
			// getFont()
			super.createMethodDescriptor(getBeanClass(),"getFont", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getFont()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the font",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getName()
			super.createMethodDescriptor(getBeanClass(),"getName", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getName()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the name",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getParent()
			super.createMethodDescriptor(getBeanClass(),"getParent", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getParent()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the parent container",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getPeer() - DEPRECATED
			// postEvent(Event) - DEPRECATED
			// removeNotify()
			super.createMethodDescriptor(getBeanClass(),"removeNotify", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "removeNotify()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Remove the peer",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// setFont(Font)
			super.createMethodDescriptor(getBeanClass(),"setFont", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setFont(Font)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the font",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenucomponent.getString("fontParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Font",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		java.awt.Font.class 
	      	}	    		
		  	),
			// setName(String)
			super.createMethodDescriptor(getBeanClass(),"setName", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setName(String)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the name",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenucomponent.getString("nameParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "name of menu component",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		String.class 
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
			// font
			super.createPropertyDescriptor(getBeanClass(),"font", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resmenucomponent.getString("fontParmDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resmenucomponent.getString("fontSD"), //$NON-NLS-1$
	    		}
	    	),
			// name
			super.createPropertyDescriptor(getBeanClass(),"name", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resmenucomponent.getString("nameParmDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resmenucomponent.getString("nameSD"), //$NON-NLS-1$
	    		}
	    	),
			// parent
			super.createPropertyDescriptor(getBeanClass(),"parent", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resmenucomponent.getString("parentDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resmenucomponent.getString("parentSD"), //$NON-NLS-1$
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

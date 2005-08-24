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
 *  $RCSfile: WindowBeanInfo.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.beans.*;

public class WindowBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle reswindow = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.window");  //$NON-NLS-1$
	
	
public Class getBeanClass() {
	return java.awt.Window.class;
}
public BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		/* Create and return the WindowBeanInfobean descriptor. */
		aDescriptor = new java.beans.BeanDescriptor(java.awt.Window.class);
		aDescriptor.setDisplayName(reswindow.getString("WindowDN")); //$NON-NLS-1$
		aDescriptor.setShortDescription(reswindow.getString("WindowSD")); //$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/window32.gif");//$NON-NLS-2$//$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/window16.gif");//$NON-NLS-2$//$NON-NLS-1$
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
		EventSetDescriptor aDescriptorList[] = {
			windowEventSetDescriptor()
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
	    return loadImage("window32.gif");//$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("window16.gif");//$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {		
			// addNotify()
			super.createMethodDescriptor(getBeanClass(),"addNotify", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "addNotify()",//$NON-NLS-1$
			   	EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Create the window peer",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// dispose()
			super.createMethodDescriptor(getBeanClass(),"dispose", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "dispose()",//$NON-NLS-1$
			   	PREFERRED, Boolean.TRUE,
	      		SHORTDESCRIPTION, reswindow.getString("dispose()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getFocusOwner()
			super.createMethodDescriptor(getBeanClass(),"getFocusOwner", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getFocusOwner()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, reswindow.getString("getFocusOwner()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// getLocale()
			super.createMethodDescriptor(getBeanClass(),"getLocale", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getLocale()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the window locale",
	      	   EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getWarningString()
			super.createMethodDescriptor(getBeanClass(),"getWarningString", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getWarningString()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the security warning string",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// pack()
			super.createMethodDescriptor(getBeanClass(),"pack", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "pack()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, reswindow.getString("pack()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// show()
			super.createMethodDescriptor(getBeanClass(),"show", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "show()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, reswindow.getString("show()SD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// toBack()
			super.createMethodDescriptor(getBeanClass(),"toBack", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "toBack()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, reswindow.getString("toBack()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// toFront()
			super.createMethodDescriptor(getBeanClass(),"toFront", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "toFront()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, reswindow.getString("toFront()SD"), //$NON-NLS-1$
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
			// focusOwner
			super.createPropertyDescriptor(getBeanClass(),"focusOwner", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, reswindow.getString("focusOwnerDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, reswindow.getString("focusOwnerSD"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE
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
 * Gets the windowevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor windowEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.WindowEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.WindowListener.class,
				"windowActivated",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, reswindow.getString("windowActivatedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, reswindow.getString("windowActivatedSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("windowEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reswindow.getString("windowEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Window activated event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(java.awt.event.WindowListener.class,
				"windowClosed", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, reswindow.getString("windowClosedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, reswindow.getString("windowClosedSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("windowEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reswindow.getString("windowEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Window closed event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(java.awt.event.WindowListener.class,
				"windowClosing", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, reswindow.getString("windowClosingDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, reswindow.getString("windowClosingSD"), //$NON-NLS-1$
				PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("windowEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reswindow.getString("windowEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Window closing event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(java.awt.event.WindowListener.class,
				"windowDeactivated", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, reswindow.getString("windowDeactivatedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, reswindow.getString("windowDeactivatedSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("windowEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reswindow.getString("windowEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Window deactivated event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(java.awt.event.WindowListener.class,
				"windowDeiconified", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, reswindow.getString("windowDeiconifiedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, reswindow.getString("windowDeiconifiedSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("windowEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reswindow.getString("windowEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Window deiconified event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(java.awt.event.WindowListener.class,
				"windowIconified", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, reswindow.getString("windowIconifiedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, reswindow.getString("windowIconifiedSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("windowEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reswindow.getString("windowEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Window iconified event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(java.awt.event.WindowListener.class,
				"windowOpened", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, reswindow.getString("windowOpenedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, reswindow.getString("windowOpenedSD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("windowEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reswindow.getString("windowEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Window opened event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"window", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, reswindow.getString("windowEventsDN"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, reswindow.getString("windowEventsSD"), //$NON-NLS-1$
	      				INDEFAULTEVENTSET, Boolean.TRUE,
	      				PREFERRED, Boolean.TRUE,
	      				EVENTADAPTERCLASS, "java.awt.event.WindowAdapter"			 //$NON-NLS-1$
	      			}, 
						aDescriptorList, java.awt.event.WindowListener.class,
						"addWindowListener", "removeWindowListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}
}

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
 *  $RCSfile: MenuItemBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:12 $ 
 */

import java.beans.*;

public class MenuItemBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle resmenuitem = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.menuitem");  //$NON-NLS-1$
	
	
public java.beans.EventSetDescriptor actionEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.ActionEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.ActionListener.class,
				"actionPerformed", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resmenuitem.getString("actionPerformedEventDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenuitem.getString("actionPerformedEventSD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("actionEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenuitem.getString("actionEventDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Event on clicking menu",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"action", new Object[] {//$NON-NLS-1$
							DISPLAYNAME, resmenuitem.getString("actionEventsDN"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, resmenuitem.getString("actionEventsSD"), //$NON-NLS-1$
	      				// INDEFAULTEVENTSET, Boolean.TRUE,
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
	return java.awt.MenuItem.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		/* Create and return the MenuItemBeanInfobean descriptor. */
		aDescriptor = new java.beans.BeanDescriptor(java.awt.MenuItem.class);
		aDescriptor.setDisplayName(resmenuitem.getString("MenuItemDN")); //$NON-NLS-1$
		aDescriptor.setShortDescription(resmenuitem.getString("MenuItemSD")); //$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/menuit32.gif");//$NON-NLS-2$//$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/menuit16.gif");//$NON-NLS-2$//$NON-NLS-1$
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
	    return loadImage("menuit32.gif");//$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("menuit16.gif");//$NON-NLS-1$
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
	   			DISPLAYNAME, "addNotify()",//$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Create the menu item peer",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// deleteShortcut()
		  	super.createMethodDescriptor(getBeanClass(),"deleteShortcut", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "deleteShortcut()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenuitem.getString("deleteShortcut()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getActionCommand()
			super.createMethodDescriptor(getBeanClass(),"getActionCommand", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getActionCommand()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenuitem.getString("getActionCommand()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getLabel()
			super.createMethodDescriptor(getBeanClass(),"getLabel", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getLabel()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the menu label",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// getShortcut()
			super.createMethodDescriptor(getBeanClass(),"getShortcut", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getShortcut()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the menu shortcut",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// isEnabled()
			super.createMethodDescriptor(getBeanClass(),"isEnabled", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "isEnabled()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenuitem.getString("isEnabled()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// paramString()
			super.createMethodDescriptor(getBeanClass(),"paramString", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "paramString()",//$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "String representing the menu state",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// setActionCommand(String)
			super.createMethodDescriptor(getBeanClass(),"setActionCommand", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setActionCommand(String)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenuitem.getString("setActionCommand(String)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenuitem.getString("commandParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "String for command name",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		String.class 
	      	}	    		
		  	),
		  	// setEnabled(Boolean)
			super.createMethodDescriptor(getBeanClass(),"setEnabled", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setEnabled(boolean)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenuitem.getString("setEnabled(boolean)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenuitem.getString("boolParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "TRUE to enable the menu",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		boolean.class 
	      	}	    		
		  	),	
			// setLabel(String)
			super.createMethodDescriptor(getBeanClass(),"setLabel", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setLabel(String)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the menu's label",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenuitem.getString("labelParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Menu label",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		java.lang.String.class 
	      	}	    		
		  	),
		  	// setShortcut(MenuShortcut)
			super.createMethodDescriptor(getBeanClass(),"setShortcut", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setShortcut(MenuShortcut)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the menu's shortcut",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenuitem.getString("shortcutParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Menu shortcut",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		java.awt.MenuShortcut.class 
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
			// actionCommand
	   	super.createPropertyDescriptor(getBeanClass(),"actionCommand", new Object[] {//$NON-NLS-1$
		   	DISPLAYNAME, resmenuitem.getString("actionCommandDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resmenuitem.getString("actionCommandSD"), //$NON-NLS-1$
	    		}
	    	),				
			// label
	   	super.createPropertyDescriptor(getBeanClass(),"label", new Object[] {//$NON-NLS-1$
		   	DISPLAYNAME, resmenuitem.getString("labelDN"),		   	 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resmenuitem.getString("labelSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
			// shortcut
			super.createPropertyDescriptor(getBeanClass(),"shortcut", new Object[] {//$NON-NLS-1$
		   	DISPLAYNAME, resmenuitem.getString("shortcutDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resmenuitem.getString("shortcutSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
	    	// enabled
			super.createPropertyDescriptor(getBeanClass(),"enabled", new Object[] {//$NON-NLS-1$
		   	DISPLAYNAME, resmenuitem.getString("enabledDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resmenuitem.getString("enabledSD"), //$NON-NLS-1$
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

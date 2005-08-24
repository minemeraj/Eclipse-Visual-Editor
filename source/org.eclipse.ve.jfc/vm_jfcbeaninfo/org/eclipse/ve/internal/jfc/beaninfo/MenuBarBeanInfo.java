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
 *  $RCSfile: MenuBarBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.beans.*;

public class MenuBarBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle resmenubar = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.menubar");  //$NON-NLS-1$

	
public Class getBeanClass() {
	return java.awt.MenuBar.class;
}
public BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		/* Create and return the MenuBarBeanInfobean descriptor. */
		aDescriptor = new java.beans.BeanDescriptor(java.awt.MenuBar.class);
		aDescriptor.setDisplayName(resmenubar.getString("MenuBarDN")); //$NON-NLS-1$
		aDescriptor.setShortDescription(resmenubar.getString("MenuBarSD")); //$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/menubr32.gif");//$NON-NLS-2$//$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/menubr16.gif");//$NON-NLS-2$//$NON-NLS-1$
	} catch (Throwable exception) {
	};
	return aDescriptor;
}
/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	return (new EventSetDescriptor[0]);
}
	/**
	 * @return an icon of the specified kind for JButton
	 */
	public java.awt.Image getIcon(int kind) {
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("menubr32.gif");//$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("menubr16.gif");//$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
			// add(Menu)
			super.createMethodDescriptor(getBeanClass(),"add", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "add(Menu)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenubar.getString("add(Menu)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenubar.getString("menuParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Menu to add",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		java.awt.Menu.class 
	      	}	    		
		  	),
			// addNotify()
			super.createMethodDescriptor(getBeanClass(),"addNotify", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "addNotify()",//$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Create the peer of the choice",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// deleteShortcut(MenuShortcut)
			super.createMethodDescriptor(getBeanClass(),"deleteShortcut", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "deleteShortcut(MenuShortcut)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenubar.getString("deleteShortcut(MenuShortcut)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenubar.getString("menuShortcutParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "menu shortcut",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		java.awt.MenuShortcut.class 
	      	}	    		
		  	),
		  	// getHelpMenu()
			super.createMethodDescriptor(getBeanClass(),"getHelpMenu", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getHelpMenu()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the help menu",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getMenu(int)
			super.createMethodDescriptor(getBeanClass(),"getMenu", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getMenu(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenubar.getString("getMenu(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenubar.getString("indexParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "index of menu to get",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class 
	      	}	    		
		  	),
			// getMenuCount()
			super.createMethodDescriptor(getBeanClass(),"getMenuCount", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getMenuCount()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenubar.getString("getMenuCount()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// getShortcutMenuItem(MenuShortcut)
			super.createMethodDescriptor(getBeanClass(),"getShortcutMenuItem", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getShortcutMenuItem(MenuShortcut)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenubar.getString("getShortcutMenuItem(MenuShortcut)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenubar.getString("menuShortcutParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "menu shortcut",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		java.awt.MenuShortcut.class 
	      	}	    		
		  	),		
			// remove(int)
			super.createMethodDescriptor(getBeanClass(),"remove", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "remove(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenubar.getString("remove(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenubar.getString("positionParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "index of item to remove",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class 
	      	}	    		
		  	),
			// remove(MenuComponent)
			super.createMethodDescriptor(getBeanClass(),"remove", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "remove(MenuComponent)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenubar.getString("remove(MenuComponent)SD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenubar.getString("itemParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "item to remove",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		java.awt.MenuComponent.class 
	      	}	    		
		  	),
		  	// removeNotify()
			super.createMethodDescriptor(getBeanClass(),"removeNotify", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "removeNotify()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Remove menu peer",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// setHelpMenu(Menu)
			super.createMethodDescriptor(getBeanClass(),"setHelpMenu", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setHelpMenu(Menu)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the help menu",
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenubar.getString("menuParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Help menu",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		java.awt.Menu.class 
	      	}	    		
		  	),
			// shortcuts()
			super.createMethodDescriptor(getBeanClass(),"shortcuts", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "shortcuts()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenubar.getString("shortcuts()SD"), //$NON-NLS-1$
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
			// helpMenu
			super.createPropertyDescriptor(getBeanClass(),"helpMenu", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resmenubar.getString("helpMenuDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resmenubar.getString("helpMenuSD"), //$NON-NLS-1$
	    		}
	    	),
			// menuCount
			super.createPropertyDescriptor(getBeanClass(),"menuCount", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resmenubar.getString("menuCountDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resmenubar.getString("menuCountSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}
}

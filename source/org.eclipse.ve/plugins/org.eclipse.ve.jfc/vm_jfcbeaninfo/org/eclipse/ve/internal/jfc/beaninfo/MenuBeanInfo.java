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
 *  $RCSfile: MenuBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.beans.*;
public class MenuBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle resmenu = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.menu");  //$NON-NLS-1$
	
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return java.awt.Menu.class;
}

public BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		/* Create and return the MenuBeanInfobean descriptor. */
		aDescriptor = new java.beans.BeanDescriptor(java.awt.Menu.class);
		aDescriptor.setDisplayName(resmenu.getString("MenuDN")); //$NON-NLS-1$
		aDescriptor.setShortDescription(resmenu.getString("MenuSD")); //$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/menu32.gif");//$NON-NLS-2$//$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/menu16.gif");//$NON-NLS-2$//$NON-NLS-1$
	} catch (Throwable exception) {
	};
	return aDescriptor;
}
/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	return( new EventSetDescriptor[0]);
}
	/**
	 * @return an icon of the specified kind for JButton
	 */
	public java.awt.Image getIcon(int kind) {
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("menu32.gif");//$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("menu16.gif");//$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
			// add(MenuItem)
			super.createMethodDescriptor(getBeanClass(),"add", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "add(MenuItem)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenu.getString("add(MenuItem)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenu.getString("menuItemParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Menu item to add",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		java.awt.MenuItem.class 
	      	}	    		
		  	),
			// add(String)
			super.createMethodDescriptor(getBeanClass(),"add", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "add(String)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenu.getString("add(String)SD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenu.getString("labelParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Label of menu item to add",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		String.class 
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
		  	// addSeparator()
			super.createMethodDescriptor(getBeanClass(),"addSeparator", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "addSeparator()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Add a separator line",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getItem(int)
			super.createMethodDescriptor(getBeanClass(),"getItem", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getItem(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenu.getString("getItem(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenu.getString("indexParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "index of menu item to get",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class 
	      	}	    		
		  	),
			// getItemCount()
			super.createMethodDescriptor(getBeanClass(),"getItemCount", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getItemCount()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenu.getString("getItemCount()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// insert(MenuItem,int)
			super.createMethodDescriptor(getBeanClass(),"insert", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "insert(MenuItem,int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenu.getString("insert(MenuItem,int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenu.getString("itemParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "menu item to insert",
	      			}
	      		),	
	    			createParameterDescriptor("arg2", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenu.getString("indexParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "index to insert at",
	      			}
	      		)
	      	},
	      	new Class[] {
	      		java.awt.MenuItem.class,
	      		int.class 
	      	}	    		
		  	),
		  	// insert(String,int)
			super.createMethodDescriptor(getBeanClass(),"insert", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "insert(String,int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenu.getString("insert(String,int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenu.getString("labelParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Label of menu item to insert",
	      			}
	      		),	
	    			createParameterDescriptor("arg2", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenu.getString("indexParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "index to insert at",
	      			}
	      		)
	      	},
	      	new Class[] {
	      		String.class,
	      		int.class 
	      	}	    		
		  	),
		  	// insertSeparator(int)
			super.createMethodDescriptor(getBeanClass(),"insertSeparator", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "insertSeparator(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenu.getString("insertSeparator(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenu.getString("indexParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "index to insert at",
	      			}
	      		)
	      	},
	      	new Class[] {
	      		int.class 
	      	}	    		
		  	),
		  	// isTearOff()
			super.createMethodDescriptor(getBeanClass(),"isTearOff", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "isTearOff()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenu.getString("isTearOff()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// paramString()
			super.createMethodDescriptor(getBeanClass(),"paramString", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "paramString()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "String representing menu state",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),		
			// remove(int)
			super.createMethodDescriptor(getBeanClass(),"remove", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "remove(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenu.getString("remove(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenu.getString("positionParmDN"), //$NON-NLS-1$
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
	      		SHORTDESCRIPTION, resmenu.getString("remove(MenuComponent)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resmenu.getString("itemParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "item to remove",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		java.awt.MenuComponent.class 
	      	}	    		
		  	),
			// removeAll()
			super.createMethodDescriptor(getBeanClass(),"removeAll", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "removeAll()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resmenu.getString("removeAll()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
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
			// itemCount
			super.createPropertyDescriptor(getBeanClass(),"itemCount", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resmenu.getString("itemCountDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resmenu.getString("itemCountSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
			 // tearOff
			super.createPropertyDescriptor(getBeanClass(),"tearOff", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resmenu.getString("tearOffDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resmenu.getString("tearOffSD"), //$NON-NLS-1$
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

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
 *  $RCSfile: JMenuBeanInfo.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.beans.*;

public class JMenuBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JMenuMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jmenu");  //$NON-NLS-1$
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JMenu.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JMenuMessages.getString("JMenu.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JMenuMessages.getString("JMenu.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/menu32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/menu16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
			menuEventSetDescriptor()
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
	    return loadImage("menu32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("menu16.gif"); //$NON-NLS-1$
   	return super.getIcon(kind);
}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
			// add(Action)
			super.createMethodDescriptor(getBeanClass(),"add",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("add(Action).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuMessages.getString("add(Action).Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("action", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuMessages.getString("add(Action).action.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Action object",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.Action.class
	      		}		    		
		  	),
		  	// add(JMenuItem)
			super.createMethodDescriptor(getBeanClass(),"add",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("add(JMenuItem).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuMessages.getString("add(JMenuItem).Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("menuItem", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuMessages.getString("add(JMenuItem).menuItem.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Menu item",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.JMenuItem.class
	      		}		    		
		  	),
		  	// add(String)
			super.createMethodDescriptor(getBeanClass(),"add",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("add(String).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuMessages.getString("add(String).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("text", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuMessages.getString("add(String).text.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "text",
	      				})
	      		},
	      		new Class[] {
	      			String.class
	      		}		    		
		  	),
		  	// addSeparator()
			super.createMethodDescriptor(getBeanClass(),"addSeparator",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("addSeparator().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Add a separator",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getAccessibleContext()
			super.createMethodDescriptor(getBeanClass(),"getAccessibleContext",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessible context",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getDelay()
			super.createMethodDescriptor(getBeanClass(),"getDelay",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("getDelay().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuMessages.getString("getDelay().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getItem(int)
			super.createMethodDescriptor(getBeanClass(),"getItem",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("getItem(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the menu item at specified position",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("pos", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuMessages.getString("getItem(int).position.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "position",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
			// getItemCount()
			super.createMethodDescriptor(getBeanClass(),"getItemCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("getItemCount().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the number of menu items",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMenuComponent(int)
			super.createMethodDescriptor(getBeanClass(),"getMenuComponent",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("getMenuComponent(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the menu component at specified position",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("pos", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuMessages.getString("getMenuComponent(int).position.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "position",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// getMenuComponentCount()
			super.createMethodDescriptor(getBeanClass(),"getMenuComponentCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("getMenuComponentCount().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuMessages.getString("getMenuComponentCount().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMenuComponents()
			super.createMethodDescriptor(getBeanClass(),"getMenuComponents",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("getMenuComponents().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get an array of menu components",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// insert(Action,int)
			super.createMethodDescriptor(getBeanClass(),"insert",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("insert(Action,int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuMessages.getString("insert(Action,int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("action", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuMessages.getString("insert(Action,int).action.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Action object",
	      				}),
	    			createParameterDescriptor("pos", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuMessages.getString("insert(Action,int).position.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "position"
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.Action.class, int.class
	      		}		    		
		  	),
		  	// insert(JMenuItem,int)
			super.createMethodDescriptor(getBeanClass(),"insert",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("insert(JMenuItem,int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Insert the menu item at specified position",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("menu", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuMessages.getString("insert(JMenuItem,int).menu.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Menu item",
	      				}),
	    			createParameterDescriptor("pos", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuMessages.getString("insert(JMenuItem,int).position.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "position"
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.JMenuItem.class, int.class
	      		}		    		
		  	),
		  	// insert(String,int)
			super.createMethodDescriptor(getBeanClass(),"insert",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("insert(String,int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuMessages.getString("insert(String,int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("action", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuMessages.getString("insert(String,int).action.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Action object",
	      				}),
	    			createParameterDescriptor("pos", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuMessages.getString("insert(String,int).position.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "position"
	      				})
	      		},
	      		new Class[] {
	      			String.class, int.class
	      		}		    		
		  	),
		  	// insertSeparator(int)
			super.createMethodDescriptor(getBeanClass(),"insertSeparator",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("insertSeparator(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuMessages.getString("insertSeparator(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("pos", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuMessages.getString("insertSeparator(int).position.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "position"
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// isPopupMenuVisible()
			super.createMethodDescriptor(getBeanClass(),"isPopupMenuVisible",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("isPopupMenuVisible().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuMessages.getString("isPopupMenuVisible().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isTopLevelMenu()
			super.createMethodDescriptor(getBeanClass(),"isTopLevelMenu",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("isTopLevelMenu().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuMessages.getString("isTopLevelMenu().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// remove(JMenuItem)
			super.createMethodDescriptor(getBeanClass(),"remove",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("remove(JMenuItem).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuMessages.getString("remove(JMenuItem).Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("menuItem", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuMessages.getString("remove(JMenuItem).menuItem.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Menu item",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.JMenuItem.class
	      		}		    		
		  	),
		  	// Hidden from JMenu by design of Swing
			super.createMethodDescriptor(getBeanClass(),"setAccelerator",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("setAccelerator(KeyStroke).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the accelerator key for the menu item",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("key", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuMessages.getString("setAccelerator(KeyStroke).keyStroke.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "KeyStroke",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.KeyStroke.class
	      		}		    		
		  	),
         	// setDelay(int)
			super.createMethodDescriptor(getBeanClass(),"setDelay",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("setDelay(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the delay ",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("d", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuMessages.getString("setDelay(int).delay.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Delay time in milliseconds",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setMenuLocation(int,int)
			super.createMethodDescriptor(getBeanClass(),"setMenuLocation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("setMenuLocation(int,int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the menu at given location",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("x", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuMessages.getString("setMenuLocation(int,int).x.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "x coordinate",
	      				}),
	      			createParameterDescriptor("y", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuMessages.getString("setMenuLocation(int,int).y.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "y coordinate",
	      				})
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// setPopupMenuVisible(boolean)
			super.createMethodDescriptor(getBeanClass(),"setPopupMenuVisible",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("setPopupMenuVisible(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set whether the popup menu is visible",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuMessages.getString("setPopupMenuVisible(boolean).b.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "True if visible",
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
			// delay
			super.createPropertyDescriptor(getBeanClass(),"delay", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JMenuMessages.getString("delay.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuMessages.getString("delay.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// itemCount _ AWT compatibility
			super.createPropertyDescriptor(getBeanClass(),"itemCount", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JMenuMessages.getString("itemCount.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuMessages.getString("itemCount.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// menuComponentCount
			super.createPropertyDescriptor(getBeanClass(),"menuComponentCount", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JMenuMessages.getString("menuComponentCount.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuMessages.getString("menuComponentCount.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// menuComponents
			super.createPropertyDescriptor(getBeanClass(),"menuComponents", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JMenuMessages.getString("menuComponents.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuMessages.getString("menuComponents.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// popupMenu
			super.createPropertyDescriptor(getBeanClass(),"popupMenu", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JMenuMessages.getString("popupMenu.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuMessages.getString("popupMenu.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// popupMenuVisible
			super.createPropertyDescriptor(getBeanClass(),"popupMenuVisible", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JMenuMessages.getString("popupMenuVisible.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuMessages.getString("popupMenuVisible.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE,
	      	    DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
	    	// topLevelMenu
			super.createPropertyDescriptor(getBeanClass(),"topLevelMenu", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JMenuMessages.getString("topLevelMenu.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuMessages.getString("topLevelMenu.Desc"), //$NON-NLS-1$
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
 * Gets the actionevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public java.beans.EventSetDescriptor menuEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { javax.swing.event.MenuEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(javax.swing.event.MenuListener.class,
				"menuCanceled",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("menuCanceled.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuMessages.getString("menuCanceled.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("menuEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JMenuMessages.getString("menuCanceled.menuEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Event on canceling menu",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(javax.swing.event.MenuListener.class,
				"menuSelected",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("menuSelected.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuMessages.getString("menuSelected.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("menuEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JMenuMessages.getString("menuSelected.menuEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Event on selecting menu",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(javax.swing.event.MenuListener.class,
				"menuDeselected",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuMessages.getString("menuDeselected.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuMessages.getString("menuDeselected.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("menuEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JMenuMessages.getString("menuDeselected.menuEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Event on deselecting menu",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"menu", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JMenuMessages.getString("menuEvents.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JMenuMessages.getString("menuEvents.Desc"), //$NON-NLS-1$
	      			}, 
						aDescriptorList, javax.swing.event.MenuListener.class,
						"addMenuListener", "removeMenuListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}
}

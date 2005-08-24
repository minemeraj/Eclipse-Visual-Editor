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
 *  $RCSfile: JMenuItemBeanInfo.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.beans.*;

public class JMenuItemBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JMenuItemMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jmenuitem");  //$NON-NLS-1$

/**
 * Gets the menudragmouseevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor menuDragMouseEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { javax.swing.event.MenuDragMouseEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(javax.swing.event.MenuDragMouseListener.class,
				"menuDragMouseDragged", //$NON-NLS-1$
				new Object[] {
				DISPLAYNAME, JMenuItemMessages.getString("menuDragMouseDraggedDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, JMenuItemMessages.getString("menuDragMouseDraggedSD"), //$NON-NLS-1$
				}, 
				new ParameterDescriptor[] {
					createParameterDescriptor("menuDragMouseEvent", new Object[] {//$NON-NLS-1$
					DISPLAYNAME, JMenuItemMessages.getString("menuDragMouseEventParmDN"), //$NON-NLS-1$
					// SHORTDESCRIPTION, "menu Drag Mouse Dragged event",
					}
				)
			},
			paramTypes
			),
			super.createMethodDescriptor(javax.swing.event.MenuDragMouseListener.class,
				"menuDragMouseEntered", //$NON-NLS-1$
				new Object[] {
				DISPLAYNAME, JMenuItemMessages.getString("menuDragMouseEnteredDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, JMenuItemMessages.getString("menuDragMouseEnteredSD"), //$NON-NLS-1$
				}, 
				new ParameterDescriptor[] {
					createParameterDescriptor("menuDragMouseEvent", new Object[] {//$NON-NLS-1$
					DISPLAYNAME, JMenuItemMessages.getString("menuDragMouseEventDN"), //$NON-NLS-1$
					// SHORTDESCRIPTION, "menuDragMouse entered event",
					}
				)
			},
			paramTypes
			),
			super.createMethodDescriptor(javax.swing.event.MenuDragMouseListener.class,
				"menuDragMouseExited", //$NON-NLS-1$
				new Object[] {
				DISPLAYNAME, JMenuItemMessages.getString("menuDragMouseExitedDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, JMenuItemMessages.getString("menuDragMouseExitedSD"), //$NON-NLS-1$
				}, 
				new ParameterDescriptor[] {
					createParameterDescriptor("menuDragMouseEvent", new Object[] {//$NON-NLS-1$
					DISPLAYNAME, JMenuItemMessages.getString("menuDragMouseEventDN"), //$NON-NLS-1$
					// SHORTDESCRIPTION, "menuDragMouse exited event",
					}
				)
			},
			paramTypes
			),
			super.createMethodDescriptor(javax.swing.event.MenuDragMouseListener.class,
				"menuDragMouseReleased", //$NON-NLS-1$
				new Object[] {
				DISPLAYNAME, JMenuItemMessages.getString("menuDragMouseReleasedDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, JMenuItemMessages.getString("menuDragMouseReleasedSD"), //$NON-NLS-1$
				}, 
				new ParameterDescriptor[] {
					createParameterDescriptor("menuDragMouseEvent", new Object[] {//$NON-NLS-1$
					DISPLAYNAME, JMenuItemMessages.getString("menuDragMouseEventParmDN"), //$NON-NLS-1$
					// SHORTDESCRIPTION, "menuDragMouse released event",
					}
				)
			},
			paramTypes
			)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"menuDragMouse", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, JMenuItemMessages.getString("menuDragMouseEventsDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, JMenuItemMessages.getString("menuDragMouseEventsSD"), //$NON-NLS-1$		
					}, 
						aDescriptorList, javax.swing.event.MenuDragMouseListener.class,
						"addMenuDragMouseListener", "removeMenuDragMouseListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}

/**
 * Gets the keyevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor menuKeyEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { javax.swing.event.MenuKeyEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(javax.swing.event.MenuKeyListener.class,
				"menuKeyPressed", //$NON-NLS-1$
				new Object[] {
				DISPLAYNAME, JMenuItemMessages.getString("menuKeyPressedDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, JMenuItemMessages.getString("menuKeyPressedSD"), //$NON-NLS-1$
				}, 
				new ParameterDescriptor[] {
					createParameterDescriptor("menuKeyEvent", new Object[] {//$NON-NLS-1$
					DISPLAYNAME, JMenuItemMessages.getString("menuKeyEventDN"), //$NON-NLS-1$
					// SHORTDESCRIPTION, "menuKey pressed event",
					}
				)
			},
			paramTypes
			),
			super.createMethodDescriptor(javax.swing.event.MenuKeyListener.class,
				"menuKeyReleased", //$NON-NLS-1$
				new Object[] {
				DISPLAYNAME, JMenuItemMessages.getString("menuKeyReleasedDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, JMenuItemMessages.getString("menuKeyReleasedSD"), //$NON-NLS-1$
				}, 
				new ParameterDescriptor[] {
					createParameterDescriptor("menuKeyEvent", new Object[] {//$NON-NLS-1$
					DISPLAYNAME, JMenuItemMessages.getString("menuKeyEventParmDN"), //$NON-NLS-1$
					// SHORTDESCRIPTION, "menuKey released event",
					}
				)
			},
			paramTypes
			),
			super.createMethodDescriptor(javax.swing.event.MenuKeyListener.class,
				"menuKeyTyped", //$NON-NLS-1$
				new Object[] {
				DISPLAYNAME, JMenuItemMessages.getString("menuKeyTypedDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, JMenuItemMessages.getString("menuKeyTypedSD"), //$NON-NLS-1$
				}, 
				new ParameterDescriptor[] {
					createParameterDescriptor("menuKeyEvent", new Object[] {//$NON-NLS-1$
					DISPLAYNAME, JMenuItemMessages.getString("menuKeyEventParmDN"), //$NON-NLS-1$
					// SHORTDESCRIPTION, "menuKey typed event",
					}
				)
			},
			paramTypes	    		
			)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"menuKey", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, JMenuItemMessages.getString("menuKeyEventsDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, JMenuItemMessages.getString("menuKeyEventsSD"), //$NON-NLS-1$
					}, 
						aDescriptorList, javax.swing.event.MenuKeyListener.class,
						"addMenuKeyListener", "removeMenuKeyListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}

/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	try {
		EventSetDescriptor aDescriptorList[] = {
			menuDragMouseEventSetDescriptor(),
			menuKeyEventSetDescriptor(),
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
	return javax.swing.JMenuItem.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JMenuItemMessages.getString("JMenuItem.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JMenuItemMessages.getString("JMenuItem.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/menuit32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/menuit16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
	    return loadImage("menuit32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("menuit16.gif"); //$NON-NLS-1$
   	return super.getIcon(kind);
}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
		  	// getAccelerator()
			super.createMethodDescriptor(getBeanClass(),"getAccelerator",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuItemMessages.getString("getAccelerator().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuItemMessages.getString("getAccelerator().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isArmed()
			super.createMethodDescriptor(getBeanClass(),"isArmed", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuItemMessages.getString("isArmed().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuItemMessages.getString("isArmed().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// setAccelerator(KeyStroke)
			super.createMethodDescriptor(getBeanClass(),"setAccelerator",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuItemMessages.getString("setAccelerator(KeyStroke).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the accelerator key for the menu item",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("key", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuItemMessages.getString("setAccelerator(KeyStroke).keyStroke.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "KeyStroke",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.KeyStroke.class
	      		}		    		
		  	),
		  	// setArmed(boolean)
			super.createMethodDescriptor(getBeanClass(),"setArmed", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuItemMessages.getString("setArmed(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuItemMessages.getString("setArmed(boolean).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuItemMessages.getString("setArmed(boolean).aBool.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "TRUE to arm the menu item",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		boolean.class 
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
		PropertyDescriptor aDescriptorList[] = {
			// accelerator
			super.createPropertyDescriptor(getBeanClass(),"accelerator", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JMenuItemMessages.getString("accelerator.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuItemMessages.getString("accelerator.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE,
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// armed
		    super.createPropertyDescriptor(getBeanClass(),"armed", new Object[] {//$NON-NLS-1$
		   	    DISPLAYNAME, JMenuItemMessages.getString("armed.Name"),				 //$NON-NLS-1$
	      	    SHORTDESCRIPTION, JMenuItemMessages.getString("armed.Desc"), //$NON-NLS-1$
	      	    EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// enabled
			super.createPropertyDescriptor(getBeanClass(),"enabled", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JMenuItemMessages.getString("enabled.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuItemMessages.getString("enabled.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE,
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

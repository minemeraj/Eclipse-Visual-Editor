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
 *  $RCSfile: FrameBeanInfo.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.beans.ParameterDescriptor;

public class FrameBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle resframe = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.frame");  //$NON-NLS-1$

	
public Class getBeanClass() {
	return java.awt.Frame.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		/* Create and return the FrameBeanInfobean descriptor. */
		aDescriptor = new java.beans.BeanDescriptor(java.awt.Frame.class);
		aDescriptor.setDisplayName(resframe.getString("FrameDN")); //$NON-NLS-1$
		aDescriptor.setShortDescription(resframe.getString("FrameSD")); //$NON-NLS-1$
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
	return( new java.beans.EventSetDescriptor[0]);
}
	/**
	 * @return an icon of the specified kind for JButton
	 */
	public java.awt.Image getIcon(int kind) {
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("frame32.gif");//$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("frame16.gif");//$NON-NLS-1$
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
	      		// SHORTDESCRIPTION, "Create the frame peer",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// dispose()
			super.createMethodDescriptor(getBeanClass(),"dispose", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "dispose()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resframe.getString("disposeSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// getIconImage()
			super.createMethodDescriptor(getBeanClass(),"getIconImage", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getIconImage()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the icon image",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	super.createMethodDescriptor(getBeanClass(),"getMenuBar", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getMenuBar()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the menu bar",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getTitle()
			super.createMethodDescriptor(getBeanClass(),"getTitle", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getTitle()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the title",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// isResizable()
			super.createMethodDescriptor(getBeanClass(),"isResizable", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "isResizable()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Is the frame resizable",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// remove(MenuComponent)
			super.createMethodDescriptor(getBeanClass(), "remove", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "remove(MenuComponent)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "remove the menu bar",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("menubar", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resframe.getString("menubarParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Menu bar to be removed",
	      			}
	      		)	      		
	      	},
	      	new Class[] { 
	      		java.awt.MenuComponent.class
	      	}   		
		  	),
			// setCursor(int) - DEPRECATED
			// setIconImage(Image)
			super.createMethodDescriptor(getBeanClass(),"setIconImage", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setIconImage(Image)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resframe.getString("setIconSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resframe.getString("imageParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "image for icon",
	      			})
	      		},
	      		new Class[] {
	      			java.awt.Image.class 
	      		}	    		
		  	),
			// setMenuBar(MenuBar)
			super.createMethodDescriptor(getBeanClass(),"setMenuBar", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setMenuBar(MenuBar)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the frame's menu bar",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resframe.getString("menubarParmDN"),//$NON-NLS-1$
	      			// SHORTDESCRIPTION, "menu bar",
	      			})
	      		},
	      		new Class[] { 
	      			java.awt.MenuBar.class 
	      		}	    		
		  	),
			// setResizable(boolean)
			super.createMethodDescriptor(getBeanClass(),"setResizable", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setResizable(boolean)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set whether the dialog is resizable",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resframe.getString("resizableParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "True if frame can be resized",
	      			})
	      		},
	      		new Class[] { 
	      			boolean.class 
	      		}	    		
		  	),
			// setTitle(String)
			super.createMethodDescriptor(getBeanClass(),"setTitle", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setTitle(String)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the frame's title",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resframe.getString("titleParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "New title",
	      			})
	      		},
	      		new Class[] { 
	      			String.class 
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
			// iconImage
			super.createPropertyDescriptor(getBeanClass(),"iconImage", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resframe.getString("iconImageDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resframe.getString("iconImageSD"), //$NON-NLS-1$
	    		}
	    	),
	    	// menuBar
	    	super.createPropertyDescriptor(getBeanClass(),"menuBar", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resframe.getString("menuBarDN"),		    	 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resframe.getString("menuBarSD"), //$NON-NLS-1$
	      	//EXPERT, Boolean.TRUE,
			HIDDEN, Boolean.TRUE //UI support for AWT menu bar poor
	    		}
	    	),
			// resizable
			super.createPropertyDescriptor(getBeanClass(),"resizable", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resframe.getString("resizableDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resframe.getString("resizableSD"), //$NON-NLS-1$
	    		}
	    	),
			// title
			super.createPropertyDescriptor(getBeanClass(),"title", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resframe.getString("titleDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resframe.getString("titleSD"), //$NON-NLS-1$
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

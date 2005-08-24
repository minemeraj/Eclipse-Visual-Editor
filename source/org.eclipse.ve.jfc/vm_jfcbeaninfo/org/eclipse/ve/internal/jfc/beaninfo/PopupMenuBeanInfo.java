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
 *  $RCSfile: PopupMenuBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.beans.*;

public class PopupMenuBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle respopupmenu = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.popupmenu");  //$NON-NLS-1$
	
	
public Class getBeanClass() {
	return java.awt.PopupMenu.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		/* Create and return the PopupMenuBeanInfobean descriptor. */
		aDescriptor = new java.beans.BeanDescriptor(java.awt.PopupMenu.class);
		aDescriptor.setDisplayName(respopupmenu.getString("PopupMenuDN")); //$NON-NLS-1$
		aDescriptor.setShortDescription(respopupmenu.getString("PopupMenuSD")); //$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/popupm32.gif");//$NON-NLS-2$//$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/popupm16.gif");//$NON-NLS-2$//$NON-NLS-1$
	} catch (Throwable exception) {
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
	    return loadImage("popupm32.gif");//$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("popupm16.gif");//$NON-NLS-1$
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
	      		// SHORTDESCRIPTION, "Create the popup menu peer",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// show(Component,int,int)
			super.createMethodDescriptor(getBeanClass(),"show", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "show(Component,int,int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, respopupmenu.getString("show(Component,int,int)SD"), //$NON-NLS-1$
	    		PREFERRED, Boolean.TRUE
	      			},
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, respopupmenu.getString("originParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Component defining coordinate space",
	      			}
	      		),
	      		createParameterDescriptor("arg2", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, respopupmenu.getString("xParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "x coordinate",
	      			}
	      		),
	      		createParameterDescriptor("arg3", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, respopupmenu.getString("yParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "y coordinate",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		java.awt.Component.class, int.class, int.class 
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
	return( new	java.beans.PropertyDescriptor[0]);
}
}

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
 *  $RCSfile: CanvasBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:12 $ 
 */

import java.beans.*;
public class CanvasBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle rescanvas = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.canvas");  //$NON-NLS-1$

	
public Class getBeanClass() {
	return java.awt.Canvas.class;
}
public BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the CanvasBeanInfo bean descriptor. */
	aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               	DISPLAYNAME, rescanvas.getString("CanvasDN"), //$NON-NLS-1$
	        		SHORTDESCRIPTION, rescanvas.getString("CanvasSD") //$NON-NLS-1$
							}			    
						);
	aDescriptor.setValue("ICON_COLOR_32x32", "icons/canvas32.gif");//$NON-NLS-2$//$NON-NLS-1$
	aDescriptor.setValue("ICON_COLOR_16x16", "icons/canvas16.gif");//$NON-NLS-2$//$NON-NLS-1$
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
	    return loadImage("canvas32.gif");//$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("canvas16.gif");//$NON-NLS-1$
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
	      		// SHORTDESCRIPTION, "Create the peer of the canvas",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// paint(Graphics)
			super.createMethodDescriptor(getBeanClass(), "paint", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "paint(Graphics)", //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Paint this component",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescanvas.getString("graphicsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "the graphics context to use",
	      			}
	      		)	      		
	      	},
	      	new Class[] { 
	      		java.awt.Graphics.class
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
	return(new PropertyDescriptor[0]);
}
}

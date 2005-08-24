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
 *  $RCSfile: JWindowBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.beans.*;

public class JWindowBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JWindowMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jwindow");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JWindow.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JWindowMessages.getString("JWindow.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JWindowMessages.getString("JWindow.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/window32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/window16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
	return( new EventSetDescriptor[0]);
}
	/**
	 * @return an icon of the specified kind for JButton
	 */
	public java.awt.Image getIcon(int kind) {
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("window32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("window16.gif"); //$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
			// getAccessibleContext()
			super.createMethodDescriptor(getBeanClass(),"getAccessibleContext",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JWindowMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessible context",
	      		
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getContentPane()
			super.createMethodDescriptor(getBeanClass(),"getContentPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JWindowMessages.getString("getContentPane().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the client area component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getGlassPane()
			super.createMethodDescriptor(getBeanClass(),"getGlassPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JWindowMessages.getString("getGlassPane().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the glass pane component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getLayeredPane()
			super.createMethodDescriptor(getBeanClass(),"getLayeredPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JWindowMessages.getString("getLayeredPane().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the layered pane component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getRootPane()
			super.createMethodDescriptor(getBeanClass(),"getRootPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JWindowMessages.getString("getRootPane().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the rootPane component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// setContentPane(Container)
			super.createMethodDescriptor(getBeanClass(),"setContentPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JWindowMessages.getString("setContentPane(Container).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the client area pane of the window",
	      		HIDDEN, Boolean.TRUE,
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("contentPane", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JWindowMessages.getString("setContentPane(Container).contentPane.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Client area of window",
	      			}
	      		)},
	      		new Class[] {
	      			java.awt.Container.class
	      		}		    		
		  	),
			// setGlassPane(Component)
			super.createMethodDescriptor(getBeanClass(),"setGlassPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JWindowMessages.getString("setGlassPane(Component).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the glassPane component",
	      		HIDDEN, Boolean.TRUE,
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("glassPane", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JWindowMessages.getString("setGlassPane(Component).glassPane.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Glasspane component of applet",
	      			}
	      		)},
	      		new Class[] {
	      			java.awt.Component.class
	      		}		    		
		  	),
			// setLayeredPane(JLayeredPane)
			super.createMethodDescriptor(getBeanClass(),"setLayeredPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JWindowMessages.getString("setLayeredPane(JLayeredPane).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the layeredPane component",
	      		HIDDEN, Boolean.TRUE,
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("layeredPane", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JWindowMessages.getString("setLayeredPane(JLayeredPane).layeredPane.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "layeredPane component of applet",
	      			}
	      		)},
	      		new Class[] {
	      			javax.swing.JLayeredPane.class
	      		}		    		
		  	),
			// setLayout(LayoutManager)
				// set only the layout of contentPane 
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
			// contentPane
			super.createPropertyDescriptor(getBeanClass(),"contentPane", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JWindowMessages.getString("contentPane.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JWindowMessages.getString("contentPane.Desc"), //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
			// glassPane
			super.createPropertyDescriptor(getBeanClass(),"glassPane", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JWindowMessages.getString("glassPane.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JWindowMessages.getString("glassPane.Desc"), //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
			// layeredPane
			super.createPropertyDescriptor(getBeanClass(),"layeredPane", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JWindowMessages.getString("layeredPane.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JWindowMessages.getString("layeredPane.Desc"), //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
			// layout - discard from the property sheet
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JWindowMessages.getString("layout.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JWindowMessages.getString("layout.Desc"), //$NON-NLS-1$
	      		DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
			// rootPane
			super.createPropertyDescriptor(getBeanClass(),"rootPane", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JWindowMessages.getString("rootPane.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JWindowMessages.getString("rootPane.Desc"), //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
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

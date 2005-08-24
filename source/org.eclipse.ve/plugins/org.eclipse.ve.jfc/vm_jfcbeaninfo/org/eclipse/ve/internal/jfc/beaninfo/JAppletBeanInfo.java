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
 *  $RCSfile: JAppletBeanInfo.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.beans.*;

public class JAppletBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle JAppletMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.japplet");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JApplet.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JAppletMessages.getString("JApplet.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JAppletMessages.getString("JApplet.Desc") //$NON-NLS-1$
						}			    
				  	  );
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
	    return loadImage("applet32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("applet16.gif"); //$NON-NLS-1$
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
	   			DISPLAYNAME, JAppletMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessible context",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getContentPane()
			super.createMethodDescriptor(getBeanClass(),"getContentPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JAppletMessages.getString("getContentPane().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the contentPane component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getGlassPane()
			super.createMethodDescriptor(getBeanClass(),"getGlassPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JAppletMessages.getString("getGlassPane().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the glassPane component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getJMenuBar()
			super.createMethodDescriptor(getBeanClass(),"getJMenuBar",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JAppletMessages.getString("getJMenuBar().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the menu bar for the applet",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getLayeredPane()
			super.createMethodDescriptor(getBeanClass(),"getLayeredPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JAppletMessages.getString("getLayeredPane().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the layeredPane component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getRootPane()
			super.createMethodDescriptor(getBeanClass(),"getRootPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JAppletMessages.getString("getRootPane().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the rootPane component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// setContentPane(Container)
			super.createMethodDescriptor(getBeanClass(),"setContentPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JAppletMessages.getString("setContentPane(Container).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the client area pane",
	      		HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("contentPane", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JAppletMessages.getString("setContentPane(Container).contentPane.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Client area of applet",
	      			}
	      		)},
	      		new Class[] {
	      			java.awt.Container.class
	      		}		    		
		  	),
			// setGlassPane(Component)
			super.createMethodDescriptor(getBeanClass(),"setGlassPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JAppletMessages.getString("setGlassPane(Component).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the glassPane component",
	      		HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("glassPane", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JAppletMessages.getString("setGlassPane(Component).glassPane.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Glasspane component of applet",
	      			}
	      		)},
	      		new Class[] {
	      			java.awt.Component.class
	      		}		    		
		  	),
			// setJMenuBar(JMenuBar)
			super.createMethodDescriptor(getBeanClass(),"setJMenuBar",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JAppletMessages.getString("setJMenuBar(JMenuBar).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the menu bar",
	      		//HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("menubar", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JAppletMessages.getString("setJMenuBar(JMenuBar).menubar.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Menu bar",
	      			}
	      		)},
	      		new Class[] {
	      			javax.swing.JMenuBar.class
	      		}		    		
		  	),
			// setLayeredPane(JLayeredPane)
			super.createMethodDescriptor(getBeanClass(),"setLayeredPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JAppletMessages.getString("setLayeredPane(JLayeredPane).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the layeredPane component",
	      		HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("layeredPane", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JAppletMessages.getString("setLayeredPane(JLayeredPane).layeredPane.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "layeredPane component of applet",
	      			}
	      		)},
	      		new Class[] {
	      			javax.swing.JLayeredPane.class
	      		}		    		
		  	),
			// setLayout(LayoutManager)
				// set only the layout of contentPane 
			// update(Graphics)
			super.createMethodDescriptor(getBeanClass(), "update",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JAppletMessages.getString("update(Graphics).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "update this component",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("graphics", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JAppletMessages.getString("update(Graphics).Graphics.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "the graphics context to use",
	      			}
	      		)},
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
	try {
		PropertyDescriptor aDescriptorList[] = {
			// accessibleContext
			super.createPropertyDescriptor(getBeanClass(),"accessibleContext", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JAppletMessages.getString("accessibleContext.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JAppletMessages.getString("accessibleContext.Desc"), //$NON-NLS-1$
	    		}
	    	),
			// contentPane
			super.createPropertyDescriptor(getBeanClass(),"contentPane", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JAppletMessages.getString("contentPane.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JAppletMessages.getString("contentPane.Desc"), //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
			// glassPane
			super.createPropertyDescriptor(getBeanClass(),"glassPane", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JAppletMessages.getString("glassPane.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JAppletMessages.getString("glassPane.Desc"), //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
			// JMenuBar
			super.createPropertyDescriptor(getBeanClass(),"JMenuBar", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JAppletMessages.getString("JMenuBar.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JAppletMessages.getString("JMenuBar.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE	      	
	    		}
	    	),
			// layeredPane
			super.createPropertyDescriptor(getBeanClass(),"layeredPane", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JAppletMessages.getString("layeredPane.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JAppletMessages.getString("layeredPane.Desc"), //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
			// layout - discard from the property sheet
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JAppletMessages.getString("layout.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JAppletMessages.getString("layout.Desc"), //$NON-NLS-1$
	      		DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
			// rootPane
			super.createPropertyDescriptor(getBeanClass(),"rootPane", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JAppletMessages.getString("rootPane.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JAppletMessages.getString("rootPane.Desc"), //$NON-NLS-1$
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

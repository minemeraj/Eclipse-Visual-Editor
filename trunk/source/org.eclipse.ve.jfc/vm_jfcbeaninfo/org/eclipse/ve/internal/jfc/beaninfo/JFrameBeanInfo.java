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
 *  $RCSfile: JFrameBeanInfo.java,v $
 *  $Revision: 1.6 $  $Date: 2005-10-06 15:18:44 $ 
 */

import java.beans.*;

import org.eclipse.jem.beaninfo.vm.IBaseBeanInfoConstants;

public class JFrameBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle JFrameMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jframe");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JFrame.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JFrameMessages.getString("JFrame.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JFrameMessages.getString("JFrame.Desc"), //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/frame32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/frame16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
	    return loadImage("frame32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("frame16.gif"); //$NON-NLS-1$
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
	   			DISPLAYNAME, JFrameMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessible context",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getContentPane()
			super.createMethodDescriptor(getBeanClass(),"getContentPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JFrameMessages.getString("getContentPane().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the contentPane component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getDefaultCloseOperation
		  	super.createMethodDescriptor(getBeanClass(),"getDefaultCloseOperation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JFrameMessages.getString("getDefaultCloseOperation().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JFrameMessages.getString("getDefaultCloseOperation().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getGlassPane()
			super.createMethodDescriptor(getBeanClass(),"getGlassPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JFrameMessages.getString("getGlassPane().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the glassPane component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getJMenuBar()
			super.createMethodDescriptor(getBeanClass(),"getJMenuBar",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JFrameMessages.getString("getJMenuBar().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the menu bar for the frame",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getLayeredPane()
			super.createMethodDescriptor(getBeanClass(),"getLayeredPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JFrameMessages.getString("getLayeredPane().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the layeredPane component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getRootPane()
			super.createMethodDescriptor(getBeanClass(),"getRootPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JFrameMessages.getString("getRootPane().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the rootPane component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// setContentPane(Container)
			super.createMethodDescriptor(getBeanClass(),"setContentPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JFrameMessages.getString("setContentPane(Container).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the client area pane",
	      		HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("contentPane", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JFrameMessages.getString("setContentPane(Container).contentPane.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Client area of frame",
	      			}
	      		)},
	      		new Class[] {
	      			java.awt.Container.class
	      		}		    		
		  	),
		  	// setDefaultCloseOperation(int)
			super.createMethodDescriptor(getBeanClass(),"setDefaultCloseOperation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JFrameMessages.getString("setDefaultCloseOperation(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JFrameMessages.getString("setDefaultCloseOperation(int).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("operation", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JFrameMessages.getString("setDefaultCloseOperation(int).operation.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Do nothing, hide or dispose",
	      			}
	      		)},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
			// setGlassPane(Component)
			super.createMethodDescriptor(getBeanClass(),"setGlassPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JFrameMessages.getString("setGlassPane(Component).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the glassPane component",
	      		HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("glassPane", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JFrameMessages.getString("setGlassPane(Component).glassPane.Name"), //$NON-NLS-1$
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
	   			DISPLAYNAME, JFrameMessages.getString("setJMenuBar(JMenuBar).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the menu bar",
	      		HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("menubar", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JFrameMessages.getString("setJMenuBar(JMenuBar).menubar.Name"), //$NON-NLS-1$
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
	   			DISPLAYNAME, JFrameMessages.getString("setLayeredPane(JLayeredPane).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the layeredPane component",
	      		HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("layeredPane", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JFrameMessages.getString("setLayeredPane(JLayeredPane).layeredPane.Name"), //$NON-NLS-1$
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
	   			DISPLAYNAME, JFrameMessages.getString("update(Graphics).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "update this component",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("graphics", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JFrameMessages.getString("update(Graphics).Graphics.Name"), //$NON-NLS-1$
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
			// contentPane
			super.createPropertyDescriptor(getBeanClass(),"contentPane", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JFrameMessages.getString("contentPane.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JFrameMessages.getString("contentPane.Desc"), //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
	    	// defaultCloseOperation
	    	super.createPropertyDescriptor(getBeanClass(),"defaultCloseOperation", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JFrameMessages.getString("defaultCloseOperation.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JFrameMessages.getString("defaultCloseOperation.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE,
	      	IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      			JFrameMessages.getString("defaultCloseOperation.DO_NOTHING"), new Integer(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE), //$NON-NLS-1$
	      				"javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE", //$NON-NLS-1$
	            	JFrameMessages.getString("defaultCloseOperation.HIDE"), new Integer(javax.swing.WindowConstants.HIDE_ON_CLOSE), //$NON-NLS-1$
	            		"javax.swing.WindowConstants.HIDE_ON_CLOSE", //$NON-NLS-1$
	           		JFrameMessages.getString("defaultCloseOperation.DISPOSE"),new Integer(javax.swing.WindowConstants.DISPOSE_ON_CLOSE), //$NON-NLS-1$
	           			"javax.swing.WindowConstants.DISPOSE_ON_CLOSE", //$NON-NLS-1$
	           		JFrameMessages.getString("defaultCloseOperation.EXIT"),new Integer(javax.swing.JFrame.EXIT_ON_CLOSE), //$NON-NLS-1$
	           			"javax.swing.JFrame.EXIT_ON_CLOSE" } //$NON-NLS-1$
	    		}
	    	),
			// glassPane
			super.createPropertyDescriptor(getBeanClass(),"glassPane", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JFrameMessages.getString("glassPane.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JFrameMessages.getString("glassPane.Desc"), //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
			// JMenuBar
			super.createPropertyDescriptor(getBeanClass(),"JMenuBar", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JFrameMessages.getString("JMenuBar.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JFrameMessages.getString("JMenuBar.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE	      	
	    		}
	    	),
			// layeredPane
			super.createPropertyDescriptor(getBeanClass(),"layeredPane", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JFrameMessages.getString("layeredPane.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JFrameMessages.getString("layeredPane.Desc"), //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
			// layout - discard from the property sheet
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JFrameMessages.getString("layout.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JFrameMessages.getString("layout.Desc"), //$NON-NLS-1$
	      		DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
			// menuBar - cannot do anything with menubar here
	    	super.createPropertyDescriptor(getBeanClass(),"menuBar", new Object[] {//$NON-NLS-1$
	    	DISPLAYNAME, JFrameMessages.getString("menuBar.Name"), //$NON-NLS-1$
	    	SHORTDESCRIPTION, JFrameMessages.getString("menuBar.Desc"), //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE,
	    		}
	    	),

			// rootPane
			super.createPropertyDescriptor(getBeanClass(),"rootPane", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JFrameMessages.getString("rootPane.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JFrameMessages.getString("rootPane.Desc"), //$NON-NLS-1$
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

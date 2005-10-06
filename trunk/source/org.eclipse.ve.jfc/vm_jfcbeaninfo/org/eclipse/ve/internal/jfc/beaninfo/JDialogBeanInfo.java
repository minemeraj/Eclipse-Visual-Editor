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
 *  $RCSfile: JDialogBeanInfo.java,v $
 *  $Revision: 1.6 $  $Date: 2005-10-06 15:18:44 $ 
 */

import java.beans.*;

import org.eclipse.jem.beaninfo.vm.IBaseBeanInfoConstants;

public class JDialogBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JDialogMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jdialog");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JDialog.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JDialogMessages.getString("JDialog.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JDialogMessages.getString("JDialog.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/dialog32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/dialog16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
	    return loadImage("dialog32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("dialog16.gif"); //$NON-NLS-1$
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
	   			DISPLAYNAME, JDialogMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessible context",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getContentPane()
			super.createMethodDescriptor(getBeanClass(),"getContentPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JDialogMessages.getString("getContentPane().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the contentPane component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getDefaultCloseOperation
		  	super.createMethodDescriptor(getBeanClass(),"getDefaultCloseOperation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JDialogMessages.getString("getDefaultCloseOperation().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JDialogMessages.getString("getDefaultCloseOperation().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getGlassPane()
			super.createMethodDescriptor(getBeanClass(),"getGlassPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JDialogMessages.getString("getGlassPane().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the glassPane component",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getJMenuBar()
			super.createMethodDescriptor(getBeanClass(),"getJMenuBar",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JDialogMessages.getString("getJMenuBar().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the menu bar for the frame",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getLayeredPane()
			super.createMethodDescriptor(getBeanClass(),"getLayeredPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JDialogMessages.getString("getLayeredPane().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the layeredPane component",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getRootPane()
			super.createMethodDescriptor(getBeanClass(),"getRootPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JDialogMessages.getString("getRootPane().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the rootPane component",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// setContentPane(Container)
			super.createMethodDescriptor(getBeanClass(),"setContentPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JDialogMessages.getString("setContentPane(Container).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the client area pane",
	      		HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("contentPane", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JDialogMessages.getString("setContentPane(Container).contentPane.Name"), //$NON-NLS-1$
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
	   			DISPLAYNAME, JDialogMessages.getString("setDefaultCloseOperation(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JDialogMessages.getString("setDefaultCloseOperation(int).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("operation", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JDialogMessages.getString("setDefaultCloseOperation(int).operation.Name"), //$NON-NLS-1$
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
	   			DISPLAYNAME, JDialogMessages.getString("setGlassPane(Component).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the glassPane component",
	      		HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("glassPane", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JDialogMessages.getString("setGlassPane(Component).glassPane.Name"), //$NON-NLS-1$
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
	   			DISPLAYNAME, JDialogMessages.getString("setJMenuBar(JMenuBar).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the menu bar",
	      		HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("menubar", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JDialogMessages.getString("setJMenuBar(JMenuBar).menubar.Name"), //$NON-NLS-1$
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
	   			DISPLAYNAME, JDialogMessages.getString("setLayeredPane(JLayeredPane).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the layeredPane component",
	      		HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("layeredPane", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JDialogMessages.getString("setLayeredPane(JLayeredPane).layeredPane.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "layeredPane component of applet",
	      			}
	      		)},
	      		new Class[] {
	      			javax.swing.JLayeredPane.class
	      		}		    		
		  	),
			// setLayout(LayoutManager)
				// set only the layout of contentPane
			// setLocationRelativeTo(Component)
			super.createMethodDescriptor(getBeanClass(),"setLocationRelativeTo",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JDialogMessages.getString("setLocationRelativeTo(Component).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JDialogMessages.getString("setLocationRelativeTo(Component).Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("c", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JDialogMessages.getString("setLocationRelativeTo(Component).aComponent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Reference component",
	      			}
	      		)},
	      		new Class[] {
	      			java.awt.Component.class
	      		}		    		
		  	), 
			// update(Graphics)
			super.createMethodDescriptor(getBeanClass(), "update",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JDialogMessages.getString("update(Graphics).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Paint this component",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("graphics", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JDialogMessages.getString("update(Graphics).Graphics.Name"), //$NON-NLS-1$
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
			DISPLAYNAME, JDialogMessages.getString("contentPane.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JDialogMessages.getString("contentPane.Desc"), //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
	    	// defaultCloseOperation
	    	super.createPropertyDescriptor(getBeanClass(),"defaultCloseOperation", new Object[] { //$NON-NLS-1$
	    	DISPLAYNAME, JDialogMessages.getString("defaultCloseOperation.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JDialogMessages.getString("defaultCloseOperation.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE,
	      	IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      			JDialogMessages.getString("CloseOperation.DO_NOTHING_ON_CLOSE"), new Integer(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE), //$NON-NLS-1$
	      				"javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE", //$NON-NLS-1$
	            	JDialogMessages.getString("CloseOperation.HIDE_ON_CLOSE"), new Integer(javax.swing.WindowConstants.HIDE_ON_CLOSE), //$NON-NLS-1$
	            		"javax.swing.WindowConstants.HIDE_ON_CLOSE", //$NON-NLS-1$
	           		JDialogMessages.getString("CloseOperation.DISPOSE_ON_CLOSE"),new Integer(javax.swing.WindowConstants.DISPOSE_ON_CLOSE), //$NON-NLS-1$
	           			"javax.swing.WindowConstants.DISPOSE_ON_CLOSE" } //$NON-NLS-1$
	    		}
	    	),
			// glassPane
			super.createPropertyDescriptor(getBeanClass(),"glassPane", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JDialogMessages.getString("glassPane.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JDialogMessages.getString("glassPane.Desc"), //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
			// JMenuBar
			super.createPropertyDescriptor(getBeanClass(),"JMenuBar", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JDialogMessages.getString("JMenuBar.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JDialogMessages.getString("JMenuBar.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE	      	
	    		}
	    	),
	    	// modal
			super.createPropertyDescriptor(getBeanClass(),"modal", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JDialogMessages.getString("modal.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JDialogMessages.getString("modal.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE	      	
	    		}
	    	),
			// layeredPane
			super.createPropertyDescriptor(getBeanClass(),"layeredPane", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JDialogMessages.getString("layeredPane.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JDialogMessages.getString("layeredPane.Desc"), //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
			// layout - discard from the property sheet
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JDialogMessages.getString("layout.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JDialogMessages.getString("layout.Desc"), //$NON-NLS-1$
	      		DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
			// rootPane
			super.createPropertyDescriptor(getBeanClass(),"rootPane", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JDialogMessages.getString("rootPane.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JDialogMessages.getString("rootPane.Desc"), //$NON-NLS-1$
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

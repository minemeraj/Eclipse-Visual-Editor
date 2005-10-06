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
 *  $RCSfile: JDesktopPaneBeanInfo.java,v $
 *  $Revision: 1.7 $  $Date: 2005-10-06 15:18:44 $ 
 */

import java.beans.*;

import org.eclipse.jem.beaninfo.vm.IBaseBeanInfoConstants;

public class JDesktopPaneBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JDesktopMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jdesktoppane");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JDesktopPane.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JDesktopMessages.getString("JDesktopPane.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JDesktopMessages.getString("JDesktopPane.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/jdtpn32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/jdtpn16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
	    return loadImage("jdtpn32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("jdtpn16.gif"); //$NON-NLS-1$
   	return super.getIcon(kind);
}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
		  	// getAllFrames()
			super.createMethodDescriptor(getBeanClass(),"getAllFrames",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JDesktopMessages.getString("getAllFrames().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get an array of internal frames",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getDesktopManager()
			super.createMethodDescriptor(getBeanClass(),"getDesktopManager",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JDesktopMessages.getString("getDesktopManager().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the desktop manager that handles UI actions",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getDragMode()
			super.createMethodDescriptor(getBeanClass(),"getDragMode",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JDesktopMessages.getString("getDragMode().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JDesktopMessages.getString("getDragMode().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getSelectedFrame()
			super.createMethodDescriptor(getBeanClass(),"getSelectedFrame",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JDesktopMessages.getString("getSelectedFrame().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the currently active JInternalFrame in this JDesktopPane",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getUI()
			super.createMethodDescriptor(getBeanClass(),"getUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JDesktopMessages.getString("getUI().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the L&F object that renders this component",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setDesktopManager(DesktopManager)
			super.createMethodDescriptor(getBeanClass(),"setDesktopManager",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JDesktopMessages.getString("setDesktopManager(DesktopManager).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JDesktopMessages.getString("setDesktopManager(DesktopManager).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("d", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JDesktopMessages.getString("setDesktopManager(DesktopManager).manager.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Desktop manager",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.DesktopManager.class
	      		}		    		
		  	),
		  	// setDragMode(int)
			super.createMethodDescriptor(getBeanClass(),"setDragMode",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JDesktopMessages.getString("setDragMode(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the dragging style",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("dragMode", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JDesktopMessages.getString("setDragMode(int).dragMode.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "LIVE_DRAG_MODE, OUTLINE_DRAG_MODE",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setSelectedFrame(JInternalFrame)
			super.createMethodDescriptor(getBeanClass(),"setSelectedFrame",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JDesktopMessages.getString("setSelectedFrame(JInternalFrame).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JDesktopMessages.getString("setSelectedFrame(JInternalFrame).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("f", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JDesktopMessages.getString("setDesktopManager(JInternalFrame).frame.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Selected frame",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.JInternalFrame.class
	      		}		    		
		  	),
		  	// setUI(DesktopPaneUI)
			super.createMethodDescriptor(getBeanClass(),"setUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JDesktopMessages.getString("setUI(DesktopPaneUI).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JDesktopMessages.getString("setUI(DesktopPaneUI).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("ui", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JDesktopMessages.getString("setUI(DesktopPaneUI).ui.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "L&F",
	      				})
	      		},
	      		new Class[] {
					javax.swing.plaf.DesktopPaneUI.class
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
			// layout - discard from the property sheet
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JDesktopMessages.getString("layout.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JDesktopMessages.getString("layout.Desc"), //$NON-NLS-1$
	      		DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
	    	// dragMode
			super.createPropertyDescriptor(getBeanClass(),"dragMode", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JDesktopMessages.getString("dragMode.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JDesktopMessages.getString("dragMode.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE,
	      		PREFERRED, Boolean.TRUE,
	      		IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      			JDesktopMessages.getString("dragMode.LIVE_DRAG_MODE"), new Integer(javax.swing.JDesktopPane.LIVE_DRAG_MODE), //$NON-NLS-1$
	      				"javax.swing.JDesktopPane.LIVE_DRAG_MODE", //$NON-NLS-1$
	      			JDesktopMessages.getString("dragMode.OUTLINE_DRAG_MODE"), new Integer(javax.swing.JDesktopPane.OUTLINE_DRAG_MODE), //$NON-NLS-1$
	      				"javax.swing.JDesktopPane.OUTLINE_DRAG_MODE"} //$NON-NLS-1$
	    		}
	    	),
			super.createPropertyDescriptor(getBeanClass(),"desktopManager", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JDesktopMessages.getString("desktopManager.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JDesktopMessages.getString("desktopManager.Desc"), //$NON-NLS-1$
      		    EXPERT, Boolean.TRUE,
	      		//HIDDEN, Boolean.TRUE
	    		}
	    	),
			//TODO: hidden as code sets selected frame prior to frames being added
			super.createPropertyDescriptor(getBeanClass(),"selectedFrame", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JDesktopMessages.getString("selectedFrame.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JDesktopMessages.getString("selectedFrame.Desc"), //$NON-NLS-1$
      		    //EXPERT, Boolean.TRUE,
	      		HIDDEN, Boolean.TRUE
	    		}
	    	),
			super.createPropertyDescriptor(getBeanClass(),"UI", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JDesktopMessages.getString("UI.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JDesktopMessages.getString("UI.Desc"), //$NON-NLS-1$
      		    EXPERT, Boolean.TRUE,
				BOUND, Boolean.TRUE
	      		//HIDDEN, Boolean.TRUE
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

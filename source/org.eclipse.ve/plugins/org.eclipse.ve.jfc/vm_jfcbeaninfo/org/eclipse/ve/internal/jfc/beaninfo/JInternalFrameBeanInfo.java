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
 *  $RCSfile: JInternalFrameBeanInfo.java,v $
 *  $Revision: 1.7 $  $Date: 2005-10-06 15:18:44 $ 
 */

import java.beans.*;

import org.eclipse.jem.beaninfo.vm.BaseBeanInfo;
import org.eclipse.jem.beaninfo.vm.IBaseBeanInfoConstants;

public class JInternalFrameBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JInternalFrameMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jinternalframe");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JInternalFrame.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JInternalFrameMessages.getString("JInternalFrame.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JInternalFrameMessages.getString("JInternalFrame.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/jifram32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/jifram16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
	} catch (Throwable exception) {
		handleException(exception);
	};
	return aDescriptor;
}

/**
 * Gets the InternalFrameEvent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor internalFrameEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { javax.swing.event.InternalFrameEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(javax.swing.event.InternalFrameListener.class,
				"internalFrameActivated",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("internalFrameActivatedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("internalFrameActivatedSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("internalFrameEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("internalFrameEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "internalFrame activated event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(javax.swing.event.InternalFrameListener.class,
				"internalFrameClosed", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("internalFrameClosedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("internalFrameClosedSD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("internalFrameEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("internalFrameEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "internalFrame closed event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(javax.swing.event.InternalFrameListener.class,
				"internalFrameClosing", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("internalFrameClosingDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("internalFrameClosingSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("internalFrameEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("internalFrameEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "internalFrame closing event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(javax.swing.event.InternalFrameListener.class,
				"internalFrameDeactivated", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("internalFrameDeactivatedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("internalFrameDeactivatedSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("internalFrameEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("internalFrameEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "internalFrame deactivated event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(javax.swing.event.InternalFrameListener.class,
				"internalFrameDeiconified", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("internalFrameDeiconifiedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("internalFrameDeiconifiedSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("internalFrameEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("internalFrameEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "internalFrame deiconified event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(javax.swing.event.InternalFrameListener.class,
				"internalFrameIconified", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("internalFrameIconifiedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("internalFrameIconifiedSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("internalFrameEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("internalFrameEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "internalFrame iconified event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(javax.swing.event.InternalFrameListener.class,
				"internalFrameOpened", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("internalFrameOpenedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("internalFrameOpenedSD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("internalFrameEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("internalFrameEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "internalFrame opened event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"internalFrame", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, JInternalFrameMessages.getString("internalFrameEventsDN"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JInternalFrameMessages.getString("internalFrameEventsSD"), //$NON-NLS-1$
	      				INDEFAULTEVENTSET, Boolean.TRUE,
	      				PREFERRED, Boolean.TRUE,
	      				BaseBeanInfo.EVENTADAPTERCLASS, "javax.swing.event.InternalFrameAdapter"			 //$NON-NLS-1$
	      			}, 
						aDescriptorList, javax.swing.event.InternalFrameListener.class,
						"addInternalFrameListener", "removeInternalFrameListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}

/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	try {
		EventSetDescriptor aDescriptorList[] = {
			internalFrameEventSetDescriptor()
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
	    return loadImage("jifram32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("jifram16.gif"); //$NON-NLS-1$
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
	   			DISPLAYNAME, JInternalFrameMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessible context",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getBackground()
			super.createMethodDescriptor(getBeanClass(),"getBackground",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("getBackground().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the background color",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getContentPane()
			super.createMethodDescriptor(getBeanClass(),"getContentPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("getContentPane().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the contentPane component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getDefaultCloseOperation
		  	super.createMethodDescriptor(getBeanClass(),"getDefaultCloseOperation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("getDefaultCloseOperation().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("getDefaultCloseOperation().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getDesktopIcon()
			super.createMethodDescriptor(getBeanClass(),"getDesktopIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("getDesktopIcon().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the desktop icon",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getDesktopPane()
			super.createMethodDescriptor(getBeanClass(),"getDesktopPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("getDesktopPane().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the desktop pane",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getForeground()
			super.createMethodDescriptor(getBeanClass(),"getForeground",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("getForeground().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the foreground color",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getFrameIcon()
			super.createMethodDescriptor(getBeanClass(),"getFrameIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("getFrameIcon().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("getFrameIcon().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getGlassPane()
			super.createMethodDescriptor(getBeanClass(),"getGlassPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("getGlassPane().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the glassPane component",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getMenuBar()
			super.createMethodDescriptor(getBeanClass(),"getMenuBar",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("getMenuBar().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the menu bar for the frame",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getNormalBounds()
			super.createMethodDescriptor(getBeanClass(),"getNormalBounds",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("getNormalBounds().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the bounds that the JInternalFrame would be restored to",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getLayer()
			super.createMethodDescriptor(getBeanClass(),"getLayer",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("getLayer().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the layer attribute",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getLayeredPane()
			super.createMethodDescriptor(getBeanClass(),"getLayeredPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("getLayeredPane().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the layeredPane component",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getRootPane()
			super.createMethodDescriptor(getBeanClass(),"getRootPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("getRootPane().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the rootPane component",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getTitle()
			super.createMethodDescriptor(getBeanClass(),"getTitle",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("getTitle().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the title",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getUI()
			super.createMethodDescriptor(getBeanClass(),"getUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("getUI().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the UI subclass",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getUIClassID()
			super.createMethodDescriptor(getBeanClass(),"getUIClassID",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("getUIClassID().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the UI subclass name",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getWarningString()
			super.createMethodDescriptor(getBeanClass(),"getWarningString",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("getWarningString().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the warning string",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isClosable()
			super.createMethodDescriptor(getBeanClass(),"isClosable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("isClosable().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("isClosable().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isClosed()
			super.createMethodDescriptor(getBeanClass(),"isClosed",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("isClosed().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("isClosed().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isIcon()
			super.createMethodDescriptor(getBeanClass(),"isIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("isIcon().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("isIcon().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isIconifiable()
			super.createMethodDescriptor(getBeanClass(),"isIconifiable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("isIconifiable().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("isIconifiable().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isMaximizable()
			super.createMethodDescriptor(getBeanClass(),"isMaximizable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("isMaximizable().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Can the frame be maximized",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isMaximum()
			super.createMethodDescriptor(getBeanClass(),"isMaximum",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("isMaximum().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("isMaximum().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isResizable()
			super.createMethodDescriptor(getBeanClass(),"isResizable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("isResizable().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Is the frame resizable",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isSelected()
			super.createMethodDescriptor(getBeanClass(),"isSelected",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("isSelected().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Is the frame currently active",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// moveToBack()
			super.createMethodDescriptor(getBeanClass(),"moveToBack",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("moveToBack().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Move the frame to the back",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// moveToFront()
			super.createMethodDescriptor(getBeanClass(),"moveToFront",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("moveToFront().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Move the frame to the front",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// reshape(int,int,int,int)
		  	super.createMethodDescriptor(getBeanClass(),"reshape",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("reshape(int,int,int,int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Move and resize the component",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("x", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("reshape(int,int,int,int).x.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "origin x",
	      			}),
	    			createParameterDescriptor("y", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("reshape(int,int,int,int).y.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "origin y",
	      			}),
	      			createParameterDescriptor("w", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("reshape(int,int,int,int).width.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "width",
	      			}),
	      			createParameterDescriptor("h", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("reshape(int,int,int,int).height.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "height",
	      			})
	      		},
	      		new Class[] { 
	      			int.class, int.class, int.class, int.class 
	      		}	    		
		  	),
		  	// setBackground(Color)
			super.createMethodDescriptor(getBeanClass(),"setBackground",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("setBackground(Color).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the background color",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("c", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setBackground(Color).color.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Color to set to",
	      			}
	      		)},
	      		new Class[] {
	      			java.awt.Color.class
	      		}		    		
		  	),
		  	// setClosable(boolean)
			super.createMethodDescriptor(getBeanClass(),"setClosable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("setClosable(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("setClosable(boolean).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setClosable(boolean).aBoolean.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "TRUE for closable",
	      			}
	      		)},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setClosed(boolean)
			super.createMethodDescriptor(getBeanClass(),"setClosed",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("setClosed(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("setClosed(boolean).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setClosed(boolean).aBoolean.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "TRUE to close",
	      			}
	      		)},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),		  	
			// setContentPane(Container)
			super.createMethodDescriptor(getBeanClass(),"setContentPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("setContentPane(Container).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the client area pane",
	      		HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("contentPane", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setContentPane(Container).contentPane.Name"), //$NON-NLS-1$
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
	   			DISPLAYNAME, JInternalFrameMessages.getString("setDefaultCloseOperation(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("setDefaultCloseOperation(int).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("operation", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setDefaultCloseOperation(int).operation.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Do nothing, hide or dispose",
	      			}
	      		)},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setDesktopIcon(JDesktopIcon)
			super.createMethodDescriptor(getBeanClass(),"setDesktopIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("setDesktopIcon(JDesktopIcon).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the desktop icon"
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("icon", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setDesktopIcon(JDesktopIcon).icon.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Icon to set to",
	      			}
	      		)},
	      		new Class[] {
	      			javax.swing.JInternalFrame.JDesktopIcon.class
	      		}		    		
		  	),
		  	// setForeground(Color)
			super.createMethodDescriptor(getBeanClass(),"setForeground",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("setForeground(Color).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the foreground color",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("c", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setForeground(Color).color.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Color to set to",
	      			}
	      		)},
	      		new Class[] {
	      			java.awt.Color.class
	      		}		    		
		  	),
		  	// setFrameIcon(Icon)
			super.createMethodDescriptor(getBeanClass(),"setFrameIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("setFrameIcon(Icon).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the frame icon"
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("icon", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setFrameIcon(Icon).icon.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Icon to set to",
	      			}
	      		)},
	      		new Class[] {
	      			javax.swing.Icon.class
	      		}		    		
		  	),
		  	// setIcon(boolean)
			super.createMethodDescriptor(getBeanClass(),"setIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("setIcon(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("setIcon(boolean).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setIcon(boolean).boolean.Name"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, JInternalFrameMessages.getString("setIcon(boolean).boolean.Desc"), //$NON-NLS-1$
	      			}
	      		)},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setIconifiable(boolean)
			super.createMethodDescriptor(getBeanClass(),"setIconifiable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("setIconifiable(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("setIconifiable(boolean).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setIconifiable(boolean).boolean.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "TRUE to iconify",
	      			}
	      		)},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setMaximizable(boolean)
			super.createMethodDescriptor(getBeanClass(),"setMaximizable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("setMaximizable(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set whether the frame can be maximized"
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setMaximizable(boolean).boolean.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "TRUE to make maximizable",
	      			}
	      		)},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),		  	
			// setGlassPane(Component)
			super.createMethodDescriptor(getBeanClass(),"setGlassPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("setGlassPane(Component).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the glassPane component",
	      		HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("glassPane", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setGlassPane(Component).glassPane.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Glasspane component of applet",
	      			}
	      		)},
	      		new Class[] {
	      			java.awt.Component.class
	      		}		    		
		  	),
		  	// setMaximum(boolean)
			super.createMethodDescriptor(getBeanClass(),"setMaximum",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("setMaximum(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("setMaximum(boolean).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setMaximum(boolean).boolean.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "TRUE to maximizable",
	      			}
	      		)},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
			// setMenuBar(JMenuBar)
			super.createMethodDescriptor(getBeanClass(),"setMenuBar",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("setMenuBar(JMenuBar).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the menu bar",
	      		HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("menubar", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setMenuBar(JMenuBar).menubar.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Menu bar",
	      			}
	      		)},
	      		new Class[] {
	      			javax.swing.JMenuBar.class
	      		}		    		
		  	),
			// setNormalBounds(Rectangle)
			super.createMethodDescriptor(getBeanClass(),"setNormalBounds",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("setNormalBounds(Rectangle).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the bounds",
	      		HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("r", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setNormalBounds(Rectangle).rectangle.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Bounds rectangle",
	      			}
	      		)},
	      		new Class[] {
	      			java.awt.Rectangle.class
	      		}
		  	),
		  	// setLayer(Integer)
			super.createMethodDescriptor(getBeanClass(),"setLayer",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("setLayer(Integer).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the layer attribute",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("layer", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setLayer(Integer).layer.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Which layer is used",
	      			}
	      		)},
	      		new Class[] {
	      			Integer.class
	      		}		    		
		  	),
			// setLayeredPane(JLayeredPane)
			super.createMethodDescriptor(getBeanClass(),"setLayeredPane",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("setLayeredPane(JLayeredPane).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the layeredPane component",
	      		HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("layeredPane", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setLayeredPane(JLayeredPane).layeredPane.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "layeredPane component of applet",
	      			}
	      		)},
	      		new Class[] {
	      			javax.swing.JLayeredPane.class
	      		}		    		
		  	),
			// setLayout(LayoutManager)
				// set only the layout of contentPane
			// setResizable(boolean)
			super.createMethodDescriptor(getBeanClass(),"setResizable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("setResizable(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set whether the frame can be resized"
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setResizable(boolean).boolean.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "TRUE to make resizable",
	      			}
	      		)},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setSelected(boolean)
			super.createMethodDescriptor(getBeanClass(),"setSelected",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("setSelected(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("setSelected(boolean).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setSelected(boolean).boolean.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "TRUE to select",
	      			}
	      		)},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setTitle(String)
			super.createMethodDescriptor(getBeanClass(),"setTitle",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("setTitle(String).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the title"
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("text", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setTitle(String).text.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "New title",
	      			}
	      		)},
	      		new Class[] {
	      			String.class
	      		}		    		
		  	),
		  	// setUI(InternalFrameUI)
			super.createMethodDescriptor(getBeanClass(),"setUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("setUI(InternalFrameUI).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the UI",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("ui", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setUI(InternalFrameUI).ui.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "UI subclass",
	      			}
	      		)},
	      		new Class[] {
	      			javax.swing.plaf.InternalFrameUI.class
	      		}		    		
		  	),
		  	// setVisible(boolean)
			super.createMethodDescriptor(getBeanClass(),"setVisible",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("setVisible(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Show or hide the frame",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JInternalFrameMessages.getString("setVisible(boolean).aBoolean.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "TRUE to show",
	      			}
	      		)},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// show()
			super.createMethodDescriptor(getBeanClass(), "show",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("show().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("show().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}   		
		  	),			 
			// updateUI()
			super.createMethodDescriptor(getBeanClass(), "updateUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JInternalFrameMessages.getString("updateUI().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "update the UI",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}   		
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
	      	DISPLAYNAME, JInternalFrameMessages.getString("accessibleContext.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("accessibleContext.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// background
			super.createPropertyDescriptor(getBeanClass(),"background", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JInternalFrameMessages.getString("background.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("background.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
	    	// closable
			super.createPropertyDescriptor(getBeanClass(),"closable", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JInternalFrameMessages.getString("closable.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("closable.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
	    	// closed
			super.createPropertyDescriptor(getBeanClass(),"closed", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JInternalFrameMessages.getString("closed.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("closed.Desc"), //$NON-NLS-1$
	      	CONSTRAINED, Boolean.TRUE
	    		}
	    	),
			// contentPane
			super.createPropertyDescriptor(getBeanClass(),"contentPane", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JInternalFrameMessages.getString("contentPane.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("contentPane.Desc"), //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE,
	      	BOUND, Boolean.TRUE
	    		}
	    	),	    		    	
	    	// defaultCloseOperation
	    	super.createPropertyDescriptor(getBeanClass(),"defaultCloseOperation", new Object[] { //$NON-NLS-1$
	    	DISPLAYNAME, JInternalFrameMessages.getString("defaultCloseOperation.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("defaultCloseOperation.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE,
	      	IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      			JInternalFrameMessages.getString("defaultCloseOperation.DO_NOTHING_ON_CLOSE"), new Integer(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE), //$NON-NLS-1$
	      				"javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE", //$NON-NLS-1$
	            	JInternalFrameMessages.getString("defaultCloseOperation.HIDE_ON_CLOSE"), new Integer(javax.swing.WindowConstants.HIDE_ON_CLOSE), //$NON-NLS-1$
	            		"javax.swing.WindowConstants.HIDE_ON_CLOSE", //$NON-NLS-1$
	           		JInternalFrameMessages.getString("defaultCloseOperation.DISPOSE_ON_CLOSE"),new Integer(javax.swing.WindowConstants.DISPOSE_ON_CLOSE), //$NON-NLS-1$
	           			"javax.swing.WindowConstants.DISPOSE_ON_CLOSE" } //$NON-NLS-1$
	    		}
	    	),
	    	// desktopIcon
			super.createPropertyDescriptor(getBeanClass(),"desktopIcon", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JInternalFrameMessages.getString("desktopIcon.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("desktopIcon.Desc"), //$NON-NLS-1$
	      	OBSCURE, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE,
	      	DESIGNTIMEPROPERTY, Boolean.FALSE
    		}
	    	),
	    	// desktopPane
			super.createPropertyDescriptor(getBeanClass(),"desktopPane", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JInternalFrameMessages.getString("desktopPane.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("desktopPane.Desc"), //$NON-NLS-1$
	      	OBSCURE, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// foreground
			super.createPropertyDescriptor(getBeanClass(),"foreground", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JInternalFrameMessages.getString("foreground.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("foreground.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
	    	// frameIcon
			super.createPropertyDescriptor(getBeanClass(),"frameIcon", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JInternalFrameMessages.getString("frameIcon.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("frameIcon.Desc"), //$NON-NLS-1$
	    		}
	    	),
			// glassPane
			super.createPropertyDescriptor(getBeanClass(),"glassPane", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JInternalFrameMessages.getString("glassPane.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("glassPane.Desc"), //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
	    	// icon
			super.createPropertyDescriptor(getBeanClass(),"icon", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JInternalFrameMessages.getString("icon.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("icon.Desc"), //$NON-NLS-1$
	      	CONSTRAINED, Boolean.TRUE,
      		DESIGNTIMEPROPERTY, Boolean.FALSE	      	
	    		}
	    	),
			// JMenuBar
			super.createPropertyDescriptor(getBeanClass(),"JMenuBar", new Object[] { //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE	      	
	    		}
	    	),
	    	// layer
			super.createPropertyDescriptor(getBeanClass(),"layer", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JInternalFrameMessages.getString("layer.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("layer.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	OBSCURE, Boolean.TRUE
	    		}
	    	),
			// layout - discard from the property sheet
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JInternalFrameMessages.getString("layout.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JInternalFrameMessages.getString("layout.Desc"), //$NON-NLS-1$
	      		DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
	    	// iconifiable
			super.createPropertyDescriptor(getBeanClass(),"iconifiable", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JInternalFrameMessages.getString("iconifiable.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("iconifiable.Desc"), //$NON-NLS-1$
	    		}
	    	),
			// layeredPane
			super.createPropertyDescriptor(getBeanClass(),"layeredPane", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JInternalFrameMessages.getString("layeredPane.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("layeredPane.Desc"), //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE,
	      	BOUND, Boolean.TRUE
	    		}
	    	),
	    	// maximizable
			super.createPropertyDescriptor(getBeanClass(),"maximizable", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JInternalFrameMessages.getString("maximizable.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("maximizable.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// maximum
			super.createPropertyDescriptor(getBeanClass(),"maximum", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JInternalFrameMessages.getString("maximum.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("maximum.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// normalBounds
			super.createPropertyDescriptor(getBeanClass(),"normalBounds", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JInternalFrameMessages.getString("normalBounds.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("normalBounds.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	    		}
	    	),
	    	// resizable
			super.createPropertyDescriptor(getBeanClass(),"resizable", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JInternalFrameMessages.getString("resizable.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("resizable.Desc"), //$NON-NLS-1$
	    		}
	    	),
			// rootPane
			super.createPropertyDescriptor(getBeanClass(),"rootPane", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JInternalFrameMessages.getString("rootPane.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("rootPane.Desc"), //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
	    	// selected
			super.createPropertyDescriptor(getBeanClass(),"selected", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JInternalFrameMessages.getString("selected.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("selected.Desc"), //$NON-NLS-1$
	      	CONSTRAINED, Boolean.TRUE
	    		}
	    	),
	    	// title
			super.createPropertyDescriptor(getBeanClass(),"title", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JInternalFrameMessages.getString("title.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("title.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
	    	// ui
			super.createPropertyDescriptor(getBeanClass(),"UI", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JInternalFrameMessages.getString("ui.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JInternalFrameMessages.getString("ui.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
			BOUND, Boolean.TRUE
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

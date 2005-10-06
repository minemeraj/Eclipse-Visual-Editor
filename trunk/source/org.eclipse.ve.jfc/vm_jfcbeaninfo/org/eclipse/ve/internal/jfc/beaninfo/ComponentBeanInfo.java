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
 *  $RCSfile: ComponentBeanInfo.java,v $
 *  $Revision: 1.8 $  $Date: 2005-10-06 15:18:44 $ 
 */

import java.awt.dnd.DropTarget;
import java.beans.*;

import org.eclipse.jem.beaninfo.vm.BaseBeanInfo;
import org.eclipse.jem.beaninfo.vm.IBaseBeanInfoConstants;
/**
 * BeanInfo descriptor for a standard AWT component.
 */

public class ComponentBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle rescomponent = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.component");  //$NON-NLS-1$
	
/**
 * Gets the componentevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor componentEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.ComponentEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.ComponentListener.class,
				"componentHidden", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescomponent.getString("componentHiddenDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("componentHiddenSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("componentEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("componentEventDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "component hidden event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(java.awt.event.ComponentListener.class,
				"componentShown", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescomponent.getString("componentShownDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("componentShownSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("componentEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("componentEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "component shown event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(java.awt.event.ComponentListener.class,
				"componentResized", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescomponent.getString("componentResizedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("componentResizedSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("componentEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("componentEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "component resized event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(java.awt.event.ComponentListener.class,
				"componentMoved", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescomponent.getString("componentMovedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("componentMovedSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("componentEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("componentEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "component moved event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"component", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, rescomponent.getString("componentEventsDN"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, rescomponent.getString("componentEventsSD"), //$NON-NLS-1$
						BaseBeanInfo.EVENTADAPTERCLASS, "java.awt.event.ComponentAdapter" //$NON-NLS-1$	      				
	      			}, 
						aDescriptorList, java.awt.event.ComponentListener.class,
						"addComponentListener", "removeComponentListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}
/**
 * Gets the keyevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor focusEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.FocusEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.FocusListener.class,
				"focusGained", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescomponent.getString("focusGainedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("focusGainedSD"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("focusEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("focusEventDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "focus gained event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(java.awt.event.FocusListener.class,
				"focusLost", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescomponent.getString("focusLostDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("focusLostSD"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("focusEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("focusEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "focus lost event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"focus", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, rescomponent.getString("focusEventsDN"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, rescomponent.getString("focusEventsSD"), //$NON-NLS-1$
	      				EXPERT, Boolean.TRUE ,
	      				BaseBeanInfo.EVENTADAPTERCLASS, "java.awt.event.FocusAdapter" //$NON-NLS-1$
	      			}, 
						aDescriptorList, java.awt.event.FocusListener.class,
						"addFocusListener", "removeFocusListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return java.awt.Component.class;
}
public BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the ComponentBeanInfobean descriptor. */
	aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               	DISPLAYNAME, rescomponent.getString("ComponentDN"), //$NON-NLS-1$
	        		SHORTDESCRIPTION, rescomponent.getString("ComponentSD") //$NON-NLS-1$
	    }			    
		);
	return aDescriptor;
}
/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	try {
		EventSetDescriptor aDescriptorList[] = {
			componentEventSetDescriptor(),
			focusEventSetDescriptor(),
			hierarchyBoundsEventSetDescriptor(),
			hierarchyEventSetDescriptor(),
			inputMethodEventSetDescriptor(),
			keyEventSetDescriptor(),
			mouseEventSetDescriptor(),
			mouseMotionEventSetDescriptor(),
			propertyChangeEventSetDescriptor()
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
			// action(Event evt, Object what) - DEPRECATED
			// add(PopupMenu popup)
			super.createMethodDescriptor(getBeanClass(), "add", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "add(PopupMenu)",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Add the popup menu",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("popupParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Popup menu to add",
	      			}
	      		)
	      		},
	      		new Class[] { 
	      			java.awt.PopupMenu.class 
	      		}	    		
		  	),			
			// addNotify()
			super.createMethodDescriptor(getBeanClass(), "addNotify", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "addNotify()",  //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Notify component has been added",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// bounds() - DEPRECATED			
			// checkImage(Image,ImageObserver)
			super.createMethodDescriptor(getBeanClass(), "checkImage", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "checkImage(Image,ImageObserver)", //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Check status of image",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("imageParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Image drawn",
	      			}
	      		),
	      		createParameterDescriptor("arg2", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("observerParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Observer to be notified",
	      			})	      		
	      		},
	      		new Class[] { 
	      			java.awt.Image.class,
	      			java.awt.image.ImageObserver.class
	      		}   		
		  	),
			// contains(int,int)
			super.createMethodDescriptor(getBeanClass(), "contains", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "contains(int,int)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("contains(int,int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("x", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("xParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "x coordinate of point",
	      			}
	      		),
	      		createParameterDescriptor("y", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("yParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "y coordinate of point",
	      			})	      		
	      		},
	      		new Class[] { 
	      			int.class,
	      			int.class
	      		}   		
		  	),
			// contains(Point)
			super.createMethodDescriptor(getBeanClass(), "contains", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "contains(Point)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("contains(Point)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("pointParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Point",
	      			})      		
	      		},
	      		new Class[] { 
	      			java.awt.Point.class
	      		}   		
		  	),
			// createImage(int,int)
			super.createMethodDescriptor(getBeanClass(), "createImage", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "createImage(int,int)",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Create off-screen drawable image",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("widthParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "width of image",
	      			}
	      		),
	      		createParameterDescriptor("arg2", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("heightParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "height of image",
	      			})	      		
	      		},
	      		new Class[] { 
	      			int.class,
	      			int.class
	      		}   		
		  	),
			// createImage(ImageProducer)
			super.createMethodDescriptor(getBeanClass(), "createImage", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "createImage(ImageProducer)",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Create image from producer",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("producerDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Image producer",
	      			})	      		
	      		},
	      		new Class[] {
	      			java.awt.image.ImageProducer.class
	      		}   		
		  	),
			// deliver(Event) - DEPRECATED
			// disable - DEPRECATED
			// dispatchEvent(AWTEvent)
			super.createMethodDescriptor(getBeanClass(), "dispatchEvent", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "dispatchEvent(AWTEvent)",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "dispatch event to component",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("eventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "event to dispatch",
	      			})	      		
	      		},
	      		new Class[] {
	      			java.awt.AWTEvent.class
	      		}   		
		  	),
			// doLayout()
			super.createMethodDescriptor(getBeanClass(), "doLayout", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "doLayout()",  //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Lay out the component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// enable(boolean) - DEPRECATED
			// enable() - DEPRECATED
			// getAlignmentX()
			super.createMethodDescriptor(getBeanClass(), "getAlignmentX", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getAlignmentX()",  //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get alignment along x-axis",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getAlignmentY()
			super.createMethodDescriptor(getBeanClass(), "getAlignmentY", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getAlignmentY()",  //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get alignment along y-axis",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getBackground()
			super.createMethodDescriptor(getBeanClass(), "getBackground", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getBackground()",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("getBackground()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getBounds()
			super.createMethodDescriptor(getBeanClass(), "getBounds", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getBounds()",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("getBounds()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getColorModel()
			super.createMethodDescriptor(getBeanClass(), "getColorModel", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getColorModel()",  //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the color model",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getComponentAt(int,int)
			super.createMethodDescriptor(getBeanClass(), "getComponentAt", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getComponentAt(int,int)",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the component at point",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("x", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("xParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "x coordinate of point",
	      			}
	      		),
	      		createParameterDescriptor("y", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("yParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "y coordinate of point",
	      			})	      		
	      		},
	      		new Class[] { 
	      			int.class,
	      			int.class
	      		}   		
		  	),
			// getComponentAt(Point)
			super.createMethodDescriptor(getBeanClass(), "getComponentAt", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getComponentAt(Point)",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the component at point",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("point", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("pointParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Point",
	      			})      		
	      		},
	      		new Class[] { 
	      			java.awt.Point.class
	      		}   		
		  	),
			// getCursor()
			super.createMethodDescriptor(getBeanClass(), "getCursor", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getCursor()", //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the cursor",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getFont()
			super.createMethodDescriptor(getBeanClass(), "getFont", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getFont()", //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the font",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getFontMetrics(Font)
			super.createMethodDescriptor(getBeanClass(), "getFontMetrics", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getFontMetrics(Font)",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the font metrics for the font",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("font", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("fontParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Font",
	      			})      		
	      		},
	      		new Class[] { 
	      			java.awt.Font.class
	      		}   		
		  	),
			// getForeground()
			super.createMethodDescriptor(getBeanClass(), "getForeground", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getForeground()",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("getForeground()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getGraphics()
			super.createMethodDescriptor(getBeanClass(), "getGraphics", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getGraphics()",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the graphics context",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getLocale()
			super.createMethodDescriptor(getBeanClass(), "getLocale", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getLocale()",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the locale of the component",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getLocation()
			super.createMethodDescriptor(getBeanClass(), "getLocation", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getLocation()",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("getLocation()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getLocationOnScreen()
			super.createMethodDescriptor(getBeanClass(), "getLocationOnScreen", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getLocationOnScreen()",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the location in screen coordinates",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getMaximumSize()
			super.createMethodDescriptor(getBeanClass(), "getMaximumSize", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getMaximumSize()",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the maximum size",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getMinimumSize()
			super.createMethodDescriptor(getBeanClass(), "getMinimumSize", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getMinimumSize()",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the minimum size",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getName()
			super.createMethodDescriptor(getBeanClass(), "getName", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getName()",  //$NON-NLS-1$
	   			  		EXPERT, Boolean.TRUE,
	   			
	      		// SHORTDESCRIPTION, "Get the name of the component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getParent()
			super.createMethodDescriptor(getBeanClass(), "getParent", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getParent()",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the parent of the component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getPeer() - DEPRECATED
			// getSize()
			super.createMethodDescriptor(getBeanClass(), "getSize", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getSize()",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the size",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getToolkit()
			super.createMethodDescriptor(getBeanClass(), "getToolkit", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getToolkit()",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("getToolkit()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getTreeLock()
			super.createMethodDescriptor(getBeanClass(), "getTreeLock", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getTreeLock()",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the component's locking object",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// gotFocus(Event,Object) - DEPRECATED
			// handleEvent(Event) - DEPRECATED
			// hide() - DEPRECATED
			// inside(int, int)- DEPRECATED
			// invalidate()
			super.createMethodDescriptor(getBeanClass(), "invalidate", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "invalidate()",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("invalidate()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// isEnabled()
			super.createMethodDescriptor(getBeanClass(), "isEnabled", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "isEnabled()",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Is the component enabled?",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// isFocusTraversable()
			super.createMethodDescriptor(getBeanClass(), "isFocusTraversable", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "isFocusTraversable()",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("isFocusTraversable()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// isShowing()
			super.createMethodDescriptor(getBeanClass(), "isShowing", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "isShowing()",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Is component and parent visible?",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// isValid()
			super.createMethodDescriptor(getBeanClass(), "isValid", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "isValid()",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Is component validated?",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}
	      ),	
			// isVisible()
			super.createMethodDescriptor(getBeanClass(), "isVisible", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "isVisible()",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("isVisible()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}
	      	),
			// keyDown(Event) - DEPRECATED
			// keyUp(Event) - DEPRECATED
			// layout() - DEPRECATED

			// list(PrintStream)
			super.createMethodDescriptor(getBeanClass(), "list", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "list(PrintStream)",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Print listing of component to stream",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("outParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Stream to print to",
	      			})	      		
	      		},
	      		new Class[] { 
	      			java.io.PrintStream.class
	      		}   		
		  	),
			// list()
			super.createMethodDescriptor(getBeanClass(), "list", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "list()",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Print listing of component to stdout",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {} 		
		  	),
			// list(PrintWriter,int)
			super.createMethodDescriptor(getBeanClass(), "list", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "list(PrintWriter,int)",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Print listing of component to stream",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("out", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("outParm"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Writer to print to",
	      			}
	      		),
	      		createParameterDescriptor("indent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("indentDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "number of spaces to indent",
	      			})	      		
	      	},
	      	new Class[] { 
	      		java.io.PrintWriter.class,
	      		int.class
	      	}   		
		  	),
			// locate(int,int) - DEPRECATED
			// location() - DEPRECATED
			// lostFocus(Event,Object) - DEPRECATED
			// minimumSize() - DEPRECATED
			// mouseDown(Event,int,int) - DEPRECATED
			// mouseDrag(Event,int,int) - DEPRECATED
			// mouseEnter(Event,int,int) - DEPRECATED
			// mouseExit(Event,int,int) - DEPRECATED
			// mouseMove(Event,int,int) - DEPRECATED
			// mouseUp(Event,int,int) - DEPRECATED
			// move(int,int) - DEPRECATED
			// nextFocus() - DEPRECATED
			// paint(Graphics)
			super.createMethodDescriptor(getBeanClass(), "paint", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "paint(Graphics)",  //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Paint this component",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("graphics", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("GraphicsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "the graphics context to use",
	      			}
	      		)	      		
	      	},
	      	new Class[] { 
	      		java.awt.Graphics.class
	      	}   		
		  	),
		  	// paintAll(Graphics)
		  	super.createMethodDescriptor(getBeanClass(), "paintAll", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "paintAll(Graphics)",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Paint this component and subcomponents",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("graphicsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "the graphics context to use",
	      			})	      		
	      		},
	      		new Class[] { 
	      			java.awt.Graphics.class
	      		}   		
		  	),
			// postEvent(Event) - DEPRECATED			
			// prepareImage(Image,ImageObserver()
			super.createMethodDescriptor(getBeanClass(), "prepareImage", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "prepareImage(Image,ImageObserver)", //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Prepare image for rendering",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("image", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("imageParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Image drawn",
	      			}
	      		),
	      		createParameterDescriptor("observer", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("observerParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Observer to be notified",
	      			}
	      		)	      		
	      	},
	      	new Class[] { 
	      		java.awt.Image.class,
	      		java.awt.image.ImageObserver.class
	      	}   		
		  	),	
			// print(Graphics)
			super.createMethodDescriptor(getBeanClass(), "print", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "print(Graphics)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("print(Graphics)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("graphicsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "the graphics context to use",
	      			})	      		
	      		},
	      		new Class[] { 
	      			java.awt.Graphics.class
	      		}   		
		  	),
			// printAll(Graphics)
			super.createMethodDescriptor(getBeanClass(), "printAll", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "printAll(Graphics)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("printAll(Graphics)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("graphicsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "the graphics context to use",
	      			}
	      		)	      		
	      	},
	      	new Class[] { 
	      		java.awt.Graphics.class
	      	}   		
		  	),
			// remove(MenuComponent)
			super.createMethodDescriptor(getBeanClass(), "remove", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "remove(MenuComponent)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("remove(MenuComponent)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("popupParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Popup menu to be removed",
	      			})	      		
	      		},
	      		new Class[] { 
	      			java.awt.MenuComponent.class
	      		}   		
		  	),
			// removeNotify()
			super.createMethodDescriptor(getBeanClass(), "removeNotify", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "removeNotify()",  //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Notify that component has been removed",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// repaint()
			super.createMethodDescriptor(getBeanClass(), "repaint", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "repaint()",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Repaint the component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// requestFocus()
			super.createMethodDescriptor(getBeanClass(), "requestFocus", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "requestFocus()",  //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Request the input focus",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// reshape(int,int,int,int) - DEPRECATED
			// resize(int,int) - DEPRECATED
			// resize(Dimension) - DEPRECATED
			// setBackground(Color)
			super.createMethodDescriptor(getBeanClass(), "setBackground", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setBackground(Color)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("setBackground(Color)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("colorParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Background color",
	      			}),		      				      		
	      		},
	      		new Class[] {
	      			java.awt.Color.class, 
	      		}   		
		  	),
			// setBounds(int,int,int,int)
			super.createMethodDescriptor(getBeanClass(), "setBounds", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setBounds(int,int,int,int)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("setBounds(int,int,int,int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("xParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "x coordinate",
	      			}
	      		),
	      		createParameterDescriptor("arg2", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("yParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "y coordinate",
	      			}
	      		),
	      		createParameterDescriptor("arg3", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("widthParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "width of component",
	      			}
	      		),
	      		createParameterDescriptor("arg4", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("heightParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "height of component",
	      			}),		      				      		
	      		},
	      		new Class[] { 
	      			int.class, int.class,
	      			int.class, int.class
	      		}   		
		  	),			
			// setBounds(Rectangle)
			super.createMethodDescriptor(getBeanClass(), "setBounds", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setBounds(Rectangle)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("setBounds(Rectangle)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("rect", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("rectangleParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "The bounding rectangle",
	      			})	      				      		
	      		},
	      		new Class[] { 
	      			java.awt.Rectangle.class
	      		}   		
		  	),
			// setCursor(Cursor)
			super.createMethodDescriptor(getBeanClass(), "setCursor", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setCursor(Cursor)",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the cursor",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("cursorParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Pre-defined cursor",
	      			})	      				      		
	      		},
	      		new Class[] { 
	      			java.awt.Cursor.class
	      		}   		
		  	),
			// setEnabled(boolean)
			super.createMethodDescriptor(getBeanClass(), "setEnabled", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setEnabled(boolean)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("setEnabled(boolean)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("enabled", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("booleanParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "true to enable",
	      			}),		      				      		
	      		},
	      		new Class[] {
	      			boolean.class, 
	      		}   		
		  	),
			// setFont(Font)
			super.createMethodDescriptor(getBeanClass(), "setFont", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setFont",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "set the Font",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("font", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("fontParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Font to set to",
	      			}),		      				      		
	      		},
	      		new Class[] {
	      			java.awt.Font.class, 
	      		}   		
		  	),
			// setForegroundColor()
			super.createMethodDescriptor(getBeanClass(), "setForeground", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setForeground(Color)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("setForeground(Color)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("colorParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Foreground color",
	      			}),		      				      		
	      		},
	      		new Class[] {
	      			java.awt.Color.class, 
	      		}   		
		  	),
			// setLocale(Locale)
			super.createMethodDescriptor(getBeanClass(), "setLocale", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setLocale(Locale)",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the locale of the component",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("localeParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Locale to set to",
	      			}),		      				      		
	      		},
	      		new Class[] {
	      			java.util.Locale.class, 
	      		}   		
		  	),
			// setLocation(int,int)
			super.createMethodDescriptor(getBeanClass(), "setLocation", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setLocation(int,_int)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("setLocation(int,_int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("xParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "x coordinate",
	      			}
	      		),
	      		createParameterDescriptor("arg2", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("yParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "y coordinate",
	      			})		      				      		
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}   		
		  	),
			// setLocation(Point)
			super.createMethodDescriptor(getBeanClass(), "setLocation", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setLocation(Point)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("setLocation(Point)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("pointParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Point of origin",
	      			})		      				      		
	      		},
	      		new Class[] {
	      			java.awt.Point.class
	      		}   		
		  	),
			// setName(String)
			super.createMethodDescriptor(getBeanClass(), "setName", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setName(String)",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the name",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("nameParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "name of component",
	      			})		      				      		
	      		},
	      		new Class[] {
	      			String.class
	      		}   		
		  	),
			// setSize(int,int)
			super.createMethodDescriptor(getBeanClass(), "setSize", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setSize(int,_int)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("setSize(int,_int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("widthParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "new width",
	      			}
	      		),
	      		createParameterDescriptor("arg2", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("heightParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "new height",
	      			})		      				      		
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}   		
		  	),
			// setSize(Dimension)
			super.createMethodDescriptor(getBeanClass(), "setSize", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setSize(Dimension)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("setSize(Dimension)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("dimensionParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "new dimension",
	      			})	      				      		
	      		},
	      		new Class[] {
	      			java.awt.Dimension.class
	      		}   		
		  	),
			// setVisible(boolean)
			super.createMethodDescriptor(getBeanClass(), "setVisible", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setVisible(boolean)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("setVisible(boolean)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("visible", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("booleanParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "true to show",
	      			}),		      				      		
	      		},
	      		new Class[] {
	      			boolean.class, 
	      		}   		
		  	),
			// show(boolean) - DEPRECATED
			// show() - DEPRECATED
			// size() - DEPRECATED
			// transferFocus()
			super.createMethodDescriptor(getBeanClass(), "transferFocus", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "transferFocus()",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("transferFocus()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// update(Graphics)
			super.createMethodDescriptor(getBeanClass(), "update", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "update(Graphics)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("update(Graphics)SD"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("graphics", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("graphicParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "the graphics context to use",
	      			})	      		
	      		},
	      		new Class[] { 
	      			java.awt.Graphics.class
	      	}   		
		  	),
			// validate()
			super.createMethodDescriptor(getBeanClass(), "validate", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "validate()",  //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "validate the component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getDropTarget()
			super.createMethodDescriptor(getBeanClass(),"getDropTarget",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescomponent.getString("MthdDesc.GetDropTarget.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the drop target",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setDropTarget(DropTarget)
			super.createMethodDescriptor(getBeanClass(),"setDropTarget",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescomponent.getString("MthdDesc.SetDropTarget.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the drop target",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("dropTarget", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, rescomponent.getString("ParamDesc.SetDropTarget.dropTarget.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "DropTarget",
	      				})
	      		},
	      		new Class[] {
	      			DropTarget.class
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
			// alignmentX  
	   	super.createPropertyDescriptor(getBeanClass(), "alignmentX", new Object[] {//$NON-NLS-1$
	   		DISPLAYNAME, rescomponent.getString("alignmentXDN"), //$NON-NLS-1$
	   		SHORTDESCRIPTION, rescomponent.getString("alignmentXSD"), //$NON-NLS-1$
	        EXPERT, Boolean.TRUE,
	    		}
		  	),
			// ,alignmentY
			super.createPropertyDescriptor(getBeanClass(), "alignmentY", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, rescomponent.getString("alignmentYDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescomponent.getString("alignmentYSD"), //$NON-NLS-1$
	      	
	      	EXPERT, Boolean.TRUE,

	    		}
	    	),
			// background
			super.createPropertyDescriptor(getBeanClass(), "background", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("backgroundDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescomponent.getString("backgroundSD"),  //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	    		}
	    	),
			// bounds
			super.createPropertyDescriptor(getBeanClass(), "bounds", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("boundsDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescomponent.getString("boundsSD"), //$NON-NLS-1$

	    		}
	    	),
			//colorModel
			super.createPropertyDescriptor(getBeanClass(), "colorModel", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("colorModelDN"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	SHORTDESCRIPTION, rescomponent.getString("colorModelSD"), //$NON-NLS-1$

	    		}
	    	),
			// cursor
			super.createPropertyDescriptor(getBeanClass(), "cursor", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("cursorDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescomponent.getString("cursorSD"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,

	    		}
	    	),
			// enabled
			super.createPropertyDescriptor(getBeanClass(), "enabled", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("enabledDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescomponent.getString("enabledSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE,
	      	BOUND, Boolean.TRUE,
	    		}
	    	),
			// focusTraversable
			super.createPropertyDescriptor(getBeanClass(), "focusTraversable", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("focusTraversableDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescomponent.getString("focusTraversableSD"),  //$NON-NLS-1$
	    		}
	    	),
			// font
			super.createPropertyDescriptor(getBeanClass(), "font", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("fontDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescomponent.getString("fontSD"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	    		}
	    	),
			// foreground
			super.createPropertyDescriptor(getBeanClass(), "foreground", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("foregroundDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescomponent.getString("foregroundSD"),  //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	    		}
	    	),	
			// graphics
			super.createPropertyDescriptor(getBeanClass(), "graphics", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("graphicsDN"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	SHORTDESCRIPTION, rescomponent.getString("graphicsSD"), //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE,
	    		}
	    	),	
			// locale
			super.createPropertyDescriptor(getBeanClass(), "locale", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("localeDN"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	SHORTDESCRIPTION, rescomponent.getString("localeSD"),  //$NON-NLS-1$
	    		}
	    	),	
			// locationOnScreen
			super.createPropertyDescriptor(getBeanClass(), "locationOnScreen", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("locationOnScreenDN"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	SHORTDESCRIPTION, rescomponent.getString("locationOnScreenSD"),  //$NON-NLS-1$

	    		}
	    	),
			// location
			super.createPropertyDescriptor(getBeanClass(), "location", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("locationDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescomponent.getString("locationSD"), //$NON-NLS-1$
	    		}
	    	),
			// maximumSize
			super.createPropertyDescriptor(getBeanClass(), "maximumSize", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("maximumSizeDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescomponent.getString("maximumSizeSD"),  //$NON-NLS-1$
	    		}
	    	),
			// minimumSize
			super.createPropertyDescriptor(getBeanClass(), "minimumSize", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("minimumSizeDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescomponent.getString("minimumSizeSD"),  //$NON-NLS-1$

	    		}
	    	),			
			// name
			super.createPropertyDescriptor(getBeanClass(), "name", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("nameDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescomponent.getString("nameSD"),	      	 //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.TRUE,
	      	
	    		}
	    	),	
			// parent
			super.createPropertyDescriptor(getBeanClass(), "parent", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("parentDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescomponent.getString("parentSD"),  //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE,

	    		}
	    	),	
			// peer - DEPRECATED
			// showing
			super.createPropertyDescriptor(getBeanClass(), "showing", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("showingDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescomponent.getString("showingSD"), //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE,

	    		}
	    	),
			// size
			super.createPropertyDescriptor(getBeanClass(), "size", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("sizeDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescomponent.getString("sizeSD"),  //$NON-NLS-1$

	    		}
	    	),
			// toolkit
			super.createPropertyDescriptor(getBeanClass(), "toolkit", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("toolkitDN"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	SHORTDESCRIPTION, rescomponent.getString("toolkitSD"), //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE,

	    		}
	    	),
			// treeLock
			super.createPropertyDescriptor(getBeanClass(), "treeLock", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("treeLockDN"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	SHORTDESCRIPTION, rescomponent.getString("treeLockSD"), //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE,

	    		}
	    	),	
			// valid
			super.createPropertyDescriptor(getBeanClass(), "valid", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("validDN"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	SHORTDESCRIPTION, rescomponent.getString("validSD"), //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE,

	    		}
	    	),	
			// visible
			super.createPropertyDescriptor(getBeanClass(), "visible", new Object[] {//$NON-NLS-1$
	      	DISPLAYNAME, rescomponent.getString("visibleDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescomponent.getString("visibleSD"),  //$NON-NLS-1$
			BOUND, Boolean.TRUE,
	    		}
	    	),	
           // ComponentOrientation
            super.createPropertyDescriptor(getBeanClass(), "componentOrientation", new Object[] {//$NON-NLS-1$
              DISPLAYNAME, rescomponent.getString("componentOrientationDN"),  //$NON-NLS-1$
              SHORTDESCRIPTION,rescomponent.getString("componentOrientationSD"),  //$NON-NLS-1$
              IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
                          rescomponent.getString("componentOrientation.LEFT_TO_RIGHT"), java.awt.ComponentOrientation.LEFT_TO_RIGHT,  //$NON-NLS-1$
                          "java.awt.ComponentOrientation.LEFT_TO_RIGHT",//$NON-NLS-1$
                          rescomponent.getString("componentOrientation.RIGHT_TO_LEFT"), java.awt.ComponentOrientation.RIGHT_TO_LEFT,  //$NON-NLS-1$
                          "java.awt.ComponentOrientation.RIGHT_TO_LEFT",//$NON-NLS-1$
                          rescomponent.getString("componentOrientation.UNKNOWN"), java.awt.ComponentOrientation.UNKNOWN,  //$NON-NLS-1$
                          "java.awt.ComponentOrientation.UNKNOWN"//$NON-NLS-1$
                    } ,
                    BOUND, Boolean.TRUE,                                   
               }
            ),
	    	// dropTarget
			super.createPropertyDescriptor(getBeanClass(),"dropTarget", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, rescomponent.getString("PropDesc.DropTarget.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescomponent.getString("PropDesc.DropTarget.Desc"), //$NON-NLS-1$
	      	//BOUND, Boolean.TRUE,
	      	//PREFERRED, Boolean.TRUE,
	      	//EXPERT, Boolean.TRUE
			HIDDEN, Boolean.TRUE
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
 * Gets the hierarchyevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor hierarchyEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.HierarchyEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.HierarchyListener.class,
				"hierarchyChanged", //$NON-NLS-1$
				new Object[] {
	   			    DISPLAYNAME, rescomponent.getString("hierarchyChangedDN"), //$NON-NLS-1$
	      		    SHORTDESCRIPTION, rescomponent.getString("hierarchyChangedSD"), //$NON-NLS-1$
	      		    EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("hierarchyEvent", new Object[] {//$NON-NLS-1$
	   				    DISPLAYNAME, rescomponent.getString("hierarchyEventDN"), //$NON-NLS-1$
	      			    // SHORTDESCRIPTION, "focus gained event",
	      		    }
	      	        )
  	      	    },
	      	    paramTypes
		  	)
		};
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"hierarchy", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, rescomponent.getString("hierarchyEventsDN"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, rescomponent.getString("hierarchyEventsSD"), //$NON-NLS-1$
	      				EXPERT, Boolean.TRUE
	      			}, 
						aDescriptorList, java.awt.event.HierarchyListener.class,
						"addHierarchyListener", "removeHierarchyListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}
/**
 * Gets the hierarchyboundsevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor hierarchyBoundsEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.HierarchyEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.HierarchyBoundsListener.class,
				"ancestorMoved", //$NON-NLS-1$
				new Object[] {
	   			    DISPLAYNAME, rescomponent.getString("ancestorMovedDN"), //$NON-NLS-1$
	      		    SHORTDESCRIPTION, rescomponent.getString("ancestorMovedSD"), //$NON-NLS-1$
	      		    EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("hierarchyEvent", new Object[] {//$NON-NLS-1$
	   				    DISPLAYNAME, rescomponent.getString("hierarchyEventDN"), //$NON-NLS-1$
	      			    // SHORTDESCRIPTION, "focus gained event",
	      		    }
	      	        )
  	      	    },
	      	    paramTypes
		  	),
			super.createMethodDescriptor(java.awt.event.HierarchyBoundsListener.class,
				"ancestorResized", //$NON-NLS-1$
				new Object[] {
	   			    DISPLAYNAME, rescomponent.getString("ancestorResizedDN"), //$NON-NLS-1$
	      		    SHORTDESCRIPTION, rescomponent.getString("ancestorResizedSD"), //$NON-NLS-1$
	      		    EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("hierarchyEvent", new Object[] {//$NON-NLS-1$
	   				    DISPLAYNAME, rescomponent.getString("hierarchyEventDN"), //$NON-NLS-1$
	      			    // SHORTDESCRIPTION, "focus gained event",
	      		    }
	      	        )
  	      	    },
	      	    paramTypes
		  	)		  	
		};
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"hierarchyBounds", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, rescomponent.getString("hierarchyBoundsEventsDN"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, rescomponent.getString("hierarchyBoundsEventsSD"), //$NON-NLS-1$
						BaseBeanInfo.EVENTADAPTERCLASS, "java.awt.event.HierarchyBoundsAdapter", //$NON-NLS-1$	      				
	      				EXPERT, Boolean.TRUE
	      			    }, 
						aDescriptorList, java.awt.event.HierarchyBoundsListener.class,
						"addHierarchyBoundsListener", "removeHierarchyBoundsListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}

/**
 * Gets the inputmethodevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor inputMethodEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.InputMethodEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.InputMethodListener.class,
				"caretPositionChanged", //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, rescomponent.getString("caretPositionChangedDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, rescomponent.getString("caretPositionChangedSD"), //$NON-NLS-1$
					EXPERT, Boolean.TRUE
				}, 
				new ParameterDescriptor[] {
					createParameterDescriptor("inputMethodEvent", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, rescomponent.getString("inputMethodEventDN"), //$NON-NLS-1$
					}
					)
				},
				paramTypes
			),
			super.createMethodDescriptor(java.awt.event.InputMethodListener.class,
				"inputMethodTextChanged", //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, rescomponent.getString("inputMethodTextChangedDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, rescomponent.getString("inputMethodTextChangedSD"), //$NON-NLS-1$
					EXPERT, Boolean.TRUE
				}, 
				new ParameterDescriptor[] {
					createParameterDescriptor("inputMethodEvent", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, rescomponent.getString("inputMethodEventDN"), //$NON-NLS-1$
					}
					)
				},
				paramTypes
			)		  	
		};
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"inputMethod", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, rescomponent.getString("inputMethodEventsDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, rescomponent.getString("inputMethodEventsSD"), //$NON-NLS-1$    				
						EXPERT, Boolean.TRUE
						}, 
						aDescriptorList, java.awt.event.InputMethodListener.class,
						"addInputMethodListener", "removeInputMethodListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}

/**
 * Gets the keyevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor keyEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.KeyEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.KeyListener.class,
				"keyPressed", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescomponent.getString("keyPressedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("keyPressedSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("keyEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("keyEventDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "key pressed event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(java.awt.event.KeyListener.class,
				"keyReleased", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescomponent.getString("keyReleasedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("keyReleasedSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("keyEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("keyEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "key released event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(java.awt.event.KeyListener.class,
				"keyTyped", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescomponent.getString("keyTypedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("keyTypedSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("keyEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("keyEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "key typed event",
	      			}
	      		)
	      	},
	      	paramTypes	    		
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"key", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, rescomponent.getString("keyEventsDN"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, rescomponent.getString("keyEventsSD"), //$NON-NLS-1$
	      				BaseBeanInfo.EVENTADAPTERCLASS, "java.awt.event.KeyAdapter", //$NON-NLS-1$
	      			}, 
						aDescriptorList, java.awt.event.KeyListener.class,
						"addKeyListener", "removeKeyListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}
/**
 * Gets the mouse event set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor mouseEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.MouseEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.MouseListener.class,
				"mouseClicked", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescomponent.getString("mouseClickedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("mouseClickedSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("mouseEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("mouseEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "mouse clicked event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(java.awt.event.MouseListener.class,
				"mouseEntered", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescomponent.getString("mouseEnteredDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("mouseEnteredSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("mouseEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("mouseEventDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "mouse entered event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(java.awt.event.MouseListener.class,
				"mouseExited", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescomponent.getString("mouseExitedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("mouseExitedSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("mouseEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("mouseEventDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "mouse exited event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(java.awt.event.MouseListener.class,
				"mousePressed", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescomponent.getString("mousePressedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("mousePressedSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("mouseEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("mouseEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "mouse pressed event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(java.awt.event.MouseListener.class,
				"mouseReleased", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescomponent.getString("mouseReleasedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("mouseReleasedSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("mouseEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("mouseEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "mouse released event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"mouse", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, rescomponent.getString("mouseEventsDN"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, rescomponent.getString("mouseEventsSD"), //$NON-NLS-1$
	      				BaseBeanInfo.EVENTADAPTERCLASS, "java.awt.event.MouseAdapter", //$NON-NLS-1$	      				
	      			}, 
						aDescriptorList, java.awt.event.MouseListener.class,
						"addMouseListener", "removeMouseListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}
/**
 * Gets the mouseMotionevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor mouseMotionEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.MouseEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.MouseMotionListener.class,
				"mouseDragged", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescomponent.getString("mouseDraggedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("mouseDraggedSD"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("mouseEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("mouseEventDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "mouse dragged event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(java.awt.event.MouseMotionListener.class,
				"mouseMoved", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescomponent.getString("mouseMovedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("mouseMovedSD"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("mouseEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("mouseEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "mouse moved event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),

		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"mouseMotion", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, rescomponent.getString("mouseMotionEventsDN"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, rescomponent.getString("mouseMotionEventsSD"), //$NON-NLS-1$
	      				BaseBeanInfo.EVENTADAPTERCLASS, "java.awt.event.MouseMotionAdapter", //$NON-NLS-1$
	      				EXPERT, Boolean.TRUE
	      			}, 
						aDescriptorList, java.awt.event.MouseMotionListener.class,
						"addMouseMotionListener", "removeMouseMotionListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}
/**
 * Gets the componentevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor propertyChangeEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.beans.PropertyChangeEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.beans.PropertyChangeListener.class,
				"propertyChange",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescomponent.getString("propertyChange.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescomponent.getString("propertyChange.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("propertyChangeEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, rescomponent.getString("propertyChange.propertyChangeEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "property changed event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"propertyChange", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, rescomponent.getString("propertyChangeEvents.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, rescomponent.getString("propertyChangeEvents.Desc"), //$NON-NLS-1$
	      			}, 
						aDescriptorList, java.beans.PropertyChangeListener.class,
						"addPropertyChangeListener", "removePropertyChangeListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}
}

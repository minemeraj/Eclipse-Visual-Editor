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
 *  $RCSfile: JComponentBeanInfo.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.beans.*;

import javax.swing.TransferHandler;

public class JComponentBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle JComponentMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jcomponent");  //$NON-NLS-1$
	
/**
 * Gets the componentevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor ancestorEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { javax.swing.event.AncestorEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(javax.swing.event.AncestorListener.class,
				"ancestorAdded",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("ancestorAdded.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("ancestorAdded.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("ancestorEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("ancestorEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "ancestor added event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(javax.swing.event.AncestorListener.class,
				"ancestorMoved",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("ancestorMoved.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("ancestorMoved.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("ancestorEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("ancestorEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "ancestor moved event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(javax.swing.event.AncestorListener.class,
				"ancestorRemoved",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("ancestorRemoved.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("ancestorRemoved.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("ancestorEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("ancestorEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "ancestor removed event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"ancestor", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JComponentMessages.getString("ancestorEvents.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JComponentMessages.getString("ancestorEvents.Desc"), //$NON-NLS-1$
	      			}, 
						aDescriptorList, javax.swing.event.AncestorListener.class,
						"addAncestorListener", "removeAncestorListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JComponent.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JComponentMessages.getString("JComponent.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JComponentMessages.getString("JComponent.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/jcomponentcolor32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/jcomponentcolor16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
	try {
		EventSetDescriptor aDescriptorList[] = {
			ancestorEventSetDescriptor(),
			vetoableChangeEventSetDescriptor()
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
	    return loadImage("jcomponentcolor32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("jcomponentcolor16.gif"); //$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
			// addNotify()
			super.createMethodDescriptor(getBeanClass(),"addNotify",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("addNotify().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Notify component that it has a parent",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// computeVisibleRect(Rectangle)
		  	super.createMethodDescriptor(getBeanClass(),"computeVisibleRect",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("computeVisibleRect(Rectangle).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Compute the visible rectangle for the component",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("visibleRect", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("computeVisibleRect(Rectangle).visibleRect.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "rectangle",
	      			})
	      		},
	      		new Class[] { 
	      			java.awt.Rectangle.class 
	      		}	    		
		  	),
		  	// createToolTip()
			super.createMethodDescriptor(getBeanClass(),"createToolTip",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("createToolTip().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Create tooltip instance",
	      			EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// firePropertyChange(String,boolean,boolean)
			super.createMethodDescriptor(getBeanClass(), "firePropertyChange",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,boolean,boolean).Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Notify property change",
	      			EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("propertyName", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,boolean,boolean).propertyName.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Name of changed property",
	      			}),
	      			createParameterDescriptor("oldValue", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,boolean,boolean).oldValue.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "old value",
	      			}),
	      			createParameterDescriptor("newValue", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,boolean,boolean).newValue.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "new value"
	      			})		
	      		},
	      		new Class[] { 
	      			java.lang.String.class,
	      			boolean.class, boolean.class
	      		}   		
		  	),
		  	// firePropertyChange(String,char,char)
			super.createMethodDescriptor(getBeanClass(), "firePropertyChange",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,char,char).Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Notify property change",
	      			EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("propertyName", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,char,char).propertyName.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Name of changed property",
	      			}),
	      			createParameterDescriptor("oldValue", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,char,char).oldValue.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "old value",
	      			}),
	      			createParameterDescriptor("newValue", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,char,char).newValue.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "new value"
	      			})		
	      		},
	      		new Class[] { 
	      			java.lang.String.class,
	      			char.class, char.class
	      		}   		
		  	),
		  	// firePropertyChange(String,double,double)
			super.createMethodDescriptor(getBeanClass(), "firePropertyChange",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,double,double).Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Notify property change",
	      			EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("propertyName", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,double,double).propertyName.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Name of changed property",
	      			}),
	      			createParameterDescriptor("oldValue", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,double,double).oldValue.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "old value",
	      			}),
	      			createParameterDescriptor("newValue", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,double,double).newValue.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "new value"
	      			})		
	      		},
	      		new Class[] { 
	      			java.lang.String.class,
	      			double.class, double.class
	      		}   		
		  	),
		  	// firePropertyChange(String,float,float)
			super.createMethodDescriptor(getBeanClass(), "firePropertyChange",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,float,float).Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Notify property change",
	      			EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("propertyName", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,float,float).propertyName.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Name of changed property",
	      			}),
	      			createParameterDescriptor("oldValue", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,float,float).oldValue.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "old value",
	      			}),
	      			createParameterDescriptor("newValue", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,float,float).newValue.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "new value"
	      			})		
	      		},
	      		new Class[] { 
	      			java.lang.String.class,
	      			float.class, float.class
	      		}   		
		  	),
		  	// firePropertyChange(String,int,int)
			super.createMethodDescriptor(getBeanClass(), "firePropertyChange",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,int,int).Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Notify property change",
	      			EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("propertyName", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,int,int).propertyName.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Name of changed property",
	      			}),
	      			createParameterDescriptor("oldValue", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,int,int).oldValue.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "old value",
	      			}),
	      			createParameterDescriptor("newValue", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,int,int).newValue.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "new value"
	      			})		
	      		},
	      		new Class[] { 
	      			java.lang.String.class,
	      			int.class, int.class
	      		}   		
		  	),
		  	// firePropertyChange(String,long,long)
			super.createMethodDescriptor(getBeanClass(), "firePropertyChange",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,long,long).Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Notify property change",
	      			EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("propertyName", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,long,long).propertyName.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Name of changed property",
	      			}),
	      			createParameterDescriptor("oldValue", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,long,long).oldValue.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "old value",
	      			}),
	      			createParameterDescriptor("newValue", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,long,long).newValue.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "new value"
	      			})		
	      		},
	      		new Class[] { 
	      			java.lang.String.class,
	      			long.class, long.class
	      		}   		
		  	),
		  	// firePropertyChange(String,short,short)
			super.createMethodDescriptor(getBeanClass(), "firePropertyChange",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,short,short).Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Notify property change",
	      			EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("propertyName", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,short,short).propertyName.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Name of changed property",
	      			}),
	      			createParameterDescriptor("oldValue", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,short,short).oldValue.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "old value",
	      			}),
	      			createParameterDescriptor("newValue", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComponentMessages.getString("firePropertyChange(String,short,short).newValue.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "new value"
	      			})		
	      		},
	      		new Class[] { 
	      			java.lang.String.class,
	      			short.class, short.class
	      		}   		
		  	),
		  	// getAccessibleContext()
			super.createMethodDescriptor(getBeanClass(),"getAccessibleContext",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Get the accessible context",
	      			EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getActionForKeyStroke(KeyStroke)
		  	super.createMethodDescriptor(getBeanClass(),"getActionForKeyStroke",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("getActionForKeyStroke(KeyStroke).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the action listener object invoked for keystroke",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("aKeyStroke", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("getActionForKeyStroke(KeyStroke).aKeyStroke.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "key stroke",
	      			})
	      		},
	      		new Class[] { 
	      			javax.swing.KeyStroke.class 
	      		}	    		
		  	),
		  	// getActionMap()
			super.createMethodDescriptor(getBeanClass(),"getActionMap",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getActionMap().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Get the ActionMap",
        			EXPERT, Boolean.TRUE,
	      			//HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),		  	
		  	// getAlignmentX()
			super.createMethodDescriptor(getBeanClass(),"getAlignmentX",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getAlignmentX().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Get the X alignment",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getAlignmentY()
			super.createMethodDescriptor(getBeanClass(),"getAlignmentY",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getAlignmentY().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Get the Y alignment",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getAutoscrolls()
			super.createMethodDescriptor(getBeanClass(),"getAutoscrolls",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getAutoscrolls().Name"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, JComponentMessages.getString("getAutoscrolls().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getBorder()
			super.createMethodDescriptor(getBeanClass(),"getBorder",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getBorder().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Get the Border object",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getBounds(Rectangle)
		  	super.createMethodDescriptor(getBeanClass(),"getBounds",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("getBounds(Rectangle).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("getBounds(Rectangle).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("rv", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("getBounds(Rectangle).aRectangle.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Rectangle holds return values",
	      			})
	      		},
	      		new Class[] { 
	      			java.awt.Rectangle.class 
	      		}	    		
		  	),
		  	// getClientProperty(Object)
		  	super.createMethodDescriptor(getBeanClass(),"getClientProperty",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("getClientProperty(Object).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the property value for key",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("key", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("getClientProperty(Object).aKey.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "key for look up",
	      			})
	      		},
	      		new Class[] { 
	      			java.lang.Object.class 
	      		}	    		
		  	),
		  	// getConditionForKeyStroke(KeyStroke)
		  	super.createMethodDescriptor(getBeanClass(),"getConditionForKeyStroke",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("getConditionForKeyStroke(KeyStroke).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the condition for invoking the keystroke",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("aKeyStroke", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("getConditionForKeyStroke(KeyStroke).aKeyStroke.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "key stroke",
	      			})
	      		},
	      		new Class[] { 
	      			javax.swing.KeyStroke.class 
	      		}	    		
		  	),
		  	// getGraphics()
		  	super.createMethodDescriptor(getBeanClass(),"getGraphics",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getGraphics().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Get the component's graphics context"
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// getHeight()
		  	super.createMethodDescriptor(getBeanClass(),"getHeight",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getHeight().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Get the component's height",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
            // getInputVerifier()
		  	super.createMethodDescriptor(getBeanClass(),"getInputVerifier",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getInputVerifier().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Get the component's InputVerifier",
        			EXPERT, Boolean.TRUE,
	      			HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// getInsets()
		  	super.createMethodDescriptor(getBeanClass(),"getInsets",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getInsets().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Get the component border's insets",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// getLocation(Point)
		  	super.createMethodDescriptor(getBeanClass(),"getLocation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("getLocation(Point).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the component's origin",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("rv", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("getLocation(Point).aPoint.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Point contains origin",
	      			})
	      		},
	      		new Class[] { 
	      			java.awt.Point.class 
	      		}	    		
		  	),
		  	// getMaximumSize()
		  	super.createMethodDescriptor(getBeanClass(),"getMaximumSize",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getMaximumSize().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Get the maximum size of the component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// getMinimumSize()
		  	super.createMethodDescriptor(getBeanClass(),"getMinimumSize",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getMinimumSize().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Get the minimum size of the component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// getNextFocusableComponent()
		  	super.createMethodDescriptor(getBeanClass(),"getNextFocusableComponent",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getNextFocusableComponent().Name"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, JComponentMessages.getString("getNextFocusableComponent().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// getPreferredSize()
		  	super.createMethodDescriptor(getBeanClass(),"getPreferredSize",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getPreferredSize().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Get the preferred size of the component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// getRegisteredKeyStrokes()
		  	super.createMethodDescriptor(getBeanClass(),"getRegisteredKeyStrokes",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getRegisteredKeyStrokes().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Get an array of KeyStroke objects",
	      			EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// getRootPane()
		  	super.createMethodDescriptor(getBeanClass(),"getRootPane",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getRootPane().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Get the component root pane",
	      			EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// getSize(Dimension)
		  	super.createMethodDescriptor(getBeanClass(),"getSize",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("getSize(Dimension).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the component's dimensions",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("rv", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("getSize(Dimension).aDimension.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Dimension contains dimensions",
	      			})
	      		},
	      		new Class[] { 
	      			java.awt.Dimension.class 
	      		}	    		
		  	),
		  	// getToolTipText()
		  	super.createMethodDescriptor(getBeanClass(),"getToolTipText",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getToolTipText().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Get the tool tip text for component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// getTopLevelAncestor()
		  	super.createMethodDescriptor(getBeanClass(),"getTopLevelAncestor",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getTopLevelAncestor().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Get the top-level container",
	      			EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// getVerifyInputWhenFocusTarget()
		  	super.createMethodDescriptor(getBeanClass(),"getVerifyInputWhenFocusTarget",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getVerifyInputWhenFocusTarget().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Get the VerifyInputWhenFocusTarget",
	      			EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// getVisibleRect()
		  	super.createMethodDescriptor(getBeanClass(),"getVisibleRect",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getVisibleRect().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Get the component's visible rectangle",
	      			EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// getWidth()
		  	super.createMethodDescriptor(getBeanClass(),"getWidth",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getWidth().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Get the component's width",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// getX()
		  	super.createMethodDescriptor(getBeanClass(),"getX",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getX().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Get the component's x location",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// getY()
		  	super.createMethodDescriptor(getBeanClass(),"getY",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getY().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Get the component's y location",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// grabFocus()
		  	super.createMethodDescriptor(getBeanClass(),"grabFocus",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("grabFocus().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Set the focus on the component",
	      			EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// hasFocus()
		  	super.createMethodDescriptor(getBeanClass(),"hasFocus",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("hasFocus().Name"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, JComponentMessages.getString("hasFocus().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// isDoubleBuffered()
		  	super.createMethodDescriptor(getBeanClass(),"isDoubleBuffered",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("isDoubleBuffered().Name"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, JComponentMessages.getString("isDoubleBuffered().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// isFocusCycleRoot()
		  	super.createMethodDescriptor(getBeanClass(),"isFocusCycleRoot",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("isFocusCycleRoot().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "TRUE if component subtree has focus cycle",
	      			EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// isManagingFocus()
		  	super.createMethodDescriptor(getBeanClass(),"isManagingFocus",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("isManagingFocus().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "TRUE if component manages focus",
	      			EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// isOpaque()
		  	super.createMethodDescriptor(getBeanClass(),"isOpaque",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("isOpaque().Name"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, JComponentMessages.getString("isOpaque().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// isOptimizedDrawingEnabled()
		  	super.createMethodDescriptor(getBeanClass(),"isOptimizedDrawingEnabled",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("isOptimizedDrawingEnabled().Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "TRUE if the component tiles its children",
	      			EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// isRequestFocusEnabled()
		  	super.createMethodDescriptor(getBeanClass(),"isRequestFocusEnabled",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("isRequestFocusEnabled().Name"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, JComponentMessages.getString("isRequestFocusEnabled().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// isValidateRoot()
		  	super.createMethodDescriptor(getBeanClass(),"isValidateRoot",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("isValidateRoot().Name"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, JComponentMessages.getString("isValidateRoot().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// paintImmediately(Rectangle)
		  	super.createMethodDescriptor(getBeanClass(),"paintImmediately",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("paintImmediately(Rectangle).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Paint the component region",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("r", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("paintImmediately(Rectangle).rectangle.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Painting region",
	      			})
	      		},
	      		new Class[] { 
	      			java.awt.Rectangle.class 
	      		}	    		
		  	),
		  	// putClientProperty(Object,Object)
		  	super.createMethodDescriptor(getBeanClass(),"putClientProperty",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("putClientProperty(Object,Object).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Store a property value",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("key", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("putClientProperty(Object,Object).key.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "key",
	      			}),
	    			createParameterDescriptor("value", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("putClientProperty(Object,Object).value.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Property value",
	      			})
	      		},
	      		new Class[] { 
	      			java.lang.Object.class, java.lang.Object.class
	      		}	    		
		  	),
			// removeNotify()
			super.createMethodDescriptor(getBeanClass(),"removeNotify",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("removeNotify().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Notify component that it has no parent",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// repaint(Rectangle)
		  	super.createMethodDescriptor(getBeanClass(),"repaint",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("repaint(Rectangle).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Repaint the rectangular region",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("r", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("repaint(Rectangle).rectangle.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Painting region",
	      			})
	      		},
	      		new Class[] { 
	      			java.awt.Rectangle.class 
	      		}	    		
		  	),
		  	// requestDefaultFocus()
			super.createMethodDescriptor(getBeanClass(),"requestDefaultFocus",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("requestDefaultFocus().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Request focus for component with default focus",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// requestFocus()
			super.createMethodDescriptor(getBeanClass(),"requestFocus",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("requestFocus().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Set focus on component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// resetKeyboardActions()
			super.createMethodDescriptor(getBeanClass(),"resetKeyboardActions",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("resetKeyboardActions().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Unregister all keyboard actions",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// revalidate()
			super.createMethodDescriptor(getBeanClass(),"revalidate",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("revalidate().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Validate the component",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// scrollRectToVisible(Rectangle)
		  	super.createMethodDescriptor(getBeanClass(),"scrollRectToVisible",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("scrollRectToVisible(Rectangle).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Scroll the component",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("aRect", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("scrollRectToVisible(Rectangle).rectangle.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Region to scroll to",
	      			})
	      		},
	      		new Class[] { 
	      			java.awt.Rectangle.class 
	      		}	    		
		  	),
		  	// setActionMap(ActionMap)
		  	super.createMethodDescriptor(getBeanClass(),"setActionMap",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("setActionMap(ActionMap).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("setActionMap(ActionMap).Desc"), //$NON-NLS-1$
       			EXPERT, Boolean.TRUE,	      		
	      		//HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("actionMap", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("setActionMap(ActionMap).actionMap.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Component's ActionMap",
	      			})
	      		},
	      		new Class[] { 
	      			javax.swing.ActionMap.class 
	      		}	    		
		  	),
		  	// setAlignmentX(float)
		  	super.createMethodDescriptor(getBeanClass(),"setAlignmentX",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("setAlignmentX(float).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("setAlignmentX(float).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("alignmentX", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("setAlignmentX(float).alignmentX.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Float value between 0 and 1",
	      			})
	      		},
	      		new Class[] { 
	      			float.class 
	      		}	    		
		  	),
		  	// setAlignmentY(float)
		  	super.createMethodDescriptor(getBeanClass(),"setAlignmentY",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("setAlignmentY(float).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("setAlignmentY(float).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("alignmentY", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("setAlignmentY(float).alignmentY.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Float value between 0 and 1",
	      			})
	      		},
	      		new Class[] { 
	      			float.class 
	      		}	    		
		  	),
		  	// setAutoscrolls(boolean)
		  	super.createMethodDescriptor(getBeanClass(),"setAutoscrolls",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("setAutoscrolls(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Whether the component auto scrolls in a viewport",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("autoscrolls", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("setAutoscrolls(boolean).autoscrolls.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "TRUE if the component auto scrolls",
	      			})
	      		},
	      		new Class[] { 
	      			boolean.class 
	      		}	    		
		  	),
		  	// setBorder(Border)
		  	super.createMethodDescriptor(getBeanClass(),"setBorder",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("setBorder(Border).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the component's border",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("border", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("setBorder(Border).border.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Border object",
	      			})
	      		},
	      		new Class[] { 
	      			javax.swing.border.Border.class 
	      		}	    		
		  	),

		  	// setDebugGraphicsOptions(int)
		  	super.createMethodDescriptor(getBeanClass(),"setDebugGraphicsOptions",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("setDebugGraphicsOptions(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Diagnostic options for graphics operations",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("debugOptions", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("setDebugGraphicsOptions(int).debugOptions.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Value for options ",
	      			})
	      		},
	      		new Class[] { 
	      			int.class
	      		}	    		
		  	),
		  	// setDoubleBuffered(boolean)
		  	super.createMethodDescriptor(getBeanClass(),"setDoubleBuffered",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("setDoubleBuffered(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE to use off-screen painting buffer",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("aFlag", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("setDoubleBuffered(boolean).aFlag.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "TRUE to buffer",
	      			})
	      		},
	      		new Class[] { 
	      			boolean.class
	      		}	    		
		  	),
		  	// setInputVerifier(InputVerifier)
		  	super.createMethodDescriptor(getBeanClass(),"setInputVerifier",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("setInputVerifier(InputVerifier).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("setInputVerifier(InputVerifier).Desc"), //$NON-NLS-1$
       			EXPERT, Boolean.TRUE,	      		
	      		HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("inputVerifier", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("setInputVerifier(InputVerifier).inputVerifier.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Component's inputVerifier",
	      			})
	      		},
	      		new Class[] { 
	      			javax.swing.InputVerifier.class 
	      		}	    		
		  	),	  	
		  	// setMaximumSize(Dimension)
		  	super.createMethodDescriptor(getBeanClass(),"setMaximumSize",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("setMaximumSize(Dimension).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the maximum size of the component",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("maximumSize", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("setMaximumSize(Dimension).maximumSize.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Dimensions for maximum size",
	      			})
	      		},
	      		new Class[] { 
	      			java.awt.Dimension.class
	      		}	    		
		  	),
		  	// setMinimumSize(Dimension)
		  	super.createMethodDescriptor(getBeanClass(),"setMinimumSize",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("setMinimumSize(Dimension).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the minimum size of the component",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("minimumSize", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("setMinimumSize(Dimension).minimumSize.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Dimensions for minimum size",
	      			})
	      		},
	      		new Class[] { 
	      			java.awt.Dimension.class
	      		}	    		
		  	),
		  	// setNextFocusableComponent(Component)
		  	super.createMethodDescriptor(getBeanClass(),"setNextFocusableComponent",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("setNextFocusableComponent(Component).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the next component to get the focus",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("aComponent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("setNextFocusableComponent(Component).aComponent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Next component",
	      			})
	      		},
	      		new Class[] { 
	      			java.awt.Component.class
	      		}	    		
		  	),
		  	// setPreferredSize(Dimension)
		  	super.createMethodDescriptor(getBeanClass(),"setPreferredSize",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("setPreferredSize(Dimension).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the preferred size of the component",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("preferredSize", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("setPreferredSize(Dimension).preferredSize.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Dimensions for preferred size",
	      			})
	      		},
	      		new Class[] { 
	      			java.awt.Dimension.class
	      		}	    		
		  	),
		  	// setOpaque(boolean)
		  	super.createMethodDescriptor(getBeanClass(),"setOpaque",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("setOpaque(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE if component is opaque",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("isOpaque", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("setOpaque(boolean).isOpaque"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "TRUE if opaque",
	      			})
	      		},
	      		new Class[] { 
	      			boolean.class
	      		}	    		
		  	),
		  	// setRequestFocusEnabled(boolean)
		  	super.createMethodDescriptor(getBeanClass(),"setRequestFocusEnabled",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("setRequestFocusEnabled(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE if the component can obtain focus on request",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("aFlag", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("setRequestFocusEnabled(boolean).aFlag.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "TRUE to obtain focus",
	      			})
	      		},
	      		new Class[] { 
	      			boolean.class
	      		}	    		
		  	),
		  	// setToolTipText(String)
		  	super.createMethodDescriptor(getBeanClass(),"setToolTipText",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("setToolTipText(String).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the tooltip text",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("text", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("setToolTipText(String).text.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Text to display",
	      			})
	      		},
	      		new Class[] { 
	      			String.class
	      		}	    		
		  	),
		  	// setVerifyInputWhenFocusTarget(boolean)
		  	super.createMethodDescriptor(getBeanClass(),"setVerifyInputWhenFocusTarget",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("setVerifyInputWhenFocusTarget(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("setVerifyInputWhenFocusTarget(boolean).Desc"), //$NON-NLS-1$
       			EXPERT, Boolean.TRUE,	      		
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("verifyInputWhenFocusTarget", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("setVerifyInputWhenFocusTarget(boolean).verifyInputWhenFocusTarget.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "whether to verifyInputWhenFocusTarget",
	      			})
	      		},
	      		new Class[] { 
	      			boolean.class 
	      		}	    		
		  	),		  	
		  	// unregisterKeyboardAction(KeyStroke)
		  	super.createMethodDescriptor(getBeanClass(),"unregisterKeyboardAction",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("unregisterKeyboardAction(KeyStroke).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Unregister a keyboard action",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("aKeyStroke", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("unregisterKeyboardAction(KeyStroke).aKeyStroke.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "TRUE to make visible",
	      			})
	      		},
	      		new Class[] { 
	      			javax.swing.KeyStroke.class
	      		}	    		
		  	),
		  	// updateUI()
			super.createMethodDescriptor(getBeanClass(),"updateUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("updateUI().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Reset the UI property",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getTransferHandler()
		  	super.createMethodDescriptor(getBeanClass(),"getTransferHandler",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JComponentMessages.getString("getTransferHandler().Name"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, JComponentMessages.getString("getTransferHandler().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}	    		
		  	),
		  	// setNextFocusableComponent(Component)
		  	super.createMethodDescriptor(getBeanClass(),"setTransferHandler",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("setTransferHandler(TransferHandler).Name"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("aComponent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("setTransferHandler(TransferHandler).aTransferHandler.Name"), //$NON-NLS-1$
	      			})
	      		},
	      		new Class[] { 
		  			TransferHandler.class
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
			// actionMap
			super.createPropertyDescriptor(getBeanClass(),"actionMap", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("actionMap.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("actionMap.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE,
	      		//HIDDEN, Boolean.TRUE
	    		}
	    	),
			// alignmentX
			super.createPropertyDescriptor(getBeanClass(),"alignmentX", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("alignmentX.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("alignmentX.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// alignmentY
			super.createPropertyDescriptor(getBeanClass(),"alignmentY", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("alignmentY.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("alignmentY.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
			// autoscrolls
			super.createPropertyDescriptor(getBeanClass(),"autoscrolls", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("autoscrolls.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("autoscrolls.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
			// border
			super.createPropertyDescriptor(getBeanClass(),"border", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("border.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("border.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE,
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
			// debugGraphicsOptions
			super.createPropertyDescriptor(getBeanClass(),"debugGraphicsOptions", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("debugGraphicsOptions.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("debugGraphicsOptions.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE,
	      		DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
			// doubleBuffered
			super.createPropertyDescriptor(getBeanClass(),"doubleBuffered", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("doubleBuffered.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("doubleBuffered.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// focusCycleRoot
			super.createPropertyDescriptor(getBeanClass(),"focusCycleRoot", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("focusCycleRoot.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("focusCycleRoot.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// focusTraversable
			super.createPropertyDescriptor(getBeanClass(),"focusTraversable", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("focusTraversable.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("focusTraversable.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// height
			super.createPropertyDescriptor(getBeanClass(),"height", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("height.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("height.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// inputVerifier
			super.createPropertyDescriptor(getBeanClass(),"inputVerifier", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("inputVerifier.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("inputVerifier.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE,
	      		BOUND, Boolean.TRUE
	    		}
	    	),
	    	// insets
			super.createPropertyDescriptor(getBeanClass(),"insets", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("insets.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("insets.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// managingFocus
			super.createPropertyDescriptor(getBeanClass(),"managingFocus", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("managingFocus.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("managingFocus.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// maximumSize
			super.createPropertyDescriptor(getBeanClass(),"maximumSize", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("maximumSize.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("maximumSize.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// minimumSize
			super.createPropertyDescriptor(getBeanClass(),"minimumSize", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("minimumSize.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("minimumSize.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// nextFocusableComponent
			super.createPropertyDescriptor(getBeanClass(),"nextFocusableComponent", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("nextFocusableComponent.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JComponentMessages.getString("nextFocusableComponent.Desc"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Next component to get the focus",
	      		EXPERT, Boolean.TRUE,

	    		}
	    	),
	    	// opaque
			super.createPropertyDescriptor(getBeanClass(),"opaque", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("opaque.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("opaque.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// optimizedDrawingEnabled
			super.createPropertyDescriptor(getBeanClass(),"optimizedDrawingEnabled", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("optimizedDrawingEnabled.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("optimizedDrawingEnabled.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// paintingTile
			super.createPropertyDescriptor(getBeanClass(),"paintingTile", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("paintingTile.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("paintingTile.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// preferredSize
			super.createPropertyDescriptor(getBeanClass(),"preferredSize", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("preferredSize.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("preferredSize.Desc"), //$NON-NLS-1$
				BOUND, Boolean.TRUE
	    		}
	    	),
	    	// registeredKeyStrokes
			super.createPropertyDescriptor(getBeanClass(),"registeredKeyStrokes", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("registeredKeyStrokes.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("registeredKeyStrokes.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// requestFocusEnabled
			super.createPropertyDescriptor(getBeanClass(),"requestFocusEnabled", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("requestFocusEnabled.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("requestFocusEnabled.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// rootPane
			super.createPropertyDescriptor(getBeanClass(),"rootPane", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("rootPane.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("rootPane.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// toolTipText
			super.createPropertyDescriptor(getBeanClass(),"toolTipText", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("toolTipText.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("toolTipText.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// topLevelAncestor
			super.createPropertyDescriptor(getBeanClass(),"topLevelAncestor", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("topLevelAncestor.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("topLevelAncestor.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE,
	      		DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
	    	// validateRoot
			super.createPropertyDescriptor(getBeanClass(),"validateRoot", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("validateRoot.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("validateRoot.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// verifyInputWhenFocusTarget
			super.createPropertyDescriptor(getBeanClass(),"verifyInputWhenFocusTarget", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("verifyInputWhenFocusTarget.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("verifyInputWhenFocusTarget.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// visibleRect
			super.createPropertyDescriptor(getBeanClass(),"visibleRect", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("visibleRoot.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("visibleRoot.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// width
			super.createPropertyDescriptor(getBeanClass(),"width", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("width.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("width.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// x
			super.createPropertyDescriptor(getBeanClass(),"x", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("x.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("x.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// y
			super.createPropertyDescriptor(getBeanClass(),"y", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("y.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("y.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// transferHandler
			super.createPropertyDescriptor(getBeanClass(),"transferHandler", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JComponentMessages.getString("transferHandler.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("transferHandler.Desc"), //$NON-NLS-1$
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
/**
 * Gets the componentevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor vetoableChangeEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.beans.PropertyChangeEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.beans.VetoableChangeListener.class,
				"vetoableChange",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComponentMessages.getString("vetoableChange.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComponentMessages.getString("vetoableChange.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("propertyChangeEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComponentMessages.getString("vetoableChange.propertyChangeEvent.Name"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, JComponentMessages.getString("vetoableChange.propertyChangeEvent.Desc"), //$NON-NLS-1$
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"vetoableChange", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JComponentMessages.getString("vetoableChangeEvents.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JComponentMessages.getString("vetoableChangeEvents.Desc"), //$NON-NLS-1$
	      			}, 
						aDescriptorList, java.beans.VetoableChangeListener.class,
						"addVetoableChangeListener", "removeVetoableChangeListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}
}



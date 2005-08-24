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
 *  $RCSfile: JSplitPaneBeanInfo.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.beans.*;

public class JSplitPaneBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JSplitPaneMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jsplitpane");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JSplitPane.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JSplitPaneMessages.getString("JSplitPane.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JSplitPaneMessages.getString("JSplitPane.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/jsplpn32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/jsplpn16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
	    return loadImage("jsplpn32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("jsplpn16.gif"); //$NON-NLS-1$
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
	   			DISPLAYNAME, JSplitPaneMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessible text",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getBottomComponent()
			super.createMethodDescriptor(getBeanClass(),"getBottomComponent",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("getBottomComponent().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSplitPaneMessages.getString("getBottomComponent().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getDividerLocation()
			super.createMethodDescriptor(getBeanClass(),"getDividerLocation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("getDividerLocation().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSplitPaneMessages.getString("getDividerLocation().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getDividerSize()
			super.createMethodDescriptor(getBeanClass(),"getDividerSize",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("getDividerSize().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSplitPaneMessages.getString("getDividerSize().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getLastDividerLocation()
			super.createMethodDescriptor(getBeanClass(),"getLastDividerLocation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("getLastDividerLocation().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSplitPaneMessages.getString("getLastDividerLocation().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getLeftComponent()
			super.createMethodDescriptor(getBeanClass(),"getLeftComponent",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("getLeftComponent().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSplitPaneMessages.getString("getLeftComponent().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getMaximumDividerLocation()
			super.createMethodDescriptor(getBeanClass(),"getMaximumDividerLocation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("getMaximumDividerLocation().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSplitPaneMessages.getString("getMaximumDividerLocation().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMinimumDividerLocation()
			super.createMethodDescriptor(getBeanClass(),"getMinimumDividerLocation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("getMinimumDividerLocation().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSplitPaneMessages.getString("getMinimumDividerLocation().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getOrientation()
			super.createMethodDescriptor(getBeanClass(),"getOrientation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("getOrientation().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSplitPaneMessages.getString("getOrientation().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getResizeWeight()
		  	super.createMethodDescriptor(getBeanClass(),"getResizeWeight", //$NON-NLS-1$
		  	    new Object[] {
		  	    DISPLAYNAME, JSplitPaneMessages.getString("getResizeWeight().Name"), //$NON-NLS-1$
		  	    SHORTDESCRIPTION, JSplitPaneMessages.getString("getResizeWeight().Desc"), //$NON-NLS-1$
		  	    },
		  	    new ParameterDescriptor[] {},
		  	    new Class[] {}
		  	),
		  	// getRightComponent()
			super.createMethodDescriptor(getBeanClass(),"getRightComponent",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("getRightComponent().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSplitPaneMessages.getString("getRightComponent().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getTopComponent()
			super.createMethodDescriptor(getBeanClass(),"getTopComponent",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("getTopComponent().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSplitPaneMessages.getString("getTopComponent().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getUI()
			super.createMethodDescriptor(getBeanClass(),"getUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("getUI().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the UI",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isContinuousLayout()
			super.createMethodDescriptor(getBeanClass(),"isContinuousLayout",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("isContinuousLayout().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Are components continuously redisplayed on user action",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isOneTouchExpandable()
			super.createMethodDescriptor(getBeanClass(),"isOneTouchExpandable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("isOneTouchExpandable().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "If TRUE, divider has expand/collapse UI widget"
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// remove(int)
			super.createMethodDescriptor(getBeanClass(),"remove",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("remove(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSplitPaneMessages.getString("remove(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSplitPaneMessages.getString("remove(int).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of component",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// remove(Component)
			super.createMethodDescriptor(getBeanClass(),"remove",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("remove(Component).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSplitPaneMessages.getString("remove(Component).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("component", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSplitPaneMessages.getString("remove(Component).component.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Component to remove",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Component.class
	      		}		    		
		  	),
		  	// removeAll()
			super.createMethodDescriptor(getBeanClass(),"removeAll",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("removeAll().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSplitPaneMessages.getString("removeAll().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// resetToPreferredSizes()
			super.createMethodDescriptor(getBeanClass(),"resetToPreferredSizes",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("resetToPreferredSizes().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSplitPaneMessages.getString("resetToPreferredSizes().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// setBottomComponent(Component)
			super.createMethodDescriptor(getBeanClass(),"setBottomComponent",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("setBottomComponent(Component).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSplitPaneMessages.getString("setBottomComponent(Component).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("component", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSplitPaneMessages.getString("setBottomComponent(Component).component.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JSplitPaneMessages.getString("setBottomComponent(Component).component.Desc"), //$NON-NLS-1$
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Component.class
	      		}		    		
		  	),
		  	// setContinuousLayout(boolean)
			super.createMethodDescriptor(getBeanClass(),"setContinuousLayout",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("setContinuousLayout(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSplitPaneMessages.getString("setContinuousLayout(boolean).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("aBoolean", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSplitPaneMessages.getString("setContinuousLayout(boolean).continuous.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to display continuously",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setDividerLocation(double)
			super.createMethodDescriptor(getBeanClass(),"setDividerLocation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("setDividerLocation(double).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the divider location",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("proportional", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSplitPaneMessages.getString("setDividerLocation(double).proportional.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Proportional location between  0 and 1",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			double.class
	      		}		    		
		  	),
		  	// setDividerLocation(int)
			super.createMethodDescriptor(getBeanClass(),"setDividerLocation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("setDividerLocation(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the divider location",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("location", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSplitPaneMessages.getString("setDividerLocation(int).location.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Location to set to",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setDividerSize(int)
			super.createMethodDescriptor(getBeanClass(),"setDividerSize",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("setDividerSize(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the divider size",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("newSize", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSplitPaneMessages.getString("setDividerSize(int).newSize.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Size of divider",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setLastDividerLocation(int)
			super.createMethodDescriptor(getBeanClass(),"setLastDividerLocation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("setLastDividerLocation(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the divider location",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("location", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSplitPaneMessages.getString("setDividerSize(int).location.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Location to set to",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setLeftComponent(Component)
			super.createMethodDescriptor(getBeanClass(),"setLeftComponent",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("setLeftComponent(Component).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the top or left component",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("component", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSplitPaneMessages.getString("setLeftComponent(Component).component.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Component to add",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Component.class
	      		}		    		
		  	),
		  	// setOneTouchExpandable(boolean)
			super.createMethodDescriptor(getBeanClass(),"setOneTouchExpandable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("setOneTouchExpandable(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE to display UI widget for expand/collapse on divider",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("aBoolean", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSplitPaneMessages.getString("setOneTouchExpandable(boolean).newValue.Name"), //$NON-NLS-1$
	   					// SHORTDESCRIPTION, "TRUE to display UI widget",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setOrientation(int)
			super.createMethodDescriptor(getBeanClass(),"setOrientation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("setOrientation(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the pane orientation",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("orientation", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSplitPaneMessages.getString("setOrientation(int).orientation.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "VERTICAL or HORIZONTAL split"
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setResizeWeight(double)
		  	super.createMethodDescriptor(getBeanClass(),"setResizeWeight", //$NON-NLS-1$
		  	    new Object[] {
		  	    DISPLAYNAME, JSplitPaneMessages.getString("setResizeWeight(double).Name"), //$NON-NLS-1$
		  	    // SHORTDESCRIPTION, "Set the resize weight",
		  	    },
		  	    new ParameterDescriptor[] {
		  	    	createParameterDescriptor("weight", new Object[] { //$NON-NLS-1$
		  	    		DISPLAYNAME, JSplitPaneMessages.getString("setResizeWeight(double).weight.Name"), //$NON-NLS-1$
		  	    		// SHORTDESCRIPTION, "Resize weight",
		  	    	    }
		  	    	)
		  	    },
		  	    new Class[] {
		  	    	double.class
		  	    }
		  	),
		  	// setRightComponent(Component)
			super.createMethodDescriptor(getBeanClass(),"setRightComponent",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("setRightComponent(Component).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the bottom or right component",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("component", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSplitPaneMessages.getString("setRightComponent(Component).component.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Component to add",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Component.class
	      		}		    		
		  	),
		  	// setTopComponent(Component)
			super.createMethodDescriptor(getBeanClass(),"setTopComponent",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("setTopComponent(Component).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the top or left component",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("component", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSplitPaneMessages.getString("setTopComponent(Component).component.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Component to add",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Component.class
	      		}		    		
		  	),
		  	// setUI(SplitPaneUI)
			super.createMethodDescriptor(getBeanClass(),"setUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSplitPaneMessages.getString("setUI(SplitPaneUI).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the split pane UI",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("ui", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSplitPaneMessages.getString("setUI(SplitPaneUI).anUI.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Split pane UI",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.plaf.SplitPaneUI.class
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
			// bottomComponent
			super.createPropertyDescriptor(getBeanClass(),"bottomComponent", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JSplitPaneMessages.getString("bottomComponent.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JSplitPaneMessages.getString("bottomComponent.Desc"), //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
			// continuousLayout
			super.createPropertyDescriptor(getBeanClass(),"continuousLayout", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JSplitPaneMessages.getString("continuousLayout.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JSplitPaneMessages.getString("continuousLayout.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE
	    		}
	    	),
	    	// dividerLocation
			super.createPropertyDescriptor(getBeanClass(),"dividerLocation", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JSplitPaneMessages.getString("dividerLocation.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JSplitPaneMessages.getString("dividerLocation.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// dividerSize
			super.createPropertyDescriptor(getBeanClass(),"dividerSize", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JSplitPaneMessages.getString("dividerSize.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JSplitPaneMessages.getString("dividerSize.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	    		}
	    	),
	    	// lastDividerLocation
			super.createPropertyDescriptor(getBeanClass(),"lastDividerLocation", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JSplitPaneMessages.getString("lastDividerLocation.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JSplitPaneMessages.getString("lastDividerLocation.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
			// layout - discard from the property sheet
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JSplitPaneMessages.getString("layout.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSplitPaneMessages.getString("layout.Desc"), //$NON-NLS-1$
	      		DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
	    	// leftComponent
			super.createPropertyDescriptor(getBeanClass(),"leftComponent", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JSplitPaneMessages.getString("leftComponent.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JSplitPaneMessages.getString("leftComponent.Desc"), //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
	    	// maximumDividerLocation
			super.createPropertyDescriptor(getBeanClass(),"maximumDividerLocation", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JSplitPaneMessages.getString("maximumDividerLocation.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JSplitPaneMessages.getString("maximumDividerLocation.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// minimumDividerLocation
			super.createPropertyDescriptor(getBeanClass(),"minimumDividerLocation", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JSplitPaneMessages.getString("minimumDividerLocation.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JSplitPaneMessages.getString("minimumDividerLocation.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// oneTouchExpandable
			super.createPropertyDescriptor(getBeanClass(),"oneTouchExpandable", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JSplitPaneMessages.getString("oneTouchExpandable.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JSplitPaneMessages.getString("oneTouchExpandable.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	    		}
	    	),
	    	// orientation
			super.createPropertyDescriptor(getBeanClass(),"orientation", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JSplitPaneMessages.getString("orientation.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JSplitPaneMessages.getString("orientation.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	PREFERRED, Boolean.TRUE,
	      	ENUMERATIONVALUES, new Object[] {
	      			JSplitPaneMessages.getString("orientation.VERTICAL_SPLIT"), new Integer(javax.swing.JSplitPane.VERTICAL_SPLIT), //$NON-NLS-1$
	      				"javax.swing.JSplitPane.VERTICAL_SPLIT", //$NON-NLS-1$
	      			JSplitPaneMessages.getString("orientation.HORIZONTAL_SPLIT"), new Integer(javax.swing.JSplitPane.HORIZONTAL_SPLIT), //$NON-NLS-1$
	      				"javax.swing.JSplitPane.HORIZONTAL_SPLIT", //$NON-NLS-1$
	    		}
	    	}
	    	),
	    	// resizeWeight
	    	super.createPropertyDescriptor(getBeanClass(),"resizeWeight", new Object[] { //$NON-NLS-1$
	    	DISPLAYNAME, JSplitPaneMessages.getString("resizeWeight.Name"), //$NON-NLS-1$
	    	SHORTDESCRIPTION, JSplitPaneMessages.getString("resizeWeight.Desc"), //$NON-NLS-1$
	    	
	    	   }
	    	),
	    	// rightComponent
			super.createPropertyDescriptor(getBeanClass(),"rightComponent", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JSplitPaneMessages.getString("rightComponent.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JSplitPaneMessages.getString("rightComponent.Desc"), //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
	    	// topComponent
			super.createPropertyDescriptor(getBeanClass(),"topComponent", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JSplitPaneMessages.getString("topComponent.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JSplitPaneMessages.getString("topComponent.Desc"), //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
	    	// ui
			super.createPropertyDescriptor(getBeanClass(),"UI", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JSplitPaneMessages.getString("ui.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JSplitPaneMessages.getString("ui.Desc"), //$NON-NLS-1$
	      	//DESIGNTIMEPROPERTY, Boolean.FALSE
			EXPERT, Boolean.TRUE,
			BOUND, Boolean.TRUE,
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

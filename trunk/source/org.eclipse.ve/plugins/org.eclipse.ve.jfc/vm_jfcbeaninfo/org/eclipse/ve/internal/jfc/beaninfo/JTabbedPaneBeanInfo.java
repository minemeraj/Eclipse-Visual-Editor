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
 *  $RCSfile: JTabbedPaneBeanInfo.java,v $
 *  $Revision: 1.8 $  $Date: 2005-10-18 15:32:23 $ 
 */

import java.beans.*;

import org.eclipse.jem.beaninfo.common.IBaseBeanInfoConstants;

public class JTabbedPaneBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JTabbedPaneMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jtabbedpane");  //$NON-NLS-1$

/**
 * Gets the componentevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor changeEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { javax.swing.event.ChangeEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(javax.swing.event.ChangeListener.class,
				"stateChanged",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("stateChanged.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("stateChanged.Desc"), //$NON-NLS-1$
				PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("stateChangeEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JTabbedPaneMessages.getString("stateChangeEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Tab selection changed event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"change", new Object[] { //$NON-NLS-1$
							DISPLAYNAME, JTabbedPaneMessages.getString("changeEvents.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JTabbedPaneMessages.getString("changeEvents.Desc"), //$NON-NLS-1$
	      			}, 
						aDescriptorList, javax.swing.event.ChangeListener.class,
						"addChangeListener", "removeChangeListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JTabbedPane.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JTabbedPaneMessages.getString("JTabbedPane.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JTabbedPaneMessages.getString("JTabbedPane.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/jtabpn32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/jtabpn16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
			changeEventSetDescriptor()
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
	    return loadImage("jtabpn32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("jtabpn16.gif"); //$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
			// addTab(String,Component)
			super.createMethodDescriptor(getBeanClass(),"addTab",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("addTab(String,Component).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("addTab(String,Component).Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("title", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("addTab(String,Component).title.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Title to be displayed",
	      				}
	      			),
	    			createParameterDescriptor("component", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("addTab(String,Component).component.Desc"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Component to add",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			String.class,java.awt.Component.class
	      		}		    		
		  	),
		  	// addTab(String,Icon,Component)
			super.createMethodDescriptor(getBeanClass(),"addTab",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("addTab(String,Icon,Component).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Add a component with icon",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("title", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("addTab(String,Icon,Component).title.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Title to be displayed",
	      				}
	      			),
	      			createParameterDescriptor("icon", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("addTab(String,Icon,Component).icon.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Icon to be displayed",
	      				}
	      			),
	    			createParameterDescriptor("component", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("addTab(String,Icon,Component).component.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Component to add",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			String.class,javax.swing.Icon.class,
	      			java.awt.Component.class
	      		}		    		
		  	),
		  	// addTab(String,Icon,Component,String)
			super.createMethodDescriptor(getBeanClass(),"addTab",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("addTab(String,Icon,Component,String).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Add a component with icon and tooltip",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("title", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("addTab(String,Icon,Component,String).title.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Title to be displayed at tab",
	      				}
	      			),
	      			createParameterDescriptor("icon", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("addTab(String,Icon,Component,String).icon.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Icon to be displayed",
	      				}
	      			),
	      			createParameterDescriptor("component", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("addTab(String,Icon,Component,String).component.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Component to add",
	      				}
	      			),
	      			createParameterDescriptor("tip", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("addTab(String,Icon,Component,String).tip.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tool tip for tab",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			String.class, javax.swing.Icon.class,
	      			java.awt.Component.class, String.class
	      		}		    		
		  	),
		  	// getAccessibleContext()
			super.createMethodDescriptor(getBeanClass(),"getAccessibleContext",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessible text",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getBackgroundAt(int)
			super.createMethodDescriptor(getBeanClass(),"getBackgroundAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("getBackgroundAt(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("getBackgroundAt(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("getBackgroundAt(int).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of tab",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// getBoundsAt(int)
			super.createMethodDescriptor(getBeanClass(),"getBoundsAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("getBoundsAt(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("getBoundsAt(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("getBoundsAt(int).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of tab",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// getComponentAt(int)
			super.createMethodDescriptor(getBeanClass(),"getComponentAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("getComponentAt(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Component at index",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("getComponentAt(int).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of tab",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// getDisabledIconAt(int)
			super.createMethodDescriptor(getBeanClass(),"getDisabledIconAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("getDisabledIconAt(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Disabled tab icon at index",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("getDisabledIconAt(int).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of tab",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// getForegroundAt(int)
			super.createMethodDescriptor(getBeanClass(),"getForegroundAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("getForegroundAt(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("getForegroundAt(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("getForegroundAt(int).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of tab",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// getIconAt(int)
			super.createMethodDescriptor(getBeanClass(),"getIconAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("getIconAt(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("getIconAt(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("getIconAt(int).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of tab",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// getModel()
			super.createMethodDescriptor(getBeanClass(),"getModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("getModel().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the tab selection model",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectedComponent()
			super.createMethodDescriptor(getBeanClass(),"getSelectedComponent",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("getSelectedComponent().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("getSelectedComponent().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectedIndex()
			super.createMethodDescriptor(getBeanClass(),"getSelectedIndex",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("getSelectedIndex().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("getSelectedIndex().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getTabCount()
			super.createMethodDescriptor(getBeanClass(),"getTabCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("getTabCount().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("getTabCount().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getTabPlacement()
			super.createMethodDescriptor(getBeanClass(),"getTabPlacement",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("getTabPlacement().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the placement of tabs",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getTabRunCount()
			super.createMethodDescriptor(getBeanClass(),"getTabRunCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("getTabRunCount().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the number of tab rows or columns",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getTitleAt(int)
			super.createMethodDescriptor(getBeanClass(),"getTitleAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("getTitleAt(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Tab title at index",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("getTitleAt(int).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of tab",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// indexOfComponent(Component)
			super.createMethodDescriptor(getBeanClass(),"indexOfComponent",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("indexOfComponent(Component).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get tab index for component",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("component", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("indexOfComponent(Component).component.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Component to query",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Component.class
	      		}		    		
		  	),
		  	// indexOfTab(String)
			super.createMethodDescriptor(getBeanClass(),"indexOfTab",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("indexOfTab(String).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get tab index for title",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("title", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("indexOfTab(String).title.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tab title",
	      				})
	      		},
	      		new Class[] {
	      			String.class
	      		}		    		
		  	),
		  	// insertTab(String,Icon,Component,String,int)
			super.createMethodDescriptor(getBeanClass(),"insertTab",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("insertTab.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Insert a component with icon and tooltip at index",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("title", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("insertTab.title.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Title to be displayed at tab",
	      				}
	      			),
	      			createParameterDescriptor("icon", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("insertTab.icon.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Icon to be displayed",
	      				}
	      			),
	      			createParameterDescriptor("component", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("insertTab.component.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Component to add",
	      				}
	      			),
	      			createParameterDescriptor("tip", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("insertTab.tip.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tool tip for tab",
	      				}
	      			),
	      			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("insertTab.index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index to insert at",
	      				}
	      			),
	      			
	      		},
	      		new Class[] {
	      			String.class, javax.swing.Icon.class,
	      			java.awt.Component.class, String.class, int.class
	      		}		    		
		  	),
		  	// isEnabledAt(int)
			super.createMethodDescriptor(getBeanClass(),"isEnabledAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("isEnabledAt(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("isEnabledAt(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("isEnabledAt(int).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of tab",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// removeTabAt(int)
			super.createMethodDescriptor(getBeanClass(),"removeTabAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("removeTabAt(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("removeTabAt(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("removeTabAt(int).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of tab",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
			// setBackgroundAt(int,Color)
			super.createMethodDescriptor(getBeanClass(),"setBackgroundAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("setBackgroundAt(int,Color).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("setBackgroundAt(int,Color).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("setBackgroundAt(int,Color).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of tab",
	      				}),
	      			createParameterDescriptor("background", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("setBackgroundAt(int,Color).background.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Background color",
	      				})
	      		},
	      		new Class[] {
	      			int.class, java.awt.Color.class
	      		}		    		
		  	),
		  	// setComponentAt(int,Component)
			super.createMethodDescriptor(getBeanClass(),"setComponentAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("setComponentAt(int,Component).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("setComponentAt(int,Component).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("setComponentAt(int,Component).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of tab",
	      				}),
	      			createParameterDescriptor("component", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("setComponentAt(int,Component).component.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Component to add",
	      				})
	      		},
	      		new Class[] {
	      			int.class, java.awt.Component.class
	      		}		    		
		  	),
		  	// setDisabledIconAt(int,Icon)
			super.createMethodDescriptor(getBeanClass(),"setDisabledIconAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("setDisabledIconAt(int,Icon).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the disabled icon at tab index",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("setDisabledIconAt(int,Icon).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of tab",
	      				}),
	      			createParameterDescriptor("icon", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("setDisabledIconAt(int,Icon).icon.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Icon to display disabled state",
	      				})
	      		},
	      		new Class[] {
	      			int.class, javax.swing.Icon.class
	      		}		    		
		  	),
		  	// setEnabledAt(int,boolean)
			super.createMethodDescriptor(getBeanClass(),"setEnabledAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("setEnabledAt(int,boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Enable or disable tab at index",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("setEnabledAt(int,boolean).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of tab",
	      				}),
	      			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("setEnabledAt(int,boolean).aBoolean.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to enable",
	      				})
	      		},
	      		new Class[] {
	      			int.class, boolean.class
	      		}		    		
		  	),
		  	// setForegroundAt(int,Color)
			super.createMethodDescriptor(getBeanClass(),"setForegroundAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("setForegroundAt(int,Color).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("setForegroundAt(int,Color).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("setForegroundAt(int,Color).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of tab",
	      				}),
	      			createParameterDescriptor("foreground", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("setForegroundAt(int,Color).foreground.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Foreground color",
	      				})
	      		},
	      		new Class[] {
	      			int.class, java.awt.Color.class
	      		}		    		
		  	),
			// setIconAt(int,Icon)
			super.createMethodDescriptor(getBeanClass(),"setIconAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("setIconAt(int,Icon).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("setIconAt(int,Icon).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("setIconAt(int,Icon).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of tab",
	      				}),
	      			createParameterDescriptor("icon", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("setIconAt(int,Icon).icon.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Icon to display enabled state",
	      				})
	      		},
	      		new Class[] {
	      			int.class, javax.swing.Icon.class
	      		}		    		
		  	),
		  	// setModel(SingleSelectionModel)
			super.createMethodDescriptor(getBeanClass(),"setModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("setModel(SingleSelectionModel).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("setModel(SingleSelectionModel).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("model", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("setModel(SingleSelectionModel).model.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JTabbedPaneMessages.getString("setModel(SingleSelectionModel).model.Desc"), //$NON-NLS-1$
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.SingleSelectionModel.class
	      		}		    		
		  	),
		  	// setSelectedComponent(Component)
			super.createMethodDescriptor(getBeanClass(),"setSelectedComponent",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("setSelectedComponent(Component).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Select the component",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("component", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("setSelectedComponent(Component).component.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Component to select",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Component.class
	      		}		    		
		  	),
		  	// setSelectedIndex(int)
			super.createMethodDescriptor(getBeanClass(),"setSelectedIndex",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("setSelectedIndex(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the currently selected index",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("setSelectedIndex(int).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of tab",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setTabPlacement(int)
			super.createMethodDescriptor(getBeanClass(),"setTabPlacement",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("setTabPlacement(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the tab placement",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("placement", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("setTabPlacement(int).placement.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TOP, LEFT, RIGHT, BOTTOM",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setTitleAt(int,String)
			super.createMethodDescriptor(getBeanClass(),"setTitleAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("setTitleAt(int,String).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set tab title at index",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("setTitleAt(int,String).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of tab",
	      				}),
	      			createParameterDescriptor("title", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("setTitleAt(int,String).title.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tab title",
	      				})
	      		},
	      		new Class[] {
	      			int.class, String.class
	      		}		    		
		  	),
		  	// setUI(TabbedPaneUI)
			super.createMethodDescriptor(getBeanClass(),"setUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTabbedPaneMessages.getString("setUI(TabbedPaneUI).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the tabbed pane LookAndFeel UI",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("ui", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTabbedPaneMessages.getString("setUI(TabbedPaneUI).anUI.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tabbed pane UI",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.plaf.TabbedPaneUI.class
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
	      		DISPLAYNAME, JTabbedPaneMessages.getString("layout.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("layout.Desc"), //$NON-NLS-1$
	      		DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
			// model
			super.createPropertyDescriptor(getBeanClass(),"model", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JTabbedPaneMessages.getString("model.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("model.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE,
	      		//DESIGNTIMEPROPERTY, Boolean.FALSE
				EXPERT, Boolean.TRUE
	    		}
	    	),
			// selectedComponent
			super.createPropertyDescriptor(getBeanClass(),"selectedComponent", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JTabbedPaneMessages.getString("selectedComponent.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("selectedComponent.Desc"), //$NON-NLS-1$
	      		DESIGNTIMEPROPERTY, Boolean.FALSE
				//EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// selectedIndex
			super.createPropertyDescriptor(getBeanClass(),"selectedIndex", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JTabbedPaneMessages.getString("selectedIndex.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("selectedIndex.Desc"), //$NON-NLS-1$
	      		DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
	    	// tabCount
			super.createPropertyDescriptor(getBeanClass(),"tabCount", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JTabbedPaneMessages.getString("tabCount.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("tabCount.Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}
	    	),
	    	// tabPlacement
			super.createPropertyDescriptor(getBeanClass(),"tabPlacement", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JTabbedPaneMessages.getString("tabPlacement.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("tabPlacement.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE,
	      		PREFERRED, Boolean.TRUE,
	      		IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      			JTabbedPaneMessages.getString("tabPlacement.TOP"), new Integer(javax.swing.JTabbedPane.TOP), //$NON-NLS-1$
	      				"javax.swing.JTabbedPane.TOP", //$NON-NLS-1$
	      			JTabbedPaneMessages.getString("tabPlacement.LEFT"), new Integer(javax.swing.JTabbedPane.LEFT), //$NON-NLS-1$
	      				"javax.swing.JTabbedPane.LEFT", //$NON-NLS-1$
	      			JTabbedPaneMessages.getString("tabPlacement.BOTTOM"), new Integer(javax.swing.JTabbedPane.BOTTOM), //$NON-NLS-1$
	      				"javax.swing.JTabbedPane.BOTTOM", //$NON-NLS-1$
	      			JTabbedPaneMessages.getString("tabPlacement.RIGHT"), new Integer(javax.swing.JTabbedPane.RIGHT), //$NON-NLS-1$
	      				"javax.swing.JTabbedPane.RIGHT"} //$NON-NLS-1$
	    		}
	    	),
	    	// tabRunCount
			super.createPropertyDescriptor(getBeanClass(),"tabRunCount", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JTabbedPaneMessages.getString("tabRunCount.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("tabRunCount.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE	      	
	    		}
	    	),
			// ui
			super.createPropertyDescriptor(getBeanClass(),"UI", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JTabbedPaneMessages.getString("ui.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTabbedPaneMessages.getString("ui.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE,
	      		BOUND, Boolean.TRUE	      	
	    		}
	    	),
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}
}

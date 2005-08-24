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
 *  $RCSfile: JPopupMenuBeanInfo.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.beans.*;

import javax.swing.plaf.PopupMenuUI;

public class JPopupMenuBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JPopupMenuMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jpopupmenu");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JPopupMenu.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JPopupMenuMessages.getString("JPopupMenu.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JPopupMenuMessages.getString("JPopupMenu.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/popupm32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/popupm16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
			popupMenuEventSetDescriptor()
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
	    return loadImage("popupm32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("popupm16.gif"); //$NON-NLS-1$
   	return super.getIcon(kind);
}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
			// add(Action)
			super.createMethodDescriptor(getBeanClass(),"add",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("add(Action).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JPopupMenuMessages.getString("add(Action).Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("action", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPopupMenuMessages.getString("add(Action).action.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Action object",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.Action.class
	      		}		    		
		  	),
		  	// add(JMenuItem)
			super.createMethodDescriptor(getBeanClass(),"add",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("add(JMenuItem).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JPopupMenuMessages.getString("add(JMenuItem).Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("menuItem", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPopupMenuMessages.getString("add(JMenuItem).menuItem.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Menu item",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.JMenuItem.class
	      		}		    		
		  	),
		  	// add(Component)
			super.createMethodDescriptor(getBeanClass(),"add",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("add(Component).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Add a component to the menu",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("comp", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPopupMenuMessages.getString("add(Component).component.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Component",
	      				})
	      		},
	      		new Class[] {
	      			java.awt.Component.class
	      		}		    		
		  	),
		  	// addSeparator()
			super.createMethodDescriptor(getBeanClass(),"addSeparator",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("addSeparator()_24"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Add a separator",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getAccessibleContext()
			super.createMethodDescriptor(getBeanClass(),"getAccessibleContext",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessible context",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getComponentAtIndex(int)
			super.createMethodDescriptor(getBeanClass(),"getComponentAtIndex",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("getComponentAtIndex(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the menu component at specified position",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("pos", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPopupMenuMessages.getString("getComponentAtIndex(int).position.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "position",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// getLabel()
			super.createMethodDescriptor(getBeanClass(),"getLabel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("getLabel().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the popup menu's label"
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMargin()
			super.createMethodDescriptor(getBeanClass(),"getMargin",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("getMargin().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the border insets"
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectionModel()
			super.createMethodDescriptor(getBeanClass(),"getSelectionModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("getSelectionModel().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the selection model"
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// insert(Component,int)
			super.createMethodDescriptor(getBeanClass(),"insert",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("insert(Component,int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Insert the component at specified position",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("menu", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPopupMenuMessages.getString("insert(Component,int).component.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Menu item component",
	      				}),
	    			createParameterDescriptor("pos", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPopupMenuMessages.getString("insert(Component,int).position.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "position"
	      				})
	      		},
	      		new Class[] {
	      			java.awt.Component.class, int.class
	      		}		    		
		  	),
		  	// isLightWeightPopupEnabled
			super.createMethodDescriptor(getBeanClass(),"isLightWeightPopupEnabled",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("isLightWeightPopupEnabled.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JPopupMenuMessages.getString("isLightWeightPopupEnabled.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isVisible()
			super.createMethodDescriptor(getBeanClass(),"isVisible",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("isVisible().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JPopupMenuMessages.getString("isVisible().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setBorderPainted(boolean)
			super.createMethodDescriptor(getBeanClass(),"setBorderPainted",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("setBorderPainted(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set whether the border is painted",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPopupMenuMessages.getString("setBorderPainted(boolean).aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to paint border",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setLabel(String)
			super.createMethodDescriptor(getBeanClass(),"setLabel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("setLabel(String).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the label",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("text", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPopupMenuMessages.getString("setLabel(String).aLabel.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "New label",
	      				})
	      		},
	      		new Class[] {
	      			String.class
	      		}		    		
		  	),
		  	// setLightWeightPopupEnabled(boolean)
			super.createMethodDescriptor(getBeanClass(),"setLightWeightPopupEnabled",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("setLightWeightPopupEnabled(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "When displaying the popup, choose to use a light weight popup if it fits.",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPopupMenuMessages.getString("setLightWeightPopupEnabled(boolean).aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to enable lightweight",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setLocation(int,int)
			super.createMethodDescriptor(getBeanClass(),"setLocation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("setLocation(int,int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the menu at given location",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("x", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPopupMenuMessages.getString("setLocation(int,int).x.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "x coordinate",
	      				}),
	      			createParameterDescriptor("y", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPopupMenuMessages.getString("setLocation(int,int).y.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "y coordinate",
	      				})
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// setPopupSize(int,int)
			super.createMethodDescriptor(getBeanClass(),"setPopupSize",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("setPopupSize(int,int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the size of the popup",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("wd", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPopupMenuMessages.getString("setPopupSize(int,int).width.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "width",
	      				}),
	      			createParameterDescriptor("ht", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPopupMenuMessages.getString("setPopupSize(int,int).height.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "height"
	      				})
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// setPopupSize(Dimension)
			super.createMethodDescriptor(getBeanClass(),"setPopupSize",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("setPopupSize(Dimension).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the size of the popup",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("dim", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPopupMenuMessages.getString("setPopupSize(Dimension).dimension.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "dimension",
	      				})
	      		},
	      		new Class[] {
	      			java.awt.Dimension.class
	      		}		    		
		  	),
		  	// setSelectionModel(SingleSelectionModel)
			super.createMethodDescriptor(getBeanClass(),"setSelectionModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("setSelectionModel(SingleSelectionModel).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the selection model",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("model", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPopupMenuMessages.getString("setSelectionModel(SingleSelectionModel).model.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "model",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.SingleSelectionModel.class
	      		}		    		
		  	),
		  	// setVisible(boolean)
			super.createMethodDescriptor(getBeanClass(),"setVisible",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("setVisible(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JPopupMenuMessages.getString("setVisible(boolean).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("bool", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPopupMenuMessages.getString("setVisible(boolean).aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to show",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// show(Component,int,int)
			super.createMethodDescriptor(getBeanClass(),"show",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("show(Component,int,int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JPopupMenuMessages.getString("show(Component,int,int).Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("invoker", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPopupMenuMessages.getString("show(Component,int,int).invoker.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "parent component",
	      				}),
	    			createParameterDescriptor("x", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPopupMenuMessages.getString("show(Component,int,int).x.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "x coordinate"
	      				}),
	      			createParameterDescriptor("y", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPopupMenuMessages.getString("show(Component,int,int).y.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "y coordinate"
	      				})
	      		},
	      		new Class[] {
	      			java.awt.Component.class, int.class, int.class
	      		}		    		
		  	),
		  	// getInvoker()
			super.createMethodDescriptor(getBeanClass(),"getInvoker",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("getInvoker().Name"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setInvoker(Component)
			super.createMethodDescriptor(getBeanClass(),"setInvoker",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("setInvoker(Component).Name"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("invoker", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPopupMenuMessages.getString("setInvoker(Component).aComponent.Name"), //$NON-NLS-1$
	      				})
	      		},
	      		new Class[] {
	      			java.awt.Component.class
	      		}		    		
		  	),
		  	// getUI()
			super.createMethodDescriptor(getBeanClass(),"getUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("getUI().Name"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setInvoker(Component)
			super.createMethodDescriptor(getBeanClass(),"setUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("setUI(PopupMenuUI).Name"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("popupMenuUI", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPopupMenuMessages.getString("setUI(PopupMenuUI).popupMenuUI.Name"), //$NON-NLS-1$
	      				})
	      		},
	      		new Class[] {
					PopupMenuUI.class
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
			// borderPainted
			super.createPropertyDescriptor(getBeanClass(),"borderPainted", new Object[] { //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Whether the border should be painted",
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// label
			super.createPropertyDescriptor(getBeanClass(),"label", new Object[] { //$NON-NLS-1$
	      		SHORTDESCRIPTION, JPopupMenuMessages.getString("label.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// lightWeightPopupEnabled
			super.createPropertyDescriptor(getBeanClass(),"lightWeightPopupEnabled", new Object[] { //$NON-NLS-1$
	      	SHORTDESCRIPTION, JPopupMenuMessages.getString("lightWeightPopupEnabled.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	    		}
	    	),
			// layout - discard from the property sheet
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
	      		DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
	    	// margin
			super.createPropertyDescriptor(getBeanClass(),"margin", new Object[] { //$NON-NLS-1$
	      		SHORTDESCRIPTION, JPopupMenuMessages.getString("margin.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// popupSize
			super.createPropertyDescriptor(getBeanClass(),"popupSize", new Object[] { //$NON-NLS-1$
	      		SHORTDESCRIPTION, JPopupMenuMessages.getString("popupSize.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// selectionModel
			super.createPropertyDescriptor(getBeanClass(),"selectionModel", new Object[] { //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "The model delegate to handle single selections",
	      		EXPERT, Boolean.TRUE,
	      		//DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
	    	// visible
			super.createPropertyDescriptor(getBeanClass(),"visible", new Object[] { //$NON-NLS-1$
	      		SHORTDESCRIPTION, JPopupMenuMessages.getString("visible.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// invoker
			super.createPropertyDescriptor(getBeanClass(),"invoker", new Object[] { //$NON-NLS-1$
	      		SHORTDESCRIPTION, JPopupMenuMessages.getString("invoker.Desc"), //$NON-NLS-1$
				HIDDEN, Boolean.TRUE
	    		}
	    	),
	    	// ui
			super.createPropertyDescriptor(getBeanClass(),"UI", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JPopupMenuMessages.getString("ui.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JPopupMenuMessages.getString("ui.Desc"), //$NON-NLS-1$
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
/**
 * Gets the actionevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public java.beans.EventSetDescriptor popupMenuEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { javax.swing.event.PopupMenuEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(javax.swing.event.PopupMenuListener.class,
				"popupMenuCanceled",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("popupMenuCanceled.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JPopupMenuMessages.getString("popupMenuCanceled.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("menuEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JPopupMenuMessages.getString("popupMenuCanceled.menuEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Event on canceling popup menu",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(javax.swing.event.PopupMenuListener.class,
				"popupMenuWillBecomeInvisible",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("popupMenuWillBecomeInvisible.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JPopupMenuMessages.getString("popupMenuWillBecomeInvisible.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("menuEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JPopupMenuMessages.getString("popupMenuWillBecomeInvisible.menuEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Event when popup becomes invisible",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(javax.swing.event.PopupMenuListener.class,
				"popupMenuWillBecomeVisible",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPopupMenuMessages.getString("popupMenuWillBecomeVisible.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JPopupMenuMessages.getString("popupMenuWillBecomeVisible.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("menuEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JPopupMenuMessages.getString("popupMenuWillBecomeVisible.menuEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Event on deselecting menu",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"popupMenu", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JPopupMenuMessages.getString("popupMenuEvents.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JPopupMenuMessages.getString("popupMenuEvents.Desc"), //$NON-NLS-1$
	      			}, 
						aDescriptorList, javax.swing.event.PopupMenuListener.class,
						"addPopupMenuListener", "removePopupMenuListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}
}

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
 *  $RCSfile: JToolBarBeanInfo.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.beans.*;

public class JToolBarBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JToolBarMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jtoolbar");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JToolBar.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JToolBarMessages.getString("JToolBar.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JToolBarMessages.getString("JToolBar.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/jtoolb32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/jtoolb16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
	    return loadImage("jtoolb32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("jtoolb16.gif"); //$NON-NLS-1$
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
	   			DISPLAYNAME, JToolBarMessages.getString("add(Action).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JToolBarMessages.getString("add(Action).Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("action", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JToolBarMessages.getString("add(Action).action.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Action object",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.Action.class
	      		}		    		
		  	),
		  	// addSeparator()
			super.createMethodDescriptor(getBeanClass(),"addSeparator",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JToolBarMessages.getString("addSeparator().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Add a separator",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getComponentAtIndex(int)
			super.createMethodDescriptor(getBeanClass(),"getComponentAtIndex",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JToolBarMessages.getString("getComponentAtIndex(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the component at specified position",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("pos", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JToolBarMessages.getString("getComponentAtIndex(int).position.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "position",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// getMargin()
			super.createMethodDescriptor(getBeanClass(),"getMargin",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JToolBarMessages.getString("getMargin().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the toolbar's border insets"
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getOrientation()
			super.createMethodDescriptor(getBeanClass(),"getOrientation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JToolBarMessages.getString("getOrientation().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JToolBarMessages.getString("getOrientation().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// isBorderPainted()
			super.createMethodDescriptor(getBeanClass(),"isBorderPainted",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JToolBarMessages.getString("isBorderPainted().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Check whether the border should be painted"
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isFloatable()
			super.createMethodDescriptor(getBeanClass(),"isFloatable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JToolBarMessages.getString("isFloatable().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JToolBarMessages.getString("isFloatable().Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setBorderPainted(boolean)
			super.createMethodDescriptor(getBeanClass(),"setBorderPainted",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JToolBarMessages.getString("setBorderPainted(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set whether the border is painted",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JToolBarMessages.getString("setBorderPainted(boolean).aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to paint border",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setFloatable(boolean)
			super.createMethodDescriptor(getBeanClass(),"setFloatable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JToolBarMessages.getString("setFloatable(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set whether the toolbar can be dragged out",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JToolBarMessages.getString("setFloatable(boolean).aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to float toolbar",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setMargin(Insets)
			super.createMethodDescriptor(getBeanClass(),"setMargin",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JToolBarMessages.getString("setMargin(Insets).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the border insets",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("insets", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JToolBarMessages.getString("setMargin(Insets).insets.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Border insets",
	      				})
	      		},
	      		new Class[] {
	      			java.awt.Insets.class
	      		}		    		
		  	),
		  	// setOrientation(int)
			super.createMethodDescriptor(getBeanClass(),"setOrientation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JToolBarMessages.getString("setOrientation(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the toolbar orientation",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("orientation", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JToolBarMessages.getString("setOrientation(int).orientation.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "VERTICAL or HORIZONTAL"
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// getUI()
			super.createMethodDescriptor(getBeanClass(),"getUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JToolBarMessages.getString("getUI().Name"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setUI(ToolBarUI)
			super.createMethodDescriptor(getBeanClass(),"setUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JToolBarMessages.getString("setUI(ToolBarUI).Name"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("toolBarUI", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JToolBarMessages.getString("setUI(ToolBarUI).toolBarUI.Name"), //$NON-NLS-1$
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.plaf.ToolBarUI.class
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
			super.createPropertyDescriptor(getBeanClass(),"border", new Object[] { //$NON-NLS-1$
	      		BOUND, Boolean.TRUE,
	      		EXPERT, Boolean.TRUE,
	    		}
	    	),
			// borderPainted
			super.createPropertyDescriptor(getBeanClass(),"borderPainted", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JToolBarMessages.getString("borderPainted.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JToolBarMessages.getString("borderPainted.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// floatable
			super.createPropertyDescriptor(getBeanClass(),"floatable", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JToolBarMessages.getString("floatable.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JToolBarMessages.getString("floatable.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE,
	    		}
	    	),
			// layout - discard from the property sheet
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JToolBarMessages.getString("layout.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JToolBarMessages.getString("layout.Desc"), //$NON-NLS-1$
	      		DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
	    	// margin
			super.createPropertyDescriptor(getBeanClass(),"margin", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JToolBarMessages.getString("margin.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JToolBarMessages.getString("margin.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE,
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// orientation
			super.createPropertyDescriptor(getBeanClass(),"orientation", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JToolBarMessages.getString("orientation.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JToolBarMessages.getString("orientation.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	PREFERRED, Boolean.TRUE,
	      	ENUMERATIONVALUES, new Object[] {
	      			JToolBarMessages.getString("orientation.VERTICAL"), new Integer(javax.swing.JToolBar.VERTICAL), //$NON-NLS-1$
	      				"javax.swing.JToolBar.VERTICAL", //$NON-NLS-1$
	      			JToolBarMessages.getString("orientation.HORIZONTAL"), new Integer(javax.swing.JToolBar.HORIZONTAL), //$NON-NLS-1$
	      				"javax.swing.JToolBar.HORIZONTAL", //$NON-NLS-1$
	    		}
	    	}
	    	),
	    	// ui
			super.createPropertyDescriptor(getBeanClass(),"UI", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JToolBarMessages.getString("ui.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JToolBarMessages.getString("ui.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE,
	      		EXPERT, Boolean.TRUE
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

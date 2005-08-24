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
 *  $RCSfile: JMenuBarBeanInfo.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.beans.*;

import javax.swing.plaf.MenuBarUI;

public class JMenuBarBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle JMenuBarMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jmenubar");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JMenuBar.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JMenuBarMessages.getString("JMenuBar.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JMenuBarMessages.getString("JMenuBar.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/menubr32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/menubr16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
	    return loadImage("menubr32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("menubr16.gif"); //$NON-NLS-1$
   	return super.getIcon(kind);
}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
			// add(JMenu)
			super.createMethodDescriptor(getBeanClass(),"add",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuBarMessages.getString("add(JMenu).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuBarMessages.getString("add(JMenu).Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("menu", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuBarMessages.getString("add(JMenu).menu.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JMenuBarMessages.getString("add(JMenu).menu.Desc"), //$NON-NLS-1$
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.JMenu.class
	      		}		    		
		  	),
		  	// getComponentAtIndex(int)
			super.createMethodDescriptor(getBeanClass(),"getComponentAtIndex",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuBarMessages.getString("getComponentAtIndex(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuBarMessages.getString("getComponentAtIndex(int).Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("pos", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuBarMessages.getString("getComponentAtIndex(int).position.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JMenuBarMessages.getString("getComponentAtIndex(int).position.Desc"), //$NON-NLS-1$
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// getMargin()
			super.createMethodDescriptor(getBeanClass(),"getMargin",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuBarMessages.getString("getMargin().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuBarMessages.getString("getMargin().Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMenu(int)
			super.createMethodDescriptor(getBeanClass(),"getMenu",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuBarMessages.getString("getMenu(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuBarMessages.getString("getMenu(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("pos", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuBarMessages.getString("getMenu(int).position.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JMenuBarMessages.getString("getMenu(int).position.Desc"), //$NON-NLS-1$
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// getMenuCount()
			super.createMethodDescriptor(getBeanClass(),"getMenuCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuBarMessages.getString("getMenuCount().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuBarMessages.getString("getMenuCount().Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectionModel()
			super.createMethodDescriptor(getBeanClass(),"getSelectionModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuBarMessages.getString("getSelectionModel().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuBarMessages.getString("getSelectionModel().Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// isBorderPainted()
			super.createMethodDescriptor(getBeanClass(),"isBorderPainted",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuBarMessages.getString("isBorderPainted().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuBarMessages.getString("isBorderPainted().Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setBorderPainted(boolean)
			super.createMethodDescriptor(getBeanClass(),"setBorderPainted",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuBarMessages.getString("setBorderPainted(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuBarMessages.getString("setBorderPainted(boolean).Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuBarMessages.getString("setBorderPainted(boolean).aBool.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JMenuBarMessages.getString("setBorderPainted(boolean).aBool.Desc"), //$NON-NLS-1$
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setMargin(Insets)
			super.createMethodDescriptor(getBeanClass(),"setMargin",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuBarMessages.getString("setMargin(Insets).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuBarMessages.getString("setMargin(Insets).Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("insets", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuBarMessages.getString("setMargin(Insets).insets.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JMenuBarMessages.getString("setMargin(Insets).insets.Desc"), //$NON-NLS-1$
	      				})
	      		},
	      		new Class[] {
	      			java.awt.Insets.class
	      		}		    		
		  	),
		  	// setSelectionModel(SingleSelectionModel)
			super.createMethodDescriptor(getBeanClass(),"setSelectionModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuBarMessages.getString("setSelectionModel(SingleSelectionModel).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuBarMessages.getString("setSelectionModel(SingleSelectionModel).Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("model", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuBarMessages.getString("setSelectionModel(SingleSelectionModel).model.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JMenuBarMessages.getString("setSelectionModel(SingleSelectionModel).model.Desc"), //$NON-NLS-1$
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.SingleSelectionModel.class
	      		}		    		
		  	),
		  	// getHelpMenu()
			super.createMethodDescriptor(getBeanClass(),"getHelpMenu",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuBarMessages.getString("getHelpMenu().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuBarMessages.getString("getHelpMenu().Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setHelpMenu(JMenu)
			super.createMethodDescriptor(getBeanClass(),"setHelpMenu",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuBarMessages.getString("setHelpMenu(JMenu).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuBarMessages.getString("setHelpMenu(JMenu).Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("helpMenu", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuBarMessages.getString("setHelpMenu(JMenu).helpMenu.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JMenuBarMessages.getString("setHelpMenu(JMenu).helpMenu.Desc"), //$NON-NLS-1$
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.JMenu.class
	      		}		    		
		  	),
		  	// getUI()
			super.createMethodDescriptor(getBeanClass(),"getUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuBarMessages.getString("getUI().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuBarMessages.getString("getUI().Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setUI(MenuBarUI)
			super.createMethodDescriptor(getBeanClass(),"setUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JMenuBarMessages.getString("setUI(MenuBarUI).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuBarMessages.getString("setUI(MenuBarUI).Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("menuBarUI", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JMenuBarMessages.getString("setUI(MenuBarUI).ui.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JMenuBarMessages.getString("setUI(MenuBarUI).ui.Desc"), //$NON-NLS-1$
	      				})
	      		},
	      		new Class[] {
	      			MenuBarUI.class
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
	      		DISPLAYNAME, JMenuBarMessages.getString("borderPainted.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuBarMessages.getString("borderPainted.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
			// layout - discard from the property sheet
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
	      		DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
	    	// margin
			super.createPropertyDescriptor(getBeanClass(),"margin", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JMenuBarMessages.getString("margin.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuBarMessages.getString("margin.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// menuCount
			super.createPropertyDescriptor(getBeanClass(),"menuCount", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JMenuBarMessages.getString("menuCount.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuBarMessages.getString("menuCount.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// selectionModel
			super.createPropertyDescriptor(getBeanClass(),"selectionModel", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JMenuBarMessages.getString("selectionModel.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuBarMessages.getString("selectionModel.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE,
	      		BOUND, Boolean.TRUE
	    		}
	    	),
	    	// helpMenu
			super.createPropertyDescriptor(getBeanClass(),"helpMenu", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JMenuBarMessages.getString("helpMenu.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuBarMessages.getString("helpMenu.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE,
	      		DESIGNTIMEPROPERTY, Boolean.FALSE //TODO - not implemented as of 1.4; check again for 1.5 (aka Java 5) + up
	    		}
	    	),
	    	// UI
			super.createPropertyDescriptor(getBeanClass(),"UI", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JMenuBarMessages.getString("ui.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JMenuBarMessages.getString("ui.Desc"), //$NON-NLS-1$
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

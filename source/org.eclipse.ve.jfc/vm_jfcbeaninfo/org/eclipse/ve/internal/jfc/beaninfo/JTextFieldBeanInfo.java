package org.eclipse.ve.internal.jfc.beaninfo;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JTextFieldBeanInfo.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:33 $ 
 */

import java.beans.*;

public class JTextFieldBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JTextFieldMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jtextfield");  //$NON-NLS-1$

/**
 * Gets the actionevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public java.beans.EventSetDescriptor actionEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.ActionEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.ActionListener.class,
				"actionPerformed",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextFieldMessages.getString("actionPerformed.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextFieldMessages.getString("actionPerformed.Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("actionEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JTextFieldMessages.getString("actionPerformed.actionEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Event on ENTER",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"action", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JTextFieldMessages.getString("actionEvents.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JTextFieldMessages.getString("actionEvents.Desc"), //$NON-NLS-1$
	      				INDEFAULTEVENTSET, Boolean.TRUE,
	      			}, 
						aDescriptorList, java.awt.event.ActionListener.class,
						"addActionListener", "removeActionListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JTextField.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JTextFieldMessages.getString("JTextField.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JTextFieldMessages.getString("JTextField.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/txtfld32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/txtfld16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
			actionEventSetDescriptor()
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
	    return loadImage("txtfld32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("txtfld16.gif"); //$NON-NLS-1$
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
	   			DISPLAYNAME, JTextFieldMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessibility context",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getActions()
			super.createMethodDescriptor(getBeanClass(),"getActions",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextFieldMessages.getString("getActions().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextFieldMessages.getString("getActions().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getColumns()
			super.createMethodDescriptor(getBeanClass(),"getColumns",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextFieldMessages.getString("getColumns().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextFieldMessages.getString("getColumns().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getHorizontalAlignment()
			super.createMethodDescriptor(getBeanClass(),"getHorizontalAlignment",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextFieldMessages.getString("getHorizontalAlignment().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the horizontal alignment",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMinimumSize()
			super.createMethodDescriptor(getBeanClass(),"getMinimumSize",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextFieldMessages.getString("getMinimumSize().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the minimum size",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getPreferredSize()
			super.createMethodDescriptor(getBeanClass(),"getPreferredSize",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextFieldMessages.getString("getPreferredSize().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the preferred size",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getScrollOffset()
			super.createMethodDescriptor(getBeanClass(),"getScrollOffset",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextFieldMessages.getString("getScrollOffset().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the Scroll Offset",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setActionCommand(String)
			super.createMethodDescriptor(getBeanClass(),"setActionCommand",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextFieldMessages.getString("setActionCommand(String).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the command string identifier for action events",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("string", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextFieldMessages.getString("setActionCommand(String).command.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "action identifier",
	      				})
	      		},
	      		new Class[] {
	      			String.class
	      		}		    		
		  	),
		  	// setColumns(int)
			super.createMethodDescriptor(getBeanClass(),"setColumns",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextFieldMessages.getString("setColumns(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the number of columns",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("num", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextFieldMessages.getString("setColumns(int).columns.Namr"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Number of columns",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setFont(Font)
			super.createMethodDescriptor(getBeanClass(),"setFont",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextFieldMessages.getString("setFont(Font).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the font",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("font", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextFieldMessages.getString("setFont(Font).font.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Font",
	      				})
	      		},
	      		new Class[] {
	      			java.awt.Font.class
	      		}		    		
		  	),
		  	// setHorizontalAlignment(int)
			super.createMethodDescriptor(getBeanClass(),"setHorizontalAlignment",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextFieldMessages.getString("setHorizontalAlignment(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the horizontal alignment",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("alignment", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextFieldMessages.getString("setHorizontalAlignment(int).alignment.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "CENTER, LEFT or RIGHT",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setScrollOffset(int)
			super.createMethodDescriptor(getBeanClass(),"setScrollOffset",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextFieldMessages.getString("setScrollOffset(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the scroll offset",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("scrollOffset", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextFieldMessages.getString("setScrollOffset(int).scrollOffset.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "The scroll offset",
	      				})
	      		},
	      		new Class[] {
	      			int.class
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
			// accessibleContext
			super.createPropertyDescriptor(getBeanClass(),"accessibleContext", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextFieldMessages.getString("acessibleContext.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextFieldMessages.getString("acessibleContext.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	OBSCURE, Boolean.TRUE
	    		}
	    	),
	    	// actionCommand
			super.createPropertyDescriptor(getBeanClass(),"actionCommand", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextFieldMessages.getString("actionCommand.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextFieldMessages.getString("actionCommand.Desc"), //$NON-NLS-1$
	    		}
	    	),
			// actions
			super.createPropertyDescriptor(getBeanClass(),"actions", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextFieldMessages.getString("actions.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextFieldMessages.getString("actions.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// columns
			super.createPropertyDescriptor(getBeanClass(),"columns", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextFieldMessages.getString("columns.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextFieldMessages.getString("columns.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// font
			super.createPropertyDescriptor(getBeanClass(),"font", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextFieldMessages.getString("font.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextFieldMessages.getString("font.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// horizontalAlignment
			super.createPropertyDescriptor(getBeanClass(),"horizontalAlignment", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextFieldMessages.getString("horizontalAlignment.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextFieldMessages.getString("horizontalAlignment.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	ENUMERATIONVALUES, new Object[] {
	      		JTextFieldMessages.getString("Alignment.LEFT"), new Integer(javax.swing.JTextField.LEFT), //$NON-NLS-1$
	      			"javax.swing.JTextField.LEFT", //$NON-NLS-1$
	      		JTextFieldMessages.getString("Alignment.CENTER"), new Integer(javax.swing.JTextField.CENTER), //$NON-NLS-1$
	      			"javax.swing.JTextField.CENTER", //$NON-NLS-1$
	      		JTextFieldMessages.getString("Alignment.RIGHT"), new Integer(javax.swing.JTextField.RIGHT), //$NON-NLS-1$
	      			"javax.swing.JTextField.RIGHT", //$NON-NLS-1$
	      		JTextFieldMessages.getString("Alignment.LEADING"), new Integer(javax.swing.JTextField.LEADING), //$NON-NLS-1$
	      			"javax.swing.JTextField.LEADING", //$NON-NLS-1$
	      		JTextFieldMessages.getString("Alignment.TRAILING"), new Integer(javax.swing.JTextField.TRAILING), //$NON-NLS-1$
	      			"javax.swing.JTextField.TRAILING" //$NON-NLS-1$	      			
	    		}
	    	}
	    	),
	    	// preferredSize
			super.createPropertyDescriptor(getBeanClass(),"preferredSize", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextFieldMessages.getString("preferredSize.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextFieldMessages.getString("preferredSize.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// scrollOffset
			super.createPropertyDescriptor(getBeanClass(),"scrollOffset", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextFieldMessages.getString("scrollOffset.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextFieldMessages.getString("scrollOffset.Desc"), //$NON-NLS-1$
      	    DESIGNTIMEPROPERTY, Boolean.FALSE
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

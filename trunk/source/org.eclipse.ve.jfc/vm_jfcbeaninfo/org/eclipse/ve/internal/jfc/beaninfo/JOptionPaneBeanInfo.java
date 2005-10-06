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
 *  $RCSfile: JOptionPaneBeanInfo.java,v $
 *  $Revision: 1.8 $  $Date: 2005-10-06 15:18:44 $ 
 */

import java.beans.*;

import org.eclipse.jem.beaninfo.vm.IBaseBeanInfoConstants;

public class JOptionPaneBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle JOptionPaneMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.joptionpane");  //$NON-NLS-1$

		
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JOptionPane.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JOptionPaneMessages.getString("JOptionPane.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JOptionPaneMessages.getString("JOptionPane.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/joptpn32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/joptpn16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
	    return loadImage("joptpn32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("joptpn16.gif"); //$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
		  	// createDialog(Component,String)
			super.createMethodDescriptor(getBeanClass(),"createDialog",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("createDialog(Component,String).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("createDialog(Component,String).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("parentComponent", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("createDialog(Component,String).parentComponent.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Parent for dialog",
	      				}
	      			),
	      			createParameterDescriptor("title", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("createDialog(Component,String).title.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Title of the dialog",
	      				}
	      			),
	      		},
	      		new Class[] {
	      			java.awt.Component.class, java.lang.String.class
	      		}		    		
		  	),
		  	// createInternalFrame(Component,String)
			super.createMethodDescriptor(getBeanClass(),"createInternalFrame",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("createInternalFrame(Component,String).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Create an internal frame with title in given component",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("parentComponent", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("createInternalFrame(Component,String).parentComponent.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Parent for frame",
	      				}
	      			),
	      			createParameterDescriptor("title", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("createInternalFrame(Component,String).title.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Title of the frame",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Component.class, java.lang.String.class
	      		}		    		
		  	),
		  	// getDesktopPaneForComponent(Component)
			super.createMethodDescriptor(getBeanClass(),"getDesktopPaneForComponent",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("getDesktopPaneForComponent(Component).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the desktop pane for given component",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("parentComponent", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("getDesktopPaneForComponent(Component).parentComponent.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Parent for frame",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Component.class
	      		}		    		
		  	),
		  	// getFrameForComponent(Component)
			super.createMethodDescriptor(getBeanClass(),"getFrameForComponent",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("getFrameForComponent(Component).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the frame for given component",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("parentComponent", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("getFrameForComponent(Component).parentComponent.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Parent for frame",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Component.class
	      		}		    		
		  	),
		  	// getIcon()
			super.createMethodDescriptor(getBeanClass(),"getIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("getIcon().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("getIcon().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getInitialSelectionValue()
			super.createMethodDescriptor(getBeanClass(),"getInitialSelectionValue",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("getInitialSelectionValue().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the initially selected value",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getInitialValue()
			super.createMethodDescriptor(getBeanClass(),"getInitialValue",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("getInitialValue().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the initial value",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getInputValue()
			super.createMethodDescriptor(getBeanClass(),"getInputValue",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("getInputValue().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("getInputValue().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMaxCharactersPerLineCount()
			super.createMethodDescriptor(getBeanClass(),"getMaxCharactersPerLineCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("getMaxCharactersPerLineCount().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("getMaxCharactersPerLineCount().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMessage()
			super.createMethodDescriptor(getBeanClass(),"getMessage",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("getMessage().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("getMessage().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMessageType()
			super.createMethodDescriptor(getBeanClass(),"getMessageType",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("getMessageType().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("getMessageType().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getOptions()
			super.createMethodDescriptor(getBeanClass(),"getOptions",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("getOptions().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("getOptions().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getOptionType()
			super.createMethodDescriptor(getBeanClass(),"getOptionType",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("getOptionType().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("getOptionType().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectionValues()
			super.createMethodDescriptor(getBeanClass(),"getSelectionValues",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("getSelectionValues().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("getSelectionValues().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getUI()
			super.createMethodDescriptor(getBeanClass(),"getUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("getUI().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the ComboBoxUI object",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE	      		
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getValue()
			super.createMethodDescriptor(getBeanClass(),"getValue",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("getValue().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("getValue().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getWantsInput()
			super.createMethodDescriptor(getBeanClass(),"getWantsInput",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("getWantsInput().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE if parent component will be provided for input",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// selectInitialValue()
			super.createMethodDescriptor(getBeanClass(),"selectInitialValue",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("selectInitialValue().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("selectInitialValue().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setIcon(Icon)
			super.createMethodDescriptor(getBeanClass(),"setIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("setIcon(Icon).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the icon to display",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("newIcon", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("setIcon(Icon).newIcon.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Icon to display",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.Icon.class
	      		}		    		
		  	),
		  	// setInitialValue(Object)
			super.createMethodDescriptor(getBeanClass(),"setInitialValue",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("setInitialValue(Object).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the object that gets the default focus",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("newInitialValue", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("setInitialValue(Object).newInitialValue.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Component with default focus",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.lang.Object.class
	      		}		    		
		  	),
		  	// setInitialSelectionValue(Object)
			super.createMethodDescriptor(getBeanClass(),"setInitialSelectionValue",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("setInitialSelectionValue(Object).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the initial selection value",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("newValue", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("setInitialSelectionValue(Object).newValue.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Object holding new value",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.lang.Object.class
	      		}		    		
		  	),
		  	// setInputValue(Object)
			super.createMethodDescriptor(getBeanClass(),"setInputValue",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("setInputValue(Object).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the user input value",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("newValue", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("setInputValue(Object).newValue.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Object with user input value",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.lang.Object.class
	      		}		    		
		  	),
		  	// setMessage(Object)
			super.createMethodDescriptor(getBeanClass(),"setMessage",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("setMessage(Object).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the message object",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("newValue", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("setMessage(Object).newValue.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Object with message to display",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.lang.Object.class
	      		}		    		
		  	),		  	
		  	// setMessageType(int)
			super.createMethodDescriptor(getBeanClass(),"setMessageType",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("setMessageType(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("setMessageType(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("newType", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("setMessageType(int).newType.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Message type displayed",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setOptions(Object[])
			super.createMethodDescriptor(getBeanClass(),"setOptions",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("setOptions(Object[]).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("setOptions(Object[]).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("newOptions", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("setOptions(Object[]).newOptions.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Array of option objects",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.lang.Object[].class
	      		}		    		
		  	),
		  	// setOptionType(int)
			super.createMethodDescriptor(getBeanClass(),"setOptionType",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("setOptionType(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("setOptionType(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("newType", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("setOptionType(int).newType.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Option type (OK_CANCEL_OPTION...)",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setRootFrame(Frame)
			super.createMethodDescriptor(getBeanClass(),"setRootFrame",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("setRootFrame(Frame).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("setRootFrame(Frame).Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("rootFrame", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("setRootFrame(Frame).rootFrame.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Default frame to use",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Frame.class
	      		}		    		
		  	),
		  	// setSelectionValues(Object[])
			super.createMethodDescriptor(getBeanClass(),"setSelectionValues",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("setSelectionValues(Object[]).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("setSelectionValues(Object[]).Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("newValues", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("setSelectionValues(Object[]).newValues.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Array of objects",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.lang.Object[].class
	      		}		    		
		  	),
		  	// setUI(OptionPaneUI)
			super.createMethodDescriptor(getBeanClass(),"setUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("setUI(OptionPaneUI).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the option pane UI",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("ui", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("setUI(OptionPaneUI).aUI.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Option pane UI",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.plaf.OptionPaneUI.class
	      		}		    		
		  	),
		  	// setValue(Object)
			super.createMethodDescriptor(getBeanClass(),"setValue",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("setValue(Object).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("setValue(Object).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("newValue", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("setValue(Object).newValue.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "User value",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.lang.Object.class
	      		}		    		
		  	),
		  	// setWantsInput(boolean)
			super.createMethodDescriptor(getBeanClass(),"setWantsInput",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("setWantsInput(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("setWantsInput(boolean).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("aBool", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("setWantsInput(boolean).aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE for parentComponent",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// showConfirmDialog(Component,Object)
			super.createMethodDescriptor(getBeanClass(),"showConfirmDialog",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("showConfirmDialog(Component,Object).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("showConfirmDialog(Component,Object).Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("parentComponent", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showConfirmDialog(Component,Object).parentComponent.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Parent frame or null",
	      				}
	      			),
	      			createParameterDescriptor("message", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showConfirmDialog(Component,Object).message.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Message object",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Component.class, java.lang.Object.class
	      		}		    		
		  	),
		  	// showConfirmDialog(Component,Object,String,int)
			super.createMethodDescriptor(getBeanClass(),"showConfirmDialog",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("showConfirmDialog(Component,Object,String,int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("showConfirmDialog(Component,Object,String,int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("parentComponent", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showConfirmDialog(Component,Object,String,int).parentComponent.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Parent frame or null",
	      				}),
	      			createParameterDescriptor("message", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showConfirmDialog(Component,Object,String,int).message.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Message object",
	      				}),
	      			createParameterDescriptor("title", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showConfirmDialog(Component,Object,String,int).title.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Dialog title",
	      				}),
	      			createParameterDescriptor("optionType", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showConfirmDialog(Component,Object,String,int).optionType.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "YES_NO_OPTION...",
	      				})	      				
	      		},
	      		new Class[] {
	      			java.awt.Component.class, java.lang.Object.class,
	      			String.class, int.class
	      		}		    		
		  	),
			// showInputDialog(Object)
			super.createMethodDescriptor(getBeanClass(),"showInputDialog",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("showInputDialog(Object).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("showInputDialog(Object).Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	      			createParameterDescriptor("message", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showInputDialog(Object).message.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Message object",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.lang.Object.class
	      		}		    		
		  	),
			// showInputDialog(Component,Object)
			super.createMethodDescriptor(getBeanClass(),"showInputDialog",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("showInputDialog(Component,Object).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("showInputDialog(Component,Object).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("parentComponent", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showInputDialog(Component,Object).parentComponent.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Parent frame or null",
	      				}
	      			),
	      			createParameterDescriptor("message", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showInputDialog(Component,Object).message.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Message object",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Component.class, java.lang.Object.class
	      		}		    		
		  	),
		  	// showInputDialog(Component,Object,String,int)
			super.createMethodDescriptor(getBeanClass(),"showInputDialog",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("showInputDialog(Component,Object,String,int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("showInputDialog(Component,Object,String,int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("parentComponent", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showInputDialog(Component,Object,String,int).parentComponent.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Parent frame or null",
	      				}),
	      			createParameterDescriptor("message", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showInputDialog(Component,Object,String,int).message.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Message object",
	      				}),
	      			createParameterDescriptor("title", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showInputDialog(Component,Object,String,int).title.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Dialog title",
	      				}),
	      			createParameterDescriptor("messageType", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showInputDialog(Component,Object,String,int).messageType.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "ERROR_MESSAGE...",
	      				})	      				
	      		},
	      		new Class[] {
	      			java.awt.Component.class, java.lang.Object.class,
	      			String.class, int.class
	      		}		    		
		  	),
			// showInternalConfirmDialog(Component,Object,String,int)
			super.createMethodDescriptor(getBeanClass(),"showInternalConfirmDialog",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("showInternalConfirmDialog(Component,Object,String,int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Show an internal confirm dialog with specified options",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("parentComponent", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showInternalConfirmDialog(Component,Object,String,int).parentComponent.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Parent frame or null",
	      				}),
	      			createParameterDescriptor("message", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showInternalConfirmDialog(Component,Object,String,int).message.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Message object",
	      				}),
	      			createParameterDescriptor("title", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showInternalConfirmDialog(Component,Object,String,int).title.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Dialog title",
	      				}),
	      			createParameterDescriptor("optionType", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showInternalConfirmDialog(Component,Object,String,int).optionType.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "YES_NO_OPTION...",
	      				})	      				
	      		},
	      		new Class[] {
	      			java.awt.Component.class, java.lang.Object.class,
	      			String.class, int.class
	      		}		    		
		  	),
		  	// showInternalInputDialog(Component,Object,String,int)
			super.createMethodDescriptor(getBeanClass(),"showInternalInputDialog",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("showInternalInputDialog(Component,Object,String,int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Show internal user input dialog with specified options",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("parentComponent", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showInternalInputDialog(Component,Object,String,int).parentComponent.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Parent frame or null",
	      				}),
	      			createParameterDescriptor("message", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showInternalInputDialog(Component,Object,String,int).message.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Message object",
	      				}),
	      			createParameterDescriptor("title", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showInternalInputDialog(Component,Object,String,int).title.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Dialog title",
	      				}),
	      			createParameterDescriptor("messageType", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showInternalInputDialog(Component,Object,String,int).messageType.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "ERROR_MESSAGE...",
	      				})	      				
	      		},
	      		new Class[] {
	      			java.awt.Component.class, java.lang.Object.class,
	      			String.class, int.class
	      		}		    		
		  	),
		  	// showInternalMessageDialog(Component,Object,String,int)
			super.createMethodDescriptor(getBeanClass(),"showInternalMessageDialog",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("showInternalMessageDialog(Component,Object,String,int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Show internal message dialog with specified options",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("parentComponent", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showInternalMessageDialog(Component,Object,String,int).parentComponent.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Parent frame or null",
	      				}),
	      			createParameterDescriptor("message", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showInternalMessageDialog(Component,Object,String,int).message.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Message object",
	      				}),
	      			createParameterDescriptor("title", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showInternalMessageDialog(Component,Object,String,int).title.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Dialog title",
	      				}),
	      			createParameterDescriptor("messageType", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showInternalMessageDialog(Component,Object,String,int).messageType.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "ERROR_MESSAGE...",
	      				})	      				
	      		},
	      		new Class[] {
	      			java.awt.Component.class, java.lang.Object.class,
	      			String.class, int.class
	      		}		    		
		  	),
		  	// showMessageDialog(Component,Object)
			super.createMethodDescriptor(getBeanClass(),"showMessageDialog",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("showMessageDialog(Component,Object).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("showMessageDialog(Component,Object).Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("parentComponent", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showMessageDialog(Component,Object).parentComponent.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Parent frame or null",
	      				}
	      			),
	      			createParameterDescriptor("message", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showMessageDialog(Component,Object).message.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Message object",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Component.class, java.lang.Object.class
	      		}		    		
		  	),
		  	// showMessageDialog(Component,Object,String,int)
			super.createMethodDescriptor(getBeanClass(),"showMessageDialog",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JOptionPaneMessages.getString("showMessageDialog(Component,Object,String,int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JOptionPaneMessages.getString("showMessageDialog(Component,Object,String,int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("parentComponent", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showMessageDialog(Component,Object,String,int).parentComponent.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Parent frame or null",
	      				}),
	      			createParameterDescriptor("message", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showMessageDialog(Component,Object,String,int).message.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Message object",
	      				}),
	      			createParameterDescriptor("title", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showMessageDialog(Component,Object,String,int).title.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Dialog title",
	      				}),
	      			createParameterDescriptor("messageType", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JOptionPaneMessages.getString("showMessageDialog(Component,Object,String,int).messageType.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "ERROR_MESSAGE...",
	      				})	      				
	      		},
	      		new Class[] {
	      			java.awt.Component.class, java.lang.Object.class,
	      			String.class, int.class
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
			// icon
			super.createPropertyDescriptor(getBeanClass(),"icon", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JOptionPaneMessages.getString("icon.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JOptionPaneMessages.getString("showMessageDialog(Component,Object,String,int).Icon_to_display.Name"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	    		}
	    	),
			// initialSelectionValue
			super.createPropertyDescriptor(getBeanClass(),"initialSelectionValue", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JOptionPaneMessages.getString("initialSelectionValue.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JOptionPaneMessages.getString("initialSelectionValue.Desc"), //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE,
	      	BOUND, Boolean.TRUE
	    		}
	    	),
			// initialValue
			super.createPropertyDescriptor(getBeanClass(),"initialValue", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JOptionPaneMessages.getString("initialValue.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JOptionPaneMessages.getString("initialValue.Desc"), //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE,
	      	BOUND, Boolean.TRUE
	    		}
	    	),
	    	// inputValue
			super.createPropertyDescriptor(getBeanClass(),"inputValue", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JOptionPaneMessages.getString("inputValue.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JOptionPaneMessages.getString("inputValue.Desc"), //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE,
	      	BOUND, Boolean.TRUE
	    		}
	    	),
			// layout - hide it
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
	    	// maxCharactersPerLineCount
			super.createPropertyDescriptor(getBeanClass(),"maxCharactersPerLineCount", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JOptionPaneMessages.getString("maxCharactersPerLineCount.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JOptionPaneMessages.getString("maxCharactersPerLineCount.Name"), //$NON-NLS-1$
	    		}
	    	),
	    	// message
			super.createPropertyDescriptor(getBeanClass(),"message", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JOptionPaneMessages.getString("message.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JOptionPaneMessages.getString("message.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
	    	// messageType
			super.createPropertyDescriptor(getBeanClass(),"messageType", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JOptionPaneMessages.getString("messageType.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JOptionPaneMessages.getString("messagetype.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      			JOptionPaneMessages.getString("messagetype.ERROR"), new Integer(javax.swing.JOptionPane.ERROR_MESSAGE), //$NON-NLS-1$
	      				"javax.swing.JOptionPane.ERROR_MESSAGE", //$NON-NLS-1$
	      			JOptionPaneMessages.getString("messagetype.INFORMATION"), new Integer(javax.swing.JOptionPane.INFORMATION_MESSAGE), //$NON-NLS-1$
	      				"javax.swing.JOptionPane.INFORMATION_MESSAGE", //$NON-NLS-1$
	      			JOptionPaneMessages.getString("messagetype.WARNING"), new Integer(javax.swing.JOptionPane.WARNING_MESSAGE), //$NON-NLS-1$
	      				"javax.swing.JOptionPane.WARNING_MESSAGE", //$NON-NLS-1$
	      			JOptionPaneMessages.getString("messagetype.QUESTION"), new Integer(javax.swing.JOptionPane.QUESTION_MESSAGE), //$NON-NLS-1$
	      				"javax.swing.JOptionPane.QUESTION_MESSAGE", //$NON-NLS-1$
	      			JOptionPaneMessages.getString("messagetype.PLAIN"), new Integer(javax.swing.JOptionPane.PLAIN_MESSAGE), //$NON-NLS-1$
	      				"javax.swing.JOptionPane.PLAIN_MESSAGE"	      			 //$NON-NLS-1$
	    			}
	    		}
	    	),
	    	// optionType
			super.createPropertyDescriptor(getBeanClass(),"optionType", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JOptionPaneMessages.getString("optionType.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JOptionPaneMessages.getString("optionType.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      			JOptionPaneMessages.getString("optionType.DEFAULT"), new Integer(javax.swing.JOptionPane.DEFAULT_OPTION), //$NON-NLS-1$
	      				"javax.swing.JOptionPane.DEFAULT_OPTION", //$NON-NLS-1$
	      			JOptionPaneMessages.getString("optionType.YES_NO"), new Integer(javax.swing.JOptionPane.YES_NO_OPTION), //$NON-NLS-1$
	      				"javax.swing.JOptionPane.YES_NO_OPTION", //$NON-NLS-1$
	      			JOptionPaneMessages.getString("optionType.YES_NO_CANCEL"), new Integer(javax.swing.JOptionPane.YES_NO_CANCEL_OPTION), //$NON-NLS-1$
	      				"javax.swing.JOptionPane.YES_NO_CANCEL_OPTION", //$NON-NLS-1$
	      			JOptionPaneMessages.getString("optionType.OK_CANCEL"), new Integer(javax.swing.JOptionPane.OK_CANCEL_OPTION), //$NON-NLS-1$
	      				"javax.swing.JOptionPane.OK_CANCEL_OPTION", //$NON-NLS-1$
	    			}
	    		}
	    	),
	    	// value
			super.createPropertyDescriptor(getBeanClass(),"value", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JOptionPaneMessages.getString("value.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JOptionPaneMessages.getString("value.Desc"), //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE,
	      	BOUND, Boolean.TRUE,
			PREFERRED, Boolean.TRUE
	    		}
	    	),
	    	// wantsInput
			super.createPropertyDescriptor(getBeanClass(),"wantsInput", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JOptionPaneMessages.getString("wantsInput.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JOptionPaneMessages.getString("wantsInput.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	OBSCURE, Boolean.TRUE	      	
	    		}
	    	),
	    	// ui
			super.createPropertyDescriptor(getBeanClass(),"UI", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JOptionPaneMessages.getString("ui.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JOptionPaneMessages.getString("ui.Desc"), //$NON-NLS-1$
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

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
 *  $RCSfile: TextFieldBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.beans.*;

public class TextFieldBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle restextfield = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.textfield");  //$NON-NLS-1$
	
	
public java.beans.EventSetDescriptor actionEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.ActionEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.ActionListener.class,
				"actionPerformed", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, restextfield.getString("actionPerformedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, restextfield.getString("actionPerformedSD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("actionEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextfield.getString("actionEventDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Event on hitting ENTER",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"action", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, restextfield.getString("actionEventsDN"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, restextfield.getString("actionEventsSD"), //$NON-NLS-1$
	      				INDEFAULTEVENTSET, Boolean.TRUE
	      				}, 
						aDescriptorList, java.awt.event.ActionListener.class,
						"addActionListener", "removeActionListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return java.awt.TextField.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		/* Create and return the TextFieldBeanInfo bean descriptor. */
		aDescriptor = new java.beans.BeanDescriptor(java.awt.TextField.class);
		aDescriptor.setDisplayName(restextfield.getString("TextFieldDN")); //$NON-NLS-1$
		aDescriptor.setShortDescription(restextfield.getString("TextFieldSD")); //$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/txtfld32.gif");//$NON-NLS-2$//$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/txtfld16.gif");//$NON-NLS-2$//$NON-NLS-1$
	} catch (Throwable exception) {
	};
	return aDescriptor;
}
/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	try {
		java.beans.EventSetDescriptor aDescriptorList[] = {
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
	    return loadImage("txtfld32.gif");//$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("txtfld16.gif");//$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		java.beans.MethodDescriptor aDescriptorList[] = {
			// addNotify()
			super.createMethodDescriptor(getBeanClass(),"addNotify", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "addNotify()",//$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Create the textfield's peer",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// echoCharIsSet()
			super.createMethodDescriptor(getBeanClass(),"echoCharIsSet", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "echoCharIsSet()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextfield.getString("echoCharIsSet()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getColumns()
			super.createMethodDescriptor(getBeanClass(),"getColumns", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getColumns()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextfield.getString("getColumns()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getEchoChar()
			super.createMethodDescriptor(getBeanClass(),"getEchoChar", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getEchoChar()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextfield.getString("getEchoChar()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getMinimumSize()
			super.createMethodDescriptor(getBeanClass(),"getMinimumSize", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getMinimumSize()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get minimum dimensions",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// getPreferredSize()
			super.createMethodDescriptor(getBeanClass(),"getPreferredSize", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getPreferredSize()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get preferred dimensions",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// getMinimumSize(int)
			super.createMethodDescriptor(getBeanClass(),"getMinimumSize", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getMinimumSize(int)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get minimum dimensions for given columns",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextfield.getString("columnsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Number of columns",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class 
	      	}	    		
		  	),
		  	// getPreferredSize(int)
			super.createMethodDescriptor(getBeanClass(),"getPreferredSize", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getPreferredSize(int)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get preferred dimensions for given columns",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextfield.getString("columnsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Number of columns",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class 
	      	}	    		
		  	),
		  	// setColumns(int)
			super.createMethodDescriptor(getBeanClass(),"setColumns", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setColumns(int)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set number of columns",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextfield.getString("columnsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Number of columns",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class 
	      	}	    		
		  	),
		  	// setEchoChar(char)
			super.createMethodDescriptor(getBeanClass(),"setEchoChar", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setEchoChar(char)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextfield.getString("setEchoChar(char)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextfield.getString("charParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "echo character",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		char.class 
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
		java.beans.PropertyDescriptor aDescriptorList[] = {
			// echoChar
			super.createPropertyDescriptor(getBeanClass(),"echoChar", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, restextfield.getString("echoCharDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, restextfield.getString("echoCharSD"), //$NON-NLS-1$
	    		}
	    	),
	    	// columns
	    	super.createPropertyDescriptor(getBeanClass(),"columns", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, restextfield.getString("columnsDN"),		    	 //$NON-NLS-1$
	      	SHORTDESCRIPTION, restextfield.getString("columnsSD"), //$NON-NLS-1$
	    		}
	    	),
	    	// minimumSize
	    	super.createPropertyDescriptor(getBeanClass(),"minimumSize", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, restextfield.getString("minimumSizeDN"),		    	 //$NON-NLS-1$
	      	SHORTDESCRIPTION, restextfield.getString("minimumSizeSD"), //$NON-NLS-1$
	    		}
	    	),
	    	// preferredSize
	    	super.createPropertyDescriptor(getBeanClass(),"preferredSize", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, restextfield.getString("preferredSizeDN"),		    	 //$NON-NLS-1$
	      	SHORTDESCRIPTION, restextfield.getString("preferredSizeSD"), //$NON-NLS-1$
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

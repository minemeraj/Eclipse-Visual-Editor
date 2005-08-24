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
 *  $RCSfile: CheckboxBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.beans.*;
public class CheckboxBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle rescheckbox = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.checkbox");  //$NON-NLS-1$
	
	
public Class getBeanClass() {
	return java.awt.Checkbox.class;
}
public BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the CheckboxBeanInfo bean descriptor. */
	aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               	DISPLAYNAME, rescheckbox.getString("CheckboxDN"), //$NON-NLS-1$
	        				SHORTDESCRIPTION, rescheckbox.getString("CheckboxSD") //$NON-NLS-1$
							}			    
						);
	aDescriptor.setValue("ICON_COLOR_32x32", "icons/cbox32.gif");//$NON-NLS-2$//$NON-NLS-1$
	aDescriptor.setValue("ICON_COLOR_16x16", "icons/cbox16.gif");//$NON-NLS-2$//$NON-NLS-1$
	return aDescriptor;
}
/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	try {
		java.beans.EventSetDescriptor aDescriptorList[] = {
			itemEventSetDescriptor()
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
	    return loadImage("cbox32.gif");//$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("cbox16.gif");//$NON-NLS-1$
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
			super.createMethodDescriptor(getBeanClass(),"addNotify", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "addNotify()",  //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Create the peer of the checkbox and notify",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),		
		// getCheckboxGroup()
		super.createMethodDescriptor(getBeanClass(),"getCheckboxGroup", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getCheckboxGroup()",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Determine the check box's group",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),		
			// getLabel()
			super.createMethodDescriptor(getBeanClass(),"getLabel", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getLabel()",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the label of the check box",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),		
			// getSelectedObjects()
			super.createMethodDescriptor(getBeanClass(),"getSelectedObjects", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getSelectedObjects()",  //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Return array with selected checkbox label",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getState()
			super.createMethodDescriptor(getBeanClass(),"getState", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getState()",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Return the boolean state of the checkbox",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),				
			// setCheckboxGroup(CheckboxGroup)
			super.createMethodDescriptor(getBeanClass(),"setCheckboxGroup", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setCheckboxGroup(CheckboxGroup)", //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set this check box's group",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescheckbox.getString("groupParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Checkbox group",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		java.awt.CheckboxGroup.class 
	      	}	    		
		  	),			
			// setLabel(String)
			super.createMethodDescriptor(getBeanClass(),"setLabel", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setLabel(String)",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set this check box's label",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescheckbox.getString("labelParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Checkbox label",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		java.lang.String.class 
	      	}	    		
		  	),
			// setState(boolean)
			super.createMethodDescriptor(getBeanClass(),"setState", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setState(boolean)",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set this check box's state",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescheckbox.getString("stateParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Boolean state",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		boolean.class 
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
public java.beans.PropertyDescriptor[] getPropertyDescriptors(){
	try {
		java.beans.PropertyDescriptor aDescriptorList[] = {
			// checkboxGroup 
	   	super.createPropertyDescriptor(getBeanClass(),"checkboxGroup", new Object[] {//$NON-NLS-1$
		   	DISPLAYNAME, rescheckbox.getString("checkboxGroupDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescheckbox.getString("checkboxGroupSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE,
	      	//HIDDEN, Boolean.TRUE
	    		}
	    	),	
			// label
	   	super.createPropertyDescriptor(getBeanClass(),"label", new Object[] {//$NON-NLS-1$
		   	DISPLAYNAME, rescheckbox.getString("labelDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescheckbox.getString("labelSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
			// selectedObjects
	   	super.createPropertyDescriptor(getBeanClass(),"selectedObjects", new Object[] {//$NON-NLS-1$
		   	DISPLAYNAME, rescheckbox.getString("selectedObjectsDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescheckbox.getString("selectedObjectsSD"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
		// state
		super.createPropertyDescriptor(getBeanClass(),"state", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, rescheckbox.getString("stateDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescheckbox.getString("stateSD"), //$NON-NLS-1$
	        PREFERRED, Boolean.TRUE
	    		}
	    	)
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		// uvm.tools.DebugSupport.halt();
		handleException(exception);
	};
	return null;
}

/**
 * Gets the itemevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public java.beans.EventSetDescriptor itemEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.ItemEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.ItemListener.class,
				"itemStateChanged", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescheckbox.getString("itemStateChangedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescheckbox.getString("itemStateChangedSD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("itemEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescheckbox.getString("itemEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "item state changed event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"item", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, rescheckbox.getString("itemEventsDN"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, rescheckbox.getString("itemEventsSD"), //$NON-NLS-1$
	      				INDEFAULTEVENTSET, Boolean.TRUE,
	      			}, 
						aDescriptorList, java.awt.event.ItemListener.class,
						"addItemListener", "removeItemListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}
}

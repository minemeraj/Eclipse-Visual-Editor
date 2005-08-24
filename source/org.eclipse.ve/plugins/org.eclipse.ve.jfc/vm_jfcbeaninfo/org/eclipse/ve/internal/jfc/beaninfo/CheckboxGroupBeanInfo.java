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
 *  $RCSfile: CheckboxGroupBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:12 $ 
 */

import java.beans.*;
public class CheckboxGroupBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle rescheckbox = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.checkboxgroup");  //$NON-NLS-1$
	
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return java.awt.CheckboxGroup.class;
}
public BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the ComponentBeanInfobean descriptor. */
	aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               	DISPLAYNAME, rescheckbox.getString("CheckboxGroupDN"), //$NON-NLS-1$
	        		SHORTDESCRIPTION, rescheckbox.getString("CheckboxGroupSD") //$NON-NLS-1$
	    }			    
		);
	aDescriptor.setValue("ICON_COLOR_32x32", "icons/cboxg32.gif");//$NON-NLS-2$//$NON-NLS-1$
	aDescriptor.setValue("ICON_COLOR_16x16", "icons/cboxg16.gif");//$NON-NLS-2$//$NON-NLS-1$
	return aDescriptor;
}
/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	return (new EventSetDescriptor[0]);
}
	/**
	 * @return an icon of the specified kind for JButton
	 */
	public java.awt.Image getIcon(int kind) {
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("cboxg32.gif");//$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("cboxg16.gif");//$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		java.beans.MethodDescriptor aDescriptorList[] = {
			// getCurrent() - DEPRECATED
		   // getSelectedCheckbox()
			super.createMethodDescriptor(getBeanClass(),"getSelectedCheckbox", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getSelectedCheckbox()",  //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the selected checkbox in the group",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// setCurrent(Checkbox) - DEPRECATED
			// setSelectedCheckbox(Checkbox)
			super.createMethodDescriptor(getBeanClass(),"setSelectedCheckbox", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setSelectedCheckbox(Checkbox)", //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the selected check box in group",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("box", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescheckbox.getString("boxParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "currently selected Checkbox",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		java.awt.Checkbox.class 
	      	}	    		
		  	),			
			// toString()
			super.createMethodDescriptor(getBeanClass(),"toString", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "toString()",  //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the string representation of this object",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
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
		java.beans.PropertyDescriptor aDescriptorList[] = {
			// selectedCheckbox
			super.createPropertyDescriptor(getBeanClass(),"selectedCheckbox", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, rescheckbox.getString("selectedCheckboxDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescheckbox.getString("selectedCheckboxSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE,
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

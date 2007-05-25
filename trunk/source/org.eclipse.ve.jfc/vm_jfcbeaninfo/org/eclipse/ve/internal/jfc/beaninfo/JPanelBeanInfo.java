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
 *  $RCSfile: JPanelBeanInfo.java,v $
 *  $Revision: 1.8 $  $Date: 2007-05-25 04:19:32 $ 
 */

import java.beans.*;

public class JPanelBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle JPanelMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jpanel");  //$NON-NLS-1$
		
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JPanel.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JPanelMessages.getString("JPanel.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JPanelMessages.getString("JPanel.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/panel32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/panel16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
	    return loadImage("panel32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("panel16.gif"); //$NON-NLS-1$
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
	   			DISPLAYNAME, JPanelMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessible context",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// updateUI()
			super.createMethodDescriptor(getBeanClass(),"updateUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPanelMessages.getString("updateUI().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Update the UI",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getUI()
			super.createMethodDescriptor(getBeanClass(),"getUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPanelMessages.getString("getUI().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the ComboBoxUI object",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE	      		
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setUI(PanelUI)
			super.createMethodDescriptor(getBeanClass(),"setUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JPanelMessages.getString("setUI(PanelUI).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the panel UI",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("ui", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JPanelMessages.getString("setUI(PanelUI).aUI.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Panel UI",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.plaf.PanelUI.class
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
				// ui
				super.createPropertyDescriptor(getBeanClass(),"UI", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JPanelMessages.getString("ui.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JPanelMessages.getString("ui.Desc"), //$NON-NLS-1$
						EXPERT, Boolean.TRUE,
						BOUND, Boolean.TRUE
				}
				),
				};
			return aDescriptorList;
		}catch(Throwable t) {
			handleException(t);
		}
	return null;
}


	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.jfc.beaninfo.IvjBeanInfo#overridePropertyDescriptors(java.beans.PropertyDescriptor[])
	 */
	protected PropertyDescriptor[] overridePropertyDescriptors(PropertyDescriptor[] pds) {
		PropertyDescriptor[] newPDs = pds.clone();
		
		replacePropertyDescriptor(newPDs, "border", null, new Object[] { //$NON-NLS-1$
			EXPERT, Boolean.FALSE,
			}
		);

		return newPDs;
	}

}



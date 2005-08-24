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
 *  $RCSfile: DialogBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.beans.*;
public class DialogBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle resdialog = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.dialog");  //$NON-NLS-1$

public Class getBeanClass() {
	return java.awt.Dialog.class;
}

public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		/* Create and return the DialogBeanInfobean descriptor. */
		aDescriptor = new java.beans.BeanDescriptor(java.awt.Dialog.class);
		aDescriptor.setDisplayName(resdialog.getString("DialogDN")); //$NON-NLS-1$
		aDescriptor.setShortDescription(resdialog.getString("DialogSD")); //$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/dialog32.gif");//$NON-NLS-2$//$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/dialog16.gif");//$NON-NLS-2$//$NON-NLS-1$
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
	return(new java.beans.EventSetDescriptor[0]);
}
	/**
	 * @return an icon of the specified kind for JButton
	 */
	public java.awt.Image getIcon(int kind) {
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("dialog32.gif");//$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("dialog16.gif");//$NON-NLS-1$
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
	      		// SHORTDESCRIPTION, "Create the peer",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getTitle()
			super.createMethodDescriptor(getBeanClass(),"getTitle", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getTitle()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the title",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// isModal()
			super.createMethodDescriptor(getBeanClass(),"isModal", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "isModal()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resdialog.getString("isModal()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// isResizable()
			super.createMethodDescriptor(getBeanClass(),"isResizable", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "isResizable()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resdialog.getString("isResizable()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// setModal(boolean)
			super.createMethodDescriptor(getBeanClass(),"setModal", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setModal(boolean)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resdialog.getString("setModal(boolean)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, resdialog.getString("modalParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "True if modal",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		boolean.class 
	      	}	    		
		  	),
			// setResizable(boolean)
			super.createMethodDescriptor(getBeanClass(),"setResizable", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setResizable(boolean)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set whether the dialog is resizable",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("resizable", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resdialog.getString("resizableParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "True if dialog can be resized",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		boolean.class 
	      	}	    		
		  	),
			// setTitle(String)
			super.createMethodDescriptor(getBeanClass(),"setTitle", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setTitle(String)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the dialog's title",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("title", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resdialog.getString("titleParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "New title",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		String.class 
	      	}	    		
		  	),
		  	// show()
		  	super.createMethodDescriptor(getBeanClass(),"show", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "show()",//$NON-NLS-1$
			    PREFERRED, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Show the dialog",
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
			// modal
			super.createPropertyDescriptor(getBeanClass(),"modal", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resdialog.getString("modalDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resdialog.getString("modalSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
			// resizable
			super.createPropertyDescriptor(getBeanClass(),"resizable", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resdialog.getString("resizableDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resdialog.getString("resizableSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
			// title
			super.createPropertyDescriptor(getBeanClass(),"title", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resdialog.getString("titleDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resdialog.getString("titleSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
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

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: MenuListenerEventSet.java,v $
 *  $Revision: 1.1 $  $Date: 2004-06-01 18:04:09 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;
import java.util.ResourceBundle;
 

/**
 * @since 1.0.0
 *
 */
public class MenuListenerEventSet {
	private static ResourceBundle resources = ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.menulistener");  //$NON-NLS-1$

	public static EventSetDescriptor getEventSetDescriptor(Class targetClass) {
		EventSetDescriptor aDescriptor = null;
		Class[] paramTypes = { org.eclipse.swt.events.MenuEvent.class };
		MethodDescriptor aDescriptorList[] = {
				IvjBeanInfo.createMethodDescriptor(org.eclipse.swt.events.MenuListener.class,
					"menuHidden",  //$NON-NLS-1$
					new Object[] {
					IvjBeanInfo.DISPLAYNAME, resources.getString("menuHiddenDN"), //$NON-NLS-1$
					IvjBeanInfo.SHORTDESCRIPTION, resources.getString("menuHiddenSD"), //$NON-NLS-1$
					IvjBeanInfo.PREFERRED, Boolean.TRUE,
		    		}, 
		    		new ParameterDescriptor[] {
						IvjBeanInfo.createParameterDescriptor("menuEvent", new Object[] {//$NON-NLS-1$
						IvjBeanInfo.DISPLAYNAME, resources.getString("menuHiddenParamDN"), //$NON-NLS-1$
		      			}
		      		)
		      		},
					paramTypes
			  	),
				IvjBeanInfo.createMethodDescriptor(org.eclipse.swt.events.MenuListener.class,
					"menuShown",  //$NON-NLS-1$
					new Object[] {
					IvjBeanInfo.DISPLAYNAME, resources.getString("menuShownDN"), //$NON-NLS-1$
					IvjBeanInfo.SHORTDESCRIPTION, resources.getString("menuShownSD"), //$NON-NLS-1$
			    	}, 
			    	new ParameterDescriptor[] {
						IvjBeanInfo.createParameterDescriptor("menuEvent", new Object[] {//$NON-NLS-1$
						IvjBeanInfo.DISPLAYNAME, resources.getString("menuShownParamDN"), //$NON-NLS-1$
			      			}
			      		)
			     	},
					paramTypes
				),	
		};
		aDescriptor = IvjBeanInfo.createEventSetDescriptor(targetClass,
				"menu", new Object[] {//$NON-NLS-1$
				IvjBeanInfo.DISPLAYNAME, resources.getString("MenuDN"), //$NON-NLS-1$
				IvjBeanInfo.SHORTDESCRIPTION, resources.getString("MenuSD"), //$NON-NLS-1$
				IvjBeanInfo.INDEFAULTEVENTSET, Boolean.TRUE,
				IvjBeanInfo.PREFERRED, Boolean.TRUE,
				IvjBeanInfo.EVENTADAPTERCLASS, "org.eclipse.swt.events.MenuAdapter"			 //$NON-NLS-1$
		   		}, 
				aDescriptorList, org.eclipse.swt.events.MenuListener.class,
				"addMenuListener", "removeMenuListener");//$NON-NLS-2$//$NON-NLS-1$

		return aDescriptor;
	}
}

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
 *  $RCSfile: ControlListenerEventSet.java,v $
 *  $Revision: 1.1 $  $Date: 2004-06-01 18:04:09 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;
import java.beans.EventSetDescriptor;
import java.util.ResourceBundle;
 

/**
 * @since 1.0.0
 *
 */
public class ControlListenerEventSet {

	private static ResourceBundle resources = ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.controllistener");  //$NON-NLS-1$

	public static EventSetDescriptor getEventSetDescriptor(Class targetClass) {
		EventSetDescriptor aDescriptor = null;
		Class[] paramTypes = { org.eclipse.swt.events.ControlEvent.class };
		MethodDescriptor aDescriptorList[] = {
				IvjBeanInfo.createMethodDescriptor(org.eclipse.swt.events.ControlListener.class,
					"controlMoved",  //$NON-NLS-1$
					new Object[] {
					IvjBeanInfo.DISPLAYNAME, resources.getString("controlMovedDN"), //$NON-NLS-1$
					IvjBeanInfo.SHORTDESCRIPTION, resources.getString("controlMovedSD"), //$NON-NLS-1$
		    		}, 
		    		new ParameterDescriptor[] {
						IvjBeanInfo.createParameterDescriptor("controlEvent", new Object[] {//$NON-NLS-1$
						IvjBeanInfo.DISPLAYNAME, resources.getString("controlMovedParamDN"), //$NON-NLS-1$
		      			}
		      		)
		      		},
					paramTypes
			  	),
				IvjBeanInfo.createMethodDescriptor(org.eclipse.swt.events.ControlListener.class,
					"controlResized",  //$NON-NLS-1$
					new Object[] {
					IvjBeanInfo.DISPLAYNAME, resources.getString("controlResizedDN"), //$NON-NLS-1$
					IvjBeanInfo.SHORTDESCRIPTION, resources.getString("controlResizedSD"), //$NON-NLS-1$
			    	}, 
			    	new ParameterDescriptor[] {
						IvjBeanInfo.createParameterDescriptor("controlEvent", new Object[] {//$NON-NLS-1$
						IvjBeanInfo.DISPLAYNAME, resources.getString("controlResizedParamDN"), //$NON-NLS-1$
			      			}
			      		)
			     	},
					paramTypes
				),	
		};
		aDescriptor = IvjBeanInfo.createEventSetDescriptor(targetClass,
				"control", new Object[] {//$NON-NLS-1$
				IvjBeanInfo.DISPLAYNAME, resources.getString("ControlDN"), //$NON-NLS-1$
				IvjBeanInfo.SHORTDESCRIPTION, resources.getString("ControlSD"), //$NON-NLS-1$
				IvjBeanInfo.INDEFAULTEVENTSET, Boolean.FALSE,
				IvjBeanInfo.PREFERRED, Boolean.FALSE,
				IvjBeanInfo.EVENTADAPTERCLASS, "org.eclipse.swt.events.ControlAdapter"			 //$NON-NLS-1$
		   		}, 
				aDescriptorList, org.eclipse.swt.events.ControlListener.class,
				"addControlListener", "removeControlListener");//$NON-NLS-2$//$NON-NLS-1$

		return aDescriptor;
	}

}

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
 *  $RCSfile: FocusListenerEventSet.java,v $
 *  $Revision: 1.1 $  $Date: 2004-06-01 18:04:09 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;
import java.util.ResourceBundle;
 

/**
 * @since 1.0.0
 *
 */
public class FocusListenerEventSet {
	private static ResourceBundle resources = ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.focuslistener");  //$NON-NLS-1$

	public static EventSetDescriptor getEventSetDescriptor(Class targetClass) {
		EventSetDescriptor aDescriptor = null;
		Class[] paramTypes = { org.eclipse.swt.events.FocusEvent.class };
		MethodDescriptor aDescriptorList[] = {
				IvjBeanInfo.createMethodDescriptor(org.eclipse.swt.events.FocusListener.class,
					"focusGained",  //$NON-NLS-1$
					new Object[] {
					IvjBeanInfo.DISPLAYNAME, resources.getString("focusGainedDN"), //$NON-NLS-1$
					IvjBeanInfo.SHORTDESCRIPTION, resources.getString("focusGainedSD"), //$NON-NLS-1$
		    		}, 
		    		new ParameterDescriptor[] {
						IvjBeanInfo.createParameterDescriptor("focusEvent", new Object[] {//$NON-NLS-1$
						IvjBeanInfo.DISPLAYNAME, resources.getString("focusGainedParamDN"), //$NON-NLS-1$
		      			}
		      		)
		      		},
					paramTypes
			  	),
				IvjBeanInfo.createMethodDescriptor(org.eclipse.swt.events.FocusListener.class,
					"focusLost",  //$NON-NLS-1$
					new Object[] {
					IvjBeanInfo.DISPLAYNAME, resources.getString("focusLostDN"), //$NON-NLS-1$
					IvjBeanInfo.SHORTDESCRIPTION, resources.getString("focusLostSD"), //$NON-NLS-1$
			    	}, 
			    	new ParameterDescriptor[] {
						IvjBeanInfo.createParameterDescriptor("focusEvent", new Object[] {//$NON-NLS-1$
						IvjBeanInfo.DISPLAYNAME, resources.getString("focusLostParamDN"), //$NON-NLS-1$
			      			}
			      		)
			     	},
					paramTypes
				),	
		};
		aDescriptor = IvjBeanInfo.createEventSetDescriptor(targetClass,
				"focus", new Object[] {//$NON-NLS-1$
				IvjBeanInfo.DISPLAYNAME, resources.getString("FocusDN"), //$NON-NLS-1$
				IvjBeanInfo.SHORTDESCRIPTION, resources.getString("FocusSD"), //$NON-NLS-1$
				IvjBeanInfo.INDEFAULTEVENTSET, Boolean.TRUE,
				IvjBeanInfo.PREFERRED, Boolean.FALSE,
				IvjBeanInfo.EVENTADAPTERCLASS, "org.eclipse.swt.events.FocusAdapter"			 //$NON-NLS-1$
		   		}, 
				aDescriptorList, org.eclipse.swt.events.FocusListener.class,
				"addFocusListener", "removeFocusListener");//$NON-NLS-2$//$NON-NLS-1$

		return aDescriptor;
	}
}

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
 *  $RCSfile: DisposeListenerEventSet.java,v $
 *  $Revision: 1.1 $  $Date: 2004-06-01 18:04:09 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;
import java.util.ResourceBundle;
 

/**
 * @since 1.0.0
 *
 */
public class DisposeListenerEventSet {
	private static ResourceBundle resources = ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.disposelistener");  //$NON-NLS-1$

	public static EventSetDescriptor getEventSetDescriptor(Class targetClass) {
		EventSetDescriptor aDescriptor = null;
		Class[] paramTypes = { org.eclipse.swt.events.DisposeEvent.class };
		MethodDescriptor aDescriptorList[] = {
				IvjBeanInfo.createMethodDescriptor(org.eclipse.swt.events.DisposeListener.class,
					"widgetDisposed",  //$NON-NLS-1$
					new Object[] {
					IvjBeanInfo.DISPLAYNAME, resources.getString("widgetDisposedDN"), //$NON-NLS-1$
					IvjBeanInfo.SHORTDESCRIPTION, resources.getString("widgetDisposedSD"), //$NON-NLS-1$
		    		}, 
		    		new ParameterDescriptor[] {
						IvjBeanInfo.createParameterDescriptor("disposeEvent", new Object[] {//$NON-NLS-1$
						IvjBeanInfo.DISPLAYNAME, resources.getString("widgetDisposedParamDN"), //$NON-NLS-1$
		      			}
		      		)
		      	},
		      	paramTypes
			  	),
		};
		aDescriptor = IvjBeanInfo.createEventSetDescriptor(targetClass,
				"dispose", new Object[] {//$NON-NLS-1$
				IvjBeanInfo.DISPLAYNAME, resources.getString("DisposeDN"), //$NON-NLS-1$
				IvjBeanInfo.SHORTDESCRIPTION, resources.getString("DisposeSD"), //$NON-NLS-1$
				IvjBeanInfo.INDEFAULTEVENTSET, Boolean.TRUE,
				IvjBeanInfo.PREFERRED, Boolean.FALSE,
		   		}, 
				aDescriptorList, org.eclipse.swt.events.DisposeListener.class,
				"addDisposeListener", "removeDisposeListener");//$NON-NLS-2$//$NON-NLS-1$

		return aDescriptor;
	}
}

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
 *  $RCSfile: MouseMoveListenerEventSet.java,v $
 *  $Revision: 1.1 $  $Date: 2004-06-01 18:04:09 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;
import java.util.ResourceBundle;
 

/**
 * @since 1.0.0
 *
 */
public class MouseMoveListenerEventSet {
	private static ResourceBundle resources = ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.mousemovelistener");  //$NON-NLS-1$

	public static EventSetDescriptor getEventSetDescriptor(Class targetClass) {
		EventSetDescriptor aDescriptor = null;
		Class[] paramTypes = { org.eclipse.swt.events.MouseEvent.class };
		MethodDescriptor aDescriptorList[] = {
				IvjBeanInfo.createMethodDescriptor(org.eclipse.swt.events.MouseMoveListener.class,
					"mouseMove",  //$NON-NLS-1$
					new Object[] {
					IvjBeanInfo.DISPLAYNAME, resources.getString("mouseMoveDN"), //$NON-NLS-1$
					IvjBeanInfo.SHORTDESCRIPTION, resources.getString("mouseMoveSD"), //$NON-NLS-1$
		    		}, 
		    		new ParameterDescriptor[] {
						IvjBeanInfo.createParameterDescriptor("mouseEvent", new Object[] {//$NON-NLS-1$
						IvjBeanInfo.DISPLAYNAME, resources.getString("mouseMoveDN"), //$NON-NLS-1$
		      			}
		      		)
		      	},
		      	paramTypes
			  	),
		};
		aDescriptor = IvjBeanInfo.createEventSetDescriptor(targetClass,
				"mouseMove", new Object[] {//$NON-NLS-1$
				IvjBeanInfo.DISPLAYNAME, resources.getString("MouseMoveDN"), //$NON-NLS-1$
				IvjBeanInfo.SHORTDESCRIPTION, resources.getString("MouseMoveSD"), //$NON-NLS-1$
				IvjBeanInfo.INDEFAULTEVENTSET, Boolean.FALSE,
				IvjBeanInfo.PREFERRED, Boolean.FALSE,
		   		}, 
				aDescriptorList, org.eclipse.swt.events.MouseMoveListener.class,
				"addMouseMoveListener", "removeMouseMoveListener");//$NON-NLS-2$//$NON-NLS-1$

		return aDescriptor;
	}
}

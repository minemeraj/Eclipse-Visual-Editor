/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ExtendedModifyListenerEventSet.java,v $
 *  $Revision: 1.1 $  $Date: 2005-05-04 21:02:43 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;
import java.util.ResourceBundle;
 

public class ExtendedModifyListenerEventSet {
	private static ResourceBundle resources = ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.extendedmodifylistener");  //$NON-NLS-1$

	public static EventSetDescriptor getEventSetDescriptor(Class targetClass) {
		EventSetDescriptor aDescriptor = null;
		Class[] paramTypes = { org.eclipse.swt.custom.ExtendedModifyEvent.class };
		MethodDescriptor aDescriptorList[] = {
				IvjBeanInfo.createMethodDescriptor(org.eclipse.swt.custom.ExtendedModifyListener.class,
					"modifyText",  //$NON-NLS-1$
					new Object[] {
					IvjBeanInfo.DISPLAYNAME, resources.getString("modifyTextDN"), //$NON-NLS-1$
					IvjBeanInfo.SHORTDESCRIPTION, resources.getString("modifyTextSD"), //$NON-NLS-1$
					IvjBeanInfo.PREFERRED, Boolean.FALSE,
		    		}, 
		    		new ParameterDescriptor[] {
						IvjBeanInfo.createParameterDescriptor("extendedModifyEvent", new Object[] {//$NON-NLS-1$
						IvjBeanInfo.DISPLAYNAME, resources.getString("modifyTextParamDN"), //$NON-NLS-1$
		      			}
		      		)
		      	},
		      	paramTypes
			  	),
		};
		aDescriptor = IvjBeanInfo.createEventSetDescriptor(targetClass,
				"extendedModify", new Object[] {//$NON-NLS-1$
				IvjBeanInfo.DISPLAYNAME, resources.getString("ExtendedModifyDN"), //$NON-NLS-1$
				IvjBeanInfo.SHORTDESCRIPTION, resources.getString("ExtendedModifySD"), //$NON-NLS-1$
				IvjBeanInfo.INDEFAULTEVENTSET, Boolean.TRUE,
				IvjBeanInfo.PREFERRED, Boolean.FALSE,
		   		}, 
				aDescriptorList, org.eclipse.swt.custom.ExtendedModifyListener.class,
				"addExtendedModifyListener", "removeExtendedModifyListener");//$NON-NLS-2$//$NON-NLS-1$

		return aDescriptor;
	}
}

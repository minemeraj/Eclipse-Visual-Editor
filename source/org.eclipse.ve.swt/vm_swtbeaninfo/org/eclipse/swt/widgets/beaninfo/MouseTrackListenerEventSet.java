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
 *  $RCSfile: MouseTrackListenerEventSet.java,v $
 *  $Revision: 1.1 $  $Date: 2004-06-01 18:04:09 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;
import java.util.ResourceBundle;
 

/**
 * @since 1.0.0
 *
 */
public class MouseTrackListenerEventSet {
	private static ResourceBundle resources = ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.mousetracklistener");  //$NON-NLS-1$

	public static EventSetDescriptor getEventSetDescriptor(Class targetClass) {
		EventSetDescriptor aDescriptor = null;
		Class[] paramTypes = { org.eclipse.swt.events.MouseEvent.class };
		MethodDescriptor aDescriptorList[] = {
				IvjBeanInfo.createMethodDescriptor(org.eclipse.swt.events.MouseTrackListener.class,
					"mouseEnter",  //$NON-NLS-1$
					new Object[] {
					IvjBeanInfo.DISPLAYNAME, resources.getString("mouseEnterDN"), //$NON-NLS-1$
					IvjBeanInfo.SHORTDESCRIPTION, resources.getString("mouseEnterSD"), //$NON-NLS-1$
		    		}, 
		    		new ParameterDescriptor[] {
						IvjBeanInfo.createParameterDescriptor("mouseEvent", new Object[] {//$NON-NLS-1$
						IvjBeanInfo.DISPLAYNAME, resources.getString("mouseEnterParamDN"), //$NON-NLS-1$
		      			}
		      		)
		      		},
					paramTypes
			  	),
				IvjBeanInfo.createMethodDescriptor(org.eclipse.swt.events.MouseTrackListener.class,
					"mouseExit",  //$NON-NLS-1$
					new Object[] {
					IvjBeanInfo.DISPLAYNAME, resources.getString("mouseExitDN"), //$NON-NLS-1$
					IvjBeanInfo.SHORTDESCRIPTION, resources.getString("mouseExitSD"), //$NON-NLS-1$
			    	}, 
			    	new ParameterDescriptor[] {
						IvjBeanInfo.createParameterDescriptor("mouseEvent", new Object[] {//$NON-NLS-1$
						IvjBeanInfo.DISPLAYNAME, resources.getString("mouseExitDN"), //$NON-NLS-1$
			      			}
			      		)
			     	},
					paramTypes
				),	
				IvjBeanInfo.createMethodDescriptor(org.eclipse.swt.events.MouseTrackListener.class,
					"mouseHover",  //$NON-NLS-1$
					new Object[] {
					IvjBeanInfo.DISPLAYNAME, resources.getString("mouseHoverDN"), //$NON-NLS-1$
					IvjBeanInfo.SHORTDESCRIPTION, resources.getString("mouseHoverSD"), //$NON-NLS-1$
			    	}, 
			    	new ParameterDescriptor[] {
						IvjBeanInfo.createParameterDescriptor("mouseEvent", new Object[] {//$NON-NLS-1$
						IvjBeanInfo.DISPLAYNAME, resources.getString("mouseHoverParamDN"), //$NON-NLS-1$
			      			}
			      		)
			     	},
					paramTypes
				),	
		};
		aDescriptor = IvjBeanInfo.createEventSetDescriptor(targetClass,
				"mouseTrack", new Object[] {//$NON-NLS-1$
				IvjBeanInfo.DISPLAYNAME, resources.getString("MouseTrackDN"), //$NON-NLS-1$
				IvjBeanInfo.SHORTDESCRIPTION, resources.getString("MouseTrackSD"), //$NON-NLS-1$
				IvjBeanInfo.INDEFAULTEVENTSET, Boolean.FALSE,
				IvjBeanInfo.PREFERRED, Boolean.FALSE,
				IvjBeanInfo.EVENTADAPTERCLASS, "org.eclipse.swt.events.MouseTrackAdapter"			 //$NON-NLS-1$
		   		}, 
				aDescriptorList, org.eclipse.swt.events.MouseTrackListener.class,
				"addMouseTrackListener", "removeMouseTrackListener");//$NON-NLS-2$//$NON-NLS-1$

		return aDescriptor;
	}
}

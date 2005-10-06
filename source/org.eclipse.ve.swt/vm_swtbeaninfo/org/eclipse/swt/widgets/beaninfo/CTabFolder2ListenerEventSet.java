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
 *  $RCSfile: CTabFolder2ListenerEventSet.java,v $
 *  $Revision: 1.2 $  $Date: 2005-10-06 15:18:51 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;
import java.util.ResourceBundle;

import org.eclipse.jem.beaninfo.vm.BaseBeanInfo;
 

/**
 * @since 1.0.0
 *
 */
public class CTabFolder2ListenerEventSet {
	private static ResourceBundle resources = ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.ctabfolder2listener");  //$NON-NLS-1$

	public static EventSetDescriptor getEventSetDescriptor(Class targetClass) {
		EventSetDescriptor aDescriptor = null;
		Class[] paramTypes = { org.eclipse.swt.custom.CTabFolderEvent.class };
		MethodDescriptor aDescriptorList[] = {
				IvjBeanInfo.createMethodDescriptor(org.eclipse.swt.custom.CTabFolder2Listener.class,
					"close",  //$NON-NLS-1$
					new Object[] {
					IvjBeanInfo.DISPLAYNAME, resources.getString("closeDN"), //$NON-NLS-1$
					IvjBeanInfo.SHORTDESCRIPTION, resources.getString("closeSD"), //$NON-NLS-1$
					IvjBeanInfo.PREFERRED, Boolean.FALSE,
		    		}, 
		    		new ParameterDescriptor[] {
						IvjBeanInfo.createParameterDescriptor("closeEvent", new Object[] {//$NON-NLS-1$
						IvjBeanInfo.DISPLAYNAME, resources.getString("cTabFolderEventParamDN"), //$NON-NLS-1$
		      			}
		      		)
		      		},
					paramTypes
			  	),
				IvjBeanInfo.createMethodDescriptor(org.eclipse.swt.custom.CTabFolder2Listener.class,
					"minimize",  //$NON-NLS-1$
					new Object[] {
					IvjBeanInfo.DISPLAYNAME, resources.getString("minimizeDN"), //$NON-NLS-1$
					IvjBeanInfo.SHORTDESCRIPTION, resources.getString("minimizeSD"), //$NON-NLS-1$
					IvjBeanInfo.PREFERRED, Boolean.FALSE,
			    	}, 
			    	new ParameterDescriptor[] {
						IvjBeanInfo.createParameterDescriptor("minimizeEvent", new Object[] {//$NON-NLS-1$
						IvjBeanInfo.DISPLAYNAME, resources.getString("cTabFolderEventParamDN"), //$NON-NLS-1$
			      			}
			      		)
			     	},
					paramTypes
				),
				IvjBeanInfo.createMethodDescriptor(org.eclipse.swt.custom.CTabFolder2Listener.class,
						"maximize",  //$NON-NLS-1$
						new Object[] {
						IvjBeanInfo.DISPLAYNAME, resources.getString("maximizeDN"), //$NON-NLS-1$
						IvjBeanInfo.SHORTDESCRIPTION, resources.getString("maximizeSD"), //$NON-NLS-1$
						IvjBeanInfo.PREFERRED, Boolean.FALSE,
				    	}, 
				    	new ParameterDescriptor[] {
							IvjBeanInfo.createParameterDescriptor("maxmizeEvent", new Object[] {//$NON-NLS-1$
							IvjBeanInfo.DISPLAYNAME, resources.getString("cTabFolderEventParamDN"), //$NON-NLS-1$
				      			}
				      		)
				     	},
						paramTypes
				),	
				IvjBeanInfo.createMethodDescriptor(org.eclipse.swt.custom.CTabFolder2Listener.class,
						"restore",  //$NON-NLS-1$
						new Object[] {
						IvjBeanInfo.DISPLAYNAME, resources.getString("restoreDN"), //$NON-NLS-1$
						IvjBeanInfo.SHORTDESCRIPTION, resources.getString("restoreSD"), //$NON-NLS-1$
						IvjBeanInfo.PREFERRED, Boolean.FALSE,
				    	}, 
				    	new ParameterDescriptor[] {
							IvjBeanInfo.createParameterDescriptor("restoreEvent", new Object[] {//$NON-NLS-1$
							IvjBeanInfo.DISPLAYNAME, resources.getString("cTabFolderEventParamDN"), //$NON-NLS-1$
				      			}
				      		)
				     	},
						paramTypes
				),	
				IvjBeanInfo.createMethodDescriptor(org.eclipse.swt.custom.CTabFolder2Listener.class,
						"showList",  //$NON-NLS-1$
						new Object[] {
						IvjBeanInfo.DISPLAYNAME, resources.getString("showListDN"), //$NON-NLS-1$
						IvjBeanInfo.SHORTDESCRIPTION, resources.getString("showListSD"), //$NON-NLS-1$
						IvjBeanInfo.PREFERRED, Boolean.FALSE,
				    	}, 
				    	new ParameterDescriptor[] {
							IvjBeanInfo.createParameterDescriptor("showListEvent", new Object[] {//$NON-NLS-1$
							IvjBeanInfo.DISPLAYNAME, resources.getString("cTabFolderEventParamDN"), //$NON-NLS-1$
				      			}
				      		)
				     	},
						paramTypes
				),	
		};
		aDescriptor = IvjBeanInfo.createEventSetDescriptor(targetClass,
				"cTabFolder2", new Object[] {//$NON-NLS-1$
				IvjBeanInfo.DISPLAYNAME, resources.getString("CTabFolder2DN"), //$NON-NLS-1$
				IvjBeanInfo.SHORTDESCRIPTION, resources.getString("CTabFolder2SD"), //$NON-NLS-1$
				IvjBeanInfo.INDEFAULTEVENTSET, Boolean.FALSE,
				IvjBeanInfo.PREFERRED, Boolean.TRUE,
				BaseBeanInfo.EVENTADAPTERCLASS, "org.eclipse.swt.custom.CTabFolder2Adapter"			 //$NON-NLS-1$
		   		}, 
				aDescriptorList, org.eclipse.swt.custom.CTabFolder2Listener.class,
				"addCTabFolder2Listener", "removeCTabFolder2Listener");//$NON-NLS-2$//$NON-NLS-1$

		return aDescriptor;
	}
}

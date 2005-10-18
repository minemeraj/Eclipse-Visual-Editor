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
 *  $RCSfile: FileDialogBeanInfo.java,v $
 *  $Revision: 1.5 $  $Date: 2005-10-18 15:32:23 $ 
 */

import java.beans.*;

import org.eclipse.jem.beaninfo.common.IBaseBeanInfoConstants;
public class FileDialogBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle resfiledialog = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.filedialog");  //$NON-NLS-1$
	
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return java.awt.FileDialog.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		/* Create and return the FileDialogBeanInfobean descriptor. */
		aDescriptor = new java.beans.BeanDescriptor(java.awt.FileDialog.class);
		aDescriptor.setDisplayName(resfiledialog.getString("FileDialogDN")); //$NON-NLS-1$
		aDescriptor.setShortDescription(resfiledialog.getString("FileDialogSD")); //$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/fildia32.gif");//$NON-NLS-2$//$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/fildia16.gif");//$NON-NLS-2$//$NON-NLS-1$
	} catch (Throwable exception) {
	};
	return aDescriptor;
}
/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	return( new java.beans.EventSetDescriptor[0]);
}
		/**
	 * @return an icon of the specified kind for JButton
	 */
	public java.awt.Image getIcon(int kind) {
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("fildia32.gif");//$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("fildia16.gif");//$NON-NLS-1$
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
			// getDirectory()
			super.createMethodDescriptor(getBeanClass(),"getDirectory", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getDirectory()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resfiledialog.getString("getDirSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getFile()
			super.createMethodDescriptor(getBeanClass(),"getFile", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getFile()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resfiledialog.getString("getFileSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getFilenameFilter()
			super.createMethodDescriptor(getBeanClass(),"getFilenameFilter", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getFilenameFilter()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the filename filter",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getMode()
			super.createMethodDescriptor(getBeanClass(),"getMode", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getMode()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resfiledialog.getString("getModeSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// setDirectory(String)
			super.createMethodDescriptor(getBeanClass(),"setDirectory", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setDirectory(String)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the directory",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resfiledialog.getString("dirParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Directory",
	      			})
	      		},
	      		new Class[] { 
	      			String.class 
	      		}	    		
		  	),
			// setFile(String)
			super.createMethodDescriptor(getBeanClass(),"setFile", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setFile(String)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resfiledialog.getString("setFileSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resfiledialog.getString("fileParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Default file",
	      			})
	      		},
	      		new Class[] { 
	      			String.class 
	      		}	    		
		  	),
			// setFilenameFilter(FilenameFilter)
			super.createMethodDescriptor(getBeanClass(),"setFilenameFilter", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setFilenameFilter(FilenameFilter)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the filename filter",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resfiledialog.getString("filterParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "filename filter",
	      			})
	      		},
	      		new Class[] { 
	      			java.io.FilenameFilter.class 
	      		}	    		
		  	),
			// setMode(int)
			super.createMethodDescriptor(getBeanClass(),"setMode", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setMode(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resfiledialog.getString("setModeSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resfiledialog.getString("modeParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "LOAD or SAVE",
	      			})
	      		},
	      		new Class[] { 
	      			int.class 
	      		}	    		
		  	)
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
			// directory
			super.createPropertyDescriptor(getBeanClass(),"directory", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resfiledialog.getString("directoryDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resfiledialog.getString("directorySD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
			// filenameFilter
			super.createPropertyDescriptor(getBeanClass(),"filenameFilter", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resfiledialog.getString("filenameFilterDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resfiledialog.getString("filenameFilterSD"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
			// file
			super.createPropertyDescriptor(getBeanClass(),"file", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resfiledialog.getString("fileDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resfiledialog.getString("fileSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
			// layout - hide it
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
	    	// mode
	    	super.createPropertyDescriptor(getBeanClass(),"mode", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resfiledialog.getString("modeDN"),		    	 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resfiledialog.getString("modeSD"), //$NON-NLS-1$
	      	IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      				resfiledialog.getString("LOADEnumDN"), new Integer(java.awt.FileDialog.LOAD), //$NON-NLS-1$
	      				"java.awt.FileDialog.LOAD",//$NON-NLS-1$
	      				resfiledialog.getString("SAVEEnumDN"), new Integer(java.awt.FileDialog.SAVE), //$NON-NLS-1$
	      				"java.awt.FileDialog.SAVE",//$NON-NLS-1$
	    			}
	    		}
	    	),
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}
}

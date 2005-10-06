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
 *  $RCSfile: LabelBeanInfo.java,v $
 *  $Revision: 1.4 $  $Date: 2005-10-06 15:18:44 $ 
 */

import java.beans.*;

import org.eclipse.jem.beaninfo.vm.IBaseBeanInfoConstants;

public class LabelBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle reslabel = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.label");  //$NON-NLS-1$
	
	
public Class getBeanClass() {
	return java.awt.Label.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		/* Create and return the LabelBeanInfobean descriptor. */
		aDescriptor = new java.beans.BeanDescriptor(java.awt.Label.class);
		aDescriptor.setDisplayName(reslabel.getString("LabelDN")); //$NON-NLS-1$
		aDescriptor.setShortDescription(reslabel.getString("LabelSD")); //$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/label32.gif");//$NON-NLS-2$//$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/label16.gif");//$NON-NLS-2$//$NON-NLS-1$
	} catch (Throwable exception) {
	};
	return aDescriptor;
}
/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	return (new java.beans.EventSetDescriptor[0]);
}
	/**
	 * @return an icon of the specified kind for JButton
	 */
	public java.awt.Image getIcon(int kind) {
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("label32.gif");//$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("label16.gif");//$NON-NLS-1$
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
	      		// SHORTDESCRIPTION, "Create the label peer",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getAlignment()
			super.createMethodDescriptor(getBeanClass(),"getAlignment", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getAlignment()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, reslabel.getString("alignmentSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getText()
			super.createMethodDescriptor(getBeanClass(),"getText", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getText()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Label text",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// setAlignment(int)
			super.createMethodDescriptor(getBeanClass(),"setAlignment", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setAlignment(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, reslabel.getString("setAlignmentSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reslabel.getString("alignmentParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "LEFT, RIGHT or CENTER",
	      			}
	      		)
	      		},
	      		new Class[] { 
	      			int.class 
	      		}	    		
		  	),
			// setText(String)
			super.createMethodDescriptor(getBeanClass(),"setText", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setText(String)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the label text",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reslabel.getString("textParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Label text",
	      			})
	      		},
	      		new Class[] { 
	      			String.class 
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
			// alignment
			super.createPropertyDescriptor(getBeanClass(),"alignment", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, reslabel.getString("alignmentDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, reslabel.getString("alignmentClassSD"), //$NON-NLS-1$
	      	IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      			reslabel.getString("LEFTEnumDN"), new Integer(java.awt.Label.LEFT), //$NON-NLS-1$
	      				"java.awt.Label.LEFT",//$NON-NLS-1$
	      			reslabel.getString("CENTEREnumDN"), new Integer(java.awt.Label.CENTER), //$NON-NLS-1$
	      				"java.awt.Label.CENTER",//$NON-NLS-1$
	      			reslabel.getString("RIGHTEnumDN"), new Integer(java.awt.Label.RIGHT), //$NON-NLS-1$
	      				"java.awt.Label.RIGHT"	      			//$NON-NLS-1$
	    		}
	    	}
	    	),
			// text
			super.createPropertyDescriptor(getBeanClass(),"text", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, reslabel.getString("textDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, reslabel.getString("testSD"), //$NON-NLS-1$
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

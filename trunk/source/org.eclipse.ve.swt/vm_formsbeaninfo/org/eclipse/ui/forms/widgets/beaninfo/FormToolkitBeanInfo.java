/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: FormToolkitBeanInfo.java,v $
 *  $Revision: 1.6 $  $Date: 2006-02-21 17:16:40 $ 
 */
package org.eclipse.ui.forms.widgets.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.beaninfo.ControlMessages;
import org.eclipse.swt.widgets.beaninfo.IvjBeanInfo;
import org.eclipse.ui.forms.widgets.FormToolkit;

import org.eclipse.jem.beaninfo.common.IBaseBeanInfoConstants;
import org.eclipse.jem.beaninfo.vm.BaseBeanInfo;
 
/**
 * FormToolkit Beaninfo
 * @since 1.0.0
 */
public class FormToolkitBeanInfo extends IvjBeanInfo {

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return FormToolkit.class;
	}
	
	public BeanDescriptor getBeanDescriptor() {
		return createBeanDescriptor(getBeanClass(), 
			new Object[] {BaseBeanInfo.FACTORY_CREATION, new Object[][] {
				new Object[] {"new org.eclipse.ui.forms.widgets.FormToolkit(org.eclipse.swt.widgets.Display.getCurrent())", Boolean.TRUE, Boolean.TRUE},
				new Object[] {
					"createButton", "org.eclipse.swt.widgets.Button", Boolean.FALSE, new Object[] {"parentComposite", "text", "style"}
				},
				new Object[] {
					"createComposite", "org.eclipse.swt.widgets.Composite", Boolean.FALSE, new Object[] {"parentComposite"}, new Object[] {"parentComposite", "style"}
				},
				new Object[] {
					"createCompositeSeparator", "org.eclipse.swt.widgets.Composite", Boolean.FALSE, new Object[] {"parentComposite"},
				},
				new Object[] {
					"createExpandableComposite", "org.eclipse.ui.forms.widgets.ExpandableComposite", Boolean.FALSE, new Object[] {"parentComposite", "style"},
				},
				new Object[] {
					"createSection", "org.eclipse.ui.forms.widgets.Section", Boolean.FALSE, new Object[] {"parentComposite", "style"},
				},				
				new Object[] {
					"createForm", "org.eclipse.ui.forms.widgets.Form", Boolean.FALSE, new Object[] {"parentComposite"},
				},				
				new Object[] {
					"createLabel", "org.eclipse.swt.widgets.Label", Boolean.FALSE, new Object[] {"parentComposite", "text"}, new Object[] {"parentComposite", "text", "style"}
				},
				new Object[] {
					"createText", "org.eclipse.swt.widgets.Text", Boolean.FALSE, new Object[] {"parentComposite", "text"}, new Object[] {"parentComposite", "text", "style"}
				},
				new Object[] {
					"createHyperlink", "org.eclipse.ui.forms.Hyperlink", Boolean.FALSE, new Object[] {"parentComposite", "text", "style"}
				},
				new Object[] {
					"createImageHyperlink", "org.eclipse.ui.forms.ImageHyperlink", Boolean.FALSE, new Object[] {"parentComposite", "style"}
				},
				new Object[] {
					"createTable", "org.eclipse.swt.widgets.Table", Boolean.FALSE, new Object[] {"parentComposite", "style"}
				},
				new Object[] {
					"createTree", "org.eclipse.swt.widgets.Tree", Boolean.FALSE, new Object[] {"parentComposite", "style"}
				},					
			}
		}
		);
	}
	
	public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor aDescriptorList[] = {
		    	super.createPropertyDescriptor(getBeanClass(),"borderStyle", new Object[] { //$NON-NLS-1$
		    		DISPLAYNAME, "border style", 
		    		SHORTDESCRIPTION, "the style to use for borders of created controls.",
		    		PREFERRED, Boolean.TRUE,
		    		IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
		    				"border", new Integer(SWT.BORDER), "org.eclipse.swt.SWT.BORDER", 
		    				"no border", new Integer(SWT.NONE), "org.eclipse.swt.SWT.NONE",
		    		}
		    	}),
				super.createPropertyDescriptor(getBeanClass(),"background", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, ControlMessages.getString("backgroundDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, ControlMessages.getString("backgroundSD"), //$NON-NLS-1$
				}),
		    	super.createPropertyDescriptor(getBeanClass(),"orientation", new Object[] { //$NON-NLS-1$
			      	DISPLAYNAME, "control orientation", 
			      	SHORTDESCRIPTION, "the orientation for created controls.",
			      	PREFERRED, Boolean.TRUE,
			      	IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
			      		"default", new Integer(SWT.NONE), "org.eclipse.swt.SWT.NONE",
			      		ControlMessages.getString("ControlBeanInfo.StyleBits.ControlOrientation.Value.LeftToRight") , new Integer(SWT.LEFT_TO_RIGHT), "org.eclipse.swt.SWT.LEFT_TO_RIGHT" ,				 //$NON-NLS-1$ //$NON-NLS-2$
			      		ControlMessages.getString("ControlBeanInfo.StyleBits.ControlOrientation.Value.RightToLeft") , new Integer(SWT.RIGHT_TO_LEFT), "org.eclipse.swt.SWT.RIGHT_TO_LEFT" ,				 //$NON-NLS-1$ //$NON-NLS-2$
			      	}
			      })
			};
			return aDescriptorList;
		} catch (Throwable exception) {
			handleException(exception);
		};
		return null;
	}
	
}

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
 *  $Revision: 1.4 $  $Date: 2006-02-10 21:53:46 $ 
 */
package org.eclipse.ui.forms.widgets.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;

import org.eclipse.swt.SWT;
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
			new Object[] {
			BaseBeanInfo.FACTORY_CREATION, new Object[][] {
				new Object[] {"new org.eclipse.ui.forms.widgets.FormToolkit(org.eclipse.swt.widgets.Display.getCurrent())", Boolean.TRUE, Boolean.TRUE}, //$NON-NLS-1$
				new Object[] {
					"createButton", "org.eclipse.swt.widgets.Button", Boolean.FALSE, new Object[] {"parentComposite", "text", "style"} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				},
				new Object[] {
					"createComposite", "org.eclipse.swt.widgets.Composite", Boolean.FALSE, new Object[] {"parentComposite"}, new Object[] {"parentComposite", "style"} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				},
				new Object[] {
					"createCompositeSeparator", "org.eclipse.swt.widgets.Composite", Boolean.FALSE, new Object[] {"parentComposite"}, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				},
				new Object[] {
					"createExpandableComposite", "org.eclipse.ui.forms.widgets.ExpandableComposite", Boolean.FALSE, new Object[] {"parentComposite", "style"}, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				},
				new Object[] {
					"createForm", "org.eclipse.ui.forms.widgets.Form", Boolean.FALSE, new Object[] {"parentComposite"}, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				},				
				new Object[] {
					"createLabel", "org.eclipse.swt.widgets.Label", Boolean.FALSE, new Object[] {"parentComposite", "text"}, new Object[] {"parentComposite", "text", "style"} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
				},
			}
			});
	}
	
	public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor aDescriptorList[] = {
		    	super.createPropertyDescriptor(getBeanClass(),"borderStyle", new Object[] { //$NON-NLS-1$
		    		DISPLAYNAME, FormToolkitMessages.getString("FormToolkitBeanInfo.borderStyleDN"), //$NON-NLS-1$
		    		SHORTDESCRIPTION, FormToolkitMessages.getString("FormToolkitBeanInfo.borderStyleSD"), //$NON-NLS-1$
		    		PREFERRED, Boolean.TRUE,
		    		IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
		    				FormToolkitMessages.getString("FormToolkitBeanInfo.borderDN"), new Integer(SWT.BORDER), "org.eclipse.swt.SWT.BORDER",   //$NON-NLS-1$ //$NON-NLS-2$
		    				FormToolkitMessages.getString("FormToolkitBeanInfo.noBorderDN"), new Integer(SWT.NONE), "org.eclipse.swt.SWT.NONE",  //$NON-NLS-1$ //$NON-NLS-2$
		    		}
		    	}),
				super.createPropertyDescriptor(getBeanClass(),"background", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, FormToolkitMessages.getString("backgroundDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, FormToolkitMessages.getString("backgroundSD"), //$NON-NLS-1$
				}),
		    	super.createPropertyDescriptor(getBeanClass(),"orientation", new Object[] { //$NON-NLS-1$
			      	DISPLAYNAME, FormToolkitMessages.getString("FormToolkitBeanInfo.orientationDN"), //$NON-NLS-1$
			      	SHORTDESCRIPTION, FormToolkitMessages.getString("FormToolkitBeanInfo.orientationSD"), //$NON-NLS-1$
			      	PREFERRED, Boolean.TRUE,
			      	IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
			      		FormToolkitMessages.getString("FormToolkitBeanInfo.defaultDN"), new Integer(SWT.NONE), "org.eclipse.swt.SWT.NONE",  //$NON-NLS-1$ //$NON-NLS-2$
			      		FormToolkitMessages.getString("ControlBeanInfo.StyleBits.ControlOrientation.Value.LeftToRight") , new Integer(SWT.LEFT_TO_RIGHT), "org.eclipse.swt.SWT.LEFT_TO_RIGHT" ,				 //$NON-NLS-1$ //$NON-NLS-2$
			      		FormToolkitMessages.getString("ControlBeanInfo.StyleBits.ControlOrientation.Value.RightToLeft") , new Integer(SWT.RIGHT_TO_LEFT), "org.eclipse.swt.SWT.RIGHT_TO_LEFT" ,				 //$NON-NLS-1$ //$NON-NLS-2$
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

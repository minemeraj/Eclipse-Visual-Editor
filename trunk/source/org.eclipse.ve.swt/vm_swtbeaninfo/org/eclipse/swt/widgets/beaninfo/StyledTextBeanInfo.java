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
 *  $RCSfile: StyledTextBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2007-05-25 04:20:17 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;

import org.eclipse.swt.SWT;

public class StyledTextBeanInfo extends IvjBeanInfo {

	public Class getBeanClass() {
		return org.eclipse.swt.custom.StyledText.class;
	}
	
	/* (non-Javadoc)
	 * @see java.beans.BeanInfo#getEventSetDescriptors()
	 */
	public EventSetDescriptor[] getEventSetDescriptors() {
		return new EventSetDescriptor[] {
				BidiSegmentListenerEventSet.getEventSetDescriptor(getBeanClass()),
				ExtendedModifyListenerEventSet.getEventSetDescriptor(getBeanClass()),
				LineBackgroundListenerEventSet.getEventSetDescriptor(getBeanClass()),
				LineStyleListenerEventSet.getEventSetDescriptor(getBeanClass()),
				ModifyListenerEventSet.getEventSetDescriptor(getBeanClass()),
				SelectionListenerEventSet.getEventSetDescriptor(getBeanClass()),
				VerifyKeyListenerEventSet.getEventSetDescriptor(getBeanClass()),
				VerifyListenerEventSet.getEventSetDescriptor(getBeanClass()),
		};
	}
	
	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
		descriptor.setValue(
			SweetHelper.STYLE_BITS_ID,
		    new Object[] [] {			
				{ "readOnly" , StyledTextMessages.getString("StyledTextBeanInfo.StyleBits.ReadOnly.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
					StyledTextMessages.getString("StyledTextBeanInfo.StyleBits.ReadOnly.Value.READ_ONLY") , "org.eclipse.swt.SWT.READ_ONLY" , new Integer(SWT.READ_ONLY)//$NON-NLS-1$ //$NON-NLS-2$
				} } ,
				{ "lineMode" , StyledTextMessages.getString("StyledTextBeanInfo.StyleBits.LineMode.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
					StyledTextMessages.getString("StyledTextBeanInfo.StyleBits.LineMode.Value.SINGLE") , "org.eclipse.swt.SWT.SINGLE" , new Integer(SWT.SINGLE),//$NON-NLS-1$ //$NON-NLS-2$
					StyledTextMessages.getString("StyledTextBeanInfo.StyleBits.LineMode.Value.MULTI") , "org.eclipse.swt.SWT.MULTI" , new Integer(SWT.MULTI)//$NON-NLS-1$ //$NON-NLS-2$
				} } ,
				{ "fullSelection" , StyledTextMessages.getString("StyledTextBeanInfo.StyleBits.FullSelection.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
					StyledTextMessages.getString("StyledTextBeanInfo.StyleBits.FullSelection.Value.FULL_SELECTION") , "org.eclipse.swt.SWT.FULL_SELECTION" , new Integer(SWT.FULL_SELECTION)//$NON-NLS-1$ //$NON-NLS-2$
				} } ,
				{ "wrap" , StyledTextMessages.getString("StyledTextBeanInfo.StyleBits.Wrap.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
					StyledTextMessages.getString("StyledTextBeanInfo.StyleBits.Wrap.Value.WRAP") , "org.eclipse.swt.SWT.WRAP" , new Integer(SWT.WRAP)//$NON-NLS-1$ //$NON-NLS-2$
				} }		
			}
		);
		SweetHelper.mergeSuperclassStyleBits(descriptor);
		return descriptor;
	}

	/**
	 * Return the property descriptors for this bean.
	 * @return java.beans.PropertyDescriptor[]
	 */
	public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor aDescriptorList[] = {
				// caret offset
				super.createPropertyDescriptor(getBeanClass(),"caretOffset", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, StyledTextMessages.getString("caretOffsetDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, StyledTextMessages.getString("caretOffsetSD"), //$NON-NLS-1$
					EXPERT, Boolean.TRUE,
				}
				),
				// content
				super.createPropertyDescriptor(getBeanClass(),"content", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, StyledTextMessages.getString("contentDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, StyledTextMessages.getString("contentSD"), //$NON-NLS-1$
					EXPERT, Boolean.TRUE,
				}
				),				
				// double click enabled
				super.createPropertyDescriptor(getBeanClass(),"doubleClickEnabled", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, StyledTextMessages.getString("doubleClickEnabledDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, StyledTextMessages.getString("doubleClickEnabledSD"), //$NON-NLS-1$
				}
				),
				// editable
				super.createPropertyDescriptor(getBeanClass(),"editable", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, StyledTextMessages.getString("editableDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, StyledTextMessages.getString("editableSD"), //$NON-NLS-1$
					EXPERT, Boolean.TRUE,
				}
				),
				// horizontal index
				super.createPropertyDescriptor(getBeanClass(),"horizontalIndex", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, StyledTextMessages.getString("horizontalIndexDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, StyledTextMessages.getString("horizontalIndexSD"), //$NON-NLS-1$
					EXPERT, Boolean.TRUE,
				}
				),
				// horizontal pixel
				super.createPropertyDescriptor(getBeanClass(),"horizontalPixel", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, StyledTextMessages.getString("horizontalPixelDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, StyledTextMessages.getString("horizontalPixelSD"), //$NON-NLS-1$
					EXPERT, Boolean.TRUE,
				}
				),
				// selectionBackground
				super.createPropertyDescriptor(getBeanClass(),"selectionBackground", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, StyledTextMessages.getString("selectionBackgroundDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, StyledTextMessages.getString("selectionBackgroundSD"), //$NON-NLS-1$
				}
				),
				// selectionForeground
				super.createPropertyDescriptor(getBeanClass(),"selectionForeground", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, StyledTextMessages.getString("selectionForegroundDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, StyledTextMessages.getString("selectionForegroundSD"), //$NON-NLS-1$
				}
				),
				// tabs
				super.createPropertyDescriptor(getBeanClass(),"tabs", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, StyledTextMessages.getString("tabsDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, StyledTextMessages.getString("tabsSD"), //$NON-NLS-1$
				}
				),
				// text
				super.createPropertyDescriptor(getBeanClass(),"text", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, StyledTextMessages.getString("textDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, StyledTextMessages.getString("textSD"), //$NON-NLS-1$
				}
				),
				// textLimit
				super.createPropertyDescriptor(getBeanClass(),"textLimit", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, StyledTextMessages.getString("textLimitDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, StyledTextMessages.getString("textLimitSD"), //$NON-NLS-1$
				}
				),
				// topIndex
				super.createPropertyDescriptor(getBeanClass(),"topIndex", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, StyledTextMessages.getString("topIndexDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, StyledTextMessages.getString("topIndexSD"), //$NON-NLS-1$
					EXPERT, Boolean.TRUE,
				}
				),
				// topPixel
				super.createPropertyDescriptor(getBeanClass(),"topPixel", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, StyledTextMessages.getString("topPixelDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, StyledTextMessages.getString("topPixelSD"), //$NON-NLS-1$
					EXPERT, Boolean.TRUE,
				}
				),
			};
			return aDescriptorList;
		} catch (Throwable exception) {
			handleException(exception);
		};
		return null;
	}
	
	protected PropertyDescriptor[] overridePropertyDescriptors(PropertyDescriptor[] pds) {
		PropertyDescriptor[] newPDs = pds.clone();

		replacePropertyDescriptor(newPDs, "layout", null, new Object[] { //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,});

		return newPDs;
	}
}

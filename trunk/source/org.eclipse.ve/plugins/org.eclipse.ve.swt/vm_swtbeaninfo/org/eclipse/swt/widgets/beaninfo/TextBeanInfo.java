/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TextBeanInfo.java,v $
 *  $Revision: 1.9 $  $Date: 2005-08-24 23:52:54 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;

import org.eclipse.swt.SWT;
 
/**
 * 
 * @since 1.0.0
 */
public class TextBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Text.class;
	}
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
		new Object[] [] {
			{ "textAlignment" , TextMessages.getString("TextBeanInfo.StyleBits.TextAlignment.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
			    TextMessages.getString("TextBeanInfo.StyleBits.TextAlignment.Value.Center") , "org.eclipse.swt.SWT.CENTER" , new Integer(SWT.CENTER) , //$NON-NLS-1$ //$NON-NLS-2$
				TextMessages.getString("TextBeanInfo.StyleBits.TextAlignment.Value.Left") , "org.eclipse.swt.SWT.LEFT" ,  new Integer(SWT.LEFT), 				 //$NON-NLS-1$ //$NON-NLS-2$
				TextMessages.getString("TextBeanInfo.StyleBits.TextAlignment.Value.Right") , "org.eclipse.swt.SWT.RIGHT" , new Integer(SWT.RIGHT)  //$NON-NLS-1$ //$NON-NLS-2$
			} } ,
			{ "readOnly" , TextMessages.getString("TextBeanInfo.StyleBits.ReadOnly.Name"), Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				TextMessages.getString("TextBeanInfo.StyleBits.ReadOnly.Value.ReadOnly") , "org.eclipse.swt.SWT.READ_ONLY" , new Integer(SWT.READ_ONLY)					 //$NON-NLS-1$ //$NON-NLS-2$
			} },
			{ "password" , TextMessages.getString("TextBeanInfo.StyleBits.Password.Name"), Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				TextMessages.getString("TextBeanInfo.StyleBits.Password.Value.Password") , "org.eclipse.swt.SWT.PASSWORD" , new Integer(SWT.PASSWORD)					 //$NON-NLS-1$ //$NON-NLS-2$
			} },
			{ "lines" , TextMessages.getString("TextBeanInfo.StyleBits.Lines.Name"), Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				TextMessages.getString("TextBeanInfo.StyleBits.Lines.Value.Single") , "org.eclipse.swt.SWT.SINGLE" , new Integer(SWT.SINGLE) ,					 //$NON-NLS-1$ //$NON-NLS-2$
				TextMessages.getString("TextBeanInfo.StyleBits.Lines.Value.Multi") , "org.eclipse.swt.SWT.MULTI" , new Integer(SWT.MULTI)				 //$NON-NLS-1$ //$NON-NLS-2$
			} },			
			{ "wrap" , TextMessages.getString("TextBeanInfo.StyleBits.Wrap.Name"), Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				TextMessages.getString("TextBeanInfo.StyleBits.Wrap.Value.Wrap") , "org.eclipse.swt.SWT.WRAP" , new Integer(SWT.WRAP)					 //$NON-NLS-1$ //$NON-NLS-2$
			} }
		}
	);
	SweetHelper.mergeSuperclassStyleBits(descriptor);
	return descriptor;
}

/* (non-Javadoc)
 * @see java.beans.BeanInfo#getEventSetDescriptors()
 */
public EventSetDescriptor[] getEventSetDescriptors() {
	return new EventSetDescriptor[] {
			ModifyListenerEventSet.getEventSetDescriptor(getBeanClass()),
			SelectionListenerEventSet.getEventSetDescriptor(getBeanClass()),
			VerifyListenerEventSet.getEventSetDescriptor(getBeanClass())
	};
}

/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
			// borderWidth
			super.createPropertyDescriptor(getBeanClass(),"borderWidth", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TextMessages.getString("borderWidthDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TextMessages.getString("borderWidthSD"), //$NON-NLS-1$
			}
			),
			// caretLineNumber
			super.createPropertyDescriptor(getBeanClass(),"caretLineNumber", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TextMessages.getString("caretLineNumberDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TextMessages.getString("caretLineNumberSD"), //$NON-NLS-1$
			}
			),
			// caretLocation
			super.createPropertyDescriptor(getBeanClass(),"caretLocation", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TextMessages.getString("caretLocationDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TextMessages.getString("caretLocationSD"), //$NON-NLS-1$
			}
			),
			// caretPosition
			super.createPropertyDescriptor(getBeanClass(),"caretPosition", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TextMessages.getString("caretPositionDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TextMessages.getString("caretPositionSD"), //$NON-NLS-1$
			}
			),
			// charCount
			super.createPropertyDescriptor(getBeanClass(),"charCount", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TextMessages.getString("charCountDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TextMessages.getString("charCountSD"), //$NON-NLS-1$
			}
			),
			// doubleClickEnabled
			super.createPropertyDescriptor(getBeanClass(),"doubleClickEnabled", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TextMessages.getString("doubleClickEnabledDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TextMessages.getString("doubleClickEnabledSD"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
			}
			),
			// echoChar
			super.createPropertyDescriptor(getBeanClass(),"echoChar", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TextMessages.getString("echoCharDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TextMessages.getString("echoCharSD"), //$NON-NLS-1$
			}
			),
			// editable
			super.createPropertyDescriptor(getBeanClass(),"editable", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TextMessages.getString("editableDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TextMessages.getString("editableSD"), //$NON-NLS-1$
			}
			),
			// lineCount
			super.createPropertyDescriptor(getBeanClass(),"lineCount", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TextMessages.getString("lineCountDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TextMessages.getString("lineCountSD"), //$NON-NLS-1$
			}
			),
			// lineDelimiter
			super.createPropertyDescriptor(getBeanClass(),"lineDelimiter", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TextMessages.getString("lineDelimiterDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TextMessages.getString("lineDelimiterSD"), //$NON-NLS-1$
			}
			),
			// lineHeight
			super.createPropertyDescriptor(getBeanClass(),"lineHeight", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TextMessages.getString("lineHeightDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TextMessages.getString("lineHeightSD"), //$NON-NLS-1$
			}
			),
			// orientation
			super.createPropertyDescriptor(getBeanClass(),"orientation", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TextMessages.getString("orientationDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TextMessages.getString("orientationSD"), //$NON-NLS-1$
			}
			),
			// selection
			super.createPropertyDescriptor(getBeanClass(),"selection", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TextMessages.getString("selectionDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TextMessages.getString("selectionSD"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
			}
			),
			// selectionCount
			super.createPropertyDescriptor(getBeanClass(),"selectionCount", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TextMessages.getString("selectionCountDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TextMessages.getString("selectionCountSD"), //$NON-NLS-1$
			}
			),
			// selectionText
			super.createPropertyDescriptor(getBeanClass(),"selectionText", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TextMessages.getString("selectionTextDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TextMessages.getString("selectionTextSD"), //$NON-NLS-1$
			}
			),
			// tabs
			super.createPropertyDescriptor(getBeanClass(),"tabs", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TextMessages.getString("tabsDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TextMessages.getString("tabsSD"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
			}
			),
			// text
			super.createPropertyDescriptor(getBeanClass(),"text", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TextMessages.getString("textDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TextMessages.getString("textSD"), //$NON-NLS-1$
				FACTORY_CREATION , new Object[] { new Object[] { "org.eclipse.ui.forms.widgets.FormToolkit" , "createText" , new Integer(1) , 
						new String[] { "org.eclipse.swt.widgets.Composite" , "java.lang.String" , "int"} } }				
			}
			),
			// textLimit
			super.createPropertyDescriptor(getBeanClass(),"textLimit", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TextMessages.getString("textLimitDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TextMessages.getString("textLimitSD"), //$NON-NLS-1$
			}
			),
			// topIndex
			super.createPropertyDescriptor(getBeanClass(),"topIndex", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TextMessages.getString("topIndexDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TextMessages.getString("topIndexSD"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
			}
			),
			// topPixel
			super.createPropertyDescriptor(getBeanClass(),"topPixel", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TextMessages.getString("topPixelDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TextMessages.getString("topPixelSD"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
			}
			),			
			// style bit
			super.createPropertyDescriptor(getBeanClass(),"style", new Object[] { //$NON-NLS-1$
				FACTORY_CREATION  , new Object[] { 
						new Object[] { "org.eclipse.ui.forms.widgets.FormToolkit" , "createText" , new Integer(2) , 
								new String[] { "org.eclipse.swt.widgets.Composite" , "java.lang.String" , "int"} } ,
						new Object[] { "org.eclipse.ui.forms.widgets.FormToolkit" , "createText" , new Integer(1) , 
								new String[] { "org.eclipse.swt.widgets.Composite" , "int"} } }					
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

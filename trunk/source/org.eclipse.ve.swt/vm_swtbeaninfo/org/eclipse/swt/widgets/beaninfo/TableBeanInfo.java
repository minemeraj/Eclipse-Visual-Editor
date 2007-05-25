/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TableBeanInfo.java,v $
 *  $Revision: 1.20 $  $Date: 2007-05-25 04:20:17 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;

import org.eclipse.swt.SWT;
 
/**
 * 
 * @since 1.0.0
 */
public class TableBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Table.class;
	}
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
	    new Object[] [] {				
			{ "style" , TableMessages.getString("TableBeanInfo.StyleBits.Style.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
			    TableMessages.getString("TableBeanInfo.StyleBits.Style.Value.Check") , "org.eclipse.swt.SWT.CHECK" , new Integer(SWT.CHECK)  //$NON-NLS-1$ //$NON-NLS-2$
			} } ,
			{ "selectionStyle" , TableMessages.getString("TableBeanInfo.StyleBits.SelectionStyle.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				TableMessages.getString("TableBeanInfo.StyleBits.SelectionStyle.Value.Single") , "org.eclipse.swt.SWT.SINGLE" , new Integer(SWT.SINGLE) ,					 //$NON-NLS-1$ //$NON-NLS-2$
				TableMessages.getString("TableBeanInfo.StyleBits.SelectionStyle.Value.Multi") , "org.eclipse.swt.SWT.MULTI" , new Integer(SWT.MULTI)				 //$NON-NLS-1$ //$NON-NLS-2$
			} },
			{ "fullSelection" , TableMessages.getString("TableBeanInfo.StyleBits.FullSelection.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				TableMessages.getString("TableBeanInfo.StyleBits.FullSelection.Value.FullSelection") , "org.eclipse.swt.SWT.FULL_SELECTION" , new Integer(SWT.FULL_SELECTION) 					 //$NON-NLS-1$ //$NON-NLS-2$
			} } ,
			{ "hideSelection" , TableMessages.getString("TableBeanInfo.StyleBits.HideSelection.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				TableMessages.getString("TableBeanInfo.StyleBits.HideSelection.Value.HideSelection") , "org.eclipse.swt.SWT.HIDE_SELECTION" , new Integer(SWT.HIDE_SELECTION) 					 //$NON-NLS-1$ //$NON-NLS-2$
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
			SelectionListenerEventSet.getEventSetDescriptor(getBeanClass())
	};
}

/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
			// columnCount
			super.createPropertyDescriptor(getBeanClass(),"columnCount", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TableMessages.getString("columnCountDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TableMessages.getString("columnCountSD"), //$NON-NLS-1$
			}
			),
			// columns
			super.createPropertyDescriptor(getBeanClass(),"columns", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TableMessages.getString("columnsDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TableMessages.getString("columnsSD"), //$NON-NLS-1$
			}
			),
			// gridLineWidth
			super.createPropertyDescriptor(getBeanClass(),"gridLineWidth", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TableMessages.getString("gridLineWidthDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TableMessages.getString("gridLineWidthSD"), //$NON-NLS-1$
			}
			),
			// headerHeight
			super.createPropertyDescriptor(getBeanClass(),"headerHeight", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TableMessages.getString("headerHeightDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TableMessages.getString("headerHeightSD"), //$NON-NLS-1$
			}
			),
			// headerVisible
			super.createPropertyDescriptor(getBeanClass(),"headerVisible", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TableMessages.getString("headerVisibleDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TableMessages.getString("headerVisibleSD"), //$NON-NLS-1$
			}
			),
			// itemCount
			super.createPropertyDescriptor(getBeanClass(),"itemCount", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TableMessages.getString("itemCountDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TableMessages.getString("itemCountSD"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
			}
			),
			// itemHeight
			super.createPropertyDescriptor(getBeanClass(),"itemHeight", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TableMessages.getString("itemHeightDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TableMessages.getString("itemHeightSD"), //$NON-NLS-1$
			}
			),
			// items
			super.createPropertyDescriptor(getBeanClass(),"items", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TableMessages.getString("itemsDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TableMessages.getString("itemsSD"), //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,
			}
			),
			// linesVisible
			super.createPropertyDescriptor(getBeanClass(),"linesVisible", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TableMessages.getString("linesVisibleDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TableMessages.getString("linesVisibleSD"), //$NON-NLS-1$
			}
			),
			// selection
			super.createPropertyDescriptor(getBeanClass(),"selection", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TableMessages.getString("selectionDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TableMessages.getString("selectionSD"), //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,
			}
			),
			// selectionCount
			super.createPropertyDescriptor(getBeanClass(),"selectionCount", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TableMessages.getString("selectionCountDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TableMessages.getString("selectionCountSD"), //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,
			}
			),
			// selectionIndex
			super.createPropertyDescriptor(getBeanClass(),"selectionIndex", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TableMessages.getString("selectionIndexDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TableMessages.getString("selectionIndexSD"), //$NON-NLS-1$
			}
			),
			// selectionIndices
			super.createPropertyDescriptor(getBeanClass(),"selectionIndices", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TableMessages.getString("selectionIndicesDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TableMessages.getString("selectionIndicesSD"), //$NON-NLS-1$
			}
			),
			// topIndex
			super.createPropertyDescriptor(getBeanClass(),"topIndex", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TableMessages.getString("topIndexDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TableMessages.getString("topIndexSD"), //$NON-NLS-1$
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
	
	replacePropertyDescriptor(newPDs, "layout", null, new Object[] {  //$NON-NLS-1$
		DESIGNTIMEPROPERTY, Boolean.FALSE,
		}
	);

	return newPDs;
}
}

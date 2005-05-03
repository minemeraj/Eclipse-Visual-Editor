package org.eclipse.swt.widgets.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;

import org.eclipse.swt.SWT;


public class ScrolledCompositeBeanInfo extends IvjBeanInfo {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.custom.ScrolledComposite.class;
	}
	
	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
		descriptor.setValue(
			SweetHelper.STYLE_BITS_ID,
		    new Object[] [] {
				{ "horizontalScroll" , ScrolledCompositeMessages.getString("ScrolledCompositeBeanInfo.StyleBits.HorizontalScroll.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				    ScrolledCompositeMessages.getString("ScrolledCompositeBeanInfo.StyleBits.HorizontalScroll.Value.HScroll") , "org.eclipse.swt.SWT.H_SCROLL" , new Integer(SWT.H_SCROLL) , //$NON-NLS-1$ //$NON-NLS-2$
				} },
				{ "verticalScroll" , ScrolledCompositeMessages.getString("ScrolledCompositeBeanInfo.StyleBits.VerticalScroll.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				    ScrolledCompositeMessages.getString("ScrolledCompositeBeanInfo.StyleBits.VerticalScroll.Value.VScroll") , "org.eclipse.swt.SWT.V_SCROLL" , new Integer(SWT.V_SCROLL) , //$NON-NLS-1$ //$NON-NLS-2$
				} }
			}
		);
		return descriptor;
	}	

	/**
	 * Return the property descriptors for this bean.
	 * 
	 * @return java.beans.PropertyDescriptor[]
	 */
	public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor aDescriptorList[] = {
				// alwaysShowScrollBars
				super.createPropertyDescriptor(getBeanClass(),"alwaysShowScrollBars", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, ScrolledCompositeMessages.getString("alwaysShowScrollBarsDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, ScrolledCompositeMessages.getString("alwaysShowScrollBarsSD"), //$NON-NLS-1$
				}
				),
				// expandHorizontal
				super.createPropertyDescriptor(getBeanClass(),"expandHorizontal", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, ScrolledCompositeMessages.getString("expandHorizontalDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, ScrolledCompositeMessages.getString("expandHorizontalSD"), //$NON-NLS-1$
				}
				),
				// expandVertical
				super.createPropertyDescriptor(getBeanClass(),"expandVertical", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, ScrolledCompositeMessages.getString("expandVerticalDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, ScrolledCompositeMessages.getString("expandVerticalSD"), //$NON-NLS-1$
				}
				),
				// minHeight
				super.createPropertyDescriptor(getBeanClass(),"minHeight", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, ScrolledCompositeMessages.getString("minHeightDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, ScrolledCompositeMessages.getString("minHeightSD"), //$NON-NLS-1$
						DESIGNTIMEPROPERTY, Boolean.FALSE, //$NON_NLS-1$
				}
				),
				// minWidth
				super.createPropertyDescriptor(getBeanClass(),"minWidth", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, ScrolledCompositeMessages.getString("minWidthDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, ScrolledCompositeMessages.getString("minWidthSD"), //$NON-NLS-1$
						DESIGNTIMEPROPERTY, Boolean.FALSE, //$NON_NLS-1$
				}
				),
				// minSize
				super.createPropertyDescriptor(getBeanClass(),"minSize", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, ScrolledCompositeMessages.getString("minSizeDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, ScrolledCompositeMessages.getString("minSizeSD"), //$NON-NLS-1$
				}
				),
				// origin
				super.createPropertyDescriptor(getBeanClass(),"origin", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, ScrolledCompositeMessages.getString("originDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, ScrolledCompositeMessages.getString("originSD"), //$NON-NLS-1$
						DESIGNTIMEPROPERTY, Boolean.FALSE, //$NON_NLS-1$
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

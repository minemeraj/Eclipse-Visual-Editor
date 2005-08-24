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
 *  $RCSfile: ControlBeanInfo.java,v $
 *  $Revision: 1.9 $  $Date: 2005-08-24 23:52:54 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;

import org.eclipse.swt.SWT;
 
/**
 * 
 * @since 1.0.0
 */
public class ControlBeanInfo extends IvjBeanInfo {

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Control.class;
	}
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
	    new Object[] [] {
			{ "border" , ControlMessages.getString("ControlBeanInfo.StyleBits.Border.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
			    ControlMessages.getString("ControlBeanInfo.StyleBits.Border.Value.Border") , "org.eclipse.swt.SWT.BORDER" , new Integer(SWT.BORDER)				 //$NON-NLS-1$ //$NON-NLS-2$
			} },
			{ "controlOrientation" , ControlMessages.getString("ControlBeanInfo.StyleBits.ControlOrientation.Name") , Boolean.TRUE, new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
			    ControlMessages.getString("ControlBeanInfo.StyleBits.ControlOrientation.Value.LeftToRight") , "org.eclipse.swt.SWT.LEFT_TO_RIGHT" , new Integer(SWT.LEFT_TO_RIGHT),				 //$NON-NLS-1$ //$NON-NLS-2$
			    ControlMessages.getString("ControlBeanInfo.StyleBits.ControlOrientation.Value.RightToLeft") , "org.eclipse.swt.SWT.RIGHT_TO_LEFT" , new Integer(SWT.RIGHT_TO_LEFT)				 //$NON-NLS-1$ //$NON-NLS-2$
			} } 
		}
	);
	return descriptor;
}

/* (non-Javadoc)
 * @see java.beans.BeanInfo#getEventSetDescriptors()
 */
public EventSetDescriptor[] getEventSetDescriptors() {
	return new EventSetDescriptor[] {
		ControlListenerEventSet.getEventSetDescriptor(getBeanClass()),
		FocusListenerEventSet.getEventSetDescriptor(getBeanClass()),
		HelpListenerEventSet.getEventSetDescriptor(getBeanClass()),
		KeyListenerEventSet.getEventSetDescriptor(getBeanClass()),
		MouseListenerEventSet.getEventSetDescriptor(getBeanClass()),
		MouseMoveListenerEventSet.getEventSetDescriptor(getBeanClass()),
		MouseTrackListenerEventSet.getEventSetDescriptor(getBeanClass()),
		PaintListenerEventSet.getEventSetDescriptor(getBeanClass()),
		TraverseListenerEventSet.getEventSetDescriptor(getBeanClass()),
	};
}


/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
			// accessible
			super.createPropertyDescriptor(getBeanClass(),"accessible", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ControlMessages.getString("accessibleDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ControlMessages.getString("accessibleSD"), //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,
			}
			),
			// background
			super.createPropertyDescriptor(getBeanClass(),"background", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ControlMessages.getString("backgroundDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ControlMessages.getString("backgroundSD"), //$NON-NLS-1$
			}
			),
			// borderWidth
			super.createPropertyDescriptor(getBeanClass(),"borderWidth", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ControlMessages.getString("borderWidthDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ControlMessages.getString("borderWidthSD"), //$NON-NLS-1$
			}
			),
			// bounds
			super.createPropertyDescriptor(getBeanClass(),"bounds", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ControlMessages.getString("boundsDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ControlMessages.getString("boundsSD"), //$NON-NLS-1$
			}
			),
			// enabled
			super.createPropertyDescriptor(getBeanClass(),"enabled", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ControlMessages.getString("enabledDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ControlMessages.getString("enabledSD"), //$NON-NLS-1$
			}
			),
			// focusControl
			super.createPropertyDescriptor(getBeanClass(),"focusControl", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ControlMessages.getString("focusControlDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ControlMessages.getString("focusControlSD"), //$NON-NLS-1$
			}
			),
			// font
			super.createPropertyDescriptor(getBeanClass(),"font", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ControlMessages.getString("fontDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ControlMessages.getString("fontSD"), //$NON-NLS-1$
			}
			),
			// foreground
			super.createPropertyDescriptor(getBeanClass(),"foreground", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ControlMessages.getString("foregroundDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ControlMessages.getString("foregroundSD"), //$NON-NLS-1$
			}
			),
			// layoutData
			super.createPropertyDescriptor(getBeanClass(),"layoutData", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ControlMessages.getString("layoutDataDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ControlMessages.getString("layoutDataSD"), //$NON-NLS-1$
			}
			),
			// location
			super.createPropertyDescriptor(getBeanClass(),"location", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ControlMessages.getString("locationDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ControlMessages.getString("locationSD"), //$NON-NLS-1$
			}
			),
			// menu
			super.createPropertyDescriptor(getBeanClass(),"menu", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ControlMessages.getString("menuDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ControlMessages.getString("menuSD"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
			}
			),
			// monitor
			super.createPropertyDescriptor(getBeanClass(),"monitor", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ControlMessages.getString("monitorDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ControlMessages.getString("monitorSD"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
			}
			),
			// parent
			super.createPropertyDescriptor(getBeanClass(),"parent", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ControlMessages.getString("parentDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ControlMessages.getString("parentSD"), //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE
			}
			),
			// reparentable
			super.createPropertyDescriptor(getBeanClass(),"reparentable", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ControlMessages.getString("reparentableDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ControlMessages.getString("reparentableSD"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
			}
			),
			// shell
			super.createPropertyDescriptor(getBeanClass(),"shell", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ControlMessages.getString("shellDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ControlMessages.getString("shellSD"), //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE
			}
			),
			// size
			super.createPropertyDescriptor(getBeanClass(),"size", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ControlMessages.getString("sizeDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ControlMessages.getString("sizeSD"), //$NON-NLS-1$
			}
			),
			// toolTipText
			super.createPropertyDescriptor(getBeanClass(),"toolTipText", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ControlMessages.getString("toolTipTextDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ControlMessages.getString("toolTipTextSD"), //$NON-NLS-1$
			}
			),
			// visible
			super.createPropertyDescriptor(getBeanClass(),"visible", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ControlMessages.getString("visibleDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ControlMessages.getString("visibleSD"), //$NON-NLS-1$
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

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CanvasBeanInfo.java,v $
 *  $Revision: 1.5 $  $Date: 2004-06-25 21:26:06 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;

import org.eclipse.swt.SWT;
 
/**
 * 
 * @since 1.0.0
 */
public class CanvasBeanInfo extends IvjBeanInfo {

/* (non-Javadoc)
 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
 */
public Class getBeanClass() {
	return org.eclipse.swt.widgets.Canvas.class;
}
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
	    new Object[] [] {			
			{ "noBackground" , CanvasMessages.getString("CanvasBeanInfo.StyleBits.NoBackground.Name") , Boolean.TRUE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
			    CanvasMessages.getString("CanvasBeanInfo.StyleBits.NoBackground.Value.NoBackground") , "org.eclipse.swt.SWT.NO_BACKGROUND" , new Integer(SWT.NO_BACKGROUND)				 //$NON-NLS-1$ //$NON-NLS-2$
			} } ,
			{ "noFocus" , CanvasMessages.getString("CanvasBeanInfo.StyleBits.NoFocus.Name") , Boolean.TRUE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				CanvasMessages.getString("CanvasBeanInfo.StyleBits.NoFocus.Value.NoFocus") , "org.eclipse.swt.SWT.NO_FOCUS" , new Integer(SWT.NO_FOCUS)				 //$NON-NLS-1$ //$NON-NLS-2$
			} } ,
			{ "noMergePaints" , CanvasMessages.getString("CanvasBeanInfo.StyleBits.NoMergePaints.Name"), Boolean.TRUE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				CanvasMessages.getString("CanvasBeanInfo.StyleBits.NoMergePaints.Value.NoMergePaints") , "org.eclipse.swt.SWT.NO_MERGE_PAINTS" , new Integer(SWT.NO_MERGE_PAINTS)				 //$NON-NLS-1$ //$NON-NLS-2$
			} } ,
			{ "noRedrawResize" , CanvasMessages.getString("CanvasBeanInfo.StyleBits.NoRedrawResize.Name") , Boolean.TRUE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				CanvasMessages.getString("CanvasBeanInfo.StyleBits.NoRedrawResize.Value.NoRedrawResize") , "org.eclipse.swt.SWT.NO_REDRAW_RESIZE" , new Integer(SWT.NO_REDRAW_RESIZE)				 //$NON-NLS-1$ //$NON-NLS-2$
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
			// caret
			super.createPropertyDescriptor(getBeanClass(),"caret", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CanvasMessages.getString("caretDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CanvasMessages.getString("caretSD"), //$NON-NLS-1$
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

}

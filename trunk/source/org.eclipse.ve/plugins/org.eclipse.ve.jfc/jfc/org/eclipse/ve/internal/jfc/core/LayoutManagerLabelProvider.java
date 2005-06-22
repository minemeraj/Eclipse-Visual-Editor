/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: LayoutManagerLabelProvider.java,v $
 *  $Revision: 1.6 $  $Date: 2005-06-22 14:53:04 $ 
 */

import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.propertysheet.INeedData;
/**
 * Renderer for the layout property for a Container
 */
public class LayoutManagerLabelProvider extends LabelProvider implements INeedData {

	protected EditDomain editDomain;

	public String getText(Object element) {
		IBeanProxy beanProxy = BeanProxyUtilities.getBeanProxy((IJavaInstance) element, JavaEditDomainHelper.getResourceSet(editDomain));
		if (beanProxy != null) {
			String qualifiedName = getQualifiedName(element);
			return LayoutManagerCellEditor.getDisplayName(editDomain, qualifiedName);
		} else {
			return JFCMessages.Layout_NullLayout; 
		}
	}

	public static String getQualifiedName(Object element) {
		String qualifiedName = ""; //$NON-NLS-1$
		IBeanProxy beanProxy = BeanProxyUtilities.getBeanProxy((IJavaInstance) element);
		if (beanProxy != null) {
			qualifiedName = beanProxy.getTypeProxy().getTypeName();
			// With a BoxLayout, there are two possible display names...
			// based on the axis. Need to get the axis to determine the name (we use a fake name here since it is really the
			// same class type but with a different setting within it).
			if (qualifiedName.equals("javax.swing.BoxLayout")) { //$NON-NLS-1$
				if (BeanAwtUtilities.getBoxLayoutAxis(beanProxy))
					qualifiedName = "javax.swing.BoxLayoutX_Axis"; //$NON-NLS-1$
				else
					qualifiedName = "javax.swing.BoxLayoutY_Axis"; //$NON-NLS-1$
			}
		}
		return qualifiedName;
	}
	/**
	 * @see INeedData#setData(Object)
	 */
	public void setData(Object data) {
		editDomain = (EditDomain) data;
	}

}

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
package org.eclipse.ve.internal.swt;
/*
 *  $RCSfile: LayoutLabelProvider.java,v $
 *  $Revision: 1.4 $  $Date: 2005-02-15 23:51:49 $ 
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
public class LayoutLabelProvider extends LabelProvider implements INeedData {

	protected EditDomain editDomain;

	public String getText(Object element) {
		IBeanProxy beanProxy = BeanProxyUtilities.getBeanProxy((IJavaInstance) element, JavaEditDomainHelper.getResourceSet(editDomain));
		if (beanProxy != null) {
			String qualifiedName = getQualifiedName(element);
			return LayoutCellEditor.getDisplayName(editDomain, qualifiedName);
		} else {
			return "null";
		}
	}

	public static String getQualifiedName(Object element) {

		IBeanProxy beanProxy = BeanProxyUtilities.getBeanProxy((IJavaInstance) element);
		if (beanProxy != null) {
			return beanProxy.getTypeProxy().getTypeName();
		}
		return ""; // $NON-NLS-1$
	}
	/**
	 * @see INeedData#setData(Object)
	 */
	public void setData(Object data) {
		editDomain = (EditDomain) data;
	}

}

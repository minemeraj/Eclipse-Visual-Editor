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
package org.eclipse.ve.internal.java.core;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.ICharacterBeanProxy;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.propertysheet.CharLabelProvider;
import org.eclipse.ve.internal.propertysheet.INeedData;

//see org.eclipse.ve.java.core\overrides\java\lang\Character.override
//see org.eclipse.ve.java.core\overrides\java\lang\Object.override
/**
 * Label provider for Character/char values.
 */
public class CharJavaLabelProvider extends CharLabelProvider implements INeedData {

	protected EditDomain editDomain;

	/**
	 * Returns the String value of the expected IJavaInstance 'element'
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object element) {
		if (element instanceof IJavaInstance) {
			IBeanProxy proxy = BeanProxyUtilities.getBeanProxy((IJavaInstance) element, JavaEditDomainHelper.getResourceSet(editDomain));
			if (proxy == null)
				return ""; // It shouldn't be null. //$NON-NLS-1$
			else if (proxy instanceof ICharacterBeanProxy)
				return super.getText(((ICharacterBeanProxy) proxy).characterValue());
			else
				return proxy.toBeanString();
		} else {
			return super.getText(element);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.propertysheet.INeedData#setData(java.lang.Object)
	 */
	public void setData(Object data) {
		if (data instanceof EditDomain) {
			editDomain = (EditDomain) data;
		}
	}

}

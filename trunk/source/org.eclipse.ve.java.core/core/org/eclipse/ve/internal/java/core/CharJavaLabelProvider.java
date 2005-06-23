/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
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

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.propertysheet.CharLabelProvider;

// see org.eclipse.ve.java.core\overrides\java\lang\Character.override
// see org.eclipse.ve.java.core\overrides\java\lang\Object.override
/**
 * Label provider for Character/char values.
 */
public class CharJavaLabelProvider extends CharLabelProvider {

	protected EditDomain editDomain;

	/**
	 * Returns the String value of the expected IJavaInstance 'element'
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object element) {
		if (element instanceof IJavaInstance) {
			IBeanProxy proxy = BeanProxyUtilities.getBeanProxy((IJavaInstance) element);
			if (proxy == null)
				return ""; // It shouldn't be null. //$NON-NLS-1$
			return BeanUtilities.getEscapedString(proxy.toBeanString());
		} else
			return super.getText(element);
	}

}

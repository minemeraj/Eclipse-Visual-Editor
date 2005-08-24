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
/*
 *  $RCSfile: NumberJavaLabelProvider.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:46 $ 
 */

import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.ve.internal.propertysheet.NumberLabelProvider;
import org.eclipse.jem.internal.proxy.core.INumberBeanProxy;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
/**
 * This is a generic cell renderer that can format numbers correctly
 * It goes to the bean proxy to get the number value, however because it casts to the
 * the specifc type it doesn't actually trip to the target VM as the value of the number
 * is stored in the IDE's implementation of IBeanProxy
 */
public class NumberJavaLabelProvider extends NumberLabelProvider implements INeedData {
	
	protected EditDomain editDomain;
	
public String getText(Object element){
	
	if (element instanceof IJavaInstance) {
		IBeanProxy proxy = BeanProxyUtilities.getBeanProxy((IJavaInstance)element, JavaEditDomainHelper.getResourceSet(editDomain));
		if (proxy == null)
			return "";	// It shouldn't be null. //$NON-NLS-1$
		else if (proxy instanceof INumberBeanProxy)
			return super.getText(((INumberBeanProxy) proxy).numberValue());
		else
			return proxy.toBeanString();
	} else
		return super.getText(element);
}
	/**
	 * @see INeedData#setData(Object)
	 */
	public void setData(Object data) {
		editDomain = (EditDomain) data;
	}

}

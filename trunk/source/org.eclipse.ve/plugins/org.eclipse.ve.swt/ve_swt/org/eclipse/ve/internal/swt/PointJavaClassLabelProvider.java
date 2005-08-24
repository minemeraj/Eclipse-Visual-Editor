/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IPointBeanProxy;
/**
 * Render an SWT Point in a usable form, e.g. 100,100
 */
public class PointJavaClassLabelProvider extends LabelProvider {
	
	private static final String NULL_POINT_STRING = "0,0";	 //$NON-NLS-1$
	
public String getText(Object element){	
	if (element instanceof IJavaInstance)
		return toString((IJavaInstance) element);
	else if (element == null)
		return NULL_POINT_STRING;
	else
		return super.getText(element);
}

/**
 * Static helper that is used by the editor as well
 */
public static String toString(IJavaInstance aPoint){
	IBeanProxy beanProxy = BeanProxyUtilities.getBeanProxy(aPoint);
	if (beanProxy instanceof IPointBeanProxy) {
		IPointBeanProxy pointBeanProxy = (IPointBeanProxy)beanProxy;
		// Point has two public fields for x and y
		StringBuffer sb = new StringBuffer(50);
		sb.append(String.valueOf(pointBeanProxy.getX()));
		sb.append(',');
		sb.append(String.valueOf(pointBeanProxy.getY()));
		return sb.toString();
	} 
	return beanProxy != null ? beanProxy.toBeanString() : NULL_POINT_STRING;
}
}

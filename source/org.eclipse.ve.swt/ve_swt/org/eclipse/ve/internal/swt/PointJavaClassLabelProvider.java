package org.eclipse.ve.internal.swt;
/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2002 - All Rights Reserved. 
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 */

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.jem.internal.proxy.awt.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
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
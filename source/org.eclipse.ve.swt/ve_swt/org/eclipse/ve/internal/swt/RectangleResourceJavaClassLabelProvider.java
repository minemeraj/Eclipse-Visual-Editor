package org.eclipse.ve.internal.swt;
/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2002 - All Rights Reserved. 
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 */

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.ve.internal.java.core.*;

import org.eclipse.jem.internal.proxy.awt.IRectangleBeanProxy;
import org.eclipse.jem.internal.proxy.core.*;

/**
 * Render a Rectangle in a usable form, e.g. 10,10,100,100
 */
public class RectangleResourceJavaClassLabelProvider extends LabelProvider {
	
	private static final String NULL_RECTANGLE_STRING = "0,0,0,0";	 //$NON-NLS-1$
	
public String getText(Object element){	
	if (element instanceof IJavaInstance)
		return toString((IJavaInstance) element);
	else if (element == null)
		return NULL_RECTANGLE_STRING;
	else
		return super.getText(element);
}

/**
 * Static helper that is used by the editor as well
 */
public static String toString(IJavaInstance aRectangle){
	IBeanProxy beanProxy = BeanProxyUtilities.getBeanProxy(aRectangle);
	if (beanProxy instanceof IRectangleBeanProxy) {
		IRectangleBeanProxy rectangleProxy = (IRectangleBeanProxy) beanProxy;
		StringBuffer sb = new StringBuffer(50);
		sb.append(String.valueOf(rectangleProxy.getX()));
		sb.append(',');
		sb.append(String.valueOf(rectangleProxy.getY()));
		sb.append(',');				
		sb.append(String.valueOf(rectangleProxy.getWidth()));
		sb.append(',');
		sb.append(String.valueOf(rectangleProxy.getHeight()));
		return sb.toString();
	} else
		return beanProxy != null ? beanProxy.toBeanString() : NULL_RECTANGLE_STRING;
}
}
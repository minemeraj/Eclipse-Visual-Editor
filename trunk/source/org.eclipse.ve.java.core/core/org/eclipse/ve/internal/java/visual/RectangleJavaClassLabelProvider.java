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
package org.eclipse.ve.internal.java.visual;
/*
 *  $RCSfile: RectangleJavaClassLabelProvider.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:30:47 $ 
 */

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IRectangleBeanProxy;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
/**
 * Render a Rectangle in a usable form, e.g. 10,10,100,100
 */
public class RectangleJavaClassLabelProvider extends LabelProvider {
	
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

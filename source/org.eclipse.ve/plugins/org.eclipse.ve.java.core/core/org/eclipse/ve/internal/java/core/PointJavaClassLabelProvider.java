package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: PointJavaClassLabelProvider.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */


import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.awt.IPointBeanProxy;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
/**
 * Render a dimension in a usable form, e.g. 100,100
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
public static String toString(IJavaInstance aDimension){
	IBeanProxy beanProxy = BeanProxyUtilities.getBeanProxy(aDimension);
	if (beanProxy instanceof IPointBeanProxy) {
		IPointBeanProxy pointProxy = (IPointBeanProxy) beanProxy;
		StringBuffer sb = new StringBuffer(50);
		sb.append(String.valueOf(pointProxy.getX()));
		sb.append(',');
		sb.append(String.valueOf(pointProxy.getY()));
		return sb.toString();
	} else
		return beanProxy != null ? beanProxy.toBeanString() : NULL_POINT_STRING;
}
}



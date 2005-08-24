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
 *  $RCSfile: DimensionJavaClassLabelProvider.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:30:47 $ 
 */

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IDimensionBeanProxy;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
/**
 * Render a dimension in a usable form, e.g. 100,100
 */
public class DimensionJavaClassLabelProvider extends LabelProvider {
	
	private static final String NULL_DIMENSION_STRING = "0,0";	 //$NON-NLS-1$
	
public String getText(Object element){	
	if (element instanceof IJavaInstance)
		return toString((IJavaInstance) element);
	else if (element == null)
		return NULL_DIMENSION_STRING;
	else
		return super.getText(element);
}

/**
 * Static helper that is used by the editor as well
 */
public static String toString(IJavaInstance aDimension){
	IBeanProxy beanProxy = BeanProxyUtilities.getBeanProxy(aDimension);
	if (beanProxy instanceof IDimensionBeanProxy) {
		IDimensionBeanProxy dimensionProxy = (IDimensionBeanProxy) beanProxy;
		StringBuffer sb = new StringBuffer(50);
		sb.append(String.valueOf(dimensionProxy.getWidth()));
		sb.append(',');
		sb.append(String.valueOf(dimensionProxy.getHeight()));
		return sb.toString();
	} else
		return beanProxy != null ? beanProxy.toBeanString() : NULL_DIMENSION_STRING;
}
}

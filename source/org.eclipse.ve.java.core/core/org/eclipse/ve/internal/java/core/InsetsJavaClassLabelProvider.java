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
 *  $RCSfile: InsetsJavaClassLabelProvider.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:46 $ 
 */

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;
	

public class InsetsJavaClassLabelProvider extends LabelProvider{
	private static final String NULL_INSETS_STRING = "0,0,0,0";	 //$NON-NLS-1$
	
public String getText(Object element){	
	if (element instanceof IJavaInstance)
		return toString((IJavaInstance) element);
	else if (element == null)
		return NULL_INSETS_STRING;
	else
		return super.getText(element);
}

/**
 * Static helper that is used by the editor as well
 */
public static String toString(IJavaInstance anInsets){
	IBeanProxy beanProxy = BeanProxyUtilities.getBeanProxy(anInsets);
	if (beanProxy != null) {
		StringBuffer sb = new StringBuffer(50);
		IIntegerBeanProxy intProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanField("top", beanProxy); //$NON-NLS-1$
		sb.append(intProxy.intValue());
		sb.append(',');
		intProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanField("left", beanProxy); //$NON-NLS-1$
		sb.append(intProxy.intValue());
		sb.append(',');
		intProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanField("bottom", beanProxy); //$NON-NLS-1$
		sb.append(intProxy.intValue());
		sb.append(',');
		intProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanField("right", beanProxy); //$NON-NLS-1$
		sb.append(intProxy.intValue());
		return sb.toString();
	} else
		return NULL_INSETS_STRING;
}
}

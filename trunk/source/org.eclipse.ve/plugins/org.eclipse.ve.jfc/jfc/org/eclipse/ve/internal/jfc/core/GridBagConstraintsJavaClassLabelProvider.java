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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: GridBagConstraintsJavaClassLabelProvider.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:10 $ 
 */

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.JavaMessages;
	
/**
 * Label provider for displaying GridBagConstaints
 */
public class GridBagConstraintsJavaClassLabelProvider extends LabelProvider {
	private static final String NULL_GRIDBAGCONSTRAINTS_STRING = JavaMessages.Labelprovider_X+":,"+JavaMessages.Labelprovider_Y+":,"+JavaMessages.Labelprovider_Width+":,"+JavaMessages.Labelprovider_Height+":";	 
	
public String getText(Object element){	
	if (element instanceof IJavaInstance)
		return toString((IJavaInstance) element);
	else if (element == null)
		return NULL_GRIDBAGCONSTRAINTS_STRING;
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
		sb.append(JavaMessages.Labelprovider_X+":"); 
		IIntegerBeanProxy intProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanField("gridx", beanProxy); //$NON-NLS-1$
		sb.append(intProxy.intValue());
		sb.append(", "+JavaMessages.Labelprovider_Y+":"); 
		intProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanField("gridy", beanProxy); //$NON-NLS-1$
		sb.append(intProxy.intValue());
		sb.append(", "+JavaMessages.Labelprovider_Width+":"); 
		intProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanField("gridwidth", beanProxy); //$NON-NLS-1$
		sb.append(intProxy.intValue());
		sb.append(", "+JavaMessages.Labelprovider_Height+":"); 
		intProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanField("gridheight", beanProxy); //$NON-NLS-1$
		sb.append(intProxy.intValue());
		return sb.toString();
	} else
		return NULL_GRIDBAGCONSTRAINTS_STRING;
}
}

/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $Revision: 1.3 $  $Date: 2005-02-15 23:42:05 $ 
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
	private static final String NULL_GRIDBAGCONSTRAINTS_STRING = JavaMessages.getString("Labelprovider.X")+":,"+JavaMessages.getString("Labelprovider.Y")+":,"+JavaMessages.getString("Labelprovider.Width")+":,"+JavaMessages.getString("Labelprovider.Height")+":";	 //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
	
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
		sb.append(JavaMessages.getString("Labelprovider.X")+":"); //$NON-NLS-1$ //$NON-NLS-2$
		IIntegerBeanProxy intProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanField("gridx", beanProxy); //$NON-NLS-1$
		sb.append(intProxy.intValue());
		sb.append(", "+JavaMessages.getString("Labelprovider.Y")+":"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		intProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanField("gridy", beanProxy); //$NON-NLS-1$
		sb.append(intProxy.intValue());
		sb.append(", "+JavaMessages.getString("Labelprovider.Width")+":"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		intProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanField("gridwidth", beanProxy); //$NON-NLS-1$
		sb.append(intProxy.intValue());
		sb.append(", "+JavaMessages.getString("Labelprovider.Height")+":"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		intProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanField("gridheight", beanProxy); //$NON-NLS-1$
		sb.append(intProxy.intValue());
		return sb.toString();
	} else
		return NULL_GRIDBAGCONSTRAINTS_STRING;
}
}

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
/*
 *  $RCSfile: FontJavaClassLabelProvider.java,v $
 *  $Revision: 1.6 $  $Date: 2005-03-29 16:17:15 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;

public class FontJavaClassLabelProvider extends LabelProvider {
	
public String getText(Object element){	
	if (element instanceof IJavaInstance){
		return getText((IJavaInstance)element);
	} else {
		return "";
	}
}
public static String getText(IJavaInstance element){
	try{
		// Get the description of the Font.  This comes from getting the FontData
		// and its name, style and size and formatting these.  To save lots of 
		// target VM traffic a single static helper method exists on the utility class
		// org.eclipse.ve.internal.swt.targetvm.Environment.getFontLabel(Font aFont)
		IBeanProxy fontBeanProxy = BeanProxyUtilities.getBeanProxy((IJavaInstance)element);
		if (fontBeanProxy == null) return "";
		IBeanTypeProxy environmentBeanTypeProxy = fontBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.swt.targetvm.Environment");
		IMethodProxy getFontLabelMethodProxy = environmentBeanTypeProxy.getMethodProxy("getFontLabel", new IBeanTypeProxy[] {fontBeanProxy.getTypeProxy()});
		IStringBeanProxy fontLabelBeanProxy = (IStringBeanProxy) getFontLabelMethodProxy.invoke(environmentBeanTypeProxy,fontBeanProxy);
		return fontLabelBeanProxy.stringValue();			
	} catch (ThrowableProxy exc){
	}
	return "";	
}
}

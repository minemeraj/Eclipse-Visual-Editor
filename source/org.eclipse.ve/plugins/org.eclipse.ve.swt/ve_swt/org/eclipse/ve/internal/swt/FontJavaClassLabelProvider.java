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
 *  $RCSfile: FontJavaClassLabelProvider.java,v $
 *  $Revision: 1.1 $  $Date: 2003-12-15 10:28:01 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;

public class FontJavaClassLabelProvider extends LabelProvider {
	
public String getText(Object element){	
	if (element instanceof IJavaInstance){
		try{
			// Get the description of the Font.  This comes from getting the FontData
			// and its name, style and size and formatting these.  To save lots of 
			// target VM traffic a single static helper method exists on the utility class
			// com.ibm.etools.jbcf.swt.targetvm.Environment.getFontLabel(Font aFont)
			IBeanProxy fontBeanProxy = BeanProxyUtilities.getBeanProxy((IJavaInstance)element);
			IBeanTypeProxy environmentBeanTypeProxy = fontBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("com.ibm.etools.jbcf.swt.targetvm.Environment");
			IMethodProxy getFontLabelMethodProxy = environmentBeanTypeProxy.getMethodProxy("getFontLabel", new IBeanTypeProxy[] {fontBeanProxy.getTypeProxy()});
			IStringBeanProxy fontLabelBeanProxy = (IStringBeanProxy) getFontLabelMethodProxy.invoke(environmentBeanTypeProxy,fontBeanProxy);
			return fontLabelBeanProxy.stringValue();			
		} catch (ThrowableProxy exc){
		}
	}
	return "";	
}
}

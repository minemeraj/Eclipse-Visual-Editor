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
/*
 *  $RCSfile: FontJavaClassLabelProvider.java,v $
 *  $Revision: 1.12 $  $Date: 2005-10-03 19:20:48 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;

public class FontJavaClassLabelProvider extends LabelProvider {

	public String getText(Object element) {
		if (element instanceof IJavaInstance) {
			return getText((IJavaInstance) element);
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	public static String getText(IJavaInstance element) {
		/*
		 * For JFace fonts, show the label based on the ParseTreeAllocation information
		 */
		if (element != null && element.isParseTreeAllocation()) {
			ParseTreeAllocation ptAlloc = (ParseTreeAllocation) element.getAllocation();
			PTExpression exp = ptAlloc.getExpression();
			if (exp instanceof PTMethodInvocation && ((PTMethodInvocation) exp).getReceiver() instanceof PTMethodInvocation
					&& ((PTMethodInvocation) ((PTMethodInvocation) exp).getReceiver()).getName().equals("getFontRegistry")) { //$NON-NLS-1$
				PTExpression arg = (PTExpression) ((PTMethodInvocation) exp).getArguments().get(0);
				StringBuffer fontLabelBuffer = new StringBuffer();
				fontLabelBuffer.append("JFace FontRegistry, "); //$NON-NLS-1$
				fontLabelBuffer.append(((PTMethodInvocation) exp).getName());
				fontLabelBuffer.append(','); //$NON-NLS-1$
				fontLabelBuffer.append(' '); //$NON-NLS-1$
				if (arg instanceof PTFieldAccess) {
					fontLabelBuffer.append(((PTFieldAccess) arg).getField());
				}
				return fontLabelBuffer.toString();
			}
		}
		try {
			// Get the description of the Font. This comes from getting the FontData
			// and its name, style and size and formatting these. To save lots of
			// target VM traffic a single static helper method exists on the utility class
			// org.eclipse.ve.internal.swt.targetvm.Environment.getFontLabel(Font aFont)
			IBeanProxy fontBeanProxy = BeanProxyUtilities.getBeanProxy(element);
			if (fontBeanProxy == null) return ""; //$NON-NLS-1$ 
			IBeanTypeProxy environmentBeanTypeProxy = fontBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(
					"org.eclipse.ve.internal.swt.targetvm.Environment"); //$NON-NLS-1$
			IMethodProxy getFontLabelMethodProxy = environmentBeanTypeProxy.getMethodProxy("getFontLabel", new IBeanTypeProxy[] { fontBeanProxy.getTypeProxy()}); //$NON-NLS-1$
			IStringBeanProxy fontLabelBeanProxy = (IStringBeanProxy) getFontLabelMethodProxy.invoke(environmentBeanTypeProxy, fontBeanProxy);
			return fontLabelBeanProxy.stringValue();
		} catch (ThrowableProxy exc) {
		}
		return ""; //$NON-NLS-1$
	}
}

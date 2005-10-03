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
package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;

/**
 * Provides label for Color elements in SWT Properties view. See Color.override
 */
public class ColorJavaClassLabelProvider extends LabelProvider {

	public String getText(Object element) {
		if (element instanceof IJavaInstance) {
			return getText((IJavaInstance) element);
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	public static String getText(IJavaInstance element) {
		if (element != null && element.isParseTreeAllocation()) {
			ParseTreeAllocation ptAlloc = (ParseTreeAllocation) element.getAllocation();
			PTExpression exp = ptAlloc.getExpression();
			StringBuffer colorLabelBuffer = new StringBuffer(""); //$NON-NLS-1$
			if (exp instanceof PTMethodInvocation && ((PTMethodInvocation) exp).getReceiver() instanceof PTMethodInvocation) {
				String methodName = ((PTMethodInvocation) ((PTMethodInvocation) exp).getReceiver()).getName();
				if 	(methodName.equals("getColorRegistry")) { //$NON-NLS-1$
					colorLabelBuffer.append("JFace ColorRegistry, get, "); //$NON-NLS-1$
				} else if (methodName.equals("getDefault")) { //$NON-NLS-1$
					colorLabelBuffer.append("SWT, SystemColor, "); //$NON-NLS-1$
				}
				PTExpression arg = (PTExpression) ((PTMethodInvocation) exp).getArguments().get(0);
				if (arg instanceof PTFieldAccess) {
					colorLabelBuffer.append(((PTFieldAccess) arg).getField());
				}
				return colorLabelBuffer.toString();
			} else if (exp instanceof PTClassInstanceCreation && ((PTClassInstanceCreation)exp).getType().equals("org.eclipse.swt.graphics.Color")) { //$NON-NLS-1$
				colorLabelBuffer.append("Color{"); //$NON-NLS-1$
				List args = ((PTClassInstanceCreation)exp).getArguments();
				colorLabelBuffer.append(((PTNumberLiteral) args.get(1)).getToken());
				colorLabelBuffer.append(','); //$NON-NLS-1$
				colorLabelBuffer.append(((PTNumberLiteral) args.get(2)).getToken());
				colorLabelBuffer.append(','); //$NON-NLS-1$
				colorLabelBuffer.append(((PTNumberLiteral) args.get(3)).getToken());
				colorLabelBuffer.append('}'); //$NON-NLS-1$
				return colorLabelBuffer.toString();
			}
		} else
			return BeanProxyUtilities.getBeanProxy(element).toBeanString();
		
		return ""; //$NON-NLS-1$
	}

}

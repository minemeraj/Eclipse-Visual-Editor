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
package org.eclipse.ve.internal.swt;

import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;

/**
 * Provides label for Color elements in SWT Properties view. See Color.override
 */
public class ColorJavaClassLabelProvider extends LabelProvider {

	public String getText(Object element) {
		if (element instanceof IJavaInstance) {
			return BeanProxyUtilities.getBeanProxy((IJavaInstance) element).toBeanString();
		} else {
			return "";
		}
	}

}

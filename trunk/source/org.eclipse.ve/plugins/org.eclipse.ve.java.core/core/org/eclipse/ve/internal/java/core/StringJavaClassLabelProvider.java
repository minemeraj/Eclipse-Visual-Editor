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
 *  $RCSfile: StringJavaClassLabelProvider.java,v $
 *  $Revision: 1.4 $  $Date: 2005-06-23 16:08:44 $ 
 */

package org.eclipse.ve.internal.java.core;


import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;

public class StringJavaClassLabelProvider extends LabelProvider {

	public String getText(Object element) {

		if (element instanceof IJavaInstance) {
			IBeanProxy proxy = BeanProxyUtilities.getBeanProxy((IJavaInstance) element);
			if (proxy == null)
				return ""; // It shouldn't be null. //$NON-NLS-1$
			return BeanUtilities.getEscapedString(proxy.toBeanString());
		} else
			return super.getText(element);
	}
}
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
 * $RCSfile: ImageLabelProvider.java,v $ $Revision: 1.6 $ $Date: 2005-02-15 23:42:05 $
 */

import java.util.StringTokenizer;

import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

public class ImageLabelProvider extends LabelProvider {

	public String getText(Object element) {

		if (element instanceof IJavaObjectInstance) {
			IJavaObjectInstance imageIcon = (IJavaObjectInstance) element;
			String initStr = ImageCellEditor.getPathFromInitializationAllocation(imageIcon.getAllocation());
			int ind_first = initStr.indexOf("\""); //$NON-NLS-1$
			int ind_last = initStr.lastIndexOf("\""); //$NON-NLS-1$
			if ((ind_first != -1) && (ind_last != -1)) {
				initStr = initStr.substring(ind_first + 1, ind_last);
				StringTokenizer tokenizer = new StringTokenizer(initStr, "\"\\/"); //$NON-NLS-1$
				String fname = ""; //$NON-NLS-1$
				while (tokenizer.hasMoreTokens()) {
					fname = tokenizer.nextToken();
				}
				return fname;
			}
		}
		return ""; //$NON-NLS-1$
	}
}

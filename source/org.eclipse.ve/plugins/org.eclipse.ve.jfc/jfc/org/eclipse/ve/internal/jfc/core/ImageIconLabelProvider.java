package org.eclipse.ve.internal.jfc.core;
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
 *  $RCSfile: ImageIconLabelProvider.java,v $
 *  $Revision: 1.2 $  $Date: 2004-02-06 20:19:47 $ 
 */

import java.util.StringTokenizer;

import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

public class ImageIconLabelProvider extends LabelProvider {

	public String getText(Object element) {

		if (element instanceof IJavaObjectInstance) {
			IJavaObjectInstance imageIcon = (IJavaObjectInstance) element;
			// TODO Need to fix this compile error... for now just return null
			return null;
//			return getPathFromInitializationString(imageIcon.getInitializationString());
		}
		return ""; //$NON-NLS-1$
	}

	public static String fixBackSlash(String pathname) {
		if (pathname != null && !pathname.equals("")) { //$NON-NLS-1$
			pathname = pathname.replace('\\', '/');
		}
		return pathname;

	}
	
	/**
	 * Parse through the initialization string and strip out the path information.
	 */
	protected String getPathFromInitializationString(String initStr) {
		if (initStr == null || initStr.equals("") || initStr.indexOf('"') == -1) //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		StringTokenizer stk = new StringTokenizer(initStr, "\""); //$NON-NLS-1$
		stk.nextToken();
		String pathname = fixBackSlash(stk.nextToken().trim());
		return pathname;
	}
}
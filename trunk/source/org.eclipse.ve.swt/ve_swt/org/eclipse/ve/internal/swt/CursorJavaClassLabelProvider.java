/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CursorJavaClassLabelProvider.java,v $
 *  $Revision: 1.1 $  $Date: 2004-06-18 14:59:10 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.InitStringAllocation;
import org.eclipse.jem.internal.instantiation.ParseTreeAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.impl.NaiveExpressionFlattener;

/**
 * 
 * @since 1.0.0
 */
public class CursorJavaClassLabelProvider extends LabelProvider {

	public String getText(Object element) {
		if (element instanceof IJavaInstance) {
			return getText((IJavaInstance) element);
		} else {
			return "";
		}
	}

	public static String getText(IJavaInstance element) {
		String initStr = "";
		if (element.getAllocation() instanceof ParseTreeAllocation) {
			PTExpression exp = ((ParseTreeAllocation) ((IJavaInstance) element).getAllocation()).getExpression();
			if (exp instanceof PTClassInstanceCreation) {
				NaiveExpressionFlattener flattener = new NaiveExpressionFlattener();
				exp.accept(flattener);
				initStr = flattener.getResult();
			}
		} else if (element.getAllocation() instanceof InitStringAllocation) {
			initStr = ((InitStringAllocation) ((IJavaInstance) element).getAllocation()).getInitString();
		}
		int begin_cursor = initStr.indexOf("SWT.CURSOR_");
		if (begin_cursor != -1) {
			int after_cursor = initStr.indexOf(")", begin_cursor);
			if (after_cursor != -1) {
				String cursorStr = initStr.substring(begin_cursor + 11, after_cursor);
				String[] cursors = CursorPropertyEditor.cursorConstants;
				for (int i = 0; i < cursors.length; i++) {
					if (cursors[i].equals(cursorStr))
						return CursorPropertyEditor.cursorNames[i];
				}
			}
		}
		return initStr;
	}
}
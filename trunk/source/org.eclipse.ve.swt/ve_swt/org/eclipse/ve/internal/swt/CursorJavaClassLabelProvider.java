/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CursorJavaClassLabelProvider.java,v $
 *  $Revision: 1.6 $  $Date: 2005-10-03 19:20:48 $ 
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
			return ""; //$NON-NLS-1$
		}
	}

	public static String getText(IJavaInstance element) {
		String initStr = ""; //$NON-NLS-1$
		if (element.isParseTreeAllocation()) {
			PTExpression exp = ((ParseTreeAllocation) element.getAllocation()).getExpression();
			if (exp instanceof PTClassInstanceCreation) {
				NaiveExpressionFlattener flattener = new NaiveExpressionFlattener();
				exp.accept(flattener);
				initStr = flattener.getResult();
			}
		} else if (element.getAllocation() instanceof InitStringAllocation) {
			initStr = ((InitStringAllocation) element.getAllocation()).getInitString();
		}
		int begin_cursor = initStr.indexOf("SWT.CURSOR_"); //$NON-NLS-1$
		if (begin_cursor != -1) {
			int after_cursor = initStr.indexOf(")", begin_cursor); //$NON-NLS-1$
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

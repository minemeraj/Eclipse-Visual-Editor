/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: BeanFeatureTextCellEditor.java,v $
 *  $Revision: 1.2 $  $Date: 2004-08-27 15:34:09 $ 
 */

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.ve.internal.java.core.BeanFeatureEditor.IWrappedCellEditor;
/*
 * BeanTextCellEditor for working with the BeanFeatureEditor.
 * <package-protected> because it only makes sense working with the BeanFeatureEditor.
 * Too tightly tied together. 
 * 
 * @since 1.0.0
 */
class BeanFeatureTextCellEditor extends TextCellEditor implements IWrappedCellEditor {

	public BeanFeatureTextCellEditor(Composite parent) {
		super(parent);
	}

	/*
	 * @see IWrappedCellEditor#newValue(String)
	 */
	public void newValue(String text) {
		setValue(text);
	}
}

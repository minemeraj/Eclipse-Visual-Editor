package org.eclipse.ve.internal.java.core;
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
 *  $RCSfile: BeanTextCellEditor.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.ve.internal.java.core.BeanFeatureEditor.IWrappedCellEditor;

public class BeanTextCellEditor extends TextCellEditor implements IWrappedCellEditor {

	/**
	 * Constructor for BeanTextCellEditor.
	 * @param parent
	 */
	public BeanTextCellEditor(Composite parent) {
		super(parent);
	}

	/**
	 * @see IWrappedCellEditor#newValue(String)
	 */
	public void newValue(String text) {
		setValue(text);
	}
}

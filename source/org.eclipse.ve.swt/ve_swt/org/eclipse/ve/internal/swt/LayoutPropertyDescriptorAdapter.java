package org.eclipse.ve.internal.swt;
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
 *  $RCSfile: LayoutPropertyDescriptorAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2004-01-02 20:49:03 $ 
 */

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.java.core.BeanPropertyDescriptorAdapter;
import org.eclipse.ve.internal.propertysheet.command.ICommandPropertyDescriptor;

public class LayoutPropertyDescriptorAdapter extends BeanPropertyDescriptorAdapter {
	
	public CellEditor createPropertyEditor(Composite parent) {
		return createCellEditorInstance(LayoutCellEditor.class,parent , null, null);
	}	

}

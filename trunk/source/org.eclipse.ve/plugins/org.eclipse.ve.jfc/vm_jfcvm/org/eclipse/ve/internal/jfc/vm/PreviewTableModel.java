package org.eclipse.ve.internal.jfc.vm;
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
 *  $RCSfile: PreviewTableModel.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:35 $ 
 */

import javax.swing.table.DefaultTableModel;

public class PreviewTableModel extends DefaultTableModel {
	
public PreviewTableModel(){
	super(5, 5); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
}
public Object getValueAt(int row, int column){
	return String.valueOf(row) + 'x' + String.valueOf(column); //$NON-NLS-1$
}
}
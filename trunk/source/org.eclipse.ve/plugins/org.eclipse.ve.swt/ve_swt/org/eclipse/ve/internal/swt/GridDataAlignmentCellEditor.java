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
 *  $RCSfile: GridDataAlignmentCellEditor.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:52:55 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.ve.internal.java.core.EnumeratedIntValueCellEditor;

public class GridDataAlignmentCellEditor extends EnumeratedIntValueCellEditor {
	
	public GridDataAlignmentCellEditor(Composite composite){
		super(composite,
			new String[] { SWTMessages.GridDataAlignmentCellEditor_Name_Beginning , SWTMessages.GridDataAlignmentCellEditor_Name_Center, SWTMessages.GridDataAlignmentCellEditor_Name_End, SWTMessages.GridDataAlignmentCellEditor_Name_Fill } , 
	        new Integer[] { new Integer(GridData.BEGINNING) , new Integer(GridData.CENTER), new Integer(GridData.END), new Integer(GridData.FILL) } ,			
			new String[] { "org.eclipse.swt.layout.GridData.BEGINNING" , "org.eclipse.swt.layout.GridData.CENTER", "org.eclipse.swt.layout.GridData.END", "org.eclipse.swt.layout.GridData.FILL" }  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		);
	}	
}

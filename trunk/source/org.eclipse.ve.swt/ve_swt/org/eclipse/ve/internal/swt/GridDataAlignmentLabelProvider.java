package org.eclipse.ve.internal.swt;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: GridDataAlignmentLabelProvider.java,v $
 *  $Revision: 1.1 $  $Date: 2004-03-23 20:41:40 $ 
 */

import org.eclipse.swt.layout.GridData;

public class GridDataAlignmentLabelProvider extends EnumeratedIntValueLabelProvider {
	
	public GridDataAlignmentLabelProvider(){
		super(
				new String[] { "BEGINNING" , "CENTER", "END", "FILL" } ,
		        new Integer[] { new Integer(GridData.BEGINNING) , new Integer(GridData.CENTER), new Integer(GridData.END), new Integer(GridData.FILL) }
		);
	}
}

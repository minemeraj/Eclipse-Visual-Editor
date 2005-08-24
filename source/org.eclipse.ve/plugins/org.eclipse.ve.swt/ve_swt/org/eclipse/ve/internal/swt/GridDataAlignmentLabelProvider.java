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
 *  $RCSfile: GridDataAlignmentLabelProvider.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:52:55 $ 
 */
package org.eclipse.ve.internal.swt;


import org.eclipse.swt.layout.GridData;

import org.eclipse.ve.internal.java.core.EnumeratedIntValueLabelProvider;

public class GridDataAlignmentLabelProvider extends EnumeratedIntValueLabelProvider {
	
	public GridDataAlignmentLabelProvider(){
		super(
				new String[] { SWTMessages.GridDataAlignmentLabelProvider_Beginning , SWTMessages.GridDataAlignmentLabelProvider_Center, SWTMessages.GridDataAlignmentLabelProvider_End, SWTMessages.GridDataAlignmentLabelProvider_Fill } , 
		        new Integer[] { new Integer(GridData.BEGINNING) , new Integer(GridData.CENTER), new Integer(GridData.END), new Integer(GridData.FILL) }
		);
	}
}

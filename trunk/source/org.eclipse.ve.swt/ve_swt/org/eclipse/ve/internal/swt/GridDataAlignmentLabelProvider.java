/*******************************************************************************
 * Copyright (c)  2004 IBM Corporation and others.
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
 *  $Revision: 1.4 $  $Date: 2005-02-15 23:51:47 $ 
 */
package org.eclipse.ve.internal.swt;


import org.eclipse.swt.layout.GridData;

import org.eclipse.ve.internal.java.core.EnumeratedIntValueLabelProvider;

public class GridDataAlignmentLabelProvider extends EnumeratedIntValueLabelProvider {
	
	public GridDataAlignmentLabelProvider(){
		super(
				new String[] { SWTMessages.getString("GridDataAlignmentLabelProvider.Beginning") , SWTMessages.getString("GridDataAlignmentLabelProvider.Center"), SWTMessages.getString("GridDataAlignmentLabelProvider.End"), SWTMessages.getString("GridDataAlignmentLabelProvider.Fill") } , //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		        new Integer[] { new Integer(GridData.BEGINNING) , new Integer(GridData.CENTER), new Integer(GridData.END), new Integer(GridData.FILL) }
		);
	}
}

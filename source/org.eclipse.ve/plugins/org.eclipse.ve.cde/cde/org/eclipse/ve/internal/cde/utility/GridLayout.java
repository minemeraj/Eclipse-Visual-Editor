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
 *  $RCSfile: GridLayout.java,v $
 *  $Revision: 1.2 $  $Date: 2004-08-18 00:13:55 $ 
 */
package org.eclipse.ve.internal.cde.utility;

import org.eclipse.draw2d.FlowLayout;

/**
 * Lays out children in rows or columns, wrapping when the current row/column is filled.
 */
public class GridLayout extends FlowLayout {

	public GridLayout(){
		super(false);
		fill = true;
	}
	
}

/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on Mar 4, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.swt.SWT;

import org.eclipse.ve.internal.java.core.*;

/**
 * @author JoeWin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class OrientationLabelProvider extends EnumeratedIntValueLabelProvider {
	
	public OrientationLabelProvider(){
		super(
		    new String[] { SWTMessages.OrientationLabelProvider_Name_Horizontal , SWTMessages.OrientationLabelProvider_Name_Vertical } , 
			new Integer[] { new Integer(SWT.HORIZONTAL) , new Integer(SWT.VERTICAL ) }
		);
	}
}

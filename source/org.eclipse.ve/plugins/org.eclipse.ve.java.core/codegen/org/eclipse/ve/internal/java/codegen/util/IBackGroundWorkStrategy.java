package org.eclipse.ve.internal.java.codegen.util;
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
 *  $RCSfile: IBackGroundWorkStrategy.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.widgets.Display;


public interface IBackGroundWorkStrategy {
	
	public void run(Display disp, IDocumentListener docListener,
  	                Object[] items,
  	                ICancelMonitor monitor) ;

}
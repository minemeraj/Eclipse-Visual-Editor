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
 *  $Revision: 1.2 $  $Date: 2004-02-23 19:56:12 $ 
 */

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.swt.widgets.Display;

import org.eclipse.ve.internal.java.codegen.core.ICodegenLockManager;


public interface IBackGroundWorkStrategy {
	
	public void run(Display disp, ICompilationUnit workingCopy,
					ICodegenLockManager lockManager, List allDocumentEventsList, 
  	                ICancelMonitor monitor) ;

}
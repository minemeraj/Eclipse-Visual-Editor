/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.codegen.util;
/*
 *  $RCSfile: IBackGroundWorkStrategy.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:30:46 $ 
 */

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.swt.widgets.Display;

import org.eclipse.ve.internal.java.codegen.core.IEditorUpdateState;


public interface IBackGroundWorkStrategy {
	
	public void run(Display disp, ICompilationUnit workingCopy,
					IEditorUpdateState lockManager, List allDocumentEventsList, 
					IProgressMonitor monitor) ;

}

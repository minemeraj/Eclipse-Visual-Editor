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
 *  $RCSfile: IWorkingCopyProvider.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.IFileEditorInput;

import org.eclipse.ve.internal.java.codegen.java.ITypeResolver;



public interface IWorkingCopyProvider extends ITypeResolver{


 
 public ICompilationUnit getSharedWorkingCopy() ;
 public ICompilationUnit getSharedWorkingCopy(boolean forceReconcile) ;
 public ICompilationUnit getLocalWorkingCopy() ;
 
 public IFileEditorInput getEditor() ;
 public IFile getFile() ;
 
 public IDocument getSharedDocument() ;
 public IDocument getLocalDocument() ;
 public Object    getLocalDocLock() ;
 
 /**
  * Disconnect from the Shared/Local providers.
  */
 public void disconnect() ;
 /**
  * ReConnect to the Shared/Local providers.
  * @param new (or same) input file.
  */
 public void reconnect(IFile file) ;
 
 
 public void ReplaceWithLocalContent(IProgressMonitor pm, boolean commit) throws CodeGenException ; 
 public void UpdateDeltaToShared(ICancelMonitor pm, IDocumentListener docListener, List handles, boolean commit) throws CodeGenException ;
 
 public void aboutToChangeShared() ;
 public void changeCompleteShared() ;
 public ISourceRange getSharedSourceRange(String handle) ;
 public int getSharedLineNo(int Offset) ;
 public ITypeHierarchy getHierarchy() ;
 
 
 
 public void dispose() ;
 
 
}
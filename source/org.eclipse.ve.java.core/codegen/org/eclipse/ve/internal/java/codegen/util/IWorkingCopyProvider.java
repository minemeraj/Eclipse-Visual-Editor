/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $Revision: 1.4 $  $Date: 2004-08-27 15:34:10 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;


import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IFileEditorInput;



public interface IWorkingCopyProvider {


/**
 * Get the working copy
 * 
 * @param forceReconcile if true will reconcile the CU if needed
 */ 
 public ICompilationUnit getWorkingCopy(boolean forceReconcile) ;
 
 /**
  * @return the Input editor for this CU
  */
 public IFileEditorInput getEditor() ;
 
 /**
  * @return the resource associated with this CU
  */
 public IFile getFile() ;
/**
 * @return the Document associated with this CU
 */ 
 public IDocument getDocument() ;
/**
 * @return a global lock to be associated with this CU 
 */ 
 public Object    getDocLock() ;
 
 /**
  * DisAssociated from the working copy
  */
 public void disconnect() ;
 /**
  * Re/Connect to working copy
  * @param new (or same) input file.
  */
 public void connect(IFile file) ;
 /**
  * @param handle IJavaElement handle
  * @return source range if one exists
  */
 public ISourceRange getSourceRange(String handle) ;
 /**
  * @param Offset is the character offset
  * @return line number
  */
 public int getLineNo(int Offset) ;
 /**
  * @return the hierarchy associated with this CU
  */
 public ITypeHierarchy getHierarchy() ;
 /**
  * 
  * return the IJavaElement associated with a given handle signiture
  * @param handle
  * @return element if found, else null
  * 
  * @since 1.0.0
  */
 public IJavaElement getElement(String handle);
 
 public void dispose() ;
 
 /**
  * Return the type resolver for this provider.
  * @return type resolver.
  * 
  * @since 1.0.0
  */
 public TypeResolver getResolver();
}

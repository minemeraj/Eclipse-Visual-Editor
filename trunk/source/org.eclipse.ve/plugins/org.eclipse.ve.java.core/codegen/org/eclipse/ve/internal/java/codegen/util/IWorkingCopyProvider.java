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
/*
 *  $RCSfile: IWorkingCopyProvider.java,v $
 *  $Revision: 1.8 $  $Date: 2006-02-25 23:32:06 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;


import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IFileEditorInput;

import org.eclipse.ve.internal.java.core.TypeResolver;



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
 
 public Object getDocumentLock();
 
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

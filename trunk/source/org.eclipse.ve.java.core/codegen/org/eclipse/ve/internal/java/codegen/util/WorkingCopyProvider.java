package org.eclipse.ve.internal.java.codegen.util;
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
 *  $RCSfile: WorkingCopyProvider.java,v $
 *  $Revision: 1.3 $  $Date: 2004-01-21 00:00:24 $ 
 */

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import org.eclipse.jem.internal.core.MsgLogger;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;



/**
 *   This class provides a primitive factory to hook with to the working copy and associated
 *   IDocument that is used by the JavaEditor  
 *   Note:  It is the responsibility of the client of this class to call dispose()
 *          at some point.
 */
public class WorkingCopyProvider implements IWorkingCopyProvider {

      
	ICompilationUnit  fCU = null ;              // "the" working copy shared with other editors
	IFileEditorInput  fEditorIn = null ;        // Editor input associated with the file
	IFile             fFile = null ;            // the file itself
	boolean		  	  fdisconnected ;          
		
	Object            fdocLock = new Object() ;
	CodegenTypeResolver internalResolver = null;
	
	
	public WorkingCopyProvider (IFile file) {
		fFile = file ;
	}

 /**
  * @return boolean noting if a update was made
  */
 protected  boolean primReconcileCU(ICompilationUnit cu) {
 	try {
 	 if (!cu.isConsistent()) {
 	     cu.reconcile() ;
 	     return true ;
 	 }
 	}
 	catch (JavaModelException e) {
 		JavaVEPlugin.log(e, MsgLogger.LOG_WARNING) ;
 	}
 	return false ;
 }

 
 
 public  ICompilationUnit primGetWorkingCopy() {
 	
 	 synchronized (this) {
 	   if (fCU == null) {
 		fCU = JavaUI.getWorkingCopyManager().getWorkingCopy(getEditor()) ; 	    
 	   }
 	 } 	
 	 primReconcileCU(fCU) ;
 	 return fCU ;
 }

 public ICompilationUnit getWorkingCopy(boolean forceReconcile) {
 	if (forceReconcile || fCU == null)
 	   return primGetWorkingCopy();
 	else
 	   return fCU ; 	   
 }
 
public Object getDocLock() {
	return fdocLock ;
}
 
 /**
  *  @return IFileEditorInput accociated with the file resource 
  */
 public synchronized IFileEditorInput getEditor() {
 	if (fEditorIn != null) 
 	   return fEditorIn ; 	   
 	fEditorIn = new FileEditorInput(fFile) ;
 	// Raise the ref count
 	try {
 	  getDocumentProvider().connect(fEditorIn);
 	  getDocumentProvider().getAnnotationModel(fEditorIn).connect(getDocument());
 	}
 	catch (Exception e) {
 		JavaVEPlugin.log(e, MsgLogger.LOG_WARNING) ;
 	}
	 	
 	return fEditorIn ;
 }
 
 
 
 /**
  * @return IFile resource associated with this provider
  */ 
 public IFile getFile() {
 	return fFile ;
 }
 
 private CodegenTypeResolver getInternalResolver(){
 	if(internalResolver==null){
 		if(getWorkingCopy(false)!=null){
 			internalResolver = new CodegenTypeResolver(CodeGenUtil.getMainType(getWorkingCopy(false)));
 			internalResolver.connect();
 		}
 	}
 	return internalResolver;
 }
 
 protected IDocumentProvider getDocumentProvider() {
 	return JavaUI.getDocumentProvider() ;	
 	
 }
 
 /**
  *  @return IDocument associated with "the" Java Model working copy
  */
 public synchronized IDocument getDocument() {
 	return getDocumentProvider().getDocument(getEditor()) ;
 	
 }

 
 protected void primDisconnect() {
 	getInternalResolver().disconnect();
 	internalResolver=null ;
 	getDocumentProvider().getAnnotationModel(fEditorIn).disconnect(getDocument());     
 	getDocumentProvider().disconnect(fEditorIn);
 }
 
 
 /**
  *  Dispose this provider
  */
 public synchronized void dispose() {
 	disconnect();
 	fFile = null ;
 	fdocLock = null ;
 } 

 /**
  * Disconnect from the providers.
  */
 public synchronized void disconnect() { 	
 	if (fdisconnected) return ;
 	
 	primDisconnect();
 	fCU = null ;     
 	fEditorIn = null ;
 	fdisconnected = true ; 	
 }
 /**
  * ReConnect to the Shared/Local providers.
  * @param new (or same) input file.
  */
 public synchronized void connect(IFile file) {
     
     if (file == null) throw new IllegalArgumentException() ;
     if (!fdisconnected){
     	 if(file.equals(fFile)) return ;
     }
     else    	
         disconnect() ;
               
     fdocLock = new Object() ;  
     fFile = file ;         

     getEditor() ;    
     fdisconnected = false;     
 }


 public static IJavaElement getElement(String handle, IJavaElement root) {

		if (root != null) {
		    if (root.getHandleIdentifier().equals(handle))
		    	 return root;
		    else if (root instanceof IParent) 
			try {
				IJavaElement[] kids = ((IParent) root).getChildren();
				for (int i = 0; i < kids.length; i++) {
					IJavaElement r = getElement(handle,kids[i]) ;
					if (r!=null) return r;
				}				
			} catch (JavaModelException e) {}
		}
		return null;
	}
 
 public IJavaElement getElement(String handle) {
 	return getElement(handle,getWorkingCopy(true));
 }


/**
 *  The JavaModel does not includes comments, line indents etc.  
 * @deprecate
 */
 public static String getCompleteElementText(IMember m) throws JavaModelException {
	
	int left = m.getSourceRange().getOffset() ;
	int right = left + m.getSourceRange().getLength() ;
	String s = m.getCompilationUnit().getSource() ;
	
	
	if (m instanceof IField) {
		ExpressionParser p = new ExpressionParser((IField)m) ;
		return p.getExpression() ; 
	}
	else {
       // JCMMethod	  	  
	  while ((left-1)>=0 && (Character.isWhitespace(s.charAt(left-1))||(s.charAt(left-1)=='\t')) && s.charAt(left-1) != '\n' && s.charAt(left-1) != '\r') 
	    left -- ;
	
	  while (right<s.length() && Character.isWhitespace(s.charAt(right)) && 
	         s.charAt(right) != '\r' && s.charAt(right) != '\n') 
	     right++ ;
	  while (right<s.length() && (s.charAt(right) == '\r' || s.charAt(right) == '\n')) 
	    right++ ;
	
	  return s.substring(left,right) ;
	}	
	
}

 public void selectDocumentRegion(int offset, int len) {

   org.eclipse.ui.IWorkbenchWindow[] windows =	PlatformUI.getWorkbench().getWorkbenchWindows() ;
   if (windows != null && windows.length > 0) {
   	for (int i=0; i<windows.length; i++) {
   		org.eclipse.ui.IEditorPart ep = windows[i].getActivePage().getActiveEditor() ;
   		if (ep.getEditorInput().getName().equals(getEditor().getName()) && ep instanceof ITextEditor) {
                ((ITextEditor)ep).selectAndReveal(offset,len) ;
   		      break ;
   		}
   	}
   }
 }
 
 public ISourceRange getSourceRange(String handle) {   
     IJavaElement shared = getElement(handle, getWorkingCopy(true));
		if (shared != null)
			try {
				if (shared instanceof ISourceReference)
					return ((ISourceReference) shared).getSourceRange();
			} catch (JavaModelException e) {
			}
		return null;
 }
 
 public int getLineNo(int Offset) {
     try {
       return getDocument().getLineOfOffset(Offset) ;
     }
     catch (BadLocationException e) {} 
     return -1 ;
 }

 // TODO:  we should consider bindings here
 public String resolve(String unresolved){	
		return unresolved != null ? getInternalResolver().resolveTypeComplex(unresolved) : null;
 }
 
	/*
	 * @see ITypeResolver#resolveThis()
	 */
	public String resolveThis() {
		IType type = CodeGenUtil.getMainType(getWorkingCopy(true));
		return type.getTypeQualifiedName();
	}
    
    public ITypeHierarchy getHierarchy() {
        try {
          IType t = CodeGenUtil.getMainType(getWorkingCopy(true)) ;
          return t.newSupertypeHierarchy(null) ;
        }
        catch (org.eclipse.jdt.core.JavaModelException e) {}
        return null ;
    }
    
    public String toString() {    
    	return "WCP ["+fFile+"]" ; //$NON-NLS-1$ //$NON-NLS-2$
    }

}
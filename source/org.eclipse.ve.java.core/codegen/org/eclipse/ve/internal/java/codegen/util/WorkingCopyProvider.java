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
 *  $RCSfile: WorkingCopyProvider.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.lang.reflect.Method;
import java.util.*;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.core.JavaModelManager;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jface.text.*;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.codegen.java.FreeFormThisAnnotationDecoder;
import org.eclipse.ve.internal.java.codegen.java.ICodeGenSourceRange;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;



/**
 *   This class provides a primitive function that hook up with 
 *   the Java Model Manager's working copy for a particular (single) file resource.
 *   In essence, it facilitates local shadow of a Document Provider for the Java Model 
 *   (a working copy for the working copy).
 *   This allows a client to fully control access to a local working copy, 
 *   one controled delta at a time, and update "the" Java Model working copy on a need to basis.
 *   Note:  It is the responsibility of the client of this class to call dispose()
 *          at some point.
 */
public class WorkingCopyProvider implements IWorkingCopyProvider {

      
	ICompilationUnit  fLocalCU = null ;           // Local Shadow of the actual Working Copy
	ICompilationUnit  fWcCU = null ;              // "the" working copy shared with other editors
	IFileEditorInput  fEditorIn = null ;     
	IFileEditorInput  fLocalEditorIn = null ;     // This one never changes
	IFile             fFile = null ;              // backing resource java file
	boolean		  fdisconnected ;
	
	ShadowCUDocumentProvider   fLocalCUProvider = null ;	
	Object            fLocalDocLock = new Object() ;
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
/**
 *  @return ICompilationUnit of the local shadowed CU (not shared)
 */	
 public synchronized ICompilationUnit getLocalWorkingCopy()  {
 	IFileEditorInput editor = getLocalEditor() ;
 	if (fLocalCU != null) {
 	   primReconcileCU(fLocalCU) ;
 	   return fLocalCU ;
 	}
 	try { 	  
	  fLocalCU = getLocalDocumentProvider().getWCU(editor) ;
 	  // TODO  Need to listen for changes first
 	  getLocalDocumentProvider().getDocument(getEditor()).set(primGetSharedWorkingCopy().getSource()) ;
 	  primReconcileCU(fLocalCU) ;
 	  return fLocalCU ;
 	  
 	}
 	catch (JavaModelException e) {
 	 	JavaVEPlugin.log(e, MsgLogger.LOG_WARNING) ;
 	 	return null ;
 	} 	
 }

 protected  ICompilationUnit primGetSharedWorkingCopy() {
 	
 	 synchronized (this) {
 	   if (fWcCU == null) {
 		fWcCU = JavaUI.getWorkingCopyManager().getWorkingCopy(getEditor()) ; 	    
 	   }
 	 } 	
 	 primReconcileCU(fWcCU) ;
 	 return fWcCU ;
 }
 /**
  *  @return ICompilationUnit of "the" shared Java Model working copy
  */
 public  ICompilationUnit getSharedWorkingCopy() { 	
 	  return primGetSharedWorkingCopy() ; 	
 }
 public ICompilationUnit getSharedWorkingCopy(boolean forceReconcile) {
 	if (forceReconcile || fWcCU == null)
 	   return getSharedWorkingCopy() ;
 	else
 	   return fWcCU ; 	   
 }
 
public Object getLocalDocLock() {
	return fLocalDocLock ;
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
 	  getSharedDocumentProvider().connect(fEditorIn);
 	  getSharedDocumentProvider().getAnnotationModel(fEditorIn).connect(getSharedDocument());
 	  if (fLocalCUProvider==null) {  // Only for the first time do we create/attach
 	    getLocalDocumentProvider().connect(fEditorIn) ;  
 	  }
 	}
 	catch (Exception e) {
 		JavaVEPlugin.log(e, MsgLogger.LOG_WARNING) ;
 	}
	 	
 	return fEditorIn ;
 }
 
 public synchronized IFileEditorInput getLocalEditor() {
 	if (fLocalEditorIn!=null)
 	  return fLocalEditorIn ;
 	  
   fLocalEditorIn = getEditor() ;
   return fLocalEditorIn ;
 }
 
 /**
  * @return IFile resource associated with this provider
  */ 
 public IFile getFile() {
 	return fFile ;
 }
 
 private CodegenTypeResolver getInternalResolver(){
 	if(internalResolver==null){
 		if(getSharedWorkingCopy()!=null){
 			internalResolver = new CodegenTypeResolver(CodeGenUtil.getMainType(getSharedWorkingCopy()));
 			internalResolver.connect();
 		}
 	}
 	return internalResolver;
 }
 
 protected IDocumentProvider getSharedDocumentProvider() {
 	return JavaUI.getDocumentProvider() ;	
 	
 }
 
 /**
  *  @return IDocumentProvider of the shadowed document
  */
  
 protected ShadowCUDocumentProvider getLocalDocumentProvider() {
 	if (fLocalCUProvider != null)
 	  return fLocalCUProvider  ;
 	
 	fLocalCUProvider = ShadowCUDocumentProvider.getProvider() ;
 	return fLocalCUProvider ;
 }
 
 
 /**
  *  @return IDocument associated with "the" Java Model working copy
  */
 public synchronized IDocument getSharedDocument() {
 	return getSharedDocumentProvider().getDocument(getEditor()) ;
 	
 }

 /**
  *  @return IDocument associated with the shadowed document
  */ 
 public IDocument getLocalDocument() {
 	return getLocalDocumentProvider().getDocument(getLocalEditor()) ;
 }

 /**
  *  DisAssociate this working copy with the shared document
  */
 public synchronized void dispose() {
 	if (fEditorIn != null) { 	
 	  getInternalResolver().disconnect();
 		// Decrement ref. count
 	  getSharedDocumentProvider().getAnnotationModel(fEditorIn).disconnect(getSharedDocument()); 	
 	  getSharedDocumentProvider().disconnect(fEditorIn);
 	    // No need for the local CU.
 	  if (fLocalCUProvider.getAnnotationModel(fLocalEditorIn)!=null) 
 	    fLocalCUProvider.getAnnotationModel(fLocalEditorIn).disconnect(getLocalDocument()) ; 	     
 	  fLocalCUProvider.disconnect(fLocalEditorIn) ;
 	  
 	  // Workaround for a JDK leakage on working Copy Provider
 	  try {
 	  	// TODO See if this is still necessary, or is there an QPI way.
		Method getElements = JavaModelManager.class.getDeclaredMethod("getElementsOutOfSynchWithBuffers", new Class[] {}); //$NON-NLS-1$
		getElements.setAccessible(true) ;
		Map elementsOutOfSync = (Map) getElements.invoke(JavaModelManager.getJavaModelManager(), new Object[] {}) ;
		elementsOutOfSync.remove(fLocalCU);
	  }
	  catch (Throwable e) {}
	  if (fLocalCU != null) fLocalCU.destroy() ;
 	  fLocalCU = fWcCU = null ;
 	  fEditorIn = null ;
// 	  fLocalCUProvider.shutdown() ;
 	  // Workaround for a JDT continue
 	  JavaModelManager.getJavaModelManager().sharedWorkingCopies.remove(fLocalCUProvider.getBufferFactory());
 	  fLocalCUProvider=null ;
 	}
 } 

 /**
  * Disconnect from the Shared/Local providers.
  */
 public synchronized void disconnect() {
     if (fdisconnected) return ;
     
     getInternalResolver().disconnect();
     internalResolver=null ;
     getSharedDocumentProvider().getAnnotationModel(fEditorIn).disconnect(getSharedDocument());     
     getSharedDocumentProvider().disconnect(fEditorIn);
     
     
     if (fLocalCUProvider != null) {
     	getLocalDocument().set("") ; //$NON-NLS-1$
//     	if (fLocalCUProvider.getAnnotationModel(fLocalEditorIn) != null)
//           fLocalCUProvider.getAnnotationModel(fLocalEditorIn).disconnect(fLocalCUProvider.getDocument(fLocalEditorIn)) ;
        fLocalCUProvider.disconnect(fLocalEditorIn==null ? fEditorIn : fLocalEditorIn) ;
     }
     
     fWcCU = null ;     
     
     if (fLocalCU != null) {
     	// This is a workaround to the fact that the JavaModelManager assume a single CU Provider
     	// Forcefully remove its indirect access to the local provider's state sync. state.
		try {
			Method getElements = JavaModelManager.class.getDeclaredMethod("getElementsOutOfSynchWithBuffers", new Class[] {}); //$NON-NLS-1$
			getElements.setAccessible(true) ;
			Map elementsOutOfSync = (Map) getElements.invoke(JavaModelManager.getJavaModelManager(), new Object[] {}) ;
			elementsOutOfSync.remove(fLocalCU);
						
		}
		catch (Throwable e) {}
		fLocalCU.destroy() ;
     }
     fLocalCU = null ;
     
//     We are sharing a local provider across editors
//     if (fLocalCUProvider != null)
//          fLocalCUProvider.shutdown() ;  
       
          
     // Same workaround
     JavaModelManager.getJavaModelManager().sharedWorkingCopies.remove(fLocalCUProvider.getBufferFactory());
     fLocalCUProvider=null ;     
          
     fEditorIn = fLocalEditorIn = null ;
     fdisconnected = true ;
 }
 /**
  * ReConnect to the Shared/Local providers.
  * @param new (or same) input file.
  */
 public synchronized void reconnect(IFile file) {
     
     if (file == null || fFile == null) throw new IllegalArgumentException() ;
     
     if (!fdisconnected) 
         disconnect() ;
               
     fLocalDocLock = new Object() ;  

     fFile = file ;         

     getLocalEditor() ;

    
     // Must explicitly update the local CU's content
     try {         	
		getLocalDocumentProvider().getDocument(getLocalEditor()).set(getSharedDocument().get()) ;
		getLocalWorkingCopy().reconcile() ;
	  } catch(JavaModelException e) {
        JavaVEPlugin.log(e) ;
     }
    
     fdisconnected = false;     
 }

 
 /**
  *  Completely replace the shared document with the content of the shadow document.
  */
 public void ReplaceWithLocalContent(IProgressMonitor pm, boolean commit) throws CodeGenException {
 	IDocumentProvider docProvider = getSharedDocumentProvider() ;
 	IDocument sDoc = getSharedDocument() ;
	try {	    
	 
	    primReconcileCU(getSharedWorkingCopy()) ;	    
	    synchronized (fLocalDocLock) {
	      getLocalWorkingCopy() ;		
		
		IDocument lDoc = getLocalDocument() ;		
		sDoc.set(lDoc.get());
		if (commit) {
			docProvider.aboutToChange(getEditor());				
			docProvider.saveDocument(pm, fEditorIn, sDoc,false) ;		
		}
	    }	
	} catch (Exception e) {
		JavaVEPlugin.log(e, MsgLogger.LOG_WARNING) ;
		throw new CodeGenException (e) ;
	} finally {
		if (commit) 
		  docProvider.changed(fEditorIn);
	}
}

 protected static IJavaElement getElement (String handle, IType t) {
 	try {
 	 IJavaElement[] elements = t.getChildren() ;
 	 for (int i=0 ; i<elements.length; i++) {
 		if (elements[i].getHandleIdentifier().equals(handle)) {
 			return elements[i] ;
 		}
 	 }
 	}
 	catch (Exception e) {}
 	return null ;
 }
 public static  IJavaElement getElement (String handle, ICompilationUnit cu) { 	
 	try {
 	 IType [] types = cu.getAllTypes() ;
 	 for (int i=0; i<types.length; i++) { 		
 		IJavaElement result = getElement(handle,types[i]) ;
 		if (result != null) return result ;
  	 }
 	}
 	catch (Exception e) {} ;
 	return null ;
 }

/**
 *  The JavaModel does not includes comments, line indents etc.  
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


protected IJavaElement   getSiebling(IMember element) {
    IType parent = (IType) element.getParent() ;
    try {
		IJavaElement[] kids = parent.getChildren() ;
		boolean foundMyself = false;
	    for (int i = 0; i < kids.length; i++) {
	        IJavaElement e = kids[i];
	        if(foundMyself && e.getElementType()==element.getElementType())
	        	return e;
	        if (e.equals(element))
	        	foundMyself = true;
	    }
	} catch(JavaModelException e) {}
    return null ;
}

protected void ReplaceWithLocalClassDecleration (IProgressMonitor pm) {
    String newContent = FreeFormThisAnnotationDecoder.getDesignatedAnnotationString(getLocalWorkingCopy());
	ICodeGenSourceRange destSR = FreeFormThisAnnotationDecoder.getDesignatedAnnotationRange(getSharedWorkingCopy()) ;	
	if (newContent == null || destSR == null) {
       JavaVEPlugin.log("WorkingCopyProvider.ReplaceWithLocalClassDecleration(): Could not update THIS FF annotation",MsgLogger.LOG_FINE) ; //$NON-NLS-1$
       return ;          	
    }
	try {
		getSharedDocument().replace(
								destSR.getOffset(),
								destSR.getLength(),
								newContent);	
	} catch (BadLocationException e) {
		JavaVEPlugin.log(e,org.eclipse.jem.internal.core.MsgLogger.LOG_SEVERE) ;
	}
}

/**
 * Will move the content of element in handles, to "the" Java Model working copy
 */
public void ReplaceWithLocalContent(String handle, IProgressMonitor pm) throws CodeGenException {
	IMember local,shared ;
	
	synchronized (fLocalDocLock) {
		if (handle.equals(BeanPart.THIS_HANDLE)) { // Non Supported JDT IMember
			ReplaceWithLocalClassDecleration(pm);
			
		} else { // Use JDT
			local = (IMember) getElement(handle, getLocalWorkingCopy());
			// Local Copy has been reconciled
			shared = (IMember) getElement(handle, getSharedWorkingCopy());

			if (local == null && shared == null)
				return;

			try {
				if (local == null) {
					// remove from shared.
					JavaVEPlugin.log("WorkingCopyProvider.ReplaceWithLocalContent(): Deleting " + shared.getElementName(), MsgLogger.LOG_FINE); //$NON-NLS-1$
					shared.delete(true, pm);
				} else if (shared == null) {
					// create element in share

					IType sType = (IType) CodeGenUtil.getMainType(getSharedWorkingCopy());
					JavaVEPlugin.log("WorkingCopyProvider.ReplaceWithLocalContent(): creating " + local.getElementName(), MsgLogger.LOG_FINE); //$NON-NLS-1$
					String content = getCompleteElementText(local);
					if (local instanceof IField) {
						sType.createField(content, null, false, pm);
					} else if (local instanceof IMethod) {
						IMethod lSieb = (IMethod) getSiebling(local);
						IMethod sSieb = null;
						if (lSieb != null)
							sSieb = CodeGenUtil.getMethod(sType, lSieb.getHandleIdentifier());
						// Workaround, not to include extras that are added to a IMethod.
						// TODO
						//content = content.substring(content.indexOf(BeanMethodTemplate.COMMENT_BEG)) ;
						sType.createMethod(content, sSieb, false, pm);
					} else
						throw new CodeGenException("Do not know how to create: " + local); //$NON-NLS-1$
				} else {
					// Refresh share with local copy

					JavaVEPlugin.log("WorkingCopyProvider.ReplaceWithLocalContent(): replacing " + shared.getElementName(), MsgLogger.LOG_FINE); //$NON-NLS-1$
					if (primReconcileCU(getSharedWorkingCopy())) {
						// SourceRange may have changed.
						shared = (IMember) getElement(handle, getSharedWorkingCopy());
					}
					// This is more efficient than delete/re-create of a method	   	   
					if(!(shared instanceof IField)){
						// Non-IFields
						getSharedDocument().replace(
							shared.getSourceRange().getOffset(),
							shared.getSourceRange().getLength(),
							local.getSource());
					}else{
						// IFields....
//						int localStart =
//							local.getSourceRange().getOffset() + local.getSourceRange().getLength();
//						// do not pass the EOL
//						int localEnd = getLocalDocument().get().indexOf('\n', localStart);
//						if(getLocalDocument().get().charAt(localEnd-1)=='\r')
//							localEnd--;
//
//						if (primReconcileCU(getSharedWorkingCopy())) {
//							shared = (IMember) getElement(handle, getSharedWorkingCopy());
//						}
//
//						// Update the commet, if needed	       	       
//						// IField does not include the comment !!!
//						int sharedStart =
//							shared.getSourceRange().getOffset()
//								+ shared.getSourceRange().getLength();
//						int sharedEnd = getSharedDocument().get().indexOf('\n', sharedStart);
//						if(getSharedDocument().get().charAt(sharedEnd-1)=='\r')
//							sharedEnd--;

						// We cannot rely on JDT for replacing IFields, because in the case of COMMENT_LINEs the LINE_SEPARATOR
						// is included in the IField's source, and in the cases of COMMENT_BLOCK & COMMENT_JAVADOC it is not. This
						// results in unecessary addition of LINE_SEPARATORS to the code.
						
						// Use the new ExpressionParser capable of handling IFields
						ExpressionParser sharedParser = new ExpressionParser((IField)shared);
						ExpressionParser localParser = new ExpressionParser((IField)local);
						int sharedStart = sharedParser.getExpressionOff();
						int sharedEnd = sharedStart + sharedParser.getExpressionLen();
						int localStart = localParser.getExpressionOff();
						int localEnd = localStart+localParser.getExpressionLen();
						
						if (sharedEnd >= sharedStart && localEnd >= localStart) {
							getSharedDocument().replace(
								sharedStart,
								sharedEnd - sharedStart,
								getLocalDocument().get().substring(localStart, localEnd));
							//	          String delta = getLocalDocument().get().substring(localStart,localEnd) ;
							//	          IDocument d = getSharedDocument() ;
							//	          Thread.yield() ;
							//	          d.replace(sharedStart,sharedEnd-sharedStart,delta) ;
						} else if (sharedEnd < sharedStart) {
							org.eclipse.ve.internal.java.core.JavaVEPlugin.log("WorkingCopyProvider.ReplaceWithLocalContent(): Error refreshing comment for " + handle, //$NON-NLS-1$
							org.eclipse.jem.internal.core.MsgLogger.LOG_WARNING);
						}
					}
				}
			} catch (Exception e) {
				JavaVEPlugin.log(e, MsgLogger.LOG_WARNING);
			}
		}
	}
}


/**
 *  All elements in handles, are going to be flushed to the shared document.
 *  Duplicate handles are allowed.
 */
 public void UpdateDeltaToShared(ICancelMonitor pm, IDocumentListener docListener, List handles, boolean commit) throws CodeGenException {
 	 	    
 	synchronized (fLocalDocLock) {
 	 try { 	
 	  if (handles != null) {
  	    ArrayList processed = new ArrayList () ;
 	    Iterator itr = handles.iterator() ;

         if (docListener != null)	  
            getSharedDocument().removeDocumentListener(docListener) ;
           
 	   while (itr.hasNext()) {
 	  	String toBeProcessed = (String) itr.next() ;
 	  	if (!processed.contains(toBeProcessed)) {
 	  	    if (pm != null && pm.isCanceled()) return ;
 		    ReplaceWithLocalContent(toBeProcessed,null) ;
 		    processed.add(toBeProcessed) ;
 	  	}
 	   } 	
 	  }
 	  if (commit) {
 	     getSharedDocumentProvider().aboutToChange(getEditor()) ; 	  
	     getSharedDocumentProvider().saveDocument(null, getEditor(), getSharedDocument(),false) ;
 	  }
 	 }
 	 catch (Exception e) {
 	 	JavaVEPlugin.log(e, MsgLogger.LOG_WARNING) ;
 	 	throw new CodeGenException(e) ;
 	 }
 	 finally {
 	   if (docListener != null)
 	      getSharedDocument().addDocumentListener(docListener) ;
 	   if (commit)
	     getSharedDocumentProvider().changed(getEditor()) ;	   	   
	  // The JavaReconciler will eventually re-concile the shared copy.
 	 } 	
 	} 	 	
 }
 
 public void aboutToChangeShared() {
 	getSharedDocumentProvider().aboutToChange(getEditor()) ;
 }
 public void changeCompleteShared() {
 	getSharedDocumentProvider().changed(getEditor()) ; 	
 }
 
 public void selectSharedRegion(int offset, int len) {
 	
   org.eclipse.ui.IWorkbenchWindow[] windows =	org.eclipse.ui.internal.WorkbenchPlugin.getDefault().
                                                getWorkbench().getWorkbenchWindows() ;
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
 
 public ISourceRange getSharedSourceRange(String handle) {
     if (handle == null) return null ;
     
     IMember shared = (IMember) getElement (handle,getSharedWorkingCopy()) ;
     if (shared == null) return null ;
     try {
       return shared.getSourceRange() ;
     }
     catch (JavaModelException e) {}
     return null ;
 }
 
 public int getSharedLineNo(int Offset) {
     try {
       return getSharedDocument().getLineOfOffset(Offset) ;
     }
     catch (Exception e) {} 
     return -1 ;
 }

 public String resolve(String unresolved){
	try{
		return unresolved != null ? getInternalResolver().resolveTypeComplex(unresolved) : null;
	}catch(Exception e){
		return unresolved;
	}
 }
 
	/*
	 * @see ITypeResolver#resolveThis()
	 */
	public String resolveThis() {
		IType type = CodeGenUtil.getMainType(getSharedWorkingCopy());
		return type.getTypeQualifiedName();
	}
    
    public ITypeHierarchy getHierarchy() {
        try {
          IType t = CodeGenUtil.getMainType(getLocalWorkingCopy()) ;
          return t.newSupertypeHierarchy(null) ;
        }
        catch (org.eclipse.jdt.core.JavaModelException e) {}
        return null ;
    }
    
    public String toString() {    
    	return "WCP ["+fFile+"]" ; //$NON-NLS-1$ //$NON-NLS-2$
    }

}
package org.eclipse.ve.internal.java.codegen.model;
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
 *  $RCSfile: BeanDeclModel.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-21 00:00:24 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
//  The following will suport a working copy for the working copy
//import org.eclipse.jdt.internal.core.BufferManager;

import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.codegen.core.ICodeGenStatus;
import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;



/*
 *  This class is the root for the Bean Decleration Model
 */
public class BeanDeclModel implements IBeanDeclModel {
	
	ArrayList   fBeans=null ;                           // Root Beans
	Hashtable   fBeansKey = new Hashtable () ;   	    // Keep a hash key for the fBeans vector
	Hashtable   fBeanReturns = new Hashtable() ;        // Keep a hash of Methods with return Beans ... e.g., getJButton1() ;
	Hashtable	fBeanInitMethod = new Hashtable() ;     // Keep a hash to all Methods  that update beans 
	Hashtable   fRefObjKey = new Hashtable () ;         // Keep a hash from a EObject to Bean Part
    ArrayList   fBeanDeleteCandidates = new ArrayList() ; // Hold before an Editor command is completed
	
	TypeDeclaration				fTypeDeclaration = null ;           // The class element of the JDOM model
	CodeTypeRef                 fTypeRef = null ;
	List						fEventHandlers = new ArrayList() ;  // CodeEventHandlerRef list
	CompilationUnitDeclaration	fJDOM = null  ;                     // Root of the JDOM
	IWorkingCopyProvider		fWorkCopyP = null ;
//  The following will suport a working copy for the working copy
//	ICompilationUnit			fworkingWC = null ;					// Will be used as a temporary working copy for the working copy	
	JavaSourceSynchronizer      fSrcSync = null ;
	IDiagramModelInstance		fCompositionModel = null ;
	String                      fLineSeperator = null ;
	int                         fState = BDM_STATE_DOWN ;
	ICodeGenStatus				fStatus = null ;
	EditDomain					fDomain = null ; 
	
//  The following will suport a working copy for the working copy	
//	WorkingCopyOwner			fworkingWCowner = new WorkingCopyOwner() {		
//			BufferManager d= new BufferManager();
//			public IBuffer createBuffer(ICompilationUnit workingCopy) {
//				return d.createBuffer(workingCopy);
//			}
//	     };
	
/**
 * 
 * This is a temporary workaround for the fact that we do not
 * have the notion of bean processing proirity see
 * defect 256142
 * 
 */	
private boolean isPriority(BeanPart b) {
	String s = b.getType() ;
	return  (s.equals("java.awt.GridBagConstraints"));	//$NON-NLS-1$
}

/**
 * Get all Beans
 */	
public List getBeans() {
	ArrayList v = new ArrayList() ;
	
	Enumeration e = fBeansKey.elements() ;
	while (e.hasMoreElements()) {
	  Object o = e.nextElement() ;	  
	  if (isPriority((BeanPart)o))
	     v.add(0,o);
	  else
	     v.add(o) ;
	}
	return v ;
}	

public List getEventHandlers() {
	return fEventHandlers ;
}



public synchronized void dispose() {
	fState = BDM_STATE_DOWN ;
//	if (fSrcSync != null) 
//	   fSrcSync.uninstall() ;
	List beans = getBeans() ;
	for (int i=beans.size()-1; i>=0; i--) {
		BeanPart b = (BeanPart) beans.remove(i) ;
		// clean up all expression adapters
		b.dispose () ;
	}
	fTypeRef.dispose() ;
	for (int i = 0; i < fEventHandlers.size(); i++) 
		((CodeEventHandlerRef)fEventHandlers.get(i)). dispose() ;
	fCompositionModel = null ;
	fSrcSync = null ;
	fWorkCopyP = null ;
}




/**
 * Get beans directly sitting on the Compositions Beans
 */	
public List getRootBeans() {
	if (fBeans == null) fBeans = new ArrayList() ;
	return fBeans ;
}	

public void setLineSeperator(String sep) {
	fLineSeperator = sep ;
}
public String getLineSeperator() {
	return fLineSeperator ;
}

/**
 * When the VCE model removes a BeanPart from the model, it may be a temporary
 * stage were it is about to be added onto a different container/aggregator.
 */
public void designateAsDelete(BeanPart bean) {
    if (bean == null || fBeanDeleteCandidates.contains(bean)) return ;
    synchronized(fBeanDeleteCandidates) {
      fBeanDeleteCandidates.add(bean) ;
    }
}

/**
 * When the VCE model is added with an object, a client should check to see
 * if the tobe added is a delete candidate.
 * 
 * @param obj instance in question
 * @param remove determine if to remove the object fromt he delete candidate list
 * @return the BeanPart associated with the object, if it is in the delete candidate
 */
public BeanPart getDeleteDesignated(IJavaObjectInstance obj, boolean remove) {
    if (obj == null) return null ;
    BeanPart bean = getABean(obj) ;
    if (bean != null && fBeanDeleteCandidates.contains(bean)) {
       if (remove) 
         synchronized(fBeanDeleteCandidates) {
          fBeanDeleteCandidates.remove(bean) ;
         }
       return bean ;
    } else
        return null ;
}

/**
 * An editor compound command has completed.  Those elements that have not
 * bean added back to the JVE model will be removed from the BDM and source code.
 */
public void deleteDesignatedBeans() {
    
    synchronized (fBeanDeleteCandidates) {
        for(int i=0; i<fBeanDeleteCandidates.size(); i++) {
          try {
            BeanPart b = (BeanPart) fBeanDeleteCandidates.get(i) ;        
            BeanPartFactory bgen = new BeanPartFactory(this,getCompositionModel()) ;
            bgen.removeBeanPart(b) ;
          }
          catch (Throwable e) {
              org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
          }
        }
        fBeanDeleteCandidates.clear() ;
    }
    
}


/**
 * Return all beans initialized by this method
 */	
public BeanPart[] getBeansInializedByMethod(String methodHandle) {
	Vector beans = new Vector () ;
	Iterator itr = getBeans().iterator() ;
	while (itr.hasNext()) {
		BeanPart b = (BeanPart)itr.next() ;
		if(b.getInitMethod()!=null && // consider beanparts with fields but no methods
		   b.getInitMethod().getMethodHandle().equals(methodHandle))
		   beans.add(b) ;
	}
	BeanPart [] result ;
	if (beans.size() > 0) {
		result = new BeanPart[beans.size()] ;
		for (int i=0; i<beans.size(); i++) {
			result[i] = (BeanPart)beans.elementAt(i) ;
		}
	}
	else
	    result = null ;
	    
	return result ;	
}


/**
 *  Add a BeanPart
 */
public void addBean (BeanPart bean) {

   // TODO  Need to deal with duplicates
   bean.setModel(this) ;
   if (bean.getContainer() == null) {
      if (!getRootBeans().contains(bean))
         getRootBeans().add(bean) ;
   }
   else
      getRootBeans().remove(bean) ;
   fBeansKey.put(bean.getUniqueName(),bean) ;
   if (bean.getEObject() != null)
     fRefObjKey.put(bean.getEObject(),bean) ;
}

/**
 * BeanPart has a Container, remove from root elements
 */
public void removeBeanFromRoot (BeanPart bean) {

   getRootBeans().remove(bean) ;
}
/**
 * BeanPart has a Container, remove from root elements
 */
public void removeBean (BeanPart bean) {

   removeBeanFromRoot(bean) ;
   fBeansKey.remove(bean.getUniqueName()) ; 
   if (bean.getEObject()!= null)  
      fRefObjKey.remove(bean.getEObject()) ;
   fBeanReturns.values().remove(bean) ;
   bean.setModel(null) ;
}


/**
 *
 */
public BeanPart getABean(String name) {
   return (BeanPart) fBeansKey.get(name)	 ;
}

/**
 *
 */
public BeanPart getABean(EObject obj) {
   return (BeanPart) fRefObjKey.get(obj)	 ;
}

public void UpdateRefObjKey(BeanPart bean,EObject prev) {
	if (prev != null) fRefObjKey.remove(prev) ;
	if (bean.getEObject() != null)
	   fRefObjKey.put(bean.getEObject(),bean) ;
}

/**
 *
 */
public TypeDeclaration getTypeDecleration() {
   return fTypeDeclaration ;
}

/**
 *  Set Type Decleration
 */
public void setTypeDecleration(TypeDeclaration decl) {
    fTypeDeclaration=decl ;
}
/**
 *  
 */
public CompilationUnitDeclaration getJDOM() {
   return fJDOM ;
}

public CodeTypeRef getTypeRef() {
	return fTypeRef ;
}
public void setTypeRef (CodeTypeRef tr) {
	fTypeRef = tr ;
}

/**
 *  Set Type Compilation Unit Decleration
 */
public void setJDOM(CompilationUnitDeclaration dom) {
    fJDOM=dom ;
}

public IDiagramModelInstance getCompositionModel() {
	return fCompositionModel ;
}
public void setCompositionModel(IDiagramModelInstance cm) {
    
    if (cm == null)
      if (fCompositionModel == null) return ;
   
	fCompositionModel = cm ;
	
	org.eclipse.ve.internal.jcm.BeanSubclassComposition c = cm.getModelRoot() ;
	
	   
	BeanPart b = new BeanPart("BeanSubclassComposition","") ; //$NON-NLS-1$ //$NON-NLS-2$
	if (!isStateSet(BDM_STATE_SNIPPET)) {
	   // Main BDM, adaptet to the composition
	   b.setModel(this) ;
	   b.setEObject(c) ;	   
	   	// Remove the vanilla BeanPart adapter put by the setRefObject()
		ICodeGenAdapter a = (ICodeGenAdapter) EcoreUtil.getExistingAdapter(c,ICodeGenAdapter.JVE_CODE_GEN_TYPE) ;
		while (a != null){
		   c.eAdapters().remove(a) ;
		   a = (ICodeGenAdapter) EcoreUtil.getExistingAdapter(c,ICodeGenAdapter.JVE_CODE_GEN_TYPE) ;
		}

		// Set up a Root CodeGen adapter		
		CompositionDecoderAdapter adapter = new CompositionDecoderAdapter(b) ;
		adapter.setTarget(c) ;
	    c.eAdapters().add(adapter) ;
	}
	else  // do not adapt Snippet BDMs
	   b.setModel(this) ;
}

/**
 *  Setter/Getter for the Compilation Unit Associated with this Model
 */
public void setWorkingCopyProvider(IWorkingCopyProvider wcp) {
	fWorkCopyP = wcp ;
	if (fSrcSync != null) 
	   fSrcSync.uninstall() ;
	fSrcSync = null ;	   
}

public void setSourceSynchronizer(JavaSourceSynchronizer sync) {
	if (fSrcSync != null) 
	   fSrcSync.uninstall() ;
	fSrcSync = sync ;	
}


/**
 * During updates, the first call to this method will create
 * a working copy to THE working copy.
 */
public synchronized ICompilationUnit getCompilationUnit() {
	if(fWorkCopyP!=null) {
//  The following will suport a working copy for the working copy		
//		if (isStateSet(IBeanDeclModel.BDM_STATE_UPDATING_DOCUMENT))
//			return getWorkingWorkingCopy() ;
//		else
		    return fWorkCopyP.getWorkingCopy(true);
	}
	return null ;
}

public IBuffer getDocumentBuffer () {	
	if(fWorkCopyP!=null)
		try {
			return getCompilationUnit().getBuffer();
		} catch (JavaModelException e) {}
	return null ;
}

public Object getDocumentLock() {
	return fWorkCopyP.getDocLock() ;
}

public IWorkingCopyProvider getWorkingCopyProvider() {
	return fWorkCopyP ;
}


/**
 *  If a method returns a Bean instance, this function will 
 *  return the corresponding bean part.
 */
public BeanPart getBeanReturned(String methodName) {
   return (BeanPart) fBeanReturns.get(methodName)	 ;
}
/**
 *  The method returns an instance of beanName
 */
public void addMethodReturningABean(String method,String beanName) throws CodeGenException {

   BeanPart bean = getABean(beanName) ;
   if (bean == null || method == null) throw new CodeGenException("Invalid Bean Name "+beanName) ; //$NON-NLS-1$
   
   fBeanReturns.put(method,bean);
}

/**
 *  Add a key to the methodRef, using the IMethod handle string
 */
public void addMethodInitializingABean (CodeMethodRef methodRef) {
	if (methodRef.getMethodHandle() != null)
	   fBeanInitMethod.put(methodRef.getMethodHandle(),methodRef) ;
}
/**
 *  methodRef, using the IMethod handle string 
 *  @return 
 */
public CodeMethodRef getMethodInitializingABean (String methodHandle) {
	// TODO  Need to deal with more than one bean
	return (CodeMethodRef) fBeanInitMethod.get(methodHandle) ;
}

public void removeMethodRef(CodeMethodRef mr) {
	// TODO  Need to deal with multi beans per method
	fBeanInitMethod.remove(mr.getMethodHandle()) ;
	fBeanReturns.remove(mr.getMethodName()) ;
	Iterator allBeans = getBeans().iterator();
	while(allBeans.hasNext()){
		BeanPart bean = (BeanPart)allBeans.next();
		if(bean.getInitMethod()!=null && bean.getInitMethod().equals(mr))
			bean.removeInitMethod(mr);
		if(bean.getReturnedMethod()!=null && bean.getReturnedMethod().equals(mr))
			bean.removeReturnMethod(mr);
	}
	allBeans = getBeans().iterator();
	while(allBeans.hasNext()){
		BeanPart bean = (BeanPart)allBeans.next();
		CodeExpressionRef exps[] = new CodeExpressionRef[bean.getRefExpressions().size()];
		Iterator beansExps = bean.getRefExpressions().iterator();
		int count =0;
		while(beansExps.hasNext()){
			exps[count] = (CodeExpressionRef)beansExps.next();
			count++;
		}
		if (!isStateSet(BDM_STATE_DOWN)) {
			for (count = 0; count < exps.length; count++) {
				CodeExpressionRef exp = exps[count];
				if (exp != null && exp.getCodeContent() != null && CodeGenUtil.isExactlyPresent(exp.getCodeContent(),mr.getMethodName())){
					exp.deleteFromComposition();
					exp.dispose();
				}
			}
		}
	}
}

/**
 * Returns a String that represents the value of this object.
 * @return a string representation of the receiver
 */
public String toString() {	
	
	StringBuffer sb = new StringBuffer () ;
	sb.append("BDM : [") ; //$NON-NLS-1$
	if (isStateSet(BDM_STATE_SNIPPET))
	   sb.append(" DELTA") ; //$NON-NLS-1$
	if (isStateSet(BDM_STATE_UP_AND_RUNNING))
	   sb.append (" UP") ; //$NON-NLS-1$
	if (isStateSet(BDM_STATE_UPDATING_JVE_MODEL))
	   sb.append (" UPDATING_MODEL") ; //$NON-NLS-1$
	if (isStateSet(BDM_STATE_UPDATING_DOCUMENT))
		   sb.append (" UPDATING_DOCUMENT") ; //$NON-NLS-1$   
	sb.append ("]") ; //$NON-NLS-1$
	
	return sb.toString();
}

public String resolve(String unresolved){
	return getWorkingCopyProvider().resolve(unresolved);
}

/*
 * @see IBeanDeclModel#resolveThis()
 */
public String resolveThis() {
	return getWorkingCopyProvider().resolveThis();
}

public ITypeHierarchy getClassHierarchy() {
    return getWorkingCopyProvider().getHierarchy() ;
}

protected void setState(int flag) {
	fState = flag ;
}

protected int getState() {
	return fState ;
}

public void setState(int flag, boolean state) throws CodeGenException {
	
	if (((flag & BDM_STATE_UP_AND_RUNNING)>0 && (flag & BDM_STATE_DOWN)>0) || // Up and Down
	    ((flag & BDM_STATE_UP_AND_RUNNING)>0 && (getState() & BDM_STATE_SNIPPET)>0) // Up and Snippet
	   )
	   throw new CodeGenException ("Invalid BDM state: "+flag) ; //$NON-NLS-1$
	
	if(state) {
		setState(getState() | flag);
		if ((flag & BDM_STATE_UP_AND_RUNNING)>0) // If Up, set the Down Flag.
		       setState(getState()&~BDM_STATE_DOWN) ;
		else if ((flag & BDM_STATE_DOWN)>0)
		       setState(getState()&~BDM_STATE_UP_AND_RUNNING) ;		       
	}
	else {
		setState(getState() & (~flag));
    }
}

public boolean isStateSet(int state){
	return ((getState() & state) == state);
}

private static String constructUniqueName(String methodHandle, String simpleName){
	return methodHandle+"^"+simpleName; //$NON-NLS-1$
}

public static String constructUniqueName(IMethod method, String simpleName){
	return constructUniqueName(method.getHandleIdentifier(), simpleName);
}

public static String constructUniqueName(CodeMethodRef method, String simpleName){
	return constructUniqueName(method.getMethodHandle(),simpleName);
}

public Collection getBeansInitilizedByMethod(CodeMethodRef mref) {
   // TODO  We should have a hash table for these
   if (mref == null) return null ;
   ArrayList beans = new ArrayList() ;
   Iterator itr = getBeans().iterator() ;
   while (itr.hasNext()) {
	BeanPart b = (BeanPart) itr.next();
	if (b.getInitMethod()!=null && b.getInitMethod().equals(mref)) beans.add(b) ;
   }	
   return beans ;
}	

/**
 * If a method's name has changed, so was its handle, and so would a bean's unique name
 */
public void updateBeanNameChange(BeanPart bp) {
	
	fBeansKey.values().remove(bp) ;
	fBeansKey.put(bp.getUniqueName(),bp) ;
}


	/**
	 * Returns the fStatus.
	 * @return ICodeGenStatus
	 */
	public ICodeGenStatus getFStatus() {
		return fStatus;
	}

	/**
	 * Sets the fStatus.
	 * @param fStatus The fStatus to set
	 */
	public void setFStatus(ICodeGenStatus fStatus) {
		this.fStatus = fStatus;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel#resolveSingleNameReference(String)
	 */
	public String resolveSingleNameReference(String selector, int location) {
		return ASTHelper.resolveSingleNameReference(
			selector, location,
			getWorkingCopyProvider().getDocument().get());
	}

	/**
	 * Returns the domain.
	 * @return EditDomain
	 */
	public EditDomain getDomain() {
		return fDomain;
	}

	/**
	 * Sets the domain.
	 * @param domain The domain to set
	 */
	public void setDomain(EditDomain domain) {
		fDomain = domain;
	}
		
	private void updateMethodOffset(CodeMethodRef sMethod, CodeMethodRef m, int docOff, int delta) {
		if (sMethod != null && sMethod.equals(m)) {

			// Expression encapsulated in this method
			m.updateExpressionsOffset(docOff, delta);
		} else if (m.getOffset() < docOff)
			return; // Beyond the content of this method
		else {
			m.setOffset(m.getOffset() + delta);
		}
	}
	
	public Iterator getAllMethods() {		
		ArrayList list = new ArrayList() ;
		Iterator m = getTypeRef().getMethods() ;
		while (m.hasNext())
		   list.add(m.next()) ;
		Iterator t = getEventHandlers().iterator() ;
		while (t.hasNext()) {
			CodeEventHandlerRef handler = (CodeEventHandlerRef) t.next();
			m = handler.getMethods() ;
			while (m.hasNext())
				list.add(m.next()) ;
		}	
		return list.iterator() ;		
	}
	/**
	 * The assumption here is that there is a complete, error free update of an expression
	 * from the JVE model, and the change is totally encompassed within a given method
	 *
	 * @param docOff offset of expression
	 * @param delta changes in document content
	 */
	public void driveExpressionChangedEvent(CodeMethodRef sourceMethod, int docOff, int delta) {		
		Iterator m = getAllMethods() ;
		while (m.hasNext())
		  updateMethodOffset(sourceMethod, (CodeMethodRef)m.next(), docOff, delta) ;
	}
	

	
	public CodeMethodRef getMethod(String handle) {
		Iterator itr = getAllMethods() ;
		while (itr.hasNext()) {
			CodeMethodRef m = (CodeMethodRef) itr.next() ;
			if (m.getMethodHandle().equals(handle))
			   return m ;
		}
		return null ;			
	}	

	/**
	 * When updating the BDM (top down), changes will be made to a temporary
	 * working copy, and then commited once to THE working copy.  This will avoid unecessary
	 * notification on the WC buffer changes while decoders are generating code.
	 * e.g., change from null to GridBag layout, will invoke many decoders.
	 * 
	 * @return a working copy for THE working copy
	 * 
	 * @since 1.0.0
	 */
//  The following will suport a working copy for the working copy	
//	protected ICompilationUnit getWorkingWorkingCopy() {
//		if (fworkingWC != null) return fworkingWC;
//		
//		try {
//			fworkingWC = (ICompilationUnit) fWorkCopyP.getWorkingCopy(true).getWorkingCopy(fworkingWCowner,null,null) ;
//			return (fworkingWC) ;
//		} catch (JavaModelException e) {
//			JavaVEPlugin.log(e) ;
//		}
//		
//		
//		return null ;
//	}
	/**
	 * 
	 * A call to this method will disable CodeGen's listening to changes on the CU document.
	 * This is before a top down driven change is started.  A call to docChanged() must follow
	 * when the change has completed. 
	 * 
	 * @since 1.0.0
	 */
	public synchronized void aboutTochangeDoc() {
		if (isStateSet(IBeanDeclModel.BDM_STATE_UPDATING_DOCUMENT)) 
			  return ;
		try {
			setState(IBeanDeclModel.BDM_STATE_UPDATING_DOCUMENT, true) ;
		} catch (CodeGenException e) {
			JavaVEPlugin.log(e) ;
		}
		if (fSrcSync!=null) {
			fSrcSync.suspendDocListener() ;
		}
	}
	
	public synchronized void docChanged() {
		if (isStateSet(IBeanDeclModel.BDM_STATE_UPDATING_DOCUMENT)) {
//  The following will suport a working copy for the working copy			
//			if (fworkingWC != null) {
//				try {
//					// need to commit changes made to the working copy of THE working copy
//					fworkingWC.commit(true,null) ;
//					fworkingWC.destroy() ;
//					fworkingWC=null ;
//				} catch (JavaModelException e1) {
//					JavaVEPlugin.log(e1);
//				}
//			}
			fSrcSync.resumeDocListener() ;
			try {
				setState(IBeanDeclModel.BDM_STATE_UPDATING_DOCUMENT, false) ;
			} catch (CodeGenException e) {
				JavaVEPlugin.log(e);
			}
		}
	}
	
	public void refreshMethods () {
		try {
			if (!getCompilationUnit().isConsistent()) 
				   getCompilationUnit().reconcile() ;
		}
		catch (JavaModelException e) {}
		IType mainType = CodeGenUtil.getMainType(getCompilationUnit());
		HashMap map = new HashMap() ;
		try {
			IMethod[] mtds = mainType.getMethods() ;
			for (int i = 0; i < mtds.length; i++) {
				map.put(mtds[i].getHandleIdentifier(),mtds[i]) ;
			}
			Iterator itr = getAllMethods();
			while (itr.hasNext()) {
				CodeMethodRef m = (CodeMethodRef) itr.next();
				m.refreshIMethod((IMethod)map.get(m.getMethodHandle())) ;			
			}
		} catch (JavaModelException e1) {
			JavaVEPlugin.log(e1, MsgLogger.LOG_WARNING);
		}
	}
}
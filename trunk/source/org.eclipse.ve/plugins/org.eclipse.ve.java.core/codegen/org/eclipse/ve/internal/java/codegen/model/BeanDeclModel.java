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
package org.eclipse.ve.internal.java.codegen.model;
/*
 *  $RCSfile: BeanDeclModel.java,v $
 *  $Revision: 1.28 $  $Date: 2005-09-22 22:13:16 $ 
 */

import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.java.rules.InstanceVariableCreationRule;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;



/*
 *  This class is the root for the Bean Decleration Model
 */
public class BeanDeclModel implements IBeanDeclModel {
	
	ArrayList fBeans=null ;                           // Root Beans
	HashMap   fBeanDecleration = new HashMap();
	HashMap   fBeansKey = new HashMap () ;   	    // Keep a hash key for the fBeans by unique manes
	HashMap   fBeanReturns = new HashMap() ;        // Keep a hash of Methods with return Beans ... e.g., getJButton1() ;
	HashMap	  fBeanInitMethod = new HashMap() ;     // Keep a hash to all Methods  that update beans 
	HashMap   fRefObjKey = new HashMap () ;         // Keep a hash from a EObject to Bean Part
    ArrayList fBeanDeleteCandidates = new ArrayList() ; // Hold before an Editor command is completed
	ArrayList flazyCreateList = new ArrayList();  // Beans that were added to the VE model, but not geenrated yet
	
	TypeDeclaration				fTypeDeclaration = null ;           // The class element of the JDOM model
	CodeTypeRef                 fTypeRef = null ;
	List						fEventHandlers = new ArrayList() ;  // CodeEventHandlerRef list	
	IWorkingCopyProvider		fWorkCopyP = null ;	
	JavaSourceSynchronizer      fSrcSync = null ;
	IVEModelInstance			fCompositionModel = null ;
	String                      fLineSeperator = null ;
	int                         fState = BDM_STATE_DOWN ;
	EditDomain					fDomain = null ; 
	IScannerFactory 			fScannerFactory = null;
	
/**
 * 
 * This is a temporary workaround for the fact that we do not
 * have the notion of bean processing proirity see
 * defect 256142
 * 
 */	
private boolean isPriority(BeanPart b) {
	String s = b.getType() ;
	return  (s!=null && s.equals("java.awt.GridBagConstraints"));	//$NON-NLS-1$
}

/**
 * Get all Beans
 */	
public List getBeans() {
	ArrayList v = new ArrayList() ;
	
	Iterator e = fBeansKey.values().iterator();
	while (e.hasNext()) {
	  Object o = e.next() ;	  
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
    fBeanDeleteCandidates.add(bean) ;
    
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
          fBeanDeleteCandidates.remove(bean) ;
       
       return bean ;
    } else
        return null ;
}

/**
 * An editor compound command has completed.  Those elements that have not
 * bean added back to the JVE model will be removed from the BDM and source code.
 */
public void deleteDesignatedBeans() {
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


/**
 * Return all beans initialized by this method
 */	
public BeanPart[] getBeansInializedByMethod(String methodHandle) {
	List beans = new ArrayList () ;
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
			result[i] = (BeanPart)beans.get(i) ;
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
}


/**
 *
 */
public BeanPart getABean(String name) {
   BeanPart bp = (BeanPart) fBeansKey.get(name)	 ;
   if(bp!=null && !bp.isActive())
	   bp.activate();
   return bp;
}

/**
 * It is possible that an object was entered to the VE model, but we did not generate it 
 * yet.  If we refer to it, that means it is type to create it.
 * @param obj
 * @return the prompted BeanPart
 * 
 * @since 1.1.0
 */
protected BeanPart  lazyCreateBeanInstance(EObject obj) {
	
	int idx = flazyCreateList.indexOf(obj);
	if (idx<0)
		return null;  // Not on the list of to be lazily created.
	
	if (! (obj instanceof IJavaObjectInstance))
		return null;
	
	flazyCreateList.remove(idx);
	
    BeanPartFactory bgen = new BeanPartFactory(this,getCompositionModel()) ;
    try {      
      return bgen.createFromJVEModel((IJavaObjectInstance)obj,getCompilationUnit()) ;
    }
    catch (org.eclipse.ve.internal.java.codegen.util.CodeGenException e) {
      JavaVEPlugin.log(e, Level.WARNING) ;
    }
	return null;
}

public void lazyCreateBean(IJavaObjectInstance obj) {
    if (!flazyCreateList.contains(obj))
		flazyCreateList.add(obj);
}

/**
 *
 */
public BeanPart getABean(EObject obj) {
   BeanPart result = (BeanPart) fRefObjKey.get(obj); 
   if (result==null) 
	   result= lazyCreateBeanInstance(obj);
   return result;   
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


public CodeTypeRef getTypeRef() {
	return fTypeRef ;
}
public void setTypeRef (CodeTypeRef tr) {
	fTypeRef = tr ;
}


public IVEModelInstance getCompositionModel() {
	return fCompositionModel ;
}
public void setCompositionModel(IVEModelInstance cm) {
    
    if (cm == null)
      if (fCompositionModel == null) return ;
   
	fCompositionModel = cm ;
	
	org.eclipse.ve.internal.jcm.BeanSubclassComposition c = cm.getModelRoot() ;
	
	BeanPartDecleration decl = new BeanPartDecleration("BeanSubclassComposition",""); //$NON-NLS-1$ //$NON-NLS-2$
	decl.setDeclaringMethod(null);
	BeanPart b = new BeanPart(decl) ; //$NON-NLS-1$ //$NON-NLS-2$
	
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
	   fSrcSync.disconnect() ;
	fSrcSync = null ;	   
}

public void setSourceSynchronizer(JavaSourceSynchronizer sync) {
	if (fSrcSync != null) 
	   fSrcSync.disconnect() ;
	fSrcSync = sync ;	
}


/**
 * During updates, the first call to this method will create
 * a working copy to THE working copy.
 */
public  ICompilationUnit getCompilationUnit() {
	if(fWorkCopyP!=null) {
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


public IWorkingCopyProvider getWorkingCopyProvider() {
	return fWorkCopyP ;
}


/**
 *  If a method returns a Bean instance, this function will 
 *  return the corresponding bean part.
 */
public BeanPart getBeanReturned(String methodName) {
   BeanPart bp = (BeanPart) fBeanReturns.get(methodName)	 ;
   if(bp!=null && !bp.isActive())
	   bp.activate();
   return bp;
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
 *  @return the method associated with the handle
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

public ITypeHierarchy getClassHierarchy() {
    return getWorkingCopyProvider().getHierarchy() ;
}

public TypeResolver getResolver() {
	return getWorkingCopyProvider().getResolver();
}
	
protected void setState(int flag) {
	fState = flag ;
}

protected synchronized int getState() {
	return fState ;
}

public synchronized void setState(int flag, boolean state) throws CodeGenException {
	
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
				   getCompilationUnit().reconcile(ICompilationUnit.NO_AST, false, null, null) ;
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
			JavaVEPlugin.log(e1, Level.WARNING);
		}
	}
	
	public void suspendSynchronizer() {
		fSrcSync.stallProcessing();
	}
	public void resumeSynchronizer() {
		fSrcSync.resumeProcessing();
	}
	public void addBeanDecleration (BeanPartDecleration d) {
		fBeanDecleration.put(d.getDeclerationHandle(),d);
	}
	public void removeBeanDecleration (BeanPartDecleration d) {
		fBeanDecleration.remove(d.getDeclerationHandle());
	}
	public BeanPartDecleration getModelDecleration(BeanPartDecleration d) {
		return getModelDecleration(d.getDeclerationHandle());
	}
	public BeanPartDecleration getModelDecleration(String handle) {
		return (BeanPartDecleration) fBeanDecleration.get(handle);
	}
	
	/**
	 * This method returns Unreferenced beanParts which are not <i>referenced</i>.
	 * <i>Referenced</i> beanParts are those which:
	 * <ol>
	 * 	<li> Have <code>modelled=true</code> in the overrides <b>or</b> is <code>this</code> bean.
	 * 	<li> Have <code>modelled=false</code> in the overrides but has a <code>//@jve:decl-index=0</code> for the field/variable
	 * 	<li> Have <code>modelled=false</code>, but are referenced by BeanParts in 1. or 2. <i>(parent-child or property relationships)</i>
	 * 	<li> Have <code>modelled=false</code>, but are referenced by BeanParts in 3. <i>(parent-child or property relationships)</i>
	 * </ol>
	 *  
	 *  This API should detect BeanParts which are disconnected islands and
	 *  return them for deactivation. This API should be called only 
	 *  after all the decoding of expressions has finished so that the relationships
	 *  between BeanParts is established. 
	 *  
	 *  @since 1.1
	 */
	public BeanPart[] getUnreferencedBeanParts(){
		// determine references first
		HashMap beanDependentsMap = new HashMap();
		for (Iterator unrefItr = getBeans().iterator(); unrefItr.hasNext();) {
			BeanPart bp = (BeanPart) unrefItr.next();
			Collection all = bp.getRefExpressions();
			all.addAll(bp.getNoSrcExpressions());						
			Iterator expItr = all.iterator();
			while (expItr.hasNext()) {
				CodeExpressionRef exp = (CodeExpressionRef) expItr.next();
				List refs = exp.getReferences();
				if(refs!=null && refs.size()>0){
					for (Iterator iter = refs.iterator(); iter.hasNext();) {
						EObject ref = (EObject) iter.next();
						BeanPart refBP = getABean(ref);
						if(refBP!=null){
							List bpList = (List) beanDependentsMap.get(bp);
							if(bpList==null){
								bpList = new ArrayList();
								beanDependentsMap.put(bp, bpList);
							}
							bpList.add(refBP);
						}
					}
				}
			}			
		}
		
		List unreferenced = new ArrayList(getBeans());
		List referenced = new ArrayList();
		
		for (Iterator unrefItr = unreferenced.iterator(); unrefItr.hasNext();) {
			BeanPart unrefBP = (BeanPart) unrefItr.next();
			if(referenced.contains(unrefBP))
				continue;
			boolean isReferenced = false;
			// Rule 1
			if(	(BeanPart.THIS_NAME.equals(unrefBP.getSimpleName())) || 
				(unrefBP.getEObject()!=null &&
				 InstanceVariableCreationRule.isModelled(unrefBP.getEObject().eClass(), getCompositionModel().getModelResourceSet()))){
				isReferenced = true;
			}else{
				// Rule 2 - If not isModelled() and not-referenced, check if decl-index=0 is present
				ASTNode node = unrefBP.getDecleration().getFieldDecl();
				if (node instanceof FieldDeclaration) {
					FieldDeclaration fd = (FieldDeclaration) node;
					if(AnnotationDecoderAdapter.isDeclarationParseable(fd, this))
						isReferenced = true;
				}else if(node instanceof VariableDeclarationStatement){
					VariableDeclarationStatement vds = (VariableDeclarationStatement) node;
					if(AnnotationDecoderAdapter.isDeclarationParseable(vds, this))
						isReferenced = true;
				}
				
			}
			if(isReferenced){
				referenced.add(unrefBP);
				List list = (List) beanDependentsMap.get(unrefBP);
				// Rule 3&4
				addReferencedBeans(referenced, list, beanDependentsMap);
			}
		}
		unreferenced.removeAll(referenced);
		return (BeanPart[]) unreferenced.toArray(new BeanPart[unreferenced.size()]);
	}
	/*
	 * Recursively adds referenced beans to the referencedList.
	 */
	private void addReferencedBeans(List referencedList, List currentReferences, HashMap beanDependentsMap) {
		if(currentReferences==null || currentReferences.size()<1)
			return;
		for (Iterator iter = currentReferences.iterator(); iter.hasNext();) {
			BeanPart bp = (BeanPart) iter.next();
			if(referencedList.contains(bp))
				continue;
			referencedList.add(bp);
			// Rule 3
			List list = (List) beanDependentsMap.get(bp);
			addReferencedBeans(referencedList, list, beanDependentsMap);
		}
	}

	public IScannerFactory getScannerFactory() {
		if(fScannerFactory==null)
			setScannerFactory(new DefaultScannerFactory());
		return fScannerFactory;
	}

	public void setScannerFactory(IScannerFactory scannerFactory) {
		this.fScannerFactory = scannerFactory;
	}
}

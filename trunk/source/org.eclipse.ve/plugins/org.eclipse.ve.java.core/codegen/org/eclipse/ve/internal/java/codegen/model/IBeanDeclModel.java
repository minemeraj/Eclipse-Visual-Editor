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
 *  $RCSfile: IBeanDeclModel.java,v $
 *  $Revision: 1.7 $  $Date: 2004-04-07 22:48:31 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.*;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.ITypeResolver;
import org.eclipse.ve.internal.java.codegen.java.JavaSourceSynchronizer;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.IWorkingCopyProvider;

public interface IBeanDeclModel extends ITypeResolver{
	
	public final  int           BDM_STATE_DOWN            		= 0x01 ;	
	public final  int           BDM_STATE_UP_AND_RUNNING  		= 0x02 ;
	public final  int           BDM_STATE_UPDATING_JVE_MODEL	= 0x04 ;
	public final  int           BDM_STATE_SNIPPET     			= 0x08 ;
	public final  int           BDM_STATE_UPDATING_DOCUMENT		= 0x10 ;

	
	public List getBeans() ;
	public void addBean (BeanPart bean) ;
	public void addMethodReturningABean(String method,String beanName) throws CodeGenException ;
	public BeanPart getABean(String name) ;
	public BeanPart getABean(EObject obj) ;	
	public BeanPart getBeanReturned(String methodName) ;
	public TypeDeclaration getTypeDecleration()  ;
	public void setTypeDecleration(TypeDeclaration decl)  ;
	public IVEModelInstance getCompositionModel() ;
	public void setCompositionModel(IVEModelInstance cm) ;	
	public ICompilationUnit getCompilationUnit() ;
	public IBuffer getDocumentBuffer() ;
	/**
	 * @deprecated
	 */
	public Object getDocumentLock() ;	
	public void addMethodInitializingABean (CodeMethodRef methodRef) ;
	public CodeMethodRef getMethodInitializingABean (String methodHandle) ;
	public CodeTypeRef getTypeRef() ;
	public void setTypeRef (CodeTypeRef tr) ;
    public BeanPart[] getBeansInializedByMethod(String methodHandle) ;
    public void removeBeanFromRoot (BeanPart bean) ;
    public void removeBean (BeanPart bean) ;
    public void removeMethodRef(CodeMethodRef mr) ;
    public void UpdateRefObjKey(BeanPart bean,EObject prev) ;    
    public void setLineSeperator(String sep) ;
    public String getLineSeperator() ;
    public void setWorkingCopyProvider(IWorkingCopyProvider wcp) ;
	public IWorkingCopyProvider getWorkingCopyProvider() ;
	public void setState(int flag, boolean state) throws CodeGenException ;
    public boolean isStateSet(int state) ;
    public void dispose() ;
    public void setSourceSynchronizer(JavaSourceSynchronizer sync) ;
    public void designateAsDelete(BeanPart bean) ;    
    public BeanPart getDeleteDesignated(IJavaObjectInstance obj, boolean remove) ;
    public void deleteDesignatedBeans() ;
    public List getRootBeans() ;
    public ITypeHierarchy getClassHierarchy()  ;
    public Collection getBeansInitilizedByMethod(CodeMethodRef mref) ;
    public void updateBeanNameChange(BeanPart bp) ;
	public EditDomain getDomain() ;
	public List getEventHandlers() ;
	public void driveExpressionChangedEvent(CodeMethodRef sourceMethod, int docOff, int delta) ;
	public CodeMethodRef getMethod(String handle) ;
	public Iterator getAllMethods() ;
	
	/**
	 * A CodeExpressionRef will call this API to fire an about to change Text when
	 * a decoder is about to generate new text.  A working copy for the working copy will
	 * be created, and CodeGen will stop listening to document changes.
	 * @since 1.0.0
	 */
	public  void aboutTochangeDoc();
	/**
	 * At the end of a transaction (commit), this API will be calld to denote that 
	 * all CodeExpresseionRef have finished to update the source code.  At this point the working
	 * copy will be commited, and CodeGen will resume to listen to document changes.
	 * @since 1.0.0
	 */
	public  void docChanged() ;	
	/**
	 * Will refresh all methods offsets/content 
	 * This will be called when a new field/method is being create.
	 * 
	 * @since 1.0.0
	 */
	public void refreshMethods () ;
	
	public void suspendSynchronizer() ;
	public void resumeSynchronizer() ;
	
}
      
	

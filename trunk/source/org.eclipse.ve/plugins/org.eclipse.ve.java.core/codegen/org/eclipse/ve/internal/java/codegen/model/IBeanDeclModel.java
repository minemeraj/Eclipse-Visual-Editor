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
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jface.text.IDocument;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.codegen.core.ICodeGenStatus;
import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.java.ITypeResolver;
import org.eclipse.ve.internal.java.codegen.java.JavaSourceSynchronizer;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.IWorkingCopyProvider;

public interface IBeanDeclModel extends ITypeResolver{
	
	public final  int           BDM_STATE_DOWN            	= 0x01 ;	
	public final  int           BDM_STATE_UP_AND_RUNNING  	= 0x02 ;
	public final  int           BDM_STATE_UPDATING_JVE_MODEL	= 0x04 ;
	public final  int           BDM_STATE_SNIPPET     		= 0x08 ;
	public final  int           BDM_STATE_UPDATING_DOCUMENT	= 0x10 ;
	
	public List getBeans() ;
	public void addBean (BeanPart bean) ;
	public void addMethodReturningABean(String method,String beanName) throws CodeGenException ;
	public BeanPart getABean(String name) ;
	public BeanPart getABean(EObject obj) ;	
	public BeanPart getBeanReturned(String methodName) ;
	public TypeDeclaration getTypeDecleration()  ;
	public void setTypeDecleration(TypeDeclaration decl)  ;
	public void setJDOM(CompilationUnitDeclaration dom)  ;
	public CompilationUnitDeclaration getJDOM() ;
	public IDiagramModelInstance getCompositionModel() ;
	public void setCompositionModel(IDiagramModelInstance cm) ;	
	public ICompilationUnit getCompilationUnit() ;
	public IDocument getDocument() ;	
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
    public String resolveSingleNameReference(String selector, int location);
    public void setLineSeperator(String sep) ;
    public String getLineSeperator() ;
    public void setWorkingCopyProvider(IWorkingCopyProvider wcp) ;
	public IWorkingCopyProvider getWorkingCopyProvider() ;
	public void updateJavaSource (String elementHandle) ;
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
	public ICodeGenStatus getFStatus() ;
	public void setFStatus(ICodeGenStatus fStatus) ;
	public EditDomain getDomain() ;
	public List getEventHandlers() ;
	public void driveExpressionChangedEvent(CodeMethodRef sourceMethod, int docOff, int delta) ;
	public CodeMethodRef getMethod(String handle) ;
	public Iterator getAllMethods() ;
	
}
      
	

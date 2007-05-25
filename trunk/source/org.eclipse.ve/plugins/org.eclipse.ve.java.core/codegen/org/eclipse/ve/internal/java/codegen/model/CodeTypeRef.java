/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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
 *  $RCSfile: CodeTypeRef.java,v $
 *  $Revision: 1.11 $  $Date: 2007-05-25 04:18:47 $ 
 */


import java.util.Iterator;
import java.util.Vector;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import org.eclipse.ve.internal.jcm.BeanSubclassComposition;

import org.eclipse.ve.internal.java.codegen.java.JavaBeanModelBuilder;
import org.eclipse.ve.internal.java.core.TypeResolver.ResolvedType;


public class CodeTypeRef {

protected   TypeDeclaration			fdeclType=null ;
protected   IType					fType = null ;
protected   ICompilationUnit		fCU = null ;
protected   String					fName ;
protected   IBeanDeclModel			fBeanModel ;
protected   Vector<CodeMethodRef>	fMethods = new Vector<CodeMethodRef> () ;
protected   BeanSubclassComposition	fbeanComposition = null ;
	
public CodeTypeRef (String typeName, IBeanDeclModel model){
	fName = typeName;
	fBeanModel = model;
	if (model.getCompositionModel() != null)
	   fbeanComposition = model.getCompositionModel().getModelRoot() ;
}

public CodeTypeRef (TypeDeclaration declType, IBeanDeclModel model) {
	fdeclType = declType ;
	ResolvedType resolveType = model.getResolver().resolveType(declType.getName());
	if (resolveType!=null)
	   fName = resolveType.getName();
	else
	   fName = declType.getName().getFullyQualifiedName(); //TODO none resolved class... do we still want to model this 
	fBeanModel = model ;
	fbeanComposition = model.getCompositionModel().getModelRoot() ;
}	


public TypeDeclaration getTypeDecl() {
	return fdeclType ;
}

/**
 * Returns a String that represents the value of this object.
 * @return a string representation of the receiver
 */
public String toString() {
	return super.toString() + ":" + fName ; //$NON-NLS-1$
}

public IBeanDeclModel getBeanModel() {
	return fBeanModel ;
}

public void  addRefMethod(CodeMethodRef  method) {
	if (!fMethods.contains(method)) {
		fMethods.add(method) ;
	}
}

public void removeRefMethod (CodeMethodRef method) {
	fMethods.remove(method) ;
}

public Iterator<CodeMethodRef> getMethods() {
	return fMethods.iterator() ;
}

public String getName(){
	return fName;
}

public String getSimpleName() {
	if (fName.indexOf('$')>=0)
		   return fName.substring(fName.indexOf('$')+1);
		else
		   return fName;
}

public void dispose() {
	if (fMethods != null) {
		Object[] tmp = fMethods.toArray() ;
		for (int i=0; i<tmp.length; i++) {
			((CodeMethodRef)tmp[i]).dispose() ;
		}
	}
	fMethods.clear() ;
}

/**
 * Returns the beanComposition.
 * @return BeanSubclassComposition
 */
public BeanSubclassComposition getBeanComposition() {
	return fbeanComposition;
}

public boolean refresh(CodeTypeRef typeRef){
	if(typeRef==null)
		return false;
	if(typeRef.getTypeDecl()!=null){
		if(fdeclType!=null && fdeclType.properties()!=null && fdeclType.properties().containsKey(JavaBeanModelBuilder.ASTNODE_SOURCE_PROPERTY))
			fdeclType.setProperty(JavaBeanModelBuilder.ASTNODE_SOURCE_PROPERTY, null); //clear old source
		fdeclType = typeRef.getTypeDecl();
	}
	return true;
}
}



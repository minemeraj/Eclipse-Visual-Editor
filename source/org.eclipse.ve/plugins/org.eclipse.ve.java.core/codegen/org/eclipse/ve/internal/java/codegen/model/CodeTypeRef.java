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
 *  $RCSfile: CodeTypeRef.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */


import java.util.Iterator;
import java.util.Vector;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;

import org.eclipse.ve.internal.jcm.BeanSubclassComposition;


public class CodeTypeRef {

protected   CompilationUnitDeclaration  fdeclCU = null ;
protected   TypeDeclaration				fdeclType=null ;
protected   IType							fType = null ;
protected   ICompilationUnit				fCU = null ;
protected   String						fName ;
protected   IBeanDeclModel              	fBeanModel ;
protected   Vector                      	fMethods = new Vector () ;
protected   BeanSubclassComposition		fbeanComposition = null ;
	
public CodeTypeRef (String typeName, IBeanDeclModel model){
	fName = typeName;
	fBeanModel = model;
	if (model.getCompositionModel() != null)
	   fbeanComposition = model.getCompositionModel().getModelRoot() ;
}

public CodeTypeRef (TypeDeclaration declType, IBeanDeclModel model) {
	fdeclType = declType ;
	fName = new String (declType.name) ;	
	fBeanModel = model ;
	fbeanComposition = model.getCompositionModel().getModelRoot() ;
}	

public CodeTypeRef (CompilationUnitDeclaration declCU, IBeanDeclModel model) {
    this(declCU.types[0],model) ;	
	fdeclCU = declCU ;
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

public Iterator getMethods() {
	return fMethods.iterator() ;
}

public String getName(){
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

}



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
package org.eclipse.ve.internal.java.codegen.java;
/*
 *  $RCSfile: CodeSnippetModelBuilder.java,v $
 *  $Revision: 1.15 $  $Date: 2005-12-18 16:25:58 $ 
 */

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.codegen.java.rules.IVisitorFactoryRule;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.IWorkingCopyProvider;

public class CodeSnippetModelBuilder  extends JavaBeanShadowModelBuilder {


protected String contents;
protected int[] importStarts;
protected int[] importEnds;
protected int[] fieldStarts;
protected int[] fieldEnds;
protected int[] methodStarts;
protected int[] methodEnds;
protected String[] methodHandles;
protected IScannerFactory scannerFactory;


public CodeSnippetModelBuilder(EditDomain d, IWorkingCopyProvider wcp, String contents, String[] methodHandles, int[] importStarts, int[] importEnds, int[] fieldStarts, int[] fieldEnds, int[] methodStarts, int[] methodEnds, ICompilationUnit referenceCU, IProgressMonitor monitor, IScannerFactory scannerFactory){
	super(d,"CodeSnippetClass_2",null, monitor); //$NON-NLS-1$
	this.referenceCU = referenceCU;
	this.contents = contents;
	this.importStarts=importStarts;
	this.importEnds=importEnds;
	this.fieldStarts=fieldStarts;
	this.fieldEnds=fieldEnds;
	this.methodStarts=methodStarts;
	this.methodEnds=methodEnds;
	this.methodHandles=new String[methodHandles.length+1];
	this.methodHandles = methodHandles;
	this.fWCP = createPseudoWorkingCopyProvider(referenceCU, wcp.getResolver()) ;
	this.scannerFactory = scannerFactory;
}



protected char[] getFileContents() throws CodeGenException {
	return contents.toCharArray();
}

/**
 * JDT and old AST differ in the way they look at things like comments of elements etc.
 * Hence have to adjust the AST such that it reflects the JDT - since the BDM is based 
 * off the JDT. 
 * Examples of such behaviour are when you have two comment blocks before a method -
 * AST says only the bottomost comment block belongs to the method, while JDT says all
 * comment blocks belong to that method.   
 * 
 * @param decl
 */
protected void updateModelPositions(CompilationUnit decl){
	if(decl.types()!=null && decl.types().size()>0){
		TypeDeclaration type = (TypeDeclaration) decl.types().get(0);
		FieldDeclaration[] decls = type.getFields();
		MethodDeclaration[] methods = type.getMethods();
		if(decl!=null && fieldStarts!=null && decls != null && decls.length==fieldStarts.length){ 
			// liner mapping.. no problems.
			for(int dc=0;dc<decls.length;dc++){
				if(fieldStarts[dc]!=0 || fieldEnds[dc]!=0) // Sometimes the snapshot is not reconciled
					decls[dc].setSourceRange(fieldStarts[dc], fieldEnds[dc]-fieldStarts[dc]+1);
			}
		}
		if(methods!=null && methodStarts!=null){
			int usefulMethodIndex = 0;
			for(int mc=0;mc<methods.length;mc++){
				if(!methods[mc].isConstructor()) {
							if(methodStarts[usefulMethodIndex]!=0 || methodEnds[usefulMethodIndex]!=0) // Sometimes the snapshot is not reconciled
								methods[mc].setSourceRange(methodStarts[usefulMethodIndex], methodEnds[usefulMethodIndex]-methodStarts[usefulMethodIndex]+1);
							usefulMethodIndex++;
				}
			}
		}
	}
}

protected void visitType(TypeDeclaration type, IBeanDeclModel model,  JavaElementInfo[] mthds, List tryAgain, IProgressMonitor pm, IVisitorFactoryRule visitorFactoryRule){
	TypeVisitor visitor = visitorFactoryRule.getTypeVisitor();
	visitor.initialize(type,model,fFileContent, methodHandles,tryAgain,true, visitorFactoryRule);
	visitor.setProgressMonitor(pm);
	visitor.visit()  ;
}


/**
 * JDT and old AST differ in the way they look at things like comments of elements etc.
 * Hence have to adjust the AST such that it reflects the JDT - since the BDM is based 
 * off the JDT. 
 * Examples of such behaviour are when you have two comment blocks before a method -
 * AST says only the bottomost comment block belongs to the method, while JDT says all
 * comment blocks belong to that method.   
 *
 * @see org.eclipse.ve.internal.java.codegen.java.JavaBeanModelBuilder#ParseJavaCode()
 */
protected CompilationUnit ParseJavaCode(IProgressMonitor pm) throws CodeGenException {
	CompilationUnit cu = super.ParseJavaCode(pm);
	updateModelPositions(cu);
	return cu;
}

protected void CreateBeanDeclModel() throws CodeGenException {
	super.CreateBeanDeclModel();
	fModel.setScannerFactory(scannerFactory);
}
}

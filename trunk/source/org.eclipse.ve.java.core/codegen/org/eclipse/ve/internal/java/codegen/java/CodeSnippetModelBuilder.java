package org.eclipse.ve.internal.java.codegen.java;
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
 *  $RCSfile: CodeSnippetModelBuilder.java,v $
 *  $Revision: 1.3 $  $Date: 2004-03-05 23:18:38 $ 
 */

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;

public class CodeSnippetModelBuilder  extends JavaBeanShadowModelBuilder {


protected String contents;
protected int[] importStarts;
protected int[] importEnds;
protected int[] fieldStarts;
protected int[] fieldEnds;
protected int[] methodStarts;
protected int[] methodEnds;
protected String[] methodHandles;


public CodeSnippetModelBuilder(EditDomain d, String contents, String[] methodHandles, int[] importStarts, int[] importEnds, int[] fieldStarts, int[] fieldEnds, int[] methodStarts, int[] methodEnds, ICompilationUnit referenceCU){
	super(d,"CodeSnippetClass_2",null); //$NON-NLS-1$
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
	this.fWCP = createPseudoWorkingCopyProvider(referenceCU) ;
}



protected char[] getFileContents() throws CodeGenException {
	return contents.toCharArray();
}

//g public CompilationUnitDeclaration getModelFromParser(
//	ProblemReporter reporter,
//	CompilationResult result,
//	BasicCompilationUnit cu){
//	CompilationUnitDeclaration decl = super.getModelFromParser(reporter, result, cu);
//	updateModelPositions(decl);
//	return decl;
//}

/**
 * JDT and old AST differ in the way they look at things like comments of elements etc.
 * Hence have to adjust the AST such that it reflects the JDT - since the BDM is based 
 * off the JDT. 
 * Examples of such behaviour are when you have two comment blocks before a method -
 * AST says only the bottomost comment block belongs to the method, while JDT says all
 * comment blocks belong to that method.   
 * 
 * @param decl
 */protected void updateModelPositions(CompilationUnit decl){
//g	TypeDeclaration type = decl.types[0];
//	FieldDeclaration[] decls = type.fields;
//	AbstractMethodDeclaration[] methods = type.methods;
//	if(decl!=null && fieldStarts!=null && decls != null && decls.length==fieldStarts.length){ 
//		// liner mapping.. no problems.
//		for(int dc=0;dc<decls.length;dc++){
//			decls[dc].sourceStart = fieldStarts[dc];
//			decls[dc].sourceEnd = fieldEnds[dc];
//		}
//	}
//	if(methods!=null && methodStarts!=null){
//		int usefulMethodIndex = 0;
//		for(int mc=0;mc<methods.length;mc++){
//			if(	methods[mc]!=null && 
//					// Since Methods are the only things we care about
//					methods[mc] instanceof MethodDeclaration) {
//				methods[mc].declarationSourceStart=methodStarts[usefulMethodIndex];
//				methods[mc].declarationSourceEnd=methodEnds[usefulMethodIndex];
//				usefulMethodIndex++;
//			}
//		}
//	}
}

protected void visitType(TypeDeclaration type, IBeanDeclModel model,  List tryAgain){
	new TypeVisitor(type,model,fFileContent, methodHandles,tryAgain,true).visit()  ;
}


}
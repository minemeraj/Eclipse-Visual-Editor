/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $Revision: 1.9 $  $Date: 2005-02-15 23:28:34 $ 
 */

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.model.JavaElementInfo;
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


public CodeSnippetModelBuilder(EditDomain d, IWorkingCopyProvider wcp, String contents, String[] methodHandles, int[] importStarts, int[] importEnds, int[] fieldStarts, int[] fieldEnds, int[] methodStarts, int[] methodEnds, ICompilationUnit referenceCU){
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
	this.fWCP = createPseudoWorkingCopyProvider(referenceCU, wcp.getResolver()) ;
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
				decls[dc].setSourceRange(fieldStarts[dc], fieldEnds[dc]-fieldStarts[dc]+1);
			}
		}
		if(methods!=null && methodStarts!=null){
			int usefulMethodIndex = 0;
			for(int mc=0;mc<methods.length;mc++){
				if(	methods[mc]!=null && 
						// Since Methods are the only things we care about
						methods[mc] instanceof MethodDeclaration &&
						!((MethodDeclaration)methods[mc]).isConstructor()) {
							methods[mc].setSourceRange(methodStarts[usefulMethodIndex], methodEnds[usefulMethodIndex]-methodStarts[usefulMethodIndex]+1);
							usefulMethodIndex++;
				}
			}
		}
	}
}

protected void visitType(TypeDeclaration type, IBeanDeclModel model,  JavaElementInfo[] mthds, List tryAgain, IProgressMonitor pm){
	TypeVisitor visitor = new TypeVisitor(type,model,fFileContent, methodHandles,tryAgain,true);
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

}

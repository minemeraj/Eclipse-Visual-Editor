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
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.core.BasicCompilationUnit;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;

public class CodeSnippetModelBuilder 
	extends JavaBeanShadowModelBuilder {


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

public CompilationUnitDeclaration getModelFromParser(
	ProblemReporter reporter,
	CompilationResult result,
	BasicCompilationUnit cu){
	CompilationUnitDeclaration decl = super.getModelFromParser(reporter, result, cu);
	updateModelPositions(decl);
	return decl;
}

/** 
 * Now the JDOM has different offsets and lengths, so 
 * set them to how we like.
 * Ex: When you have multiple comment blocks prefixing 
 * the method declaration, then the JDOM takes only the 
 * last comment block, whereas JDT takes in all the 
 * multiple comment blocks. Since the BDM is based on
 * the JDT, we adjust the JDOM's offsets.
 */
protected void updateModelPositions(CompilationUnitDeclaration decl){
	TypeDeclaration type = decl.types[0];
	FieldDeclaration[] decls = type.fields;
	AbstractMethodDeclaration[] methods = type.methods;
	if(decl!=null && fieldStarts!=null && decls != null && decls.length==fieldStarts.length){ 
		// liner mapping.. no problems.
		for(int dc=0;dc<decls.length;dc++){
			decls[dc].sourceStart = fieldStarts[dc];
			decls[dc].sourceEnd = fieldEnds[dc];
		}
	}
	if(methods!=null && methodStarts!=null){
		int count = 0;
		for(int mc=1;mc<methods.length;mc++){
			if(methods[mc].isConstructor())
				continue;
			if(methods[mc].isClinit())
				continue;
			if(methods[mc]!=null){
				methods[mc].declarationSourceStart=methodStarts[count];
				methods[mc].declarationSourceEnd=methodEnds[count];
				count++;
			}
		}
	}
}

protected void visitType(TypeDeclaration type, IBeanDeclModel model, char[] content, List tryAgain){
	new TypeVisitor(type,model,content,methodHandles,tryAgain,true).visit()  ;
}


}
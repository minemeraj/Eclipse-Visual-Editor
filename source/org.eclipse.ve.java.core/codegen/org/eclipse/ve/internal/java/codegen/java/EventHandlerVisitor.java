/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EventHandlerVisitor.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.ArrayList;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jem.internal.core.MsgLogger;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.codegen.model.CodeEventHandlerRef;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;

/**
 * @author Gili Mendel
 *
 */
public class EventHandlerVisitor extends TypeVisitor {

public EventHandlerVisitor (TypeDeclaration node, IBeanDeclModel model, boolean forceJDOMUsage) {
	super(new CodeEventHandlerRef(node,model), node,model,new ArrayList(), forceJDOMUsage) ;	
	fModel.getEventHandlers().add(fType) ;
}

public void visit()  {

	AbstractMethodDeclaration[] methods = fType.getTypeDecl().methods;
	if (forceJDOMUsage) {
//.. have not done anything for snippet here... need to verify
//		
		// No compilation unit... depend on jdom solely
		if(methods==null || methods.length==0)
			return;
		try{
			int methodHandleUseCount = 0;
			for(int i=0; i<methods.length; i++){
				if(	(methods[i]!=null) &&
					(methods[i] instanceof MethodDeclaration || 
					 methods[i] instanceof ConstructorDeclaration)){
					String thisMethodHandle = ""; //$NON-NLS-1$
					if (methods[i] instanceof MethodDeclaration) {
						thisMethodHandle = methodHandles[methodHandleUseCount];
						methodHandleUseCount++;						
					}

   					
					// Please see CodeSnippetModelBuilder.updateMethodOffsets() to see how
					// the declaration Source starts, and the declaration source ends are 
					// being modified. This is due to inconsistency between JDOM and JDT.
					new EventMethodCallBackVisitor( methods[i], fModel, fType, thisMethodHandle, // null,
								 getSourceRange(methods[i].declarationSourceStart,methods[i].declarationSourceEnd), 
								 new String(content).substring(methods[i].declarationSourceStart, methods[i].declarationSourceEnd)).
								 visit();
				}
			}
		}catch(Exception e){
			JavaVEPlugin.log(e, MsgLogger.LOG_WARNING) ;
		}
	}else{
		IMethod cuMethods[] = getCUMethods(methods, CodeGenUtil.getMethods(fModel.getCompilationUnit(), fType.getName()), fModel);
		if(cuMethods==null || cuMethods.length<1)
			return;
		// Compilation unit methods and jdom methods should match.
		if (cuMethods.length != methods.length) 
			throw new RuntimeException("methods length error") ; //$NON-NLS-1$
		int i=0 ;
		try {
			for (; i < methods.length ; i++){
				// Visit each method with the correct visitor
				if ( cuMethods[i] != null && 
					(methods[i] instanceof MethodDeclaration ||
					 methods[i] instanceof ConstructorDeclaration)) {
					new EventMethodCallBackVisitor(methods[i],fModel,fType,((IMethod)cuMethods[i]).getHandleIdentifier(),
								cuMethods[i].getSourceRange(),
								cuMethods[i].getSource()).visit();
				}
			}
		}catch (JavaModelException e) {
			JavaVEPlugin.log ("EventHandlerVisitor.visit() could not visit"+String.valueOf(methods[i].selector)+" : "+e.getMessage(), MsgLogger.LOG_WARNING) ; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
}


}

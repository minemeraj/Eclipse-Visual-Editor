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
 *  $Revision: 1.6 $  $Date: 2004-06-02 15:57:22 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.ArrayList;
import java.util.logging.Level;

import org.eclipse.jdt.core.dom.*;

import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.model.CodeEventHandlerRef;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

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

	MethodDeclaration[] methods = fType.getTypeDecl().getMethods();
	if (forceJDOMUsage) {
//TODO.. have not done anything for snippet here... need to verify
//		
		// No compilation unit... depend on jdom solely
		if(methods==null || methods.length==0)
			return;
		try{
			int methodHandleUseCount = 0;
			for(int i=0; i<methods.length; i++){
				if(	(methods[i]!=null) &&
					 methods[i] instanceof MethodDeclaration ){
					String thisMethodHandle = ""; //$NON-NLS-1$
					if (methods[i] instanceof MethodDeclaration) {
						thisMethodHandle = methodHandles[methodHandleUseCount];
						methodHandleUseCount++;						
					}

   					
					// Please see CodeSnippetModelBuilder.updateMethodOffsets() to see how
					// the declaration Source starts, and the declaration source ends are 
					// being modified. This is due to inconsistency between JDOM and JDT.
//					
					new EventMethodCallBackVisitor( methods[i], fModel, fType, thisMethodHandle, // null,
								 getSourceRange(methods[i].getStartPosition(),methods[i].getStartPosition()+methods[i].getLength()),
						         String.copyValueOf(content,methods[i].getStartPosition(),methods[i].getLength())).  
								 visit();								 
				}
			}
		}catch(Exception e){
			JavaVEPlugin.log(e, Level.WARNING) ;
		}
	}else{
		JavaElementInfo[] cuMethods = getCUMethods(methods, CodeGenUtil.getMethodsInfo(fModel.getCompilationUnit(), fType.getSimpleName()), fModel);
		if(cuMethods==null || cuMethods.length<1)
			return;
		// Compilation unit methods and jdom methods should match.
		if (cuMethods.length != methods.length) 
			throw new RuntimeException("methods length error") ; //$NON-NLS-1$
		int i=0 ;		
			for (; i < methods.length ; i++){
				// Visit each method with the correct visitor
				if ( cuMethods[i] != null && 
					 methods[i] instanceof MethodDeclaration) {
					new EventMethodCallBackVisitor(methods[i],fModel,fType,cuMethods[i].getHandle(),
								cuMethods[i].getSourceRange(),
								cuMethods[i].getContent()).visit();
				}
			}		
	}
}


}

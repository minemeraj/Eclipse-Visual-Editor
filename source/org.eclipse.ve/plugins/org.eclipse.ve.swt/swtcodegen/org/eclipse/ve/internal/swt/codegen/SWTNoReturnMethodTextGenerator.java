/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: SWTNoReturnMethodTextGenerator.java,v $
 *  $Revision: 1.1 $  $Date: 2004-04-29 21:06:36 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.ParseTreeAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.model.CodeMethodRef;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.IMethodTemplate;

 
/**
 * This method generator will add the parent argument when initializing 
 * the init bean
 * 
 * @author Gili Mendel
 * @since 1.0.0
 */
public class SWTNoReturnMethodTextGenerator extends NoReturnNoArgMethodTextGenerator {

	/**
	 * @param component
	 * @param model
	 * 
	 * @since 1.0.0
	 */
	public SWTNoReturnMethodTextGenerator(EObject component, IBeanDeclModel model) {
		super(component, model);		
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IMethodTextGenerator#generateMethod(org.eclipse.ve.internal.java.codegen.model.CodeMethodRef, java.lang.String, java.lang.String)
	 */
	public String generateMethod(CodeMethodRef method, String methodName, String beanName) throws CodeGenException {
		fmethodName = methodName ;
		finitbeanName = beanName ;
		
		IJavaObjectInstance obj = (IJavaObjectInstance)fComponent ;
		
		IMethodTemplate template = getMethodTemplate() ;
		// Set the parent/flags as a constructor argument if needed
		JavaAllocation alloc = obj.getAllocation() ;
		if (alloc != null && alloc instanceof ParseTreeAllocation) {
			ParseTreeAllocation pt = (ParseTreeAllocation) alloc ;
			PTExpression exp =  pt.getExpression() ;
			// TODO: this is a temporary hack until we model emf referenced PTEs
			if (exp instanceof PTClassInstanceCreation) {
				PTClassInstanceCreation t = (PTClassInstanceCreation) exp ;
				PTExpression arg0 = (PTExpression)t.getArguments().get(0) ;
				if (arg0 instanceof PTInstanceReference) {
					IJavaObjectInstance parent =  ((PTInstanceReference)arg0).getObject() ;
					PTExpression arg1 = (PTExpression) t.getArguments().get(1) ;
					if (arg1 instanceof PTName) {
						String flags = ((PTName)arg1).getName() ;
						BeanPart b = fModel.getABean(parent) ;
						setBeanInitArgs(new String[] {b.getSimpleName(),flags}) ;						
					}
				}
			}			
		}
		return template.generateMethod(getInfo()) ;				
	}
}

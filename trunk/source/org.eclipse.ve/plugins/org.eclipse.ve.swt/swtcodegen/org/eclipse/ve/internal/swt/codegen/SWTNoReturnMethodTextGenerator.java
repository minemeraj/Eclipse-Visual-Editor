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
 *  $Revision: 1.2 $  $Date: 2004-05-08 01:19:05 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.codegen.model.CodeMethodRef;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.util.*;

 
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

	
//	/* (non-Javadoc)
//	 * @see org.eclipse.ve.internal.java.codegen.java.IMethodTextGenerator#generateMethod(org.eclipse.ve.internal.java.codegen.model.CodeMethodRef, java.lang.String, java.lang.String)
//	 */
//	public String generateMethod(CodeMethodRef method, String methodName, String beanName) throws CodeGenException {
//		fmethodName = methodName ;
//		finitbeanName = beanName ;
//		
//		IJavaObjectInstance obj = (IJavaObjectInstance)fComponent ;
//		setBeanInitString(CodeGenUtil.getInitString(obj, fModel));		
//		IMethodTemplate template = getMethodTemplate() ;
//		return template.generateMethod(getInfo()) ;				
//	}
}

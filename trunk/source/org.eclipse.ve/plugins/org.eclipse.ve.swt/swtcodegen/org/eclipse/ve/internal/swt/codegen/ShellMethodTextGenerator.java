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
 *  $RCSfile: ShellMethodTextGenerator.java,v $
 *  $Revision: 1.1 $  $Date: 2004-01-23 21:04:12 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.ve.internal.java.codegen.java.AbstractMethodTextGenerator;
import org.eclipse.ve.internal.java.codegen.model.CodeMethodRef;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.IMethodTemplate;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public class ShellMethodTextGenerator extends AbstractMethodTextGenerator {

	public static final String METHOD_TEMPLATE_CLASS_NAME = "ShellMethodTemplate" ; //$NON-NLS-1$
	public static final String METHOD_TEMPLATE_NAME = METHOD_TEMPLATE_CLASS_NAME+JAVAJET_EXT ;
	
	AbstractMethodTextGenerator.MethodInfo fInfo = null ;

	/**
	 * Shell has no return type
	 **/	
	protected AbstractMethodTextGenerator.MethodInfo getInfo() {
	   if (fInfo != null) return fInfo ;
       fInfo = new AbstractMethodTextGenerator.MethodInfo(false) ;
       return fInfo ;
	}
		
	public ShellMethodTextGenerator(EObject component, IBeanDeclModel model) {
		super(component,model) ;
	}
	
	
	protected IMethodTemplate getMethodTemplate() {
        return getMethodTemplate(METHOD_TEMPLATE_NAME,METHOD_TEMPLATE_CLASS_NAME) ;
	}
	
	

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IMethodTextGenerator#generateExpressionsContent()
	 */
	public void generateExpressionsContent() throws CodeGenException {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IMethodTextGenerator#generateInLine(org.eclipse.ve.internal.java.codegen.model.CodeMethodRef, java.lang.String, java.util.List)
	 */
	public String generateInLine(CodeMethodRef method, String beanName, List kids) throws CodeGenException {
		// TODO Auto-generated method stub
		return null;
	}


}

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
 *  $RCSfile: IMethodTextGenerator.java,v $
 *  $Revision: 1.1 $  $Date: 2004-01-23 21:04:08 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.List;

import org.eclipse.ve.internal.java.codegen.model.CodeMethodRef;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public interface IMethodTextGenerator {

	public static final String  DEFAULT_METHOD_COMMENT =    "This method initializes " ; //$NON-NLS-1$

	/**
	 * 
	 * @param method the method to generate the source for
	 * @param methodName
	 * @param beanName  
	 * @return String containing the source snippet for the method
	 * @throws CodeGenException
	 * 
	 * @since 1.0.0
	 */
	public String generateMethod(CodeMethodRef method,String methodName,String beanName) throws CodeGenException ;
	
	/**
	 * 
	 * @param method
	 * @param beanName
	 * @param kids referenced IJavaObjects that are owned by this bean
	 * @return
	 * @throws CodeGenException
	 * 
	 * @since 1.0.0
	 */
	public String generateInLine(CodeMethodRef method,String beanName, List kids) throws CodeGenException ;
	
	/**
	 * This method will look/generate Expression meta objects for all settings for a given
	 * bean.
	 */
	public void generateExpressionsContent() throws CodeGenException ;
	
	/**
	 * JavaDoc like comments
	 * @param comments
	 * 
	 * @since 1.0.0
	 */
	public void setComments (String[] comments);

}

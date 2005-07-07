/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IMethodInvocationGenerator.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:28:35 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public interface IMethodInvocationGenerator {

	/**
	 * JavaDoc like comments
	 * @param comments
	 * 
	 * @since 1.0.0
	 */
	public void setComment (String comment);
	
	/**
	 * 
	 * @param methodName
	 * @return 
	 * 
	 * @since 1.0.0
	 */
	public String generateMethodInvocation(String methodName) ;
	
	/**
	 * Optionally set the arguments that are part of the method's signiture.
	 * This will be "type name" array
	 * 
	 * @param fmethodArguments The fmethodArguments to set.
	 */
	public void setMethodArguments(String[] fmethodArguments) ;
   

}
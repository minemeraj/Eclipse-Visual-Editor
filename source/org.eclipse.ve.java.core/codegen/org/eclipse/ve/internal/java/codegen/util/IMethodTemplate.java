/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IMethodTemplate.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:30:47 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public interface IMethodTemplate {

	public String generateMethod(AbstractMethodTextGenerator.MethodInfo info) ;

}

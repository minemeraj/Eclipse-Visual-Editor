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
 *  $RCSfile: IExpressionTemplate.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:47 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public interface IExpressionTemplate {

	public String generateExpression(AbstractExpressionGenerator.ExprInfo info) ;
}

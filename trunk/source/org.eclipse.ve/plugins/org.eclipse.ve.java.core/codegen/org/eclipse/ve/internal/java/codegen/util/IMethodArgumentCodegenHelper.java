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
 *  $RCSfile: IMethodArgumentCodegenHelper.java,v $
 *  $Revision: 1.2 $  $Date: 2005-08-24 23:30:46 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
 

/**
 * There is a generic way to make Codegen understand method arguments. The way this is done
 * is that when the visitors visit a method and see an argument, they add a IMethodArgumentCodegenHelper
 * as <code>KEY_METHODARGUMENT_CODEGENHELPER</code> a property of that ASTNode. Once this is
 * set the decoder has a generic way of getting a structural feature, any real ASTNode or a custom decoder
 * helper if required. 
 * Classes implmeneting this interface must return a valid value for each of the API 
 * 
 * @since 1.1
 */
public interface IMethodArgumentCodegenHelper {
	public static final String KEY_METHODARGUMENT_CODEGENHELPER = "KEY_METHODARGUMENT_CODEGENHELPER"; //$NON-NLS-1$
	
	public String getSFName();
	public IJavaFeatureMapper getNewFeatureMapper(BeanPart beanPart, Statement astStatement);
	public ASTNode getASTNode();
	public IExpressionDecoderHelper getNewExpressionDecoderHelper(BeanPart beanPart, Statement expr, IJavaFeatureMapper featureMapper, IExpressionDecoder decoder);
}

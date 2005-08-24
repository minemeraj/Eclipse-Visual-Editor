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
 *  $RCSfile: WorkbenchPartArgumentCodegenHelper.java,v $
 *  $Revision: 1.2 $  $Date: 2005-08-24 23:52:56 $ 
 */
package org.eclipse.ve.internal.jface.codegen;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.IMethodArgumentCodegenHelper;
 

/**
 * 
 * @since 1.1
 */
public class WorkbenchPartArgumentCodegenHelper implements IMethodArgumentCodegenHelper {

	private String sfName = null;
	private ASTNode node = null;
	
	public WorkbenchPartArgumentCodegenHelper(String sfName, ASTNode node){
		this.sfName = sfName;
		this.node = node;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.IMethodArgumentCodegenHelper#getASTNode()
	 */
	public ASTNode getASTNode() {
		return node;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.IMethodArgumentCodegenHelper#getExpressionDecoderHelper(org.eclipse.ve.internal.java.codegen.model.BeanPart, org.eclipse.jdt.core.dom.Statement, org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper, org.eclipse.ve.internal.java.codegen.java.IExpressionDecoder)
	 */
	public IExpressionDecoderHelper getNewExpressionDecoderHelper(BeanPart beanPart, Statement expr, IJavaFeatureMapper featureMapper,
			IExpressionDecoder decoder) {
		return new WorkbenchPartArgumentDecoderHelper(beanPart, expr, featureMapper, decoder);
	}

	public IJavaFeatureMapper getNewFeatureMapper(BeanPart beanPart, Statement astStatement) {
		HardCodedFeatureMapper fm = new HardCodedFeatureMapper(getSFName(), null);
		fm.setRefObject((IJavaInstance)beanPart.getModel().getABean(BeanPart.THIS_NAME).getEObject());	
		return fm;
	}

	public String getSFName() {
		return sfName;
	}

}

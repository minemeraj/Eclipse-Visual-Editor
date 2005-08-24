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
 *  $RCSfile: InitExpressionGenerator.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:46 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public class InitExpressionGenerator extends AbstractExpressionGenerator {
	
	public static final String INITEXP_TEMPLATE_CLASS_NAME = "InitExpressionTemplate" ; //$NON-NLS-1$
	public static final String INITEXP_TEMPLATE_NAME = INITEXP_TEMPLATE_CLASS_NAME+JAVAJET_EXT ;
	public final static  String BASE_PLUGIN = "org.eclipse.ve.java.core"; //$NON-NLS-1$
	public final static  String TEMPLATE_PATH = "templates/org/eclipse/ve/internal/java/codegen/jjet/util" ; //$NON-NLS-1$
	
	
	AbstractExpressionGenerator.ExprInfo fInfo = null ;
		

	/**
	 * @param component
	 * @param model
	 * 
	 * @since 1.0.0
	 */
	public InitExpressionGenerator(EObject component, IBeanDeclModel model) {
		super(component, model);	
	}



	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.AbstractMethodTextGenerator#getInfo()
	 */
	protected ExprInfo getInfo() {
		if (fInfo != null) return fInfo ;
		fInfo = new AbstractExpressionGenerator.ExprInfo() ;
		return fInfo ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.AbstractMethodTextGenerator#getBasePlugin()
	 */
	protected String getBasePlugin() {
		return BASE_PLUGIN;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.AbstractMethodTextGenerator#getTemplatePath()
	 */
	protected String getTemplatePath() {
		return TEMPLATE_PATH ;
	}



	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.AbstractExpressionGenerator#getExpressionTemplate()
	 */
	protected IExpressionTemplate getExpressionTemplate() {
		return getMethodTemplate(INITEXP_TEMPLATE_NAME,INITEXP_TEMPLATE_CLASS_NAME) ;
	}
	
	public String generate() {
	     return getExpressionTemplate().generateExpression(getInfo());
	}

}

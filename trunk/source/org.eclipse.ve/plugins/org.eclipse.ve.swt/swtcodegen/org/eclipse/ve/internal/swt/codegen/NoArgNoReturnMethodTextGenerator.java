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
 *  $RCSfile: NoArgNoReturnMethodTextGenerator.java,v $
 *  $Revision: 1.4 $  $Date: 2004-01-29 12:58:26 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.ve.internal.java.codegen.model.CodeMethodRef;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.IMethodTemplate;
 
/**
 * This method generator is a generic method generator for a method with no arguments, 
 * no return, and null construction for its init bean.
 * 
 * @author Gili Mendel
 * @since 1.0.0
 */
public class NoArgNoReturnMethodTextGenerator extends AbstractMethodTextGenerator {

	public static final String METHOD_TEMPLATE_CLASS_NAME = "NoArgNoReturnMethodTemplate" ; //$NON-NLS-1$
	public static final String METHOD_TEMPLATE_NAME = METHOD_TEMPLATE_CLASS_NAME+JAVAJET_EXT ;
	public final static  String BASE_PLUGIN = "org.eclipse.ve.swt"; //$NON-NLS-1$
	public final static  String TEMPLATE_PATH = "templates/org/eclipse/ve/internal/swt/codegen/jjet/util" ; //$NON-NLS-1$
	
	public final static  String METHOD_PREFIX = "create";
	
	public final static  String[] ignoredFeatures = {
	                                                  "allocation" };
												
	AbstractMethodTextGenerator.MethodInfo fInfo = null ;

	/**
	 * Shell has no return type
	 **/	
	protected AbstractMethodTextGenerator.MethodInfo getInfo() {
	   if (fInfo != null) return fInfo ;
       fInfo = new AbstractMethodTextGenerator.MethodInfo(false) ;
       return fInfo ;
	}
		
	public NoArgNoReturnMethodTextGenerator(EObject component, IBeanDeclModel model) {
		super(component,model) ;
	}
	
	
	protected IMethodTemplate getMethodTemplate() {
        return getMethodTemplate(METHOD_TEMPLATE_NAME,METHOD_TEMPLATE_CLASS_NAME) ;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IMethodTextGenerator#generateInLine(org.eclipse.ve.internal.java.codegen.model.CodeMethodRef, java.lang.String, java.util.List)
	 */
	public String generateInLine(CodeMethodRef method, String beanName, List kids) throws CodeGenException {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.AbstractMethodTextGenerator#getBasePlugin()
	 */
	protected String getBasePlugin() {
		return BASE_PLUGIN ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.AbstractMethodTextGenerator#getTemplatePath()
	 */
	protected String getTemplatePath() {
		return TEMPLATE_PATH ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.AbstractMethodTextGenerator#getIgnoreSFnameList()
	 */
	protected String[] getIgnoreSFnameList() {
		return ignoredFeatures;
	}
	public String getMethodPrefix() {
		return METHOD_PREFIX;
	}

}

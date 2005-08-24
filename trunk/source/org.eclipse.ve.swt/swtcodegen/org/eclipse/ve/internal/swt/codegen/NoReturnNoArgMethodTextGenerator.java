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
 *  $RCSfile: NoReturnNoArgMethodTextGenerator.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:52:56 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.formatter.CodeFormatter;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.util.*;
 
/**
 * This method generator is a generic method generator for a method with no arguments, 
 * no return, and null construction for its init bean.
 * 
 * @author Gili Mendel
 * @since 1.0.0
 */
public class NoReturnNoArgMethodTextGenerator extends AbstractMethodTextGenerator {

	public static final String METHOD_TEMPLATE_CLASS_NAME = "NoReturnMethodTemplate" ; //$NON-NLS-1$
	public static final String METHOD_TEMPLATE_NAME = METHOD_TEMPLATE_CLASS_NAME+JAVAJET_EXT ;
	public final static  String BASE_PLUGIN = "org.eclipse.ve.swt"; //$NON-NLS-1$
	public final static  String TEMPLATE_PATH = "templates/org/eclipse/ve/internal/swt/codegen/jjet/util" ; //$NON-NLS-1$
	
	public final static  String METHOD_PREFIX = "create"; //$NON-NLS-1$
	
	public final static  String[] ignoredFeatures = {JavaInstantiation.ALLOCATION };
	
	public static final String MAIN_TEMPLATE_CLASS_NAME = "SWTMainMethodTemplate" ; //$NON-NLS-1$
	public static final String MAIN_TEMPLATE_NAME = MAIN_TEMPLATE_CLASS_NAME+JAVAJET_EXT ;
												
	AbstractMethodTextGenerator.MethodInfo fInfo = null ;

	/**
	 * Shell has no return type
	 **/	
	protected AbstractMethodTextGenerator.MethodInfo getInfo() {
	   if (fInfo != null) return fInfo ;
       fInfo = new AbstractMethodTextGenerator.MethodInfo(false) ;
       return fInfo ;
	}
		
	public NoReturnNoArgMethodTextGenerator(EObject component, IBeanDeclModel model) {
		super(component,model) ;
	}
	
	
	protected IMethodTemplate getMethodTemplate() {
        return getMethodTemplate(METHOD_TEMPLATE_NAME,METHOD_TEMPLATE_CLASS_NAME) ;
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
	
	

	private boolean isExistNullConstructorMethod() {
		IType t = CodeGenUtil.getMainType(fModel.getCompilationUnit());		
		IMethod nullConstructor  = t.getMethod(t.getElementName(), new String[]{});
		if (nullConstructor!=null && !nullConstructor.exists())
			nullConstructor=null;
		if (nullConstructor!=null)
			return true;
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.IMethodTextGenerator#generateMain()
	 */
	public String generateMain(String className) {
		if (fMethodTemplate==null) 
			return null ;  // regular method was not generated yet.
	
		if ((!getInfo().finitBeanType.equals("org.eclipse.swt.widgets.Shell") && //$NON-NLS-1$
			 !getInfo().finitBeanType.equals("Shell")) || //$NON-NLS-1$
			!isExistNullConstructorMethod()) // The template assume a null constructor
			return null ;
		
		fMethodTemplate=null;
		IMethodTemplate mt = getMethodTemplate(MAIN_TEMPLATE_NAME,MAIN_TEMPLATE_CLASS_NAME) ;
		fMethodTemplate=null;
		getInfo().finitBeanType=className;
		String r = mt.generateMethod(getInfo());		
		fInfo=null;
		Map options = fModel.getCompilationUnit().getJavaProject().getOptions(true);		
		return DefaultClassGenerator.format(r, CodeFormatter.K_CLASS_BODY_DECLARATIONS, options, fModel.getLineSeperator() );		
	}
}

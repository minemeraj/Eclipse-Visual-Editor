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
 *  $RCSfile: AbstractMethodTextGenerator.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-27 01:12:06 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.jem.java.impl.JavaClassImpl;

import org.eclipse.ve.internal.java.codegen.model.CodeMethodRef;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.IMethodTemplate;
import org.eclipse.ve.internal.java.vce.templates.*;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public abstract class AbstractMethodTextGenerator implements IMethodTextGenerator {
	
	public final static String  JAVAJET_EXT = ".javajet" ; //$NON-NLS-1$
    public final static  String BASE_PLUGIN = "org.eclipse.ve.swt"; //$NON-NLS-1$
	public final static  String TEMPLATE_PATH = "templates/org/eclipse/ve/internal/swt/codegen/jjet/util" ; //$NON-NLS-1$

	protected	IBeanDeclModel	fModel ;
	protected	EObject			fComponent ;	
	protected   String			fmethodName = null ;
	protected   String[]		fComments = null ;
	protected	String			finitbeanName = null ;
	protected   String[]			fmethodArgs = null ;
	protected   String[]		fbeanInitArgs = null ;
	protected   IMethodTemplate fMethodTemplate = null ;
	
	
	public class MethodInfo {
		public String		   fSeperator ;
		public String[]		   fComments ;  // method's JavaDoc/Comments
		public String          fmethodName ;
		public String		   freturnType ;
		public String[]		   fmethodArguments ;    // type name array
		public String		   finitBeanName ;
		public String		   finitBeanType ;
		public String[]		   finitBeanArgs;		// Constructor arguments
		
		public MethodInfo(boolean generateReturn) {
			this.fSeperator = fModel.getLineSeperator() ;
			fmethodName = AbstractMethodTextGenerator.this.fmethodName ;
			finitBeanType = ((JavaClassImpl)fComponent.eClass()).getQualifiedName();
			finitBeanName = AbstractMethodTextGenerator.this.finitbeanName ;
			if (AbstractMethodTextGenerator.this.fComments==null)
				fComments = new String[] {IMethodTextGenerator.DEFAULT_METHOD_COMMENT+finitBeanName } ;
		    else
		    	fComments = AbstractMethodTextGenerator.this.fComments;
			
			if (generateReturn)
				freturnType = finitBeanType;
		    else
		    	freturnType = null ;
			finitBeanArgs = AbstractMethodTextGenerator.this.fbeanInitArgs;
			fmethodArgs = AbstractMethodTextGenerator.this.fmethodArgs;
		}

	}
	
	public AbstractMethodTextGenerator(EObject component, IBeanDeclModel model) {
		fModel = model ;
		fComponent=component ;
	}

    protected abstract IMethodTemplate getMethodTemplate() ;
    protected abstract AbstractMethodTextGenerator.MethodInfo getInfo();

    public String toString() {
    	return super.toString() + ": " + fmethodName ;
    }


	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IMethodTextGenerator#generateMethod(org.eclipse.ve.internal.java.codegen.model.CodeMethodRef, java.lang.String, java.lang.String)
	 */
	public String generateMethod(CodeMethodRef method, String methodName, String beanName) throws CodeGenException {
		fmethodName = methodName ;
		finitbeanName = beanName ;
				
		return getMethodTemplate().generateMethod(getInfo()) ;				
	}

	protected IMethodTemplate getMethodTemplate(String name, String className) {
		if (fMethodTemplate != null) return fMethodTemplate ;
		
		try {
			List list = TemplateUtil.getPluginAndPreReqJarPath(BASE_PLUGIN);
			list.addAll(TemplateUtil.getPlatformJREPath());
			String[] classPath = (String[]) list.toArray(new String[list.size()]);
			String   templatePath = TemplateUtil.getPluginInstallPath(BASE_PLUGIN, TEMPLATE_PATH) ;
			
			fMethodTemplate = (IMethodTemplate)
				TemplateObjectFactory.getClassInstance(classPath, new String[] {templatePath}, 
				name, TemplateUtil.getClassLoader(BASE_PLUGIN),
				className,null) ;
		}
		catch (TemplatesException e) {
			org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
		} 	                                                            	
		return fMethodTemplate ; 	
	}

	/**
	 * @return Returns the fbeanInitArgs.
	 */
	public String[] getBeanInitArgs() {
		return fbeanInitArgs;
	}

	/**
	 * @param fbeanInitArgs The fbeanInitArgs to set.
	 */
	public void setBeanInitArgs(String[] beanInitArgs) {
		this.fbeanInitArgs = beanInitArgs;
	}

	/**
	 * @return Returns the fmethodArguments.
	 */
	public String[] getMethodArguments() {
		return fmethodArgs;
	}

	/**
	 * @param fmethodArguments The fmethodArguments to set.
	 */
	public void setMethodArguments(String[] methodArguments) {
		this.fmethodArgs = methodArguments;
	}


	/**
	 * @param comments The fComments to set.
	 */
	public void setComments(String[] comments) {
		fComments = comments;
	}

}

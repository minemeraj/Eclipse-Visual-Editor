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
 *  $RCSfile: DefaultMethodInvocationGenerator.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:30:47 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

import java.util.List;

import org.eclipse.ve.internal.java.vce.templates.*;
 
/**
 * e.g., a call to createComposite();
 * @author Gili Mendel
 * @since 1.0.0
 */
public class DefaultMethodInvocationGenerator implements IMethodInvocationGenerator {
	
	public final static String  JAVAJET_EXT = ".javajet" ; //$NON-NLS-1$
	public final static  String BASE_PLUGIN = "org.eclipse.ve.java.core"; //$NON-NLS-1$
	public final static  String TEMPLATE_PATH = "templates/org/eclipse/ve/internal/java/codegen/jjet/util" ; //$NON-NLS-1$
	
	public static final String METHOD_TEMPLATE_CLASS_NAME = "DefaultMethodInvocationTemplate" ; //$NON-NLS-1$
	public static final String METHOD_TEMPLATE_NAME = METHOD_TEMPLATE_CLASS_NAME+JAVAJET_EXT ;
	

	String[] fmethodArgs ;
	String   fmethodName ;
	String   fComment = null ;
	
	IMethodInvocationTemplate fTemplate = null ;
	
	DefaultMethodInvocationGenerator.InvocationInfo finfo =  null ;
	
	public class InvocationInfo {
		public String		   fComment ;  // method's JavaDoc/Comments
		public String          fmethodName ;
		public String[]		   fmethodArguments ;    // type name array
	
		
		public InvocationInfo () {		
			fmethodName = DefaultMethodInvocationGenerator.this.fmethodName ;
			fComment = DefaultMethodInvocationGenerator.this.fComment ;			
			fmethodArgs = DefaultMethodInvocationGenerator.this.fmethodArgs;
		}

	}
	
	protected InvocationInfo getInfo() {
		if (finfo !=null ) return finfo ;		
		finfo = new DefaultMethodInvocationGenerator.InvocationInfo() ;
		return finfo ;
	}
	
	/**
	 * @param comment The fComment to set.
	 */
	public void setComment(String comment) {
		fComment = comment;
	}


	protected IMethodInvocationTemplate getMethodTemplate(String name, String className) {
		if (fTemplate != null) return fTemplate ;
		
		try {
			List list = TemplateUtil.getPluginAndPreReqJarPath(getBasePlugin());
			list.addAll(TemplateUtil.getPlatformJREPath());
			String[] classPath = (String[]) list.toArray(new String[list.size()]);
			String   templatePath = TemplateUtil.getPathForBundleFile(getBasePlugin(), getTemplatePath()) ;
			
			fTemplate = (IMethodInvocationTemplate)
			TemplateObjectFactory.getClassInstance(classPath, new String[] {templatePath}, 
			name, TemplateUtil.getClassLoader(getBasePlugin()),
			className,null) ;
		}
		catch (TemplatesException e) {
			org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
		} 	                                                            	
		return fTemplate ; 	
	}


	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.IMethodInvocationGenerator#generateMethodInvocation(java.lang.String)
	 */
	public String generateMethodInvocation(String methodName) {
		fmethodName = methodName ;

		IMethodInvocationTemplate t = getMethodTemplate(METHOD_TEMPLATE_NAME,METHOD_TEMPLATE_CLASS_NAME);		
		return t.generateMethod(getInfo()) ;		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.IMethodInvocationGenerator#setMethodArguments(java.lang.String[])
	 */
	public void setMethodArguments(String[] fmethodArguments) {
		this.fmethodArgs = fmethodArguments;

	}

	/**
	 * @return Returns the bASE_PLUGIN.
	 */
	public  String getBasePlugin() {
		return BASE_PLUGIN;
	}

    public String getTemplatePath() {
		return TEMPLATE_PATH ;
    }

}

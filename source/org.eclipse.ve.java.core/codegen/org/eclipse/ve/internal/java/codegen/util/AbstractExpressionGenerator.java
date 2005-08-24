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
 *  $RCSfile: AbstractExpressionGenerator.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:30:46 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.jem.java.impl.JavaClassImpl;

import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.vce.templates.*;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public abstract class AbstractExpressionGenerator {

	
	public final static String  JAVAJET_EXT = ".javajet" ; //$NON-NLS-1$	


	protected	IBeanDeclModel	fModel ;
	protected	EObject			fComponent ;	
	protected   String			fComment = null ;			// Trailing comment	
	protected   String			finitbeanConstructionString=null ; // If this exists, finitBeanArgs is ignored
	protected   String			finitbeanType = null;
	protected	String			finitbeanName = null ;		// Initialized Bean
	protected   String[]		finitbeanArgs = null ;		// Init constructor args.
	protected	String			fIndent = "\t"; //$NON-NLS-1$
	
	
	
	protected	IExpressionTemplate fTemplate = null ;
	

	
	
	public class ExprInfo {
		public String		   fSeperator;
		public String		   fComment ;			// method's JavaDoc/Comments
		public String		   finitBeanName ;
		public String		   finitBeanType ;
		public String		   finitbeanConstructionString;
		public String[]		   finitBeanArgs;		// Constructor arguments
		public String		   fIndent;
		
		public ExprInfo() {
			this.fSeperator = fModel.getLineSeperator() ;
			if (AbstractExpressionGenerator.this.finitbeanType == null) 
			   finitBeanType = ((JavaClassImpl)fComponent.eClass()).getQualifiedName();
			else
			   finitBeanType = AbstractExpressionGenerator.this.finitbeanType;
			finitBeanName = AbstractExpressionGenerator.this.finitbeanName ;	
			finitBeanArgs = AbstractExpressionGenerator.this.finitbeanArgs;
			fIndent = AbstractExpressionGenerator.this.fIndent;
			finitbeanConstructionString=AbstractExpressionGenerator.this.finitbeanConstructionString;
		}

	}
	
	public AbstractExpressionGenerator(EObject component, IBeanDeclModel model) {
		fModel = model ;
		fComponent=component ;
	}

	protected abstract IExpressionTemplate getExpressionTemplate() ;
	protected abstract AbstractExpressionGenerator.ExprInfo getInfo();

	public String toString() {
		return super.toString() + ": " + finitbeanName ; //$NON-NLS-1$
	}



	protected IExpressionTemplate getMethodTemplate(String name, String className) {
		if (fTemplate != null) return fTemplate ;
		
		try {
			List list = TemplateUtil.getPluginAndPreReqJarPath(getBasePlugin());
			list.addAll(TemplateUtil.getPlatformJREPath());
			String[] classPath = (String[]) list.toArray(new String[list.size()]);
			String   templatePath = TemplateUtil.getPathForBundleFile(getBasePlugin(), getTemplatePath()) ;
			
			fTemplate = (IExpressionTemplate)
			TemplateObjectFactory.getClassInstance(classPath, new String[] {templatePath}, 
			name, TemplateUtil.getClassLoader(getBasePlugin()),
			className,null) ;
		}
		catch (TemplatesException e) {
			org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
		} 	                                                            	
		return fTemplate ; 	
	}

	/**
	 * @return Returns the fbeanInitArgs.
	 */
	public String[] getBeanInitArgs() {
		return finitbeanArgs;
	}

	/**
	 * @param fbeanInitArgs The fbeanInitArgs to set.
	 */
	public void setBeanInitArgs(String[] beanInitArgs) {
		this.finitbeanArgs = beanInitArgs;
	}

	/**
	 * @param comments The fComments to set.
	 */
	public void setComment(String comment) {
		fComment = comment;
	}
	
	protected abstract String getBasePlugin() ;	
	protected abstract String getTemplatePath() ;
	
	

	/**
	 * @return Returns the fIndent.
	 */
	public String getIndent() {
		return fIndent;
	}

	/**
	 * @param indent The fIndent to set.
	 */
	public void setIndent(String indent) {
		fIndent = indent;
	}

	/**
	 * @return Returns the finitbeanConstructionString.
	 */
	public String getInitbeanConstructionString() {
		return finitbeanConstructionString;
	}

	/**
	 * @param finitbeanConstructionString The finitbeanConstructionString to set.
	 */
	public void setInitbeanConstructionString(String finitbeanConstructionString) {
		this.finitbeanConstructionString = finitbeanConstructionString;
	}

	/**
	 * @return Returns the finitbeanName.
	 */
	public String getInitbeanName() {
		return finitbeanName;
	}

	/**
	 * @param finitbeanName The finitbeanName to set.
	 */
	public void setInitbeanName(String finitbeanName) {
		this.finitbeanName = finitbeanName;
	}

	/**
	 * @param type The finitbeanType to set.
	 */
	public void setInitbeanType(String type) {
		this.finitbeanType = type;
	}
}

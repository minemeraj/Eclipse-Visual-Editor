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
 *  $Revision: 1.3 $  $Date: 2004-01-28 22:39:44 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.impl.JavaClassImpl;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.java.BeanDecoderAdapter;
import org.eclipse.ve.internal.java.codegen.java.ICodeGenAdapter;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.templates.*;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public abstract class AbstractMethodTextGenerator implements IMethodTextGenerator {
	
	public final static String  JAVAJET_EXT = ".javajet" ; //$NON-NLS-1$
	public final static String  DEFAULT_PREFIX = "get";   //$NON-NLS-1$


	protected	IBeanDeclModel	fModel ;
	protected	EObject			fComponent ;	
	protected   String			fmethodName = null ;
	protected   String[]		fComments = null ;
	protected	String			finitbeanName = null ;
	protected   String[]			fmethodArgs = null ;
	protected   String[]		fbeanInitArgs = null ;
	protected   IMethodTemplate fMethodTemplate = null ;
	protected	EStructuralFeature[] fignoreSFlist = null ;
	
	
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
			List list = TemplateUtil.getPluginAndPreReqJarPath(getBasePlugin());
			list.addAll(TemplateUtil.getPlatformJREPath());
			String[] classPath = (String[]) list.toArray(new String[list.size()]);
			String   templatePath = TemplateUtil.getPluginInstallPath(getBasePlugin(), getTemplatePath()) ;
			
			fMethodTemplate = (IMethodTemplate)
				TemplateObjectFactory.getClassInstance(classPath, new String[] {templatePath}, 
				name, TemplateUtil.getClassLoader(getBasePlugin()),
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
	
	protected abstract String getBasePlugin() ;	
	protected abstract String getTemplatePath() ;
	protected abstract String[] getIgnoreSFnameList();
	
	
	private EStructuralFeature[] getIgnoreSFlist() {
		if (fignoreSFlist!=null) return fignoreSFlist ;
		String[] list = getIgnoreSFnameList() ;
		if (list==null) return null ;
		EStructuralFeature[] sfList = new EStructuralFeature[list.length] ;
		EClass c = fComponent.eClass();
		for (int i = 0; i < sfList.length; i++) {
			sfList[i]=c.getEStructuralFeature(list[i]) ;
		}
		fignoreSFlist=sfList;
		return fignoreSFlist;
	}
	
	/**
	 * Determine if source sould be generated for the given SF
	 * @param sf
	 * @return true or false
	 * 
	 * @since 1.0.0
	 */
	protected boolean ignoreSF(EStructuralFeature sf) {				
		EStructuralFeature[] ignore = getIgnoreSFlist() ;
		if (sf == null || ignore==null || sf.isTransient() || sf.isMany()) return true ;
		
		for (int i = 0; i < ignore.length; i++) {
			if (sf.equals(ignore[i])) return true ;
		}
		return false ;
	}
	
	/**
	 *  Create a new Expression for a given SF
	 */
	protected CodeExpressionRef GenerateAttribute(EStructuralFeature sf,BeanPart bean) throws CodeGenException {		
		ExpressionRefFactory egen = new ExpressionRefFactory(bean,sf) ;      	
		CodeExpressionRef exp = egen.createFromJVEModel(null) ;	
		exp.insertContentToDocument() ;
		return exp ;
	}
	/**
	 * This method will generate Expressions for all set features
	 * that do not have codeGen adapter on them.
	 * 
	 * (At this time, the allocation feature is set independantly)
	 * 
	 * @since 1.0.0
	 */
	protected void generateForSetFeatures (BeanPart bean) throws CodeGenException {
		EObject obj = bean.getEObject() ;
		BeanDecoderAdapter a = (BeanDecoderAdapter) EcoreUtil.getExistingAdapter(obj, ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER) ;		
		Iterator itr = ((JavaClass)obj.eClass()).getEAllStructuralFeatures().iterator();
		while (itr.hasNext()) {
			EStructuralFeature sf = (EStructuralFeature) itr.next();
			if (obj.eIsSet(sf)) {
				if (ignoreSF(sf))
					continue;
				// Check if source was generated already
				if (a.getSettingAdapters(sf) != null && a.getSettingAdapters(sf).length > 0)
					continue;
				CodeExpressionRef newExpr = GenerateAttribute(sf, bean);
				String src = newExpr.getContent();
				if (src == null)
					throw new CodeGenException("Could not Generate Source"); //$NON-NLS-1$
				JavaVEPlugin.log("\tAdding: " + src, MsgLogger.LOG_FINE); //$NON-NLS-1$
			}
		}
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IMethodTextGenerator#generateExpressionsContent()
	 */
	public void generateExpressionsContent() throws CodeGenException {
		BeanPart b = fModel.getABean(fComponent) ;
		generateForSetFeatures(b) ;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.IMethodTextGenerator#getMethodPrefix()
	 */
	public String getMethodPrefix() {
		return DEFAULT_PREFIX;
	}

}

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
 *  $Revision: 1.6 $  $Date: 2004-02-20 00:44:29 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.impl.JavaClassImpl;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.templates.*;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public abstract class AbstractMethodTextGenerator implements IMethodTextGenerator {
	
	public final static String  JAVAJET_EXT = ".javajet" ; //$NON-NLS-1$
	public final static String  DEFAULT_METHOD_PREFIX = "get";   //$NON-NLS-1$


	protected	IBeanDeclModel	fModel ;
	protected	EObject			fComponent ;	
	protected   String			fmethodName = null ;
	protected   String[]		fComments = null ;			// JavaDoc for the method	
	protected   String[]		fmethodArgs = null ;
	protected	String			finitbeanName = null ;		// Initialized Bean
	protected   String[]		finitbeanArgs = null ;		// Init constructor args. 
	protected   IMethodTemplate fMethodTemplate = null ;
	protected	EStructuralFeature[] fignoreSFlist = null ;
	protected   boolean			fGenerateComments=false ;
	
	
	public class MethodInfo {
		public String		   fSeperator ;
		public String[]		   fComments ;			// method's JavaDoc/Comments
		public String          fmethodName ;
		public String		   freturnType ;
		public String[]		   fmethodArguments ;	// type name array
		public String		   finitBeanName ;
		public String		   finitBeanType ;
		public String[]		   finitBeanArgs;		// Constructor arguments
		
		public MethodInfo(boolean generateReturn) {
			this.fSeperator = fModel.getLineSeperator() ;
			fmethodName = AbstractMethodTextGenerator.this.fmethodName ;
			finitBeanType = ((JavaClassImpl)fComponent.eClass()).getQualifiedName();
			finitBeanName = AbstractMethodTextGenerator.this.finitbeanName ;
			fGenerateComments = AbstractMethodTextGenerator.this.fGenerateComments ;
			if (AbstractMethodTextGenerator.this.fComments==null)
				fComments = new String[] {IMethodTextGenerator.DEFAULT_METHOD_COMMENT+finitBeanName } ;
		    else
		    	fComments = AbstractMethodTextGenerator.this.fComments;
			
			if (generateReturn)
				freturnType = finitBeanType;
		    else
		    	freturnType = null ;
			finitBeanArgs = AbstractMethodTextGenerator.this.finitbeanArgs;
			fmethodArgs = AbstractMethodTextGenerator.this.fmethodArgs;
		}

	}
		
	
	public AbstractMethodTextGenerator(EObject component, IBeanDeclModel model) {
		fModel = model ;
		fComponent=component ;
// Given that we are using templates, these type of comments can be added by a user
//		Preferences store = VCEPreferences.getPlugin().getPluginPreferences();
//		fGenerateComments=store.getBoolean(VCEPreferences.GENERATE_COMMENT);
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
		return finitbeanArgs;
	}

	/**
	 * @param fbeanInitArgs The fbeanInitArgs to set.
	 */
	public void setBeanInitArgs(String[] beanInitArgs) {
		this.finitbeanArgs = beanInitArgs;
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
				JavaVEPlugin.log("\tAdding: " + src, Level.FINE); //$NON-NLS-1$
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
		return DEFAULT_METHOD_PREFIX;
	}
	
	protected CodeExpressionRef createInitExpression(BeanPart bean) {
		ExpressionRefFactory eg = new ExpressionRefFactory(bean,null) ;
		return eg.createInitExpression() ;
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.IMethodTextGenerator#generateInLine(org.eclipse.ve.internal.java.codegen.model.CodeMethodRef, java.lang.String, java.util.List)
	 */
	public void generateInLine(CodeMethodRef method, String beanName, List kids) throws CodeGenException {
				    	
				
		// Set up a new BeanPart in the decleration Model
		BeanPart bp = fModel.getABean(beanName) ;
		if(bp==null)
			bp = fModel.getABean(BeanDeclModel.constructUniqueName(method,beanName));//method.getMethodHandle()+"^"+fName);
				
		
		CodeExpressionRef initExp = createInitExpression(bp);
		// Allow the expression sorted to find a nice spot for this one
		initExp.setState(CodeExpressionRef.STATE_SRC_LOC_FIXED, false); // initExp.setState(initExp.getState()&~initExp.STATE_SRC_LOC_FIXED) ;
		initExp.setOffset(-1) ;
		try {   
			method.updateExpressionOrder() ;
		}
		catch (Throwable e) {
			JavaVEPlugin.log(e, Level.SEVERE) ;
			return   ;
		}
		// We may be processing a nested child, 
		// and the method is not in the source yet
		if (method.getMethodHandle() != null)
			initExp.insertContentToDocument() ;
		else
			initExp.setState(CodeExpressionRef.STATE_EXP_NOT_PERSISTED,true) ;
		
		generateForSetFeatures(bp) ;							
	}

}

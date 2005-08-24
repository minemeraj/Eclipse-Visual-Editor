/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: PropertyChangeInvocationHelper.java,v $
 *  $Revision: 1.11 $  $Date: 2005-08-24 23:30:44 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.jcm.*;
import org.eclipse.ve.internal.jcm.PropertyChangeEventInvocation;
import org.eclipse.ve.internal.jcm.PropertyEvent;

import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.codegen.util.TypeResolver;
import org.eclipse.ve.internal.java.codegen.util.TypeResolver.ResolvedType;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @author gmendel
 */
public abstract class PropertyChangeInvocationHelper extends EventDecoderHelper {
	
	public final static String  DEFAULT_PROPERTY_CHANGED_ADD_METHOD = "addPropertyChangeListener" ; //$NON-NLS-1$
	public static final  String PROPERTY_CALLBACK_NAME = "propertyChange" ;//$NON-NLS-1$
	public static final  String PROPERTY_NAME_GETTER =   "getPropertyName" ;//$NON-NLS-1$
	public static List    PropetyChangeCallbacks = null ;
		
	Method		fMethod = null ;

	public PropertyChangeInvocationHelper(BeanPart bean, Statement exp,  IEventDecoder owner) {
			super(bean,exp,owner) ;
	}
	
	protected Method getMethodName() {
		if (fMethod==null)
		   fMethod = ((PropertyChangeEventInvocation)fEventInvocation).getAddMethod() ;
		return fMethod ;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.EventDecoderHelper#isValidSelector(java.lang.String)
	 */
	protected boolean isValidSelector(String selector) {
		boolean valid =  PropertyChangedAllocationStyleHellper.DEFAULT_PROPERTY_CHANGED_ADD_METHOD.equals(selector) ;
		if (!valid)
		   CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Invalid Selector",true) ; //$NON-NLS-1$
		return valid ;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.EventDecoderHelper#isValidArguments(org.eclipse.jdt.internal.compiler.ast.Expression[])
	 */
	protected boolean isValidArguments(List exps) {
		boolean valid =  exps != null && exps.size()>0 && exps.size()<=2 ;
		if (!valid)
		    CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Invalid Arguments",true) ; //$NON-NLS-1$
		return valid ;
	}
	
	protected Method getAddMethod (MethodInvocation event) {
		
		JavaClass beanClass = (JavaClass) JavaRefFactory.eINSTANCE.reflectType(
					fbeanPart.getType(),fbeanPart.getModel().getCompositionModel().getModelResourceSet()) ;
		
		JavaClass propChangeClass = (JavaClass) JavaRefFactory.eINSTANCE.reflectType(
				"java.beans.PropertyChangeListener",fbeanPart.getModel().getCompositionModel().getModelResourceSet()) ; //$NON-NLS-1$	
		
		List argsList = new ArrayList() ;
		TypeResolver resolver = fbeanPart.getModel().getResolver();
		for (int i = 0; i < event.arguments().size(); i++) {			
			if (event.arguments().get(i) instanceof StringLiteral)
				   argsList.add("java.lang.String") ; //$NON-NLS-1$
			else if (event.arguments().get(i) instanceof ClassInstanceCreation) {
				ResolvedType resolveType = resolver.resolveType(((ClassInstanceCreation)event.arguments().get(i)).getName());
				if (resolveType == null)
					return null;
				String t = resolveType.getName();
				JavaClass tclass = (JavaClass) JavaRefFactory.eINSTANCE.reflectType(t, fbeanPart.getModel().getCompositionModel().getModelResourceSet());
				if (propChangeClass.isAssignableFrom(tclass))
					argsList.add("java.beans.PropertyChangeListener") ; //$NON-NLS-1$	
			}
			else if (event.arguments().get(i) instanceof SimpleName) {
				// Just hard coded at this time
				SimpleName sn = (SimpleName)event.arguments().get(i);				
				if (resolveInstance(sn.getIdentifier()) != null) 
				   argsList.add("java.beans.PropertyChangeListener") ;		//$NON-NLS-1$				
			}
		}		
		Method listenRegMethod = null ;
		while (listenRegMethod==null && beanClass != null) {         
			listenRegMethod = beanClass.getMethod(DEFAULT_PROPERTY_CHANGED_ADD_METHOD, argsList) ;
			if (listenRegMethod==null)
				beanClass = beanClass.getSupertype() ;
		}
		
		return listenRegMethod ;
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#unadaptToCompositionModel()
	 */
	public void unadaptToCompositionModel() {
		super.unadaptToCompositionModel();
		List propEevents = ((PropertyChangeEventInvocation) fEventInvocation).getProperties();
		for (int i = 0; i < propEevents.size(); i++) {
			PropertyEvent pe = (PropertyEvent) propEevents.get(i);
			unadaptPropertyEvent(pe);
		}
	}
	
	
	public void adaptToCompositionModel(IEventDecoder decoder) {	
		super.adaptToCompositionModel(decoder);
		List propEevents = ((PropertyChangeEventInvocation) fEventInvocation).getProperties();
		for (int i = 0; i < propEevents.size(); i++) {
			PropertyEvent pe = (PropertyEvent) propEevents.get(i);
			adaptPropertyEvent(pe);
		}
	}
	protected void adaptPropertyEvent(PropertyEvent pe) {
		if (feventAdapter != null)
			pe.eAdapters().add(feventAdapter.getPropertyEventSourceRangeAdapter(pe));
		else
			JavaVEPlugin.log("PropertyChangedAllocationStyleHellper.adaptPropertyEvent: NoAdapter", Level.WARNING); //$NON-NLS-1$
	}

	
	protected void unadaptPropertyEvent(PropertyEvent pe) {
		ICodeGenAdapter ca = (ICodeGenAdapter) EcoreUtil.getExistingAdapter(pe, ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
		if (ca != null)
			pe.eAdapters().remove(ca);
	}
	

	/* (non-Javadoc)
	 * @see com.ibm.etools.jbcf.codegen.java.EventDecoderHelper#getCallBackList()
	 */
	protected List getCallBackList() {
		if (PropetyChangeCallbacks != null) return PropetyChangeCallbacks ;		
		JavaClass cb = (JavaClass) JavaRefFactory.eINSTANCE.reflectType("java.beans.PropertyChangeListener",  //$NON-NLS-1$
					   fbeanPart.getModel().getCompositionModel().getModelResourceSet()) ;
		PropetyChangeCallbacks = cb.getMethods() ;
		return PropetyChangeCallbacks;
	}
	protected boolean isDifferntEnvocation(AbstractEventInvocation ei) {
		PropertyChangeEventInvocation e = (PropertyChangeEventInvocation)ei;	
		if (fEventInvocation==null) return true;
		List curProps = ((PropertyChangeEventInvocation)fEventInvocation).getProperties();
		List ePropes = e.getProperties();
		
		if (curProps.size()!=ePropes.size()) return true;
		
		for (int i = 0; i < curProps.size(); i++) {
			PropertyEvent cE = (PropertyEvent)curProps.get(i);
			PropertyEvent eE = (PropertyEvent)ePropes.get(i);
			if (cE.getPropertyName()!=eE.getPropertyName()) return true;		
		}
		return false;		
	}

	protected boolean isDiffrentDetails(AbstractEventInvocation ee) {
		List eeProps = ((PropertyChangeEventInvocation)ee).getProperties();
		List curProps = ((PropertyChangeEventInvocation) fEventInvocation).getProperties();
		if (eeProps.size()!=curProps.size())
			return true;
				
		for (int i = 0; i < eeProps.size(); i++) {
				boolean found = false;
				PropertyEvent e1 = (PropertyEvent)eeProps.get(i);
				for (int j = 0; j < curProps.size(); j++) {
					PropertyEvent e2 = (PropertyEvent)curProps.get(j);
					if (e1.isUseIfExpression()==e2.isUseIfExpression()) {
						if (e1.getPropertyName()==null || e1.getPropertyName()==null) { 
						  if (e1.getPropertyName()==e2.getPropertyName()) {
							found = true;
							break;
						  }
						}
						else if (e1.getPropertyName().equals(e2.getPropertyName())) {
							found = true;
							break;
						}
					}
				}
				if (!found) return true;
			}
		return false;
	}
}

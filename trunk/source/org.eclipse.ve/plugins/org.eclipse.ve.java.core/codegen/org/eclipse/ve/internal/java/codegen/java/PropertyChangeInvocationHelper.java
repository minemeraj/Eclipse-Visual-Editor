/*
 * Created on May 30, 2003
 * by gmendel
 *
*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: PropertyChangeInvocationHelper.java,v $
 *  $Revision: 1.4 $  $Date: 2004-02-20 00:44:29 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.ast.Statement;

import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.jcm.PropertyChangeEventInvocation;
import org.eclipse.ve.internal.jcm.PropertyEvent;

import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
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
	protected boolean isValidArguments(Expression[] exps) {
		boolean valid =  exps != null && exps.length>0 && exps.length<=2 ;
		if (!valid)
		    CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Invalid Arguments",true) ; //$NON-NLS-1$
		return valid ;
	}
	
	protected Method getAddMethod (MessageSend event) {
		
		JavaClass beanClass = (JavaClass) JavaRefFactory.eINSTANCE.reflectType(
					fbeanPart.getType(),fbeanPart.getModel().getCompositionModel().getModelResourceSet()) ;
		
		List argsList = new ArrayList() ;
		for (int i = 0; i < event.arguments.length; i++) {			
			if (event.arguments[i] instanceof StringLiteral)
				   argsList.add("java.lang.String") ; //$NON-NLS-1$
			else if (event.arguments[i] instanceof QualifiedAllocationExpression) {
				String t = ((QualifiedAllocationExpression)event.arguments[i]).type.toString() ;
				t = fbeanPart.getModel().resolve(t) ;
				argsList.add(t) ;
			}
			else if (event.arguments[i] instanceof AllocationExpression){
				AllocationExpression ae = (AllocationExpression)event.arguments[i] ;
				String t = ae.type.toString() ;
				t = fbeanPart.getModel().resolve(t) ;
				if ((event.arguments.length ==1||event.arguments.length ==2)
				        && ae.type instanceof SingleTypeReference){				
				    // Reference to an inner listener class
				    argsList.add("java.beans.PropertyChangeListener") ; //$NON-NLS-1$	
				}
				else
				   argsList.add(t) ;
			}
			else if (event.arguments[i] instanceof SingleNameReference) {
				// Just hard coded at this time
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

}

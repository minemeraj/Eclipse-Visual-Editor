/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: DefaultVisitorFactoryRule.java,v $
 *  $Revision: 1.1 $  $Date: 2005-03-30 17:34:23 $ 
 */
package org.eclipse.ve.internal.java.codegen.java.rules;

import java.util.HashMap;
import java.util.logging.Level;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.emf.ClassDecoratorFeatureAccess;
import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 

public class DefaultVisitorFactoryRule implements IVisitorFactoryRule {

	protected EClassifier classifier = null;
	protected HashMap classToVistorsCache= new HashMap();
	protected HashMap sfCache = new HashMap();
	protected EObject codegenHelperClass = null;
	
	protected final static String 
		SF_TYPE_VISITOR = "typeVisitor",
		SF_METHOD_VISITOR = "methodVisitor",
		SF_EVENT_METHOD_CALLBACK_VISITOR = "eventMethodCallBackVisitor",
		SF_EVENT_METHOD_VISITOR = "eventMethodVisitor",
		SF_EVENT_HANDLER_VISITOR = "eventHandlerVisitor",
		SF_RETURN_STMT_VISITOR = "returnStmtVisitor",
		SF_EXPRESSION_VISITOR = "expressionVisitor",
		SF_EVENT_EXPRESSION_VISITOR = "eventExpressionVisitor",
		SF_EVENT_CALLBACK_EXPRESSION_VISITOR = "eventCallBackExpressionVisitor";
	
	protected EStructuralFeature getSF(String sfName){
		EStructuralFeature sf = (EStructuralFeature) sfCache.get(sfName);
		
		// If SF present in cache - check if it is coming from same resource sets - else exceptions will arise later
		if(sf!=null){
			ResourceSet sfRS = (sf==null || sf.eResource() == null) ? null : sf.eResource().getResourceSet();
			ResourceSet classifierRS = (getClassifier()==null || getClassifier().eResource()==null) ? null : getClassifier().eResource().getResourceSet(); 
			if(sfRS!=classifierRS){
				codegenHelperClass = null;
				sf = null;
				sfCache.clear();
			}
		}
		
		if(sf==null){
			if(codegenHelperClass==null)
				codegenHelperClass = getClassifier().eResource().getResourceSet().getEObject(ExpressionDecoderFactory.URIcodeGenHelperClass, true);
			sf = ((EClass)codegenHelperClass).getEStructuralFeature(sfName);
			sfCache.put(sfName, sf);
		}
		return sf;
	}
	
	protected ISourceVisitor retrieveFromCache(String visitorSFName){
		HashMap visitorsCache = (HashMap) classToVistorsCache.get(getClassifier().getName());
		if(visitorsCache==null){
			visitorsCache = new HashMap();
			classToVistorsCache.put(getClassifier().getName(), visitorsCache);
		}
		
		String visitorTypeName = (String) visitorsCache.get(visitorSFName);
		if(visitorTypeName==null){
			// there was no visitor in the cache for the classifier - determine which
			EAnnotation decr = ClassDecoratorFeatureAccess.getDecoratorWithFeature(getClassifier(), ExpressionDecoderFactory.SOURCE_DECORATOR_KEY, getSF(visitorSFName));
			String visitorType = (String) decr.eGet(getSF(visitorSFName));
			if(visitorType!=null){
				visitorsCache.put(visitorSFName, visitorType);
				visitorTypeName = visitorType;
			}
		}
		
		// Return a new instance of the type class name
		ISourceVisitor visitorInstance = null;
		if(visitorTypeName!=null){
			try {
				Class visitorTypeClass = CDEPlugin.getClassFromString(visitorTypeName);
				if(visitorTypeClass != null)
					visitorInstance = (ISourceVisitor) visitorTypeClass.newInstance();
			} catch (ClassNotFoundException e) {
				JavaVEPlugin.log(e, Level.WARNING);
			} catch (InstantiationException e) {
				JavaVEPlugin.log(e, Level.WARNING);
			} catch (IllegalAccessException e) {
				JavaVEPlugin.log(e, Level.WARNING);
			}
		}
		return visitorInstance;
	}
	
	public TypeVisitor getTypeVisitor() {
		return (TypeVisitor) retrieveFromCache(SF_TYPE_VISITOR);
	}

	public MethodVisitor getMethodVisitor() {
		return (MethodVisitor) retrieveFromCache(SF_METHOD_VISITOR);
	}

	public EventMethodCallBackVisitor getEventMethodCallBackVisitor() {
		return (EventMethodCallBackVisitor) retrieveFromCache(SF_EVENT_METHOD_CALLBACK_VISITOR);
	}

	public EventMethodVisitor getEventMethodVisitor() {
		return (EventMethodVisitor) retrieveFromCache(SF_EVENT_METHOD_VISITOR);
	}

	public EventHandlerVisitor getEventHandlerVisitor() {
		return (EventHandlerVisitor) retrieveFromCache(SF_EVENT_HANDLER_VISITOR);
	}

	public ReturnStmtVisitor getReturnStmtVisitor() {
		return (ReturnStmtVisitor) retrieveFromCache(SF_RETURN_STMT_VISITOR);
	}

	public ExpressionVisitor getExpressionVisitor() {
		return (ExpressionVisitor) retrieveFromCache(SF_EXPRESSION_VISITOR);
	}

	public EventExpressionVisitor getEventExpressionVisitor() {
		return (EventExpressionVisitor) retrieveFromCache(SF_EVENT_EXPRESSION_VISITOR);
	}

	public EventCallBackExpressionVisitor getEventCallBackExpressionVisitor() {
		return (EventCallBackExpressionVisitor) retrieveFromCache(SF_EVENT_CALLBACK_EXPRESSION_VISITOR);
	}

	public void setClassifier(EClassifier eclassifier) {
		classifier = eclassifier;
	}

	public EClassifier getClassifier() {
		return classifier;
	}

	public void setRegistry(IRuleRegistry registry) {
		// TODO Auto-generated method stub

	}

}

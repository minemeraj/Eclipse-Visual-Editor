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
 *  $RCSfile: EventInvocationHelper.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.compiler.ast.*;

import org.eclipse.jem.internal.beaninfo.EventSetDecorator;
import org.eclipse.jem.internal.beaninfo.MethodProxy;

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.jem.internal.java.Method;
import org.eclipse.jem.internal.java.JavaClass;
import org.eclipse.jem.internal.java.JavaParameter;

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.java.rules.IEventMethodParsingRule;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;

/**
 * @author gmendel
 */
public abstract class EventInvocationHelper extends EventDecoderHelper {
	
	EventSetDecorator  fEventDecorator = null ;
	
	public EventInvocationHelper(BeanPart bean, Statement exp,  IEventDecoder owner) {
		super(bean,exp,owner) ;
	}
	
	protected EventSetDecorator getDecorator() {
		if (fEventDecorator == null)
		   fEventDecorator = org.eclipse.jem.internal.beaninfo.adapters.Utilities.getEventSetDecorator(((EventInvocation)fEventInvocation).getEvent()) ;
		return fEventDecorator ;		   
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.EventDecoderHelper#isValidSelector(java.lang.String)
	 */
	protected boolean isValidSelector(String selector) {
		boolean valid = getDecorator().getAddListenerMethod().getName().equals(selector) ;
		if(!valid)
		   CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Invalid Selector",true) ; //$NON-NLS-1$
		return valid ;
	}
	
	protected boolean isSameArgs(AbstractMethodDeclaration md, Method m) {
		boolean result = true ;
		for (int i=0; i<m.getParameters().size(); i++) {
			JavaParameter p = (JavaParameter) m.getParameters().get(i) ;
			Argument a = md.arguments[i] ;
			String atype = fbeanPart.getModel().resolve(a.type.toString()) ;
			if (!p.getJavaType().getQualifiedName().equals(atype)) {
				result = false ;
				break ;
			}
		}
		if (!result)
		   CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Invalid arguments",true) ; //$NON-NLS-1$
		return result ;
	}
	
	/**
	 * Parse an anonymouse type and find the list of (event) methods that it implements
	 */
	protected List getAnonymousTypeEventMethods(AnonymousLocalTypeDeclaration exp) {
		AbstractMethodDeclaration[] methods = exp.methods ;
		List ml = new ArrayList() ;
		List listenMethods = getDecorator().getListenerMethods() ;
		IEventMethodParsingRule rule = (IEventMethodParsingRule) CodeGenUtil.getEditorStyle(fbeanPart.getModel()).getRule(IEventMethodParsingRule.RULE_ID) ;
		if (methods!=null) {
			for (int i = 0; i < methods.length; i++) {
				for (Iterator itr = listenMethods.iterator(); itr.hasNext();) {
					Method m = ((MethodProxy) itr.next()).getMethod();
					if (m.getName().equals(new String(methods[i].selector))) {
						int s1, s2;
						s1 = m.getParameters().size();
						s2 = methods[i].arguments == null ? 0 : methods[i].arguments.length;
						if (s1 == s2 && isSameArgs(methods[i],m)) {
							// TODO Need to compare return, and param sig
							if (!rule.ignoreAnonymousEventMethod(methods[i]))
								ml.add(m);
						}
					}
				}
			}
		}
		return ml ;
	}
	
	/**
	 * Analyze an Inner class from the working copy to figure out which event methods it implements
	 */
	protected List getInnerTypeEventMethods(JavaClass c) {
		List ml = new ArrayList();     	
		try {
			IType t = getInnerType(c) ;    	
			if (t == null) return null ;
			
			// In Style 3, all the methods count
			IMethod iMethods[] = t.getMethods() ;
			List    listenMethods = getDecorator().getListenerMethods() ;
			for (int i = 0; i < iMethods.length; i++) {
				for (Iterator listenIter = listenMethods.iterator(); listenIter.hasNext();) {
				   Method lM = ((MethodProxy)listenIter.next()).getMethod();
				   if (iMethods[i].getElementName().equals(lM.getName()) &&
					iMethods[i].getNumberOfParameters() == lM.getParameters().size()) {
						// TODO Need to compare return, and param sig 
						ml.add(lM) ;
						break ;					
					}
				}				
			}
		}
		catch (JavaModelException e) {
			return null ;
		}    	    	
		return ml ;    	
	}
	
	/**
	 * Analyze an explicity type, and figure out which (event) methods it implements
	 * --- at this point only method implemented by the class, not its supers
	 */
	protected List getExplicitTypeEventMethods(JavaClass c) {
		if (isInnerClass(c)) 
		   return getInnerTypeEventMethods(c) ;
    	
		List ml = new ArrayList() ;    
		// For external classes, we will not list implemented methods		
//		List listenMethods = fEventDecorator.getListenerMethods() ;
//		List classMethods = c.getMethods()	;
//		for (Iterator classItr = classMethods.iterator(); classItr.hasNext();) {
//			JCMMethod cM = (JCMMethod) classItr.next();
//			for (Iterator listenIter = listenMethods.iterator(); listenIter.hasNext();) {
//				JCMMethod lM = ((MethodProxy)listenIter.next()).getMethod();
//				if (cM.getName().equals(lM.getName()) &&
//					cM.getParameters().size() == lM.getParameters().size()) {
//					org.eclipse.ve.internal.cde.core.CDEHack.fixMe("Need to compare return, and param sig");
//					ml.add(lM) ;
//					break ;
//				}
//			}
//		}
		return ml ;
	}
	

	/**
	 * 
	 */
	public static EventInvocation  getNewEventInvocation (IDiagramModelInstance cm) {
		EventInvocation ee =  JCMFactory.eINSTANCE.createEventInvocation() ;
		return ee ;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.EventDecoderHelper#isValidArguments(org.eclipse.jdt.internal.compiler.ast.Expression[])
	 */
	protected boolean isValidArguments(Expression[] exps) {
		boolean result = exps != null && exps.length == 1;
		if (result) {
			JavaParameter p = (JavaParameter) fEventDecorator.getAddListenerMethod().getParameters().get(0);
			if (exps[0] instanceof QualifiedAllocationExpression) {
				QualifiedAllocationExpression e = (QualifiedAllocationExpression) exps[0];
				String type = fbeanPart.getModel().resolve(e.type.toString());
				StringBuffer b = new StringBuffer(type) ;
				if (type.indexOf("Adapter") >= 0) {   //$NON-NLS-1$
				    b.replace(type.indexOf("Adapter"), type.length(), "Listener") ; //$NON-NLS-1$ //$NON-NLS-2$				    
				}
				if (!p.getJavaType().getQualifiedName().equals(type) &&
				    !p.getJavaType().getQualifiedName().equals(b.toString()))
					result = false;
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#addPropertyEvent(org.eclipse.ve.internal.jcm.PropertyEvent)
	 */
	public void addPropertyEvent(PropertyEvent c) {
		throw new IllegalArgumentException ("No Properties available for this decoder") ;//$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#removePropertyEvent(org.eclipse.ve.internal.jcm.PropertyEvent)
	 */
	public void removePropertyEvent(PropertyEvent c) {
		throw new IllegalArgumentException ("No Properties available for this decoder") ;//$NON-NLS-1$

	}
	
	protected List getCallBackList() {
		return getDecorator().getListenerMethods();
	}

}

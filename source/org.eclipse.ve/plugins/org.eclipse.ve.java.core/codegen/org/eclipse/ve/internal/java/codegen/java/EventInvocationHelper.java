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
 *  $Revision: 1.8 $  $Date: 2004-04-20 17:30:39 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.jem.internal.beaninfo.EventSetDecorator;
import org.eclipse.jem.internal.beaninfo.MethodProxy;
import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
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
		   fEventDecorator = org.eclipse.jem.internal.beaninfo.core.Utilities.getEventSetDecorator(((EventInvocation)fEventInvocation).getEvent()) ;
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
	
	protected boolean isSameArgs(MethodDeclaration md, Method m) {
		boolean result = true ;
		for (int i=0; i<m.getParameters().size(); i++) {
			JavaParameter p = (JavaParameter) m.getParameters().get(i) ;
			SingleVariableDeclaration a = (SingleVariableDeclaration) md.parameters().get(i) ;
			String atype ;
			if (a.getType() instanceof SimpleType)
				atype = CodeGenUtil.resolve(((SimpleType)a.getType()).getName(),fbeanPart.getModel());
			else
				atype = a.getType().toString();  // Need more specifics for arrays
			if (!p.getJavaType().getQualifiedName().equals(atype)) {
				result = false ;
				break ;
			}
		}
		if (!result)
		   CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Invalid arguments",true) ; //$NON-NLS-1$
		return result ;
	}
	
	protected List getMethods(List bodyDecleration) {
		List methods = new ArrayList();
		for (int i = 0; i < bodyDecleration.size(); i++) {
			if (bodyDecleration.get(i) instanceof MethodDeclaration)
				methods.add(bodyDecleration.get(i));
		}
		return methods;
	}
	/**
	 * Parse an anonymouse type and find the list of (event) methods that it implements
	 */
	protected List getAnonymousTypeEventMethods(AnonymousClassDeclaration exp) {
		List methods = getMethods(exp.bodyDeclarations()) ;
		List ml = new ArrayList() ;
		List listenMethods = getDecorator().getListenerMethods() ;
		IEventMethodParsingRule rule = (IEventMethodParsingRule) CodeGenUtil.getEditorStyle(fbeanPart.getModel()).getRule(IEventMethodParsingRule.RULE_ID) ;
		if (methods!=null) {
			for (int i = 0; i < methods.size(); i++) {
				MethodDeclaration md = (MethodDeclaration)methods.get(i);
				for (Iterator itr = listenMethods.iterator(); itr.hasNext();) {
					Method m = ((MethodProxy) itr.next()).getMethod();
					if (m.getName().equals(md.getName().getIdentifier())) {
						int s1, s2;
						s1 = m.getParameters().size();
						s2 = md.parameters().size();
						if (s1 == s2 && isSameArgs(md,m)) {
							// TODO Need to compare return, and param sig
							if (!rule.ignoreAnonymousEventMethod(md))
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
	public static EventInvocation  getNewEventInvocation (IVEModelInstance cm) {
		EventInvocation ee =  JCMFactory.eINSTANCE.createEventInvocation() ;
		return ee ;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.EventDecoderHelper#isValidArguments(org.eclipse.jdt.internal.compiler.ast.Expression[])
	 */
	protected boolean isValidArguments(List exps) {
		boolean result = exps != null && exps.size() == 1;
		if (result) { // Check parmeter type
			JavaParameter p = (JavaParameter) fEventDecorator.getAddListenerMethod().getParameters().get(0);
			if (exps.get(0) instanceof ClassInstanceCreation) {
				ClassInstanceCreation e = (ClassInstanceCreation) exps.get(0);
				String type = CodeGenUtil.resolve(e.getName(),fbeanPart.getModel()); 
				JavaHelpers jclazz = JavaRefFactory.eINSTANCE.reflectType(type, fbeanPart.getEObject());
				if (jclazz != null && ((JavaClass)jclazz).isExistingType()) {
					if (p.getJavaType().isAssignableFrom(jclazz))
						result = true;
					else
						result = false;
				}
				else {
					// Class may not be saved (inner/anonymouse class) and hence not in the JCM
					StringBuffer b = new StringBuffer(type) ;
					if (type.indexOf("Adapter") >= 0) {   //$NON-NLS-1$
					    b.replace(type.indexOf("Adapter"), type.length(), "Listener") ; //$NON-NLS-1$ //$NON-NLS-2$				    
					}
					if (!p.getJavaType().getQualifiedName().equals(type) &&
					    !p.getJavaType().getQualifiedName().equals(b.toString()))
						result = false;
					
				}
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

/*******************************************************************************
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
 *  $RCSfile: AllocationStyleHelper.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 16:16:38 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.compiler.ast.*;

import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.CodeEventRef;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.jcm.*;

import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.Method;

/**
 * @author Gili Mendel
 *
 */
public class AllocationStyleHelper extends EventInvocationHelper {
		
	public AllocationStyleHelper(BeanPart bean, Statement exp, IEventDecoder owner) {
		super(bean, exp, owner);
	}

	protected boolean processEvent(MessageSend event) {
		
		Expression exp = event.arguments[0] ;
		cleanUpPreviousIfNedded() ;
		int index = getInvocationIndex();
//		EventInvocation ee = getNewEventInvocation(fbeanPart.getModel().getCompositionModel());
//		ee.setEvent((BeanEvent) fEventDecorator.eContainer());
		EventInvocation ee = (EventInvocation) fEventInvocation ;
		if (exp instanceof QualifiedAllocationExpression) {
			// Anonymous allocation
			QualifiedAllocationExpression qe = (QualifiedAllocationExpression) exp;
			JavaClass clazz = getAllocatedType(qe.type);
			if (clazz == null || !clazz.isExistingType())
				return false;

			Listener l;
			if (clazz.isInterface())
				l = getAnonymousListener(null, new Object[] { clazz });
			else
				l = getAnonymousListener(clazz, null);
			ee.setListener(l);
			List impl = getAnonymousTypeEventMethods(qe.anonymousType);
			for (Iterator itr = impl.iterator(); itr.hasNext();) {
				Method m = (Method) itr.next();
				// Anonymouse callback is not shared
				addMethod(ee,m,false);
			}
			addInvocationToModel(ee, index);
			return true;
		}
		else if (exp instanceof AllocationExpression) {
			// Allocation of a new class
			AllocationExpression ae = (AllocationExpression) exp;
			JavaClass clazz = getAllocatedType(ae.type);
			if (clazz == null)
				return false;
			Listener l = getIsClassListener(clazz);
			if (l == null)
				return false;

			ee.setListener(l);
			List impl = getExplicitTypeEventMethods(clazz);
			for (Iterator itr = impl.iterator(); itr.hasNext();) {
				Method m = (Method) itr.next();
				// If it is an inner class, it is not parsed by this decoder and hence
				// marked as shared
				addMethod(ee,m,isInnerClass(clazz)) ;
			}
			addInvocationToModel(ee,index);
			return true;
		}
		return false;
	}

	/**
	* @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#getPriorityOfExpression()
	*/
	public Object getPriorityOfExpression() {
		return null;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#getCurrentExpression()
	 */
	public String getCurrentExpression() {
		return null;
	}


	public String toString() {
		return "VCEAnonymousStyleHelper: " + fExpr; //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.EventDecoderHelper#getSrcGenerator(Object[])
	 */
	protected IEventSrcGenerator getSrcGenerator(Object[] args) {
		if (fSrcGenerator == null) {
			fSrcGenerator = new AnonymousEventSrcGenerator((EventInvocation) args[0], (Listener) args[1], fbeanPart.getSimpleName());
		}
		fSrcGenerator.setIndent(fIndentFiller) ;
		if (args.length > 2 && args[2] != null)
			fSrcGenerator.setEventArgName((String) args[2]);
		return fSrcGenerator;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#removeCallBack(org.eclipse.ve.internal.jcm.Callback)
	 */
	public void removeCallBack(Callback c) {
		CodeEventRef ref = (CodeEventRef) fOwner.getExprRef() ;
		EventExpressionParser parser = (EventExpressionParser) ref.getContentParser() ;		
		try {
			if (fEventInvocation.getListener().getListenerType().getExtends() == null)
			    // Interface, remove just the method body
			    parser = parser.removeAnonymousMethodBody(c.getMethod().getName()) ;
			else
			    parser = parser.removeAnonymousMethod(c.getMethod().getName()) ;
			    
		}
		catch (CodeGenException e) {
			org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
			return ;
		}
		unadaptCallBack(c) ;
		
		if (parser != null)
		   ref.updateDocument(parser) ;

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#addCallBack(org.eclipse.ve.internal.jcm.Callback)
	 */
	public void addCallBack(Callback c) {
		Object[] args = new Object[3] ;
		args[0] = c.eContainer() ;  // MethodInvocation
		args[1] = ((AbstractEventInvocation)args[0]).getListener() ;
		args[2] = getEventArgName() ;
		String content = getSrcGenerator(args).generateEventMethod(new Callback[] { c }) ;
		
		CodeEventRef ref = (CodeEventRef) fOwner.getExprRef() ;
		EventExpressionParser parser = (EventExpressionParser) ref.getContentParser() ;
		try {
			parser = parser.removeAnonymousMethod(c.getMethod().getName()) ;
		}
		catch (CodeGenException e) {}		
			
		try {
			parser = parser.addAnonymousMethod(content) ;
		}
		catch (CodeGenException e) {
			org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
		}	
		if (parser != null)
		  ref.updateDocument(parser) ;
		adaptCallBack(c) ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#getCallBackSourceRange(org.eclipse.ve.internal.jcm.Callback)
	 */
	public ICodeGenSourceRange getCallBackSourceRange(Callback c) {
		String lName = null;
		try {
			lName = fEventInvocation.getListener().getListenerType().getName() ;			
		}
		catch (Exception e) {}
		if (lName==null || lName.length()==0) {	
			// Annonymouse class			
			CodeEventRef ref = (CodeEventRef) fOwner.getExprRef();
			EventExpressionParser parser = (EventExpressionParser) ref.getContentParser();
			if (parser != null) {
				try {

					ICodeGenSourceRange sr = parser.getAnonymousMethodHighlight(c.getMethod().getName());
					// Relative offset to the main expression				
					ICodeGenSourceRange Esr = ref.getTargetSourceRange();
					if (Esr == null || sr == null)
						return null;
					CodeGenSourceRange result = new CodeGenSourceRange(Esr.getOffset() + sr.getOffset(), sr.getLength());
					result.setLineOffset(Esr.getLineOffset());
					return result;

				}
				catch (CodeGenException e) {}
			}
		}
		else {
			// Shared class, can be potentially be an inner class.  Use JDT to figure out where
			// is the method
			int idx = lName.lastIndexOf('.');
			if (idx>0)
			   lName = lName.substring(0,idx) + "$" + lName.substring(idx+1,lName.length()) ; //$NON-NLS-1$
			JavaClass clazz = (JavaClass) org.eclipse.jem.java.impl.JavaClassImpl.reflect(lName,fbeanPart.getModel().getCompositionModel().getModelResourceSet()) ;			
			try {
				IType iType = getInnerType(clazz) ;
				if (iType != null) {
					IMethod[] methods =iType.getMethods() ;
					Method m = c.getMethod() ;
					for (int i = 0; i < methods.length; i++) {
						if (m.getName().endsWith(methods[i].getElementName()))
						  if (m.getParameters().size() == methods[i].getParameterNames().length) {
						  	// TODO: Need to compare parameter types as well
						  	ICodeGenSourceRange sr = new CodeGenSourceRange(methods[i].getSourceRange()) ;
						  	return sr ;
						  }
						
					}
				}
			}
			catch (JavaModelException e1) {}
			
			
					
		}
		return null ;
	}
	

}

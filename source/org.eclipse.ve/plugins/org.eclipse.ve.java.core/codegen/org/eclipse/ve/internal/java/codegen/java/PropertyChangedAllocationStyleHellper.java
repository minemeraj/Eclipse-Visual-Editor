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
 *  $RCSfile: PropertyChangedAllocationStyleHellper.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.ArrayList;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.compiler.AbstractSyntaxTreeVisitorAdapter;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;

import org.eclipse.ve.internal.jcm.*;
import org.eclipse.jem.internal.java.JavaClass;
import org.eclipse.jem.internal.java.Method;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.CodeEventRef;
import org.eclipse.ve.internal.java.codegen.util.*;

/**
 * @author gmendel
 */
public class PropertyChangedAllocationStyleHellper extends PropertyChangeInvocationHelper {
		
    public static final  String PREPERTY_CALLBACK_ARG_TYPE =  "java.beans.PropertyChangeEvent";//$NON-NLS-1$	

	
	    
	public PropertyChangedAllocationStyleHellper(BeanPart bean, Statement exp, IEventDecoder owner) {
			super(bean, exp, owner);
	}
	
	
	protected String[] parseProperties(Expression exp) {
		final ArrayList props = new ArrayList() ;
		AbstractSyntaxTreeVisitorAdapter visitor = new AbstractSyntaxTreeVisitorAdapter() {
			public boolean visit(IfStatement ifStatement, BlockScope scope) {
					if (ifStatement.condition instanceof MessageSend) {
						MessageSend m = (MessageSend) ifStatement.condition;
						if (m.receiver instanceof MessageSend) {
							MessageSend left = (MessageSend) m.receiver;
							if (new String(left.selector).endsWith(PROPERTY_NAME_GETTER)) {
								if (m.arguments != null && m.arguments.length == 1 && m.arguments[0] instanceof StringLiteral){								
								    // remove the ""
								    String p = m.arguments[0].toString().substring(1) ;
								    p = p.substring(0,p.length()-1) ;
									props.add(p);
								}
							}
						}

					}
					return false;
			}
		};		
		exp.traverse(visitor, null);
		return (String[]) props.toArray(new String[props.size()]);
	}
	
	protected Method[] parseCallBacks(Expression exp, final JavaClass listener) {
		final ArrayList methods = new ArrayList();
		AbstractSyntaxTreeVisitorAdapter visitor = new AbstractSyntaxTreeVisitorAdapter() {
			public boolean visit(MethodDeclaration methodDeclaration, ClassScope scope) {
				if (new String(methodDeclaration.selector).equals(PROPERTY_CALLBACK_NAME)) {
					ArrayList args = new ArrayList() ;
					args.add(PREPERTY_CALLBACK_ARG_TYPE); 	
					Method m = listener.getMethodExtended(PROPERTY_CALLBACK_NAME,args);		
					methods.add(m) ;					
				}
				return false;
			}
		};

        exp.traverse(visitor, null);
		return (Method[]) methods.toArray(new Method[methods.size()]);
	}
	
	
	protected boolean processEvent(MessageSend event) {
		Expression exp = event.arguments[event.arguments.length-1] ;
				                                		
		
		cleanUpPreviousIfNedded() ;
		int index = getInvocationIndex();

		PropertyChangeEventInvocation ee = (PropertyChangeEventInvocation) fEventInvocation ;
		
	
        Method listenRegMethod = getAddMethod(event) ;
        if (listenRegMethod != null)
           ee.setAddMethod(listenRegMethod) ;	            
		
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
						
			if (event.arguments.length == 1) {
				String[] props = parseProperties(exp);
				if (props.length > 0) {
					for (int i = 0; i < props.length; i++) {
						PropertyEvent pe = JCMFactory.eINSTANCE.createPropertyEvent();
						pe.setPropertyName(props[i]);
						pe.setUseIfExpression(true);
						ee.getProperties().add(pe);
					}
				}
			}
			else if (event.arguments.length == 2) {
				if (event.arguments[0] instanceof StringLiteral) {
					String pname = ((StringLiteral) event.arguments[0]).toString();
					pname = pname.substring(1, pname.length() - 1);
					PropertyEvent pe = JCMFactory.eINSTANCE.createPropertyEvent();
					pe.setPropertyName(pname);
//					pe.setUseIfExpression(false);
                    // We are using this flag to denote that we can parse/control this property
					pe.setUseIfExpression(true);
					ee.getProperties().add(pe);
				}
			}
			
			
			Method[] callbacks = parseCallBacks(exp,clazz) ;
			for (int i = 0; i < callbacks.length; i++) {
				addMethod(ee, callbacks[i], true) ;
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
			
//			// Fill in the propertyChanged callback
//			List impl = getExplicitTypeEventMethods(clazz);
//			for (Iterator itr = impl.iterator(); itr.hasNext();) {
//				JCMMethod m = (JCMMethod) itr.next();
//				addMethod(ee,m,false) ;
//			}
			
			if (event.arguments.length == 2) {
				if (event.arguments[0] instanceof StringLiteral) {
					String pname = ((StringLiteral) event.arguments[0]).toString();
					pname = pname.substring(1, pname.length() - 1);
					PropertyEvent pe = JCMFactory.eINSTANCE.createPropertyEvent();
					pe.setPropertyName(pname);
					pe.setUseIfExpression(!isInnerClass(clazz));
					ee.getProperties().add(pe);
				}
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
			fSrcGenerator = new PropChangedAnonymosEventSrcGenerator((AbstractEventInvocation) args[0], (Listener) args[1], fbeanPart.getSimpleName());
		}
		fSrcGenerator.setIndent(fIndentFiller);
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
		if (fEventInvocation.getListener().getListenerType().getExtends() == null) {
			// This is an intefrace, remove the empty stub first
			try {
				parser = parser.removeAnonymousMethod(c.getMethod().getName()) ;
			}
			catch (CodeGenException e) {				
				org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
				return ;
			}
		}		
			
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
		CodeEventRef ref = (CodeEventRef) fOwner.getExprRef();
		EventExpressionParser parser = (EventExpressionParser) ref.getContentParser();
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
		catch (CodeGenException e) {
			org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e);
			return null;
		}
	}
	



	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#getPropertyEventSourceRange(org.eclipse.ve.internal.jcm.PropertyEvent)
	 */
	public ICodeGenSourceRange getPropertyEventSourceRange(PropertyEvent pe) {
		ICodeGenSourceRange sr = null ;
		
		if (pe.isUseIfExpression()) {
			CodeEventRef ref = (CodeEventRef) fOwner.getExprRef();
			EventExpressionParser parser = (EventExpressionParser) ref.getContentParser();
			try {
				sr = parser.getAnonymousMethodProperty(pe.getPropertyName());
				if (sr != null) {
					// Relative offset to the main expression				
					ICodeGenSourceRange Esr = ref.getTargetSourceRange();
					if (Esr == null || sr == null)
						return null;
					CodeGenSourceRange result = new CodeGenSourceRange(Esr.getOffset() + sr.getOffset(), sr.getLength());
					result.setLineOffset(Esr.getLineOffset());
					return result;
				}
			}
			catch (CodeGenException e) {}
		}
		if (sr == null && fEventInvocation.getCallbacks().size()>0) {		
			// No *if* statement, use the callBack's offset
			Callback cb = (Callback) fEventInvocation.getCallbacks().get(0);
			ICodeGenAdapter ca = (ICodeGenAdapter) EcoreUtil.getExistingAdapter(cb, ICodeGenAdapter.JVE_CODE_GEN_TYPE);
			try {
				if (ca != null)
				   return ca.getHighlightSourceRange();
			}
			catch (CodeGenException e) {
				return null ;
			}
		}	
		else {
			try {
				// Inner class for a propertyChange ... use JDT to return offset of the method
				String lName = fEventInvocation.getListener().getListenerType().getName();
				int idx = lName.lastIndexOf('.');
				if (idx > 0)
					lName = lName.substring(0, idx) + "$" + lName.substring(idx + 1, lName.length()); //$NON-NLS-1$
				JavaClass clazz = (JavaClass) org.eclipse.jem.internal.java.impl.JavaClassImpl.reflect(lName, fbeanPart.getModel().getCompositionModel().getModelResourceSet());

				IType iType = getInnerType(clazz);
				if (iType != null) {
					IMethod[] methods = iType.getMethods();
					Method m = (Method) getCallBackList().get(0) ;
					for (int i = 0; i < methods.length; i++) {
						if (m.getName().endsWith(methods[i].getElementName()))
							if (m.getParameters().size() == methods[i].getParameterNames().length) {
								// TODO: Need to compare parameter types as well
								return new CodeGenSourceRange(methods[i].getSourceRange());								
							}

					}
				}
			}
			catch (Exception e) {}
		}
		return null ;	
	}
		

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#addPropertyEvent(org.eclipse.ve.internal.jcm.PropertyEvent)
	 */
	public void addPropertyEvent(PropertyEvent pe) {
		Object[] args = new Object[3];
		args[0] = pe.eContainer(); // MethodInvocation
		args[1] = ((AbstractEventInvocation) args[0]).getListener();
		args[2] = getEventArgName() ;
		String content = ((IPropertyEventSrcGenerator)getSrcGenerator(args)).generatePropertiesBlocks(new PropertyEvent[] { pe });

		CodeEventRef ref = (CodeEventRef) fOwner.getExprRef();
		EventExpressionParser parser = (EventExpressionParser) ref.getContentParser();
//		if (fEventInvocation.getListener().getListenerType().getExtends() == null) {
//			// This is an intefrace, remove the empty stub first
//			try {
//				parser = parser.removeAnonymousMethod(pe.getPropertyName());
//			}
//			catch (CodeGenException e) {
//				org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e);
//				return;
//			}
//		}

		try {
			parser = parser.addPropertyBlock(content);
		}
		catch (CodeGenException e) {
			org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e);
		}
		if (parser != null)
			ref.updateDocument(parser);
		adaptPropertyEvent(pe);

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#removePropertyEvent(org.eclipse.ve.internal.jcm.PropertyEvent)
	 */
	public void removePropertyEvent(PropertyEvent pe) {
		CodeEventRef ref = (CodeEventRef) fOwner.getExprRef();
		EventExpressionParser parser = (EventExpressionParser) ref.getContentParser();
		try {	
				parser = parser.removeAnonymousProperty(pe.getPropertyName()) ;
		}
		catch (CodeGenException e) {
			// If the callback has already been removed, the property
			// will not be found.
			parser = null ;
		}
		unadaptPropertyEvent(pe) ;

		if (parser != null)
			ref.updateDocument(parser);

	}

}

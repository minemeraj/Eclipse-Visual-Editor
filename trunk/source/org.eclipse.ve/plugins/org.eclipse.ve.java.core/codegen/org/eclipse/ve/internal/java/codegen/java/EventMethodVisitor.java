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
 *  $RCSfile: EventMethodVisitor.java,v $
 *  $Revision: 1.2 $  $Date: 2004-02-20 00:44:29 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.compiler.ast.*;

import org.eclipse.jem.internal.beaninfo.EventSetDecorator;

import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @author Gili Mendel
 * 
 * This visitor visits a method, and scan event relationships
 * for a particular bean
 *
 */
public class EventMethodVisitor extends MethodVisitor {

	List 					   fESigs;
	CompilationUnitDeclaration fDom ;
	BeanPart				   fBean ;
	String					   fPrefixConstraint = null ;   // Help ignore most setting expressions up front
	final static String		fDefaultPrefix = "add" ; //$NON-NLS-1$

	public EventMethodVisitor(BeanPart b, IBeanDeclModel model, List signitures, CompilationUnitDeclaration dom) {
		this(b.getInitMethod().getDeclMethod(), b, model, signitures, dom);
	}
	
	public EventMethodVisitor(AbstractMethodDeclaration method, BeanPart b, IBeanDeclModel model, List signitures, CompilationUnitDeclaration dom) {
		this(method, b.getInitMethod(), b, model, signitures, dom) ;
	}
	/**
	 * Overidde CodeMethodRef
	 */	
	public EventMethodVisitor(AbstractMethodDeclaration method, CodeMethodRef m, BeanPart b, IBeanDeclModel model, List signitures, CompilationUnitDeclaration dom) {
		super(method, model, null, m);
		fBean = b ;
		fESigs = signitures;
		fDom = dom ;
		boolean useDefault = true ;
		for (int i=0; i<fESigs.size(); i++) {
		    if (!((EventSetDecorator)fESigs.get(i)).getAddListenerMethod().getName().startsWith(fDefaultPrefix)) {
		    	useDefault = false ;
		    	break ;
		    }		   		    
		}
		if (useDefault) fPrefixConstraint = fDefaultPrefix ;
	}

	/**
	 *   Overide to skip processing constructors
	 */
	protected void processStatementArray(Statement[] statements) throws CodeGenException {
		if (statements != null)
			for (int i = 0; i < statements.length; i++)
				processAStatement(statements[i]);
	}
	
	protected CodeMethodRef getMethodRef (AbstractMethodDeclaration method,CodeTypeRef tRef, String methodHandle,ISourceRange range, String content) {
		Iterator itr = fMethod.getTypeRef().getMethods() ;
		while (itr.hasNext()) {
		   CodeMethodRef m = (CodeMethodRef)itr.next() ;
		   if (methodHandle.equals(m.getMethodHandle()))
		      return m ;
		}
		CodeMethodRef m = new CodeMethodRef(method, tRef, methodHandle, range, content);
		return m ;
	}
	
	protected void processMessageSend(MessageSend stmt) throws CodeGenException {

		// Traverse a method call again
		if (stmt.receiver instanceof ThisReference && !(stmt.receiver instanceof SuperReference)) {
			EventMethodVisitor newVisitor = null;
			if (stmt.arguments == null) {
				// No Arg method call (e.g initConnections()
				String method = new String(stmt.selector);
				AbstractMethodDeclaration methods[] = fDom.types[0].methods;
				IMethod cuMethods[] = TypeVisitor.getCUMethods(methods, CodeGenUtil.getMethods(fModel.getCompilationUnit()), fModel);
				int idx;
				for (idx = 0; idx < methods.length; idx++) {
					if (!(methods[idx] instanceof MethodDeclaration))
						continue;
					MethodDeclaration md = (MethodDeclaration) methods[idx];
					if (md.arguments != null)
						continue;
					if (method.equals(new String(md.selector))) {
						if (!cuMethods[idx].getElementName().equals(new String(md.selector)))
							throw new CodeGenException("Not the same JCMMethod"); //$NON-NLS-1$
						try {
							CodeMethodRef mref = getMethodRef(md, fMethod.getTypeRef(), cuMethods[idx].getHandleIdentifier(), cuMethods[idx].getSourceRange(), cuMethods[idx].getSource());
							newVisitor = new EventMethodVisitor(md, mref, fBean, fModel, fESigs, fDom);
						}
						catch (JavaModelException e) {}
						break;
					}
				}
				// This was not an event, stop processing
				return;
			}
			if (newVisitor != null)
				newVisitor.visit();
		}

		// Most listeners will start with addXXX.  If this is the case for this bean's events
		// check it right now rather than allocating an ExpressionVisitor that will search
		// Event signiture matches.
		if (fPrefixConstraint != null) {
			String selector = new String(stmt.selector);
			if (!selector.startsWith(fPrefixConstraint))
				return;
		}

		if (stmt.receiver instanceof MessageSend) {
			// getBean().addXXX
			// Check to see if the receiver is a bean we care about			
			CodeMethodRef mref = fBean.getReturnedMethod();
			MessageSend ms = (MessageSend) stmt.receiver;
			if (mref != null) {
				if (mref.getMethodName().equals(new String(ms.selector))) {
					new EventExpressionVisitor(fBean, fMethod, (Expression) stmt, fModel, fESigs, fDom).visit();
				}
			}
		}
		else if (stmt.receiver instanceof SingleNameReference ||
		          stmt.receiver instanceof ThisReference) {
			// ivjBean.addXXX()
			if (fBean.getSimpleName().equals(stmt.receiver.toString())) {
				new EventExpressionVisitor(fBean, fMethod, (Expression) stmt, fModel, fESigs, fDom).visit();
			}
		}
		

	}
	
	/**
	 *   Overide to not process local declerations, and return statements
	 */
	protected void processAStatement(Statement stmt) throws CodeGenException {
		// Block Statement
		if (stmt instanceof Block)
			processBlockStatement((Block) stmt);
		// Try Block
		else if (stmt instanceof TryStatement)
			processTryStatement((TryStatement) stmt);
		// If Statement
		else if (stmt instanceof IfStatement)
			processIFStatement((IfStatement) stmt);
		// Synchronized
		else if (stmt instanceof SynchronizedStatement)
			processSynchStatement((SynchronizedStatement) stmt);
	    else if (stmt instanceof MessageSend) 
	        processMessageSend((MessageSend)stmt) ;
				// Handle an Expression          
		else if (stmt instanceof Expression)
			new EventExpressionVisitor(fBean, fMethod, (Expression) stmt, fModel, fESigs, fDom).visit();
		else
			JavaVEPlugin.log("\t[Event] MethodVisitor() skiping: " + stmt, Level.FINE); //$NON-NLS-1$
	}
	
	public String toString() {
		return "EventMethodVisitor("+fBean+","+fMethod.getMethodName()+")" ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}

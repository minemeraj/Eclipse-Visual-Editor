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
 *  $Revision: 1.7 $  $Date: 2004-06-18 18:26:45 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.dom.*;

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

	List 					fESigs;
	CompilationUnit 		fastDom ;
	BeanPart				fBean ;
	String					fPrefixConstraint = null ;   // Help ignore most setting expressions up front
	final static String		fDefaultPrefix = "add" ; //$NON-NLS-1$

	public EventMethodVisitor(BeanPart b, IBeanDeclModel model, List signitures, CompilationUnit dom) {
		this(b.getInitMethod().getDeclMethod(), b, model, signitures, dom);
	}
	
	public EventMethodVisitor(MethodDeclaration method, BeanPart b, IBeanDeclModel model, List signitures, CompilationUnit dom) {
		this(method, b.getInitMethod(), b, model, signitures, dom) ;
	}
	/**
	 * Overidde CodeMethodRef
	 */	
	public EventMethodVisitor(MethodDeclaration method, CodeMethodRef m, BeanPart b, IBeanDeclModel model, List signitures, CompilationUnit dom) {
		super(method, model, null, m);
		fBean = b ;
		fESigs = signitures;
		fastDom = dom ;
		boolean useDefault = true ;
		for (int i=0; i<fESigs.size(); i++) {
		  try {
		  	EventSetDecorator dec = (EventSetDecorator)fESigs.get(i);
		    if (dec.getAddListenerMethod() !=null && dec.getAddListenerMethod().getName().startsWith(fDefaultPrefix)) {
		    	useDefault = false ;
		    	break ;
		    }	
		  }
		  catch (Exception e) {
		  	JavaVEPlugin.log(e);
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
	
	protected CodeMethodRef getMethodRef (MethodDeclaration method,CodeTypeRef tRef, String methodHandle,ISourceRange range, String content) {
		Iterator itr = fMethod.getTypeRef().getMethods() ;
		while (itr.hasNext()) {
		   CodeMethodRef m = (CodeMethodRef)itr.next() ;
		   if (methodHandle.equals(m.getMethodHandle()))
		      return m ;
		}
		CodeMethodRef m = new CodeMethodRef(method, tRef, methodHandle, range, content);
		return m ;
	}
	
	protected void processMessageSend(MethodInvocation stmt) throws CodeGenException {

		// Traverse a method call again
		if (stmt.getExpression() instanceof ThisExpression && !(stmt.getExpression() instanceof SuperMethodInvocation)) {
			EventMethodVisitor newVisitor = null;
			if (stmt.arguments ().size()== 0) {
				// No Arg method call (e.g initConnections()
				String method = stmt.getName().getIdentifier();
				MethodDeclaration methods[] = ((TypeDeclaration)fastDom.types().get(0)).getMethods();
				JavaElementInfo cuMethods[] = TypeVisitor.getCUMethods(methods, CodeGenUtil.getMethodsInfo(fModel.getCompilationUnit()), fModel);
				int idx;
				for (idx = 0; idx < methods.length; idx++) {
					if (!(methods[idx] instanceof MethodDeclaration))
						continue;
					MethodDeclaration md = (MethodDeclaration) methods[idx];
					if (md.parameters().size()>0)
						continue;
					if (method.equals(md.getName().getIdentifier())) {
						if (!cuMethods[idx].getName().equals(md.getName().getIdentifier()))
							throw new CodeGenException("Not the same JCMMethod"); //$NON-NLS-1$						
					    CodeMethodRef mref = getMethodRef(md, fMethod.getTypeRef(), cuMethods[idx].getHandle(), cuMethods[idx].getSourceRange(), cuMethods[idx].getContent());
						newVisitor = new EventMethodVisitor(md, mref, fBean, fModel, fESigs, fastDom);
					
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
			String selector = stmt.getName().getIdentifier();
			if (!selector.startsWith(fPrefixConstraint))
				return;
		}

		if (stmt.getExpression() instanceof MethodInvocation) {
			// getBean().addXXX
			// Check to see if the receiver is a bean we care about			
			CodeMethodRef mref = fBean.getReturnedMethod();
			MethodInvocation ms = (MethodInvocation) stmt.getExpression();
			if (mref != null) {
				if (mref.getMethodName().equals(ms.getName().getIdentifier())) {
					new EventExpressionVisitor(fBean, fMethod, (Statement)stmt.getParent(), fModel, fESigs, fastDom).visit();
				}
			}
		}
		else if (stmt.getExpression() instanceof SimpleName ||
		          stmt.getExpression() instanceof ThisExpression) {
			// ivjBean.addXXX()
			if (fBean.getSimpleName().equals(stmt.getExpression().toString())) {
				new EventExpressionVisitor(fBean, fMethod, (Statement) stmt.getParent(), fModel, fESigs, fastDom).visit();
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
		else if (stmt instanceof ExpressionStatement) {
			ExpressionStatement es = (ExpressionStatement) stmt;
			if (es.getExpression() instanceof MethodInvocation)
				processMessageSend((MethodInvocation)es.getExpression()) ;
			else
//				new EventExpressionVisitor(fBean, fMethod,  stmt, fModel, fESigs, fastDom).visit();	
				JavaVEPlugin.log("\t[Event] MethodVisitor() skiping: " + stmt, Level.FINE); //$NON-NLS-1$
		}
		else
			JavaVEPlugin.log("\t[Event] MethodVisitor() skiping: " + stmt, Level.FINE); //$NON-NLS-1$
	}
	
	public String toString() {
		return "EventMethodVisitor("+fBean+","+fMethod.getMethodName()+")" ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}

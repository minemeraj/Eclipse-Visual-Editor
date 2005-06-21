/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EventExpressionVisitor.java,v $
 *  $Revision: 1.13 $  $Date: 2005-06-21 22:15:43 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.beaninfo.BeanEvent;
import org.eclipse.jem.internal.beaninfo.EventSetDecorator;

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @author Gili Mendel
 *
 */
public class EventExpressionVisitor extends SourceVisitor {



	
CodeMethodRef				fMethod  ;
CodeEventRef				fExpression  ;
BeanPart					fBean ;
CompilationUnit				fastDom ;
List						fEventSigs ;
	

public void initialize(BeanPart b, Statement stmt,IBeanDeclModel model, List eSigs, CompilationUnit dom) {
	super.initialize(stmt,model,null) ;	
	fMethod = b.getInitMethod() ;
	fBean = b ;
	fExpression = new CodeEventRef (stmt,fMethod, dom) ;
	fastDom = dom ;
	fEventSigs = eSigs ;
}

public void initialize(BeanPart b, CodeMethodRef mref, Statement stmt,IBeanDeclModel model, List eSigs, CompilationUnit dom) {
	super.initialize(stmt,model,null) ;	
	fMethod = mref ;
	fBean = b ;
	fExpression = new CodeEventRef (stmt,fMethod, dom) ;
	fastDom = dom ;
	fEventSigs = eSigs ;
}


	
/**
 *  Figure out which BeanPart (if any) this expression is acting on.
 */
protected void processAMessageSend() {
	MethodInvocation stmt = (MethodInvocation) ((ExpressionStatement)fExpression.getExprStmt()).getExpression();
	if (stmt == null)
		return;

	String selector = stmt.getName().getIdentifier();
	AbstractEventInvocation ei ;
	if (selector.equals(PropertyChangedAllocationStyleHellper.DEFAULT_PROPERTY_CHANGED_ADD_METHOD)) {
		// Property Changed signiture
		ei = JCMFactory.eINSTANCE.createPropertyChangeEventInvocation() ;				
	}	
    else {
		EventSetDecorator ed = null;    
		for (int i = 0; i < fEventSigs.size(); i++) {
			try {
				if (((EventSetDecorator) fEventSigs.get(i)).getAddListenerMethod().getName().equals(selector)) {
					ed = (EventSetDecorator) fEventSigs.get(i);
					break;
				}
			} catch (NullPointerException e) {
				// In case of Introspection error, continue
			}
		}
		if (ed == null) {			
			return;
		}
		else {
			// Vanilla Event
			ei = EventInvocationHelper.getNewEventInvocation(fModel.getCompositionModel());
			BeanEvent be = (BeanEvent) ed.getEModelElement();
			((EventInvocation)ei).setEvent(be);			
		}
	}
	fExpression.setBean(fBean);
	fExpression.setEventInvocation(ei);
	fMethod.setModel(fModel);	
	
}



	
/**
 *  Go for it
 */
public void visit(){
	getProgressMonitor().subTask(MessageFormat.format(CodeGenJavaMessages.EventExpressionVisitor_TypeMethodExpression, new Object[]{fMethod.getTypeRef().getSimpleName(), fMethod.getMethodName(), fExpression.getCodeContent()})); 
	if (fExpression.getExprStmt() instanceof ExpressionStatement &&
	    ((ExpressionStatement)fExpression.getExprStmt()).getExpression() instanceof MethodInvocation)
	   processAMessageSend () ;
    else if (JavaVEPlugin.isLoggingLevel(Level.FINE))
       JavaVEPlugin.log ("\t[Event] ExpressionVisitor: *** did not process Expression:"+fExpression, Level.FINE) ; //$NON-NLS-1$

}

public String toString() {
	
	return "EventExpression("+fVisitedNode+")" ; //$NON-NLS-1$ //$NON-NLS-2$
	
}


}

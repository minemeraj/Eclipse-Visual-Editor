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
 *  $RCSfile: EventExpressionVisitor.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.List;

import org.eclipse.jdt.internal.compiler.ast.*;

import org.eclipse.jem.internal.beaninfo.BeanEvent;
import org.eclipse.jem.internal.beaninfo.EventSetDecorator;
import org.eclipse.jem.internal.core.MsgLogger;

import org.eclipse.ve.internal.jcm.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.codegen.model.*;

/**
 * @author Gili Mendel
 *
 */
public class EventExpressionVisitor extends SourceVisitor {



	
CodeMethodRef				fMethod  ;
CodeEventRef				fExpression  ;
BeanPart					fBean ;
CompilationUnitDeclaration	fDom ;
List						fEventSigs ;
	

EventExpressionVisitor(BeanPart b,Statement stmt,IBeanDeclModel model, List eSigs, CompilationUnitDeclaration dom) {
	super((AstNode)stmt,model,null) ;	
	fMethod = b.getInitMethod() ;
	fBean = b ;
	fExpression = new CodeEventRef (stmt,fMethod, dom) ;
	fDom = dom ;
	fEventSigs = eSigs ;
}

EventExpressionVisitor(BeanPart b, CodeMethodRef mref, Statement stmt,IBeanDeclModel model, List eSigs, CompilationUnitDeclaration dom) {
	super((AstNode)stmt,model,null) ;	
	fMethod = mref ;
	fBean = b ;
	fExpression = new CodeEventRef (stmt,fMethod, dom) ;
	fDom = dom ;
	fEventSigs = eSigs ;
}


	
/**
 *  Figure out which BeanPart (if any) this expression is acting on.
 */
protected void processAMessageSend() {
	MessageSend stmt = (MessageSend) fExpression.getExpression();
	if (stmt == null)
		return;

	String selector = new String(stmt.selector);
	AbstractEventInvocation ei ;
	if (selector.equals(PropertyChangedAllocationStyleHellper.DEFAULT_PROPERTY_CHANGED_ADD_METHOD)) {
		// Property Changed signiture
		ei = JCMFactory.eINSTANCE.createPropertyChangeEventInvocation() ;				
	}	
    else {
		EventSetDecorator ed = null;    
		for (int i = 0; i < fEventSigs.size(); i++) {
			if (((EventSetDecorator) fEventSigs.get(i)).getAddListenerMethod().getName().equals(selector)) {
				ed = (EventSetDecorator) fEventSigs.get(i);
				break;
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
	
	if (fExpression.getExpression() instanceof MessageSend)
	   processAMessageSend () ;
    else
       JavaVEPlugin.log ("\t[Event] ExpressionVisitor: *** did not process Expression:"+fExpression, MsgLogger.LOG_FINE) ; //$NON-NLS-1$

}

public String toString() {
	
	return "EventExpression("+fVisitedNode+")" ; //$NON-NLS-1$ //$NON-NLS-2$
	
}


}

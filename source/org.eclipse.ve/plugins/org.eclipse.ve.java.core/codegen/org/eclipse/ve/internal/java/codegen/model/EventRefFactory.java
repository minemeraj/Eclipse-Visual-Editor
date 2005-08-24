/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EventRefFactory.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:30:47 $ 
 */
package org.eclipse.ve.internal.java.codegen.model;

import java.util.Iterator;
import java.util.logging.Level;

import org.eclipse.ve.internal.jcm.AbstractEventInvocation;
import org.eclipse.ve.internal.jcm.Listener;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;

/**
 * @author Gili Mendel
 *
 */
public class EventRefFactory  {

    BeanPart				fBeanPart ; 
    CodeEventRef			fExpr = null ;
    AbstractEventInvocation	fEi ;
                  
    
   public  EventRefFactory (BeanPart bean, AbstractEventInvocation ei) {
   	fBeanPart = bean ;
   	fEi = ei ;
   }
   
   
protected boolean areEqual(Object a, Object b) {
	if (a instanceof Object[]) {
		if (b instanceof Object[]) {
			Object[] aa = (Object[]) a;
			Object[] ba = (Object[]) b;
			if (aa.length != ba.length)
				return false;
			else {
				for (int i = 0; i < ba.length; i++) {
					if (!areEqual(aa[i], ba[i]))
						return false;
				}
				return true;
			}
		}
		else
		  return false ;
	}
	else {
		if (b instanceof Object[]) {
			return false ;
		}
		else {
			if (a == null) {
				if (b == null)
					return true;
				return false;
			}
			else {
				if (b == null)
					return false;
				else
					return a.equals(b);
			}
		}
	}
             
}   
/**
 *  Look for an existing Expression in the BDM
 */
public CodeEventRef getExistingExpression(Object[] args) {
   	if (fExpr != null)  return fExpr ;

		
	Iterator itr = fBeanPart.getRefEventExpressions().iterator() ;
	while (itr.hasNext()) {
		CodeEventRef exp = (CodeEventRef) itr.next() ;
		if (exp.getEventDecoder() != null &&
		    areEqual(exp.getEventDecoder().getEventInvocation(),fEi) &&		   
		    areEqual(exp.getArgs(),args)) {
		    	fExpr = exp ;
		    	break ;
		}
	}
	return fExpr ;
}
   	
	public CodeEventRef createFromJVEModel() throws CodeGenException {

		Listener l = fEi.getListener() ;		
		Object[] args = new Object[] { fEi, l } ; 
		  
		if (fExpr == null) {
			if (getExistingExpression(args) != null)
				throw new CodeGenException("Expression already exists"); //$NON-NLS-1$

			CodeMethodRef mr = fBeanPart.getEventInitMethod();
			CodeEventRef exp = new CodeEventRef(mr, fBeanPart);
			exp.setArguments(args);
			//exp.setState(exp.STATE_EXIST) ;
			exp.clearState();
			exp.setState(CodeExpressionRef.STATE_EXIST, true);
			exp.generateSource(fEi);
			if ((!exp.isAnyStateSet()) || exp.isStateSet(CodeExpressionRef.STATE_DELETE)) //exp.getState() == exp.STATE_NOT_EXISTANT)
				return null;

			fExpr = exp;
			try {
				mr.updateExpressionOrder();
			}
			catch (Throwable e) {
				JavaVEPlugin.log(e, Level.WARNING);
//				Iterator itr = mr.getExpressions();
//				CodeExpressionRef prev = null;
//
//				while (itr.hasNext()) {
//					CodeExpressionRef x = (CodeExpressionRef) itr.next();
//
//					if (x.equals(exp))
//						break;
//					prev = x;
//				}
//				if (prev == null)
//					prev = (CodeExpressionRef) itr.next();
//				exp.setOffset(prev.getOffset() + prev.getLen());
//
//				CDEHack.fixMe("remove this try"); //$NON-NLS-1$

				return fExpr;
			}
		}
		return fExpr ;
	}
}

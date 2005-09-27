/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.codegen.java;
/*
 *  $RCSfile: CodeSnippetMergelet.java,v $
 *  $Revision: 1.13 $  $Date: 2005-09-27 15:12:09 $ 
 */

import java.util.*;
import java.util.logging.Level;

import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class CodeSnippetMergelet {

	
	ICodeDelta     fBDMDelta ;
	String         fElementContent=null ;
	String         fElementHandle=null ;
	boolean        fisMethod ;
	IBeanDeclModel fBeanModel = null ;
	HashMap		   fExpressionsByMethod = new HashMap() ;
	

 


public CodeSnippetMergelet (ICodeDelta delta, String elementContent, String elementHandle, boolean isMethodElement) {
	fBDMDelta = delta ;
	fElementContent = elementContent ;
	fElementHandle = elementHandle ;
	fisMethod = isMethodElement ;
}


private CodeMethodRef getOriginalMethodElement() throws CodeGenException {
	if (!fisMethod) throw new CodeGenException ("Invalid Context") ;	 //$NON-NLS-1$
	return fBeanModel.getMethodInitializingABean(fElementHandle) ;	
}

private List getExpressions(CodeMethodRef m) {
	List l = (List) fExpressionsByMethod.get(m) ;
	if (l == null) {
		l = new ArrayList() ;
		fExpressionsByMethod.put(m,l) ;
		Iterator itr = m.getAllExpressions() ;
		while (itr.hasNext())
		    l.add(itr.next()) ;					
	}
	return l;
}
/**
 * Get the corresponding expression
 */
private CodeExpressionRef getOriginal(CodeExpressionRef exp) throws CodeGenException {
	if (!fisMethod)
		throw new CodeGenException("Invalid Context"); //$NON-NLS-1$

	CodeMethodRef m = getOriginalMethodElement();
	if (m == null)
		return null;
	Iterator itr = getExpressions(m).iterator();
	CodeExpressionRef result = null;
	// Look for the best matched expression
	while (itr.hasNext()) {
		CodeExpressionRef e = (CodeExpressionRef) itr.next();
		if (e == exp) {
			result = e;
			break;
		}
		else {
			int eqv = e.isEquivalent(exp);
			if (eqv < 0) {
				// no match
				continue;
			}
			else {

				if (eqv == 1) {
					// Found a match, stop looking
					result = e;
					break;
				}
				else
				  if (result==null)
				     // get the first partial match
				     result = e ;
			}

		}
	}
	if (result != null) {
		// Do not consider this expression anymore
		getExpressions(m).remove(result);
		return result;
	}
	return null;
}


/**
 * Get the coresponding BeanPart
 */
private BeanPart getOriginal (BeanPart b) {
	Iterator itr = fBeanModel.getBeans(false).iterator() ;
	while (itr.hasNext()) {
		BeanPart bean = (BeanPart) itr.next() ;
		if (bean.isEquivalent(b)) 
			if(bean.getUniqueName().equals(b.getUniqueName()))
				return bean ;
	}
	return null ;
}

/**
 *  Generate an expression that is hooked into the main BDM
 */
private CodeExpressionRef createNewExpression(CodeExpressionRef e, CodeMethodRef m, boolean decode) throws CodeGenException{
    if (getOriginal(e) != null) throw new CodeGenException("duplicate Expression") ; //$NON-NLS-1$
    BeanPart b = getOriginal(e.getBean()) ;
    if (b == null) throw new CodeGenException("No Bean Part") ; //$NON-NLS-1$
    ExpressionRefFactory gen = new ExpressionRefFactory(b,null) ;
    CodeExpressionRef newe = gen.createFromSource(e,m) ;
    newe.setState(CodeExpressionRef.STATE_NO_MODEL, e.isStateSet(CodeExpressionRef.STATE_NO_MODEL));
    newe.setState(CodeExpressionRef.STATE_INIT_EXPR, e.isStateSet(CodeExpressionRef.STATE_INIT_EXPR));
    if (decode) {
       if (!newe.decodeExpression()) {
         newe.dispose() ;
         newe = null ;
       }
    }
    return newe ;
}

/**
 *  Generate an expression that is hooked into the main BDM
 */
private CodeEventRef createNewEventExpression(CodeEventRef e, CodeMethodRef m, boolean decode) throws CodeGenException{
	if (getOriginal(e) != null) throw new CodeGenException("duplicate Expression") ; //$NON-NLS-1$
	BeanPart b = getOriginal(e.getBean()) ;
	if (b == null) throw new CodeGenException("No Bean Part") ; //$NON-NLS-1$
	CodeEventRef newe = new CodeEventRef(m, b);
	newe.setState(CodeExpressionRef.STATE_NO_MODEL, e.isStateSet(CodeExpressionRef.STATE_NO_MODEL));
	newe.setState(CodeExpressionRef.STATE_INIT_EXPR, e.isStateSet(CodeExpressionRef.STATE_INIT_EXPR));
	newe.setState(CodeExpressionRef.STATE_SRC_LOC_FIXED, true); //fexpStmt.getState() | fexpStmt.STATE_SRC_LOC_FIXED) ;
	newe.setExprStmt(e.getExprStmt()) ;
	newe.setContent(e.getContentParser()) ;
	newe.setOffset(e.getOffset()) ;
 	newe.setEventInvocation(e.getEventInvocation());
 	if (decode) {
	   if (!newe.decodeExpression()) {
		 newe.dispose() ;
		 newe = null ;
	   }
	}
	return newe ;
}

	private boolean processExpressionDelta(CodeExpressionRef dExp, CodeExpressionRef oExp, CodeMethodRef oMethod, int status) throws CodeGenException {

		boolean updated = false;
		if (oExp != null && dExp != null && !dExp.isStateSet(CodeExpressionRef.STATE_EXP_IN_LIMBO)) {
			if (oExp.isEquivalent(dExp) < 0)
				throw new CodeGenException("No the same Expressions"); //$NON-NLS-1$
			oExp.setExprStmt(dExp.getExprStmt());
		}
		if (oExp != null && dExp != null)
			oExp.updateLimboState(dExp);
		if (oExp == null && status != ICodeDelta.ELEMENT_ADDED && status != ICodeDelta.ELEMENT_UNDETERMINED) {
			if (JavaVEPlugin.isLoggingLevel(Level.FINE))
				JavaVEPlugin.log("CodeSnippetMergelent.processExpressionDelta(): could not find" + dExp, //$NON-NLS-1$
						Level.FINE);
		}
		switch (status) {
			case ICodeDelta.ELEMENT_UNDETERMINED:
				if (JavaVEPlugin.isLoggingLevel(Level.FINE))
					JavaVEPlugin.log("CodeSnippetMergelent.processExpressionDelta() In Limbo: " + oExp, //$NON-NLS-1$
							Level.FINE);
				if (oExp != null)
					oExp.setState(CodeExpressionRef.STATE_EXP_IN_LIMBO, true);

				// VCE model is not updated
				break;
			case ICodeDelta.ELEMENT_UPDATED_OFFSETS:
				oExp.setOffset(dExp.getOffset());
				// No need to refresh when a shadow expression
				// We also do not care about event ordering
				if (!(dExp instanceof CodeEventRef))
					// Will take care of reordering of expressions
					oExp.refreshFromJOM(dExp);
				break;
			case ICodeDelta.ELEMENT_NO_CHANGE:
				if (oExp != null)
					oExp.setContent(dExp.getContentParser());
				break;
			case ICodeDelta.ELEMENT_CHANGED:
				if (oExp != null) {
					if (oExp.isStateSet(CodeExpressionRef.STATE_INIT_EXPR)) {
						// Change in a "new" statement we can not refresh at this point
						fBeanModel.setState(IBeanDeclModel.BDM_STATE_DOWN, true);
						return false;
					}
					if (oExp.getOffset() != dExp.getOffset())
						oExp.setOffset(dExp.getOffset());
					oExp.refreshFromJOM(dExp);
					updated = true;
				}
				break;
			case ICodeDelta.ELEMENT_DELETED:
				if (oExp != null) {
					if (oExp.isStateSet(CodeExpressionRef.STATE_INIT_EXPR)) {
						// Change in a "new" statement we can not refresh at this point
						fBeanModel.setState(IBeanDeclModel.BDM_STATE_DOWN, true);
						return false;
					}
					oExp.dispose();
					updated = true;
				}
				break;
			case ICodeDelta.ELEMENT_ADDED:
				try {
					if (dExp.isStateSet(CodeExpressionRef.STATE_INIT_EXPR)) {
						// Change in a "new" statement we can not refresh at this point
						fBeanModel.setState(IBeanDeclModel.BDM_STATE_DOWN, true);
						return false;
					}
					CodeExpressionRef newExp = createNewExpression(dExp, oMethod, !dExp.isStateSet(CodeExpressionRef.STATE_NO_MODEL));//((dExp.getState()
																																	  // &
																																	  // dExp.STATE_NO_OP)
																																	  // !=
																																	  // dExp.STATE_NO_OP))
																																	  // ;
					if (newExp == null && dExp instanceof CodeEventRef)
						newExp = createNewEventExpression((CodeEventRef) dExp, oMethod, !dExp.isStateSet(CodeExpressionRef.STATE_NO_MODEL));
					updated = true;
				} catch (CodeGenException e) {
				}
				; // Do not create dup expressions
				break;
			default:
				throw new CodeGenException("Invalid Status"); //$NON-NLS-1$
		}
		return updated;
	}


/**
 * JCMMethod is already in the BDM, update changes to its content
 */
private boolean updateMethodDelta (CodeMethodRef m, List deleteList) throws CodeGenException {

	CodeMethodRef     oMethod = getOriginalMethodElement() ;   // Original method
	
	boolean  updated = false ;
      
    // Update:
    // If the methodRef is in state ELEMENT_UNDETERMINED, then it has a parse
    // error, and some of the expressions dont know where they are. Since
    // some of them DO know where they are, we have to update those
    // expression offsets. (also when the method is in ELEMENT_UNDETERMINED state
    // we are guaranteed of having a one-to-one relationship between the old
    // and new expressions).
	if((fBDMDelta.getElementStatus(m)&ICodeDelta.ELEMENT_UNDETERMINED)==ICodeDelta.ELEMENT_UNDETERMINED){  // Plan B
		List deltaExpressions = new ArrayList();
		List oldExpressions = new ArrayList();
		Iterator newItr = m.getExpressions() ;
		Iterator oldItr = oMethod.getExpressions();
		while(newItr.hasNext())	
			deltaExpressions.add(newItr.next());
		while(oldItr.hasNext())
			oldExpressions.add(oldItr.next());
		// Need to move expressions to new Lists , or else
		// there will be java.util.ConcurrentModificationExceptions
		// thrown when expressions are removed from methods.
		newItr = deltaExpressions.iterator();
		oldItr = oldExpressions.iterator();
		while (newItr.hasNext() && oldItr.hasNext()) {
			CodeExpressionRef dExp = (CodeExpressionRef) newItr.next() ; // Delta exp
			CodeExpressionRef oExp = (CodeExpressionRef) oldItr.next();  // Original corresponding exp
			updated |= processExpressionDelta(dExp,oExp,oMethod,fBDMDelta.getElementStatus(dExp)) ;
		}
	}else{
		// Delete 
		Iterator itr ;
		if (deleteList != null) {   
			itr = deleteList.iterator() ;
			while (itr.hasNext()) {
				CodeExpressionRef dExp = (CodeExpressionRef) itr.next() ; // Delta exp	 
				CodeExpressionRef oExp = dExp;           // Original exp is given by the delta
				updated |= processExpressionDelta(dExp,oExp,oMethod,fBDMDelta.getElementStatus(dExp)) ;
				if (fBeanModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return updated ;
			}
		}
          
		itr = m.getAllExpressions() ;
		while (itr.hasNext() && !fBeanModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) {		
			CodeExpressionRef dExp = (CodeExpressionRef) itr.next() ; // Delta exp	 
			CodeExpressionRef oExp = getOriginal(dExp) ;              // Original exp
			updated |= processExpressionDelta(dExp,oExp,oMethod,fBDMDelta.getElementStatus(dExp)) ;
		}
	}
	return updated ;
	
}



/**
 *  Assume a single element is to be merged (JCMMethod/Instance Var.)
 * @return boolean if any update was made to the model
 */
public boolean updateBDM(IBeanDeclModel model) throws CodeGenException {
	
	if (model == null || fElementHandle == null) throw new CodeGenException("Invalid Arg") ; //$NON-NLS-1$
		
	fBeanModel = model ;
	
	boolean   modelUpdated = false ;
	
	CodeMethodRef mref = fBDMDelta.getDeltaMethod() ;
	if (mref != null) { // JCMMethod Update
	  switch (fBDMDelta.getElementStatus(mref)) {
	 	case ICodeDelta.ELEMENT_NO_CHANGE:
	 	     break ;
		case ICodeDelta.ELEMENT_ADDED:		   
		     // createNewMethod(mref,Offset) ;
		     // At this time induce the model to be reloaded.
		     fBeanModel.setState(IBeanDeclModel.BDM_STATE_DOWN,true) ;
		     modelUpdated = true ;
		     break ;
		case ICodeDelta.ELEMENT_DELETED:
		     // deleteExistingMethod(mref) ;
		     // At this time induce the model to be reloaded.
		     fBeanModel.setState(IBeanDeclModel.BDM_STATE_DOWN,true) ;
		     modelUpdated = true ;
		     break ;
		case ICodeDelta.ELEMENT_UNDETERMINED:
		case ICodeDelta.ELEMENT_UPDATED_OFFSETS:
		case ICodeDelta.ELEMENT_CHANGED:
		     modelUpdated |= updateMethodDelta(mref,fBDMDelta.getDeletedElements(mref)) ;
		     // Check if we need to reLoad 
		     if (fBeanModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return modelUpdated ;
             CodeMethodRef oriMref = getOriginalMethodElement() ;
		     BeanPartFactory.fixOffsetIfNeeded(fElementContent,oriMref) ;
		     break ;
		default:
		     throw new CodeGenException("Invalid State") ; //$NON-NLS-1$
	  }
	}
	else { // Instance Variable update
	 	// TBD
		if (JavaVEPlugin.isLoggingLevel(Level.WARNING))
			JavaVEPlugin.log("CodeSnippetMerglent.updateBDM() : no CodeMethodRef", Level.WARNING) ; //$NON-NLS-1$
	}
	return modelUpdated ;
}

}

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
 *  $RCSfile: CodeCallBackRef.java,v $
 *  $Revision: 1.3 $  $Date: 2004-02-03 20:11:36 $ 
 */
package org.eclipse.ve.internal.java.codegen.model;

import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.Statement;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.codegen.java.IJVEDecoder;
import org.eclipse.ve.internal.java.codegen.java.PropertyChangedInnerStyleHelper;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.ExpressionParser;

/**
 * @author Gili Mendel
 *
 */
public class CodeCallBackRef extends CodeExpressionRef {

    protected	IJVEDecoder	fDecoder     = null ; 

	public CodeCallBackRef(Statement exp, CodeMethodRef method) {
		super(exp, method);
	}
		
	 /**
 *  Find a decoder for this expression
 *  Temporary until we properly set up the decoder factory
 */
public IJVEDecoder  getEventDecoder(){
	
	if (fDecoder != null) return fDecoder ;

    if (PropertyChangedInnerStyleHelper.parsePropertyFromIfStatement((IfStatement)fExpr) != null)    
      fDecoder = new PropertyEventDecoder() ;
    else
      fDecoder = new CallBackDecoder() ;    	
    fDecoder.setBeanModel(fBean.getModel()) ;
    fDecoder.setBeanPart(fBean) ;
    fDecoder.setCompositionModel(fBean.getModel().getCompositionModel()) ;
    try {
		fDecoder.setExpression((CodeExpressionRef)this) ;
	}
	catch (CodeGenException e) {
		JavaVEPlugin.log(e) ;
	}
    
    return fDecoder ;
 
}

/**
 *  Decode this. expression 
 */
public synchronized boolean decodeExpression() throws CodeGenException {

	// If it is already in MOF, no need to create it again.
	if ((!isAnyStateSet()) || isStateSet(STATE_NOT_EXISTANT)) // ((fState&~STATE_SRC_LOC_FIXED) != STATE_NOT_EXISTANT) 
		return true;

	if (isStateSet(STATE_NO_MODEL))
		return true;

	if (getEventDecoder() != null) // this may not aloways have a decoder (snippet)
		return getEventDecoder().decode();
	else
		return false;
}

public void setBean(BeanPart bean) {
	fBean = bean;
	fMethod.addExpressionRef(this);
	bean.addCallBackExpression(this);
}

public void dispose() {
	fMethod.removeExpressionRef(this);
	fBean.removeCallBackExpression(this);
}

public synchronized void refreshFromComposition() throws CodeGenException {
	
	if ((!isAnyStateSet()) || 
		(isStateSet(STATE_NOT_EXISTANT))){
		   // Clear the expression's content
		   clearState();
		   setState(STATE_NOT_EXISTANT, true) ;
		   setContent((ExpressionParser) null) ;
		   return ;
		}

	if(isStateSet(STATE_NO_MODEL))     //((fState & STATE_NO_OP) > 0)
		return ;	
	
	if (fDecoder == null) throw new CodeGenException ("No Decoder") ; //$NON-NLS-1$
	
	if (fDecoder.isDeleted()) {
		clearState();
		setState(STATE_NOT_EXISTANT, true) ;
		setContent((ExpressionParser) null) ;
		return ;
	}
//	String curContent = ExpressionTemplate.getExpression(fDecoder.reflectExpression(getCodeContent())) ;
//	if (!curContent.equals(getCodeContent())) {	
//	  setCodeContent(curContent) ;
//	  setOffset(-1) ;		
//	}	  
	setState(STATE_IN_SYNC, true);  // fState |= STATE_IN_SYNC|STATE_EXIST ;
	setState(STATE_EXIST, true);
}

}

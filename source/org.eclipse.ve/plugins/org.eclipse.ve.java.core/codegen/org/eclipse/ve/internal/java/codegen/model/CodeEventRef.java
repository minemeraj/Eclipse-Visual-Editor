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
 *  $RCSfile: CodeEventRef.java,v $
 *  $Revision: 1.19 $  $Date: 2005-08-29 21:38:20 $ 
 */
package org.eclipse.ve.internal.java.codegen.model;

import java.util.Iterator;
import java.util.logging.Level;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.jcm.AbstractEventInvocation;

import org.eclipse.ve.internal.java.codegen.java.IEventDecoder;
import org.eclipse.ve.internal.java.codegen.java.IJVEDecoder;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @author Gili Mendel
 *
 */
public class CodeEventRef extends CodeExpressionRef {
	
	private 	AbstractEventInvocation		fEventInvocation = null ;
	protected	IEventDecoder				fDecoder     = null ; 

	public CodeEventRef(Statement exp, CodeMethodRef method, CompilationUnit dom) {
		super(exp, method);		
	}
	
	public CodeEventRef (CodeMethodRef method,BeanPart bean) {	
	   super(method,bean) ;	        	   
    }


	/**
	*  setters/getters
	*/
	public void setBean(BeanPart bean) {
		fBean = bean;
		fMethod.addEventExpressionRef(this);
		bean.addEventExpression(this);
		bean.addEventInitMethod(fMethod) ;
	}
	

	/**
	 * @see org.eclipse.ve.internal.java.codegen.model.CodeExpressionRef#dispose()
	 */
	public void dispose() {
	  if (fMethod != null)
	    fMethod.removeEventRef(this) ;		

	    if (fBean != null)
	      fBean.removeEventExpression(this) ;
		super.dispose();
	}

	
/**
 * @return true if there is already equivalent expression
 */
protected boolean isDuplicate() {
	boolean result = false ;
	
	Iterator itr = fBean.getRefEventExpressions().iterator() ;
	while (itr.hasNext()) {
		CodeEventRef e = (CodeEventRef) itr.next();
		if (e == this) {
			return false ;
		}
		try {
		 if (e.isEquivalent(this)>=0) 
		     return true ;
		}
		catch (CodeGenException exp) {
			if (JavaVEPlugin.isLoggingLevel(Level.FINEST))
				JavaVEPlugin.log("CodeExpressionRef.isDuplicate():  can not determine: "+this) ;	 //$NON-NLS-1$
		}
	}	
	return result ;
}

public void setDecoder(IEventDecoder decoder) {
	if (fDecoder == decoder) return  ;
	
    if (fDecoder != null) fDecoder.dispose() ;
	fDecoder = decoder ;
}

 /**
 *  Find a decoder for this expression
 *  Temporary until we properly set up the decoder factory
 */
protected IEventDecoder  getEventDecoder(){
	
	if (fDecoder != null) return fDecoder ;
	
	fDecoder = CodeGenUtil.getDecoderFactory(fBean.getModel()).getEventDecoder((IJavaInstance)fBean.getEObject()) ;
	// Factory new the object using the default constructor
    fDecoder.setBeanModel(fBean.getModel()) ;
    fDecoder.setBeanPart(fBean) ;
    fDecoder.setCompositionModel(fBean.getModel().getCompositionModel()) ;
    try {
		fDecoder.setExpression(this) ;
	}
	catch (CodeGenException e) {
		JavaVEPlugin.log(e) ;
	}
    fDecoder.setEventInvocation(fEventInvocation) ;
    
    return fDecoder ;
 
}

	
/**
 *  Decode this. expression 
 */
public boolean  decodeExpression() throws CodeGenException {
    
      // If it is already in MOF, no need to create it again.
      if ((!isAnyStateSet()) || isStateSet(STATE_DELETE))  
      	return true ;
      
      if(isStateSet(STATE_NO_MODEL))
      	return true;
      
           
      // TODO Need to be able and work with a IProgressMonitor
      if(getEventDecoder()!=null) {// this may not aloways have a decoder (snippet)
      	  boolean result = getEventDecoder().decode() ;
      	  if (result)
      	     getEventDecoder().setFiller(fContentParser.getFiller()) ;       
	      return  result ;
      }
	else
		return false;
}

	/**
	 * Returns the eventDecorator.
	 * @return EventSetDecorator
	 */
	public AbstractEventInvocation getEventInvocation() {
		return fEventInvocation;
	}

	/**
	 * Sets the eventDecorator.
	 * @param eventDecorator The eventDecorator to set
	 */
	public void setEventInvocation(AbstractEventInvocation ei) {
		
		if (fDecoder != null) {
			try {
				fDecoder.setExpression(this);
			}
			catch (CodeGenException e) { JavaVEPlugin.log(e); }		
			// A decoder may not need to replace its EventInvocation
			if (fDecoder.setEventInvocation(ei))
				fEventInvocation = ei;
		}
		else
			fEventInvocation = ei;
	}
	
	/**
 *  Generate this. expression 
 */
public String generateSource(AbstractEventInvocation ei) throws CodeGenException {

	if (!isStateSet(STATE_EXIST))
		return null;

	String result = getEventDecoder().generate(ei, fArguments);
	if (result == null)
		return result;

	String e = ExpressionTemplate.getExpression(result);
	int offset = result.indexOf(e) ;
	ExpressionParser p = createExpressionParser(result, offset, e.length(), getEventDecoder().getBeanModel().getScannerFactory());
	setContent(p);
	setOffset(-1);
	fEventInvocation = ei;
	getEventDecoder().setEventInvocation(fEventInvocation) ;
	try {
	   refreshAST();
	   getEventDecoder().setStatement(fexpStmt);
	}
	catch (Exception e1) {
//		  JavaVEPlugin.log(e1) ;
	}
	return result;
}

/**
 * Event decoders will have to generate, as they may or may not be multi lined
 * expression.
 */
public void setFillerContent(String filler) throws CodeGenException {
	fDecoder.setFiller(filler);
	generateSource(fEventInvocation);
}

/* (non-Javadoc)
 * @see org.eclipse.ve.internal.java.codegen.model.CodeExpressionRef#createExpressionParser(java.lang.String, int, int)
 */
protected ExpressionParser createExpressionParser(String sourceSnippet, int expOffset, int expLen, IScannerFactory scannerFactory) {
	return new EventExpressionParser(sourceSnippet, expOffset, expLen, scannerFactory);
}

public void refreshFromComposition() throws CodeGenException {
	
	if ((!isAnyStateSet()) || 
		(isStateSet(STATE_DELETE))){
		   // Clear the expression's content
		   clearState();
		   setState(STATE_DELETE, true) ;
		   setContent((ExpressionParser) null) ;
		   return ;
		}

	if(isStateSet(STATE_NO_MODEL))     //((fState & STATE_NO_OP) > 0)
		return ;	
	
	if (fDecoder == null) throw new CodeGenException ("No Decoder") ; //$NON-NLS-1$
	
	if (fDecoder.isDeleted()) {
		clearState();
		setState(STATE_DELETE, true) ;
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

	/* 
	 * Event expressions do not control or undestand much of their content. Ordering goes by the order of definition,
	 * (no indexes).. so equivalent is only superficial.  It is the caller's responsibility to resolve identity 
	 * collisions (where there are more than one listener for a given event
	 */
	public int isEquivalent(AbstractCodeRef code) throws CodeGenException {
		if (code instanceof CodeEventRef) {
			CodeEventRef exp1 = (CodeEventRef) code;

			if (exp1.equals(this))
				return 1;
				
			if (exp1.getMethod() == null || exp1.getMethod().getMethodHandle()==null)
			   return -1 ;
			if (!exp1.getMethod().getMethodHandle().equals(this.getMethod().getMethodHandle()))
			   return -1 ;

			if (getBean() == null && exp1.getBean() != null)
				return -1;
			else if (getBean() != null && exp1.getBean() == null)
				return -1;

			boolean beanNameEquivalency = getBean().getSimpleName().equals(exp1.getBean().getSimpleName());

			String expc1 = exp1.getMethodNameContent();
			String expc2 = getMethodNameContent();
			boolean expEquivalency = expc1.equals(expc2);

			if (beanNameEquivalency && expEquivalency) {

				if (isStateSet(STATE_NO_MODEL))
					if (exp1.isStateSet(STATE_NO_MODEL)) {
						if (getCodeContent().equals(exp1.getCodeContent()))
							return 1;
						else
							return 0;
					}
					else
						return -1;
				else if (exp1.isStateSet(STATE_NO_MODEL))
					return -1;

				if (getCodeContent().equals(exp1.getCodeContent()))
					return 1; // Identical
				else
					return 0; // Same expression, but needs update

			}
		}
		return -1;
	}
 	
	protected IJVEDecoder primGetDecoder() {
	   return getEventDecoder() ;
	}

	/* (non-Javadoc)
	 * @see com.ibm.etools.jbcf.codegen.model.CodeExpressionRef#refreshFromJOM(com.ibm.etools.jbcf.codegen.model.CodeExpressionRef, boolean)
	 */
	public void refreshFromJOM(CodeExpressionRef exp) {
		// Reset the event invocation - it may instigate a decoder to reset its helper
		if (exp.getExprStmt() != null)
			setExprStmt(exp.getExprStmt());
		setEventInvocation(((CodeEventRef)exp).getEventInvocation()) ;
		super.refreshFromJOM(exp);
	}

	public void setExprStmt(Statement statement) {
		super.setExprStmt(statement);
		if(fDecoder!=null)
			fDecoder.setStatement(statement);
	}

}

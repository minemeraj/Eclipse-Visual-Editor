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
 *  $RCSfile: IEventDecoderHelper.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import org.eclipse.jdt.internal.compiler.ast.Statement;

import org.eclipse.ve.internal.jcm.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;

/**
 * @author Gili Mendel
 *
 */
public interface IEventDecoderHelper {

	// Returns the priority of the expression this 
	// decoder helper is the helper of.
	public Object getPriorityOfExpression();
	
   // A decode method will decode specific expressions.
   // If the expression was decoded, a true will be returned.
   // if a false is returned, the expression does not belong to
   // this decoder.
   public boolean decode () throws CodeGenException ;
   // Generate expression source.  Args may or may not be null,
   // depending on the expression type
   public String generate(Object[] args) throws CodeGenException ;
   
   // Delete the existing VCE model element associated with this decoder.
   public void delete() ;
   
   
/**
 *  Reflect the current value in the Composition Model 
 *  @param exprSig represent the current expression signiture in the source code
 *  @return a string representing the expression as reflected from the Compotision
 */   
   public String primRefreshFromComposition(String exprSig) throws CodeGenException ;
   
/**
 *  Is this feature still part the composition
 *  @return a boolean denoting the existance of this feature in the composition
 */
   public boolean primIsDeleted() ;

   public String getCurrentExpression() ;
   
   public void adaptToCompositionModel(IEventDecoder decoder) ;
   public void unadaptToCompositionModel() ;
   /**
    *  If this expression is implicit, should the generate() be called ?
    *  Default is no, helpers should overide this otherwise.
    */
   public void setDecodingContent (Statement exp) ;
   
   
   	public AbstractEventInvocation getEventInvocation() ;

	/**
	 * Sets the eventDecorator.
	 * @param eventDecorator The eventDecorator to set
	 */
	public void setEventInvocation(AbstractEventInvocation ei) ;
	
   public void   setFiller(String filler) ;
   public String getFiller(String filler) ;
   
   public void removeCallBack(Callback c) ;
   public void addCallBack(Callback c) ;
   public void addPropertyEvent(PropertyEvent c) ;
   public void removePropertyEvent(PropertyEvent c) ;
   
   public ICodeGenSourceRange getCallBackSourceRange(Callback c) ;
   public ICodeGenSourceRange getPropertyEventSourceRange(PropertyEvent pe) ;

}

package org.eclipse.ve.internal.java.codegen.java;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IExpressionDecoderHelper.java,v $
 *  $Revision: 1.3 $  $Date: 2004-03-05 23:18:38 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.ve.internal.java.codegen.util.CodeGenException;


/**
 *  A Decoder Helper is designed to deal a with specific type of expressions.
 *  The IExpressionDecoder will provide the logic to determine which helper 
 *  to instanciate.
 */
public interface IExpressionDecoderHelper {

	
	/**
	 *  Returns the priority of the expression this decoder helper is the helper of.
	 * @see IJVEDecoder.deteminePriorityOfExpression()
	 */
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
   public void removeFromModel() ;
   
   
/**
 *  Reflect the current value in the Composition Model 
 *  @param exprSig represent the current expression signiture in the source code
 *  @return a string representing the expression as reflected from the Compotision
 */   
   public String primRefreshFromComposition(String exprSig) throws CodeGenException ;
   
   /** 
	* Should return whether this helper can handle the change in MOF. Typically
	* it can be checked before calling primRefreshFromComposition(). If TRUE, 
	* then this decoder can reflectMOFChange() correcty. If FALSE, this decoder 
	* cannot handle the values in MOF.
	* 
	*/ 
   public boolean canRefreshFromComposition();
   
/**
 *  Is this feature still part the composition
 *  @return a boolean denoting the existance of this feature in the composition
 */
   public boolean primIsDeleted() ;
   
   public boolean isImplicit(Object args[]) ;
   
   public String getCurrentExpression() ;
   
   public void adaptToCompositionModel(IExpressionDecoder decoder) ;
   public void unadaptToCompositionModel() ;
   /**
    *  If this expression is implicit, should the generate() be called ?
    *  Default is no, helpers should overide this otherwise.
    */
   public boolean isGenerateOnImplicit() ;
   public void setDecodingContent (Statement exp) ;
   public Object[] getArgsHandles(Statement exp) ;
   public Object[] getAddedInstance() ;
   // is a change in this sf impact this decoder
   public boolean isRelevantFeature (EStructuralFeature sf) ;
   
   
   
   
}
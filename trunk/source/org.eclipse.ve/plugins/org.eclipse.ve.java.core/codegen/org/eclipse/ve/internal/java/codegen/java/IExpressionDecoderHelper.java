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
 *  $RCSfile: IExpressionDecoderHelper.java,v $
 *  $Revision: 1.11 $  $Date: 2005-09-16 13:34:48 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper.VEexpressionPriority;
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
	public VEexpressionPriority getPriorityOfExpression();
	
   // A decode method will decode specific expressions.
   // If the expression was decoded, a true will be returned.
   // if a false is returned, the expression does not belong to
   // this decoder.
   public boolean decode () throws CodeGenException ;
   // Generate expression source.  Args may or may not be null,
   // depending on the expression type
   public String generate(Object[] args) throws CodeGenException ;
   /**
	 * The decoder should initialized itself.  This is typically called
	 * when the EMF model was constructed from cache, and the BDM is building
	 * in the background.   
	 * A restore should never update the EMF model.  It is implemented
	 * to initialize the decoder propertly, as if a decode was called.
	 * It is possible that the expression can not be decoded and most likely
	 * there are no art effects representing this expression in the model (a case 
	 * where a decode() will return false, or throw an exception)
	 * 
	 * @return same semantics of the decode 
	 * @throws CodeGenException
	 */
   public boolean restore() throws CodeGenException;
   
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
      
   public String getCurrentExpression() ;
   
   public void adaptToCompositionModel(IExpressionDecoder decoder) ;
   public void unadaptToCompositionModel() ;
   public void setDecodingContent (Statement exp) ;
   public Object[] getArgsHandles(Statement exp) ;
   public Object[] getAddedInstance() ;
   /**
    * 
    * @return evaluation of the references of this decoder.
    * 
    * @since 1.1.0
    */
   public Object[] getReferencedInstances();
   // is a change in this sf impact this decoder
   public boolean isRelevantFeature (EStructuralFeature sf) ;
   
   
}

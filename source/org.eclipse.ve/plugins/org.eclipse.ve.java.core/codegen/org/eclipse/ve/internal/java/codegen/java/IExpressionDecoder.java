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
 *  $RCSfile: IExpressionDecoder.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */

import java.util.Vector;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.internal.compiler.ast.Statement;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;


/**
 *   An expression decoder should be implemented to translate a java expression
 *   to a Composition model element/s and (in the future) vice versa.
 */
public interface IExpressionDecoder extends IJVEDecoder {

   // A decode method will decode specific expressions.
   // If the expression was decoded, a true will be returned.
   // if a false is returned, the expression could not be decoded by
   // this decoder.
   
   
  	// Generate the source code from the JVE model
	public String generate(EStructuralFeature sf, Object[] args) throws CodeGenException ;
 

   // Return a vector that contain a set of pair elements:
   // child component, the SF associated with the relationship of the child
   public Vector getChildren(IJavaObjectInstance component) ;
   public boolean isImplicit (Object[] args) ;

   public EStructuralFeature getSF() ;
   public void setSF (EStructuralFeature sf) ;
   public Object[] getArgsHandles(Statement expr) throws CodeGenException ;

   // is this decoder impacted by changes to this SF
   boolean isRelevantFeature (EStructuralFeature sf) ;
   // Get a list of added instances, if any
   public Object[] getAddedInstance() ;
   
   /** 
	* Should return whether this decoder can handle the change in MOF. Typically
	* it can be checked before calling reflectMOFChange(). If TRUE, then this decoder
	* can reflectMOFChange() correcty. If FALSE, this decoder cannot handle the 
	* values in MOF.
	*/   
   public boolean canReflectMOFChange();
   
   
   

}



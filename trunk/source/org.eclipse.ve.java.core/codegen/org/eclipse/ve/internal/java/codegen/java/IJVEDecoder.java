/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IJVEDecoder.java,v $
 *  $Revision: 1.6 $  $Date: 2004-08-27 15:34:09 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper.VEexpressionPriority;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;

/**
 * @author Gili Mendel
 *
 */
public interface IJVEDecoder {

	// Given a source code AST node, decode and update the JVE model
	public boolean decode() throws CodeGenException;
	/**
	 *  Expression priority will determine where will this expression inserted in the code
	 * Should return an array of two integers. The first integer is the priority of the 
	 * Structural Feature. The second integer is an index priority - higher its value, 
	 * the more the expression will float in regards to other expressions with the same
	 * Structural Feature.
	 */
	public VEexpressionPriority determinePriority();

	// At this time the Factory will construct decoders with a default constructor
	public void setExpression(CodeExpressionRef expr) throws CodeGenException;
	public CodeExpressionRef getExprRef();

	public void setBeanModel(IBeanDeclModel model);
	public IBeanDeclModel getBeanModel();
	public void setCompositionModel(IVEModelInstance cm);
	public IVEModelInstance getCompositionModel();
	public void setBeanPart(BeanPart part);
	public BeanPart getBeanPart();

	public boolean isDeleted();
	public String getCurrentExpression();
	// Refresh existing expression content from the JVE model
	public String reflectExpression(String expSig) throws CodeGenException;
	// Delete from the source
	public void deleteFromSrc();
	public void dispose();
	// Update the Source code with the latest JVE model values
	public void reflectMOFchange();

	// Delete from the JVE model only
	public void deleteFromComposition();
	// BDM will adapt an instance using this adapter
	public ICodeGenAdapter createCodeGenInstanceAdapter(BeanPart bp) ;
	public ICodeGenAdapter createThisCodeGenInstanceAdapter(BeanPart bp) ;
	public void setStatement(Statement s);
   

}

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
 *  $RCSfile: ICodeGenAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.emf.common.notify.Adapter;

import org.eclipse.ve.internal.java.codegen.util.CodeGenException;

public interface ICodeGenAdapter extends Adapter {
	public final static Class JVE_CODE_GEN_TYPE = ICodeGenAdapter.class;  // Any Code Gen Adapter
	public final static Class JVE_MEMBER_ADAPTER = MemberDecoderAdapter.class ;
	public final static Class JVE_CODEGEN_BEAN_PART_ADAPTER = BeanDecoderAdapter.class ;
	public final static Class JVE_CODEGEN_EXPRESSION_ADAPTER = ExpressionDecoderAdapter.class ;
	public final static Class JVE_CODEGEN_EVENT_ADAPTER = EventDecoderAdapter.class ;
	public final static Class JVE_CODEGEN_EXPRESSION_SOURCE_RANGE = ICodeGenSourceRange.class ; // Expression or Expresshio Shadow Adapter
	public final static Class JVE_CODEGEN_ANNOTATION_ADAPTER = AbstractAnnotationDecoder.class ;
	
	// Get code highlight offset/length. Useful for highlighting.
	public ICodeGenSourceRange getHighlightSourceRange() throws CodeGenException ;
	// Get the (java source) offset/length of the element the adapter is attached to.
	public ICodeGenSourceRange getJavaSourceRange() throws CodeGenException ;
    // Get BDM (Local) Source RAnge
	public ICodeGenSourceRange getBDMSourceRange() throws CodeGenException ;

}
/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ConstructorDecoderHelper.java,v $
 *  $Revision: 1.1 $  $Date: 2004-02-03 20:11:36 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import org.eclipse.jdt.internal.compiler.ast.Statement;

import org.eclipse.jem.internal.instantiation.PTExpression;
import org.eclipse.jem.internal.instantiation.ParseTreeAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaObjectInstance;

import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public class ConstructorDecoderHelper extends ExpressionDecoderHelper {

	/**
	 * 
	 * This will deal with simple constructors
	 * 
	 * @param bean
	 * @param exp
	 * @param fm
	 * @param owner
	 * 
	 * @since 1.0.0
	 */
	public ConstructorDecoderHelper(BeanPart bean, Statement exp, IJavaFeatureMapper fm, IExpressionDecoder owner) {
		super(bean, exp, fm, owner);		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#decode()
	 */
	public boolean decode() throws CodeGenException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#generate(java.lang.Object[])
	 */
	public String generate(Object[] args) throws CodeGenException {
		IJavaObjectInstance obj = (JavaObjectInstance)fbeanPart.getEObject();
		StringBuffer sb = new StringBuffer();		
		if (obj.getAllocation() instanceof ParseTreeAllocation) {
			// ivjFoo = <allocation>;			
			sb.append(fbeanPart.getSimpleName());
			sb.append(" = ");
			PTExpression exp = ((ParseTreeAllocation) obj.getAllocation()).getExpression();
			CodeGenExpFlattener ef = new CodeGenExpFlattener(fbeanPart.getModel()) ;
			// Visit the parsed tree
			exp.accept(ef) ;
			sb.append(ef.getResult());
			sb.append(";");
			sb.append(fbeanPart.getModel().getLineSeperator());
			return sb.toString();
		}
		else
			 return CodeGenUtil.getInitString(obj) ;
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#removeFromModel()
	 */
	public void removeFromModel() {
		fbeanPart.getEObject().eUnset(fFmapper.getFeature(null)) ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#primRefreshFromComposition(java.lang.String)
	 */
	public String primRefreshFromComposition(String expSig) throws CodeGenException {
		// TODO Want to do smarter here, and compar PTExpressions (generate one from expSig)
		return generate(null) ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#primIsDeleted()
	 */
	public boolean primIsDeleted() {
		return fbeanPart.getEObject().eIsSet(fFmapper.getFeature(null)) ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#getArgsHandles(org.eclipse.jdt.internal.compiler.ast.Statement)
	 */
	public Object[] getArgsHandles(Statement expr) {		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#isImplicit(java.lang.Object[])
	 */
	public boolean isImplicit(Object[] args) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.ExpressionDecoderHelper#getSFPriority()
	 */
	protected int getSFPriority() {		
		return IJavaFeatureMapper.PRIORITY_CONSTRUCTOR;
	}

}

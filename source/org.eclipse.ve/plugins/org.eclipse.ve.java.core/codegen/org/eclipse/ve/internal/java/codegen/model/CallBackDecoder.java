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
 *  $RCSfile: CallBackDecoder.java,v $
 *  $Revision: 1.5 $  $Date: 2004-03-16 20:55:59 $ 
 */
package org.eclipse.ve.internal.java.codegen.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.ve.internal.jcm.Callback;
import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.java.ICallbackDecoder;
import org.eclipse.ve.internal.java.codegen.java.IExpressionChangeListener;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;

/**
 * @author Gili Mendel
 *
 */
public class CallBackDecoder implements ICallbackDecoder {
	
	List    				fListeners = new ArrayList() ;
	IVEModelInstance 	fDiagramModel = null ;
	CodeCallBackRef			fExpr = null ;
	IBeanDeclModel			fModel = null ;
	BeanPart				fbeanPart = null ;
	boolean 				fdeleted = false ;
	Callback				fCallBack = null ;

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICallbackDecoder#decode()
	 */
	public boolean decode() throws CodeGenException {
		notifyListeners() ;
		return true;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICallbackDecoder#setExpression(CodeCallBackRef)
	 */
	public void setExpression(CodeExpressionRef expr) throws CodeGenException {
		if (!(expr instanceof CodeCallBackRef)) throw new CodeGenException("Invalid Parameter") ; //$NON-NLS-1$
		fExpr = (CodeCallBackRef) expr ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICallbackDecoder#setBeanModel(IBeanDeclModel)
	 */
	public void setBeanModel(IBeanDeclModel model) {
		fModel = model ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICallbackDecoder#getBeanModel()
	 */
	public IBeanDeclModel getBeanModel() {
		return fModel;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICallbackDecoder#setCompositionModel(IDiagramModelInstance)
	 */
	public void setCompositionModel(IVEModelInstance cm) {
		fDiagramModel = cm ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICallbackDecoder#getCompositionModel()
	 */
	public IVEModelInstance getCompositionModel() {
		return fDiagramModel;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICallbackDecoder#setBeanPart(BeanPart)
	 */
	public void setBeanPart(BeanPart part) {
		fbeanPart = part ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICallbackDecoder#getBeanPart()
	 */
	public BeanPart getBeanPart() {
		return fbeanPart;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICallbackDecoder#isDeleted()
	 */
	public boolean isDeleted() {
		return fdeleted;
	}

	/**


	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICallbackDecoder#deleteFromSrc()
	 */
	public void deleteFromSrc() {
		fdeleted = true ;
		reflectMOFchange() ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICallbackDecoder#dispose()
	 */
	public void dispose() {}


	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICallbackDecoder#deleteFromComposition()
	 */
	public void deleteFromComposition() {}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICallbackDecoder#getExprRef()
	 */
	public CodeExpressionRef getExprRef() {
		return fExpr;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICallbackDecoder#addChangeListener(IExpressionChangeListener)
	 */
	public void addChangeListener(IExpressionChangeListener l) {
		if (fListeners.contains(l)) return ;
		fListeners.add(l) ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICallbackDecoder#removeChangeListener(IExpressionChangeListener)
	 */
	public void removeChangeListener(IExpressionChangeListener l) {
		fListeners.remove(l) ;
	}
	protected void notifyListeners() {
		for (int i=0; i<fListeners.size(); i++) {
			try {
				((IExpressionChangeListener)fListeners.get(i)).expressionChanged(this) ;
			}
			catch (Throwable e) {
				org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
			}
		}
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IJVEDecoder#determinePriority()
	 */
	public Object determinePriority() {
		return null;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IJVEDecoder#generate(Object[])
	 */
	public String generate(Object[] args) throws CodeGenException {
		return null;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IJVEDecoder#generate(EStructuralFeature, Object[])
	 */
	public String generate(EStructuralFeature sf, Object[] args) throws CodeGenException {
		return null;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IJVEDecoder#getCurrentExpression()
	 */
	public String getCurrentExpression() {
		return null;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IJVEDecoder#reflectExpression(String)
	 */
	public String reflectExpression(String expSig) throws CodeGenException {
		return null;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IJVEDecoder#reflectMOFchange()
	 */
	public void reflectMOFchange() {
		fExpr.setState(CodeExpressionRef.STATE_IN_SYNC, false);
		if ((!fExpr.isAnyStateSet()) || fExpr.isStateSet(CodeExpressionRef.STATE_NO_SRC))
			return;
		fExpr.updateDocument(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.ICallbackDecoder#getCallBack()
	 */
	public Callback getCallBack() {
		return fCallBack ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.ICallbackDecoder#setCallBack(org.eclipse.ve.internal.jcm.Callback)
	 */
	public void setCallBack(Callback c) {
		fCallBack = c ;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IJVEDecoder#createCodeGenInstanceAdapter()
	 */
	public ICodeGenAdapter createCodeGenInstanceAdapter(BeanPart bp) {		
		return new BeanDecoderAdapter(bp) ;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IJVEDecoder#createThisCodeGenInstanceAdapter(org.eclipse.ve.internal.java.codegen.model.BeanPart)
	 */
	public ICodeGenAdapter createThisCodeGenInstanceAdapter(BeanPart bp) {
		EObject bean = bp.getEObject();
		return new ThisBeanDecoderAdapter(bean, bp);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IJVEDecoder#setStatement(org.eclipse.jdt.core.dom.Statement)
	 */
	public void setStatement(Statement s) { 			
	}

}

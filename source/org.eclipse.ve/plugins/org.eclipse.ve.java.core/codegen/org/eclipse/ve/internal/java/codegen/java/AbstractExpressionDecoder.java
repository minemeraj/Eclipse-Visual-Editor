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
 *  $RCSfile: AbstractExpressionDecoder.java,v $
 *  $Revision: 1.7 $  $Date: 2004-03-16 20:55:59 $ 
 */
import java.util.logging.Level;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public abstract class AbstractExpressionDecoder implements IExpressionDecoder {

	protected Statement fExpr = null;
	protected CodeExpressionRef fExprRef = null;
	protected IJavaFeatureMapper fFeatureMapper = null;
	protected IExpressionDecoderHelper fhelper = null;

	protected IBeanDeclModel fBeanModel = null;
	protected IVEModelInstance fCompositonModel = null;
	protected BeanPart fbeanPart = null;
	protected Object fPriority = null;

	public AbstractExpressionDecoder(CodeExpressionRef expr, IBeanDeclModel model, IVEModelInstance bldr, BeanPart part) {
		fExpr =  expr.getExprStmt();
		fExprRef = expr;
		fExprRef.setDecoder(this);
		fBeanModel = model;
		fCompositonModel = bldr;
		fbeanPart = part;	
	}

	public AbstractExpressionDecoder() {
		super();
	}

	protected abstract void initialFeatureMapper();
	protected abstract void initialFeatureMapper(EStructuralFeature sf);
	protected abstract void initialDecoderHelper();

	public Object determinePriority() {

		if (!Initialize())
			return new int[] { IJavaFeatureMapper.PRIORITY_DEFAULT, IJavaFeatureMapper.PRIORITY_DEFAULT };

		// If instanceof ContainerAddDecoderHelper, then the expressions
		// priority keeps changing based on the position of the expression
		// inside the containers children.
		if ((fPriority == null) || (!isPriorityCacheable()))
			fPriority = fhelper.getPriorityOfExpression();
		return fPriority;
	}

	/**
	 * The decoder should decide based on which helper 
	 * it gives, wether or not the priority should be cached.
	 * Since it depends so much on the helper, this method's
	 * decision should carefully mirror the decision taken to
	 * allocate a helper.
	 */
	protected abstract boolean isPriorityCacheable();

	IExpressionDecoderHelper getHelper() {
		if (fhelper == null) {
			fPriority = null;
			initialDecoderHelper();
		}
		return fhelper;
	}

	/**
	 * Make sure a IJavaFeatureMapper, and a IExpressionDecoderHelper have been
	 * allocated to the specific expression at hand. 
	 */
	protected synchronized boolean Initialize() {

		if (fFeatureMapper == null)
			initialFeatureMapper();
		fFeatureMapper.setRefObject((IJavaInstance) fbeanPart.getEObject());

		// Make sure we could resolve the proper PD/SF           		
		if (fFeatureMapper.getFeature(fExpr) == null && !fExprRef.isStateSet(CodeExpressionRef.STATE_NO_MODEL)) {
			CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Feature " + fFeatureMapper.getMethodName() + " not recognized.", false); //$NON-NLS-1$ //$NON-NLS-2$	   
			return false;
		}

		if (getHelper() == null)
			return false;
		if (fExpr != null)
			getHelper().setDecodingContent(fExpr);

		return true;
	}

	protected boolean Initialize(EStructuralFeature sf) {

		if (fFeatureMapper == null)
			initialFeatureMapper(sf);
		initialDecoderHelper();
		return true;
	}

	/**
	 *  This method will be called when we parse for the first time, or in the case
	 *  that the code has changed.  In the later case, content/semantics may have changed.
	 */
	public boolean decode() throws CodeGenException {

		// Refresh decoder 
		if ((getExprRef().getExprStmt() != null) && (getExprRef().getExprStmt() != fExpr))
			fExpr = getExprRef().getExprStmt();

		if (fFeatureMapper != null && fFeatureMapper.getMethodName() == null)
			fFeatureMapper = null;

		// If it has a Simple helper, give it a chance be promoted to a (heavy weight) child decoder.
		// If the child helper fails, it will fall revert to a simple helper
		if (fhelper != null && (fhelper instanceof SimpleAttributeDecoderHelper))
			fhelper = null;

		if (!Initialize())
			return false;

		// Go for it
		boolean result = false;
		try {
			result = fhelper.decode();
		} catch (Exception e) {
			JavaVEPlugin.log(e.getMessage(), Level.FINEST);
		}

		// Always try to revert to a simple helper
		if (!result && !(fhelper instanceof SimpleAttributeDecoderHelper)) {
			// A specialized decoded had faild.  Try to process with a simple Attribute Decoder
			fhelper = new SimpleAttributeDecoderHelper(fbeanPart, fExpr, fFeatureMapper, this);
			result = fhelper.decode();
			determinePriority();
		}
		if (result) {
			//fExprRef.setState(fExprRef.getState() | CodeExpressionRef.STATE_EXIST | CodeExpressionRef.STATE_IN_SYNC) ;
			fExprRef.setState(CodeExpressionRef.STATE_EXIST, true);
			fExprRef.setState(CodeExpressionRef.STATE_IN_SYNC, true);
			fhelper.adaptToCompositionModel(this);
		}
		return result;
	}

	/**
	 *  This is it for now
	 */
	public String generate(EStructuralFeature feature, Object[] args) throws CodeGenException {

		if (!Initialize(feature))
			return null;
		if (isImplicit(args) && !fhelper.isGenerateOnImplicit())
			return null;

		// Go for it

		String result = null;
		try {
			result = fhelper.generate(args);
		} catch (CodeGenException e) {
		}
		
		if (fExprRef.isStateSet(CodeExpressionRef.STATE_NO_SRC)) {
			fhelper.adaptToCompositionModel(this);
			return result ;
		}

		if (result == null && !(fhelper instanceof SimpleAttributeDecoderHelper)) {
			// Specialized decoder may not be applicable, try a vanilla one
			fhelper = new SimpleAttributeDecoderHelper(fbeanPart, fExpr, fFeatureMapper, this);
			JavaVEPlugin.log("generate():  *Defaulting* to a SimpleAttr. Helper", Level.FINE); //$NON-NLS-1$
			result = fhelper.generate(args);
		}

		if (result != null) {			
			fExprRef.setState(CodeExpressionRef.STATE_EXIST, true);
			fExprRef.setState(CodeExpressionRef.STATE_IN_SYNC, true);
			fhelper.adaptToCompositionModel(this);
		} else {
			fExprRef.dispose();
		}

		return result;
	}

	public void setExpression(CodeExpressionRef expr) {
		fExprRef = expr;
		fExpr = (Statement) expr.getExprStmt();
		fExprRef.setDecoder(this);
		if (fhelper != null)
			fhelper.setDecodingContent(fExpr);
	}
	public void setBeanModel(IBeanDeclModel model) {
		fBeanModel = model;
	}

	public IBeanDeclModel getBeanModel() {
		return fBeanModel;
	}

	public void setCompositionModel(IVEModelInstance cm) {
		fCompositonModel = cm;
	}

	public IVEModelInstance getCompositionModel() {
		return fCompositonModel;
	}

	public void setBeanPart(BeanPart part) {
		fbeanPart = part;
	}
	
	
	protected void markExprAsDeleted() {
		fExprRef.clearState();
		fExprRef.setState(CodeExpressionRef.STATE_DELETE, true); 
	}
	
	/**
	 *  Mark as delete, And Remove from document
	 */
	public void deleteFromSrc() {
		fhelper.unadaptToCompositionModel();
		markExprAsDeleted();
		fExprRef.updateDocument(true);
	}

	/**
	 *  Should this feature be considered as a deleted feature ?
	 *  @return boolean  
	 */
	public boolean isDeleted() {
		boolean result = false;

		result = getHelper().primIsDeleted();
		if (result == true)
			fhelper.unadaptToCompositionModel();  // Should not be needed, but just in case ...
		return result;
	}

	/**
	 *  refresh the status of this component 
	 *  Although should not expect it to change ...
	 */

	public boolean isImplicit(Object[] args) {
		// TODO  Need to be looked at again : org.eclipse.ve.internal.cde.core.CDEHack.fixMe() ;
		if (true)
			return false;
		if (getHelper().isImplicit(args) && !fhelper.isGenerateOnImplicit()) {
			//fExprRef.setState(CodeExpressionRef.STATE_EXIST | CodeExpressionRef.STATE_IN_SYNC |
			//                  CodeExpressionRef.STATE_IMPLICIT | fExprRef.getState()) ;
			fExprRef.setState(CodeExpressionRef.STATE_EXIST, true);
			fExprRef.setState(CodeExpressionRef.STATE_IN_SYNC, true);
			fExprRef.setState(CodeExpressionRef.STATE_IMPLICIT, true);
			fExprRef.setContent((ExpressionParser) null);
			return true;
		} else {
			// fExprRef.setState(fExprRef.getState() & ~CodeExpressionRef.STATE_IMPLICIT) ;	
			fExprRef.setState(CodeExpressionRef.STATE_IMPLICIT, false);
			return false;
		}
	}

	public String reflectExpression(String expSig) throws CodeGenException {
		if (!isDeleted()) {
			if (isImplicit(null))
				return null;

			return fhelper.primRefreshFromComposition(expSig);

		} else {
			return null;
		}
	}

	public void reflectMOFchange() {
		fExprRef.setState(CodeExpressionRef.STATE_IN_SYNC, false);
		//fExprRef.setState(fExprRef.getState()&~CodeExpressionRef.STATE_IN_SYNC) ;
		if (!fExprRef.isAnyStateSet()) return;
		fExprRef.updateDocument(true);
	}

	public String getCurrentExpression() {
		if (!isDeleted())
			return fhelper.getCurrentExpression();
		else
			return null;
	}

	public EStructuralFeature getSF() {
		Initialize();
		EStructuralFeature sf = null;
		if (fFeatureMapper != null) {
			sf = fFeatureMapper.getFeature(null);
		}
		return sf;
	}

	public void setSF(EStructuralFeature sf) {
		Initialize();
		if (fFeatureMapper != null) {
			fFeatureMapper.setFeature(sf);
			// ReDrive
			Initialize();
		}
	}
	public BeanPart getBeanPart() {
		return fbeanPart;
	}

	public  void dispose() {		
		if (fhelper != null){
			if (!isDeleted()) {
				deleteFromComposition() ;
			}
			else
			    fhelper.unadaptToCompositionModel();
		}
		markExprAsDeleted();
		fFeatureMapper = null;
		fhelper = null;		
	}

	public  void deleteFromComposition() {
		fhelper.removeFromModel();
	}

	public Object[] getArgsHandles(Statement expr) throws CodeGenException {
		if (!Initialize())
			throw new CodeGenException("Can not Initialize"); //$NON-NLS-1$

		return fhelper.getArgsHandles(expr);
	}

	public CodeExpressionRef getExprRef() {
		return fExprRef;
	}

	/**
	 * Returns a String that represents the value of this object.
	 * @return a string representation of the receiver
	 */
	public String toString() {
		return super.toString() + ":" + fExprRef.toString(); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoder#isRelevantFeature(EStructuralFeature)
	 */
	public boolean isRelevantFeature(EStructuralFeature sf) {
		return fhelper.isRelevantFeature(sf);
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoder#getAddedInstance()
	 */
	public Object[] getAddedInstance() {
		if (fhelper != null)
			return fhelper.getAddedInstance();
		return new Object[0];
	}

	/* 
	 * Should return whether this decoder can handle the change in MOF. Typically
	 * it can be checked before calling reflectMOFChange(). If TRUE, then this decoder
	 * can reflectMOFChange() correcty. If FALSE, this decoder cannot handle the 
	 * values in MOF.
	 * 
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoder#canReflectMOFChange()
	 */
	public boolean canReflectMOFChange() {
		if (getHelper() != null)
			return getHelper().canRefreshFromComposition();
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IJVEDecoder#setStatement(org.eclipse.jdt.core.dom.Statement)
	 */
	public void setStatement(Statement s) {
		fExpr=s;
		getHelper().setDecodingContent(s);
	}

}
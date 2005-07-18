/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $RCSfile: ExpressionDecoderHelper.java,v $
 *  $Revision: 1.12 $  $Date: 2005-07-18 20:25:43 $ 
 */
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.ve.internal.jcm.MemberContainer;

import org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper.VEexpressionPriority;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.*;

public abstract class ExpressionDecoderHelper implements IExpressionDecoderHelper {

	protected IExpressionDecoder fOwner = null;
	protected IJavaFeatureMapper fFmapper = null;
	protected Statement fExpr; // This hold the parsed Source
	protected String fExprSig; // This holds the actual source
	protected BeanPart fbeanPart = null;
	protected ExpressionDecoderAdapter fexpAdapter = null;	

	public ExpressionDecoderHelper(BeanPart bean, Statement exp, IJavaFeatureMapper fm, IExpressionDecoder owner) {
		fOwner = owner;
		fbeanPart = bean;
		fFmapper = fm;
		fExpr = exp;
	}

	/**
	 * The decoder will attempt update the EMF model
	 * from the source.
	 */
	public abstract boolean decode() throws CodeGenException;
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
	 * @return sucess 
	 * @throws CodeGenException
	 */
	public abstract boolean restore() throws CodeGenException;
	/**
	 * The decoder will generate source code relevant to the EMF model state.
	 */
	public abstract String generate(Object[] args) throws CodeGenException;
	public abstract void removeFromModel();

	/**
	 *  If the current value in mof is not the same as we last decoded,
	 *  return a string reflecting the new expression.  Rturn null if no
	 *  refresh is needed.
	 */
	public abstract String primRefreshFromComposition(String expSig) throws CodeGenException;

	/**
	 *  Is this feature still part the composition
	 */
	public abstract boolean primIsDeleted();

	/**
	 *  Get the current expression the decoder has
	 */
	public String getCurrentExpression() {
		return fExprSig;
	}

	/**
	 *  If this expression is implicit, should the generate() be called ?
	 *  Default is no, helpers should overide this otherwise.
	 */
	public boolean isGenerateOnImplicit() {
		return false;
	}

	/**
	 * Most helpers should consider overiding this one
	 */
	public void adaptToCompositionModel(IExpressionDecoder decoder) {
		// Make sure we do not have stale, need to be a bit smarter of how we do this.
		unadaptToCompositionModel();

		BeanDecoderAdapter a =
			(BeanDecoderAdapter) EcoreUtil.getExistingAdapter(fbeanPart.getEObject(), ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
		ExpressionDecoderAdapter adapter = new ExpressionDecoderAdapter(decoder);
		fexpAdapter = adapter;
		a.addSettingAdapter(fFmapper.getFeature(fExpr), adapter);
	}

	/**
	 * Most helpers should consider overiding this one
	 */
	public void unadaptToCompositionModel() {

		BeanDecoderAdapter a =
			(BeanDecoderAdapter) EcoreUtil.getExistingAdapter(fbeanPart.getEObject(), ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
		a.removeSettingAdapter(fFmapper.getFeature(fExpr), fexpAdapter);
		fexpAdapter = null;
	}

	/**
	 *  Create a default ExpressionTemplate with the existing arguments
	 */
	protected ExpressionTemplate getExpressionTemplate(String[] args) throws CodeGenException {
		String mtd = fFmapper.getDecorator().getWriteMethod().getName();
		String sel = fbeanPart.getSimpleName();
		ExpressionTemplate exp = new ExpressionTemplate(sel, mtd, args, null, 0);
		exp.setLineSeperator(fbeanPart.getModel().getLineSeperator());
		return exp;
	}

	protected int getSFPriority() {
		return IJavaFeatureMapper.PRIORITY_DEFAULT;
	}

	protected IJavaFeatureMapper.VEexpressionPriority.VEpriorityIndex getIndexPriority() {
		return null;
	}

	public VEexpressionPriority getPriorityOfExpression() {
		VEexpressionPriority pri = new VEexpressionPriority(getSFPriority(), getIndexPriority());
		return pri;		
	}

	public void setDecodingContent(Statement exp) {
		fExpr = exp;
	}

	/**
	 *  Return a list of unique strings for arguments that make this expression
	 *         unique.  e.g.  P1.add(B1,"North") ;   B1 is unique.  "North" is not
	 */
	public abstract Object[] getArgsHandles(Statement expr);

	/**
	 * Returns a String that represents the value of this object.
	 * @return a string representation of the receiver
	 */
	public String toString() {
		// Insert code to print the receiver here.
		// This implementation forwards the message to super.
		// You may replace or supplement this.
		return (super.toString() + ":\n\t" + fExpr != null ? fExpr.toString() : "?"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#isRelevantFeature(EStructuralFeature)
	 */
	public boolean isRelevantFeature(EStructuralFeature sf) {
		if (sf == null || fFmapper == null)
			return false;
		EStructuralFeature hSF = fFmapper.getFeature(null);
		if (hSF == null)
			return false;

		if (hSF.equals(sf)) {
			//	    || (((XMIResource)hSF.eResource()).getID(hSF) != null && ((XMIResource)hSF.eResource()).getID(hSF).((XMIResource)equals(sf.eResource()).getID(equals(sf)))) 
			return true;
		}

		return false;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#getInstances()
	 */
	public Object[] getAddedInstance() {
		return new Object[0];
	}

	protected void cleanProperty(EObject obj) {
		if (obj == null)
			return;

		if (obj.eContainer() != null && obj.eContainer() instanceof MemberContainer) {
			((MemberContainer) obj.eContainer()).getProperties().remove(obj);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#canRefreshFromComposition()
	 */
	public boolean canRefreshFromComposition() {
		return true;
	}
	
	/**
	 * Two type statements are possible 
	 * Local decleratioin, and Expression Statement 
	 * @param stmt
	 * @return
	 * 
	 * @since 1.0.0
	 */
	protected Expression getExpression(Statement stmt) {
		if (stmt==null) return null;
		if (stmt instanceof ExpressionStatement)
			return ((ExpressionStatement)stmt).getExpression();
		else if (stmt instanceof VariableDeclarationStatement) {
			VariableDeclaration vd = (VariableDeclaration)((VariableDeclarationStatement)stmt).fragments().get(0);
			return vd.getInitializer();
		}
	    return null ;
	}
	protected Expression getExpression() {
		return getExpression(fExpr);
	}
	public List getExpressionReferences() {
		return fOwner.getExprRef().getReferences();
	}
	public Object[] getReferencedInstances() {
		Collection result = CodeGenUtil.getReferences(fbeanPart.getEObject(), false);
		EStructuralFeature sf = fFmapper.getFeature(fExpr) ;
		Object[] o;
		if (sf.isMany()) {
			EList l = (EList) fbeanPart.getEObject().eGet(sf) ;
			o = l.toArray();
		}
		else
		    o = new Object[] { fbeanPart.getEObject().eGet(sf)};
		
		for (int i = 0; i < o.length; i++) {
			result.addAll(CodeGenUtil.getReferences(o,true));						
		}
		return result.toArray();
	}
}

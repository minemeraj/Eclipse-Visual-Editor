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
 *  $RCSfile: AbstractContainerAddDecoderHelper.java,v $
 *  $Revision: 1.4 $  $Date: 2004-01-30 23:19:36 $ 
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.internal.compiler.ast.*;

import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.*;

import org.eclipse.ve.internal.java.core.BeanUtilities;
/**
 *  This helper comes to help build a specialize add, which take arguments for other objects.
 *  e.g., JTabbedPanel's addTab() method.
 */
public abstract class AbstractContainerAddDecoderHelper extends AbstractIndexedChildrenDecoderHelper {

	protected BeanPart fAddedPart = null;
	protected EObject fAddedInstance = null;
	protected EObject fRootObj = null; // The root object that hold all object. 
	protected String fAddedIndex = null;
	protected boolean indexValueFound = false;

	public AbstractContainerAddDecoderHelper(BeanPart bean, Statement exp, IJavaFeatureMapper fm, IExpressionDecoder owner) {
		super(bean, exp, fm, owner);
	}

	/**
	 * Add SourceRange adapters to the arguments of the added component.
	 */
	protected void addShadowAdapters() {
		// Does not support multi expression adapters per object
		ExpressionDecoderAdapter adapter = fexpAdapter;
		if (adapter != null) {
			IJavaObjectInstance[] args = getComponentArguments(getRootObject(true));
			for (int i = 0; i < args.length; i++)
				if (args[i] != null
					&& EcoreUtil.getExistingAdapter(args[i], ICodeGenAdapter.JVE_CODEGEN_EXPRESSION_SOURCE_RANGE) == null) {
					args[i].eAdapters().add(adapter.getShadowSourceRangeAdapter());
				}
		}
	}

	protected void removeShadowAdapters() {
		// Does not support multi expressions adapters per object
		IJavaObjectInstance[] args = getComponentArguments(getRootObject(false));
		for (int i = 0; i < args.length; i++)
			if (args[i] != null) {
				ICodeGenAdapter a =
					(ICodeGenAdapter) EcoreUtil.getExistingAdapter(args[i], ICodeGenAdapter.JVE_CODEGEN_EXPRESSION_SOURCE_RANGE);
				// Only remove shadow adapters
				if (a != null && !(a instanceof ExpressionDecoderAdapter))
					args[i].eAdapters().remove(a);
			}

	}

	/**
	 * Overide the default action, as we are maintained as a list
	 */
	public void adaptToCompositionModel(IExpressionDecoder decoder) {
		unadaptToCompositionModel();

		super.adaptToCompositionModel(decoder);
		BeanDecoderAdapter ba =
			(BeanDecoderAdapter) EcoreUtil.getExistingAdapter(fbeanPart.getEObject(), ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);

		if (fRootObj != null && fRootObj != getComponent(fRootObj)) {
			BeanDecoderAdapter refAdapter = ba.getRefAdapter(fOwner, org.eclipse.emf.common.notify.Notification.ADD);
			fRootObj.eAdapters().add(refAdapter);
			// Add Source Range adapters to the arguments, if needed
			addShadowAdapters();
		}
	}

	/**
	 * Most helpers should consider overiding this one
	 */
	public void unadaptToCompositionModel() {

		super.unadaptToCompositionModel();
		EObject root = getRootObject(false);

		if (root != null && root != getComponent(root)) {
			BeanDecoderAdapter adapter =
				(BeanDecoderAdapter) EcoreUtil.getExistingAdapter(root, ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
			root.eAdapters().remove(adapter);
			removeShadowAdapters();
		}
	}

	/*
	 * Decoder Specific Add 
	 */
	protected void add(BeanPart toAdd, BeanPart target) {
		add(toAdd, target, -1);
	}

	/*
	 * Decoder Specific Add 
	 * @return EObject the ConstraintComponent object
	 */
	protected abstract EObject add(BeanPart toAdd, BeanPart target, int index);
	/**
	 * @return The added component instance
	 **/
	protected abstract IJavaObjectInstance getComponent(EObject root);
	/**
	 * @return Array of constraint instances associated with getComponent() 
	 *          Note: the returned array may contain null entries.
	 **/
	protected abstract IJavaObjectInstance[] getComponentArguments(EObject root);

	protected abstract EStructuralFeature getRootComponentSF();

	/**
	 * @return List of all the components aggregated by fbeanPart
	 */
	protected List getRootComponentList() {

		List compList = null;
		try {
			compList = (List) fbeanPart.getEObject().eGet(fFmapper.getFeature(null));
		} catch (Exception e) {
		}
		// Look for out added part
		if (compList != null && compList.size() == 0)
			return null;
		return compList;

	}

	/**
	 * @return EObject denoting the root object associated this expression (e.g., CC)
	 */
	protected EObject getRootObject(boolean cache) {
		if (fRootObj != null && cache)
			return fRootObj;

		if (fAddedPart != null)
			fAddedInstance = fAddedPart.getEObject();
		EObject targetRoot = null;
		if (fAddedInstance != null) {
			targetRoot =
				InverseMaintenanceAdapter.getIntermediateReference(
					fbeanPart.getEObject(),
					(EReference) fFmapper.getFeature(null),
					(EReference) getRootComponentSF(),
					fAddedInstance);
		}
		if (targetRoot != null)
			fRootObj = targetRoot;
		return targetRoot;
	}

	protected void clearPreviousIfNeeded() {

		// Get the list of add()ed children. These could be CC's or straight targets
		List compList = getRootComponentList();

		// Get the immediate/intermediate child in the list so that he can be removed
		EObject targetRoot = getRootObject(false);

		// Remove the immediate/intermediate child from the list
		if (targetRoot != null && compList != null && compList.size() > 0) {
			compList.remove(targetRoot);
		}

		// Unlink the real object from the intermediate/immediate object
		if (targetRoot != null && targetRoot.eClass().getEAllStructuralFeatures().contains(getRootComponentSF())) {
			targetRoot.eUnset(getRootComponentSF());
		}

		if (fAddedPart != null)
			fAddedPart.removeBackRef(fbeanPart, true);
		else
			cleanProperty(fAddedInstance);
		IJavaObjectInstance constraints[] = getComponentArguments(targetRoot);
		for (int i = 0; i < constraints.length; i++) {
			cleanProperty(constraints[i]);

		}

	}

	protected abstract BeanPart parseAddedPart(MessageSend exp) throws CodeGenException;

	/**
	 *   Add new Componet to target Bean,
	 */
	protected boolean addComponent() throws CodeGenException {

		BeanPart oldAddedPart = fAddedPart;

		clearPreviousIfNeeded();

		fAddedPart = parseAddedPart((MessageSend) fExpr);
		if (fAddedPart != null)
			fAddedInstance = (IJavaObjectInstance) fAddedPart.getEObject();
		if (fAddedPart == null && fAddedInstance == null)
			throw new CodeGenException("No Added Part"); //$NON-NLS-1$

		if (oldAddedPart != null)
			if (fAddedPart != oldAddedPart) {
				oldAddedPart.removeBackRef(fbeanPart, true);
			}

		Expression[] args = ((MessageSend) fExpr).arguments;
		if (fAddedPart != null)
			fAddedPart.addToJVEModel();
		else
			fbeanPart.getInitMethod().getCompMethod().getProperties().add(fAddedInstance);
		return parseAndAddArguments(args);
	}

	protected abstract boolean parseAndAddArguments(Expression[] args) throws CodeGenException;

	/**
	 *  Look for our added part, 
	 */
	public boolean primIsDeleted() {

		EObject rootObj = getRootObject(false);
		if (rootObj == null) {
			if (fAddedPart != null)
				fAddedPart.removeBackRef(fbeanPart, false);
			return true;
		} else
			return false;
	}

	/**
	 *   Overidable test: Is this expression fits this decoder
	 */
	protected abstract boolean isMySigniture();

	/**
	 *   Go for it
	 */
	public boolean decode() throws CodeGenException {

		if (fFmapper.getFeature(fExpr) == null || fExpr == null) {
			CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Feature " + fFmapper.getMethodName() + " is not recognized.", false); //$NON-NLS-1$ //$NON-NLS-2$
			throw new CodeGenException("null Feature:" + fExpr); //$NON-NLS-1$
		}

		if (isMySigniture())
			return addComponent();
		else
			return false;
	}

	/**
	 * 
	 */
	public void removeFromModel() {
		unadaptToCompositionModel();
		clearPreviousIfNeeded();

	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractIndexedChildrenDecoderHelper#getEntryInIndexedEntries()
	 */
	protected Object getIndexedEntry() {
		if (fAddedPart != null)
			return fAddedPart.getEObject();
		else
			return fAddedInstance;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractIndexedChildrenDecoderHelper#getIndexedEntries()
	 */
	protected List getIndexedEntries() {
		List rc = getRootComponentList();
		List realEntries = new ArrayList(rc.size());
		for (int i = 0; i < rc.size(); i++) {
			realEntries.add(getComponent((EObject) rc.get(i)));
		}
		return realEntries;
	}

	/**
	 *  Overide the abstract method, to deal No Decorations.
	 */
	protected abstract ExpressionTemplate getExpressionTemplate() throws CodeGenException;

	protected abstract void primRefreshArguments();

	public String generate(Object[] args) throws CodeGenException {

		if (fFmapper.getFeature(null) == null)
			throw new CodeGenException("null Feature"); //$NON-NLS-1$
		if (args == null || args[0] == null)
			throw new CodeGenException("ContainerAddDecoderHelper.generate() : no Target"); //$NON-NLS-1$

		EObject root = (EObject) args[0];
		fRootObj = root;
		fAddedInstance = getComponent(root);
		fAddedPart = fbeanPart.getModel().getABean(fAddedInstance);
		if (fAddedPart != null) {
			fbeanPart.addChild(fAddedPart);
			fAddedPart.addBackRef(fbeanPart, (EReference) fFmapper.getFeature(null));
		}
		

		primRefreshArguments();

		IMethodInvocationGenerator iGen = new DefaultMethodInvocationGenerator() ;
		
		
		fExprSig = iGen.generateMethodInvocation(fAddedPart.getInitMethod().getMethodName()) ;
		return fExprSig;
	}

	public boolean isImplicit(Object args[]) {
		// TBD
		return false;
	}

	public Object[] getArgsHandles(Statement expr) {
		Object[] result = null;
		try {
			if (fAddedPart == null && expr != null) {
				// Brand new expression
				BeanPart bp = parseAddedPart((MessageSend) expr);
				if (bp != null)
					result = new Object[] { bp.getType() + "[" + bp.getSimpleName() + "]" }; //$NON-NLS-1$ //$NON-NLS-2$      	    
			} else if (fAddedPart != null) {
				return new Object[] { fAddedPart.getType() + "[" + fAddedPart.getSimpleName() + "]" }; //$NON-NLS-1$ //$NON-NLS-2$
			}
		} catch (CodeGenException e) {
		}
		return result;
	}

	/**
	 * Prefix/PostFix quoates.
	 */
	protected void setInitString(IJavaObjectInstance obj, String val) {
		obj.setAllocation(InstantiationFactory.eINSTANCE.createInitStringAllocation(BeanUtilities.createStringInitString(val)));
	}
	//
	///**
	// * Try to figure out where to insert this object, with accordance to its position
	// * in the code, in relation to other components.
	// */
	//protected int findIndex (BeanPart target) {
	//    
	//     if (fOwner.getExprRef().getOffset()<0) return -1 ;
	//    
	//     java.util.List compList = (java.util.List) target.getEObject().
	//                               eGet(fFmapper.getFeature(null));                               
	//     if (compList == null || compList.size() == 0) return -1 ;
	//     
	//     int thisOffset = fOwner.getExprRef().getOffset() ;
	//     int result = -1 ;
	//     for (int i = compList.size()-1; i >= 0; i--) {
	//        try {
	//          EObject root = (EObject) compList.get(i) ;
	//          ICodeGenAdapter a = (ICodeGenAdapter) EcoreUtil.getExistingAdapter(root,ICodeGenAdapter.JVE_CODEGEN_EXPRESSION_ADAPTER) ;
	//          if (a!= null && a.getBDMSourceRange().getOffset()>thisOffset)
	//            result = i ;        
	//        }
	//        catch (CodeGenException e) {}
	//     }
	//     
	//     return result ;     
	//}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#getAddedInstance()
	 */
	public Object[] getAddedInstance() {
		if (fAddedPart != null)
			return new Object[] { fAddedPart.getEObject(), fRootObj };
		else if (fAddedInstance != null)
			return new Object[] { fAddedInstance, fRootObj };

		return new Object[0];
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#primRefreshFromComposition(String)
	 */
	public String primRefreshFromComposition(String exprSig) throws CodeGenException {
		return null;
	}

}

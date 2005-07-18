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
 *  $RCSfile: AbstractContainerAddDecoderHelper.java,v $
 *  $Revision: 1.20 $  $Date: 2005-07-18 20:25:43 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.*;
/**
 *  This helper comes to help build a specialize add, which take arguments for other objects.
 *  e.g., JTabbedPanel's addTab() method.
 */
public abstract class AbstractContainerAddDecoderHelper extends AbstractIndexedChildrenDecoderHelper {

	protected BeanPart fAddedPart = null;
	protected IJavaObjectInstance fAddedInstance = null;
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
			fAddedInstance = (IJavaObjectInstance) fAddedPart.getEObject();
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
		if (targetRoot != null && targetRoot.eClass().getFeatureID(getRootComponentSF()) != -1) {
			// The unsetting should be done on the intermediate object ONLY,
			// which is NOT the component object. Else erroneous removal
			// of settings is done
			if(targetRoot!=getComponent(targetRoot)) 
				targetRoot.eUnset(getRootComponentSF());
		}
		
		// Remove reference from list of references of this expression
		EObject referencedInstance = fAddedPart!=null ? fAddedPart.getEObject() : fAddedInstance;
		List references = fOwner.getExprRef().getReferences();
		if(references.contains(referencedInstance))
			references.remove(referencedInstance);

		if (fAddedPart != null)
			fAddedPart.removeBackRef(fbeanPart, true);
		else
			cleanProperty(fAddedInstance);
		IJavaObjectInstance constraints[] = getComponentArguments(targetRoot);
		for (int i = 0; i < constraints.length; i++) {
			cleanProperty(constraints[i]);

		}

	}

	protected abstract BeanPart parseAddedPart(MethodInvocation exp) throws CodeGenException;
	protected abstract int		getAddedPartArgIndex(int totalArgs);
	
	protected boolean sameInitString(IJavaObjectInstance cur, Expression proposed) {
		if (proposed==null || proposed instanceof NullLiteral) 
			return cur==null;		
		if (cur==null)
			return false;		
	  String curStr = ConstructorDecoderHelper.convertToString(cur.getAllocation());
	  JavaAllocation pAlloc = getAllocation(proposed);
	  String proposedStr = ConstructorDecoderHelper.convertToString(pAlloc);
	  return (curStr.equals(proposedStr));
	}
	
	protected boolean shouldCommit(BeanPart oldAddedPart, BeanPart newAddedPart, EObject newAddedInstance, List args){
		boolean addedInstanceChanged = oldAddedPart!=newAddedPart;
		if(!addedInstanceChanged){
			// We may be added a property (e.g., add(new Foo(), x, y z)		
			if (fRootObj!=null) {
				if (args.size()>0 && args.get(getAddedPartArgIndex(args.size())) instanceof ClassInstanceCreation) {
				   String curAllocation = ConstructorDecoderHelper.convertToString(fAddedInstance.getAllocation());
				   JavaAllocation astAlloc = getAllocation((Expression)args.get(getAddedPartArgIndex(args.size())));
				   String astAllocation = ConstructorDecoderHelper.convertToString(astAlloc);
				   if (!curAllocation.equals(astAllocation)) {
				   	fAddedInstance.setAllocation(astAlloc);
				   	addedInstanceChanged = true;
				   }
				}
			}
			else
				addedInstanceChanged = fAddedInstance != null;												
	
		}
		return addedInstanceChanged;
	}

	/**
	 *   Add new Componet to target Bean,
	 */
	protected boolean addComponent() throws CodeGenException {
		BeanPart oldAddedPart = fAddedPart;
		BeanPart newAddedPart = parseAddedPart((MethodInvocation) getExpression(fExpr)); 
		EObject newAddedInstance = fAddedInstance;
		if(newAddedPart!=null)
			newAddedInstance = newAddedPart.getEObject();
		List args = ((MethodInvocation) getExpression(fExpr)).arguments();

		// SMART DECODING - 
		if(shouldCommit(oldAddedPart, newAddedPart, newAddedInstance, args)){
		clearPreviousIfNeeded();

		fAddedPart = newAddedPart;
		fAddedInstance = (IJavaObjectInstance) newAddedInstance;
		if (fAddedPart == null && fAddedInstance == null)
			throw new CodeGenException("No Added Part"); //$NON-NLS-1$

		if (oldAddedPart != null)
			if (fAddedPart != oldAddedPart) {
				oldAddedPart.removeBackRef(fbeanPart, true);
			}
		
		EObject referencedInstance = null;

		if (fAddedPart != null){
			fAddedPart.addToJVEModel();
			referencedInstance = fAddedPart.getEObject();
		}else{
			fbeanPart.getInitMethod().getCompMethod().getProperties().add(fAddedInstance);
			referencedInstance = fAddedInstance;
		}
	
		// Update list of references for this expression
		List references = fOwner.getExprRef().getReferences();
		if(referencedInstance!=null && !references.contains(referencedInstance))
			references.add(referencedInstance);


		return parseAndAddArguments(args);
		}
		return true;	
	}

	protected abstract boolean parseAndAddArguments(List args) throws CodeGenException;

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
	

	public boolean restore() throws CodeGenException {		
		if (fFmapper.getFeature(fExpr) == null || fExpr == null) {
			CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Feature " + fFmapper.getMethodName() + " is not recognized.", false); //$NON-NLS-1$ //$NON-NLS-2$
			throw new CodeGenException("null Feature:" + fExpr); //$NON-NLS-1$
		}
		if (isMySigniture()) {			
			fAddedPart = parseAddedPart((MethodInvocation) getExpression(fExpr));
			if (fAddedPart!=null) {
			   fAddedInstance = (IJavaObjectInstance)fAddedPart.getEObject();
			   getRootObject(false);
			   fAddedPart.addBackRef(fbeanPart, (EReference)fFmapper.getFeature(null)) ;
			   fbeanPart.addChild(fAddedPart) ;
			   
				// Update list of references for this expression
				List references = fOwner.getExprRef().getReferences();
				if(fAddedInstance!=null && !references.contains(fAddedInstance))
					references.add(fAddedInstance);
				
			   return true; 			   
			}			
		}		
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
	
	protected abstract String generateSrc() throws CodeGenException;

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

		fExprSig = generateSrc();
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
				BeanPart bp = parseAddedPart((MethodInvocation) getExpression(expr));
				if (bp != null)
					result = new Object[] { bp.getType() + "[" + bp.getUniqueName() + "]" }; //$NON-NLS-1$ //$NON-NLS-2$      	    
			} else if (fAddedPart != null) {
				return new Object[] { fAddedPart.getType() + "[" + fAddedPart.getUniqueName() + "]" }; //$NON-NLS-1$ //$NON-NLS-2$
			}
		} catch (CodeGenException e) {
		}
		return result;
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
		ExpressionTemplate exp = getExpressionTemplate();
		return exp.toString();
	}
	
	/**
	 * Smart decoding capability:
	 * This method returns whether decoding should be performed or not due to offset
	 * changes. This is helpful during snippet update process where re-decoding is done for 
	 * offset changes, and decoding should not be done unless there is some 
	 * content change, or if the expression ordering has not changed 
	 * 
	 * @return
	 */
	protected boolean canAddingBeSkippedByOffsetChanges() {
		EStructuralFeature sf = fFmapper.getFeature(null);
		if(sf.isMany() && fbeanPart.getEObject()!=null){
			List components = getIndexedEntries();
			EObject entry = (EObject) getIndexedEntry();
			int currentIndex = components.indexOf(entry);
			Collection expressions = fbeanPart.getRefExpressions();
			for (Iterator expItr = expressions.iterator(); expItr.hasNext();) {
				CodeExpressionRef exp = (CodeExpressionRef) expItr.next();
				Object[] expAddedInstances = exp.getAddedInstances();
				for(int eaic=0; expAddedInstances!=null && eaic<expAddedInstances.length; eaic++){
					EObject expAddedInstance = (EObject)expAddedInstances[eaic];
					if(!components.contains(expAddedInstance))
						expAddedInstance = getComponent(expAddedInstance);
					if(		components.contains(expAddedInstance) && 
							(	(components.indexOf(expAddedInstance) < currentIndex && 
								exp.getOffset()>=fOwner.getExprRef().getOffset()) ||
								(components.indexOf(expAddedInstance) > currentIndex && 
								exp.getOffset()<=fOwner.getExprRef().getOffset()))){
								return false;
							}
				}
			}
		}
		return true;
	}
	
	protected JavaAllocation getAllocation (Expression exp) {
		CodeMethodRef expOfMethod = (fOwner!=null && fOwner.getExprRef()!=null) ? fOwner.getExprRef().getMethod():null;
		   JavaAllocation alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation(
		 		  ConstructorDecoderHelper.getParsedTree(exp,
		 		  expOfMethod,fOwner.getExprRef().getOffset(), fbeanPart.getModel(),getExpressionReferences()));
		return alloc;
	}
	
	public Object[] getReferencedInstances() {
		Collection result = CodeGenUtil.getReferences(fbeanPart.getEObject(),false);
		Object[] added = getAddedInstance();
		for (int i = 0; i < added.length; i++) 
			result.addAll(CodeGenUtil.getReferences(added[i],true));			
		return result.toArray();
	}

	protected EObject getIndexParent() {		
		return getRootObject(false);
	}
	

}

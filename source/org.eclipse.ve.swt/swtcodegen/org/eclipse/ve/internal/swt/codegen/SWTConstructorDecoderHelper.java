/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: SWTConstructorDecoderHelper.java,v $
 *  $Revision: 1.35 $  $Date: 2006-02-09 15:22:12 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import java.util.*;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.CodeExpressionRef;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.core.FactoryCreationData;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.core.FactoryCreationData.MethodData;

import org.eclipse.ve.internal.swt.SwtPlugin;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public class SWTConstructorDecoderHelper extends ConstructorDecoderHelper {
	
	BeanPart fParent = null ;
	protected String constructorSF = null;
	protected CodeExpressionRef masterExpression = null; // Expression drives the existence of this expression

	private static boolean isWidget(EObject eObject, ResourceSet rs) {
		JavaHelpers widgetType = JavaRefFactory.eINSTANCE.reflectType("org.eclipse.swt.widgets", "Widget", rs) ; //$NON-NLS-1$ //$NON-NLS-2$
		if(widgetType!=null && widgetType.isInstance(eObject)){
			return true;
		}
		return false;
	}
	
	private static boolean isComposite(EObject eObject, ResourceSet rs) {
		JavaHelpers widgetType = JavaRefFactory.eINSTANCE.reflectType("org.eclipse.swt.widgets", "Composite", rs) ; //$NON-NLS-1$ //$NON-NLS-2$
		if(widgetType!=null && widgetType.isInstance(eObject)){
			return true;
		}
		return false;
	}	

	/**
	 * @param bean
	 * @param exp
	 * @param fm
	 * @param owner
	 * 
	 * @since 1.0.0
	 */
	public SWTConstructorDecoderHelper(BeanPart bean, Statement exp, IJavaFeatureMapper fm, IExpressionDecoder owner, String constructorSF) {
		super(bean, exp, fm, owner);
		this.constructorSF = constructorSF;
	}
	/**
	 * If this widget is initialized in the same method as its parent, than
	 * we know that this widget will be instantiated, and we need to set up
	 * the parent/child relationship
	 * @return
	 * 
	 * @since 1.0.0
	 */
	
	protected BeanPart getParent() {
		if (fParent!=null) return fParent;
		// It the BeanPart is an implicit swt, then the parent may not be the first referenced widget. It may be the parent widget
		// itself.
		IJavaObjectInstance jo = (IJavaObjectInstance) fbeanPart.getEObject();
		// Try to handle special if implicit.
		if (jo.isImplicitAllocation()) {
			ImplicitAllocation ia = (ImplicitAllocation) jo.getAllocation();
			EObject pParent = ia.getParent();
			// If parent is a composite, then use it as the parent.
			if(isComposite(pParent, fOwner.getBeanModel().getCompositionModel().getModelResourceSet())){
				fParent = fbeanPart.getModel().getABean(pParent);
				return fParent;
			}
		} else if (jo.isParseTreeAllocation()) {
			// See if factory creation.
			ParseTreeAllocation pa = (ParseTreeAllocation) jo.getAllocation();
			if (pa.getExpression() instanceof PTMethodInvocation) {
				PTMethodInvocation mi = (PTMethodInvocation) pa.getExpression();
				FactoryCreationData fcd = FactoryCreationData.getCreationData(mi);
				if (fcd != null) {
					// This is a factory receiver, see if get parentComposite. This will tell us the parent.
					MethodData methodData = fcd.getMethodData(mi.getName());
					if (methodData != null) {
						// It is a factory method
						int argIndex = methodData.getArgIndex(SwtPlugin.PARENT_COMPOSITE_PROPERTY, mi.getArguments().size());
						if (argIndex >= 0) {
							// We found it. Assuming an instance ref.
							Object parentExpression = mi.getArguments().get(argIndex);
							if (parentExpression instanceof PTInstanceReference) {
								fParent = fbeanPart.getModel().getABean(((PTInstanceReference) parentExpression).getReference());
								return fParent;
							}
						}
					}
				}
			}
		}
		
		
		if (getExpressionReferences().size()>0){
			Iterator refItr = getExpressionReferences().iterator();
			while (refItr.hasNext()) {
				EObject reference = (EObject) refItr.next();
				if(isWidget(reference, fOwner.getBeanModel().getCompositionModel().getModelResourceSet())){
					fParent = fbeanPart.getModel().getABean(reference);
					break;
				}
			}
		} else {
			// decoder was not active yet
			fParent = CodeGenUtil.determineParentBeanpart(fbeanPart);		
		}
		return fParent;
	}
	protected boolean isControlFeatureNeeded() {
		// Does the constructor refer to a parent
		if (getExpressionReferences().size()>0) {
			// check to see if both parent/child are defined on with the same method
			// The assumption is that a call to the init method of teh child will create
			// the control feature						
			if (fbeanPart.getInitMethod().equals(getParent().getInitMethod()))
				return true;
		}
		return false ;
	}
	
	protected EStructuralFeature getIndexSF() {
		if (getParent()!=null && !getParent().isDisposed())
		    return getParent().getEObject().eClass().getEStructuralFeature(constructorSF);
		return null;
	}
	
	/**
	 * Add this bean to the "controls" feature of the parent,
	 * and create the proper Expression for the BDM
	 * @param oldIndexSF 
	 * @param oldParentBP 
	 * @since 1.0.0
	 */
	protected void createControlFeature(boolean updateEMFModel, BeanPart oldParentBP, EStructuralFeature oldIndexSF) throws CodeGenException{
		
		EStructuralFeature sf = getIndexSF();
		CodeExpressionRef exp = fOwner.getExprRef();
		
		// The master expression sits on the parent and can get disposed if 
		// the parent is disposed. Check to see if it is disposed and dont use if it is.
		if(masterExpression!=null && masterExpression.isStateSet(CodeExpressionRef.STATE_DELETE))
			masterExpression = null;
		
		if (updateEMFModel) {
			
			// clean up if the parent has changed - label moved from one container to another
			if(getParent()!=oldParentBP && oldParentBP!=null && oldIndexSF!=null){
				// not the same parent - clean up old setting
				EList oldControlList = (EList)oldParentBP.getEObject().eGet(oldIndexSF);
				oldControlList.remove(fbeanPart.getEObject());
			}
			
			EObject parent = getParent().getEObject();
			BeanDecoderAdapter pAdapter = (BeanDecoderAdapter) EcoreUtil.getExistingAdapter(parent, ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);

			// find all the other "control" expressions
			ICodeGenAdapter controls[] = pAdapter.getSettingAdapters(sf);

			HashMap map = new HashMap() ;
			// Map child instance to the chile "add" expression
			for (int i = 0; i < controls.length; i++) {
				if (controls[i] instanceof ExpressionDecoderAdapter) {
				  ExpressionDecoderAdapter a = (ExpressionDecoderAdapter)controls[i];
				  map.put(a.getDecoder().getAddedInstance()[0], a.getDecoder().getExprRef());
				  if (a.getDecoder().getAddedInstance()[0]==fbeanPart.getEObject()) 
						 masterExpression = a.getDecoder().getExprRef();
				}
			}
						
			EList controlList = (EList)parent.eGet(sf);
			
			int expOffset = exp.getOffset();
			// if declaration is implicit, the offset is the parent's 
			// init expression offset.
			if(exp.getBean().isImplicit() && exp.getBean().getImplicitParent()!=null){
				BeanPart parentBP = exp.getBean().getImplicitParent();
				if(parentBP.getInitExpression()!=null)
					expOffset = parentBP.getInitExpression().getOffset();
			}
			
			// determine z order
			int index = 0;
			int curIndex=-1;
			for (int i=0; i<controlList.size(); i++) {			
				CodeExpressionRef cExpr = (CodeExpressionRef) map.get(controlList.get(i));
				if (cExpr.getMasteredExpression()==fOwner.getExprRef()) 
					curIndex=i;
				else if (cExpr != null && cExpr.getOffset()<expOffset){
					index=i+1;
				}			
			}

			if (curIndex<0){
				EObject toAddEObject = fbeanPart.getEObject();
				if(toAddEObject!=null && toAddEObject.eContainer()!=null){
					// remove it from the the freeform components (if it happens to be there) as it is being reparented. This is
					// so that it is removed from the freeform before it is added to the new parent.
					List compList = fbeanPart.getModel().getCompositionModel().getModelRoot().getComponents();
					if(compList!=null)
						compList.remove(toAddEObject);
				}
				controlList.add(index, toAddEObject);
			} else 
			  if (curIndex!=index) {
				if (curIndex<index) // our move will shift all to the left
					controlList.move(index-1, fbeanPart.getEObject());
				else
					controlList.move(index, fbeanPart.getEObject());
			  }
		}
		
		if(masterExpression==null){					
			// Create a pseudo expression for the parent (no add(Foo) in SWT)
			// This will drive the creation of a decoder with controls, and will
			// establish the parent/child relationship in the BDM
			ExpressionRefFactory eGen = new ExpressionRefFactory(fParent, sf);		
			try {
				masterExpression = eGen.createFromJVEModel(new Object[] { fbeanPart.getEObject() });
				// The z order for the "control" feature 
				// will be base on this constructor
				masterExpression.setMasteredExpression(exp);
			} catch (CodeGenException e) {
				JavaVEPlugin.log(e);
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#decode()
	 */
	public boolean decode() throws CodeGenException {		
		boolean result = super.decode();
		if (result) {
			BeanPart oldParentBP = getParent();
			EStructuralFeature oldIndexSF = getIndexSF();
			fParent = null;
			if (isControlFeatureNeeded()) {
				createControlFeature(true, oldParentBP, oldIndexSF);								
			}
		}
		if (getParent()!=null) {
			// our parent may have called us with createFoo() ... 
			// during parsing time we could not resolve who is the parent... now is the time
			fbeanPart.resolveParentExpressions(getParent());
		}
		return (result);
	}
	
	public boolean restore() throws CodeGenException {
		boolean result = super.restore();
		if (result) {
			if (isControlFeatureNeeded()) {
				createControlFeature(false, null, null);								
			}
		}
		if (getParent()!=null) {
			// our parent may have called us with createFoo() ... 
			// during parsing time we could not resolve who is the parent... now is the time
			fbeanPart.resolveParentExpressions(getParent());
		}
		return result;
	}
	
	/**
	 * 
	 * @return the index of this bean in the control feature list, -1 if not found
	 * @todo Generated comment
	 */
	protected int getControlIndex() {
		int result = -1;
		if (getParent()!=null) {
			  List controls = (List)getParent().getEObject().eGet(getIndexSF());
			  for (int i = 0; i < controls.size(); i++) 
				if (controls.get(i).equals(fbeanPart.getEObject())) {
					result = i ;
				    break ;
				}
		}
		return result;
	}
	
	public static int getIndexPriority(BeanPart bean, List sieblings) {
		int priority =  getDefaultBeanPriority(bean);	
		int index = sieblings.indexOf(bean.getEObject());
		if (index>0)
			priority+=index;
		return priority;		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.ExpressionDecoderHelper#getIndexPriority()
	 */
	protected IJavaFeatureMapper.VEexpressionPriority.VEpriorityIndex getIndexPriority() {		
		EStructuralFeature sf = getIndexSF();
		if (sf==null) {
			return null; // no index priority
		}
		List controls = Collections.EMPTY_LIST;
		if (getParent()!=null)
			controls = (List)getParent().getEObject().eGet(getIndexSF());
		// use a common method
		int index = getIndexPriority(fbeanPart, controls) ;
		return new IJavaFeatureMapper.VEexpressionPriority.VEpriorityIndex(sf, index, getParent().getEObject());
	}
	
	/** (non-Javadoc)
	 *  The problem with constructor generation for SWT is that in 
	 *  this case the constructor is put into the JVE model
	 *  before the "control" feature, and there is no way to know
	 *  in advance the z order to insert the constructor....
	 *  ... so we will delay the generation phase until 
	 *  the "control" feature is set.
	 */
	public String generate(Object[] args) throws CodeGenException {
		// let the constructor update the reference context
		BeanPart parent = getParent();
		if (parent != null && !getExpressionReferences().contains(parent.getEObject()))
			getExpressionReferences().add(parent.getEObject());
		Iterator bpItr = CodeGenUtil.getAllocationReferences(fbeanPart).iterator();
		while (bpItr.hasNext()) {
			BeanPart bp = (BeanPart) bpItr.next();
			if(!getExpressionReferences().contains(bp.getEObject()))
				getExpressionReferences().add(bp.getEObject());
		}
		if (isControlFeatureNeeded()) {
			if (getControlIndex()<0) {
			// Need to wait here for the control feature first
			// Temporarily disable the source generation for this expression
			
			// 116879: If there is no controls feature, we remove the expression. 
		    // As when the controls feature is set, it will automatically generate
			// the creation of an init expression, and magically move all remaining 
			// expressions. Hence we return null, which makes the CodeExpressionRef 
			// remove it from the source. 
			// 
			// fOwner.getExprRef().setNoSrcExpression(true);
			
			  return null ;
			}
		}		
		return super.generate(args);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#primIsDeleted()
	 */
	public boolean primIsDeleted() {
		boolean result = super.primIsDeleted() || fOwner.getExprRef().isStateSet(CodeExpressionRef.STATE_MASTER_DELETED);
		return result;
	}
	
	protected CodeExpressionRef getMasterExpression() {
		if (masterExpression!=null) return masterExpression;
		
		if (getParent()!=null) {
			EObject parent = getParent().getEObject();
			if(parent==null || getParent().isDisposed()){
				// parent beanpart got disposed before this beanpart - no master expression
			}else{
				BeanDecoderAdapter pAdapter = (BeanDecoderAdapter) EcoreUtil.getExistingAdapter(parent, ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
		
				// find all the other "control" expressions
				ICodeGenAdapter controls[] = pAdapter.getSettingAdapters(getIndexSF());
				for (int i = 0; i < controls.length; i++) 
					if (controls[i] instanceof ExpressionDecoderAdapter) {
						ExpressionDecoderAdapter a = (ExpressionDecoderAdapter)controls[i];
				         if (a.getDecoder().getAddedInstance()[0]==fbeanPart.getEObject()) {
							 masterExpression = a.getDecoder().getExprRef();
							 break;
				         }
					}
			}
		}
		return masterExpression;
	}
	public void removeFromModel() {
		if (getMasterExpression()!=null) {			
			masterExpression.dispose();
			masterExpression=null;
		}
		super.removeFromModel();		
	}
}

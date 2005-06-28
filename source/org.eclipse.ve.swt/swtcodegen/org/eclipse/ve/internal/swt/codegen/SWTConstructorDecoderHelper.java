/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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
 *  $Revision: 1.21 $  $Date: 2005-06-28 22:51:35 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import java.util.*;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.CodeExpressionRef;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public class SWTConstructorDecoderHelper extends ConstructorDecoderHelper {
	
	BeanPart fParent = null ;
	protected String constructorSF = null;
	protected CodeExpressionRef masterExpression = null; // Expression drives the existence of this expression

	private static boolean isWidget(EObject eObject, ResourceSet rs) {
		JavaHelpers widgetType = JavaRefFactory.eINSTANCE.reflectType("org.eclipse.swt.widgets.Widget", rs) ; //$NON-NLS-1$
		if(widgetType!=null && widgetType.isAssignableFrom(eObject.eClass())){
			// first reference is not a widget - use the factory instance approach
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
		if (getParent()!=null)
		    return getParent().getEObject().eClass().getEStructuralFeature(constructorSF);
		return null;
	}
	
	/**
	 * Add this bean to the "controls" feature of the parent,
	 * and create the proper Expression for the BDM
	 * @since 1.0.0
	 */
	protected void createControlFeature(boolean updateEMFModel) throws CodeGenException{
		
		EStructuralFeature sf = getIndexSF();
		CodeExpressionRef exp = fOwner.getExprRef();
		
		
		if (updateEMFModel) {
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

			
			// determine z order
			int index = 0;
			int curIndex=-1;
			for (int i=0; i<controlList.size(); i++) {			
				CodeExpressionRef cExpr = (CodeExpressionRef) map.get(controlList.get(i));
				if (cExpr.getMasteredExpression()==fOwner.getExprRef()) 
					curIndex=i;
				else if (cExpr != null && cExpr.getOffset()<exp.getOffset()){
					index=i+1;
				}			
			}
			if (curIndex<0)
				controlList.add(index, fbeanPart.getEObject());
			else 
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
			if (isControlFeatureNeeded()) {
				createControlFeature(true);								
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
				createControlFeature(false);								
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
	protected Object[] getIndexPriority() {
		Object[] result = new Object[2];
		result[1] = getIndexSF();
		if (result[1]==null) {
			return null; // no index priority
		}
		List controls = Collections.EMPTY_LIST;
		if (getParent()!=null)
			controls = (List)getParent().getEObject().eGet(getIndexSF());
		// use a common method
		result[0] = new Integer(getIndexPriority(fbeanPart, controls)) ;
		return result;
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
		 	  fOwner.getExprRef().setNoSrcExpression(true);
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
		return masterExpression;
	}
	public void removeFromModel() {
		if (getMasterExpression()!=null)
			masterExpression.dispose();
		super.removeFromModel();		
	}
}

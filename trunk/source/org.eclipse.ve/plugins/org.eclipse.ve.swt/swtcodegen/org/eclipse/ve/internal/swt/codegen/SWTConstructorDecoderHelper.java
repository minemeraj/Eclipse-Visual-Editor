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
 *  $RCSfile: SWTConstructorDecoderHelper.java,v $
 *  $Revision: 1.5 $  $Date: 2004-04-23 23:15:53 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

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

	/**
	 * @param bean
	 * @param exp
	 * @param fm
	 * @param owner
	 * 
	 * @since 1.0.0
	 */
	public SWTConstructorDecoderHelper(BeanPart bean, Statement exp, IJavaFeatureMapper fm, IExpressionDecoder owner) {
		super(bean, exp, fm, owner);		
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
		if (fReferences.size()>0)
		    fParent = fbeanPart.getModel().getABean((IJavaObjectInstance)fReferences.get(0));
		else {
			// decoder was not active yet
			fParent = CodeGenUtil.determineParentBeanpart(fbeanPart);			
		}
		return fParent;
	}
	protected boolean isControlFeatureNeeded() {
		// Does the constructor refer to a parent
		if (fReferences.size()>0) {
			// check to see if both parent/child are defined on with the same method
			// The assumption is that a call to the init method of teh child will create
			// the control feature						
			if (fbeanPart.getInitMethod().equals(getParent().getInitMethod()))
				return true;
		}
		return false ;
	}
	
	protected EStructuralFeature getControlSF() {
		return getParent().getEObject().eClass().getEStructuralFeature("controls");
	}
	
	/**
	 * Add this bean to the "controls" feature of the parent,
	 * and create the proper Expression for the BDM
	 * @since 1.0.0
	 */
	protected void createControlFeature() {				
		
		EObject parent = getParent().getEObject();
		BeanDecoderAdapter pAdapter = (BeanDecoderAdapter) EcoreUtil.getExistingAdapter(parent, ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
						
		EStructuralFeature sf = getControlSF();
		// find all the other "control" expressions
		ICodeGenAdapter controls[] = pAdapter.getSettingAdapters(sf);
		HashMap map = new HashMap() ;
		// Map "control" feature added part, to its expression
		for (int i = 0; i < controls.length; i++) {
			if (controls[i] instanceof ExpressionDecoderAdapter) 
			  map.put(((ExpressionDecoderAdapter)controls[i]).getDecoder().getAddedInstance()[0],
					  ((ExpressionDecoderAdapter)controls[i]).getDecoder().getExprRef());
		}
		
		CodeExpressionRef exp = fOwner.getExprRef();
		List controlList = (List)parent.eGet(sf);
		
		// determine z order
		int index = 0;
		for (int i=0; i<controlList.size(); i++) {			
			CodeExpressionRef cExpr = (CodeExpressionRef) map.get(controlList.get(i));
			if (cExpr != null && cExpr.getOffset()<exp.getOffset()){
				index=i+1;
			}			
		}
		// Add a control feature
		controlList.add(index, fbeanPart.getEObject());
				
		// Create a pseudo expression for the parent (no add(Foo) in SWT)
		// This will drive the creation of a decoder with controls, and will
		// establish the parent/child relationship in the BDM
		ExpressionRefFactory eGen = new ExpressionRefFactory(fParent, sf);		
		try {
			CodeExpressionRef newExpr = eGen.createFromJVEModel(new Object[] { fbeanPart.getEObject() });
			// The z order for the "control" feature 
			// will be base on this constructor
			newExpr.setMasterExpression(exp);
		} catch (CodeGenException e) {
			JavaVEPlugin.log(e);
		}		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#decode()
	 */
	public boolean decode() throws CodeGenException {		
		boolean result = super.decode();
		if (result) {
			if (isControlFeatureNeeded()) {
				createControlFeature();								
			}
		}
		return (result);
	}
	
	/**
	 * 
	 * @return the index of this bean in the control feature list, -1 if not found
	 * @todo Generated comment
	 */
	protected int getControlIndex() {
		int result = -1;
		if (getParent()!=null) {
			  List controls = (List)getParent().getEObject().eGet(getControlSF());
			  for (int i = 0; i < controls.size(); i++) 
				if (controls.get(i).equals(fbeanPart.getEObject())) {
					result = i ;
				    break ;
				}
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.ExpressionDecoderHelper#getIndexPriority()
	 */
	protected int getIndexPriority() {
		// The following will determine proirities according to parent dependencies
		int priority = super.getIndexPriority();
		// add also the context for which we are added (z order) as controls
		int index = getControlIndex();
		if (index>=0)
			priority -=index;
		return priority;
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
		if (parent != null && !fReferences.contains(parent.getEObject()))
			fReferences.add(parent.getEObject());
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
}

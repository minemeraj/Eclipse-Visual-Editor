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
 *  $RCSfile: WorkbenchPartArgumentDecoderHelper.java,v $
 *  $Revision: 1.3 $  $Date: 2005-10-03 19:20:48 $ 
 */
package org.eclipse.ve.internal.jface.codegen;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.jem.internal.instantiation.ImplicitAllocation;
import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
 

/**
 * 
 * @since 1.1
 */
public class WorkbenchPartArgumentDecoderHelper extends ExpressionDecoderHelper {

	public WorkbenchPartArgumentDecoderHelper(BeanPart bean, Statement exp, IJavaFeatureMapper fm, IExpressionDecoder owner) {
		super(bean, exp, fm, owner);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.ExpressionDecoderHelper#decode()
	 */
	public boolean decode() throws CodeGenException {
		BeanPart thisBP = fOwner.getBeanModel().getABean(BeanPart.THIS_NAME);
		if(thisBP!=null){
			boolean sfNeedsUpdate = true; // smart decoding variable
			EStructuralFeature delegateControlSF = fFmapper.getFeature(fExpr);
			EObject thisEObject = thisBP.getEObject();
			if(thisEObject.eIsSet(delegateControlSF)){
				Object setting = thisEObject.eGet(delegateControlSF);
				if(setting instanceof EObject){
					if(fbeanPart.equals(fOwner.getBeanModel().getABean((EObject) setting)))
						sfNeedsUpdate = false;
				}
			}
			
			boolean allocationNeedsUpdate = true; // SF is set fine - how about allocation?
			if(fbeanPart.getEObject() instanceof IJavaInstance){
				IJavaInstance ji = (IJavaInstance) fbeanPart.getEObject();
				if(ji.isImplicitAllocation()){
					ImplicitAllocation ia = (ImplicitAllocation) ji.getAllocation();
					if(ia.getParent()!=null && ia.getParent().equals(thisEObject))
						allocationNeedsUpdate = false;
				}
			}
			
			
			if(sfNeedsUpdate)
				thisEObject.eSet(fFmapper.getFeature(fExpr), fbeanPart.getEObject());
			if(allocationNeedsUpdate)
				applyImplicitAllocation(fbeanPart, thisBP);
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.ExpressionDecoderHelper#restore()
	 */
	public boolean restore() throws CodeGenException {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.ExpressionDecoderHelper#generate(java.lang.Object[])
	 */
	public String generate(Object[] args) throws CodeGenException {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.ExpressionDecoderHelper#removeFromModel()
	 */
	public void removeFromModel() {
		BeanPart thisBP = fOwner.getBeanModel().getABean(BeanPart.THIS_NAME);
		if(thisBP!=null)
			thisBP.getEObject().eUnset(fFmapper.getFeature(fExpr));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.ExpressionDecoderHelper#primRefreshFromComposition(java.lang.String)
	 */
	public String primRefreshFromComposition(String expSig) throws CodeGenException {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.ExpressionDecoderHelper#primIsDeleted()
	 */
	public boolean primIsDeleted() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.ExpressionDecoderHelper#getArgsHandles(org.eclipse.jdt.core.dom.Statement)
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

	protected void applyImplicitAllocation(BeanPart implicitCompositeBP, BeanPart workbenchPartBP) {
		if(implicitCompositeBP.getEObject() instanceof IJavaInstance){
			ImplicitAllocation implicitalloc = InstantiationFactory.eINSTANCE.createImplicitAllocation(workbenchPartBP.getEObject(), fFmapper.getFeature(fExpr));
			((IJavaInstance)implicitCompositeBP.getEObject()).setAllocation(implicitalloc);
		}
	}
}

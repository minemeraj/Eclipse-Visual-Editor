/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  Created Oct 5, 2005 by Gili Mendel
 * 
 *  $RCSfile: ViewerDecoder.java,v $
 *  $Revision: 1.3 $  $Date: 2005-11-10 20:48:26 $ 
 */
 
package org.eclipse.ve.internal.jface.codegen;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.java.AllocationFeatureMapper;
import org.eclipse.ve.internal.java.codegen.java.ObjectDecoder;
import org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper.VEexpressionPriority;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.CodeExpressionRef;
 
/**
 * This decoder deal with the fact that Viewers may have
 * a visual given as part of thier constructor
 * 
 * @since 1.2.0
 */
public class ViewerDecoder extends ObjectDecoder {

	protected void initialDecoderHelper() {
		
		if (fFeatureMapper.getFeature(null).getName().equals(AllocationFeatureMapper.ALLOCATION_FEATURE)) {
			fhelper =  new ViewerConstructorDecoderHelper(fbeanPart, fExpr, fFeatureMapper, this);
		}
		else		
		   super.initialDecoderHelper();
	}

	public VEexpressionPriority determinePriority() {
		fPriority = super.determinePriority();
		if (getExprRef().isStateSet(CodeExpressionRef.STATE_INIT_EXPR)) {
			EStructuralFeature ifeature = ConstructorDecoderHelper.getRequiredImplicitFeature((IJavaObjectInstance)fbeanPart.getEObject());
			if (ifeature!=null && fPriority.getProiorityIndex()==null) {
				// We may need to get the index priority of our visual
				EObject implicit = (EObject) fbeanPart.getEObject().eGet(ifeature);
				BeanPart ibp = fBeanModel.getABean(implicit);
				if (ibp!=null && ibp.isImplicit()) {
					CodeExpressionRef initExpr = ibp.getInitExpression();
					if (initExpr!=null) {
						VEexpressionPriority impPriority = initExpr.getPriority();
						fPriority = new IJavaFeatureMapper.VEexpressionPriority(fPriority.getProiority(), impPriority.getProiorityIndex());
					}
				}				
			}
		}		
		return fPriority;
	}

}

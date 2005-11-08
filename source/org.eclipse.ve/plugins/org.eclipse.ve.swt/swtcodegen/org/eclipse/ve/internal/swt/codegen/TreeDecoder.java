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
 *  $RCSfile: TreeDecoder.java,v $
 *  $Revision: 1.2 $  $Date: 2005-11-08 16:51:45 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.codegen.java.ConstructorDecoderHelper;
import org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper;
import org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper.VEexpressionPriority;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.CodeExpressionRef;

import org.eclipse.ve.internal.swt.SWTConstants;

 

/**
 * @author sri
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TreeDecoder extends CompositeDecoder {
	protected final static String ADD_METHOD_PREFIX = "create"; //$NON-NLS-1$
	protected final static String ADD_METHOD_SF_NAME = URItoFeature(SWTConstants.SF_TREE_COLUMNS);
	protected final static String ADD_TREEITEMS_METHOD_PREFIX = "create"; //$NON-NLS-1$
	protected final static String ADD_TREEITEMS_METHOD_SF_NAME = URItoFeature(SWTConstants.SF_TREE_ITEMS);

	public TreeDecoder(){
		super();
		addStructuralFeatureAndWriteMethod(ADD_METHOD_SF_NAME, ADD_METHOD_PREFIX);
		addStructuralFeatureAndWriteMethod(ADD_TREEITEMS_METHOD_SF_NAME, ADD_TREEITEMS_METHOD_PREFIX);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.codegen.AbstractCompositeDecoder#getAppropriateFeatureMapper(java.lang.String)
	 */
	protected IJavaFeatureMapper getAppropriateFeatureMapper(String structuralFeature) {
		 if (structuralFeature.equals(ADD_METHOD_SF_NAME) || structuralFeature.equals(ADD_TREEITEMS_METHOD_SF_NAME))
		    return new CompositeFeatureMapper();
		return super.getAppropriateFeatureMapper(structuralFeature);
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
					VEexpressionPriority impPriority = initExpr.getPriority();
					fPriority = new IJavaFeatureMapper.VEexpressionPriority(fPriority.getProiority(), impPriority.getProiorityIndex()); 						
				}				
			}
		}		
		return fPriority;
	}
}

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
 *  $RCSfile: CompositeDecoder.java,v $
 *  $Revision: 1.20 $  $Date: 2005-08-24 23:52:56 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.*;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public class CompositeDecoder extends AbstractCompositeDecoder {

	protected final static String ADD_METHOD_PREFIX = "create"; //$NON-NLS-1$
	protected final static String ADD_METHOD_SF_NAME = "controls"; //$NON-NLS-1$


	// First element must be the SF/JCMMethod which has the true children.
	protected final static String[] writeMethodPrefix = { ADD_METHOD_PREFIX, AllocationFeatureMapper.NEW };
	public final static String[] structuralFeatures = { ADD_METHOD_SF_NAME, AllocationFeatureMapper.ALLOCATION_FEATURE }; //$NON-NLS-1$
	
	public CompositeDecoder(CodeExpressionRef expr, IBeanDeclModel model, IVEModelInstance cm, BeanPart part) {
		super(expr, model, cm, part, structuralFeatures, writeMethodPrefix);
	}

	public CompositeDecoder() {
		super(structuralFeatures, writeMethodPrefix);
	}
	
	protected boolean isCreateMethod(String prefix, EStructuralFeature sf) {
		
		return  !sf.getName().equals(AllocationFeatureMapper.ALLOCATION_FEATURE);
	}

	protected IExpressionDecoderHelper getAppropriateDecoderHelper(String structuralFeature) {
				
		EStructuralFeature sf = fbeanPart.getEObject().eClass().getEStructuralFeature(structuralFeature);
		if (sf != null && isCreateMethod(ADD_METHOD_PREFIX, sf))
			return new CompositeAddDecoderHelper(fbeanPart, fExpr, fFeatureMapper, this);
		else
			return null;
	}

	/*
	 * @see AbstractCompositionalDecoder#getAppropriateFeatureMapper(String)
	 */
	protected IJavaFeatureMapper getAppropriateFeatureMapper(String structuralFeature) {
		if (structuralFeature.equals(AllocationFeatureMapper.ALLOCATION_FEATURE))
			return new AllocationFeatureMapper(fbeanPart.getEObject());
		else if (structuralFeature.equals(ADD_METHOD_SF_NAME))
		    return new CompositeFeatureMapper();
		return null ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractCompositionalDecoder#isInternalPriorityCacheable()
	 */
	protected boolean isInternalPriorityCacheable() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IJVEDecoder#dispose()
	 * If this expression is disposed because it is removed from the source,
	 * we will remove the SWT orphaned child from the model
	 */
	public void dispose() {
        IExpressionDecoderHelper helper = fhelper ;
		super.dispose();
		if (helper instanceof CompositeAddDecoderHelper)
			((CompositeAddDecoderHelper)helper).removeChildFromModel() ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractExpressionDecoder#isPriorityCacheable()
	 */
	protected boolean isPriorityCacheable() {
		if ((fhelper instanceof ConstructorDecoderHelper) ||
			 fFeatureMapper.getFeature(null).getName().equals(ADD_METHOD_SF_NAME)) // createFoo()
			return false ;
		else
		    return super.isPriorityCacheable();
	}
	protected void initialFeatureMapper() {
		// If setBounds() or setSize() on composite, reduce its priority as re-lays out its children
		if(fFeatureMapper!=null)
			fFeatureMapper.setRefObject(null); // reset it as super.initalFeatureMappers could set one, else we blast one later
		if(	isMethodNamePresent(ISWTFeatureMapper.COMPOSITE_BOUNDS_NAME) || 
			isMethodNamePresent(ISWTFeatureMapper.COMPOSITE_SIZE_NAME)){
			fFeatureMapper = new CompositePropertyFeatureMapper();
		}else
			super.initialFeatureMapper() ;
		if(fFeatureMapper.getRefObject()==null)
			fFeatureMapper.setRefObject((IJavaInstance) fbeanPart.getEObject());
		if (fFeatureMapper.getFeature(fExpr) == null) {
			// not a regular property, but given that it was parsed in... this could be
			// a control feature
			String method = AbstractFeatureMapper.getWriteMethod(fExpr);
			if (method!=null) {
				if (method.indexOf(ADD_METHOD_PREFIX)>=0) {
					fFeatureMapper = getAppropriateFeatureMapper(ADD_METHOD_SF_NAME) ;
				}
			}			
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractExpressionDecoder#initialFeatureMapper(org.eclipse.emf.ecore.EStructuralFeature)
	 */
	protected void initialFeatureMapper(EStructuralFeature sf) {
		// If setBounds() or setSize() on composite, reduce its priority as re-lays out its children
		if(	sf!=null && ( ISWTFeatureMapper.COMPOSITE_SIZE_FEATURE_NAME.equals(sf.getName()) || ISWTFeatureMapper.COMPOSITE_BOUNDS_FEATURE_NAME.equals(sf.getName()))){
			fFeatureMapper = new CompositePropertyFeatureMapper();
			fFeatureMapper.setFeature(sf);
		}else
			super.initialFeatureMapper(sf);
	}
}

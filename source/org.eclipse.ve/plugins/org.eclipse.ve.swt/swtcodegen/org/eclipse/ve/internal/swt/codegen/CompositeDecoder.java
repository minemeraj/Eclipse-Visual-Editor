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
 *  $RCSfile: CompositeDecoder.java,v $
 *  $Revision: 1.3 $  $Date: 2004-01-28 00:47:08 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.java.* ;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public class CompositeDecoder extends AbstractCompositeDecoder {

	protected final static String ADD_METHOD_PREFIX = "create"; //$NON-NLS-1$
	protected final static String ADD_METHOD_SF_NAME = "controls"; //$NON-NLS-1$

	// First element must be the SF/JCMMethod which has the true children.
	protected final static String[] writeMethodPrefix = { ADD_METHOD_PREFIX };
	public final static String[] structuralFeatures = { ADD_METHOD_SF_NAME }; //$NON-NLS-1$
	
	public CompositeDecoder(CodeExpressionRef expr, IBeanDeclModel model, IDiagramModelInstance cm, BeanPart part) {
		super(expr, model, cm, part, structuralFeatures, writeMethodPrefix);
	}

	public CompositeDecoder() {
		super(structuralFeatures, writeMethodPrefix);
	}
	
	protected boolean isCreateMethod(String prefix, EStructuralFeature sf) {
		
		return true ;
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
		return new CompositeFeatureMapper();
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractCompositionalDecoder#isInternalPriorityCacheable()
	 */
	protected boolean isInternalPriorityCacheable() {
		return false;
	}
	

}

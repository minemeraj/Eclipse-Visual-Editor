/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.codegen;
/*
 *  $RCSfile: JMenuDecoder.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:12 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.*;

/**
 * @author gmendel
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class JMenuDecoder extends AbstractCompositionalDecoder {

	protected final static String ITEM_METHOD = "add"; //$NON-NLS-1$

	// First element must be the SF/JCMMethod which has the true children.
	protected final static String[] writeMethods = { ITEM_METHOD };
	protected final static String[] structuralFeatures = { IJFCFeatureMapper.JMEN_FEATURE_NAME }; //$NON-NLS-1$
	/**
	 *  A JPanel (maybe changing to Container ) Decoder will also add  ...
	 *  May need to augment it with other features in the future
	 */

	public JMenuDecoder(CodeExpressionRef expr, IBeanDeclModel model, IVEModelInstance cm, BeanPart part) {
		super(expr, model, cm, part, structuralFeatures, writeMethods);
	}

	public JMenuDecoder() {
		super(structuralFeatures, writeMethods);
	}

	protected IExpressionDecoderHelper getAppropriateDecoderHelper(String structuralFeature) {
		EStructuralFeature sf = fbeanPart.getEObject().eClass().getEStructuralFeature(structuralFeature);
		if (sf != null && isMethod(ITEM_METHOD, sf))
			return new JFCNoConstraintAddDecoderHelper(fbeanPart, fExpr, fFeatureMapper, this);
		else
			return null;
	}

	/*
	 * @see AbstractCompositionalDecoder#getAppropriateFeatureMapper(String)
	 */
	protected IJavaFeatureMapper getAppropriateFeatureMapper(String structuralFeature) {
		return new JMenuFeatureMapper();
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractCompositionalDecoder#isInternalPriorityCacheable()
	 */
	protected boolean isInternalPriorityCacheable() {
		return false;
	}

}

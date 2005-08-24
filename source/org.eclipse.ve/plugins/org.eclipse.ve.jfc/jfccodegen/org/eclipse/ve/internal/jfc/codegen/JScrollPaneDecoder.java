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
 *  $RCSfile: JScrollPaneDecoder.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:12 $ 
 */

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.*;

public class JScrollPaneDecoder extends AbstractCompositionalDecoder {

	public static final String SCROLLPANE_VIEWPORT_METHOD = "setViewportView"; //$NON-NLS-1$

	// First element must be the SF/JCMMethod which has the true children.
	protected static final String[] writeMethods = { SCROLLPANE_VIEWPORT_METHOD };
	protected static final String[] structuralFeatures = { "viewportView" }; //$NON-NLS-1$

	public JScrollPaneDecoder(CodeExpressionRef expr, IBeanDeclModel model, IVEModelInstance cm, BeanPart part) {
		super(expr, model, cm, part, structuralFeatures, writeMethods);
	}

	public JScrollPaneDecoder() {
		super(structuralFeatures, writeMethods);
	}

	protected IExpressionDecoderHelper getAppropriateDecoderHelper(String structuralFeature) {
		return new JFCChildRelationshipDecoderHelper(fbeanPart, fExpr, fFeatureMapper, this);
	}
	/*
	 * @see AbstractCompositionalDecoder#getAppropriateFeatureMapper(String)
	 */
	protected IJavaFeatureMapper getAppropriateFeatureMapper(String structuralFeature) {
		return null;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractCompositionalDecoder#isInternalPriorityCacheable()
	 */
	protected boolean isInternalPriorityCacheable() {
		return true;
	}

}

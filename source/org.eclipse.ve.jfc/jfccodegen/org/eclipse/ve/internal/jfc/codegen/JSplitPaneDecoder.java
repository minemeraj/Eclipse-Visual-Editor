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
 *  $RCSfile: JSplitPaneDecoder.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:12 $ 
 */

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.*;

/**
 * @version 	1.0
 * @author
 */
public class JSplitPaneDecoder extends AbstractCompositionalDecoder {

	public static final String SPLITPANE_LEFT_METHOD = "setLeftComponent"; //$NON-NLS-1$
	public static final String SPLITPANE_RIGHT_METHOD = "setRightComponent"; //$NON-NLS-1$
	public static final String SPLITPANE_TOP_METHOD = "setTopComponent"; //$NON-NLS-1$
	public static final String SPLITPANE_BOTTOM_METHOD = "setBottomComponent"; //$NON-NLS-1$

	// First element must be the SF/JCMMethod which has the true children.
	protected static final String[] writeMethods =
		{ SPLITPANE_LEFT_METHOD, SPLITPANE_RIGHT_METHOD, SPLITPANE_TOP_METHOD, SPLITPANE_BOTTOM_METHOD };
	protected static final String[] structuralFeatures = { "leftComponent", "rightComponent", "topComponent", "bottomComponent" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	public JSplitPaneDecoder(CodeExpressionRef expr, IBeanDeclModel model, IVEModelInstance cm, BeanPart part) {
		super(expr, model, cm, part, structuralFeatures, writeMethods);
	}

	public JSplitPaneDecoder() {
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

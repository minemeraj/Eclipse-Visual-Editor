package org.eclipse.ve.internal.jfc.codegen;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JFrameDecoder.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.*;

public class JFrameDecoder extends AbstractCompositionalDecoder {

	protected final static String CONTENT_PANE_METHOD = "setContentPane"; //$NON-NLS-1$
	protected final static String GLASS_PANE_METHOD = "setGlassPane"; //$NON-NLS-1$
	protected final static String MENU_BAR_METHOD = "setJMenuBar"; //$NON-NLS-1$
	protected final static String LAYERED_PANE_METHOD = "setLayeredPane"; //$NON-NLS-1$
	protected final static String ROOT_PANE_METHOD = "setRootPane"; //$NON-NLS-1$

	// First element must be the SF/JCMMethod which has the true children.
	protected final static String[] writeMethods =
		{ CONTENT_PANE_METHOD, GLASS_PANE_METHOD, MENU_BAR_METHOD, LAYERED_PANE_METHOD, ROOT_PANE_METHOD };
	protected final static String[] structuralFeatures = { "contentPane", //$NON-NLS-1$
		"glassPane", //$NON-NLS-1$
		"JMenuBar", //$NON-NLS-1$
		"layeredPane", //$NON-NLS-1$
		"rootPane" }; //$NON-NLS-1$
	/**
	 *  A JPanel (maybe changing to Container ) Decoder will also add  ...
	 *  May need to augment it with other features in the future
	 */

	public JFrameDecoder(CodeExpressionRef expr, IBeanDeclModel model, IDiagramModelInstance cm, BeanPart part) {
		super(expr, model, cm, part, structuralFeatures, writeMethods);
	}

	public JFrameDecoder() {
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
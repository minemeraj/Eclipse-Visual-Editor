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
 *  $RCSfile: JTableDecoder.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.*;

/**
 * @version 	1.0
 * @author
 */
public class JTableDecoder extends AbstractCompositionalDecoder {

	public static final String JTABLE_ADDCOLUMN_METHOD = "addColumn"; //$NON-NLS-1$
	public static final String JTABLE_COLUMNS_FEATURE_NAME = "columns"; //$NON-NLS-1$
	protected static String[] structualFeatures = new String[] { JTABLE_COLUMNS_FEATURE_NAME };
	protected static String[] writeMethodNames = new String[] { JTABLE_ADDCOLUMN_METHOD };

	/*
	 * @see AbstractCompositionalDecoder#getAppropriateDecoderHelper(String)
	 */
	/**
	 * Constructor for JTableDecoder.
	 * @param expr
	 * @param model
	 * @param cm
	 * @param part
	 * @param structualFeatures
	 * @param writeMethodNames
	 */
	public JTableDecoder(CodeExpressionRef expr, IBeanDeclModel model, IDiagramModelInstance cm, BeanPart part) {
		super(expr, model, cm, part, structualFeatures, writeMethodNames);
	}

	/**
	 * Constructor for JTableDecoder.
	 * @param structualFeatures
	 * @param writeMethodNames
	 */
	public JTableDecoder() {
		super(structualFeatures, writeMethodNames);
	}

	protected IExpressionDecoderHelper getAppropriateDecoderHelper(String structuralFeature) {
		// return new ChildRelationshipDecoderHelper(fbeanPart, fExpr,  fFeatureMapper, this);
		return new JFCNoConstraintAddDecoderHelper(fbeanPart, fExpr, fFeatureMapper, this);
	}

	public Vector getChildren(IJavaObjectInstance component) {
		Vector kids = super.getChildren(component);
		for (int sfc = 0; sfc < structuralFeatures.length; sfc++) { // Is SF containing children present?
			if (structuralFeatures[sfc].length() < 1)
				continue;
			EStructuralFeature sf = component.eClass().getEStructuralFeature(structuralFeatures[sfc]);
			if (!(component.eGet(sf) instanceof EList))
				continue;
			EList children = (EList) component.eGet(sf);
			Iterator itr = children.iterator();
			while (itr.hasNext()) {
				IJavaObjectInstance child = (IJavaObjectInstance) itr.next();
				if (child != null) {
					kids.add(child);
					kids.add(sf);
				}
			}
		}
		return kids;
	}
	/*
	 * @see AbstractCompositionalDecoder#getAppropriateFeatureMapper(String)
	 */
	protected IJavaFeatureMapper getAppropriateFeatureMapper(String structuralFeature) {
		return new JTableColumnFeatureMapper();
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractCompositionalDecoder#isInternalPriorityCacheable()
	 */
	protected boolean isInternalPriorityCacheable() {
		return false;
	}

}

/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.codegen;
/*
 *  $RCSfile: JTextComponentDecoder.java,v $
 *  $Revision: 1.4 $  $Date: 2004-08-27 15:34:49 $ 
 */

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper;
import org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper;
import org.eclipse.ve.internal.java.codegen.model.*;

/**
 * @version 	1.0
 * @author
 */
public class JTextComponentDecoder extends AbstractCompositionalDecoder {

	public static final String JTEXT_CARET_METHOD = "setCaret"; //$NON-NLS-1$
	public static final String JTEXT_CARET_FEATURE_NAME = "caret"; //$NON-NLS-1$
	protected static String[] structualFeatures = new String[] {JTEXT_CARET_FEATURE_NAME};
	protected static String[] writeMethodNames = new String[] {JTEXT_CARET_METHOD};
	
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
	public JTextComponentDecoder(
		CodeExpressionRef expr,
		IBeanDeclModel model,
		IVEModelInstance cm,
		BeanPart part) {
		super(expr, model, cm, part, structualFeatures, writeMethodNames);
	}

	/**
	 * Constructor for JTableDecoder.
	 * @param structualFeatures
	 * @param writeMethodNames
	 */
	public JTextComponentDecoder() {
		super(structualFeatures, writeMethodNames);
	}

	protected IExpressionDecoderHelper getAppropriateDecoderHelper(String structuralFeature) {
		return new JFCChildRelationshipDecoderHelper(fbeanPart, fExpr,  fFeatureMapper, this);
	}

	public List getChildren(IJavaObjectInstance component) {
		List kids = super.getChildren(component);
		for(int sfc=0;sfc<structuralFeatures.length;sfc++){ // Is SF containing children present?
			if(structuralFeatures[sfc].length()<1)
				continue;
			EStructuralFeature sf = component.eClass().getEStructuralFeature(structuralFeatures[sfc]) ;
			if(!(component.eGet(sf) instanceof EList))
				continue;
			EList children = (EList)component.eGet(sf) ;
			Iterator itr = children.iterator();
			while(itr.hasNext()){
				IJavaObjectInstance child = (IJavaObjectInstance) itr.next();
				if (child != null) {
					kids.add(child) ;
					kids.add(sf) ;
				}
			}
		}
		return kids ;
	}
	/*
	 * @see AbstractCompositionalDecoder#getAppropriateFeatureMapper(String)
	 */
	protected IJavaFeatureMapper getAppropriateFeatureMapper(String structuralFeature) {
		return null ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractCompositionalDecoder#isInternalPriorityCacheable()
	 */
	protected boolean isInternalPriorityCacheable() {
		return true;
	}

}

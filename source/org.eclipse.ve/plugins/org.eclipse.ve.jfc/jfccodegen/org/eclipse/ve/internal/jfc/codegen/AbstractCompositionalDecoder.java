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
 *  $RCSfile: AbstractCompositionalDecoder.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 23:13:34 $ 
 */
import java.util.Vector;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper;
import org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;

public abstract class AbstractCompositionalDecoder extends ContainerDecoder {

	protected String[] structuralFeatures;
	protected String[] writeMethodNames;

	public AbstractCompositionalDecoder(
		CodeExpressionRef expr,
		IBeanDeclModel model,
		IDiagramModelInstance cm,
		BeanPart part,
		String[] structualFeatures,
		String[] writeMethodNames) {
		super(expr, model, cm, part);
		setReferenceStructuralFeatures(structualFeatures);
		setReferenceWriteMethodNames(writeMethodNames);
	}

	public AbstractCompositionalDecoder(String[] structualFeatures, String[] writeMethodNames) {
		super();
		setReferenceStructuralFeatures(structualFeatures);
		setReferenceWriteMethodNames(writeMethodNames);
	}

	/**
	 *  Returns the Decoder Helper suitable for a structural feature.
	 */
	abstract protected IExpressionDecoderHelper getAppropriateDecoderHelper(String structuralFeature);
	abstract protected IJavaFeatureMapper getAppropriateFeatureMapper(String structuralFeature);

	/**
	 * Get the first level descendents. Requires the first
	 * structural feature in the array to be the holder of
	 * of the true children.
	 */
	public Vector getChildren(IJavaObjectInstance component) {
		Vector kids = super.getChildren(component);
		// Look for delegated children
		for (int sfc = 0; sfc < structuralFeatures.length; sfc++) { // Is SF containing children present?
			if (structuralFeatures[sfc].length() < 1)
				continue;
			EStructuralFeature sf = component.eClass().getEStructuralFeature(structuralFeatures[sfc]);
			if (kids.contains(sf))
				continue;

			Object value = null;
			try {
				value = component.eGet(sf);
			} catch (Exception e) {
				org.eclipse.ve.internal.java.core.JavaVEPlugin.log("AbstractCompotionDecoder.getChildren() Error: Obj= " + component + " SF: " + sf); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if (value == null || (!(value instanceof IJavaObjectInstance)))
				continue;
			IJavaObjectInstance child = (IJavaObjectInstance) component.eGet(sf);
			if (child != null) {
				kids.add(child);
				kids.add(sf);
			}
		}
		return kids;
	}

	protected abstract boolean isInternalPriorityCacheable();

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractExpressionDecoder#isPriorityCacheable()
	 */
	protected boolean isPriorityCacheable() {
		for (int mName = 0; mName < writeMethodNames.length; mName++) {
			if (isMethodNamePresent(writeMethodNames[mName])) {
				return isInternalPriorityCacheable();
			}
		}
		if (fhelper == null) {
			// For structural features which dont have property decorators and no CodeExpressionRef (VCE->Java)
			for (int sName = 0; sName < structuralFeatures.length; sName++) {
				if (isSFPresent(structuralFeatures[sName])) {
					return isInternalPriorityCacheable();
				}
			}
		}
		return super.isPriorityCacheable();
	}

	protected void initialDecoderHelper() {
		for (int mName = 0; mName < writeMethodNames.length; mName++) {
			if (isMethodNamePresent(writeMethodNames[mName])) {
				fhelper = getAppropriateDecoderHelper(structuralFeatures[mName]);
				break;
			}
		}
		if (fhelper == null) {
			// For structural features which dont have property decorators and no CodeExpressionRef (VCE->Java)
			for (int sName = 0; sName < structuralFeatures.length; sName++) {
				if (isSFPresent(structuralFeatures[sName])) {
					fhelper = getAppropriateDecoderHelper(structuralFeatures[sName]);
					break;
				}
			}
		}
		if (fhelper == null)
			super.initialDecoderHelper();
	}

	/**
	 *  Which FeatureMapper to use
	 */
	protected void initialFeatureMapper(EStructuralFeature sf) {
		for (int sName = 0; sName < structuralFeatures.length; sName++) {
			EStructuralFeature eSF = fbeanPart.getEObject().eClass().getEStructuralFeature(structuralFeatures[sName]);
			if (sf.equals(eSF)) {
				fFeatureMapper = getAppropriateFeatureMapper(structuralFeatures[sName]);
				if (fFeatureMapper != null)
					fFeatureMapper.setFeature(sf);
				break;
			}
		}
		if (fFeatureMapper == null)
			super.initialFeatureMapper(sf);
	}

	/**
	 *  Which FeatureMapper to use
	 */
	protected void initialFeatureMapper() {
		for (int mName = 0; mName < writeMethodNames.length; mName++) {
			if (isMethodNamePresent(writeMethodNames[mName])) {
				fFeatureMapper = getAppropriateFeatureMapper(structuralFeatures[mName]);
				break;
			}
		}
		if (fFeatureMapper == null)
			super.initialFeatureMapper();
	}

	/**
	 *  Is this a component add expression
	 */
	protected boolean isSFPresent(String sfName) {
		String sf = null;
		if (fFeatureMapper != null)
			if (fFeatureMapper.getFeatureName() != null)
				sf = fFeatureMapper.getFeatureName();
		return sf != null && sf.equals(sfName);
	}

	/**
	 *  Is this a component add expression
	 */
	protected boolean isMethodNamePresent(String methodName) {
		String method = null;
		if (fFeatureMapper != null)
			if (fFeatureMapper.getDecorator() != null)
				method = fFeatureMapper.getDecorator().getWriteMethod().getName();
			else
				// Specelized Feature mappers may be able to get it anyhow.
				method = fFeatureMapper.getMethodName();
		if (method == null)
			method = CodeGenUtil.getWriteMethod(fExpr);
		return method != null && method.equals(methodName);
	}

	protected void setReferenceStructuralFeatures(String[] structuralFeatures) {
		this.structuralFeatures = structuralFeatures;
	}

	protected void setReferenceWriteMethodNames(String[] writeMethodNames) {
		this.writeMethodNames = writeMethodNames;
	}

}
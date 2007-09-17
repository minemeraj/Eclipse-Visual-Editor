/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.palette.impl;
/*
 *  $RCSfile: CreationToolEntryImpl.java,v $
 *  $Revision: 1.8 $  $Date: 2007-09-17 14:17:13 $ 
 */

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.*;

import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.requests.CreationFactory;


import org.eclipse.ve.internal.cde.core.AnnotationCreationFactory;
import org.eclipse.ve.internal.cde.core.CDECreationTool;
import org.eclipse.ve.internal.cde.palette.CreationToolEntry;
import org.eclipse.ve.internal.cde.palette.PalettePackage;



import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.KeyedValueHolder;




import org.eclipse.ve.internal.cdm.impl.MapEntryImpl;
import org.eclipse.ve.internal.cdm.model.KeyedValueHolderHelper;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Creation Tool Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.CreationToolEntryImpl#getKeyedValues <em>Keyed Values</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class CreationToolEntryImpl extends AbstractToolEntryImpl implements CreationToolEntry {

	/**
	 * The cached value of the '{@link #getKeyedValues() <em>Keyed Values</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKeyedValues()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, String> keyedValues;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CreationToolEntryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PalettePackage.Literals.CREATION_TOOL_ENTRY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public EMap getKeyedValues() {
		if (keyedValues == null) {
			keyedValues = KeyedValueHolderHelper.createKeyedValuesEMap(this, PalettePackage.CREATION_TOOL_ENTRY__KEYED_VALUES);
		}
		return keyedValues;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PalettePackage.CREATION_TOOL_ENTRY__KEYED_VALUES:
				return ((InternalEList<?>)getKeyedValues()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PalettePackage.CREATION_TOOL_ENTRY__KEYED_VALUES:
				if (coreType) return getKeyedValues();
				else return getKeyedValues().map();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case PalettePackage.CREATION_TOOL_ENTRY__KEYED_VALUES:
				((EStructuralFeature.Setting)getKeyedValues()).set(newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case PalettePackage.CREATION_TOOL_ENTRY__KEYED_VALUES:
				getKeyedValues().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case PalettePackage.CREATION_TOOL_ENTRY__KEYED_VALUES:
				return keyedValues != null && !keyedValues.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	public EObject eObjectForURIFragmentSegment(String uriFragmentSegment) {
		EObject eo = KeyedValueHolderHelper.eObjectForURIFragmentSegment(this, uriFragmentSegment);
		return eo == KeyedValueHolderHelper.NOT_KEYED_VALUES_FRAGMENT ? super.eObjectForURIFragmentSegment(uriFragmentSegment) : eo;
	}


	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == KeyedValueHolder.class) {
			switch (derivedFeatureID) {
				case PalettePackage.CREATION_TOOL_ENTRY__KEYED_VALUES: return CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == KeyedValueHolder.class) {
			switch (baseFeatureID) {
				case CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES: return PalettePackage.CREATION_TOOL_ENTRY__KEYED_VALUES;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	protected PaletteEntry createPaletteEntry() {
		org.eclipse.gef.palette.CreationToolEntry ce = new org.eclipse.gef.palette.CreationToolEntry(getLabel(), getDescription(), getFactory(), getSmallIcon(), getLargeIcon());
		ce.setToolClass(getCreationToolClass());
		return ce;
	}
	
	/**
	 * Get the creation tool class to use. The default is CDECreationTool. Subclasses should override if they need a different one. Typically
	 * a different one is not needed, the factory takes care of the needed differentiation.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected Class getCreationToolClass() {
		return CDECreationTool.class;
	}
	/*
	 * Create a new factory and wrapper it within an annotation factory if necessary.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private CreationFactory getFactory() {
		CreationFactory f = createFactory();
		if (eIsSet(CDMPackage.eINSTANCE.getKeyedValueHolder_KeyedValues())) {
			f = new AnnotationCreationFactory(getKeyedValues(), f);
		}
		return f;
	}
	/**
	 * Create the factory for this creation tool.
	 * <p>
	 * <b>Note:</b> This factory may become wrappered within another factory. This other factory is used to add in any
	 * annotations (such as NameInComposition") that may be stored in the keyed values.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected abstract CreationFactory createFactory();

}

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
package org.eclipse.ve.internal.cde.palette.impl;
/*
 *  $RCSfile: CreationToolEntryImpl.java,v $
 *  $Revision: 1.5 $  $Date: 2005-09-15 21:27:15 $ 
 */
import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.*;

import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.requests.CreationFactory;


import org.eclipse.ve.internal.cde.core.AnnotationCreationFactory;
import org.eclipse.ve.internal.cde.core.CDECreationTool;
import org.eclipse.ve.internal.cde.palette.CreationToolEntry;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.Permissions;

import org.eclipse.ve.internal.cde.utility.AbstractString;


import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.KeyedValueHolder;



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
	protected EMap keyedValues = null;

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
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getCreationToolEntry();
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
	
	public EObject eObjectForURIFragmentSegment(String uriFragmentSegment) {
		EObject eo = KeyedValueHolderHelper.eObjectForURIFragmentSegment(this, uriFragmentSegment);
		return eo == KeyedValueHolderHelper.NOT_KEYED_VALUES_FRAGMENT ? super.eObjectForURIFragmentSegment(uriFragmentSegment) : eo;
	}


	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.CREATION_TOOL_ENTRY__ENTRY_LABEL:
					return basicSetEntryLabel(null, msgs);
				case PalettePackage.CREATION_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
					return basicSetEntryShortDescription(null, msgs);
				case PalettePackage.CREATION_TOOL_ENTRY__STRING_PROPERTIES:
					return ((InternalEList)getStringProperties()).basicRemove(otherEnd, msgs);
				case PalettePackage.CREATION_TOOL_ENTRY__KEYED_VALUES:
					return ((InternalEList)getKeyedValues()).basicRemove(otherEnd, msgs);
				default:
					return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
			}
		}
		return eBasicSetContainer(null, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case PalettePackage.CREATION_TOOL_ENTRY__ICON16_NAME:
				return getIcon16Name();
			case PalettePackage.CREATION_TOOL_ENTRY__ICON32_NAME:
				return getIcon32Name();
			case PalettePackage.CREATION_TOOL_ENTRY__VISIBLE:
				return isVisible() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.CREATION_TOOL_ENTRY__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.CREATION_TOOL_ENTRY__ID:
				return getId();
			case PalettePackage.CREATION_TOOL_ENTRY__MODIFICATION:
				return getModification();
			case PalettePackage.CREATION_TOOL_ENTRY__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.CREATION_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
			case PalettePackage.CREATION_TOOL_ENTRY__STRING_PROPERTIES:
				return getStringProperties();
			case PalettePackage.CREATION_TOOL_ENTRY__KEYED_VALUES:
				return getKeyedValues();
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(EStructuralFeature eFeature, Object newValue) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case PalettePackage.CREATION_TOOL_ENTRY__ICON16_NAME:
				setIcon16Name((String)newValue);
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__ICON32_NAME:
				setIcon32Name((String)newValue);
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__VISIBLE:
				setVisible(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__ID:
				setId((String)newValue);
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__MODIFICATION:
				setModification((Permissions)newValue);
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)newValue);
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__STRING_PROPERTIES:
				getStringProperties().clear();
				getStringProperties().addAll((Collection)newValue);
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__KEYED_VALUES:
				getKeyedValues().clear();
				getKeyedValues().addAll((Collection)newValue);
				return;
		}
		eDynamicSet(eFeature, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case PalettePackage.CREATION_TOOL_ENTRY__ICON16_NAME:
				setIcon16Name(ICON16_NAME_EDEFAULT);
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__ICON32_NAME:
				setIcon32Name(ICON32_NAME_EDEFAULT);
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__ID:
				setId(ID_EDEFAULT);
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__MODIFICATION:
				setModification(MODIFICATION_EDEFAULT);
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)null);
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__STRING_PROPERTIES:
				getStringProperties().clear();
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__KEYED_VALUES:
				getKeyedValues().clear();
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case PalettePackage.CREATION_TOOL_ENTRY__ICON16_NAME:
				return ICON16_NAME_EDEFAULT == null ? icon16Name != null : !ICON16_NAME_EDEFAULT.equals(icon16Name);
			case PalettePackage.CREATION_TOOL_ENTRY__ICON32_NAME:
				return ICON32_NAME_EDEFAULT == null ? icon32Name != null : !ICON32_NAME_EDEFAULT.equals(icon32Name);
			case PalettePackage.CREATION_TOOL_ENTRY__VISIBLE:
				return ((eFlags & VISIBLE_EFLAG) != 0) != VISIBLE_EDEFAULT;
			case PalettePackage.CREATION_TOOL_ENTRY__DEFAULT_ENTRY:
				return isDefaultEntry() != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.CREATION_TOOL_ENTRY__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case PalettePackage.CREATION_TOOL_ENTRY__MODIFICATION:
				return modification != MODIFICATION_EDEFAULT;
			case PalettePackage.CREATION_TOOL_ENTRY__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.CREATION_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
			case PalettePackage.CREATION_TOOL_ENTRY__STRING_PROPERTIES:
				return stringProperties != null && !stringProperties.isEmpty();
			case PalettePackage.CREATION_TOOL_ENTRY__KEYED_VALUES:
				return keyedValues != null && !keyedValues.isEmpty();
		}
		return eDynamicIsSet(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class baseClass) {
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
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class baseClass) {
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

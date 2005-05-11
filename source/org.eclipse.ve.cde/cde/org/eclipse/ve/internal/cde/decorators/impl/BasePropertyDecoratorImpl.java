package org.eclipse.ve.internal.cde.decorators.impl;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: BasePropertyDecoratorImpl.java,v $
 *  $Revision: 1.5 $  $Date: 2005-05-11 22:41:15 $ 
 */
import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EAnnotationImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.jface.viewers.ILabelProvider;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ve.internal.cde.core.CDEMessages;
import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator;
import org.eclipse.ve.internal.cde.decorators.DecoratorsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Base Property Decorator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.BasePropertyDecoratorImpl#getCellEditorValidatorClassnames <em>Cell Editor Validator Classnames</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.BasePropertyDecoratorImpl#getLabelProviderClassname <em>Label Provider Classname</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.BasePropertyDecoratorImpl#getCellEditorClassname <em>Cell Editor Classname</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.BasePropertyDecoratorImpl#isNullInvalid <em>Null Invalid</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.BasePropertyDecoratorImpl#isEntryExpandable <em>Entry Expandable</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public class BasePropertyDecoratorImpl extends EAnnotationImpl implements BasePropertyDecorator {

	

	/**
	 * The cached value of the '{@link #getCellEditorValidatorClassnames() <em>Cell Editor Validator Classnames</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCellEditorValidatorClassnames()
	 * @generated
	 * @ordered
	 */
	protected EList cellEditorValidatorClassnames = null;
	/**
	 * The default value of the '{@link #getLabelProviderClassname() <em>Label Provider Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabelProviderClassname()
	 * @generated
	 * @ordered
	 */
	protected static final String LABEL_PROVIDER_CLASSNAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLabelProviderClassname() <em>Label Provider Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabelProviderClassname()
	 * @generated
	 * @ordered
	 */
	protected String labelProviderClassname = LABEL_PROVIDER_CLASSNAME_EDEFAULT;
	/**
	 * This is true if the Label Provider Classname attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean labelProviderClassnameESet = false;

	/**
	 * The default value of the '{@link #getCellEditorClassname() <em>Cell Editor Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCellEditorClassname()
	 * @generated
	 * @ordered
	 */
	protected static final String CELL_EDITOR_CLASSNAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCellEditorClassname() <em>Cell Editor Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCellEditorClassname()
	 * @generated
	 * @ordered
	 */
	protected String cellEditorClassname = CELL_EDITOR_CLASSNAME_EDEFAULT;
	/**
	 * This is true if the Cell Editor Classname attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean cellEditorClassnameESet = false;

	/**
	 * The default value of the '{@link #isNullInvalid() <em>Null Invalid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isNullInvalid()
	 * @generated
	 * @ordered
	 */
	protected static final boolean NULL_INVALID_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isNullInvalid() <em>Null Invalid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isNullInvalid()
	 * @generated
	 * @ordered
	 */
	protected boolean nullInvalid = NULL_INVALID_EDEFAULT;

	/**
	 * This is true if the Null Invalid attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean nullInvalidESet = false;

	/**
	 * The default value of the '{@link #isEntryExpandable() <em>Entry Expandable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isEntryExpandable()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ENTRY_EXPANDABLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isEntryExpandable() <em>Entry Expandable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isEntryExpandable()
	 * @generated
	 * @ordered
	 */
	protected boolean entryExpandable = ENTRY_EXPANDABLE_EDEFAULT;

	/**
	 * This is true if the Entry Expandable attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean entryExpandableESet = false;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected BasePropertyDecoratorImpl() {
		super();
		setSource(this.getClass().getName());		
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return DecoratorsPackage.eINSTANCE.getBasePropertyDecorator();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getCellEditorValidatorClassnames() {
		if (cellEditorValidatorClassnames == null) {
			cellEditorValidatorClassnames = new EDataTypeUniqueEList(String.class, this, DecoratorsPackage.BASE_PROPERTY_DECORATOR__CELL_EDITOR_VALIDATOR_CLASSNAMES);
		}
		return cellEditorValidatorClassnames;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLabelProviderClassname() {
		return labelProviderClassname;
	}

	public void setLabelProviderClassname(String newLabelProviderClassname) {
		hasInitializedLabelProvider = false;
		labelProviderClass = null;
		labelProviderConstructor = null;
		setLabelProviderClassnameGen(newLabelProviderClassname);
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLabelProviderClassnameGen(String newLabelProviderClassname) {
		String oldLabelProviderClassname = labelProviderClassname;
		labelProviderClassname = newLabelProviderClassname;
		boolean oldLabelProviderClassnameESet = labelProviderClassnameESet;
		labelProviderClassnameESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.BASE_PROPERTY_DECORATOR__LABEL_PROVIDER_CLASSNAME, oldLabelProviderClassname, labelProviderClassname, !oldLabelProviderClassnameESet));
	}

	public void unsetLabelProviderClassname() {
		hasInitializedLabelProvider = false;
		labelProviderClass = null;
		labelProviderConstructor = null;
		unsetLabelProviderClassnameGen();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetLabelProviderClassnameGen() {
		String oldLabelProviderClassname = labelProviderClassname;
		boolean oldLabelProviderClassnameESet = labelProviderClassnameESet;
		labelProviderClassname = LABEL_PROVIDER_CLASSNAME_EDEFAULT;
		labelProviderClassnameESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DecoratorsPackage.BASE_PROPERTY_DECORATOR__LABEL_PROVIDER_CLASSNAME, oldLabelProviderClassname, LABEL_PROVIDER_CLASSNAME_EDEFAULT, oldLabelProviderClassnameESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetLabelProviderClassname() {
		return labelProviderClassnameESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCellEditorClassname() {
		return cellEditorClassname;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCellEditorClassname(String newCellEditorClassname) {
		String oldCellEditorClassname = cellEditorClassname;
		cellEditorClassname = newCellEditorClassname;
		boolean oldCellEditorClassnameESet = cellEditorClassnameESet;
		cellEditorClassnameESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.BASE_PROPERTY_DECORATOR__CELL_EDITOR_CLASSNAME, oldCellEditorClassname, cellEditorClassname, !oldCellEditorClassnameESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetCellEditorClassname() {
		String oldCellEditorClassname = cellEditorClassname;
		boolean oldCellEditorClassnameESet = cellEditorClassnameESet;
		cellEditorClassname = CELL_EDITOR_CLASSNAME_EDEFAULT;
		cellEditorClassnameESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DecoratorsPackage.BASE_PROPERTY_DECORATOR__CELL_EDITOR_CLASSNAME, oldCellEditorClassname, CELL_EDITOR_CLASSNAME_EDEFAULT, oldCellEditorClassnameESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetCellEditorClassname() {
		return cellEditorClassnameESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isNullInvalid() {
		return nullInvalid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNullInvalid(boolean newNullInvalid) {
		boolean oldNullInvalid = nullInvalid;
		nullInvalid = newNullInvalid;
		boolean oldNullInvalidESet = nullInvalidESet;
		nullInvalidESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.BASE_PROPERTY_DECORATOR__NULL_INVALID, oldNullInvalid, nullInvalid, !oldNullInvalidESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetNullInvalid() {
		boolean oldNullInvalid = nullInvalid;
		boolean oldNullInvalidESet = nullInvalidESet;
		nullInvalid = NULL_INVALID_EDEFAULT;
		nullInvalidESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DecoratorsPackage.BASE_PROPERTY_DECORATOR__NULL_INVALID, oldNullInvalid, NULL_INVALID_EDEFAULT, oldNullInvalidESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetNullInvalid() {
		return nullInvalidESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isEntryExpandable() {
		return entryExpandable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEntryExpandable(boolean newEntryExpandable) {
		boolean oldEntryExpandable = entryExpandable;
		entryExpandable = newEntryExpandable;
		boolean oldEntryExpandableESet = entryExpandableESet;
		entryExpandableESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.BASE_PROPERTY_DECORATOR__ENTRY_EXPANDABLE, oldEntryExpandable, entryExpandable, !oldEntryExpandableESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetEntryExpandable() {
		boolean oldEntryExpandable = entryExpandable;
		boolean oldEntryExpandableESet = entryExpandableESet;
		entryExpandable = ENTRY_EXPANDABLE_EDEFAULT;
		entryExpandableESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DecoratorsPackage.BASE_PROPERTY_DECORATOR__ENTRY_EXPANDABLE, oldEntryExpandable, ENTRY_EXPANDABLE_EDEFAULT, oldEntryExpandableESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetEntryExpandable() {
		return entryExpandableESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case DecoratorsPackage.BASE_PROPERTY_DECORATOR__EANNOTATIONS:
					return ((InternalEList)getEAnnotations()).basicAdd(otherEnd, msgs);
				case DecoratorsPackage.BASE_PROPERTY_DECORATOR__EMODEL_ELEMENT:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, DecoratorsPackage.BASE_PROPERTY_DECORATOR__EMODEL_ELEMENT, msgs);
				default:
					return eDynamicInverseAdd(otherEnd, featureID, baseClass, msgs);
			}
		}
		if (eContainer != null)
			msgs = eBasicRemoveFromContainer(msgs);
		return eBasicSetContainer(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case DecoratorsPackage.BASE_PROPERTY_DECORATOR__EANNOTATIONS:
					return ((InternalEList)getEAnnotations()).basicRemove(otherEnd, msgs);
				case DecoratorsPackage.BASE_PROPERTY_DECORATOR__DETAILS:
					return ((InternalEList)getDetails()).basicRemove(otherEnd, msgs);
				case DecoratorsPackage.BASE_PROPERTY_DECORATOR__EMODEL_ELEMENT:
					return eBasicSetContainer(null, DecoratorsPackage.BASE_PROPERTY_DECORATOR__EMODEL_ELEMENT, msgs);
				case DecoratorsPackage.BASE_PROPERTY_DECORATOR__CONTENTS:
					return ((InternalEList)getContents()).basicRemove(otherEnd, msgs);
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
	public NotificationChain eBasicRemoveFromContainer(NotificationChain msgs) {
		if (eContainerFeatureID >= 0) {
			switch (eContainerFeatureID) {
				case DecoratorsPackage.BASE_PROPERTY_DECORATOR__EMODEL_ELEMENT:
					return eContainer.eInverseRemove(this, EcorePackage.EMODEL_ELEMENT__EANNOTATIONS, EModelElement.class, msgs);
				default:
					return eDynamicBasicRemoveFromContainer(msgs);
			}
		}
		return eContainer.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - eContainerFeatureID, null, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__EANNOTATIONS:
				return getEAnnotations();
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__SOURCE:
				return getSource();
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__DETAILS:
				return getDetails();
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__EMODEL_ELEMENT:
				return getEModelElement();
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__CONTENTS:
				return getContents();
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__REFERENCES:
				return getReferences();
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__CELL_EDITOR_VALIDATOR_CLASSNAMES:
				return getCellEditorValidatorClassnames();
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__LABEL_PROVIDER_CLASSNAME:
				return getLabelProviderClassname();
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__CELL_EDITOR_CLASSNAME:
				return getCellEditorClassname();
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__NULL_INVALID:
				return isNullInvalid() ? Boolean.TRUE : Boolean.FALSE;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__ENTRY_EXPANDABLE:
				return isEntryExpandable() ? Boolean.TRUE : Boolean.FALSE;
		}
		return eDynamicGet(eFeature, resolve);
	}

	/*
	 * Called by overrides to eIsSet to test if source is set. This is because for the 
	 * FeatureDecorator and subclasses, setting source to the classname is considered
	 * to be not set since that is the new default for each class level. By doing this
	 * when serializing it won't waste space and time adding a copy of the source string
	 * to the serialized output and then creating a NEW copy on each decorator loaded
	 * from an XMI file. 
	 * 
	 * @return <code>true</code> if source is not null and not equal to class name.
	 * 
	 * @since 1.1.0
	 */
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__SOURCE:
				return source != null && !getClass().getName().equals(source);
			default:
				return eIsSetGen(eFeature);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSetGen(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__EANNOTATIONS:
				return eAnnotations != null && !eAnnotations.isEmpty();
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__SOURCE:
				return SOURCE_EDEFAULT == null ? source != null : !SOURCE_EDEFAULT.equals(source);
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__DETAILS:
				return details != null && !details.isEmpty();
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__EMODEL_ELEMENT:
				return getEModelElement() != null;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__CONTENTS:
				return contents != null && !contents.isEmpty();
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__REFERENCES:
				return references != null && !references.isEmpty();
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__CELL_EDITOR_VALIDATOR_CLASSNAMES:
				return cellEditorValidatorClassnames != null && !cellEditorValidatorClassnames.isEmpty();
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__LABEL_PROVIDER_CLASSNAME:
				return isSetLabelProviderClassname();
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__CELL_EDITOR_CLASSNAME:
				return isSetCellEditorClassname();
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__NULL_INVALID:
				return isSetNullInvalid();
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__ENTRY_EXPANDABLE:
				return isSetEntryExpandable();
		}
		return eDynamicIsSet(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(EStructuralFeature eFeature, Object newValue) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__EANNOTATIONS:
				getEAnnotations().clear();
				getEAnnotations().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__SOURCE:
				setSource((String)newValue);
				return;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__DETAILS:
				getDetails().clear();
				getDetails().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__EMODEL_ELEMENT:
				setEModelElement((EModelElement)newValue);
				return;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__CONTENTS:
				getContents().clear();
				getContents().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__REFERENCES:
				getReferences().clear();
				getReferences().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__CELL_EDITOR_VALIDATOR_CLASSNAMES:
				getCellEditorValidatorClassnames().clear();
				getCellEditorValidatorClassnames().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__LABEL_PROVIDER_CLASSNAME:
				setLabelProviderClassname((String)newValue);
				return;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__CELL_EDITOR_CLASSNAME:
				setCellEditorClassname((String)newValue);
				return;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__NULL_INVALID:
				setNullInvalid(((Boolean)newValue).booleanValue());
				return;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__ENTRY_EXPANDABLE:
				setEntryExpandable(((Boolean)newValue).booleanValue());
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
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__EANNOTATIONS:
				getEAnnotations().clear();
				return;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__SOURCE:
				setSource(SOURCE_EDEFAULT);
				return;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__DETAILS:
				getDetails().clear();
				return;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__EMODEL_ELEMENT:
				setEModelElement((EModelElement)null);
				return;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__CONTENTS:
				getContents().clear();
				return;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__REFERENCES:
				getReferences().clear();
				return;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__CELL_EDITOR_VALIDATOR_CLASSNAMES:
				getCellEditorValidatorClassnames().clear();
				return;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__LABEL_PROVIDER_CLASSNAME:
				unsetLabelProviderClassname();
				return;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__CELL_EDITOR_CLASSNAME:
				unsetCellEditorClassname();
				return;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__NULL_INVALID:
				unsetNullInvalid();
				return;
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR__ENTRY_EXPANDABLE:
				unsetEntryExpandable();
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (cellEditorValidatorClassnames: ");
		result.append(cellEditorValidatorClassnames);
		result.append(", labelProviderClassname: ");
		if (labelProviderClassnameESet) result.append(labelProviderClassname); else result.append("<unset>");
		result.append(", cellEditorClassname: ");
		if (cellEditorClassnameESet) result.append(cellEditorClassname); else result.append("<unset>");
		result.append(", nullInvalid: ");
		if (nullInvalidESet) result.append(nullInvalid); else result.append("<unset>");
		result.append(", entryExpandable: ");
		if (entryExpandableESet) result.append(entryExpandable); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

	private Constructor labelProviderConstructor;	// Constructor that takes an IPropertyDescriptor - may not be present
	private Class labelProviderClass;
	private boolean hasInitializedLabelProvider = false;	// boolean to store whether or not a constructor has been looked for
	/**
	 * @param IPropertyDescriptor for the property the label provider is going to be used for
	 * @return An instantiated label provider 
	 */
	public ILabelProvider getLabelProvider(IPropertyDescriptor aPropertyDescriptor){
		
		if(!hasInitializedLabelProvider){
			if (getLabelProviderClassname() != null) {
				try {
					labelProviderClass = CDEPlugin.getClassFromString(getLabelProviderClassname());
					labelProviderConstructor = labelProviderClass.getConstructor(new Class[] { IPropertyDescriptor.class});
				} catch (ClassNotFoundException e) {
					CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, "", e)); //$NON-NLS-1$				
				} catch (NoSuchMethodException e) {
					// Do nothing - it is possible there is no constructor with an IPropertyDescriptor argument
					// in which case the default constructor will be used instead
				}
			}
			hasInitializedLabelProvider = true;
		}
		try {
			if (labelProviderConstructor != null) {
				return (ILabelProvider) CDEPlugin.setInitializationData(labelProviderConstructor.newInstance(new Object[] { aPropertyDescriptor }), getLabelProviderClassname(), null);
			} else if (labelProviderClass != null) {
				return (ILabelProvider) CDEPlugin.setInitializationData(labelProviderClass.newInstance(), getLabelProviderClassname(), null);
			} else
				return null;
		} catch (Exception exc) {
			String msg = MessageFormat.format(CDEMessages.getString("Object.noinstantiate_EXC_"), new Object[] { labelProviderClass }); //$NON-NLS-1$
			CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, msg, exc));			
			return null;
		}
	}
}

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
package org.eclipse.ve.internal.cde.decorators.impl;
import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.jface.viewers.ILabelProvider;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ve.internal.cde.core.CDEMessages;
import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator;
import org.eclipse.ve.internal.cde.decorators.DecoratorsPackage;
import org.eclipse.ve.internal.cde.decorators.PropertyDescriptorDecorator;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Property Descriptor Decorator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.PropertyDescriptorDecoratorImpl#getCellEditorValidatorClassnames <em>Cell Editor Validator Classnames</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.PropertyDescriptorDecoratorImpl#getLabelProviderClassname <em>Label Provider Classname</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.PropertyDescriptorDecoratorImpl#getCellEditorClassname <em>Cell Editor Classname</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.PropertyDescriptorDecoratorImpl#isNullInvalid <em>Null Invalid</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.PropertyDescriptorDecoratorImpl#isEntryExpandable <em>Entry Expandable</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.PropertyDescriptorDecoratorImpl#isDesigntimeProperty <em>Designtime Property</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.PropertyDescriptorDecoratorImpl#isAlwaysIncompatible <em>Always Incompatible</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public class PropertyDescriptorDecoratorImpl
	extends FeatureDescriptorDecoratorImpl
	implements PropertyDescriptorDecorator {

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
	 * The flag representing whether the Label Provider Classname attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected static final int LABEL_PROVIDER_CLASSNAME_ESETFLAG = 1 << 10;

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
	 * The flag representing whether the Cell Editor Classname attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected static final int CELL_EDITOR_CLASSNAME_ESETFLAG = 1 << 11;

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
	 * The flag representing the value of the '{@link #isNullInvalid() <em>Null Invalid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isNullInvalid()
	 * @generated
	 * @ordered
	 */
	protected static final int NULL_INVALID_EFLAG = 1 << 12;

	/**
	 * The flag representing whether the Null Invalid attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected static final int NULL_INVALID_ESETFLAG = 1 << 13;

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
	 * The flag representing the value of the '{@link #isEntryExpandable() <em>Entry Expandable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isEntryExpandable()
	 * @generated
	 * @ordered
	 */
	protected static final int ENTRY_EXPANDABLE_EFLAG = 1 << 14;

	/**
	 * The flag representing whether the Entry Expandable attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected static final int ENTRY_EXPANDABLE_ESETFLAG = 1 << 15;

	/**
	 * The default value of the '{@link #isDesigntimeProperty() <em>Designtime Property</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDesigntimeProperty()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DESIGNTIME_PROPERTY_EDEFAULT = false;

	
	/**
	 * The flag representing the value of the '{@link #isDesigntimeProperty() <em>Designtime Property</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDesigntimeProperty()
	 * @generated
	 * @ordered
	 */
	protected static final int DESIGNTIME_PROPERTY_EFLAG = 1 << 16;

	/**
	 * The flag representing whether the Designtime Property attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected static final int DESIGNTIME_PROPERTY_ESETFLAG = 1 << 17;

	/**
	 * The default value of the '{@link #isAlwaysIncompatible() <em>Always Incompatible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAlwaysIncompatible()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ALWAYS_INCOMPATIBLE_EDEFAULT = false;

	/**
	 * The flag representing the value of the '{@link #isAlwaysIncompatible() <em>Always Incompatible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAlwaysIncompatible()
	 * @generated
	 * @ordered
	 */
	protected static final int ALWAYS_INCOMPATIBLE_EFLAG = 1 << 18;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PropertyDescriptorDecoratorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return DecoratorsPackage.Literals.PROPERTY_DESCRIPTOR_DECORATOR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isDesigntimeProperty() {
		return (eFlags & DESIGNTIME_PROPERTY_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDesigntimeProperty(boolean newDesigntimeProperty) {
		boolean oldDesigntimeProperty = (eFlags & DESIGNTIME_PROPERTY_EFLAG) != 0;
		if (newDesigntimeProperty) eFlags |= DESIGNTIME_PROPERTY_EFLAG; else eFlags &= ~DESIGNTIME_PROPERTY_EFLAG;
		boolean oldDesigntimePropertyESet = (eFlags & DESIGNTIME_PROPERTY_ESETFLAG) != 0;
		eFlags |= DESIGNTIME_PROPERTY_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__DESIGNTIME_PROPERTY, oldDesigntimeProperty, newDesigntimeProperty, !oldDesigntimePropertyESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetDesigntimeProperty() {
		boolean oldDesigntimeProperty = (eFlags & DESIGNTIME_PROPERTY_EFLAG) != 0;
		boolean oldDesigntimePropertyESet = (eFlags & DESIGNTIME_PROPERTY_ESETFLAG) != 0;
		if (DESIGNTIME_PROPERTY_EDEFAULT) eFlags |= DESIGNTIME_PROPERTY_EFLAG; else eFlags &= ~DESIGNTIME_PROPERTY_EFLAG;
		eFlags &= ~DESIGNTIME_PROPERTY_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__DESIGNTIME_PROPERTY, oldDesigntimeProperty, DESIGNTIME_PROPERTY_EDEFAULT, oldDesigntimePropertyESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetDesigntimeProperty() {
		return (eFlags & DESIGNTIME_PROPERTY_ESETFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isAlwaysIncompatible() {
		return (eFlags & ALWAYS_INCOMPATIBLE_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAlwaysIncompatible(boolean newAlwaysIncompatible) {
		boolean oldAlwaysIncompatible = (eFlags & ALWAYS_INCOMPATIBLE_EFLAG) != 0;
		if (newAlwaysIncompatible) eFlags |= ALWAYS_INCOMPATIBLE_EFLAG; else eFlags &= ~ALWAYS_INCOMPATIBLE_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__ALWAYS_INCOMPATIBLE, oldAlwaysIncompatible, newAlwaysIncompatible));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__CELL_EDITOR_VALIDATOR_CLASSNAMES:
				return getCellEditorValidatorClassnames();
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__LABEL_PROVIDER_CLASSNAME:
				return getLabelProviderClassname();
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__CELL_EDITOR_CLASSNAME:
				return getCellEditorClassname();
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__NULL_INVALID:
				return isNullInvalid() ? Boolean.TRUE : Boolean.FALSE;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__ENTRY_EXPANDABLE:
				return isEntryExpandable() ? Boolean.TRUE : Boolean.FALSE;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__DESIGNTIME_PROPERTY:
				return isDesigntimeProperty() ? Boolean.TRUE : Boolean.FALSE;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__ALWAYS_INCOMPATIBLE:
				return isAlwaysIncompatible() ? Boolean.TRUE : Boolean.FALSE;
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__CELL_EDITOR_VALIDATOR_CLASSNAMES:
				getCellEditorValidatorClassnames().clear();
				getCellEditorValidatorClassnames().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__LABEL_PROVIDER_CLASSNAME:
				setLabelProviderClassname((String)newValue);
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__CELL_EDITOR_CLASSNAME:
				setCellEditorClassname((String)newValue);
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__NULL_INVALID:
				setNullInvalid(((Boolean)newValue).booleanValue());
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__ENTRY_EXPANDABLE:
				setEntryExpandable(((Boolean)newValue).booleanValue());
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__DESIGNTIME_PROPERTY:
				setDesigntimeProperty(((Boolean)newValue).booleanValue());
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__ALWAYS_INCOMPATIBLE:
				setAlwaysIncompatible(((Boolean)newValue).booleanValue());
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__CELL_EDITOR_VALIDATOR_CLASSNAMES:
				getCellEditorValidatorClassnames().clear();
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__LABEL_PROVIDER_CLASSNAME:
				unsetLabelProviderClassname();
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__CELL_EDITOR_CLASSNAME:
				unsetCellEditorClassname();
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__NULL_INVALID:
				unsetNullInvalid();
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__ENTRY_EXPANDABLE:
				unsetEntryExpandable();
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__DESIGNTIME_PROPERTY:
				unsetDesigntimeProperty();
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__ALWAYS_INCOMPATIBLE:
				setAlwaysIncompatible(ALWAYS_INCOMPATIBLE_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean eIsSetGen(int featureID) {
		switch (featureID) {
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__CELL_EDITOR_VALIDATOR_CLASSNAMES:
				return cellEditorValidatorClassnames != null && !cellEditorValidatorClassnames.isEmpty();
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__LABEL_PROVIDER_CLASSNAME:
				return isSetLabelProviderClassname();
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__CELL_EDITOR_CLASSNAME:
				return isSetCellEditorClassname();
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__NULL_INVALID:
				return isSetNullInvalid();
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__ENTRY_EXPANDABLE:
				return isSetEntryExpandable();
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__DESIGNTIME_PROPERTY:
				return isSetDesigntimeProperty();
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__ALWAYS_INCOMPATIBLE:
				return ((eFlags & ALWAYS_INCOMPATIBLE_EFLAG) != 0) != ALWAYS_INCOMPATIBLE_EDEFAULT;
		}
		return super.eIsSet(featureID);
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
	public boolean eIsSet(int featureId) {
		switch (featureId) {
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__SOURCE:
				return source != null && !eClass().getInstanceClassName().equals(source);
			default:
				return eIsSetGen(featureId);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class baseClass) {
		if (baseClass == BasePropertyDecorator.class) {
			switch (derivedFeatureID) {
				case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__CELL_EDITOR_VALIDATOR_CLASSNAMES: return DecoratorsPackage.BASE_PROPERTY_DECORATOR__CELL_EDITOR_VALIDATOR_CLASSNAMES;
				case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__LABEL_PROVIDER_CLASSNAME: return DecoratorsPackage.BASE_PROPERTY_DECORATOR__LABEL_PROVIDER_CLASSNAME;
				case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__CELL_EDITOR_CLASSNAME: return DecoratorsPackage.BASE_PROPERTY_DECORATOR__CELL_EDITOR_CLASSNAME;
				case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__NULL_INVALID: return DecoratorsPackage.BASE_PROPERTY_DECORATOR__NULL_INVALID;
				case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__ENTRY_EXPANDABLE: return DecoratorsPackage.BASE_PROPERTY_DECORATOR__ENTRY_EXPANDABLE;
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
		if (baseClass == BasePropertyDecorator.class) {
			switch (baseFeatureID) {
				case DecoratorsPackage.BASE_PROPERTY_DECORATOR__CELL_EDITOR_VALIDATOR_CLASSNAMES: return DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__CELL_EDITOR_VALIDATOR_CLASSNAMES;
				case DecoratorsPackage.BASE_PROPERTY_DECORATOR__LABEL_PROVIDER_CLASSNAME: return DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__LABEL_PROVIDER_CLASSNAME;
				case DecoratorsPackage.BASE_PROPERTY_DECORATOR__CELL_EDITOR_CLASSNAME: return DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__CELL_EDITOR_CLASSNAME;
				case DecoratorsPackage.BASE_PROPERTY_DECORATOR__NULL_INVALID: return DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__NULL_INVALID;
				case DecoratorsPackage.BASE_PROPERTY_DECORATOR__ENTRY_EXPANDABLE: return DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__ENTRY_EXPANDABLE;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getCellEditorValidatorClassnames() {
		if (cellEditorValidatorClassnames == null) {
			cellEditorValidatorClassnames = new EDataTypeUniqueEList(String.class, this, DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__CELL_EDITOR_VALIDATOR_CLASSNAMES);
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
		boolean oldLabelProviderClassnameESet = (eFlags & LABEL_PROVIDER_CLASSNAME_ESETFLAG) != 0;
		eFlags |= LABEL_PROVIDER_CLASSNAME_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__LABEL_PROVIDER_CLASSNAME, oldLabelProviderClassname, labelProviderClassname, !oldLabelProviderClassnameESet));
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
		boolean oldLabelProviderClassnameESet = (eFlags & LABEL_PROVIDER_CLASSNAME_ESETFLAG) != 0;
		labelProviderClassname = LABEL_PROVIDER_CLASSNAME_EDEFAULT;
		eFlags &= ~LABEL_PROVIDER_CLASSNAME_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__LABEL_PROVIDER_CLASSNAME, oldLabelProviderClassname, LABEL_PROVIDER_CLASSNAME_EDEFAULT, oldLabelProviderClassnameESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetLabelProviderClassname() {
		return (eFlags & LABEL_PROVIDER_CLASSNAME_ESETFLAG) != 0;
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
		boolean oldCellEditorClassnameESet = (eFlags & CELL_EDITOR_CLASSNAME_ESETFLAG) != 0;
		eFlags |= CELL_EDITOR_CLASSNAME_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__CELL_EDITOR_CLASSNAME, oldCellEditorClassname, cellEditorClassname, !oldCellEditorClassnameESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetCellEditorClassname() {
		String oldCellEditorClassname = cellEditorClassname;
		boolean oldCellEditorClassnameESet = (eFlags & CELL_EDITOR_CLASSNAME_ESETFLAG) != 0;
		cellEditorClassname = CELL_EDITOR_CLASSNAME_EDEFAULT;
		eFlags &= ~CELL_EDITOR_CLASSNAME_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__CELL_EDITOR_CLASSNAME, oldCellEditorClassname, CELL_EDITOR_CLASSNAME_EDEFAULT, oldCellEditorClassnameESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetCellEditorClassname() {
		return (eFlags & CELL_EDITOR_CLASSNAME_ESETFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isNullInvalid() {
		return (eFlags & NULL_INVALID_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNullInvalid(boolean newNullInvalid) {
		boolean oldNullInvalid = (eFlags & NULL_INVALID_EFLAG) != 0;
		if (newNullInvalid) eFlags |= NULL_INVALID_EFLAG; else eFlags &= ~NULL_INVALID_EFLAG;
		boolean oldNullInvalidESet = (eFlags & NULL_INVALID_ESETFLAG) != 0;
		eFlags |= NULL_INVALID_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__NULL_INVALID, oldNullInvalid, newNullInvalid, !oldNullInvalidESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetNullInvalid() {
		boolean oldNullInvalid = (eFlags & NULL_INVALID_EFLAG) != 0;
		boolean oldNullInvalidESet = (eFlags & NULL_INVALID_ESETFLAG) != 0;
		if (NULL_INVALID_EDEFAULT) eFlags |= NULL_INVALID_EFLAG; else eFlags &= ~NULL_INVALID_EFLAG;
		eFlags &= ~NULL_INVALID_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__NULL_INVALID, oldNullInvalid, NULL_INVALID_EDEFAULT, oldNullInvalidESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetNullInvalid() {
		return (eFlags & NULL_INVALID_ESETFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isEntryExpandable() {
		return (eFlags & ENTRY_EXPANDABLE_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEntryExpandable(boolean newEntryExpandable) {
		boolean oldEntryExpandable = (eFlags & ENTRY_EXPANDABLE_EFLAG) != 0;
		if (newEntryExpandable) eFlags |= ENTRY_EXPANDABLE_EFLAG; else eFlags &= ~ENTRY_EXPANDABLE_EFLAG;
		boolean oldEntryExpandableESet = (eFlags & ENTRY_EXPANDABLE_ESETFLAG) != 0;
		eFlags |= ENTRY_EXPANDABLE_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__ENTRY_EXPANDABLE, oldEntryExpandable, newEntryExpandable, !oldEntryExpandableESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetEntryExpandable() {
		boolean oldEntryExpandable = (eFlags & ENTRY_EXPANDABLE_EFLAG) != 0;
		boolean oldEntryExpandableESet = (eFlags & ENTRY_EXPANDABLE_ESETFLAG) != 0;
		if (ENTRY_EXPANDABLE_EDEFAULT) eFlags |= ENTRY_EXPANDABLE_EFLAG; else eFlags &= ~ENTRY_EXPANDABLE_EFLAG;
		eFlags &= ~ENTRY_EXPANDABLE_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR__ENTRY_EXPANDABLE, oldEntryExpandable, ENTRY_EXPANDABLE_EDEFAULT, oldEntryExpandableESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetEntryExpandable() {
		return (eFlags & ENTRY_EXPANDABLE_ESETFLAG) != 0;
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
		if ((eFlags & LABEL_PROVIDER_CLASSNAME_ESETFLAG) != 0) result.append(labelProviderClassname); else result.append("<unset>");
		result.append(", cellEditorClassname: ");
		if ((eFlags & CELL_EDITOR_CLASSNAME_ESETFLAG) != 0) result.append(cellEditorClassname); else result.append("<unset>");
		result.append(", nullInvalid: ");
		if ((eFlags & NULL_INVALID_ESETFLAG) != 0) result.append((eFlags & NULL_INVALID_EFLAG) != 0); else result.append("<unset>");
		result.append(", entryExpandable: ");
		if ((eFlags & ENTRY_EXPANDABLE_ESETFLAG) != 0) result.append((eFlags & ENTRY_EXPANDABLE_EFLAG) != 0); else result.append("<unset>");
		result.append(", designtimeProperty: ");
		if ((eFlags & DESIGNTIME_PROPERTY_ESETFLAG) != 0) result.append((eFlags & DESIGNTIME_PROPERTY_EFLAG) != 0); else result.append("<unset>");
		result.append(", alwaysIncompatible: ");
		result.append((eFlags & ALWAYS_INCOMPATIBLE_EFLAG) != 0);
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
			String msg = MessageFormat.format(CDEMessages.Object_noinstantiate_EXC_, new Object[] { labelProviderClass }); 
			CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, msg, exc));			
			return null;
		}
	}

}

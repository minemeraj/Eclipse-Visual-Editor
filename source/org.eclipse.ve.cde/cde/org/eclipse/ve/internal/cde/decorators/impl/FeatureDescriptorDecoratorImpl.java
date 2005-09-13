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
/*
 *  $RCSfile: FeatureDescriptorDecoratorImpl.java,v $
 *  $Revision: 1.8 $  $Date: 2005-09-13 20:30:53 $ 
 */
import java.util.Collection;
import java.util.Iterator;

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
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ve.internal.cde.decorators.DecoratorsPackage;
import org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator;
import org.eclipse.ve.internal.cde.utility.AbstractString;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Feature Descriptor Decorator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.FeatureDescriptorDecoratorImpl#isHidden <em>Hidden</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.FeatureDescriptorDecoratorImpl#getHelpContextIdsString <em>Help Context Ids String</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.FeatureDescriptorDecoratorImpl#isPreferred <em>Preferred</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.FeatureDescriptorDecoratorImpl#getCategoryString <em>Category String</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.FeatureDescriptorDecoratorImpl#getFilterFlagStrings <em>Filter Flag Strings</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.FeatureDescriptorDecoratorImpl#getDisplayNameString <em>Display Name String</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.FeatureDescriptorDecoratorImpl#getDescriptionString <em>Description String</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class FeatureDescriptorDecoratorImpl extends EAnnotationImpl implements FeatureDescriptorDecorator {

	/**
	 * The default value of the '{@link #isHidden() <em>Hidden</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isHidden()
	 * @generated
	 * @ordered
	 */
	protected static final boolean HIDDEN_EDEFAULT = false;

	
	/**
	 * The cached value of the '{@link #isHidden() <em>Hidden</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isHidden()
	 * @generated
	 * @ordered
	 */
	protected boolean hidden = HIDDEN_EDEFAULT;
	/**
	 * The cached value of the '{@link #getHelpContextIdsString() <em>Help Context Ids String</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHelpContextIdsString()
	 * @generated
	 * @ordered
	 */
	protected EList helpContextIdsString = null;
	/**
	 * The default value of the '{@link #isPreferred() <em>Preferred</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPreferred()
	 * @generated
	 * @ordered
	 */
	protected static final boolean PREFERRED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isPreferred() <em>Preferred</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPreferred()
	 * @generated
	 * @ordered
	 */
	protected boolean preferred = PREFERRED_EDEFAULT;
	/**
	 * The cached value of the '{@link #getCategoryString() <em>Category String</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCategoryString()
	 * @generated
	 * @ordered
	 */
	protected AbstractString categoryString = null;
	/**
	 * The cached value of the '{@link #getFilterFlagStrings() <em>Filter Flag Strings</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFilterFlagStrings()
	 * @generated
	 * @ordered
	 */
	protected EList filterFlagStrings = null;
	/**
	 * The cached value of the '{@link #getDisplayNameString() <em>Display Name String</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDisplayNameString()
	 * @generated
	 * @ordered
	 */
	protected AbstractString displayNameString = null;
	/**
	 * The cached value of the '{@link #getDescriptionString() <em>Description String</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescriptionString()
	 * @generated
	 * @ordered
	 */
	protected AbstractString descriptionString = null;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */	
	protected FeatureDescriptorDecoratorImpl() {
		super();
		setSource(getSourceDefault());		
	}
	
	/**
	 * Subclasses should override and return the default source.
	 * @return
	 * 
	 * @since 1.2.0
	 */
	protected String getSourceDefault() {
		return FeatureDescriptorDecorator.class.getName();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return DecoratorsPackage.eINSTANCE.getFeatureDescriptorDecorator();
	}

	/**
	 * Answer whether this flag is within the filter flags.
	 */
	public boolean isFiltered(String flag) {
		Iterator itr = getFilterFlagStrings().iterator();
		while (itr.hasNext()) {
			if (((AbstractString) itr.next()).equals(flag))
				return true;
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractString getDisplayNameString() {
		return displayNameString;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDisplayNameString(AbstractString newDisplayNameString, NotificationChain msgs) {
		AbstractString oldDisplayNameString = displayNameString;
		displayNameString = newDisplayNameString;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING, oldDisplayNameString, newDisplayNameString);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDisplayNameString(AbstractString newDisplayNameString) {
		if (newDisplayNameString != displayNameString) {
			NotificationChain msgs = null;
			if (displayNameString != null)
				msgs = ((InternalEObject)displayNameString).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING, null, msgs);
			if (newDisplayNameString != null)
				msgs = ((InternalEObject)newDisplayNameString).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING, null, msgs);
			msgs = basicSetDisplayNameString(newDisplayNameString, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING, newDisplayNameString, newDisplayNameString));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractString getDescriptionString() {
		return descriptionString;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDescriptionString(AbstractString newDescriptionString, NotificationChain msgs) {
		AbstractString oldDescriptionString = descriptionString;
		descriptionString = newDescriptionString;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING, oldDescriptionString, newDescriptionString);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDescriptionString(AbstractString newDescriptionString) {
		if (newDescriptionString != descriptionString) {
			NotificationChain msgs = null;
			if (descriptionString != null)
				msgs = ((InternalEObject)descriptionString).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING, null, msgs);
			if (newDescriptionString != null)
				msgs = ((InternalEObject)newDescriptionString).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING, null, msgs);
			msgs = basicSetDescriptionString(newDescriptionString, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING, newDescriptionString, newDescriptionString));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHidden(boolean newHidden) {
		boolean oldHidden = hidden;
		hidden = newHidden;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__HIDDEN, oldHidden, hidden));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getHelpContextIdsString() {
		if (helpContextIdsString == null) {
			helpContextIdsString = new EDataTypeUniqueEList(String.class, this, DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__HELP_CONTEXT_IDS_STRING);
		}
		return helpContextIdsString;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isPreferred() {
		return preferred;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPreferred(boolean newPreferred) {
		boolean oldPreferred = preferred;
		preferred = newPreferred;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__PREFERRED, oldPreferred, preferred));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractString getCategoryString() {
		if (categoryString != null && categoryString.eIsProxy()) {
			AbstractString oldCategoryString = categoryString;
			categoryString = (AbstractString)eResolveProxy((InternalEObject)categoryString);
			if (categoryString != oldCategoryString) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__CATEGORY_STRING, oldCategoryString, categoryString));
			}
		}
		return categoryString;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractString basicGetCategoryString() {
		return categoryString;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCategoryString(AbstractString newCategoryString) {
		AbstractString oldCategoryString = categoryString;
		categoryString = newCategoryString;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__CATEGORY_STRING, oldCategoryString, categoryString));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getFilterFlagStrings() {
		if (filterFlagStrings == null) {
			filterFlagStrings = new EObjectResolvingEList(AbstractString.class, this, DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__FILTER_FLAG_STRINGS);
		}
		return filterFlagStrings;
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
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__SOURCE:
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
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__EANNOTATIONS:
				return eAnnotations != null && !eAnnotations.isEmpty();
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__SOURCE:
				return SOURCE_EDEFAULT == null ? source != null : !SOURCE_EDEFAULT.equals(source);
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DETAILS:
				return details != null && !details.isEmpty();
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT:
				return getEModelElement() != null;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__CONTENTS:
				return contents != null && !contents.isEmpty();
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__REFERENCES:
				return references != null && !references.isEmpty();
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__HIDDEN:
				return hidden != HIDDEN_EDEFAULT;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__HELP_CONTEXT_IDS_STRING:
				return helpContextIdsString != null && !helpContextIdsString.isEmpty();
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__PREFERRED:
				return preferred != PREFERRED_EDEFAULT;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__CATEGORY_STRING:
				return categoryString != null;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__FILTER_FLAG_STRINGS:
				return filterFlagStrings != null && !filterFlagStrings.isEmpty();
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING:
				return displayNameString != null;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING:
				return descriptionString != null;
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
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__EANNOTATIONS:
				getEAnnotations().clear();
				getEAnnotations().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__SOURCE:
				setSource((String)newValue);
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DETAILS:
				getDetails().clear();
				getDetails().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT:
				setEModelElement((EModelElement)newValue);
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__CONTENTS:
				getContents().clear();
				getContents().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__REFERENCES:
				getReferences().clear();
				getReferences().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__HIDDEN:
				setHidden(((Boolean)newValue).booleanValue());
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__HELP_CONTEXT_IDS_STRING:
				getHelpContextIdsString().clear();
				getHelpContextIdsString().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__PREFERRED:
				setPreferred(((Boolean)newValue).booleanValue());
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__CATEGORY_STRING:
				setCategoryString((AbstractString)newValue);
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__FILTER_FLAG_STRINGS:
				getFilterFlagStrings().clear();
				getFilterFlagStrings().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING:
				setDisplayNameString((AbstractString)newValue);
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING:
				setDescriptionString((AbstractString)newValue);
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
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__EANNOTATIONS:
				getEAnnotations().clear();
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__SOURCE:
				setSource(SOURCE_EDEFAULT);
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DETAILS:
				getDetails().clear();
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT:
				setEModelElement((EModelElement)null);
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__CONTENTS:
				getContents().clear();
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__REFERENCES:
				getReferences().clear();
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__HIDDEN:
				setHidden(HIDDEN_EDEFAULT);
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__HELP_CONTEXT_IDS_STRING:
				getHelpContextIdsString().clear();
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__PREFERRED:
				setPreferred(PREFERRED_EDEFAULT);
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__CATEGORY_STRING:
				setCategoryString((AbstractString)null);
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__FILTER_FLAG_STRINGS:
				getFilterFlagStrings().clear();
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING:
				setDisplayNameString((AbstractString)null);
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING:
				setDescriptionString((AbstractString)null);
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
		result.append(" (hidden: ");
		result.append(hidden);
		result.append(", helpContextIdsString: ");
		result.append(helpContextIdsString);
		result.append(", preferred: ");
		result.append(preferred);
		result.append(')');
		return result.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__EANNOTATIONS:
					return ((InternalEList)getEAnnotations()).basicAdd(otherEnd, msgs);
				case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT, msgs);
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
				case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__EANNOTATIONS:
					return ((InternalEList)getEAnnotations()).basicRemove(otherEnd, msgs);
				case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DETAILS:
					return ((InternalEList)getDetails()).basicRemove(otherEnd, msgs);
				case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT:
					return eBasicSetContainer(null, DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT, msgs);
				case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__CONTENTS:
					return ((InternalEList)getContents()).basicRemove(otherEnd, msgs);
				case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING:
					return basicSetDisplayNameString(null, msgs);
				case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING:
					return basicSetDescriptionString(null, msgs);
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
				case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT:
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
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__EANNOTATIONS:
				return getEAnnotations();
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__SOURCE:
				return getSource();
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DETAILS:
				return getDetails();
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT:
				return getEModelElement();
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__CONTENTS:
				return getContents();
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__REFERENCES:
				return getReferences();
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__HIDDEN:
				return isHidden() ? Boolean.TRUE : Boolean.FALSE;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__HELP_CONTEXT_IDS_STRING:
				return getHelpContextIdsString();
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__PREFERRED:
				return isPreferred() ? Boolean.TRUE : Boolean.FALSE;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__CATEGORY_STRING:
				if (resolve) return getCategoryString();
				return basicGetCategoryString();
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__FILTER_FLAG_STRINGS:
				return getFilterFlagStrings();
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING:
				return getDisplayNameString();
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING:
				return getDescriptionString();
		}
		return eDynamicGet(eFeature, resolve);
	}

}

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
package org.eclipse.ve.internal.cde.decorators.impl;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EAnnotationImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

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
	 * The flag representing the value of the '{@link #isHidden() <em>Hidden</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isHidden()
	 * @generated
	 * @ordered
	 */
	protected static final int HIDDEN_EFLAG = 1 << 8;

	/**
	 * The cached value of the '{@link #getHelpContextIdsString() <em>Help Context Ids String</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHelpContextIdsString()
	 * @generated
	 * @ordered
	 */
	protected EList<String> helpContextIdsString;
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
	 * The flag representing the value of the '{@link #isPreferred() <em>Preferred</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPreferred()
	 * @generated
	 * @ordered
	 */
	protected static final int PREFERRED_EFLAG = 1 << 9;

	/**
	 * The cached value of the '{@link #getCategoryString() <em>Category String</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCategoryString()
	 * @generated
	 * @ordered
	 */
	protected AbstractString categoryString;
	/**
	 * The cached value of the '{@link #getFilterFlagStrings() <em>Filter Flag Strings</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFilterFlagStrings()
	 * @generated
	 * @ordered
	 */
	protected EList<AbstractString> filterFlagStrings;
	/**
	 * The cached value of the '{@link #getDisplayNameString() <em>Display Name String</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDisplayNameString()
	 * @generated
	 * @ordered
	 */
	protected AbstractString displayNameString;
	/**
	 * The cached value of the '{@link #getDescriptionString() <em>Description String</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescriptionString()
	 * @generated
	 * @ordered
	 */
	protected AbstractString descriptionString;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected FeatureDescriptorDecoratorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DecoratorsPackage.Literals.FEATURE_DESCRIPTOR_DECORATOR;
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
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING:
				return basicSetDisplayNameString(null, msgs);
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING:
				return basicSetDescriptionString(null, msgs);
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
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
		@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__HIDDEN:
				setHidden(((Boolean)newValue).booleanValue());
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__HELP_CONTEXT_IDS_STRING:
				getHelpContextIdsString().clear();
				getHelpContextIdsString().addAll((Collection<? extends String>)newValue);
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__PREFERRED:
				setPreferred(((Boolean)newValue).booleanValue());
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__CATEGORY_STRING:
				setCategoryString((AbstractString)newValue);
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__FILTER_FLAG_STRINGS:
				getFilterFlagStrings().clear();
				getFilterFlagStrings().addAll((Collection<? extends AbstractString>)newValue);
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING:
				setDisplayNameString((AbstractString)newValue);
				return;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING:
				setDescriptionString((AbstractString)newValue);
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
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean eIsSetGen(int featureID) {
		switch (featureID) {
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__HIDDEN:
				return ((eFlags & HIDDEN_EFLAG) != 0) != HIDDEN_EDEFAULT;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__HELP_CONTEXT_IDS_STRING:
				return helpContextIdsString != null && !helpContextIdsString.isEmpty();
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__PREFERRED:
				return ((eFlags & PREFERRED_EFLAG) != 0) != PREFERRED_EDEFAULT;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__CATEGORY_STRING:
				return categoryString != null;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__FILTER_FLAG_STRINGS:
				return filterFlagStrings != null && !filterFlagStrings.isEmpty();
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING:
				return displayNameString != null;
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING:
				return descriptionString != null;
		}
		return super.eIsSet(featureID);
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
		return (eFlags & HIDDEN_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHidden(boolean newHidden) {
		boolean oldHidden = (eFlags & HIDDEN_EFLAG) != 0;
		if (newHidden) eFlags |= HIDDEN_EFLAG; else eFlags &= ~HIDDEN_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__HIDDEN, oldHidden, newHidden));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getHelpContextIdsString() {
		if (helpContextIdsString == null) {
			helpContextIdsString = new EDataTypeUniqueEList<String>(String.class, this, DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__HELP_CONTEXT_IDS_STRING);
		}
		return helpContextIdsString;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isPreferred() {
		return (eFlags & PREFERRED_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPreferred(boolean newPreferred) {
		boolean oldPreferred = (eFlags & PREFERRED_EFLAG) != 0;
		if (newPreferred) eFlags |= PREFERRED_EFLAG; else eFlags &= ~PREFERRED_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__PREFERRED, oldPreferred, newPreferred));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractString getCategoryString() {
		if (categoryString != null && categoryString.eIsProxy()) {
			InternalEObject oldCategoryString = (InternalEObject)categoryString;
			categoryString = (AbstractString)eResolveProxy(oldCategoryString);
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
	public EList<AbstractString> getFilterFlagStrings() {
		if (filterFlagStrings == null) {
			filterFlagStrings = new EObjectResolvingEList<AbstractString>(AbstractString.class, this, DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__FILTER_FLAG_STRINGS);
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
	public boolean eIsSet(int featureId) {
		// Must override eIsSet and not eIsSetGen because of an infinite loop due to a subclass (ClassDescriptorDecorator for instance)
		// If overrode eIsSet(int) then when this is called on the subclass we get the following loop:
		// cd.eIsSet(int)
		//   cd.eIsSetGen(int)
		//     super.eIsSet(int) which is fd.eIsSet(int)
		//       eIsSetGen(int) in fd, but this is actually cd.isSetGen(int) because of override. And now we have our loop.
		switch (featureId) {
			case DecoratorsPackage.FEATURE_DESCRIPTOR_DECORATOR__SOURCE:
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
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (hidden: ");
		result.append((eFlags & HIDDEN_EFLAG) != 0);
		result.append(", helpContextIdsString: ");
		result.append(helpContextIdsString);
		result.append(", preferred: ");
		result.append((eFlags & PREFERRED_EFLAG) != 0);
		result.append(')');
		return result.toString();
	}

}

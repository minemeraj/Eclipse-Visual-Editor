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
package org.eclipse.ve.internal.cdm.impl;
/*
 *  $RCSfile: AnnotationGenericImpl.java,v $
 *  $Revision: 1.7 $  $Date: 2007-05-25 04:09:35 $ 
 */

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.ve.internal.cdm.AnnotationGeneric;
import org.eclipse.ve.internal.cdm.CDMPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Annotation Generic</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cdm.impl.AnnotationGenericImpl#getAnnotatesID <em>Annotates ID</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AnnotationGenericImpl extends AnnotationImpl implements AnnotationGeneric {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "";


	/**
	 * The default value of the '{@link #getAnnotatesID() <em>Annotates ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAnnotatesID()
	 * @generated
	 * @ordered
	 */
	protected static final String ANNOTATES_ID_EDEFAULT = null;

	
	/**
	 * The cached value of the '{@link #getAnnotatesID() <em>Annotates ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAnnotatesID()
	 * @generated
	 * @ordered
	 */
	protected String annotatesID = ANNOTATES_ID_EDEFAULT;
	
	/**
	 * The flag representing whether the Annotates ID attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected static final int ANNOTATES_ID_ESETFLAG = 1 << 8;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected AnnotationGenericImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CDMPackage.Literals.ANNOTATION_GENERIC;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAnnotatesID() {
		return annotatesID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAnnotatesID(String newAnnotatesID) {
		String oldAnnotatesID = annotatesID;
		annotatesID = newAnnotatesID;
		boolean oldAnnotatesIDESet = (eFlags & ANNOTATES_ID_ESETFLAG) != 0;
		eFlags |= ANNOTATES_ID_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CDMPackage.ANNOTATION_GENERIC__ANNOTATES_ID, oldAnnotatesID, annotatesID, !oldAnnotatesIDESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetAnnotatesID() {
		String oldAnnotatesID = annotatesID;
		boolean oldAnnotatesIDESet = (eFlags & ANNOTATES_ID_ESETFLAG) != 0;
		annotatesID = ANNOTATES_ID_EDEFAULT;
		eFlags &= ~ANNOTATES_ID_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CDMPackage.ANNOTATION_GENERIC__ANNOTATES_ID, oldAnnotatesID, ANNOTATES_ID_EDEFAULT, oldAnnotatesIDESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetAnnotatesID() {
		return (eFlags & ANNOTATES_ID_ESETFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CDMPackage.ANNOTATION_GENERIC__ANNOTATES_ID:
				return getAnnotatesID();
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
			case CDMPackage.ANNOTATION_GENERIC__ANNOTATES_ID:
				setAnnotatesID((String)newValue);
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
			case CDMPackage.ANNOTATION_GENERIC__ANNOTATES_ID:
				unsetAnnotatesID();
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
			case CDMPackage.ANNOTATION_GENERIC__ANNOTATES_ID:
				return isSetAnnotatesID();
		}
		return super.eIsSet(featureID);
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
		result.append(" (annotatesID: ");
		if ((eFlags & ANNOTATES_ID_ESETFLAG) != 0) result.append(annotatesID); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

}

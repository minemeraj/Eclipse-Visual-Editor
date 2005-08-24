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
package org.eclipse.ve.internal.cdm.impl;
/*
 *  $RCSfile: DiagramImpl.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:12:49 $ 
 */
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.Diagram;
import org.eclipse.ve.internal.cdm.DiagramData;
import org.eclipse.ve.internal.cdm.DiagramFigure;
import org.eclipse.ve.internal.cdm.VisualInfo;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cdm.impl.DiagramImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cdm.impl.DiagramImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cdm.impl.DiagramImpl#getDiagramData <em>Diagram Data</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cdm.impl.DiagramImpl#getVisualInfos <em>Visual Infos</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cdm.impl.DiagramImpl#getFigures <em>Figures</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DiagramImpl extends KeyedValueHolderImpl implements Diagram {

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = " "; //$NON-NLS-1$

	
	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;
	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getVisualInfos() <em>Visual Infos</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVisualInfos()
	 * @generated
	 * @ordered
	 */
	protected EList visualInfos = null;
	/**
	 * The cached value of the '{@link #getFigures() <em>Figures</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFigures()
	 * @generated
	 * @ordered
	 */
	protected EList figures = null;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected DiagramImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return CDMPackage.eINSTANCE.getDiagram();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CDMPackage.DIAGRAM__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CDMPackage.DIAGRAM__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramData getDiagramData() {
		if (eContainerFeatureID != CDMPackage.DIAGRAM__DIAGRAM_DATA) return null;
		return (DiagramData)eContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDiagramData(DiagramData newDiagramData) {
		if (newDiagramData != eContainer || (eContainerFeatureID != CDMPackage.DIAGRAM__DIAGRAM_DATA && newDiagramData != null)) {
			if (EcoreUtil.isAncestor(this, newDiagramData))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eContainer != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newDiagramData != null)
				msgs = ((InternalEObject)newDiagramData).eInverseAdd(this, CDMPackage.DIAGRAM_DATA__DIAGRAMS, DiagramData.class, msgs);
			msgs = eBasicSetContainer((InternalEObject)newDiagramData, CDMPackage.DIAGRAM__DIAGRAM_DATA, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CDMPackage.DIAGRAM__DIAGRAM_DATA, newDiagramData, newDiagramData));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getVisualInfos() {
		if (visualInfos == null) {
			visualInfos = new EObjectWithInverseResolvingEList(VisualInfo.class, this, CDMPackage.DIAGRAM__VISUAL_INFOS, CDMPackage.VISUAL_INFO__DIAGRAM);
		}
		return visualInfos;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getFigures() {
		if (figures == null) {
			figures = new EObjectContainmentEList(DiagramFigure.class, this, CDMPackage.DIAGRAM__FIGURES);
		}
		return figures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case CDMPackage.DIAGRAM__DIAGRAM_DATA:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, CDMPackage.DIAGRAM__DIAGRAM_DATA, msgs);
				case CDMPackage.DIAGRAM__VISUAL_INFOS:
					return ((InternalEList)getVisualInfos()).basicAdd(otherEnd, msgs);
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
				case CDMPackage.DIAGRAM__KEYED_VALUES:
					return ((InternalEList)getKeyedValues()).basicRemove(otherEnd, msgs);
				case CDMPackage.DIAGRAM__DIAGRAM_DATA:
					return eBasicSetContainer(null, CDMPackage.DIAGRAM__DIAGRAM_DATA, msgs);
				case CDMPackage.DIAGRAM__VISUAL_INFOS:
					return ((InternalEList)getVisualInfos()).basicRemove(otherEnd, msgs);
				case CDMPackage.DIAGRAM__FIGURES:
					return ((InternalEList)getFigures()).basicRemove(otherEnd, msgs);
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
				case CDMPackage.DIAGRAM__DIAGRAM_DATA:
					return eContainer.eInverseRemove(this, CDMPackage.DIAGRAM_DATA__DIAGRAMS, DiagramData.class, msgs);
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
			case CDMPackage.DIAGRAM__KEYED_VALUES:
				return getKeyedValues();
			case CDMPackage.DIAGRAM__NAME:
				return getName();
			case CDMPackage.DIAGRAM__ID:
				return getId();
			case CDMPackage.DIAGRAM__DIAGRAM_DATA:
				return getDiagramData();
			case CDMPackage.DIAGRAM__VISUAL_INFOS:
				return getVisualInfos();
			case CDMPackage.DIAGRAM__FIGURES:
				return getFigures();
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case CDMPackage.DIAGRAM__KEYED_VALUES:
				return keyedValues != null && !keyedValues.isEmpty();
			case CDMPackage.DIAGRAM__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case CDMPackage.DIAGRAM__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case CDMPackage.DIAGRAM__DIAGRAM_DATA:
				return getDiagramData() != null;
			case CDMPackage.DIAGRAM__VISUAL_INFOS:
				return visualInfos != null && !visualInfos.isEmpty();
			case CDMPackage.DIAGRAM__FIGURES:
				return figures != null && !figures.isEmpty();
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
			case CDMPackage.DIAGRAM__KEYED_VALUES:
				getKeyedValues().clear();
				getKeyedValues().addAll((Collection)newValue);
				return;
			case CDMPackage.DIAGRAM__NAME:
				setName((String)newValue);
				return;
			case CDMPackage.DIAGRAM__ID:
				setId((String)newValue);
				return;
			case CDMPackage.DIAGRAM__DIAGRAM_DATA:
				setDiagramData((DiagramData)newValue);
				return;
			case CDMPackage.DIAGRAM__VISUAL_INFOS:
				getVisualInfos().clear();
				getVisualInfos().addAll((Collection)newValue);
				return;
			case CDMPackage.DIAGRAM__FIGURES:
				getFigures().clear();
				getFigures().addAll((Collection)newValue);
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
			case CDMPackage.DIAGRAM__KEYED_VALUES:
				getKeyedValues().clear();
				return;
			case CDMPackage.DIAGRAM__NAME:
				setName(NAME_EDEFAULT);
				return;
			case CDMPackage.DIAGRAM__ID:
				setId(ID_EDEFAULT);
				return;
			case CDMPackage.DIAGRAM__DIAGRAM_DATA:
				setDiagramData((DiagramData)null);
				return;
			case CDMPackage.DIAGRAM__VISUAL_INFOS:
				getVisualInfos().clear();
				return;
			case CDMPackage.DIAGRAM__FIGURES:
				getFigures().clear();
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
		result.append(" (name: ");
		result.append(name);
		result.append(", id: ");
		result.append(id);
		result.append(')');
		return result.toString();
	}

}

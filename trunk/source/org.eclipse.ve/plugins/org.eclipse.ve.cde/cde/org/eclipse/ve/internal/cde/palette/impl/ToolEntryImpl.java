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
 *  $RCSfile: ToolEntryImpl.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:12:50 $ 
 */
import java.util.Collection;


import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.Permissions;
import org.eclipse.ve.internal.cde.palette.ToolEntry;
import org.eclipse.ve.internal.cde.utility.AbstractString;
/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tool Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.ToolEntryImpl#getToolClassName <em>Tool Class Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ToolEntryImpl extends AbstractToolEntryImpl implements ToolEntry {

	/**
	 * The default value of the '{@link #getToolClassName() <em>Tool Class Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToolClassName()
	 * @generated
	 * @ordered
	 */
	protected static final String TOOL_CLASS_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getToolClassName() <em>Tool Class Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToolClassName()
	 * @generated
	 * @ordered
	 */
	protected String toolClassName = TOOL_CLASS_NAME_EDEFAULT;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected ToolEntryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getToolEntry();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getToolClassName() {
		return toolClassName;
	}

	public void setToolClassName(String newToolClassName) {
		toolClass = null;
		setToolClassNameGen(newToolClassName);
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setToolClassNameGen(String newToolClassName) {
		String oldToolClassName = toolClassName;
		toolClassName = newToolClassName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.TOOL_ENTRY__TOOL_CLASS_NAME, oldToolClassName, toolClassName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.TOOL_ENTRY__ENTRY_LABEL:
					return basicSetEntryLabel(null, msgs);
				case PalettePackage.TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
					return basicSetEntryShortDescription(null, msgs);
				case PalettePackage.TOOL_ENTRY__STRING_PROPERTIES:
					return ((InternalEList)getStringProperties()).basicRemove(otherEnd, msgs);
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
			case PalettePackage.TOOL_ENTRY__ICON16_NAME:
				return getIcon16Name();
			case PalettePackage.TOOL_ENTRY__ICON32_NAME:
				return getIcon32Name();
			case PalettePackage.TOOL_ENTRY__VISIBLE:
				return isVisible() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.TOOL_ENTRY__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.TOOL_ENTRY__ID:
				return getId();
			case PalettePackage.TOOL_ENTRY__MODIFICATION:
				return getModification();
			case PalettePackage.TOOL_ENTRY__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
			case PalettePackage.TOOL_ENTRY__STRING_PROPERTIES:
				return getStringProperties();
			case PalettePackage.TOOL_ENTRY__TOOL_CLASS_NAME:
				return getToolClassName();
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
			case PalettePackage.TOOL_ENTRY__ICON16_NAME:
				return ICON16_NAME_EDEFAULT == null ? icon16Name != null : !ICON16_NAME_EDEFAULT.equals(icon16Name);
			case PalettePackage.TOOL_ENTRY__ICON32_NAME:
				return ICON32_NAME_EDEFAULT == null ? icon32Name != null : !ICON32_NAME_EDEFAULT.equals(icon32Name);
			case PalettePackage.TOOL_ENTRY__VISIBLE:
				return visible != VISIBLE_EDEFAULT;
			case PalettePackage.TOOL_ENTRY__DEFAULT_ENTRY:
				return isDefaultEntry() != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.TOOL_ENTRY__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case PalettePackage.TOOL_ENTRY__MODIFICATION:
				return modification != MODIFICATION_EDEFAULT;
			case PalettePackage.TOOL_ENTRY__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
			case PalettePackage.TOOL_ENTRY__STRING_PROPERTIES:
				return stringProperties != null && !stringProperties.isEmpty();
			case PalettePackage.TOOL_ENTRY__TOOL_CLASS_NAME:
				return TOOL_CLASS_NAME_EDEFAULT == null ? toolClassName != null : !TOOL_CLASS_NAME_EDEFAULT.equals(toolClassName);
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
			case PalettePackage.TOOL_ENTRY__ICON16_NAME:
				setIcon16Name((String)newValue);
				return;
			case PalettePackage.TOOL_ENTRY__ICON32_NAME:
				setIcon32Name((String)newValue);
				return;
			case PalettePackage.TOOL_ENTRY__VISIBLE:
				setVisible(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.TOOL_ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.TOOL_ENTRY__ID:
				setId((String)newValue);
				return;
			case PalettePackage.TOOL_ENTRY__MODIFICATION:
				setModification((Permissions)newValue);
				return;
			case PalettePackage.TOOL_ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)newValue);
				return;
			case PalettePackage.TOOL_ENTRY__STRING_PROPERTIES:
				getStringProperties().clear();
				getStringProperties().addAll((Collection)newValue);
				return;
			case PalettePackage.TOOL_ENTRY__TOOL_CLASS_NAME:
				setToolClassName((String)newValue);
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
			case PalettePackage.TOOL_ENTRY__ICON16_NAME:
				setIcon16Name(ICON16_NAME_EDEFAULT);
				return;
			case PalettePackage.TOOL_ENTRY__ICON32_NAME:
				setIcon32Name(ICON32_NAME_EDEFAULT);
				return;
			case PalettePackage.TOOL_ENTRY__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
				return;
			case PalettePackage.TOOL_ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.TOOL_ENTRY__ID:
				setId(ID_EDEFAULT);
				return;
			case PalettePackage.TOOL_ENTRY__MODIFICATION:
				setModification(MODIFICATION_EDEFAULT);
				return;
			case PalettePackage.TOOL_ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)null);
				return;
			case PalettePackage.TOOL_ENTRY__STRING_PROPERTIES:
				getStringProperties().clear();
				return;
			case PalettePackage.TOOL_ENTRY__TOOL_CLASS_NAME:
				setToolClassName(TOOL_CLASS_NAME_EDEFAULT);
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
		result.append(" (toolClassName: ");
		result.append(toolClassName);
		result.append(')');
		return result.toString();
	}

	private Class toolClass;
	private Class getToolClass() {
		if (toolClass == null) {
			String toolClassName = getToolClassName();
			if (toolClassName != null) {
				try {
					toolClass = CDEPlugin.getClassFromString(toolClassName);
				} catch (ClassNotFoundException e) {
				}
			}
			if (toolClass == null) {
				toolClass = SelectionTool.class;
			}
		}
		return toolClass;
	}
	
	private static class GenericToolEntry extends org.eclipse.gef.palette.ToolEntry {
		public GenericToolEntry(String label, String description, ImageDescriptor iconSmall, ImageDescriptor iconLarge, Class tool) {
			super(label, description, iconSmall, iconLarge, tool);
		}
	}
	
	protected PaletteEntry createPaletteEntry() {
		// Need to return an inner class because ToolEntry is abstract, even though it says it doesn't need to be.
		return new GenericToolEntry(getLabel(), getDescription(), getSmallIcon(), getLargeIcon(), getToolClass());
	}


}

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
 *  $RCSfile: ToolEntryImpl.java,v $
 *  $Revision: 1.9 $  $Date: 2006-05-17 20:13:52 $ 
 */


import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;


import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.ToolEntry;
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
		return PalettePackage.Literals.TOOL_ENTRY;
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
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PalettePackage.TOOL_ENTRY__TOOL_CLASS_NAME:
				return getToolClassName();
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
			case PalettePackage.TOOL_ENTRY__TOOL_CLASS_NAME:
				setToolClassName((String)newValue);
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
			case PalettePackage.TOOL_ENTRY__TOOL_CLASS_NAME:
				setToolClassName(TOOL_CLASS_NAME_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case PalettePackage.TOOL_ENTRY__TOOL_CLASS_NAME:
				return TOOL_CLASS_NAME_EDEFAULT == null ? toolClassName != null : !TOOL_CLASS_NAME_EDEFAULT.equals(toolClassName);
		}
		return super.eIsSet(featureID);
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

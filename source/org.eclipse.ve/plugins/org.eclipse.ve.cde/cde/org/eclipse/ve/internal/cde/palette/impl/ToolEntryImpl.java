/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $Revision: 1.4 $  $Date: 2005-02-15 23:18:00 $ 
 */
import java.text.MessageFormat;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.gef.Tool;

import org.eclipse.ve.internal.cde.core.CDEMessages;
import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.palette.ICDEToolEntry;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
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

	
	protected Tool fTool;

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

	public Tool getTool() {
		if (fTool != null)
			return fTool;

		if (getToolClassName() == null)
			return null;

		// Now create the tool
		String toolClassName = getToolClassName();
		try {
			return (Tool) CDEPlugin.createInstance(null, toolClassName);
		} catch (InstantiationException e) {
			String msg = MessageFormat.format(CDEMessages.getString("Object.noinstantiate_EXC_"), new Object[] { toolClassName }); //$NON-NLS-1$
			CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, msg, e));
		} catch (ClassCastException e) {
			String msg = MessageFormat.format(CDEMessages.getString("NotInstance_EXC_"), new Object[] { toolClassName, Tool.class }); //$NON-NLS-1$
			CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, msg, e));
		} catch (Exception e) {
			CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, "", e)); //$NON-NLS-1$
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getToolClassName() {
		return toolClassName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setToolClassName(String newToolClassName) {
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
			case PalettePackage.TOOL_ENTRY__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.TOOL_ENTRY__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
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
			case PalettePackage.TOOL_ENTRY__DEFAULT_ENTRY:
				return defaultEntry != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.TOOL_ENTRY__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
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
			case PalettePackage.TOOL_ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.TOOL_ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)newValue);
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
			case PalettePackage.TOOL_ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.TOOL_ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)null);
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

	private class CDEToolEntry extends org.eclipse.gef.palette.ToolEntry implements ICDEToolEntry {
		
		private boolean defaultEntry;
		
		public CDEToolEntry() {
			super(null, null, null, null);
		}

		/**
		 * @see org.eclipse.ve.internal.cde.palette.ICDEToolEntry#isDefaultEntry()
		 */
		public boolean isDefaultEntry() {
			return defaultEntry;
		}

		/**
		 * @see org.eclipse.ve.internal.cde.palette.ICDEToolEntry#setDefaultEntry(boolean)
		 */
		public void setDefaultEntry(boolean defaultEntry) {
			this.defaultEntry = defaultEntry;
		}

		/**
		 * @see org.eclipse.gef.palette.ToolEntry#createTool()
		 */
		public Tool createTool() {
			return getTool();
		}

	}
	/**
	 * @see org.eclipse.ve.internal.cde.palette.impl.EntryImpl#createPaletteEntry()
	 */
	protected ICDEToolEntry createPaletteEntry() {
		return new CDEToolEntry();
	}

}

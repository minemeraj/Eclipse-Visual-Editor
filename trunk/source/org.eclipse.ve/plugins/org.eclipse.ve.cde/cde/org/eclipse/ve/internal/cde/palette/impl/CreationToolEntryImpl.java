package org.eclipse.ve.internal.cde.palette.impl;
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
 *  $RCSfile: CreationToolEntryImpl.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:18:00 $ 
 */
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.gef.Tool;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.CreationTool;

import org.eclipse.ve.internal.cde.emf.EMFCreationTool;
import org.eclipse.ve.internal.cde.palette.CreationToolEntry;
import org.eclipse.ve.internal.cde.palette.ICDEToolEntry;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.utility.AbstractString;
/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Creation Tool Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public abstract class CreationToolEntryImpl extends AbstractToolEntryImpl implements CreationToolEntry {

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
	 * Return the creation factory that will be used by this entry.
	 * Overrides must return something.
	 */
	public CreationFactory createFactory() {
		return null;
	}

	
	protected CreationFactory factory;

	protected final CreationFactory getFactory() {
		return factory != null ? factory : createFactory();
	}

	/**
	 * Set the factory that will be used by this entry in the creation tool. This allows wrappering the factory returned by createFactory() with another one (such as the AnnotationCreationFactory).
	 * 
	 */
	public final void setFactory(CreationFactory factory) {
		this.factory = factory;
	}

	/**
	* Return the tool.
	*/
	public final Tool getTool() {
		return createTool();
	}

	/**
	 * Create a new tool to use for creation.
	 * All createTool() overrides to this
	 * method should create a new tool and should
	 * use getFactory to get the appropriate factory.
	 * This is because the factory may be set and
	 * wrapper the one that normally is returned.
	 */
	protected Tool createTool() {
		CreationTool tool = new EMFCreationTool();
		tool.setFactory(getFactory());
		return tool;
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
			case PalettePackage.CREATION_TOOL_ENTRY__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.CREATION_TOOL_ENTRY__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.CREATION_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
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
			case PalettePackage.CREATION_TOOL_ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)newValue);
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
			case PalettePackage.CREATION_TOOL_ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.CREATION_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)null);
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
			case PalettePackage.CREATION_TOOL_ENTRY__DEFAULT_ENTRY:
				return defaultEntry != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.CREATION_TOOL_ENTRY__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.CREATION_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
		}
		return eDynamicIsSet(eFeature);
	}

	private class CreationEntry extends org.eclipse.gef.palette.CreationToolEntry implements ICDEToolEntry {
		
		private boolean defaultEntry;
		
		public CreationEntry() {
			super(null, null, null, null, null);
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
		return new CreationEntry();
	}

}

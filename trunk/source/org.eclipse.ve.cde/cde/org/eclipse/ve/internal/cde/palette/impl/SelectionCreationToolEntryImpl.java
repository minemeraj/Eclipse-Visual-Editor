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
 *  $RCSfile: SelectionCreationToolEntryImpl.java,v $
 *  $Revision: 1.5 $  $Date: 2005-06-20 23:54:40 $ 
 */
import java.text.MessageFormat;
import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.swt.widgets.Display;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.palette.*;
import org.eclipse.ve.internal.cde.utility.AbstractString;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Selection Creation Tool Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.SelectionCreationToolEntryImpl#getSelectorClassName <em>Selector Class Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public class SelectionCreationToolEntryImpl extends CreationToolEntryImpl implements SelectionCreationToolEntry {

	/**
	 * The default value of the '{@link #getSelectorClassName() <em>Selector Class Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectorClassName()
	 * @generated
	 * @ordered
	 */
	protected static final String SELECTOR_CLASS_NAME_EDEFAULT = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SelectionCreationToolEntryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getSelectionCreationToolEntry();
	}


	protected ISelector getSelector() {
		if (getSelectorClassName() == null)
			return null;

		// Now create the selector
		String selectorClassName = getSelectorClassName();
		try {
			return (ISelector) CDEPlugin.createInstance(null, selectorClassName);
		} catch (InstantiationException e) {
			String msg = MessageFormat.format(CDEMessages.getString("Object.noinstantiate_EXC_"), new Object[] { selectorClassName }); //$NON-NLS-1$
			CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, msg, e));
		} catch (ClassCastException e) {
			String msg =
				MessageFormat.format(CDEMessages.getString("NotInstance_EXC_"), new Object[] { selectorClassName, ISelector.class }); //$NON-NLS-1$
			CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, msg, e));
		} catch (Exception e) {
			CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, "", e)); //$NON-NLS-1$
		}
		return null;
	}

	protected static class SelectionFactory implements CreationFactory {
		
		public Object newObject;
		public Object type;

		public Object getNewObject() {
			return newObject;
		}

		public Object getObjectType() {
			return type;
		}
	}

	/**
	 * The cached value of the '{@link #getSelectorClassName() <em>Selector Class Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectorClassName()
	 * @generated
	 * @ordered
	 */
	protected String selectorClassName = SELECTOR_CLASS_NAME_EDEFAULT;
	
	protected CreationFactory createFactory() {
		return new SelectionFactory();
	}

	/**
	 * The creation tool to use. Different than base one because we need to 
	 * call the selector to do creation.
	 * 
	 * @since 1.1.0
	 */
	protected class SelectionCreationTool extends CDECreationTool {

		public SelectionCreationTool() {
		}

		/**
		 * Get the selection factory out of the tool. It may be
		 * wrappered by an AnnotationCreationFactory. 
		 * <p>
		 * No tests are done for validity. The factory must be either a SelectionFactory
		 * or an AnnotationCreationFactory wrappering a SelectionFactory.
		 * @return
		 * 
		 * @since 1.1.0
		 */
		protected SelectionFactory getSelectionFactory() {
			CreationFactory f = getFactory();
			if (f instanceof AnnotationCreationFactory) {
				return (SelectionFactory) ((AnnotationCreationFactory) f).getWrapperedFactory();
			}
			return (SelectionFactory) f;
		}
		
		public void activate() {
			super.activate();
			
			// Need to queue it off so that activation can be completed before we put up dialog, otherwise
			// palette has problems.
			Display.getCurrent().asyncExec(new Runnable() {
				public void run() {
					Object[] ret = null;					
					ISelector sel = getSelector();
					if (sel != null)
						ret = sel.getNewObjectAndType(SelectionCreationTool.this, SelectionCreationTool.this.getDomain());
					if (ret != null) {
						SelectionFactory selFactory = getSelectionFactory();
						selFactory.newObject = ret[0];
						selFactory.type = ret[1];
					} else {
						// It was canceled.
						getDomain().loadDefaultTool();
					}					
				}
			});
		}

		public void deactivate() {
			super.deactivate();
			SelectionFactory selFactory = getSelectionFactory();
			selFactory.newObject = null;
			selFactory.type = null;
		}


		/* (non-Javadoc)
		 * @see org.eclipse.gef.tools.AbstractTool#handleButtonUp(int)
		 */
		protected boolean handleButtonUp(int button) {
			// KLUDGE [259278] Because of the window that is often put up by the
			// selector, there could be a pending mouse up on the queue. This
			// can occur if a double-click was used to dispose of the selector
			// dialog. This pending double-click comes up and the default
			// action is to deactivate the tool. But since there was no cooresponding
			// mouse down the tool shouldn't deactivate.
			return (!isInState(STATE_INITIAL)) ? super.handleButtonUp(button) : false;
		}

	}
	
	protected Class getCreationToolClass() {
		return SelectionCreationTool.class;	// We need ours.
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSelectorClassName() {
		return selectorClassName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSelectorClassName(String newSelectorClassName) {
		String oldSelectorClassName = selectorClassName;
		selectorClassName = newSelectorClassName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.SELECTION_CREATION_TOOL_ENTRY__SELECTOR_CLASS_NAME, oldSelectorClassName, selectorClassName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ENTRY_LABEL:
					return basicSetEntryLabel(null, msgs);
				case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
					return basicSetEntryShortDescription(null, msgs);
				case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__STRING_PROPERTIES:
					return ((InternalEList)getStringProperties()).basicRemove(otherEnd, msgs);
				case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__KEYED_VALUES:
					return ((InternalEList)getKeyedValues()).basicRemove(otherEnd, msgs);
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
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ICON16_NAME:
				return getIcon16Name();
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ICON32_NAME:
				return getIcon32Name();
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__VISIBLE:
				return isVisible() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ID:
				return getId();
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__MODIFICATION:
				return getModification();
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__STRING_PROPERTIES:
				return getStringProperties();
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__KEYED_VALUES:
				return getKeyedValues();
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__SELECTOR_CLASS_NAME:
				return getSelectorClassName();
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
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ICON16_NAME:
				return ICON16_NAME_EDEFAULT == null ? icon16Name != null : !ICON16_NAME_EDEFAULT.equals(icon16Name);
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ICON32_NAME:
				return ICON32_NAME_EDEFAULT == null ? icon32Name != null : !ICON32_NAME_EDEFAULT.equals(icon32Name);
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__VISIBLE:
				return visible != VISIBLE_EDEFAULT;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__DEFAULT_ENTRY:
				return isDefaultEntry() != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__MODIFICATION:
				return modification != MODIFICATION_EDEFAULT;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__STRING_PROPERTIES:
				return stringProperties != null && !stringProperties.isEmpty();
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__KEYED_VALUES:
				return keyedValues != null && !keyedValues.isEmpty();
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__SELECTOR_CLASS_NAME:
				return SELECTOR_CLASS_NAME_EDEFAULT == null ? selectorClassName != null : !SELECTOR_CLASS_NAME_EDEFAULT.equals(selectorClassName);
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
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ICON16_NAME:
				setIcon16Name((String)newValue);
				return;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ICON32_NAME:
				setIcon32Name((String)newValue);
				return;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__VISIBLE:
				setVisible(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ID:
				setId((String)newValue);
				return;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__MODIFICATION:
				setModification((Permissions)newValue);
				return;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)newValue);
				return;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__STRING_PROPERTIES:
				getStringProperties().clear();
				getStringProperties().addAll((Collection)newValue);
				return;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__KEYED_VALUES:
				getKeyedValues().clear();
				getKeyedValues().addAll((Collection)newValue);
				return;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__SELECTOR_CLASS_NAME:
				setSelectorClassName((String)newValue);
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
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ICON16_NAME:
				setIcon16Name(ICON16_NAME_EDEFAULT);
				return;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ICON32_NAME:
				setIcon32Name(ICON32_NAME_EDEFAULT);
				return;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
				return;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ID:
				setId(ID_EDEFAULT);
				return;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__MODIFICATION:
				setModification(MODIFICATION_EDEFAULT);
				return;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)null);
				return;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__STRING_PROPERTIES:
				getStringProperties().clear();
				return;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__KEYED_VALUES:
				getKeyedValues().clear();
				return;
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__SELECTOR_CLASS_NAME:
				setSelectorClassName(SELECTOR_CLASS_NAME_EDEFAULT);
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
		result.append(" (selectorClassName: ");
		result.append(selectorClassName);
		result.append(')');
		return result.toString();
	}

}

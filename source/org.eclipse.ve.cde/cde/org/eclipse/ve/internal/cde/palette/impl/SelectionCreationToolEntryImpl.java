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
 *  $RCSfile: SelectionCreationToolEntryImpl.java,v $
 *  $Revision: 1.12 $  $Date: 2007-05-25 04:09:36 $ 
 */
import java.text.MessageFormat;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.SelectionCreationToolEntry;

import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.swt.widgets.Display;

import org.eclipse.ve.internal.cde.core.*;

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
	@Override
	protected EClass eStaticClass() {
		return PalettePackage.Literals.SELECTION_CREATION_TOOL_ENTRY;
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
	protected static class SelectionCreationTool extends CDECreationTool {

		private static final Object PROPERTY_SELECTOR_CLASSNAME = new Object(); 
		
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
		
		private String selectorClassName;
		
		protected void applyProperty(Object key, Object value) {
			if (key == PROPERTY_SELECTOR_CLASSNAME)
				selectorClassName = (String) value;
			else
				super.applyProperty(key, value);
		}
		
		public void activate() {
			super.activate();
			
			if (selectorClassName != null) {
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
		}
		
		private ISelector getSelector() {
			// Now create the selector
			try {
				return (ISelector) CDEPlugin.createInstance(null, selectorClassName);
			} catch (InstantiationException e) {
				String msg = MessageFormat.format(CDEMessages.Object_noinstantiate_EXC_, new Object[] { selectorClassName }); 
				CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, msg, e));
			} catch (ClassCastException e) {
				String msg =
					MessageFormat.format(CDEMessages.NotInstance_EXC_, new Object[] { selectorClassName, ISelector.class }); 
				CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, msg, e));
			} catch (Exception e) {
				CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, "", e)); //$NON-NLS-1$
			}
			return null;
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

	protected void configurePaletteEntry(PaletteEntry entry, Map entryToPaletteEntry) {
		super.configurePaletteEntry(entry, entryToPaletteEntry);
		// Need to put the selector classname into the properties so that after the selection tool is created using
		// the default ctor it knows what selector to use.
		String selectorClassName = getSelectorClassName();
		if (selectorClassName != null && selectorClassName.length() > 0)
			((ToolEntry) entry).setToolProperty(SelectionCreationTool.PROPERTY_SELECTOR_CLASSNAME, selectorClassName);
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
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__SELECTOR_CLASS_NAME:
				return getSelectorClassName();
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
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__SELECTOR_CLASS_NAME:
				setSelectorClassName((String)newValue);
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
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__SELECTOR_CLASS_NAME:
				setSelectorClassName(SELECTOR_CLASS_NAME_EDEFAULT);
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
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY__SELECTOR_CLASS_NAME:
				return SELECTOR_CLASS_NAME_EDEFAULT == null ? selectorClassName != null : !SELECTOR_CLASS_NAME_EDEFAULT.equals(selectorClassName);
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
		result.append(" (selectorClassName: ");
		result.append(selectorClassName);
		result.append(')');
		return result.toString();
	}

}

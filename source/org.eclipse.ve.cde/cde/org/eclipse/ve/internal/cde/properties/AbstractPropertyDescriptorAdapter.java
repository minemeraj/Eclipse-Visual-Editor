package org.eclipse.ve.internal.cde.properties;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: AbstractPropertyDescriptorAdapter.java,v $
 *  $Revision: 1.3 $  $Date: 2004-06-02 20:43:22 $ 
 */

import java.lang.reflect.Constructor;
import java.text.MessageFormat;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import org.eclipse.ve.internal.cde.core.CDEMessages;
import org.eclipse.ve.internal.cde.core.CDEPlugin;
/**
 * This is the base property descriptor adapter. There will be specific subclasses
 * to provide the required information for this adapter. An example is the DecoratedPropertyDescriptorAdapter,
 * which knows how to get the information from the CDE EMF PropertyDecorators.
 */

public abstract class AbstractPropertyDescriptorAdapter extends AdapterImpl implements IPropertyDescriptor {
	public static Class IPROPERTYDESCRIPTOR_TYPE = IPropertyDescriptor.class; // Key for adapter for property descriptors.

	/**
	 * instantiate the  class passed in. If it has a ctor that
	 * takes a IPropertyDescriptor, use that one.
	 *
	 * A new one is created each time. It is the responsibility of
	 * the caller to dispose of it when no longer needed.
	 */
	public static ILabelProvider createLabelProviderInstance(Class clazz, String data, Object initData, IPropertyDescriptor pd) {
		Constructor ctor = null;
		try {
			ctor = clazz.getConstructor(new Class[] { IPropertyDescriptor.class });
		} catch (NoSuchMethodException e) {
		};
		try {
			ILabelProvider provider = null;
			if (ctor != null) {
				provider = (ILabelProvider) ctor.newInstance(new Object[] { pd });
			} else {
				provider = (ILabelProvider) clazz.newInstance();
			}

			CDEPlugin.setInitializationData(provider, data, initData);
			return provider;
		} catch (Exception exc) {
			String msg = MessageFormat.format(CDEMessages.getString("Object.noinstantiate_EXC_"), new Object[] { clazz }); //$NON-NLS-1$
			CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, msg, exc));
		}
		return null;
	}

	/**
	 * instantiate the  class passed in. If it has an ctor that
	 * takes a IPropertyDescriptor, use that one.
	 * A CellEditor must be constructed with a Composite
	 */
	protected CellEditor createCellEditorInstance(Class clazz, Composite aComposite, String data, Object initData) {
		Constructor ctor = null;
		CellEditor editor = null;
		try {
			ctor = clazz.getConstructor(new Class[] { IPropertyDescriptor.class, Composite.class });
		} catch (NoSuchMethodException e) {
		};
		try {
			if (ctor != null) {
				editor = (CellEditor) ctor.newInstance(new Object[] { this, aComposite });
			} else {
				ctor = clazz.getConstructor(new Class[] { Composite.class });
				editor = (CellEditor) ctor.newInstance(new Object[] { aComposite });
			}

			CDEPlugin.setInitializationData(editor, data, initData);
			ICellEditorValidator validator = getValidator();
			if (validator != null)
				editor.setValidator(validator);
		} catch (Exception exc) {
			String msg = MessageFormat.format(CDEMessages.getString("Object.noinstantiate_EXC_"), new Object[] { clazz, target }); //$NON-NLS-1$
			CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, msg, exc));
		}
		return editor;
	}

	/**
	 * Subclasses should return the validator to use, if any. Null if none.
	 */
	protected abstract ICellEditorValidator getValidator();

	public String getDisplayName() {
		String dn = primGetDisplayName();

		return dn != null ? dn : EcoreUtil.getURI((EObject) getTarget()).fragment();
	}

	/**
	 * Subclasses need to return the displayName, if none return null.
	 */
	protected abstract String primGetDisplayName();

	/**
	 * Return the feature as the ID of the property descriptor
	 */
	public Object getId() {
		return target;
	}

	public boolean isAdapterForType(Object type) {
		return IPROPERTYDESCRIPTOR_TYPE.equals(type);
	}

	public boolean isCompatibleWith(IPropertyDescriptor anotherProperty) {
		return this.equals(anotherProperty);
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (getTarget() == null)
			return super.toString();
		else
			return super.toString() + ':' + getTarget().toString();
	}

}

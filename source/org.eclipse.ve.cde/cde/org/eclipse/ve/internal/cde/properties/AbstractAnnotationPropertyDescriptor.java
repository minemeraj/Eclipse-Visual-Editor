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
package org.eclipse.ve.internal.cde.properties;
/*
 *  $RCSfile: AbstractAnnotationPropertyDescriptor.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:48 $ 
 */

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.BasicEMap.Entry;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.core.AnnotationPolicy;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cdm.Annotation;
import org.eclipse.ve.internal.propertysheet.EToolsPropertyDescriptor;
import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.ve.internal.propertysheet.ISourcedPropertyDescriptor;
import org.eclipse.ve.internal.propertysheet.command.ICommandPropertyDescriptor;
/**
 * This is a default base class for handling property descriptors
 * on the keys of an annotation.
 */
public abstract class AbstractAnnotationPropertyDescriptor
	extends EToolsPropertyDescriptor
	implements ISourcedPropertyDescriptor, ICommandPropertyDescriptor, INeedData {

	protected EditDomain domain;

	/**
	 * The ID should be the key of the KeyedValue that this descriptor describes.
	 */
	public AbstractAnnotationPropertyDescriptor(Object key, String displayName) {
		super(key, displayName);
	}

	public void setData(Object data) {
		domain = (EditDomain) data;
	}

	public Object getValue(IPropertySource ps) {
		Annotation annotation = domain.getAnnotationLinkagePolicy().getAnnotation(ps.getEditableValue());
		if (annotation != null) {
			BasicEMap.Entry entry = getMapEntry(annotation, getId());
			if (entry != null)
				return getKeyedValue(entry);
		}
		return null;
	}

	protected static BasicEMap.Entry getMapEntry(Annotation annotation, Object key) {
		int keyPos = annotation.getKeyedValues().indexOfKey(key);
		return keyPos != -1 ? (Entry) annotation.getKeyedValues().get(keyPos) : null;
	}

	/**
	 * Return the value (as it getValue requires, i.e. turn into IPropertySource if necessary),
	 * to be edited/displayed in the property sheet.
	 */
	protected abstract Object getKeyedValue(BasicEMap.Entry kv);

	public boolean isSet(IPropertySource ps) {
		Annotation annotation = domain.getAnnotationLinkagePolicy().getAnnotation(ps.getEditableValue());
		if (annotation != null) {
			BasicEMap.Entry entry = getMapEntry(annotation, getId());
			if (entry != null)
				return isSetKeyedValue(entry);
		}
		return false;
	}

	/**
	 * Return whether the value in the Entry value is set.
	 */
	protected abstract boolean isSetKeyedValue(BasicEMap.Entry kv);

	public Command setValue(IPropertySource ps, Object setValue) {
		Object modelObject = ps.getEditableValue();
		Annotation annotation = domain.getAnnotationLinkagePolicy().getAnnotation(ps.getEditableValue());
		BasicEMap.Entry oldkv = null;
		if (annotation != null)
			oldkv = getMapEntry(annotation, getId());
		BasicEMap.Entry kv = getSetKeyedValue(oldkv, setValue); // Get the keyed value to apply
		return AnnotationPolicy.applyAnnotationSetting(modelObject, kv, domain);
	}

	/**
	 * Return the Entry to apply to the annotation for the given value from the property sheet.
	 * It can return the oldkv if the setValue is the same. This can occur in nested cases.
	 */
	protected abstract BasicEMap.Entry getSetKeyedValue(BasicEMap.Entry oldkv, Object setValue);

	public Command resetValue(IPropertySource ps) {
		Object modelObject = ps.getEditableValue();

		// For things to work correctly, we must always have a KeyedValue for this key.
		// So for reset, we need one that has the KeyedValue, but the value field isn't set.
		// We can't do this generically, so we need subclass to return one.
		BasicEMap.Entry kv = getUnsetKeyedValue(); // Get the unset keyed value
		return AnnotationPolicy.applyAnnotationSetting(modelObject, kv, domain);
	}

	/**
	 * Return the Entry that represents the value as not being set.
	 */
	protected abstract BasicEMap.Entry getUnsetKeyedValue();
}

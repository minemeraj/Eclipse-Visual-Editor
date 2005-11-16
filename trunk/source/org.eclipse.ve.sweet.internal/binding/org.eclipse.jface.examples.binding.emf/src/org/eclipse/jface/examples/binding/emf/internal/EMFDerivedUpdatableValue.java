/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jface.examples.binding.emf.internal;

import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.databinding.ChangeEvent;
import org.eclipse.jface.databinding.IChangeListener;
import org.eclipse.jface.databinding.IUpdatableValue;
import org.eclipse.jface.databinding.UpdatableValue;

public class EMFDerivedUpdatableValue extends UpdatableValue {

	private EObject currentObject;

	private final EStructuralFeature feature;

	public EMFDerivedUpdatableValue(final IUpdatableValue updatableValue,
			EStructuralFeature feature) {
		this.currentObject = (EObject) updatableValue.getValue();
		this.feature = feature;
		hookListener();
		updatableValue.addChangeListener(new IChangeListener() {
			public void handleChange(ChangeEvent changeEvent) {
				Object oldValue = getValue();
				removeListener();
				currentObject = (EObject) updatableValue.getValue();
				hookListener();
				fireChangeEvent(ChangeEvent.CHANGE, oldValue, getValue());
			}
		});
	}

	private Adapter listener = new AdapterImpl() {
		public void notifyChanged(Notification msg) {
			if (!msg.isTouch() && !updating) {
				if (feature.equals(msg.getFeature())) {
					fireChangeEvent(ChangeEvent.CHANGE, msg.getOldValue(), msg
							.getNewValue());
				}
			}
		}
	};

	private boolean updating;

	private void hookListener() {
		if (currentObject != null) {
			currentObject.eAdapters().add(listener);
		}
	}

	public void setValue(Object value) {
		updating = true;
		try {
			Object oldValue = getValue();
			currentObject.eSet(feature, value);
			fireChangeEvent(ChangeEvent.CHANGE, oldValue, getValue());
		} finally {
			updating = false;
		}
	}

	private void removeListener() {
		if (currentObject != null) {
			currentObject.eAdapters().remove(listener);
		}
	}

	public Object getValue() {
		return currentObject == null ? null : currentObject.eGet(feature);
	}

	public Class getValueType() {
		if (feature.isMany()) {
			return List.class;
		} else {
			return feature.getEType().getInstanceClass();
		}
	}

}

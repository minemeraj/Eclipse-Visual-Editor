/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jface.examples.binding.emf;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jface.binding.IChangeEvent;
import org.eclipse.jface.binding.UpdatableValue;

public class EMFUpdatableValue extends UpdatableValue {
	private final EObject object;

	private final EStructuralFeature attribute;

	private AdapterImpl adapter;

	public EMFUpdatableValue(EObject object,
			final EStructuralFeature attribute, boolean oversensitiveListening) {
		this.object = object;
		this.attribute = attribute;
		hookAdapter(oversensitiveListening);
	}

	private void hookAdapter(boolean oversensitiveListening) {
		if (oversensitiveListening) {
			adapter = new EContentAdapter() {
				public void notifyChanged(Notification notification) {
					if (!notification.isTouch()) {
						fireChangeEvent(IChangeEvent.CHANGE, null, null);
					}
					super.notifyChanged(notification);
				}
			};
		} else {
			adapter = new AdapterImpl() {
				public void notifyChanged(Notification notification) {
					if (!notification.isTouch()) {
						if (attribute.equals(notification.getFeature())) {
							fireChangeEvent(IChangeEvent.CHANGE, null, null);
						}
					}
					super.notifyChanged(notification);
				}
			};
		}
		object.eAdapters().add(adapter);
	}

	public void setValue(Object value) {
		object.eSet(attribute, value);
	}

	public Object getValue() {
		return object.eGet(attribute);
	}

	public void dispose() {
		super.dispose();
		object.eAdapters().remove(adapter);
	}

	public String toString() {
		return attribute.toString() + " of " + object.toString();
	}

	public Class getValueType() {
		if (attribute.isMany()) {
			return EList.class;
		} else {
			return attribute.getEType().getInstanceClass();
		}
	}

}
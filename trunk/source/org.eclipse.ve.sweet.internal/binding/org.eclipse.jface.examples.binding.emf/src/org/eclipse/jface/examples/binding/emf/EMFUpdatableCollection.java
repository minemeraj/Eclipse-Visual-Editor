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
/*
 *  Created Oct 14, 2005 by Gili Mendel
 * 
 *  $RCSfile: EMFUpdatableCollection.java,v $
 *  $Revision: 1.3 $  $Date: 2005-10-18 17:38:37 $ 
 */

package org.eclipse.jface.examples.binding.emf;

import java.util.Iterator;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.binding.IChangeEvent;
import org.eclipse.jface.binding.IUpdatableCollection;
import org.eclipse.jface.binding.Updatable;

public class EMFUpdatableCollection extends Updatable implements
		IUpdatableCollection {

	private final EStructuralFeature attribute;

	private final EObject object;

	private boolean updating = false;

	private Adapter adapter = new AdapterImpl() {

		public void notifyChanged(Notification msg) {
			super.notifyChanged(msg);
			// TODO: need to deal with add many and such
			if (!updating
					&& msg.getEventType() != Notification.REMOVING_ADAPTER
					&& msg.getEventType() != Notification.REMOVING_ADAPTER) { // A
																				// touch
																				// can
																				// designate
																				// a
																				// chile
																				// feature
																				// change
				if (msg.getNotifier() == object
						&& msg.getFeature() == attribute) {
					if (msg.getEventType() == Notification.ADD) {
						EObject newObject = (EObject) msg.getNewValue();
						newObject.eAdapters().add(this);
						fireChangeEvent(IChangeEvent.ADD, null, newObject, msg
								.getPosition());
					} else if (msg.getEventType() == Notification.REMOVE) {
						EObject oldObject = (EObject) msg.getOldValue();
						oldObject.eAdapters().remove(this);
						fireChangeEvent(IChangeEvent.REMOVE, oldObject, null,
								msg.getPosition());
					}
				} else {
					// notifier is one of the objects in the list
					int position = getElements().indexOf(msg.getNotifier());
					if (position != -1) {
						fireChangeEvent(IChangeEvent.CHANGE, msg.getNotifier(),
								msg.getNotifier(), position);
					}
				}
			}
		}
	};

	public EMFUpdatableCollection(EObject object, EStructuralFeature attribute,
			boolean oversensitiveListening) {
		this.object = object;
		this.attribute = attribute;
		object.eAdapters().add(adapter);
		for (Iterator itr = getElements().iterator(); itr.hasNext();) {
			EObject containedObject = (EObject) itr.next();
			containedObject.eAdapters().add(adapter);
		}
	}

	public void dispose() {
		super.dispose();
		for (Iterator it = getElements().iterator(); it.hasNext();) {
			EObject object = (EObject) it.next();
			object.eAdapters().remove(adapter);
		}
		object.eAdapters().remove(adapter);
	}

	protected EList getElements() {
		return (EList) object.eGet(attribute);
	}

	public int getSize() {
		return getElements().size();
	}

	public int addElement(Object value, int index) {
		EList list = getElements();
		if (index <= 0 || index > list.size())
			index = list.size();
		getElements().add(index, value);
		((EObject) value).eAdapters().add(adapter);
		return index;
	}

	public void removeElement(int index) {
		getElements().remove(index);
	}

	public void setElement(int row, Object value) {
		getElements().set(row, value);
	}

	public Object getElement(int row) {
		return getElements().get(row);
	}

	public Class getElementType() {
		return attribute.getEType().getInstanceClass();
	}

}

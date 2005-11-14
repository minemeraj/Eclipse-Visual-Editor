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
 *  Created Oct 20, 2005 by Gili Mendel
 * 
 *  $RCSfile: EMFUpdatableEList.java,v $
 *  $Revision: 1.5 $  $Date: 2005-11-14 22:26:29 $ 
 */

package org.eclipse.jface.examples.binding.emf;

import java.util.Iterator;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.databinding.ChangeEvent;
import org.eclipse.jface.databinding.IUpdatableCollection;
import org.eclipse.jface.databinding.Updatable;

public class EMFUpdatableEList extends Updatable implements
		IUpdatableCollection {

	private final EList list;

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
				if (msg.getNotifier() == list) {
					if (msg.getEventType() == Notification.ADD) {
						EObject newObject = (EObject) msg.getNewValue();
						newObject.eAdapters().add(this);
						fireChangeEvent(ChangeEvent.ADD, null,
								newObject, msg.getPosition());
					} else if (msg.getEventType() == Notification.REMOVE) {
						EObject oldObject = (EObject) msg.getOldValue();
						oldObject.eAdapters().remove(this);
						fireChangeEvent(ChangeEvent.REMOVE, oldObject,
								null, msg.getPosition());
					}
				} else {
					// notifier is one of the objects in the list
					int position = list.indexOf(msg.getNotifier());
					if (position != -1) {
						fireChangeEvent(ChangeEvent.CHANGE, msg
								.getNotifier(), msg.getNotifier(), position);
					}
				}
			}
		}
	};

	public EMFUpdatableEList(EList list, boolean oversensitiveListening) {
		this.list = list;
		for (Iterator itr = list.iterator(); itr.hasNext();) {
			Object containedObject = itr.next();
			if (containedObject instanceof EObject)
				((EObject) containedObject).eAdapters().add(adapter);

		}
	}

	public void dispose() {
		super.dispose();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Object object = it.next();
			if (object instanceof EObject)
				((EObject) object).eAdapters().remove(adapter);
		}
	}

	public int getSize() {
		return list.size();
	}

	public int addElement(Object value, int index) {
		if (index <= 0 || index > list.size())
			index = list.size();
		list.add(index, value);
		if (value instanceof EObject)
			((EObject) value).eAdapters().add(adapter);
		fireChangeEvent(ChangeEvent.ADD, null, value, index);
		return index;
	}

	public void removeElement(int index) {
		Object old = list.get(index);
		list.remove(index);
		fireChangeEvent(ChangeEvent.REMOVE, old, null, index);
		if (old instanceof EObject)
			((EObject) old).eAdapters().remove(adapter);
	}

	public void setElement(int row, Object value) {
		Object old = getElement(row);
		list.set(row, value);
		if (old instanceof EObject)
			((EObject) old).eAdapters().remove(adapter);
	}

	public Object getElement(int row) {
		return list.get(row);
	}

	public Class getElementType() {
		return Object.class;
	}
}

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
package org.eclipse.jface.examples.binding.emf;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.binding.IChangeEvent;
import org.eclipse.jface.binding.IColumn;
import org.eclipse.jface.binding.IUpdatableTable;
import org.eclipse.jface.binding.Updatable;

public class EMFUpdatableTable2 extends Updatable implements IUpdatableTable {

	private final EObject object;

	private final String reference;

	private final IColumn[] columns;

	private boolean updating = false;

	private Adapter adapter = new AdapterImpl() {
		public void notifyChanged(Notification msg) {
			super.notifyChanged(msg);
			if (!msg.isTouch() && !updating) {
				if (msg.getNotifier() == object
						&& msg.getFeature() == object.eClass()
								.getEStructuralFeature(reference)) {
					if (msg.getEventType() == Notification.ADD) {
						EObject newObject = (EObject) msg.getNewValue();
						newObject.eAdapters().add(this);
						fireChangeEvent(null, IChangeEvent.ADD, null, newObject, msg
								.getPosition());
					} else if (msg.getEventType() == Notification.REMOVE) {
						EObject oldObject = (EObject) msg.getOldValue();
						oldObject.eAdapters().remove(this);
						fireChangeEvent(null, IChangeEvent.REMOVE, oldObject, null,
								msg.getPosition());
					}
				} else {
					// notifier is one of the objects in the list
					int position = getList().indexOf(msg.getNotifier());
					if (position != -1) {
						fireChangeEvent(null, IChangeEvent.CHANGE, msg.getNotifier(),
								msg.getNotifier(), position);
					}
				}
			}
		}
	};

	public EMFUpdatableTable2(EObject object, String reference, Object[] columns) {
		this.object = object;
		this.reference = reference;
		object.eAdapters().add(adapter);
		for (Iterator it = getList().iterator(); it.hasNext();) {
			EObject containedObject = (EObject) it.next();
			containedObject.eAdapters().add(adapter);
		}
		this.columns = new IColumn[columns.length];
		for (int i = 0; i < columns.length; i++) {
			if(columns[i] instanceof IColumn){
				this.columns[i] = (IColumn) columns[i];
			} else if(columns[i] instanceof String){
				this.columns[i] = new EMFColumn((String)columns[i]);
			}
		}
	}

	public void dispose() {
		super.dispose();
		for (Iterator it = getList().iterator(); it.hasNext();) {
			EObject object = (EObject) it.next();
			object.eAdapters().remove(adapter);
		}
		object.eAdapters().remove(adapter);
	}

	public Class[] getColumnTypes() {
		Class[] result = new Class[columns.length];
		EStructuralFeature sf = ((EObject)object).eClass().getEStructuralFeature(reference);
		EClass rowClass = null;
		if(sf instanceof EReference){
			rowClass = ((EReference)sf).getEReferenceType();
		} else {
			rowClass = (EClass) sf.getEType();
		}
		for (int i = 0; i < result.length; i++) {
			result[i] = columns[i].getType(rowClass);
		}
		return result;
	}

	private List getList() {
		return (List) object.eGet(object.eClass().getEStructuralFeature(
				reference));
	}

	private EObject getRowObject(int row) {
		return (EObject) getList().get(row);
	}

	// public Object getValue(int row, int column) {
	// EObject rowObject = getRowObject(row);
	// return rowObject.eGet(rowObject.eClass().getEStructuralFeature(
	// features[column]));
	// }
	//
	// public void setValue(int row, int column, Object value) {
	// EObject rowObject = getRowObject(row);
	// rowObject.eSet(rowObject.eClass().getEStructuralFeature(
	// features[column]), value);
	// }

	public int getSize() {
		return getList().size();
	}

	public int addElement(Object value, int index) {
		updating = true;
		try {
			getList().add(index, value);
			return index;
		} finally {
			updating = false;
		}
	}

	public void removeElement(int index) {
		updating = true;
		try {
			getList().remove(index);
		} finally {
			updating = false;
		}
	}

	public void setElement(int row, Object value) {
		updating = true;
		try {
			getList().set(row, value);
		} finally {
			updating = false;
		}
	}

	public Object getElement(int row) {
		return getList().get(row);
	}

	public Class getElementType() {
		return object.eClass().getEStructuralFeature(reference).getEType()
				.getInstanceClass();
	}

	public Object[] getValues(int index) {
		EObject rowObject = getRowObject(index);
		Object[] result = new Object[columns.length];		
		for (int i = 0; i < result.length; i++) {
			result[i] = columns[i].getValue(rowObject);
		}
		return result;
	}

	public void setElementAndValues(int index, Object element, Object[] values) {
		doSetValues((EObject) element, values);
		if (element != getRowObject(index)) {
			setElement(index, element);
		}
	}

	public int addElementWithValues(int index, Object element, Object[] values) {
		doSetValues((EObject) element, values);
		return addElement(element, index);
	}

	public void setValues(int index, Object[] values) {
		EObject rowObject = getRowObject(index);
		doSetValues(rowObject, values);
	}

	private void doSetValues(EObject rowObject, Object[] values) {
		updating = true;
		try {
			for (int i = 0; i < values.length; i++) {
				columns[i].setValue(rowObject,values[i]);
			}
		} finally {
			updating = false;
		}
	}

}

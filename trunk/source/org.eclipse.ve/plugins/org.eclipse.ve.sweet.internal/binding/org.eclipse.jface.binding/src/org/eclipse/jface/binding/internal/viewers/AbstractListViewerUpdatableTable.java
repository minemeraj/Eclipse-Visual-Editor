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
package org.eclipse.jface.binding.internal.viewers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.binding.IUpdatableTable;
import org.eclipse.jface.binding.Updatable;
import org.eclipse.jface.viewers.AbstractListViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

public class AbstractListViewerUpdatableTable extends Updatable implements
		IUpdatableTable {

	private final AbstractListViewer viewer;

	private List elements = new ArrayList();

	private List labels = new ArrayList();

	// TODO for ComboViewer, sometimes you want to add an "empty element" so
	// that the user can null the current selection...
	public AbstractListViewerUpdatableTable(AbstractListViewer viewer) {
		this.viewer = viewer;
		viewer.setContentProvider(new IStructuredContentProvider() {

			public Object[] getElements(Object inputElement) {
				return elements.toArray();
			}

			public void dispose() {
			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}
		});
		viewer.setLabelProvider(new ILabelProvider() {
			public Image getImage(Object element) {
				return null;
			}

			public String getText(Object element) {
				return (String) labels.get(elements.indexOf(element));
			}

			public void addListener(ILabelProviderListener listener) {
			}

			public void dispose() {
			}

			public boolean isLabelProperty(Object element, String property) {
				return true;
			}

			public void removeListener(ILabelProviderListener listener) {
			}
		});
		viewer.setInput(this);
	}

	public int getSize() {
		return elements.size();
	}

	public int addElement(Object element, int index) {
		int position = doAddElementAndValue(element, index, "");
		viewer.add(element);
		return position;
	}

	private int doAddElementAndValue(Object element, int index, String value) {
		int position = elements.size();
		if (index < 0 || index > elements.size()) {
			position = elements.size();
			elements.add(element);
			labels.add(value);
		} else {
			elements.add(index, element);
			labels.add(index, value);
			position = index;
		}
		return position;
	}

	public void removeElement(int index) {
		Object element = elements.remove(index);
		viewer.remove(element);
	}

	public void setElement(int index, Object element) {
		if (elements.get(index).equals(element)) {
			viewer.update(element, null);
		} else {
			removeElement(index);
			addElement(element, index);
		}
	}

	public Object getElement(int index) {
		return elements.get(index);
	}

	public Class getElementType() {
		return Object.class;
	}

	public Class[] getColumnTypes() {
		return new Class[] { String.class };
	}

	public Object[] getValues(int index) {
		return new Object[] { labels.get(index) };
	}

	public void setElementAndValues(int index, Object element, Object[] values) {
		if (elements.get(index).equals(element)) {
			setValues(index, values);
		} else {
			removeElement(index);
			addElementWithValues(index, element, values);
		}
	}

	public int addElementWithValues(int index, Object element, Object[] values) {
		int position = doAddElementAndValue(element, index, (String) values[0]);
		viewer.add(element);
		return position;
	}

	public void setValues(int index, Object[] values) {
		labels.set(index, (String) values[0]);
		viewer.update(elements.get(index), null);
	}

}

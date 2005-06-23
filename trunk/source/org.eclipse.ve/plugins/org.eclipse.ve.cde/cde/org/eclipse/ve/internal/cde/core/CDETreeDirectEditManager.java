/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CDETreeDirectEditManager.java,v $
 *  $Revision: 1.2 $  $Date: 2005-06-23 16:08:42 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.properties.PropertySourceAdapter;

import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.ve.internal.propertysheet.ISourced;

public abstract class CDETreeDirectEditManager {

	private EditPartViewer viewer;
	private Tree parentTree;
	private TreeEditor editor;

	private CellEditor cellEditor;

	private Object oldValue;
	private TreeEditPart currentEditPart = null;
	private IPropertyDescriptor currentProperty = null;

	private EditPartListener currentEditPartListener = null;

	public CDETreeDirectEditManager(EditPartViewer v) {
		viewer = v;
		if (viewer.getControl() instanceof Tree) {
			parentTree = (Tree) viewer.getControl();

			parentTree.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent e) {
					if (cellEditor != null) {
						hideDirectEdit();
					}
				}
			});

			currentEditPartListener = new EditPartListener.Stub() {
				public void partDeactivated(EditPart editPart) {
					hideDirectEdit();
				}
			};
		}
	}

	private CellEditor getCellEditor() {
		if (cellEditor == null) {
			cellEditor = currentProperty.createPropertyEditor(parentTree);
			cellEditor.getControl().setBackground(parentTree.getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
			cellEditor.addListener(cellEditorListener);
			if (cellEditor instanceof INeedData) {
				((INeedData) cellEditor).setData(EditDomain.getEditDomain(currentEditPart));
			}
			if (cellEditor instanceof ISourced) {
				((ISourced) cellEditor).setSources(new Object[] {currentEditPart.getModel()}, new IPropertySource[] {(IPropertySource) currentEditPart.getAdapter(IPropertySource.class)}, new IPropertyDescriptor[] {currentProperty});
			}

		}
		return cellEditor;
	}

	private TreeEditor getEditor() {
		if (editor == null) {
			editor = new TreeEditor(parentTree);
			editor.horizontalAlignment = SWT.LEFT;
			editor.grabHorizontal = true;
			editor.minimumWidth = 50;
		}
		return editor;
	}

	/**
	 * Show the direct edit field for the given tree component targeting the given text property.
	 * 
	 * @param  the tree component to edit
	 * @param property the property to change
	 */
	public void performDirectEdit(TreeEditPart treeEP, IPropertyDescriptor property) {
		// This shouldn't happen, will only occur if hideDirectEdit wasn't called before 
		// another direct edit request occurred.
		if (currentEditPart != null || currentProperty != null) {
			return;
		}
		currentEditPart = treeEP;
		currentProperty = property;
		currentEditPart.addEditPartListener(currentEditPartListener);
		showDirectEdit();
	}

	private ICellEditorListener cellEditorListener = new ICellEditorListener() {
	
		public void editorValueChanged(boolean oldValidState, boolean newValidState) {
		}
	
		public void cancelEditor() {
			hideDirectEdit();
		}
	
		public void applyEditorValue() {
			saveChange();
		}
	
	};
	
	private void showDirectEdit() {
		oldValue = getDirectEditValue();
		CellEditor ce = getCellEditor();
		ce.setValue(oldValue);

		getEditor().setEditor(ce.getControl(), (TreeItem) currentEditPart.getWidget());
		ce.setFocus();
	}

	private void saveChange() {
		Object newValue = getCellEditor().getValue();
		if (!newValue.equals(oldValue) && currentEditPart != null && currentProperty != null) {
			EditDomain domain = EditDomain.getEditDomain(currentEditPart);
			domain.getCommandStack().execute(getDirectEditCommand(newValue, currentEditPart, currentProperty));
		}
		oldValue = null;
		hideDirectEdit();
	}
	
	/**
	 * Get the direct edit command for the property with the given text.
	 * @param newText
	 * @param ep
	 * @param property
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected abstract Command getDirectEditCommand(Object newValue, EditPart ep, IPropertyDescriptor property);

	private void hideDirectEdit() {
		getEditor().setEditor(null);
		getCellEditor().deactivate();
		cellEditor.removeListener(cellEditorListener);
		getCellEditor().dispose();
		cellEditor = null;
		
		if (currentEditPartListener != null && currentEditPart != null) {
			currentEditPart.removeEditPartListener(currentEditPartListener);
		}
		currentEditPart = null;
		currentProperty = null;
		oldValue = null;
	}

	/**
	 * Get the Direct Edit property's current value
	 * @return  the current property value
	 */
	private Object getDirectEditValue() {
		if (currentEditPart != null && currentProperty != null) {
			return getPropertyValue(currentEditPart, currentProperty);
		}
		return null;
	}
	
	/**
	 * Get the current string value of the property.
	 * @param ep
	 * @param property
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected Object getPropertyValue(EditPart ep, IPropertyDescriptor property) {
		// retrieve the property's value from the model
		Object value = null;
		IPropertySource ps = (IPropertySource) ep.getAdapter(IPropertySource.class);
		if (PropertySourceAdapter.isPropertySet(ps, property)) {
			value = PropertySourceAdapter.getPropertyValue(ps, property);
			if (value != null) {
				if (value instanceof IPropertySource)
					value = ((IPropertySource) value).getEditableValue();
			}
		}
		return value;
	}
}

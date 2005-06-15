/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: CDETreeDirectEditManager.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-15 20:19:34 $ 
 */

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public abstract class CDETreeDirectEditManager {

	private EditPartViewer viewer;
	private Tree parentTree;
	private TreeEditor editor;

	private Text textField = null;

	private String oldText;
	private TreeEditPart currentEditPart = null;
	private IPropertyDescriptor currentProperty = null;

	boolean inKeyEvent = false;
	boolean inMouseEvent = false;

	private EditPartListener currentEditPartListener = null;

	public CDETreeDirectEditManager(EditPartViewer v) {
		viewer = v;
		if (viewer.getControl() instanceof Tree) {
			parentTree = (Tree) viewer.getControl();

			parentTree.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent e) {
					if (textField != null && !textField.isDisposed()) {
						textField.dispose();
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

	private Text getTextField() {
		if (textField == null) {
			textField = new Text(parentTree, SWT.NONE);
			textField.setBackground(textField.getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));

			textField.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent f) {
					// Ignore focus lost events from when the editor is hidden by
					// ESC and Enter presses.
					if (!inKeyEvent && currentEditPart != null) {
						saveChange();
						hideDirectEdit();
					}
				}
			});

			textField.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent k) {
					inKeyEvent = true;
					if (k.character == SWT.ESC) {
						// Cancel if the escape key is pressed
						hideDirectEdit();
					} else if (k.character == '\r' || k.character == '\n') {
						// Accept the change if the enter key is pressed
						saveChange();
						hideDirectEdit();
					}
					inKeyEvent = false;
				}
			});
		}
		return textField;
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
	 * @param control the tree component to edit
	 * @param property the property to change
	 */
	public void performDirectEdit(TreeEditPart control, IPropertyDescriptor property) {
		// This shouldn't happen, will only occur if hideDirectEdit wasn't called before 
		// another direct edit request occurred.
		if (currentEditPart != null || currentProperty != null) {
			return;
		}
		currentEditPart = control;
		currentProperty = property;
		currentEditPart.addEditPartListener(currentEditPartListener);
		showDirectEdit();
	}

	private void showDirectEdit() {
		oldText = getDirectEditText();
		getTextField().setText(oldText);
		getTextField().selectAll();

		getEditor().setEditor(getTextField());
		getEditor().setItem((TreeItem) currentEditPart.getWidget());
		getTextField().setVisible(true);
		getTextField().setFocus();
	}

	private void saveChange() {
		String newText = getTextField().getText();
		if (!newText.equals(oldText) && currentEditPart != null && currentProperty != null) {
			EditDomain domain = EditDomain.getEditDomain(currentEditPart);
			domain.getCommandStack().execute(getDirectEditCommand(newText, currentEditPart, currentProperty));
		}
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
	protected abstract Command getDirectEditCommand(String newText, EditPart ep, IPropertyDescriptor property);

	private void hideDirectEdit() {
		getTextField().setVisible(false);
		getEditor().setEditor(null);
		if (currentEditPartListener != null && currentEditPart != null) {
			currentEditPart.removeEditPartListener(currentEditPartListener);
		}
		currentEditPart = null;
		currentProperty = null;
	}

	/**
	 * Get the Direct Edit property's current value
	 * @return  the current property value
	 */
	private String getDirectEditText() {
		String text = ""; //$NON-NLS-1$

		if (currentEditPart != null && currentProperty != null) {
			text = getPropertyValue(currentEditPart, currentProperty);
		}
		return text;
	}
	
	/**
	 * Get the current string value of the property.
	 * @param ep
	 * @param property
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected abstract String getPropertyValue(EditPart ep, IPropertyDescriptor property);
}

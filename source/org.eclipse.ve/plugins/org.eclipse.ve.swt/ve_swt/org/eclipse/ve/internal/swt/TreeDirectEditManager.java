/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;
/*
 *  $RCSfile: TreeDirectEditManager.java,v $
 *  $Revision: 1.2 $  $Date: 2004-08-27 15:35:50 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IStringBeanProxy;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

public class TreeDirectEditManager {

	public static final String VIEWER_DATA_KEY = "TreeDirectEditManager"; //$NON-NLS-1$

	private EditPartViewer viewer;
	private Tree parentTree;
	private TreeEditor editor;

	private Text textField = null;

	private String oldText;
	private ControlTreeEditPart currentControl = null;
	private EStructuralFeature currentProperty = null;

	boolean inKeyEvent = false;
	boolean inMouseEvent = false;

	private EditPartListener currentEditPartListener = null;

	public TreeDirectEditManager(EditPartViewer v) {
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
					if (!inKeyEvent && currentControl != null) {
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
	public void performDirectEdit(ControlTreeEditPart control, EStructuralFeature property) {
		// This shouldn't happen, will only occur if hideDirectEdit wasn't called before 
		// another direct edit request occurred.
		if (currentControl != null || currentProperty != null) {
			return;
		}
		currentControl = control;
		currentProperty = property;
		currentControl.addEditPartListener(currentEditPartListener);
		showDirectEdit();
	}

	private void showDirectEdit() {
		oldText = getDirectEditText();
		getTextField().setText(oldText);
		getTextField().selectAll();

		getEditor().setEditor(getTextField());
		getEditor().setItem((TreeItem) currentControl.getWidget());
		getTextField().setVisible(true);
		getTextField().setFocus();
	}

	private void saveChange() {
		String newText = getTextField().getText();
		if (!newText.equals(oldText) && currentControl != null && currentProperty != null) {
			EditDomain domain = EditDomain.getEditDomain(currentControl);
			RuledCommandBuilder cb = new RuledCommandBuilder(domain);
			IJavaObjectInstance component = (IJavaObjectInstance) currentControl.getModel();
			IJavaObjectInstance stringObject = BeanUtilities.createString(component.eResource().getResourceSet(), newText);
			cb.applyAttributeSetting(component, currentProperty, stringObject);
			domain.getCommandStack().execute(cb.getCommand());
		}
	}

	private void hideDirectEdit() {
		getTextField().setVisible(false);
		getEditor().setEditor(null);
		if (currentEditPartListener != null && currentControl != null) {
			currentControl.removeEditPartListener(currentEditPartListener);
		}
		currentControl = null;
		currentProperty = null;
	}

	/**
	 * Get the Direct Edit property's current value
	 * @return  the current property value
	 */
	private String getDirectEditText() {
		String text = ""; //$NON-NLS-1$

		if (currentControl != null && currentProperty != null) {
			// retrieve the property's value from the model
			IJavaObjectInstance component = (IJavaObjectInstance) currentControl.getModel();
			if (component.eIsSet(currentProperty)) {
				IJavaObjectInstance textObj = (IJavaObjectInstance) component.eGet(currentProperty);
				if (textObj != null) {
					// Get the value from the remote vm of the externalized string
					try {
						IBeanProxyHost host = BeanProxyUtilities.getBeanProxyHost(component);
						IBeanProxy propProxy = host.getBeanPropertyProxyValue(currentProperty);
						text = ((IStringBeanProxy) propProxy).stringValue();
					} catch (Exception e) {
					}
				}
			}
		}
		return text;
	}
}

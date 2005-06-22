package org.eclipse.ve.internal.java.vce.launcher;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: AppletParametersTab.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-22 13:00:22 $ 
 */

import java.text.MessageFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class AppletParametersTab extends AbstractLaunchConfigurationTab {

	protected Table fParmsTable;
	protected TableEditor fTableEditor;
	protected Button fAddButton;
	protected Button fRemoveButton;
	protected Button fEditButton;
	protected TableColumn nameColumn;
	protected TableColumn valueColumn;
	protected int fEditedItemIndex = -1;
	protected int fX;
	protected int fY;
	protected int fNumberOfParms;
	protected boolean isValid = true;
	static String ARGUMENT_DELIMITER = new Character((char) 254).toString();
	static String NAME_VALUE_DELIMITER = new Character((char) 255).toString();

	public void createControl(Composite aParent) {

		Composite comp = new Composite(aParent, SWT.NONE);
		setControl(comp);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		comp.setLayout(gridLayout);

		fParmsTable = new Table(comp, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		TableLayout fTableLayout = new TableLayout();
		fParmsTable.setLayout(fTableLayout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		fParmsTable.setLayoutData(gd);
		nameColumn = new TableColumn(fParmsTable, SWT.NONE);
		nameColumn.setText(VCELauncherMessages.AppletParms_name); 
		valueColumn = new TableColumn(fParmsTable, SWT.NONE);
		valueColumn.setText(VCELauncherMessages.AppletParms_value); 
		fTableLayout.addColumnData(new ColumnWeightData(100));
		fTableLayout.addColumnData(new ColumnWeightData(100));
		fParmsTable.setHeaderVisible(true);
		fParmsTable.setLinesVisible(true);

		createButtons(comp);

		fTableEditor = new TableEditor(fParmsTable);

		fParmsTable.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent event) {
				fX = event.x;
				fY = event.y;
			}
		});

		fParmsTable.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {

				enableRemoveButton();

				// Clean up any previous editor control
				Control oldEditor = fTableEditor.getEditor();
				if (oldEditor != null) {
					fEditedItemIndex = -1;
					oldEditor.dispose();
				}

				// Identify the selected row
				int index = fParmsTable.getSelectionIndex();
				if (index == -1)
					return;
				final TableItem item = fParmsTable.getItem(index);
				fEditedItemIndex = index;

				// The control that will be the editor must be a child of the Table
				final Text text = new Text(fParmsTable, SWT.NONE);

				//The text editor must have the same size as the cell and must
				//not be any smaller than 50 pixels.
				fTableEditor.horizontalAlignment = SWT.LEFT;
				fTableEditor.grabHorizontal = true;
				fTableEditor.minimumWidth = 50;

				// Look at the mouse to see which column we are going to edit,
				// either the name or the value of the parameter
				int nameWidth = nameColumn.getWidth();
				int columnToEdit = -1;
				if (nameWidth >= fX) {
					columnToEdit = 0;
				} else {
					columnToEdit = 1;
				}
				// Tell thje editor the column to edit and the control
				fTableEditor.setEditor(text, item, columnToEdit);
				// Pass the value into the text box
				text.setText(item.getText(columnToEdit));
				// Select the whole text
				text.setSelection(0, text.getText().length());
				// Update the table item when the text is changed
				final int finalColumnToEdit = columnToEdit;
				text.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent event) {
						// If the text is blank then error
						if (text.getText() == null || text.getText().trim().length() == 0) {
							setErrorMessage(VCELauncherMessages.AppletParms_novalue_WARN_); 
							isValid = false;
							text.setBackground(ColorConstants.red);
						} else {
							isValid = true;
							setErrorMessage(null);
							text.setBackground(ColorConstants.white);
							item.setText(finalColumnToEdit, text.getText());
							updateLaunchConfigurationDialog();	
						}
					}
				});

				// Assign focus to the text control
				text.setFocus();
			}
		});
	}
	protected void createButtons(Composite comp) {
		// Put some add, edit and remove buttons beneath the table	
		Composite buttonsComp = new Composite(comp, SWT.NONE);
		GridLayout buttonsLayout = new GridLayout();
		buttonsLayout.marginHeight = 0;
		buttonsLayout.marginWidth = 0;
		buttonsLayout.makeColumnsEqualWidth = true;
		buttonsComp.setLayout(buttonsLayout);
		GridData data =
			new GridData(
				GridData.VERTICAL_ALIGN_BEGINNING | GridData.HORIZONTAL_ALIGN_FILL);
		buttonsComp.setLayoutData(data);
		fAddButton = new Button(buttonsComp, SWT.PUSH);
		fAddButton.setText(VCELauncherMessages.AppletParms_new); 
		fAddButton.setLayoutData(new GridData());
		fAddButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				TableItem newItem = new TableItem(fParmsTable, SWT.NONE);
				// See the index of the new item
				int newItemIndex = fParmsTable.indexOf(newItem);
				String nameString =
					MessageFormat.format(VCELauncherMessages.AppletParms_nameindex, new Object[] { new Integer(newItemIndex)}); 
				newItem.setText(0, nameString);
				String valueString =
					MessageFormat.format(VCELauncherMessages.AppletParms_valueindex, new Object[] { new Integer(newItemIndex)}); 
				newItem.setText(1, valueString);
				updateLaunchConfigurationDialog();	
			}
		});

		fRemoveButton = new Button(buttonsComp, SWT.PUSH);
		fRemoveButton.setText(VCELauncherMessages.AppletParms_remove); 
		fRemoveButton.setLayoutData(new GridData());
		fRemoveButton.setEnabled(false);
		fRemoveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				int selectedIndex = fParmsTable.getSelectionIndex();
				if (selectedIndex != -1) {
					fParmsTable.remove(selectedIndex);
					// If the item being removed is being edited
					// then we must dispose ofd the control
					if (fEditedItemIndex == selectedIndex) {
						Control editor = fTableEditor.getEditor();
						if (editor != null) {
							fEditedItemIndex = -1;
							editor.dispose();
						}
					}
					fParmsTable.redraw();
					fRemoveButton.setEnabled(false);
					updateLaunchConfigurationDialog();	
				}
			}
		});
	}
	protected void enableRemoveButton() {
		TableItem[] selectedItems = fParmsTable.getSelection();
		if (selectedItems != null && selectedItems.length == 1) {
			fRemoveButton.setEnabled(true);
		} else {
			fRemoveButton.setEnabled(false);
		}
	}
	public void initializeFrom(ILaunchConfiguration config) {

		// Clear down the table
		fParmsTable.removeAll();
		;
		// The applet parms are all stored individually
		// The key APPLET_PARMS_NUMBER is the number of parms
		// and each one has a name and value, e.g.
		// APPLET_PARM_NAME1 and APPLET_PARM_VALUE1
		try {
			String numberOfParmsString =
				config.getAttribute(
					JavaBeanLaunchConfigurationDelegate.APPLET_PARMS_NUMBER,
					""); //$NON-NLS-1$
			if (numberOfParmsString.equals("")) //$NON-NLS-1$
				return;
			int numberOfParms = Integer.parseInt(numberOfParmsString);
			fNumberOfParms = numberOfParms;
			// For each parm there should be separately stored the name and value
			for (int i = 1; i <= numberOfParms; i++) {
				String parmName =
					config.getAttribute(
						JavaBeanLaunchConfigurationDelegate.APPLET_PARM_NAME + i,
						""); //$NON-NLS-1$
				String parmValue =
					config.getAttribute(
						JavaBeanLaunchConfigurationDelegate.APPLET_PARM_VALUE + i,
						""); //$NON-NLS-1$
				// Create a table item and add it with the new parms
				TableItem item = new TableItem(fParmsTable, SWT.NONE);
				item.setText(0, parmName);
				item.setText(1, parmValue);
			}
		} catch (CoreException exc) {
		}
	}
	/**
	 * @see ILaunchConfigurationTab#isValid(ILaunchConfiguration)
	 */
	public boolean isValid(ILaunchConfiguration config) {
		return isValid;
	}
	public void performApply(ILaunchConfigurationWorkingCopy aConfiguration) {

		// Saving the applet parms is done in two steps
		// First the number of parms is saved with the key
		// APPLET_PARMS_NUMBER
		// then each parm is saved with a name and value, e.g.
		// APPLET_NAME1 and APPLET_VALUE1
		TableItem[] tableItems = fParmsTable.getItems();
		if (tableItems == null) {
			aConfiguration.setAttribute(
				JavaBeanLaunchConfigurationDelegate.APPLET_PARMS_NUMBER,
				"0"); //$NON-NLS-1$
			return;
		}
		aConfiguration.setAttribute(
			JavaBeanLaunchConfigurationDelegate.APPLET_PARMS_NUMBER,
			String.valueOf(tableItems.length));
		for (int i = 0; i < tableItems.length; i++) {
			String parmName = tableItems[i].getText(0);
			String parmValue = tableItems[i].getText(1);
			aConfiguration.setAttribute(
				JavaBeanLaunchConfigurationDelegate.APPLET_PARM_NAME + (i + 1),
				parmName);
			aConfiguration.setAttribute(
				JavaBeanLaunchConfigurationDelegate.APPLET_PARM_VALUE + (i + 1),
				parmValue);
		}
	}
	public void setDefaults(ILaunchConfigurationWorkingCopy aConfiguration) {
	}
	public String getName() {
		return VCELauncherMessages.AppletParms_title; 
	}
	public Image getImage(){
		return JavaVEPlugin.getAppletImage();
	}
}
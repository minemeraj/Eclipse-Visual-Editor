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
 *  $RCSfile: AlignmentXYGridPropertiesPage.java,v $
 *  $Revision: 1.4 $  $Date: 2005-05-18 19:31:04 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.IEditorPart;

public class AlignmentXYGridPropertiesPage extends CustomizeLayoutPage {

	protected EditPart fEditPart;
	protected GridController gridController;
	// widgets
	protected Text fGridWidthText;
	protected Text fGridHeightText;
	protected Text fGridMarginText;
	protected Text fMessageLine;
	protected Button fWidthHeightSyncCheckBox;
	protected Scale fGridWidthScale;
	protected Scale fGridHeightScale;
	protected Scale fGridMarginScale;
	protected Button fShowGridCheckBox;

	// working values
	protected int fGridWidth;
	protected int fGridHeight;
	protected int fGridMargin;
	protected boolean fWidthHeightSync = true;
	protected boolean fShowGrid =  true;

	public Control getControl(Composite parent) {

		// top level group
		Composite dialogArea = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		dialogArea.setLayout(layout);
		GridData data;

		// Create a three column group for the fields.
		// Each field consists of a label, scale and text.
		Composite fieldsGroup = new Composite(dialogArea, SWT.NONE);
		GridLayout fieldsGroupLayout = new GridLayout();
		fieldsGroupLayout.numColumns = 3;
		fieldsGroup.setLayout(fieldsGroupLayout);
		data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessVerticalSpace = true;
		data.grabExcessHorizontalSpace = true;
		fieldsGroup.setLayoutData(data);

		// Create show grid checkbox
		fShowGridCheckBox = new Button(fieldsGroup, SWT.CHECK);
		fShowGridCheckBox.setText(CDEMessages.getString("AlignmentXYGridPropertiesPage.Show_Grid")); //$NON-NLS-1$
		data = new GridData();
		data.horizontalSpan = 3;
		fShowGridCheckBox.setLayoutData(data);
		fShowGridCheckBox.setSelection(gridController.isGridShowing());
		fShowGridCheckBox.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (fShowGridCheckBox.getSelection() != fShowGrid) {
					fShowGrid = fShowGridCheckBox.getSelection();
					gridController.setGridShowing(fShowGrid);
				}
				;
			}
		});

		// Create width/height sync checkbox
		fWidthHeightSyncCheckBox = new Button(fieldsGroup, SWT.CHECK);
		fWidthHeightSyncCheckBox.setText(CDEMessages.getString("AlignmentXYGridPropertiesPage.Keep_Width_Height_Same")); //$NON-NLS-1$
		data = new GridData();
		data.horizontalSpan = 3;
		fWidthHeightSyncCheckBox.setLayoutData(data);
		fWidthHeightSyncCheckBox.setSelection(fWidthHeightSync);
		fWidthHeightSyncCheckBox.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				fWidthHeightSync = fWidthHeightSyncCheckBox.getSelection();
				// If width/height sync is checked, set the height the same as the width
				if (fWidthHeightSync) {
					fGridHeightScale.setSelection(fGridWidthScale.getSelection());
					fGridHeightText.setText(fGridWidthText.getText());
				}
			};
		});

		// Grid width label
		Label widthLabel = new Label(fieldsGroup, SWT.NONE);
		widthLabel.setText(CDEMessages.getString("AlignmentXYGridPropertiesPage.width")); //$NON-NLS-1$

		data = new GridData();
		data.widthHint = 40;
		widthLabel.setLayoutData(data);
		// Grid width scale
		fGridWidthScale = new Scale(fieldsGroup, SWT.HORIZONTAL);
		fGridWidthScale.setMaximum(60);
		fGridWidthScale.setMinimum(2);
		fGridWidthScale.setIncrement(1);
		fGridWidthScale.setPageIncrement(10);
		data = new GridData();
		data.widthHint = 100;
		fGridWidthScale.setLayoutData(data);
		fGridWidthScale.setSelection(gridController.getGridWidth());
		fGridWidthScale.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				fGridWidthText.setText(String.valueOf(fGridWidthScale.getSelection()));
			};
		});
		// Text field for grid width
		fGridWidthText = new Text(fieldsGroup, SWT.SINGLE | SWT.BORDER);
		fGridWidthText.setText(String.valueOf(gridController.getGridWidth()));
		fGridWidthText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent anEvent) {
				if (isInputValid() && fGridWidth != gridController.getGridWidth()){
					gridController.setGridWidth(fGridWidth);
					if (fWidthHeightSync && fGridHeight != fGridWidth) {
						fGridHeightScale.setSelection(fGridWidthScale.getSelection());
						fGridHeightText.setText(fGridWidthText.getText());
					}
				}
			}
		});
		data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.widthHint = 40;
		fGridWidthText.setLayoutData(data);

		// Grid height label
		Label heightLabel = new Label(fieldsGroup, SWT.NONE);
		heightLabel.setText(CDEMessages.getString("AlignmentXYGridPropertiesPage.height")); //$NON-NLS-1$
		data = new GridData();
		data.widthHint = 40;
		heightLabel.setLayoutData(data);
		// Grid height scale
		fGridHeightScale = new Scale(fieldsGroup, SWT.HORIZONTAL);
		fGridHeightScale.setMaximum(60);
		fGridHeightScale.setMinimum(2);
		fGridHeightScale.setIncrement(1);
		fGridHeightScale.setPageIncrement(10);
		data = new GridData();
		data.widthHint = 100;
		fGridHeightScale.setLayoutData(data);
		fGridHeightScale.setSelection(gridController.getGridHeight());
		fGridHeightScale.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				fGridHeightText.setText(String.valueOf(fGridHeightScale.getSelection()));
			};
		});
		// Text field for grid height
		fGridHeightText = new Text(fieldsGroup, SWT.SINGLE | SWT.BORDER);
		fGridHeightText.setText(String.valueOf(gridController.getGridHeight()));
		fGridHeightText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent anEvent) {
				if (isInputValid() && fGridHeight != gridController.getGridHeight()) {
					gridController.setGridHeight(fGridHeight);
					if (fWidthHeightSync && fGridHeight != fGridWidth) {
						fGridWidthScale.setSelection(fGridHeightScale.getSelection());
						fGridWidthText.setText(fGridHeightText.getText());
					}
				}
			}
		});
		data = new GridData();
		data.widthHint = 40;
		fGridHeightText.setLayoutData(data);

		// Grid margin label
		Label marginLabel = new Label(fieldsGroup, SWT.NONE);
		marginLabel.setText(CDEMessages.getString("AlignmentXYGridPropertiesPage.margin")); //$NON-NLS-1$
		data = new GridData();
		data.widthHint = 40;
		marginLabel.setLayoutData(data);
		// Grid margin scale
		fGridMarginScale = new Scale(fieldsGroup, SWT.HORIZONTAL);
		fGridMarginScale.setMaximum(30);
		fGridMarginScale.setIncrement(1);
		fGridMarginScale.setPageIncrement(5);
		data = new GridData();
		data.widthHint = 100;
		fGridMarginScale.setLayoutData(data);
		fGridMarginScale.setSelection(gridController.getGridMargin());
		fGridMarginScale.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				fGridMarginText.setText(String.valueOf(fGridMarginScale.getSelection()));
			};
		});
		// Text field for grid margin
		fGridMarginText = new Text(fieldsGroup, SWT.SINGLE | SWT.BORDER);
		fGridMarginText.setText(String.valueOf(gridController.getGridMargin()));
		fGridMarginText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent anEvent) {
				if (isInputValid() && fGridMargin != gridController.getGridMargin())
					gridController.setGridMargin(fGridMargin);
			}
		});
		data = new GridData();
		data.widthHint = 40;
		fGridMarginText.setLayoutData(data);

		// Create the message line
		fMessageLine = new Text(dialogArea, SWT.READ_ONLY);
//		fMessageLine.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		fMessageLine.setLayoutData(data);
		fMessageLine.setText(""); //$NON-NLS-1$

		return dialogArea;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.core.CustomizeLayoutPage#getLabelForSelection(org.eclipse.jface.viewers.ISelection)
	 */
	protected String getLabelForSelection(ISelection newSelection) {
		if (newSelection instanceof IStructuredSelection) {
			if (((IStructuredSelection) newSelection).size() == 1) { return CDEMessages.getString("AlignmentXYGridPropertiesPage.title"); //$NON-NLS-1$
			}
		}
		return null;
	}

	protected void handleEditorPartChanged(IEditorPart oldEditorPart) {
	}

	protected void handleSelectionProviderInitialization(ISelectionProvider selectionProvider) {
	}

	protected boolean handleSelectionChanged(ISelection oldSelection) {
		ISelection newSelection = getSelection();
		if (newSelection != null && newSelection instanceof IStructuredSelection && !((IStructuredSelection) newSelection).isEmpty()) {
			List editparts = ((IStructuredSelection) newSelection).toList();
			EditPart firstParent;
			boolean enableAll = true;
			// Check to see if this is a single selected container
			if (editparts.size() == 1 && editparts.get(0) instanceof EditPart) {
				firstParent = (EditPart) editparts.get(0);
				// check to see if this is a container with a GridLayout
				if (isValidTarget(firstParent)) {
					fEditPart = firstParent;
					gridController = GridController.getGridController(fEditPart);
					if (gridController != null)
						return true;
				}
			}
			// Else check to see if the parent of all the selected components is a XY layout
			if (editparts.get(0) instanceof EditPart && ((EditPart) editparts.get(0)).getParent() != null) {
				firstParent = ((EditPart) editparts.get(0)).getParent();
				// Check the parent to ensure its layout policy is a XY Layout
				if (isValidTarget(firstParent)) {
					EditPart ep = (EditPart) editparts.get(0);
					/*
					 * Need to iterate through the selection list and ensure each selection is: - an EditPart - they share the same parent
					 */
					for (int i = 1; i < editparts.size(); i++) {
						if (editparts.get(i) instanceof EditPart) {
							ep = (EditPart) editparts.get(i);
							// Check to see if we have the same parent
							if (ep.getParent() == null || ep.getParent() != firstParent) {
								enableAll = false;
								break;
							}
						} else {
							enableAll = false;
							break;
						}
					}
					// If the parent is the same, enable all the actions and see if all the anchor & fill values are the same.
					if (enableAll) {
						fEditPart = firstParent;
						gridController = GridController.getGridController(fEditPart);
						if (gridController != null)
							return true;
					}
				}
			}
		}
		fEditPart = null;
		gridController = null;
		return false;
	}

	/*
	 * Return true if the parent's layout policy is a XYLayout. If parent is a tree editpart (selected from the Beans viewer, we need to get its
	 * corresponding graphical editpart from the Graph viewer in order to check its layout policy.
	 */
	public boolean isValidParent(EditPart parent) {
		if (parent instanceof TreeEditPart) {
			EditDomain ed = EditDomain.getEditDomain(parent);
			EditPartViewer viewer = (EditPartViewer) ed.getEditorPart().getAdapter(EditPartViewer.class);
			if (viewer != null) {
				// Get the graphical editpart using the model that is common between the two viewers
				EditPart ep = (EditPart) viewer.getEditPartRegistry().get(parent.getModel());
				if (ep != null)
					parent = ep;
			}
		}
		IActionFilter af = (IActionFilter) ((IAdaptable) parent).getAdapter(IActionFilter.class);
		if (af != null && af.testAttribute(parent, LAYOUT_FILTER_KEY, XYLayoutEditPolicy.LAYOUT_ID)) { //$NON-NLS-1$
			return true;
		}
		return false;
	}

	/*
	 * Return true if the parent's layout policy is a XYLayout. If parent is a tree editpart (selected from the Beans viewer, we need to get its
	 * corresponding graphical editpart from the Graph viewer in order to check its layout policy.
	 */
	public boolean isValidTarget(EditPart target) {
		if (target instanceof TreeEditPart) {
			EditDomain ed = EditDomain.getEditDomain(target);
			EditPartViewer viewer = (EditPartViewer) ed.getEditorPart().getAdapter(EditPartViewer.class);
			if (viewer != null) {
				// Get the graphical editpart using the model that is common between the two viewers
				EditPart ep = (EditPart) viewer.getEditPartRegistry().get(target.getModel());
				if (ep != null)
					target = ep;
			}
		}
		IActionFilter af = (IActionFilter) ((IAdaptable) target).getAdapter(IActionFilter.class);
		if (af != null && af.testAttribute(target, LAYOUT_FILTER_KEY, XYLayoutEditPolicy.LAYOUT_ID)) { //$NON-NLS-1$ //$NON-NLS-2$
			return true;
		}
		return false;
	}

	/*
	 * Check to see if the grid height,width, and margin are valid integers.
	 */
	protected boolean isInputValid() {

		// Set everyone to be not in error
		fGridHeightText.setBackground(null);
		fGridWidthText.setBackground(null);
		fGridMarginText.setBackground(null);
		fMessageLine.setText(""); //$NON-NLS-1$

		// Validate the grid height
		try {
			fGridHeight = Integer.parseInt(fGridHeightText.getText());
			if (fGridHeight <= 1) {
				fGridHeightText.setBackground(ColorConstants.red);
				fMessageLine.setText(CDEMessages.getString("AlignmentXYGridPropertiesPage.Height_Must_Be_Larger_Than_One")); //$NON-NLS-1$
			}
		} catch (NumberFormatException nfexc) {
			fGridHeightText.setBackground(ColorConstants.red);
			fMessageLine.setText(CDEMessages.getString("AlignmentXYGridPropertiesPage.Height_Must_Be_Integer")); //$NON-NLS-1$
		}

		// Validate the grid width
		try {
			fGridWidth = Integer.parseInt(fGridWidthText.getText());
			if (fGridWidth <= 1) {
				fGridWidthText.setBackground(ColorConstants.red);
				fMessageLine.setText(CDEMessages.getString("AlignmentXYGridPropertiesPage.Width_Must_Be_Larger_Than_One")); //$NON-NLS-1$
			}
		} catch (NumberFormatException nfexc) {
			fGridWidthText.setBackground(ColorConstants.red);
			fMessageLine.setText(CDEMessages.getString("AlignmentXYGridPropertiesPage.Width_Must_Be_Integer")); //$NON-NLS-1$
		}
		// Validate the grid margin
		try {
			fGridMargin = Integer.parseInt(fGridMarginText.getText());
			if (fGridMargin < 0) {
				fGridMarginText.setBackground(ColorConstants.red);
				fMessageLine.setText(CDEMessages.getString("AlignmentXYGridPropertiesPage.Margin_Must_Be_Positive")); //$NON-NLS-1$
			}
		} catch (NumberFormatException nfexc) {
			fGridMarginText.setBackground(ColorConstants.red);
			fMessageLine.setText(CDEMessages.getString("AlignmentXYGridPropertiesPage.Margin_Must_Be_Integer")); //$NON-NLS-1$
		}
		return fMessageLine.getText().length() > 0 ? false : true;
	}

	protected boolean selectionIsContainer(ISelection newSelection) {
		return true;
	}
}

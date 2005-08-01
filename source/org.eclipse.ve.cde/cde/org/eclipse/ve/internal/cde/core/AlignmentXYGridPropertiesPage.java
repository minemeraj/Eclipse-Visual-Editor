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
 *  $Revision: 1.10 $  $Date: 2005-08-01 17:09:37 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.*;
import org.eclipse.gef.ui.actions.ToggleGridAction;
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
//	protected Button fShowGridCheckBox;
	protected Button fSnapToGridCheckBox;

	// working values
	protected int fGridWidth;
	protected int fGridHeight;
	protected int fGridMargin;
	protected boolean fWidthHeightSync = true;
//	protected boolean fShowGrid =  false;
	protected boolean fSnapToGrid =  false;
	private boolean initializing = false;
	private PropertyChangeListener fPropertyChangeListener;

//	private IGridListener gridListener = new IGridListener() {
//		public void gridHeightChanged(int gridHeight,int oldGridHeight) {};
//		public void gridVisibilityChanged(boolean isShowing) {
//			fShowGrid = isShowing;
//			if (fShowGridCheckBox != null)
//				fShowGridCheckBox.setSelection(isShowing);
//		};
//		public void gridMarginChanged(int gridMargin,int oldGridMargin) {};
//		public void gridWidthChanged(int gridWidth,int oldGridWidth) {};
//	};


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
//		fShowGridCheckBox = new Button(fieldsGroup, SWT.CHECK);
//		fShowGridCheckBox.setText(CDEMessages.getString("AlignmentXYGridPropertiesPage.Show_Grid")); //$NON-NLS-1$
//		data = new GridData();
//		data.horizontalSpan = 3;
//		fShowGridCheckBox.setLayoutData(data);
//		fShowGridCheckBox.setSelection(gridController.isGridShowing());
//		fShowGridCheckBox.addSelectionListener(new SelectionAdapter() {
//
//			public void widgetSelected(SelectionEvent e) {
//				if (!initializing && fShowGridCheckBox.getSelection() != fShowGrid) {
//					gridController = GridController.getGridController(fEditPart);
//					if (gridController != null ) {
//						fShowGrid = fShowGridCheckBox.getSelection();
//						gridController.setGridShowing(fShowGrid);
//					}
//				}
//				;
//			}
//		});
//		fShowGridCheckBox.addDisposeListener(new DisposeListener() {
//			public void widgetDisposed(org.eclipse.swt.events.DisposeEvent e) {
//				if (gridController != null)
//					gridController.removeGridListener(gridListener);
//			};
//		});

		// Create Snap to grid checkbox
		fSnapToGridCheckBox = new Button(fieldsGroup, SWT.CHECK);
		fSnapToGridCheckBox.setText(CDEMessages.AlignmentXYGridPropertiesPage_Snap_To_Grid); 
		data = new GridData();
		data.horizontalSpan = 3;
		fSnapToGridCheckBox.setLayoutData(data);
		fSnapToGridCheckBox.setSelection(fSnapToGrid);
		fSnapToGridCheckBox.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (!initializing && fSnapToGridCheckBox.getSelection() != fSnapToGrid) {
					fSnapToGrid = fSnapToGridCheckBox.getSelection();
					ToggleGridAction snapToGridAction = new ToggleGridAction((GraphicalViewer)fEditPart.getRoot().getViewer());
					snapToGridAction.run();
				}
				;
			}
		});
		fSnapToGridCheckBox.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(org.eclipse.swt.events.DisposeEvent e) {
				if (fPropertyChangeListener != null)
					fEditPart.getRoot().getViewer().removePropertyChangeListener(fPropertyChangeListener);
			};
		});

		// Create width/height sync checkbox
		fWidthHeightSyncCheckBox = new Button(fieldsGroup, SWT.CHECK);
		fWidthHeightSyncCheckBox.setText(CDEMessages.AlignmentXYGridPropertiesPage_Keep_Width_Height_Same); 
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
		widthLabel.setText(CDEMessages.AlignmentXYGridPropertiesPage_width); 

		data = new GridData();
		data.widthHint = 40;
		data.grabExcessHorizontalSpace = true;
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
				if (!initializing && isInputValid() && fGridWidth != gridController.getGridWidth()){
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
		data.horizontalAlignment = GridData.END;
		fGridWidthText.setLayoutData(data);

		// Grid height label
		Label heightLabel = new Label(fieldsGroup, SWT.NONE);
		heightLabel.setText(CDEMessages.AlignmentXYGridPropertiesPage_height); 
		data = new GridData();
		data.widthHint = 40;
		data.grabExcessHorizontalSpace = true;
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
				if (!initializing && isInputValid() && fGridHeight != gridController.getGridHeight()) {
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
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.END;
		fGridHeightText.setLayoutData(data);

		// Grid margin label
		Label marginLabel = new Label(fieldsGroup, SWT.NONE);
		marginLabel.setText(CDEMessages.AlignmentXYGridPropertiesPage_margin); 
		data = new GridData();
		data.widthHint = 40;
		data.grabExcessHorizontalSpace = true;
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
				if (!initializing && isInputValid() && fGridMargin != gridController.getGridMargin())
					gridController.setGridMargin(fGridMargin);
			}
		});
		data = new GridData();
		data.widthHint = 40;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.END;
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
			if (((IStructuredSelection) newSelection).size() == 1) { return CDEMessages.AlignmentXYGridPropertiesPage_title; 
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
					if (fPropertyChangeListener != null)
						fEditPart.getRoot().getViewer().removePropertyChangeListener(fPropertyChangeListener);
//					if (gridController != null)
//						gridController.removeGridListener(gridListener);
					gridController = getGridController();
					if (gridController != null) {
						initializeValues();
//						gridController.addGridListener(gridListener);
						return true;
					}
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
						if (fPropertyChangeListener != null)
							fEditPart.getRoot().getViewer().removePropertyChangeListener(fPropertyChangeListener);
//						if (gridController != null)
//							gridController.removeGridListener(gridListener);
						gridController = getGridController();
						if (gridController != null) {
							initializeValues();
//							gridController.addGridListener(gridListener);
							return true;
						}
					}
				}
			}
		}
		fEditPart = null;
		gridController = null;
		fPropertyChangeListener = null;
		return false;
	}

	private GridController getGridController() {
		if (fEditPart instanceof TreeEditPart) {
			EditDomain ed = EditDomain.getEditDomain(fEditPart);
			EditPartViewer viewer = (EditPartViewer) ed.getEditorPart().getAdapter(EditPartViewer.class);
			if (viewer != null) {
				// Get the graphical editpart using the model that is common between the two viewers
				EditPart ep = (EditPart) viewer.getEditPartRegistry().get(fEditPart.getModel());
				if (ep != null)
					fEditPart = ep;
			}
		}
		return GridController.getGridController(fEditPart);
	}
	private void initializeValues() {
		initializing = true;
		gridController = GridController.getGridController(fEditPart);
		if (gridController != null) {
			fGridHeight = gridController.getGridHeight();
			fGridWidth = gridController.getGridWidth();
			fGridMargin = gridController.getGridMargin();
//			fShowGrid = gridController.isGridShowing();
			if (fGridHeightScale != null)
				fGridHeightScale.setSelection(fGridHeight);
			if (fGridHeightText != null)
				fGridHeightText.setText(String.valueOf(fGridHeight));
			if (fGridWidthScale != null)
				fGridWidthScale.setSelection(fGridWidth);
			if (fGridWidthText != null)
				fGridWidthText.setText(String.valueOf(fGridWidth));
			if (fGridMarginScale != null)
				fGridMarginScale.setSelection(fGridMargin);
			if (fGridMarginText != null)
				fGridMarginText.setText(String.valueOf(fGridMargin));
//			if (fShowGridCheckBox != null)
//				fShowGridCheckBox.setSelection(fShowGrid);
			initializeSnapToGrid();
			initializing = false;
		}
	}

	private void initializeSnapToGrid() {
		// Set the snap to grid capability based on the global setting in the viewer
		EditPartViewer primaryViewer = fEditPart.getRoot().getViewer();
		Object snapToGrid = primaryViewer.getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
		if (snapToGrid != null)
			fSnapToGrid = ((Boolean) snapToGrid).booleanValue();
		// Add a listener to know when the snap to grid action is toggled
		primaryViewer.addPropertyChangeListener(fPropertyChangeListener = new PropertyChangeListener() {

			public void propertyChange(java.beans.PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(SnapToGrid.PROPERTY_GRID_ENABLED)) {
					fSnapToGrid = ((Boolean) evt.getNewValue()).booleanValue();
					fSnapToGridCheckBox.setSelection(fSnapToGrid);
				}
			};
		});

		if (fSnapToGridCheckBox != null)
			fSnapToGridCheckBox.setSelection(fSnapToGrid);
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
				fMessageLine.setText(CDEMessages.AlignmentXYGridPropertiesPage_Height_Must_Be_Larger_Than_One); 
			}
		} catch (NumberFormatException nfexc) {
			fGridHeightText.setBackground(ColorConstants.red);
			fMessageLine.setText(CDEMessages.AlignmentXYGridPropertiesPage_Height_Must_Be_Integer); 
		}

		// Validate the grid width
		try {
			fGridWidth = Integer.parseInt(fGridWidthText.getText());
			if (fGridWidth <= 1) {
				fGridWidthText.setBackground(ColorConstants.red);
				fMessageLine.setText(CDEMessages.AlignmentXYGridPropertiesPage_Width_Must_Be_Larger_Than_One); 
			}
		} catch (NumberFormatException nfexc) {
			fGridWidthText.setBackground(ColorConstants.red);
			fMessageLine.setText(CDEMessages.AlignmentXYGridPropertiesPage_Width_Must_Be_Integer); 
		}
		// Validate the grid margin
		try {
			fGridMargin = Integer.parseInt(fGridMarginText.getText());
			if (fGridMargin < 0) {
				fGridMarginText.setBackground(ColorConstants.red);
				fMessageLine.setText(CDEMessages.AlignmentXYGridPropertiesPage_Margin_Must_Be_Positive); 
			}
		} catch (NumberFormatException nfexc) {
			fGridMarginText.setBackground(ColorConstants.red);
			fMessageLine.setText(CDEMessages.AlignmentXYGridPropertiesPage_Margin_Must_Be_Integer); 
		}
		return fMessageLine.getText().length() > 0 ? false : true;
	}

	protected boolean selectionIsContainer(ISelection oldSelection) {
		ISelection newSelection = getSelection();
		if (newSelection != null && newSelection instanceof IStructuredSelection && !((IStructuredSelection) newSelection).isEmpty()) {
			List editparts = ((IStructuredSelection) newSelection).toList();
			EditPart firstParent;

			// Check to see if this is a single selected container
			if (editparts.size() == 1 && editparts.get(0) instanceof EditPart) {
				firstParent = (EditPart) editparts.get(0);
				// check to see if this is a container with a GridLayout
				if (isValidTarget(firstParent)) { return true; }
			}
		}
		return false;
	}
}

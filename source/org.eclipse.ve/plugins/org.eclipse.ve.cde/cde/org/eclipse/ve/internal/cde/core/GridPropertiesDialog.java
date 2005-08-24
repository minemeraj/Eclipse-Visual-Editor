/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
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
 *  $RCSfile: GridPropertiesDialog.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:12:49 $ 
 */



import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class GridPropertiesDialog extends Dialog {
	protected GridController gridController;
	
	// widgets
	protected Text fGridWidthText;
	protected Text fGridHeightText;
	protected Text fGridMarginText;
	protected Text fMessageLine;	
	protected GridCanvas fCanvas;
	protected Button fOKButton;
	
	// working values
	protected int fGridWidth;
	protected int fGridHeight;
	protected int fGridMargin;

	// Grid canvas will paint a preview of the grid in a canvas inner subclass
	class GridCanvas extends Canvas {
		public GridCanvas(Composite aParent , int style ) {
			super(aParent , style );
			addPaintListener(new PaintListener(){
				public void paintControl(PaintEvent anEvent){
					// When the grid canvas is redrawn paint some grid points
					GC gc = anEvent.gc;
					gc.setForeground(ColorConstants.black);
					gc.setBackground(ColorConstants.white);
					gc.fillRectangle(getClientArea());
					int maxX = getClientArea().x + getClientArea().width - fGridMargin;
					int maxY = getClientArea().y + getClientArea().height - fGridMargin;
					for (int x = fGridMargin ; x <= maxX ; x += fGridWidth){
						for (int y = fGridMargin ; y <= maxY ; y += fGridHeight ){
							// Draw a grid point
							anEvent.gc.drawLine(  x , y , x , y );
						}				
					}
				}
			});
		}
	}
public GridPropertiesDialog(Shell parentShell, GridController gridController) {
	super(parentShell);
	this.gridController = gridController;
}

protected void configureShell(Shell shell) {
	super.configureShell(shell);
	shell.setText("Grid properties" ); //$NON-NLS-1$
	shell.setImage(shell.getDisplay().getSystemImage(SWT.ICON_QUESTION));	

}

/**
 * Override so that we know the OK button.
 */
protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
	Button b = super.createButton(parent, id, label, defaultButton);
	if (id == IDialogConstants.OK_ID) {
		fOKButton = b;
		validateInput();	// Now safe to validate input
	}
	return b;
}

protected Control createDialogArea(Composite parent){

	// top level group
	Composite dialogArea = new Composite(parent,SWT.NONE);
	GridLayout layout = new GridLayout();
	layout.numColumns = 1;
	dialogArea.setLayout(layout);

	// Create a two column group that has the fields on the LHS and the grid preview canvas on the RHS
	Composite controlGroup = new Composite(dialogArea,SWT.NONE);
	GridLayout controlGroupLayout = new GridLayout();
	controlGroupLayout.numColumns = 2;
	controlGroup.setLayout(controlGroupLayout);
	GridData data = new GridData();
	data.verticalAlignment = GridData.FILL;
	data.horizontalAlignment = GridData.FILL;
	data.grabExcessVerticalSpace = true;
	data.grabExcessHorizontalSpace = true;
	controlGroup.setLayoutData(data);

	// Create a three column group for the fields.
	// Each field consists of a label, scale and text.
	Composite fieldsGroup = new Composite(controlGroup,SWT.NONE);
	GridLayout fieldsGroupLayout = new GridLayout();
	fieldsGroupLayout.numColumns = 3;
	fieldsGroup.setLayout(fieldsGroupLayout);
	data = new GridData();
	data.verticalAlignment = GridData.FILL;
	data.horizontalAlignment = GridData.FILL;
	data.grabExcessVerticalSpace = true;
	data.grabExcessHorizontalSpace = true;
	fieldsGroup.setLayoutData(data);
	
	// Grid width label
	Label widthLabel = new Label(fieldsGroup,SWT.NONE);
	widthLabel.setText("Width"); //$NON-NLS-1$

	data = new GridData();
	data.widthHint = 40;
	widthLabel.setLayoutData(data);
	// Grid width scale
	final Scale widthScale = new Scale (fieldsGroup, SWT.HORIZONTAL);
	widthScale.setMaximum(60);
	widthScale.setIncrement(1);
	widthScale.setPageIncrement(10);
	data = new GridData();
	data.widthHint = 100;
	widthScale.setLayoutData(data);
	widthScale.setSelection (gridController.getGridWidth());
	widthScale.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {		
			fGridWidthText.setText(String.valueOf(widthScale.getSelection()));
		};
	});
	// Text field for grid width
	fGridWidthText = new Text(fieldsGroup,SWT.SINGLE|SWT.BORDER);
	fGridWidthText.setText(String.valueOf(gridController.getGridWidth()));
	fGridWidthText.addModifyListener( new ModifyListener(){
		public void modifyText(ModifyEvent anEvent){
			validateInput();
		}
	});
	data = new GridData();	
	data.grabExcessHorizontalSpace = true;
	data.widthHint = 40;
	fGridWidthText.setLayoutData(data);

	// Grid height label
	Label heightLabel = new Label(fieldsGroup,SWT.NONE);
	heightLabel.setText("Height"); //$NON-NLS-1$
	data = new GridData();
	data.widthHint = 40;
	heightLabel.setLayoutData(data);
	// Grid height scale
	final Scale heightScale = new Scale (fieldsGroup, SWT.HORIZONTAL);
	heightScale.setMaximum(60);
	heightScale.setIncrement(1);
	heightScale.setPageIncrement(10);
	data = new GridData();
	data.widthHint = 100;
	heightScale.setLayoutData(data);
	heightScale.setSelection (gridController.getGridHeight());
	heightScale.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {		
			fGridHeightText.setText(String.valueOf(heightScale.getSelection()));
		};
	});
	// Text field for grid height
	fGridHeightText = new Text(fieldsGroup,SWT.SINGLE|SWT.BORDER);
	fGridHeightText.setText(String.valueOf(gridController.getGridHeight()));
	fGridHeightText.addModifyListener( new ModifyListener(){
		public void modifyText(ModifyEvent anEvent){
			validateInput();
		}
	});
	data = new GridData();
	data.horizontalAlignment = GridData.END;
	data.widthHint = 40;
	fGridHeightText.setLayoutData(data);

	// Grid margin label
	Label marginLabel = new Label(fieldsGroup,SWT.NONE);
	marginLabel.setText("Margin"); //$NON-NLS-1$
	data = new GridData();
	data.widthHint = 40;
	marginLabel.setLayoutData(data);
	// Grid margin scale
	final Scale marginScale = new Scale (fieldsGroup, SWT.HORIZONTAL);
	marginScale.setMaximum(30);
	marginScale.setIncrement(1);
	marginScale.setPageIncrement(5);
	data = new GridData();
	data.widthHint = 100;
	marginScale.setLayoutData(data);
	marginScale.setSelection (gridController.getGridMargin());
	marginScale.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {		
			fGridMarginText.setText(String.valueOf(marginScale.getSelection()));
		};
	});
	// Text field for grid margin
	fGridMarginText = new Text(fieldsGroup,SWT.SINGLE|SWT.BORDER);
	fGridMarginText.setText(String.valueOf(gridController.getGridMargin()));
	fGridMarginText.addModifyListener( new ModifyListener(){
		public void modifyText(ModifyEvent anEvent){
			validateInput();
		}
	});
	data = new GridData();
	data.horizontalAlignment = GridData.END;
	data.widthHint = 40;
	fGridMarginText.setLayoutData(data);
	
	// Create canvas for painting the grid
	fCanvas = new GridCanvas(controlGroup,SWT.BORDER);
	data = new GridData();
	data.heightHint = 155;
	data.widthHint = 155;
	fCanvas.setLayoutData(data);

	// The separator and message line go at the bottom of the dialog area
	// between the fields and the buttons.
	Label sep2= new Label(dialogArea, SWT.HORIZONTAL | SWT.SEPARATOR);
	data = new GridData();
	data.horizontalAlignment= GridData.FILL;
	data.grabExcessHorizontalSpace= true;
	sep2.setLayoutData(data);

	// Create the message line
	fMessageLine = new Text(dialogArea, SWT.READ_ONLY);
	data = new GridData();
	data.horizontalAlignment= GridData.FILL;
	data.grabExcessHorizontalSpace= true;
	fMessageLine.setLayoutData(data);
	fMessageLine.setText(""); //$NON-NLS-1$

	return dialogArea;

}

/* Save the values into the Gridcontroller and close the dialog box
 */
protected void okPressed(){
	gridController.setGridWidth(fGridWidth);
	gridController.setGridHeight(fGridHeight);
	gridController.setGridMargin(fGridMargin);
	super.okPressed();
}
/* Check to see if the grid height,width, and margin are valid integers.
 */
protected void validateInput(){

	// Set everyone to be not in error
	fGridHeightText.setBackground(null);
	fGridWidthText.setBackground(null);
	fGridMarginText.setBackground(null);
	fMessageLine.setText(""); //$NON-NLS-1$
	
	// Validate the grid height
	try {
		fGridHeight = Integer.parseInt(fGridHeightText.getText());
		if ( fGridHeight <= 1 ) {
			fGridHeightText.setBackground(ColorConstants.red);
			fMessageLine.setText("Height must be larger than one"); //$NON-NLS-1$
		} 
	} catch ( NumberFormatException nfexc ) {
		fGridHeightText.setBackground(ColorConstants.red);
		fMessageLine.setText("Height must be an integer"); //$NON-NLS-1$
	}

	// Validate the grid width
	try{
		fGridWidth = Integer.parseInt(fGridWidthText.getText());
		if ( fGridWidth <= 1 ) {
			fGridWidthText.setBackground(ColorConstants.red);
			fMessageLine.setText("Width must be larger than one"); //$NON-NLS-1$
		} 
	} catch ( NumberFormatException nfexc ) {
		fGridWidthText.setBackground(ColorConstants.red);
		fMessageLine.setText("Width must be an integer"); //$NON-NLS-1$
	}
	// Validate the grid margin
	try{
		fGridMargin = Integer.parseInt(fGridMarginText.getText());
		if ( fGridMargin < 0 ) {
			fGridMarginText.setBackground(ColorConstants.red);
			fMessageLine.setText("Margin must be positive"); //$NON-NLS-1$
		}
	} catch ( NumberFormatException nfexc ) {
		fGridMarginText.setBackground(ColorConstants.red);
		fMessageLine.setText("Margin must be an integer"); //$NON-NLS-1$
	}

	// If the message line is empty no error occurred
	// Redraw the canvas showing the preview of the grid and enable the OK button, else disable it
	if ( fMessageLine.getText() == null || fMessageLine.getText().equals("")) { //$NON-NLS-1$
		fCanvas.redraw();
		fOKButton.setEnabled(true);
	} else {
		fOKButton.setEnabled(false);
	}

}
}

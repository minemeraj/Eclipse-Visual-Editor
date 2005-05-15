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
 *  $RCSfile: GridLayoutLayoutComposite.java,v $
 *  $Revision: 1.1 $  $Date: 2005-05-15 00:18:35 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class GridLayoutLayoutComposite extends Composite {


	private Button equalWidthCheckBox;
	private boolean showGrid = false;
	private GridLayoutLayoutPage parentPage;

	private org.eclipse.swt.widgets.Spinner numColumnsSpinner;
	private org.eclipse.swt.widgets.Spinner horizontalSpinner;
	private org.eclipse.swt.widgets.Spinner verticalSpinner;
	private org.eclipse.swt.widgets.Spinner heightSpinner;
	private org.eclipse.swt.widgets.Spinner widthSpinner;

	private boolean initialized = false;

	private Composite composite = null;

	private Button showGridCheckBox = null;

	protected ModifyListener spinnerModify = new ModifyListener() {

		public void modifyText(ModifyEvent e) {
			if (initialized) {
				Spinner spinner = (Spinner) e.widget;
				if (spinner == numColumnsSpinner)
					parentPage.propertyChanged(GridLayoutLayoutPage.NUM_COLUMNS_CHANGED, String.valueOf(numColumnsSpinner.getSelection()));
				else if (spinner == horizontalSpinner)
					parentPage.propertyChanged(GridLayoutLayoutPage.HORIZONTAL_SPACING_CHANGED, String.valueOf(horizontalSpinner.getSelection()));
				else if (spinner == verticalSpinner)
					parentPage.propertyChanged(GridLayoutLayoutPage.VERTICAL_SPACING_CHANGED, String.valueOf(verticalSpinner.getSelection()));
				else if (spinner == heightSpinner)
					parentPage.propertyChanged(GridLayoutLayoutPage.MARGIN_HEIGHT_CHANGED, String.valueOf(heightSpinner.getSelection()));
				else if (spinner == widthSpinner)
					parentPage.propertyChanged(GridLayoutLayoutPage.MARGIN_WIDTH_CHANGED, String.valueOf(widthSpinner.getSelection()));
			}
		}
	};

	public GridLayoutLayoutComposite(GridLayoutLayoutPage parentPage, Composite parent, int style) {
		super(parent, style);
		this.parentPage = parentPage;
		initialize();
	}

	private void initialize() {

		this.setLayout(new RowLayout());
		createComposite();

		Group spaceGroup = new Group(this, SWT.NONE);
		GridLayout g3 = new GridLayout();
		g3.numColumns = 2;
		spaceGroup.setLayout(g3);
		spaceGroup.setText(SWTMessages.getString("GridLayoutLayoutPage.spacingTitle")); //$NON-NLS-1$

		Label l2 = new Label(spaceGroup, SWT.NONE);
		l2.setText(SWTMessages.getString("GridLayoutLayoutPage.horizontalSpacing")); //$NON-NLS-1$
		horizontalSpinner = new org.eclipse.swt.widgets.Spinner(spaceGroup, SWT.NONE);
		horizontalSpinner.addModifyListener(spinnerModify);
		GridData gd3 = new GridData();
		gd3.grabExcessHorizontalSpace = true;
		l2.setLayoutData(gd3);

		Label l3 = new Label(spaceGroup, SWT.NONE);
		l3.setText(SWTMessages.getString("GridLayoutLayoutPage.verticalSpacing")); //$NON-NLS-1$
		verticalSpinner = new org.eclipse.swt.widgets.Spinner(spaceGroup, SWT.NONE);
		verticalSpinner.addModifyListener(spinnerModify);

		Label l4 = new Label(spaceGroup, SWT.NONE);
		l4.setText(SWTMessages.getString("GridLayoutLayoutPage.marginWidth")); //$NON-NLS-1$
		widthSpinner = new org.eclipse.swt.widgets.Spinner(spaceGroup, SWT.NONE);
		widthSpinner.addModifyListener(spinnerModify);

		Label l5 = new Label(spaceGroup, SWT.NONE);
		l5.setText(SWTMessages.getString("GridLayoutLayoutPage.marginHeight")); //$NON-NLS-1$
		heightSpinner = new org.eclipse.swt.widgets.Spinner(spaceGroup, SWT.NONE);
		heightSpinner.addModifyListener(spinnerModify);
		this.setSize(new org.eclipse.swt.graphics.Point(344,147));

	}

	/**
	 * This method initializes composite	
	 *
	 */    
	private void createComposite() {
		composite = new Composite(this, SWT.NONE);		   
		composite.setLayout(new GridLayout());
		showGridCheckBox = new Button(composite, SWT.CHECK);
		showGridCheckBox.setText("Show Grid");
		showGridCheckBox.setSelection(showGrid);
		showGridCheckBox.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (initialized)
					parentPage.propertyChanged(GridLayoutLayoutPage.SHOW_GRID_CHANGED, new Boolean(showGridCheckBox.getSelection()));
			}
		});

		Group colGroup = new Group(composite, SWT.NONE);
		GridData gd0 = new GridData();
		gd0.verticalAlignment = GridData.BEGINNING;
		colGroup.setLayoutData(gd0);
		GridLayout g2 = new GridLayout();
		g2.numColumns = 2;
		colGroup.setLayout(g2);
		colGroup.setText(SWTMessages.getString("GridLayoutLayoutPage.columnsTitle")); //$NON-NLS-1$
		Label l1 = new Label(colGroup, SWT.NONE);
		l1.setText(SWTMessages.getString("GridLayoutLayoutPage.numColumns")); //$NON-NLS-1$
		GridData gd4 = new GridData();
		gd4.grabExcessHorizontalSpace = true;
		l1.setLayoutData(gd4);
		numColumnsSpinner = new org.eclipse.swt.widgets.Spinner(colGroup, SWT.NONE);
		numColumnsSpinner.setMinimum(1);
		numColumnsSpinner.addModifyListener(spinnerModify);

		equalWidthCheckBox = new Button(colGroup, SWT.CHECK);
		equalWidthCheckBox.setText(SWTMessages.getString("GridLayoutLayoutPage.columnsEqualWidth")); //$NON-NLS-1$
		GridData gd1 = new GridData();
		gd1.horizontalSpan = 2;
		equalWidthCheckBox.setLayoutData(gd1);
		equalWidthCheckBox.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (initialized)
					parentPage.propertyChanged(GridLayoutLayoutPage.MAKE_COLS_EQUAL_WIDTH_CHANGED, new Boolean(equalWidthCheckBox.getSelection()));
			}
		});
	}

	
	public void setShowGrid(boolean showGrid) {
		this.showGrid = showGrid;
		if (showGridCheckBox != null && showGridCheckBox.getSelection() != showGrid)
			showGridCheckBox.setSelection(showGrid);
	}
	/*
	 * Initial values from the parent page. Order is
	 * 1. Show grid (boolean)
	 * 2. Number of columns
	 * 3. Horizontal spacing 
	 * 4. Vertical spacing
	 * 5. Margin height
	 * 6. Margin width
	 * 7. Make columns equal width (boolean)
	 */
	protected void setInitialValues (Object [] values) {
		if (values == null || values.length != 7)
			return;
		showGridCheckBox.setSelection(((Boolean)values[0]).booleanValue());
		numColumnsSpinner.setSelection(((Integer)values[1]).intValue());
		horizontalSpinner.setSelection(((Integer)values[2]).intValue());
		verticalSpinner.setSelection(((Integer)values[3]).intValue());
		heightSpinner.setSelection(((Integer)values[4]).intValue());
		widthSpinner.setSelection(((Integer)values[5]).intValue());
		equalWidthCheckBox.setSelection(((Boolean)values[6]).booleanValue());
		initialized = true;
	}


} // @jve:decl-index=0:visual-constraint="10,10"

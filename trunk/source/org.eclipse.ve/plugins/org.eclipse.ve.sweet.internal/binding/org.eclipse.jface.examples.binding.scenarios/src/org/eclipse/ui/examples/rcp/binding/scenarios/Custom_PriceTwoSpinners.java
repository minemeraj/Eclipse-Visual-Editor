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
package org.eclipse.ui.examples.rcp.binding.scenarios;

import org.eclipse.jface.databinding.BindSpec;
import org.eclipse.jface.databinding.IDataBindingContext;
import org.eclipse.jface.databinding.PropertyDescription;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.examples.rcp.adventure.Adventure;

public class Custom_PriceTwoSpinners extends Composite {

	private Label label = null;

	private Label lblPrice = null;

	private Label label2 = null;

	private Text txtPrice = null;

	private Spinner spin_Dollars = null;

	private Label label4 = null;

	private Label label3 = null;

	private Spinner spin_Cents = null;

	public Custom_PriceTwoSpinners(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.grabExcessHorizontalSpace = false;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		label = new Label(this, SWT.NONE);
		label.setText("Price:");
		lblPrice = new Label(this, SWT.NONE);
		lblPrice.setLayoutData(gridData);
		label2 = new Label(this, SWT.NONE);
		txtPrice = new Text(this, SWT.BORDER);
		label4 = new Label(this, SWT.NONE);
		label4.setText("Dollars:");
		spin_Dollars = new Spinner(this, SWT.NONE);
		spin_Dollars.setMaximum(1000000);
		spin_Dollars.setLayoutData(gridData1);
		label3 = new Label(this, SWT.NONE);
		label3.setText("Cents:");
		spin_Cents = new Spinner(this, SWT.NONE);
		spin_Cents.setMaximum(99);
		spin_Cents.setLayoutData(gridData2);
		this.setLayout(gridLayout);
		setSize(new Point(300, 200));
		bind();
	}

	private void bind() {
		IDataBindingContext dbc = SampleData.getSWTtoEMFDatabindingContext(this);

		final Adventure skiTrip = SampleData.WINTER_HOLIDAY;

		BindSpec doubleBindSpec = new BindSpec(new DoubleConverter(),null);
		dbc.bind(lblPrice, new PropertyDescription(skiTrip, "price"),doubleBindSpec);
		dbc.bind(txtPrice, new PropertyDescription(skiTrip, "price"),doubleBindSpec);

		BindSpec priceDollarsBindSpec = new BindSpec(new PriceDollarsConverter(),null);
		dbc.bind(spin_Dollars, new PropertyDescription(skiTrip, "price"),priceDollarsBindSpec);

		BindSpec priceCentsBindSpec = new BindSpec(new PriceCentsConverter(),null);
		dbc.bind(spin_Cents, new PropertyDescription(skiTrip, "price"),priceCentsBindSpec);

	}
}

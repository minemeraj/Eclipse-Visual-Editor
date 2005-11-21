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

import org.eclipse.jface.databinding.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.examples.rcp.adventure.Adventure;

public class Custom_DerivedAggregateProperty extends Composite {

	private IDataBindingContext dbc;

	private Group group = null;

	private Label label = null;

	private Text txtCustom = null;

	private Label label1 = null;

	private Text txtDescription = null;

	private Label label6 = null;

	private Text txtName = null;

	private Group group1 = null;

	private Label label5 = null;

	private Text txtCustom_1 = null;

	private Group group2 = null;

	private Label label2 = null;

	private Text txtCustom_multi = null;

	public Custom_DerivedAggregateProperty(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		setLayout(gridLayout);
		setSize(new org.eclipse.swt.graphics.Point(392, 244));
		createGroup();
		createGroup1();
		createGroup2();
		bind();
	}

	private void bind() {
		dbc = SampleData.getSWTtoEMFDatabindingContext(this);

		Adventure skiTrip = SampleData.WINTER_HOLIDAY;

		dbc.bind(txtDescription, new PropertyDescription(skiTrip, "description"),null);
		dbc.bind(txtName, new PropertyDescription(skiTrip, "name"),null);

		AdventureNameAndDescription customUpdatable = new AdventureNameAndDescription(
				skiTrip);
		dbc.bind(dbc.createUpdatable(new PropertyDescription(txtCustom, "text")), customUpdatable,null);

		IUpdatableValue descriptionUpdatable = (IUpdatableValue) dbc
				.createUpdatable(new PropertyDescription(skiTrip, "description"));
		IUpdatableValue nameUpdatable = (IUpdatableValue) dbc.createUpdatable(
				new PropertyDescription(skiTrip, "name"));

		AggregateUpdatableValue customUpdatable_1 = new AggregateUpdatableValue(
				new IUpdatableValue[] { descriptionUpdatable, nameUpdatable },
				",");
		dbc.bind(dbc.createUpdatable(txtCustom_1), customUpdatable_1,null);

		AggregateUpdatableValue customUpdatable_2 = new AggregateUpdatableValue(
				new IUpdatableValue[] { descriptionUpdatable, nameUpdatable },
				"\n");
		dbc.bind(dbc.createUpdatable(txtCustom_multi),
				customUpdatable_2,null);
	}

	/**
	 * This method initializes group
	 * 
	 */
	private void createGroup() {
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData.horizontalSpan = 3;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 4;
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData1.horizontalSpan = 1;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		group = new Group(this, SWT.NONE);
		group.setText("Custom updatable value");
		group.setLayout(gridLayout1);
		group.setLayoutData(gridData1);
		label = new Label(group, SWT.NONE);
		label.setText("Custom:");
		txtCustom = new Text(group, SWT.BORDER);
		txtCustom.setText("");
		txtCustom.setLayoutData(gridData);
		label1 = new Label(group, SWT.NONE);
		label1.setText("Description:");
		txtDescription = new Text(group, SWT.BORDER);
		txtDescription.setLayoutData(gridData2);
		label6 = new Label(group, SWT.NONE);
		label6.setText("Name:");
		txtName = new Text(group, SWT.BORDER);
		txtName.setLayoutData(gridData3);
	}

	/**
	 * This method initializes group1
	 * 
	 */
	private void createGroup1() {
		GridData gridData7 = new org.eclipse.swt.layout.GridData();
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData7.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData4 = new org.eclipse.swt.layout.GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.horizontalSpan = 3;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 4;
		group1 = new Group(this, SWT.NONE);
		group1.setText("Description and name separated by a comma");
		group1.setLayoutData(gridData7);
		group1.setLayout(gridLayout2);
		label5 = new Label(group1, SWT.NONE);
		label5.setText("Custom:");
		txtCustom_1 = new Text(group1, SWT.BORDER);
		txtCustom_1.setText("");
		txtCustom_1.setLayoutData(gridData4);
	}

	/**
	 * This method initializes group2
	 * 
	 */
	private void createGroup2() {
		GridData gridData11 = new org.eclipse.swt.layout.GridData();
		gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData11.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData8 = new GridData();
		gridData8.horizontalAlignment = GridData.FILL;
		gridData8.grabExcessHorizontalSpace = true;
		gridData8.horizontalSpan = 3;
		gridData8.heightHint = 50;
		gridData8.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 4;
		group2 = new Group(this, SWT.NONE);
		group2.setText("Description and name separated by carriage return");
		group2.setLayoutData(gridData11);
		group2.setLayout(gridLayout3);
		label2 = new Label(group2, SWT.NONE);
		label2.setText("Custom:");
		txtCustom_multi = new Text(group2, SWT.BORDER | SWT.MULTI);
		txtCustom_multi.setText("");
		txtCustom_multi.setLayoutData(gridData8);
	}
}

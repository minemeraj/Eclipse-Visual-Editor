/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies e.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.examples.rcp.binding.scenarios;

import org.eclipse.jface.databinding.IDataBindingContext;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.examples.rcp.adventure.AdventurePackage;
import org.eclipse.ui.examples.rcp.adventure.Catalog;

public class SimpleListBinding extends Composite {

	private Label label = null;

	private Label label1 = null;

	private List list = null;

	private Text txtDefaultLodging = null;

	private IDataBindingContext dbc;

	private ListViewer listViewer;

	private Label label2 = null;

	private Text txtName = null;

	public SimpleListBinding(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		setLayout(new GridLayout(2, false));
		GridData gridData6 = new org.eclipse.swt.layout.GridData();
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData6.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.grabExcessHorizontalSpace = false;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		label = new Label(this, SWT.NONE);
		label.setText("Lodgings");
		createCombo();
		label1 = new Label(this, SWT.NONE);
		label1.setText("Description");
		txtDefaultLodging = new Text(this, SWT.BORDER);
		txtDefaultLodging.setLayoutData(gridData);
		label2 = new Label(this, SWT.NONE);
		label2.setText("Name");
		txtName = new Text(this, SWT.BORDER);
		txtName.setLayoutData(gridData6);
		this.setSize(new org.eclipse.swt.graphics.Point(269, 133));
		bind();
	}

	private void bind() {
		dbc = SampleData.getSWTtoEMFDatabindingContext(this);
		AdventurePackage emfPackage = AdventurePackage.eINSTANCE;

		Catalog catalog = SampleData.CATALOG_2005;

		// dbc.bindTable(
		// dbc.createUpdatableTable(listViewer,"contents"),
		// new EMFUpdatableTable(catalog,"lodgings",new String[]
		// {"description"})
		// );
		//		
		// IUpdatableValue selectedLodging = dbc.createUpdatableValue(
		// listViewer,ViewersProperties.SELECTION);
		//		
		// dbc.bindValue(txtDefaultLodging, "text", new
		// EMFDerivedUpdatableValue(
		// selectedLodging, emfPackage.getLodging_Description()));
		//
		// dbc.bindValue(txtName, "text", new EMFDerivedUpdatableValue(
		// selectedLodging, emfPackage.getLodging_Name()));

	}

	/**
	 * This method initializes combo
	 * 
	 */
	private void createCombo() {
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		list = new List(this, SWT.V_SCROLL | SWT.BORDER);
		list.setLayoutData(gridData1);
		listViewer = new ListViewer(list);
	}

} // @jve:decl-index=0:visual-constraint="10,10"

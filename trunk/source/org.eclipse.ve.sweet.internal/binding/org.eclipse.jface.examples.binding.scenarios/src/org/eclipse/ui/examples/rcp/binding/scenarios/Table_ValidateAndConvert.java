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
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.examples.rcp.adventure.Catalog;

public class Table_ValidateAndConvert extends Composite {

	private Table table = null;

	private IDataBindingContext dbc;

	private TableViewer tableViewer;

	private Table table1 = null;

	private TableViewer tableViewer1;

	private Label lblErrorMessage = null;

	public Table_ValidateAndConvert(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.horizontalSpan = 2;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		createTable();
		this.setLayout(gridLayout);
		createTable1();
		setSize(new org.eclipse.swt.graphics.Point(507,224));
		lblErrorMessage = new Label(this, SWT.NONE);
		lblErrorMessage.setText("Label");
		lblErrorMessage.setLayoutData(gridData2);
		
		bind();
		bind1();
	}

	/**
	 * This method initializes table 
	 */
	private void createTable() {
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(60);
		tableColumn.setText("firstName");
		TableColumn tableColumn1 = new TableColumn(table, SWT.NONE);
		tableColumn1.setWidth(100);
		tableColumn1.setText("phone");
		TableColumn tableColumn2 = new TableColumn(table, SWT.NONE);
		tableColumn2.setWidth(100);
		tableColumn2.setText("state");

		tableViewer = new TableViewer(table);
	}

	private void bind() {

		// For a given catalog show its accounts with columns for "firstName,
		// "phone" and "state"
		dbc = SampleData.getSWTtoEMFDatabindingContext(this);

		Catalog catalog = SampleData.CATALOG_2005;
		
		TableViewerDescription tableViewerDescription = new TableViewerDescription(
				tableViewer);		
		tableViewerDescription.addColumn("firstName");
		tableViewerDescription.addColumn("phone",new PhoneValidator(), new PhoneConverter());
		tableViewerDescription.addColumn("state", null, null, new StateConverter());	
		dbc.bind(tableViewerDescription, new PropertyDescription(catalog,"accounts"), null);
		
		IUpdatable errorMsgUpdatable = dbc.createUpdatable(new PropertyDescription(lblErrorMessage,"text"));
		dbc.bind(errorMsgUpdatable, dbc.getCombinedValidationMessage(),null);		
		
	}
	private void bind1() {

		// For a given catalog show its accounts with columns for "firstName,
		// "lastName" and "state"
		dbc = SampleData.getSWTtoEMFDatabindingContext(this);

		Catalog catalog = SampleData.CATALOG_2005;
		
		TableViewerDescription tableViewerDescription = new TableViewerDescription(
				tableViewer1);		
		tableViewerDescription.addColumn("firstName");
		tableViewerDescription.addColumn("phone");
		tableViewerDescription.addColumn("state", new StateCellEditor(table1), null, null);	
		dbc.bind(tableViewerDescription, new PropertyDescription(catalog,"accounts"), null);		

		dbc.bind(tableViewerDescription, new PropertyDescription(catalog,"accounts"),null);
		
	}	

	/**
	 * This method initializes table1	 
	 */
	private void createTable1() {
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessVerticalSpace = true;
		table1 = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table1.setHeaderVisible(true);
		table1.setLayoutData(gridData1);
		table1.setLinesVisible(true);
		TableColumn tableColumn3 = new TableColumn(table1, SWT.NONE);
		tableColumn3.setText("firstName");
		tableColumn3.setWidth(60);
		TableColumn tableColumn4 = new TableColumn(table1, SWT.NONE);
		tableColumn4.setText("phone");
		tableColumn4.setWidth(100);
		TableColumn tableColumn5 = new TableColumn(table1, SWT.NONE);
		tableColumn5.setText("state");
		tableColumn5.setWidth(60);
		
		tableViewer1 = new TableViewer(table1);
		
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"

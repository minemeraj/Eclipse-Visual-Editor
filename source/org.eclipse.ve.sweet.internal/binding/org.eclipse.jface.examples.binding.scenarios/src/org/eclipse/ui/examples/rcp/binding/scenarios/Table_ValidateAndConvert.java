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

import org.eclipse.jface.binding.BindingException;
import org.eclipse.jface.binding.DatabindingContext;
import org.eclipse.jface.binding.PropertyDescription;
import org.eclipse.jface.binding.swt.TableViewerDescription;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.examples.rcp.adventure.Catalog;

public class Table_ValidateAndConvert extends Composite {

	private Table table = null;

	private DatabindingContext dbc;

	private TableViewer tableViewer;

	private Table table1 = null;

	private TableViewer tableViewer1;

	public Table_ValidateAndConvert(Composite parent, int style)
			throws BindingException {
		super(parent, style);
		initialize();
	}

	private void initialize() throws BindingException {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		createTable();
		this.setLayout(gridLayout);
		createTable1();
		setSize(new org.eclipse.swt.graphics.Point(507, 224));
	}

	/**
	 * This method initializes table
	 * 
	 * @throws BindingException
	 * 
	 */
	private void createTable() throws BindingException {
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

		bind();
	}

	private void bind() throws BindingException {

		// For a given catalog show its accounts with columns for "firstName,
		// "phone" and "state"
		dbc = SampleData.getSWTtoEMFDatabindingContext(this);

		Catalog catalog = SampleData.CATALOG_2005;

		TableViewerDescription tableViewerDescription = new TableViewerDescription(
				tableViewer);
		tableViewerDescription.addColumn("FirstName", "firstName");
		tableViewerDescription.addColumn("Phone", "phone",
				new PhoneValidator(), new PhoneConverter());
		tableViewerDescription.addColumn("State", "state", null, null,
				new StateConverter());
		dbc.bind2(tableViewerDescription, new PropertyDescription(catalog,
				"accounts"), null);

	}

	private void bind1() throws BindingException {

		// For a given catalog show its accounts with columns for "firstName,
		// "lastName" and "state"
		dbc = SampleData.getSWTtoEMFDatabindingContext(this);

		Catalog catalog = SampleData.CATALOG_2005;

		TableViewerDescription tableViewerDescription = new TableViewerDescription(
				tableViewer1);
		tableViewerDescription.addColumn("FirstName", "firstName");
		tableViewerDescription.addColumn("Phone", "phone");
		tableViewerDescription.addColumn("State", "state", new StateCellEditor(
				table1), null, null);
		dbc.bind2(tableViewerDescription, new PropertyDescription(catalog,
				"accounts"), null);

		dbc.bind2(tableViewerDescription, new PropertyDescription(catalog,
				"accounts"), null);

	}

	/**
	 * This method initializes table1
	 * 
	 * @throws BindingException
	 * 
	 */
	private void createTable1() throws BindingException {
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

		bind1();
	}
} // @jve:decl-index=0:visual-constraint="10,10"

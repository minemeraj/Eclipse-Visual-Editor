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
package org.eclipse.ui.examples.pojo.binding.scenarios;

import org.eclipse.jface.databinding.IDataBindingContext;
import org.eclipse.jface.databinding.Property;
import org.eclipse.jface.databinding.viewers.TableViewerDescription;
import org.eclipse.jface.tests.binding.scenarios.pojo.Account;
import org.eclipse.jface.tests.binding.scenarios.pojo.Catalog;
import org.eclipse.jface.tests.binding.scenarios.pojo.SampleData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class Table_Accounts extends Composite {

	private Table table = null;

	private IDataBindingContext dbc;

	private TableViewer tableViewer;

	private Table table1 = null;

	private TableViewer tableViewer1;

	public Table_Accounts(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		createTable();
		this.setLayout(gridLayout);
		createTable1();
		setSize(new org.eclipse.swt.graphics.Point(474, 241));
	}

	/**
	 * This method initializes table
	 * 
	 */
	private void createTable() {
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		table = new Table(this, SWT.FULL_SELECTION | SWT.BORDER);
		table.setHeaderVisible(true);
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(60);
		tableColumn.setText("firstName");
		TableColumn tableColumn1 = new TableColumn(table, SWT.NONE);
		tableColumn1.setWidth(60);
		tableColumn1.setText("lastName");
		TableColumn tableColumn2 = new TableColumn(table, SWT.NONE);
		tableColumn2.setWidth(60);
		tableColumn2.setText("state");

		tableViewer = new TableViewer(table);

		bind(tableViewer);
	}

	private void bind(TableViewer viewer) {

		// For a given catalog show its accounts with columns for "firstName,
		// "lastName" and "state"
		dbc = SampleData.getDatabindingContext(this);

		Catalog catalog = SampleData.CATALOG_2005;

		TableViewerDescription tableViewerDescription = new TableViewerDescription(
				viewer);
		tableViewerDescription.addColumn("firstName");
		tableViewerDescription.addColumn("lastName");
		tableViewerDescription.addColumn("state");
		dbc.bind(tableViewerDescription, new Property(catalog,"accounts",Account.class,Boolean.TRUE), null);

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
		table1 = new Table(this, SWT.FULL_SELECTION | SWT.BORDER);
		table1.setHeaderVisible(true);
		table1.setLayoutData(gridData1);
		table1.setLinesVisible(true);
		TableColumn tableColumn3 = new TableColumn(table1, SWT.NONE);
		tableColumn3.setText("firstName");
		tableColumn3.setWidth(60);
		TableColumn tableColumn4 = new TableColumn(table1, SWT.NONE);
		tableColumn4.setText("lastName");
		tableColumn4.setWidth(60);
		TableColumn tableColumn5 = new TableColumn(table1, SWT.NONE);
		tableColumn5.setText("state");
		tableColumn5.setWidth(60);

		tableViewer1 = new TableViewer(table1);
		bind(tableViewer1);
	}
} // @jve:decl-index=0:visual-constraint="10,10"

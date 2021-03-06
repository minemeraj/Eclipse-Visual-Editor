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
import org.eclipse.jface.tests.binding.scenarios.pojo.Category;
import org.eclipse.jface.tests.binding.scenarios.pojo.SampleData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class Table_Lodgings extends Composite {

	private Table table = null;

	private IDataBindingContext dbc;

	private TableViewer tableViewer;

	private Table table1 = null;

	private TableViewer tableViewer1;

	public Table_Lodgings(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		createTable();
		this.setLayout(gridLayout);
		createTable1();
		setSize(new org.eclipse.swt.graphics.Point(750, 241));
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
		tableColumn.setWidth(150);
		tableColumn.setText("Description");
		TableColumn tableColumn1 = new TableColumn(table, SWT.NONE);
		tableColumn1.setWidth(60);
		tableColumn1.setText("Price");
		TableColumn tableColumn2 = new TableColumn(table, SWT.NONE);
		tableColumn2.setWidth(80);
		tableColumn2.setText("Default Lodging");

		tableViewer = new TableViewer(table);

		bind(tableViewer);
	}

	private void bind(TableViewer viewer) {

		// For a given catalog show its accounts with columns for "firstName,
		// "lastName" and "state"
		dbc = SampleData.getDatabindingContext(this);

		Category category = SampleData.WINTER_CATEGORY;

		TableViewerDescription tableViewerDescription = new TableViewerDescription(
				viewer);
		tableViewerDescription.addEditableColumn("description");
		tableViewerDescription.addEditableColumn("price",
				new DoubleTextCellEditor(viewer.getTable()), null,
				new DoubleConverter());
		tableViewerDescription.addEditableColumn("defaultLodging", null ,
				null, new LodgingConverter());
		dbc.bind(tableViewerDescription, new Property(category,
				"adventures"), null);

	}

	/**
	 * This method initializes table1
	 * 
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
		tableColumn3.setText("Description");
		tableColumn3.setWidth(160);
		TableColumn tableColumn4 = new TableColumn(table1, SWT.NONE);
		tableColumn4.setText("Price");
		tableColumn4.setWidth(60);
		TableColumn tableColumn5 = new TableColumn(table1, SWT.NONE);
		tableColumn5.setText("Default Lodging");
		tableColumn5.setWidth(80);

		tableViewer1 = new TableViewer(table1);
		bind(tableViewer1);
	}
} // @jve:decl-index=0:visual-constraint="10,10"

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

import java.util.StringTokenizer;

import org.eclipse.jface.binding.BindingException;
import org.eclipse.jface.binding.DatabindingContext;
import org.eclipse.jface.binding.DefaultCellModifier;
import org.eclipse.jface.binding.PropertyDescription;
import org.eclipse.jface.binding.swt.TableViewerDescription;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.examples.rcp.adventure.Account;
import org.eclipse.ui.examples.rcp.adventure.Catalog;

public class Table_Accounts_CustomColumns extends Composite {

	private Table table = null;

	private DatabindingContext dbc;

	private TableViewer tableViewer;

	private Table table1 = null;

	private TableViewer tableViewer1;

	public Table_Accounts_CustomColumns(Composite parent, int style)
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
		setSize(new org.eclipse.swt.graphics.Point(474,241));
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
		table = new Table(this, SWT.NONE);
		table.setHeaderVisible(true);
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(120);
		tableColumn.setText("first,last");
		TableColumn tableColumn1 = new TableColumn(table, SWT.NONE);
		tableColumn1.setWidth(60);
		tableColumn1.setText("state");		
		tableViewer = new TableViewer(table);

		bind();
	}

	private void bind1() throws BindingException {

		// For a given catalog show its accounts with columns for "firstName,
		// "lastName" and "state"
		dbc = SampleData.getSWTtoEMFDatabindingContext(this);
		Catalog catalog = SampleData.CATALOG_2005;

		TableViewerDescription tableViewerDescription = new TableViewerDescription(tableViewer1);		
		tableViewerDescription.addColumn("First Name", "firstName");
		tableViewerDescription.addColumn("Last Name", "lastName");
		tableViewerDescription.addColumn("State", "state");		
		dbc.bind2(tableViewerDescription, new PropertyDescription(catalog,
				"accounts"), null);	
	
	}
	
	private void bind() throws BindingException {

		// For a given catalog show its accounts with columns for "firstName,
		// "lastName" and "state"
		dbc = SampleData.getSWTtoEMFDatabindingContext(this);
		Catalog catalog = SampleData.CATALOG_2005;
		
		TableViewerDescription tableViewerDescription = new TableViewerDescription(tableViewer);		
		tableViewerDescription.addColumn("Full Name", "fullName");
		tableViewerDescription.addColumn("State", "state");
		tableViewerDescription.setCellModifier(new DefaultCellModifier(tableViewerDescription){
			public Object getValue(Object element, String property) {
				if("fullName".equals(property)){
					return ((Account)element).getFirstName() + "," + ((Account)element).getLastName();
				} else {
					return super.getValue(element, property);
				}
			}
			public void modify(Object element, String property, Object value) {
				if("fullName".equals(property)){
					Account account = (Account) ((TableItem)element).getData();
					StringTokenizer tokenizer = new StringTokenizer((String)value,",");
					account.setFirstName(tokenizer.nextToken().trim());
					account.setLastName(tokenizer.nextToken().trim());
				} else {
					super.modify(element, property, value);
				}
			}
		});
		dbc.bind2(tableViewerDescription, new PropertyDescription(catalog,
				"accounts"), null);		

	}	

	/**
	 * This method initializes table1	
	 * @throws BindingException 
	 *
	 */
	private void createTable1() throws BindingException {
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessVerticalSpace = true;
		table1 = new Table(this, SWT.NONE);
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
		bind1();
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"

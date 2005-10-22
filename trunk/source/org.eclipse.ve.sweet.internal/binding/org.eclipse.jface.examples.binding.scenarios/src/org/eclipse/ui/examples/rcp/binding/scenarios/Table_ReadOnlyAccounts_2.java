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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.binding.BindingException;
import org.eclipse.jface.binding.DatabindingContext;
import org.eclipse.jface.binding.IUpdatable;
import org.eclipse.jface.binding.IUpdatableFactory2;
import org.eclipse.jface.binding.IdentityConverter;
import org.eclipse.jface.binding.TableDescription;
import org.eclipse.jface.binding.TableDescription2;
import org.eclipse.jface.examples.binding.emf.EMFColumn;
import org.eclipse.jface.examples.binding.emf.EMFUpdatableTable;
import org.eclipse.jface.examples.binding.emf.EMFUpdatableTable2;
import org.eclipse.jface.examples.binding.emf.IColumn;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.examples.rcp.adventure.Account;
import org.eclipse.ui.examples.rcp.adventure.Catalog;

public class Table_ReadOnlyAccounts_2 extends Composite {

	private Table table = null;

	private DatabindingContext dbc;

	private TableViewer tableViewer;

	public Table_ReadOnlyAccounts_2(Composite parent, int style)
			throws BindingException {
		super(parent, style);
		initialize();
	}

	private void initialize() throws BindingException {
		this.setLayout(new GridLayout());
		createTable();
		setSize(new Point(300, 200));
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
		tableColumn.setWidth(60);
		tableColumn.setText("firstName");
		TableColumn tableColumn1 = new TableColumn(table, SWT.NONE);
		tableColumn1.setWidth(60);
		tableColumn1.setText("lastName");
		TableColumn tableColumn2 = new TableColumn(table, SWT.NONE);
		tableColumn2.setWidth(60);
		tableColumn2.setText("state");

		tableViewer = new TableViewer(table);

		bind();
	}

	private void bind() throws BindingException {

		// For a given catalog show its accounts with columns for "firstName,
		// "lastName" and "state"
		dbc = SampleData.getSWTtoEMFDatabindingContext(this);

		Catalog catalog = SampleData.CATALOG_2005;

		dbc.addUpdatableFactory2(new IUpdatableFactory2() {
			public IUpdatable createUpdatable(Object description) {
				if (description instanceof TableDescription2) {
					TableDescription2 tableDescription = (TableDescription2) description;
					Object object = tableDescription.getObject();
					if (object instanceof EObject) {
						return new EMFUpdatableTable2((EObject) object,
								(String) tableDescription.getPropertyID(),
								(Object[]) tableDescription
										.getColumnPropertyIDs());
					}
				}
				return null;
			}
		});

		dbc.bind2(tableViewer, new TableDescription2(catalog, "accounts",
				new Object[] { "firstName", "lastName", "state"}), null);

		
	}
}

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
 *  $RCSfile: Table_ReadWriteAccounts.java,v $
 *  $Revision: 1.2 $  $Date: 2005-10-18 14:05:34 $ 
 */
package org.eclipse.ui.examples.rcp.binding.scenarios;

import org.eclipse.jface.binding.BindingException;
import org.eclipse.jface.binding.DatabindingService;
import org.eclipse.jface.binding.IUpdatable;
import org.eclipse.jface.binding.IdentityConverter;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.examples.rcp.adventure.Account;
import org.eclipse.ui.examples.rcp.adventure.Catalog;

public class Table_ReadWriteAccounts extends Composite {

	private Table table = null;
	private DatabindingService dbs;
	private TableViewer tableViewer;

	public Table_ReadWriteAccounts(Composite parent, int style) throws BindingException {
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
	private void bind() throws BindingException{
		
		// For a given catalog show its accounts with columns for "firstName, "lastName" and "state"
		dbs = SampleData.getSWTtoEMFDatabindingService(this);		
		
		Catalog catalog = SampleData.CATALOG_2005;
				
		tableViewer.setLabelProvider(new ITableLabelProvider(){
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}
			public String getColumnText(Object element, int columnIndex) {
				Account account = (Account)element;
				switch (columnIndex) {
				case 0:
					return account.getFirstName();
				case 1:
					return account.getLastName();
				case 2:
					return account.getState();
				default:
					return null;
				}
			}
			public void addListener(ILabelProviderListener listener){}
			public void dispose() {}
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}
			public void removeListener(ILabelProviderListener listener) {}
		});
		
		tableViewer.setColumnProperties(new String[] {"firstName","lastName","state"});
		tableViewer.setCellModifier(new ICellModifier(){
			public boolean canModify(Object element, String property) {
				return true;
			}
			public Object getValue(Object element, String property) {
				Account account = (Account)element;
				if ("firstName".equals(property)){
					return account.getFirstName();
				} else if ("lastName".equals(property)){
					return account.getLastName();
				} else if ("state".equals(property)){
					return account.getState();
				} else {
					return "UNKNOWN " + property;
				}
			}
			public void modify(Object element, String property, Object value) {
				Account account = (Account) ((TableItem)element).getData();
				if ("firstName".equals(property)){
					account.setFirstName((String) value);
				} else if ("lastName".equals(property)){
					account.setLastName((String) value);
				} else if ("state".equals(property)){
					account.setState((String) value);
				}			
			}			
		});
		
		tableViewer.setCellEditors(new CellEditor[]{
				new TextCellEditor(table), 
				new TextCellEditor(table), 
				new TextCellEditor(table)
		});

		dbs.bind(tableViewer,"contents",catalog,"accounts",new IdentityConverter(Account.class,Object.class));
		
	}
}

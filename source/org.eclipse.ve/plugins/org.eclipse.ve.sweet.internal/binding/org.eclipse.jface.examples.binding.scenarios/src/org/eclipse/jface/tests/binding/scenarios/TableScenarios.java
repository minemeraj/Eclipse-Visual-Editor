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
package org.eclipse.jface.tests.binding.scenarios;

import java.lang.reflect.Field;
import java.util.List;

import org.eclipse.jface.databinding.BindingException;
import org.eclipse.jface.databinding.PropertyDescription;
import org.eclipse.jface.databinding.swt.TableViewerDescription;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.examples.rcp.adventure.Account;
import org.eclipse.ui.examples.rcp.adventure.Catalog;
import org.eclipse.ui.examples.rcp.binding.scenarios.PhoneConverter;
import org.eclipse.ui.examples.rcp.binding.scenarios.SampleData;
import org.eclipse.ui.examples.rcp.binding.scenarios.StateConverter;

/**
 * To run the tests in this class, right-click and select "Run As JUnit Plug-in
 * Test". This will also start an Eclipse instance. To clean up the launch
 * configuration, open up its "Main" tab and select "[No Application] - Headless
 * Mode" as the application to run.
 */

public class TableScenarios extends ScenariosTestCase {

	private TableViewer tableViewer;
	private Catalog catalog;
	private TableColumn firstNameColumn;
	private TableColumn lastNameColumn;
	private TableColumn stateColumn;

	protected void setUp() throws Exception {
		super.setUp();
		getComposite().setLayout(new FillLayout());
		tableViewer = new TableViewer(getComposite());
		firstNameColumn = new TableColumn(tableViewer.getTable(),SWT.NONE);
		lastNameColumn = new TableColumn(tableViewer.getTable(),SWT.NONE);		
		stateColumn = new TableColumn(tableViewer.getTable(),SWT.NONE);		
		
		catalog = SampleData.CATALOG_2005; // Lodging source	
	}

	protected void tearDown() throws Exception {
		// do any teardown work here
		super.tearDown();
		tableViewer.getTable().dispose();
		tableViewer = null;
		firstNameColumn = null;
		lastNameColumn = null;
		stateColumn = null;
	}

	public void testScenario01() throws BindingException {
		// Show that a TableViewer with three columns renders the accounts
		List accounts = catalog.getAccounts();		
		
		TableViewerDescription tableViewerDescription = new TableViewerDescription(tableViewer);		
		tableViewerDescription.addColumn("FirstName", "firstName");
		tableViewerDescription.addColumn("LastName", "lastName");
		tableViewerDescription.addColumn("State", "state");	
		getDbc().bind2(tableViewerDescription, new PropertyDescription(catalog,"accounts"), null);

		// Verify the data in the table columns matches the accounts
		for (int i = 0; i < accounts.size(); i++) {
			Account account = (Account) catalog.getAccounts().get(i);		
			String col_0 = ((ITableLabelProvider)tableViewer.getLabelProvider()).getColumnText(account,0);
			assertEquals(account.getFirstName(),col_0);
			String col_1 = ((ITableLabelProvider)tableViewer.getLabelProvider()).getColumnText(account,1);
			assertEquals(account.getLastName(),col_1);
			String col_2 = ((ITableLabelProvider)tableViewer.getLabelProvider()).getColumnText(account,2);
			assertEquals(account.getState(),col_2);
			
		}	
	}
	
	public void testScenario02() throws BindingException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		// Show that a TableViewer with three columns can be used to update columns
		List accounts = catalog.getAccounts();	
		
		TableViewerDescription tableViewerDescription = new TableViewerDescription(tableViewer);		
		tableViewerDescription.addColumn("firstName", "firstName");
		tableViewerDescription.addColumn("lastName", "lastName",null, new PhoneConverter());
		tableViewerDescription.addColumn("state", "state",null,new StateConverter());	
		getDbc().bind2(tableViewerDescription, new PropertyDescription(catalog,"accounts"), null);			
		
		CellEditor[] cellEditors = tableViewer.getCellEditors();
		tableViewer.setSelection(new StructuredSelection(tableViewer.getTable().getItem(0)));
		// To activate the cell editor on the first row we must get a private field which is the TableViewer.tableEditor
		Field tableEditorField = TableViewer.class.getDeclaredField("tableEditor");
		tableEditorField.setAccessible(true);
		TableEditor tableEditor = (TableEditor)tableEditorField.get(tableViewer);
		// Test firstName
		TextCellEditor firstNameEditor = (TextCellEditor)cellEditors[0];
		tableEditor.setEditor(firstNameEditor.getControl(),tableViewer.getTable().getItem(0),0);
		// Change the firstName and test it goes to the model
		((Text)firstNameEditor.getControl()).setText("Bill");
		((Text)((TextCellEditor)cellEditors[0]).getControl()).notifyListeners(SWT.DefaultSelection,null);
		Account account = (Account)accounts.get(0);		
//		assertEquals("Bill",account.getFirstName());
	}	
	
	public void testScenario03() throws BindingException {
		// Show that converters work for table columns
		List accounts = catalog.getAccounts();	
		
		TableViewerDescription tableViewerDescription = new TableViewerDescription(tableViewer);		
		tableViewerDescription.addColumn("lastName", "lastName");
		tableViewerDescription.addColumn("phone", "phone",null, new PhoneConverter());
		tableViewerDescription.addColumn("state", "state",null,new StateConverter());	
		getDbc().bind2(tableViewerDescription, new PropertyDescription(catalog,"accounts"), null);		
				
		// Verify that the data in the the table columns matches the expected
		// What we are looking for is that the phone numbers are converterted to nnn-nnn-nnnn and that
		// the state letters are converted to state names
		// Verify the data in the table columns matches the accounts
		PhoneConverter phoneConverter = new PhoneConverter();
		StateConverter stateConverter = new StateConverter();
		for (int i = 0; i < accounts.size(); i++) {
			Account account = (Account) catalog.getAccounts().get(i);		
			// Check the phone number
			String col_phone = ((ITableLabelProvider)tableViewer.getLabelProvider()).getColumnText(account,1);
			assertEquals(phoneConverter.convertModelToTarget(account.getPhone()),col_phone);
			String col_state = ((ITableLabelProvider)tableViewer.getLabelProvider()).getColumnText(account,2);
			assertEquals(stateConverter.convertModelToTarget(account.getState()),col_state);			
		}					
	}		
}

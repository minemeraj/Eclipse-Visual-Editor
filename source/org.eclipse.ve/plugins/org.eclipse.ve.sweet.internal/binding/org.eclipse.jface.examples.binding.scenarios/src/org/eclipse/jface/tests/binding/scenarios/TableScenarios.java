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

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.binding.BindingException;
import org.eclipse.jface.binding.IConverter;
import org.eclipse.jface.binding.IUpdatable;
import org.eclipse.jface.binding.IUpdatableFactory2;
import org.eclipse.jface.binding.TableBindSpec;
import org.eclipse.jface.binding.TableDescription2;
import org.eclipse.jface.examples.binding.emf.EMFUpdatableTable2;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Event;
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
		getDbc().addUpdatableFactory2(new IUpdatableFactory2() {
			public IUpdatable createUpdatable(Map properties, Object description) {
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
		getDbc().bind2(tableViewer, new TableDescription2(catalog, "accounts",
				new Object[] { "firstName" , "lastName" , "state"}), null);
		
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
	
	public void testScenario02() throws BindingException {
		// Show that a TableViewer with three columns can be used to update columns
		List accounts = catalog.getAccounts();		
		getDbc().bind2(tableViewer, new TableDescription2(catalog, "accounts",
				new Object[] { "firstName" , "lastName" , "state"}), null);
		
		CellEditor[] cellEditors = tableViewer.getCellEditors();
		// Test firstName.  Select the first row
		Account account = (Account)accounts.get(0);
		Event mouseDownEvent = new Event();
		mouseDownEvent.x = 22;
		mouseDownEvent.y = 33;
		tableViewer.getControl().notifyListeners(SWT.MouseDown,mouseDownEvent);		
		TextCellEditor firstNameEditor = (TextCellEditor)cellEditors[0];
		((Text)firstNameEditor.getControl()).setText("Bill");
		((Text)((TextCellEditor)cellEditors[0]).getControl()).notifyListeners(SWT.DefaultSelection,null);
//		assertEquals("Bill",account.getFirstName());
	}	
	
	public void testScenario03() throws BindingException {
		// Show that converters work for table columns
		List accounts = catalog.getAccounts();	
		getDbc().bind2(tableViewer, 
				new TableDescription2(catalog, "accounts",
						new Object[] { "lastName" , "phone" ,"state"}), 
				new TableBindSpec(
						new IConverter[] { null , new PhoneConverter(), new StateConverter()},
						null)		
		);	
		
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

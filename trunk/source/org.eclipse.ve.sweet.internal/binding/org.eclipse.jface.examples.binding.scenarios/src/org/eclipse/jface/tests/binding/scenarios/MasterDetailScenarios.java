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

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.binding.BindingException;
import org.eclipse.jface.binding.IUpdatable;
import org.eclipse.jface.binding.IUpdatableFactory2;
import org.eclipse.jface.binding.IUpdatableValue;
import org.eclipse.jface.binding.ListDescription;
import org.eclipse.jface.binding.swt.SWTBindingConstants;
import org.eclipse.jface.examples.binding.emf.EMFUpdatableTable;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.examples.rcp.adventure.Catalog;
import org.eclipse.ui.examples.rcp.binding.scenarios.SampleData;

/**
 * To run the tests in this class, right-click and select "Run As JUnit Plug-in
 * Test". This will also start an Eclipse instance. To clean up the launch
 * configuration, open up its "Main" tab and select "[No Application] - Headless
 * Mode" as the application to run.
 */

public class MasterDetailScenarios extends ScenariosTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		getDbc().addUpdatableFactory2(new IUpdatableFactory2() {
			public IUpdatable createUpdatable(Object description) {
				if (description instanceof ListDescription) {
					ListDescription listDescription = (ListDescription) description;
					Object object = listDescription.getObject();
					if (object instanceof EObject) {
						return new EMFUpdatableTable((EObject) object,
								(String) listDescription.getPropertyID(),
								new String[] { (String) listDescription
										.getLabelPropertyID() });
					}
				}
				return null;
			}
		});

		// do any setup work here
	}

	protected void tearDown() throws Exception {
		// do any teardown work here
		super.tearDown();
	}

	protected Object getViewerSelection(ContentViewer contentViewer) {
		return ((IStructuredSelection) contentViewer.getSelection())
				.getFirstElement();
	}

	/**
	 * @return the ComboViewer's domain object list
	 */
	protected List getViewerContent(ContentViewer contentViewer) {
		Object[] elements = ((IStructuredContentProvider) contentViewer
				.getContentProvider()).getElements(null);
		if (elements != null)
			return Arrays.asList(elements);
		return null;
	}

	public void testScenario01() throws BindingException {
		// Displaying the catalog's list of Lodging objects in a list viewer,
		// using their names. The name of the currently selected Lodging can
		// be edited in a text widget. There is always a selected Lodging
		// object.
		ListViewer listViewer = new ListViewer(getComposite(), SWT.BORDER);
		listViewer.getList().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, false, false));
		Catalog catalog = SampleData.CATALOG_2005;
		
		getDbc().bind2(listViewer,
				new ListDescription(catalog, "lodgings", "name"), null);
		
		assertEquals(catalog.getLodgings(), getViewerContent(listViewer));
		
		IUpdatableValue selectedLodging = (IUpdatableValue) getDbc()
				.createUpdatable(listViewer, SWTBindingConstants.SELECTION);
		
		selectedLodging.setValue(SampleData.CAMP_GROUND, null);
		
		assertEquals(SampleData.CAMP_GROUND, getViewerSelection(listViewer));
		Text txtName = new Text(getComposite(), SWT.BORDER);
		
		getDbc().bind(txtName, SWTBindingConstants.TEXT, selectedLodging,
				"name");
		
		assertEquals(txtName.getText(), SampleData.CAMP_GROUND.getName());
		txtName.setText("foobar");
		assertEquals("foobar", SampleData.CAMP_GROUND.getName());
		listViewer.setSelection(new StructuredSelection(
				SampleData.FIVE_STAR_HOTEL));
		assertEquals(SampleData.FIVE_STAR_HOTEL, selectedLodging.getValue());
		assertEquals(SampleData.FIVE_STAR_HOTEL.getName(), txtName.getText());
		SampleData.FIVE_STAR_HOTEL.setName("barfoo");
		assertEquals("barfoo", txtName.getText());
	}

	public void testScenario02() throws BindingException {
		// Selecting from the list of lodgings for an adventure and editing the
		// properties of the selected lodging in text widgets. If no lodging is
		// selected the input controls for name and adventure are disabled.
		// There are two buttons "Add" and "Remove"; clicking on "Add" creates a
		// new lodging and selects it so it can be edited, clicking on "Remove"
		// removes the currently selected lodging from the list.
	}

	public void testScenario03() throws BindingException {
		// List adventures and for the selected adventure allow its default
		// lodging’s name and description to be changed in text controls. If
		// there is no selected adventure or the default lodging is null the
		// text controls are disabled. This is a nested property. The default
		// lodging can be changed elsewhere, and the list
	}
}
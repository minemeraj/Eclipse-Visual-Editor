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

import org.eclipse.jface.databinding.BindingException;
import org.eclipse.jface.databinding.ConditionalUpdatableValue;
import org.eclipse.jface.databinding.IUpdatableValue;
import org.eclipse.jface.databinding.PropertyDescription;
import org.eclipse.jface.databinding.swt.SWTBindingConstants;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.examples.rcp.adventure.Adventure;
import org.eclipse.ui.examples.rcp.adventure.AdventureFactory;
import org.eclipse.ui.examples.rcp.adventure.Catalog;
import org.eclipse.ui.examples.rcp.adventure.Category;
import org.eclipse.ui.examples.rcp.adventure.Lodging;
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

		listViewer.setLabelProvider(new LabelProvider() {
			public String getText(Object element) {
				return ((Lodging) element).getName();
			}
		});
		getDbc().bind2(listViewer,
				new PropertyDescription(catalog, "lodgings"), null);

		assertEquals(catalog.getLodgings(), getViewerContent(listViewer));

		IUpdatableValue selectedLodging = (IUpdatableValue) getDbc()
				.createUpdatable2(
						new PropertyDescription(listViewer,
								SWTBindingConstants.SELECTION));

		selectedLodging.setValue(SampleData.CAMP_GROUND);

		assertEquals(SampleData.CAMP_GROUND, getViewerSelection(listViewer));
		Text txtName = new Text(getComposite(), SWT.BORDER);

		getDbc().bind2(
				txtName,
				new PropertyDescription(selectedLodging, "name", String.class,
						Boolean.FALSE), null);

		assertEquals(txtName.getText(), SampleData.CAMP_GROUND.getName());
		enterText(txtName, "foobar");
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
		final ListViewer listViewer = new ListViewer(getComposite(), SWT.BORDER);
		listViewer.getList().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, false, false));
		final Catalog catalog = SampleData.CATALOG_2005;

		listViewer.setLabelProvider(new LabelProvider() {
			public String getText(Object element) {
				return ((Lodging) element).getName();
			}
		});
		getDbc().bind2(listViewer,
				new PropertyDescription(catalog, "lodgings"), null);

		assertEquals(catalog.getLodgings(), getViewerContent(listViewer));

		final IUpdatableValue selectedLodgingUpdatable = (IUpdatableValue) getDbc()
				.createUpdatable2(
						new PropertyDescription(listViewer,
								SWTBindingConstants.SELECTION));

		selectedLodgingUpdatable.setValue(null);
		assertTrue(listViewer.getSelection().isEmpty());

		ConditionalUpdatableValue selectionExistsUpdatable = new ConditionalUpdatableValue(
				selectedLodgingUpdatable) {
			protected boolean compute(Object currentValue) {
				return currentValue != null;
			}
		};

		assertFalse(((Boolean) selectionExistsUpdatable.getValue())
				.booleanValue());

		final Text txtName = new Text(getComposite(), SWT.BORDER);

		getDbc().bind2(
				new PropertyDescription(txtName, SWTBindingConstants.ENABLED),
				selectionExistsUpdatable, null);
		// TODO discuss this - the NestedPropertyDescription is needed so that
		// the type of the nested property is known even if the current value of
		// the given updatable is null at bind time.
		getDbc().bind2(
				new PropertyDescription(txtName, SWTBindingConstants.TEXT),
				new PropertyDescription(selectedLodgingUpdatable, "name",
						String.class, Boolean.FALSE), null);

		assertEquals(txtName.getText(), "");
		assertFalse(txtName.getEnabled());

		final Text txtDescription = new Text(getComposite(), SWT.BORDER);

		getDbc().bind2(
				new PropertyDescription(txtDescription,
						SWTBindingConstants.ENABLED), selectionExistsUpdatable,
				null);
		// TODO discuss this - the NestedPropertyDescription is needed so that
		// the type of the nested property is known even if the current value of
		// the given updatable is null at bind time.
		getDbc().bind2(
				new PropertyDescription(txtDescription,
						SWTBindingConstants.TEXT),
				new PropertyDescription(selectedLodgingUpdatable,
						"description", String.class, Boolean.FALSE), null);

		assertEquals(txtDescription.getText(), "");
		assertFalse(txtDescription.getEnabled());

		Button addButton = new Button(getComposite(), SWT.PUSH);
		addButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				Lodging selectedLodging = (Lodging) selectedLodgingUpdatable
						.getValue();
				int insertionIndex = 0;
				if (selectedLodging != null) {
					insertionIndex = catalog.getLodgings().indexOf(
							selectedLodging);
					assertTrue(insertionIndex >= 0);
				}
				Lodging newLodging = AdventureFactory.eINSTANCE.createLodging();
				int itemCount = listViewer.getList().getItemCount();
				newLodging.setName("new lodging name " + itemCount);
				newLodging.setDescription("new lodging description "
						+ itemCount);
				catalog.getLodgings().add(insertionIndex, newLodging);
				assertEquals(itemCount + 1, listViewer.getList().getItemCount());
				listViewer.setSelection(new StructuredSelection(newLodging));
				assertSame(newLodging, selectedLodgingUpdatable.getValue());
				assertTrue(txtName.getEnabled());
				assertTrue(txtDescription.getEnabled());
				assertEquals(newLodging.getName(), txtName.getText());
				assertEquals(newLodging.getDescription(), txtDescription
						.getText());
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		Button removeButton = new Button(getComposite(), SWT.PUSH);
		removeButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				Lodging selectedLodging = (Lodging) selectedLodgingUpdatable
						.getValue();
				assertNotNull(selectedLodging);
				int deletionIndex = catalog.getLodgings().indexOf(
						selectedLodging);
				assertTrue(deletionIndex >= 0);
				int itemCount = listViewer.getList().getItemCount();
				catalog.getLodgings().remove(deletionIndex);
				assertEquals(itemCount - 1, listViewer.getList().getItemCount());
				assertNull(selectedLodgingUpdatable.getValue());
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		pushButtonWithEvents(addButton);
		pushButtonWithEvents(removeButton);
		pushButtonWithEvents(addButton);
		pushButtonWithEvents(addButton);
		pushButtonWithEvents(removeButton);
	}

	public void testScenario03() throws BindingException {
		// List adventures and for the selected adventure allow its default
		// lodging�s name and description to be changed in text controls. If
		// there is no selected adventure or the default lodging is null the
		// text controls are disabled. This is a nested property. The default
		// lodging can be changed elsewhere, and the list
		final Catalog catalog = SampleData.CATALOG_2005;

		final ListViewer categoryListViewer = new ListViewer(getComposite(),
				SWT.BORDER);
		categoryListViewer.getList().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, false, false));
		categoryListViewer.setLabelProvider(new LabelProvider() {
			public String getText(Object element) {
				return ((Category) element).getName();
			}
		});
		getDbc().bind2(categoryListViewer,
				new PropertyDescription(catalog, "categories"), null);

		assertEquals(catalog.getCategories(),
				getViewerContent(categoryListViewer));

		final IUpdatableValue selectedCategoryUpdatable = (IUpdatableValue) getDbc()
				.createUpdatable2(
						new PropertyDescription(categoryListViewer,
								SWTBindingConstants.SELECTION));

		final ListViewer adventureListViewer = new ListViewer(getComposite(),
				SWT.BORDER);
		adventureListViewer.getList().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, false, false));
		adventureListViewer.setLabelProvider(new LabelProvider() {
			public String getText(Object element) {
				return ((Adventure) element).getName();
			}
		});

		// TODO discuss this - the NestedTableDescription is needed so that
		// the type of the nested property and the types of the columns are
		// known even if the current value of
		// the given updatable is null at bind time.
		getDbc().bind2(
				adventureListViewer,
				new PropertyDescription(selectedCategoryUpdatable,
						"adventures", Adventure.class, Boolean.TRUE), null);

		ConditionalUpdatableValue categorySelectionExistsUpdatable = new ConditionalUpdatableValue(
				selectedCategoryUpdatable) {
			protected boolean compute(Object currentValue) {
				return currentValue != null;
			}
		};

		getDbc().bind2(
				new PropertyDescription(adventureListViewer.getList(),
						SWTBindingConstants.ENABLED),
				categorySelectionExistsUpdatable, null);

		final IUpdatableValue selectedAdventureUpdatable = (IUpdatableValue) getDbc()
				.createUpdatable2(
						new PropertyDescription(adventureListViewer,
								SWTBindingConstants.SELECTION));

		ConditionalUpdatableValue adventureSelectionExistsUpdatable = new ConditionalUpdatableValue(
				selectedAdventureUpdatable) {
			protected boolean compute(Object currentValue) {
				return currentValue != null;
			}
		};

		final Text txtName = new Text(getComposite(), SWT.BORDER);

		getDbc().bind2(
				new PropertyDescription(txtName, SWTBindingConstants.ENABLED),
				adventureSelectionExistsUpdatable, null);
		// TODO discuss this - the NestedPropertyDescription is needed so that
		// the type of the nested property is known even if the current value of
		// the given updatable is null at bind time.
		getDbc().bind2(
				new PropertyDescription(txtName, SWTBindingConstants.TEXT),
				new PropertyDescription(selectedAdventureUpdatable, "name",
						String.class, Boolean.FALSE), null);

		assertEquals(txtName.getText(), "");
		assertFalse(txtName.getEnabled());

		final Text txtDescription = new Text(getComposite(), SWT.BORDER);

		getDbc().bind2(
				new PropertyDescription(txtDescription,
						SWTBindingConstants.ENABLED),
				adventureSelectionExistsUpdatable, null);
		// TODO discuss this - the NestedPropertyDescription is needed so that
		// the type of the nested property is known even if the current value of
		// the given updatable is null at bind time.
		getDbc().bind2(
				new PropertyDescription(txtDescription,
						SWTBindingConstants.TEXT),
				new PropertyDescription(selectedAdventureUpdatable,
						"description", String.class, Boolean.FALSE), null);

		assertFalse(adventureListViewer.getList().isEnabled());
		categoryListViewer.setSelection(new StructuredSelection(
				SampleData.SUMMER_CATEGORY));
		assertTrue(adventureListViewer.getList().isEnabled());
		assertFalse(txtName.getEnabled());
		adventureListViewer.setSelection(new StructuredSelection(
				SampleData.RAFTING_HOLIDAY));
		assertTrue(txtName.getEnabled());
		assertEquals(SampleData.RAFTING_HOLIDAY.getName(), txtName.getText());
		categoryListViewer.setSelection(new StructuredSelection(
				SampleData.WINTER_CATEGORY));
		assertTrue(adventureListViewer.getList().isEnabled());
		assertFalse(txtName.getEnabled());
	}
}
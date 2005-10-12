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

import org.eclipse.jface.binding.BindingException;
import org.eclipse.jface.binding.IConverter;
import org.eclipse.jface.binding.IUpdatableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.examples.rcp.adventure.Adventure;
import org.eclipse.ui.examples.rcp.adventure.AdventureFactory;
import org.eclipse.ui.examples.rcp.adventure.Cart;
import org.eclipse.ui.examples.rcp.binding.scenarios.SampleData;

/**
 * To run the tests in this class, right-click and select "Run As JUnit Plug-in
 * Test". This will also start an Eclipse instance. To clean up the launch
 * configuration, open up its "Main" tab and select "[No Application] - Headless
 * Mode" as the application to run.
 */

public class PropertyScenarios extends ScenariosTestCase {

	private Adventure adventure;

	protected void setUp() throws Exception {
		super.setUp();
		// do any setup work here
		adventure = SampleData.WINTER_HOLIDAY;
	}

	protected void tearDown() throws Exception {
		// do any teardown work here
		super.tearDown();
	}

	public void testScenario01() throws BindingException {
		Text text = new Text(getComposite(), SWT.BORDER);
		getDbs().bindValue(text, "text", adventure, "name");
		// uncomment the following line to see what's happening
		// happening
		// spinEventLoop(1);
		assertEquals(adventure.getName(), text.getText());
		text.setText("foobar");
		// uncomment the following line to see what's happening
		// spinEventLoop(1);
		assertEquals("foobar", adventure.getName());
		adventure.setName("barfoo");
		// uncomment the following line to see what's happening
		// spinEventLoop(1);
		assertEquals("barfoo", text.getText());
	}

	public void testScenario02() throws BindingException {
		// Binding the name property of an Adventure object to the contents of
		// Text controls, no conversion, no validation. The Text widget editable
		// is set to false.by the developer (can not change the name)
		Text text = new Text(getComposite(), SWT.READ_ONLY);
		getDbs().bindValue(text, "text", adventure, "name");
		assertEquals(adventure.getName(), text.getText());
	}

	public void testScenario03() throws BindingException {
		// Binding of a read-only property of an Adventure object to the
		// contents of Text controls, no conversion, no validation. Text control
		// is not editable as a side effect of binding to a read-only property..
		Cart cart = AdventureFactory.eINSTANCE.createCart();
		cart.setAdventureDays(42);
		// bind to the lodgingDays feature, which is read-only and always one
		// less than the number of adventure days.
		Text text = new Text(getComposite(), SWT.BORDER);
		getDbs().bindValue(text, "text", cart, "lodgingDays", new IConverter() {
			public Class getFromType() {
				return String.class;
			}

			public Class getToType() {
				return Integer.class;
			}

			public Object convert(Object object) {
				return new Integer((String) object);
			}
		}, new IConverter() {
			public Class getFromType() {
				return Integer.class;
			}

			public Class getToType() {
				return String.class;
			}

			public Object convert(Object object) {
				return object.toString();
			}
		});
		assertEquals(new Integer(cart.getLodgingDays()).toString(), text
				.getText());
		// TODO API extension needed: getChangeable() and setChangeable() on
		// IUpdatableValue or IUpdatable
		assertEquals(
				"Needs API extension: getChangeable() and setChangeable() on IUpdatableValue or IUpdatable.",
				false, text.getEditable());
	}

	public void testScenario04() throws BindingException {
		// Binding a nested property of an Adventure object to the content of a
		// Text control, no conversion, no validation.
		Text text = new Text(getComposite(), SWT.BORDER);
		// TODO Scenario needs to be more specific - I'm binding to the default
		// lodging's description of an adventure. What do we expect to happen
		// when the default lodging changes? If we expect no change, then this
		// scenario does not introduce anything new. If we expect the binding to
		// be to the new default lodging's description, shouldn't we move this
		// scenario to the master/detail section? I'm assuming the latter for
		// now.
		IUpdatableValue defaultLodging = getDbs().createUpdatableValue(
				adventure, "defaultLodging");
		getDbs().bindValue(text, "text", defaultLodging, "description");

		// test changing the description
		assertEquals(adventure.getDefaultLodging().getDescription(), text
				.getText());
		text.setText("foobar");
		assertEquals("foobar", adventure.getDefaultLodging().getDescription());
		adventure.getDefaultLodging().setDescription("barfoo");
		assertEquals(adventure.getDefaultLodging().getDescription(), text
				.getText());

		// test changing the default lodging
		adventure.setDefaultLodging(SampleData.CAMP_GROUND);
		assertEquals(adventure.getDefaultLodging().getDescription(), text
				.getText());
		adventure.getDefaultLodging().setDescription("barfo");
		assertEquals(adventure.getDefaultLodging().getDescription(), text
				.getText());

		adventure.setDefaultLodging(null);
		assertEquals("", text.getText());

		adventure.setDefaultLodging(SampleData.FIVE_STAR_HOTEL);
		assertEquals(adventure.getDefaultLodging().getDescription(), text
				.getText());
		adventure.getDefaultLodging().setDescription("barf");
		assertEquals(adventure.getDefaultLodging().getDescription(), text
				.getText());

	}
}

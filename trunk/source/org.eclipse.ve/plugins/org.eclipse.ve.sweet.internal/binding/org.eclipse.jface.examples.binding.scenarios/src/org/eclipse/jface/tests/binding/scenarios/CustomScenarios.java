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
import org.eclipse.jface.binding.IUpdatableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.examples.rcp.adventure.Adventure;
import org.eclipse.ui.examples.rcp.binding.scenarios.AggregateUpdatableValue;
import org.eclipse.ui.examples.rcp.binding.scenarios.PriceCentsConverter;
import org.eclipse.ui.examples.rcp.binding.scenarios.PriceDollarsConverter;
import org.eclipse.ui.examples.rcp.binding.scenarios.SampleData;

/**
 * To run the tests in this class, right-click and select "Run As JUnit Plug-in
 * Test". This will also start an Eclipse instance. To clean up the launch
 * configuration, open up its "Main" tab and select "[No Application] - Headless
 * Mode" as the application to run.
 */

public class CustomScenarios extends ScenariosTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		// do any setup work here
	}

	protected void tearDown() throws Exception {
		// do any teardown work here
		super.tearDown();
	}

	public void testScenario01() throws BindingException {
		// Binding the name property of an Adventure object to the contents of
		// Text controls, no conversion, no validation.
	
		Adventure adventure = SampleData.WINTER_HOLIDAY;
		Text text = new Text(getComposite(), SWT.BORDER);

		IUpdatableValue descriptionUpdatable = getDbs().createUpdatableValue(adventure,"description");
		IUpdatableValue nameUpdatable = getDbs().createUpdatableValue(adventure,"name");
		
		AggregateUpdatableValue customUpdatable_comma = new AggregateUpdatableValue( new IUpdatableValue[] {descriptionUpdatable,nameUpdatable} , ",");
		
		getDbs().bindValue(getDbs().createUpdatableValue(text, "text"),customUpdatable_comma);
		// spinEventLoop(1);
		// Make sure that the description on the model match the widget
		assertEquals(adventure.getDescription() + "," + adventure.getName(), text.getText());
		
		// Change the widget to newDescription,newName and ensure the model is updated
		text.setText("newDescription,newName");
		assertEquals("newDescription", adventure.getDescription());
		assertEquals("newName", adventure.getName());		
		
		// Change the model to newDescription_0 and newName_0 and ensure the GUI is updated
		adventure.setDescription("newDescription_0");
		adventure.setName("newName_0");
		assertEquals("newDescription_0,newName_0",text.getText());
		
		// Change text to newDescription_1 with no comma and ensure the model is updated correctly with no name
		text.setText("newDescription_1");
		assertEquals("newDescription_1", adventure.getDescription());
		assertEquals(null,adventure.getName());
		
		// Change text to newName with a preceeding comma and ensure the model is updated correctly with no description
		text.setText(",newName_1");
		assertEquals(null,adventure.getDescription());
		assertEquals("newName_1",adventure.getName());
		
	}

}

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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.examples.rcp.adventure.Adventure;
import org.eclipse.ui.examples.rcp.binding.scenarios.AggregateUpdatableValue;
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
		assertEquals(adventure.getDescription() + "," + adventure.getName(), text.getText());
		text.setText("newDescription,newName");
		assertEquals("newDescription", adventure.getDescription());
		assertEquals("newName", adventure.getName());		
		
		text.setText("newDescription");
		
	}

}

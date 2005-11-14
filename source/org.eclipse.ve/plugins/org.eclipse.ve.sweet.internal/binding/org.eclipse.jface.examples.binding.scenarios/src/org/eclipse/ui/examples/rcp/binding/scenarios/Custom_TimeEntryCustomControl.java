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

import java.util.Map;

import org.eclipse.jface.databinding.BindingException;
import org.eclipse.jface.databinding.IDataBindingContext;
import org.eclipse.jface.databinding.IUpdatable;
import org.eclipse.jface.databinding.IUpdatableFactory;
import org.eclipse.jface.databinding.IValidationContext;
import org.eclipse.jface.databinding.PropertyDescription;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.examples.rcp.adventure.Transportation;

public class Custom_TimeEntryCustomControl extends Composite {

	private TimeEntry timeEntry = null;

	private Label lbl_time = null;

	private TimeEntry timeEntry1 = null;

	public Custom_TimeEntryCustomControl(Composite parent, int style)
			throws BindingException {
		super(parent, style);
		initialize();
	}

	private void initialize() throws BindingException {
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		this.setLayout(new GridLayout());
		createTimeEntry();
		setSize(new Point(300, 200));
		lbl_time = new Label(this, SWT.NONE);
		lbl_time.setLayoutData(gridData);
		createTimeEntry1();
		bind();
	}

	private void bind() throws BindingException {

		IDataBindingContext dbc = SampleData.getSWTtoEMFDatabindingContext(this);

		Transportation bus = SampleData.GREYHOUND_BUS;

		dbc.addUpdatableFactory(new IUpdatableFactory() {
			public IUpdatable createUpdatable(Map properties,
					Object description, IDataBindingContext bindingContext, IValidationContext validationContext)
					throws BindingException {
				if (description instanceof PropertyDescription) {
					PropertyDescription propertyDescription = (PropertyDescription) description;
					if (propertyDescription.getObject() instanceof TimeEntry) {
						if ("time".equals(propertyDescription.getPropertyID())) {
							return new TimeEntryUpdatableValue(
									(TimeEntry) propertyDescription.getObject());
						} else {
							throw new IllegalArgumentException(
									propertyDescription.getPropertyID()
											+ " is unknown feature");
						}
					}
				}
				return null;
			}
		});

		dbc.bind(lbl_time, new PropertyDescription(bus, "arrivalTime"), null);
		dbc.bind(new PropertyDescription(timeEntry, "time"), new PropertyDescription(bus, "arrivalTime"), null);
		dbc.bind(new PropertyDescription(timeEntry1, "time"), new PropertyDescription(bus, "arrivalTime"), null);

	}

	/**
	 * This method initializes timeEntry
	 * 
	 */
	private void createTimeEntry() {
		timeEntry = new TimeEntry(this, SWT.NONE);
	}

	/**
	 * This method initializes timeEntry1
	 * 
	 */
	private void createTimeEntry1() {
		timeEntry1 = new TimeEntry(this, SWT.NONE);
	}

}

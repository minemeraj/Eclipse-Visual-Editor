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

import org.eclipse.jface.databinding.BindingException;
import org.eclipse.jface.databinding.DatabindingContext;
import org.eclipse.jface.databinding.IUpdatable;
import org.eclipse.jface.databinding.IUpdatableFactory;
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

		DatabindingContext dbc = SampleData.getSWTtoEMFDatabindingContext(this);

		Transportation bus = SampleData.GREYHOUND_BUS;

		dbc.addUpdatableFactory(TimeEntry.class, new IUpdatableFactory() {
			public IUpdatable createUpdatable(Object object, Object attribute) {
				if ("time".equals(attribute)) {
					return new TimeEntryUpdatableValue((TimeEntry) object);
				} else {
					throw new IllegalArgumentException(attribute
							+ " is unknown feature");
				}
			}
		});

		dbc.bind2(lbl_time, "text", bus, "arrivalTime",null);
		dbc.bind2(timeEntry, "time", bus, "arrivalTime",null);
		dbc.bind2(timeEntry1, "time", bus, "arrivalTime",null);

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

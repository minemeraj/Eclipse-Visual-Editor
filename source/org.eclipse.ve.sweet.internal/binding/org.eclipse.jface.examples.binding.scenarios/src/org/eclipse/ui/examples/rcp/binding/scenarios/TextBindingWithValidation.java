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

import org.eclipse.jface.databinding.*;
import org.eclipse.jface.databinding.converter.IConverter;
import org.eclipse.jface.databinding.converters.IdentityConverter;
import org.eclipse.jface.databinding.swt.SWTUpdatableFactory;
import org.eclipse.jface.databinding.validator.IValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.examples.rcp.adventure.Adventure;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

public class TextBindingWithValidation extends Composite {

	private IDataBindingContext dbc;

	private Group group = null;

	private Text txtDescription = null;

	private Label label2 = null;

	private Label lateValidationMessage = null;

	private Label latePartialMessage = null;

	private Group group1 = null;

	private Label label3 = null;

	private Text text = null;

	private Label label5 = null;

	private Label label7 = null;

	private Label label6 = null;

	private Label label10 = null;

	private Label earlyValidationMessage = null;

	private Label earlyPartialMessage = null;

	public TextBindingWithValidation(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	/**
	 * This method initializes sShell
	 * 
	 */
	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		setLayout(gridLayout);
		createGroup();
		createGroup1();
		this.setSize(new org.eclipse.swt.graphics.Point(444, 215));
		bind();
	}

	private void bind() {
		dbc = SampleData.getSWTtoEMFDatabindingContext(this);

		Adventure skiTrip = SampleData.WINTER_HOLIDAY;

		IConverter converter = new IdentityConverter(String.class);
		IValidator validator = new IValidator() {
			public String isPartiallyValid(Object value) {
				// Everything must be uppercase
				String stringValue = (String)value;
				if(stringValue.toLowerCase().equals(stringValue)){
					return null;					
				} else {
					return "Only lowercase letters allowed for Description";
				}
			}
			public String isValid(Object value) {
				if (((String) value).length() > 20) {
					return "Description cannot be longer than 20 characters.";
				}
				return null;
			}
		};
		
		//  Bind the group for the TIME_LATE		
		BindSpec bindSpec = new BindSpec(converter,validator);		
		dbc.bind(txtDescription, new Property(skiTrip, "description"), bindSpec);

		IUpdatable errorMsgUpdatable = dbc.createUpdatable(new Property(lateValidationMessage,"text"));
		dbc.bind(errorMsgUpdatable, dbc.getCombinedValidationMessage(),null);
		
		IUpdatable partialErrorMsgUpdatable = dbc.createUpdatable(new Property(latePartialMessage,"text"));
		dbc.bind(partialErrorMsgUpdatable, dbc.getPartialValidationMessage(),null);
		
		//  Bind the group for the TIME_EARLY
		SampleData.getSwtUpdatableFactory().setUpdateTime(IDataBindingContext.TIME_EARLY);		
		dbc.bind(text, new Property(skiTrip, "description"), bindSpec);

		IUpdatable earlyErrorMsgUpdatable = dbc.createUpdatable(new Property(earlyValidationMessage,"text"));
		dbc.bind(earlyErrorMsgUpdatable, dbc.getCombinedValidationMessage(),null);
		
		IUpdatable earlyPartialErrorMsgUpdatable = dbc.createUpdatable(new Property(earlyPartialMessage,"text"));
		dbc.bind(earlyPartialErrorMsgUpdatable, dbc.getPartialValidationMessage(),null);
		
		
	}

	/**
	 * This method initializes group
	 * 
	 */
	private void createGroup() {
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.horizontalSpan = 1;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalSpan = 1;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		gridData3.grabExcessHorizontalSpace = false;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.horizontalSpan = 2;
		gridData2.grabExcessVerticalSpace = false;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		group = new Group(this, SWT.NONE);
		group.setText("TIME_LATE");
		group.setLayout(gridLayout1);
		group.setLayoutData(gridData2);
		label5 = new Label(group, SWT.NONE);
		label5.setText("combined");
		lateValidationMessage = new Label(group, SWT.NONE);
		lateValidationMessage.setForeground(getDisplay().getSystemColor(
				SWT.COLOR_RED));
		lateValidationMessage.setLayoutData(gridData);
		label7 = new Label(group, SWT.NONE);
		label7.setText("partial");
		latePartialMessage = new Label(group, SWT.NONE);
		latePartialMessage.setForeground(getDisplay().getSystemColor(SWT.COLOR_RED));
		latePartialMessage.setLayoutData(gridData1);
		label2 = new Label(group, SWT.NONE);
		label2.setText("Description:");
		txtDescription = new Text(group, SWT.BORDER);
		txtDescription.setLayoutData(gridData3);
	}

	/**
	 * This method initializes group1	
	 *
	 */
	private void createGroup1() {
		GridData gridData5 = new org.eclipse.swt.layout.GridData();
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData4 = new org.eclipse.swt.layout.GridData();
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData7 = new org.eclipse.swt.layout.GridData();
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData7.grabExcessHorizontalSpace = true;
		gridData7.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = GridData.FILL;
		gridData6.grabExcessHorizontalSpace = true;
		gridData6.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 2;
		group1 = new Group(this, SWT.NONE);
		group1.setText("TIME_EARLY");
		group1.setLayoutData(gridData7);
		group1.setLayout(gridLayout2);
		label6 = new Label(group1, SWT.NONE);
		label6.setText("combined");
		earlyValidationMessage = new Label(group1, SWT.NONE);
		earlyValidationMessage.setForeground(getDisplay().getSystemColor(SWT.COLOR_RED));
		earlyValidationMessage.setLayoutData(gridData4);
		label10 = new Label(group1, SWT.NONE);
		label10.setText("partial");
		earlyPartialMessage = new Label(group1, SWT.NONE);
		earlyPartialMessage.setForeground(getDisplay().getSystemColor(SWT.COLOR_RED));
		earlyPartialMessage.setLayoutData(gridData5);
		label3 = new Label(group1, SWT.NONE);
		label3.setText("Description:");
		text = new Text(group1, SWT.BORDER);
		text.setLayoutData(gridData6);
	}
}

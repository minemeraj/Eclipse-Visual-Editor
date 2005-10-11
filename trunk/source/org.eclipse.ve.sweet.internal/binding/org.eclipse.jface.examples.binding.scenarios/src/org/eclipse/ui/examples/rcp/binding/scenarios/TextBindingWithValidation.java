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
/*
 *  $RCSfile: TextBindingWithValidation.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-05 21:34:05 $ 
 */
package org.eclipse.ui.examples.rcp.binding.scenarios;

import org.eclipse.jface.binding.BindingException;
import org.eclipse.jface.binding.DatabindingService;
import org.eclipse.jface.binding.IValidator;
import org.eclipse.jface.binding.IdentityConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.examples.rcp.adventure.Adventure;

public class TextBindingWithValidation extends Composite {

	private DatabindingService dbs;

	private Group group = null;

	private Text txtDescription = null;

	private Label label2 = null;

	private Label label3 = null;

	private Label label5 = null;

	private Text txtName = null;

	private Text txtLocation = null;

	private Label validationMessage = null;

	public TextBindingWithValidation(Composite parent, int style)
			throws BindingException {
		super(parent, style);
		initialize();
	}

	/**
	 * This method initializes sShell
	 * 
	 * @throws BindingException
	 */
	private void initialize() throws BindingException {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		setLayout(gridLayout);
		createGroup();
		this.setSize(new org.eclipse.swt.graphics.Point(444, 215));
		bind();
	}

	private void bind() throws BindingException {
		dbs = SampleData.getSWTtoEMFDatabindingService(this);

		Adventure skiTrip = SampleData.WINTER_HOLIDAY;

		IdentityConverter identity = new IdentityConverter(String.class);
		dbs.bindValue(txtDescription, "text", skiTrip, "description", identity, identity, new IValidator(){
			public String isPartiallyValid(Object value) {
				return isValid(value);
			}
			public String isValid(Object value) {
				if (((String)value).length() > 20) {
					return "Description cannot be longer than 20 characters.";
				}
				return null;
			}});

		dbs.bindValue(txtName, "text", skiTrip, "name");

		dbs.bindValue(txtLocation, "text", skiTrip, "location");
		
		dbs.bindValue(validationMessage, "text", dbs.getCombinedValidationMessage());
	}

	/**
	 * This method initializes group
	 * 
	 */
	private void createGroup() {
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalSpan = 2;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData5 = new org.eclipse.swt.layout.GridData();
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData4 = new org.eclipse.swt.layout.GridData();
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.horizontalSpan = 2;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		group = new Group(this, SWT.NONE);
		group.setText("Winter holiday");
		group.setLayout(gridLayout1);
		group.setLayoutData(gridData2);
		validationMessage = new Label(group, SWT.NONE);
		validationMessage.setForeground(getDisplay().getSystemColor(SWT.COLOR_RED));
		validationMessage.setLayoutData(gridData);
		label2 = new Label(group, SWT.NONE);
		label2.setText("Description:");
		txtDescription = new Text(group, SWT.BORDER);
		txtDescription.setLayoutData(gridData3);
		label3 = new Label(group, SWT.NONE);
		label3.setText("Name:");
		txtName = new Text(group, SWT.BORDER);
		txtName.setLayoutData(gridData4);
		label5 = new Label(group, SWT.NONE);
		label5.setText("Location:");
		txtLocation = new Text(group, SWT.BORDER);
		txtLocation.setLayoutData(gridData5);
	}
}
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
 *  $RCSfile: LabelBinding.java,v $
 *  $Revision: 1.3 $  $Date: 2005-10-18 17:38:36 $ 
 */
package org.eclipse.ui.examples.rcp.binding.scenarios;

import org.eclipse.jface.binding.BindingException;
import org.eclipse.jface.binding.DatabindingService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.examples.rcp.adventure.Adventure;
import org.eclipse.ui.examples.rcp.adventure.AdventurePackage;

public class LabelBinding extends Composite {

	private Label label = null;

	private Text txtName = null;

	private Label lblName = null;

	private DatabindingService dbs;

	private Label label1 = null;

	private Text txtDescription = null;

	private Label lblDescription = null;

	public LabelBinding(Composite parent, int style) throws BindingException {
		super(parent, style);
		initialize();
	}

	private void initialize() throws BindingException {
		GridData gridData8 = new org.eclipse.swt.layout.GridData();
		gridData8.grabExcessHorizontalSpace = true;
		gridData8.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData7 = new org.eclipse.swt.layout.GridData();
		gridData7.grabExcessHorizontalSpace = true;
		gridData7.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		label = new Label(this, SWT.NONE);
		label.setText("Name:");
		txtName = new Text(this, SWT.BORDER);
		txtName.setLayoutData(gridData);
		lblName = new Label(this, SWT.NONE);
		lblName.setText("");
		lblName.setLayoutData(gridData1);
		label1 = new Label(this, SWT.NONE);
		label1.setText("Description:");
		txtDescription = new Text(this, SWT.BORDER);
		txtDescription.setLayoutData(gridData7);
		lblDescription = new Label(this, SWT.NONE);
		lblDescription.setText("");
		lblDescription.setLayoutData(gridData8);
		this.setLayout(gridLayout);
		setSize(new org.eclipse.swt.graphics.Point(367, 168));
		bind();
	}

	private void bind() throws BindingException {
		dbs = SampleData.getSWTtoEMFDatabindingService(this);
		AdventurePackage emfPackage = AdventurePackage.eINSTANCE;

		Adventure skiTrip = SampleData.WINTER_HOLIDAY;

		dbs.bind(txtName, "text", skiTrip, emfPackage.getAdventure_Name());
		dbs.bind(lblName, "text", skiTrip, emfPackage.getAdventure_Name());

		dbs.bind(txtDescription, "text", skiTrip, emfPackage
				.getAdventure_Description());
		dbs.bind(lblDescription, "text", skiTrip, emfPackage
				.getAdventure_Description());

	}

} // @jve:decl-index=0:visual-constraint="10,10"

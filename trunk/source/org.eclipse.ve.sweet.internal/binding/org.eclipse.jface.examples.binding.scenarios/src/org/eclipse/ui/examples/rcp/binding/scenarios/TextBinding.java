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
 *  $RCSfile: TextBinding.java,v $
 *  $Revision: 1.3 $  $Date: 2005-10-18 13:47:39 $ 
 */
package org.eclipse.ui.examples.rcp.binding.scenarios;

import org.eclipse.jface.binding.BindingException;
import org.eclipse.jface.binding.DatabindingService;
import org.eclipse.jface.binding.swt.SWTDatabindingService;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.examples.rcp.adventure.Adventure;
import org.eclipse.ui.examples.rcp.adventure.AdventureFactory;
import org.eclipse.ui.examples.rcp.adventure.AdventurePackage;
import org.eclipse.swt.widgets.Group;

public class TextBinding extends Composite {

	private DatabindingService dbs;
	private Group group = null;
	private Text txtDescription = null;
	private Label label2 = null;
	private Label label3 = null;
	private Label label5 = null;
	private Text txtName = null;
	private Text txtLocation = null;
	private Text txtDescription_1 = null;
	private Text txtName_1 = null;
	private Text txtLocation_1 = null;
	
	public TextBinding(Composite parent, int style) throws BindingException{
		super(parent,style);
		initialize();
	}
	
	/**
	 * This method initializes sShell
	 * @throws BindingException 
	 */
	private void initialize() throws BindingException {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		setLayout(gridLayout);
		createGroup();
		this.setSize(new org.eclipse.swt.graphics.Point(444,215));
		bind();
	}
	private void bind() throws BindingException {
		dbs = SampleData.getSWTtoEMFDatabindingService(this);

		Adventure skiTrip = SampleData.WINTER_HOLIDAY;
		
		dbs.bind(txtDescription,"text",skiTrip,"description");
		dbs.bind(txtDescription_1,"text",skiTrip,"description");		
		
		dbs.bind(txtName,"text",skiTrip,"name");		
		dbs.bind(txtName_1,"text",skiTrip,"name");
		
		dbs.bind(txtLocation,"text",skiTrip,"location");		
		dbs.bind(txtLocation_1,"text",skiTrip,"location");		
				
	}
	/**
	 * This method initializes group	
	 *
	 */
	private void createGroup() {
		GridData gridData8 = new org.eclipse.swt.layout.GridData();
		gridData8.grabExcessHorizontalSpace = true;
		gridData8.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData7 = new org.eclipse.swt.layout.GridData();
		gridData7.grabExcessHorizontalSpace = true;
		gridData7.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData6 = new org.eclipse.swt.layout.GridData();
		gridData6.grabExcessHorizontalSpace = true;
		gridData6.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
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
		gridLayout1.numColumns = 3;
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
		label2 = new Label(group, SWT.NONE);
		label2.setText("Description:");
		txtDescription = new Text(group, SWT.BORDER);
		txtDescription.setLayoutData(gridData3);
		txtDescription_1 = new Text(group, SWT.BORDER);
		txtDescription_1.setLayoutData(gridData8);
		label3 = new Label(group, SWT.NONE);
		label3.setText("Name:");
		txtName = new Text(group, SWT.BORDER);
		txtName.setLayoutData(gridData4);
		txtName_1 = new Text(group, SWT.BORDER);
		txtName_1.setLayoutData(gridData7);
		label5 = new Label(group, SWT.NONE);
		label5.setText("Location:");
		txtLocation = new Text(group, SWT.BORDER);
		txtLocation.setLayoutData(gridData5);
		txtLocation_1 = new Text(group, SWT.BORDER);
		txtLocation_1.setLayoutData(gridData6);
	}
}

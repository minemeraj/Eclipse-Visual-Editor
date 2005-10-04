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
 *  $RCSfile: ListBinding.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-04 13:17:23 $ 
 */
package org.eclipse.jface.examples.binding.scenarios.desired;

import org.eclipse.jface.binding.BindingException;
import org.eclipse.jface.binding.DatabindingService;
import org.eclipse.jface.binding.IUpdatableValue;
import org.eclipse.jface.binding.IdentityConverter;
import org.eclipse.jface.examples.binding.emf.EMFDerivedUpdatableValue;
import org.eclipse.jface.examples.binding.emf.EMFUpdatableTable;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.examples.rcp.adventure.Adventure;
import org.eclipse.ui.examples.rcp.adventure.AdventurePackage;
import org.eclipse.ui.examples.rcp.adventure.Catalog;
import org.eclipse.ui.examples.rcp.adventure.Lodging;

public class ListBinding extends Composite {

	private Label label = null;
	private Label label1 = null;
	private List list = null;
	private Text txtDefaultLodging = null;
	private DatabindingService dbs;
	private ListViewer listViewer;
	private Label label2 = null;
	private Text txtName = null;
	
	public ListBinding(Composite parent, int style) throws BindingException{
		super(parent,style);
		initialize();
	}
	
	private void initialize() throws BindingException {
		setLayout(new GridLayout(2,false));
		GridData gridData6 = new org.eclipse.swt.layout.GridData();
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData6.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.grabExcessHorizontalSpace = false;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		label = new Label(this, SWT.NONE);
		label.setText("Default Lodging");
		this.setSize(new org.eclipse.swt.graphics.Point(336,134));
		createCombo();
		label1 = new Label(this, SWT.NONE);
		label1.setText("Lodging Description:");
		txtDefaultLodging = new Text(this, SWT.BORDER);
		txtDefaultLodging.setLayoutData(gridData);
		label2 = new Label(this, SWT.NONE);
		label2.setText("Lodging Name");
		txtName = new Text(this, SWT.BORDER);
		txtName.setLayoutData(gridData6);
		bind();
	}
	private void bind() throws BindingException{
		dbs = SampleData.getSWTtoEMFDatabindingService(this);	
		AdventurePackage emfPackage = AdventurePackage.eINSTANCE;
		
		Adventure skiTrip = SampleData.WINTER_HOLIDAY;
		Catalog catalog = SampleData.CATALOG_2005;		
		
		dbs.bindContents(listViewer,catalog,"lodgings");
		
		dbs.bindValue(listViewer,"selection",skiTrip,"defaultLodging");		
		
		IUpdatableValue defaultLodging = dbs.createUpdatableValue(skiTrip,"defaultLodging");
		
		dbs.bindContents(txtDefaultLodging, defaultLodging, "description" );
		
		dbs.bindContents(txtName, defaultLodging, "name");
		
	}
	/**
	 * This method initializes combo	
	 *
	 */
	private void createCombo() {
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		list = new List(this, SWT.V_SCROLL | SWT.BORDER);
		list.setLayoutData(gridData1);
		listViewer = new ListViewer(list);
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

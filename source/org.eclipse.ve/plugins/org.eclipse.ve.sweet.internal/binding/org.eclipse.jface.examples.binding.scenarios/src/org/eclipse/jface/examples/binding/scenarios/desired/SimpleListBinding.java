/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies e.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: SimpleListBinding.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-04 13:17:23 $ 
 */
package org.eclipse.jface.examples.binding.scenarios.desired;

import java.net.BindException;

import org.eclipse.jface.binding.BindingException;
import org.eclipse.jface.binding.DatabindingService;
import org.eclipse.jface.binding.IUpdatableValue;
import org.eclipse.jface.binding.IdentityConverter;
import org.eclipse.jface.examples.binding.emf.EMFDerivedUpdatableValue;
import org.eclipse.jface.examples.binding.emf.EMFUpdatableTable;
import org.eclipse.jface.examples.binding.emf.EMFUpdatableValue;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.examples.rcp.adventure.Adventure;
import org.eclipse.ui.examples.rcp.adventure.AdventureFactory;
import org.eclipse.ui.examples.rcp.adventure.AdventurePackage;
import org.eclipse.ui.examples.rcp.adventure.Catalog;
import org.eclipse.ui.examples.rcp.adventure.Lodging;
import org.eclipse.swt.custom.CCombo;

public class SimpleListBinding extends Composite {

	private Label label = null;
	private Label label1 = null;
	private List list = null;
	private Text txtDefaultLodging = null;
	private DatabindingService dbs;
	private ListViewer listViewer;
	private Label label2 = null;
	private Text txtName = null;
	
	public SimpleListBinding(Composite parent, int style) throws BindingException{
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
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		label = new Label(this, SWT.NONE);
		label.setText("Lodgings");
		createCombo();
		label1 = new Label(this, SWT.NONE);
		label1.setText("Description");
		txtDefaultLodging = new Text(this, SWT.BORDER);
		txtDefaultLodging.setLayoutData(gridData);
		label2 = new Label(this, SWT.NONE);
		label2.setText("Name");
		txtName = new Text(this, SWT.BORDER);
		txtName.setLayoutData(gridData6);
			this.setSize(new org.eclipse.swt.graphics.Point(269,133));
		bind();
	}
	private void bind() throws BindingException{
		dbs = SampleData.getSWTtoEMFDatabindingService(this);		
		AdventurePackage emfPackage = AdventurePackage.eINSTANCE;
		
		Catalog catalog = SampleData.CATALOG_2005;		
		
		dbs.bindContents("listViewer",catalog,"lodgings");
		
		IUpdatableValue selectedLodging = dbs.createUpdatableValue(listViewer,"selection");
		
		dbs.bindContents(txtDefaultLodging, selectedLodging, "description");

		dbs.bindContents(txtName, selectedLodging, "name");
		
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

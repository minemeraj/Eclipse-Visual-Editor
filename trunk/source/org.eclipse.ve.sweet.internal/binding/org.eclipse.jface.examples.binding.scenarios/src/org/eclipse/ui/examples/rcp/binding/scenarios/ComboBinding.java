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

import org.eclipse.jface.binding.*;
import org.eclipse.jface.binding.swt.SWTBindingConstants;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.examples.rcp.adventure.*;

public class ComboBinding extends Composite {

	private Label label = null;

	private Label label1 = null;

	private Combo vcombo = null;

	private Text txtDefaultLodging = null;

	private DatabindingContext dbc;

	private ComboViewer comboViewer;
	
	private TabFolder folder;

	private Composite viewerComposite = null;

	private Composite aloneComposite = null;

	private Combo pureCombo = null;

	private Label label2 = null;

	private Text pureComboTxt = null;

	public ComboBinding(Composite parent, int style) throws BindingException {
		super(parent, style);
		initialize();
	}

	private void initialize() throws BindingException {
		
		
		this.setLayout(new FillLayout());
		this.setSize(new Point(300, 200));
		
		folder = new TabFolder(this, SWT.NONE);
		createComposite();
		createComposite2();
		TabItem tabItem = new TabItem(folder, SWT.NONE);
		tabItem.setText("ComboViewer");
		tabItem.setControl(viewerComposite);
		TabItem tabItem5 = new TabItem(folder, SWT.NONE);
		tabItem5.setText("Sand Alone Combo");
		tabItem5.setControl(aloneComposite);
		
		

		bind();
	}

	private void bind() throws BindingException {
		dbc = SampleData.getSWTtoEMFDatabindingContext(this);		

		Adventure skiTrip = SampleData.WINTER_HOLIDAY;
		Catalog catalog = SampleData.CATALOG_2005;
		
		
		// Bind the viewer
		ILabelProvider lp = new LabelProvider() {
			 public String getText(Object element) {
				 return ((Lodging)element).getName();
			 }
		};
		comboViewer.setLabelProvider(lp);
		dbc.bind(comboViewer, SWTBindingConstants.CONTENT, catalog, "lodgings");
		
		dbc.bind(comboViewer,"selection",skiTrip,"defaultLodging");
		
		IUpdatable defLodging = dbc.createUpdatable(skiTrip, "defaultLodging");
		dbc.bind(txtDefaultLodging, "text", defLodging, "name");
		
		
		// bind the combo
		
		 pureCombo.setItems (new String[] { "FairyLand", "TuneLand", "NoWereLand", "TinkerLand", "DreamLand" });
		 Account account = (Account) catalog.getAccounts().get(0);
		 
		 dbc.bind(pureCombo, SWTBindingConstants.SELECTION, account, "country");
		 dbc.bind(pureComboTxt, "text", account, "country");

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
		vcombo = new Combo(viewerComposite, SWT.NONE);
		vcombo.setLayoutData(gridData1);
		comboViewer = new ComboViewer(vcombo);
	}

	/**
	 * This method initializes viewerComposite	
	 *
	 */
	private void createComposite() {
		viewerComposite = new Composite(folder, SWT.NONE);
		viewerComposite.setLayout(new GridLayout());
		
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		viewerComposite.setLayout(gridLayout);

		label = new Label(viewerComposite, SWT.NONE);
		label.setText("Default Lodging");
		createCombo();
		label1 = new Label(viewerComposite, SWT.NONE);
		label1.setText("Default Lodging");
		txtDefaultLodging = new Text(viewerComposite, SWT.BORDER);
		txtDefaultLodging.setLayoutData(gridData);		
	}

	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite2() {
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		aloneComposite = new Composite(folder, SWT.NONE);
		aloneComposite.setLayout(new GridLayout());
		
		Label l = new Label(aloneComposite, SWT.NONE);
		l.setText("States");
		createCombo1();
		label2 = new Label(aloneComposite, SWT.NONE);
		label2.setText("Label");
		pureComboTxt = new Text(aloneComposite, SWT.BORDER);
		pureComboTxt.setLayoutData(gridData3);
		
	}

	/**
	 * This method initializes combo1	
	 *
	 */
	private void createCombo1() {
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		pureCombo = new Combo(aloneComposite, SWT.NONE);
		pureCombo.setLayoutData(gridData2);
	}
}

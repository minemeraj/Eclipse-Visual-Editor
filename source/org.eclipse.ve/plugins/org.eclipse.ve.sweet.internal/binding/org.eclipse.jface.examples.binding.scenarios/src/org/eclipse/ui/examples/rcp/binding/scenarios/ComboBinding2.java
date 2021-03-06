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

import org.eclipse.jface.databinding.IDataBindingContext;
import org.eclipse.jface.databinding.IUpdatable;
import org.eclipse.jface.databinding.Property;
import org.eclipse.jface.databinding.swt.SWTProperties;
import org.eclipse.jface.databinding.viewers.ViewersProperties;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.examples.rcp.adventure.Account;
import org.eclipse.ui.examples.rcp.adventure.Adventure;
import org.eclipse.ui.examples.rcp.adventure.Catalog;
import org.eclipse.ui.examples.rcp.adventure.Lodging;

public class ComboBinding2 extends Composite {

	private Label label = null;

	private Label label1 = null;

	private Combo vcombo = null;

	private Text txtDefaultLodging = null;

	private IDataBindingContext dbc;  //  @jve:decl-index=0:

	private ComboViewer comboViewer;
	
	private TabFolder folder;

	private Composite viewerComposite = null;

	private Composite aloneComposite = null;

	private Combo pureCombo = null;

	private Label label2 = null;

	private Text pureComboTxt = null;

	private Composite twoViewersComposite = null;

	private Combo twoV1 = null;

	private Combo twoV2 = null;

	private Label label3 = null;

	public ComboBinding2(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		
		
		this.setLayout(new FillLayout());
		this.setSize(new Point(300, 200));
		
		folder = new TabFolder(this, SWT.NONE);
		createComposite();
		createComposite2();
		createComposite3();
		TabItem tabItem = new TabItem(folder, SWT.NONE);
		tabItem.setText("ComboViewer");
		tabItem.setControl(viewerComposite);
		TabItem tabItem5 = new TabItem(folder, SWT.NONE);
		tabItem5.setText("Sand Alone Combo");
		tabItem5.setControl(aloneComposite);
		TabItem tabItem1 = new TabItem(folder, SWT.NONE);
		tabItem1.setText("Two Viewers");
		tabItem1.setControl(twoViewersComposite);
		
		

		bind();
	}

	private void bind() {
		dbc = SampleData.getSWTtoEMFDatabindingContext(this);		

		Adventure skiTrip = SampleData.WINTER_HOLIDAY;
		Adventure sameTrip = SampleData.RAFTING_HOLIDAY;
		Catalog catalog = SampleData.CATALOG_2005;
		
		
		// Bind the viewer
		ILabelProvider lp = new LabelProvider() {
			 public String getText(Object element) {
				 return ((Lodging)element).getName();
			 }
		};
		comboViewer.setLabelProvider(lp);
//		dbc.bind(comboViewer, SWTBindingConstants.CONTENT, catalog, "lodgings");
		dbc.bind(comboViewer , new Property(catalog, "lodgings"), null);
		
//		dbc.bind(comboViewer,ViewersProperties.SELECTION,skiTrip,"defaultLodging");
		dbc.bind(new Property(comboViewer,ViewersProperties.SINGLE_SELECTION), new Property(skiTrip,"defaultLodging"), null);
//		IUpdatable defLodging = dbc.createUpdatable(skiTrip, "defaultLodging");
		IUpdatable defLodging = dbc.createUpdatable(new Property(skiTrip, "defaultLodging"));
//		dbc.bind(txtDefaultLodging, "text", defLodging, "name");
		dbc.bind(txtDefaultLodging, new Property(defLodging, "name"), null);
		
		
		// bind the combo
		
		 pureCombo.setItems (new String[] { "FairyLand", "TuneLand", "NoWereLand", "TinkerLand", "DreamLand" });
		 Account account = (Account) catalog.getAccounts().get(0);
		 
//		 dbc.bind(pureCombo, SWTBindingConstants.SELECTION, account, "country");
		 dbc.bind(new Property(pureCombo, SWTProperties.SELECTION), new Property(account, "country"), null);
//		 dbc.bind(pureComboTxt, "text", account, "country");
		 dbc.bind(pureComboTxt, new Property(account, "country"), null);
		 
		 
		 // bind the two viewers
		 ComboViewer cb1 = new ComboViewer(twoV1);
		 ComboViewer cb2 = new ComboViewer(twoV2);
		 
		 cb1.setLabelProvider(lp);
		 cb2.setLabelProvider(lp);
		 		 
//		 dbc.bind(cb1, SWTBindingConstants.CONTENT, catalog, "lodgings");
		 dbc.bind(cb1, new Property(catalog, "lodgings"),null);
//		 dbc.bind(cb1,ViewersProperties.SELECTION,sameTrip,"defaultLodging");
		 dbc.bind(new Property(cb1,ViewersProperties.SINGLE_SELECTION), new Property(sameTrip,"defaultLodging"), null);
		 
//		 dbc.bind(cb2, SWTBindingConstants.CONTENT, catalog, "lodgings");
		 dbc.bind(cb2, new Property(catalog, "lodgings"), null);
//		 dbc.bind(cb2,ViewersProperties.SELECTION,sameTrip,"defaultLodging");
		 dbc.bind(new Property(cb2,ViewersProperties.SINGLE_SELECTION), new Property(sameTrip,"defaultLodging"), null);
		 
		 
		 

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

	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite3() {
		twoViewersComposite = new Composite(folder, SWT.NONE);
		twoViewersComposite.setLayout(new GridLayout());
		label3 = new Label(twoViewersComposite, SWT.NONE);
		label3.setText("Two Viewers bound to the same list:");
		createCombo2();
		createCombo12();
	}

	/**
	 * This method initializes combo	
	 *
	 */
	private void createCombo2() {
		GridData gridData4 = new org.eclipse.swt.layout.GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		twoV1 = new Combo(twoViewersComposite, SWT.NONE);
		twoV1.setLayoutData(gridData4);
	}

	/**
	 * This method initializes combo1	
	 *
	 */
	private void createCombo12() {
		GridData gridData5 = new org.eclipse.swt.layout.GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		twoV2 = new Combo(twoViewersComposite, SWT.NONE);
		twoV2.setLayoutData(gridData5);
	}
}

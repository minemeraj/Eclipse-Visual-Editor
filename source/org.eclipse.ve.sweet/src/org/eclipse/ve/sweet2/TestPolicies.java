/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  Created Aug 10, 2005 by Gili Mendel
 * 
 *  $RCSfile: TestPolicies.java,v $
 *  $Revision: 1.1 $  $Date: 2005-08-11 22:03:11 $ 
 */
package org.eclipse.ve.sweet2;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import org.eclipse.ve.sweet2.hibernate.HibernatePersonHelper;
import org.eclipse.ve.sweet2.hibernate.Person;


public class TestPolicies {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private Group selectedGroup = null;
	private Text selFNText = null;
	private Label selFNLabel = null;
	private Label selLNLable = null;
	private Text selLNText = null;
	private Group group = null;
	private Label mgrFNLabel = null;
	private Text mgrFNText = null;
	private Label label3 = null;
	private Text mgrLNText = null;
	private Group group1 = null;
	private Label spsFNLabel = null;
	private Text spsFNText = null;
	private Label label5 = null;
	private Text spsLNText = null;
	private Button spsUpdateButton = null;
	private Label label9 = null;
	private Label label10 = null;
	private Label label8 = null;
	private Label label = null;
	private Label selObjectFN = null;
	private Label label6 = null;
	private Label selObjectLN = null;
	private Label label1 = null;
	private Label mgrObjectFN = null;
	private Label label12 = null;
	private Label mgrObjectLN = null;
	private Label spsObjectFN = null;
	private Label label17 = null;
	private Label label13 = null;
	private Label spsObjectLN = null;
	private Group databaseGroup = null;
	private Button readbutton = null;
	private List list = null;
	private ListViewer lv = null;
	private Button resetListButton = null;
	private Label validationLabel = null;
	private Button deleteButton = null;
	private Button saveButton = null;
	
	
	private HibernatePersonHelper dbHelper = HibernatePersonHelper.getHelper();
	private java.util.List personList = null;
	private Person selectedPerson = null;
	final IObjectBinder personBinder = ObjectBinder.createObjectBinder(Person.class);

	public TestPolicies() {
		super();
		// TODO Auto-generated constructor stub
	}
		
	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		sShell = new Shell();
		sShell.setText("TestPolicies");
		validationLabel = new Label(sShell, SWT.NONE);
		validationLabel.setText("Validation message");
		createComposite();
		
		createGroup();
		sShell.setLayout(gridLayout);
		sShell.setSize(new org.eclipse.swt.graphics.Point(534,330));
		createGroup2();
		createGroup1();

	}

	/**
	 * This method initializes group	
	 *
	 */
	private void createGroup() {
		GridData gridData7 = new org.eclipse.swt.layout.GridData();
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData7.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData4 = new GridData();
		gridData4.widthHint = 100;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 4;
		selectedGroup = new Group(sShell, SWT.NONE);
		selectedGroup.setText("Person - Modify");
		selectedGroup.setLayoutData(gridData7);
		selectedGroup.setLayout(gridLayout1);
		selFNLabel = new Label(selectedGroup, SWT.NONE);
		selFNLabel.setText("First");
		selFNText = new Text(selectedGroup, SWT.BORDER);
		selLNLable = new Label(selectedGroup, SWT.NONE);
		selLNLable.setText("Last");
		selLNText = new Text(selectedGroup, SWT.BORDER);		
		selLNText.setLayoutData(gridData4);
		label = new Label(selectedGroup, SWT.NONE);
		selObjectFN = new Label(selectedGroup, SWT.NONE);
		selObjectFN.setText("First Name");
		label6 = new Label(selectedGroup, SWT.NONE);
		selObjectLN = new Label(selectedGroup, SWT.NONE);
		selObjectLN.setText("Last Name");
	}

	/**
	 * This method initializes group	
	 *
	 */
	private void createGroup2() {
		GridData gridData5 = new GridData();
		gridData5.widthHint = 100;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 4;
		group = new Group(sShell, SWT.NONE);
		group.setText("Manager - Focus");
		group.setLayout(gridLayout2);
		mgrFNLabel = new Label(group, SWT.NONE);
		mgrFNLabel.setText("First");
		mgrFNText = new Text(group, SWT.BORDER);
		label3 = new Label(group, SWT.NONE);
		label3.setText("Last");
		mgrLNText = new Text(group, SWT.BORDER);
		mgrLNText.setLayoutData(gridData5);
		label1 = new Label(group, SWT.NONE);
		mgrObjectFN = new Label(group, SWT.NONE);
		mgrObjectFN.setText("First Name");
		label12 = new Label(group, SWT.NONE);
		mgrObjectLN = new Label(group, SWT.NONE);
		mgrObjectLN.setText("Last Name");
	}

	/**
	 * This method initializes group1	
	 *
	 */
	private void createGroup1() {
		GridData gridData6 = new GridData();
		gridData6.widthHint = 100;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 4;
		group1 = new Group(sShell, SWT.NONE);
		group1.setText("Spouse - Explicit");
		group1.setLayout(gridLayout3);
		spsFNLabel = new Label(group1, SWT.NONE);
		spsFNLabel.setText("First");
		spsFNText = new Text(group1, SWT.BORDER);
		label5 = new Label(group1, SWT.NONE);
		label5.setText("Last");
		spsLNText = new Text(group1, SWT.BORDER);
		spsLNText.setLayoutData(gridData6);
		label8 = new Label(group1, SWT.NONE);
		spsObjectFN = new Label(group1, SWT.NONE);
		spsObjectFN.setText("First Name");
		label13 = new Label(group1, SWT.NONE);
		spsObjectLN = new Label(group1, SWT.NONE);
		spsObjectLN.setText("Last Name");
		label17 = new Label(group1, SWT.NONE);
		spsUpdateButton = new Button(group1, SWT.NONE);
		spsUpdateButton.setText("Update");
		label9 = new Label(group1, SWT.NONE);
		label10 = new Label(group1, SWT.NONE);
	}

	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite() {
		GridLayout gridLayout4 = new GridLayout();
		gridLayout4.numColumns = 2;
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.grabExcessVerticalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalSpan = 2;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.verticalSpan = 4;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		databaseGroup = new Group(sShell, SWT.NONE);
		databaseGroup.setText("Database");
		databaseGroup.setLayout(gridLayout4);
		databaseGroup.setLayoutData(gridData);
		readbutton = new Button(databaseGroup, SWT.NONE);
		readbutton.setText("Read List");
		readbutton.setLayoutData(gridData3);
		saveButton = new Button(databaseGroup, SWT.NONE);
		saveButton.setText("Save List");
		readbutton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
		
			}
			public void widgetSelected(SelectionEvent e) {
				personList = dbHelper.primGetPersonList();
				lv.setInput(personList);				
			}
		});
		
		list = new List(databaseGroup, SWT.NONE);
		list.setLayoutData(gridData1);
		lv = new ListViewer(list);
		lv.setContentProvider(new IStructuredContentProvider() {	
			java.util.List input;
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				if (newInput instanceof java.util.List)
				    input=(java.util.List)newInput; 
				else
					input=null;
			}		
			public void dispose() {}		
			public Object[] getElements(Object inputElement) {
				if (input!=null)
				   return input.toArray();
				else
					return new Object[0];    
			}		
		});
		lv.setLabelProvider(new LabelProvider());
		lv.addSelectionChangedListener(new ISelectionChangedListener() {		
			public void selectionChanged(SelectionChangedEvent event) {
				personBinder.setSource(((IStructuredSelection)event.getSelection()).getFirstElement());
		//		person = (Person) ((IStructredSelection)event.getSelection()).getFirstElement());
				// tickle the editors??
			}
		});
		
		resetListButton = new Button(databaseGroup, SWT.NONE);
		resetListButton.setText("Reset List");
		resetListButton.setLayoutData(gridData2);
		resetListButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				dbHelper.createSampleEntries();	
				personList = dbHelper.primGetPersonList();
				lv.setInput(personList);				
			}
		});
		deleteButton = new Button(databaseGroup, SWT.NONE);
		deleteButton.setText("Delete Selected");
	}
	
	protected void bind() {
		TextEditor selFNeditor = new TextEditor(selFNText);
		selFNeditor.setContentProvider(personBinder.getPropertyProvider("firstName"));
				
//		ObjectBinder.addPropertyChangedListener(Person.class, listener)
//		ObjectBinder.addPropertyChangedListener(Address.class, listener)
		
		
	}
	
	public static void main(String[] args) {
		Display display = Display.getDefault();
		TestPolicies thisClass = new TestPolicies();
		thisClass.createSShell();
		thisClass.bind();
		thisClass.sShell.open();

		while (!thisClass.sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}

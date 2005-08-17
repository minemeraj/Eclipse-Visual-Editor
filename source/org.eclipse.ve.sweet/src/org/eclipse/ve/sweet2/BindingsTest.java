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
 *  Created Aug 17, 2005 by Gili Mendel
 * 
 *  $RCSfile: BindingsTest.java,v $
 *  $Revision: 1.1 $  $Date: 2005-08-17 18:41:35 $ 
 */
package org.eclipse.ve.sweet2;

import java.util.ArrayList;

import net.sf.cglib.transform.impl.AddStaticInitTransformer;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;

import org.eclipse.ve.sweet2.hibernate.HibernatePersonServicesHelper;
import org.eclipse.ve.sweet2.hibernate.Person;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class BindingsTest {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private Label validationMsgLabel = null;
	private Group viewersGroup = null;
	private Group demainGroup = null;
	private Group databaseGroup = null;
	private Group personGroup = null;
	private Label label1 = null;
	private Text personFNText = null;
	private Label label2 = null;
	private Text personLNText = null;
	private Group managerGroup = null;
	private Label label3 = null;
	private Text managerFNText = null;
	private Label label4 = null;
	private Text managerLNText = null;
	private Group spouseGroup = null;
	private Label label5 = null;
	private Text spouseFNText = null;
	private Label label6 = null;
	private Text spouseLNText = null;
	private Button updateDomainButton = null;
	private List domainList = null;
	private ListViewer domainListViewer = null;
	private Button radioButton = null;
	private Button radioButton1 = null;
	private Button radioButton2 = null;
	private Label label7 = null;
	private Button readDBButton = null;
	private Button writeDBButton = null;
	private Button ResetButton = null;
	private Label label10 = null;
	private Button deleteSelectedButton = null;
	private Label label8 = null;
	private Button noManagerButton = null;
	private Label label9 = null;
	private Label label11 = null;
	private Label label12 = null;
	private Button noSpouseButton = null;
	private Label label13 = null;
	private Label label14 = null;
	private Menu menuBar = null;
	
	private Shell addPersonShell = null;  //  @jve:decl-index=0:visual-constraint="37,367"
	private Label label = null;
	private Text addFNText = null;
	private Label label15 = null;
	private Text addLNText = null;
	private Label label16 = null;
	private Button addOKButton = null;
	private Label label17 = null;
	private Button addCancelButton = null;
	
	
	private HibernatePersonServicesHelper dbHelper = HibernatePersonServicesHelper.getHelper();
	private Person selectedPerson = null;
	final IObjectBinder selectedPersonBinder = ObjectBinder.createObjectBinder(Person.class);

	
	
	
	private void createMenu() {
		
	
        menuBar = new Menu (sShell, SWT.BAR);
        
                
        MenuItem fileMenuItem = new MenuItem (menuBar, SWT.CASCADE);
        fileMenuItem.setText ("File"); 
        
        Menu fileSubMenu = new Menu (sShell, SWT.DROP_DOWN);
        fileMenuItem.setMenu (fileSubMenu);
        
        
        
     	MenuItem newPersonItem = new MenuItem (fileSubMenu, SWT.PUSH);
     	newPersonItem.setText ("New Person");
     	newPersonItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
     		public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
     			if (addPersonShell==null)
     				createAddPersonShell();
     	         addPersonShell.open();
     		}
     		public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {}
     	});
                
     	MenuItem sep = new MenuItem (fileSubMenu, SWT.SEPARATOR);
        
        MenuItem exitItem = new MenuItem (fileSubMenu, SWT.PUSH);        
  	    exitItem.setText ("Exit"); 
        exitItem.setAccelerator (SWT.CTRL + 'X');
        exitItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
        	public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
        		dbHelper.endSession();
        		System.exit(0);
        	}
        	public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
        	}
        });
        
        	        
        
        MenuItem helpMenuItem = new MenuItem (menuBar, SWT.CASCADE);
        helpMenuItem.setText ("Help");
        Menu helpSubMenu = new Menu (sShell, SWT.PUSH);
        	
	}

	/**
	 * This method initializes group	
	 *
	 */
	private void createViewersGroup() {
		GridData gridData13 = new org.eclipse.swt.layout.GridData();
		gridData13.grabExcessHorizontalSpace = true;
		gridData13.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData13.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		viewersGroup = new Group(sShell, SWT.NONE);
		viewersGroup.setText("Viewers model");
		createPersonGroup();
		viewersGroup.setLayout(new GridLayout());
		createManagerGroup();
		viewersGroup.setLayoutData(gridData13);
		createSpouseGroup();
	}
	
	private void createDomainList() {
		GridData gridData5 = new org.eclipse.swt.layout.GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.grabExcessVerticalSpace = true;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.horizontalSpan = 2;
		
		domainList = new List(demainGroup, SWT.NONE);
		domainList.setLayoutData(gridData5);
		
		domainListViewer = new ListViewer(domainList);		
		domainListViewer.setContentProvider(new IStructuredContentProvider() {	
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
		domainListViewer.setLabelProvider(new LabelProvider());
		domainListViewer.addSelectionChangedListener(new ISelectionChangedListener() {		
			public void selectionChanged(SelectionChangedEvent event) {
//				personBinder.setValue(((IStructuredSelection)event.getSelection()).getFirstElement());		
			}
		});
		

	}
	

	/**
	 * This method initializes group1	
	 *
	 */
	private void createDomainGroup() {
		GridData gridData10 = new org.eclipse.swt.layout.GridData();
		gridData10.horizontalSpan = 2;
		GridLayout gridLayout4 = new GridLayout();
		gridLayout4.numColumns = 2;
		GridData gridData9 = new org.eclipse.swt.layout.GridData();
		gridData9.grabExcessVerticalSpace = true;
		gridData9.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData9.grabExcessHorizontalSpace = true;
		gridData9.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData8 = new org.eclipse.swt.layout.GridData();
		gridData8.horizontalIndent = 10;
		GridData gridData7 = new org.eclipse.swt.layout.GridData();
		gridData7.horizontalIndent = 10;
		GridData gridData6 = new org.eclipse.swt.layout.GridData();
		gridData6.horizontalIndent = 10;
		
		demainGroup = new Group(sShell, SWT.NONE);
		demainGroup.setText("Domain Model");
		demainGroup.setLayout(gridLayout4);
		demainGroup.setLayoutData(gridData9);
		
		createDomainList();

		label7 = new Label(demainGroup, SWT.NONE);
		label7.setText("Selected will set:");
		label7.setLayoutData(gridData10);
		radioButton = new Button(demainGroup, SWT.RADIO);
		radioButton.setText("Person");
		radioButton.setSelection(true);
		radioButton.setLayoutData(gridData6);
		label10 = new Label(demainGroup, SWT.NONE);
		radioButton1 = new Button(demainGroup, SWT.RADIO);
		radioButton1.setText("Manager");
		radioButton1.setLayoutData(gridData7);
		deleteSelectedButton = new Button(demainGroup, SWT.NONE);
		deleteSelectedButton.setText("Delete Selected");
		deleteSelectedButton.setToolTipText("Delete the selected person from the domain model");
		radioButton2 = new Button(demainGroup, SWT.RADIO);
		radioButton2.setText("Spouse");
		radioButton2.setLayoutData(gridData8);
	}

	/**
	 * This method initializes group2	
	 *
	 */
	private void createDBGroup() {
		GridData gridData11 = new org.eclipse.swt.layout.GridData();
		gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData11.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		databaseGroup = new Group(sShell, SWT.NONE);
		databaseGroup.setText("Database Conrols");
		databaseGroup.setLayoutData(gridData11);
		databaseGroup.setLayout(new GridLayout());
		readDBButton = new Button(databaseGroup, SWT.NONE);
		readDBButton.setText("Read  Persons List");
		readDBButton.setToolTipText("Reads domain model from DB");
		readDBButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				domainListViewer.setInput(dbHelper.primGetPersonList());				
			}
		});
		writeDBButton = new Button(databaseGroup, SWT.NONE);
		writeDBButton.setText("Write selected Person");
		writeDBButton.setToolTipText("Writes domain model to DB");
		writeDBButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				dbHelper.flushAPerson(selectedPerson);
			}
		});
		ResetButton = new Button(databaseGroup, SWT.NONE);
		ResetButton.setText("Reset  Person's List");
		ResetButton.setToolTipText("Create sample DB and read it");
	}

	/**
	 * This method initializes group3	
	 *
	 */
	private void createPersonGroup() {
		GridData gridData17 = new org.eclipse.swt.layout.GridData();
		gridData17.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData17.grabExcessHorizontalSpace = true;
		gridData17.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData14 = new org.eclipse.swt.layout.GridData();
		gridData14.grabExcessHorizontalSpace = true;
		gridData14.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData14.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData1 = new GridData();
		gridData1.widthHint = 100;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 4;
		personGroup = new Group(viewersGroup, SWT.NONE);
		personGroup.setText("Person - Update on Modify");
		personGroup.setLayoutData(gridData14);
		personGroup.setLayout(gridLayout1);
		label1 = new Label(personGroup, SWT.NONE);
		label1.setText("First");
		personFNText = new Text(personGroup, SWT.BORDER);
		personFNText.setLayoutData(gridData17);
		label2 = new Label(personGroup, SWT.NONE);
		label2.setText("Last");
		personLNText = new Text(personGroup, SWT.BORDER);
		personLNText.setLayoutData(gridData1);
	}

	/**
	 * This method initializes group4	
	 *
	 */
	private void createManagerGroup() {
		GridData gridData18 = new org.eclipse.swt.layout.GridData();
		gridData18.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData18.grabExcessHorizontalSpace = true;
		gridData18.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData15 = new org.eclipse.swt.layout.GridData();
		gridData15.grabExcessHorizontalSpace = true;
		gridData15.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData15.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData2 = new GridData();
		gridData2.widthHint = 100;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 4;
		managerGroup = new Group(viewersGroup, SWT.NONE);
		managerGroup.setText("Manager - Update on Focus lost");
		managerGroup.setLayoutData(gridData15);
		managerGroup.setLayout(gridLayout2);
		label3 = new Label(managerGroup, SWT.NONE);
		label3.setText("First");
		managerFNText = new Text(managerGroup, SWT.BORDER);
		managerFNText.setLayoutData(gridData18);
		label4 = new Label(managerGroup, SWT.NONE);
		label4.setText("Last");
		managerLNText = new Text(managerGroup, SWT.BORDER);
		managerLNText.setLayoutData(gridData2);
		label8 = new Label(managerGroup, SWT.NONE);
		noManagerButton = new Button(managerGroup, SWT.NONE);
		noManagerButton.setText("No Manager");
		noManagerButton.setToolTipText("Clear the Manager for this person");
		label9 = new Label(managerGroup, SWT.NONE);
		label11 = new Label(managerGroup, SWT.NONE);
	}

	/**
	 * This method initializes group5	
	 *
	 */
	private void createSpouseGroup() {
		GridData gridData19 = new org.eclipse.swt.layout.GridData();
		gridData19.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData19.grabExcessHorizontalSpace = true;
		gridData19.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData16 = new org.eclipse.swt.layout.GridData();
		gridData16.grabExcessHorizontalSpace = true;
		gridData16.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData16.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData4 = new org.eclipse.swt.layout.GridData();
		gridData4.horizontalSpan = 4;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData3 = new GridData();
		gridData3.widthHint = 100;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 4;
		spouseGroup = new Group(viewersGroup, SWT.NONE);
		spouseGroup.setText("Spouse - Explicit Update");
		spouseGroup.setLayoutData(gridData16);
		spouseGroup.setLayout(gridLayout3);
		label5 = new Label(spouseGroup, SWT.NONE);
		label5.setText("First");
		spouseFNText = new Text(spouseGroup, SWT.BORDER);
		spouseFNText.setLayoutData(gridData19);
		label6 = new Label(spouseGroup, SWT.NONE);
		label6.setText("Last");
		spouseLNText = new Text(spouseGroup, SWT.BORDER);
		spouseLNText.setLayoutData(gridData3);
		label12 = new Label(spouseGroup, SWT.NONE);
		noSpouseButton = new Button(spouseGroup, SWT.NONE);
		noSpouseButton.setText("No Spouse");
		noSpouseButton.setToolTipText("Clears the spouse for this person");
		label13 = new Label(spouseGroup, SWT.NONE);
		label14 = new Label(spouseGroup, SWT.NONE);
		updateDomainButton = new Button(spouseGroup, SWT.NONE);
		updateDomainButton.setText("Update Domain Model");
		updateDomainButton.setLayoutData(gridData4);
	}



	/**
	 * This method initializes sShell
	 */
	private void createMainShell() {
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalSpan = 3;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		sShell = new Shell();
		sShell.setText("Bindings  Proof of Concept");
		sShell.setLayout(gridLayout);
		sShell.setSize(new org.eclipse.swt.graphics.Point(609,333));
		
		createMenu();
		sShell.setMenuBar(menuBar);
		
		validationMsgLabel = new Label(sShell, SWT.NONE);
		validationMsgLabel.setText("Validation Message");
		validationMsgLabel.setLayoutData(gridData);
		createViewersGroup();
		createDomainGroup();
		createDBGroup();
		
		bind();
	}

	/**
	 * This method initializes sShell1	
	 *
	 */
	private void createAddPersonShell() {
		GridData gridData12 = new GridData();
		gridData12.widthHint = 100;
		GridLayout gridLayout5 = new GridLayout();
		gridLayout5.numColumns = 4;
		addPersonShell = new Shell(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		addPersonShell.setText("Add a new Person");
		addPersonShell.setLayout(gridLayout5);
		addPersonShell.setSize(new org.eclipse.swt.graphics.Point(318,92));
		label = new Label(addPersonShell, SWT.NONE);
		label.setText("First Name");
		addFNText = new Text(addPersonShell, SWT.BORDER);
		label15 = new Label(addPersonShell, SWT.NONE);
		label15.setText("Last Name");
		addLNText = new Text(addPersonShell, SWT.BORDER);
		addLNText.setLayoutData(gridData12);
		label16 = new Label(addPersonShell, SWT.NONE);
		addOKButton = new Button(addPersonShell, SWT.NONE);
		addOKButton.setText("Ok");
		addOKButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				Person p = new Person (addFNText.getText(), addLNText.getText());
				java.util.List pl = (java.util.List) domainListViewer.getInput();
				java.util.List newList;
				if (pl!=null) {
 				  // make sure it is an updatable list
				  newList = new ArrayList(pl.size()+1);
				  newList.addAll(pl);
				}
				else
					newList = new ArrayList(1);
				newList.add(p);
			    domainListViewer.setInput(newList);
				addPersonShell.setVisible(false);
			}
		});
		label17 = new Label(addPersonShell, SWT.NONE);
		addCancelButton = new Button(addPersonShell, SWT.NONE);
		addCancelButton.setText("Cancel");
		addCancelButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				addPersonShell.setVisible(false);
			}
		});
	}
	
	private void bind() {
		TextEditor personFN = new TextEditor(personFNText);
		personFN.setUpdatePolicy(Editor.COMMIT_MODIFY);
		personFN.setContentProvider(selectedPersonBinder.getPropertyProvider("name"));
	}

	/**
	 * @param args
	 * 
	 * @since 1.1.0
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		BindingsTest thisClass = new BindingsTest();
		thisClass.createMainShell();
		thisClass.sShell.open();

		while (!thisClass.sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}

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
 *  $RCSfile: Example_EndToEnd.java,v $
 *  $Revision: 1.6 $  $Date: 2005-09-07 21:14:57 $ 
 */
package org.eclipse.ve.sweet2.examples;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ve.sweet2.*;
import org.eclipse.ve.sweet2.examples.B_Foil_12_ButtonBinding.MySaveAction;
import org.eclipse.ve.sweet2.hibernate.HibernatePersonServicesHelper;
import org.eclipse.ve.sweet2.hibernate.Person;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Label;

public class Example_EndToEnd {

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
	private Group spouseGroup = null;
	private List domainList = null;
	private ListViewer domainListViewer = null;
	private Button readDBButton = null;
	private Button writeDBButton = null;
	private Button resetButton = null;
	private Menu menuBar = null;
	private Button dbDumpButton = null;
	
	private Shell addPersonShell = null;  //  @jve:decl-index=0:visual-constraint="45,581"
	private Label label = null;
	private Text addFNText = null;
	private Label label15 = null;
	private Text addLNText = null;
	private Label label16 = null;
	private Button addOKButton = null;
	private Label label17 = null;
	private Button addCancelButton = null;
	private Button domainDumpButton = null;
	private Label label18 = null;
	private Label label10 = null;
	private Table backupTable = null;
	private Combo managerCombo = null;
	private Text managerFNText = null;
	private Text managerLNText = null;
	private Button button = null;
	private Label label3 = null;
	private List spouseList = null;
	private Label label5 = null;
	private Button button1 = null;
	private Label label8 = null;
	private Group group = null;
	private Label label9 = null;
	private Label label11 = null;
	private Label label12 = null;
	private Text spouseFNText = null;
	private Label label7 = null;
	private Label label13 = null;
	private Label label14 = null;
	private Text spouseLNText = null;
	private Label label19 = null;
	private Button commitSpouseButton = null;
	
	
	private HibernatePersonServicesHelper dbHelper = HibernatePersonServicesHelper.getHelper();
	private Person selectedPerson = null;
	final IObjectDelegate selectedPersonBinder = ObjectDelegate.createObjectBinder(Person.class);
	final IObjectDelegate personList = ObjectDelegate.createObjectBinder(java.util.List.class);
		
	final IObjectDelegate managerPersonBinder = ObjectDelegate.createObjectBinder(Person.class);
	final IObjectDelegate spousePersonBinder = ObjectDelegate.createObjectBinder(Person.class);
	private Label label4 = null;
	private Label label6 = null;
	private Label label20 = null;
	private Label label21 = null;
	private Label label22 = null;
	private Label label23 = null;
	private Label label24 = null;

	
	public class  AddPersonAction extends Action {
		private IObjectDelegate personDelegate;
		public AddPersonAction() {
			setEnabled(false);
			personDelegate = ObjectDelegate.createObjectBinder(Person.class);
			personDelegate.setValue(new Person());
			personDelegate.addPropertyChangeListener(new PropertyChangeListener() {			
				public void propertyChange(PropertyChangeEvent evt) {					
					setEnabled(true);
				}			
			});
		}
		public void run() {			
			java.util.List newList = new ArrayList();
			if (personList.getValue()!=null)
			   newList.addAll((java.util.List)personList.getValue());
			newList.add(personDelegate.getValue());
			personList.setValue(newList);			
			personDelegate.setValue(new Person());
			setEnabled(false);						
			addPersonShell.setVisible(false);
		}
		
		public IObjectDelegate getPersonDelegate() {
			return personDelegate;
		}
	}
	
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
    
        	
	}

	/**
	 * This method initializes group	
	 *
	 */
	private void createViewersGroup() {
		GridData gridData24 = new org.eclipse.swt.layout.GridData();
		gridData24.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData24.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData13 = new org.eclipse.swt.layout.GridData();
		gridData13.grabExcessHorizontalSpace = true;
		gridData13.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData13.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		viewersGroup = new Group(sShell, SWT.NONE);
		viewersGroup.setText("Viewers model");
		label4 = new Label(viewersGroup, SWT.NONE);
		label4.setText("commit on modify");
		label4.setLayoutData(gridData24);
		createPersonGroup();
		viewersGroup.setLayout(new GridLayout());
		createManagerGroup();
		viewersGroup.setLayoutData(gridData13);
		createSpouseGroup();
		createBackupsGroup();
	}
	
	private void createDomainList() {
		GridData gridData5 = new org.eclipse.swt.layout.GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.grabExcessVerticalSpace = true;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.horizontalSpan = 2;
		
		domainList = new List(demainGroup, SWT.V_SCROLL | SWT.BORDER);
		domainList.setLayoutData(gridData5);
		

		

	}
	

	/**
	 * This method initializes group1	
	 *
	 */
	private void createDomainGroup() {
		GridLayout gridLayout4 = new GridLayout();
		gridLayout4.numColumns = 2;
		GridData gridData9 = new org.eclipse.swt.layout.GridData();
		gridData9.grabExcessVerticalSpace = true;
		gridData9.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData9.grabExcessHorizontalSpace = true;
		gridData9.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		
		demainGroup = new Group(sShell, SWT.NONE);
		demainGroup.setText("Domain Model");
		demainGroup.setLayout(gridLayout4);
		demainGroup.setLayoutData(gridData9);
		label18 = new Label(demainGroup, SWT.NONE);
		domainDumpButton = new Button(demainGroup, SWT.NONE);
		domainDumpButton.setText("Dump Domain Content");
		domainDumpButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				dumpPersonList((java.util.List)domainListViewer.getInput());
			}
		});
		
		createDomainList();

		label11 = new Label(demainGroup, SWT.NONE);
		label12 = new Label(demainGroup, SWT.NONE);
		label9 = new Label(demainGroup, SWT.NONE);
		createDBGroup();
		label10 = new Label(demainGroup, SWT.NONE);		
	}
	
	private void dumpPersonList(java.util.List persons) {
		if (persons!=null  && persons.size()>0) {
    	 for (Iterator iter = persons.iterator(); iter.hasNext();) {
			Person p = (Person) iter.next();
			System.out.println("\t"+p);
			System.out.println("\t\tManager: "+p.getManager());
			Person s = p.getSpouse();
			System.out.println("\t\tSpouse: " + s);
			System.out.println("\t\tBackups:");
			if (p.getBackups()!=null)
			   for (Iterator i = p.getBackups().iterator(); i.hasNext();) {
				  System.out.println("\t\t\t"+i.next());
			   }
			else
				System.out.println("\t\t\tNone");
		 }
		}
		else
			System.out.println("No Person records");
	}

	/**
	 * This method initializes group2	
	 *
	 */
	private void createDBGroup() {
		GridData gridData20 = new org.eclipse.swt.layout.GridData();
		gridData20.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData20.grabExcessHorizontalSpace = false;
		gridData20.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData19 = new org.eclipse.swt.layout.GridData();
		gridData19.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData19.grabExcessHorizontalSpace = false;
		gridData19.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData18 = new org.eclipse.swt.layout.GridData();
		gridData18.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData18.grabExcessHorizontalSpace = false;
		gridData18.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData11 = new org.eclipse.swt.layout.GridData();
		gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData11.grabExcessHorizontalSpace = false;
		gridData11.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData10 = new org.eclipse.swt.layout.GridData();
		gridData10.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData10.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		databaseGroup = new Group(demainGroup, SWT.NONE);
		databaseGroup.setText("Database Conrols");
		databaseGroup.setLayoutData(gridData10);
		databaseGroup.setLayout(new GridLayout());
		readDBButton = new Button(databaseGroup, SWT.NONE);
		readDBButton.setText("Get Domain Model");
		readDBButton.setLayoutData(gridData11);
		readDBButton.setToolTipText("Reads domain model from DB");
		readDBButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				runDBService(new Runnable() {				
					public void run() {					
						personList.setValue(dbHelper.getAllPersons());
						domainListViewer.setInput(personList);	
						selectedPerson = null;
						selectedPersonBinder.setValue(selectedPerson);
					}				
				});								
			}
		});
		writeDBButton = new Button(databaseGroup, SWT.NONE);
		writeDBButton.setText("Save Domain Model");
		writeDBButton.setLayoutData(gridData18);
		writeDBButton.setToolTipText("Writes domain model to DB");
		writeDBButton.setEnabled(useDataBase());
		writeDBButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				runDBService(new Runnable() {				
					public void run() {
						dbHelper.savePersonList((java.util.List)personList.getValue());				
					}				
				});										
			}
		});
		resetButton = new Button(databaseGroup, SWT.NONE);
		resetButton.setText("ReCreate Database Content");
		resetButton.setLayoutData(gridData19);
		resetButton.setToolTipText("Create sample DB and read it");
		resetButton.setEnabled(useDataBase());
		resetButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				runDBService(new Runnable() {				
					public void run() {
						dbHelper.createSampleEntries();
						domainListViewer.setInput(dbHelper.getAllPersons());	
						selectedPerson = null;
						selectedPersonBinder.setValue(selectedPerson);
					}				
				});
			}
		});
		dbDumpButton = new Button(databaseGroup, SWT.NONE);
		dbDumpButton.setText("Dump DB Content");
		dbDumpButton.setLayoutData(gridData20);
		dbDumpButton.setEnabled(useDataBase());
		dbDumpButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				runDBService(new Runnable() {				
					public void run() {
						dumpPersonList(dbHelper.getAllPersons());
					}				
				});
		    	
			}
		});
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
		personGroup.setText("Selected Person");
		personGroup.setToolTipText("Immediate Policy");
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
		GridData gridData25 = new org.eclipse.swt.layout.GridData();
		gridData25.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData25.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData8 = new org.eclipse.swt.layout.GridData();
		gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData8.grabExcessHorizontalSpace = true;
		gridData8.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData7 = new org.eclipse.swt.layout.GridData();
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData7.grabExcessHorizontalSpace = true;
		gridData7.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.horizontalSpan = 2;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData15 = new org.eclipse.swt.layout.GridData();
		gridData15.grabExcessHorizontalSpace = true;
		gridData15.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData15.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 2;
		managerGroup = new Group(viewersGroup, SWT.NONE);
		managerGroup.setText("Manager");
		managerGroup.setToolTipText("Focus Lost Policy");
		managerGroup.setLayoutData(gridData15);
		managerGroup.setLayout(gridLayout2);
		label6 = new Label(managerGroup, SWT.NONE);
		label20 = new Label(managerGroup, SWT.NONE);
		label20.setText("commit on focus lost");
		label20.setLayoutData(gridData25);
		managerCombo = new Combo(managerGroup, SWT.BORDER);
		managerCombo.setLayoutData(gridData2);
		managerLNText = new Text(managerGroup, SWT.NONE);
		managerLNText.setLayoutData(gridData7);
		managerFNText = new Text(managerGroup, SWT.NONE);
		button = new Button(managerGroup, SWT.NONE);
		button.setText("No Manager");
		button.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				((Person)selectedPersonBinder.getValue()).setManager(null);
				selectedPersonBinder.refresh(null);
			}
		});
		label3 = new Label(managerGroup, SWT.NONE);
		managerFNText.setLayoutData(gridData8);
	}

	/**
	 * This method initializes group5	
	 *
	 */
	private void createSpouseGroup() {
		GridData gridData26 = new org.eclipse.swt.layout.GridData();
		gridData26.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData26.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData23 = new org.eclipse.swt.layout.GridData();
		gridData23.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData23.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData22 = new org.eclipse.swt.layout.GridData();
		gridData22.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData22.grabExcessHorizontalSpace = true;
		gridData22.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData21 = new org.eclipse.swt.layout.GridData();
		gridData21.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData21.grabExcessHorizontalSpace = true;
		gridData21.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.horizontalSpan = 2;
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData16 = new org.eclipse.swt.layout.GridData();
		gridData16.grabExcessHorizontalSpace = true;
		gridData16.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData16.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 4;
		spouseGroup = new Group(viewersGroup, SWT.NONE);
		spouseGroup.setText("Spouse");
		spouseGroup.setToolTipText("Explicity Commit Policy");
		spouseGroup.setLayoutData(gridData16);
		spouseGroup.setLayout(gridLayout3);
		label21 = new Label(spouseGroup, SWT.NONE);
		label22 = new Label(spouseGroup, SWT.NONE);
		label22.setText("explicit commit");
		label22.setLayoutData(gridData26);
		label23 = new Label(spouseGroup, SWT.NONE);
		label24 = new Label(spouseGroup, SWT.NONE);
		spouseList = new List(spouseGroup, SWT.V_SCROLL);
		spouseList.setLayoutData(gridData3);
		label5 = new Label(spouseGroup, SWT.NONE);
		label14 = new Label(spouseGroup, SWT.NONE);
		spouseLNText = new Text(spouseGroup, SWT.NONE);
		spouseLNText.setLayoutData(gridData22);
		spouseFNText = new Text(spouseGroup, SWT.NONE);
		spouseFNText.setLayoutData(gridData21);
		label7 = new Label(spouseGroup, SWT.NONE);
		label13 = new Label(spouseGroup, SWT.NONE);
		button1 = new Button(spouseGroup, SWT.NONE);
		button1.setText("No Spouse");
		button1.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				((Person)selectedPersonBinder.getValue()).setSpouse(null);
				selectedPersonBinder.refresh(null);
			}
		});
		commitSpouseButton = new Button(spouseGroup, SWT.NONE);
		commitSpouseButton.setText("Commit TextEditors");
		commitSpouseButton.setLayoutData(gridData23);
		
		label8 = new Label(spouseGroup, SWT.NONE);
		label19 = new Label(spouseGroup, SWT.NONE);
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
		sShell.setSize(new org.eclipse.swt.graphics.Point(668,567));
		
		createMenu();
		sShell.setMenuBar(menuBar);
		
		validationMsgLabel = new Label(sShell, SWT.NONE);
		validationMsgLabel.setText("Validation Message");
		validationMsgLabel.setLayoutData(gridData);
		createViewersGroup();
		createDomainGroup();
		sShell.addShellListener(new org.eclipse.swt.events.ShellAdapter() {
			public void shellClosed(org.eclipse.swt.events.ShellEvent e) {
				dbHelper.endSession();
				System.exit(0);
			}
		});
		
		bind();
	}
	
	private void bind_AddNewPerson() {		
		if (addPersonShell==null)
				createAddPersonShell();
		
		// bind the Ok Button
		AddPersonAction addAction = new AddPersonAction(); 
		ButtonAction okButtonAction = new ButtonAction(addOKButton);
		okButtonAction.setAction(addAction);
		
		TextEditor fnEditor = new TextEditor(addFNText);
		fnEditor.setUpdatePolicy(Editor.COMMIT_MODIFY);		
		fnEditor.setContentProvider(new ObjectConsumerProvider("firstName"));
		fnEditor.setInput(addAction.getPersonDelegate());
		
		TextEditor lnEditor = new TextEditor(addLNText);
		lnEditor.setUpdatePolicy(Editor.COMMIT_MODIFY);		
		lnEditor.setContentProvider(new ObjectConsumerProvider("lastName"));
		lnEditor.setInput(addAction.getPersonDelegate());
		
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
						
		label17 = new Label(addPersonShell, SWT.NONE);
		addCancelButton = new Button(addPersonShell, SWT.NONE);
		addCancelButton.setText("Cancel");
		addCancelButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				addPersonShell.setVisible(false);
			}
		});
		
		
		bind_AddNewPerson();
	}
	
	private void bind_Person() {
		// Person
		
		// Binding option B
		TextEditor personFN = new TextEditor(personFNText);
		personFN.setUpdatePolicy(Editor.COMMIT_MODIFY);		
		personFN.setContentProvider(new ObjectConsumerProvider("firstName"));
		personFN.setInput(selectedPersonBinder);

		
		// Binding option A
		TextEditor personLN = new TextEditor(personLNText);
		personLN.setUpdatePolicy(Editor.COMMIT_MODIFY);
		personLN.setContentProvider(new ObjectContentProvider("lastName"));
		personLN.setContentConsumer(new ObjectContentConsumer("lastName"));
		personLN.setInput(selectedPersonBinder);
		personLN.setOutput(selectedPersonBinder);				
	}
	
	private void bind_Manager() {
		// Manager
		
		// Option A
		TextEditor managerFN = new TextEditor(managerFNText);
		managerFN.setUpdatePolicy(Editor.COMMIT_FOCUS);
		managerFN.setContentProvider(new ObjectConsumerProvider("manager.firstName"));
		managerFN.setInput(selectedPersonBinder);
				
		// Option B
		TextEditor managerLN = new TextEditor(managerLNText);
		managerLN.setUpdatePolicy(Editor.COMMIT_FOCUS);
		managerLN.setContentProvider(new ObjectContentProvider("manager.lastName"));
		managerLN.setContentConsumer(new ObjectContentConsumer("manager.lastName"));
		managerLN.setInput(selectedPersonBinder);
		managerLN.setOutput(selectedPersonBinder);
		
		ComboEditor managerEditor = new ComboEditor(managerCombo);
		managerEditor.setContentProvider(new ListContentProvider());		
		managerEditor.setInput(personList);
		managerEditor.setContentConsumer(new ObjectContentConsumer("manager"));		
		managerEditor.setOutput(selectedPersonBinder);
	}
	
	private void bind_Spouse() {
		// Spouse
		
		// Option A
		final TextEditor spouseFN = new TextEditor(spouseFNText);
		spouseFN.setUpdatePolicy(Editor.COMMIT_EXPLICIT);
		spouseFN.setContentProvider(new ObjectConsumerProvider("spouse.firstName"));
		spouseFN.setInput(selectedPersonBinder);

		// Option B
		final TextEditor spouseLN = new TextEditor(spouseLNText);
		spouseLN.setUpdatePolicy(Editor.COMMIT_EXPLICIT);
		spouseLN.setContentProvider(new ObjectContentProvider("spouse.lastName"));
		spouseLN.setContentConsumer(new ObjectContentConsumer("spouse.lastName"));
		spouseLN.setInput(selectedPersonBinder);
		spouseLN.setOutput(selectedPersonBinder);
		
		ListEditor spouseListEditor = new ListEditor(spouseList);
		spouseListEditor.setContentProvider(new ListContentProvider());
		spouseListEditor.setInput(personList);
		spouseListEditor.setContentConsumer(new ObjectContentConsumer("spouse"));
		spouseListEditor.setOutput(selectedPersonBinder);	
		// Explicit Commit
		commitSpouseButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				spouseFN.update();
				spouseLN.update();
			}
		});
	}
	
	private void bind_domainList() {
		// TODO: use ListEditor
		domainListViewer = new ListViewer(domainList);		
		domainListViewer.setContentProvider(new ListContentProvider());
        domainListViewer.setInput(personList);		
		domainListViewer.setLabelProvider(new LabelProvider());
		domainListViewer.addSelectionChangedListener(new ISelectionChangedListener() {		
			public void selectionChanged(SelectionChangedEvent event) {
				selectedPerson = (Person) ((IStructuredSelection)event.getSelection()).getFirstElement();
				selectedPersonBinder.setValue(selectedPerson);
				if (selectedPerson!=null) {
				  managerPersonBinder.setValue(selectedPerson.getManager());
				  spousePersonBinder.setValue(selectedPerson.getSpouse());
				}
			}
		});
	}
	
	private void bind() {
		
		bind_domainList();
		
		selectedPersonBinder.addPropertyChangeListener(new PropertyChangeListener() {		
			public void propertyChange(PropertyChangeEvent evt) {
				domainListViewer.refresh();		
				personList.refresh(null);
				
			}		
		});
		managerPersonBinder.addPropertyChangeListener(new PropertyChangeListener() {		
			public void propertyChange(PropertyChangeEvent evt) {
				domainListViewer.refresh();
				personList.refresh(null);
				
			}		
		});
		spousePersonBinder.addPropertyChangeListener(new PropertyChangeListener() {		
			public void propertyChange(PropertyChangeEvent evt) {
				domainListViewer.refresh();		
				personList.refresh(null);
			}		
		});

		
		
		bind_Person();
		
		bind_Manager();
		
		bind_Spouse();
				
		bind_Backups();
		
	//	bind_AddNewPerson();  will be called when the dialog comes up
		
	}
	
	protected void runDBService (Runnable runnable) {
		BusyIndicator.showWhile(Display.getDefault(), runnable);		
	}

	/**
	 * This method initializes table	
	 *
	 */
	private void createBackupsTable() {
		GridData gridData6 = new org.eclipse.swt.layout.GridData();
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData6.grabExcessHorizontalSpace = true;
		gridData6.grabExcessVerticalSpace = true;
		gridData6.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		backupTable = new Table(group, SWT.NONE);
		backupTable.setHeaderVisible(true);
		backupTable.setLayoutData(gridData6);
		backupTable.setLinesVisible(true);
		TableColumn backupFNCol = new TableColumn(backupTable, SWT.NONE);
		backupFNCol.setWidth(100);
		backupFNCol.setText("First Name");
		TableColumn backupLNCol = new TableColumn(backupTable, SWT.NONE);
		backupLNCol.setWidth(140);
		backupLNCol.setText("Last Name");
	}
		
	
	private void bind_Backups() {
		
		// backups
		TableViewer backupTableViewer = new TableViewer(backupTable);
		backupTableViewer.setContentProvider(new IStructuredContentProvider() {
			Object[] rows = new Object[0];
			IObjectDelegate binder = null;
			Viewer v = null;
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				binder = (IObjectDelegate) newInput;
				v = viewer;
				Person p = (Person)binder.getValue();
				if (p!=null) {
					rows = p.getBackups().toArray();
				}
				binder.addPropertyChangeListener(new PropertyChangeListener(){				
					public void propertyChange(PropertyChangeEvent evt) {
						Person p = (Person)binder.getValue();
						rows = new Object[0];
						if (p!=null) {
							if (p.getBackups()!=null)
							   rows = p.getBackups().toArray();														   
						}
						
						if (v!=null)
							v.refresh();						
					}				
				});
			}		
			public void dispose() {}		
			public Object[] getElements(Object inputElement) {
				return rows;
			}
		});
		backupTableViewer.setLabelProvider(new ITableLabelProvider() {		
			public void removeListener(ILabelProviderListener listener) {}		
			public boolean isLabelProperty(Object element, String property) {				
				return true;
			}
			public void dispose() {}		
			public void addListener(ILabelProviderListener listener) {}		
			public String getColumnText(Object element, int columnIndex) {
				if (element instanceof Person) {
					Person p = (Person)element;
					if (columnIndex==0)
						return p.getFirstName();
					else
						return p.getLastName();
				}
				return "???";
			}		
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}		
		});
		backupTableViewer.setInput(selectedPersonBinder);
	}
	
			

	/**
	 * This method initializes group	
	 *
	 */
	private void createBackupsGroup() {
		GridData gridData4 = new org.eclipse.swt.layout.GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.grabExcessVerticalSpace = true;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		group = new Group(viewersGroup, SWT.NONE);
		group.setText("Backups");
		group.setLayout(new GridLayout());
		createBackupsTable();
		group.setLayoutData(gridData4);
	}

	
	public boolean useDataBase() {
		return System.getProperty("fake") == null; 
	}
	
	/**
	 * @param args
	 * 
	 * @since 1.1.0
	 */
	public static void main(String[] args) {
		
		// Do not use hibernate.  Remove the following line
		// if you have a DB configured, and you want to use hibernate to 
		// persist data
		System.setProperty("fake","true");  
		
		Display display = Display.getDefault();
		Example_EndToEnd thisClass = new Example_EndToEnd();
		thisClass.createMainShell();
		thisClass.sShell.open();

		while (!thisClass.sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}

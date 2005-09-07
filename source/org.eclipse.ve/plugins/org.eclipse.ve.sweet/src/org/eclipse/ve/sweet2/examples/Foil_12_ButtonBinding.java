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
 *  Created Aug 30, 2005 by Gili Mendel
 * 
 *  $RCSfile: Foil_12_ButtonBinding.java,v $
 *  $Revision: 1.5 $  $Date: 2005-09-07 13:11:35 $ 
 */
package org.eclipse.ve.sweet2.examples;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ve.sweet2.*;

public class Foil_12_ButtonBinding {

	private Shell sShell = null;
	private Text firstName = null;
	private Text lastName = null;
	private Button saveButton = null;
	public Foil_12_ButtonBinding() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * 
	 * @since 1.1.0
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/* Before this is run, be sure to set up the launch configuration (Arguments->VM Arguments)
		 * for the correct SWT library path in order to run with the SWT dlls. 
		 * The dlls are located in the SWT plugin jar.  
		 * For example, on Windows the Eclipse SWT 3.1 plugin jar is:
		 *       installation_directory\plugins\org.eclipse.swt.win32_3.1.0.jar
		 */
		Display display = Display.getDefault();
		Foil_12_ButtonBinding thisClass = new Foil_12_ButtonBinding();
		thisClass.createSShell();
		thisClass.sShell.open();

		while (!thisClass.sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.horizontalSpan = 2;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		sShell = new Shell();
		sShell.setText("Shell");
		sShell.setLayout(gridLayout);
		sShell.setSize(new Point(300, 200));
		firstName = new Text(sShell, SWT.BORDER);
		firstName.setLayoutData(gridData);
		lastName = new Text(sShell, SWT.BORDER);
		lastName.setLayoutData(gridData1);
		saveButton = new Button(sShell, SWT.NONE);
		saveButton.setText("Save");
		saveButton.setLayoutData(gridData2);
		
		bind();
	}
	
	class MySaveAction extends Action {
		private Person person;
		public MySaveAction (Person p) {
			super();
			person = p;
			ObjectDelegate.addListener(p.getClass(), new ObjectDelegate.ChangeListener(){			
				public void objectChanged(Object object, String propertyName) {
					if (object == person)
					   setEnabled(true);					
				}			
				public void objectRemove(Object oldObject) {
				}			
				public void objectAdded(Object newObject) {
				}
			
			});
		}
		public void run() {
			System.out.println("\nSave:\n\t"+person.getFirstName()+" "+person.getLastName());
			setEnabled(false);
		}
		
	}
	
	private void bind(){
		final Person santa = Person.CHRIS_CHRINGLE;
				
		TextEditor firstNameEditor = new TextEditor(firstName);
		firstNameEditor.setContentProvider(new ObjectContentProvider("firstName"));
		firstNameEditor.setInput(santa);
		firstNameEditor.setContentConsumer(new ObjectContentConsumer("firstName"));
		firstNameEditor.setOutput(santa);
		
		TextEditor lastNameEditor = new TextEditor(lastName);
		lastNameEditor.setContentProvider(new ObjectContentProvider("lastName"));
		lastNameEditor.setInput(santa);
		lastNameEditor.setContentConsumer(new ObjectContentConsumer("lastName"));
		lastNameEditor.setOutput(santa);
		
		IAction mySave = new MySaveAction(santa);
		ButtonAction saveAction = new ButtonAction(saveButton);
		saveAction.setAction(mySave);
		
		
		
	
	}

}

package org.eclipse.ve.sweet2.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import org.eclipse.ve.sweet.fieldviewer.jface.internal.providers.IntegerLabelConsumer;
import org.eclipse.ve.sweet.fieldviewer.jface.internal.providers.ObjectContentConsumer;
import org.eclipse.ve.sweet.fieldviewer.jface.internal.providers.ObjectContentProvider;
import org.eclipse.ve.sweet3.*;

public class Foil_7_PropertyBinding {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private Label label = null;
	private Text firstNameText = null;
	private Label label1 = null;
	private Text lastNameText = null;
	private Label label2 = null;
	private Text ageText = null;
	private Person person;
	private Text fnameLabel = null;
	private Text lnameLabel = null;
	private Text aLabel = null;
	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData11 = new org.eclipse.swt.layout.GridData();
		gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData11.grabExcessHorizontalSpace = true;
		gridData11.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
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
		sShell = new Shell();
		sShell.setText("Shell");
		sShell.setLayout(gridLayout);
		sShell.setSize(new org.eclipse.swt.graphics.Point(435,163));
		sShell.addShellListener(new org.eclipse.swt.events.ShellAdapter() {
			public void shellClosed(org.eclipse.swt.events.ShellEvent e) {
				System.out.println("Person=" + person.getFirstName() + " " + person.getLastName() + " is " + person.getAge());
			}
		});
		label = new Label(sShell, SWT.NONE);
		label.setText("First Name:");
		firstNameText = new Text(sShell, SWT.BORDER);
		firstNameText.setLayoutData(gridData);
		fnameLabel = new Text(sShell, SWT.READ_ONLY);
		fnameLabel.setLayoutData(gridData11);
		label1 = new Label(sShell, SWT.NONE);
		label1.setText("Last Name:");
		lastNameText = new Text(sShell, SWT.BORDER);
		lastNameText.setLayoutData(gridData1);
		lnameLabel = new Text(sShell, SWT.READ_ONLY);
		lnameLabel.setLayoutData(gridData2);
		label2 = new Label(sShell, SWT.NONE);
		label2.setText("Age:");
		ageText = new Text(sShell, SWT.BORDER);
		aLabel = new Text(sShell, SWT.READ_ONLY);
		aLabel.setLayoutData(gridData3);
		bind();
	}
	private void bind(){
		person = Person.CHRIS_CHRINGLE;
		
		TextEditor firstNameEditor = new TextEditor(firstNameText);
		firstNameEditor.setContentProvider(new ObjectContentProvider("firstName"));
		firstNameEditor.setContentConsumer(new ObjectContentConsumer("firstName"));
		firstNameEditor.setInput(person);
		firstNameEditor.setOutput(person);
		
		TextEditor lastNameEditor = new TextEditor(lastNameText);
		lastNameEditor.setContentProvider(new ObjectContentProvider("lastName"));
		lastNameEditor.setContentConsumer(new ObjectContentConsumer("lastName"));
		lastNameEditor.setInput(person);	
		lastNameEditor.setOutput(person);
				
		TextEditor ageEditor = new TextEditor(ageText);
		ageEditor.setContentProvider(new ObjectContentProvider("age"));
		ageEditor.setInput(person);
		ageEditor.setLabelConsumer(new IntegerLabelConsumer());
		ageEditor.setContentConsumer(new ObjectContentConsumer("age"));
		ageEditor.setOutput(person);
		
		// For demoing the binding
		
		TextEditor firstNameLabel = new TextEditor(fnameLabel);
		firstNameLabel.setContentProvider(new ObjectContentProvider("firstName"));		
		firstNameLabel.setInput(person);
		
		
		TextEditor lastNameLabel = new TextEditor(lnameLabel);
		lastNameLabel.setContentProvider(new ObjectContentProvider("lastName"));
		lastNameLabel.setInput(person);	
				
		TextEditor ageLabel = new TextEditor(aLabel);
		ageLabel.setContentProvider(new ObjectContentProvider("age"));
		ageLabel.setInput(person);
		
		
		
	}
}

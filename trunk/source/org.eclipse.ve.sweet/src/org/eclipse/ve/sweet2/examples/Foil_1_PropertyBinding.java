package org.eclipse.ve.sweet2.examples;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ve.sweet2.IntegerLabelConsumer;
import org.eclipse.ve.sweet2.ObjectContentConsumer;
import org.eclipse.ve.sweet2.ObjectContentProvider;
import org.eclipse.ve.sweet2.Person;
import org.eclipse.ve.sweet2.TextEditor;

public class Foil_1_PropertyBinding {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private Label label = null;
	private Text firstNameText = null;
	private Label label1 = null;
	private Text lastNameText = null;
	private Label label2 = null;
	private Text ageText = null;
	private Person person;
	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
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
		label1 = new Label(sShell, SWT.NONE);
		label1.setText("Last Name:");
		lastNameText = new Text(sShell, SWT.BORDER);
		lastNameText.setLayoutData(gridData1);
		label2 = new Label(sShell, SWT.NONE);
		label2.setText("Age:");
		ageText = new Text(sShell, SWT.BORDER);
		bind();
	}
	private void bind(){
		person = new Person("Chris", "Chringle" , 75);
		
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
		
	}
}

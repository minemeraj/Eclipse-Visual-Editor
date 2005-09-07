package org.eclipse.ve.sweet2.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ve.sweet2.*;

public class A_Foil_8_CustomBinding {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private Text customTextField = null;
	private Text firstName = null;
	private Text lastName = null;
	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridData gridData5 = new org.eclipse.swt.layout.GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData4 = new org.eclipse.swt.layout.GridData();
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		sShell = new Shell();
		sShell.setText("Shell");
		sShell.setLayout(gridLayout);
		sShell.setSize(new org.eclipse.swt.graphics.Point(300,129));
		customTextField = new Text(sShell, SWT.BORDER);
		customTextField.setLayoutData(gridData);
		firstName = new Text(sShell, SWT.BORDER);
		firstName.setLayoutData(gridData4);
		lastName = new Text(sShell, SWT.BORDER);
		lastName.setLayoutData(gridData5);
		sShell.addShellListener(new org.eclipse.swt.events.ShellAdapter() {
			public void shellClosed(org.eclipse.swt.events.ShellEvent e) {
				System.out.println(Person.CHRIS_CHRINGLE.getFirstName() + " " + Person.CHRIS_CHRINGLE.getLastName()); // TODO Auto-generated Event stub shellClosed()
			}
		});
		bind();
	}
	private void bind(){
		
		Person santa = Person.CHRIS_CHRINGLE;
		
		TextEditor firstAndLastName = new TextEditor(customTextField);
		firstAndLastName.setContentProvider(new PersonContentProvider());		
		firstAndLastName.setInput(santa);
		firstAndLastName.setContentConsumer(new PersonContentConsumer());
		firstAndLastName.setOutput(santa);
		
		TextEditor fName = new TextEditor(firstName);
		fName.setContentProvider(new ObjectContentProvider("firstName"));		
		fName.setInput(santa);
		fName.setContentConsumer(new ObjectContentConsumer("firstName"));
		fName.setOutput(santa);
		

		TextEditor lName = new TextEditor(lastName);
		lName.setContentProvider(new ObjectContentProvider("lastName"));		
		lName.setInput(santa);
		lName.setContentConsumer(new ObjectContentConsumer("lastName"));
		lName.setOutput(santa);


		
	}
}

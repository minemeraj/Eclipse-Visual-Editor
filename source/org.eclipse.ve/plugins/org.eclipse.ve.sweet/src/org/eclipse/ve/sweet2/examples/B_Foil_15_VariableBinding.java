package org.eclipse.ve.sweet2.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ve.sweet2.*;

public class B_Foil_15_VariableBinding {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private List employeesList = null;
	private Composite composite = null;
	private Label label = null;
	private Text firstNameText = null;
	private Label label1 = null;
	private Text lastNameText = null;
	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalSpan = 1;
		gridData.widthHint = 120;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		sShell = new Shell();
		sShell.setText("Shell");
		sShell.setLayout(gridLayout);
		sShell.setSize(new org.eclipse.swt.graphics.Point(383,200));
		employeesList = new List(sShell, SWT.BORDER);
		employeesList.setLayoutData(gridData);
		createComposite();
		bind();
	}
	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite() {
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessVerticalSpace = true;
		composite = new Composite(sShell, SWT.NONE);
		composite.setLayoutData(gridData1);
		composite.setLayout(gridLayout1);
		label = new Label(composite, SWT.NONE);
		label.setText("First Name:");
		firstNameText = new Text(composite, SWT.BORDER);
		firstNameText.setLayoutData(gridData2);
		label1 = new Label(composite, SWT.NONE);
		label1.setText("Last Name:");
		lastNameText = new Text(composite, SWT.BORDER);
		lastNameText.setLayoutData(gridData3);
	}
	private void bind(){
		
		IObjectDelegate selectedPerson = ObjectDelegate.createObjectBinder(Person.class);
		
		ListEditor listEditor = new ListEditor(employeesList);
		listEditor.setContentProvider(new ListContentProvider());
		listEditor.setLabelProvider(new PersonLabelProvider());
		listEditor.setInput(Person.getSampleData());
		listEditor.setSelectionService(selectedPerson);
		
		TextEditor firstNameEditor = new TextEditor(firstNameText);
		firstNameEditor.setContentProvider(new ObjectConsumerProvider("firstName"));
		firstNameEditor.setInput(selectedPerson);
		
		TextEditor lastNameEditor = new TextEditor(lastNameText);
		lastNameEditor.setContentProvider(new ObjectConsumerProvider("lastName"));
		lastNameEditor.setInput(selectedPerson);
		
	}
}

package org.eclipse.ve.sweet2.examples;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ve.sweet2.ComboEditor;
import org.eclipse.ve.sweet2.IObjectDelegate;
import org.eclipse.ve.sweet2.ListContentProvider;
import org.eclipse.ve.sweet2.ListEditor;
import org.eclipse.ve.sweet2.ObjectContentConsumer;
import org.eclipse.ve.sweet2.ObjectContentProvider;
import org.eclipse.ve.sweet2.ObjectDelegate;
import org.eclipse.ve.sweet2.TextEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;

public class Foil_16_BindingAcrossTheGUI {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private List employees = null;
	private Composite composite = null;
	private Label label = null;
	private Label label1 = null;
	private Text managerFirstNameText = null;
	private Text firstNameText = null;
	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.widthHint = 125;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		sShell = new Shell();
		sShell.setText("Shell");
		sShell.setLayout(gridLayout);
		sShell.setSize(new org.eclipse.swt.graphics.Point(416,258));
		employees = new List(sShell, SWT.BORDER);
		employees.setLayoutData(gridData);
		createComposite();
		bind();
	}
	private void bind(){
		
		IObjectDelegate selectedPerson = ObjectDelegate.createObjectBinder(Person.class);
		Person jill = Person.JILL;
		
		ListEditor employeesList = new ListEditor(employees);
		employeesList.setContentProvider(new ListContentProvider());
		employeesList.setLabelProvider(new PersonLabelProvider());
		employeesList.setInput(Person.getSampleData());
		employeesList.setOutput(selectedPerson);
		
		TextEditor managerFirstNameEditor = new TextEditor(managerFirstNameText);
		managerFirstNameEditor.setContentProvider(new ObjectContentProvider("manager.firstName"));
		managerFirstNameEditor.setInput(selectedPerson);
		managerFirstNameEditor.setContentConsumer(new ObjectContentConsumer("manager.firstName"));
		managerFirstNameEditor.setOutput(selectedPerson);
		
		TextEditor firstNameEditor = new TextEditor(firstNameText);
		firstNameEditor.setContentProvider(new ObjectContentProvider("firstName"));
		firstNameEditor.setInput(jill);
		firstNameEditor.setContentConsumer(new ObjectContentConsumer("firstName"));
		firstNameEditor.setOutput(jill);
				
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
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		composite = new Composite(sShell, SWT.NONE);
		composite.setLayoutData(gridData1);
		composite.setLayout(gridLayout1);
		label = new Label(composite, SWT.NONE);
		label.setText("Managers first name");
		managerFirstNameText = new Text(composite, SWT.BORDER);
		managerFirstNameText.setLayoutData(gridData2);
		label1 = new Label(composite, SWT.NONE);
		label1.setText("First name");
		firstNameText = new Text(composite, SWT.BORDER);
		firstNameText.setLayoutData(gridData3);
	}

}

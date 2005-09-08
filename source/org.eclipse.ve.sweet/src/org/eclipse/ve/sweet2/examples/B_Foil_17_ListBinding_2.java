package org.eclipse.ve.sweet2.examples;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ve.sweet2.*;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;

public class B_Foil_17_ListBinding_2 {

	private Shell sShell = null;
	private List employeesList = null;
	private List managersList = null;
	private Text managerText = null;
	private Label label = null;
	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridData gridData11 = new org.eclipse.swt.layout.GridData();
		gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData11.grabExcessHorizontalSpace = true;
		gridData11.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		sShell = new Shell();
		sShell.setText("Shell");
		sShell.setLayout(gridLayout);
		sShell.setSize(new Point(300, 200));
		employeesList = new List(sShell, SWT.BORDER);
		employeesList.setLayoutData(gridData);
		managersList = new List(sShell, SWT.BORDER);
		managersList.setLayoutData(gridData1);
		label = new Label(sShell, SWT.NONE);
		label.setText("");
		managerText = new Text(sShell, SWT.READ_ONLY);
		managerText.setLayoutData(gridData11);
		bind();
	}
	private void bind(){
		
		IObjectDelegate selectedEmployee = ObjectDelegate.createObjectBinder(Person.class);
	
		ListEditor employeesListEditor = new ListEditor(employeesList);
		employeesListEditor.setContentProvider(new ListContentProvider());
		employeesListEditor.setLabelProvider(new PersonLabelProvider());
		employeesListEditor.setInput(Person.getSampleData());
		employeesListEditor.setSelectionConsumer(new ObjectSelectionConsumer(selectedEmployee));
		
		ListEditor managersListEditor = new ListEditor(managersList);
		managersListEditor.setContentProvider(new ListContentProvider());
		managersListEditor.setLabelProvider(new PersonLabelProvider());
		managersListEditor.setInput(Person.getSampleData());
		managersListEditor.setSelectionConsumer(new ObjectSelectionConsumer(selectedEmployee,"manager"));

		TextEditor managerTextEditor = new TextEditor(managerText);
		managerTextEditor.setContentProvider(new ObjectContentProvider("manager.firstName"));
		managerTextEditor.setInput(selectedEmployee);
		
	}
}

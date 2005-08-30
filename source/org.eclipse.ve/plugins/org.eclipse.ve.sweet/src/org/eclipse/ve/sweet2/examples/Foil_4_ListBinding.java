package org.eclipse.ve.sweet2.examples;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ve.sweet2.ListContentProvider;
import org.eclipse.ve.sweet2.ListEditor;
import org.eclipse.ve.sweet2.ObjectContentConsumer;

public class Foil_4_ListBinding {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private List managerList = null;
	private Person person;

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		sShell = new Shell();
		sShell.setText("Shell");
		sShell.setLayout(new GridLayout());
		sShell.setSize(new org.eclipse.swt.graphics.Point(225,242));
		sShell.addShellListener(new org.eclipse.swt.events.ShellAdapter() {
			public void shellClosed(org.eclipse.swt.events.ShellEvent e) {
				System.out.println("Tooth Fairy manager=" + person.getManager().getFirstName()); // TODO Auto-generated Event stub shellClosed()
			}
		});
		managerList = new List(sShell, SWT.NONE);
		managerList.setLayoutData(gridData);
		bind();
	}
	private void bind(){
		
		person = Person.TOOTH_FAIRY;
				
		ListEditor employees = new ListEditor(managerList);
		employees.setContentProvider(new ListContentProvider());
		employees.setLabelProvider(new PersonLabelProvider());
		employees.setInput(Person.getSampleData());
		employees.setContentConsumer(new ObjectContentConsumer("manager"));
		employees.setOutput(person);
		
	}

}

package org.eclipse.ve.sweet2.examples;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ve.sweet2.ComboEditor;
import org.eclipse.ve.sweet2.ListContentProvider;
import org.eclipse.ve.sweet2.ObjectContentConsumer;

public class Foil_3_ComboBinding {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private Combo managerCombo = null;

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		sShell = new Shell();
		sShell.setText("Shell");
		sShell.setLayout(new GridLayout());
		createManagerCombo();
		sShell.setSize(new org.eclipse.swt.graphics.Point(230,125));
		sShell.addShellListener(new org.eclipse.swt.events.ShellAdapter() {
			public void shellClosed(org.eclipse.swt.events.ShellEvent e) {
				if(Person.JOHN.getManager() != null){
					System.out.println("Manager=" + Person.JOHN.getManager().getFirstName());
				} else {
					System.out.println("Manager=Unset");					
				}
			}
		});
	}

	/**
	 * This method initializes managerCombo	
	 *
	 */
	private void createManagerCombo() {
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		managerCombo = new Combo(sShell, SWT.NONE);
		managerCombo.setLayoutData(gridData);
		bind();
	}
	private void bind(){
		
		ComboEditor managerEditor = new ComboEditor(managerCombo);
		managerEditor.setContentProvider(new ListContentProvider("backups"));
		managerEditor.setLabelProvider(new PersonLabelProvider());		
		managerEditor.setInput(Person.BIG_CHEESE);
		managerEditor.setContentConsumer(new ObjectContentConsumer("manager"));		
		managerEditor.setOutput(Person.JOHN);
		
	}

}

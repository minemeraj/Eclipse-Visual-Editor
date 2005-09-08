package org.eclipse.ve.sweet2.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ve.sweet2.*;

import org.eclipse.swt.widgets.Text;

public class B_Foil_9_ComboBinding {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private Combo managerCombo = null;
	private Text bossLabel = null;

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		sShell = new Shell();
		sShell.setText("Shell");
		sShell.setLayout(new GridLayout());
		createManagerCombo();
		sShell.setSize(new org.eclipse.swt.graphics.Point(230,125));
		bossLabel = new Text(sShell, SWT.READ_ONLY);
		bossLabel.setLayoutData(gridData1);
		sShell.addShellListener(new org.eclipse.swt.events.ShellAdapter() {
			public void shellClosed(org.eclipse.swt.events.ShellEvent e) {
				if(Person.JOHN.getManager() != null){
					System.out.println("Manager=" + Person.JOHN.getManager().getFirstName());
				} else {
					System.out.println("Manager=Unset");					
				}
			}
		});
		
		bind();
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
		
	}
	private void bind(){
		
		Person bigBoss = Person.BIG_CHEESE;
		Person needManager = Person.JOHN;
		
		ComboEditor managerEditor = new ComboEditor(managerCombo);
		managerEditor.setContentProvider(new ListContentProvider("backups"));
		managerEditor.setLabelProvider(new PersonLabelProvider());		
		managerEditor.setInput(bigBoss);		
		managerEditor.setSelectionConsumer(new ObjectSelectionConsumer(needManager,"manager"));		
		
		
		TextEditor needBossEditor = new TextEditor(bossLabel);
		needBossEditor.setContentProvider(new ObjectContentProvider("manager.firstName"));		
		needBossEditor.setInput(needManager);
		
		
	}

}

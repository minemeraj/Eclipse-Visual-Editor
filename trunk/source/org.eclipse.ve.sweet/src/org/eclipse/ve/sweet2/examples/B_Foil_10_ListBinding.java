package org.eclipse.ve.sweet2.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ve.sweet2.*;

public class B_Foil_10_ListBinding {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private List managerList = null;
	private Person person;
	private Text bossLabel = null;

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridData gridData6 = new org.eclipse.swt.layout.GridData();
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData6.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		sShell = new Shell();
		sShell.setText("Shell");
		sShell.setLayout(new GridLayout());
		sShell.setSize(new org.eclipse.swt.graphics.Point(225,242));
		managerList = new List(sShell, SWT.NONE);
		managerList.setLayoutData(gridData);
		bossLabel = new Text(sShell, SWT.READ_ONLY);
		bossLabel.setLayoutData(gridData6);
		bind();
	}
	private void bind(){
				
		
		Person bigBoss = Person.BIG_CHEESE;
		Person needManager = Person.JOHN;
				
		ListEditor employees = new ListEditor(managerList);
		employees.setContentProvider(new ListContentProvider("backups"));
		employees.setLabelProvider(new PersonLabelProvider());
		employees.setInput(bigBoss);
		employees.setSelectionConsumer(new ObjectSelectionConsumer(needManager, "manager"));		
		
		TextEditor needBossEditor = new TextEditor(bossLabel);
		needBossEditor.setContentProvider(new ObjectContentProvider("manager.firstName"));		
		needBossEditor.setInput(needManager);
		
	}

}

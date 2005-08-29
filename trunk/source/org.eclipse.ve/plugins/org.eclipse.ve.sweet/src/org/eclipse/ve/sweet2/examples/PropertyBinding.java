package org.eclipse.ve.sweet2.examples;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.layout.GridData;

public class PropertyBinding {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private Label label = null;
	private Text firstNameText = null;
	private Label label1 = null;
	private Text lastNameText = null;
	private Label label2 = null;
	private Spinner ageSpinner = null;

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
		sShell.setSize(new org.eclipse.swt.graphics.Point(433,316));
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
		ageSpinner = new Spinner(sShell, SWT.BORDER);
	}

}

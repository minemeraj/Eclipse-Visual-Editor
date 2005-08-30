package org.eclipse.ve.sweet2.examples;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ve.sweet2.TextEditor;
import org.eclipse.swt.widgets.Button;

public class Foil_2_CustomBinding {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private Label label = null;
	private Text customTextField = null;
	private Button closeButton = null;
	private Label label1 = null;

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		sShell = new Shell();
		sShell.setText("Shell");
		sShell.setLayout(gridLayout);
		sShell.setSize(new org.eclipse.swt.graphics.Point(300,129));
		sShell.addShellListener(new org.eclipse.swt.events.ShellAdapter() {
			public void shellClosed(org.eclipse.swt.events.ShellEvent e) {
				System.out.println(Person.CHRIS_CHRINGLE.getFirstName() + " " + Person.CHRIS_CHRINGLE.getLastName()); // TODO Auto-generated Event stub shellClosed()
			}
		});
		label = new Label(sShell, SWT.NONE);
		label.setText("Label");
		customTextField = new Text(sShell, SWT.BORDER);
		customTextField.setLayoutData(gridData);
		closeButton = new Button(sShell, SWT.NONE);
		closeButton.setText("Close");
		closeButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				sShell.close();
			}
		});
		label1 = new Label(sShell, SWT.NONE);
		bind();
	}
	private void bind(){
		
		TextEditor firstAndLastName = new TextEditor(customTextField);
		firstAndLastName.setContentProvider(new PersonContentProvider());		
		firstAndLastName.setInput(Person.CHRIS_CHRINGLE);
		firstAndLastName.setContentConsumer(new PersonContentConsumer());
		firstAndLastName.setOutput(Person.CHRIS_CHRINGLE);


		
	}
}

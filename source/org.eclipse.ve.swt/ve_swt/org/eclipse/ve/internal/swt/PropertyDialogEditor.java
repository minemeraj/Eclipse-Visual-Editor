package org.eclipse.ve.internal.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class PropertyDialogEditor extends Dialog {
	
	protected IProject myProject = null;	
	protected PropertyEditor chooser;
	protected String initString;
	
	public PropertyDialogEditor(Shell parentShell, PropertyEditor aChooser, IProject project) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.MIN | SWT.MAX | SWT.RESIZE);
		myProject = project;
		chooser = aChooser;
	}
	public Object getValue(){
		return chooser.getValue();
	}
	/**
	 * Store the initString before the widgets are disposed
	 */
	public boolean close() {
		initString = chooser.getJavaInitializationString();
		return super.close();
	}
	
	public String getJavaInitializationString(){
		return initString;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		// Ensure the OK button is only enabled when there is an actual value to return
		final Button okButton = getButton(IDialogConstants.OK_ID);
		okButton.setEnabled(false);
		chooser.addPropertyChangeListener(new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent evt) {
			 okButton.setEnabled(chooser.getValue() != null);
			}
		});
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent,SWT.NONE);
		GridLayout grid = new GridLayout();
		grid.numColumns = 1;
		grid.verticalSpacing = 5;
		composite.setLayout(grid);
					
		Control c = chooser.createControl(composite, SWT.NONE);
		GridData gd1 = new GridData();
		gd1.grabExcessHorizontalSpace = true;
		gd1.grabExcessVerticalSpace = true;
		gd1.horizontalAlignment = GridData.FILL;
		gd1.verticalAlignment = GridData.FILL;
		c.setLayoutData(gd1);
		
		return composite;
	}
}

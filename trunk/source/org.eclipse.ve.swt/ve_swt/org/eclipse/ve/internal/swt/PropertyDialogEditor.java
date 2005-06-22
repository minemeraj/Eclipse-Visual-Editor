/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class PropertyDialogEditor extends Dialog {
	
	protected IProject myProject = null;	
	protected PropertyEditor chooser;
	protected String initString;
	
	protected PropertyChangeListener changeListener;
	
	public PropertyDialogEditor(Shell parentShell, PropertyEditor aChooser, IProject project) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.MIN | SWT.MAX | SWT.RESIZE);
		myProject = project;
		chooser = aChooser;
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(SWTMessages.PropertyDialogEditor_dialog_title); 
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
		changeListener = new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent evt) {
				okButton.setEnabled(chooser.getValue() != null);
			}
		};
		chooser.addPropertyChangeListener(changeListener);
		okButton.addDisposeListener(new DisposeListener(){
			public void widgetDisposed(DisposeEvent e) {
				if (chooser != null) {
					chooser.removePropertyChangeListener(changeListener);
				}
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
		applyDialogFont(composite);
		return composite;
	}
}

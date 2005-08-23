/*
 * Copyright (C) 2005 by David Orme  <djo@coconut-palm-software.com>
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     David Orme - Initial API and implementation
 */
package org.eclipse.ve.sweet.objecteditor.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ve.sweet.objecteditor.ObjectEditor;

public class ObjectEditorTest {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private Composite composite = null;
	private Button button = null;
	private Button button1 = null;
	private Button button2 = null;
	private Blotter blotter = null;
	private ObjectEditor objectEditor = null;
	private Button button3 = null;
	private Button button4 = null;
	private Label label = null;
	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 6;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		composite = new Composite(sShell, SWT.NONE);
		composite.setLayoutData(gridData);
		composite.setLayout(gridLayout);
		composite.setTabList(new Control[] {});
		button3 = new Button(composite, SWT.LEFT | SWT.ARROW);
		button4 = new Button(composite, SWT.ARROW | SWT.RIGHT);
		label = new Label(composite, SWT.NONE);
		label.setText("|");
		button = new Button(composite, SWT.NONE);
		button.setText("Clients");
		button1 = new Button(composite, SWT.NONE);
		button1.setText("Work");
		button2 = new Button(composite, SWT.NONE);
		button2.setText("Billing");
	}

	/**
	 * This method initializes blotter	
	 *
	 */
	private void createBlotter() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.marginHeight = 15;
		gridLayout2.marginWidth = 15;
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessVerticalSpace = true;
		blotter = new Blotter(sShell, SWT.NONE);
		blotter.setLayoutData(gridData1);
		blotter.setLayout(gridLayout2);
		createObjectEditor();
	}

	/**
	 * This method initializes objectEditor	
	 *
	 */
	private void createObjectEditor() {
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		objectEditor = new ObjectEditor(blotter, SWT.NONE);
		objectEditor.setLayoutData(gridData2);
	}

	/**
	 * Method main.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		ObjectEditorTest thisClass = new ObjectEditorTest();
		thisClass.createSShell();
		thisClass.sShell.open();

		while (!thisClass.sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.marginHeight = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.marginWidth = 0;
		sShell = new Shell();
		sShell.setText("Object Browser");
		createComposite();
		createBlotter();
		sShell.setLayout(gridLayout1);
	}

}

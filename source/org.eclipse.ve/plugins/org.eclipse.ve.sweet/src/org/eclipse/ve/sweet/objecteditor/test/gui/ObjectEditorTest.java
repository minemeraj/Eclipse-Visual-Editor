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
package org.eclipse.ve.sweet.objecteditor.test.gui;

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
	private Composite toolBar = null;
	private Button clients = null;
	private Button work = null;
	private Button billing = null;
	private Blotter blotter = null;
	private ObjectEditor objectEditor = null;
	private Button back = null;
	private Button forward = null;
	private Label label = null;
	
	/**
	 * This method initializes the tool bar	
	 *
	 */
	private void createToolBar() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 6;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		toolBar = new Composite(sShell, SWT.NONE);
		toolBar.setLayoutData(gridData);
		toolBar.setLayout(gridLayout);
		toolBar.setTabList(new Control[] {});
		back = new Button(toolBar, SWT.LEFT | SWT.ARROW);
		back.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				back();
			}
		});
		forward = new Button(toolBar, SWT.ARROW | SWT.RIGHT);
		forward.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				forward();
			}
		});
		label = new Label(toolBar, SWT.NONE);
		label.setText("|");
		clients = new Button(toolBar, SWT.NONE);
		clients.setText("Clients");
		clients.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				showClients();
			}
		});
		work = new Button(toolBar, SWT.NONE);
		work.setText("Work");
		work.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				showWork();
			}
		});
		billing = new Button(toolBar, SWT.NONE);
		billing.setText("Billing");
		billing.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				showBilling();
			}
		});
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
		createToolBar();
		createBlotter();
		sShell.setLayout(gridLayout1);
	}
	
	// Event handlers -----------------------------------------------------------------------------

	protected void back() {
		// TODO Auto-generated method stub
		
	}

	protected void forward() {
		// TODO Auto-generated method stub
		
	}

	protected void showClients() {
		// TODO Auto-generated method stub
		
	}

	protected void showWork() {
		// TODO Auto-generated method stub
		
	}

	protected void showBilling() {
		// TODO Auto-generated method stub
		
	}

	// Method Main... -----------------------------------------------------------------------------
	
	/**
	 * Method main.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		ObjectEditorTest oet = new ObjectEditorTest();
		oet.createSShell();
		oet.sShell.open();

		while (!oet.sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}

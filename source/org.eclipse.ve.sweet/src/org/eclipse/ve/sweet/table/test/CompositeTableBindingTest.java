/*
 * Copyright (C) 2005 David Orme <djo@coconut-palm-software.com>
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     David Orme     - Initial API and implementation
 */
package org.eclipse.ve.sweet.table.test;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ve.sweet.objectviewer.IObjectViewer;
import org.eclipse.ve.sweet.objectviewer.ObjectViewerFactory;
import org.eclipse.ve.sweet.objectviewer.pojo.JavaObjectViewerFactory;
import org.eclipse.ve.sweet.table.CompositeTable;

public class CompositeTableBindingTest {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private CompositeTable table = null;
	private Header header = null;
	private Row row = null;
	
	// A model object to edit ---------------------------------------------------------------
	
	private Model model;
	
	public CompositeTableBindingTest() {
		model = new Model();
	}

	/**
	 * This method initializes the table
	 */
	private void createTable() {
		table = new CompositeTable(sShell, SWT.BORDER);
		table.setRunTime(true);
		table.setWeights(new int[] {35, 35, 20, 10});
		createHeader();
		createRow();
	}
	
	/**
	 * This method initializes header	
	 *
	 */
	private void createHeader() {
		header = new Header(table, SWT.NONE);
	}

	/**
	 * This method initializes row	
	 *
	 */
	private void createRow() {
		row = new Row(table, SWT.NONE);
	}

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		// Create the Shell...
		sShell = new Shell();
		sShell.setText("Shell");
		sShell.setLayout(new FillLayout());
		createTable();
		sShell.setSize(new org.eclipse.swt.graphics.Point(445,243));
		
		// Now bind the table to the PersonList property of the model
		row.name.setData("ColumnBinding", "Name");
		row.address.setData("ColumnBinding", "Address");
		row.city.setData("ColumnBinding", "City");
		row.state.setData("ColumnBinding", "State");
        ObjectViewerFactory.factory = new JavaObjectViewerFactory();
        final IObjectViewer personEditor = ObjectViewerFactory.edit(model);
        personEditor.bind(table, "PersonList");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		CompositeTableBindingTest thisClass = new CompositeTableBindingTest();
		thisClass.createSShell();
		thisClass.sShell.open();

		while (!thisClass.sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}

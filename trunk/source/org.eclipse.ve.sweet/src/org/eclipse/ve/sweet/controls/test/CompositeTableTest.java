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
package org.eclipse.ve.sweet.controls.test;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ve.sweet.controls.CompositeTable;
import org.eclipse.ve.sweet.controls.IDeleteHandler;
import org.eclipse.ve.sweet.controls.IInsertHandler;
import org.eclipse.ve.sweet.controls.IRowContentProvider;
import org.eclipse.ve.sweet.controls.IRowListener;

public class CompositeTableTest {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private CompositeTable table = null;
	private Header header = null;
	private Row row = null;
	
	private LinkedList personList = new LinkedList();
	
	public CompositeTableTest() {
		personList.add(new Person("John", "1234", "Wheaton", "IL"));
		personList.add(new Person("Jane", "1234", "Wheaton", "IL"));
		personList.add(new Person("Frank", "1234", "Wheaton", "IL"));
		personList.add(new Person("Joe", "1234", "Wheaton", "IL"));
		personList.add(new Person("Chet", "1234", "Wheaton", "IL"));
		personList.add(new Person("Wilbur", "1234", "Wheaton", "IL"));
		personList.add(new Person("Elmo", "1234", "Wheaton", "IL"));
	}

	/**
	 * This method initializes multiRowViewer	
	 *
	 */
	private void createMultiRowViewer() {
		table = new CompositeTable(sShell, SWT.NONE);
		table.setRunTime(true);
		table.setWeights(new int[] {35, 35, 20, 10});
		table.addRowContentProvider(rowContentProvider);
		table.addDeleteHandler(deleteHandler);
		table.addInsertHandler(insertHandler);
		table.addRowListener(rowListener);
		table.setNumRowsInCollection(personList.size());
		createHeader();
		createRow();
	}
	
	private IRowContentProvider rowContentProvider = new IRowContentProvider() {
		public void refresh(CompositeTable table, int currentObjectOffset, Control row) {
			Row rowObj = (Row) row;
			Person person = (Person)personList.get(currentObjectOffset);
			rowObj.name.setText(person.name);
			rowObj.address.setText(person.address);
			rowObj.city.setText(person.city);
			rowObj.state.setText(person.state);
		}
	};

	private IDeleteHandler deleteHandler = new IDeleteHandler() {
		public boolean canDelete(int rowInCollection) {
			return true;
		}
		public void deleteRow(int rowInCollection) {
			personList.remove(rowInCollection);
		}
	};
	
	private IInsertHandler insertHandler = new IInsertHandler() {
		public int insert(int positionHint) {
			Person newPerson = new Person();
			personList.add(positionHint, newPerson);
			return positionHint;
		}
	};
	
	private IRowListener rowListener = new IRowListener() {
		public boolean requestRowChange(CompositeTable sender, int currentObjectOffset, Control row) {
			return true;
		}
		public void depart(CompositeTable sender, int currentObjectOffset, Control row) {
			
		}
		public void arrive(CompositeTable sender, int currentObjectOffset, Control row) {
			Person person = (Person)personList.get(currentObjectOffset);
			Row rowObj = (Row) row;
			person.name = rowObj.name.getText();
			person.address = rowObj.address.getText();
			person.city = rowObj.city.getText();
			person.state = rowObj.state.getText();
		}
	};
	
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
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		CompositeTableTest thisClass = new CompositeTableTest();
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
		sShell = new Shell();
		sShell.setText("Shell");
		sShell.setLayout(new FillLayout());
		createMultiRowViewer();
		sShell.setSize(new org.eclipse.swt.graphics.Point(445,243));
	}

}

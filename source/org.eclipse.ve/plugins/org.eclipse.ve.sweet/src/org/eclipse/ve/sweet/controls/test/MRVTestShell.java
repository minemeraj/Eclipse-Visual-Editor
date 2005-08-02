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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ve.sweet.controls.IRefreshContentProvider;
import org.eclipse.ve.sweet.controls.MultiRowViewer;

public class MRVTestShell {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private MultiRowViewer multiRowViewer = null;
	private Header header = null;
	private Row row = null;

	/**
	 * This method initializes multiRowViewer	
	 *
	 */
	private void createMultiRowViewer() {
		multiRowViewer = new MultiRowViewer(sShell, SWT.NONE);
		multiRowViewer.setNumRowsInCollection(15);
		multiRowViewer.setRunTime(true);
		multiRowViewer.setWeights(new int[] {35, 35, 20, 10});
		multiRowViewer.addRefreshContentProvider(refreshContentProvider);
		createHeader();
		createRow();
	}
	
	private IRefreshContentProvider refreshContentProvider = new IRefreshContentProvider() {
		public void refresh(MultiRowViewer mrc, int offsetFromTopRow, Control row) {
			Row rowObj = (Row) row;
			rowObj.name.setText(Integer.toString(mrc.getTopRow() + offsetFromTopRow));
		}
	};

	/**
	 * This method initializes header	
	 *
	 */
	private void createHeader() {
		header = new Header(multiRowViewer, SWT.NONE);
	}

	/**
	 * This method initializes row	
	 *
	 */
	private void createRow() {
		row = new Row(multiRowViewer, SWT.NONE);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		MRVTestShell thisClass = new MRVTestShell();
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
		sShell.setSize(new org.eclipse.swt.graphics.Point(445,242));
	}

}

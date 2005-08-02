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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ve.sweet.controls.MultiRowViewer;


public class MRVTest extends Composite {

	private MultiRowViewer multiRowViewer = null;
	private Header header = null;
	private Row row = null;

	public MRVTest(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
        this.setLayout(new FillLayout());
        createMultiRowViewer();
        this.setSize(new org.eclipse.swt.graphics.Point(348,201));
	}

	/**
	 * This method initializes multiRowViewer	
	 *
	 */
	private void createMultiRowViewer() {
		multiRowViewer = new MultiRowViewer(this, SWT.NONE);
		createHeader();
		createRow();
	}

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


}  //  @jve:decl-index=0:visual-constraint="10,10"

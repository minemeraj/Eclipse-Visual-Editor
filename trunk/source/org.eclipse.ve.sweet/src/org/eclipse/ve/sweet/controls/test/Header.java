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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class Header extends Composite {

	private Label label = null;
	private Label label1 = null;
	private Label label2 = null;
	private Label label3 = null;

	public Header(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		label = new Label(this, SWT.NONE);
		label.setBounds(new org.eclipse.swt.graphics.Rectangle(13,20,31,17));
		label.setText("Name");
		label1 = new Label(this, SWT.NONE);
		label1.setBounds(new org.eclipse.swt.graphics.Rectangle(73,19,31,17));
		label1.setText("Address");
		label2 = new Label(this, SWT.NONE);
		label2.setBounds(new org.eclipse.swt.graphics.Rectangle(117,19,31,17));
		label2.setText("City");
		label3 = new Label(this, SWT.NONE);
		label3.setBounds(new org.eclipse.swt.graphics.Rectangle(170,22,31,17));
		label3.setText("State");
	}

}

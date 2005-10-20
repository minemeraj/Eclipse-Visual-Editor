/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.examples.rcp.binding.scenarios;

import org.eclipse.jface.binding.BindingException;
import org.eclipse.jface.binding.DatabindingContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.examples.rcp.adventure.Adventure;
import org.eclipse.ui.examples.rcp.adventure.AdventureFactory;
import org.eclipse.ui.examples.rcp.adventure.AdventurePackage;

public class NumberBinding {

	private Shell sShell = null; // @jve:decl-index=0:visual-constraint="10,10"

	private Label label = null;

	private Text txtPrice = null;

	private DatabindingContext dbc;

	private Text lblPrice = null;

	private Label label1 = null;

	private Text txtPrice_2 = null;

	private Label label2 = null;

	/**
	 * This method initializes sShell
	 * 
	 * @throws BindingException
	 */
	private void createSShell() throws BindingException {
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		gridData3.grabExcessHorizontalSpace = false;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		sShell = new Shell();
		sShell.setText("Shell");
		sShell.setLayout(gridLayout);
		sShell.setSize(new org.eclipse.swt.graphics.Point(388, 248));
		label = new Label(sShell, SWT.NONE);
		label.setText("Price:");
		txtPrice = new Text(sShell, SWT.BORDER);
		txtPrice.setLayoutData(gridData);
		lblPrice = new Text(sShell, SWT.READ_ONLY);
		lblPrice.setLayoutData(gridData2);
		label1 = new Label(sShell, SWT.NONE);
		label1.setText("");
		txtPrice_2 = new Text(sShell, SWT.BORDER);
		txtPrice_2.setLayoutData(gridData3);
		label2 = new Label(sShell, SWT.NONE);
		bind();
	}

	private void bind() throws BindingException {
		dbc = SampleData.getSWTtoEMFDatabindingContext(sShell);
		AdventureFactory emfFactory = AdventurePackage.eINSTANCE
				.getAdventureFactory();
		AdventurePackage emfPackage = AdventurePackage.eINSTANCE;

		Adventure skiTrip = SampleData.WINTER_HOLIDAY;

		dbc.bind(txtPrice, "text", skiTrip, emfPackage.getAdventure_Price());
		dbc.bind(lblPrice, "text", skiTrip, emfPackage.getAdventure_Price());
		dbc.bind(txtPrice_2, "text", skiTrip, emfPackage.getAdventure_Price());

	}

}

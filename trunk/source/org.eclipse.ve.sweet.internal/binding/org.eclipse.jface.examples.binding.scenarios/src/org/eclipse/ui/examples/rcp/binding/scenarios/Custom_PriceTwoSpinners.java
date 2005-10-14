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
/*
 *  $RCSfile: Custom_PriceTwoSpinners.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-14 14:04:56 $ 
 */
package org.eclipse.ui.examples.rcp.binding.scenarios;

import org.eclipse.jface.binding.BindingException;
import org.eclipse.jface.binding.DatabindingService;
import org.eclipse.jface.binding.IConverter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.examples.rcp.adventure.Adventure;

public class Custom_PriceTwoSpinners extends Composite {

	private Label label = null;
	private Label lblPrice = null;
	private Label label2 = null;
	private Text txtPrice = null;
	private Spinner spin_Dollars = null;
	private Label label4 = null;
	private Label label3 = null;
	private Spinner spin_Cents = null;
	public Custom_PriceTwoSpinners(Composite parent, int style) throws BindingException {
		super(parent, style);
		initialize();
	}

	private void initialize() throws BindingException {
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.grabExcessHorizontalSpace = false;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		label = new Label(this, SWT.NONE);
		label.setText("Price:");
		lblPrice = new Label(this, SWT.NONE);
		lblPrice.setLayoutData(gridData);
		label2 = new Label(this, SWT.NONE);
		txtPrice = new Text(this, SWT.BORDER);
		label4 = new Label(this, SWT.NONE);
		label4.setText("Dollars:");
		spin_Dollars = new Spinner(this, SWT.NONE);
		spin_Dollars.setMaximum(1000000);
		spin_Dollars.setLayoutData(gridData1);
		label3 = new Label(this, SWT.NONE);
		label3.setText("Cents:");
		spin_Cents = new Spinner(this, SWT.NONE);
		spin_Cents.setMaximum(99);
		spin_Cents.setLayoutData(gridData2);
		this.setLayout(gridLayout);
		setSize(new Point(300, 200));
		bind();
	}
	private void bind() throws BindingException{
		DatabindingService dbs = SampleData.getSWTtoEMFDatabindingService(this);
		
		final Adventure skiTrip = SampleData.WINTER_HOLIDAY;
		
		dbs.bindValue(lblPrice,"text",skiTrip,"price");
		dbs.bindValue(txtPrice,"text",skiTrip,"price");
				
		dbs.bindValue(
				dbs.createUpdatableValue(spin_Dollars,"selection"),
				dbs.createUpdatableValue(skiTrip, "price"),				
				new IConverter(){
					public Class getFromType() { return Integer.TYPE; }
					public Class getToType() { return Double.TYPE; }
					public Object convert(Object object) {
						// Argument is an Integer representing the dollar portion.  Add to cents to make the new price
						Double oldPrice = new Double(skiTrip.getPrice());
						double cents = oldPrice.doubleValue() - oldPrice.intValue();
						double newPrice = cents + ((Integer)object).intValue();
						return new Double(newPrice);
					}
				},
				new IConverter(){
						public Class getFromType() { return Double.TYPE; }
						public Class getToType() { return Integer.TYPE; }
						public Object convert(Object object) {
							// Return the dollar portion only
							Double price = (Double)object;
							int dollars = price.intValue();							
							return new Integer(dollars);
						}					
					}					
				);	
		
		dbs.bindValue(
				dbs.createUpdatableValue(spin_Cents,"selection"),
				dbs.createUpdatableValue(skiTrip, "price"),				
				new IConverter(){
					public Class getFromType() { return Integer.TYPE; }
					public Class getToType() { return Double.TYPE; }
					public Object convert(Object object) {
						// Argument is an Integer representing the cents portion.  Add to dollars to make the new price
						Double oldPrice = new Double(skiTrip.getPrice());
						double dollars = oldPrice.intValue();
						double newPrice = dollars + ((Integer)object).doubleValue()/100;
						return new Double(newPrice);
					}
				},
				new IConverter(){
						public Class getFromType() { return Double.TYPE; }
						public Class getToType() { return Integer.TYPE; }
						public Object convert(Object object) {
							// Return the cents portion only							
							Double price = (Double)object;
							double cents = price.doubleValue() - price.intValue();	
							cents = cents * 100;
							return new Integer((int)cents);
						}					
					}					
				);			
	}
}

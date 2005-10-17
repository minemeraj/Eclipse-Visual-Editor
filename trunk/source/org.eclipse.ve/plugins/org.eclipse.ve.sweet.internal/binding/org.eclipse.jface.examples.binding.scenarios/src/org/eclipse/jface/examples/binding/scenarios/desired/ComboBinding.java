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
 *  $RCSfile: ComboBinding.java,v $
 *  $Revision: 1.2 $  $Date: 2005-10-17 23:06:29 $ 
 */
package org.eclipse.jface.examples.binding.scenarios.desired;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.jface.binding.*;
import org.eclipse.jface.binding.BindingException;
import org.eclipse.jface.binding.DatabindingService;
import org.eclipse.jface.binding.IUpdatableValue;
import org.eclipse.jface.binding.IdentityConverter;
import org.eclipse.jface.examples.binding.emf.*;
import org.eclipse.jface.examples.binding.emf.EMFDerivedUpdatableValue;
import org.eclipse.jface.examples.binding.emf.EMFUpdatableTable;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.examples.rcp.adventure.Adventure;
import org.eclipse.ui.examples.rcp.adventure.AdventurePackage;
import org.eclipse.ui.examples.rcp.adventure.Catalog;
import org.eclipse.ui.examples.rcp.adventure.Lodging;

public class ComboBinding extends Composite {

	private Label label = null;
	private Label label1 = null;
	private Combo combo = null;
	private Text txtDefaultLodging = null;
	private DatabindingService dbs;
	private ComboViewer comboViewer;
	
	public ComboBinding(Composite parent, int style) throws BindingException{
		super(parent,style);
		initialize();
	}
	
	private void initialize() throws BindingException {
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.grabExcessHorizontalSpace = false;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		this.setLayout(gridLayout);
		this.setSize(new Point(300, 200));
		label = new Label(this, SWT.NONE);
		label.setText("Default Lodging");
		createCombo();
		label1 = new Label(this, SWT.NONE);
		label1.setText("Default Lodging");
		txtDefaultLodging = new Text(this, SWT.BORDER);
		txtDefaultLodging.setLayoutData(gridData);
		bind();
	}
	private void bind() throws BindingException{
		dbs = SampleData.getSWTtoEMFDatabindingService(this);	
		
		IUpdatableFactory emfFactory = new IUpdatableFactory(){		
			public IUpdatable createUpdatable(Object object, Object attribute) {
				EObject eObject = (EObject)object;
				EStructuralFeature attr;
				if (attribute instanceof EStructuralFeature)
					attr = (EStructuralFeature)attribute;
				else
					attr = eObject.eClass().getEStructuralFeature((String)attribute);
				if (attr.isMany()) {
					// TODO: This really needs to be a UpdatableTable
					return new EMFUpdatableCollection(eObject, attr, !attr.isChangeable());
				}
				else
					return new EMFUpdatableValue(eObject , attr, !attr.isChangeable());
					
			}
		
		};				
		dbs.addUpdatableFactory(EObjectImpl.class, emfFactory);
		
		AdventurePackage emfPackage = AdventurePackage.eINSTANCE;
		
		Adventure skiTrip = SampleData.WINTER_HOLIDAY;
		Catalog catalog = SampleData.CATALOG_2005;	
		
		dbs.bind(comboViewer, "contents", catalog, "lodgings",
				new ToStringIdentityListConverter(Lodging.class),
				new EMFIdentityLabelProvider(Lodging.class, "name")				
		);
				
		
		dbs.bindValue(comboViewer,"selection",skiTrip,"defaultLodging", new IdentityConverter(Lodging.class), new IdentityConverter(Lodging.class));
		
		IUpdatableValue defaultLodging = dbs.createUpdatableValue(skiTrip,"defaultLodging");
		
		dbs.bindValue(txtDefaultLodging, "text", defaultLodging, "description");
		
	}
	/**
	 * This method initializes combo	
	 *
	 */
	private void createCombo() {
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		combo = new Combo(this, SWT.NONE);
		combo.setLayoutData(gridData1);
		comboViewer = new ComboViewer(combo);
	}
}

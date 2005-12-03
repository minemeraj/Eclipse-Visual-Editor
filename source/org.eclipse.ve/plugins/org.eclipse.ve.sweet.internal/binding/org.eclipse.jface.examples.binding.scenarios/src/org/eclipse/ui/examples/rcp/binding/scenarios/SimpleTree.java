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
 *  Created Oct 25, 2005 by Gili Mendel
 * 
 *  $RCSfile: SimpleTree.java,v $
 *  $Revision: 1.4 $  $Date: 2005-12-03 01:16:56 $ 
 */

package org.eclipse.ui.examples.rcp.binding.scenarios;

import org.eclipse.jface.databinding.*;
import org.eclipse.jface.databinding.viewers.*;
import org.eclipse.jface.databinding.viewers.TableViewerDescription.Column;
import org.eclipse.jface.tests.binding.scenarios.pojo.*;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;



public class SimpleTree extends Composite {

	private Composite mainComposite = null;
	private Composite buttonComposite = null;
	private Button button = null;
	private Tree tree = null;
	private TreeViewer treeViewer1 = null;
	private Button button1 = null;
	
	private IDataBindingContext dbc;
	private Tree tree2 = null;
	private TreeViewer treeViewer2 = null;

	public SimpleTree(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
		initialize();
	}

	/**
	 * This method initializes composite	
	 *
	 */
	private void createMainComposite() {
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		mainComposite = new Composite(this, SWT.NONE);
		mainComposite.setLayout(new FillLayout());
		mainComposite.setLayoutData(gridData);
		
		tree = new Tree(mainComposite, SWT.FULL_SELECTION);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		TreeColumn treeColumn10 = new TreeColumn(tree, SWT.NONE);
		treeColumn10.setWidth(200);
		treeColumn10.setText("ID");
		TreeColumn treeColumn11 = new TreeColumn(tree, SWT.NONE);
		treeColumn11.setWidth(200);
		treeColumn11.setText("Column");
		treeViewer1 = new TreeViewer(tree);
		
	
		
		tree2 = new Tree(mainComposite, SWT.FULL_SELECTION);
		tree2.setHeaderVisible(true);
		tree2.setLinesVisible(true);	
		TreeColumn treeColumn2 = new TreeColumn(tree2, SWT.NONE);
		treeColumn2.setWidth(200);
		treeColumn2.setText("ID");
		TreeColumn treeColumn3 = new TreeColumn(tree2, SWT.NONE);
		treeColumn3.setWidth(200);
		treeColumn3.setText("Name");
		treeViewer2 = new TreeViewer(tree2);
	}

	/**
	 * This method initializes composite1	
	 *
	 */
	private void createButtonComposite() { 
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		buttonComposite = new Composite(this, SWT.NONE);
		buttonComposite.setLayout(gridLayout);
		button1 = new Button(buttonComposite, SWT.NONE);
		button1.setText("Add Element");
		button1.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				System.out.println("widgetSelected()"); // TODO Auto-generated Event stub widgetSelected()
			}
		});
		button = new Button(buttonComposite, SWT.NONE);
		button.setText("Delete");
		button.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				System.out.println("widgetSelected()"); // TODO Auto-generated Event stub widgetSelected()
			}
		});
	}
	
	private void bind() {
		

		
		try {
			dbc = DataBinding.createContext(this);

			ITree treeModel = org.eclipse.jface.tests.binding.scenarios.pojo.SampleData.CATEGORY_TREE;
			
			TreeViewerDescription treeDescription1 = new TreeViewerDescription(treeViewer1);
			treeDescription1.addColumn(Catalog.class, "class.name");
			
			treeDescription1.addColumn(Lodging.class, "name");
			treeDescription1.addColumn(Lodging.class, "description");
			
			treeDescription1.addColumn(Adventure.class, "name");
			treeDescription1.addColumn(Adventure.class, "price");

						
			treeDescription1.addColumn(Category.class, "name");
			
			treeDescription1.addColumn(Account.class, "firstName");
			treeDescription1.addColumn(Account.class, "lastName");
						
			dbc.bind(treeDescription1, treeModel, null);
			
			////
			
			TreeViewerDescription treeDescription2 = new TreeViewerDescription(treeViewer2);
			treeDescription2.addColumn(Catalog.class, "class.name");
			
			treeDescription2.addColumn(Lodging.class, "name");
			treeDescription2.addColumn(Lodging.class, "description");
			
			treeDescription2.addColumn(Adventure.class, "name");
			treeDescription2.addColumn(Adventure.class, "price");
						
			treeDescription2.addColumn(Category.class, "name");
			
			treeDescription2.addColumn(Account.class, "firstName");
			treeDescription2.addColumn(Account.class, "lastName");
			
			TreeModelDescription modelDescription = new TreeModelDescription(new Object[] {org.eclipse.jface.tests.binding.scenarios.pojo.SampleData.CATALOG_2005});
			modelDescription.addChildrenProperty(org.eclipse.jface.tests.binding.scenarios.pojo.Catalog.class, "categories");
			modelDescription.addChildrenProperty(org.eclipse.jface.tests.binding.scenarios.pojo.Catalog.class, "accounts");
			modelDescription.addChildrenProperty(org.eclipse.jface.tests.binding.scenarios.pojo.Catalog.class, "lodgings");
			
			modelDescription.addChildrenProperty(org.eclipse.jface.tests.binding.scenarios.pojo.Category.class, "adventures");
			
			dbc.bind(treeDescription2, modelDescription, null);
			
		} catch (BindingException e) {			
			e.printStackTrace();
		} 
		
	}

	private void initialize() {

		createMainComposite();
		this.setLayout(new GridLayout());

		createButtonComposite();
		this.setSize(new Point(759, 254));
		bind();
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

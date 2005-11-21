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

import org.eclipse.jface.databinding.DataBinding;
import org.eclipse.jface.databinding.IDataBindingContext;
import org.eclipse.jface.databinding.ITree;
import org.eclipse.jface.databinding.PropertyDescription;
import org.eclipse.jface.databinding.ViewersProperties;
import org.eclipse.jface.tests.binding.scenarios.pojo.SampleData;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

public class CategoryTree extends Composite {

	private IDataBindingContext dbc;
	private Tree tree = null;
	private TreeViewer viewer = null;

	public CategoryTree(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		tree = new Tree(this, SWT.BORDER);
		tree.setLinesVisible(true);
		this.setLayout(new FillLayout());
		setSize(new Point(461, 226));
		
		bind();
	}

	private void bind() {

		// For a given catalog show its accounts with columns for "firstName,
		// "lastName" and "state"
		dbc = DataBinding.createContext(this);

		ITree treeModel = SampleData.CATEGORY_TREE;
		viewer = new TreeViewer(tree);

//		TableViewerDescription tableViewerDescription = new TableViewerDescription(
//				viewer);
//		tableViewerDescription.addColumn("description");
//		tableViewerDescription.addColumn("price",
//				new DoubleTextCellEditor(viewer.getTable()), null,
//				new DoubleConverter());
//		tableViewerDescription.addColumn("defaultLodging",
//				null, new LodgingConverter());
		dbc.bind(new PropertyDescription(viewer, ViewersProperties.CONTENT), treeModel, null);     

	}
}  //  @jve:decl-index=0:visual-constraint="43,18"

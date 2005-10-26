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
 *  $Revision: 1.1 $  $Date: 2005-10-26 14:23:29 $ 
 */

package org.eclipse.ui.examples.rcp.binding.scenarios;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.examples.rcp.adventure.Catalog;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.examples.rcp.adventure.*;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.Label;


public class SimpleTree extends Composite {

	private Composite mainComposite = null;
	private Composite buttonComposite = null;
	private Button button = null;
	private Tree tree = null;
	private TreeViewer treeViewer = null;
	private Button button1 = null;

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
		TreeColumn treeColumn1 = new TreeColumn(tree, SWT.NONE);
		treeColumn1.setWidth(60);
		treeColumn1.setText("ID");
		TreeColumn treeColumn = new TreeColumn(tree, SWT.NONE);
		treeColumn.setWidth(200);
		treeColumn.setText("Column");
		treeViewer = new TreeViewer(tree);
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
		
		Catalog catalog = SampleData.CATALOG_2005;
		
		class ViewContentProvider implements ITreeContentProvider {
			public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			}

			public void dispose() {
			}

			public Object[] getElements(Object parent) {	
				if (parent instanceof Catalog) {
					return ((Catalog) parent).getCategories().toArray();
				}
				if (parent instanceof Category) {
					return ((Category) parent).getAdventures().toArray();
				}
				return new Object[0];
			}

			public Object[] getChildren(Object parentElement) {
				return getElements(parentElement);
			}

			public Object getParent(Object element) {
				if (element instanceof EObject) {
					return ((EObject) element).eContainer();
				}
				return null;
			}

			public boolean hasChildren(Object element) {
				return getChildren(element).length > 0;
			}
		}
		
		class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {

			public String getText(Object obj) {
				if (obj instanceof Category) { return ((Category) obj).getName(); }
				if (obj instanceof Adventure) { return ((Adventure) obj).getName(); }
				return super.getText(obj);
			}

			public String getColumnText(Object obj, int index) {
				EObject o = (EObject)obj;
				String featureName;
				if (index==0)
					featureName="id";
				else
					featureName="name";
				
				EStructuralFeature f = o.eClass().getEStructuralFeature(featureName);
				return (String) o.eGet(f);
			}

			public Image getColumnImage(Object obj, int index) {
				return getImage(obj);
			}

			public Image getImage(Object obj) {				
				return null;
			}
		}
		
		
		ICellModifier cellModifier = new ICellModifier() {		
			public void modify(Object element, String property, Object value) {	
				EObject o;
				if (element instanceof TreeItem)
					o = (EObject) ((TreeItem)element).getData();
				else
					o = (EObject)element;
				if (property.equals("id") || property.equals("name")) {
					EStructuralFeature f = o.eClass().getEStructuralFeature(property);
					o.eSet(f,value);
					treeViewer.update(o, null);
				}				
			}
		
			public Object getValue(Object element, String property) {
				EObject o;
				if (element instanceof TreeItem)
					o = (EObject) ((TreeItem)element).getData();
				else
					o = (EObject)element;
				if (property.equals("id") ||property.equals("name")) {
					EStructuralFeature f = ((EObject)element).eClass().getEStructuralFeature(property);
					return o.eGet(f);
				}
				return null;
			}
		
			public boolean canModify(Object element, String property) {
				return true;
			}
		
		};
		
		treeViewer.setContentProvider(new ViewContentProvider());
		treeViewer.setLabelProvider(new ViewLabelProvider());
		treeViewer.setInput(catalog);
		
		
		treeViewer.setCellEditors(new CellEditor[] {new TextCellEditor(tree), new TextCellEditor(tree)});
		treeViewer.setCellModifier(cellModifier);
		treeViewer.setColumnProperties(new String[] { "id", "name"});
		
	}

	private void initialize() {
		createMainComposite();
		this.setLayout(new GridLayout());
		createButtonComposite();
		setSize(new Point(300, 200));
		bind();
	}

}

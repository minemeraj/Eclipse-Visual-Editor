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
 *  Created Nov 29, 2005 by Gili Mendel
 * 
 *  $RCSfile: FileSystemTree.java,v $
 *  $Revision: 1.6 $  $Date: 2005-12-03 04:22:10 $ 
 */

package org.eclipse.ui.examples.rcp.binding.scenarios;

import java.io.File;
import java.util.Collections;

import org.eclipse.jface.databinding.*;
import org.eclipse.jface.databinding.viewers.TreeViewerDescription;
import org.eclipse.jface.databinding.viewers.ViewersProperties;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;

public class FileSystemTree extends Composite {

	private Composite treeComposite = null;
	private Composite buttonsComposite = null;
	private Label label = null;
	private Button directory = null;
	private Tree tree = null;
	private TreeViewer treeViewer = null;
	private Image folderImage = new Image(Display.getCurrent(), getClass().getResourceAsStream("/org/eclipse/ui/examples/rcp/binding/scenarios/folder.gif"));
	private Image fileImage = new Image(Display.getCurrent(), getClass().getResourceAsStream("/org/eclipse/ui/examples/rcp/binding/scenarios/file.gif"));
	
	ITree fileTree = null;  //  @jve:decl-index=0:
	
	TreeModelDescription modelDescription =null;

	public FileSystemTree(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
		initialize();
	}

	/**
	 * This method initializes treeComposite	
	 *
	 */
	private void createTreeComposite() {
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = GridData.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.grabExcessVerticalSpace = true;
		gridData3.verticalAlignment = GridData.FILL;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.verticalAlignment = GridData.FILL;
		treeComposite = new Composite(this, SWT.NONE);
		treeComposite.setLayout(new GridLayout());
		treeComposite.setLayoutData(gridData2);
		tree = new Tree(treeComposite, SWT.BORDER);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(false);
		tree.setLayoutData(gridData3);
//		TreeColumn treeColumn = new TreeColumn(tree, SWT.NONE);
//		treeColumn.setWidth(200);
		treeViewer = new TreeViewer(tree);
	}

	/**
	 * This method initializes buttonsComposite	
	 *
	 */
	private void createButtonsComposite() {
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = GridData.CENTER;
		gridData1.horizontalAlignment = GridData.FILL;
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.CENTER;
		gridData.horizontalAlignment = GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		buttonsComposite = new Composite(this, SWT.NONE);
		buttonsComposite.setLayout(gridLayout);
		buttonsComposite.setLayoutData(gridData1);
		label = new Label(buttonsComposite, SWT.NONE);
		label.setText("Root Directory:");
		directory = new Button(buttonsComposite, SWT.BORDER);
		directory.setImage(folderImage);
		directory.setLayoutData(gridData);
		directory.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog (directory.getShell());
				dialog.setFilterPath(directory.getText());
				dialog.open();
				directory.setText(dialog.getFilterPath());
				File root = new File(directory.getText());
				if (!root.canRead()) root = null;
				//fileTree.setChildren(null, new Object[] {root});
				modelDescription.setRootObjects(new Object[] {root});
			}
		});
		
		bind();
	}
	
	
	protected void bind() {
		
		IDataBindingContext dbc = DataBinding.createContext(this);
		
		modelDescription = new TreeModelDescription(null); // root FS will be set later
		modelDescription.addChildrenProperty(File.class, "listFiles");
				
		dbc.bind(new Property(treeViewer, ViewersProperties.CONTENT), modelDescription, null);
		
		treeViewer.setLabelProvider(new LabelProvider(){		
			public String getText(Object element) {
				return ((File)element).getName();
			}		
			public Image getImage(Object element) {
				File f = (File)element;
				if (f.isDirectory())
					return folderImage;
				else
					return fileImage;
			}		
		});
		
	}

	/**
	 * @param args
	 * 
	 * @since 1.2.0
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/* Before this is run, be sure to set up the launch configuration (Arguments->VM Arguments)
		 * for the correct SWT library path in order to run with the SWT dlls. 
		 * The dlls are located in the SWT plugin jar.  
		 * For example, on Windows the Eclipse SWT 3.1 plugin jar is:
		 *       installation_directory\plugins\org.eclipse.swt.win32_3.1.0.jar
		 */
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setSize(new Point(300, 200));
		FileSystemTree thisClass = new FileSystemTree(shell, SWT.NONE);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private void initialize() {
		this.setLayout(new GridLayout());
		createTreeComposite();
		createButtonsComposite();
		setSize(new Point(300, 200));
	}

}

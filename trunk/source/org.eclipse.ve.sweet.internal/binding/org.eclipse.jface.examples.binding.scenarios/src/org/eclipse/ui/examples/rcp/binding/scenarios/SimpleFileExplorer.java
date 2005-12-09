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
 *  Created Dec 5, 2005 by Gili Mendel
 * 
 *  $RCSfile: SimpleFileExplorer.java,v $
 *  $Revision: 1.1 $  $Date: 2005-12-09 21:44:26 $ 
 */

package org.eclipse.ui.examples.rcp.binding.scenarios;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

import org.eclipse.jface.databinding.*;
import org.eclipse.jface.databinding.viewers.ViewersProperties;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.jface.viewers.TreeViewer;

public class SimpleFileExplorer {
	
	public class SelectedFile {
		private File file=null;		 
		private PropertyChangeSupport changeSupport=null;
				
		public File getFile() {
			return file;
		}
		
		public void setFile(File file) {
			File old = this.file;
			this.file = file;
			if (changeSupport!=null)
				changeSupport.firePropertyChange("file", old, file);
		}
		
		public String getFileName() {
			return file==null? null: file.getName();
		}
						
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			if (listener!=null) {
				if (changeSupport==null)
					changeSupport = new PropertyChangeSupport(this);
				changeSupport.addPropertyChangeListener(listener);
			}
		}
		
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			if (changeSupport!=null && listener!=null)				
			   changeSupport.removePropertyChangeListener(listener);
		}
	}

	private SelectedFile selectedFile = new SelectedFile();  //  @jve:decl-index=0:
	private Image folderImage = new Image(Display.getCurrent(), getClass().getResourceAsStream("/org/eclipse/ui/examples/rcp/binding/scenarios/folder.gif"));
	private Image fileImage = new Image(Display.getCurrent(), getClass().getResourceAsStream("/org/eclipse/ui/examples/rcp/binding/scenarios/file.gif"));
	
	private Shell sShell = null;
	private Tree tree = null;
	private Label label = null;
	private Button button = null;
	private TreeViewer treeViewer = null;
	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		sShell = new Shell();
		sShell.setText("Simple File Explorer");
		sShell.setLayout(gridLayout);
		sShell.setSize(new Point(300, 200));
		tree = new Tree(sShell, SWT.BORDER);
		tree.setLayoutData(gridData);
		treeViewer = new TreeViewer(tree);
		label = new Label(sShell, SWT.NONE);
		label.setText("Select a root directory:");
		button = new Button(sShell, SWT.NONE);
		GridData gridData1 = new GridData();
		gridData1.widthHint = 70;
		button.setLayoutData(gridData1);
		button.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog (sShell);
				dialog.setFilterPath(selectedFile.getFileName());
				dialog.open();				
				String newPath = dialog.getFilterPath();				
				selectedFile.setFile(new File(newPath));				
			}
		});
		bindControls();
	}
	
	private void bindControls() {
		IDataBindingContext dbc = DataBinding.createContext(sShell);
		
		dbc.bind(new Property(button, "text"), new Property(selectedFile, "fileName"), null);
		
		TreeModelDescription modelDescription = new TreeModelDescription(new Property (selectedFile, "file")); 
		modelDescription.addChildrenProperty(File.class, "listFiles");
		
		dbc.bind(new Property(treeViewer, ViewersProperties.CONTENT), modelDescription, null);
		
		treeViewer.setLabelProvider(new LabelProvider(){		
			public String getText(Object element) {
				String name = ((File)element).getName();
				return name.length()==0? ((File)element).getPath(): name;
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

}

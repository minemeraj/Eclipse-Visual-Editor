/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  Created May 3, 2005 by Gili Mendel
 * 
 *  $RCSfile: SWTContainerWizardContent.java,v $
 *  $Revision: 1.1 $  $Date: 2005-05-05 13:02:27 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;


public class SWTContainerWizardContent extends Composite {

	private Button jFaceCheckButton = null;
	private Group group = null;
	private Button ideRadio = null;
	private Button pdeRadio = null;
	private Button customRadio = null;
	private Text customDir = null;
	private Label label = null;
	private Label label1 = null;
	private Label label2 = null;
	private Button browseButton = null;
	
	private SWTContainer.ContainerType containerType = null;
		

	/**
	 * This method initializes group	
	 *
	 */    
	private void createGroup() {
		GridLayout gridLayout4 = new GridLayout();
		gridLayout4.numColumns = 2;
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData1.horizontalIndent = 20;
		group = new Group(this, SWT.NONE);		   
		group.setText("Jar and Library Source Location");
		group.setLayoutData(gridData2);
		group.setLayout(gridLayout4);
		ideRadio = new Button(group, SWT.RADIO);
		ideRadio.setText("IDE Platform");
		ideRadio.setToolTipText("Use the IDE's jars and libraries");		
		label = new Label(group, SWT.NONE);
		pdeRadio = new Button(group, SWT.RADIO);
		pdeRadio.setText("PDE Target ");
		pdeRadio.setToolTipText("Use the PDE Target's jars and libraries");
		label1 = new Label(group, SWT.NONE);
		customRadio = new Button(group, SWT.RADIO);
		customRadio.setText("Custom location");
		customRadio.setToolTipText("Specify a directory for jars and libraries");
		label2 = new Label(group, SWT.NONE);
		customDir = new Text(group, SWT.BORDER);
		customDir.setToolTipText("Specify  a root directory where all jars/libraries are located");
		customDir.setEditable(false);
		customDir.setLayoutData(gridData1);
		browseButton = new Button(group, SWT.NONE);
		browseButton.setText("Browse");
		browseButton.setEnabled(false);
		
		pdeRadio.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (pdeRadio.getSelection()) {
					customDir.setText(containerType.getPdePath());
					customDir.setToolTipText(containerType.getPdePath());				
					containerType.setPathType(SWTContainer.SWT_CONTAINER_PATH_PDE, true);
				}
			}
		});
		ideRadio.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (ideRadio.getSelection()) {
					customDir.setText(containerType.getPlatformPath());
					customDir.setToolTipText(containerType.getPlatformPath());				
					containerType.setPathType(SWTContainer.SWT_CONTAINER_PATH_PLATFORM, true);
				}
			}
		});
		customRadio.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    				
					customDir.setEditable(customRadio.getSelection());
					browseButton.setEnabled(customRadio.getSelection());
					if (customRadio.getSelection()) {
						customDir.setText(containerType.getCustomPath());
						customDir.setToolTipText(containerType.getCustomPath());					
						containerType.setPathType(SWTContainer.SWT_CONTAINER_PATH_CUSTOM, true);
					}
			}
		});
		browseButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				DirectoryDialog dialog = new DirectoryDialog(getShell(),SWT.SINGLE);
				dialog.setFilterPath(customDir.getText());				
				String result = dialog.open();
				if (result!=null) {
					customDir.setText(result);
					customDir.setToolTipText(result);
					containerType.setCustomPath(result);
				}
			}
		});
		
		
		customDir.addKeyListener(new org.eclipse.swt.events.KeyAdapter() { 
			public void keyReleased(org.eclipse.swt.events.KeyEvent e) {    
				containerType.setCustomPath(customDir.getText());
			}
		});

	}


	public SWTContainerWizardContent(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		this.setLayout(new GridLayout());
		createGroup();
		setSize(new Point(300, 200));
		jFaceCheckButton = new Button(this, SWT.CHECK);
		jFaceCheckButton.setText(SWTMessages.getString("SWTContainerWizardPage.includeJFaceCheck")); //$NON-NLS-1$
		jFaceCheckButton.setToolTipText("Contribute the JFace jars from the location above");
		jFaceCheckButton.setLayoutData(gridData3);
		
		jFaceCheckButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    									
				containerType.setPathType(SWTContainer.SWT_CONTAINER_JFACE, jFaceCheckButton.getSelection()) ;				
			}
		});
	}
			
	public SWTContainer.ContainerType getContainerType() {
		return containerType;
	}
	
	public void setContainerType(SWTContainer.ContainerType containerType) {
		this.containerType = containerType;
		if (containerType.isTargetPath()) {
			pdeRadio.setSelection(true);
			customDir.setText(containerType.getPdePath());
			customDir.setToolTipText(containerType.getPdePath());
		}
		else if (containerType.isPlatformPath()) {
			ideRadio.setSelection(true);
			customDir.setText((containerType.getPlatformPath()));
			customDir.setToolTipText((containerType.getPlatformPath()));
		}
		else {
			customRadio.setSelection(true);
			customDir.setText(containerType.getCustomPath());
			customDir.setToolTipText(containerType.getCustomPath());
		}
		jFaceCheckButton.setSelection(containerType.includeJFace());
	}

}


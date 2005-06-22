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
 *  $Revision: 1.6 $  $Date: 2005-06-22 16:24:10 $ 
 */
package org.eclipse.ve.internal.swt;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.wizards.BuildPathDialogAccess;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.wizard.WizardPage;
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
	private Label platformVersion = null;
	private Label pdeVersion = null;	
	private Button browseButton = null;
	private WizardPage  wizard = null;
	
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
		group.setText(SWTMessages.SWTContainerWizardContent_0); 
		group.setLayoutData(gridData2);
		group.setLayout(gridLayout4);
		ideRadio = new Button(group, SWT.RADIO);
		ideRadio.setText(SWTMessages.SWTContainerWizardContent_1); 
		ideRadio.setToolTipText(SWTMessages.SWTContainerWizardContent_2);		 
		platformVersion = new Label(group, SWT.NONE);
		pdeRadio = new Button(group, SWT.RADIO);
		pdeRadio.setText(SWTMessages.SWTContainerWizardContent_3); 
		pdeRadio.setToolTipText(SWTMessages.SWTContainerWizardContent_4); 
		pdeVersion = new Label(group, SWT.NONE);
		customRadio = new Button(group, SWT.RADIO);
		customRadio.setText(SWTMessages.SWTContainerWizardContent_5); 
		customRadio.setToolTipText(SWTMessages.SWTContainerWizardContent_6); 
		new Label(group, SWT.NONE);
		customDir = new Text(group, SWT.BORDER);
		customDir.setToolTipText(SWTMessages.SWTContainerWizardContent_7); 
		customDir.setEditable(false);
		customDir.setLayoutData(gridData1);
		browseButton = new Button(group, SWT.NONE);
		browseButton.setText(SWTMessages.SWTContainerWizardContent_8); 
		browseButton.setEnabled(false);
		
		pdeRadio.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (pdeRadio.getSelection()) {
					setPDE(false);
					containerType.setPathType(SWTContainer.SWT_CONTAINER_PATH_PDE, true);
				}
			}
		});
		ideRadio.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (ideRadio.getSelection()) {
					setPlatform(false);
					containerType.setPathType(SWTContainer.SWT_CONTAINER_PATH_PLATFORM, true);
				}
			}
		});
		customRadio.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    									
					browseButton.setEnabled(customRadio.getSelection());
					if (customRadio.getSelection()) {
						setCustom(false);
						containerType.setPathType(SWTContainer.SWT_CONTAINER_PATH_CUSTOM, true);
					}
			}
		});
		browseButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				IPath[] current = new Path[] { new Path(containerType.getCustomPath()) };
				IPath[] paths= BuildPathDialogAccess.chooseVariableEntries(getShell(), current);		
				if (paths!=null && paths.length>0) {									
					containerType.setCustomPath(paths[0].toPortableString());
					setCustom(false);					
				}
			}
		});
		
		
		customDir.addKeyListener(new org.eclipse.swt.events.KeyAdapter() { 
			public void keyReleased(org.eclipse.swt.events.KeyEvent e) {    
				containerType.setCustomPath(customDir.getText());
			}
		});

	}


	public SWTContainerWizardContent(Composite parent, int style, WizardPage page) {
		super(parent, style);
		this.wizard = page;
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
		jFaceCheckButton.setText(SWTMessages.SWTContainerWizardPage_includeJFaceCheck); 
		jFaceCheckButton.setToolTipText(SWTMessages.SWTContainerWizardContent_9); 
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
	
	protected void setPlatform(boolean setSelection) {
		if (setSelection)
		   ideRadio.setSelection(true);
		platformVersion.setText(containerType.getPlatformVersion());
		customDir.setText((containerType.getPlatformPath()));
		customDir.setToolTipText((containerType.getPlatformPath()));
		jFaceCheckButton.setSelection(containerType.includeJFace());
		jFaceCheckButton.setEnabled(true);
		setError(null);
		wizard.setMessage(SWTMessages.SWTContainerWizardContent_10, IMessageProvider.INFORMATION); 
		group.layout();
	}
	protected void setPDE(boolean setSelection) {
		if (setSelection)
		  pdeRadio.setSelection(true);
		pdeVersion.setText(containerType.getPdeVersion());
		customDir.setText(containerType.getPdePath());
		customDir.setToolTipText(containerType.getPdePath());
		jFaceCheckButton.setSelection(containerType.includeJFace());
		jFaceCheckButton.setEnabled(true);
		setError(null);		
		wizard.setMessage(SWTMessages.SWTContainerWizardContent_11, IMessageProvider.INFORMATION); 
		group.layout();
	}
	
	protected void setError (String err) {
		if (err==null) {
			wizard.setPageComplete(true);
			wizard.setErrorMessage(null);
		}
		else {
			wizard.setPageComplete(false);
			wizard.setErrorMessage(err);
		}
	}
	
	
	protected void setCustom (boolean setSelection) {
		if (setSelection)
		   customRadio.setSelection(true);
		IPath resolvedPath= JavaCore.getResolvedVariablePath(new Path(containerType.getCustomPath()));
		if (resolvedPath==null)
			resolvedPath = new Path(""); //$NON-NLS-1$
		
		File f = resolvedPath.toFile();
		if (f.exists()&& f.isDirectory()) { 
			setError(null);
			wizard.setMessage(SWTMessages.SWTContainerWizardContent_13, IMessageProvider.INFORMATION); 
		}
		else
			setError(SWTMessages.SWTContainerWizardContent_14); 
		//TODO: current limitation
		jFaceCheckButton.setSelection(false);
		jFaceCheckButton.setEnabled(false);
	    customDir.setText(resolvedPath.toOSString());		
		customDir.setToolTipText(resolvedPath.toOSString());
		browseButton.setEnabled(true);
		group.layout();
	}
	
	public void setContainerType(SWTContainer.ContainerType containerType) {
		this.containerType = containerType;
		if (containerType.isPDEPath()) {
			setPDE(true);
			platformVersion.setText(containerType.getPlatformVersion());
		}
		else if (containerType.isPlatformPath()) { 
			setPlatform(true);
			pdeVersion.setText(containerType.getPdeVersion());
		}
		else {
			setCustom(true);
			pdeVersion.setText(containerType.getPdeVersion());
			platformVersion.setText(containerType.getPlatformVersion());
		}
		
		jFaceCheckButton.setSelection(containerType.includeJFace());
	}

}


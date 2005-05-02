/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class SWTContainerWizardPage extends WizardPage implements IClasspathContainerPage {
	
	private IClasspathEntry fClassPathEntry;
	private Button includeJFaceButton = null;
	
	public SWTContainerWizardPage() {
		super("Standard Widget Toolkit (SWT)"); //$NON-NLS-1$
		setTitle(SWTMessages.getString("SWTContainerWizardPage.wizardTitle")); //$NON-NLS-1$
		setMessage(SWTMessages.getString("SWTContainerWizardPage.wizardMessage")); //$NON-NLS-1$
		setImageDescriptor(JavaPluginImages.DESC_WIZBAN_ADD_LIBRARY);
	}	
	
	public void createControl(Composite parent) {
		
		Composite c = new Composite(parent,SWT.NONE);
		c.setLayout(new GridLayout());
		// First and default option is to use the SWT level with Eclipse itself
/*		Button check = new Button(c,SWT.RADIO);
		check.setText("Use platform SWT level");
		check.setSelection(true);
//		check.setEnabled(false); 
		// Second option is to use the SWT level with the project in the build path
		Button useBuildLocation = new Button(c,SWT.RADIO);
		useBuildLocation.setText("Use \"org.eclipse.swt\" project in the build path");
		useBuildLocation.setEnabled(false);*/
		
		includeJFaceButton = new Button(c, SWT.CHECK);
		includeJFaceButton.setText(SWTMessages.getString("SWTContainerWizardPage.includeJFaceCheck")); //$NON-NLS-1$
		
		setControl(c);
		
		initializeFromSelection();		
		
	}
	
	protected void initializeFromSelection() {
		if (getControl() != null) {
			if (fClassPathEntry != null) {
				includeJFaceButton.setSelection("JFACE".equals(fClassPathEntry.getPath().segment(1))); //$NON-NLS-1$
			}
		}
			
	}
	
	public boolean finish(){
		IPath path = new Path(SWTContainer.SWT_CONTAINER_SIGNITURE);
		if (includeJFaceButton != null && includeJFaceButton.getSelection()) {
			path = path.append(SWTContainer.SWT_CONTAINER_JFACE_SIGNITURE);
		} else {
			path = new Path(SWTContainer.SWT_CONTAINER_SIGNITURE); //$NON-NLS-1$
		}
		setSelection(JavaCore.newContainerEntry(path));			
		return true;
	}
	
	public void setSelection(IClasspathEntry containerEntry) {
		fClassPathEntry = containerEntry;
		initializeFromSelection();		
	}
	public IClasspathEntry getSelection() {
		return fClassPathEntry;
	}	
}

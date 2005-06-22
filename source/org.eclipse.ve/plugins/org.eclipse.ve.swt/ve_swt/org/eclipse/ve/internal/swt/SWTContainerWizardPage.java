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
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public  class SWTContainerWizardPage extends WizardPage implements IClasspathContainerPage {
	
	private IClasspathEntry fClassPathEntry;	
	private IPath initialPath ;
	private SWTContainer.ContainerType containerType = null;
	
	public  SWTContainerWizardPage() {
		super("Standard Widget Toolkit (SWT)"); //$NON-NLS-1$
		setTitle(SWTMessages.SWTContainerWizardPage_wizardTitle); 
		setMessage(SWTMessages.SWTContainerWizardPage_wizardMessage); 
		setImageDescriptor(JavaPluginImages.DESC_WIZBAN_ADD_LIBRARY);
	}	
	
	public void createControl(Composite parent) {
		
		SWTContainerWizardContent c = new SWTContainerWizardContent(parent,SWT.NONE, this);
		c.setLayout(new GridLayout());		
		setControl(c);
		
		initializeFromSelection();				
	}
	
	protected void initializeFromSelection() {
		SWTContainerWizardContent page = (SWTContainerWizardContent) getControl();		
		if (page != null) {			
			if (fClassPathEntry != null) 
				containerType = new SWTContainer.ContainerType(fClassPathEntry.getPath());													
			else 
				containerType = new SWTContainer.ContainerType();			
			page.setContainerType(containerType);
		}			
	}
	
	public boolean finish(){		
		SWTContainer.ContainerType ori = new SWTContainer.ContainerType(initialPath);
		// Compatible with older versions of Path signiture
		if (initialPath==null || !ori.equals(containerType))
		   setSelection(JavaCore.newContainerEntry(containerType.getContainerPath()));			
		return true;
	}
	
	public void setSelection(IClasspathEntry containerEntry) {
		fClassPathEntry = containerEntry;
		if (containerEntry!=null)
		    initialPath = containerEntry.getPath();
		
		initializeFromSelection();		
	}
	public IClasspathEntry getSelection() {
		return fClassPathEntry;
	}	
}

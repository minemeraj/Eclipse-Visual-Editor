package org.eclipse.ve.internal.swt;

import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
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
		super("Standard Widget Toolkit (SWT)");
	}	
	
	public void createControl(Composite parent) {
		
		Composite c = new Composite(parent,SWT.NONE);
		c.setLayout(new GridLayout());
		// First and default option is to use the SWT level with Eclipse itself
		Button check = new Button(c,SWT.RADIO);
		check.setText("Use platform SWT level");
		check.setSelection(true);
//		check.setEnabled(false); 
		// Second option is to use the SWT level with the project in the build path
		Button useBuildLocation = new Button(c,SWT.RADIO);
		useBuildLocation.setText("Use \"org.eclipse.swt\" project in the build path");
		useBuildLocation.setEnabled(false);
		
		includeJFaceButton = new Button(c, SWT.CHECK);
		includeJFaceButton.setText("Include support for JFace library");
		
		
		setControl(c);
		
	}	
	
	public boolean finish(){
		Path path;
		if (includeJFaceButton != null && includeJFaceButton.getSelection()) {
			path = new Path("SWT_CONTAINER/JFACE");
		} else {
			path = new Path("SWT_CONTAINER");
		}
		setSelection(JavaCore.newContainerEntry(path));			
		return true;
	}
	
	public void setSelection(IClasspathEntry containerEntry) {
		fClassPathEntry = containerEntry;
	}
	public IClasspathEntry getSelection() {
		return fClassPathEntry;
	}	
}

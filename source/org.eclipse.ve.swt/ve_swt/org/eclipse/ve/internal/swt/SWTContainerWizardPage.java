package org.eclipse.ve.internal.swt;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.wizards.*;
import org.eclipse.jface.wizard.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ve.internal.java.wizard.InternalMessages;


public class SWTContainerWizardPage extends WizardPage implements IClasspathContainerPage {
	
	private IClasspathEntry fClassPathEntry;
	
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
		
		setControl(c);
		
	}	
	
	public boolean finish(){

		// Now append the name of the variable the user has supplied in the initialization data to the container
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			protected void execute(IProgressMonitor monitor) throws CoreException, InterruptedException {
				try {
					monitor.beginTask(InternalMessages.getString("ClasspathWizardPage.Finish.task.SettingProject"), 2000); //$NON-NLS-1$
					Path path = new Path("SWT_CONTAINER");
					path.append("EXTERNAL");
					setSelection(JavaCore.newContainerEntry(path));			
				} finally {
					monitor.done();
				}
			}
		};			

			
		try {
			getContainer().run(true, true, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof CoreException) {
				setErrorMessage(((CoreException) e.getTargetException()).getStatus().getMessage());
			} else {
				// CoreExceptions are handled above, but unexpected runtime exceptions and errors may still occur.
				setErrorMessage(e.getTargetException().getLocalizedMessage());
			}
		return false;
		}			

		return true;
	}
	public void setSelection(IClasspathEntry containerEntry) {
		fClassPathEntry = containerEntry;
	}
	public IClasspathEntry getSelection() {
		return fClassPathEntry;
	}	
}

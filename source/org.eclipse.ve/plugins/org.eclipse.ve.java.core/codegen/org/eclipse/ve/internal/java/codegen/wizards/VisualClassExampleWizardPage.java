package org.eclipse.ve.internal.java.codegen.wizards;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: VisualClassExampleWizardPage.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.corext.codemanipulation.StubUtility;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 */
// TODO NewClassWizardPage does not works well with just dumping an example in !!!!!
//      At this point we just bent it to work.  Need to clean up.
public class VisualClassExampleWizardPage extends NewClassWizardPage {
		
     IType fCreatedType = null ;
     String fPluginName = null ;
     
	/*
	 * @see WizardPage#createControl
	 */
	public void createControl(Composite parent) {
		
		initializeDialogUnits(parent);
		
		Composite composite= new Composite(parent, SWT.NONE);
		
		int nColumns= 4;
		
		GridLayout layout= new GridLayout();
		layout.numColumns= nColumns;		
		composite.setLayout(layout);
		
		// Just allow the user to specify the project and the class name		
		createContainerControls(composite, nColumns);	
		createPackageControls(composite,nColumns);
			
//		createEnclosingTypeControls(composite, nColumns);
				
		createSeparator(composite, nColumns);
		
		createTypeNameControls(composite, nColumns);
		setControl(composite);
					
	}
	
	public void validateType(){
		updateStatus(typeNameChanged());
	}
	
    //	Duplicating: super fCreatedType is private
	 public IType getCreatedType() {
			 return fCreatedType;
	 }
	

		
	public void createType(IProgressMonitor monitor) throws CoreException, InterruptedException {
		if (monitor == null) {
			monitor= new NullProgressMonitor();
		}

		monitor.beginTask(NewWizardMessages.getString("NewTypeWizardPage.operationdesc"), 10); //$NON-NLS-1$
		
		ICompilationUnit createdWorkingCopy= null;
		try {
			IPackageFragmentRoot root= getPackageFragmentRoot();
			IPackageFragment pack= getPackageFragment();
			if (pack == null) {
				pack= root.getPackageFragment(""); //$NON-NLS-1$
			}
			
			if (!pack.exists()) {
				String packName= pack.getElementName();
				pack= root.createPackageFragment(packName, true, null);
			}		
			
			monitor.worked(1);
			
			String clName= getTypeName();
			
			boolean isInnerClass= isEnclosingTypeSelected();
			
			IType createdType;			
			int indent= 0;
	
	
			
			String lineDelimiter= null;	
			if (!isInnerClass) {
				lineDelimiter= System.getProperty("line.separator", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
										
				ICompilationUnit parentCU= pack.createCompilationUnit(clName + ".java", "", false, new SubProgressMonitor(monitor, 2)); //$NON-NLS-1$ //$NON-NLS-2$
				createdWorkingCopy= (ICompilationUnit) parentCU.getSharedWorkingCopy(null, JavaUI.getBufferFactory(), null);
											
				String typeContent= getExampleFileContents() ;				
				String cuContent= constructCUContent(parentCU, typeContent, lineDelimiter);
				
				createdWorkingCopy.getBuffer().setContents(cuContent);
				
				createdType= createdWorkingCopy.getType(clName);
			} else {
				IType enclosingType= getEnclosingType();
				
				// if we are working on a enclosed type that is open in an editor,
				// then replace the enclosing type with its working copy
				IType workingCopy= (IType) EditorUtility.getWorkingCopy(enclosingType);
				if (workingCopy != null) {
					enclosingType= workingCopy;
				}
	
				ICompilationUnit parentCU= enclosingType.getCompilationUnit();
					
				// add imports that will be removed again. Having the imports solves 14661
				
				lineDelimiter= StubUtility.getLineDelimiterUsed(enclosingType);
				StringBuffer content= new StringBuffer();
				String comment= getTypeComment(parentCU);
				if (comment != null) {
					content.append(comment);
					content.append(lineDelimiter);
				}
				content.append(getExampleFileContents());
				IJavaElement[] elems= enclosingType.getChildren();
				IJavaElement sibling= elems.length > 0 ? elems[0] : null;
				
				createdType= enclosingType.createType(content.toString(), sibling, false, new SubProgressMonitor(monitor, 1));
			
				indent= StubUtility.getIndentUsed(enclosingType) + 1;
			}
			
			fCreatedType = createdType ;
			
			// add imports for superclass/interfaces, so types can be resolved correctly
	
			ICompilationUnit cu= createdType.getCompilationUnit();	
			synchronized(cu) {
				cu.reconcile();
			}			
			ISourceRange range= createdType.getSourceRange();
			
			IBuffer buf= cu.getBuffer();
			String originalContent= buf.getText(range.getOffset(), range.getLength());
			String formattedContent= StubUtility.codeFormat(originalContent, indent, lineDelimiter);
			buf.replace(range.getOffset(), range.getLength(), formattedContent);
			
			synchronized(cu) {
				cu.reconcile();
			}	
			
			cu.commit(true, monitor) ;	
	
			monitor.worked(1);
			
		} finally {
			if (createdWorkingCopy != null) {
				createdWorkingCopy.destroy();
			}
			monitor.done();
		}
	}	
	protected URL getFileLocation() {
		Plugin p=null ;
		if (fPluginName != null)
		   p = Platform.getPlugin(fPluginName) ;
		if (p == null)
		   p = JavaVEPlugin.getPlugin();
		String fileLocation = "Examples/" + getTypeName() + ".java"; //$NON-NLS-1$ //$NON-NLS-2$
		URL result = null;
		try {
			result = new URL(p.getDescriptor().getInstallURL(), fileLocation);
		} catch (MalformedURLException e) {
		}
		return result;
	}
	public String getExampleFileContents(){

		try { 		
			URL toDoListFileURL = getFileLocation();
			if (toDoListFileURL==null) return "Could not find example file"; //$NON-NLS-1$
			InputStream stream = toDoListFileURL.openStream();
			// Return the result of the file ExampleToDoList.java
			BufferedReader in = new BufferedReader(new InputStreamReader(stream));
			String lineSeparator = System.getProperty("line.separator"); //$NON-NLS-1$
			String line;
			StringBuffer outputBuffer = new StringBuffer(1000);
			// All of the examples are in the default package, so they have no package declaration
			// If the new file is in a package then we should add the line that declares it
//			if ( getPackageText() != null && !getPackageText().trim().equals("") ) { //$NON-NLS-1$
//				outputBuffer.append("package " + getPackageText() + ";"); //$NON-NLS-1$ //$NON-NLS-2$
//				outputBuffer.append(lineSeparator);
//			}
			while((line = in.readLine()) != null){
				outputBuffer.append(line);
				outputBuffer.append(lineSeparator);
			}
			in.close();
			return outputBuffer.toString();
		} catch ( Exception exc ) {
			exc.toString();
			return null;			
		}
	}
	/**
	 * We don't really allow the JVE examples to be created within another type
	 * so we should null out this method, as by default it will be called if a class is selected
	 * and the wizard is brought up
	 */
	public void setEnclosingType(IType type,boolean canBeModified){
		// Do nothing
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.ui.wizards.NewContainerWizardPage#containerChanged()
	 */
	protected IStatus containerChanged() {
		IStatus status = super.containerChanged();
		if(status.getSeverity()==IStatus.WARNING && status instanceof StatusInfo)
			((StatusInfo)status).setError(status.getMessage());
		return status;
	}

	/**
	 * @return
	 */
	public String getPluginName() {
		return fPluginName;
	}

	/**
	 * @param string
	 */
	public void setPluginName(String string) {
		fPluginName = string;
	}

}

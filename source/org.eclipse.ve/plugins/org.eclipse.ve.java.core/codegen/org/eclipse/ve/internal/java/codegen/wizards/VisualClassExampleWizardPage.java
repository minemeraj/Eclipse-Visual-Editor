/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: VisualClassExampleWizardPage.java,v $
 *  $Revision: 1.13 $  $Date: 2006-05-17 20:14:53 $ 
 */
package org.eclipse.ve.internal.java.codegen.wizards;

import java.io.*;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Bundle;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 

public class VisualClassExampleWizardPage extends NewTypeWizardPage {

	private String pluginName = null;

	public VisualClassExampleWizardPage() {
		super(true, "VisualClassExampleWizardPage"); //$NON-NLS-1$
		setTitle(CodegenWizardsMessages.VisualClassExampleWizardPage_title);
		setDescription(CodegenWizardsMessages.VisualClassExampleWizardPage_description);
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		
		Composite overallComposite= new Composite(parent, SWT.NONE);
		overallComposite.setFont(parent.getFont());
		
		int nColumns= 4;
		
		GridLayout layout= new GridLayout();
		layout.numColumns= nColumns;		
		overallComposite.setLayout(layout);
		
		createContainerControls(overallComposite, nColumns);
		createPackageControls(overallComposite, nColumns);
		
		createSeparator(overallComposite, nColumns);
		createTypeNameControls(overallComposite, nColumns);
		
		setControl(overallComposite);

		Dialog.applyDialogFont(overallComposite);
	}
	
	protected String constructCUContent(ICompilationUnit cu, String typeContent, String lineDelimiter) throws CoreException {
		return super.constructCUContent(cu, getExampleFileContents(), lineDelimiter);
	}
	
	public void createType(IProgressMonitor monitor) throws CoreException, InterruptedException {
		super.createType(monitor);
		
		// Bug 120851 - Imports not added
		String addedContents = getExampleFileContents();
		if(addedContents!=null && addedContents.length()>0){
			ASTParser parser = ASTParser.newParser(AST.JLS3);
			parser.setSource(addedContents.toCharArray());
			ASTNode node = parser.createAST(monitor);
			if (node instanceof CompilationUnit) {
				CompilationUnit cu = (CompilationUnit) node;
				List imports = cu.imports();
				if(imports!=null && imports.size()>0){
					
					boolean cuChanged = false;
					ICompilationUnit createdCU = getCreatedType().getCompilationUnit();
					ICompilationUnit cuWorkingCopy = createdCU.getWorkingCopy(monitor);
					
					for (Iterator importItr = imports.iterator(); importItr.hasNext();) {
						ImportDeclaration importDecl = (ImportDeclaration) importItr.next();
						String importName = importDecl.getName().getFullyQualifiedName();
						if(importDecl.isOnDemand())
							importName = importName + ".*"; //$NON-NLS-1$
						IImportDeclaration cuImportDecl = cuWorkingCopy.getImport(importName);
						if(cuImportDecl==null || !cuImportDecl.exists()){
							cuWorkingCopy.createImport(importName, null, monitor);
							cuChanged = true;
						}
					}
					
					if(cuChanged)
						cuWorkingCopy.commitWorkingCopy(true, monitor);
					cuWorkingCopy.discardWorkingCopy();
				}
			}
		}
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
	
	protected URL getFileLocation() {
		Bundle b=null ;
		if (pluginName != null)
		   b = Platform.getBundle(pluginName);
		if (b == null)
		   b = JavaVEPlugin.getPlugin().getBundle();
		String fileLocation = "Examples/" + getTypeName() + ".java"; //$NON-NLS-1$ //$NON-NLS-2$
		return FileLocator.find(b, new Path(fileLocation), null);
	}

	/*
	 * The wizard owning this page is responsible for calling this method with the
	 * current selection. The selection is used to initialize the fields of the wizard 
	 * page.
	 * 
	 * @param selection used to initialize the fields
	 */
	public void init(IStructuredSelection selection) {
		IJavaElement jelem= getInitialJavaElement(selection);
		initContainerPage(jelem);
		initTypePage(jelem);
		doStatusUpdate();
	}

	public void setPluginName(String pluginName) {
		this.pluginName  = pluginName;
	}

	
	public String getPluginName() {
		return pluginName;
	}

	public void doStatusUpdate(){
		// status of all used components
		IStatus[] status= new IStatus[] {
			fContainerStatus,
			isEnclosingTypeSelected() ? fEnclosingTypeStatus : fPackageStatus,
			fTypeNameStatus,
		};
		
		// the mode severe status will be displayed and the OK button enabled/disabled.
		updateStatus(status);
	}
	
	/*
	 * @see NewContainerWizardPage#handleFieldChanged
	 */
	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);
		// As fields change, update the status bar
		doStatusUpdate();
	}
	
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		doStatusUpdate();
	}
}

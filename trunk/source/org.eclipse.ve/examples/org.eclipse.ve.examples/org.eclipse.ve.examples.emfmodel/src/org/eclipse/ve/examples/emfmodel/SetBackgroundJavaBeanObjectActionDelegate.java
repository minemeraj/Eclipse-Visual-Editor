/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.examples.emfmodel;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPart;
import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.java.codegen.editorpart.JavaVisualEditorPart;
import org.eclipse.ve.internal.java.core.BeanUtilities;

public class SetBackgroundJavaBeanObjectActionDelegate implements IObjectActionDelegate  {

	private EditDomain editDomain;
	private Shell shell;
	private Iterator editParts;
	private ResourceSet resourceSet;
	private JavaClass CONTROL_TYPE;
	private JavaClass COMPONENT_TYPE;
	private IWorkbenchPart currentTargetPart;
	private ArrayList swtControls;
	private ArrayList awtComponents;
	
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
		if(targetPart != currentTargetPart){
			editDomain = ((JavaVisualEditorPart)targetPart).getEditDomain();
			resourceSet = editDomain.getDiagramData().eResource().getResourceSet();		
			CONTROL_TYPE = Utilities.getJavaClass("org.eclipse.swt.widgets.Control",resourceSet);
			COMPONENT_TYPE = Utilities.getJavaClass("java.awt.Component",resourceSet);						
		}
	}

	public void run(IAction action) {
		// Prompt for the color to change all of the edit parts to
		ColorDialog colorDialog = new ColorDialog(shell);
		RGB rgb = colorDialog.open();		
		if(rgb != null){
			processAWTComponents(rgb);
//			processSWTControls(rgb);
		}
	}
	
	private void processAWTComponents(RGB anRGB){
		// Get the "background" color from java.awt.Component
		EStructuralFeature backgroundSF = COMPONENT_TYPE.getEStructuralFeature("background");
		Iterator components = awtComponents.iterator();
		StringBuffer initBuffer = new StringBuffer();
		initBuffer.append("new java.awt.Color(");
		initBuffer.append(anRGB.red);
		initBuffer.append(',');
		initBuffer.append(anRGB.green);
		initBuffer.append(',');
		initBuffer.append(anRGB.blue);
		initBuffer.append(')');
		// Get the java.awt.Color class
		JavaClass awtColorClass = Utilities.getJavaClass("java.awt.Color",resourceSet);
		while(components.hasNext()){
			// Create a Color with the RGB specific.  InitString is "new java.awt.Color(r,g,b)";
			IJavaInstance newColor = BeanUtilities.createJavaObject(awtColorClass,resourceSet,initBuffer.toString());
			((IJavaInstance)components.next()).eSet(backgroundSF,newColor);
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// Iterate over the selected edit parts and divide into SWT and AWT ones
		editParts = ((IStructuredSelection)selection).iterator();
		swtControls = new ArrayList();
		awtComponents = new ArrayList();
		// Divide into SWT and AWT
		while(editParts.hasNext()){
			EditPart editPart = (EditPart)editParts.next();
			JavaHelpers javaType = ((IJavaInstance)editPart.getModel()).getJavaType();
			if(CONTROL_TYPE.isAssignableFrom(javaType)){
				swtControls.add(editPart.getModel());
			} else if (COMPONENT_TYPE.isAssignableFrom(javaType)){
				awtComponents.add(editPart.getModel());
			}
		}	
		action.setEnabled(!(swtControls.isEmpty() && awtComponents.isEmpty()));
	}
}

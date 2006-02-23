/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.examples.cdm.dept.dinner.ui;
/*
 *  $RCSfile: DinnerGraphicalEditorPart.java,v $
 *  $Revision: 1.5 $  $Date: 2006-02-23 15:17:05 $ 
 */

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.palette.*;

import org.eclipse.ve.internal.cde.emf.EMFGraphicalEditorPart;
import org.eclipse.ve.internal.cde.emf.EMFPrototypeFactory;
import org.eclipse.ve.internal.cdm.*;

public class DinnerGraphicalEditorPart extends EMFGraphicalEditorPart {

	protected EditPart getTreeEditPart(EObject rootModel) {
		// For now, we just get the first diagram.
		DiagramData dData = (DiagramData) rootModel;
		List ds = dData.getDiagrams();
		if (!ds.isEmpty()) {
			Diagram d = (Diagram) ds.get(0);
			return new DinnerContentsTreeEditPart(d);
		}
		return null;

	}

	protected void handleResourceChanged() {
	}

	protected PaletteRoot getPaletteRoot() {
		// Must have a pallete or selection won't work.
		PaletteRoot dinnerPalette = new PaletteRoot();
		fillPalette(dinnerPalette);
		return dinnerPalette;
	}

	private void fillPalette(PaletteRoot root) {
		fillControlGroup(root);
		
		PaletteGroup dinnerGroup = new PaletteGroup("Dinner Group");
		root.add(dinnerGroup);

		// Create the prototype of a entree (which is a DiagramFigure, and type is set to DinnerConstants.ENTREE_CHILD_TYPE).
		DiagramFigure proto = CDMFactory.eINSTANCE.createDiagramFigure();
		proto.setType(DinnerConstants.ENTREE_CHILD_TYPE);

		CreationToolEntry tool =
			new CreationToolEntry("Entree", "Create a new entree", new EMFPrototypeFactory(proto), null, null);
		dinnerGroup.add(tool);
		
		// Create the prototype of a employee (which is a DiagramFigure, and type is set to DinnerConstants.EMPLOYEE_CHILD_TYPE).
		proto = CDMFactory.eINSTANCE.createDiagramFigure();
		proto.setType(DinnerConstants.EMPLOYEE_CHILD_TYPE);

		tool =
			new CreationToolEntry("Employee", "Add an employee", new EMFPrototypeFactory(proto), null, null);
		dinnerGroup.add(tool);
		
	}


	private void fillControlGroup(PaletteRoot root) {
		PaletteGroup controlGroup = new PaletteGroup("Control Group");
		
		ToolEntry toolentry = new SelectionToolEntry();
		root.setDefaultEntry(toolentry);
		controlGroup.add(toolentry);
		root.add(controlGroup);
	}

	protected Resource createEmptyResource(String filename, ResourceSet rs) {
		Resource res = rs.createResource(URI.createFileURI(filename));

		DiagramData dData = CDMFactory.eINSTANCE.createDiagramData();
	
		// Need a diagram - which is a meal - for this to work.
		Diagram d = CDMFactory.eINSTANCE.createDiagram();
		dData.getDiagrams().add(d);

		res.getContents().add(dData);
		return res;
	}

	protected EObject getModel(Resource res) {
		DiagramData dd = (DiagramData) EcoreUtil.getObjectByType(res.getContents(), CDMPackage.eINSTANCE.getDiagramData());
		if (dd != null)
			setupDiagramData(dd);
		return dd;
	}

	public void gotoMarker(IMarker marker) {
	}

	protected void validateModel(EObject model) {
	}

	protected EditPart getEditPart(EObject model) {
		// For now, we just get the first diagram.
		DiagramData dData = (DiagramData) model;
		List ds = dData.getDiagrams();
		if (!ds.isEmpty()) {
			Diagram d = (Diagram) ds.get(0);
			return new DinnerContentsGraphicalEditPart(d);
		}
		return null;
	}

	protected void initialize(IFile file) throws CoreException {
	}
}

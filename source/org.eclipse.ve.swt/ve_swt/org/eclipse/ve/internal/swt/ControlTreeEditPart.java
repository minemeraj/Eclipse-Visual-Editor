/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ControlTreeEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2004-04-23 19:49:07 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.*;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.JavaBeanTreeEditPart;
 
/**
 * TreeEditPart for a SWT Control.
 */
public class ControlTreeEditPart extends JavaBeanTreeEditPart {
	
	private EStructuralFeature sfDirectEditProperty;
	
	private TreeDirectEditManager manager;
	
	public ControlTreeEditPart(Object model) {
		super(model);
	}
	
	protected void createEditPolicies() {
		super.createEditPolicies();
		sfDirectEditProperty = getDirectEditTargetProperty();
		if (sfDirectEditProperty != null) {
			EditDomain ed = EditDomain.getEditDomain(this);
			EditPartViewer viewer = getRoot().getViewer();
			manager = (TreeDirectEditManager)ed.getViewerData(viewer, TreeDirectEditManager.VIEWER_DATA_KEY);
			if( manager == null ) {
				manager = new TreeDirectEditManager(viewer);
				ed.setViewerData(viewer, TreeDirectEditManager.VIEWER_DATA_KEY, manager);
			}
		}
		
	}
	
	/**
	 * Get the structural feature for the property for which Direct Edit
	 * will be available.  This will return null if there is no
	 * Direct Edit property on this edit part.
	 * 
	 * @return  the property's structural feature
	 */
	private EStructuralFeature getDirectEditTargetProperty() {
		EStructuralFeature target = null;
		IJavaObjectInstance component = (IJavaObjectInstance)getModel();
		JavaClass modelType = (JavaClass) component.eClass();
	
		// Hard coded string properties to direct edit.
		// If more than one is available, it'll choose the first in the list below
			
		target = modelType.getEStructuralFeature("text"); //$NON-NLS-1$
		return target;			
	}
	
	private void performDirectEdit(){
		if(manager != null)
			manager.performDirectEdit(this, sfDirectEditProperty);
	}

	public void performRequest(Request request){
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT && sfDirectEditProperty != null)
			performDirectEdit();
	}

}

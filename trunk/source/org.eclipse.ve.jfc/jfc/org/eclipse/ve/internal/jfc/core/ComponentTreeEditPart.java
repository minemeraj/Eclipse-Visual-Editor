package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ComponentTreeEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.internal.java.JavaClass;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.ve.internal.java.core.JavaBeanTreeEditPart;
/**
 * Tree EditPart for an awt component.
 */
public class ComponentTreeEditPart extends JavaBeanTreeEditPart {

	private EStructuralFeature sfDirectEditProperty;
	protected IPropertySource propertySource;
	
	TreeDirectEditManager manager;

	public ComponentTreeEditPart(Object model) {
		super(model);
	}

	public Object getAdapter(Class type) {
		if (type == IPropertySource.class)
			if (propertySource != null)
				return propertySource;
			else
				return EcoreUtil.getRegisteredAdapter((EObject)getModel(), IPropertySource.class);
				
		return super.getAdapter(type);
	}


	public void setPropertySource(IPropertySource source) {
		propertySource = source;
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
		if (target != null) {
			return target;			
		}
		target = modelType.getEStructuralFeature("label"); //$NON-NLS-1$
		if (target != null) {
			return target;
		}
		target = modelType.getEStructuralFeature("title"); //$NON-NLS-1$
		return target;
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
	
	private void performDirectEdit(){
		if(manager != null)
			manager.performDirectEdit(this, sfDirectEditProperty);
	}

	public void performRequest(Request request){
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT && sfDirectEditProperty != null)
			performDirectEdit();
	}
	

}
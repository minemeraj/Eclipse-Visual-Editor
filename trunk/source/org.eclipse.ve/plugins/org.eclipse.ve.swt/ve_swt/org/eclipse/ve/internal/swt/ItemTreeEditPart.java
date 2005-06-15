/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ItemTreeEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-15 20:19:21 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.*;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.properties.PropertySourceAdapter;

import org.eclipse.ve.internal.java.core.BeanTreeDirectEditManager;
import org.eclipse.ve.internal.java.core.JavaBeanTreeEditPart;
 

/**
 * Tree Editpart for swt Items. 
 * @since 1.1.0
 */
public class ItemTreeEditPart extends JavaBeanTreeEditPart {

	
	private EStructuralFeature sfText;
	
	/**
	 * @param aModel
	 * 
	 * @since 1.1.0
	 */
	public ItemTreeEditPart(Object aModel) {
		super(aModel);
		sfText = JavaInstantiation.getReference((IJavaObjectInstance) getModel(), SWTConstants.SF_ITEM_TEXT);
	}

	protected BeanTreeDirectEditManager manager;
	
	protected void createEditPolicies() {
		super.createEditPolicies();
		EditDomain ed = EditDomain.getEditDomain(this);
		EditPartViewer viewer = getRoot().getViewer();
		directEditProperty = getDirectEditTargetProperty();
		if (directEditProperty != null)
			manager = BeanTreeDirectEditManager.getDirectEditManager(ed, viewer);
	}
	
	private void performDirectEdit(){
		if(manager != null)
			manager.performDirectEdit(this, directEditProperty);
	}
	
	IPropertyDescriptor directEditProperty;
	
	protected IPropertyDescriptor getDirectEditTargetProperty() {
		if (sfText != null) {
			IPropertySource source = (IPropertySource) getAdapter(IPropertySource.class);
			return PropertySourceAdapter.getDescriptorForID(source, sfText);
		} else
			return null;
	}

	public void performRequest(Request request){
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT && sfText != null)
			performDirectEdit();
		else
			super.performRequest(request);
	}

}

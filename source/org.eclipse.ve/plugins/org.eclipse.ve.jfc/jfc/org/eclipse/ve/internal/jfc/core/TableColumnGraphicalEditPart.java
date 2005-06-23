/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TableColumnGraphicalEditPart.java,v $
 *  $Revision: 1.7 $  $Date: 2005-06-23 16:08:44 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.*;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.*;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.properties.PropertySourceAdapter;

import org.eclipse.ve.internal.java.core.BeanDirectEditCellEditorLocator;
import org.eclipse.ve.internal.java.core.BeanDirectEditPolicy;

/**
 * TableColumn Graphical Editpart.
 * <p>
 * javax.swing.table.TableColumn does not inherit from java.awt.Component, so we need to do the visuals ourselves. The edit part should create a
 * figure that has the height of the parent JTable plus the height of the table header.
 * 
 * @since 1.0.0
 */
public class TableColumnGraphicalEditPart extends AbstractGraphicalEditPart {

	protected IPropertyDescriptor sfDirectEditProperty = null;

	protected DirectEditManager manager = null;

	/**
	 * TableColumn Graphical Editpart constructor
	 * 
	 * @param model
	 * 
	 * @since 1.1.0
	 */
	public TableColumnGraphicalEditPart(Object model) {
		setModel(model);
	}

	protected IFigure createFigure() {
		Figure figure = new Figure();
		figure.setOpaque(false);
		figure.setBackgroundColor(ColorConstants.cyan);
		figure.setBorder(new OutlineBorder());
		return figure;
	}

	public Object getAdapter(Class type) {
		if (type == IPropertySource.class)
			return EcoreUtil.getRegisteredAdapter((IJavaObjectInstance) getModel(), IPropertySource.class);
		Object result = super.getAdapter(type);
		if (result != null) {
			return result;
		} else {
			// See if any of the MOF adapters on our target can return a value
			// for the request
			Iterator mofAdapters = ((IJavaInstance) getModel()).eAdapters().iterator();
			while (mofAdapters.hasNext()) {
				Object mofAdapter = mofAdapters.next();
				if (mofAdapter instanceof IAdaptable) {
					Object mofAdapterAdapter = ((IAdaptable) mofAdapter).getAdapter(type);
					if (mofAdapterAdapter != null) { return mofAdapterAdapter; }
				}
			}
		}
		return null;
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefaultComponentEditPolicy());
		sfDirectEditProperty = getDirectEditTargetProperty();
		if (sfDirectEditProperty != null) {
			installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new BeanDirectEditPolicy());
		}
	}

	protected IPropertyDescriptor getDirectEditTargetProperty() {
		EStructuralFeature feature = null;
		IJavaObjectInstance component = (IJavaObjectInstance) getModel();
		JavaClass modelType = (JavaClass) component.eClass();
		// Hard coded string properties to direct edit.
		// If more than one is available, it'll choose the first in the list
		// below
		feature = modelType.getEStructuralFeature("headerValue"); //$NON-NLS-1$
		if (feature != null) {
			IPropertySource source = (IPropertySource) getAdapter(IPropertySource.class);
			return PropertySourceAdapter.getDescriptorForID(source, feature);
		} else
			return null;
	}

	private void performDirectEdit() {
		if (manager == null)
			manager = new CDEDirectEditManager(this, new BeanDirectEditCellEditorLocator(getFigure()), sfDirectEditProperty);
		manager.show();
	}

	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT && sfDirectEditProperty != null)
			performDirectEdit();
	}
}

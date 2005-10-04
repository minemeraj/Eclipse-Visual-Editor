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
/*
 *  $RCSfile: TableTreeColumnGraphicalEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-04 15:41:48 $ 
 */
package org.eclipse.ve.internal.swt;

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
 * org.eclipse.swt.widgets.TableColumn.
 * 
 * @since 1.0.0
 */
public class TableTreeColumnGraphicalEditPart extends AbstractGraphicalEditPart {

	protected IPropertyDescriptor sfDirectEditProperty = null;

	protected DirectEditManager manager = null;

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
		// Install policy that will allow direct edit capability on the table column
		if (sfDirectEditProperty != null) {
			installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new BeanDirectEditPolicy());
		}
	}

	private IPropertyDescriptor getDirectEditTargetProperty() {
		EStructuralFeature feature = null;
		IJavaObjectInstance component = (IJavaObjectInstance) getModel();
		JavaClass modelType = (JavaClass) component.eClass();
		// Hard coded string properties to direct edit.
		// If more than one is available, it'll choose the first in the list
		// below
		feature = modelType.getEStructuralFeature("text"); //$NON-NLS-1$
		if (feature != null) {
			IPropertySource source = (IPropertySource) getAdapter(IPropertySource.class);
			return PropertySourceAdapter.getDescriptorForID(source, feature);
		} else
			return null;
	}

	protected IFigure createFigure() {
		Figure figure = new Figure();
		figure.setOpaque(false);
		figure.setBackgroundColor(ColorConstants.cyan);
		figure.setBorder(new OutlineBorder());
		return figure;
	}

	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT && sfDirectEditProperty != null)
			performDirectEdit();
	}

	private void performDirectEdit() {
		if (manager == null)
			manager = new CDEDirectEditManager(this, new BeanDirectEditCellEditorLocator(getFigure()), sfDirectEditProperty);
		manager.show();
	}
}

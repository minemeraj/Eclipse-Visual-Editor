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
 *  $Revision: 1.4 $  $Date: 2005-03-21 22:48:09 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.*;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.java.core.BeanDirectEditManager;
import org.eclipse.ve.internal.java.core.BeanDirectEditPolicy;
 

/**
 * javax.swing.table.TableColumn does not inherit from java.awt.Component
 * The edit part should create a figure that has the height of the parent JTable plus the height
 * of the table header.
 * 
 * the width of header's width for the given column
 * and the x based on the header x for the given column
 * and the y based on subtracting the header height from the top of the parent JTable
 * @since 1.0.0
 */
public class TableColumnGraphicalEditPart extends AbstractGraphicalEditPart implements IDirectEditableEditPart {
	
	protected EStructuralFeature sfDirectEditProperty = null;
	protected DirectEditManager manager = null;
	

	protected Rectangle bounds = null;
	public Rectangle getBounds() {
		return bounds;
	}
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	protected IFigure createFigure() {
		Figure figure = new Figure();
		figure.setOpaque(false);
		figure.setBackgroundColor(ColorConstants.cyan);
		figure.setBorder(new OutlineBorder());
		return figure;
	}
	
	public void refresh(){
		super.refresh();
		if (bounds != null) {
			getFigure().setBounds(getBounds());
		}
		
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
	
	private EStructuralFeature getDirectEditTargetProperty() {
		EStructuralFeature target = null;
		IJavaObjectInstance component = (IJavaObjectInstance) getModel();
		JavaClass modelType = (JavaClass) component.eClass();
		// Hard coded string properties to direct edit.
		target = modelType.getEStructuralFeature("headerValue"); //$NON-NLS-1$
		return target;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.jfc.core.IDirectEditableEditPart#getSfDirectEditProperty()
	 */
	public EStructuralFeature getSfDirectEditProperty() {
		return sfDirectEditProperty;
	}
	
	private void performDirectEdit() {
		if (manager == null)
			manager = new BeanDirectEditManager(this, TextCellEditor.class, new ComponentCellEditorLocator(getFigure()), sfDirectEditProperty);
		manager.show();
	}

	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT && sfDirectEditProperty != null)
			performDirectEdit();
	}
}

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
 * $RCSfile: TableTreeEditPart.java,v $ $Revision: 1.1 $ $Date: 2004-06-08 15:03:04 $
 */
package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPolicy;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

/**
 * Table has a relationship "columns" which holds its children which are org.eclipse.swt.widgets.TableColumn instances
 */
public class TableTreeEditPart extends ControlTreeEditPart {

	protected EStructuralFeature sfColumns;

	public TableTreeEditPart(Object model) {
		super(model);
	}

	private Adapter compositeAdapter = new EditPartAdapterRunnable() {
		public void run() {
			if (isActive())
				refreshChildren();
		}

		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sfColumns)
				queueExec(TableTreeEditPart.this);
		}
	};

	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(compositeAdapter);
	}

	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(compositeAdapter);
	}

	protected void createEditPolicies() {
		// The TreeContainerEditPolicy is the CDE one
		// We don't care about being a Container with components
		// We are just interested in showing the viewPortView as a child
		super.createEditPolicies();
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new org.eclipse.ve.internal.cde.core.TreeContainerEditPolicy(new TableContainerPolicy(
				EditDomain.getEditDomain(this))));
	}

	public List getChildJavaBeans() {
		return (List) ((EObject) getModel()).eGet(sfColumns);
	}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		ResourceSet rset = ((EObject) model).eResource().getResourceSet();
		sfColumns = JavaInstantiation.getSFeature(rset, SWTConstants.SF_TABLE_COLUMNS);
	}
}
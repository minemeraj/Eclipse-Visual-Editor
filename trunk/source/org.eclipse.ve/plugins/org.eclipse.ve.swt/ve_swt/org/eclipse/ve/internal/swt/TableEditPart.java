/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: TableEditPart.java,v $ $Revision: 1.1 $ $Date: 2005-06-15 20:19:21 $
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
public class TableEditPart extends ControlTreeEditPart {

	protected EStructuralFeature sfColumns;

	public TableEditPart(Object model) {
		super(model);
	}

	private Adapter compositeAdapter = new EditPartAdapterRunnable(this) {
		protected void doRun() {
			refreshChildren();
		}

		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sfColumns)
				queueExec(TableEditPart.this, "COLUMNS"); //$NON-NLS-1$
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

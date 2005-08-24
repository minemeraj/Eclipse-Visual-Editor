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
/*
 * $RCSfile: JPopupMenuTreeEditPart.java,v $ $Revision: 1.9 $ $Date: 2005-08-24 23:38:09 $
 */

package org.eclipse.ve.internal.jfc.core;

import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

/**
 * @author pwalker
 * 
 * Tree editpart for JPopupMenu. Similar to JMenuEditTreeEditPart
 */
public class JPopupMenuTreeEditPart extends ComponentTreeEditPart {
	protected EStructuralFeature sfItems;

	private Adapter containerAdapter = new EditPartAdapterRunnable(this) {
		protected void doRun() {
			refreshChildren();
		}

		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sfItems)
				queueExec(JPopupMenuTreeEditPart.this, "ITEMS"); //$NON-NLS-1$
		}
	};

	/**
	 * Constructor for JPopupMenuTreeEditPart.
	 * 
	 * @param model
	 */
	public JPopupMenuTreeEditPart(Object model) {
		super(model);
	}

	protected void createEditPolicies() {
		// The TreeContainerEditPolicy is the CDE one
		super.createEditPolicies();
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new org.eclipse.ve.internal.cde.core.TreeContainerEditPolicy(new JMenuContainerPolicy(
				EditDomain.getEditDomain(this))));
	}

	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(containerAdapter);
	}

	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(containerAdapter);
	}

	public List getChildJavaBeans() {
		return (List) ((EObject) getModel()).eGet(sfItems);
	}

	protected EditPart createChildEditPart(Object model) {
		EditPart ep = super.createChildEditPart(model);
		if (ep instanceof ComponentTreeEditPart) ((ComponentTreeEditPart) ep).setPropertySource(new NonBoundsBeanPropertySource((EObject) model));
		return ep;
	}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		JavaClass modelType = (JavaClass) ((EObject) model).eClass();
		sfItems = modelType.getEStructuralFeature("items"); //$NON-NLS-1$
	}
}

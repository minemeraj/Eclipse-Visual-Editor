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
 * $RCSfile: RootPaneContainerTreeEditPart.java,v $ $Revision: 1.9 $ $Date: 2005-08-24 23:38:09 $
 */
package org.eclipse.ve.internal.jfc.core;

import java.util.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;

/**
 * This is the tree editpart for any RootPaneContainer, such as JFrame. There is a Swing interface called RootPaneContainer, and this is the general
 * edit part for it.
 */
public class RootPaneContainerTreeEditPart extends ComponentTreeEditPart {

	private EStructuralFeature sf_contentPane;

	/**
	 * Constructor with the model argument
	 */
	public RootPaneContainerTreeEditPart(Object model) {
		super(model);
	}

	private Adapter containerAdapter = new EditPartAdapterRunnable(this) {
		protected void doRun() {
			refreshChildren();
		}

		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sf_contentPane)
				queueExec(RootPaneContainerTreeEditPart.this, "CONTENTPANE"); //$NON-NLS-1$
		}
	};

	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(containerAdapter);
	}

	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(containerAdapter);
	}

	protected EditPart createChildEditPart(Object model) {
		EditPart ep = super.createChildEditPart(model);
		((ComponentTreeEditPart) ep).setPropertySource(new NonBoundsBeanPropertySource((EObject) model));
		return ep;
	}

	/**
	 * Our children is the root pane. We must create one for now if it is not there
	 */
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new org.eclipse.ve.internal.cde.core.TreeContainerEditPolicy(createContainerEditPolicy()));
	}

	protected org.eclipse.ve.internal.cde.core.ContainerPolicy createContainerEditPolicy() {
		return new RootPaneContainerPolicy(EditDomain.getEditDomain(this));
	}

	public List getChildJavaBeans() {
		EObject model = (EObject) getModel();
		ArrayList result = new ArrayList();

		if (sf_contentPane != null) {
			if (model.eIsSet(sf_contentPane)) {
				// We have an explicit content pane, return it.
				// unless it is null - This can occur if the user has the line ivjJFrame.setContentPane(null)
				Object contentPane = model.eGet(sf_contentPane);
				if (contentPane != null && BeanProxyUtilities.getBeanProxy((IJavaInstance) contentPane) != null) {
					result.add(contentPane);
				}
			}
		}
		if (!result.isEmpty())
			return result;
		return Collections.EMPTY_LIST;
	}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		JavaClass modelType = (JavaClass) ((EObject) model).eClass();
		sf_contentPane = modelType.getEStructuralFeature("contentPane"); //$NON-NLS-1$
	}
}

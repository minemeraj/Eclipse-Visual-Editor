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
 * $RCSfile: RootPaneJMenuBarContainerTreeEditPart.java,v $ $Revision: 1.9 $ $Date: 2005-08-24 23:38:09 $
 */
package org.eclipse.ve.internal.jfc.core;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPolicy;

import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

/**
 * @author pwalker
 * 
 * To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates. To enable and disable the creation
 * of type comments go to Window>Preferences>Java>Code Generation.
 */
public class RootPaneJMenuBarContainerTreeEditPart extends RootPaneContainerTreeEditPart {

	private EStructuralFeature sf_jmenubar;

	/**
	 * Constructor for RootPaneJMenuBarContainerTreeEditPart.
	 * 
	 * @param model
	 */
	public RootPaneJMenuBarContainerTreeEditPart(Object model) {
		super(model);
	}

	private Adapter containerAdapter = new EditPartAdapterRunnable(this) {
		protected void doRun() {
			refreshChildren();
		}

		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sf_jmenubar)
				queueExec(RootPaneJMenuBarContainerTreeEditPart.this, "MENUBAR"); //$NON-NLS-1$
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

	/**
	 * Our children is the root pane. We must create one for now if it is not there
	 */
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new org.eclipse.ve.internal.cde.core.TreeContainerEditPolicy(createContainerEditPolicy()));
	}

	protected org.eclipse.ve.internal.cde.core.ContainerPolicy createContainerEditPolicy() {
		return new RootPaneJMenuBarContainerPolicy(EditDomain.getEditDomain(this));
	}

	/**
	 * Our logical child is the JMenuBar.
	 */
	public List getChildJavaBeans() {
		EObject model = (EObject) getModel();
		List result = super.getChildJavaBeans();

		// If there is a JMenuBar, include this in the list
		if (sf_jmenubar != null) {
			Object jmenuBar = model.eGet(sf_jmenubar);
			if (jmenuBar != null) {
				// Bit of a kludge. Superclass returns Collections.EMPTY_LIST for empty,
				// and we can't add to that one. So when empty, create a new one.
				if (!result.isEmpty())
					result.add(jmenuBar);
				else {
					result = Collections.singletonList(jmenuBar);
				}
			}
		}

		return result;
	}

	public void setModel(Object model) {
		super.setModel(model);
		JavaClass modelType = (JavaClass) ((EObject) model).eClass();
		sf_jmenubar = modelType.getEStructuralFeature("JMenuBar"); //$NON-NLS-1$
	}
}

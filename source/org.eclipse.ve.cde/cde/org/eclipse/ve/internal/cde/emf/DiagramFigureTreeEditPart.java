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
 * $RCSfile: DiagramFigureTreeEditPart.java,v $ $Revision: 1.7 $ $Date: 2005-08-24 23:12:48 $
 */
package org.eclipse.ve.internal.cde.emf;

import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;

import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.DiagramFigure;

import org.eclipse.ve.internal.cde.core.TreePrimaryDragRoleEditPolicy;

/**
 * TreeEditPart for a DiagramFigure where DiagramFigures are its children.
 */
public abstract class DiagramFigureTreeEditPart extends AbstractTreeEditPart {

	/**
	 * Subclasses need to call super.createEditPolicies so that this method gets called.
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new TreePrimaryDragRoleEditPolicy());
	}

	protected List getModelChildren() {
		DiagramFigure diagramFigure = (DiagramFigure) getModel();
		return diagramFigure.getChildFigures();
	}

	public void activate() {
		super.activate();
		((DiagramFigure) getModel()).eAdapters().add(diagramAdapter);
	}

	public void deactivate() {
		super.deactivate();
		((DiagramFigure) getModel()).eAdapters().remove(diagramAdapter);
	}

	protected DiagramFigureAdapter diagramAdapter = createModelAdapter();

	protected class DiagramFigureAdapter extends EditPartAdapterRunnable {
		public DiagramFigureAdapter() {
			super(DiagramFigureTreeEditPart.this);
		}

		protected void doRun() {
			refreshChildren();
		}

		/*
		 * This method may be overridden for more notifications, but super.notifyChanged() must be called from subclasses.
		 * Also if <code>refreshChildren</code> is not sufficient, it must provide its own runnable.
		 */
		public void notifyChanged(Notification msg) {
			if (msg.getFeatureID(DiagramFigure.class) == CDMPackage.DIAGRAM_FIGURE__CHILD_FIGURES)
				queueExec(DiagramFigureTreeEditPart.this, "CHILDREN"); //$NON-NLS-1$
		}
	}

	/*
	 * This may be overridden to return a subclass of DiagramAdapter.
	 */
	protected DiagramFigureAdapter createModelAdapter() {
		// Create an adaptor that is used to listen to changes from the
		// MOF model object. The MOF notifications are used by this edit part to stay updated.
		return new DiagramFigureAdapter();
	}
}

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
 * $RCSfile: DiagramFigureGraphicalEditPart.java,v $ $Revision: 1.3 $ $Date: 2005-02-15 23:17:58 $
 */
package org.eclipse.ve.internal.cde.emf;

import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.DiagramFigure;

/**
 * GraphicalEditPart for a DiagramFigure where DiagramFigures are its children.
 */
public abstract class DiagramFigureGraphicalEditPart extends AbstractGraphicalEditPart {

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
		public void run() {
			if (isActive())
				refreshChildren();
		}

		/*
		 * This method may be overridden for more notifications, but super.notifyChanged() must be called from subclasses.
		 * They also must provide their own runnable if <code>refreshChildren</code> is not sufficient.
		 */
		public void notifyChanged(Notification msg) {
			if (msg.getFeatureID(DiagramFigure.class) == CDMPackage.DIAGRAM_FIGURE__CHILD_FIGURES)
				queueExec(DiagramFigureGraphicalEditPart.this);
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

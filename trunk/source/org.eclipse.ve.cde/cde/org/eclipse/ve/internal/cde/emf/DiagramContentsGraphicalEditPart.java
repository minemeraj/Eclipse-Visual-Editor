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
 * $RCSfile: DiagramContentsGraphicalEditPart.java,v $ $Revision: 1.3 $ $Date: 2005-02-15 23:17:58 $
 */
package org.eclipse.ve.internal.cde.emf;

import java.util.List;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.Diagram;

import org.eclipse.ve.internal.cde.core.ContentsGraphicalEditPart;

/**
 * Contents GraphicalEditPart for a Diagram where DiagramFigures are its children. Subclasses will be used to determine the children editparts that
 * need to be created.
 */
public abstract class DiagramContentsGraphicalEditPart extends ContentsGraphicalEditPart {

	protected List getModelChildren() {
		Diagram diagram = (Diagram) getModel();
		return diagram.getFigures();
	}

	public void activate() {
		super.activate();
		((Diagram) getModel()).eAdapters().add(diagramAdapter);
	}

	public void deactivate() {
		super.deactivate();
		((Diagram) getModel()).eAdapters().remove(diagramAdapter);
	}

	protected DiagramAdapter diagramAdapter = createModelAdapter();

	protected class DiagramAdapter extends EditPartAdapterRunnable {
		public void run() {
			if (isActive())
				refreshChildren();
		}

		/*
		 * This method may be overridden for more notifications, but super.notifyChanged() must be called from subclasses.
		 */
		public void notifyChanged(Notification msg) {
			if (msg.getFeatureID(Diagram.class) == CDMPackage.DIAGRAM__FIGURES)
				queueExec(DiagramContentsGraphicalEditPart.this);
		}
	}

	/*
	 * This may be overridden to return a subclass of DiagramAdapter.
	 */
	protected DiagramAdapter createModelAdapter() {
		// Create an adaptor that is used to listen to changes from the
		// MOF model object. The MOF notifications are used by this edit part to stay updated.
		return new DiagramAdapter();
	}
}

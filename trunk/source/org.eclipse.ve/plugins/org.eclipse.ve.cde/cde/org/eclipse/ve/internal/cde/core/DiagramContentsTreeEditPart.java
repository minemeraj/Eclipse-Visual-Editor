package org.eclipse.ve.internal.cde.core;
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
 *  $RCSfile: DiagramContentsTreeEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */

import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.editparts.AbstractTreeEditPart;

import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.Diagram;

/**
 * Contents TreeEditPart for a Diagram where DiagramFigures are its children.
 * Subclasses will be used to determine the children editparts that need to be created.
 */
public abstract class DiagramContentsTreeEditPart extends AbstractTreeEditPart {
	
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

	protected class DiagramAdapter extends AdapterImpl {
		/*
		 * This method may be overridden for more notifications, but super.notifyChanged() must be called from subclasses.
		 */
		public void notifyChanged(Notification msg) {
			if (msg.getFeatureID(Diagram.class) == CDMPackage.DIAGRAM__FIGURES)
				refreshChildren();
		}		
	}
	
	/*
	 * This may be overridden to return a subclass of DiagramAdapter.
	 */
	protected DiagramAdapter createModelAdapter() {
		// Create an adaptor that is used to listen to changes from the
		// MOF model object.  The MOF notifications are used by this edit part to stay updated.
		return new DiagramAdapter();
	}
}

package org.eclipse.ve.internal.cde.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ZoomOutAction.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-21 21:43:42 $ 
 */




import org.eclipse.ui.IEditorPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.gef.ui.actions.EditorPartAction;

public class ZoomOutAction extends EditorPartAction {
	public static final String ACTION_ID = "cdm.ZOOMOUT"; //$NON-NLS-1$

public ZoomOutAction(IEditorPart part) {
	super(part);
	setText(CDEMessages.ZoomOutAction_label); 
	setToolTipText(CDEMessages.ZoomOutAction_tooltip); 
	setId(ACTION_ID);
	setImageDescriptor(ImageDescriptor.createFromFile(getClass(),CDEMessages.ZoomOutAction_image)); 
	setEnabled(true);
}

public void run() {
	ZoomController zoomController = ZoomController.getZoomController(getEditorPart());
	if ( zoomController != null ) {
		int zoomValue = zoomController.getZoomValue();
		zoomController.setZoomValue(zoomValue - ZoomAction.ZOOM_DELTA);
	} else {
		// Kludge, don't know if we have zoom controller until after action is created.
		setEnabled(false);
	}
}
	/**
	 * @see org.eclipse.gef.ui.actions.EditorPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		return false;
	}

}
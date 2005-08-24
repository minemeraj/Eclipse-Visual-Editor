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
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: ZoomInAction.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:12:50 $ 
 */



import org.eclipse.ui.IEditorPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.gef.ui.actions.EditorPartAction;

public class ZoomInAction extends EditorPartAction {
	public static final String ACTION_ID = "cdm.ZOOMIN"; //$NON-NLS-1$
		
public ZoomInAction(IEditorPart part) {
	super(part);
	setText(CDEMessages.ZoomInAction_label); 
	setToolTipText(CDEMessages.ZoomInAction_tooltip); 
	setId(ACTION_ID);
	setImageDescriptor(ImageDescriptor.createFromFile(getClass(),CDEMessages.ZoomInAction_image)); 
	setEnabled(true);
}

public void run() {
	ZoomController zoomController = ZoomController.getZoomController(getEditorPart());
	if ( zoomController != null ) {
		int zoomValue = zoomController.getZoomValue();
		zoomController.setZoomValue(zoomValue + ZoomAction.ZOOM_DELTA);
	} else {
		// Kludge, don't know if we have a zoom controller until after action created.
		setEnabled(false)	;
	}
}
	/**
	 * @see org.eclipse.gef.ui.actions.EditorPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		return false;
	}

}

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
 *  $RCSfile: ZoomAction.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */


import org.eclipse.gef.ui.actions.EditorPartAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorPart;
/**
 * Action to request zoom prompt.
 */
public class ZoomAction extends EditorPartAction {
	public static final String ACTION_ID = "cdm.ZOOM"; //$NON-NLS-1$
	public static int ZOOM_DELTA = 20; // The amount to increase and decrase zoom by

public ZoomAction(IEditorPart part) {
	super(part);
	setText(CDEMessages.getString("ZoomAction.label")); //$NON-NLS-1$
	setToolTipText(CDEMessages.getString("ZoomAction.tooltip")); //$NON-NLS-1$
	setId(ACTION_ID);
	setImageDescriptor(ImageDescriptor.createFromFile(getClass(),CDEMessages.getString("ZoomAction.image"))); //$NON-NLS-1$
	setEnabled(true);
}

public void run() {

	ZoomController zoomController = ZoomController.getZoomController(getEditorPart());
	
	if (zoomController != null ) {
		ZoomHelperDialog dialog = new ZoomHelperDialog(
			getEditorPart().getSite().getWorkbenchWindow().getShell(), 
			zoomController.getZoomValue() );
			dialog.open();
			int zoomValue = dialog.getZoomValue();
			if (zoomValue != ZoomHelperDialog.NO_CHANGE)
				zoomController.setZoomValue(zoomValue);
	}
}

	/**
	 * @see org.eclipse.gef.ui.actions.EditorPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		return false;
	}

}
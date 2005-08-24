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
 *  $RCSfile: ZoomAction.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:12:49 $ 
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
	setText(CDEMessages.ZoomAction_label); 
	setToolTipText(CDEMessages.ZoomAction_tooltip); 
	setId(ACTION_ID);
	setImageDescriptor(ImageDescriptor.createFromFile(getClass(),CDEMessages.ZoomAction_image)); 
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

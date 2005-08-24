/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CDESelectionTool.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:12:49 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Cursor;
 

/**
 * CDE Selection tool. This is for use within CDE to handle:
 * <bl>
 * <li>Uses the CDEUtilities.calculateCursor to return the appropriate cursor for the state of the domain.
 * <li>Uses the CDEUtilities.getHoldState when redirecting to the drag trackers so that if the hold state is not ready it will not let the drag tracker
 * handle it and instead put up the appropriate cursor.
 * </bl>
 * 
 * @see org.eclipse.ve.internal.cde.core.CDEUtilities#calculateCursor(EditDomain)
 * @see org.eclipse.ve.internal.cde.core.CDEUtilities#getHoldState(EditDomain)
 * @see org.eclipse.ve.internal.cde.core.CDECreationTool
 * @since 1.0.0
 */
public class CDESelectionTool extends SelectionTool {

	/* (non-Javadoc)
	 * @see org.eclipse.gef.Tool#mouseMove(org.eclipse.swt.events.MouseEvent, org.eclipse.gef.EditPartViewer)
	 */
	public void mouseMove(MouseEvent me, EditPartViewer viewer) {
		super.mouseMove(me, viewer);
		if (getDragTracker() != null && CDEUtilities.getHoldState((EditDomain) getDomain()) != ModelChangeController.READY_STATE)
			refreshCursor();	// Get the appropriate cursor up.		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.Tool#mouseDrag(org.eclipse.swt.events.MouseEvent, org.eclipse.gef.EditPartViewer)
	 */
	public void mouseDrag(MouseEvent e, EditPartViewer viewer) {
		super.mouseDrag(e, viewer);
		if (getDragTracker() != null && CDEUtilities.getHoldState((EditDomain) getDomain()) != ModelChangeController.READY_STATE)
			refreshCursor();	// Get the appropriate cursor up.		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.AbstractTool#refreshCursor()
	 */
	protected void refreshCursor() {
		if (getDragTracker() != null && CDEUtilities.getHoldState((EditDomain) getDomain()) != ModelChangeController.READY_STATE) {
			// Can't use super.refreshCursor because that will send to drag tracker, but while in accessible drag
			// we don't want drag tracker to put it up if bad cursor.
			if (isActive())
				setCursor(calculateCursor());
		}
		super.refreshCursor();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.AbstractTool#handleKeyUp(org.eclipse.swt.events.KeyEvent)
	 */
	protected boolean handleKeyUp(KeyEvent e) {
		boolean result = super.handleKeyUp(e);
		if (getDragTracker() != null && CDEUtilities.getHoldState((EditDomain) getDomain()) != ModelChangeController.READY_STATE)
			refreshCursor();	// Get the appropriate cursor up.
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.AbstractTool#calculateCursor()
	 */
	protected Cursor calculateCursor() {
		if (getDragTracker() != null) {
			// Only want to check if actively tracking or doing accessible drag. Regular selection is ok.
			Cursor result = CDEUtilities.calculateCursor((EditDomain) getDomain());
			if (result != null)
				return result;
		}
		return super.calculateCursor();
	}
}

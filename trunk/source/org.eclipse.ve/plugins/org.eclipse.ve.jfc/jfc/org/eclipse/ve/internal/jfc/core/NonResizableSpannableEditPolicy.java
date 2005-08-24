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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: NonResizableSpannableEditPolicy.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:09 $ 
 */

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.ResizeTracker;


public class NonResizableSpannableEditPolicy extends NonResizableEditPolicy {
	protected GridBagLayoutEditPolicy layoutEditPolicy;
	/**
	 * 
	 */
	public NonResizableSpannableEditPolicy(GridBagLayoutEditPolicy layoutEditPolicy) {
		super();
		this.layoutEditPolicy = layoutEditPolicy;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#createSelectionHandles()
	 */
	protected List createSelectionHandles() {
		int numChildrenSelected = 0;
		
		Iterator children = getHost().getParent().getChildren().iterator();
		while(children.hasNext()){
			EditPart child = (EditPart) children.next();
			if(child.getSelected() == EditPart.SELECTED_PRIMARY || child.getSelected() == EditPart.SELECTED)
				numChildrenSelected++;
		}
		
		List nonResizeHandles = super.createSelectionHandles();
		if (getHost().getSelected() == EditPart.SELECTED_PRIMARY && numChildrenSelected == 1) {
			nonResizeHandles.add(createHandle((GraphicalEditPart) getHost(), PositionConstants.EAST));
			nonResizeHandles.add(createHandle((GraphicalEditPart) getHost(), PositionConstants.SOUTH));
		}
		return nonResizeHandles;
	}
	private Handle createHandle(GraphicalEditPart owner, int direction) {
		GridBagSpanHandle handle = new GridBagSpanHandle(owner, direction);
		handle.setDragTracker(new ResizeTracker(owner, direction));
		return handle;
	}
	public Command getCommand(Request request) {
		if (REQ_RESIZE.equals(request.getType()))
			return getSpanCommand((ChangeBoundsRequest)request);

		return super.getCommand(request);
	}

	private Command getSpanCommand(ChangeBoundsRequest request) {
		List editParts = request.getEditParts();
		if (editParts.isEmpty() || editParts.size() > 1) {
			return UnexecutableCommand.INSTANCE;
		}
		ChangeBoundsRequest req = createSpanRequest(request);
		return getHost().getParent().getCommand(req);
	}
	/*
	 * Return a ChangeBoundsRequest with type REQ_GRIDBAGLAYOUT_SPAN from a REQ_RESIZE ChangeBoundsRequest
	 */
	private ChangeBoundsRequest createSpanRequest(ChangeBoundsRequest request) {
		ChangeBoundsRequest req = new ChangeBoundsRequest(GridBagLayoutEditPolicy.REQ_GRIDBAGLAYOUT_SPAN);
		req.setEditParts(getHost());
		req.setMoveDelta(request.getMoveDelta());
		req.setSizeDelta(request.getSizeDelta());
		req.setLocation(request.getLocation());
		req.setResizeDirection(request.getResizeDirection());
		return req;
	}
	
	public void eraseSourceFeedback(Request request) {
		if (REQ_RESIZE.equals(request.getType())) {
			layoutEditPolicy.eraseTargetFeedback(request);
			eraseChangeBoundsFeedback((ChangeBoundsRequest) request);
		} else
			super.eraseSourceFeedback(request);
	}
	public void showSourceFeedback(Request request) {
		if (REQ_RESIZE.equals(request.getType())) {
			if (request instanceof ChangeBoundsRequest && ((ChangeBoundsRequest)request).getEditParts().size() == 1) {
				layoutEditPolicy.showSpanTargetFeedback(createSpanRequest((ChangeBoundsRequest)request));
				showChangeBoundsFeedback((ChangeBoundsRequest) request);
			}
		} else
			super.showSourceFeedback(request);
	}

	public boolean understandsRequest(Request request) {
		if (REQ_RESIZE.equals(request.getType()))
			return true;
		return super.understandsRequest(request);
	}
}

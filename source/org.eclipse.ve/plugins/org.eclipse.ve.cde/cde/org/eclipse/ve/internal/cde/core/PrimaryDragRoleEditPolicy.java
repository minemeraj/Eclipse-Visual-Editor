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
 *  $RCSfile: PrimaryDragRoleEditPolicy.java,v $
 *  $Revision: 1.6 $  $Date: 2005-06-08 23:08:35 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;

/**
 * The Primary Drag Role EditPolicy in CDE that the alignment/distribution requests.
 * 
 * It forwards adaptable requests to the dragRolePolicy so it can handle accessibility.
 */
public class PrimaryDragRoleEditPolicy implements EditPolicy, IAdaptable {
	protected EditPart host;
	protected EditPolicy dragRolePolicy;
	protected boolean allowAlignment;
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (dragRolePolicy instanceof IAdaptable)
			return ((IAdaptable) dragRolePolicy).getAdapter(adapter);
		else
			return null;
	}
	
	public PrimaryDragRoleEditPolicy(EditPolicy dragRolePolicy, boolean allowAlignment) {
		this.dragRolePolicy = dragRolePolicy;
		this.allowAlignment = allowAlignment;
	}
	
	public void activate() {
		dragRolePolicy.activate();
	}
	
	public void deactivate() {
		dragRolePolicy.deactivate();
	}
	
	public void eraseSourceFeedback(Request request) {
		dragRolePolicy.eraseSourceFeedback(request);
	}
	
	public void eraseTargetFeedback(Request request) {
		dragRolePolicy.eraseTargetFeedback(request);
	}
	
	public Command getCommand(Request request) {
		if (allowAlignment && RequestConstantsCDE.REQ_ALIGNMENT.equals(request.getType()))
			return getAlignmentCommand(request);
		if (allowAlignment && RequestConstantsCDE.REQ_DISTRIBUTE.equals(request.getType()))
			return getDistributeCommand(request);
		if (allowAlignment && RequestConstantsCDE.REQ_RESTORE_PREFERRED_SIZE.equals(request.getType()))
			return getRestorePreferredSizeCommand(request);
			
		return dragRolePolicy.getCommand(request);
	}
	
	protected Command getAlignmentCommand(Request request) {
		AlignmentCommandRequest alignReq = (AlignmentCommandRequest) request;
		AlignmentChildCommandRequest req = new AlignmentChildCommandRequest(host, alignReq);
		return host.getParent().getCommand(req);
	}
	
	protected Command getDistributeCommand(Request request) {
		DistributeCommandRequest distReq = (DistributeCommandRequest) request;
		DistributeChildCommandRequest req = new DistributeChildCommandRequest(host, distReq);
		return host.getParent().getCommand(req);
	}
	
	protected Command getRestorePreferredSizeCommand(Request request) {
		ChildRequest req = new ChildRequest(RequestConstantsCDE.REQ_RESTORE_PREFERRED_SIZE_CHILD, host);
		return host.getParent().getCommand(req);
	}
	
	public EditPart getTargetEditPart(Request request) {
		return dragRolePolicy.getTargetEditPart(request);
	}
	
	public void setHost(EditPart editpart) {
		host = editpart;
		dragRolePolicy.setHost(editpart);
	}
	
	public void showSourceFeedback(Request request) {
		dragRolePolicy.showSourceFeedback(request);
	}
	
	public void showTargetFeedback(Request request) {
		dragRolePolicy.showTargetFeedback(request);
	}
	
	public boolean understandsRequest(Request request) {
		return dragRolePolicy.understandsRequest(request);
	}
	/**
	 * @see org.eclipse.gef.EditPolicy#getHost()
	 */
	public EditPart getHost() {
		return host;
	}

}

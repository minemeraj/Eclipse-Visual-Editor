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
 *  $RCSfile: FlowLayoutEditPolicy.java,v $
 *  $Revision: 1.8 $  $Date: 2006-02-19 01:32:34 $ 
 */

import java.util.Collections;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.ui.IActionFilter;

import org.eclipse.ve.internal.cde.core.CustomizeLayoutPage;
import org.eclipse.ve.internal.cde.core.CustomizeLayoutWindowAction;

import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

/**
 * Layout input policy for handling the Flow layout of a Container.
 */
public class FlowLayoutEditPolicy extends org.eclipse.ve.internal.cde.core.FlowLayoutEditPolicy implements IActionFilter {
	
	public final static String LAYOUT_ID = "java.awt.FlowLayout"; //$NON-NLS-1$
	
	protected FlowLayoutPolicyHelper helper = new FlowLayoutPolicyHelper();

	/**
	 * Create with the container policy for handling DiagramFigures.
	 */
	public FlowLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		super(containerPolicy);
		setHorizontal(Boolean.TRUE);
		helper.setContainerPolicy(containerPolicy);
	}
	
	public void activate() {
		super.activate();
		CustomizeLayoutWindowAction.addLayoutCustomizationPage(getHost().getViewer(), FlowLayoutLayoutPage.class);
	}
	
	/**
	 * The child editpart is about to be added to the parent.
	 */
	public Command createAddCommand(EditPart childEditPart, EditPart before) {

		// We need to create a constraint and send this and the child over to the container policy.
		Object child = childEditPart.getModel();
		return helper.getAddChildrenCommand(Collections.singletonList(child), Collections.singletonList(null), before != null ? before.getModel() : null).getCommand();
	}
	protected Command getCreateCommand(CreateRequest aRequest) {
		Object child = aRequest.getNewObject();
		EditPart before = getInsertionReference(aRequest);
		return helper.getCreateChildCommand(child, null, before != null ? before.getModel() : null).getCommand();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionFilter#testAttribute(java.lang.Object, java.lang.String, java.lang.String)
	 */
	public boolean testAttribute(Object target, String name, String value) {
		if (name.startsWith(CustomizeLayoutPage.LAYOUT_POLICY_KEY) && value.equals(LAYOUT_ID))
			return true;
		
		return false;
	}
}

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
package org.eclipse.ve.examples.cdm.dept.ui;
/*
 *  $RCSfile: ManagedConnectionEditPart.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:16:43 $ 
 */

import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.gef.EditPolicy;
import org.eclipse.ve.internal.cde.core.VisualBendpointEditPolicy;
/**
 * Edit Part for the Manager to Department connection.
 */
public class ManagedConnectionEditPart extends AbstractConnectionEditPart {
	
	public void activate() {
		super.activate();
		installConnectionEditPolicy();
	}
	
	protected void installConnectionEditPolicy() {
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new VisualBendpointEditPolicy());
	}
	
	protected IFigure createFigure() {
		if (getModel() == null)
			return null;
		PolylineConnection conn = new PolylineConnection();
		conn.setSourceDecoration(null);
		PolygonDecoration arrow = new PolygonDecoration();
		arrow.setTemplate(PolygonDecoration.TRIANGLE_TIP);
		arrow.setScale(5,2.5);
		conn.setTargetDecoration(arrow);
		return conn;
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
	}

}

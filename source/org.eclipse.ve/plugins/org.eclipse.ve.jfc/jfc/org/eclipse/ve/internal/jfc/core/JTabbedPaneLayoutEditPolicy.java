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
 *  $RCSfile: JTabbedPaneLayoutEditPolicy.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:09 $ 
 */

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.graphics.Color;

import org.eclipse.ve.internal.cde.core.EditDomain;

public class JTabbedPaneLayoutEditPolicy extends LayoutEditPolicy {
	protected JTabbedPaneContainerPolicy containerPolicy;

	public JTabbedPaneLayoutEditPolicy(EditDomain anEditDomain) {
		containerPolicy = new JTabbedPaneContainerPolicy(anEditDomain);
	}
	public void activate() {
		super.activate();
		containerPolicy.setContainer(getHost().getModel());
	}
	public void deactivate() {
		super.deactivate();
		containerPolicy.setContainer(null);
	}
	public EditPolicy createChildEditPolicy(EditPart aChild) {
		return new NonResizableEditPolicy();
	}
	/**
	 * Add of children requested.
	 */
	protected Command getAddCommand(Request request) {
		Command addContributionCmd = containerPolicy.getCommand(request);
		if (addContributionCmd == null)
			return UnexecutableCommand.INSTANCE; // It can't be added.
		return addContributionCmd;
	}

	/**
	 * A new child is being created in this container.
	 */
	protected Command getCreateCommand(CreateRequest request) {
		Command createContributionCmd = containerPolicy.getCommand(request);
		if (createContributionCmd == null)
			return UnexecutableCommand.INSTANCE; // It can't be created.
		return createContributionCmd;
	}

	/**
	 * getDeleteDependantCommand method comment.
	 */
	protected Command getDeleteDependantCommand(Request request) {
		Command deleteContributionCmd = containerPolicy.getCommand(request);
		if (deleteContributionCmd == null)
			return UnexecutableCommand.INSTANCE; // It can't be created.
		return deleteContributionCmd;
	}
	/**
	 * getMoveChildCommand method comment.
	 * We don't perform move/resize since we don't know how.
	 */
	protected Command getMoveChildrenCommand(Request request) {
		return null;
	}
	/**
	 * getOrphanChildCommand. To orphan, we delete only the child. We don't
	 * delete the subpart since the part is not going away, just somewhere's
	 * else. So the subpart stays.
	 */
	protected Command getOrphanChildrenCommand(Request request) {
		Command orphanContributionCmd = containerPolicy.getCommand(request);
		if (orphanContributionCmd == null)
			return UnexecutableCommand.INSTANCE; // It can't be created.
		return orphanContributionCmd;
	}

	/**
	 * The figure used to provide drop feedback for the JTabbedPane.
	 * 
	 * @since 1.1.0
	 */
	protected static class JTabbedPaneLayoutFeedback extends RectangleFigure {
		
		/**
		 * Create the feedback.
		 * 
		 * 
		 * @since 1.1.0
		 */
		public JTabbedPaneLayoutFeedback() {
			setOutline(false);
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.draw2d.RectangleFigure#fillShape(org.eclipse.draw2d.Graphics)
		 */
		protected void fillShape(Graphics graphics) {
			Rectangle b = getBounds().getExpanded(-3, -3);	// And shrink by 2.
			int tabHeight = Math.min((int) Math.round(b.height*.3), 20);
			int tabWidth = Math.min((int) Math.round(b.width*.2)-3, 20);
			
			int x_NewTab = b.x+2*tabWidth+7;	// + is to allow a gap between the tabs.
			int x_EndNewTab = b.x+3*tabWidth+7;
			int[] newTabShape = new int[] {
					b.x,b.y+b.height,	// Lower Left
					b.x,b.y+tabHeight,
					x_NewTab,b.y+tabHeight,
					x_NewTab,b.y+tabHeight/3,
					x_NewTab+tabWidth/3, b.y,
					x_EndNewTab,b.y,
					x_EndNewTab,b.y+tabHeight,
					b.x+b.width,b.y+tabHeight,
					b.x+b.width,b.y+b.height
			};
			
			// First XOR out the original background so it it stands out.
			graphics.setXORMode(true);
			graphics.fillRectangle(b);
			graphics.setXORMode(false);
			
			graphics.setForegroundColor(ColorConstants.black);
			
			Color incomingBackground = graphics.getBackgroundColor();			
			
			// Draw and fill the new tab and panel outline.
			graphics.setBackgroundColor(ColorConstants.lightGray);
			graphics.fillPolygon(newTabShape);
			graphics.drawPolygon(newTabShape);
			
			// Fill the panel area of new tab with ligher (original) color.
			graphics.setBackgroundColor(incomingBackground);
			Rectangle panelRect = new Rectangle(b.x, b.y+tabHeight, b.width, b.height-tabHeight).shrink(2,2);
			graphics.fillRectangle(panelRect);
			graphics.drawRectangle(panelRect);						
		}
	}

	protected Figure tabbedLayoutFeedback;
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#showLayoutTargetFeedback(org.eclipse.gef.Request)
	 */
	protected void showLayoutTargetFeedback(Request request) {
		if (tabbedLayoutFeedback == null) {
			tabbedLayoutFeedback = new JTabbedPaneLayoutFeedback();
			IFigure f = ((GraphicalEditPart) getHost()).getFigure();
			tabbedLayoutFeedback.setBounds(f.getBounds());
			addFeedback(tabbedLayoutFeedback);
		}
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#eraseLayoutTargetFeedback(org.eclipse.gef.Request)
	 */
	protected void eraseLayoutTargetFeedback(Request request) {
		if (tabbedLayoutFeedback != null) {
			removeFeedback(tabbedLayoutFeedback);
			tabbedLayoutFeedback = null;
		}
	}
}

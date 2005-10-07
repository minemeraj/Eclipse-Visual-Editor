/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ActionBarGraphicalEditPart.java,v $
 *  $Revision: 1.2 $  $Date: 2005-10-07 15:34:04 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;

/*
 * Special editpart for the action bar to host the editpart contributors
 */
public class ActionBarGraphicalEditPart extends AbstractGraphicalEditPart {

	IFigure actionBarFigure = null;			// Action bar that contains the editpart contributor figures
	Polyline connectionFigure = null;		// Connection from the host figure to the action bar
	RectangleFigure anchorFigure = null;	// Small rectangle to anchor the connection to in the host figure
	List actionBarChildren = null;

	static final Color ACTIONBAR_BACKGROUND_COLOR = new Color(null, 255, 255, 220); // very light yellow
	static final int ACTIONBAR_FIGURE_MARGIN = 5;

	public void activate() {
		super.activate();
		getLayer(LayerConstants.HANDLE_LAYER).add(getFigure());
	}

	public void deactivate() {
		super.deactivate();
		getLayer(LayerConstants.HANDLE_LAYER).remove(getFigure());
		if (connectionFigure != null) {
			getLayer(LayerConstants.HANDLE_LAYER).remove(connectionFigure);
			getLayer(LayerConstants.HANDLE_LAYER).remove(anchorFigure);
		}
	}

	public void addActionBarChildren(List actionBarChildren) {
		this.actionBarChildren = actionBarChildren;
	}

	public void show(Rectangle hostBounds, int orientation) {
		Rectangle bounds = actionBarFigure.getBounds();
		actionBarFigure.setLocation(new Point(hostBounds.x + hostBounds.width / 2 + 3, hostBounds.y - bounds.height - 7));
		actionBarFigure.setVisible(true);
		if (connectionFigure == null && anchorFigure == null) {
			connectionFigure = new Polyline() {

				protected void outlineShape(Graphics g) {
					try {
						g.setAntialias(SWT.ON); // This makes the connection line look smooth
					} catch (SWTException e) {
						// For OS platforms that don't support Antialias
					}
					super.outlineShape(g);
				}
			};
			connectionFigure.setLineWidth(1);
			anchorFigure = new RectangleFigure();
			anchorFigure.setSize(5, 5);
			anchorFigure.setBackgroundColor(ColorConstants.lightGreen);
			getLayer(LayerConstants.HANDLE_LAYER).add(connectionFigure);
			getLayer(LayerConstants.HANDLE_LAYER).add(anchorFigure);
		}
		PointList pl = new PointList(new int[] { hostBounds.x + hostBounds.width / 5, hostBounds.y + hostBounds.height / 5, bounds.x - 8,
				bounds.y + bounds.height / 2 + 5, bounds.x, bounds.y + bounds.height / 2 + 5});
		connectionFigure.setPoints(pl);
		connectionFigure.setVisible(true);
		anchorFigure.setLocation(new Point(hostBounds.x + hostBounds.width / 5 - 3, hostBounds.y + hostBounds.height / 5 + 1));
		anchorFigure.setVisible(true);
	}

	public void hide() {
		actionBarFigure.setVisible(false);
		connectionFigure.setVisible(false);
		anchorFigure.setVisible(false);
	}

	protected IFigure createFigure() {
		actionBarFigure = new RoundedRectangle() {

			protected void fillShape(Graphics graphics) {
				Rectangle rect = getBounds().getCopy().expand(-1, -1);
				graphics.fillGradient(rect, false);
			}

			protected void outlineShape(Graphics graphics) {
				graphics.setForegroundColor(ColorConstants.darkGray);
				Rectangle f = Rectangle.SINGLETON;
				Rectangle r = getBounds();
				f.x = r.x + lineWidth / 2;
				f.y = r.y + lineWidth / 2;
				f.width = r.width - lineWidth;
				f.height = r.height - lineWidth;
				graphics.drawRoundRectangle(f, corner.width, corner.height);
			}
		};
		// Use XY positioning
		XYLayout xylayout = new XYLayout();
		actionBarFigure.setLayoutManager(xylayout);

		// For the fill gradient, left to right go from white to very light yellow to show depth
		actionBarFigure.setForegroundColor(ColorConstants.buttonLightest);
		actionBarFigure.setBackgroundColor(ACTIONBAR_BACKGROUND_COLOR);

		actionBarFigure.setVisible(false);
		return actionBarFigure;
	}

	protected void createEditPolicies() {
		// Need a layout policy in order to provide selection capability on the action edit parts
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new FlowLayoutEditPolicy(new ActionBarContainerPolicy(EditDomain.getEditDomain(this))) {

			protected void showLayoutTargetFeedback(Request request) {
				// Don't do anything... don't want to move around the action bar editparts
			}

			protected boolean isHorizontal() {
				return true;
			}
		});
	}

	protected void refreshChildren() {
		super.refreshChildren();
		setChildrenConstraints();
		calculateFigureSize();
	}

	/*
	 * Calculate and set the action bar size
	 */
	private void calculateFigureSize() {
		List children = getFigure().getChildren();
		if (children.isEmpty()) { return; }
		int abWidth = ACTIONBAR_FIGURE_MARGIN;
		int abHeight = 0;
		for (int i = 0; i < children.size(); i++) {
			Dimension size = ((IFigure) children.get(i)).getPreferredSize();
			abWidth += size.width + ACTIONBAR_FIGURE_MARGIN;
			if (size.height > abHeight)
				abHeight = size.height;
		}
		getFigure().setSize(abWidth + 25, abHeight + ACTIONBAR_FIGURE_MARGIN * 2);
	}

	private void setChildrenConstraints() {
		List children = getFigure().getChildren();
		LayoutManager lm = getFigure().getLayoutManager();
		if (children.isEmpty() || lm == null) { return; }
		int abWidth = ACTIONBAR_FIGURE_MARGIN;
		for (int i = 0; i < children.size(); i++) {
			IFigure childFigure = (IFigure) children.get(i);
			if (lm.getConstraint(childFigure) == null)
				getFigure().setConstraint(childFigure,
						new Rectangle(abWidth, ACTIONBAR_FIGURE_MARGIN, childFigure.getPreferredSize().width, childFigure.getPreferredSize().height));
			abWidth += childFigure.getPreferredSize().width + ACTIONBAR_FIGURE_MARGIN;
		}
	}

	protected EditPart createChild(Object model) {
		return (EditPart) model;
	}

	protected List getModelChildren() {
		return actionBarChildren == null ? Collections.EMPTY_LIST : actionBarChildren;
	}

	/*
	 * Dummy container policy required for the ActionBar editpart
	 */
	class ActionBarContainerPolicy extends ContainerPolicy {

		public ActionBarContainerPolicy(EditDomain domain) {
			super(domain);
		}

		public Command getAddCommand(List children, Object positionBeforeChild) {
			return null;
		}

		public Command getCreateCommand(Object child, Object positionBeforeChild) {
			return null;
		}

		public Command getDeleteDependentCommand(Object child) {
			return null;
		}

		protected Command getOrphanTheChildrenCommand(List children) {
			return null;
		}

		public Command getMoveChildrenCommand(List children, Object positionBeforeChild) {
			return null;
		}

	}
}

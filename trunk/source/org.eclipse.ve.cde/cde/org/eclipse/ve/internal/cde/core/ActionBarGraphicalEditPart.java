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
 *  $Revision: 1.11 $  $Date: 2005-12-19 17:21:49 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.util.*;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/*
 * Special editpart for the action bar to host the editpart contributors
 */
public class ActionBarGraphicalEditPart extends AbstractGraphicalEditPart {

	IFigure actionBarFigure = null; // Action bar that contains the editpart contributor figures
	ActionBarDecorationsFigure decorationsFigure = null; // Figure for highlighting the host & action bar figure
	List actionBarChildren = null;
	boolean actionBarOpen = false; // toggle for flyout capability
	Rectangle dividerGradientArea = null; // Area for left arrow when action bar is open
	private Rectangle hostBounds = null;

	static final Color ACTIONBAR_BACKGROUND_COLOR = new Color(null, 255, 255, 220); // very light yellow
	static final int ACTIONBAR_CHILD_FIGURE_MARGIN = 5;

	public void activate() {
		super.activate();
		getFigure().addMouseListener(new ActionBarMouseListener());
		getLayer(LayerConstants.HANDLE_LAYER).add(getFigure());
	}

	public void deactivate() {
		super.deactivate();
		getLayer(LayerConstants.HANDLE_LAYER).remove(getFigure());
		if (decorationsFigure != null) {
			getLayer(LayerConstants.HANDLE_LAYER).remove(decorationsFigure);
		}
	}

	public void addActionBarChildren(List actionBarChildren) {
		this.actionBarChildren = actionBarChildren;
	}

	public void show(Rectangle hostBounds, int orientation) {
		this.hostBounds = hostBounds;  
		actionBarFigure.setLocation(new Point(hostBounds.x + hostBounds.width - 8, hostBounds.y + 1));

		// The decorations figure is used to highlight the host figure, action bar figure,
		// and used to draw a shadow figure for the action bar.
		if (decorationsFigure == null) {
			decorationsFigure = new ActionBarDecorationsFigure();
			// Note: decorations figure must be added first so that action bar figure is drawn
			// over the shadow figure (part of the decorations figure)
			getLayer(LayerConstants.HANDLE_LAYER).remove(actionBarFigure);
			getLayer(LayerConstants.HANDLE_LAYER).add(decorationsFigure);
			getLayer(LayerConstants.HANDLE_LAYER).add(actionBarFigure);
		}
		decorationsFigure.setHostBounds(hostBounds.getCopy());

		// Keep action bar on top to avoid obscurring by selection handles from other editparts
		// if (getLayer(LayerConstants.HANDLE_LAYER).getChildren().size() > 2) {
		getLayer(LayerConstants.HANDLE_LAYER).remove(decorationsFigure);
		getLayer(LayerConstants.HANDLE_LAYER).remove(actionBarFigure);
		getLayer(LayerConstants.HANDLE_LAYER).add(decorationsFigure);
		getLayer(LayerConstants.HANDLE_LAYER).add(actionBarFigure);
		// }
		decorationsFigure.setVisible(true);
		actionBarFigure.setVisible(true);
	}

	public void hide() {
		actionBarOpen = false;
		actionBarFigure.setVisible(false);
		decorationsFigure.setVisible(false);
	}

	/*
	 * The figure for the action bar. This is constructed as a RoundedRectangle but the paintFigure is overriden to draw lots of other little figures
	 * depending on whether the action bar is opened or closed to simulate a fly-out capability.
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		actionBarFigure = new RoundedRectangle() {
			PointList actionBarStub = null;
			PointList rightArrow = null;
			PointList leftArrow = null;
			PointList divider = null;

			/*
			 * Set the bounds. 
			 * Setup the point lists for the various drawing figures within this figure 
			 * so we don't have to recreate point lists every time we paint. 
			 * - right and left arrows 
			 * - action divider for when the action bar is open. 
			 * - action bar stub for when the action bar is closed. 
			 * - gradient area where the arrows will be displayed so it looks like a 3d button
			 * 
			 * @see org.eclipse.draw2d.IFigure#setBounds(org.eclipse.draw2d.geometry.Rectangle)
			 */
			public void setBounds(Rectangle rect) {
				super.setBounds(rect);
				// Setup the action bar stub and arrow to show when action bar is closed
				actionBarStub = new PointList(6);
				actionBarStub.addPoint(bounds.x + 5, bounds.y + 1);
				actionBarStub.addPoint(bounds.x + bounds.width - 4, bounds.y + 1);
				actionBarStub.addPoint(bounds.x + bounds.width - 1, bounds.y + 4);
				actionBarStub.addPoint(bounds.x + bounds.width - 1, bounds.y + bounds.height - 4);
				actionBarStub.addPoint(bounds.x + bounds.width - 4, bounds.y + bounds.height - 1);
				actionBarStub.addPoint(bounds.x + 5, bounds.y + bounds.height - 1);
				rightArrow = new PointList(3);
				rightArrow.addPoint(bounds.x + bounds.width - 7, bounds.y + bounds.height / 2 - 3);
				rightArrow.addPoint(bounds.x + bounds.width - 4, bounds.y + bounds.height / 2);
				rightArrow.addPoint(bounds.x + bounds.width - 7, bounds.y + bounds.height / 2 + 3);

				// Setup the left arrow and action divider to show when the action bar is opened
				leftArrow = new PointList(3);
				leftArrow.addPoint(bounds.x + bounds.width - 5, bounds.y + bounds.height / 2 - 3);
				leftArrow.addPoint(bounds.x + bounds.width - 8, bounds.y + bounds.height / 2);
				leftArrow.addPoint(bounds.x + bounds.width - 5, bounds.y + bounds.height / 2 + 3);
				divider = new PointList(4);
				divider.addPoint(bounds.x + bounds.width - 10, bounds.y + 1);
				divider.addPoint(bounds.x + bounds.width - 12, bounds.y + 3);
				divider.addPoint(bounds.x + bounds.width - 12, bounds.y + bounds.height - 3);
				divider.addPoint(bounds.x + bounds.width - 10, bounds.y + bounds.height - 1);

				// Setup the bounds for the divider gradient area where the arrows reside
				dividerGradientArea = bounds.getCopy();
				dividerGradientArea.x = bounds.x + bounds.width - 12;
				dividerGradientArea.width = bounds.x + bounds.width - dividerGradientArea.x;
				dividerGradientArea.expand(-1, -1);
			}

			public void paintFigure(Graphics graphics) {
				if (actionBarOpen) {
					// Action bar is open. Show left arrow and action divider
					super.paintFigure(graphics);
					// Draw the gradient area where the left arrow button is located
					graphics.setForegroundColor(ColorConstants.white);
					graphics.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
					graphics.fillGradient(dividerGradientArea, false);
					// Draw the left arrow
					graphics.setLineWidth(2);
					graphics.setForegroundColor(ColorConstants.black);
					graphics.drawPolyline(leftArrow);
					// draw the divider
					// graphics.setLineWidth(1);
					graphics.setForegroundColor(ColorConstants.gray);
					graphics.drawPolyline(divider);
					graphics.setLineWidth(1);
					graphics.setForegroundColor(ColorConstants.black);
					outlineShape(graphics);
				} else {
					// Action bar is closed. Show action bar stub and right arrow
					graphics.setBackgroundColor(ColorConstants.white);
					// Draw the filler for action bar stub
					graphics.fillPolygon(actionBarStub);
					// Draw the gradient area where the right arrow button is located
					graphics.setForegroundColor(ColorConstants.white);
					graphics.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
					graphics.fillGradient(dividerGradientArea, false);
					// Draw the border around the stub to match the host figure's border
					graphics.setLineWidth(2);
					graphics.setForegroundColor(ColorConstants.orange);
					graphics.drawPolyline(actionBarStub);
					// Draw the left arrow
					graphics.setForegroundColor(ColorConstants.black);
					graphics.drawPolyline(rightArrow);
				}
			}

			/*
			 * Use a gradient on the action bar and go left to right and from white to very light yellow to show depth.
			 * 
			 * @see org.eclipse.draw2d.Shape#fillShape(org.eclipse.draw2d.Graphics)
			 */
			protected void fillShape(Graphics graphics) {
				graphics.setForegroundColor(ColorConstants.white);
				graphics.setBackgroundColor(ColorConstants.tooltipBackground);
				Rectangle rect = getBounds().getCopy().expand(-1, -1);
				rect.width -= dividerGradientArea.width;
				graphics.fillGradient(rect, false);
			}

		};
		// Use XY positioning
		XYLayout xylayout = new XYLayout();
		actionBarFigure.setLayoutManager(xylayout);

		actionBarFigure.setVisible(false);
		return actionBarFigure;
	}

	protected void createEditPolicies() {
		// Need a layout policy in order to provide selection capability on the selectable action edit parts
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new FlowLayoutEditPolicy() {

			protected void showLayoutTargetFeedback(Request request) {
				// Don't do anything... don't want to show insertion line or move around the action bar editparts
			}

			protected boolean isHorizontal() {
				return true;
			}

			protected Command createAddCommand(EditPart child, EditPart after) {
				return null;
			}

			protected Command createMoveChildCommand(EditPart child, EditPart after) {
				return null;
			}

			protected Command getCreateCommand(CreateRequest request) {
				return null;
			}

			protected Command getDeleteDependantCommand(Request request) {
				return null;
			}

			protected EditPolicy createChildEditPolicy(EditPart child) {
				if (child instanceof ActionBarActionEditPart)
					return new AbstractEditPolicy() {
					};
				return super.createChildEditPolicy(child);
			}
		});
	}

	protected void refreshChildren() {
		super.refreshChildren();
		refreshFigures();
	}

	/*
	 * Show/hide children based on whether the action bar is open or closed. 
	 * Set the children constraints used in the layout on the action bar.
	 * Set the overall action bar size based on the children preferred sizes
	 */
	protected void refreshFigures() {
		// Show or hide the children based on whether the action bar is open or closed.
		Iterator iter = actionBarFigure.getChildren().iterator();
		while (iter.hasNext()) {
			IFigure child = (IFigure) iter.next();
			if (child.isVisible() != actionBarOpen)
				child.setVisible(actionBarOpen);
		}

		setChildrenConstraints();
		calculateFigureSize();
	}

	/*
	 * Calculate and set the action bar size
	 */
	private void calculateFigureSize() {
		List children = getFigure().getChildren();
		if (children.isEmpty()) { return; }
		int abWidth = ACTIONBAR_CHILD_FIGURE_MARGIN;
		int abHeight = 0;
		for (int i = 0; i < children.size(); i++) {
			Dimension size = ((IFigure) children.get(i)).getPreferredSize();
			abWidth += size.width + ACTIONBAR_CHILD_FIGURE_MARGIN;
			if (size.height > abHeight)
				abHeight = size.height;
		}
		if (actionBarOpen)
			abWidth += 30;
		else
			abWidth = 15;
		getFigure().setSize(abWidth, abHeight + ACTIONBAR_CHILD_FIGURE_MARGIN * 2);
		
		// Reset the host bounds for the decorations figure so it will show the shadowing correctly
		if (decorationsFigure != null && hostBounds != null) {
			decorationsFigure.setHostBounds(hostBounds.getCopy());
		}
	}

	private void setChildrenConstraints() {
		List children = getFigure().getChildren();
		LayoutManager lm = getFigure().getLayoutManager();
		if (children.isEmpty() || lm == null) { return; }
		int abWidth = ACTIONBAR_CHILD_FIGURE_MARGIN;
		for (int i = 0; i < children.size(); i++) {
			IFigure childFigure = (IFigure) children.get(i);
			if (lm.getConstraint(childFigure) == null)
				getFigure().setConstraint(
						childFigure,
						new Rectangle(abWidth, ACTIONBAR_CHILD_FIGURE_MARGIN, childFigure.getPreferredSize().width,
								childFigure.getPreferredSize().height));
			abWidth += childFigure.getPreferredSize().width + ACTIONBAR_CHILD_FIGURE_MARGIN;
		}
	}

	protected EditPart createChild(Object model) {
		return (EditPart) model;
	}

	protected List getModelChildren() {
		return actionBarChildren == null ? Collections.EMPTY_LIST : actionBarChildren;
	}

	/*
	 * Mouse listener for the ActionBar figure. Open/close the action bar when the mouse is pressed within the small arrow
	 */
	private class ActionBarMouseListener extends MouseListener.Stub {

		public void mousePressed(MouseEvent me) {
			if (me.getSource() == actionBarFigure && dividerGradientArea != null && dividerGradientArea.contains(me.getLocation())) {
				actionBarOpen = !actionBarOpen;
				refreshFigures();
			}
		}
	}

	/*
	 * Figure used to decorate the border on the host figure and show a shadow under the action bar when it is shown in an open state.
	 */
	private class ActionBarDecorationsFigure extends Figure {
		PointList hostBorderPoints = null;
		private Rectangle hostBounds;

		public void paint(Graphics graphics) {
			// Draw the border on the host figure
			if (hostBorderPoints != null) {
				graphics.setForegroundColor(ColorConstants.orange);
				graphics.setLineWidth(2);
				graphics.drawPolyline(hostBorderPoints);
			}
			// Draw the action bar shadow if the action bar is open
			if (actionBarOpen) {
				graphics.setBackgroundColor(ColorConstants.darkGray);
				try {
					graphics.setAlpha(110); // This is for the shadow figure to allow some transparency
				} catch (SWTException e) {
					// For OS platforms that don't support alpha
				}
				Rectangle shadowRect = actionBarFigure.getBounds().getCopy().translate(13, 10);
				graphics.fillRoundRectangle(shadowRect, 8, 8);
			}
		}

		/*
		 * Set the host bounds. Calculate the points list for the border highlight on the hostfigure. 
		 * Set the bounds of this figure based on the union of the host bounds and action bar figure bounds.
		 */
		public void setHostBounds(Rectangle bounds) {
			hostBounds = bounds;
			// Points are added to host points list starting from the upper right corner and
			// moving counter-clockwise... leaving a gap for the action bar figure on the right.
			Rectangle afBounds = actionBarFigure.getBounds().getCopy();
			hostBorderPoints = new PointList(5);
			hostBorderPoints.addPoint(hostBounds.x + hostBounds.width - 2, hostBounds.y + 2);
			hostBorderPoints.addPoint(hostBounds.x + 2, hostBounds.y + 2);
			hostBorderPoints.addPoint(hostBounds.x + 2, hostBounds.y + hostBounds.height - 2);
			hostBorderPoints.addPoint(hostBounds.x + hostBounds.width - 2, hostBounds.y + hostBounds.height - 2);
			hostBorderPoints.addPoint(hostBounds.x + hostBounds.width - 2, hostBounds.y + afBounds.height);
			setBounds(hostBounds.getCopy().union(afBounds.translate(13,10)));
		}

		public boolean isOpaque() {
			return false;
		}
		// Return true only if within the action bar
		public boolean containsPoint(int x, int y) {
			return (actionBarFigure != null && actionBarFigure.getBounds().contains(x, y));
		}
	}
}

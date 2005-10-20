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
 *  $Revision: 1.6 $  $Date: 2005-10-20 17:37:48 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.util.Collections;
import java.util.List;

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

/*
 * Special editpart for the action bar to host the editpart contributors
 */
public class ActionBarGraphicalEditPart extends AbstractGraphicalEditPart {

	IFigure actionBarFigure = null;			// Action bar that contains the editpart contributor figures
	Polyline decorationsFigure = null;		// Figure for highlighting the host & action bar figure
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
		if (decorationsFigure != null) {
			getLayer(LayerConstants.HANDLE_LAYER).remove(decorationsFigure);
		}
	}

	public void addActionBarChildren(List actionBarChildren) {
		this.actionBarChildren = actionBarChildren;
	}

	public void show(Rectangle hostBounds, int orientation) {
		Rectangle bounds = actionBarFigure.getBounds();
		actionBarFigure.setLocation(new Point(hostBounds.x + hostBounds.width + 10, hostBounds.y - bounds.height - 14));

		// The decorations figure is used to highlight the host figure, action bar figure, and it's connection in green.
		// It is also used to draw a shadow figure for the action bar.
		if (decorationsFigure == null) {
			decorationsFigure = new Polyline() {
				PointList hostPoints = null;
				PointList actionBarPoints = null;
				PointList shadowPoints = null;

				protected void outlineShape(Graphics g) {
					try {
						g.setAntialias(SWT.ON); // This makes the connection line look smooth
					} catch (SWTException e) {
						// For OS platforms that don't support Antialias
					}
					g.setForegroundColor(ColorConstants.black);
					// Draw the green border around the host figure
					if (hostPoints != null) {
						g.setForegroundColor(ColorConstants.lightGreen);
						g.setLineWidth(2);
						g.drawPolyline(hostPoints);
					}
					// Draw the action bar shadow and the green border around the action bar figure
					if (actionBarPoints != null) {
						// draw the action figure shadow
						g.setBackgroundColor(ColorConstants.darkGray);
						Point fp = shadowPoints.getFirstPoint();
						Point lp = shadowPoints.getPoint(2);
						try {
							g.setAlpha(110); // This is for the shadow figure to allow some transparency
						} catch (SWTException e) {
							// For OS platforms that don't support alpha
						}
						g.fillRoundRectangle(new Rectangle(fp.x, fp.y, lp.x - fp.x - 4, lp.y - fp.y - 4), 12, 12);
						try {
							g.setAlpha(255); // Reset
						} catch (SWTException e) {
							// For OS platforms that don't support alpha
						}

						// now draw the green highlight border around the action bar figure
						g.setForegroundColor(ColorConstants.lightGreen);
						g.setLineWidth(2);
						fp = actionBarPoints.getFirstPoint();
						lp = actionBarPoints.getPoint(2);
//						g.drawRoundRectangle(new Rectangle(fp.x, fp.y, lp.x - fp.x, lp.y - fp.y), 8, 8);
					}
					// draw the connecting line between the host figure and the action bar figure
					g.setForegroundColor(ColorConstants.black);
					g.setLineWidth(1);
					g.setLineStyle(SWT.LINE_SOLID);
					PointList anchorLinePoints = new PointList(2);
					Point p1 = hostPoints.getPoint(3);
					Point p2 = actionBarPoints.getPoint(1);
					anchorLinePoints.addPoint(p1);
					anchorLinePoints.addPoint(p2.x + 2, p2.y - 2);
					g.drawPolyline(anchorLinePoints); 

					// draw the very light connecting line between the host figure and the action bar shadow
					g.setForegroundColor(ColorConstants.darkGray);
					PointList shadowAnchorPoints = new PointList(2);
					p1 = hostPoints.getPoint(3);
					p2 = shadowPoints.getPoint(1);
					shadowAnchorPoints.addPoint(p1.x + 2, p1.y + 6);
					shadowAnchorPoints.addPoint(p2.x + 2, p2.y - 8);
					g.setLineWidth(1);
					try {
						g.setAlpha(40); // Very light, almost transparent connecting line
					} catch (SWTException e) {
						// For OS platforms that don't support alpha
					}
					g.drawPolyline(shadowAnchorPoints);
				}

				/*
				 * points should contain 15 points... 
				 *    first 5 is the points for the host figure, 
				 *    next 5 is the points for the action bar figure, 
				 *    last 5 is the points for the action bar shadow.
				 */
				public void setPoints(PointList points) {
					super.setPoints(points);
					hostPoints = null;
					actionBarPoints = null;
					shadowPoints = null;
					if (points.size() == 15) {
						hostPoints = new PointList(5);
						actionBarPoints = new PointList(5);
						shadowPoints = new PointList(5);

						hostPoints.addPoint(points.getPoint(0).x + 2, points.getPoint(0).y + 2);
						hostPoints.addPoint(points.getPoint(1).x + 2, points.getPoint(1).y - 2);
						hostPoints.addPoint(points.getPoint(2).x - 2, points.getPoint(2).y - 2);
						hostPoints.addPoint(points.getPoint(3).x - 2, points.getPoint(3).y + 2);
						hostPoints.addPoint(points.getPoint(4).x + 2, points.getPoint(4).y + 2);

						actionBarPoints.addPoint(points.getPoint(5).x + 2, points.getPoint(5).y + 2);
						actionBarPoints.addPoint(points.getPoint(6).x + 2, points.getPoint(6).y - 2);
						actionBarPoints.addPoint(points.getPoint(7).x - 2, points.getPoint(7).y - 2);
						actionBarPoints.addPoint(points.getPoint(8).x - 2, points.getPoint(8).y + 2);
						actionBarPoints.addPoint(points.getPoint(9).x + 2, points.getPoint(9).y + 2);

						for (int i = 10; i < 15; i++) {
							shadowPoints.addPoint(points.getPoint(i));
						}
					}
				};
			};
			// Note: decorations figure must be added first so that action bar figure is drawn
			// over the shadow figure (part of the decorations figure)
			getLayer(LayerConstants.HANDLE_LAYER).remove(actionBarFigure);
			getLayer(LayerConstants.HANDLE_LAYER).add(decorationsFigure);
			getLayer(LayerConstants.HANDLE_LAYER).add(actionBarFigure);
		}
		// Set the points list that is used for drawing the green border around the host figure, action figure,
		// connection line, and for drawing the shadow figure.
		PointList pl = new PointList();
		// First five points is the host figure's points
		Rectangle hb = hostBounds.getCopy();
		// Points are added to points list starting from the upper left corner and moving counter-clockwise
		pl.addPoint(hb.x, hb.y);
		pl.addPoint(hb.x, hb.y + hb.height);
		pl.addPoint(hb.x + hb.width, hb.y + hb.height);
		pl.addPoint(hb.x + hb.width, hb.y);
		pl.addPoint(hb.x, hb.y);
		
		// Second set of five points is the action figure's points
		Rectangle afBounds = actionBarFigure.getBounds().getCopy();
		afBounds.x -= 3;
		afBounds.y -= 3;
		afBounds.width += 6;
		afBounds.height += 6;
		PointList actionFigurePoints = new PointList(5);
		actionFigurePoints.addPoint(afBounds.x, afBounds.y);
		actionFigurePoints.addPoint(afBounds.x, afBounds.y + afBounds.height);
		actionFigurePoints.addPoint(afBounds.x + afBounds.width, afBounds.y + afBounds.height);
		actionFigurePoints.addPoint(afBounds.x + afBounds.width, afBounds.y);
		actionFigurePoints.addPoint(afBounds.x, afBounds.y);
		pl.addAll(actionFigurePoints);
		
		// Last five points is the action figure's shadow points which is offset by the following translate statement:
		actionFigurePoints.translate(17, 14); // Create points for action figure shadow
		pl.addAll(actionFigurePoints);
		decorationsFigure.setPoints(pl);

		// Keep action bar on top to avoid obscurring by selection handles from other editparts
		if (getLayer(LayerConstants.HANDLE_LAYER).getChildren().size() > 2) {
			getLayer(LayerConstants.HANDLE_LAYER).remove(decorationsFigure);
			getLayer(LayerConstants.HANDLE_LAYER).remove(actionBarFigure);
			getLayer(LayerConstants.HANDLE_LAYER).add(decorationsFigure);
			getLayer(LayerConstants.HANDLE_LAYER).add(actionBarFigure);
		}
		actionBarFigure.setVisible(true);
		decorationsFigure.setVisible(true);
	}

	public void hide() {
		actionBarFigure.setVisible(false);
		decorationsFigure.setVisible(false);
	}

	protected IFigure createFigure() {
		actionBarFigure = new RoundedRectangle() {

			protected void fillShape(Graphics graphics) {
				Rectangle rect = getBounds().getCopy().expand(-1, -1);
				graphics.fillGradient(rect, false);
			}

			protected void outlineShape(Graphics graphics) {
				try {
					graphics.setAntialias(SWT.ON); // This makes the connection line look smooth
				} catch (SWTException e) {
					// For OS platforms that don't support Antialias
				}
				graphics.setForegroundColor(ColorConstants.black);
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
					return new AbstractEditPolicy() {};
				return super.createChildEditPolicy(child);
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

}

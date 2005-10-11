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
 *  $Revision: 1.3 $  $Date: 2005-10-11 20:08:23 $ 
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
		}
	}

	public void addActionBarChildren(List actionBarChildren) {
		this.actionBarChildren = actionBarChildren;
	}

	public void show(Rectangle hostBounds, int orientation) {
		Rectangle bounds = actionBarFigure.getBounds();
		actionBarFigure.setLocation(new Point(hostBounds.x + hostBounds.width + 14, hostBounds.y - bounds.height - 14));
//		actionBarFigure.setLocation(new Point(hostBounds.x + hostBounds.width + 9, hostBounds.y + hostBounds.height + 9));
//		actionBarFigure.setLocation(new Point(hostBounds.x - bounds.width - 9, hostBounds.y + hostBounds.height + 9));
		actionBarFigure.setVisible(true);
		if (connectionFigure == null) {
			connectionFigure = new Polyline() {
				PointList hostBorderPoints = null;
				PointList actionBarBorderPoints = null;
				PointList hostPoints = null;
				PointList actionBarPoints = null;

				protected void outlineShape(Graphics g) {
					try {
						g.setAntialias(SWT.ON); // This makes the connection line look smooth
					} catch (SWTException e) {
						// For OS platforms that don't support Antialias
					}
					g.setForegroundColor(ColorConstants.black);
					if (hostBorderPoints != null) {
//						g.setForegroundColor(ColorConstants.black);
//						g.setLineWidth(1);
//						g.drawPolyline(hostBorderPoints);
					}
					if (hostPoints != null) {
						g.setForegroundColor(ColorConstants.green);
						g.setLineWidth(2);
						g.drawPolyline(hostPoints);
					}
					if (actionBarBorderPoints != null) {
//						g.setForegroundColor(ColorConstants.black);
//						g.setLineWidth(1);
//						Point fp = actionBarBorderPoints.getFirstPoint();
//						Point lp = actionBarBorderPoints.getPoint(2);
//						Rectangle rect = new Rectangle(fp.x, fp.y, lp.x - fp.x, lp.y - fp.y);
//						g.drawRoundRectangle(rect, 8, 8);
					}
					if (actionBarPoints != null) {
						g.setForegroundColor(ColorConstants.green);
						g.setLineWidth(2);
						Point fp = actionBarPoints.getFirstPoint();
						Point lp = actionBarPoints.getPoint(2);
						Rectangle rect = new Rectangle(fp.x, fp.y, lp.x - fp.x, lp.y - fp.y);
						g.drawRoundRectangle(rect, 8, 8);
					}
					g.setForegroundColor(ColorConstants.lightGreen);
					g.setLineWidth(2);
					PointList anchorLinePoints = new PointList(2);
//					Point p1 = hostBorderPoints.getPoint(3);
//					Point p2 = actionBarBorderPoints.getPoint(1);
					Point p1 = hostPoints.getPoint(3);
					Point p2 = actionBarPoints.getPoint(1);
					anchorLinePoints.addPoint(p1);
					anchorLinePoints.addPoint(p2);
					g.drawPolyline(anchorLinePoints);

					g.setForegroundColor(ColorConstants.darkGray);
					g.setLineWidth(1);
					anchorLinePoints = new PointList(2);
					anchorLinePoints.addPoint(p1.x-2, p1.y-2);
					anchorLinePoints.addPoint(p2.x-2, p2.y-2);
//					g.drawPolyline(anchorLinePoints);

					anchorLinePoints = new PointList(2);
					anchorLinePoints.addPoint(p1.x+1, p1.y+2);
					anchorLinePoints.addPoint(p2.x+2, p2.y);
//					g.drawPolyline(anchorLinePoints);
				}
				/*
				 * points should contain 10 points... first 5 is the points for the host figure,
				 * last 5 is the points for the action bar figure.
				 */
				public void setPoints(PointList points) {
					super.setPoints(points);
					hostBorderPoints = null;
					hostPoints = null;
					actionBarBorderPoints = null;
					actionBarPoints = null;
					if (points.size() == 10) {
						hostBorderPoints = new PointList(5);
						hostPoints = new PointList(5);
						actionBarBorderPoints = new PointList(5);
						actionBarPoints = new PointList(5);
						for (int i = 0; i < 5; i++) {
							hostBorderPoints.addPoint(points.getPoint(i));
						}
						hostPoints.addPoint(hostBorderPoints.getPoint(0).x + 2, hostBorderPoints.getPoint(0).y + 2);
						hostPoints.addPoint(hostBorderPoints.getPoint(1).x + 2, hostBorderPoints.getPoint(1).y - 2);
						hostPoints.addPoint(hostBorderPoints.getPoint(2).x - 2, hostBorderPoints.getPoint(2).y - 2);
						hostPoints.addPoint(hostBorderPoints.getPoint(3).x - 2, hostBorderPoints.getPoint(3).y + 2);
						hostPoints.addPoint(hostBorderPoints.getPoint(4).x + 2, hostBorderPoints.getPoint(4).y + 2);

						for (int i = 5; i < 10; i++) {
							actionBarBorderPoints.addPoint(points.getPoint(i));
						}
						actionBarPoints.addPoint(actionBarBorderPoints.getPoint(0).x + 2, actionBarBorderPoints.getPoint(0).y + 2);
						actionBarPoints.addPoint(actionBarBorderPoints.getPoint(1).x + 2, actionBarBorderPoints.getPoint(1).y - 2);
						actionBarPoints.addPoint(actionBarBorderPoints.getPoint(2).x - 2, actionBarBorderPoints.getPoint(2).y - 2);
						actionBarPoints.addPoint(actionBarBorderPoints.getPoint(3).x - 2, actionBarBorderPoints.getPoint(3).y + 2);
						actionBarPoints.addPoint(actionBarBorderPoints.getPoint(4).x + 2, actionBarBorderPoints.getPoint(4).y + 2);
					}
				};
			};
			connectionFigure.setLineWidth(1);
			getLayer(LayerConstants.HANDLE_LAYER).add(connectionFigure);
		}
		PointList pl = new PointList();
		Rectangle hb = hostBounds.getCopy();
		hb.x -= 5;
		hb.y -= 5;
		hb.width += 10;
		hb.height += 10;
		pl.addPoint(hb.x, hb.y);
		pl.addPoint(hb.x, hb.y + hb.height);
		pl.addPoint(hb.x + hb.width, hb.y + hb.height);
		pl.addPoint(hb.x + hb.width, hb.y);
		pl.addPoint(hb.x, hb.y);
		Rectangle afBounds = actionBarFigure.getBounds().getCopy();
		afBounds.x -= 3;
		afBounds.y -= 3;
		afBounds.width += 6;
		afBounds.height += 6;
		pl.addPoint(afBounds.x, afBounds.y);
		pl.addPoint(afBounds.x, afBounds.y + afBounds.height);
		pl.addPoint(afBounds.x + afBounds.width, afBounds.y + afBounds.height);
		pl.addPoint(afBounds.x + afBounds.width, afBounds.y);
		pl.addPoint(afBounds.x, afBounds.y);
		connectionFigure.setPoints(pl);
		connectionFigure.setVisible(true);
	}

	public void hide() {
		actionBarFigure.setVisible(false);
		connectionFigure.setVisible(false);
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

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
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: VisualBendpointEditPolicy.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:12:50 $ 
 */

import java.util.*;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEMap;

import org.eclipse.ve.internal.cdm.*;
import org.eclipse.ve.internal.cdm.impl.KeyedPointsImpl;
import org.eclipse.ve.internal.cdm.model.CDMModelConstants;
import org.eclipse.ve.internal.cdm.model.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Connection;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;
/**
 * This editpolicy handles changing the bendpoints of a connection.
 * It is used when the bendpoints are stored in a visual info of
 * connection model, which is the host of this policy.
 * 
 * It will handle the bendpoints entirely. It will listen for changes
 * to the bendpoints, and when they do, refresh the connection.
 */

public class VisualBendpointEditPolicy extends BendpointEditPolicy implements IZoomListener {

	
	private static final Object REFRESH_BENDPOINTS = new Object();
	
	protected ZoomController zoomController;
	protected VisualInfoPolicy.VisualInfoListener viListener;

	public void activate() {
		super.activate();

		zoomController = ZoomController.getZoomController(getHost());
		if (zoomController != null) {
			zoomController.addZoomListener(this);
			zoomChanged(zoomController.getZoomValue(), 0);
		}

		// Add listener to visual constraint.
		EditPartViewer viewer = getHost().getRoot().getViewer();
		EditDomain dom = (EditDomain) viewer.getEditDomain();
		viListener = new VisualInfoPolicy.VisualInfoListener(getHost().getModel(), dom.getDiagram(viewer), dom) {
			public void notifyVisualInfoChanges(Notification msg) {
				if (msg.getFeatureID(VisualInfo.class) == CDMPackage.VISUAL_INFO__KEYED_VALUES) {
					final Notification kvMsg = KeyedValueNotificationHelper.notifyChanged(msg, CDMModelConstants.VISUAL_BENDPOINTS_KEY);
					if (kvMsg != null) {
						// The bendpoints keyedvalue was changed
						switch (kvMsg.getEventType()) {
							case Notification.SET : // It was changed.
							case Notification.UNSET : // It was removed
								CDEUtilities.displayExec(getHost(), REFRESH_BENDPOINTS, new Runnable() {
									public void run() {
										if (diagram != null && getHost() != null && getHost().isActive())
											refreshBendpoints(((BasicEMap.Entry) kvMsg.getNewValue()).getValue());
									}
								});
								break;
						}
					}
				}
			}

			public void notifyVisualInfo(int eventType, VisualInfo oldVI, VisualInfo newVI) {
				// A visual info was either added or removed
				CDEUtilities.displayExec(getHost(), REFRESH_BENDPOINTS, new Runnable() {
					public void run() {
						// if goes inactive, then diagram will be null
						if (diagram != null && getHost() != null && getHost().isActive())
							refreshBendpoints();
					}
				});
			}

			public void notifyAnnotationChanges(Notification msg) {
				// An annotation was either added or removed
				CDEUtilities.displayExec(getHost(), REFRESH_BENDPOINTS, new Runnable() {
					public void run() {
						// if goes inactive, then diagram will be null
						if (diagram != null && getHost() != null && getHost().isActive())
							refreshBendpoints();
					}
				});
			}

		};
		
		refreshBendpoints();
	}

	public void deactivate() {
		if (zoomController != null) {
			zoomController.removeZoomListener(this);
			zoomController = null;
		}

		if (viListener != null)
			viListener.removeListening();
		viListener = null;

		setHost(null);
		super.deactivate();
	}

	public void zoomChanged(int newZoom, int oldZoom) {
		refreshBendpoints(); // Zoom has changed, the entire bendpoints must be re-calaculated.
	}

	protected void refreshBendpoints() {
		// Get the bendpoints, if any, and send them to the refresh.
		Object kv = null;
		VisualInfo vi = VisualInfoPolicy.getVisualInfo(getHost());
		if (vi != null) {
			kv = vi.getKeyedValues().get(CDMModelConstants.VISUAL_BENDPOINTS_KEY);
		}
		refreshBendpoints(kv);
	}

	protected void refreshBendpoints(Object kv) {
		List figureConstraint = Collections.EMPTY_LIST;
		if (kv instanceof List) {
			List modelConstraint = (List) kv;
			figureConstraint = new ArrayList();
			for (int i = 0; i < modelConstraint.size(); i++) {
				Point point = (Point) modelConstraint.get(i);
				AbsoluteBendpoint bp = new AbsoluteBendpoint(zoomCoordinate(point.x), zoomCoordinate(point.y));
				figureConstraint.add(bp);
			}
		}
		((Connection) ((GraphicalEditPart) getHost()).getFigure()).setRoutingConstraint(figureConstraint);
	}

	protected Command getCreateBendpointCommand(BendpointRequest request) {
		// The absolute location is the coordinate on the graph viewer
		// We may be zoomed up in which case we need to adjust by the zoom factor, so we get a copy to make changes.
		org.eclipse.draw2d.geometry.Point absoluteLocation = request.getLocation().getCopy();
		unzoomPoint(absoluteLocation);
		Point modelPoint = new Point(absoluteLocation.x, absoluteLocation.y);

		List currentPoints = getCurrentPoints();
		if (currentPoints == null) {
			currentPoints = new ArrayList(1);
			currentPoints.add(modelPoint);
		} else {
			currentPoints = new ArrayList(currentPoints); // We will be modifying it.
			currentPoints.add(request.getIndex(), modelPoint);
		}

		return changeBendpointsCommand(currentPoints);
	}

	protected Command getMoveBendpointCommand(BendpointRequest request) {
		List currentPoints = getCurrentPoints();
		if (currentPoints == null)
			return null; // We have no points to move.

		currentPoints = new ArrayList(currentPoints); // We will be modifying it.

		// The absolute location is the coordinate on the graph viewer
		// We may be zoomed up in which case we need to adjust by the zoom factor, so we get a copy to make changes.
		org.eclipse.draw2d.geometry.Point absoluteLocation = request.getLocation().getCopy();
		unzoomPoint(absoluteLocation);
		Point modelPoint = new Point(absoluteLocation.x, absoluteLocation.y);
		currentPoints.set(request.getIndex(), modelPoint);
		return changeBendpointsCommand(currentPoints);
	}

	protected Command getDeleteBendpointCommand(BendpointRequest request) {
		int index = request.getIndex();
		List currentPoints = getCurrentPoints();
		if (currentPoints == null || index >= currentPoints.size())
			return null; // We have no points to delete, or the point is outside the list.

		currentPoints = new ArrayList(currentPoints); // We will be modifying it.
		currentPoints.remove(index);
		return changeBendpointsCommand(currentPoints);
	}

	/**
	 * Given the list of points, create change request.
	 * The points are in model constraint format, not GEF format.
	 */
	protected Command changeBendpointsCommand(List points) {
		EditPartViewer viewer = getHost().getRoot().getViewer();
		List oldPoints = getCurrentPoints();
		if (!points.equals(oldPoints)) {
			// We have actually changed some.
			// Update the keyed value
			if (!points.isEmpty()) {
				KeyedPointsImpl newConstraint = (KeyedPointsImpl) CDMFactory.eINSTANCE.create(CDMPackage.eINSTANCE.getKeyedPoints());
				newConstraint.setKey(CDMModelConstants.VISUAL_BENDPOINTS_KEY);
				newConstraint.getTypedValue().addAll(points);
				EditDomain dom = EditDomain.getEditDomain(getHost());
				return VisualInfoPolicy.applyVisualInfoSetting(getHost().getModel(), newConstraint, dom, dom.getDiagram(viewer));
			} else {
				// The points list is empty, so just remove the setting
				VisualInfo vi = VisualInfoPolicy.getVisualInfo(getHost());
				return VisualInfoPolicy.cancelVisualInfoSetting(vi, CDMModelConstants.VISUAL_BENDPOINTS_KEY);
			}
		}

		return null;

	}

	/**
	 * Get the current list.
	 */
	protected List getCurrentPoints() {
		EditPartViewer viewer = getHost().getRoot().getViewer();
		VisualInfo vi = VisualInfoPolicy.getVisualInfo(getHost().getModel(), viewer);
		Object bendpointsKV = vi != null ? vi.getKeyedValues().get(CDMModelConstants.VISUAL_BENDPOINTS_KEY) : null;
		List oldPoints = bendpointsKV instanceof List ? (List) bendpointsKV : null;
		return oldPoints;
	}

	/**
	 * Adjust the point for the zoom factor.
	 */
	protected void unzoomPoint(org.eclipse.draw2d.geometry.Point p) {
		if (zoomController != null) {
			p.setLocation(zoomController.unzoomCoordinate(p.x), zoomController.unzoomCoordinate(p.y));
		}
	}

	/**
	 * Adjust the coordinate for the zoom factor.
	 */
	protected int zoomCoordinate(int coor) {
		return (zoomController == null) ? coor : zoomController.zoomCoordinate(coor);
	}

}

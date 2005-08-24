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
 *  $RCSfile: ZoomController.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:49 $ 
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.ui.IEditorPart;
/**
 * The zoom controller. It maintains the current zoom
 * value. It also notifies the ZoomListeners of any
 * zoom changes. There are also conversion methods
 * convert from/to zoom coordinates.
 *
 * To find the ZoomController, it is stored in the
 * EditDomain under the viewer data for the viewer
 * in question. The key is the ZOOM_KEY defined within
 * this class.
 */
public class ZoomController {

	public static final String ZOOM_KEY = "org.eclipse.ve.internal.cde.core.zoomkey";	// Key in viewer data for zoom controller //$NON-NLS-1$
	
	protected List listeners = new ArrayList(5);
	protected int zoomValue = 100;
	private int unzoomRounder = 50;
	
	
	/**
	 * Static helper method to return the zoom controller form the active editor, if the primary viewer is zoomable
	 */
	public static ZoomController getZoomController(IEditorPart activeEditor){
		
		EditPartViewer viewer = (EditPartViewer) activeEditor.getAdapter(org.eclipse.gef.GraphicalViewer.class);
		if (viewer != null) {
			EditDomain dom = (EditDomain) viewer.getEditDomain();
			return (ZoomController) dom.getViewerData(viewer, ZOOM_KEY);
		}
		return null;
	}
	
	/**
	 * Static helper method to return the zoom controller from the editpart, if the viewer is zoomable
	 */
	public static ZoomController getZoomController(EditPart editPart){	
		EditPartViewer viewer = editPart.getRoot().getViewer();
		EditDomain dom = (EditDomain) viewer.getEditDomain();
		return (ZoomController) dom.getViewerData(viewer, ZOOM_KEY);
	}
	
	/**
	 * Add a Zoom Listener.
	 * Note: Not thread safe, do not add/remove a listener
	 * during zoom notification.
	 */
	public void addZoomListener(IZoomListener listener) {
		listeners.add(listener);
	}
	
	/**	
	 * Remove a Zoom Listener.
	 * Note: Not thread safe, do not add/remove a listener
	 * during zoom notification.
	 */
	public void removeZoomListener(IZoomListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Get the current zoom value.
	 */
	public int getZoomValue() {
		return zoomValue;
	}
	
	/**
	 * Set the zoom value
	 */
	public void setZoomValue(int newZoom) {
		if (newZoom < 1)
			newZoom = 1;	// Can't zoom to less than one, 0 and less don't make sense.
		int oldZoom = zoomValue;
		zoomValue = newZoom;
		unzoomRounder = (((zoomValue*10)/2)+5)/10;
		for (int i=0; i<listeners.size(); i++)
			((IZoomListener) listeners.get(i)).zoomChanged(zoomValue, oldZoom);
	}

	/**
	 * Go from unzoomed to zoomed on a coordinate value.
	 * It doesn't matter if it is x, y, width, or height. They
	 * all zoom the same way.
	 */
	public int zoomCoordinate(int coordinate) {
		return ((coordinate*zoomValue)+50)/100;	// Symetric round to get closest zoomed point.
	}
	
	/**
	 * Go from zoomed to unzoomed on a coordinate value
 	 * It doesn't matter if it is x, y, width, or height. They
	 * all zoom the same way.
	 */
	public int unzoomCoordinate(int coordinate) {
		return ((coordinate*100)+unzoomRounder)/zoomValue;	// Symetric round to closest unzoomed point
	}
	
	/**
	 * Go from unzoomed to zoomed on a coordinate value.
	 * It doesn't matter if it is x, y, width, or height. They
	 * all zoom the same way. This returns it as a double with no rounding to accurate zoom value.
	 */
	public double zoomCoordinateReal(int coordinate) {
		return (coordinate*zoomValue)/100.0d;	
	}
	
	/**
	 * Go from zoomed to unzoomed on a coordinate value
 	 * It doesn't matter if it is x, y, width, or height. They
	 * all zoom the same way. This returns it as a double with no rounding to accurate umzoom value.
	 */
	public double unzoomCoordinateReal(int coordinate) {
		return (coordinate*100.0d)/zoomValue;
	}	
}

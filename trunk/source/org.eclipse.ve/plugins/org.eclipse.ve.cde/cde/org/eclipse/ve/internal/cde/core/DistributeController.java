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
 *  $RCSfile: DistributeController.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:49 $ 
 */



import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.*;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
/*
 * This helper class creates a DistributionFigure to be used to draw a 
 * boundary box around the selected editparts so they can be distributed 
 * within it's surface area. It also creates the selection handles (see
 * DistributeHandle) in each corner and in between to allow resizing the 
 * bounding area.
 *
 * Distribute Box works only with the freeform surface.
 * It only works with a GraphicalViewer.
 *
 *
 * To find the DistributeController, it is stored in the
 * EditDomain under the viewer data for the viewer
 * in question. The key is the DISTRIBUTE_KEY defined within
 * this class.
 */
public class DistributeController implements ISelectionChangedListener, FigureListener {
	public static final String DISTRIBUTE_KEY = "org.eclipse.ve.internal.cde.core.distributekey"; //$NON-NLS-1$

	protected GraphicalViewer viewer;	
	protected List handles;
	protected Rectangle boundaryBox = new Rectangle();
	protected DistributeFigure distributeFigure = new DistributeFigure();
	protected boolean active = false;
	protected boolean boxVisible = false;
	protected List childFigures = new ArrayList();
	private boolean revalidateWaiting = false;

/**
 * The part will be asked (thru getAdapter) for the active EditPartViewer. It is assumed that
 * whatever part that works with DistributeControllers will return the active viewer thru getAdapter(EditPartViewer.class).
 * 
 * @param activePart The part that is currently active
 * @return The DistributeController for the current active viewer for the part
 */
public static DistributeController getDistributeController(IWorkbenchPart activePart){
		
		EditPartViewer viewer = (EditPartViewer) activePart.getAdapter(EditPartViewer.class);
		if (viewer != null) {
			EditDomain dom = (EditDomain) viewer.getEditDomain();
			return dom != null ? (DistributeController) dom.getViewerData(viewer, DISTRIBUTE_KEY) : null;
		}
		return null;
	}

/**
 * Static helper method to return the distribute controller from the editpart, if any in the viewer is distributable.
 */
public static DistributeController getDistributeController(EditPart editPart){	
	EditPartViewer viewer = editPart.getRoot().getViewer();
	EditDomain dom = (EditDomain) viewer.getEditDomain();
	return (DistributeController) dom.getViewerData(viewer, DISTRIBUTE_KEY);
}

public DistributeController (GraphicalViewer viewer) {
	this.viewer = viewer;;
}

public void setBoxVisible(boolean visible) {
	boxVisible = visible;
	if (visible) {
		revalidate();
		viewer.addSelectionChangedListener(this);	// Listen for selections only while box is visible.
	} else {
		viewer.removeSelectionChangedListener(this);		
		deactivate();
	}
}

protected void createSelectionHandles() {
	if (handles == null) {
		handles = new ArrayList(8);
		handles.add(new DistributeHandle(distributeFigure, PositionConstants.NORTH));
		handles.add(new DistributeHandle(distributeFigure, PositionConstants.NORTH_WEST));
		handles.add(new DistributeHandle(distributeFigure, PositionConstants.NORTH_EAST));
		handles.add(new DistributeHandle(distributeFigure, PositionConstants.SOUTH));
		handles.add(new DistributeHandle(distributeFigure, PositionConstants.SOUTH_WEST));
		handles.add(new DistributeHandle(distributeFigure, PositionConstants.SOUTH_EAST));
		handles.add(new DistributeHandle(distributeFigure, PositionConstants.EAST));
		handles.add(new DistributeHandle(distributeFigure, PositionConstants.WEST));
	}
}

protected void calculateBoundingBox(List editparts) {
	// First determine the size of the boundary area that includes all the
	// selected objects.
	boundaryBox = new Rectangle(((GraphicalEditPart)editparts.get(0)).getFigure().getBounds());
	for (int i = 1; i < editparts.size(); i++) {
		boundaryBox.union(((GraphicalEditPart)editparts.get(i)).getFigure().getBounds());
	}
	distributeFigure.setBoundingBox(boundaryBox);

}

public boolean isBoxActive() {
	return active;
	
}

public boolean isBoxVisible() {
	return boxVisible;
}

public Rectangle getBoundaryBox() {
	return boundaryBox.getCopy();
}

public void activate() {
	if (active || !boxVisible)
		return;
		
	List editparts = viewer.getSelectedEditParts();
	if (editparts.size() > 1) {
		active = true;
		calculateBoundingBox(editparts);
		IFigure layerFigure = ((LayerManager) viewer.getEditPartRegistry().get(LayerManager.ID)).getLayer(LayerConstants.HANDLE_LAYER);
		layerFigure.add(distributeFigure);
		distributeFigure.addFigureListener(this);
		createSelectionHandles();
		for (int i=0; i < handles.size(); i++)
			layerFigure.add((IFigure)handles.get(i));
		// Need to listen to the child figures so that we can keep the box following them as they move.
		childFigures.clear();
		for (int i=0; i < editparts.size(); i++) {
			IFigure childF = ((GraphicalEditPart) editparts.get(i)).getFigure();
			childFigures.add(childF);
			childF.addFigureListener(this);
		}
	}
}

public void deactivate() {
	if (!active)
		return;
	active = false;
	IFigure layerFigure = ((LayerManager) viewer.getEditPartRegistry().get(LayerManager.ID)).getLayer(LayerConstants.HANDLE_LAYER);		
	distributeFigure.removeFigureListener(this);
	layerFigure.remove(distributeFigure);
	for (int i=0; i < handles.size(); i++)
		layerFigure.remove((IFigure)handles.get(i));		
	for (int i=0; i < childFigures.size(); i++) {
		IFigure childF = (IFigure) childFigures.get(i);
		childF.removeFigureListener(this);
	}
	childFigures.clear();
}

/* 
 * Use this method if the distribute figure needs to be re-created and
 * re-layed out due to changes in the parent (such as zooming in, etc.)
 */
public void revalidate () {
	deactivate();
	activate();
}

/*
 * The distribute figure has moved, we need a new bounding box.
 */
public void figureMoved(IFigure source) {
	if (source == distributeFigure)
		boundaryBox = source.getBounds();
	else
		queueRevalidate();	// Queue up a revalidate because one of the child figures has moved.
}

protected void queueRevalidate() {
	revalidateWaiting = true;
	Display.getCurrent().asyncExec(new Runnable() {
		public void run() {
			if (revalidateWaiting) {
				revalidateWaiting = false;
				revalidate();
			}
		}
	});
}

public void selectionChanged(SelectionChangedEvent event) {
	if (!boxVisible)
		return;
		
	if (active)
		revalidate();
	else {
		if (!event.getSelection().isEmpty())
			activate();
		else
			deactivate();
	}
}
}

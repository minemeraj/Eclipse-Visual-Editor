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
 *  $RCSfile: GridController.java,v $
 *  $Revision: 1.9 $  $Date: 2007-05-25 04:09:36 $ 
 */

import java.util.*;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.ui.IEditorPart;

import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;
import org.eclipse.ve.internal.cdm.Annotation;
/**
 * The grid controller. It maintains the current grid
 * information.
 *
 * It also notifies when any of the grid properties
 * change.
 *
 * To find the GridController, it is stored in the
 * EditDomain under the viewer data for the viewer
 * in question. The key is the GRID_KEY defined within
 * this class.
 *
 * For Grid to work the following must be done:
 *
 *  1) The layoutEditPolicy upon activation will need to:
 *     a) Create a GridFigure and add it at the end of the
 *        EditPart's contentPane,
 *        it should pass in the zoomController if it also
 *        can process zooming.
 *     b) Add itself as a listener to the GridController for
 *        the current viewer.
 *     c) Register the edit part as being gridable with the GridController.      
 *  2) Upon any GridController notification, it should call repaint on the GridFigure
 *     so that it is rescheduled to 
 *     draw the new grid. For grid visibility, it should set the GridFigure visibility
 *     appropriately.
 *  4) Upon deactivation it should:
 *     a) remove itself as a listener from the
 *        GridController.
 *     b) Remove the GridFigure from the 
 *        EditPart contentPane.
 *     c) Remove the edit part from the registered list.
 *
 */
public class GridController {
	
	public static final String GRID_KEY = "org.eclipse.ve.internal.cde.core.gridkey"; //$NON-NLS-1$
	public static final String GRID_STATE_KEY = "org.eclipse.ve.internal.cde.core.gridstatekey"; //$NON-NLS-1$
	public static final String GRID_THIS_PART = "THIS_PART"; //$NON-NLS-1$

	protected List listeners = new ArrayList(5);
	protected HashMap registeredEPs = new HashMap(2);
	
	private boolean showGrid = false;
	private int gridWidth = 15;
	private int gridHeight = 15;
	private int gridMargin = 0; 
	
	public GridController(){
		gridWidth = gridHeight = CDEPlugin.getPlugin().getPluginPreferences().getInt(CDEPlugin.XY_GRID_SPACING);
	}
	
	/**
	 * Static helper method to return the grid controller from the active editor, if any in the primary viewer is griddable
	 */
	public static GridController getGridController(IEditorPart activeEditor){
	
		EditPartViewer viewer = (EditPartViewer) activeEditor.getAdapter(org.eclipse.gef.GraphicalViewer.class);
		if (viewer != null) {
			EditDomain dom = (EditDomain) viewer.getEditDomain();
			return (GridController) dom.getViewerData(viewer, GRID_KEY);
		}
		return null;
	}
	
	/**
	 * Static helper method to return the grid controller for a specific editpart.
	 */
	public static GridController getGridController(EditPart editPart){	
		EditPartViewer viewer = editPart.getRoot().getViewer();
		EditDomain dom = (EditDomain) viewer.getEditDomain();
		// This is the master grid controller for this viewer
		GridController gc = (GridController) dom.getViewerData(viewer, GRID_KEY);
		if (gc != null) {
			return (GridController) gc.registeredEPs.get(editPart);
		}
		return null;
	}
	/**
	 * Register the Editpart as having a grid on it.
	 */
	public static void registerEditPart(EditPart ep, GridController newgc) {
		EditPartViewer viewer = ep.getRoot().getViewer();
		EditDomain dom = (EditDomain) viewer.getEditDomain();
		// This is the master grid controller for this viewer
		GridController gc = (GridController) dom.getViewerData(viewer, GRID_KEY);
		if (gc != null) 
			gc.registeredEPs.put(ep, newgc);
	}
	
	
	/**
	 * Unregister the EditPart.
	 */
	public static void unregisterEditPart(EditPart ep) {
		EditPartViewer viewer = ep.getRoot().getViewer();
		EditDomain dom = (EditDomain) viewer.getEditDomain();
		// This is the master grid controller for this viewer
		GridController gc = (GridController) dom.getViewerData(viewer, GRID_KEY);
		if (gc != null && gc.registeredEPs.get(ep) != null)
			gc.registeredEPs.remove(ep);
	}
		
	/**
	 * Add a Grid Listener.
	 * Note: Not thread safe, do not add/remove a listener
	 * during grid notification.
	 */
	public void addGridListener(IGridListener listener) {
		listeners.add(listener);
	}
	
	/**	
	 * Remove a Grid Listener.
	 * Note: Not thread safe, do not add/remove a listener
	 * during grid notification.
	 */
	public void removeGridListener(IGridListener listener) {
		listeners.remove(listener);
	}
	
	
	/**
	 * Get the grid height.
	 */
	public int getGridHeight() {
		return gridHeight;
	}
	
	/**
	 * Set the grid height.
	 */
	public void setGridHeight(int gridHeight) {
		int oldGridHeight = this.gridHeight;
		this.gridHeight = gridHeight;
		for (int i=0; i<listeners.size(); i++)
			((IGridListener) listeners.get(i)).gridHeightChanged(gridHeight, oldGridHeight);
	}
	
	/**
	 * Get the grid width.
	 */
	public int getGridWidth() {
		return gridWidth;
	}
	
	/**
	 * Set the grid width.
	 */
	public void setGridWidth(int gridWidth) {
		int oldGridWidth = this.gridWidth;
		this.gridWidth = gridWidth;
		for (int i=0; i<listeners.size(); i++)
			((IGridListener) listeners.get(i)).gridWidthChanged(gridWidth, oldGridWidth);
	}
	
	/**
	 * Get the grid margin.
	 */
	public int getGridMargin() {
		return gridMargin;
	}
	
	/**
	 * Set the grid margin.
	 */
	public void setGridMargin(int gridMargin) {
		int oldGridMargin = this.gridMargin;
		this.gridMargin = gridMargin;
		for (int i=0; i<listeners.size(); i++)
			((IGridListener) listeners.get(i)).gridMarginChanged(gridMargin, oldGridMargin);
	}		
	
	/**
	 * Get the grid visibility.
	 */
	public boolean isGridShowing() {
		return showGrid;
	}
	
	/**
	 * Set the grid visibility.
	 */
	public void setGridShowing(boolean showGrid) {
		this.showGrid = showGrid;
		for (int i=0; i<listeners.size(); i++) {
			((IGridListener) listeners.get(i)).gridVisibilityChanged(showGrid);
			// Set the grid state in case a reload from scratch is done later
			if (listeners.get(i) instanceof LayoutEditPolicy)
				setGridState(((LayoutEditPolicy)listeners.get(i)).getHost(), showGrid);
		}
	}
	protected void setGridState(EditPart ep, boolean showGrid) {
		// TODO Somewhat of a hack here to persist the state of a grid (show or hide) 
		//      so that we can reshow the grid if a reload from scratch occurs.
		//      Need to redesign how this is done in the future. 
		//      For now store it in a HashSet in the EditDomain using the annotation name as the key.
		EditDomain domain = EditDomain.getEditDomain(ep);
		HashSet gridStateData = (HashSet)domain.getData(GridController.GRID_STATE_KEY);
		if (gridStateData == null)
			gridStateData = new HashSet(2);
		AnnotationLinkagePolicy policy = domain.getAnnotationLinkagePolicy();
		Annotation ann = policy.getAnnotation(ep.getModel());
		if (ann != null) {
			 String name = (String)ann.getKeyedValues().get(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
			 // If no annotation name, container must be the root part... use special name.
			 if (name == null)
				name = GRID_THIS_PART; 
			if (showGrid)
				gridStateData.add(name);
			else
				gridStateData.remove(name);
			domain.setData(GridController.GRID_STATE_KEY, gridStateData);
		}
		
	}

}

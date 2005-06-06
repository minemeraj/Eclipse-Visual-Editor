/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;
/*
 * $RCSfile: GridLayoutEditPolicy.java,v $ 
 * $Revision: 1.18 $ $Date: 2005-06-06 22:34:20 $
 */
import java.util.List;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.DropRequest;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionFilter;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.commands.NoOpCommand;
import org.eclipse.ve.internal.cde.core.*;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;


public class GridLayoutEditPolicy extends DefaultLayoutEditPolicy implements IGridListener, IActionFilter {
	
	// unique ID of this layout edit policy
	public static final String LAYOUT_ID = "org.eclipse.swt.layout.GridLayout"; //$NON-NLS-1$
	public static final String REQ_GRIDLAYOUT_SPAN = "GridLayout span cells"; //$NON-NLS-1$
	
	private final int DEFAULT_EDGE = 5;
	private final int HEIGHT_PADDING = 4;

	boolean fShowGrid = false;

	GridLayoutPolicyHelper helper = new GridLayoutPolicyHelper();
	int [][] layoutDimensions = null;
	private GridLayoutGridFigure fGridLayoutGridFigure;
	private GridLayoutFeedbackFigure fGridLayoutCellFigure;
	private GridController gridController;
	private GridImageListener fGridImageListener;
	
	protected FigureListener hostFigureListener = new FigureListener() {
		public void figureMoved(IFigure source) {
				refreshGridFigure();
				helper.refresh();
		}
	};
	private EditPartListener editPartSelectionListener;

	
	private class GridImageListener implements IImageListener {
		public void imageChanged(ImageData data) {
				refreshGridFigure();
				helper.refresh();
		}
	}
	
	protected IImageListener getGridImageListener() {
		if (fGridImageListener == null)
			fGridImageListener = new GridImageListener();
		return fGridImageListener;
	}
	
	/**
	 * @param containerPolicy
	 */
	public GridLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		super(containerPolicy);
		helper.setContainerPolicy(containerPolicy);
	}
	
	public void activate(){
		gridController = new GridController();
		if (gridController != null) {
			gridController.addGridListener(this);
			GridController.registerEditPart(getHost(), gridController);
			ControlProxyAdapter beanProxy = (ControlProxyAdapter) BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance)getHost().getModel());
			if (beanProxy != null)
				beanProxy.addImageListener(getGridImageListener());
			getHostFigure().addFigureListener(hostFigureListener);	// need to know when the host figure changes so we can refresh the grid

			// show grid if host editpart is selected and prefs is set
			if (CDEPlugin.getPlugin().getPluginPreferences().getBoolean(CDEPlugin.SHOW_GRID_WHEN_SELECTED)
					&& (getHost().getSelected() == EditPart.SELECTED || getHost().getSelected() == EditPart.SELECTED_PRIMARY))
				gridController.setGridShowing(true);
			// Add editpart listener to show grid when selected if prefs is set
			editPartSelectionListener = createEditPartSelectionListener();
			getHost().addEditPartListener(editPartSelectionListener);
		}
		super.activate();
		CustomizeLayoutWindowAction.addLayoutCustomizationPage(getHost().getViewer(), GridLayoutLayoutPage.class);
		CustomizeLayoutWindowAction.addComponentCustomizationPage(getHost().getViewer(), GridLayoutComponentPage.class);
	}
	
	public void deactivate() {
		containerPolicy.setContainer(null);
		GridController gridController = GridController.getGridController(getHost());
		eraseGridFigure();
		if (gridController != null) {
			gridController.removeGridListener(this);
			GridController.unregisterEditPart(getHost());
		}
		ControlProxyAdapter beanProxy = (ControlProxyAdapter) BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance)getHost().getModel());
		if (beanProxy != null)
			beanProxy.removeImageListener(getGridImageListener());
		getHostFigure().removeFigureListener(hostFigureListener);
		if (editPartSelectionListener != null) {
			getHost().removeEditPartListener(editPartSelectionListener);
			editPartSelectionListener = null;
		}
		super.deactivate();
	}
	
	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new NonResizableSpannableEditPolicy(this);
	}
	
	/**
	 * Factors out REQ_GRIDLAYOUT_SPAN requests, otherwise calls <code>super</code>.
	 * @see org.eclipse.gef.EditPolicy#getCommand(Request)
	 */
	public Command getCommand(Request request) {
		if (REQ_GRIDLAYOUT_SPAN.equals(request.getType()))
			return getSpanChildrenCommand(request);

		return super.getCommand(request);
	}
	
	protected Command getSpanChildrenCommand(Request generic) {
		ChangeBoundsRequest request = (ChangeBoundsRequest)generic;
		List editParts = request.getEditParts();
		if (editParts.isEmpty() || editParts.size() > 1)
			return UnexecutableCommand.INSTANCE;

		// Get the cell location that the mouse was dragged to
		Point spanToPosition = getLocationFromRequest(request).getCopy();
		// This point is absolute.  Make it relative to the model
		getHostFigure().translateToRelative(spanToPosition);
		Point spanToCellLocation = getGridLayoutGridFigure().getCellLocation(spanToPosition.x, spanToPosition.y);
		// Get the cell location where we started the drag operation
		Dimension dim = request.getSizeDelta();
		int handleSizeOffset = GridSpanHandle.HANDLE_SIZE/2;
		Point startPosition = new Point(spanToPosition.x - dim.width - handleSizeOffset, spanToPosition.y - dim.height - handleSizeOffset);
		// Get the cell location of the child component
		GraphicalEditPart ep = (GraphicalEditPart)editParts.get(0);
		Point childPosition = ep.getContentPane().getBounds().getLocation();
		Point childCellLocation = getGridLayoutGridFigure().getCellLocation(childPosition.x, childPosition.y);
		Point startCellLocation = getGridLayoutGridFigure().getCellLocation(startPosition.x, startPosition.y);
		// If the cell location where the pointer is located is different from the original cell location where we started,
		// create the commands to change the gridwidth or gridheight
		if ( (spanToCellLocation.x >= childCellLocation.x && spanToCellLocation.y >= childCellLocation.y) &&
				(spanToCellLocation.x != startCellLocation.x || spanToCellLocation.y != startCellLocation.y) ) {
			// Let the helper get the gridWidth or gridHeight commands based on the cell location
			// where the pointer is and the span direction (EAST for gridwidth or SOUTH for gridheight)
			return helper.getSpanChildrenCommand((EditPart)editParts.get(0), childCellLocation, spanToCellLocation, request.getResizeDirection());
		}
		return NoOpCommand.INSTANCE;
	}
	
	/**
	 * @see org.eclipse.ui.IActionFilter#testAttribute(Object, String, String)
	 * 
	 * Return true for show grid action if grid is hidden
	 * Return true for hide grid action if grid is showing
	 * Return true if the layoutpolicy is GridLayout
	 * otherwise return false
	 */
	public boolean testAttribute(Object target, String name, String value) {
		if (name.startsWith("showgrid") && value.equals("false") && !fShowGrid) //$NON-NLS-1$ //$NON-NLS-2$
			return true;
		else if (name.startsWith("showgrid") && value.equals("true") && fShowGrid) //$NON-NLS-1$ //$NON-NLS-2$
			return true;
		else if (name.startsWith("customizelayout") && value.equals("true")) //$NON-NLS-1$ //$NON-NLS-2$
			return true;
		else if (name.startsWith("LAYOUTPOLICY") && value.equals(LAYOUT_ID)) //$NON-NLS-1$
			return true;

		return false;
	}
	
	protected GridLayoutGridFigure getGridLayoutGridFigure() {
		if (fGridLayoutGridFigure == null) {
			IFigure f = ((GraphicalEditPart) getHost()).getContentPane();
			int [][] layoutDimensions = null;
			Rectangle layoutSpacing = null;
			Rectangle clientArea = null;
			/*
			 * If the container is empty, we can't depend on the layout dimensions or layout origin
			 * from the GridLayout, just use the default values.
			 */
			if (helper.getContainer() == null || getHost().getChildren().size() == 0) {
				layoutDimensions = new int [2][0];
				layoutDimensions[0] = new int[0];
				layoutDimensions[1] = new int[0];
			} else {
				layoutDimensions = helper.getContainerLayoutDimensions();
				layoutSpacing = helper.getContainerLayoutSpacing();
			}
			clientArea = helper.getContainerClientArea();
			fGridLayoutGridFigure = new GridLayoutGridFigure(f.getBounds().getCopy(), layoutDimensions, layoutSpacing, clientArea );
		}
		return fGridLayoutGridFigure;
	}
	
	protected void eraseGridFigure() {
		if (fGridLayoutGridFigure != null) {
			removeFeedback(fGridLayoutGridFigure);
			fGridLayoutGridFigure = null;
		}
		fShowGrid = false;		
	}
	
	public void eraseTargetFeedback(Request request) {
		if (!fShowGrid)
			if (fGridLayoutGridFigure != null && fGridLayoutGridFigure.getParent() != null) {
				removeFeedback(fGridLayoutGridFigure);
				fGridLayoutGridFigure = null;
			}
		if (fGridLayoutCellFigure != null) {
			removeFeedback(fGridLayoutCellFigure);
			fGridLayoutCellFigure = null;
		}
		super.eraseTargetFeedback(request);
	}
	
	protected void showGridFigure() {
		if (!fShowGrid) {
			fShowGrid = true;
			addFeedback(getGridLayoutGridFigure());
		}
		fShowGrid = true;
	}
	

	
	/*
	 * Show target feedback when dragging the span handles of a component.
	 * Highlight the cells the component will occupy based on the begining cell position
	 * and end cell position of the pointer.
	 */
	public void showSpanTargetFeedback(ChangeBoundsRequest request) {
		// If the grid is not on, turn it on
		if (!fShowGrid)
			addFeedback(getGridLayoutGridFigure());

		Point spanToPosition = request.getLocation().getCopy();
		// This point is absolute.  Make it relative to the model
		getHostFigure().translateToRelative(spanToPosition);

		// Get the cell location of the child component
		GraphicalEditPart ep = (GraphicalEditPart)request.getEditParts().get(0);
		Point childPosition = ep.getContentPane().getBounds().getLocation();
		// Get the start and end cell bounds in order to determine the entire bounds of the cell feedback figure.
		Rectangle startCellBounds = getGridLayoutGridFigure().getCellBounds(childPosition.x, childPosition.y);
		Rectangle endCellBounds = getGridLayoutGridFigure().getCellBounds(spanToPosition.x, spanToPosition.y);
		Rectangle spanrect = new Rectangle(startCellBounds.x, 
									startCellBounds.y, 
									endCellBounds.x + endCellBounds.width - startCellBounds.x,
									endCellBounds.y + endCellBounds.height - startCellBounds.y);
		if (fGridLayoutCellFigure == null) {
			fGridLayoutCellFigure = new GridLayoutFeedbackFigure();
		}
		fGridLayoutCellFigure.setBounds(spanrect);
		addFeedback(fGridLayoutCellFigure);
	}
	
	/**
	 * The model has changed. Remove and reconstruct the grid figure
	 * to reflect the changes.
	 */
	protected void refreshGridFigure() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if (fShowGrid) {
					eraseGridFigure();
					showGridFigure();
				} else {
					fGridLayoutGridFigure = null;
				}
			}
		});
	}
	
	/**
	 * Set the grid decoration on the edit part to be a fixed grid
	 */
	public void gridVisibilityChanged(boolean showGrid) {
		if (showGrid)
			showGridFigure();
		else
			eraseGridFigure();
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IGridListener#gridHeightChanged(int, int)
	 */
	public void gridHeightChanged(int gridHeight, int oldGridHeight) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IGridListener#gridWidthChanged(int, int)
	 */
	public void gridWidthChanged(int gridWidth, int oldGridWidth) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IGridListener#gridMarginChanged(int, int)
	 */
	public void gridMarginChanged(int gridMargin, int oldGridMargin) {
		// TODO Auto-generated method stub
		
	}
	
	// modifying the feedback behavior from FlowLayoutEditPolicy
	
	private Rectangle getAbsoluteBounds(GraphicalEditPart ep) {
		Rectangle bounds = ep.getContentPane().getBounds().getCopy();
		ep.getContentPane().translateToAbsolute(bounds);
		return bounds;
	}
	
	private Point getLocationFromRequest(Request request) {
		return ((DropRequest)request).getLocation();
	}
	
	/**
	 * Get the feedback index for the given request.  If there
	 * are no children, or the cursor is after the last child,
	 * the index will be -1.
	 * 
	 * @param request the Request
	 * @return the index for the insertion reference
	 */
	protected int getFeedbackIndexFor(Request request) {
		List children = getHost().getChildren();
		if (children.isEmpty())
			return -1;
		
		Point p = getLocationFromRequest(request);
		Point cell = getGridLayoutGridFigure().getCellLocation(p);
		
		int index =  helper.getChildIndexAtCell(cell);
		
		if (index != -1) {
			// Check to see if the cursor position is more than middle of the given
			// edit part, so that the insertion will occur after this position, rather
			// than before.
			Rectangle r = getAbsoluteBounds((GraphicalEditPart)children.get(index));
			if (p.x > r.x + (r.width / 2)) {
				index += 1;
				if (index >= children.size()) {
					index = -1;
				}
			}
		}
		
		return index;
	}

	/**
	 * Shows an insertion line if there is one or more current children.
	 */	
	protected void showLayoutTargetFeedback(Request request) {
		if (getHost().getChildren().size() == 0)
			return;
		
		if (!fShowGrid)
			addFeedback(getGridLayoutGridFigure());
		
		Polyline fb = getLineFeedback();
		fb.setLineWidth(4);
		
		boolean before = true;
		int epIndex = getFeedbackIndexFor(request);	
			
		Rectangle r = null;
		if (epIndex == -1) {
			// add to the end of the layout
			before = false;
			epIndex = getHost().getChildren().size() - 1;
			EditPart editPart = (EditPart) getHost().getChildren().get(epIndex);
			r = getAbsoluteBounds((GraphicalEditPart)editPart);
		} else {
			EditPart editPart = (EditPart) getHost().getChildren().get(epIndex);
			r = getAbsoluteBounds((GraphicalEditPart)editPart);
			before = true;
		}
		int x = Integer.MIN_VALUE;
		Rectangle parentBox = getAbsoluteBounds((GraphicalEditPart)getHost());
		
		
		
		if (before) {
			if (epIndex == 0) {
				// before the first component
				x = parentBox.x + (r.x - parentBox.x) / 2;
				if (x < DEFAULT_EDGE) {
					x = DEFAULT_EDGE;
				}
			} else if (helper.isOnSameRow(epIndex - 1, epIndex)) {
				// between two elements on the same row
				Rectangle otherFigure = getAbsoluteBounds((GraphicalEditPart)getHost().getChildren().get(epIndex - 1));
				Rectangle otherGrid = getGridLayoutGridFigure().getGridBroundsForCellBounds(helper.getChildrenDimensions()[epIndex - 1]);
				x = otherGrid.right() + (r.x - otherGrid.right()) / 2;
				if (otherFigure.height < r.height) {
					r = otherFigure;
				}
			} else if (helper.isCellEmptyBefore(epIndex)){
				// There's empty cells on the preceeding row, so put on the right edge of
				// the previous item
				r = getAbsoluteBounds((GraphicalEditPart)getHost().getChildren().get(epIndex - 1));
				x = parentBox.right() - (parentBox.right() - r.right()) / 2;
				if (x > DEFAULT_EDGE) {
					x = r.right() + DEFAULT_EDGE;
				}
			} else {
				// not on the same row, so put it between the start and the edge
				x = parentBox.x + (r.x - parentBox.x) / 2;
				if (x < DEFAULT_EDGE) {
					x = r.x - DEFAULT_EDGE;					
				}
			}
		} else {
			// after the last component
			x = parentBox.right() - (parentBox.right() - r.right()) / 2;
			if (x > DEFAULT_EDGE) {
				x = r.right() + DEFAULT_EDGE;
			}
		}
		
		Point p1 = new Point(x, r.y - HEIGHT_PADDING);
		fb.translateToRelative(p1);
		Point p2 = new Point(x, r.y + r.height + HEIGHT_PADDING);
		fb.translateToRelative(p2);
		fb.setPoint(p1, 0);
		fb.setPoint(p2, 1);
	}

	/*
	 * Create an editpart listener for the host edit part that will show the grid
	 * if the editpart is selected. This is based on the SHOW_GRID_WHEN_SELECTED preferences.
	 */
	private EditPartListener createEditPartSelectionListener() {
		return new EditPartListener.Stub() {

			public void selectedStateChanged(EditPart editpart) {
				if (CDEPlugin.getPlugin().getPluginPreferences().getBoolean(CDEPlugin.SHOW_GRID_WHEN_SELECTED)) {
					if (editpart == null || editpart == getHost()
							&& (editpart.getSelected() == EditPart.SELECTED || editpart.getSelected() == EditPart.SELECTED_PRIMARY)) {
						if (gridController != null)
							gridController.setGridShowing(true);
					} else {
						if (gridController != null)
							gridController.setGridShowing(false);
					}
				} else {
					// Hide the grid just in case we were show before and changed the prefs
					if (gridController != null && gridController.isGridShowing())
						gridController.setGridShowing(false);
				}
			}
		};
	}

}

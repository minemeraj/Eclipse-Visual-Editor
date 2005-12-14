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
package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy;
import org.eclipse.gef.requests.*;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionFilter;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.commands.NoOpCommand;
import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IContainmentHandler.StopRequestException;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

/**
 * Layout edit policy for SWT Grid Layout.
 * <p>
 * It can handle both Left to Right and Right to Left by working model coordinates instead of figure coordinates.
 * 
 * @since 1.1.0
 */
public class GridLayoutEditPolicy extends ConstrainedLayoutEditPolicy implements IGridListener, IActionFilter {

	// unique ID of this layout edit policy
	public static final String LAYOUT_ID = "org.eclipse.swt.layout.GridLayout"; //$NON-NLS-1$

	public static final String REQ_GRIDLAYOUT_SPAN = "GridLayout span cells"; //$NON-NLS-1$
	
	static final int DEFAULT_CELL_WIDTH = 40;
	static final int DEFAULT_CELL_HEIGHT = 35;

	// private final int DEFAULT_EDGE = 5;
	// private final int HEIGHT_PADDING = 4;

	boolean fShowGrid = false;

	JavaHelpers controlType;
	
	GridLayoutPolicyHelper helper = new GridLayoutPolicyHelper();

	private GridLayoutGridFigure fGridLayoutGridFigure;

	private GridLayoutSpanFeedbackFigure fGridLayoutSpanFigure;

	private GridLayoutFeedbackFigure fGridLayoutCellFigure;

	private IFigure fRowColFigure = null;

	private GridController gridController;

	private IVisualComponentListener fGridComponentListener;

	private VisualContainerPolicy containerPolicy;

	protected FigureListener hostFigureListener = new FigureListener() {

		public void figureMoved(IFigure source) {
			helper.refresh();
			refreshGridFigure();
		}
	};

	private EditPartListener editPartListener;

	private class GridComponentListener extends VisualComponentAdapter {

		public void componentValidated() {
			helper.refresh();
			refreshGridFigure();
		}
	}

	protected GridLayoutPolicyHelper getHelper() {
		return helper;
	}

	protected IVisualComponentListener getGridComponentListener() {
		if (fGridComponentListener == null)
			fGridComponentListener = new GridComponentListener();
		return fGridComponentListener;
	}

	class GridLayoutRowFigure extends Figure {

		Rectangle rowBounds;

		public GridLayoutRowFigure(Rectangle rowBounds) {
			super();
			this.rowBounds = rowBounds;
			setBounds(rowBounds.getCopy().expand(8, 6));
		}

		public void paintFigure(Graphics g) {
			try {
				g.setAlpha(150);
			} catch (SWTException e) {
				// For OS platforms that don't support alpha
			}
			int[] polygonPoints = new int[] { bounds.x + 1, bounds.y + 1, // left upper corner
					bounds.x + 1, bounds.y + bounds.height - 1, rowBounds.x, rowBounds.y + rowBounds.height, rowBounds.x + rowBounds.width,
					rowBounds.y + rowBounds.height, bounds.x + bounds.width - 1, bounds.y + bounds.height - 1, bounds.x + bounds.width - 1,
					bounds.y + 1, rowBounds.x + rowBounds.width, rowBounds.y, rowBounds.x, rowBounds.y, bounds.x + 1, bounds.y + 1};
			g.setBackgroundColor(ColorConstants.yellow);
			g.fillPolygon(polygonPoints);
			g.setBackgroundColor(ColorConstants.black);
			g.drawPolygon(polygonPoints);
		}
	}

	class GridLayoutColumnFigure extends Figure {

		Rectangle columnBounds;

		public GridLayoutColumnFigure(Rectangle columnBounds) {
			super();
			this.columnBounds = columnBounds;
			setBounds(columnBounds.getCopy().expand(6, 8));
		}

		public void paintFigure(Graphics g) {
			try {
				g.setAlpha(150);
			} catch (SWTException e) {
				// For OS platforms that don't support alpha
			}
			int[] polygonPoints = new int[] { bounds.x + 1, bounds.y + 1, // left upper corner
					columnBounds.x, columnBounds.y, columnBounds.x, columnBounds.y + columnBounds.height, bounds.x + 1, bounds.y + bounds.height - 1,
					bounds.x + bounds.width - 1, bounds.y + bounds.height - 1, columnBounds.x + columnBounds.width,
					columnBounds.y + columnBounds.height, columnBounds.x + columnBounds.width, columnBounds.y, bounds.x + bounds.width - 1,
					bounds.y + 1, bounds.x + 1, bounds.y + 1};
			g.setBackgroundColor(ColorConstants.yellow);
			g.fillPolygon(polygonPoints);
			g.setBackgroundColor(ColorConstants.black);
			g.drawPolygon(polygonPoints);
		}
	}

	/*
	 * Class used to identify the type of request relative to GridLayout.
	 */
	
	static final int NO_ADD = -1;
	
	static final int INSERT_COLUMN = 0;

	static final int INSERT_COLUMN_WITHIN_ROW = 1;

	static final int INSERT_ROW = 2;

	static final int ADD_COLUMN = 3;

	static final int ADD_ROW = 4;
	
	static final int ADD_ROW_COL = 5;

	static final int REPLACE_FILLER = 6;

	static final int ADD_TO_EMPTY_CELL = 7;

	public static class GridLayoutRequest {

		int type;

		int column;

		int row;
	}

	/**
	 * @param containerPolicy
	 */
	public GridLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		this.containerPolicy = containerPolicy;
		helper.setContainerPolicy(containerPolicy);
	}

	public void setHost(EditPart host) {
		super.setHost(host);
		EditDomain editDomain = containerPolicy.getEditDomain();
		controlType = JavaRefFactory.eINSTANCE.reflectType("org.eclipse.swt.widgets.Control", EMFEditDomainHelper.getResourceSet(editDomain)); //$NON-NLS-1$
	}

	public void activate() {
		gridController = new GridController();
		if (gridController != null) {
			gridController.addGridListener(this);
			GridController.registerEditPart(getHost(), gridController);
			ControlProxyAdapter beanProxy = (ControlProxyAdapter) BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) getHost().getModel());
			if (beanProxy != null)
				beanProxy.addComponentListener(getGridComponentListener());
			getHostFigure().addFigureListener(hostFigureListener); // need to know when the host figure changes so we can refresh the grid

			// show grid if host editpart is selected and prefs is set
			if (CDEPlugin.getPlugin().getPluginPreferences().getBoolean(CDEPlugin.SHOW_GRID_WHEN_SELECTED)
					&& (getHost().getSelected() == EditPart.SELECTED || getHost().getSelected() == EditPart.SELECTED_PRIMARY))
				gridController.setGridShowing(true);
			// Add editpart listener to show grid when selected if prefs is set
			editPartListener = createEditPartListener();
			getHost().addEditPartListener(editPartListener);
			List children = getHost().getChildren();
			Iterator iterator = children.iterator();
			while (iterator.hasNext())
				((EditPart) iterator.next()).addEditPartListener(editPartListener);
		}
		containerPolicy.setContainer(getHost().getModel());
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
		ControlProxyAdapter beanProxy = (ControlProxyAdapter) BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) getHost().getModel());
		if (beanProxy != null)
			beanProxy.removeComponentListener(getGridComponentListener());
		getHostFigure().removeFigureListener(hostFigureListener);
		if (editPartListener != null) {
			getHost().removeEditPartListener(editPartListener);
			List children = getHost().getChildren();
			Iterator iterator = children.iterator();
			while (iterator.hasNext())
				((EditPart) iterator.next()).removeEditPartListener(editPartListener);
			editPartListener = null;
		}
		super.deactivate();
	}

	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new NonResizableSpannableEditPolicy(this);
	}

	/**
	 * Factors out REQ_GRIDLAYOUT_SPAN requests, otherwise calls <code>super</code>.
	 * 
	 * @see org.eclipse.gef.EditPolicy#getCommand(Request)
	 */
	public Command getCommand(Request request) {
		if (REQ_GRIDLAYOUT_SPAN.equals(request.getType()))
			return getSpanChildrenCommand(request);

		return super.getCommand(request);
	}

	protected Command getSpanChildrenCommand(Request generic) {
		ChangeBoundsRequest request = (ChangeBoundsRequest) generic;
		List editParts = request.getEditParts();
		if (editParts.isEmpty() || editParts.size() > 1)
			return UnexecutableCommand.INSTANCE;

		// Get the cell location that the mouse was dragged to
		Point spanToPosition = getLocationFromRequest(request).getCopy();
		Point spanToCellLocation = getGridLayoutGridFigure().getCellLocation(mapFigureToModel(spanToPosition.x, spanToPosition.y));
		// Get the cell location where we started the drag operation
		Dimension dim = request.getSizeDelta();
		int handleSizeOffset = GridSpanHandle.HANDLE_SIZE / 2;
		Point startPosition = mapFigureToModel(spanToPosition.x - dim.width - handleSizeOffset, spanToPosition.y - dim.height - handleSizeOffset);
		// Get the cell location of the child component
		GraphicalEditPart ep = (GraphicalEditPart) editParts.get(0);
		EObject child = (EObject) ep.getModel();
		Point childCellLocation = helper.getChildDimensions(child).getLocation();
		Point startCellLocation = getGridLayoutGridFigure().getCellLocation(startPosition);
		// If the cell location where the pointer is located is different from the original cell location where we started,
		// create the commands to change the gridwidth or gridheight
		if ((spanToCellLocation.x >= childCellLocation.x && spanToCellLocation.y >= childCellLocation.y)
				&& (spanToCellLocation.x != startCellLocation.x || spanToCellLocation.y != startCellLocation.y)) {
			// Let the helper get the gridWidth or gridHeight commands based on the cell location
			// where the pointer is and the span direction (EAST for gridwidth or SOUTH for gridheight)
			helper.startRequest();
			helper.spanChild(child, new Point(spanToCellLocation.x-childCellLocation.x+1, spanToCellLocation.y-childCellLocation.y+1), request.getResizeDirection(), null); 
			return helper.stopRequest();
		}
		return NoOpCommand.INSTANCE;
	}

	/**
	 * @see org.eclipse.ui.IActionFilter#testAttribute(Object, String, String)
	 * 
	 * Return true for show grid action if grid is hidden Return true for hide grid action if grid is showing Return true if the layoutpolicy is
	 * GridLayout otherwise return false
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
			int[][] layoutDimensions = null;
			GridLayoutPolicyHelper.GridComponent[][] cellContents = null;
			Rectangle layoutSpacing = null;
			/*
			 * If the container is empty, we can't depend on the layout dimensions or layout origin from the GridLayout, so we have nothing being
			 * drawn.
			 */
			if (helper.getContainer() != null && !getHost().getChildren().isEmpty()) {
				layoutDimensions = helper.getContainerLayoutDimensions();
				layoutSpacing = helper.getContainerLayoutSpacing();
				cellContents = helper.getLayoutTable();
			}
			fGridLayoutGridFigure = new GridLayoutGridFigure(f.getBounds().getCopy(), layoutDimensions, cellContents, layoutSpacing, helper);
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

	// private static final Object requestData = new Object();
	public void eraseTargetFeedback(Request request) {
		if (!fShowGrid)
			if (fGridLayoutGridFigure != null && fGridLayoutGridFigure.getParent() != null) {
				removeFeedback(fGridLayoutGridFigure);
				fGridLayoutGridFigure = null;
			}
		if (fGridLayoutSpanFigure != null) {
			removeFeedback(fGridLayoutSpanFigure);
			fGridLayoutSpanFigure = null;
		}
		if (fGridLayoutCellFigure != null) {
			removeFeedback(fGridLayoutCellFigure);
			fGridLayoutCellFigure = null;
		}
		if (fRowColFigure != null) {
			removeFeedback(fRowColFigure);
			fRowColFigure = null;
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
	 * Show target feedback when dragging the span handles of a component. Highlight the cells the component will occupy based on the begining cell
	 * position and end cell position of the pointer.
	 */
	public void showSpanTargetFeedback(ChangeBoundsRequest request) {
		// If the grid is not on, turn it on
		if (!fShowGrid)
			addFeedback(getGridLayoutGridFigure());

		Point spanToPosition = mapFigureToModel(request.getLocation().getCopy());

		// Get the cell location of the child component
		GraphicalEditPart ep = (GraphicalEditPart) request.getEditParts().get(0);
		EObject child = (EObject) ep.getModel();
		Rectangle childDim = getHelper().getChildDimensions(child);
		
		// Get the start and end cell bounds in order to determine the entire bounds of the cell feedback figure.
		Rectangle startCellBounds = getGridLayoutGridFigure().getCellBounds(childDim.getLocation());
		Rectangle endCellChildBounds = getGridLayoutGridFigure().getCellBounds(childDim.getBottomRight().translate(-1,-1));	// This is the lower right of the child itself.
		if (request.getResizeDirection() == PositionConstants.EAST || request.getResizeDirection() == PositionConstants.WEST) {
			spanToPosition.y = endCellChildBounds.y; // This forces us to not span north/south when going east/west. And it will make it tall enough that entire cell spanned height is covered.
		} else {
			spanToPosition.x = endCellChildBounds.x+endCellChildBounds.width-1; // This forces us to not span left/right when going north/south. And it will make it wide enough that entire cell spanned width is covered.
		}
		Rectangle endCellBounds = getGridLayoutGridFigure().getCellBounds(getGridLayoutGridFigure().getCellLocation(spanToPosition));
		if (endCellBounds == null || endCellBounds.x < startCellBounds.x || endCellBounds.y < startCellBounds.y) {
			// End is not within a cell, or the end is before the start cell.
			if (fGridLayoutSpanFigure != null) {
				removeFeedback(fGridLayoutSpanFigure);
				fGridLayoutSpanFigure = null;
			}
			return;
		}

		Rectangle spanrect = startCellBounds.union(endCellBounds).resize(-1, -1);
		if (fGridLayoutSpanFigure == null) {
			fGridLayoutSpanFigure = new GridLayoutSpanFeedbackFigure(request.getResizeDirection());
		}
		fGridLayoutSpanFigure.setLayoutFigureBounds(mapModelToFigure(spanrect));
		addFeedback(fGridLayoutSpanFigure);
	}

	/**
	 * The model has changed. Remove and reconstruct the grid figure to reflect the changes.
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.core.IGridListener#gridHeightChanged(int, int)
	 */
	public void gridHeightChanged(int gridHeight, int oldGridHeight) {
		// nothing to do here
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.core.IGridListener#gridWidthChanged(int, int)
	 */
	public void gridWidthChanged(int gridWidth, int oldGridWidth) {
		// nothing to do here
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.core.IGridListener#gridMarginChanged(int, int)
	 */
	public void gridMarginChanged(int gridMargin, int oldGridMargin) {
		// nothing to do here
	}

	// modifying the feedback behavior from FlowLayoutEditPolicy

	/*
	 * this gets the location from the request and then makes it
	 * absolute (relative to the bounds of the host figure). If
	 * we didn't do this the point is relative to the viewport
	 * that is displayed, not absolute wrt to the entire canvas. 
	 */
	private Point getLocationFromRequest(Request request) {
		Point loc = ((DropRequest) request).getLocation().getCopy();
		getHostFigure().translateToRelative(loc);
		return loc;
	}

	/**
	 * Shows an insertion line if there is one or more current children.
	 */
	protected void showLayoutTargetFeedback(Request request) {

		if (!fShowGrid)
			addFeedback(getGridLayoutGridFigure());

		if (fRowColFigure != null) {
			removeFeedback(fRowColFigure);
			fRowColFigure = null;
		}
		
		if (fGridLayoutCellFigure != null) {
			removeFeedback(fGridLayoutCellFigure);
			fGridLayoutCellFigure = null;
		}

		// We need to determine if true child is a control. If it is then continue, if not, then we have normal feedback and let container policy handle it.
		try {
			// First see if the true child is a control. If not, then let normal create processing handle it.
			Object child;
			if (request.getType().equals(RequestConstants.REQ_CREATE)) { 
				child = ((CreateRequest) request).getNewObject();
			} else if (request.getType().equals(RequestConstants.REQ_ADD) || request.getType().equals(RequestConstants.REQ_MOVE)) {
				child = ((EditPart) ((ChangeBoundsRequest) request).getEditParts().get(0)).getModel();
			} else
				return;	// Anything else has no feedback.
			Object trueChild = containerPolicy.getTrueChild(child, VisualContainerPolicy.CREATE_REQ, new CommandBuilder(), new CommandBuilder());
			if (trueChild == null || !controlType.isInstance(trueChild))
				return;	// No extra feedback.
		} catch (StopRequestException e) {
			return;
		}
		
		Point position = getLocationFromRequest(request).getCopy();
		Point positionModel = mapFigureToModel(position.getCopy());
		GridLayoutRequest gridReq = createGridLayoutRequest(positionModel);

		Point cell = new Point(gridReq.column, gridReq.row);
		Rectangle cellBounds = getGridLayoutGridFigure().getCellBounds(cell);

		// Calculate the bounds of the target cell figure based on whether a column is added,
		// row is added, or it's inserted before another control.
		switch (gridReq.type) {
			case INSERT_COLUMN:
				// If a column is added, show the target figure in between the two columns
				showNewColumnFeedBack(gridReq.column);
				Rectangle colFigBounds = fRowColFigure.getBounds();
				mapModelToFigure(cellBounds);
				cellBounds.width = DEFAULT_CELL_WIDTH;
				if (cellBounds.height < 10)
					cellBounds.expand(0, 20);
				cellBounds.x = colFigBounds.x + colFigBounds.width / 2 - cellBounds.width / 2;
				break;
			case INSERT_ROW:
				// If a row is added, show the target figure in between the two rows
				showNewRowFeedBack(gridReq.row);
				Rectangle rowFigBounds = fRowColFigure.getBounds();
				mapModelToFigure(cellBounds);
				cellBounds.height = DEFAULT_CELL_HEIGHT;
				if (cellBounds.width < 10)
					cellBounds.expand(20, 0);
				cellBounds.y = rowFigBounds.y + rowFigBounds.height / 2 - cellBounds.height / 2;
				break;
			case INSERT_COLUMN_WITHIN_ROW:
				// In case cell is spanned vertically we need to have the complete cellbounds.
				showColumnFeedBackWithinARow(cellBounds);
				mapModelToFigure(cellBounds);
				colFigBounds = fRowColFigure.getBounds();
				cellBounds.width = DEFAULT_CELL_WIDTH;
				if (cellBounds.height < 10)
					cellBounds.expand(0, 20);
				cellBounds.x = colFigBounds.x + colFigBounds.width / 2 - cellBounds.width / 2;
				break;
			case ADD_COLUMN:
			case ADD_ROW:
			case ADD_ROW_COL:
				showAddedCellFeedback(cellBounds);
				colFigBounds = fRowColFigure.getBounds();
				// Center the cell within these bounds.
				cellBounds = colFigBounds.getCopy().shrink(ADDEDCELLBORDER, ADDEDCELLBORDER);
				break;
			case NO_ADD:
				return;	// No feedback.
			default:
				mapModelToFigure(cellBounds.expand(cellBounds.width < 10 ? 20 : 0, cellBounds.height < 10 ? 20 : 0));
				break;
		}

		if (fGridLayoutCellFigure == null) {
			fGridLayoutCellFigure = new GridLayoutFeedbackFigure();
		}
		fGridLayoutCellFigure.setBounds(cellBounds);
		addFeedback(fGridLayoutCellFigure);
	}

	/*
	 * Create an editpart listener for the host edit part that will show the grid if the editpart is selected. This is based on the
	 * SHOW_GRID_WHEN_SELECTED preferences.
	 */
	private EditPartListener createEditPartListener() {
		return new EditPartListener.Stub() {

			public void selectedStateChanged(EditPart editpart) {
				if (CDEPlugin.getPlugin().getPluginPreferences().getBoolean(CDEPlugin.SHOW_GRID_WHEN_SELECTED)) {
					if ((editpart == null || editpart == getHost() || isChildEditPart(editpart))
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

			public void childAdded(EditPart editpart, int index) {
				if (editPartListener != null)
					editpart.addEditPartListener(editPartListener);
			}

			public void removingChild(EditPart editpart, int index) {
				if (editPartListener != null)
					editpart.removeEditPartListener(editPartListener);
			}
		};
	}

	/*
	 * Return true if ep is a child editpart of the host container
	 */
	private boolean isChildEditPart(EditPart ep) {
		if (ep != null) {
			List children = getHost().getChildren();
			if (!children.isEmpty())
				return (children.indexOf(ep) != -1);
		}
		return false;
	}

	protected Command getDeleteDependantCommand(Request aRequest) {
		// Get the commands to insert filler labels into the cells where the control used to be
		if (aRequest instanceof ForwardedRequest) {
			EditPart editPart = ((ForwardedRequest) aRequest).getSender();
			helper.startRequest();
			helper.deleteChild((EObject) editPart.getModel());
			return helper.stopRequest();
		} else
			return UnexecutableCommand.INSTANCE;
	}

	/**
	 * Show a new yellow row inserted into the grid near the row closest to position
	 */
	protected void showNewRowFeedBack(int row) {
		Rectangle rect = fGridLayoutGridFigure.getRowRectangle(row);
		rect.translate(-2, -3);
		rect.width += 4;
		rect.height = 6;
		fRowColFigure = new GridLayoutRowFigure(mapModelToFigure(rect));
		addFeedback(fRowColFigure);
	}

	/**
	 * Show a new yellow column inserted into the grid near the column closest to position
	 */
	protected void showNewColumnFeedBack(int col) {
		Rectangle rect = fGridLayoutGridFigure.getColumnRectangle(col);
		rect.x -= 3;
		rect.width = 6;
		fRowColFigure = new GridLayoutColumnFigure(mapModelToFigure(rect));
		addFeedback(fRowColFigure);
	}

	/**
	 * Show a new yellow column inserted into the grid near the column closest to position and only within that row
	 */
	protected void showColumnFeedBackWithinARow(Rectangle cellBounds) {
		cellBounds = cellBounds.getCopy();
		cellBounds.x -= 3; // start to the right by three from side of cell.
		cellBounds.width = 6; // But only six wide. So it will be centered over the right side of the cell.
		fRowColFigure = new GridLayoutColumnFigure(mapModelToFigure(cellBounds));
		addFeedback(fRowColFigure);
	}
	
	private static final int ADDEDCELLBORDER = 3;	// How much to expand cell for added cell to draw the new border.
	
	/**
	 * Show the adding cell outside figure feedback.
	 * @param cellBounds
	 * 
	 * @since 1.2.0
	 */
	protected void showAddedCellFeedback(Rectangle cellBounds) {
		cellBounds = cellBounds.getExpanded(cellBounds.width < 10 ? 20 : 0, cellBounds.height < 10 ? 20 : 0);
		fRowColFigure = new GridLayoutAddedCellFeedbackFigure();
		fRowColFigure.setBounds(mapModelToFigure(cellBounds));
		addFeedback(fRowColFigure);
	}

	protected Command createAddCommand(EditPart child, Object constraint) {
		return null;
	}

	protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
		return null;
	}

	protected Object getConstraintFor(Point point) {
		return null;
	}

	protected Object getConstraintFor(Rectangle rect) {
		return null;
	}

	protected Command getCreateCommand(CreateRequest request) {
		if (fGridLayoutGridFigure == null)
			return UnexecutableCommand.INSTANCE;
		
		// Since we need to work with true EObjects for the grid to work correctly, we need to bypass the normal processing and 
		// get the real EObject child, if different than the new object.
		EObject trueEObject;
		try {
			// First see if the true child is a control. If not, then let normal create processing handle it.
			Object trueChild = containerPolicy.getTrueChild(request.getNewObject(), VisualContainerPolicy.CREATE_REQ, new CommandBuilder(), new CommandBuilder());
			if (trueChild == null || !controlType.isInstance(trueChild))
				return containerPolicy.getCreateCommand(request.getNewObject(), null).getCommand();	// Do normal, the container policy will handle it.
			trueEObject = (EObject) trueChild;
		} catch (StopRequestException e) {
			return UnexecutableCommand.INSTANCE;
		}
		
		Point position = mapFigureToModel(getLocationFromRequest(request).getCopy());
		GridLayoutRequest gridReq = createGridLayoutRequest(position);
		Point cell = new Point(gridReq.column, gridReq.row);
		
		Object requestType = request.getType();
		
		CommandBuilder cb = new CommandBuilder();
		helper.startRequest();
		switch (gridReq.type) {
			case REPLACE_FILLER:
				helper.replaceFiller(trueEObject, request.getNewObject(), requestType, cell);
				break;
			case INSERT_COLUMN_WITHIN_ROW:
				helper.insertColWithinRow(cell);
				helper.replaceFillerOrEmpty(trueEObject, request.getNewObject(), requestType, cell);
				break;
			case INSERT_COLUMN:
			case ADD_COLUMN:
				helper.createNewCol(cell.x);
				helper.replaceFillerOrEmpty(trueEObject, request.getNewObject(), requestType, cell);
				break;
			case INSERT_ROW:
			case ADD_ROW:
				helper.createNewRow(cell.y);
				helper.replaceFillerOrEmpty(trueEObject, request.getNewObject(), requestType, cell);
				break;
			case ADD_TO_EMPTY_CELL:
				helper.replaceEmptyCell(trueEObject, request.getNewObject(), requestType, cell);
				break;
			case ADD_ROW_COL:
				cell.setLocation(helper.getNumColumns(), helper.getNumRows());
				helper.createNewCol(cell.x);
				if (cell.x != 0 || cell.y != 0)
					helper.createNewRow(cell.y);	// If other than (0,0) for add row col, we need a new row. If it was (0,0) then we are adding the first entry to the grid.
				helper.replaceFillerOrEmpty(trueEObject, request.getNewObject(), requestType, cell);
				break;
			case NO_ADD:
				return UnexecutableCommand.INSTANCE;
		}
		
		cb.append(helper.stopRequest());
		
		if (cb.isEmpty())
			return UnexecutableCommand.INSTANCE;
		return cb.getCommand();
	}

	private GridLayoutRequest createGridLayoutRequest(Point position) {
		return getGridLayoutGridFigure().getGridLayoutRequest(position, helper);
	}

	protected Command getOrphanChildrenCommand(Request request) {
		// Get the commands to insert filler labels into the cells where the control used to be
		if (request instanceof GroupRequest) {
			helper.startRequest();
			List children = VisualContainerPolicy.getChildren((GroupRequest) request);
			helper.orphanChildren(children);
			return helper.stopRequest();
		} else
			return UnexecutableCommand.INSTANCE;
	}

	protected Command getAddCommand(Request request) {
		return getMoveChildrenCommand(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getMoveChildrenCommand(org.eclipse.gef.Request)
	 * 
	 * This method will handle both moving a child within the same container and adding a child from another container. The only difference is that
	 * for the move, there is extra processing to remove empty rows or columns and filler labels have to inserted into the cells where the child moved
	 * from.
	 */
	protected Command getMoveChildrenCommand(Request request) {
		if (fGridLayoutGridFigure == null || !(request instanceof ChangeBoundsRequest))
			return UnexecutableCommand.INSTANCE;
		ChangeBoundsRequest req = (ChangeBoundsRequest) request;
		List editparts = req.getEditParts();
		// Only allow one object to be moved
		if (editparts.size() > 1)
			return UnexecutableCommand.INSTANCE;
		
		EObject trueEObject;
		Object child;
		try {
			// First see if the true child is a control. If not, then let normal create processing handle it.
			child = ((EditPart) editparts.get(0)).getModel();
			Object trueChild = containerPolicy.getTrueChild(child, VisualContainerPolicy.ADD_REQ, new CommandBuilder(), new CommandBuilder());
			if (trueChild == null || !controlType.isInstance(trueChild))
				return containerPolicy.getAddCommand(Collections.singletonList(child), null).getCommand();	// Do normal, the container policy will handle it.
			trueEObject = (EObject) trueChild;
		} catch (StopRequestException e) {
			return UnexecutableCommand.INSTANCE;
		}
		
		Point position = mapFigureToModel(getLocationFromRequest(request).getCopy());
		GridLayoutRequest gridReq = createGridLayoutRequest(position);
		Point cell = new Point(gridReq.column, gridReq.row);
		
		Object requestType = request.getType();
		
		CommandBuilder cb = new CommandBuilder();
		helper.startRequest();
		switch (gridReq.type) {
			case REPLACE_FILLER:
				helper.replaceFiller(trueEObject, child, requestType, cell);
				break;
			case INSERT_COLUMN_WITHIN_ROW:
				helper.insertColWithinRow(cell);
				helper.replaceFillerOrEmpty(trueEObject, child, requestType, cell);
				break;
			case INSERT_COLUMN:
			case ADD_COLUMN:
				helper.createNewCol(cell.x);
				helper.replaceFillerOrEmpty(trueEObject, child, requestType, cell);
				break;
			case INSERT_ROW:
			case ADD_ROW:
				helper.createNewRow(cell.y);
				helper.replaceFillerOrEmpty(trueEObject, child, requestType, cell);
				break;
			case ADD_TO_EMPTY_CELL:
				helper.replaceEmptyCell(trueEObject, child, requestType, cell);
				break;
			case ADD_ROW_COL:
				cell.setLocation(helper.getNumColumns(), helper.getNumRows());
				helper.createNewCol(cell.x);
				if (cell.x != 0 || cell.y != 0)
					helper.createNewRow(cell.y);	// If other than (0,0) for add row col, we need a new row. If it was (0,0) then we are adding the first entry to the grid.
				helper.replaceFillerOrEmpty(trueEObject, child, requestType, cell);
				break;
			case NO_ADD:
				return UnexecutableCommand.INSTANCE;
		}
		
		cb.append(helper.stopRequest());
		
		if (cb.isEmpty())
			return UnexecutableCommand.INSTANCE;
		return cb.getCommand();
	}

	public Rectangle getFullCellBounds(EditPart child) {
		if (getGridLayoutGridFigure() == null)
			return new Rectangle();
		Rectangle dims = helper.getChildDimensions((EObject) child.getModel());
		Rectangle bounds;
		if (dims != null) {
			bounds = getGridLayoutGridFigure().getGridBroundsForCellBounds(dims);
		} else
			bounds = new Rectangle();

		return mapModelToFigure(bounds);
	}

	public Rectangle mapModelToFigure(Rectangle rect) {
		return getGridLayoutGridFigure().mapModelToFigure(rect);
	}

	public Point mapFigureToModel(int x, int y) {
		return getGridLayoutGridFigure().mapFigureToModel(x, y);
	}

	public Point mapFigureToModel(Point p) {
		return getGridLayoutGridFigure().mapFigureToModel(p);
	}

	public Rectangle mapFigureToModel(Rectangle rect) {
		return getGridLayoutGridFigure().mapFigureToModel(rect);
	}

}

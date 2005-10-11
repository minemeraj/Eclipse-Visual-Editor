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
/*
 * $RCSfile: GridLayoutEditPolicy.java,v $ 
 * $Revision: 1.41 $ $Date: 2005-10-11 21:23:47 $
 */
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

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.commands.NoOpCommand;
import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;


public class GridLayoutEditPolicy extends ConstrainedLayoutEditPolicy implements IGridListener, IActionFilter {
	
	// unique ID of this layout edit policy
	public static final String LAYOUT_ID = "org.eclipse.swt.layout.GridLayout"; //$NON-NLS-1$
	public static final String REQ_GRIDLAYOUT_SPAN = "GridLayout span cells"; //$NON-NLS-1$
	
//	private final int DEFAULT_EDGE = 5;
//	private final int HEIGHT_PADDING = 4;

	boolean fShowGrid = false;

	GridLayoutPolicyHelper helper = new GridLayoutPolicyHelper();
	int [][] layoutDimensions = null;
	private GridLayoutGridFigure fGridLayoutGridFigure;
	private GridLayoutSpanFeedbackFigure fGridLayoutSpanFigure;
	private GridLayoutFeedbackFigure fGridLayoutCellFigure;
	private GridLayoutRowFigure fRowFigure = null; 
	private GridLayoutColumnFigure fColumnFigure = null;
	private GridController gridController;
	private IVisualComponentListener fGridComponentListener;
	private org.eclipse.ve.internal.cde.core.ContainerPolicy containerPolicy;
	
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
			bounds = rowBounds.getCopy();
			bounds.expand(8, 6);
		}
		public void paintFigure(Graphics g) {
			try {
				g.setAlpha(150);
			} catch (SWTException e) {
				// For OS platforms that don't support alpha
			}
			int [] polygonPoints = new int [] {
					bounds.x+1, bounds.y+1, // left upper corner
					bounds.x+1, bounds.y + bounds.height-1,
					rowBounds.x, rowBounds.y + rowBounds.height,
					rowBounds.x + rowBounds.width, rowBounds.y + rowBounds.height,
					bounds.x + bounds.width-1, bounds.y + bounds.height-1,
					bounds.x + bounds.width-1, bounds.y + 1,
					rowBounds.x + rowBounds.width, rowBounds.y,
					rowBounds.x, rowBounds.y,
					bounds.x+1, bounds.y+1}; 
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
			bounds = columnBounds.getCopy();
			bounds.expand(6, 8);
		}
		public void paintFigure(Graphics g) {
			try {
				g.setAlpha(150);
			} catch (SWTException e) {
				// For OS platforms that don't support alpha
			}
			int [] polygonPoints = new int [] {
					bounds.x+1, bounds.y+1, // left upper corner
					columnBounds.x, columnBounds.y,
					columnBounds.x, columnBounds.y + columnBounds.height,
					bounds.x+1, bounds.y + bounds.height-1,
					bounds.x + bounds.width-1, bounds.y + bounds.height-1,
					columnBounds.x + columnBounds.width, columnBounds.y + columnBounds.height,
					columnBounds.x + columnBounds.width, columnBounds.y,
					bounds.x + bounds.width-1, bounds.y+1,
					bounds.x+1, bounds.y+1}; 
			g.setBackgroundColor(ColorConstants.yellow);
			g.fillPolygon(polygonPoints);
			g.setBackgroundColor(ColorConstants.black);
			g.drawPolygon(polygonPoints);
		}
	}

	/*
	 * Class used to identify the type of request relative to GridLayout.
	 */
	static final int INSERT_COLUMN = 0;
	static final int INSERT_COLUMN_WITHIN_ROW = 1;
	static final int INSERT_ROW = 2;
	static final int ADD_COLUMN = 3;
	static final int ADD_ROW = 4;
	static final int REPLACE_FILLER = 5;
	static final int ADD = 6;
	static final int ADD_TO_EMPTY_CELL = 7;
	class GridLayoutRequest {
		int type = ADD;
		int column = 0;
		int row = 0;
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
		helper.setEditDomain(EditDomain.getEditDomain(this.getHost()));
	}
	
	public void activate(){
		gridController = new GridController();
		if (gridController != null) {
			gridController.addGridListener(this);
			GridController.registerEditPart(getHost(), gridController);
			ControlProxyAdapter beanProxy = (ControlProxyAdapter) BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance)getHost().getModel());
			if (beanProxy != null)
				beanProxy.addComponentListener(getGridComponentListener());
			getHostFigure().addFigureListener(hostFigureListener);	// need to know when the host figure changes so we can refresh the grid

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
		ControlProxyAdapter beanProxy = (ControlProxyAdapter) BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance)getHost().getModel());
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
		Point childPosition;
		Point childCellLocation;
		if (helper.getChildrenDimensions() != null)
			childCellLocation = helper.getChildrenDimensions()[getHost().getChildren().indexOf(ep)].getLocation();
		else {
			childPosition = ep.getContentPane().getBounds().getLocation();
			childCellLocation = getGridLayoutGridFigure().getCellLocation(childPosition.x, childPosition.y);
		}
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
			EObject[][] cellContents = null;
			Rectangle layoutSpacing = null;
			Rectangle clientArea = helper.getContainerClientArea();
			/*
			 * If the container is empty, we can't depend on the layout dimensions or layout origin
			 * from the GridLayout, so we have nothing being drawn.
			 */
			if (helper.getContainer() != null && !getHost().getChildren().isEmpty()) {
				layoutDimensions = helper.getContainerLayoutDimensions();
				layoutSpacing = helper.getContainerLayoutSpacing();
				cellContents = helper.getLayoutTable();
			}
			fGridLayoutGridFigure = new GridLayoutGridFigure(f.getBounds().getCopy(), layoutDimensions, cellContents, layoutSpacing, clientArea );
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
	//private static final Object requestData = new Object();
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
		if (fRowFigure != null) {
			removeFeedback(fRowFigure);
			fRowFigure = null;
		}
		if (fColumnFigure != null) {
			removeFeedback(fColumnFigure);
			fColumnFigure = null;
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
		if (request.getResizeDirection() == PositionConstants.EAST) {
			spanToPosition.y = childPosition.y;	// This forces us to not span north/south when going east/west.
		} else {
			spanToPosition.x = childPosition.x;	// This forces us to not span left/right when going north/south.
		}
		Rectangle endCellBounds = getGridLayoutGridFigure().getCellBounds(spanToPosition.x, spanToPosition.y);
		Rectangle spanrect = new Rectangle(startCellBounds.x, 
									startCellBounds.y, 
									endCellBounds.x + endCellBounds.width - startCellBounds.x,
									endCellBounds.y + endCellBounds.height - startCellBounds.y);
		if (fGridLayoutSpanFigure == null) {
			fGridLayoutSpanFigure = new GridLayoutSpanFeedbackFigure(request.getResizeDirection());
		}
		fGridLayoutSpanFigure.setLayoutFigureBounds(spanrect);
		addFeedback(fGridLayoutSpanFigure);
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
		// nothing to do here
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IGridListener#gridWidthChanged(int, int)
	 */
	public void gridWidthChanged(int gridWidth, int oldGridWidth) {
		// nothing to do here
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IGridListener#gridMarginChanged(int, int)
	 */
	public void gridMarginChanged(int gridMargin, int oldGridMargin) {
		// nothing to do here
	}
	
	// modifying the feedback behavior from FlowLayoutEditPolicy
	
	private Point getLocationFromRequest(Request request) {
		return ((DropRequest)request).getLocation();
	}
	
	/**
	 * Shows an insertion line if there is one or more current children.
	 */	
	protected void showLayoutTargetFeedback(Request request) {
		
		if (!fShowGrid)
			addFeedback(getGridLayoutGridFigure());
		
		if (fRowFigure != null) {
			removeFeedback(fRowFigure);
			fRowFigure = null;
		}
		if (fColumnFigure != null) {
			removeFeedback(fColumnFigure);
			fColumnFigure = null;
		}

		Point position = getLocationFromRequest(request).getCopy();
		// This point is absolute.  Make it relative to the model
		getHostFigure().translateToRelative(position);

		GridLayoutRequest gridReq = createGridLayoutRequest(position);

		Rectangle cellBounds = getGridLayoutGridFigure().getCellBounds(position.x, position.y);

		// Calculate the bounds of the target cell figure based on whether a column is added,
		// row is added, or it's inserted before another control.
		if (gridReq.type == INSERT_COLUMN){
			// If a column is added, show the target figure in between the two columns
			showNewColumnFeedBack(position);
			Rectangle colFigBounds = fColumnFigure.getBounds().getCopy();
			cellBounds.width = 40;
			cellBounds.x = colFigBounds.x + colFigBounds.width/2 - cellBounds.width/2;
		} else if (gridReq.type == INSERT_ROW) {
			// If a row is added, show the target figure in between the two rows
			showNewRowFeedBack(position);
			Rectangle rowFigBounds = fRowFigure.getBounds().getCopy();
			cellBounds.height = 35;
			cellBounds.y = rowFigBounds.y + rowFigBounds.height/2 - cellBounds.height/2;
		} else if (gridReq.type == INSERT_COLUMN_WITHIN_ROW) {
			// just the cell is selected. need to find out if replacing or inserting
			showColumnFeedBackWithinARow(cellBounds);
			Rectangle colFigBounds = fColumnFigure.getBounds().getCopy();
			cellBounds.width = 40;
			cellBounds.x = colFigBounds.x + colFigBounds.width/2 - cellBounds.width/2;
		} else if (gridReq.type == ADD || gridReq.type == ADD_COLUMN || gridReq.type == ADD_ROW) {
			// No cell found, provide a default size and location.
			cellBounds.setLocation(position.x-6, position.y-6);
			cellBounds.width = 40;
			cellBounds.height = 35;
		}

		if (fGridLayoutCellFigure == null) {
			fGridLayoutCellFigure = new GridLayoutFeedbackFigure();
		}
		fGridLayoutCellFigure.setBounds(cellBounds);
		addFeedback(fGridLayoutCellFigure);
	}

	/*
	 * Create an editpart listener for the host edit part that will show the grid
	 * if the editpart is selected. This is based on the SHOW_GRID_WHEN_SELECTED preferences.
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
	private boolean isChildEditPart (EditPart ep) {
		if (ep != null) {
			List children = getHost().getChildren();
			if (!children.isEmpty())
				return (children.indexOf(ep) != -1);
		}
		return false;
	}

	protected Command getDeleteDependantCommand(Request aRequest) {
		// Get the commands for doing the delete
		Command cmd = containerPolicy.getCommand(aRequest);
		
		// Get the commands to insert filler labels into the cells where the control used to be 
		if (cmd != null && aRequest instanceof ForwardedRequest) {
			EditPart editPart = ((ForwardedRequest) aRequest).getSender();
			CommandBuilder cb = new CommandBuilder();
			cb.append(helper.getFillerLabelsForDeletedControlCommands((EObject) editPart.getModel()));
			cb.append(cmd);
			return cb.getCommand();
		}
		return cmd != null ? cmd : UnexecutableCommand.INSTANCE;
	}

	/**
	 * Show a new yellow row inserted into the grid near the row closest to position
	 */
	protected void showNewRowFeedBack(Point position) {
		Point rowStart = fGridLayoutGridFigure.getRowStartPosition(position.y);
		Point rowEnd = fGridLayoutGridFigure.getRowEndPosition(position.y);
		Rectangle rect = new Rectangle(rowStart.x-2, rowStart.y - 3, rowEnd.x - rowStart.x + 4, 6);
		fRowFigure = new GridLayoutRowFigure(rect);
		addFeedback(fRowFigure);
	}
	/**
	 * Show a new yellow column inserted into the grid near the column closest to position
	 */
	protected void showNewColumnFeedBack(Point position) {
		Point colStart = fGridLayoutGridFigure.getColumnStartPosition(position.x);
		Point colEnd = fGridLayoutGridFigure.getColumnEndPosition(position.x);
		Rectangle rect = new Rectangle(colStart.x - 3, colStart.y, 6, colEnd.y - colStart.y);
		fColumnFigure = new GridLayoutColumnFigure(rect);
		addFeedback(fColumnFigure);
	}
	/**
	 * Show a new yellow column inserted into the grid near the column closest to position 
	 * and only within that row
	 */
	protected void showColumnFeedBackWithinARow(Rectangle cellBounds) {
		Point colStart = cellBounds.getLocation();
		Point colEnd = new Point(cellBounds.x, cellBounds.y + cellBounds.height);
		Rectangle rect = new Rectangle(colStart.x - 3, colStart.y, 6, colEnd.y - colStart.y);
		fColumnFigure = new GridLayoutColumnFigure(rect);
		addFeedback(fColumnFigure);
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
		List children = getHost().getChildren();
		if (children.isEmpty()) // If no children, just add to the end
			return containerPolicy.getCreateCommand(request.getNewObject(), null).getCommand();

		Point position = getLocationFromRequest(request).getCopy();
		// This point is absolute. Make it relative to the model
		getHostFigure().translateToRelative(position);
		GridLayoutRequest gridReq = createGridLayoutRequest(position);
		GraphicalEditPart editPart = null;
		Point cell = new Point(gridReq.column, gridReq.row);
		int epIndex = helper.getChildIndexAtCell(cell);
		if (epIndex != -1)
			editPart = (GraphicalEditPart) children.get(epIndex);
		CommandBuilder cb = new CommandBuilder();

		if (gridReq.type == REPLACE_FILLER) {
			// Just replace a filler label with the new control
			cb.append(containerPolicy.getCreateCommand(request.getNewObject(), editPart.getModel()).getCommand());
			cb.append(containerPolicy.getDeleteDependentCommand(editPart.getModel()));

		} else if (gridReq.type == INSERT_COLUMN_WITHIN_ROW) {
			cb.append(helper.createNumColumnsCommand(helper.getNumColumns() + 1)); // First add another column to the grid
			cb.append(helper.createInsertColumnWithinRowCommands(cell.x, cell.y, request.getNewObject(), request)); // then add empty labels at the end of each row

		} else if (gridReq.type == INSERT_COLUMN || gridReq.type == ADD_COLUMN) {
			boolean isLastColumn = false;
			int column = getGridLayoutGridFigure().getNearestColumn(position.x);
			// Not the last column
			if (column != helper.getNumColumns()) {
				epIndex = helper.getChildIndexAtCell(new Point(column, cell.y));
				// Not the last control
				if (epIndex != -1)
					editPart = (GraphicalEditPart) children.get(epIndex);
			} else {
				// Last column... look for the first control on the next row
				epIndex = helper.getChildIndexAtCell(new Point(0, cell.y + 1));
				if (epIndex != -1 && epIndex < children.size())
					editPart = (GraphicalEditPart) children.get(epIndex);
				else
					editPart = null;
				// Insert before the first column of each row since we actually
				// adding to the end of each row
				column = 0;
				cell.y = cell.y + 1;
				isLastColumn = true;
			}
			cb.append(helper.createNumColumnsCommand(helper.getNumColumns() + 1)); // First add another column to the grid
			cb.append(helper.createInsertColumnCommands(request.getNewObject(), request, column, cell.y, isLastColumn)); // then add empty labels at this column position

		} else if (gridReq.type == INSERT_ROW || gridReq.type == ADD_ROW) {
			int row = getGridLayoutGridFigure().getNearestRow(position.y);
			// Insert a row by adding labels and the new object at the appropriate column position
			cb.append(helper.createFillerLabelsForNewRowCommand(request.getNewObject(), row, cell.x, request));

		} else if (gridReq.type == ADD_TO_EMPTY_CELL) {
			// Add to an empty cell at a column position
			cb.append(helper.createAddToEmptyCellCommand(request.getNewObject(), cell, request));
		}

		if (cb.isEmpty())
			return UnexecutableCommand.INSTANCE;
		return cb.getCommand();
	}

	private GridLayoutRequest createGridLayoutRequest(Point position) {
		Point cell = getGridLayoutGridFigure().getCellLocation(position);
		GridLayoutRequest gridReq = new GridLayoutRequest(); // Create request. default is PUT request
		gridReq.column = cell.x;
		gridReq.row = cell.y;

		if (cell.x == -1 && cell.y == -1) 
			gridReq.type = ADD;
		else if (cell.x == -1)
			gridReq.type = ADD_COLUMN;
		else if (cell.y == -1)
			gridReq.type = ADD_ROW;
		else if (fGridLayoutGridFigure.isPointerNearAColumn(position.x))
			gridReq.type = INSERT_COLUMN;
		else if (fGridLayoutGridFigure.isPointerNearARow(position.y))
			gridReq.type = INSERT_ROW;
		else {
			EditPart editPart = null;
			Point cellLocation = new Point(cell.x, cell.y);
			int indexBeforeEP = helper.getChildIndexAtCell(cellLocation);
			if (indexBeforeEP != -1) {
				editPart = (EditPart) getHost().getChildren().get(indexBeforeEP);
				if (editPart != null) {
					if (helper.isFillerLabelAtCell(cellLocation))
						gridReq.type = REPLACE_FILLER;
					else
						gridReq.type = INSERT_COLUMN_WITHIN_ROW;
				}
			} else
				gridReq.type = ADD_TO_EMPTY_CELL;
		}
		return gridReq;
	}

	protected Command getOrphanChildrenCommand(Request request) {
		// Get the commands to do the orphaning
		Command cmd = containerPolicy.getCommand(request);
		
		// Get the commands to insert filler labels into the cells where the control used to be 
		if (cmd != null && request instanceof GroupRequest) {
			List editparts = ((GroupRequest)request).getEditParts();
			// Only allow one object to be orphaned
			if (editparts.size() > 1)
				return UnexecutableCommand.INSTANCE;
			EditPart editPart = (EditPart) editparts.iterator().next();
			CommandBuilder cb = new CommandBuilder();
			cb.append(helper.getFillerLabelsForDeletedControlCommands((EObject) editPart.getModel()));
			cb.append(cmd);
			return cb.getCommand();
		}
		return cmd != null ? cmd : UnexecutableCommand.INSTANCE;
	}

	protected Command getAddCommand(Request request) {
		return getMoveChildrenCommand(request);
	}	

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getMoveChildrenCommand(org.eclipse.gef.Request)
	 * 
	 * This method will handle both moving a child within the same container and adding a child
	 * from another container. The only difference is that for the move, there is extra processing
	 * to remove empty rows or columns and filler labels have to inserted into the cells where the child
	 * moved from. 
	 */
	protected Command getMoveChildrenCommand(Request request) {
		List children = getHost().getChildren();
		if (fGridLayoutGridFigure == null || !(request instanceof ChangeBoundsRequest))
			return UnexecutableCommand.INSTANCE;
		ChangeBoundsRequest req = (ChangeBoundsRequest) request;
		List editparts = req.getEditParts();
		// Only allow one object to be moved
		if (editparts.size() > 1)
			return null;

		Point position = req.getLocation();
		// This point is absolute.  Make it relative to the model
		getHostFigure().translateToRelative(position);
		GridLayoutRequest gridReq = createGridLayoutRequest(position);
		Point cell = new Point(gridReq.column, gridReq.row);
	
		int numColumns = helper.getNumColumns();
		EditPart childEP = (EditPart) editparts.iterator().next(), beforeEP = null;
		int childIndex = children.indexOf(childEP);
		int epIndex = helper.getChildIndexAtCell(cell);
		if (epIndex != -1)
			beforeEP = (EditPart) children.get(epIndex);
		CommandBuilder cb = new CommandBuilder();
	
		// No change if selected editpart and the target editpart are the same.
		if (childEP == beforeEP)
			return UnexecutableCommand.INSTANCE;
		EObject child = (EObject) childEP.getModel();
		
		// If child is not one of the children this is an add request (i.e. orphaned from one container and added to this container).
		if (childIndex == -1 && children.isEmpty())
			return containerPolicy.getAddCommand(Collections.singletonList(child), null).getCommand();

		// If it's an add child request, see if it's a valid add (can only add composites, not controls).
		if (childIndex == -1 && !(BeanSWTUtilities.isValidBeanLocation(containerPolicy.getEditDomain(), (IJavaObjectInstance)child, (EObject) containerPolicy.getContainer())))
			return UnexecutableCommand.INSTANCE;
		
		// Replace a filler label with the moved control
		if (gridReq.type == REPLACE_FILLER) {
			cb.append(containerPolicy.getMoveChildrenCommand(Collections.singletonList(child), beforeEP.getModel()));
			cb.append(containerPolicy.getDeleteDependentCommand(beforeEP.getModel()));
	
		// Insert the child within the row, adding one column
		} else if (gridReq.type == INSERT_COLUMN_WITHIN_ROW) {
			cb.append(helper.createNumColumnsCommand(++numColumns)); // First add another column to the grid
			cb.append(helper.createInsertColumnWithinRowCommands(cell.x, cell.y, child, request)); // then add empty labels at the end of each row
			
		// Insert the child within the row, adding a full column at the mouse position
		} else if (gridReq.type == INSERT_COLUMN || gridReq.type == ADD_COLUMN) {
			boolean isLastColumn = false;
			int column = getGridLayoutGridFigure().getNearestColumn(position.x);
			// Not the last column
			if (column != helper.getNumColumns()) {
				epIndex = helper.getChildIndexAtCell(new Point(column, cell.y));
				// Not the last control
				if (epIndex != -1)
					beforeEP = (GraphicalEditPart) children.get(epIndex);
			} else {
				// Last column... look for the first control on the next row
				epIndex = helper.getChildIndexAtCell(new Point(0, cell.y + 1));
				if (epIndex != -1 && epIndex < children.size())
					beforeEP = (GraphicalEditPart) children.get(epIndex);
				else
					beforeEP = null;
				// Insert before the first column of each row since we actually
				// adding to the end of each row
				column = 0;
				cell.y = cell.y + 1;
				isLastColumn = true;
			}
			cb.append(helper.createNumColumnsCommand(++numColumns)); // First add another column to the grid
			cb.append(helper.createInsertColumnCommands(child, request, column, cell.y, isLastColumn)); // then add empty labels at this column position
	
		} else if (gridReq.type == INSERT_ROW || gridReq.type == ADD_ROW) {
			int row = getGridLayoutGridFigure().getNearestRow(position.y);
			// Insert a row by adding labels and the new object at the appropriate column position
			cb.append(helper.createFillerLabelsForNewRowCommand(child, row, cell.x, request));
	
		} else if (gridReq.type == ADD_TO_EMPTY_CELL) {
			// Add to an empty cell at a column position
			cb.append(helper.createAddToEmptyCellCommand(child, cell, request));
		}
	
		// This next block of code is to handle moving a child only an assumes the child exists in this container.
		if (childIndex != -1) {
			// Remove columns and/or rows that are empty or contain all filler labels after the move
			Rectangle rect = helper.getChildrenDimensions()[childIndex];
			// If this control spans horizontally and/or vertically, reset to default values
			if (rect.width != helper.getDefaultHorizontalSpan())
				cb.append(helper.createHorizontalSpanDefaultCommand(child));
			if (rect.height != helper.getDefaultVerticalSpan())
				cb.append(helper.createVerticalSpanDefaultCommand(child));

			// Only interested in removing the row if the moved control cell and target cell is not the same row.
			Command rowCmds = null, columnCmds = null;
			if (rect.y != cell.y) {
				rowCmds = helper.createRemoveRowCommand(rect.y, child);
				if (rowCmds != null)
					cb.append(rowCmds);
			}
			// Only interested in removing the column if the moved control cell and target cell is not the same column.
			if (rect.x != cell.x) {
				columnCmds = helper.createRemoveColumnCommand(rect.x, child, helper.getNumColumns());
				if (columnCmds != null)
					cb.append(columnCmds);
			}
			// If no rows or colums to remove, add a filler label to fill the cell of the moved control.
			if (rowCmds == null && columnCmds == null) {
				if (beforeEP != null)
					cb.append(helper.createFillerLabelsForMovedControlCommands(child, (EObject) beforeEP.getModel()));
				else
					cb.append(containerPolicy.getCreateCommand(helper.createFillerLabelObject(), null).getCommand());
			}
		}
		if (cb.isEmpty())
			return UnexecutableCommand.INSTANCE;
		return cb.getCommand();
	}
	public Rectangle getFullCellBounds(EditPart child) {
		Rectangle bounds = new Rectangle();
		List children = getHost().getChildren();
		if (children.isEmpty() || fGridLayoutGridFigure == null)
			return bounds;
		int childIndex = children.indexOf(child);
		if (childIndex != -1) {
			Rectangle[] dims = helper.getChildrenDimensions();
			if (childIndex < dims.length)
				bounds = getGridLayoutGridFigure().getGridBroundsForCellBounds(dims[childIndex]);
			else
				bounds = new Rectangle();
		}
		return bounds;
	}
}

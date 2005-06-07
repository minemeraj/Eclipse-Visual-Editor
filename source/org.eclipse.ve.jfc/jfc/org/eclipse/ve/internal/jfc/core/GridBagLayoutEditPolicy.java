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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: GridBagLayoutEditPolicy.java,v $
 *  $Revision: 1.16 $  $Date: 2005-06-07 15:30:41 $ 
 */

import java.util.*;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy;
import org.eclipse.gef.requests.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionFilter;

import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.commands.NoOpCommand;
import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

public class GridBagLayoutEditPolicy extends ConstrainedLayoutEditPolicy implements IGridListener, IActionFilter {
	public static final String LAYOUT_ID = "java.awt.GridBagLayout"; //$NON-NLS-1$
	public static final String REQ_GRIDBAGLAYOUT_SPAN = "GridBagLayout span cells"; //$NON-NLS-1$
	protected GridBagLayoutGridFigure fGridBagLayoutGridFigure = null;
	protected GridBagLayoutFeedbackFigure fGridBagLayoutCellFigure = null;
	protected Cursor currentFeedbackCursor;
	protected IFigure fCursorFigure = null;
	protected Label xCursorLabel = null;
	protected Label yCursorLabel = null;
	protected CursorHelper fCursorHelper = null;
	protected RectangleFigure fColumnFigure = null, fRowFigure = null;
	protected GridBagLayoutPolicyHelper helper = new GridBagLayoutPolicyHelper();
	protected ContainerPolicy containerPolicy; // Handles the containment functions
	protected GridController gridController;
	protected boolean fShowgrid = false;
	protected EStructuralFeature sfGridX, sfGridY;
	protected EReference sfComponents, sfConstraintComponent, sfConstraintConstraint;
	protected GridBagImageListener fGridBagImageListener = null;
	protected boolean insertNewColumn = false, insertNewRow = false;
	protected ResourceSet rset;
	protected FigureListener hostFigureListener = new FigureListener() {
		public void figureMoved(IFigure source) {
				refreshGridFigure();
				helper.refresh();
		}
	};
	private EditPartListener editPartListener;


	private class GridBagImageListener implements IImageListener {
		public void imageChanged(ImageData data) {
				refreshGridFigure();
				helper.refresh();
		}
	}
	
	public GridBagLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		this.containerPolicy = (ContainerPolicy)containerPolicy;
		helper.setContainerPolicy(containerPolicy);
	}
	public void activate() {
		gridController = new GridController();
		if (gridController != null) {
			gridController.addGridListener(this);
			GridController.registerEditPart(getHost(), gridController);
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
		super.activate();
		containerPolicy.setContainer(getHost().getModel());
		rset = ((IJavaObjectInstance) getHost().getModel()).eResource().getResourceSet();
		sfConstraintConstraint = JavaInstantiation.getReference(rset, JFCConstants.SF_CONSTRAINT_CONSTRAINT);
		sfComponents = JavaInstantiation.getReference(rset, JFCConstants.SF_CONTAINER_COMPONENTS);
		sfConstraintComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_CONSTRAINT_COMPONENT);
		sfGridX = JavaInstantiation.getSFeature(rset, JFCConstants.SF_GRIDBAGCONSTRAINTS_GRIDX);
		sfGridY = JavaInstantiation.getSFeature(rset, JFCConstants.SF_GRIDBAGCONSTRAINTS_GRIDY);
		IImageNotifier imageNotifier = (IImageNotifier) BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) getHost().getModel());
		if (imageNotifier != null)
			imageNotifier.addImageListener(getGrigBagImageListener());
		getHostFigure().addFigureListener(hostFigureListener); // need to know when the host figure changes so we can refresh the grid
		CustomizeLayoutWindowAction.addComponentCustomizationPage(getHost().getViewer(), GridBagComponentPage.class);
	}
	
	public void deactivate() {
		containerPolicy.setContainer(null);
		GridController gridController = GridController.getGridController(getHost());
		eraseGridFigure();
		if (gridController != null) {
			gridController.removeGridListener(this);
			GridController.unregisterEditPart(getHost());
		}
		IImageNotifier imageNotifier = (IImageNotifier) BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance)getHost().getModel());
		if (imageNotifier != null)
			imageNotifier.removeImageListener(getGrigBagImageListener());
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
	protected Command getCreateCommand(CreateRequest request) {
		Object child = request.getNewObject();
		Point position = getLocationFromRequest(request).getCopy();
		// This point is absolute.  Make it relative to the model
		getHostFigure().translateToRelative(position);
		Point cellLocation = getGridBagLayoutGridFigure().getCellLocation(position.x, position.y);

		// Don't allow creation if the pointer is not near a column or row and the cell is not empty.
		if ( !(fGridBagLayoutGridFigure.isPointerNearAColumn(position.x)) && 
				!(fGridBagLayoutGridFigure.isPointerNearARow(position.y)) &&
				!helper.isCellEmpty(cellLocation.x, cellLocation.y) )
			return UnexecutableCommand.INSTANCE;
		// If near a column or row, get cell location including a possible hidden row and/or column
		boolean nearAColumn = false, nearARow = false;	
		if ( fGridBagLayoutGridFigure.isPointerNearAColumn(position.x) )
			nearAColumn = true;
		if ( fGridBagLayoutGridFigure.isPointerNearARow(position.y) )
			nearARow = true; 
		cellLocation = getGridBagLayoutGridFigure().getCellLocation(position.x, position.y, nearAColumn, nearARow);
		GridBagConstraint gridbagconstraint = helper.getConstraint((IJavaObjectInstance) child, cellLocation.x, cellLocation.y);
		if (gridbagconstraint != null) {
			CommandBuilder cb = new CommandBuilder();
			// get the command to apply the gridbagconstraint to the ComponentConstraint
			cb.append(helper.getCreateChildCommand(child, gridbagconstraint, null));
			// get the commands for other components that may have been affect by inserting a column and/or row
			cb.append(getAdjustConstraintsCommand(child, position, cellLocation));
			return cb.getCommand();
		}
		return UnexecutableCommand.INSTANCE;
	}
	/*
	 * Return the commands to adjust the gridx and/or gridy values of any components 
	 * affected if a column and/or row is inserted into the gridbaglayout.
	 */
	protected Command getAdjustConstraintsCommand(Object child, Point position, Point cellLocation) {
		insertNewColumn = false; 
		insertNewRow = false;
		if (fGridBagLayoutGridFigure.isPointerNearAColumn(position.x) && !fGridBagLayoutGridFigure.isColumnHidden(cellLocation.x))
			insertNewColumn = true;
		if (fGridBagLayoutGridFigure.isPointerNearARow(position.y) && !fGridBagLayoutGridFigure.isRowHidden(cellLocation.y))
			insertNewRow = true;
		if (insertNewColumn || insertNewRow) {
			return helper.adjustConstraintsCommand((IJavaObjectInstance)child, cellLocation, insertNewColumn, insertNewRow);
		}
		return null;
	}
	
	protected Command getDeleteDependantCommand(Request aRequest) {
		Command deleteContributionCmd = containerPolicy.getCommand(aRequest);
		if (deleteContributionCmd == null)
			return UnexecutableCommand.INSTANCE; // It can't be deleted

		// Note: If there is any annotation, that will be deleted too by the
		// container policy, and that will then also delete all of the view info.
		// So we don't need to handle viewinfo here.

		return deleteContributionCmd;
	}
	/**
	 * Helper that can get the location from heteregenous request types
	 */
	protected Point getLocationFromRequest(Request request) {
		if (request instanceof CreateRequest)
			return ((CreateRequest) request).getLocation();
		if (request instanceof ChangeBoundsRequest)
			return ((ChangeBoundsRequest) request).getLocation();
		return null;
	}
	protected Command getMoveChildrenCommand(Request request) {
		if (!(request instanceof ChangeBoundsRequest))
			return null;
		ChangeBoundsRequest req = (ChangeBoundsRequest) request;
		List editparts = req.getEditParts();
		// Only allow one object to be moved
		if (editparts.size() > 1)
			return null;
		EditPart ep = (EditPart) editparts.iterator().next();
		Point position = req.getLocation();
		// This point is absolute.  Make it relative to the model
		getHostFigure().translateToRelative(position);
		return getConstraintCommands(ep, position);
	}
	/*
	 * Return the commands containing the gridbag constraints needed to apply the gridx and gridy to the component.
	 * If the component came from an already existing gridbag layout, keep the GridBagConstraints settings
	 * and just apply the gridx and gridy values.
	 */
	protected Command getConstraintCommands(EditPart ep, Point position) {
		Point cellLocation = getGridBagLayoutGridFigure().getCellLocation(position.x, position.y);
		Point childPosition = ((GraphicalEditPart)ep).getContentPane().getBounds().getLocation();
		Point childCellLocation = getGridBagLayoutGridFigure().getCellLocation(childPosition.x, childPosition.y);
		IJavaObjectInstance component = (IJavaObjectInstance) ep.getModel();
		// If this component is added or moved from within this or another gridbag layout, use it's existing constraint so
		// we don't lose the original settings... we just want to set the gridx, gridy settings.
		EObject constraintComponent = InverseMaintenanceAdapter.getIntermediateReference(component, sfComponents, sfConstraintComponent, component);
		
		// Don't allow if cell is occupied and the pointer is not near a column or row.
		if ( !(fGridBagLayoutGridFigure.isPointerNearAColumn(position.x)) && 
				!(fGridBagLayoutGridFigure.isPointerNearARow(position.y)) &&
				(childCellLocation == cellLocation || !helper.isCellValidForMove(cellLocation, constraintComponent)))
			return UnexecutableCommand.INSTANCE;
		
		// If near a column or row, get the cell location including a possible hidden row and/or column
		boolean nearAColumn = false, nearARow = false;	
		if ( fGridBagLayoutGridFigure.isPointerNearAColumn(position.x) )
			nearAColumn = true;
		if ( fGridBagLayoutGridFigure.isPointerNearARow(position.y) )
			nearARow = true; 
		cellLocation = getGridBagLayoutGridFigure().getCellLocation(position.x, position.y, nearAColumn, nearARow);
		
		CommandBuilder cb = new CommandBuilder();
		if (constraintComponent != null) {
			JavaObjectInstance constraintObject = (JavaObjectInstance) constraintComponent.eGet(sfConstraintConstraint);
			if (constraintObject != null && constraintObject.getJavaType().getJavaName().equals("java.awt.GridBagConstraints")) { //$NON-NLS-1$
				// This is a GridBagConstraints object. Just change the gridx and gridy, then re-apply the constraint to the ConstraintComponent
				RuledCommandBuilder componentCB = new RuledCommandBuilder(EditDomain.getEditDomain(ep), null, false);
				Object intObject = BeanUtilities.createJavaObject("int", rset, String.valueOf(cellLocation.x)); //$NON-NLS-1$
				componentCB.applyAttributeSetting(constraintObject, sfGridX, intObject);
				intObject = BeanUtilities.createJavaObject("int", rset, String.valueOf(cellLocation.y)); //$NON-NLS-1$
				componentCB.applyAttributeSetting(constraintObject, sfGridY, intObject);
				componentCB.applyAttributeSetting(constraintComponent, sfConstraintConstraint, constraintObject);
				cb.append(componentCB.getCommand());
			}
		}
		if (cb.isEmpty()) {
			// No gridbag constraint found, create a new one with the specific gridx & gridy settings
			GridBagConstraint gridbagconstraint = helper.getConstraint(component, cellLocation.x, cellLocation.y);
			cb.append(helper.getCreateChildCommand(component, gridbagconstraint, null));
		}
		// get the commands for other components that may have been affect by inserting a column and/or row
		cb.append(getAdjustConstraintsCommand(component, position, cellLocation));
		return cb.getCommand();
	}
	/**
	 * getOrphanChildCommand: About to remove a child from the model
	 * so that it can be added someplace else.
	 *
	 * Remove the constraints since it may not be appropriate in 
	 * the new position. We need to use the Helper for this.
	 */
	protected Command getOrphanChildrenCommand(Request aRequest) {
		return helper.getOrphanChildrenCommand(ContainerPolicy.getChildren((GroupRequest) aRequest));
	}
	/**
	 * The child editpart is about to be added to the parent.
	 * The child is an existing child that was orphaned from a previous parent.
	 */
	protected Command createAddCommand(EditPart childEditPart, Object cellLocation) {
		// We need to create a constraint and send this and the child over to the container policy.
		Object child = childEditPart.getModel();
		GridBagConstraint gridbagconst = helper.getConstraint((IJavaObjectInstance) child, ((Point)cellLocation).x, ((Point)cellLocation).y);
		return helper.getAddChildrenCommand(Collections.singletonList(child), Collections.singletonList(gridbagconst), null);
	}
	protected Command createChangeConstraintCommand(EditPart childEditPart, Object constraint) {
		return helper.getChangeConstraintCommand(Collections.singletonList(childEditPart.getModel()), Collections.singletonList(constraint));
	}
	protected Object getConstraintFor(Point p1) {
		return null;
	}
	protected Object getConstraintFor(Rectangle p1) {
		return p1;
	}
	protected GridBagLayoutGridFigure getGridBagLayoutGridFigure() {
		if (fGridBagLayoutGridFigure == null) {
			IFigure f = ((GraphicalEditPart) getHost()).getContentPane();
			int [][] layoutDimensions = null;
			Point layoutOrigin = null;
			/*
			 * If the container is empty, we can't depend on the layout dimensions or layout origin
			 * from the GridBagLayout, just use the default values.
			 */
			if (helper.isContainerEmpty()) {
				layoutDimensions = new int [2][0];
				layoutDimensions[0] = new int[0];
				layoutDimensions[1] = new int[0];
				layoutOrigin = new Point();
			} else {
				layoutDimensions = helper.getContainerLayoutDimensions();
				layoutOrigin = helper.getContainerLayoutOrigin();
			}
			fGridBagLayoutGridFigure = new GridBagLayoutGridFigure(f.getBounds().getCopy(), layoutDimensions, layoutOrigin );
		}
		return fGridBagLayoutGridFigure;
	}
	protected void eraseGridFigure() {
		fShowgrid = false;
		if (fGridBagLayoutGridFigure != null) {
			removeFeedback(fGridBagLayoutGridFigure);
			fGridBagLayoutGridFigure = null;
		}
	}
	/**
	 * The model has changed. Remove and reconstruct the grid figure
	 * to reflect the changes.
	 */
	protected void refreshGridFigure() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if (fShowgrid) {
					eraseGridFigure();
					showGridFigure();
				} else {
					fGridBagLayoutGridFigure = null;
				}
			}
		});
	}
	protected void showGridFigure() {
		fShowgrid = true;
		addFeedback(getGridBagLayoutGridFigure());
	}
	public void eraseTargetFeedback(Request request) {
		if (!fShowgrid)
			if (fGridBagLayoutGridFigure != null && fGridBagLayoutGridFigure.getParent() != null) {
				removeFeedback(fGridBagLayoutGridFigure);
				fGridBagLayoutGridFigure = null;
			}
		if (xCursorLabel != null) {
			xCursorLabel = null;
		}
		if (yCursorLabel != null) {
			yCursorLabel = null;
		}
		if (fCursorFigure != null) {
			fCursorFigure = null;
		}
		if (fCursorHelper != null) {
			fCursorHelper.dispose();
			fCursorHelper = null;
		}
		if (fGridBagLayoutCellFigure != null) {
			removeFeedback(fGridBagLayoutCellFigure);
			fGridBagLayoutCellFigure = null;
		}
		if (fColumnFigure != null) {
			removeFeedback(fColumnFigure);
			fColumnFigure = null;
		}
		if (fRowFigure != null) {
			removeFeedback(fRowFigure);
			fRowFigure = null;
		}
		// Dispose the feedback cursor if we have one
		// We can't do this as it will have been set into the GEF composite canvas, so instead
		// we set the Cursor.class key to null and the CDECreationTool will pick this up and
		// dispose the cursor and revert it to a default cursor
		if ( currentFeedbackCursor != null && request instanceof CDERequest) {
			((CDERequest)request).put(Cursor.class,null);
		}
	}

	protected void showLayoutTargetFeedback(Request request) {
		Point position = getLocationFromRequest(request).getCopy();
		// This point is absolute.  Make it relative to the model
		getHostFigure().translateToRelative(position);
		Rectangle rect = null;
		Point cellLocation = getGridBagLayoutGridFigure().getCellLocation(position.x, position.y, true, true);
		if (!fShowgrid)
			addFeedback(getGridBagLayoutGridFigure());
		
		if (fGridBagLayoutGridFigure.isPointerNearARow(position.y)) {
			if (!fGridBagLayoutGridFigure.isRowHidden(cellLocation.y))
				hightlightRowHeaders(position);
			else
				resetHightlightedRowHeaders();
			showNewRowFeedBack(position);
		} else if (fRowFigure != null) {
			resetHightlightedRowHeaders();
			removeFeedback(fRowFigure);
			fRowFigure = null;
		}
		if (getGridBagLayoutGridFigure().isPointerNearAColumn(position.x)) {
			if (!getGridBagLayoutGridFigure().isColumnHidden(cellLocation.x))
				hightlightColumnHeaders(position);
			else
				resetHightlightedColumnHeaders();
			showNewColumnFeedBack(position);
		} else if (fColumnFigure != null) {
			resetHightlightedColumnHeaders();
			removeFeedback(fColumnFigure);
			fColumnFigure = null;
		}
		
		// if the highlighted column and row figures are displayed show the cell feedback
		if (fColumnFigure == null && fRowFigure == null) {
			if (fGridBagLayoutCellFigure == null) {
				GridBagLayoutFeedbackFigure gf = new GridBagLayoutFeedbackFigure();
				fGridBagLayoutCellFigure = gf;
			}
			rect = getGridBagLayoutGridFigure().getCellBounds(position.x, position.y);
			fGridBagLayoutCellFigure.setBounds(rect);
			addFeedback(fGridBagLayoutCellFigure);
		} else if (fGridBagLayoutCellFigure != null) {
			removeFeedback(fGridBagLayoutCellFigure);
			fGridBagLayoutCellFigure = null;
		}

		showCursorFeedback(position,request);

	}
	/**
	 * Show feedback next to the cursor to indicate the gridx and gridy values as the 
	 * mouse is moved over each cell in the grid bag.
	 */
	protected void showCursorFeedback(Point position,Request aRequest) {
		//	Get the cell location (gridx, gridy) based on whether the row and/or column figures
		// are showing (i.e. we're near a row or column). If near one, get the hidden row/column
		// so we can drop into it as well.
		boolean includeEmptyColumns, includeEmptyRows;
		if (fColumnFigure != null) includeEmptyColumns = true;
		else includeEmptyColumns = false;
		if (fRowFigure != null) includeEmptyRows = true;
		else includeEmptyRows = false;
		Point cellLocation = getGridBagLayoutGridFigure().getCellLocation(position.x, position.y, includeEmptyColumns, includeEmptyRows);
		getHostFigure().translateToAbsolute(position);
		org.eclipse.swt.graphics.Point absolutePosition = getHost().getViewer().getControl().toDisplay(position.x, position.y);
		absolutePosition.x += 8;
		absolutePosition.y += 19;
		/**
		 * The cursor feedback consists of a PopupHelper that contains a standard Figure with a FlowLayout.
		 * The Figure contains two Labels, one that contains the X value representing the gridx value,
		 * and the other Label containts the Y value representing the gridY value.
		 */
		
		// First create the X and Y Labels
		if (xCursorLabel == null) {
			xCursorLabel = new Label();
			xCursorLabel.setOpaque(true);
			xCursorLabel.setBorder(new MarginBorder(new Insets(0,2,0,0)));
		}
		xCursorLabel.setText(String.valueOf(cellLocation.x));
		if (fColumnFigure != null)
			xCursorLabel.setBackgroundColor(ColorConstants.yellow);
		else 
			xCursorLabel.setBackgroundColor(ColorConstants.cyan);
		
		if (yCursorLabel == null) {
			yCursorLabel = new Label();
			yCursorLabel.setOpaque(true);
			yCursorLabel.setBorder(new MarginBorder(new Insets(0,2,0,0)));
		}
		yCursorLabel.setText(String.valueOf(cellLocation.y));
		// Set the background color of each label based on whether or not the cursor
		// is close to a row or column. Highlight to yellow if close to either or both.
		if (fRowFigure != null)
			yCursorLabel.setBackgroundColor(ColorConstants.yellow);
		else 
			yCursorLabel.setBackgroundColor(ColorConstants.cyan);
		// Now create the container figure that contains the X,Y labels.
		if (fCursorFigure == null){
			fCursorFigure = new Figure();
			FlowLayout fl = new FlowLayout();
			fl.setMinorSpacing(1);
			fCursorFigure.setLayoutManager(fl);
			fCursorFigure.setBorder(new LineBorder());
			fCursorFigure.setOpaque(true);
			fCursorFigure.setBackgroundColor(ColorConstants.black);
		}
		fCursorFigure.add(xCursorLabel);
		fCursorFigure.add(yCursorLabel);
		// Now create the PopupHelper to contain the overall figure so that the cursor
		// feedback will paint over the top of other views if necessary.
		if (fCursorHelper == null) {
			fCursorHelper = new CursorHelper(getHost().getViewer().getControl());
		}
		fCursorHelper.showCursorFigure(fCursorFigure, absolutePosition.x, absolutePosition.y);
		
		// Update the cursor with the default cursor if the create request allows it
		if ( aRequest instanceof CDERequest && ((CDERequest) aRequest).get(Cursor.class) == null ) {
			// Need to use the basic Arrow cursor so we can show the row/column number underneath it
			// If we already have a cursor, don't create another.
			Cursor defaultCursor = new Cursor(getHost().getViewer().getControl().getDisplay(), SWT.CURSOR_ARROW);
			((CDERequest)aRequest).put(Cursor.class,defaultCursor);
			currentFeedbackCursor = defaultCursor;
		}
		
	}
	protected void hightlightColumnHeaders(Point position) {
		Point cellLocation = fGridBagLayoutGridFigure.getCellLocation(position.x, position.y);
		fGridBagLayoutGridFigure.highlightColumnHeadersFromColumn(cellLocation.x);
		addFeedback(fGridBagLayoutGridFigure);
	}
	protected void resetHightlightedColumnHeaders() {
		fGridBagLayoutGridFigure.resetHighlightedColumnHeaders();
		addFeedback(fGridBagLayoutGridFigure);
	}
	/**
	 * Show a new yellow column inserted into the grid near the column closest to position
	 */
	protected void showNewColumnFeedBack(Point position) {
		if (fColumnFigure == null)
			fColumnFigure = new RectangleFigure();
		Point colStart = fGridBagLayoutGridFigure.getColumnStartPosition(position.x);
		Point colEnd = fGridBagLayoutGridFigure.getColumnEndPosition(position.x);
		fColumnFigure.setBounds(new Rectangle(colStart.x-2, colStart.y, 10, colEnd.y - colStart.y));
		fColumnFigure.setBackgroundColor(ColorConstants.yellow);
		addFeedback(fColumnFigure);
	}
	protected void hightlightRowHeaders(Point position) {
		Point cellLocation = fGridBagLayoutGridFigure.getCellLocation(position.x, position.y);
		fGridBagLayoutGridFigure.highlightRowHeadersFromRow(cellLocation.y);
		addFeedback(fGridBagLayoutGridFigure);
	}
	protected void resetHightlightedRowHeaders() {
		fGridBagLayoutGridFigure.resetHighlightedRowHeaders();
		addFeedback(fGridBagLayoutGridFigure);
	}
	/**
	 * Show a new yellow row inserted into the grid near the row closest to position
	 */
	protected void showNewRowFeedBack(Point position) {
		if (fRowFigure == null)
			fRowFigure = new RectangleFigure();
		Point rowStart = fGridBagLayoutGridFigure.getRowStartPosition(position.y);
		Point rowEnd = fGridBagLayoutGridFigure.getRowEndPosition(position.y);
		fRowFigure.setBounds(new Rectangle(rowStart.x, rowStart.y-2, rowEnd.x - rowStart.x, 10));
		fRowFigure.setBackgroundColor(ColorConstants.yellow);
		addFeedback(fRowFigure);
	}
	/**
	 * @see org.eclipse.ui.IActionFilter#testAttribute(Object, String, String)
	 * 
	 * Return true for show grid action if grid is hidden
	 * Return true for hide grid action if grid is showing
	 * Return true if the layoutpolicy is GridBagLayout
	 * otherwise return false
	 */
	public boolean testAttribute(Object target, String name, String value) {
		if (name.startsWith("showgrid") && value.equals("false") && !fShowgrid) //$NON-NLS-1$ //$NON-NLS-2$
			return true;
		else if (name.startsWith("showgrid") && value.equals("true") && fShowgrid) //$NON-NLS-1$ //$NON-NLS-2$
			return true;
		else if (name.startsWith(CustomizeLayoutPage.LAYOUT_POLICY_KEY) && value.equals(LAYOUT_ID)) //$NON-NLS-1$ //$NON-NLS-2$
			return true;

		return false;
	}
	/**
	 * @see org.eclipse.ve.internal.cde.core.IGridListener#gridHeightChanged(int, int)
	 */
	public void gridHeightChanged(int gridHeight, int oldGridHeight) {
	}

	/**
	 * @see org.eclipse.ve.internal.cde.core.IGridListener#gridMarginChanged(int, int)
	 */
	public void gridMarginChanged(int gridMargin, int oldGridMargin) {
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

	/**
	 * @see org.eclipse.ve.internal.cde.core.IGridListener#gridWidthChanged(int, int)
	 */
	public void gridWidthChanged(int gridWidth, int oldGridWidth) {
	}

	protected IImageListener getGrigBagImageListener() {
		if (fGridBagImageListener == null)
			fGridBagImageListener = new GridBagImageListener();
		return fGridBagImageListener;
	}
	/**
	 * Factors out REQ_GRIDBAGLAYOUT_SPAN requests, otherwise calls <code>super</code>.
	 * @see org.eclipse.gef.EditPolicy#getCommand(Request)
	 */
	public Command getCommand(Request request) {
		if (REQ_GRIDBAGLAYOUT_SPAN.equals(request.getType()))
			return getSpanChildrenCommand(request);

		return super.getCommand(request);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getAddCommand(org.eclipse.gef.Request)
	 * The editparts are about to be added to the parent.
	 * Each child (editpart) is an existing child that was orphaned from a previous parent.
	 * Since we are adding to a GridBagLayout we can't handle adding more than one child
	 * because we can only drop into one cell at a time.
	 */
	protected Command getAddCommand(Request generic) {
		ChangeBoundsRequest request = (ChangeBoundsRequest)generic;
		List editParts = request.getEditParts();
		if (editParts.isEmpty() || editParts.size() > 1)
			return UnexecutableCommand.INSTANCE;
		Point position = getLocationFromRequest(request).getCopy();
		// This point is absolute.  Make it relative to the model
		getHostFigure().translateToRelative(position);
		Point cellLocation = getGridBagLayoutGridFigure().getCellLocation(position.x, position.y);
		// Don't allow if cell is occupied and the pointer is not near a column or row.
		if ( !(fGridBagLayoutGridFigure.isPointerNearAColumn(position.x)) && 
				!(fGridBagLayoutGridFigure.isPointerNearARow(position.y)) &&
				!helper.isCellEmpty(cellLocation.x, cellLocation.y) )
			return UnexecutableCommand.INSTANCE;

		// If near a column or row, get the cell location including a possible hidden row and/or column
		boolean nearAColumn = false, nearARow = false;	
		if ( fGridBagLayoutGridFigure.isPointerNearAColumn(position.x) )
			nearAColumn = true;
		if ( fGridBagLayoutGridFigure.isPointerNearARow(position.y) )
			nearARow = true; 
		cellLocation = getGridBagLayoutGridFigure().getCellLocation(position.x, position.y, nearAColumn, nearARow);
		
		CommandBuilder cb = new CommandBuilder();
		cb.append(createAddCommand((EditPart)editParts.get(0), cellLocation));
		// get the commands for other components that may have been affect by inserting a column and/or row
		cb.append(getAdjustConstraintsCommand(((EditPart)editParts.get(0)).getModel(), position, cellLocation));
		return cb.getCommand();
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
		Point spanToCellLocation = getGridBagLayoutGridFigure().getCellLocation(spanToPosition.x, spanToPosition.y);
		// Get the cell location where we started the drag operation
		Dimension dim = request.getSizeDelta();
		int handleSizeOffset = GridBagSpanHandle.HANDLE_SIZE/2;
		Point startPosition = new Point(spanToPosition.x - dim.width - handleSizeOffset, spanToPosition.y - dim.height - handleSizeOffset);
		// Get the cell location of the child component
		GraphicalEditPart ep = (GraphicalEditPart)editParts.get(0);
		Point childPosition = ep.getContentPane().getBounds().getLocation();
		Point childCellLocation = getGridBagLayoutGridFigure().getCellLocation(childPosition.x, childPosition.y);
		Point startCellLocation = getGridBagLayoutGridFigure().getCellLocation(startPosition.x, startPosition.y);
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

	/*
	 * Show target feedback when dragging the span handles of a component.
	 * Highlight the cells the component will occupy based on the begining cell position
	 * and end cell position of the pointer.
	 */
	public void showSpanTargetFeedback(ChangeBoundsRequest request) {
//		If the grid is not on, turn it on
		if (!fShowgrid)
			addFeedback(getGridBagLayoutGridFigure());

		Point spanToPosition = request.getLocation().getCopy();
		// This point is absolute.  Make it relative to the model
		getHostFigure().translateToRelative(spanToPosition);

		// Get the cell location of the child component
		GraphicalEditPart ep = (GraphicalEditPart)request.getEditParts().get(0);
		Point childPosition = ep.getContentPane().getBounds().getLocation();
		// Get the start and end cell bounds in order to determine the entire bounds of the cell feedback figure.
		Rectangle startCellBounds = getGridBagLayoutGridFigure().getCellBounds(childPosition.x, childPosition.y);
		Rectangle endCellBounds = getGridBagLayoutGridFigure().getCellBounds(spanToPosition.x, spanToPosition.y);
		Rectangle spanrect = new Rectangle(startCellBounds.x, 
									startCellBounds.y, 
									endCellBounds.x + endCellBounds.width - startCellBounds.x,
									endCellBounds.y + endCellBounds.height - startCellBounds.y);
		if (fGridBagLayoutCellFigure == null) {
			fGridBagLayoutCellFigure = new GridBagLayoutFeedbackFigure();
		}
		fGridBagLayoutCellFigure.setBounds(spanrect);
		addFeedback(fGridBagLayoutCellFigure);
		// Cursor feedback (cell locations) as we move the pointer
		showCursorFeedback(spanToPosition, request);
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
					// Hide the grid just in case we were showing before and changed the prefs
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

}

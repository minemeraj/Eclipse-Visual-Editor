package org.eclipse.ve.internal.cde.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: XYLayoutEditPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2004-05-11 18:36:23 $ 
 */



import java.util.*;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.ui.IActionFilter;

import org.eclipse.ve.internal.cde.commands.NoOpCommand;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;
import org.eclipse.ve.internal.cdm.Annotation;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

/**
 * XYLayoutInputPolicy for the VCE.
 *
 * This handles the Alignment/Distribute commands.
 */
public abstract class XYLayoutEditPolicy extends org.eclipse.gef.editpolicies.XYLayoutEditPolicy implements IZoomListener, IGridListener, IActionFilter {
	
	public final static String LAYOUT_ID = "org.eclipse.ve.internal.cde.core.XYLayout"; //$NON-NLS-1$
	
	private ZoomController zoomController;
	private GridController gridController;
	private GridFigure gridFigure;
	private IFigure targetFeedback;
	protected XYLayoutGridConstrainer layoutConstrainer;
	ArrayList feedbackList;
	
	protected boolean allowZooming = false, allowGridding = true;	// Whether zooming and gridding are allowed.
	
	
/**
 * creates the EditPartListener for observing when children are added to the host.
 * Need to override this to force the grid figure to the beginning when a child is added.
 * @return EditPartListener
 */
protected EditPartListener createListener() {
	return new EditPartListener.Stub() {
		public void childAdded(EditPart child, int index) {
			decorateChild(child);
			if (gridController != null && gridFigure != null && gridController.isGridShowing()) {
				getHostFigure().remove(gridFigure);
				getHostFigure().add(gridFigure, 0);
			}
		}
	};
}
/**
 * If gridding is not wanted, then set this to false.
 * The default is true.
 */
public void setGriddable(boolean allowGridding) {
	this.allowGridding = allowGridding;
}

/*
 * Called to convert from model to figure constraint.
 */
protected abstract Object modelToFigureConstraint(Object figureConstraint);

/**
 * If zooming is wanted, then set this to true.
 * The default is false;
 */
public void setZoomable(boolean allowZooming) {
	this.allowZooming = allowZooming;
}	

public void activate() {
	super.activate();
	if (allowZooming) {
		zoomController = ZoomController.getZoomController(getHost());
		if (zoomController != null) {
			zoomController.addZoomListener(this);
			zoomChanged(zoomController.getZoomValue(), 0);
		}
	}
	
	if (allowGridding) {
		gridController = new GridController();
		if (gridController != null) {
			gridFigure = createGridFigure();
			gridFigure.setVisible(gridController.isGridShowing());
			IFigure fig = ((GraphicalEditPart) getHost()).getContentPane();
			fig.add(gridFigure, 0);	// grid needs to be first so it doesn't overlay the children
			gridController.addGridListener(this);
			GridController.registerEditPart(getHost(), gridController);
			initializeGrid();
		}
	}
	
	CustomizeLayoutWindowAction.addComponentCustomizationPage(getHost().getViewer(), AlignmentXYComponentPage.class);	
}
/*
 * Get grid state data from the edit domain to determine whether to turn on/off the grid.
 * The state is set each the time the grid is turned on or off. We need this in order to 
 * reshow the grid in case a reload from scratch occurs.
 * The data is a HashSet with the annotation name as the key
 */	
protected void initializeGrid() {
	EditDomain domain = EditDomain.getEditDomain(getHost());
	HashSet gridStateData = (HashSet) domain.getData(GridController.GRID_STATE_KEY);
	if (gridStateData != null) {
		AnnotationLinkagePolicy policy = domain.getAnnotationLinkagePolicy();
		Annotation ann = policy.getAnnotation(getHost().getModel());
		if (ann != null) {
			String name = (String) ann.getKeyedValues().get(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
			if (name == null)
				name = GridController.GRID_THIS_PART;
			if (gridStateData.contains(name))
				if (gridController != null)
					gridController.setGridShowing(true);
		}
	}
}

public void deactivate() {
	if (zoomController != null) {
		zoomController.removeZoomListener(this);
		zoomController = null;
	}
	if (gridController != null) {
		gridController.removeGridListener(this);
		GridController.unregisterEditPart(getHost());
		gridController = null;
		IFigure fig = ((GraphicalEditPart) getHost()).getContentPane();
		fig.remove(gridFigure);
		gridFigure = null;		
	}	
	undecorateChildren();
	super.deactivate();
}

/**
 * Zoom has changed need to reposition all of the children.
 */
public void zoomChanged(int newValue, int oldValue) {
	Iterator iter = getHost().getChildren().iterator();
	while (iter.hasNext()) {
		EditPart child = (EditPart) iter.next();
		Object modelConstraint = getChildConstraint(child);
		Rectangle figureConstraint = (Rectangle) modelToFigureConstraint(modelConstraint);
		setConstraintToFigure(child, figureConstraint);
	}
	if (gridController != null)
		gridFigure.repaint();	// Also repaint the entire grid	
}

/**
 * The grid width has changed.
 */
public void gridWidthChanged(int newWidth, int oldWidth) {
	if (gridController.isGridShowing())
		gridFigure.repaint();	// Tell the figure to repaint.
}

/**
 * The grid height has changed.
 */
public void gridHeightChanged(int newHeight, int oldHeight) {
	if (gridController.isGridShowing())
		gridFigure.repaint();	// Tell the figure to repaint.
}

/**
 * The grid margin has changed.
 */
public void gridMarginChanged(int newMargin, int oldMargin) {
	if (gridController.isGridShowing())
		gridFigure.repaint();	// Tell the figure to repaint.
}

/**
 * Set the grid decoration on the edit part to be a fixed grid
 */
public void gridVisibilityChanged(boolean showGrid) {
	gridFigure.setVisible(showGrid);
	getHostFigure().remove(gridFigure);
	getHostFigure().add(gridFigure, 0);
	if (showGrid) {
		// Set out layout constrainer and also decorate the figure.
		layoutConstrainer = createGridConstrainer();
	} else {
		layoutConstrainer = null;
	}
}
public Command getCommand(Request request){
	if (RequestConstantsCDE.REQ_DISTRIBUTE_CHILD.equals(request.getType()))
		return getDistributeChildCommand(request);
	if (RequestConstantsCDE.REQ_ALIGNMENT_CHILD.equals(request.getType()))
		return getAlignmentChildCommand(request);
	return super.getCommand(request);
}

/**
 * subclasses should return whether this child editpart is resizeable or not.
 */
protected abstract boolean isChildResizeable(EditPart aChild);

/**
 * subclasses should return the model constraint for this child.
 */
protected abstract Object getChildConstraint(EditPart child);

/**
 * subclasses should set the figure with this constraint.
 */
protected abstract void setConstraintToFigure(EditPart child, Rectangle figureConstraint);

/**
 * The child's constraint is being changed. This shouldn't be called anymore. All now go through
 * getResizeChildrenCommand and test and set the ismove/isresize appropriately.
 */
protected Command createChangeConstraintCommand(EditPart childEditPart, Object constraint) {
	return createChangeConstraintCommand(childEditPart, constraint, false, true);
}
/**
 * Change the constraint, amove indicates the location is changing, a resize indicates that the size is changing.
 */
protected abstract Command createChangeConstraintCommand(EditPart child, Object constraint, boolean move, boolean resize);


/**
 * We actually want to know the difference between a move and a resize.
 */
protected Command getResizeChildrenCommand(ChangeBoundsRequest req) {
	CompoundCommand changeConstraintCmd = new CompoundCommand();
	Command c;
	GraphicalEditPart child;
	List children = req.getEditParts();

	boolean move = req.getMoveDelta().x != 0 || req.getMoveDelta().y != 0;
	boolean resize = req.getSizeDelta().width != 0 || req.getSizeDelta().height != 0;
	for (int i = 0; i < children.size(); i++) {
		child = (GraphicalEditPart) children.get(i);
		// defect 201856 - don't allow constraints with negative width's or height's
		Rectangle constraint = (Rectangle)getConstraintFor(req, child);
		if (resize && (constraint.width < 0 || constraint.height < 0))
			continue;
		c = createChangeConstraintCommand(child, translateToModelConstraint(constraint), move, resize);
		changeConstraintCmd.append(c);
	}
	return changeConstraintCmd.isEmpty() ? null : changeConstraintCmd.unwrap();
	// It is ok for there to be no move/resize commands (i.e. it didn't really change position).
}
/**
 * Create the Primary Drag Role Edit Policy. This handles only
 * the selection handles and drag feedback.
 */
protected EditPolicy createChildEditPolicy(final EditPart aPart){
	// Create the Primary Drag Role editpolicy - We will just wrapper the appropriate
	// so that we can turn off certain things when grid is on. Also it will
	// send alignment/distribute requests to here as align/distribute child requests.
	EditPolicy dragRolePolicy = isChildResizeable(aPart) ?
		new org.eclipse.gef.editpolicies.ResizableEditPolicy() :
		new org.eclipse.gef.editpolicies.NonResizableEditPolicy();
	return new PrimaryDragRoleEditPolicy(gridController, dragRolePolicy, true);
}

/**
 * Called when the context menu is about to open.
 * Override to add your own context dependent menu 
 * contributions to a child view object.
 */
//public ContextMenuManager contributeToChildContextMenu(ContextMenuManager menu, IViewer viewer, IViewObject child) {
//	super.contributeToChildContextMenu(menu, viewer, child);
//	
//	IEditor myEditor = ( ( IGEFViewer ) viewer ).getEditor();
//	AbstractMenuManager alignMenu = menu.findMenuUsingPath("Alignment");
//		
//	alignMenu.addSeparator("distributeSep");
//			
//	DistributeAction distAction = new DistributeAction(VCENls.RESBUNDLE, myEditor, DistributeAction.HORIZONTAL_SURFACE);
//	alignMenu.add(new ActionContributionItem("distributeHoriz", distAction));
//	
//	distAction = new DistributeAction(VCENls.RESBUNDLE, myEditor, DistributeAction.VERTICAL_SURFACE);
//	alignMenu.add(new ActionContributionItem("distributeVert", distAction));
//	
//	return menu;
//
//}

//protected ToolBarManager addAlignmentToolBarItems(ToolBarManager tbm, IGEFViewer viewer) {
//
//	super.addAlignmentToolBarItems(tbm, viewer);
//	
//	IEditor editor = viewer.getEditor();
//	IGraphViewer gViewer = (IGraphViewer)viewer;
//	
//	if (tbm.find("distribute") == null) {
//		// Want this separator right after the last entry in the match group,
//		// so we will append to match group, and this will create a new one for us.
//		tbm.appendToGroup("match", new Separator("distribute"));
//		gViewer.addDynamicToolBarItem("distribute");
//	}
//
//	if (tbm.find("distributeHorzAction") == null) {
//		DistributeAction action = new DistributeAction(VCENls.RESBUNDLE, editor, DistributeAction.HORIZONTAL_SURFACE);		
//		tbm.appendToGroup("distribute", "distributeHorzAction", action);
//		gViewer.addDynamicToolBarItem("distributeHorzAction");
//	}
//
//	if (tbm.find("distributeVertAction") == null) {
//		DistributeAction action = new DistributeAction(VCENls.RESBUNDLE, editor, DistributeAction.VERTICAL_SURFACE);		
//		tbm.appendToGroup("distribute", "distributeVertAction", action);
//		gViewer.addDynamicToolBarItem("distributeVertAction");
//	}
//	
//	return tbm;
//}
	
protected Command getAlignmentChildCommand(Request request) {
	AlignmentCommandRequest alignReq = ((AlignmentChildCommandRequest) request).getAlignmentRequest();
	Point clientAreaOffset = getHostFigure().getClientArea().getLocation().negate();
	Rectangle anchorRect = new Rectangle(((GraphicalEditPart)alignReq.getAnchorObject()).getFigure().getBounds());
	anchorRect.translate(clientAreaOffset);
	anchorRect = (Rectangle)getConstraintFor(anchorRect);
	EditPart child = ((AlignmentChildCommandRequest)request).getChildEditPart();
	Rectangle currRect = new Rectangle(((GraphicalEditPart) child).getFigure().getBounds());
	currRect.translate(clientAreaOffset);
	currRect = (Rectangle)getConstraintFor(currRect);
	Rectangle newRect = getNewPosition(alignReq.getAlignType(), anchorRect, currRect);
	if (isAlignmentResize(alignReq.getAlignType()) && !isChildResizeable(child))
		return UnexecutableCommand.INSTANCE;	// Can't be resized, so don't allow it.
	Command cmd = createChangeConstraintCommand(child,translateToModelConstraint(newRect), isAlignmentMove(alignReq.getAlignType()), isAlignmentResize(alignReq.getAlignType()));
	return cmd != null ? cmd : NoOpCommand.INSTANCE;	// If no changes (i.e. null returned) we still must return something to indicate we processed the request.
}

protected boolean isAlignmentResize(int alignType) {
	return alignType == AlignmentCommandRequest.MATCH_WIDTH || alignType == AlignmentCommandRequest.MATCH_HEIGHT;
}

protected boolean isAlignmentMove(int alignType) {
	return !isAlignmentResize(alignType);	// Turns out that they are exclusive of each other.
}

protected Rectangle getNewPosition(int alignType, Rectangle anchorRect, Rectangle currRect) {
	int delta;
	switch (alignType) {
		case (AlignmentCommandRequest.LEFT_ALIGN) :
			{
				return (new Rectangle(anchorRect.x, currRect.y, currRect.width, currRect.height));
			}
		case (AlignmentCommandRequest.CENTER_ALIGN) :
			{
				delta = (anchorRect.x + anchorRect.width / 2) - (currRect.x + currRect.width / 2);
				return (new Rectangle(currRect.x + delta, currRect.y, currRect.width, currRect.height));
			}
		case (AlignmentCommandRequest.RIGHT_ALIGN) :
			{
				delta = (anchorRect.x + anchorRect.width) - (currRect.x + currRect.width);
				return (new Rectangle(currRect.x + delta, currRect.y, currRect.width, currRect.height));
			}
		case (AlignmentCommandRequest.TOP_ALIGN) :
			{
				return (new Rectangle(currRect.x, anchorRect.y, currRect.width, currRect.height));
			}
		case (AlignmentCommandRequest.MIDDLE_ALIGN) :
			{
				delta = (anchorRect.y + anchorRect.height / 2) - (currRect.y + currRect.height / 2);
				return (new Rectangle(currRect.x, currRect.y + delta, currRect.width, currRect.height));
			}
		case (AlignmentCommandRequest.BOTTOM_ALIGN) :
			{
				delta = (anchorRect.y + anchorRect.height) - (currRect.y + currRect.height);
				return (new Rectangle(currRect.x, currRect.y + delta, currRect.width, currRect.height));
			}
		case (AlignmentCommandRequest.MATCH_WIDTH) :
			{
				return (new Rectangle(currRect.x, currRect.y, anchorRect.width, currRect.height));
			}
		case (AlignmentCommandRequest.MATCH_HEIGHT) :
			{
				return (new Rectangle(currRect.x, currRect.y, currRect.width, anchorRect.height));
			}
		default :
			{ // default to left align
				return (new Rectangle(anchorRect.x, currRect.y, currRect.width, currRect.height));
			}
	}
}

protected Command getDistributeChildCommand(Request request) {
	DistributeCommandRequest distReq = ((DistributeChildCommandRequest) request).getDistributeRequest();
	EditPart child = ((DistributeChildCommandRequest) request).getChildEditPart();
	// new position was calculated in DistributeAction.createDistributeCommand()
	Rectangle newRect = distReq.getBounds();
	newRect.translate(getHostFigure().getClientArea().getLocation().negate());
	Rectangle relativeRect = (Rectangle)getConstraintFor(newRect);
	newRect.x = relativeRect.x;
	newRect.y = relativeRect.y;
	Command cmd = createChangeConstraintCommand(child,translateToModelConstraint(newRect), true, false);
	return cmd != null ? cmd : NoOpCommand.INSTANCE;	// If no changes (i.e. null returned) we still must return something to indicate we processed the request.	
}
/*
 * Get the constraint from the super class and adjust it using the zoom factor.
 */
public Object getConstraintFor(Point p) {
	Rectangle rect = (Rectangle) super.getConstraintFor(p);
	if (zoomController != null) {
		// the coordinate is from the canvas, so it is zoomed, need to unzoom it for a constraint.
		rect.setLocation(zoomController.unzoomCoordinate(rect.x), zoomController.unzoomCoordinate(rect.y));
	}
	// Let the layout constrainer adjust it
	if ( layoutConstrainer != null ) {
		rect = layoutConstrainer.adjustConstraintFor(rect);
	}
	return rect;
}
/*
 * Get the constraint from the super class, and adjust it's position 
 * relative to it's host position, and removing the zoom factor from
 * and it's position.
 */
public Object getConstraintFor(Rectangle r) {
	Rectangle rect = (Rectangle)super.getConstraintFor(r);
	if (zoomController != null) {
		// the coordinate is from the canvas, so it is zoomed, need to unzoom it for a constraint.
		rect.setLocation(zoomController.unzoomCoordinate(rect.x), zoomController.unzoomCoordinate(rect.y));
	}
	// Let the layout constrainer adjust it
	if ( layoutConstrainer != null ) {
		rect = layoutConstrainer.adjustConstraintFor(rect);
	}
	return rect;
}

/*
 * Create a figure used for target feedback when dragging.
 * Use the rectangle parameter as it's bounds.
 */
protected IFigure createDragTargetFeedbackFigure(Rectangle rect) {
	// Use a ghost rectangle for feedback
	RectangleFigure r = new RectangleFigure();
	FigureUtilities.makeGhostShape(r);
	r.setLineStyle(Graphics.LINE_DASHDOT);
	r.setForegroundColor(ColorConstants.white);
	r.setBounds(rect);
	return r;
}
/* Remove the overall figure feedback and each target figure feedback.
 */
protected void eraseLayoutTargetFeedback(Request request) {
	super.eraseLayoutTargetFeedback(request);
	if (feedbackList != null) {
		for (int i=0; i < feedbackList.size(); i++) {
			removeFeedback((IFigure)feedbackList.get(i));
		}
		feedbackList = null;
	}
	if (targetFeedback != null) {
		removeFeedback(targetFeedback);
		targetFeedback = null;
	}
}
/*
 * Provide feedback on the overall figure and the request figure.
 * If there is a layout constrainer, let it handle the feedback.
 */
protected IFigure getRectangleFeedback(Request request) {
	if (REQ_ADD.equals(request.getType()) ||
		REQ_MOVE.equals(request.getType()) ||
		REQ_RESIZE.equals(request.getType()) )
	{
		getRectangleFeedback((ChangeBoundsRequest)request);
	}
	
	// provide feedback on the overall target figure
	if (targetFeedback == null){
		RectangleFigure rf;
		targetFeedback = rf = new RectangleFigure();
		rf.setFill(false);

		Rectangle rect = new Rectangle(getHostFigure().getBounds());
		rf.setBounds(rect.shrink(5,5));
		addFeedback(targetFeedback);
	}
	return targetFeedback;	
}

/*
 * Handle when the figure is moved or resized.
 * Provide feedback on the overall figure and the request figure.
 * If there is a layout constrainer, let it handle the feedback.
 */
protected void getRectangleFeedback(ChangeBoundsRequest request) {
	if (layoutConstrainer != null) {
		GraphicalEditPart child;
		Rectangle rect;
		IFigure targetFigure;
		List children = request.getEditParts();
		Point inset = getHostFigure().getClientArea().getLocation();
		if (feedbackList == null)
			feedbackList = new ArrayList(children.size());
		// For each figure in the ChangeBoundsRequest, create a target
		// feedback based on the constraint.
		for (int i=0; i < children.size(); i++) {
			child = (GraphicalEditPart)children.get(i);
			rect = (Rectangle)getConstraintFor(request, child);
			if (zoomController != null) {
				// Need to zoom it back up
				rect.setLocation(zoomController.zoomCoordinate(rect.x), zoomController.zoomCoordinate(rect.y));
			}
			// Retain original size, not size from request.
			Rectangle childRect = child.getFigure().getBounds();			
			rect.width = childRect.width;
			rect.height = childRect.height;
			rect.translate(inset);	// Add the insets in. any set bounds require this.
			// If this is the first time through, the feedback figure has to be created.
			if ( i > feedbackList.size()-1 ) {
				targetFigure = createDragTargetFeedbackFigure(rect);
				addFeedback(targetFigure);
				feedbackList.add(targetFigure);
			} else {
				// the figure already exists. just update its position.
				((IFigure)feedbackList.get(i)).setBounds(rect);
			}
		}
	}
}
/**
 * Returns a draw2d constraint object for the given request.
 * The returned object can be translated to the model using
 * translateToModelConstraint(Object)
 * @see translateToModelConstraint(Object)
 */
protected Object getConstraintFor (ChangeBoundsRequest request, GraphicalEditPart child){
	Rectangle rect = (Rectangle) super.getConstraintFor(request, child);
//	Rectangle rect = child.getFigure().getBounds().getTranslated(getHostFigure().getClientArea().getLocation().negate()); // Remove the inset
	// Call our own getTransformedRectangle(...) to adjust it according to the zoom factor.
//	getTransformedRectangle(rect, request);
	if ( layoutConstrainer != null ) {
		// Let the layout constrainer adjust it
		rect = layoutConstrainer.adjustConstraintFor(rect);
	}
	return rect;
}

protected ZoomController getZoomController() {
	return zoomController;
}

protected GridController getGridController() {
	return gridController;
}

protected GridFigure getGridFigure() {
	return gridFigure;
}

protected GridFigure createGridFigure() {
	return new GridFigure(gridController, zoomController);
}

protected XYLayoutGridConstrainer createGridConstrainer() {
	return 	new XYLayoutGridConstrainer(gridFigure);
}

protected void showLayoutTargetFeedback(Request request) {
	getRectangleFeedback(request);
}

protected void showSizeOnDropFeedback(CreateRequest request) {	
	super.showSizeOnDropFeedback(request);
	// TODO Need to get this snapping to the grid
}

/**
 * @see org.eclipse.ui.IActionFilter#testAttribute(Object, String, String)
 * 
 * Return true for show grid action if grid is hidden
 * Return true for hide grid action if grid is showing
 * otherwise return false
 */
public boolean testAttribute(Object target, String name, String value) {
	if (name.startsWith("showgrid") &&  //$NON-NLS-1$
			value.equals("false") && //$NON-NLS-1$
			!gridController.isGridShowing() )
		return true;
	else if (name.startsWith("showgrid") && //$NON-NLS-1$
			value.equals("true") && //$NON-NLS-1$
			gridController.isGridShowing() )
		return true;
	else if (name.startsWith("LAYOUTPOLICY") && value.equals(LAYOUT_ID)) //$NON-NLS-1$
		return true;
		
	return false;
}
protected void undecorateChildren(){
	Iterator children = getHost().getChildren().iterator();
	while (children.hasNext())
		undecorateChild((EditPart) children.next());
}
protected void undecorateChild(EditPart child){
	child.removeEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#decorateChild(org.eclipse.gef.EditPart)
	 */
	protected void decorateChild(EditPart child) {		
		super.decorateChild(child);
	}

}

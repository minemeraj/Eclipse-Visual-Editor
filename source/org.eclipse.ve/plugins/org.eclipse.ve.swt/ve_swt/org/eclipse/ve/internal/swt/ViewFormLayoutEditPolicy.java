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
 *  $RCSfile: ViewFormLayoutEditPolicy.java,v $
 *  $Revision: 1.4 $  $Date: 2005-10-11 21:23:47 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.SWT;

import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.commands.NoOpCommand;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
 
/**
 * The LayoutEditPolicy for a ViewForm will use the helper class to add feedback to regions
 * based on what is currently being moused over and the availability of the region. 
 * 
 * @since 1.1
 */
public class ViewFormLayoutEditPolicy extends LayoutEditPolicy{
	
	protected VisualContainerPolicy fPolicy;
	protected ViewFormLayoutPolicyHelper fLayoutPolicyHelper;
	protected ViewFormLayoutFeedback fViewFormLayoutFeedback = null;
	protected CustomLayoutRegionFeedback fRegionFeedback = null;
	protected Rectangle fCurrentRectangle = null;

	public ViewFormLayoutEditPolicy(EditDomain anEditDomain) {
		fPolicy = new ViewFormContainerPolicy(anEditDomain);
		fLayoutPolicyHelper = new ViewFormLayoutPolicyHelper();
		fLayoutPolicyHelper.setContainerPolicy(fPolicy);
	}
	
	public void activate() {
		super.activate();
		fPolicy.setContainer(getHost().getModel());
	}
	
	public void deactivate() {
		super.deactivate();
		fPolicy.setContainer(null);
	}
	
	public EditPolicy createChildEditPolicy(EditPart aChild) {
		return new NonResizableEditPolicy();
	}
	
	/**
	 * Helper that can get the location from heteregenous request types
	 */
	protected Point getLocationFromRequest(Request request){
		if( request instanceof CreateRequest)
			return ((CreateRequest)request).getLocation();
		if( request instanceof ChangeBoundsRequest)
			return ((ChangeBoundsRequest)request).getLocation();
		return null;
	}
	
	/**
	 * returns the constraint value of the cell at the given point.
	 */
	protected java.lang.Object getConstraintFor(Point point) {
		if (fViewFormLayoutFeedback != null) {
			Point relativePoint = point.getCopy();
			getHostFigure().translateToRelative(relativePoint);
			return fViewFormLayoutFeedback.getCurrentConstraint(relativePoint);
		}
		return null;
	}
	
	/**
	 * show the feedback for the region
	 */
	private void addRegionFeedback(Request request) {
		// Now show feedback on the current available region
		Point position = getLocationFromRequest(request).getCopy();
		// This point is absolute.  Make it relative to the model
		getHostFigure().translateToRelative(position);
		// Get the constraint at this position
		String str = fViewFormLayoutFeedback.getCurrentConstraint(position);
		if (fLayoutPolicyHelper.isRegionAvailable(str)) {
			Rectangle r = fViewFormLayoutFeedback.getCurrentRectangle(position);
			if (r != null) {
				if (fCurrentRectangle == null || !r.equals(fCurrentRectangle)) {
					if (fRegionFeedback != null)
						removeFeedback(fRegionFeedback);
					fCurrentRectangle = r;
					CustomLayoutRegionFeedback rf = new CustomLayoutRegionFeedback();
					rf.setLabel(ViewFormLayoutFeedback.getDisplayConstraint(str));
					fRegionFeedback = rf;
					fRegionFeedback.setBounds(r);
					addFeedback(fRegionFeedback);
				}
			}
		}else{
			// moved into an already occupied or unavailable
			// region, so remove the old feedback, as it gives
			// false illusion of movement.
			if(fRegionFeedback!=null){
				removeFeedback(fRegionFeedback);
				fRegionFeedback=null;
				fCurrentRectangle=null;
			}
		}
	}
	
	/**
	 * createAddCommand.
	 * A new child is being moved to the container.
	 * Create the command to add it.
	 */
	protected Command createAddCommand(EditPart childEditPart, Object constraint) {
		if (constraint == null || !(constraint instanceof String)) return UnexecutableCommand.INSTANCE;
		if (fLayoutPolicyHelper.isRegionAvailable((String)constraint)) {
			return fLayoutPolicyHelper.getAddChildrenCommand(Collections.singletonList(childEditPart.getModel()), Collections.singletonList(constraint), null).getCommand(); 
		} else
			return UnexecutableCommand.INSTANCE;
	}
	
	/**
	 * The constraint is an IJavaObjectInstance with the string for the region
	 */
	protected Command createChangeConstraintCommand(EditPart childEditPart, Object constraint) {
		return fLayoutPolicyHelper.getChangeConstraintCommand(Collections.singletonList(childEditPart.getModel()), Collections.singletonList(constraint));
	}
	
	/**
	 * createCreateCommand.
	 * A brand new child is being add to the container.
	 * Create the command to add it.
	 */
	protected Command createCreateCommand(Object child, String aConstraint) {
		Command addCmd = UnexecutableCommand.INSTANCE;
		if (fLayoutPolicyHelper.isRegionAvailable(aConstraint)) {
			addCmd = fLayoutPolicyHelper.getCreateChildCommand(child,aConstraint,null).getCommand();
		}
		return addCmd;
	}
	
	/**
	 * focus has been lost from the layout, so remove the feedback
	 */
	protected void eraseLayoutTargetFeedback(Request request) {
		if (fViewFormLayoutFeedback != null) {
			removeFeedback(fViewFormLayoutFeedback);
			fViewFormLayoutFeedback = null;
		}
		if (fRegionFeedback != null) {
			removeFeedback(fRegionFeedback);
			fRegionFeedback = null;
			fCurrentRectangle = null;
		}
	}
	
	/**
	 * Analyze the request to find the position and use the layout feeback to get a constraint
	 * ( The feedback rectangles and has the smarts to know how to turn a point into a constraint )
	 */
	protected Command getAddCommand(Request request){
		ChangeBoundsRequest cbReq = (ChangeBoundsRequest) request;
		// Can't handle more than one component
		if (cbReq.getEditParts().size() > 1) return UnexecutableCommand.INSTANCE;
		
		EditPart child = (EditPart) cbReq.getEditParts().get(0);
		Point p = cbReq.getLocation().getCopy();
		getHostFigure().translateToRelative(p);
		String constraint = null;
		if (fViewFormLayoutFeedback != null) {
			constraint = fViewFormLayoutFeedback.getCurrentConstraint(p);
		}
		if (constraint != null) {
			return createAddCommand(child, constraint);
		} else { 
			return UnexecutableCommand.INSTANCE;
		}
	}
	
	/**
	 * return the feedback for the layout
	 */
	private Figure getViewFormLayoutFeedback(Request request) {
		// show the view form layout feedback
		if (fViewFormLayoutFeedback == null) {
			ViewFormLayoutFeedback bf = new ViewFormLayoutFeedback();
			bf.setLineStyle(SWT.LINE_DOT);
			fViewFormLayoutFeedback = bf;
			IFigure f = ((GraphicalEditPart) getHost()).getContentPane();
			Rectangle r = f.getBounds().getCopy(); // Don't work with the original, use a copy
			r.shrink(2,2);
			fViewFormLayoutFeedback.setBounds(r);
			// Moving objects is OK because we can handle switching components between regions
			// so we only need to black out the occupied regions for other types of requests
			if (request.getType() != RequestConstants.REQ_MOVE) {
				fViewFormLayoutFeedback.setFilledRegions(fLayoutPolicyHelper.getFilledRegions());
			} else {
				fViewFormLayoutFeedback.setFilledRegions(null);
			}
			addFeedback(fViewFormLayoutFeedback);
		}
		
		addRegionFeedback(request);
		return fViewFormLayoutFeedback;
	}
	
	/**
	 * Analyze the request to find the position and use the layout feeback to get a constraint
	 * ( The feedback rectangles and has the smarts to know how to turn a point into a constraint )
	 */
	protected Command getCreateCommand(CreateRequest request){
		Point p = request.getLocation().getCopy();
		getHostFigure().translateToRelative(p);
		String constraint = null;
		if (fViewFormLayoutFeedback != null) {
			constraint = fViewFormLayoutFeedback.getCurrentConstraint(p);
		}
		if (constraint != null) {
			return createCreateCommand(request.getNewObject(), constraint);
		} else { 
			return UnexecutableCommand.INSTANCE;
		}
	}
	
	protected void showLayoutTargetFeedback(Request request) {
		getViewFormLayoutFeedback(request);
	}

	/**
	 * returns the command to delete a child of the ViewForm
	 */
	protected Command getDeleteDependantCommand(Request request) {
		Command deleteContributionCmd = fPolicy.getCommand(request);
		if (deleteContributionCmd == null)
			return UnexecutableCommand.INSTANCE; // It can't be created.
		return deleteContributionCmd;
	}
	
	/**
	 * returns the command that allows a child to move from one open cell to another
	 */
	protected Command getMoveChildrenCommand(Request generic) {
		EObject parent = (EObject) fPolicy.getContainer();
		
		EStructuralFeature sfLeftControl = 
			JavaInstantiation.getSFeature((IJavaObjectInstance) parent, SWTConstants.SF_VIEWFORM_TOPLEFT);
		EStructuralFeature sfRightControl = 
			JavaInstantiation.getSFeature((IJavaObjectInstance) parent, SWTConstants.SF_VIEWFORM_TOPRIGHT);
		EStructuralFeature sfCenterControl = 
			JavaInstantiation.getSFeature((IJavaObjectInstance) parent, SWTConstants.SF_VIEWFORM_TOPCENTER);
		EStructuralFeature sfContentControl = 
			JavaInstantiation.getSFeature((IJavaObjectInstance) parent, SWTConstants.SF_VIEWFORM_CONTENT);
		
		IJavaInstance left = (IJavaInstance) parent.eGet(sfLeftControl);
		IJavaInstance right = (IJavaInstance) parent.eGet(sfRightControl);
		IJavaInstance center = (IJavaInstance) parent.eGet(sfCenterControl);
		IJavaInstance content = (IJavaInstance) parent.eGet(sfContentControl);
		
		ChangeBoundsRequest request = (ChangeBoundsRequest)generic;
		List sources = request.getEditParts();
		
		// For now only allow one object to be moved
		if ( sources.size() > 1 ) return null;
		
		EditPart child = (EditPart) sources.iterator().next();

		String newConstraint = (String) getConstraintFor(request.getLocation());

		Command moveControl = null;
		CommandBuilder cBld = new CommandBuilder(""); //$NON-NLS-1$
		
		EStructuralFeature moveFrom = null;
		EStructuralFeature moveTo = null;
		
		if(left != null && left.equals(child.getModel()))
			moveFrom = sfLeftControl;
		else if(right != null && right.equals(child.getModel()))
			moveFrom = sfRightControl;
		else if(center != null && center.equals(child.getModel()))
			moveFrom = sfCenterControl;
		else if(content != null && content.equals(child.getModel()))
			moveFrom = sfContentControl;
		
		if(left == null && 
				((String) ViewFormLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(ViewFormLayoutPolicyHelper.LEFT_INDEX)).equals(newConstraint))
			moveTo = sfLeftControl;
		else if(right == null && 
				((String) ViewFormLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(ViewFormLayoutPolicyHelper.RIGHT_INDEX)).equals(newConstraint))
			moveTo = sfRightControl;
		else if(center == null && 
				((String) ViewFormLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(ViewFormLayoutPolicyHelper.CENTER_INDEX)).equals(newConstraint))
			moveTo = sfCenterControl;
		else if(content == null && 
				((String) ViewFormLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(ViewFormLayoutPolicyHelper.CONTENT_INDEX)).equals(newConstraint))
			moveTo = sfContentControl;
		
		if(moveFrom != null && moveTo != null){
			cBld.applyAttributeSetting(parent, moveFrom, null);
			cBld.applyAttributeSetting(parent, moveTo, child.getModel(), null);
			moveControl = cBld.getCommand();
		}
		
		if(moveControl == null || !moveControl.canExecute())
			return NoOpCommand.INSTANCE;		

		return moveControl;
	}
	
	/**
	 * getOrphanChildCommand. To orphan, we delete only the child. We don't
	 * delete the subpart since the part is not going away, just somewhere's
	 * else. So the subpart stays.
	 */
	protected Command getOrphanChildrenCommand(Request request) {
		Command orphanContributionCmd = fPolicy.getCommand(request);
		if (orphanContributionCmd == null)
			return UnexecutableCommand.INSTANCE; // It can't be created.
		return orphanContributionCmd;
	}
}

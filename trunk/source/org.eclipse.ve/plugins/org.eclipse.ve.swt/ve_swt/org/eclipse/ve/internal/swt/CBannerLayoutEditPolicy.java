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
 *  $RCSfile: CBannerLayoutEditPolicy.java,v $
 *  $Revision: 1.2 $  $Date: 2005-06-21 15:06:25 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.*;

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
 * 
 * @since 1.1.0
 */
public class CBannerLayoutEditPolicy extends LayoutEditPolicy{
	
	protected VisualContainerPolicy fPolicy;
	protected CBannerLayoutPolicyHelper fLayoutPolicyHelper;
	protected CBannerLayoutFeedback fCBannerLayoutFeedback = null;
	protected CBannerLayoutRegionFeedback fRegionFeedback = null;
	protected Rectangle fCurrentRectangle = null;

	public CBannerLayoutEditPolicy(EditDomain anEditDomain) {
		fPolicy = new CBannerContainerPolicy(anEditDomain);
		fLayoutPolicyHelper = new CBannerLayoutPolicyHelper();
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
	
	private void addRegionFeedback(Request request) {
		// Now show feedback on the current available region
		Point position = getLocationFromRequest(request).getCopy();
		// This point is absolute.  Make it relative to the model
		getHostFigure().translateToRelative(position);
		// Get the constraint at this position
		String str = fCBannerLayoutFeedback.getCurrentConstraint(position);
		if (fLayoutPolicyHelper.isRegionAvailable(str)) {
			Rectangle r = fCBannerLayoutFeedback.getCurrentRectangle(position);
			if (r != null) {
				if (fCurrentRectangle == null || !r.equals(fCurrentRectangle)) {
					if (fRegionFeedback != null)
						removeFeedback(fRegionFeedback);
					fCurrentRectangle = r;
					CBannerLayoutRegionFeedback rf = new CBannerLayoutRegionFeedback();
					rf.setLabel(CBannerLayoutFeedback.getDisplayConstraint(str));
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
		List children = new ArrayList(1);
		IJavaObjectInstance child = (IJavaObjectInstance)childEditPart.getModel();
		children.add(child);
		Command addCmd = UnexecutableCommand.INSTANCE;
		if (fLayoutPolicyHelper.isRegionAvailable((String)constraint)) {
			addCmd = fLayoutPolicyHelper.getAddChildrenCommand(children, Collections.singletonList(constraint), null); 
		}
		return addCmd;
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
			addCmd = fLayoutPolicyHelper.getCreateChildCommand(child,aConstraint,null);
		}
		return addCmd;
	}
	
	protected void eraseLayoutTargetFeedback(Request request) {
		if (fCBannerLayoutFeedback != null) {
			removeFeedback(fCBannerLayoutFeedback);
			fCBannerLayoutFeedback = null;
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
		if (fCBannerLayoutFeedback != null) {
			constraint = fCBannerLayoutFeedback.getCurrentConstraint(p);
		}
		if (constraint != null) {
			return createAddCommand(child, constraint);
		} else { 
			return UnexecutableCommand.INSTANCE;
		}
	}
	
	private Figure getCBannerLayoutFeedback(Request request) {
		// show the banner layout feedback
		if (fCBannerLayoutFeedback == null) {
			CBannerLayoutFeedback bf = new CBannerLayoutFeedback();
			bf.setLineStyle(SWT.LINE_DOT);
			fCBannerLayoutFeedback = bf;
			IFigure f = ((GraphicalEditPart) getHost()).getContentPane();
			Rectangle r = f.getBounds().getCopy(); // Don't work with the original, use a copy
			r.shrink(2,2);
			fCBannerLayoutFeedback.setBounds(r);
			// Moving objects is OK because we can handle switching components between regions
			// so we only need to black out the occupied regions for other types of requests
			if (request.getType() != RequestConstants.REQ_MOVE) {
				fCBannerLayoutFeedback.setFilledRegions(fLayoutPolicyHelper.getFilledRegions());
			} else {
				fCBannerLayoutFeedback.setFilledRegions(null);
			}
			addFeedback(fCBannerLayoutFeedback);
		}
		
		addRegionFeedback(request);
		return fCBannerLayoutFeedback;
	}
	
	/**
	 * Analyze the request to find the position and use the layout feeback to get a constraint
	 * ( The feedback rectangles and has the smarts to know how to turn a point into a constraint )
	 */
	protected Command getCreateCommand(CreateRequest request){
		Point p = request.getLocation().getCopy();
		getHostFigure().translateToRelative(p);
		String constraint = null;
		if (fCBannerLayoutFeedback != null) {
			constraint = fCBannerLayoutFeedback.getCurrentConstraint(p);
		}
		if (constraint != null) {
			return createCreateCommand(request.getNewObject(), constraint);
		} else { 
			return UnexecutableCommand.INSTANCE;
		}
	}

	
	protected void showLayoutTargetFeedback(Request request) {
		getCBannerLayoutFeedback(request);
	}

	/**
	 * getDeleteDependantCommand method comment.
	 */
	protected Command getDeleteDependantCommand(Request request) {
		Command deleteContributionCmd = fPolicy.getCommand(request);
		if (deleteContributionCmd == null)
			return UnexecutableCommand.INSTANCE; // It can't be created.
		return deleteContributionCmd;
	}
	
	/**
	 * returns the constraint value of the cell at the given point.
	 */
	protected java.lang.Object getConstraintFor(Point point) {
		if (fCBannerLayoutFeedback != null) {
			Point relativePoint = point.getCopy();
			getHostFigure().translateToRelative(relativePoint);
			return fCBannerLayoutFeedback.getCurrentConstraint(relativePoint);
		}
		return null;
	}
	
	/**
	 * allows a child to move from one open cell to another.
	 */
	protected Command getMoveChildrenCommand(Request generic) {
		EObject parent = (EObject) fPolicy.getContainer();
		
		EStructuralFeature sfLeftControl = 
			JavaInstantiation.getSFeature((IJavaObjectInstance) parent, SWTConstants.SF_CBANNER_LEFT);
		EStructuralFeature sfRightControl = 
			JavaInstantiation.getSFeature((IJavaObjectInstance) parent, SWTConstants.SF_CBANNER_RIGHT);
		EStructuralFeature sfBottomControl = 
			JavaInstantiation.getSFeature((IJavaObjectInstance) parent, SWTConstants.SF_CBANNER_BOTTOM);
		
		IJavaInstance left = (IJavaInstance) parent.eGet(sfLeftControl);
		IJavaInstance right = (IJavaInstance) parent.eGet(sfRightControl);
		IJavaInstance bottom = (IJavaInstance) parent.eGet(sfBottomControl);
		
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
		else if(bottom != null && bottom.equals(child.getModel()))
			moveFrom = sfBottomControl;
		
		if(left == null && 
				((String) CBannerLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(CBannerLayoutPolicyHelper.LEFT_INDEX)).equals(newConstraint))
			moveTo = sfLeftControl;
		else if(right == null && 
				((String) CBannerLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(CBannerLayoutPolicyHelper.RIGHT_INDEX)).equals(newConstraint))
			moveTo = sfRightControl;
		else if(bottom == null && 
				((String) CBannerLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(CBannerLayoutPolicyHelper.BOTTOM_INDEX)).equals(newConstraint))
			moveTo = sfBottomControl;
		
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

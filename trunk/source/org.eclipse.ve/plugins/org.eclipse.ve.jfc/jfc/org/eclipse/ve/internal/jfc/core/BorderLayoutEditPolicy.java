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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: BorderLayoutEditPolicy.java,v $
 *  $Revision: 1.8 $  $Date: 2005-10-11 21:23:50 $ 
 */

import java.util.*;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.*;
import org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.*;
import org.eclipse.swt.SWT;

import org.eclipse.ve.internal.cde.commands.NoOpCommand;
import org.eclipse.ve.internal.cde.core.ContainerPolicy;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
import org.eclipse.jem.internal.proxy.core.IStringBeanProxy;
/**
 * Layout input policy for a java.awt.BorderLayoutManager.
 */
public class BorderLayoutEditPolicy extends ConstrainedLayoutEditPolicy {
	protected VisualContainerPolicy fPolicy;
	protected BorderLayoutPolicyHelper fLayoutPolicyHelper;
	protected BorderLayoutFeedback fBorderLayoutFeedback = null;
	protected BorderLayoutRegionFeedback fRegionFeedback = null;
	protected Rectangle fCurrentRectangle = null;
/**
 * Create with the appropriate policy helper.
 * Creation date: (09/05/00 11:48:38 AM)
 * @param aPolicyHelper com.ibm.etools.vce.IContainerInputPolicyHelper
 */
public BorderLayoutEditPolicy(VisualContainerPolicy aContainerPolicy) {
	fLayoutPolicyHelper = new BorderLayoutPolicyHelper();
	fLayoutPolicyHelper.setContainerPolicy(aContainerPolicy);
	fPolicy = aContainerPolicy;
}
public void activate() {
	super.activate();
	fPolicy.setContainer(getHost().getModel());
}

public void deactivate() {
	super.deactivate();
	fPolicy.setContainer(null);
}
protected EditPolicy createChildEditPolicy(EditPart child) {
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
	String str = fBorderLayoutFeedback.getCurrentConstraint(position);
	if (fLayoutPolicyHelper.isRegionAvailable(str)) {
		Rectangle r = fBorderLayoutFeedback.getCurrentRectangle(position);
		if (r != null) {
			if (fCurrentRectangle == null || !r.equals(fCurrentRectangle)) {
				if (fRegionFeedback != null)
					removeFeedback(fRegionFeedback);
				fCurrentRectangle = r;
				BorderLayoutRegionFeedback rf = new BorderLayoutRegionFeedback();
				rf.setLabel(BorderLayoutFeedback.getDisplayConstraint(str));
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
protected void eraseLayoutTargetFeedback(Request request) {
	if (fBorderLayoutFeedback != null) {
		removeFeedback(fBorderLayoutFeedback);
		fBorderLayoutFeedback = null;
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
	if (fBorderLayoutFeedback != null) {
		constraint = fBorderLayoutFeedback.getCurrentConstraint(p);
	}
	if (constraint != null) {
		return createAddCommand(child, constraint);
	} else { 
		return UnexecutableCommand.INSTANCE;
	}
}
protected Command getAlignmentChildCommand(Request request) {
	return null;
}
private Figure getBorderLayoutFeedback(Request request) {
	// show the border layout feedback
	if (fBorderLayoutFeedback == null) {
		BorderLayoutFeedback bf = new BorderLayoutFeedback();
		bf.setLineStyle(SWT.LINE_DOT);
		fBorderLayoutFeedback = bf;
		IFigure f = ((GraphicalEditPart) getHost()).getContentPane();
		Rectangle r = f.getBounds().getCopy(); // Don't work with the original, use a copy
		r.shrink(2,2);
		fBorderLayoutFeedback.setBounds(r);
		// Moving objects is OK because we can handle switching components between regions
		// so we only need to black out the occupied regions for other types of requests
		if (request.getType() != RequestConstants.REQ_MOVE) {
			fBorderLayoutFeedback.setFilledRegions(fLayoutPolicyHelper.getFilledRegions());
		} else {
			fBorderLayoutFeedback.setFilledRegions(null);
		}
		addFeedback(fBorderLayoutFeedback);
	}
	
	addRegionFeedback(request);
	return fBorderLayoutFeedback;
}
/**
 * getConstraintFor method comment.
 */
protected java.lang.Object getConstraintFor(Point point) {
	if (fBorderLayoutFeedback != null) {
		Point relativePoint = point.getCopy();
		getHostFigure().translateToRelative(relativePoint);
		return fBorderLayoutFeedback.getCurrentConstraint(relativePoint);
	}
	return null;
}
/**
 * getConstraintFor method comment.
 */
protected Object getConstraintFor(Rectangle rect) {
	return null;
}
protected String getConstraintFromViewObject(EditPart anEditPart) {
	// The editPart's model is the component.  Get the Constraint that holds it
	IJavaObjectInstance component = (IJavaObjectInstance)anEditPart.getModel();
	EObject borderComponent = InverseMaintenanceAdapter.getFirstReferencedBy(component, JavaInstantiation.getReference(component, JFCConstants.SF_CONSTRAINT_COMPONENT));
	IJavaObjectInstance  javaStringConstraint =	(IJavaObjectInstance) borderComponent.eGet(JavaInstantiation.getSFeature(component, JFCConstants.SF_CONSTRAINT_CONSTRAINT));
	String stringConstraint = ((IStringBeanProxy)BeanProxyUtilities.getBeanProxy(javaStringConstraint)).stringValue();
	return stringConstraint;
}
/**
 * Analyze the request to find the position and use the layout feeback to get a constraint
 * ( The feedback rectangles and has the smarts to know how to turn a point into a constraint )
 */
protected Command getCreateCommand(CreateRequest request){
	Point p = request.getLocation().getCopy();
	getHostFigure().translateToRelative(p);
	String constraint = null;
	if (fBorderLayoutFeedback != null) {
		constraint = fBorderLayoutFeedback.getCurrentConstraint(p);
	}
	if (constraint != null) {
		return createCreateCommand(request.getNewObject(), constraint);
	} else { 
		return UnexecutableCommand.INSTANCE;
	}
}
protected Command getDeleteDependantCommand(Request aRequest) {
	Command deleteContributionCmd = fPolicy.getCommand(aRequest);
	if ( deleteContributionCmd == null )
		return UnexecutableCommand.INSTANCE;	// It can't be deleted

	// Note: If there is any annotation, that will be deleted too by the
	// container policy, and that will then also delete all of the view info.
	// So we don't need to handle viewinfo here.
		
	return deleteContributionCmd;		
}
protected Command getDistributeChildCommand(Request request) {
	return null;
}
protected Command getMoveChildrenCommand(Request generic) {
	ChangeBoundsRequest request = (ChangeBoundsRequest)generic;
	List sources = request.getEditParts();
	// For now only allow one object to be moved - dealing with a bunch it tough for now
	if ( sources.size() > 1 ) return null;
	EditPart source = (EditPart) sources.iterator().next();
	String sourceConstraint = getConstraintFromViewObject(source);
	String targetConstraint = (String) getConstraintFor(request.getLocation());
	EditPart target = null;
	// Find the component at the target constraint if any
	List children = getHost().getChildren();
	Iterator iter = children.iterator();
	while( iter.hasNext()){
		EditPart aVO = (EditPart) iter.next();
		if (!aVO.equals(source)) {
			String cons = getConstraintFromViewObject(aVO);
			if (cons != null && cons.equals(targetConstraint)) {
				// found the target VO
				target = aVO;
				break;
			}
		}
	}

	CompoundCommand cmpCmd = new CompoundCommand("Move_Child"); //$NON-NLS-1$
	// change constraint of target view object if present
	if (target != null) {
		Command tgtCmd = createChangeConstraintCommand(target,sourceConstraint);
		if (tgtCmd != null) {
			cmpCmd.add(tgtCmd);
		}
	}
	// change constraint of source to target constraint if we have one
	if((targetConstraint != null)&&(!targetConstraint.equals(sourceConstraint))){
		Command srcCmd = createChangeConstraintCommand(source,targetConstraint);
		if (srcCmd != null) {
			cmpCmd.add(srcCmd);
		}
	}
	else{
		return NoOpCommand.INSTANCE;		
	}
	return cmpCmd;
}
protected Command getOrphanChildrenCommand(Request aRequest) {
	return fLayoutPolicyHelper.getOrphanChildrenCommand(ContainerPolicy.getChildren((GroupRequest) aRequest));
}
protected void showLayoutTargetFeedback(Request request) {
	getBorderLayoutFeedback(request);
}
}

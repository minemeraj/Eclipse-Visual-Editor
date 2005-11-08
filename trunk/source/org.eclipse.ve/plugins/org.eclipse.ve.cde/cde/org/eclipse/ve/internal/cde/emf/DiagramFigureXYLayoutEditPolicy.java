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
package org.eclipse.ve.internal.cde.emf;
/*
 *  $RCSfile: DiagramFigureXYLayoutEditPolicy.java,v $
 *  $Revision: 1.6 $  $Date: 2005-11-08 22:33:27 $ 
 */

import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import org.eclipse.ve.internal.cde.commands.ApplyVisualConstraintCommand;
import org.eclipse.ve.internal.cde.commands.CancelVisualConstraintCommand;
import org.eclipse.ve.internal.cde.core.*;

import org.eclipse.ve.internal.cdm.DiagramFigure;
import org.eclipse.ve.internal.cdm.KeyedValueHolder;
import org.eclipse.ve.internal.cdm.model.*;

import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;
/**
 * XYLayoutEditPolicy for DiagramFigures. The constraint info is stored
 * in the DiagramFigure.
 */
public class DiagramFigureXYLayoutEditPolicy extends XYLayoutEditPolicy {
	protected ContainerPolicy containerPolicy; // Handles the containment functions

	/**
	 * Create with the container policy for handling DiagramFigures
	 */
	public DiagramFigureXYLayoutEditPolicy(ContainerPolicy containerPolicy) {
		this.containerPolicy = containerPolicy;
	}

	public void activate() {
		super.activate();
		containerPolicy.setContainer(getHost().getModel());
	}

	public void deactivate() {
		super.deactivate();
		containerPolicy.setContainer(null);
	}

	/**
	 * The child editpart is about to be added to the parent.
	 * The child is an existing child that was orphaned from a previous parent.
	 */
	protected Command createAddCommand(EditPart childEditPart, Object constraint) {
		// We have to first see if we can add the component. If we can, then we can continue.
		// The child in this case should be a DiagramFigure.
		KeyedValueHolder child = (KeyedValueHolder) childEditPart.getModel();
		GroupRequest grpReq = new GroupRequest(RequestConstants.REQ_ADD);
		grpReq.setEditParts(childEditPart);
		Command addContributionCmd = containerPolicy.getCommand(grpReq);
		if (addContributionCmd == null || !addContributionCmd.canExecute())
			return UnexecutableCommand.INSTANCE; // It can't be added.

		CompoundCommand command = new CompoundCommand(""); //$NON-NLS-1$
		command.append(primChangeConstraintCommand(child, (Rectangle) constraint, true, true));
		command.append(addContributionCmd);

		return command.unwrap();
	}

	protected Command createChangeConstraintCommand(EditPart childEditPart, Object constraint, boolean moved, boolean resized) {
		return primChangeConstraintCommand((KeyedValueHolder) childEditPart.getModel(), (Rectangle) constraint, moved, resized);
	}

	/**
	 * A new child is being added to the parent. It isn't yet in the model.
	 */
	protected Command getCreateCommand(CreateRequest aRequest) {
		DiagramFigure child = (DiagramFigure) aRequest.getNewObject();

		Command createContributionCmd = containerPolicy.getCommand(aRequest);
		if (createContributionCmd == null || !createContributionCmd.canExecute())
			return UnexecutableCommand.INSTANCE; // It can't be created

		Rectangle constraint = (Rectangle) translateToModelConstraint(getConstraintFor(aRequest));
		CompoundCommand command = new CompoundCommand(""); //$NON-NLS-1$
		command.append(primChangeConstraintCommand(child, constraint, true, isChildResizeable(getCreateConstraintHandler(child))));
		command.append(createContributionCmd);

		return command.unwrap();
	}

	/*
	 * Get the IConstraintHandler for a created child.
	 */
	protected IConstraintHandler getCreateConstraintHandler(Object child) {
		IModelAdapterFactory factory = CDEUtilities.getModelAdapterFactory(getHost());
		return factory != null ? (IConstraintHandler) factory.getAdapter(child, IConstraintHandler.class) : null;
	}

	/*
	 * Get the IConstraintHandler for the edit part.
	 */
	protected IConstraintHandler getConstraintHandler(EditPart editpart) {
		return (IConstraintHandler) ((IAdaptable) editpart).getAdapter(IConstraintHandler.class);
	}

	/**
	 * A child is being deleted from the model.
	 */
	protected Command getDeleteDependantCommand(Request aRequest) {
		Command deleteContributionCmd = containerPolicy.getCommand(aRequest);
		if (deleteContributionCmd == null)
			return UnexecutableCommand.INSTANCE; // It can't be deleted

		return deleteContributionCmd;
	}

	/**
	 * getOrphanChildCommand: About to remove a child from the model
	 * so that it can be added someplace else.
	 *
	 * Remove the visual constraint since it may not be appropriate in 
	 * the new position.
	 */
	protected Command getOrphanChildrenCommand(Request aRequest) {
		// Now get the orphan command for the child.
		Command orphanContributionCmd = containerPolicy.getCommand(aRequest);
		if (orphanContributionCmd == null || !orphanContributionCmd.canExecute())
			return UnexecutableCommand.INSTANCE; // It can't be orphaned		

		CompoundCommand command = new CompoundCommand(""); //$NON-NLS-1$
		GroupRequest gr = (GroupRequest) aRequest;
		Iterator children = gr.getEditParts().iterator();
		while (children.hasNext()) {
			EditPart childEditPart = (EditPart) children.next();
			DiagramFigure child = (DiagramFigure) childEditPart.getModel();
			// Delete the visual constraint, if any.
			CancelVisualConstraintCommand delCommand = new CancelVisualConstraintCommand();
			delCommand.setTarget(child);
			command.append(delCommand);
		}
		command.append(orphanContributionCmd);
		return command;
	}

	protected Command primChangeConstraintCommand(KeyedValueHolder info, Rectangle constraint, boolean pointChanged, boolean sizeChanged) {
		Object constraintKV = info.getKeyedValues().get(CDMModelConstants.VISUAL_CONSTRAINT_KEY);
		Rectangle lConstraint = constraintKV instanceof Rectangle ? (Rectangle) constraintKV : null;
		pointChanged |= (lConstraint == null); // The location also changed if there wasn't currently a setting.
		sizeChanged |= (lConstraint == null); // The size also changed if there wasn't currently a setting.
		if (pointChanged || sizeChanged) {
			// We have actually moved/sized some.
			// Update the keyed value

			ApplyVisualConstraintCommand viUpdate = new ApplyVisualConstraintCommand(""); //$NON-NLS-1$
			viUpdate.setTarget(info);
			if (pointChanged && sizeChanged)
				viUpdate.setRectangle(constraint);
			else if (pointChanged)
				viUpdate.setLocation(new Point(constraint.x, constraint.y));
			else
				viUpdate.setSize(new Dimension(constraint.width, constraint.height));
			return viUpdate;
		}

		return null;
	}

	public Command getCommand(Request request) {
		if (REQ_DELETE.equals(request.getType()))
			return containerPolicy.getCommand(request);
		return super.getCommand(request);
	}

	/**
	 * Update ViewObject LayoutConstraints
	 * based on model changes. This edit policy will of added a child
	 * editpolicy that listens for changes in the VisualConstraint. It will
	 * call this method to let us know that the constraint has changed.
	 * It will also be called when the child editpolicy has been activated
	 * with the current contents of the constraint so that the initial
	 * setting may be made.
	 */
	protected void refreshFromEditPart(EditPart child, Object constraintValue) {
		org.eclipse.draw2d.geometry.Rectangle rect = null;
		Rectangle constraint = (constraintValue instanceof Rectangle) ? (Rectangle) constraintValue : null;
		if (constraint == null)
			rect = XYLayoutUtility.modifyPreferredRectangle(new org.eclipse.draw2d.geometry.Rectangle(), true, true, true);
		// No constraint, so set to complete default.
		else
			rect = (org.eclipse.draw2d.geometry.Rectangle) modelToFigureConstraint(constraint);

		setConstraintToFigure(child, rect);
	}

	protected void setConstraintToFigure(EditPart child, org.eclipse.draw2d.geometry.Rectangle figureConstraint) {
		IConstraintHandler handler = getConstraintHandler(child);
		if (handler != null) {
			int x = figureConstraint.x; // So that any changes are restored after contribute.
			int y = figureConstraint.y;
			handler.contributeFigureSize(figureConstraint);
			figureConstraint.setLocation(x, y);
		}
		((GraphicalEditPart) getHost()).setLayoutConstraint(child, ((GraphicalEditPart) child).getFigure(), figureConstraint);
	}

	/**
	 * Translate from the figure constraint (it is assumed that zoom/grid already taken out of the picture).
	 */
	protected Object translateToModelConstraint(Object figureConstraint) {
		if (figureConstraint instanceof org.eclipse.draw2d.geometry.Rectangle) {
			org.eclipse.draw2d.geometry.Rectangle rect = (org.eclipse.draw2d.geometry.Rectangle) figureConstraint;
			return new Rectangle(rect.x, rect.y, rect.width, rect.height);	// Convert to CDM rect.
		} else if (figureConstraint instanceof org.eclipse.draw2d.geometry.Point) {
			org.eclipse.draw2d.geometry.Point point = (org.eclipse.draw2d.geometry.Point) figureConstraint;
			return new Point(point.x, point.y);	// Convert to CDM point.
		} else
			return figureConstraint;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.XYLayoutEditPolicy#convertModelToDraw2D(java.lang.Object)
	 */
	protected Object convertModelToDraw2D(Object modelconstraint) {

		if (modelconstraint instanceof Rectangle) {
			Rectangle rect = (Rectangle) modelconstraint;
			return new org.eclipse.draw2d.geometry.Rectangle(rect.x, rect.y, rect.width, rect.height);	// Convert to CDM rect.
		} else if (modelconstraint instanceof Point) {
			Point point = (Point) modelconstraint;
			return new org.eclipse.draw2d.geometry.Point(point.x, point.y);	// Convert to CDM point.
		} else
			return modelconstraint;
	
	}

	/**
	 * Change to figure constraint, need to take zoom into account.
	 */
	protected Object modelToFigureConstraint(Object modelConstraint) {
		if (modelConstraint == null)
			return null;
		Rectangle r = (Rectangle) modelConstraint;
		org.eclipse.draw2d.geometry.Rectangle figureConstraint =
			new org.eclipse.draw2d.geometry.Rectangle(r.x, r.y, r.width, r.height);
		ZoomController zoomController = getZoomController();
		if (zoomController != null) {
			if (figureConstraint.x != XYLayoutUtility.PREFERRED_LOC)
				figureConstraint.x = zoomController.zoomCoordinate(figureConstraint.x);
			if (figureConstraint.y != XYLayoutUtility.PREFERRED_LOC)
				figureConstraint.y = zoomController.zoomCoordinate(figureConstraint.y);
		}
		return figureConstraint;
	}

	protected boolean isChildResizeable(EditPart childEditpart) {
		return isChildResizeable(getConstraintHandler(childEditpart));
	}

	protected boolean isChildResizeable(IConstraintHandler handler) {
		return (handler != null) ? handler.isResizeable() : false;
	}

	protected Object getChildConstraint(EditPart child) {
		return getChildConstraint(
			child,
			((KeyedValueHolder) child.getModel()).getKeyedValues().get(CDMModelConstants.VISUAL_CONSTRAINT_KEY));
	}

	protected Object getChildConstraint(EditPart child, Object kv) {
		return kv instanceof Rectangle ? new Rectangle((Rectangle) kv) : XYLayoutUtility.modifyPreferredCDMRectangle(new Rectangle(), true, true, true);
	}

	/**
	 * A child has been added. We need to add in the listener for Visual Constraint changes. We will
	 * also have an editpolicy on the child to handle this.
	 */
	public static final String VISUAL_CONSTRAINT_REFRESH_POLICY = "org.eclipse.ve.internal.cde.core.visualConstraintRefreshPolicy"; //$NON-NLS-1$

	protected class VisualConstraintRefreshPolicy extends AbstractEditPolicy {
		private Adapter adapter;

		public void activate() {
			super.activate();
			// Add listener to visual constraint.
			adapter = getAdapter();
			KeyedValueHolder target = (KeyedValueHolder) getHost().getModel(); // The child diagram figure is a key holder.
			target.eAdapters().add(adapter);
			// Signal an initial change so that this child gets positioned correctly.
			refreshFromEditPart(getHost(), target.getKeyedValues().get(CDMModelConstants.VISUAL_CONSTRAINT_KEY));
		}

		public void deactivate() {
			super.deactivate();
			if (adapter != null)
				adapter.getTarget().eAdapters().remove(adapter);
			adapter = null;
		}

		public EditPart getTargetEditPart(Request request) {
			return null;
		}

		protected Adapter getAdapter() {
			if (adapter == null) {
				adapter = new AdapterImpl() {
					public void notifyChanged(Notification msg) {
						Notification keyMsg = KeyedValueNotificationHelper.notifyChanged(msg, CDMModelConstants.VISUAL_CONSTRAINT_KEY);
						if (keyMsg != null) {
							switch (keyMsg.getEventType()) {
								case Notification.SET : // It was changed.
									refreshFromEditPart(getHost(), ((BasicEMap.Entry) keyMsg.getNewValue()).getValue());
									break;

								case Notification.UNSET : // It was removed
									refreshFromEditPart(getHost(), null);
									break;
							}
						}
					}
				};
			}
			return adapter;
		}

	};
	protected void decorateChild(EditPart child) {
		super.decorateChild(child);
		child.installEditPolicy(VISUAL_CONSTRAINT_REFRESH_POLICY, new VisualConstraintRefreshPolicy());
	}

}

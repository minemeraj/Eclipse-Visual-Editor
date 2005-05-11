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
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: VisualInfoXYLayoutEditPolicy.java,v $
 *  $Revision: 1.6 $  $Date: 2005-05-11 19:01:26 $ 
 */

import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import org.eclipse.ve.internal.cdm.*;
import org.eclipse.ve.internal.cdm.model.*;

import org.eclipse.ve.internal.cde.commands.CancelVisualConstraintCommand;

import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;
/**
 * XYLayoutEditPolicy where the constraint info is stored
 * in the VisualInfo for the current diagram.
 */
public class VisualInfoXYLayoutEditPolicy extends XYLayoutEditPolicy {
	protected ContainerPolicy containerPolicy; // Handles the containment functions

	/**
	 * Create with the container policy for handling DiagramFigures.
	 */
	public VisualInfoXYLayoutEditPolicy(ContainerPolicy containerPolicy) {
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
		Object child = childEditPart.getModel();
		GroupRequest grpReq = new GroupRequest(RequestConstants.REQ_ADD);
		grpReq.setEditParts(childEditPart);
		Command addContributionCmd = containerPolicy.getCommand(grpReq);
		if (addContributionCmd == null || !addContributionCmd.canExecute())
			return UnexecutableCommand.INSTANCE; // It can't be added.

		// Actually when adding to the freeform, we want controls to be preferred size, not
		// the size they happen to of been.
		 ((Rectangle) constraint).width = -1;
		((Rectangle) constraint).height = -1;
		IConstraintHandler handler = getChildConstraintHandler(child);
		CompoundCommand command = new CompoundCommand();
		command.append(addContributionCmd);
		command.append(primChangeConstraintCommand(child, handler, (Rectangle) constraint, true, false));

		return command.unwrap();
	}

	protected Command createChangeConstraintCommand(EditPart childEditPart, Object constraint, boolean moved, boolean resized) {
		return primChangeConstraintCommand(
			childEditPart.getModel(),
			getConstraintHandler(childEditPart),
			(Rectangle) constraint,
			moved,
			resized);
	}

	/**
	 * A new child is being added to the parent. It isn't yet in the model.
	 */
	protected Command getCreateCommand(CreateRequest aRequest) {
		Object child = aRequest.getNewObject();

		Command createContributionCmd = containerPolicy.getCommand(aRequest);
		if (createContributionCmd == null || !createContributionCmd.canExecute())
			return UnexecutableCommand.INSTANCE; // It can't be created

		Rectangle constraint = (Rectangle) translateToModelConstraint(getConstraintFor(aRequest));
		CompoundCommand command = new CompoundCommand();
		command.append(createContributionCmd);
		IConstraintHandler constraintHandler = getChildConstraintHandler(child);
		// Resize request if child is resizeable AND this is not default size.
		command.append(
			primChangeConstraintCommand(
				child,
				constraintHandler,
				(Rectangle) constraint,
				true,
				isChildResizeable(constraintHandler) && (constraint.width != -1 || constraint.height != -1)));

		return command.unwrap();
	}

	/*
	 * Get the IConstraintHandler for a child. This is in the generic case of a child, not
	 * an editpart.
	 */
	protected IConstraintHandler getChildConstraintHandler(Object child) {
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

		// Note: If there is any annotation, that will be deleted too by the
		// container policy, and that will then also delete all of the view info.
		// So we don't need to handle viewinfo here.

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
		command.append(orphanContributionCmd);
		GroupRequest gr = (GroupRequest) aRequest;
		Iterator children = gr.getEditParts().iterator();
		while (children.hasNext()) {
			EditPart childEditPart = (EditPart) children.next();
			VisualInfo vi = VisualInfoPolicy.getVisualInfo(childEditPart);
			if (vi != null) {
				// Delete the visual constraint, if any. Leave the VI there even if empty.
				CancelVisualConstraintCommand delCommand = new CancelVisualConstraintCommand();
				delCommand.setTarget(vi);
				command.append(delCommand);
			}
			IConstraintHandler constraintHandler = getConstraintHandler(childEditPart);
			if (constraintHandler != null) {
				command.append(constraintHandler.contributeOrphanChildCommand());
			}
		}
		return command;
	}

	protected Command primChangeConstraintCommand(
		Object model,
		IConstraintHandler handler,
		Rectangle constraint,
		boolean pointChanged,
		boolean sizeChanged) {
		EditPartViewer viewer = getHost().getRoot().getViewer();
		VisualInfo vi = VisualInfoPolicy.getVisualInfo(model, viewer);
		Object constraintKV = vi != null ? vi.getKeyedValues().get(CDMModelConstants.VISUAL_CONSTRAINT_KEY) : null;
		Rectangle lConstraint = constraintKV instanceof Rectangle ? (Rectangle) constraintKV : null;
		pointChanged |= lConstraint == null;
		if (pointChanged || sizeChanged) {
			// We have actually moved/sized some.
			// Update the keyed value

			EditDomain dom = EditDomain.getEditDomain(getHost());
			Object newConstraint = null;
			Command sizeCommand = null;
			if (pointChanged && sizeChanged) {
				newConstraint = constraint;
				if (handler != null) {
					sizeCommand = handler.contributeSizeCommand(constraint.width, constraint.height, dom);
					if (sizeCommand != null)
						constraint.width = constraint.height = -1;
				} else
					constraint.width = constraint.height = -1; // No handler, so no resize.
			} else if (pointChanged)
				newConstraint = new Point(constraint.x, constraint.y);
			else {
				if (handler != null) {
					sizeCommand = handler.contributeSizeCommand(constraint.width, constraint.height, dom);
					if (sizeCommand != null)
						return sizeCommand; // Position hasn't changed, and size is not in visual info, so don't bother updating it.
				}
				newConstraint = new Dimension(constraint.width, constraint.height);
			}
			Command viCmd = VisualInfoPolicy.applyVisualInfoSetting(model, newConstraint, dom, dom.getDiagram(viewer));
			return sizeCommand == null ? viCmd : (Command) viCmd.chain(sizeCommand);
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
	 * <p>
	 */
	protected void refreshFromEditPart(EditPart child, Object constraintValue) {		
		Rectangle constraint = (Rectangle) getChildConstraint(child, constraintValue);
		org.eclipse.draw2d.geometry.Rectangle rect = (org.eclipse.draw2d.geometry.Rectangle) modelToFigureConstraint(constraint);
		setConstraintToFigure(child, rect);
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.XYLayoutEditPolicy#setConstraintToFigure(org.eclipse.gef.EditPart, org.eclipse.draw2d.geometry.Rectangle)
	 * 
	 * Note: This must be executed in the UI thread.
	 */
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

	/*
	 * Note: This must be executed in the UI thread.
	 */
	private void setNewSize(EditPart child, int width, int height) {
		// Used solely by the refresh policy to set a new size from the child so that we don't need to query
		// and calculate the whole constraint. We just get the old figure constraint and reapply with the size
		// changed.
		org.eclipse.draw2d.geometry.Rectangle rect =
			(org.eclipse.draw2d.geometry.Rectangle) ((GraphicalEditPart) getHost()).getContentPane().getLayoutManager().getConstraint(
				((GraphicalEditPart) child).getFigure());

		if (rect == null) {
			rect = XYLayoutUtility.modifyPreferredRectangle(new org.eclipse.draw2d.geometry.Rectangle(0, 0, width, height), true, false, false);
		} else
			rect = rect.getCopy().setSize(width, height);
		setConstraintToFigure(child, rect);
	}

	/**
	 * Translate from the figure constraint (it is assumed that zoom/grid already taken out of the picture).
	 */
	protected Object translateToModelConstraint(Object figureConstraint) {
		org.eclipse.draw2d.geometry.Rectangle r = (org.eclipse.draw2d.geometry.Rectangle) figureConstraint;
		return new Rectangle(r.x, r.y, r.width, r.height);
	}

	/**
	 * Translate to figure constraint, need to take zoom into account.
	 */
	protected Object modelToFigureConstraint(Object modelConstraint) {
		if (modelConstraint == null) {
			modelConstraint =  XYLayoutUtility.modifyPreferredCDMRectangle(new Rectangle(), true, true, true);
		}
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
		VisualInfo vi = VisualInfoPolicy.getVisualInfo(child);
		if (vi == null)
			return null;
		return getChildConstraint(child, vi.getKeyedValues().get(CDMModelConstants.VISUAL_CONSTRAINT_KEY));
	}

	protected Object getChildConstraint(EditPart child, Object kv) {
		Rectangle constraint;
		if (kv instanceof Rectangle)
			constraint = (Rectangle) kv;
		else {
			constraint = XYLayoutUtility.modifyPreferredCDMRectangle(new Rectangle(), true, true, true);
		}
		IConstraintHandler handler = getConstraintHandler(child);
		if (handler != null) {
			int x = constraint.x;
			int y = constraint.y;
			handler.contributeModelSize(constraint);
			constraint.x = x;
			constraint.y = y;
		}
		return constraint;
	}
	
	/**
	 * A child has been added. We need to add in the listener for Visual Constraint changes. We will
	 * also have an editpolicy on the child to handle this.
	 */
	public static final String VISUAL_CONSTRAINT_REFRESH_POLICY = "org.eclipse.ve.internal.cde.core.visualConstraintRefreshPolicy"; //$NON-NLS-1$
	protected class VisualConstraintRefreshPolicy extends AbstractEditPolicy implements IConstraintHandler.IConstraintHandlerListener {
		private VisualInfoPolicy.VisualInfoListener viListener;
		private IConstraintHandler handler;
		private boolean deactivated;	// set to true when deactivated in case we get a late async notification for resizing

		public void activate() {
			deactivated = false;
			super.activate();
			// Add listener to visual constraint.
			EditPartViewer viewer = getHost().getRoot().getViewer();
			EditDomain dom = (EditDomain) viewer.getEditDomain();
			viListener = new VisualInfoPolicy.VisualInfoListener(getHost().getModel(), dom.getDiagram(viewer), dom) {
				public void notifyVisualInfoChanges(Notification msg) {
					if (msg.getFeatureID(VisualInfo.class) == CDMPackage.VISUAL_INFO__KEYED_VALUES) {
						Notification kvMsg = KeyedValueNotificationHelper.notifyChanged(msg, CDMModelConstants.VISUAL_CONSTRAINT_KEY);
						if (kvMsg != null) {
							// The constraint keyedvalue was changed
							switch (kvMsg.getEventType()) {
								case Notification.SET : // It was changed.
									queueRefreshFromEditPart(((BasicEMap.Entry) kvMsg.getNewValue()).getValue());
									break;

								case Notification.UNSET : // It was removed
									queueRefreshFromEditPart(null);
									break;
							}
						}
					}
				}

				public void notifyVisualInfo(int eventType, VisualInfo oldVI, VisualInfo newVI) {
					// A visual info was either added or removed
					signalRefresh();
				}

				public void notifyAnnotation(int eventType, Annotation oldAnnotation, Annotation newAnnotation) {
					// An annotation was either added or removed
					super.notifyAnnotation(eventType, oldAnnotation, newAnnotation);
					signalRefresh();
				}

			};

			// Add listener to constraint handler, if there is one.
			handler = getConstraintHandler(getHost());
			if (handler != null)
				handler.addConstraintHandlerListener(this);
			// Signal an initial change so that this child gets positioned correctly.
			signalRefresh();
		}
		

		/*
		 * Signal the refresh from current state.
		 */
		private void signalRefresh() {
			Object kv = null;
			VisualInfo vi = VisualInfoPolicy.getVisualInfo(getHost());
			if (vi != null) {
				kv = vi.getKeyedValues().get(CDMModelConstants.VISUAL_CONSTRAINT_KEY);
			}
			queueRefreshFromEditPart(kv);
		}
		
		private void queueRefreshFromEditPart(final Object constraint) {
			if (getHost() != null)
				CDEUtilities.displayExec(getHost(), "REFRESH_FROM_EDITPART", new Runnable() {
				public void run() {
					if (!deactivated)
						refreshFromEditPart(getHost(), constraint);
				}
			});
		}		

		public void deactivate() {
			deactivated = true;
			super.deactivate();
			if (viListener != null)
				viListener.removeListening();
			viListener = null;
			if (handler != null)
				handler.removeConstraintHandlerListener(this);
			handler = null;
		}

		public EditPart getTargetEditPart(Request request) {
			return null;
		}

		public void sizeChanged(final int width, final int height) {
			CDEUtilities.displayExec(getHost(), new Runnable() {
				public void run() {
					if (!deactivated) {
						setNewSize(getHost(), width, height);
					}
				}
			});
		}

	};
	protected void decorateChild(EditPart child) {
		super.decorateChild(child);
		child.installEditPolicy(VISUAL_CONSTRAINT_REFRESH_POLICY, new VisualConstraintRefreshPolicy());
	}

}

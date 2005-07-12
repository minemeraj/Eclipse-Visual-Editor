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
 *  $RCSfile: VisualComponentsLayoutPolicy.java,v $
 *  $Revision: 1.12 $  $Date: 2005-07-12 21:08:25 $ 
 */

import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.gef.*;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
/**
 * This is an editpolicy that is placed onto a VisualComponent, and that has
 * children that are visual components. What it does is control the figure constraints
 * based upon the children visual component notifications. As the children change position and
 * size, this will receive the changes and change the child's figure constraint to match.
 * <p>
 * It will get the IVisualComponent from the host editpart and the children editparts by asking through the getAdapter() interface.
 * The children should return their visual component. If they need to change the visual component for some
 * reason, they need to deactivate and reactivate their edit policy with the key CONSTRAINT_REFRESH_POLICY
 * (see the declaration in this class). That will cause the visual component to be recycled.
 * <p>
 * <b>NOTE:</b> It is NOT a LayoutEditPolicy. This is because it will not handle add/moving/changing/deleting
 * of children. That is the responsibility of the LayoutEditPolicy. However, this is a sub-function of
 * the LayoutEditPolicy. The layout edit policy usually handles this function too. But for visual components
 * this isn't necessary because they handle those functions themselves. So there will be a LayoutEditPolicy
 * that works in conjunction with this. Its only function will be to handle the models. When the
 * constraints are applied to the model, the model will apply them to visual component, who will then 
 * notify back to this policy that it has changed.
 * 
 * @since 1.0.0
 */
public class VisualComponentsLayoutPolicy extends AbstractEditPolicy {
	
	// Key to use for the edit policy when it is installed on the container.
	public static final String LAYOUT_POLICY = "com.ibm.etools.visualcomponentslayoutpolicy"; //$NON-NLS-1$
	public static final String CONSTRAINT_REFRESH_POLICY = "com.ibm.etools.componentconstraintrefreshpolicy"; //$NON-NLS-1$
	
	// A static so that visual components can get tracing in on what happens. It is non-final so that a change here
	// can be seen by other classes without the other classes needing to be recompiled.
	// Other classes such as ComponentManager can use this to provide more detail tracing.
	// This is not a debug .options because it is so rarely needed that it wasn't worth it.
	public static boolean DO_VC_TRACING = false;	
	
	private EditPartListener editPartListener;
	

	/**
	 * Are children bounds relative or absolute by default.
	 * 
	 * @since 1.1.0
	 */
	protected boolean relative;
	
	/**
	 * Visual component of this parent.
	 * 
	 * @since 1.1.0
	 */
	protected IVisualComponent parentVisualComponent;
	
	/**
	 * Construct the policy with flag indicating default child bounds relativity.
	 * @param relative <code>true</code> if by default the bounds of the child are relative to this parent, or <code>false</code> if they are absolute relative to this parent.
	 *   For example if relative, the location in the bounds will be the offset of the child wrt/the origin of this figure. If absolute,
	 *   then the parent absolute location must be subtracted from the absolute location of the child to determine the offset relative
	 *   to the parent.
	 * 
	 * @since 1.1.0
	 */
	public VisualComponentsLayoutPolicy(boolean relative) {
		this.relative = relative;
	}

	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.gef.EditPolicy#activate()
	 */
	public void activate(){
		parentVisualComponent = (IVisualComponent) ((IAdaptable) getHost()).getAdapter(IVisualComponent.class);
		setEditPartListener(createEditPartListener());
		decorateChildren();
		super.activate();
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.gef.EditPolicy#deactivate()
	 */
	public void deactivate(){
		parentVisualComponent = null;
		undecorateChildren();
		setEditPartListener(null);
		super.deactivate();
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.gef.EditPolicy#getTargetEditPart(org.eclipse.gef.Request)
	 */
	public EditPart getTargetEditPart(Request request){
		return null;
	}
	
	/**
	 * Create the edit part listener. This listener handles decorating/undecorating the child.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected EditPartListener createEditPartListener(){
		return new EditPartListener.Stub(){
			public void childAdded(EditPart child, int index){
				decorateChild(child);
			}
			public void removingChild(EditPart child, int index){
				undecorateChild(child);
			}
		};
	}
	
	/**
	 * Set the edit part listener, removing the old one if there was one.
	 * @param listener
	 * 
	 * @since 1.1.0
	 */
	protected void setEditPartListener(EditPartListener listener){
		if (this.editPartListener != null)
			getHost().removeEditPartListener(this.editPartListener);
		this.editPartListener = listener;
		if (this.editPartListener != null)
			getHost().addEditPartListener(this.editPartListener);
	}
	
	/**
	 * Decorate all of the children. Called on first activate.
	 * 
	 * 
	 * @since 1.1.0
	 */
	protected void decorateChildren(){
		Iterator children = getHost().getChildren().iterator();
		while (children.hasNext())
			decorateChild((EditPart) children.next());
	}
	
	/**
	 * Decorate a child. The default adds in the constraint refresh policy.
	 * @param child
	 * 
	 * @since 1.1.0
	 */
	protected void decorateChild(EditPart child){
		child.installEditPolicy(CONSTRAINT_REFRESH_POLICY, createConstraintRefreshPolicy(child));
	}
	
	/**
	 * Create the refresh policy. It will eventually be added to the given child. This
	 * method can be overridden to provide a subclass of the policy, or to change the
	 * "relative" setting of the policy to be different than the one that the parent is
	 * set to.
	 * @param child
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected ConstraintRefreshPolicy createConstraintRefreshPolicy(EditPart child) {
		return new ConstraintRefreshPolicy(relative);
	}
	
	/**
	 * Undecorate the chilren. Called on deactivate.
	 * 
	 * 
	 * @since 1.1.0
	 */
	protected void undecorateChildren(){
		Iterator children = getHost().getChildren().iterator();
		while (children.hasNext())
			undecorateChild((EditPart) children.next());
	}
	
	/**
	 * Undecorate the child. Default removes the constraint refresh policy.
	 * @param child
	 * 
	 * @since 1.1.0
	 */
	protected void undecorateChild(EditPart child){
		child.removeEditPolicy(CONSTRAINT_REFRESH_POLICY);
	}
	
	/**
	 * The refresh editpolicy that gets added to children. This listens for bounds changes of the
	 * child and set the constraints apppropriatly.
	 * <p>
	 * <b>Note:</b> Bounds placed into the figure will always be relative to the parent. This
	 * policy needs to take the bounds that came back from the notification and handle them
	 * correctly to make sure the constraint is relative.
	 * 
	 * @since 1.1.0
	 */
	protected class ConstraintRefreshPolicy extends AbstractEditPolicy implements IVisualComponentListener {
		IVisualComponent visualComponent;
		
		/**
		 * Is this child relative or absolute compared to parent.
		 * 
		 * @since 1.1.0
		 */
		protected final boolean relative2;
		
		/**
		 * Construct the refresh policy.
		 * @param relative <code>true</code> if the child is relative to the parent, <code>false</code> if absolute.
		 * 
		 * @since 1.1.0
		 */
		public ConstraintRefreshPolicy(boolean relative) {
			relative2 = relative;
			
		}
		
		/**
		 * Get the display this policy is associated with.
		 * @return
		 * 
		 * @since 1.1.0
		 */
		protected Display getDisplay() {
			return getHost().getRoot().getViewer().getControl().getDisplay();
		}
		
		/*
		 *  (non-Javadoc)
		 * @see org.eclipse.gef.EditPolicy#activate()
		 */
		public void activate() {
			super.activate();
			// Add listener to visual component, the visual component is retrieved through the getAdapter interface from the editpart.
			// Signal an initial change so that this child gets positioned correctly.
			visualComponent = (IVisualComponent) ((IAdaptable) getHost()).getAdapter(IVisualComponent.class);
			if (visualComponent != null) {
				// Set it to (0,0,0,0) now so that selection borders don't show up until we get the true size/position in the refresh
				((GraphicalEditPart) VisualComponentsLayoutPolicy.this.getHost()).setLayoutConstraint(getHost(), ((GraphicalEditPart) getHost()).getFigure(), new Rectangle());				
				visualComponent.addComponentListener(this);
				signalRefresh();
			}
		}
		
		/*
		 *  (non-Javadoc)
		 * @see org.eclipse.gef.EditPolicy#deactivate()
		 */
		public void deactivate() {
			super.deactivate();
			if (visualComponent!= null) 
				visualComponent.removeComponentListener(this);
			visualComponent = null;
			setHost(null);
		}
		
		/*
		 *  (non-Javadoc)
		 * @see org.eclipse.gef.EditPolicy#getTargetEditPart(org.eclipse.gef.Request)
		 */
		public EditPart getTargetEditPart(Request request){
			return null;
		}
		
		/**
		 * Signal that a refresh is needed. This means something has changed, but not sure what, so 
		 * refresh the entire bounds.
		 * 
		 * 
		 * @since 1.1.0
		 */
		protected void signalRefresh() {
			// Get the current bounds and set the figure's constraint.
			if (visualComponent != null) {
				Rectangle oBounds = visualComponent.getBounds();
				if (DO_VC_TRACING)
					System.out.println("VC Component notification: "+((IJavaObjectInstance) getHost().getModel()).eClass().getName()+ " refreshed to: "+oBounds);				 //$NON-NLS-1$ //$NON-NLS-2$
				// Note: Not using EditPartRunnable because it could be that we've been deactivated but the editpart has not. Need to make the
				// test inside the run.
				CDEUtilities.displayExec(getHost(), "REFRESH", new Runnable() { //$NON-NLS-1$
					public void run() {
						EditPart ep = getHost();
						if (ep != null && ep.isActive()) {
							Rectangle oBounds = visualComponent.getBounds();
							Rectangle bounds = convertBounds(oBounds.getCopy());
							if (DO_VC_TRACING)
								System.out.println("VC Component "+((IJavaObjectInstance) getHost().getModel()).eClass().getName()+ " refreshed to (beforeConvert)"+oBounds+" (afterConvert)"+bounds); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							IFigure parent = ((GraphicalEditPart) getHost()).getFigure().getParent();
							constrain(bounds, parent, getHost());
							parent.setConstraint(((GraphicalEditPart) getHost()).getFigure(), bounds);
						}
					}
				});
			}
		}
		
		/**
		 * Convert the rect bounds. If child is relative, then no change. Else it will get the
		 * absolute loc from the parent and compute the offset from that. It will return the
		 * same rect modified.
		 * 
		 * @param rect
		 * @return
		 * 
		 * @since 1.1.0
		 */
		protected Rectangle convertBounds(Rectangle rect) {
			if (!relative2) {
				// rect is absolute, need to make relative.
				Point parentAbsolute = parentVisualComponent.getAbsoluteLocation();
				rect.setLocation(rect.x-parentAbsolute.x, rect.y-parentAbsolute.y);
			}
			return rect;
		}
		
		/*
		 *  (non-Javadoc)
		 * @see org.eclipse.ve.internal.cde.core.IVisualComponentListener#componentMoved(int, int)
		 */
		public void componentMoved(int x, int y) {
			final IFigure child = ((GraphicalEditPart) getHost()).getFigure();
			if (DO_VC_TRACING)
				System.out.println("VC Component notification: "+((IJavaObjectInstance) getHost().getModel()).eClass().getName()+ " moved to ("+x+','+y+')'); //$NON-NLS-1$ //$NON-NLS-2$
			CDEUtilities.displayExec(getHost(), "MOVED", new Runnable() { //$NON-NLS-1$
				public void run() {
					if (getHost() != null && getHost().isActive()) {
						IFigure parent = child.getParent();
						Rectangle bounds = (Rectangle) parent.getLayoutManager().getConstraint(child);
						if (bounds != null) {
							bounds = bounds.getCopy();
							// We can't use the (x,y) that came in on the move because we queued this off to the end of transaction
							// and it may of moved more than once. We would be processing only the first one if we did that.
							Point currentLoc = visualComponent.getLocation();
							if (DO_VC_TRACING)
								System.out.print("VC Component "+((IJavaObjectInstance) getHost().getModel()).eClass().getName()+ " moved to (beforeConvert)"+currentLoc);							 //$NON-NLS-1$ //$NON-NLS-2$
							bounds.setLocation(currentLoc.x, currentLoc.y);
							bounds = convertBounds(bounds);
							if (DO_VC_TRACING)
								System.out.println(" (afterConvert)"+bounds.getLocation()); //$NON-NLS-1$
							constrain(bounds, parent, getHost());
							parent.setConstraint(child, bounds);
						}
					}
				}
			});
		}
		
		/*
		 *  (non-Javadoc)
		 * @see org.eclipse.ve.internal.cde.core.IVisualComponentListener#componentResized(int, int)
		 */
		public void componentResized(int width, int height) {
			final IFigure child = ((GraphicalEditPart) getHost()).getFigure();
			CDEUtilities.displayExec(getHost(), "SIZED", new Runnable() { //$NON-NLS-1$
				public void run() {
					if (getHost() != null && getHost().isActive()) {
						IFigure parent = child.getParent();
						Rectangle bounds = (Rectangle) parent.getLayoutManager().getConstraint(child);
						if (bounds != null) {
							bounds = bounds.getCopy();
							// Can't use (width, height) sent in because there could of been more than one size change
							// during the transaction, and only the first one will be processed here.
							Dimension size = visualComponent.getSize();
							if (DO_VC_TRACING)
								System.out.println("VC Component "+((IJavaObjectInstance) getHost().getModel()).eClass().getName()+ " resized to "+size);							 //$NON-NLS-1$ //$NON-NLS-2$
							bounds.setSize(size.width, size.height);
							constrain(bounds, parent, getHost());
							parent.setConstraint(child, bounds);
						}
					}
				}
			});
		}
		
		/*
		 *  (non-Javadoc)
		 * @see org.eclipse.ve.internal.cde.core.IVisualComponentListener#componentRefreshed()
		 */
		public void componentRefreshed() {
			signalRefresh();
		}
		
		/*
		 *  (non-Javadoc)
		 * @see org.eclipse.ve.internal.cde.core.IVisualComponentListener#componentShown()
		 */
		public void componentShown() {
		}

		/*
		 *  (non-Javadoc)
		 * @see org.eclipse.ve.internal.cde.core.IVisualComponentListener#componentHidden()
		 */
		public void componentHidden() {
		}

		public void componentValidated() {
		}
		
	}

	/**
	 * Constrain the size - specialized in subclasses that want to override the figure dimensions to do things like custom clipping or making the figure large enough to be
	 * selectable even when it has a small size.  An example of this is JScrollPane where the child can sometimes be unselectable with a 0 preferred size
	 * @param bounds
	 * @param parentFigure
	 * @param childEP
	 * 
	 * @since 1.1.0
	 */
	protected void constrain(Rectangle bounds, IFigure parentFigure, EditPart childEP){
		
	}

}

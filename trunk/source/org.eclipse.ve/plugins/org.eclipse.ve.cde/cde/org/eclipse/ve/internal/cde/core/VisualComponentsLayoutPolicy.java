/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: VisualComponentsLayoutPolicy.java,v $
 *  $Revision: 1.6 $  $Date: 2004-08-27 15:35:34 $ 
 */

import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.*;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.swt.widgets.Display;
/**
 * This is an editpolicy that is placed onto a VisualComponent, and that has
 * children that are visual components. What it does is control the figure constraints
 * based upon the children visual component notifications. As the children change position and
 * size, this will receive the changes and change the child's figure constraint to match.
 *
 *
 * It will get the IVisualComponent from the children editparts by asking through the getAdapter() interface.
 * The children should return their visual component. If they need to change the visual component for some
 * reason, they need to deactivate and reactivate their edit policy with the key CONSTRAINT_REFRESH_POLICY
 * (see the declaration in this class). That will cause the visual component to be recycled.
 *
 * NOTE: It is NOT a LayoutEditPolicy. This is because it will not handle add/moving/changing/deleting
 * of children. That is the responsibility of the LayoutEditPolicy. However, this is a sub-function of
 * the LayoutEditPolicy. The layout edit policy usually handles this function too. But for visual components
 * this isn't necessary because they handle those functions themselves. So there will be a LayoutEditPolicy
 * that works in conjunction with this. Its only function will be to handle the models. When the
 * constraints are applied to the model, the model will apply them to visual component, who will then 
 * notify back to this policy that it has changed.
 */
public class VisualComponentsLayoutPolicy extends AbstractEditPolicy {
	
	// Key to use for the edit policy when it is installed on the container.
	public static final String LAYOUT_POLICY = "com.ibm.etools.visualcomponentslayoutpolicy"; //$NON-NLS-1$
	public static final String CONSTRAINT_REFRESH_POLICY = "com.ibm.etools.componentconstraintrefreshpolicy"; //$NON-NLS-1$
	
	private EditPartListener listener;
	
	public void activate(){
		setListener(createListener());
		decorateChildren();
		super.activate();
	}
	
	public void deactivate(){
		undecorateChildren();
		setListener(null);
		super.deactivate();
	}

	public EditPart getTargetEditPart(Request request){
		return null;
	}
	
	protected EditPartListener createListener(){
		return new EditPartListener.Stub(){
			public void childAdded(EditPart child, int index){
				decorateChild(child);
			}
			public void removingChild(EditPart child, int index){
				undecorateChild(child);
			}
		};
	}
	
	protected void setListener(EditPartListener listener){
		if (this.listener != null)
			getHost().removeEditPartListener(this.listener);
		this.listener = listener;
		if (this.listener != null)
			getHost().addEditPartListener(this.listener);
	}
	
	protected void decorateChildren(){
		Iterator children = getHost().getChildren().iterator();
		while (children.hasNext())
			decorateChild((EditPart) children.next());
	}
	
	protected void decorateChild(EditPart child){
		child.installEditPolicy(CONSTRAINT_REFRESH_POLICY, new ConstraintRefreshPolicy());
	}
	
	protected void undecorateChildren(){
		Iterator children = getHost().getChildren().iterator();
		while (children.hasNext())
			undecorateChild((EditPart) children.next());
	}
	
	protected void undecorateChild(EditPart child){
		child.removeEditPolicy(CONSTRAINT_REFRESH_POLICY);
	}
	
	protected class ConstraintRefreshPolicy extends AbstractEditPolicy implements IVisualComponentListener {
		IVisualComponent visualComponent;
		
		protected Display getDisplay() {
			return getHost().getRoot().getViewer().getControl().getDisplay();
		}
		
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
		
		public void deactivate() {
			super.deactivate();
			if (visualComponent!= null) 
				visualComponent.removeComponentListener(this);
			visualComponent = null;
		}
		
		public EditPart getTargetEditPart(Request request){
			return null;
		}
		
		protected void signalRefresh() {
			// Get the current bounds and set the figure's constraint.
			final Rectangle bounds = visualComponent.getBounds().getCopy();
			getDisplay().asyncExec(new Runnable() {
				/**
				 * @see java.lang.Runnable#run()
				 */
				public void run() {
					EditPart ep = getHost();
					if (ep.isActive())
						((GraphicalEditPart) VisualComponentsLayoutPolicy.this.getHost()).setLayoutConstraint(getHost(), ((GraphicalEditPart) getHost()).getFigure(), bounds);					
				}
			});
		}
		
		public void componentMoved(final int x, final int y) {
			final IFigure child = ((GraphicalEditPart) getHost()).getFigure();
			final IFigure parent = ((GraphicalEditPart) VisualComponentsLayoutPolicy.this.getHost()).getFigure();
			getDisplay().asyncExec(new Runnable() {
				/**
				 * @see java.lang.Runnable#run()
				 */
				public void run() {
					Rectangle bounds = (Rectangle) parent.getLayoutManager().getConstraint(child);
					if (bounds != null) {
						bounds = bounds.getCopy();
						bounds.x = x;
						bounds.y = y;
						constrain(bounds,parent);
						parent.setConstraint(child, bounds);				
					}
				}
			});
		}
		
		public void componentResized(final int width, final int height) {
			final IFigure child = ((GraphicalEditPart) getHost()).getFigure();
			final IFigure parent = ((GraphicalEditPart) VisualComponentsLayoutPolicy.this.getHost()).getFigure();
			getDisplay().asyncExec(new Runnable() {
				/**
				 * @see java.lang.Runnable#run()
				 */
				public void run() {
					Rectangle bounds = (Rectangle) parent.getLayoutManager().getConstraint(child);
					if (bounds != null) {
						bounds = bounds.getCopy();
						bounds.width = width;
						bounds.height = height;
						constrain(bounds,parent);
						parent.setConstraint(child, bounds);
					}
				}
			});
		}
		public void componentRefreshed() {
			signalRefresh();
		}
		
		public void componentShown() {
		}

		public void componentHidden() {
		}
		
	}

	/**
	 * Constraint the size - specialized in subclasses that want to override the figure dimensions to do things like custom clipping or making the figure large enough to be
	 * selectable even when it has a small size.  An example of this is JScrollPane where the child can sometimes be unselectable with a 0 preferred size
	 * @since 1.0.0
	 */
	protected void constrain(Rectangle bounds, IFigure parentFigure){
		
	}

}

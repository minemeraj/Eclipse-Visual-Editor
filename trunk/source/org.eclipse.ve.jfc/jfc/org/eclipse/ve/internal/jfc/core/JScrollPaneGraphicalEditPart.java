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
 * $RCSfile: JScrollPaneGraphicalEditPart.java,v $ $Revision: 1.4 $ $Date: 2004-05-24 17:56:08 $
 */
package org.eclipse.ve.internal.jfc.core;

import java.util.*;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.VisualComponentsLayoutPolicy;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;

public class JScrollPaneGraphicalEditPart extends ContainerGraphicalEditPart {

	public JScrollPaneGraphicalEditPart(Object model) {
		super(model);
	}

	private Adapter containerAdapter = new EditPartAdapterRunnable() {
		public void run() {
			if (isActive())
				refreshChildren();
		}
		
		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sf_scrollpaneViewportView)
				queueExec(JScrollPaneGraphicalEditPart.this);
		}
	};

	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(containerAdapter);
	}

	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(containerAdapter);
	}

	protected EStructuralFeature sf_scrollpaneViewportView;

	/**
	 * The layoutEditPolicy deals with the fact that the child is a viewportView
	 */
	protected void createLayoutEditPolicy() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new JScrollPaneLayoutEditPolicy(EditDomain.getEditDomain(this)));
	}
	
	/**
	 * JScrollPane hosts JTable which has special behavior - with autoCreateColumnsFromModel=false and no columns
	 * its height is 0 which means it cannot be selected, and also its height can exceed the table.  This creates a GEF figure
	 * that is either not selectable or else looks too deep
	 * Override this behavior with a special layout policy
	 */
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(VisualComponentsLayoutPolicy.LAYOUT_POLICY, new VisualComponentsLayoutPolicy(){			
			protected void constrain(Rectangle bounds, IFigure parentFigure) {
				//TODO Condition this occuring for JTable children only
				// If the bounds height is 0 or larger than us (we are the parent) then
				// make the height of the child be us
				if(bounds.height == 0 || bounds.height + bounds.y > parentFigure.getSize().height){
					bounds.height = parentFigure.getSize().height - 1 - bounds.y; //Reduce by -1 to make the bottom edge align better
				}
			}
		}); 
	}

	public List getModelChildren() {
		ArrayList result = new ArrayList(1);
		Object viewportView = ((EObject) getModel()).eGet(sf_scrollpaneViewportView);
		if (viewportView != null) {
			result.add(viewportView);
			return result;
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	protected EditPart createChild(Object model) {
		// We need to set the relative parent of the viewportView so it creates its location for the
		// the graph view editPart relative to us as its parent instead of some internal parent container
		IComponentProxyHost jScrollPaneProxyAdapter = (IComponentProxyHost) BeanProxyUtilities.getBeanProxyHost((IJavaInstance) getModel());
		IComponentProxyHost viewportViewProxyAdapter = (IComponentProxyHost) BeanProxyUtilities.getBeanProxyHost((IJavaInstance) model);
		if (viewportViewProxyAdapter != null) {
			viewportViewProxyAdapter.setParentComponentProxyHost(jScrollPaneProxyAdapter);
		}
		// Return the edit part
		return super.createChild(model);
	}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);

		sf_scrollpaneViewportView = JavaInstantiation.getSFeature((IJavaObjectInstance) model, JFCConstants.SF_JSCROLLPANE_VIEWPORTVIEW);
	}

	/**
	 * @see org.eclipse.ve.internal.jfc.core.ContainerGraphicalEditPart#setPropertySource(ComponentGraphicalEditPart, EObject)
	 */
	protected void setPropertySource(ComponentGraphicalEditPart childEP, EObject child) {
		childEP.setPropertySource(new NonBoundsBeanPropertySource(child));
	}

}
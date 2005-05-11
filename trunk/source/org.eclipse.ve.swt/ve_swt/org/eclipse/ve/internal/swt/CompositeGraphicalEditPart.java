/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: CompositeGraphicalEditPart.java,v $ $Revision: 1.19 $ $Date: 2005-05-11 22:41:37 $
 */

package org.eclipse.ve.internal.swt;

import java.util.List;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.VisualComponentsLayoutPolicy;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.visual.ILayoutPolicyFactory;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

/**
 * ViewObject for the awt Container. Creation date: (2/16/00 3:45:46 PM) @author: Joe Winchester
 */
public class CompositeGraphicalEditPart extends ControlGraphicalEditPart {

	private EReference sf_compositeLayout, sf_compositeControls;

	public CompositeGraphicalEditPart(Object model) {
		super(model);
	}

	protected VisualContainerPolicy getContainerPolicy() {
		return new CompositeContainerPolicy(EditDomain.getEditDomain(this)); // SWT standard Composite/Container Edit Policy
	}

	protected IFigure createFigure() {
		ContentPaneFigure cf = (ContentPaneFigure) super.createFigure();
		cf.getContentPane().setLayoutManager(new XYLayout());
		return cf;
	}

	protected void createEditPolicies() {
		super.createEditPolicies();
		// Allow dropping of implicit controls such as JFace or other places where the control is created by a non-visual delgate
		// This must be done before the layout edit policy because the implicit parent can be created by the implicit edit policy 
//		createImplicitEditPolicy();
		
		installEditPolicy(VisualComponentsLayoutPolicy.LAYOUT_POLICY, new VisualComponentsLayoutPolicy(true)); 
		// This is a special policy that just
		// handles the size/position of visual
		// components wrt/the figures. It does not
		// handle changing size/position.
		createLayoutEditPolicy();
	}
	
	protected void createImplicitEditPolicy(){
		EditPolicy implicitEditPolicy = new ImplicitEditPolicy(EditDomain.getEditDomain(this),this.getBean());
		installEditPolicy("IMPLICIT_CONTROL",implicitEditPolicy); //$NON-NLS-1$
	}

	protected EditPart createChild(Object model) {
		EditPart ep = super.createChild(model);
		if (ep instanceof ControlGraphicalEditPart) {
			((ControlGraphicalEditPart) ep).setTransparent(true); // So that it doesn't create an image, we subsume it here.
		}
		return ep;
	}

	/**
	 * Because org.eclipse.swt.widgets.Composite can vary its layout manager we need to use the correct layout input policy for the layout manager
	 * that is calculated by a factory
	 */
	protected void createLayoutEditPolicy() {

		EditPolicy layoutPolicy = null;
		CompositeProxyAdapter compositeBeanProxyAdapter = (CompositeProxyAdapter) BeanProxyUtilities.getBeanProxyHost((IJavaInstance) getModel());
		if(compositeBeanProxyAdapter.getBeanProxy() == null) return;
		// See the layout of the composite to determine the edit policy
		IBeanProxy layoutBeanProxy = BeanSWTUtilities.invoke_getLayout(compositeBeanProxyAdapter.getBeanProxy());
		// If the layoutBeanProxy is null then we use the null layout edit policy
		if (layoutBeanProxy == null) {
			layoutPolicy = new NullLayoutEditPolicy(getContainerPolicy(), compositeBeanProxyAdapter.getClientBox());
		} else {
			// Get the layoutPolicyFactory
			ILayoutPolicyFactory layoutPolicyFactory = BeanSWTUtilities.getLayoutPolicyFactory(compositeBeanProxyAdapter.getBeanProxy(), EditDomain
					.getEditDomain(this));
			layoutPolicy = layoutPolicyFactory.getLayoutEditPolicy(getContainerPolicy());
		}
		if (layoutPolicy == null)
			layoutPolicy = new DefaultLayoutEditPolicy(getContainerPolicy());

		removeEditPolicy(EditPolicy.LAYOUT_ROLE); // Get rid of old one, if any
		//	Layout policies put figure decorations for things like grids so we should remove this
		installEditPolicy(EditPolicy.LAYOUT_ROLE, layoutPolicy);
	}

	protected List getModelChildren() {
		return (List) ((EObject) getModel()).eGet(sf_compositeControls);
	}

	/**
	 * When the controls relationship is updated refresh the children, and when the layout property is updated recalculate the edit policy for the
	 * specific layout
	 */
	private Adapter containerAdapter = new EditPartAdapterRunnable() {
		public void run() {
			if (isActive())
				refreshChildren();
		}

		public void notifyChanged(Notification notification) {
			if (notification.getFeature() == sf_compositeControls)
				queueExec(CompositeGraphicalEditPart.this, "CONTROLS");
			else if (notification.getFeature() == sf_compositeLayout) {
				queueExec(CompositeGraphicalEditPart.this, "LAYOUT", new Runnable() {
					public void run() {
						if (isActive())
							createLayoutEditPolicy();
					}
				});
			}
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

	public void setModel(Object model) {
		super.setModel(model);
		IJavaObjectInstance javaModel = (IJavaObjectInstance) model;
		if (javaModel.eResource() != null && javaModel.eResource().getResourceSet() != null) {
			ResourceSet rset = ((IJavaObjectInstance) model).eResource().getResourceSet();
			sf_compositeLayout = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_LAYOUT);
			sf_compositeControls = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_CONTROLS);
		}
	}

}

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
 * $RCSfile: ContainerTreeEditPart.java,v $ $Revision: 1.6 $ $Date: 2004-03-26 23:07:38 $
 */

package org.eclipse.ve.internal.jfc.core;

import java.util.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;
import org.eclipse.ve.internal.java.visual.*;

/**
 * TreeEditPart for an awt Container.
 */
public class ContainerTreeEditPart extends ComponentTreeEditPart {

	public ContainerTreeEditPart(Object model) {
		super(model);
	}

	protected TreeVisualContainerEditPolicy treeContainerPolicy;

	protected VisualContainerPolicy getContainerPolicy() {
		return new ContainerPolicy(EditDomain.getEditDomain(this)); // AWT standard Contained Edit Policy
	}

	protected List getChildJavaBeans() {
		// Model children is the components feature.
		// However, this returns the constraint components, but we want to return instead
		// the components themselves. They are the "model" that gets sent to the createChild and
		// component edit part.
		List constraintChildren = (List) ((EObject) getModel()).eGet(sf_containerComponents);
		ArrayList children = new ArrayList(constraintChildren.size());
		Iterator itr = constraintChildren.iterator();
		while (itr.hasNext()) {
			EObject con = (EObject) itr.next();
			Object child = con.eGet(sf_constraintComponent);
			if (child != null) children.add(child); // Get the component out of the constraint

		}
		return children;
	}

	protected Adapter containerAdapter = new EditPartAdapterRunnable() {
		public void run() {
			if (isActive())
				refreshChildren();
		}

		public void notifyChanged(Notification notification) {
			if (notification.getFeature() == sf_containerComponents)
				queueExec(ContainerTreeEditPart.this);
			else if (notification.getFeature() == sf_containerLayout) {
				queueExec(ContainerTreeEditPart.this, new Runnable() {
					public void run() {
						if (isActive())
							createLayoutPolicyHelper();
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

	private EReference sf_containerLayout, sf_constraintComponent, sf_containerComponents;

	protected EditPart createChildEditPart(Object model) {
		EditPart ep = super.createChildEditPart(model);
		if (ep instanceof ComponentTreeEditPart) setPropertySource((EObject) model, (ComponentTreeEditPart) ep);
		return ep;
	}

	protected void setPropertySource(EObject child, ComponentTreeEditPart childEP) {
		childEP.setPropertySource((IPropertySource) EcoreUtil.getRegisteredAdapter(InverseMaintenanceAdapter.getFirstReferencedBy(child,
				sf_constraintComponent), IPropertySource.class)); // This is the property source of the actual model which is part of the
																  // constraintComponent.
	}

	protected void createEditPolicies() {
		super.createEditPolicies();
		treeContainerPolicy = new TreeVisualContainerEditPolicy(getContainerPolicy());
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, treeContainerPolicy);
		createLayoutPolicyHelper();
	}

	protected void createLayoutPolicyHelper() {
		if (treeContainerPolicy != null) {
			// Get the layout policy helper class from the layout policy factory and
			// set it in the container helper policy.
			IJavaInstance container = (IJavaInstance) getModel();
			// It is possible the live JavaBean failed to create
			ILayoutPolicyHelper lpHelper = null;
			if (BeanProxyUtilities.getBeanProxyHost(container).getErrorStatus() != IBeanProxyHost.ERROR_SEVERE) {
				IBeanProxy containerProxy = BeanProxyUtilities.getBeanProxy((IJavaInstance) getModel());
				if (containerProxy != null) {
					ILayoutPolicyFactory lpFactory = BeanAwtUtilities.getLayoutPolicyFactory(containerProxy, EditDomain.getEditDomain(this));
					lpHelper = lpFactory.getLayoutPolicyHelper(null);
				}
			}

			if (lpHelper == null) lpHelper = new UnknownLayoutPolicyHelper();

			treeContainerPolicy.setPolicyHelper(lpHelper);
		}
	}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);

		ResourceSet rset = ((IJavaObjectInstance) model).eResource().getResourceSet();
		sf_containerLayout = JavaInstantiation.getReference(rset, JFCConstants.SF_CONTAINER_LAYOUT);
		sf_constraintComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_CONSTRAINT_COMPONENT);
		sf_containerComponents = JavaInstantiation.getReference(rset, JFCConstants.SF_CONTAINER_COMPONENTS);
	}

}

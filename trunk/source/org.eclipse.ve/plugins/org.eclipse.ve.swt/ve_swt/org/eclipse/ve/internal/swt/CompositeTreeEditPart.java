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
/*
 * $RCSfile: CompositeTreeEditPart.java,v $ $Revision: 1.6 $ $Date: 2004-08-27 15:35:50 $
 */

package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPolicy;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.visual.*;

/**
 * TreeEditPart for a SWT Container.
 */
public class CompositeTreeEditPart extends ControlTreeEditPart {

	private EReference sf_compositeLayout, sf_compositeControls;

	public CompositeTreeEditPart(Object model) {
		super(model);
	}

	protected TreeVisualContainerEditPolicy treeContainerPolicy;

	protected VisualContainerPolicy getContainerPolicy() {
		return new CompositeContainerPolicy(EditDomain.getEditDomain(this));
	}

	protected List getChildJavaBeans() {
		return (List) ((EObject) getModel()).eGet(sf_compositeControls);
	}

	protected Adapter compositeAdapter = new EditPartAdapterRunnable() {
		public void run() {
			if (isActive())
				refreshChildren();
		}

		public void notifyChanged(Notification notification) {
			if (notification.getFeature() == sf_compositeControls)
				queueExec(CompositeTreeEditPart.this);
			else if (notification.getFeature() == sf_compositeLayout) {
				queueExec(CompositeTreeEditPart.this, new Runnable() {
					public void run() {
						if (isActive())
							createLayoutPolicyHelper();
					}
				});
			}
		}

		public Notifier getTarget() {
			return null;
		}

		public void setTarget(Notifier newTarget) {
		}

		public boolean isAdapterForType(Object type) {
			return false;
		}
	};

	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(compositeAdapter);
	}

	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(compositeAdapter);
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
				CompositeProxyAdapter compositeProxyAdapter = (CompositeProxyAdapter) BeanProxyUtilities.getBeanProxyHost((IJavaInstance) getModel());
				// Get the type of the layout proxy from the composite
				IBeanProxy layoutProxy = BeanSWTUtilities.invoke_getLayout(compositeProxyAdapter.getBeanProxy());
				if (layoutProxy != null) {
					ILayoutPolicyFactory lpFactory = VisualUtilities.getLayoutPolicyFactory(layoutProxy.getTypeProxy(), EditDomain
							.getEditDomain(this));
					if (lpFactory != null)
						lpHelper = lpFactory.getLayoutPolicyHelper(null);
				} else {
					lpHelper = new NullLayoutPolicyHelper(getContainerPolicy());
				}
			}

			if (lpHelper == null)
				lpHelper = new UnknownLayoutPolicyHelper(getContainerPolicy());

			treeContainerPolicy.setPolicyHelper(lpHelper);
		}
	}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);

		ResourceSet rset = ((IJavaObjectInstance) model).eResource().getResourceSet();
		sf_compositeLayout = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_LAYOUT);
		sf_compositeControls = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_CONTROLS);
	}

}

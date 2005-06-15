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
/*
 * $RCSfile: CompositeTreeEditPart.java,v $ $Revision: 1.11 $ $Date: 2005-06-15 20:19:21 $
 */

package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.EditPartRunnable;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;
import org.eclipse.ve.internal.java.visual.*;

import org.eclipse.ve.internal.swt.CompositeProxyAdapter.ControlLayoutDataAdapter;

/**
 * TreeEditPart for a SWT Container.
 * 
 * @since 1.1.0
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

	protected Adapter compositeAdapter = new EditPartAdapterRunnable(this) {
		protected void doRun() {
			refreshChildren();
		}

		public void notifyChanged(Notification notification) {
			if (notification.getFeature() == sf_compositeControls)
				queueExec(CompositeTreeEditPart.this, "CONTROLS"); //$NON-NLS-1$
			else if (notification.getFeature() == sf_compositeLayout) {
				queueExec(CompositeTreeEditPart.this, "LAYOUT", new EditPartRunnable(getHost()) { //$NON-NLS-1$
					protected void doRun() {
						createLayoutPolicyHelper();
					}
				});
			}
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

	protected CompositeProxyAdapter getCompositeProxyAdapter() {
		return (CompositeProxyAdapter) EcoreUtil.getExistingAdapter((Notifier) getModel(), IBeanProxyHost.BEAN_PROXY_TYPE);
	}

	protected EditPart createChildEditPart(Object model) {
		EditPart ep = super.createChildEditPart(model);
		if (ep instanceof ControlTreeEditPart)
			setupControl((ControlTreeEditPart) ep, (EObject) model);
		return ep;
	}

	protected void setupControl(ControlTreeEditPart childEP, EObject child) {
		ControlLayoutDataAdapter controlLayoutDataAdapter = getCompositeProxyAdapter().getControlLayoutDataAdapter(child);
		// Could be null due to instantiation error.
		if (controlLayoutDataAdapter != null)
			childEP.setErrorNotifier(controlLayoutDataAdapter.getErrorNotifier());
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
			if (BeanProxyUtilities.getBeanProxyHost(container).isBeanProxyInstantiated()) {
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

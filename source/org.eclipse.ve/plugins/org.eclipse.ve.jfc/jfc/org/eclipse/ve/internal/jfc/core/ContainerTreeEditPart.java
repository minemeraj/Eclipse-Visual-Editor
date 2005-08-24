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
 * $RCSfile: ContainerTreeEditPart.java,v $ $Revision: 1.17 $ $Date: 2005-08-24 23:38:10 $
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

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.*;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
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

	protected Adapter containerAdapter = new EditPartAdapterRunnable(this) {
		protected void doRun() {
			refreshChildren();
			// Now we need to run through the children and set the Property source correctly.
			// This is needed because the child could of been removed and then added back in with
			// a different ConstraintComponent BEFORE the refresh could happen. In that case GEF
			// doesn't see the child as being different so it doesn't create a new child editpart, and
			// so we don't get the new property source that we should. We didn't keep a record of which
			// one changed, so we just touch them all.
			List children = getChildren();
			int s = children.size();
			for (int i = 0; i < s; i++) {
				EditPart ep = (EditPart) children.get(i);
				if (ep instanceof ComponentTreeEditPart) 
					setupComponent((ComponentTreeEditPart) ep, (EObject) ep.getModel());
			}				
		}

		public void notifyChanged(Notification notification) {
			if (notification.getFeature() == sf_containerComponents)
				queueExec(ContainerTreeEditPart.this, "COMPONENTS"); //$NON-NLS-1$
			else if (notification.getFeature() == sf_containerLayout) {
				queueExec(ContainerTreeEditPart.this, "LAYOUT", new EditPartRunnable(getHost()) { //$NON-NLS-1$
					protected void doRun() {
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
		if (ep instanceof ComponentTreeEditPart)
			setupComponent((ComponentTreeEditPart) ep, (EObject) model);
		return ep;
	}

	protected void setupComponent(ComponentTreeEditPart childEP, EObject child) {
		EObject componentConstraintObject = InverseMaintenanceAdapter.getIntermediateReference((EObject) getModel(), sf_containerComponents, sf_constraintComponent, child);
		if (componentConstraintObject != null) {
			childEP.setPropertySource((IPropertySource) EcoreUtil.getRegisteredAdapter(componentConstraintObject, IPropertySource.class)); // This is the property source of the actual model which is part of the constraintComponent.
			childEP.setErrorNotifier((IErrorNotifier) EcoreUtil.getExistingAdapter(componentConstraintObject, IErrorNotifier.ERROR_NOTIFIER_TYPE));
		} else {
			childEP.setPropertySource(null);	// No CC.
			childEP.setErrorNotifier(null);
		}
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
			IBeanProxy containerProxy = BeanProxyUtilities.getBeanProxy(container);
			if (containerProxy != null) {
				// Get the layout policy helper for the correct layout manager
				IJavaInstance layoutManager =  (IJavaInstance) container.eGet(sf_containerLayout);
				if(layoutManager != null){
					lpHelper = BeanAwtUtilities.getLayoutPolicyFactoryFromLayoutManager(layoutManager,EditDomain.getEditDomain(this)).getLayoutPolicyHelper(null);
				} else {
					IBeanProxy layoutManagerProxy = BeanAwtUtilities.invoke_getLayout(containerProxy);
					if (containerProxy != null) {
						ILayoutPolicyFactory lpFactory = BeanAwtUtilities.getLayoutPolicyFactoryFromLayoutManger(layoutManagerProxy, EditDomain.getEditDomain(this));
						lpHelper = lpFactory.getLayoutPolicyHelper(null);
					}
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

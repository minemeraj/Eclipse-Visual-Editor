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
 * $RCSfile: JSplitPaneTreeEditPart.java,v $ $Revision: 1.10 $ $Date: 2005-12-09 15:25:46 $
 */
package org.eclipse.ve.internal.jfc.core;

import java.util.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;

/**
 * Tree edit part for handling JSplitPane in the Beans viewer
 */
public class JSplitPaneTreeEditPart extends ComponentTreeEditPart {
	private Adapter containerAdapter = new EditPartAdapterRunnable(this) {
		protected void doRun() {
			refreshChildren();
		}

		public void notifyChanged(Notification msg) {
			Object feature = msg.getFeature();
			if (feature == sfLeftComponent || feature == sfRightComponent || feature == sfTopComponent || feature == sfBottomComponent || feature == sf_containerComponents)
				queueExec(JSplitPaneTreeEditPart.this, "SPLITCOMPONENT"); //$NON-NLS-1$
		}
	};

	protected EStructuralFeature sfLeftComponent, sfRightComponent, sfTopComponent, sfBottomComponent, sfOrientation, sf_constraintComponent,
			sf_containerComponents;

	public JSplitPaneTreeEditPart(Object model) {
		super(model);
	}

	protected void createEditPolicies() {
		// The TreeContainerEditPolicy is the CDE one
		// We don't care about being a Container with components
		// We are just interested in showing the two children...left and right components, or top and bottom components
		super.createEditPolicies();
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new org.eclipse.ve.internal.cde.core.TreeContainerEditPolicy(new JSplitPaneContainerPolicy(
				EditDomain.getEditDomain(this))));
	}

	protected EditPart createChildEditPart(Object model) {
		EditPart ep = super.createChildEditPart(model);
		((ComponentTreeEditPart) ep).setPropertySource(new NonBoundsBeanPropertySource((EObject) model));
		((ComponentTreeEditPart) ep).setLabelDecorator(new JSplitPaneChildTreeLabelDecorator());
		return ep;
	}

	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(containerAdapter);
	}

	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(containerAdapter);
	}

	/**
	 * JSplitPane's children are really set as the left or top component and the right or bottom component. If the JSplitPane's orientation is
	 * HORIZONTAL_SPLIT, the components will be set left and right. If the JSplitPane's orientation is VERTICAL_SPLIT, the components will be set top
	 * and bottom. We'll check for both since we don't know the orientation... and even if we did, Sun lets you set the components regardless of what
	 * the orientation is set to. In other words, you can set the left and right components even though the orientation is vertical... and the
	 * opposite is true as well.
	 */
	public List getChildJavaBeans() {
		ArrayList result = new ArrayList();
		Object leftComponent = ((EObject) getModel()).eGet(sfLeftComponent);
		Object topComponent = ((EObject) getModel()).eGet(sfTopComponent);
		Object rightComponent = ((EObject) getModel()).eGet(sfRightComponent);
		Object bottomComponent = ((EObject) getModel()).eGet(sfBottomComponent);
		if (leftComponent != null) {
			result.add(leftComponent);
		} else if (topComponent != null) {
			result.add(topComponent);
		}
		if (rightComponent != null) {
			result.add(rightComponent);
		} else if (bottomComponent != null) {
			result.add(bottomComponent);
		}
		List componentList = getConstraintComponentsModelChildren();
		if (!componentList.isEmpty()) {
			for (int i = 0; i < componentList.size(); i++) {
				result.add(componentList.get(i));
			}
		}
		return result;
	}

	/**
	 * Sun let's you set the children in JSplitPane with just an add(aComponent, "left"), etc., so we have to handle the cases whereas the components
	 * of the JSplitPane are constraint components.
	 */
	protected List getConstraintComponentsModelChildren() {
		// Model children is the components feature.
		// However, this returns the constraint components, but we want to return instead
		// the components themselves. They are the "model" that gets sent to the createChild and
		// component edit part.
		List constraintChildren = (List) ((EObject) getModel()).eGet(sf_containerComponents);
		ArrayList children = new ArrayList(constraintChildren.size());
		Iterator itr = constraintChildren.iterator();
		while (itr.hasNext()) {
			EObject con = (EObject) itr.next();
			IJavaInstance component = (IJavaInstance) con.eGet(sf_constraintComponent);
			// See whether the component is in severe error. If so then exlude if from the list of children
			if (BeanProxyUtilities.getBeanProxyHost(component).isBeanProxyInstantiated()) {
				children.add(con.eGet(sf_constraintComponent)); // Get the component out of the constraint
			}
		}
		return children;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#reorderChild(org.eclipse.gef.EditPart, int)
	 */
	protected void reorderChild(EditPart editpart, int index) {
		super.reorderChild(editpart, index);
		Iterator itr = children.iterator();
		List modelChildren = getModelChildren();
		while (itr.hasNext()) {
			Object child = itr.next();
			if (child instanceof ComponentTreeEditPart && modelChildren.contains(((ComponentTreeEditPart) child).getModel()))
				((ComponentTreeEditPart) child).refresh();
		}
	}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		ResourceSet rset = ((EObject) model).eResource().getResourceSet();
		sfLeftComponent = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_LEFTCOMPONENT);
		sfRightComponent = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_RIGHTCOMPONENT);
		sfBottomComponent = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_BOTTOMCOMPONENT);
		sfTopComponent = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_TOPCOMPONENT);
		sfOrientation = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_ORIENTATION);
		sf_constraintComponent = JavaInstantiation.getSFeature(rset, JFCConstants.SF_CONSTRAINT_COMPONENT);
		sf_containerComponents = JavaInstantiation.getSFeature(rset, JFCConstants.SF_CONTAINER_COMPONENTS);
	}
}

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
 * $RCSfile: JTabbedPaneTreeEditPart.java,v $ $Revision: 1.3 $ $Date: 2004-03-26 23:07:38 $
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

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

/**
 * Tree edit part for handling JTabbedPane in the Outline viewer
 * 
 * Since we can't really select the individual tabs on the JTabbedPane from the Graphviewer, we depend on the tree editpart for selecting the pages
 * or components on those pages.
 * 
 * When a page (tree editpart.. child of JTabbePane) is selected, the corresponding JTabbedPaneGraphicalEditPart is selected and it notifies the
 * JTabbedPanedProxyAdapter to bring the page to the front.
 * 
 * When this occurs, the previously graph viewed page figures are hidden and the currently selected page is made visible. If we didn't hide the
 * editparts from the other pages, the figure rectangle outlines will "bleed" through and show on the top page.
 */
public class JTabbedPaneTreeEditPart extends ComponentTreeEditPart {
	protected EReference sfTabs, sfComponent;

	public JTabbedPaneTreeEditPart(Object model) {
		super(model);
	}

	private Adapter containerAdapter = new EditPartAdapterRunnable() {
		public void run() {
			if (isActive())
				refreshChildren();
		}

		public void notifyChanged(Notification notification) {
			if (notification.getFeature() == sfTabs)
				queueExec(JTabbedPaneTreeEditPart.this);
		}
	};

	public void activate() {
		((EObject) getModel()).eAdapters().add(containerAdapter);
		super.activate();
	}

	public void deactivate() {
		((EObject) getModel()).eAdapters().remove(containerAdapter);
		super.deactivate();
	}

	protected EditPart createChildEditPart(Object model) {
		EditPart ep = super.createChildEditPart(model);
		((ComponentTreeEditPart) ep).setPropertySource((IPropertySource) EcoreUtil.getRegisteredAdapter(InverseMaintenanceAdapter
				.getFirstReferencedBy((EObject) model, sfComponent), IPropertySource.class)); // This is the property source of the actual model
																							  // which is part of the constraintComponent.
		// keep the following for the future so we can show the tab title in the beans viewer.
		((ComponentTreeEditPart) ep).setLabelDecorator(new JTabbedPaneChildTreeLabelDecorator());
		return ep;
	}

	protected void createEditPolicies() {
		// The TreeContainerEditPolicy is the CDE one
		// We don't care about being a Container with components
		// We are just interested in showing the tabs (or pages) as children
		super.createEditPolicies();
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new org.eclipse.ve.internal.cde.core.TreeContainerEditPolicy(
				new JTabbedPaneContainerPolicy(EditDomain.getEditDomain(this))));
	}

	protected List getChildJavaBeans() {
		List jtabComponents = (List) ((EObject) getModel()).eGet(sfTabs);
		ArrayList children = new ArrayList(jtabComponents.size());
		Iterator itr = jtabComponents.iterator();
		while (itr.hasNext()) {
			EObject component = (EObject) itr.next();
			children.add(component.eGet(sfComponent));
		}
		return children;
	}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		ResourceSet rset = ((EObject) model).eResource().getResourceSet();
		sfTabs = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABBEDPANE_TABS);
		sfComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABCOMPONENT_COMPONENT);
	}
}
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
/*
 * $RCSfile: JTabbedPaneTreeEditPart.java,v $ $Revision: 1.12 $ $Date: 2005-06-15 20:19:27 $
 */
package org.eclipse.ve.internal.jfc.core;

import java.util.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.*;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IErrorNotifier;
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

	private Adapter containerAdapter = new EditPartAdapterRunnable(this) {
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
				try {
					setupComponent((ComponentTreeEditPart) ep, (EObject) ep.getModel());
				} catch (ClassCastException e) {
					// For the rare case it is not a component tree edit part, such as undefined class.
				}
			}
		}

		public void notifyChanged(Notification notification) {
			if (notification.getFeature() == sfTabs)
				queueExec(JTabbedPaneTreeEditPart.this, "TABS"); //$NON-NLS-1$
		}
	};

	public void activate() {
		((EObject) getModel()).eAdapters().add(containerAdapter);
		// We need add a listener to dispose of the special decorator used for the
		// JTabbedPane's children when the child is removed.
		addEditPartListener(new EditPartListener.Stub() {

			public void removingChild(EditPart editpart, int index) {
				disposeLabelDecorator(editpart);
			}
		});
		super.activate();
	}

	public void deactivate() {
		((EObject) getModel()).eAdapters().remove(containerAdapter);
		// Need to remove the label decorator from each of the children
		for (Iterator iter = getChildren().iterator(); iter.hasNext();) {
			disposeLabelDecorator((EditPart) iter.next());
		}
		super.deactivate();
	}

	protected EditPart createChildEditPart(Object model) {
		EditPart ep = super.createChildEditPart(model);
		try {
			ComponentTreeEditPart componentTreeEditPart = (ComponentTreeEditPart) ep;
			setupComponent(componentTreeEditPart, (EObject) model);
		} catch (ClassCastException e) {
			// For rare case not component tree editpart, such as undefined class.
		}
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
	
	protected void setupComponent(ComponentTreeEditPart childEP, EObject child) {
		EObject tab = InverseMaintenanceAdapter.getIntermediateReference((EObject) getModel(), sfTabs, sfComponent, child);
		ILabelDecorator oldLabelDecorator = childEP.getLabelDecorator();
		if (oldLabelDecorator != null) {
			// Get rid of the old one. The only reason there would be one is if the child control was reordered. In that case it
			// would be the same child, but a different tab. So we would need to get rid of the old label decorator.
			childEP.setLabelDecorator(null);
			oldLabelDecorator.dispose();
		}
		if (tab != null) {
			childEP.setPropertySource((IPropertySource) EcoreUtil.getRegisteredAdapter(tab, IPropertySource.class)); // This is the property source of the actual model which is part of the tab.
			childEP.setErrorNotifier((IErrorNotifier) EcoreUtil.getExistingAdapter(tab, IErrorNotifier.ERROR_NOTIFIER_TYPE));
			childEP.setLabelDecorator(new JTabbedPaneChildTreeLabelDecorator(tab));

		} else {
			childEP.setPropertySource(null);	// No tab.
			childEP.setErrorNotifier(null);
			childEP.setLabelDecorator(null);
		}
	}

	/**
	 * @param editpart
	 * 
	 * @since 1.0.0
	 */
	protected void disposeLabelDecorator(EditPart editpart) {
		if (editpart instanceof ComponentTreeEditPart) {
			ILabelDecorator labelDecorator2 = ((ComponentTreeEditPart)editpart).getLabelDecorator();
			if (labelDecorator2 != null) {
				((ComponentTreeEditPart)editpart).setLabelDecorator(null);
				labelDecorator2.dispose();
			}
		}
	}	
}

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TabFolderTreeEditPart.java,v $
 *  $Revision: 1.3 $  $Date: 2004-08-20 20:53:28 $ 
 */
package org.eclipse.ve.internal.swt;

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
 * 
 * @since 1.0.0
 */
public class TabFolderTreeEditPart extends CompositeTreeEditPart {

	private EReference sf_items, sf_tabItemControl;

	public TabFolderTreeEditPart(Object model) {
		super(model);
	}

	public void deactivate() {
		((EObject) getModel()).eAdapters().remove(containerAdapter);
		super.deactivate();
	}

	private Adapter containerAdapter = new EditPartAdapterRunnable() {

		public void run() {
			if (isActive()) {
				refreshChildren();
				List children = getChildren();
				int s = children.size();
				for (int i = 0; i < s; i++) {
					EditPart ep = (EditPart) children.get(i);
					if (ep instanceof ControlTreeEditPart)
						setPropertySource((ControlTreeEditPart) ep, (EObject) ep.getModel());
				}
			}
		}

		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sf_items)
				queueExec(TabFolderTreeEditPart.this);
		}
	};

	public void activate() {
		((EObject) getModel()).eAdapters().add(containerAdapter);
		super.activate();
	}

	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new org.eclipse.ve.internal.cde.core.TreeContainerEditPolicy(new TabFolderContainerPolicy(
				EditDomain.getEditDomain(this))));
	}

	protected EditPart createChildEditPart(Object model) {
		EditPart ep = super.createChildEditPart(model);
		setPropertySource((ControlTreeEditPart) ep, (EObject) model);
		return ep;
	}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		ResourceSet rset = ((EObject) model).eResource().getResourceSet();
		sf_items = JavaInstantiation.getReference(rset, SWTConstants.SF_TABFOLDER_ITEMS);
		sf_tabItemControl = JavaInstantiation.getReference(rset, SWTConstants.SF_TABITEM_CONTROL);
	}

	/*
	 * Model children is the items feature. However, this returns the TabItems, but we want to return instead the controls themselves. They
	 * are the "model" that gets sent to the createChild and control edit part.
	 */
	protected List getChildJavaBeans() {
		List tabitems = (List) ((EObject) getModel()).eGet(sf_items);
		ArrayList children = new ArrayList(tabitems.size());
		Iterator itr = tabitems.iterator();
		while (itr.hasNext()) {
			EObject tabitem = (EObject) itr.next();
			// Get the component out of the JTabComponent
			children.add(tabitem.eGet(sf_tabItemControl));
		}
		return children;
	}

	protected void setPropertySource(ControlTreeEditPart childEP, EObject child) {
		EObject tab = InverseMaintenanceAdapter.getIntermediateReference((EObject) getModel(), sf_items, sf_tabItemControl, child);
		// This is the property source of the actual child, which is the tabitem.
		if (tab != null)
			childEP.setPropertySource((IPropertySource) EcoreUtil.getRegisteredAdapter(tab, IPropertySource.class));
		else
			childEP.setPropertySource(null);
	}

}
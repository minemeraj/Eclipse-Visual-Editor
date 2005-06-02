/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CTabFolderTreeEditPart.java,v $
 *  $Revision: 1.2 $  $Date: 2005-06-02 22:32:30 $ 
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
public class CTabFolderTreeEditPart extends CompositeTreeEditPart {

	private EReference sf_items, sf_cTabItemControl;

	public CTabFolderTreeEditPart(Object model) {
		super(model);
	}

	public void deactivate() {
		((EObject) getModel()).eAdapters().remove(containerAdapter);
		super.deactivate();
	}

	private Adapter containerAdapter = new EditPartAdapterRunnable(this) {

		protected void doRun() {
			refreshChildren();
			List children = getChildren();
			int s = children.size();
			for (int i = 0; i < s; i++) {
				EditPart ep = (EditPart) children.get(i);
				if (ep instanceof ControlTreeEditPart)
					setPropertySource((ControlTreeEditPart) ep, (EObject) ep.getModel());
			}
		}

		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sf_items)
				queueExec(CTabFolderTreeEditPart.this);
		}
	};

	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(containerAdapter);
	}

	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new org.eclipse.ve.internal.cde.core.TreeContainerEditPolicy(new CTabFolderContainerPolicy(
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
		sf_items = JavaInstantiation.getReference(rset, SWTConstants.SF_CTABFOLDER_ITEMS);
		sf_cTabItemControl = JavaInstantiation.getReference(rset, SWTConstants.SF_CTABITEM_CONTROL);
	}

	/*
	 * Model children is the items feature. However, this returns the CTabItems, but we want to return instead the controls themselves. They
	 * are the "model" that gets sent to the createChild and control edit part.
	 */
	protected List getChildJavaBeans() {
		List ctabitems = (List) ((EObject) getModel()).eGet(sf_items);
		ArrayList children = new ArrayList(ctabitems.size());
		Iterator itr = ctabitems.iterator();
		while (itr.hasNext()) {
			EObject ctabitem = (EObject) itr.next();
			// Get the control out of the CTabItem
			if (ctabitem.eGet(sf_cTabItemControl) != null)
				children.add(ctabitem.eGet(sf_cTabItemControl));
		}
		return children;
	}

	protected void setPropertySource(ControlTreeEditPart childEP, EObject child) {
		EObject tab = InverseMaintenanceAdapter.getIntermediateReference((EObject) getModel(), sf_items, sf_cTabItemControl, child);
		// This is the property source of the actual child, which is the ctabitem.
		if (tab != null)
			childEP.setPropertySource((IPropertySource) EcoreUtil.getRegisteredAdapter(tab, IPropertySource.class));
		else
			childEP.setPropertySource(null);
	}

}
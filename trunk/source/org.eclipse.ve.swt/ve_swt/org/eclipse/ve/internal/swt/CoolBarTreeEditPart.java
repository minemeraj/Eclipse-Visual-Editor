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
 * $RCSfile: CoolBarTreeEditPart.java,v $ $Revision: 1.7 $ $Date: 2005-06-02 22:32:30 $
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

public class CoolBarTreeEditPart extends CompositeTreeEditPart {

	private EReference sf_items;

	private EReference sf_coolItemControl;

	public CoolBarTreeEditPart(Object model) {
		super(model);
	}

	protected void setPropertySource(ControlTreeEditPart childEP, EObject child) {
		EObject tab = InverseMaintenanceAdapter.getIntermediateReference((EObject) getModel(), sf_items, sf_coolItemControl, child);
		// This is the property source of the actual child, which is the tabitem.
		if (tab != null)
			childEP.setPropertySource((IPropertySource) EcoreUtil.getRegisteredAdapter(tab, IPropertySource.class));
		else
			childEP.setPropertySource(null);
	}

	/*
	 * Model children is the items feature. However, this returns the CoolItems, but we want to return instead the controls themselves. They are the
	 * "model" that gets sent to the createChild and control edit part.
	 */
	protected List getChildJavaBeans() {
		List coolitems = (List) ((EObject) getModel()).eGet(sf_items);
		ArrayList children = new ArrayList(coolitems.size());
		Iterator itr = coolitems.iterator();
		while (itr.hasNext()) {
			EObject coolitem = (EObject) itr.next();
			// Get the control out of the CoolItem
			children.add(coolitem.eGet(sf_coolItemControl));
		}
		return children;
	}

	protected EditPart createChildEditPart(Object model) {
		EditPart ep = super.createChildEditPart(model);
		setPropertySource((ControlTreeEditPart) ep, (EObject) model);
		return ep;
	}

	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new org.eclipse.ve.internal.cde.core.TreeContainerEditPolicy(new CoolBarContainerPolicy(
				EditDomain.getEditDomain(this))));
	}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		ResourceSet rset = ((EObject) model).eResource().getResourceSet();
		sf_items = JavaInstantiation.getReference(rset, SWTConstants.SF_COOLBAR_ITEMS);
		sf_coolItemControl = JavaInstantiation.getReference(rset, SWTConstants.SF_COOLITEM_CONTROL);
	}

	public void deactivate() {
		((EObject) getModel()).eAdapters().remove(containerAdapter);
		super.deactivate();
	}

	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(containerAdapter);
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
				queueExec(CoolBarTreeEditPart.this, "ITEMS"); //$NON-NLS-1$
		}
	};
}

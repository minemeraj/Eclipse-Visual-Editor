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
 *  $RCSfile: CoolBarGraphicalEditPart.java,v $
 *  $Revision: 1.2 $  $Date: 2004-08-25 15:46:05 $ 
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

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.*;

/**
 * 
 * @since 1.0.0
 */
public class CoolBarGraphicalEditPart extends CompositeGraphicalEditPart {

	private EReference sf_items, sf_coolItemControl;

	protected BeanProxyAdapter coolBarProxyAdapter;

	public CoolBarGraphicalEditPart(Object model) {
		super(model);
	}

	public void deactivate() {
		((EObject) getModel()).eAdapters().remove(containerAdapter);
		super.deactivate();
	}

	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(containerAdapter);
	}

	private Adapter containerAdapter = new EditPartAdapterRunnable() {

		public void run() {
			if (isActive()) {
				refreshChildren();
				List children = getChildren();
				int s = children.size();
				for (int i = 0; i < s; i++) {
					EditPart ep = (EditPart) children.get(i);
					if (ep instanceof ControlGraphicalEditPart)
						setPropertySource((ControlGraphicalEditPart) ep, (EObject) ep.getModel());
				}
				getCoolBarProxyAdapter().revalidateBeanProxy();
			}
		}

		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sf_items)
				queueExec(CoolBarGraphicalEditPart.this);
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createChild(java.lang.Object)
	 */
	protected EditPart createChild(Object model) {
		EditPart ep = super.createChild(model);
		if (ep instanceof ControlGraphicalEditPart)
			setPropertySource((ControlGraphicalEditPart) ep, (EObject) model);
		return ep;
	}

	protected void createLayoutEditPolicy() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new CoolBarLayoutEditPolicy(this));
	}

	protected void setPropertySource(ControlGraphicalEditPart childEP, EObject child) {
		EObject tab = InverseMaintenanceAdapter.getIntermediateReference((EObject) getModel(), sf_items, sf_coolItemControl, child);
		// This is the property source of the actual child, which is the coolitem.
		if (tab != null)
			childEP.setPropertySource((IPropertySource) EcoreUtil.getRegisteredAdapter(tab, IPropertySource.class));
		else
			childEP.setPropertySource(null);
	}

	/*
	 * Model children is the items feature. However, this returns the CoolItems, but we want to return instead the controls themselves. They are the
	 * "model" that gets sent to the createChild and control edit part.
	 */
	protected List getModelChildren() {
		List tabitems = (List) ((EObject) getModel()).eGet(sf_items);
		ArrayList children = new ArrayList(tabitems.size());
		Iterator itr = tabitems.iterator();
		while (itr.hasNext()) {
			EObject tabitem = (EObject) itr.next();
			// Get the control out of the TabItem
			children.add(tabitem.eGet(sf_coolItemControl));
		}
		return children;
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

	/*
	 * Return the proxy adapter associated with this CoolBar.
	 */
	protected BeanProxyAdapter getCoolBarProxyAdapter() {
		if (coolBarProxyAdapter == null) {
			IBeanProxyHost coolBarProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) getModel());
			coolBarProxyAdapter = (BeanProxyAdapter) coolBarProxyHost;
		}
		return coolBarProxyAdapter;
	}

}
/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CoolBarGraphicalEditPart.java,v $
 *  $Revision: 1.8 $  $Date: 2005-06-15 20:19:21 $ 
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

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IErrorNotifier;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

/**
 * 
 * @since 1.0.0
 */
public class CoolBarGraphicalEditPart extends CompositeGraphicalEditPart {

	private EReference sf_items, sf_coolItemControl;

	protected TabFolderProxyAdapter tabFolderProxyAdapter;

	/**
	 * @param model
	 * 
	 * @since 1.0.0
	 */
	public CoolBarGraphicalEditPart(Object model) {
		super(model);
	}

	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(containerAdapter);
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
				try {
					setupControl((ControlGraphicalEditPart) ep, (EObject) ep.getModel());
				} catch (ClassCastException e) {
					// Would only occur if child was invalid. So not a problem, already have marked this as an error.
				}
			}
		}

		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sf_items)
				queueExec(CoolBarGraphicalEditPart.this, "ITEMS"); //$NON-NLS-1$
		}
	};

	protected void createLayoutEditPolicy() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new UnknownLayoutInputPolicy(new CoolBarContainerPolicy(EditDomain.getEditDomain(this))));
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
	 * Model children is the items feature. However, this returns the TabItems, but we want to return instead the controls themselves. They are the
	 * "model" that gets sent to the createChild and control edit part.
	 */
	protected List getModelChildren() {
		List tabitems = (List) ((EObject) getModel()).eGet(sf_items);
		ArrayList children = new ArrayList(tabitems.size());
		Iterator itr = tabitems.iterator();
		while (itr.hasNext()) {
			EObject tabitem = (EObject) itr.next();
			// Get the control out of the TabItem
			if (tabitem.eGet(sf_coolItemControl) != null)
				children.add(tabitem.eGet(sf_coolItemControl));
		}
		return children;
	}

	protected void setupControl(ControlGraphicalEditPart childEP, EObject child) {
		// Get the TabItem's error notifier for the child (which is a control) and add it in to the control gep. That way TabItem
		// errors will show on the child.
		IJavaObjectInstance tab = getCoolItemForChild(child);
		if (childEP != null) {
			childEP.setPropertySource((IPropertySource) EcoreUtil.getRegisteredAdapter(tab, IPropertySource.class));
			childEP.setErrorNotifier((IErrorNotifier) EcoreUtil.getExistingAdapter(tab, IErrorNotifier.ERROR_NOTIFIER_TYPE));
		} else {
			childEP.setPropertySource(null);
			childEP.setErrorNotifier(null);
		}
	}

	/**
	 * Get the TabItem for the given child control.
	 * 
	 * @param child
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected IJavaObjectInstance getCoolItemForChild(EObject child) {
		return (IJavaObjectInstance) InverseMaintenanceAdapter.getIntermediateReference((EObject) getModel(), sf_items, sf_coolItemControl, child);
	}
}
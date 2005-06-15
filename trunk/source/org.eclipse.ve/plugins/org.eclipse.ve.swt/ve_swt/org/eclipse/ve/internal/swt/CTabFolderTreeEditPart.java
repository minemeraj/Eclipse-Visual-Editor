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
 *  $Revision: 1.3 $  $Date: 2005-06-15 20:19:21 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.*;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IErrorNotifier;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

/**
 * 
 * @since 1.0.0
 */
public class CTabFolderTreeEditPart extends CompositeTreeEditPart {

	private EReference sf_items, sf_tabItemControl;

	public CTabFolderTreeEditPart(Object model) {
		super(model);
	}

	public void deactivate() {
		((EObject) getModel()).eAdapters().remove(containerAdapter);
		// Need to remove the label decorator from each of the children
		for (Iterator iter = getChildren().iterator(); iter.hasNext();) {
			disposeLabelDecorator((EditPart) iter.next());
		}
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
					setupControl((ControlTreeEditPart) ep, (EObject) ep.getModel());
				} catch (ClassCastException e) {
					// Would only occur if child was invalid. So not a problem, already have marked this as an error.
				}
			}
		}

		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sf_items)
				queueExec(CTabFolderTreeEditPart.this, "ITEMS"); //$NON-NLS-1$
		}
	};

	public void activate() {
		super.activate();
		// We need add a listener to dispose of the special decorator used for the
		// TabFolder's children when the child is removed.
		addEditPartListener(new EditPartListener.Stub() {

			public void removingChild(EditPart editpart, int index) {
				disposeLabelDecorator(editpart);
			}
		});

		((EObject) getModel()).eAdapters().add(containerAdapter);
	}

	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new org.eclipse.ve.internal.cde.core.TreeContainerEditPolicy(new CTabFolderContainerPolicy(
				EditDomain.getEditDomain(this))));
	}

	protected void setupControl(ControlTreeEditPart childEP, EObject child) {
		// Get the TabItem's error notifier for the child (which is a control) and add it in to the control gep. That way TabItem
		// errors will show on the child.
		EObject tab = InverseMaintenanceAdapter.getIntermediateReference((EObject) getModel(), sf_items, sf_tabItemControl, child);
		disposeLabelDecorator(childEP);
		if (childEP != null) {
			childEP.setPropertySource((IPropertySource) EcoreUtil.getRegisteredAdapter(tab, IPropertySource.class));
			childEP.setErrorNotifier((IErrorNotifier) EcoreUtil.getExistingAdapter(tab, IErrorNotifier.ERROR_NOTIFIER_TYPE));
			childEP.setLabelDecorator(new ItemChildTreeLabelDecorator(tab));
		} else {
			childEP.setPropertySource(null);
			childEP.setErrorNotifier(null);
			childEP.setLabelDecorator(null);
		}
	}

	/**
	 * Dispose the label decorator on the child editpart.
	 * 
	 * @param editpart
	 * 
	 * @since 1.1.0
	 */
	protected void disposeLabelDecorator(EditPart editpart) {
		try {
			ControlTreeEditPart ctep = (ControlTreeEditPart) editpart;
			ILabelDecorator labelDecorator2 = ctep.getLabelDecorator();
			if (labelDecorator2 != null) {
				ctep.setLabelDecorator(null);
				labelDecorator2.dispose();
			}
		} catch (ClassCastException e) {
		}
	}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		ResourceSet rset = ((EObject) model).eResource().getResourceSet();
		sf_items = JavaInstantiation.getReference(rset, SWTConstants.SF_CTABFOLDER_ITEMS);
		sf_tabItemControl = JavaInstantiation.getReference(rset, SWTConstants.SF_CTABITEM_CONTROL);
	}

	/*
	 * Model children is the items feature. However, this returns the TabItems, but we want to return instead the controls themselves. They are the
	 * "model" that gets sent to the createChild and control edit part.
	 */
	protected List getChildJavaBeans() {
		List tabitems = (List) ((EObject) getModel()).eGet(sf_items);
		ArrayList children = new ArrayList(tabitems.size());
		Iterator itr = tabitems.iterator();
		while (itr.hasNext()) {
			// Get the control out of the TabItem
			Object childControl = ((EObject) itr.next()).eGet(sf_tabItemControl);
			if (childControl != null)
				children.add(childControl);
		}
		return children;
	}

}
package org.eclipse.ve.internal.java.vce;
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
 *  $RCSfile: SubclassCompositionComponentsTreeEditPart.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-23 12:45:36 $ 
 */

import java.util.*;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.TreeContainerEditPolicy;
import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
import org.eclipse.ve.internal.jcm.JCMPackage;
/**
 * Composition Graphical Edit Part that instantiates and disposes bean proxies
 */
public class SubclassCompositionComponentsTreeEditPart extends AbstractTreeEditPart {

	public SubclassCompositionComponentsTreeEditPart(Object model) {
		setModel(model);
	}
	

	protected List getModelChildren() {
		BeanSubclassComposition comp = (BeanSubclassComposition) getModel();
		if (comp != null) {
			List components = comp.getComponents();
			if (comp.eIsSet(JCMPackage.eINSTANCE.getBeanSubclassComposition_ThisPart())) {
				ArrayList children = new ArrayList(components.size()+1);
				children.add(comp.getThisPart());
				children.addAll(components);
				return children;
			} else
				return components;
		} else
			return Collections.EMPTY_LIST;
	}
	
	protected Adapter compositionAdapter = new AdapterImpl() {
		public void notifyChanged(Notification msg) {
			switch (msg.getFeatureID(BeanSubclassComposition.class)) {
				case JCMPackage.BEAN_SUBCLASS_COMPOSITION__THIS_PART:
				case JCMPackage.BEAN_SUBCLASS_COMPOSITION__COMPONENTS:
try {					
					refreshChildren();
}
catch (Exception e) {}
					break;
			}
		}

	};
	
	public void activate() {
		super.activate();
		if (getModel() != null)
			((BeanSubclassComposition) getModel()).eAdapters().add(compositionAdapter);
	}
	
	public void deactivate() {
		super.deactivate();
		if (getModel() != null)
			((BeanSubclassComposition) getModel()).eAdapters().remove(compositionAdapter);
	}
	
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new TreeContainerEditPolicy(new SubclassCompositionContainerPolicy(EditDomain.getEditDomain(this))));
	}
	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 * Initializes the entire state of the EditPart.
	 */
	protected void refreshVisuals() {
		super.refreshVisuals();
		Tree tree = (Tree)getWidget();
		TreeItem[] items = (tree).getItems();
		// GTK requires parent to be expanded, and child to have children at
		// time expand is called, otherwise expansion state is not maintained.
		// Because of this we need to expand entire tree here because this 
		// is only place these conditions are met fully.
		for(int i=0;i<items.length;i++){
			expandTree(items[i]);
		}
		// Windows will scroll tree to last expandable node. This can cause tree to scroll down
		// to the bottom. This is annoying. Instead we will scroll back to the top and select the first item.
		if (items.length > 0) {
			tree.setSelection(new TreeItem[] {items[0]});
			tree.showSelection();
		}
	}
	
	private void expandTree(TreeItem item) {
		item.setExpanded(true);
		TreeItem[] items = item.getItems();
		for (int i = 0; i < items.length; i++) {
			expandTree(items[i]);
		}
	}
	
	public Object getAdapter(Class key) {
		Object result = super.getAdapter(key);
		if (result == null && getModel() != null) {
			// See if any of the MOF adapters on our target can return a value for the request
			Iterator mofAdapters = ((Notifier) getModel()).eAdapters().iterator();
			while (mofAdapters.hasNext()) {
				Object mofAdapter = mofAdapters.next();
				if (mofAdapter instanceof IAdaptable) {
					Object mofAdapterAdapter = ((IAdaptable) mofAdapter).getAdapter(key);
					if (mofAdapterAdapter != null) {
						return mofAdapterAdapter;
					}
				}
			}
		}
		return result;
	}
}

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
 * $RCSfile: CompositionComponentsTreeEditPart.java,v $ $Revision: 1.2 $ $Date: 2004-03-26 23:08:01 $
 */
package org.eclipse.ve.internal.java.core;

import java.util.*;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.ve.internal.cde.core.*;

import org.eclipse.ve.internal.jcm.BeanComposition;
import org.eclipse.ve.internal.jcm.JCMPackage;

/**
 * Composition Graphical Edit Part for bean compositions
 */
public class CompositionComponentsTreeEditPart extends AbstractTreeEditPart {

	public CompositionComponentsTreeEditPart(Object model) {
		setModel(model);
	}

	protected List getModelChildren() {
		BeanComposition comp = (BeanComposition) getModel();
		return comp != null ? comp.getComponents() : Collections.EMPTY_LIST;
	}

	protected Adapter compositionAdapter = new AdapterImpl() {
		public void notifyChanged(Notification msg) {
			if (msg.getFeatureID(BeanComposition.class) == JCMPackage.BEAN_COMPOSITION__COMPONENTS) {
				queueRefreshChildren();
			}
		}
	};
	
	/**
	 * Queue up a refresh child for next async exec.
	 *  
	 * @since 1.0.0
	 */
	protected void queueRefreshChildren() {
		CDEUtilities.displayExec(getViewer().getControl().getDisplay(), new Runnable() {
			public void run() {
				// Test if active because this could of been queued up and not run until AFTER it was deactivated.
				if (isActive())
					refreshChildren();
			}
		});
	}	

	public void activate() {
		super.activate();
		if (getModel() != null) ((BeanComposition) getModel()).eAdapters().add(compositionAdapter);
	}

	public void deactivate() {
		super.deactivate();
		if (getModel() != null) ((BeanComposition) getModel()).eAdapters().remove(compositionAdapter);
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new TreeContainerEditPolicy(getContainerPolicy()));
	}

	/**
	 * Get the container policy. Subclasses may override to return a different one.
	 * @return
	 * 
	 * @since 1.0.0
	 */
	protected ContainerPolicy getContainerPolicy() {
		return new CompositionContainerPolicy(EditDomain.getEditDomain(this));
	}

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

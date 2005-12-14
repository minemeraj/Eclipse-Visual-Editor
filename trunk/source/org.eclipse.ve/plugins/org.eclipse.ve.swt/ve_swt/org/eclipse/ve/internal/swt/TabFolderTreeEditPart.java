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
 *  $RCSfile: TabFolderTreeEditPart.java,v $
 *  $Revision: 1.13 $  $Date: 2005-12-14 21:44:40 $ 
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
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IErrorNotifier;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.BeanTreeDirectEditManager;

import org.eclipse.ve.internal.propertysheet.command.ICommandPropertyDescriptor;

/**
 * swt TabFolder tree edit part.
 * 
 * @since 1.0.0
 */
public class TabFolderTreeEditPart extends CompositeTreeEditPart {

	private static final String TAB_ITEM_DIRECT_EDIT_MANAGER = "TAB_ITEM_DIRECT_EDIT_MANAGER"; //$NON-NLS-1$
	private EReference sf_items, sf_tabItemControl;

	public TabFolderTreeEditPart(Object model) {
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
				queueExec(TabFolderTreeEditPart.this, "ITEMS"); //$NON-NLS-1$
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
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new org.eclipse.ve.internal.cde.core.TreeContainerEditPolicy(new TabFolderContainerPolicy(
				EditDomain.getEditDomain(this))));
	}
	
	protected void addChild(EditPart child, int index) {
		super.addChild(child, index);
		// For a tab item the child's direct edit policy must be replaced with a custom one that lets the "tabText"
		// property be the one that is affected (and not the child's "text" property for example if it is a Button,Label, etc.
		EditDomain domain = EditDomain.getEditDomain(this);
		EditPartViewer viewer = getRoot().getViewer();		
		((ControlTreeEditPart)child).manager = getDirectEditManager(domain,viewer);
	}
	
	public static BeanTreeDirectEditManager getDirectEditManager(EditDomain domain, EditPartViewer viewer) {
		BeanTreeDirectEditManager manager = (BeanTreeDirectEditManager) domain.getViewerData(viewer, TAB_ITEM_DIRECT_EDIT_MANAGER);
		if (manager == null) {
			manager = new TabItemDirectEditManager(viewer);
			domain.setViewerData(viewer, TAB_ITEM_DIRECT_EDIT_MANAGER, manager);
		}
		return manager;
	}	
	
	public static class TabItemDirectEditManager extends BeanTreeDirectEditManager{
		public TabItemDirectEditManager(EditPartViewer v) {
			super(v);
		}
		protected Command getDirectEditCommand(Object newValue, EditPart ep, IPropertyDescriptor property) {
			IPropertySource ps = (IPropertySource) ep.getAdapter(IPropertySource.class);
			IPropertyDescriptor tabTextProperty = ((TabItemPropertySourceAdapter)ps).tabTextPropertyDescriptor;			
			return ((ICommandPropertyDescriptor) tabTextProperty).setValue(ps, newValue);
		}		
	};

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
		sf_items = JavaInstantiation.getReference(rset, SWTConstants.SF_TABFOLDER_ITEMS);
		sf_tabItemControl = JavaInstantiation.getReference(rset, SWTConstants.SF_TABITEM_CONTROL);
	}

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

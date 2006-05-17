/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TabFolderGraphicalEditPart.java,v $
 *  $Revision: 1.21 $  $Date: 2006-05-17 20:15:53 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IErrorNotifier;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.*;

import org.eclipse.ve.internal.propertysheet.command.ICommandPropertyDescriptor;

/**
 * swt TabFolder graphical edit part.
 * @since 1.0.0
 */
public class TabFolderGraphicalEditPart extends CompositeGraphicalEditPart {

	private EReference sf_items, sf_tabItemControl;

	protected TabFolderProxyAdapter tabFolderProxyAdapter;

	private EditPartListener pageListener;

	protected IJavaObjectInstance fSelectedItem;

	/**
	 * @param model
	 * 
	 * @since 1.0.0
	 */
	public TabFolderGraphicalEditPart(Object model) {
		super(model);
	}
	
	public static class TabItemDirectEditPolicy extends DirectEditPolicy{
		protected Command getDirectEditCommand(DirectEditRequest request) {
			Object newValue = request.getCellEditor().getValue();
			IPropertySource ps = (IPropertySource) getHost().getAdapter(IPropertySource.class);
			// Find the "text" property that sets the Tab Item's text property
			IPropertyDescriptor property = ((TabItemPropertySourceAdapter)ps).tabTextPropertyDescriptor;								
			return ((ICommandPropertyDescriptor) property).setValue(ps, newValue);
		}
		protected void showCurrentEditValue(DirectEditRequest request) {
		}
		public IPropertyDescriptor getTabTextEditProperty(){
			IPropertySource ps = (IPropertySource) getHost().getAdapter(IPropertySource.class);
			return ((TabItemPropertySourceAdapter)ps).tabTextPropertyDescriptor;			
		}
	}
	
	protected void addChild(EditPart child, int index) {
		super.addChild(child, index);
		if (child instanceof ControlGraphicalEditPart) {
			// For a tab item the child's direct edit policy must be replaced with a custom one that lets the "tabText"
			// property be the one that is affected (and not the child's "text" property for example if it is a Button,Label, etc.
			child.installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new TabItemDirectEditPolicy());
			// The child is the ControlGraphicalEditPart which knows how to copy itself, however because the model in fact has a TabItem
			// inbetween we need to override the edit policy so tabItem itself gets copied
			child.installEditPolicy(CopyAction.REQ_COPY, new TabItemCopyEditPolicy(EditDomain.getEditDomain(this)));
		}		
		
	}

	public void activate() {
		setListener(createPageListener());
		super.activate();
		((EObject) getModel()).eAdapters().add(containerAdapter);
		List children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			EditPart page = (EditPart) children.get(i);
			addPageListenerToChildren(page);
			if (i == 0) {
				setPageVisible(page, true);
				pageSelected((EditPart) getChildren().get(0));
			} else {
				setPageVisible(page, false);
			}
		}
	}

	public void deactivate() {
		Iterator children = getChildren().iterator();
		while (children.hasNext())
			removePageListenerFromChildren((EditPart) children.next());
		((EObject) getModel()).eAdapters().remove(containerAdapter);
		setListener(null);
		super.deactivate();
	}

	private Adapter containerAdapter = new EditPartAdapterRunnable(this) {

		protected void doRun() {
			if (fSelectedItem != null) {
				EditPart currentPage = getEditPartFromModel(fSelectedItem);
				setPageVisible(currentPage, false);
			}
			// Then show the newly selected page
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
			EditPart page = getEditPartFromModel(fSelectedItem);
			if (page == null && s > 0) {
				page = (EditPart) children.get(0);
			}
			setPageVisible(page, true);
			pageSelected(page);
			getTabFolderProxyAdapter().revalidateBeanProxy();
		}

		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sf_items)
				queueExec(TabFolderGraphicalEditPart.this, "ITEMS"); //$NON-NLS-1$
		}
	};

	protected void createLayoutEditPolicy() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new UnknownLayoutInputPolicy(new TabFolderContainerPolicy(EditDomain.getEditDomain(this))));
	}

	/*
	 * This page has been selected or deselected. Make it and all it's children visible or invisible.
	 */
	protected void setPageVisible(EditPart page, boolean bool) {
		if (page != null) {
			((GraphicalEditPart) page).getFigure().setVisible(bool);
			IFigure fig = ((GraphicalEditPart) page).getFigure();
			Iterator children = page.getChildren().iterator();
			while (children.hasNext())
				((GraphicalEditPart) children.next()).getFigure().setVisible(bool);
			fig.revalidate();
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
			if (tabitem.eGet(sf_tabItemControl) != null)
				children.add(tabitem.eGet(sf_tabItemControl));
		}
		return children;
	}

	protected void setListener(EditPartListener listener) {
		if (this.pageListener != null)
			removeEditPartListener(this.pageListener);
		this.pageListener = listener;
		if (this.pageListener != null)
			addEditPartListener(this.pageListener);
	}

	protected void removePageListenerFromChildren(EditPart ep) {
		ep.removeEditPartListener(pageListener);
		Iterator childen = ep.getChildren().iterator();
		while (childen.hasNext())
			removePageListenerFromChildren((EditPart) childen.next());
	}

	/*
	 * The selected page of the JTabbedPane has changed. Bring this page to the front.
	 */
	protected void pageSelected(EditPart page) {
		if (page != null) {
			if (fSelectedItem != null) {
				EditPart currentPage = getEditPartFromModel(fSelectedItem);
				setPageVisible(currentPage, false);
			}
			setPageVisible(page, true);
			fSelectedItem = (IJavaObjectInstance) page.getModel(); // save for later checks... see createPageListener()
			getTabFolderProxyAdapter().setSelection(getTabForChild(fSelectedItem));
		}
	}

	/*
	 * If the parent of this editpart is the JTabbedPane, we're on the page. If not recursely call up through the parent chain until we find the
	 * editpart (page) that the original editpart was found in.
	 */
	protected EditPart getPageOfSelectedEditpart(EditPart ep) {
		if (ep == null || ep.getParent() == this)
			return ep;
		return getPageOfSelectedEditpart(ep.getParent());
	}

	/*
	 * Return the proxy adapter associated with this TabFolder.
	 */
	protected TabFolderProxyAdapter getTabFolderProxyAdapter() {
		if (tabFolderProxyAdapter == null) {
			IBeanProxyHost tabFolderProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) getModel());
			tabFolderProxyAdapter = (TabFolderProxyAdapter) tabFolderProxyHost;
		}
		return tabFolderProxyAdapter;
	}

	/*
	 * Search through the JTabbedPane's pages (children) to find the page that matches the page model that is selected.
	 */
	protected EditPart getEditPartFromModel(IJavaObjectInstance pageModel) {
		Iterator children = getChildren().iterator();
		while (children.hasNext()) {
			EditPart page = (EditPart) children.next();
			if ((IJavaObjectInstance) page.getModel() == pageModel)
				return page;
		}
		return null;
	}

	/*
	 * Create an EditPartListener for itself and its pages so it knows when a page has been added, removed, or selected
	 */
	protected EditPartListener createPageListener() {
		return new EditPartListener.Stub() {

			public void childAdded(EditPart editpart, int index) {
				addPageListenerToChildren(editpart);
			}

			public void removingChild(EditPart editpart, int index) {
				removePageListenerFromChildren(editpart);
			}

			public void selectedStateChanged(EditPart editpart) {
				if (editpart == null || editpart == TabFolderGraphicalEditPart.this)
					return;
				// Find the page where this editpart resides and bring the page to the
				// front if isn't already.
				if ((editpart != null) && (editpart.getSelected() == EditPart.SELECTED || editpart.getSelected() == EditPart.SELECTED_PRIMARY)) {
					EditPart page = getPageOfSelectedEditpart(editpart);
					if (page != null && page.getModel() != fSelectedItem) {
						// First hide the previously selected page and hide any grids if their on
						EditPart currentPage = getEditPartFromModel(fSelectedItem);
						setPageVisible(currentPage, false);
						// Then show the newly selected page
						setPageVisible(page, true);
						pageSelected(page);
					}
				}
			}
		};
	}

	protected void addPageListenerToChildren(EditPart ep) {
		ep.addEditPartListener(pageListener);
		Iterator childen = ep.getChildren().iterator();
		while (childen.hasNext())
			addPageListenerToChildren((EditPart) childen.next());
	}
	
	protected void setupControl(ControlGraphicalEditPart childEP, EObject child) {
		// Get the TabItem's error notifier for the child (which is a control) and add it in to the control gep. That way TabItem
		// errors will show on the child.
		IJavaObjectInstance tab = getTabForChild(child);
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
	 * @param child
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected IJavaObjectInstance getTabForChild(EObject child) {
		return (IJavaObjectInstance) InverseMaintenanceAdapter.getIntermediateReference((EObject) getModel(), sf_items, sf_tabItemControl, child);
	}
	
	/**
	 * Get current page index.
	 */
	public int getCurrentPageIndex() {
		List children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			if (((EditPart) children.get(i)).getModel() == fSelectedItem)
				return i;
		}
		return -1;
	}

	/**
	 * Select the next page
	 */
	public void selectNextPage() {
		if (fSelectedItem != null) {
			List children = getChildren();
			int cp = getCurrentPageIndex();
			if (++cp < children.size()) {
				EditPart nextpage = (EditPart) children.get(cp);
				pageSelected(nextpage);
			}
		}
	}

	/**
	 * Select the previous page
	 */
	public void selectPreviousPage() {
		if (fSelectedItem != null) {
			List children = getChildren();
			int cp = getCurrentPageIndex();
			if (--cp >= 0) {
				EditPart prevpage = (EditPart) children.get(cp);
				pageSelected(prevpage);
			}
		}
	}
	
}

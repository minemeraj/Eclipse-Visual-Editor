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
 *  $RCSfile: CTabFolderGraphicalEditPart.java,v $
 *  $Revision: 1.4 $  $Date: 2005-06-07 19:22:42 $ 
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
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;

/**
 * 
 * @since 1.1
 */
public class CTabFolderGraphicalEditPart extends CompositeGraphicalEditPart {
	private EReference sf_items, sf_cTabItemControl;
	protected CTabFolderProxyAdapter cTabFolderProxyAdapter;

	private EditPartListener pageListener;

	protected IJavaObjectInstance fSelectedItem;

	/**
	 * @param model
	 * 
	 * @since 1.1
	 */
	public CTabFolderGraphicalEditPart(Object model) {
		super(model);
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
				if (ep instanceof ControlGraphicalEditPart)
					setPropertySource((ControlGraphicalEditPart) ep, (EObject) ep.getModel());
			}
			EditPart page = getEditPartFromModel(fSelectedItem);
			if(page == null && s > 0){
				page = (EditPart) children.get(0);
			}
			setPageVisible(page, true);
			pageSelected(page);
			getCTabFolderProxyAdapter().revalidateBeanProxy();
		}
	
		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sf_items)
				queueExec(CTabFolderGraphicalEditPart.this);
		}
	};

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createChild(java.lang.Object)
	 */
	protected EditPart createChild(Object model) {
		EditPart ep = super.createChild(model);
		if (ep instanceof ControlGraphicalEditPart)
			setPropertySource((ControlGraphicalEditPart) ep, (EObject) model);
		return ep;
	}
	
	protected void createLayoutEditPolicy() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new UnknownLayoutInputPolicy(new CTabFolderContainerPolicy(EditDomain.getEditDomain(this))));
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

	protected void setPropertySource(ControlGraphicalEditPart childEP, EObject child) {
		EObject tab = InverseMaintenanceAdapter.getIntermediateReference((EObject) getModel(), sf_items, sf_cTabItemControl, child);
		// This is the property source of the actual child, which is the tabitem.
		if (tab != null)
			childEP.setPropertySource((IPropertySource) EcoreUtil.getRegisteredAdapter(tab, IPropertySource.class));
		else
			childEP.setPropertySource(null);
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
	protected List getModelChildren() {
		List cTabitems = (List) ((EObject) getModel()).eGet(sf_items);
		ArrayList children = new ArrayList(cTabitems.size());
		Iterator itr = cTabitems.iterator();
		while (itr.hasNext()) {
			EObject ctabitem = (EObject) itr.next();
			// Get the control out of the CTabItem
			if (ctabitem.eGet(sf_cTabItemControl) != null)
				children.add(ctabitem.eGet(sf_cTabItemControl));
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
	 * The selected page of the CTabFolder has changed. Bring this page to the front.
	 */
	protected void pageSelected(EditPart page) {
		if (page != null) {
			if (fSelectedItem != null) {
				EditPart currentPage = getEditPartFromModel(fSelectedItem);
				setPageVisible(currentPage, false);
			}
			setPageVisible(page, true);
			fSelectedItem = (IJavaObjectInstance) page.getModel(); // save for later checks... see createPageListener()
			getCTabFolderProxyAdapter().setSelection(getChildren().indexOf(page));
		}
	}

	/*
	 * If the parent of this editpart is the CTabFolder, we're on the page. If not recursely call up through the parent chain until we find the
	 * editpart (page) that the original editpart was found in.
	 */
	protected EditPart getPageOfSelectedEditpart(EditPart ep) {
		if (ep == null || ep.getParent() == this)
			return ep;
		return getPageOfSelectedEditpart(ep.getParent());
	}

	/*
	 * Return the proxy adapter associated with this CTabFolder.
	 */
	protected CTabFolderProxyAdapter getCTabFolderProxyAdapter() {
		if (cTabFolderProxyAdapter == null) {
			IBeanProxyHost cTabFolderProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) getModel());
			cTabFolderProxyAdapter = (CTabFolderProxyAdapter) cTabFolderProxyHost;
		}
		return cTabFolderProxyAdapter;
	}

	/*
	 * Search through the CTabFolder's pages (children) to find the page that matches the page model that is selected.
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
				if (editpart == null || editpart == CTabFolderGraphicalEditPart.this)
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
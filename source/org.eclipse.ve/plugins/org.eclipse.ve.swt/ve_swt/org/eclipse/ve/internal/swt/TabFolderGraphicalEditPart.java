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
 *  $RCSfile: TabFolderGraphicalEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2004-08-10 21:26:19 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.*;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;

/**
 * 
 * @since 1.0.0
 */
public class TabFolderGraphicalEditPart extends CompositeGraphicalEditPart {

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

	public void activate() {
		setListener(createPageListener());
		super.activate();
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
		setListener(null);
		super.deactivate();
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
		fSelectedItem = (IJavaObjectInstance) page.getModel(); // save for later checks... see createPageListener()
		getTabFolderProxyAdapter().setSelection(getChildren().indexOf(page));
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
}
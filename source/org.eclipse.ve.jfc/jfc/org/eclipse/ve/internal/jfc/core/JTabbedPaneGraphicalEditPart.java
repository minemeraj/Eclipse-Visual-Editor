package org.eclipse.ve.internal.jfc.core;
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
 *  $RCSfile: JTabbedPaneGraphicalEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import java.util.*;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.*;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;

/**
 * Graphical edit part for handling JTabbedPane in the Graph viewer
 * 
 * Since we can't really select the individual tabs on the JTabbedPane,
 * we depend on the tree editpart in the outline viewer for selecting the
 * pages or components on those pages. 
 * 
 * When the tree editpart is selected, this editpart is also selected
 * and the JTabbedPaneProxyAdapter is notified so the page can be brought forward. 
 * When this occurs, the previously viewed page figures are hidden and the 
 * currently selected page is made visible. If we didn't hide the editparts 
 * from the other pages, the figure rectangle outlines will "bleed" through 
 * and show on the top page.
 */
public class JTabbedPaneGraphicalEditPart extends ContainerGraphicalEditPart {
	private EditPartListener pageListener;
	protected IJavaObjectInstance fSelectedPage;
	protected boolean fSelectingPage = false;
	protected JTabbedPaneProxyAdapter tabbedpaneAdapter;
	
	protected EReference
		sfTabs,
		sfComponent;

	public JTabbedPaneGraphicalEditPart(Object model) {
		super(model);
	}
	
	private Adapter containerAdapter = new AdapterImpl() {
		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sfTabs) {
				refreshChildren();
				// With changes to the editpart structure (deletes and moves), its hard to determine
				// which page should be hidden and which should be shown so we'll just query the live
				// JTabbedPane and find out which page is selected...then show it's corresponding
				// editpart. Then hide the rest if they are visible.
				int selectedIndex = getJTabbedPaneProxyAdapter().getSelectedIndex();
				if (selectedIndex != -1) {
					if (selectedIndex < getChildren().size()
						&& ((IJavaObjectInstance) ((EditPart) getChildren().get(selectedIndex)).getModel()) != fSelectedPage) {
						List childen = getChildren();
						for (int i = 0; i < children.size(); i++) {
							GraphicalEditPart ep = (GraphicalEditPart) childen.get(i);
							if (i == selectedIndex) {
								if (!ep.getFigure().isVisible()) {
									setPageVisible(ep, true);
									fSelectedPage = (IJavaObjectInstance) ep.getModel();
								}
							} else if (ep.getFigure().isVisible())
								setPageVisible(ep, false);
						}
					}
				}
			}

		}
	};
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
				pageSelected((EditPart)getChildren().get(0));
			} else {
				setPageVisible(page, false);
			}
		}
	}
	protected void addPageListenerToChildren(EditPart ep) {
		ep.addEditPartListener(pageListener);
		Iterator childen = ep.getChildren().iterator();
		while (childen.hasNext())
			 addPageListenerToChildren((EditPart) childen.next());
	}
	public void deactivate() {
		Iterator children = getChildren().iterator();
		while (children.hasNext())
			 removePageListenerFromChildren((EditPart) children.next());
		((EObject) getModel()).eAdapters().remove(containerAdapter);
		setListener(null);
		super.deactivate();
	}
	protected void removePageListenerFromChildren(EditPart ep) {
		ep.removeEditPartListener(pageListener);
		Iterator childen = ep.getChildren().iterator();
		while (childen.hasNext())
			 removePageListenerFromChildren((EditPart) childen.next());
	}
	protected void createLayoutEditPolicy() {
		installEditPolicy(
			EditPolicy.LAYOUT_ROLE,
			new JTabbedPaneLayoutEditPolicy(EditDomain.getEditDomain(this)));
	}
	/*
	 * Create an EditPartListener for itself and its pages so it knows 
	 * when a page has been added, removed, or selected
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
				if (editpart == null || editpart == JTabbedPaneGraphicalEditPart.this)
					return;
				// Find the page where this editpart resides and bring the page to the
				// front if isn't already.
				if ((editpart != null)
					&& (editpart.getSelected() == EditPart.SELECTED
						|| editpart.getSelected() == EditPart.SELECTED_PRIMARY)) {
					EditPart page = getPageOfSelectedEditpart(editpart);
					if (page != null && page.getModel() != fSelectedPage){
						// First hide the previously selected page and hide any grids if their on
						EditPart currentPage = getEditPartFromModel(fSelectedPage);
						setPageVisible(currentPage, false);
						BeanAwtUtilities.hideGrids(currentPage);
						// Then show the newly selected page
						setPageVisible(page, true);
						pageSelected(page);
					}
				}
			}
		};
	}
	/*
	 * Search through the JTabbedPane's pages (children) to find the page
	 * that matches the page model that is selected.
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
	 * Return the proxy adapter associated with this JTabbedPane.
	 */
	protected JTabbedPaneProxyAdapter getJTabbedPaneProxyAdapter() {
		if (tabbedpaneAdapter == null) {
			IBeanProxyHost tabbedpaneProxyHost =
				BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) getModel());
			tabbedpaneAdapter = (JTabbedPaneProxyAdapter) tabbedpaneProxyHost;
		}
		return tabbedpaneAdapter;
	}
	/*	
	 * Model children is the tabs feature.
	 * However, this returns the JTabComponents, but we want to return instead
	 * the components themselves. They are the "model" that gets sent to the createChild and
	 * component edit part.
	 */
	protected List getModelChildren() {
		List jtabComponents =
			(List) ((EObject) getModel()).eGet(sfTabs);
		ArrayList children = new ArrayList(jtabComponents.size());
		Iterator itr = jtabComponents.iterator();
		while (itr.hasNext()) {
			EObject component = (EObject) itr.next();
			// Get the component out of the JTabComponent
			children.add(component.eGet(sfComponent));
		}
		return children;
	}

	/*
	 * If the parent of this editpart is the JTabbedPane, we're on the page.
	 * If not recursely call up through the parent chain until we find the 
	 * editpart (page) that the original editpart was found in.
	 */
	protected EditPart getPageOfSelectedEditpart(EditPart ep) {
		if (ep == null || ep.getParent() == this)
			return ep;
		return getPageOfSelectedEditpart(ep.getParent());
	}
	
	/*
	 * This page has been selected or deselected. 
	 * Make it and all it's children visible or invisible.
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
		sfTabs = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABBEDPANE_TABS);
		sfComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABCOMPONENT_COMPONENT);				
	}	
	protected void setListener(EditPartListener listener) {
		if (this.pageListener != null)
			removeEditPartListener(this.pageListener);
		this.pageListener = listener;
		if (this.pageListener != null)
			addEditPartListener(this.pageListener);
	}
	/*
	 * The selected page of the JTabbedPane has changed. Bring this page to the front.
	 */
	protected void pageSelected(EditPart page) {
		fSelectedPage = (IJavaObjectInstance)page.getModel(); // save for later checks... see createPageListener()
		fSelectingPage = true; // set this so we can ignore the proxy adapter when it notifies us later in pageSelected()
		getJTabbedPaneProxyAdapter().setSelectedComponent((IJavaObjectInstance)page.getModel());
		fSelectingPage = false;
	}
	
	/**
	 * Get current page index.
	 */
	public int getCurrentPageIndex() {
		List children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			if (((EditPart)children.get(i)).getModel() == fSelectedPage)
				return i;
		}
		return -1;
	}
		
	/**
	 * Select the next page
	 */
	public void selectNextPage() {
		if (fSelectedPage != null) {
			List children = getChildren();
			int cp = getCurrentPageIndex();
			if (++cp < children.size()) {
				EditPart nextpage = (EditPart)children.get(cp);
				selectPage(nextpage);
			}
		}
	}
	
	/**
	 * Select the previous page
	 */
	public void selectPreviousPage() {
		if (fSelectedPage != null) {
			List children = getChildren();
			int cp = getCurrentPageIndex();
			if (--cp >= 0) {
				EditPart prevpage = (EditPart)children.get(cp);
				selectPage(prevpage);
			}
		}
	}
	
	/**
	 * Select the page passed in.
	 */
	public void selectPage(EditPart page) {
		getRoot().getViewer().setSelection(new StructuredSelection(page));		
	}
	/**
	 * @see org.eclipse.ve.internal.jfc.core.ContainerGraphicalEditPart#setPropertySource(ComponentGraphicalEditPart, EObject)
	 */
	protected void setPropertySource(ComponentGraphicalEditPart childEP, EObject child) {
		childEP.setPropertySource((IPropertySource) EcoreUtil.getRegisteredAdapter(InverseMaintenanceAdapter.getFirstReferencedBy(child, sfComponent), IPropertySource.class));	// This is the property source of the actual model which is part of the JTabComponent.
	}

}
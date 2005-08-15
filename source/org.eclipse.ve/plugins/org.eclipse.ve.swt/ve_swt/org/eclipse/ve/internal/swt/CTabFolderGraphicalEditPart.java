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
 *  $Revision: 1.7 $  $Date: 2005-08-15 21:05:07 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.*;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IErrorNotifier;
import org.eclipse.ve.internal.cde.emf.*;

import org.eclipse.ve.internal.java.core.*;

/**
 * 
 * @since 1.1
 */
public class CTabFolderGraphicalEditPart extends CompositeGraphicalEditPart {

	private EReference sf_items, sf_ctabItemControl;

	protected CTabFolderProxyAdapter ctabFolderProxyAdapter;

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
			getCTabFolderProxyAdapter().revalidateBeanProxy();
		}

		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sf_items)
				queueExec(CTabFolderGraphicalEditPart.this, "ITEMS"); //$NON-NLS-1$
		}
	};

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

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		ResourceSet rset = ((EObject) model).eResource().getResourceSet();
		sf_items = JavaInstantiation.getReference(rset, SWTConstants.SF_CTABFOLDER_ITEMS);
		sf_ctabItemControl = JavaInstantiation.getReference(rset, SWTConstants.SF_CTABITEM_CONTROL);
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
			if (tabitem.eGet(sf_ctabItemControl) != null)
				children.add(tabitem.eGet(sf_ctabItemControl));
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
			getCTabFolderProxyAdapter().setSelection(getTabForChild(fSelectedItem));
		}
	}
	
	/*
	 * The CTabFolder has been selected.  If a CTabItem has been clicked on switch to that tab.
	 */
	protected void switchToTab(Point p){
		if (p != null) {
			ResourceSet rset = EMFEditDomainHelper.getResourceSet(EditDomain.getEditDomain(this));
			IJavaObjectInstance point = 
				(IJavaObjectInstance)BeanUtilities.createJavaObject("org.eclipse.swt.graphics.Point", rset,  //$NON-NLS-1$
						"new org.eclipse.swt.graphics.Point(" + p.x + "," + p.y + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			int newTab = getCTabFolderProxyAdapter().getCTabItemFromLocation(point);
			
			if(newTab == -1)
				return;
			
			List children = getChildren();
            if(children != null){
            	if(newTab < children.size())
            		pageSelected((EditPart) children.get(newTab));
            }
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
	protected CTabFolderProxyAdapter getCTabFolderProxyAdapter() {
		if (ctabFolderProxyAdapter == null) {
			IBeanProxyHost ctabFolderProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) getModel());
			ctabFolderProxyAdapter = (CTabFolderProxyAdapter) ctabFolderProxyHost;
		}
		return ctabFolderProxyAdapter;
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
	 * 
	 * @param child
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected IJavaObjectInstance getTabForChild(EObject child) {
		return (IJavaObjectInstance) InverseMaintenanceAdapter.getIntermediateReference((EObject) getModel(), sf_items, sf_ctabItemControl, child);
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

	/**
	 * Use the drag tracker to determine when a mouse down is pressed, but the tool is not dragging.
	 * This means that the CTabFolder has been selected and we need to check if a tab was clicked on, and
	 * if so, change to the tab.
	 * 
	 * @since 1.1.0.1
	 */
   public DragTracker getDragTracker(Request request) {
	   DragTracker tracker = new org.eclipse.gef.tools.DragEditPartsTracker(this) {
		   protected boolean handleButtonUp(int button) {
			   if (getState() == AbstractTool.STATE_DRAG && !(getState() == AbstractTool.STATE_DRAG_IN_PROGRESS)) {
				   Rectangle bounds = CTabFolderGraphicalEditPart.this.getFigure().getBounds();
				   Point mouseLocation = getLocation().getCopy();
				   CTabFolderGraphicalEditPart.this.getFigure().translateToRelative(mouseLocation);
				   mouseLocation.translate(0 - bounds.x, 0 - bounds.y);
				   
				   // call method to select tab at mouseLocation
				   switchToTab(mouseLocation);
				   return true;
			   }
			   return super.handleButtonUp(button);
		   }
	   };
	   return tracker;
   }
   
}
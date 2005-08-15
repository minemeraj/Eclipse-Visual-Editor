/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: JTabbedPaneGraphicalEditPart.java,v $ $Revision: 1.10 $ $Date: 2005-08-15 17:59:49 $
 */
package org.eclipse.ve.internal.jfc.core;

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
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.*;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;

import org.eclipse.ve.internal.java.core.*;

/**
 * Graphical edit part for handling JTabbedPane in the Graph viewer
 * 
 * Since we can't really select the individual tabs on the JTabbedPane, we depend on the tree editpart in the outline viewer for selecting the pages
 * or components on those pages.
 * 
 * When the tree editpart is selected, this editpart is also selected and the JTabbedPaneProxyAdapter is notified so the page can be brought forward.
 * When this occurs, the previously viewed page figures are hidden and the currently selected page is made visible. If we didn't hide the editparts
 * from the other pages, the figure rectangle outlines will "bleed" through and show on the top page.
 */
public class JTabbedPaneGraphicalEditPart extends ContainerGraphicalEditPart {

	private EditPartListener pageListener;

	protected IJavaObjectInstance fSelectedPage;

	protected boolean fSelectingPage = false;

	protected JTabbedPaneProxyAdapter tabbedpaneAdapter;

	protected EReference sfTabs, sfComponent;
	protected final static String JTABBEDPANE_SELECTED_PAGE_STATE_KEY = "JTabbedPane.selected.page.state.key"; //$NON-NLS-1$
	public static final String JTABBEDPANE_THIS_PART = "JTabbedPane_THIS_PART"; //$NON-NLS-1$
	
	public JTabbedPaneGraphicalEditPart(Object model) {
		super(model);
	}

	private Adapter containerAdapter = new EditPartAdapterRunnable(this) {
		protected void doRun() {
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
			// Now we need to run through the children and set the Property source/error notifier correctly.
			// This is needed because the child could of been removed and then added back in with
			// a different ConstraintComponent BEFORE the refresh could happen. In that case GEF
			// doesn't see the child as being different so it doesn't create a new child editpart, and
			// so we don't get the new property source that we should. We didn't keep a record of which
			// one changed, so we just touch them all.
			List children = getChildren();
			int s = children.size();
			for (int i = 0; i < s; i++) {
				EditPart ep = (EditPart) children.get(i);
				try {
					setupComponent((ComponentGraphicalEditPart) ep, (EObject) ep.getModel());
				} catch (ClassCastException e) {
					// For the rare case it is not a component graphical editpart such as undefined class.
				}
			}
		}
		
		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sfTabs)
				queueExec(JTabbedPaneGraphicalEditPart.this, "TABS"); //$NON-NLS-1$
		}
	};

	public void activate() {
		setListener(createPageListener());
		super.activate();
		((EObject) getModel()).eAdapters().add(containerAdapter);
		List children = getChildren();
		if (!children.isEmpty()) {
			// In case there was a reload, get the last selected page and set it.
			EditPart lastSelectedPage = getLastSelectedPage();
			if (lastSelectedPage == null)
				// By default pick the first page
				lastSelectedPage = (EditPart) children.get(0);
			for (int i = 0; i < children.size(); i++) {
				EditPart page = (EditPart) children.get(i);
				addPageListenerToChildren(page);
				if (page == lastSelectedPage) {
					setPageVisible(page, true);
					pageSelected(page);
				} else {
					setPageVisible(page, false);
				}
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
		// Store the last selected page in case we do a reload we can restore it
		setLastSelectedPage();
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
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new JTabbedPaneLayoutEditPolicy(EditDomain.getEditDomain(this)));
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
				if (editpart == null || editpart == JTabbedPaneGraphicalEditPart.this)
					return;
				// Find the page where this editpart resides and bring the page to the
				// front if isn't already.
				if ((editpart != null) && (editpart.getSelected() == EditPart.SELECTED || editpart.getSelected() == EditPart.SELECTED_PRIMARY)) {
					EditPart page = getPageOfSelectedEditpart(editpart);
					if (page != null && page.getModel() != fSelectedPage) {
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
	 * Return the proxy adapter associated with this JTabbedPane.
	 */
	protected JTabbedPaneProxyAdapter getJTabbedPaneProxyAdapter() {
		if (tabbedpaneAdapter == null) {
			IBeanProxyHost tabbedpaneProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) getModel());
			tabbedpaneAdapter = (JTabbedPaneProxyAdapter) tabbedpaneProxyHost;
		}
		return tabbedpaneAdapter;
	}

	/*
	 * Model children is the tabs feature. However, this returns the JTabComponents, but we want to return instead the components themselves. They
	 * are the "model" that gets sent to the createChild and component edit part.
	 */
	protected List getModelChildren() {
		List jtabComponents = (List) ((EObject) getModel()).eGet(sfTabs);
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
	 * If the parent of this editpart is the JTabbedPane, we're on the page. If not recursely call up through the parent chain until we find the
	 * editpart (page) that the original editpart was found in.
	 */
	protected EditPart getPageOfSelectedEditpart(EditPart ep) {
		if (ep == null || ep.getParent() == this)
			return ep;
		return getPageOfSelectedEditpart(ep.getParent());
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
		fSelectedPage = (IJavaObjectInstance) page.getModel(); // save for later checks... see createPageListener()
		fSelectingPage = true; // set this so we can ignore the proxy adapter when it notifies us later in pageSelected()
		getJTabbedPaneProxyAdapter().setSelectedComponent((IJavaObjectInstance) page.getModel());
		fSelectingPage = false;
	}
	
	/*
	 * The JTabbedPane has been selected.  If a tab has been clicked on switch to that tab.
	 */
	protected void switchToTab(Point p){
		if (p != null) {
			ResourceSet rset = EMFEditDomainHelper.getResourceSet(EditDomain.getEditDomain(this));
			IJavaDataTypeInstance xpos = 
				(IJavaDataTypeInstance)BeanUtilities.createJavaObject("int", rset,  //$NON-NLS-1$
						String.valueOf(p.x));
			IJavaDataTypeInstance ypos = 
				(IJavaDataTypeInstance)BeanUtilities.createJavaObject("int", rset,  //$NON-NLS-1$
						String.valueOf(p.y));
			
			int newTab = getJTabbedPaneProxyAdapter().getTabItemFromLocation(xpos, ypos);
			
			if(newTab == -1)
				return;
			
			List children = getChildren();
            if(children != null){
            	if(newTab < children.size()){
            		EditPart oldPage = getEditPartFromModel(fSelectedPage);
            		EditPart newPage = (EditPart) children.get(newTab); 
            		
            		setPageVisible(oldPage, false);
            		pageSelected(newPage);
            		setPageVisible(newPage, true);
            	}
            }
		}
	}

	/**
	 * Get current page index.
	 */
	public int getCurrentPageIndex() {
		List children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			if (((EditPart) children.get(i)).getModel() == fSelectedPage)
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
				EditPart nextpage = (EditPart) children.get(cp);
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
				EditPart prevpage = (EditPart) children.get(cp);
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

	protected void setupComponent(ComponentGraphicalEditPart childEP, EObject child) {
		EObject tab = InverseMaintenanceAdapter.getIntermediateReference((EObject) getModel(), sfTabs, sfComponent, child);
		// This is the property source of the actual child, which is the tab.
		if (tab != null) {
			childEP.setPropertySource((IPropertySource) EcoreUtil.getRegisteredAdapter(tab, IPropertySource.class));
			childEP.setErrorNotifier((IErrorNotifier) EcoreUtil.getExistingAdapter(tab, IErrorNotifier.ERROR_NOTIFIER_TYPE));			
		} else {
			childEP.setPropertySource(null);
			childEP.setErrorNotifier(null);
		}
	}

	/**
	 * Somewhat of a hack here to persist the state of a which 
	 * page of the JTabbedPane was last selected so we can switch
	 * to this page if a reload from scratch occurs.
	 * Store the info in a HashMap in the EditDomain using the annotation name 
	 * of the JTabbedPane as the key and the annotation name of the selected page
	 * as the value.
	 */
	protected void setLastSelectedPage() {
		if (fSelectedPage == null)
			return;
		EditDomain domain = EditDomain.getEditDomain(this);
		HashMap selectedPageStateData = (HashMap) domain.getData(JTABBEDPANE_SELECTED_PAGE_STATE_KEY);
		if (selectedPageStateData == null)
			selectedPageStateData = new HashMap(2);
		AnnotationLinkagePolicy policy = domain.getAnnotationLinkagePolicy();
		Annotation ann = policy.getAnnotation(getModel());
		if (ann != null) {
			String key = (String) ann.getKeyedValues().get(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
			// If no annotation name, the tabbed pane must be the root part... use special name.
			if (key == null)
				key = JTABBEDPANE_THIS_PART;
			// Now get the annotation name of the current page
			ann = policy.getAnnotation(fSelectedPage);
			if (ann != null) {
				String value = (String) ann.getKeyedValues().get(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
				if (value != null) {
					selectedPageStateData.put(key, value);
					domain.setData(JTABBEDPANE_SELECTED_PAGE_STATE_KEY, selectedPageStateData);
				}
			}
		}
	}
	protected EditPart getLastSelectedPage() {
		EditDomain domain = EditDomain.getEditDomain(this);
		HashMap selectedPageStateData = (HashMap) domain.getData(JTABBEDPANE_SELECTED_PAGE_STATE_KEY);
		if (selectedPageStateData == null)
			return null;
		AnnotationLinkagePolicy policy = domain.getAnnotationLinkagePolicy();
		Annotation ann = policy.getAnnotation(getModel());
		if (ann != null) {
			String key = (String) ann.getKeyedValues().get(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
			// If no annotation name, the tabbed pane must be the root part... use special name.
			if (key == null)
				key = JTABBEDPANE_THIS_PART;
			String selectedPageName = (String) selectedPageStateData.get(key);
			if (selectedPageName != null) {
			// Look through the tabbed pane's children to find the corresponding editpart that matches this annotation name
				List children = getChildren();
				for (Iterator iter = children.iterator(); iter.hasNext();) {
					EditPart ep = (EditPart) iter.next();
					ann = policy.getAnnotation(ep.getModel());
					if (ann != null) {
						String value = (String) ann.getKeyedValues().get(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
						if (value.equals(selectedPageName))
							return ep;
					}
				}
				// Hmmm... there was an element in the edit domain for this tabbed pane but no child found
				// to match this annotation name. Remove the key in order to clean it up.
				selectedPageStateData.remove(key);
			}
		}
		return null;
	}
	
	/**
	 * Use the drag tracker to determine when a mouse down is pressed, but the tool is not dragging.
	 * 
	 * 
	 * @since 1.1.0.1
	 */
   public DragTracker getDragTracker(Request request) {
	   DragTracker tracker = new org.eclipse.gef.tools.DragEditPartsTracker(this) {
		   protected boolean handleButtonUp(int button) {
			   if (getState() == AbstractTool.STATE_DRAG && !(getState() == AbstractTool.STATE_DRAG_IN_PROGRESS)) {
				   Rectangle r = JTabbedPaneGraphicalEditPart.this.getFigure().getBounds();
				   Point mouseLocation = getLocation().getTranslated(0 - r.x, 0 - r.y);
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

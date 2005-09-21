/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: ContainerGraphicalEditPart.java,v $ $Revision: 1.20 $ $Date: 2005-09-21 10:39:46 $
 */
package org.eclipse.ve.internal.jfc.core;

import java.util.*;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.*;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.visual.*;

/**
 * ViewObject for the awt Container. Creation date: (2/16/00 3:45:46 PM) @author: Joe Winchester
 */
public class ContainerGraphicalEditPart extends ComponentGraphicalEditPart {

	public ContainerGraphicalEditPart(Object model) {
		super(model);
	}

	protected ContainerPolicy getContainerPolicy() {
		return new ContainerPolicy(EditDomain.getEditDomain(this)); // AWT standard Contained Edit Policy
	}

	protected IFigure createFigure() {
		ContentPaneFigure cf = (ContentPaneFigure) super.createFigure();
		cf.getContentPane().setLayoutManager(new XYLayout());
		return cf;
	}

	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(VisualComponentsLayoutPolicy.LAYOUT_POLICY, new VisualComponentsLayoutPolicy(false)); // This is a special policy that just
		// handles the size/position of visual
		// components wrt/the figures. It does not
		// handle changing size/position.
		createLayoutEditPolicy();
	}

	/**
	 * Because java.awt.Container can vary its layout manager we need to use the correct layout input policy for the layout manager that is
	 * calculated by a factory
	 */
	protected void createLayoutEditPolicy() {
		// Need to create the correct layout input policy depending upon the layout manager setting.
		EditPolicy layoutPolicy = null;
		// Get the layout input policy class from the layout policy factory
		IBeanProxy containerProxy = getComponentProxy().getBeanProxy();
		if (containerProxy != null) {
			// See if we have a layout manager set - if so use it to find the policy factory
			IJavaInstance layoutManager = (IJavaInstance)getBean().eGet(sf_containerLayout);
			ILayoutPolicyFactory lpFactory = null;
			if(layoutManager != null){
				lpFactory = BeanAwtUtilities.getLayoutPolicyFactoryFromLayoutManager(layoutManager,EditDomain.getEditDomain(this));
			} else {
				IBeanProxy layoutManagerProxy = BeanAwtUtilities.invoke_getLayout(getComponentProxy().getBeanProxy()); 
				lpFactory = BeanAwtUtilities.getLayoutPolicyFactoryFromLayoutManger(layoutManagerProxy, EditDomain.getEditDomain(this));				
			}			
			layoutPolicy = lpFactory.getLayoutEditPolicy(getContainerPolicy());
		}
		// If the LayoutPolicyFactory didn't specifiy a LayoutInputPolicy, use UnknownLayoutInputPolicy
		if (layoutPolicy == null) {
			layoutPolicy = new UnknownLayoutInputPolicy(getContainerPolicy());
		}
		removeEditPolicy(EditPolicy.LAYOUT_ROLE); // Get rid of old one, if any Layout policies put figure decorations for things like grids so we
		// should remove this
		installEditPolicy(EditPolicy.LAYOUT_ROLE, layoutPolicy);
	}

	protected List getModelChildren() {
		// Model children is the components feature. However, this returns the constraint components, but we want to return instead the components
		// themselves. They are the "model" that gets sent to the createChild and component edit part.
		List constraintChildren = (List) ((EObject) getModel()).eGet(sf_containerComponents);
		ArrayList children = new ArrayList(constraintChildren.size());
		Iterator itr = constraintChildren.iterator();
		while (itr.hasNext()) {
			EObject con = (EObject) itr.next();
			IJavaInstance component = (IJavaInstance) con.eGet(sf_constraintComponent); // Get the component out of
			// the constraint
			if (component != null) {
				// It was added. It would be null if it couldn't be added for some reason
				children.add(component);
			}
		}
		return children;
	}

	private Adapter containerAdapter = new EditPartAdapterRunnable(this) {

		protected void doRun() {
			refreshChildren();
			// Now we need to run through the children and set the Property source/Error ComponentManagerFeedbackControllerNotifier correctly.
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
					// For the rare case not a component graphical editpart, such as undefined class.
				}
			}
		}
		
		public void notifyChanged(Notification notification) {
			if (notification.getFeature() == sf_containerComponents) {
				queueExec(ContainerGraphicalEditPart.this, "COMPONENTS"); //$NON-NLS-1$
			} else if (notification.getFeature() == sf_containerLayout) {
				queueExec(ContainerGraphicalEditPart.this, "LAYOUT", new EditPartRunnable(getHost()) { //$NON-NLS-1$

					protected void doRun() {
						createLayoutEditPolicy();
					}
				});
			}
		}
	};

	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(containerAdapter);
	}

	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(containerAdapter);
	}

	private EReference sf_containerLayout, sf_constraintComponent, sf_containerComponents;

	protected EditPart createChild(Object model) {
		EditPart ep = super.createChild(model);
		try {
			ComponentGraphicalEditPart componentGraphicalEditPart = (ComponentGraphicalEditPart) ep;
			setupComponent(componentGraphicalEditPart, (EObject) model);
			componentGraphicalEditPart.setTransparent(true); // So that it doesn't create an image, we subsume it here.
		} catch (ClassCastException e) {
			// For the rare case not a component graphical edit part, such as undefined class.
		}
		return ep;
	}

	protected void setupComponent(ComponentGraphicalEditPart childEP, EObject child) {
		EObject componentConstraintObject = InverseMaintenanceAdapter.getIntermediateReference((EObject) getModel(), sf_containerComponents, sf_constraintComponent, child);
		if (componentConstraintObject != null) {
			childEP.setPropertySource((IPropertySource) EcoreUtil.getRegisteredAdapter(componentConstraintObject, IPropertySource.class)); // This is the property source of the actual model which is part of the constraintComponent.
			childEP.setErrorNotifier((IErrorNotifier) EcoreUtil.getExistingAdapter(componentConstraintObject, IErrorNotifier.ERROR_NOTIFIER_TYPE));
		} else {
			childEP.setPropertySource(null);	// No CC.
			childEP.setErrorNotifier(null);
		}
	}
	/*
	 * Provide a SnapToGrid helper if the option is set and this has a gridcontroller
	 */
	public Object getAdapter(Class type) {
		if (type == SnapToHelper.class) {
			EditPartViewer viewer = getRoot().getViewer();
			Object snapToGrid = viewer.getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
			if (snapToGrid != null && ((Boolean) snapToGrid).booleanValue()) {
				GridController gridController = GridController.getGridController(this);
				if (gridController != null) {
					viewer.setProperty(SnapToGrid.PROPERTY_GRID_SPACING, new Dimension(gridController.getGridWidth(), gridController.getGridHeight()));
					int margin = gridController.getGridMargin();
					viewer.setProperty(SnapToGrid.PROPERTY_GRID_ORIGIN, new Point(getFigure().getBounds().x + margin, getFigure().getBounds().y + margin));
					return new SnapToGrid(this);
				}
			} 
		} else if (type == LayoutList.class) {
			return new LayoutList(){
				public void fillMenuManager(MenuManager aMenuManager) {
					LayoutListMenuContributor layoutListMenuContributor = new LayoutListMenuContributor(){			
						protected EditPart getEditPart() {
							return ContainerGraphicalEditPart.this;
						}
						protected IJavaInstance getBean() {
							return ContainerGraphicalEditPart.this.getBean();
						}
						protected EStructuralFeature getLayoutSF() {
							return sf_containerLayout;
						}
						protected IBeanProxy getLayoutBeanProxyAdapter() {
							return BeanAwtUtilities.invoke_getLayout(BeanProxyUtilities.getBeanProxy(getBean()));
						}
						protected IJavaInstance getNewLayoutInstance(String layoutTypeName) {
							JavaClass javaClass = Utilities.getJavaClass(layoutTypeName,getBean().eResource().getResourceSet());
							ILayoutPolicyFactory factory =
								BeanAwtUtilities.getLayoutPolicyFactoryFromLayoutManger(javaClass, getEditDomain());
							return factory.getLayoutManagerInstance((IJavaObjectInstance) getBean(), javaClass, getBean().eResource().getResourceSet());		
						}
						protected String[][] getLayoutItems() {
							return LayoutManagerCellEditor.getLayoutManagerItems(getEditDomain());
						}
						protected ILayoutPolicyFactory getLayoutPolicyFactory(JavaClass layoutManagerClass) {
							return BeanAwtUtilities.getLayoutPolicyFactoryFromLayoutManger(layoutManagerClass, getEditDomain());
						}
						protected VisualContainerPolicy getVisualContainerPolicy() {
							return getContainerPolicy();
						}
						protected String getPreferencePageID() {
							return JFCVisualPlugin.PREFERENCE_PAGE_ID;
						}
					};
					layoutListMenuContributor.fillMenuManager(aMenuManager);					
				}
			};	
		}
		return super.getAdapter(type);
	}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		ResourceSet rset = ((IJavaObjectInstance) model).eResource().getResourceSet();
		sf_containerLayout = JavaInstantiation.getReference(rset, JFCConstants.SF_CONTAINER_LAYOUT);
		sf_constraintComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_CONSTRAINT_COMPONENT);
		sf_containerComponents = JavaInstantiation.getReference(rset, JFCConstants.SF_CONTAINER_COMPONENTS);
	}
}

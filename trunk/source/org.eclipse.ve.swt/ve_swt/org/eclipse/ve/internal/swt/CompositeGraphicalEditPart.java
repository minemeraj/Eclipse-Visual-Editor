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
 * $RCSfile: CompositeGraphicalEditPart.java,v $ $Revision: 1.34 $ $Date: 2006-01-24 14:26:31 $
 */

package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.*;
import org.eclipse.jface.action.MenuManager;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

import org.eclipse.ve.internal.java.core.IBeanProxyHost;
import org.eclipse.ve.internal.java.visual.*;

/**
 * ViewObject for the swt Composite. Creation date: (2/16/00 3:45:46 PM)
 * 
 * @author: Joe Winchester
 */
public class CompositeGraphicalEditPart extends ControlGraphicalEditPart {

	private EReference sf_compositeLayout, sf_compositeControls;

	public CompositeGraphicalEditPart(Object model) {
		super(model);
	}

	protected VisualContainerPolicy getContainerPolicy() {
		return new CompositeContainerPolicy(EditDomain.getEditDomain(this)); // SWT standard Composite/Container Edit Policy
	}

	protected IFigure createFigure() {
		ContentPaneFigure cf = (ContentPaneFigure) super.createFigure();
		cf.getContentPane().setLayoutManager(new XYLayout());
		return cf;
	}

	protected void createEditPolicies() {
		super.createEditPolicies();

		// Any occurences of the {formToolkit} token are replaced by a reference to a real FormToolkit instance
		installEditPolicy(FormToolkitEditPolicy.class,new FormToolkitEditPolicy());

		installEditPolicy(VisualComponentsLayoutPolicy.LAYOUT_POLICY, new VisualComponentsLayoutPolicy(false));
		// This is a special policy that just
		// handles the size/position of visual
		// components wrt/the figures. It does not
		// handle changing size/position.
		createLayoutEditPolicy();
	}

	protected EditPart createChild(Object model) {
		EditPart ep = super.createChild(model);
		try {
			ControlGraphicalEditPart controlep = (ControlGraphicalEditPart) ep;
			controlep.setTransparent(true); // So that it doesn't create an image, we subsume it here.
			setupControl(controlep, (EObject) model);
		} catch (ClassCastException e) {
			// For the rare times that it is not a ControlGraphicalEditPart (e.g. undefined).
		}
		return ep;
	}
	
	protected CompositeProxyAdapter getCompositeProxyAdapter() {
		return (CompositeProxyAdapter) EcoreUtil.getExistingAdapter((Notifier) getModel(), IBeanProxyHost.BEAN_PROXY_TYPE);
	}
	
	protected void setupControl(ControlGraphicalEditPart childEP, EObject child) {
		CompositeProxyAdapter compositeProxyAdapter = getCompositeProxyAdapter();
		if (compositeProxyAdapter.isBeanProxyInstantiated())
			childEP.setErrorNotifier(compositeProxyAdapter.getControlLayoutDataAdapter(child).getErrorNotifier());
	}

	/**
	 * Because org.eclipse.swt.widgets.Composite can vary its layout manager we need to use the correct layout input policy for the layout manager
	 * that is calculated by a factory.
	 * 
	 * @since 1.1.0
	 */
	protected void createLayoutEditPolicy() {

		EditPolicy layoutPolicy = null;
		CompositeProxyAdapter compositeBeanProxyAdapter = getCompositeProxyAdapter();
		if (compositeBeanProxyAdapter.getBeanProxy() == null)
			return;
		// See the layout of the composite to determine the edit policy
		IBeanProxy layoutBeanProxy = BeanSWTUtilities.invoke_getLayout(compositeBeanProxyAdapter.getBeanProxy());
		// If the layoutBeanProxy is null then we use the null layout edit policy
		if (layoutBeanProxy == null) {
			layoutPolicy = new NullLayoutEditPolicy(getContainerPolicy(), compositeBeanProxyAdapter);
		} else {
			// Get the layoutPolicyFactory
			ILayoutPolicyFactory layoutPolicyFactory = BeanSWTUtilities.getLayoutPolicyFactory(compositeBeanProxyAdapter.getBeanProxy(), EditDomain
					.getEditDomain(this));
			layoutPolicy = layoutPolicyFactory.getLayoutEditPolicy(getContainerPolicy());
		}
		if (layoutPolicy == null)
			layoutPolicy = new DefaultLayoutEditPolicy(getContainerPolicy());

		// Only install the newer layout policy if they aren't the same
		if (getEditPolicy(EditPolicy.LAYOUT_ROLE) == null || (!(getEditPolicy(EditPolicy.LAYOUT_ROLE).getClass()).equals(layoutPolicy.getClass()))) {
			removeEditPolicy(EditPolicy.LAYOUT_ROLE); // Get rid of old one, if any
			// Layout policies put figure decorations for things like grids so we should remove this
			installEditPolicy(EditPolicy.LAYOUT_ROLE, layoutPolicy);
		}
	}

	protected List getModelChildren() {
		return (List) ((EObject) getModel()).eGet(sf_compositeControls);
	}

	/*
	 * When the controls relationship is updated refresh the children, and when the layout property is updated recalculate the edit policy for the
	 * specific layout
	 */
	private Adapter compositeAdapter = new EditPartAdapterRunnable(this) {

		protected void doRun() {
			refreshChildren();
		}

		public void notifyChanged(Notification notification) {
			if (notification.getFeature() == sf_compositeControls)
				queueExec(CompositeGraphicalEditPart.this, "CONTROLS"); //$NON-NLS-1$
			else if (notification.getFeature() == sf_compositeLayout) {
				queueExec(CompositeGraphicalEditPart.this, "LAYOUT", new EditPartRunnable(getHost()) { //$NON-NLS-1$
					protected void doRun() {
						createLayoutEditPolicy();
					}
				});
			}
		}
	};

	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(compositeAdapter);
	}

	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(compositeAdapter);
	}

	public void setModel(Object model) {
		super.setModel(model);
		IJavaObjectInstance javaModel = (IJavaObjectInstance) model;
		if (javaModel.eResource() != null && javaModel.eResource().getResourceSet() != null) {
			ResourceSet rset = ((IJavaObjectInstance) model).eResource().getResourceSet();
			sf_compositeLayout = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_LAYOUT);
			sf_compositeControls = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_CONTROLS);
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
					Rectangle rect = getClientArea();
					viewer.setProperty(SnapToGrid.PROPERTY_GRID_ORIGIN, new Point(rect.x + margin, rect.y + margin));
					return new SnapToGrid(this);
				}
			}
			return null;
		} else if(type == LayoutList.class){
			return new LayoutList(){
				public void fillMenuManager(MenuManager aMenuManager) {
					LayoutListMenuContributor layoutListMenuContributor = new LayoutListMenuContributor(){
						protected EditPart getEditPart() {
							return CompositeGraphicalEditPart.this;
						}
						protected IJavaInstance getBean() {
							return CompositeGraphicalEditPart.this.getBean();
						}
						protected EStructuralFeature getLayoutSF() {
							return sf_compositeLayout;
						}
						
						protected IBeanProxy getLayoutBeanProxyAdapter() {
							return BeanSWTUtilities.invoke_getLayout(getCompositeProxyAdapter().getBeanProxy());
						}
						protected String[][] getLayoutItems() {
							return LayoutCellEditor.getLayoutItems(getEditDomain());
						}
						protected ILayoutPolicyFactory getLayoutPolicyFactory(JavaClass layoutManagerClass) {
							return BeanSWTUtilities.getLayoutPolicyFactoryFromLayout(layoutManagerClass, getEditDomain());
						}
						protected VisualContainerPolicy getVisualContainerPolicy() {
							return getContainerPolicy();
						}
						protected String getPreferencePageID() {
							return SwtPlugin.PREFERENCE_PAGE_ID;

						}
					};
					layoutListMenuContributor.fillMenuManager(aMenuManager);
				}
			};
		}
		return super.getAdapter(type);
	}
	protected Rectangle getClientArea() {
		// We want to modify the border so that it is down to the client area. Don't want the grid drawing outside of this.
		Rectangle clientArea = getCompositeProxyAdapter().getClientArea().getCopy();
		IFigure hostFigure = getFigure();
		clientArea = LayoutPolicyHelper.mapModelToFigure(getCompositeProxyAdapter(), hostFigure.getParent(), clientArea);
		if (!hostFigure.isCoordinateSystem()) {
			// It expects absolute. Don't ask why this works! I don't know.
			clientArea.translate(hostFigure.getBounds().getTopLeft());
		}
		return clientArea;
	};	

}

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
 *  $RCSfile: RootPaneContainerGraphicalEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import java.util.*;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.jem.internal.java.JavaClass;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;

/**
 * This is the graphical editpart for any RootPaneContainer, such as JFrame. There is
 * a Swing interface called RootPaneContainer, and this is the general edit part for it.
 */
public class RootPaneContainerGraphicalEditPart extends ComponentGraphicalEditPart  {
	private EStructuralFeature sf_contentPane;
	
	public RootPaneContainerGraphicalEditPart(Object model) {
		super(model);
	}

	private Adapter containerAdapter = new AdapterImpl() {
		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sf_contentPane)
				refreshChildren();
		}
	};
	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(containerAdapter);
		// The content pane of a RootPaneContainer doesn't have the RootPaneContainer as its
		// immediate parent. There is a RootPane in-between that we don't normally see.
		// However, for position information, we want it to look like it is a direct
		// child of the RootPaneContainer. To do this we ame the RootPaneContainer as the
		// parent component proxy host. 

		// This is to force in the implicit content pane and make it available as a mof setting.			
		getModelChildren();
	}
	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(containerAdapter);
	}

	/**
	 * Our logical child is the content pane.  We must create one for now if it is not there
	 */
	public List getModelChildren() {
		EObject model = (EObject) getModel();
		ArrayList result = new ArrayList();

		if (sf_contentPane != null) {
			if (model.eIsSet(sf_contentPane)) {
				// We have an explicit content pane, return it unless it is null
				// OR unless it doesn't have a beanProxy, i.e., is in error
				Object contentPane = model.eGet(sf_contentPane);
				if (contentPane != null && BeanProxyUtilities.getBeanProxy((IJavaInstance) contentPane) != null) {
					result.add(contentPane);
				}
			}
		}
			
		if (!result.isEmpty())
			return result;
		return Collections.EMPTY_LIST;
	}

	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(VisualComponentsLayoutPolicy.LAYOUT_POLICY, new VisualComponentsLayoutPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
	}
	
	protected EditPolicy createLayoutEditPolicy() {
		return new CDELayoutEditPolicy(new RootPaneContainerPolicy(EditDomain.getEditDomain(this)));
	}
	
	protected EditPart createChild(Object model) {
		EditPart ep = super.createChild(model);
		((ComponentGraphicalEditPart) ep).setTransparent(true);
		((ComponentGraphicalEditPart) ep).setPropertySource(new NonBoundsBeanPropertySource((EObject)model));
		// So that it doesn't create an image, we subsume it here.
		// The component proxy host of the child needs to know that we are its parent
		// otherwise it thinks that its immediate parent is ( which is a layout pane )
		// and the positions get all wrong
		IComponentProxyHost rootPaneContainerProxyAdapter =
			(IComponentProxyHost) BeanProxyUtilities.getBeanProxyHost((IJavaInstance) getModel());			
		IComponentProxyHost childProxyAdapter =
			(IComponentProxyHost) BeanProxyUtilities.getBeanProxyHost((IJavaInstance) model);
		childProxyAdapter.setParentComponentProxyHost(rootPaneContainerProxyAdapter);
				
		return ep;
	}
	protected IFigure createFigure() {
		IFigure fig = super.createFigure();
		fig.setLayoutManager(new XYLayout());
		return fig;
	}
	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		JavaClass modelType = (JavaClass) ((EObject) model).eClass();
		sf_contentPane = modelType.getEStructuralFeature("contentPane"); //$NON-NLS-1$
	}
}

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
 * $RCSfile: JScrollPaneGraphicalEditPart.java,v $ $Revision: 1.2 $ $Date: 2004-03-26 23:07:38 $
 */
package org.eclipse.ve.internal.jfc.core;

import java.util.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;

public class JScrollPaneGraphicalEditPart extends ContainerGraphicalEditPart {

	public JScrollPaneGraphicalEditPart(Object model) {
		super(model);
	}

	private Adapter containerAdapter = new EditPartAdapterRunnable() {
		public void run() {
			if (isActive())
				refreshChildren();
		}
		
		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sf_scrollpaneViewportView)
				queueExec(JScrollPaneGraphicalEditPart.this);
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

	protected EStructuralFeature sf_scrollpaneViewportView;

	/**
	 * The layoutEditPolicy deals with the fact that the child is a viewportView
	 */
	protected void createLayoutEditPolicy() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new JScrollPaneLayoutEditPolicy(EditDomain.getEditDomain(this)));
	}

	public List getModelChildren() {
		ArrayList result = new ArrayList(1);
		Object viewportView = ((EObject) getModel()).eGet(sf_scrollpaneViewportView);
		if (viewportView != null) {
			result.add(viewportView);
			return result;
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	protected EditPart createChild(Object model) {
		// We need to set the relative parent of the viewportView so it creates its location for the
		// the graph view editPart relative to us as its parent instead of some internal parent container
		IComponentProxyHost jScrollPaneProxyAdapter = (IComponentProxyHost) BeanProxyUtilities.getBeanProxyHost((IJavaInstance) getModel());
		IComponentProxyHost viewportViewProxyAdapter = (IComponentProxyHost) BeanProxyUtilities.getBeanProxyHost((IJavaInstance) model);
		if (viewportViewProxyAdapter != null) {
			viewportViewProxyAdapter.setParentComponentProxyHost(jScrollPaneProxyAdapter);
		}
		// Return the edit part
		return super.createChild(model);
	}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);

		sf_scrollpaneViewportView = JavaInstantiation.getSFeature((IJavaObjectInstance) model, JFCConstants.SF_JSCROLLPANE_VIEWPORTVIEW);
	}

	/**
	 * @see org.eclipse.ve.internal.jfc.core.ContainerGraphicalEditPart#setPropertySource(ComponentGraphicalEditPart, EObject)
	 */
	protected void setPropertySource(ComponentGraphicalEditPart childEP, EObject child) {
		childEP.setPropertySource(new NonBoundsBeanPropertySource(child));
	}

}
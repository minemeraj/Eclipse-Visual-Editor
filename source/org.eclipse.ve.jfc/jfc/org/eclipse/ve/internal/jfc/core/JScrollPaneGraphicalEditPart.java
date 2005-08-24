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
 * $RCSfile: JScrollPaneGraphicalEditPart.java,v $ $Revision: 1.11 $ $Date: 2005-08-24 23:38:09 $
 */
package org.eclipse.ve.internal.jfc.core;

import java.util.*;

import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.VisualComponentsLayoutPolicy;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

/**
 * JScrollPane graphical edit part.
 * <p>
 * Because Swing pushed into JScrollPane some JTable stuff (the headers are handled by the
 * scroll pane!) we need to handle JTable child view specially.
 * 
 * @since 1.1.0
 */
public class JScrollPaneGraphicalEditPart extends ContainerGraphicalEditPart {

	public JScrollPaneGraphicalEditPart(Object model) {
		super(model);
	}

	private Adapter containerAdapter = new EditPartAdapterRunnable(this) {

		protected void doRun() {
			refreshChildren();
		}

		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sf_scrollpaneViewportView)
				queueExec(JScrollPaneGraphicalEditPart.this, "SCROLLVIEW"); //$NON-NLS-1$
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

	/**
	 * Reinstall the standard layout policy to handle normal (i.e. not JTable) child view.
	 * 
	 * 
	 * @since 1.1.0
	 */
	protected void reinstallStandardLayoutPolicy() {
		installEditPolicy(VisualComponentsLayoutPolicy.LAYOUT_POLICY, new VisualComponentsLayoutPolicy(false));
		getContentPane().setLayoutManager(new XYLayout());
	}

	/**
	 * JScrollPane hosts JTable which has special behavior - with autoCreateColumnsFromModel=false and no columns its height is 0 which means it
	 * cannot be selected, and also its height can exceed the table. This creates a GEF figure that is either not selectable or else looks too deep
	 * Override this behavior by removing the layout policy, and changing the layout manager to be a stack layout so that the child will be same size
	 * as scrollpane.
	 * 
	 * 
	 * @since 1.1.0
	 */
	protected void installJTableLayoutPolicy() {
		removeEditPolicy(VisualComponentsLayoutPolicy.LAYOUT_POLICY);
		getContentPane().setLayoutManager(new StackLayout());
	}

	protected void addChild(EditPart child, int index) {
		super.addChild(child, index);
		// Now see if the child is JTable or not. From that install the correct layout policy.
		if (child instanceof JTableGraphicalEditPart)
			installJTableLayoutPolicy();
		else
			reinstallStandardLayoutPolicy();
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

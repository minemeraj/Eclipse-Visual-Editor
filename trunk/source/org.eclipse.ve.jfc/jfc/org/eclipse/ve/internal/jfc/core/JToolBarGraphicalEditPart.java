/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: JToolBarGraphicalEditPart.java,v $ $Revision: 1.5 $ $Date: 2004-08-27 15:34:48 $
 */
package org.eclipse.ve.internal.jfc.core;

import java.util.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPolicy;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

/**
 * GraphicalEditPart for javax.swing.JToolBar.
 */
public class JToolBarGraphicalEditPart extends ContainerGraphicalEditPart {
	protected EStructuralFeature sfItems;

	protected JavaClass classAction;

	public JToolBarGraphicalEditPart(Object model) {
		super(model);
	}

	/*
	 * Use a JToolBarLayoutPolicy which is a FlowLayout
	 */
	protected void createLayoutEditPolicy() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new JToolBarLayoutEditPolicy(this));
	}

	/*
	 * Get only Components... can't handle Actions right now allow though they will show up in the VM and be reflected back into the graph viewer.
	 */
	public List getModelChildren() {
		List items = (List) ((EObject) getModel()).eGet(sfItems);
		ArrayList children = new ArrayList();
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
			IJavaInstance item = (IJavaInstance) iter.next();
			if (!classAction.isInstance(item)) children.add(item);
		}
		return children;
	}

	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(toolbarAdapter);
	}

	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(toolbarAdapter);
	}

	private Adapter toolbarAdapter = new EditPartAdapterRunnable() {
		public void run() {
			if (isActive())
				refreshChildren();
		}
		
		public void notifyChanged(Notification notification) {
			if (notification.getFeature() == sfItems)
				queueExec(JToolBarGraphicalEditPart.this);
		}
	};

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		JavaClass modelType = (JavaClass) ((EObject) model).eClass();
		sfItems = modelType.getEStructuralFeature("items"); //$NON-NLS-1$
		ResourceSet rset = ((IJavaObjectInstance) model).eResource().getResourceSet();
		classAction = (JavaClass) Utilities.getJavaClass("javax.swing.Action", rset); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.ve.internal.jfc.core.ContainerGraphicalEditPart#setPropertySource(ComponentGraphicalEditPart, EObject)
	 */
	protected void setPropertySource(ComponentGraphicalEditPart childEP, EObject child) {
		childEP.setPropertySource(new NonBoundsBeanPropertySource(child));
	}

}

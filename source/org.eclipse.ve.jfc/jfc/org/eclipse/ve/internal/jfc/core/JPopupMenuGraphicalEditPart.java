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
 *  $RCSfile: JPopupMenuGraphicalEditPart.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 16:18:06 $ 
 */

import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPolicy;

import org.eclipse.jem.java.JavaClass;

/**
 * @author pwalker
 *
 * GraphicalEditPart for javax.swing.JPopupMenu's.
 * This is not currently used because we don't know how to create
 * the children for Actions and Strings.
 */
public class JPopupMenuGraphicalEditPart extends ContainerGraphicalEditPart {

	protected EStructuralFeature sfItems;

	/**
	 * Constructor for JPopupMenuTreeEditPart.
	 * @param model
	 */
	public JPopupMenuGraphicalEditPart(Object model) {
		super(model);
	}

	/**
	 * Use a JPopupMenuLayoutPolicy which is a FlowLayout
	 */
	protected void createLayoutEditPolicy() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new JPopupMenuLayoutEditPolicy(this));
	}

	public List getModelChildren() {
		return (List) ((EObject) getModel()).eGet(sfItems);
	}
	public void notifyChanged(
		Notifier notifier,
		int eventType,
		EObject sf,
		Object oldValue,
		Object newValue,
		int pos) {
		if (sf == sfItems)
			refreshChildren();
	}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		JavaClass modelType = (JavaClass) ((EObject) model).eClass();
		sfItems = modelType.getEStructuralFeature("items"); //$NON-NLS-1$
	}
}

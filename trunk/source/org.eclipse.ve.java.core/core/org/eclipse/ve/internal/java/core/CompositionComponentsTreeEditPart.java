package org.eclipse.ve.internal.java.core;
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
 *  $RCSfile: CompositionComponentsTreeEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.TreeContainerEditPolicy;
import org.eclipse.ve.internal.jcm.BeanComposition;
import org.eclipse.ve.internal.jcm.JCMPackage;
/**
 * Composition Graphical Edit Part that instantiates and disposes bean proxies
 */
public class CompositionComponentsTreeEditPart extends AbstractTreeEditPart {


	public CompositionComponentsTreeEditPart(Object model) {
		setModel(model);
	}
	
	protected List getModelChildren() {
		BeanComposition comp = (BeanComposition) getModel();
		return comp != null ? comp.getComponents() : Collections.EMPTY_LIST;
	}
	
	protected Adapter compositionAdapter = new AdapterImpl() {
		public void notifyChanged(Notification msg) {
			if (msg.getFeatureID(BeanComposition.class) == JCMPackage.BEAN_COMPOSITION__COMPONENTS)
				refreshChildren();
		}
	};
	
	public void activate() {
		super.activate();
		if (getModel() != null)
			((BeanComposition) getModel()).eAdapters().add(compositionAdapter);
	}
	
	public void deactivate() {
		super.deactivate();
		if (getModel() != null)
			((BeanComposition) getModel()).eAdapters().remove(compositionAdapter);
	}
	
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new TreeContainerEditPolicy(new CompositionContainerPolicy(EditDomain.getEditDomain(this))));
	}
}

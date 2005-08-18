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
 *  Created Aug 15, 2005 by Gili Mendel
 * 
 *  $RCSfile: DecorationsTreeEditPart.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-18 21:55:55 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

/**
 * Decorations Tree Editpart
 * 
 * @since 1.1.0.1
 */
public class DecorationsTreeEditPart extends CompositeTreeEditPart {

	protected EReference sf_menuBar;
	
	public DecorationsTreeEditPart(Object model) {
		super(model);
	}
	
	public void setModel(Object model) {
		super.setModel(model);

		ResourceSet rset = ((IJavaObjectInstance) model).eResource().getResourceSet();
		sf_menuBar = JavaInstantiation.getReference(rset, SWTConstants.SF_DECORATIONS_MENU_BAR);
	}
	
	protected Adapter decorationsAdapter = new EditPartAdapterRunnable(this) {
		protected void doRun() {
			refreshChildren();
		}

		public void notifyChanged(Notification notification) {
			if (notification.getFeature() == sf_menuBar)
				queueExec(DecorationsTreeEditPart.this, "MENUBAR"); //$NON-NLS-1$
		}
	};
	
	protected VisualContainerPolicy getContainerPolicy() {
		return new DecorationsContainerPolicy(EditDomain.getEditDomain(this));
	}

	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(decorationsAdapter);
	}

	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(decorationsAdapter);
	}	
	
	protected List getChildJavaBeans() {
		List controls = super.getChildJavaBeans();
		if (((EObject) getModel()).eIsSet(sf_menuBar)) {
			if (controls.isEmpty())
				return Collections.singletonList(((EObject) getModel()).eGet(sf_menuBar));
			else {
				List children = new ArrayList(controls.size()+1);
				children.add(((EObject) getModel()).eGet(sf_menuBar));
				children.addAll(controls);
				return children;
			}
		}
		
		return controls;
	}

}

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
 *  $RCSfile: RootPaneJMenuBarContainerGraphicalEditPart.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 16:18:06 $ 
 */

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPolicy;

import org.eclipse.ve.internal.cde.core.CDELayoutEditPolicy;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.java.JavaClass;

/**
 * @author pwalker
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class RootPaneJMenuBarContainerGraphicalEditPart extends RootPaneContainerGraphicalEditPart {
	private EStructuralFeature sf_jmenubar;

	/**
	 * Constructor for RootPaneJMenuBarContainerGraphicalEditPart.
	 * @param model
	 */
	public RootPaneJMenuBarContainerGraphicalEditPart(Object model) {
		super(model);
	}
	
	private Adapter containerAdapter = new AdapterImpl() {
		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sf_jmenubar)
				refreshChildren();
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
	
	
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
	}
	
	protected EditPolicy createLayoutEditPolicy() {
		return new CDELayoutEditPolicy(new RootPaneJMenuBarContainerPolicy(EditDomain.getEditDomain(this)));
	}
	
	/**
	 * Our logical child is the JMenuBar.
	 */
	public List getModelChildren() {
		EObject model = (EObject) getModel();
		List result = super.getModelChildren();

		// If there is a JMenuBar, include this in the list
		if (sf_jmenubar != null) {
			Object jmenuBar = model.eGet(sf_jmenubar);
			if (jmenuBar != null) {
				// Bit of a kludge. Superclass returns Collections.EMPTY_LIST for empty,
				// and we can't add to that one. So when empty, create a new one.
				if (!result.isEmpty())
					result.add(jmenuBar);
				else {
					result = Collections.singletonList(jmenuBar);
				}
			}
		}

		return result;
	}

	public void setModel(Object model) {
		super.setModel(model);
		JavaClass modelType = (JavaClass) ((EObject) model).eClass();
		sf_jmenubar = modelType.getEStructuralFeature("JMenuBar"); //$NON-NLS-1$
	}
}

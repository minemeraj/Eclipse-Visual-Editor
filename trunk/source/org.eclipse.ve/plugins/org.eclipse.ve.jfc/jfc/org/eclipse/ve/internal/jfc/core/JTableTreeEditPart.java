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
 *  $RCSfile: JTableTreeEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPolicy;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
/**
 * JTable has a relationship "columns" which holds its children which are javax.swing.table.TableColumn instances
 */
public class JTableTreeEditPart extends ComponentTreeEditPart {

	protected EStructuralFeature sfColumns;
	public JTableTreeEditPart(Object model) {
		super(model);
	}
	
	private Adapter containerAdapter = new AdapterImpl() {
		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sfColumns)
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
		// The TreeContainerEditPolicy is the CDE one
		// We don't care about being a Container with components
		// We are just interested in showing the viewPortView as a child
		super.createEditPolicies();
		installEditPolicy(
			EditPolicy.TREE_CONTAINER_ROLE,
			new org.eclipse.ve.internal.cde.core.TreeContainerEditPolicy(
				new JTableContainerPolicy(EditDomain.getEditDomain(this))));
	}
	public List getChildJavaBeans() {
		return (List) ((EObject) getModel()).eGet(sfColumns);
	}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		ResourceSet rset = ((EObject) model).eResource().getResourceSet();
		sfColumns = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JTABLE_COLUMNS);
	}
}
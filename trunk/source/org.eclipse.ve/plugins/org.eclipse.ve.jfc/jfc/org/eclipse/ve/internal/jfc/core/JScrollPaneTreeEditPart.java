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
 *  $RCSfile: JScrollPaneTreeEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPolicy;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

/**
 * This inherits from Container because Container knows how to create the correct
 * editPart for the child, although unlike Container which gives us components as children
 * we give up the viewportView
 * In future we may want to give up things like the topRight, etc... for all 9 possible children
 */
public class JScrollPaneTreeEditPart extends ContainerTreeEditPart {

	
	
protected EStructuralFeature sf_scrollpaneViewportView;
private Adapter containerAdapter = new AdapterImpl() {
	public void notifyChanged(Notification msg) {
		if (msg.getFeature() == sf_scrollpaneViewportView)
			refreshChildren();		
	}
};

public JScrollPaneTreeEditPart (Object model) {
	super(model);
}

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
	installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new org.eclipse.ve.internal.cde.core.TreeContainerEditPolicy(new JScrollPaneContainerPolicy(EditDomain.getEditDomain(this))));
}
public List getChildJavaBeans(){
	Object viewportView = ((EObject)getModel()).eGet(sf_scrollpaneViewportView);
	if ( viewportView != null ) {
		return Collections.singletonList(viewportView);
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
	 * @see org.eclipse.ve.internal.jfc.core.ContainerTreeEditPart#setPropertySource(EObject, ComponentTreeEditPart)
	 */
	protected void setPropertySource(EObject child, ComponentTreeEditPart childEP) {
		childEP.setPropertySource(new NonBoundsBeanPropertySource(child));
	}

}
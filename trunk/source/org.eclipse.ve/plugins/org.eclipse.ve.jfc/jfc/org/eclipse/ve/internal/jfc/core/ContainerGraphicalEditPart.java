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
 *  $RCSfile: ContainerGraphicalEditPart.java,v $
 *  $Revision: 1.3 $  $Date: 2004-01-27 16:36:10 $ 
 */
import java.lang.reflect.Constructor;
import java.util.*;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.VisualComponentsLayoutPolicy;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.visual.*;

import org.eclipse.jem.internal.proxy.core.IBeanProxy;

/**
 * ViewObject for the awt Container.
 * Creation date: (2/16/00 3:45:46 PM)
 * @author: Joe Winchester
 */
public class ContainerGraphicalEditPart extends ComponentGraphicalEditPart {

public ContainerGraphicalEditPart(Object model) {
	super(model);
}

protected ContainerPolicy getContainerPolicy() {
	return new ContainerPolicy(EditDomain.getEditDomain(this));	// AWT standard Contained Edit Policy
}

protected IFigure createFigure() {
	IFigure fig = super.createFigure();
	fig.setLayoutManager(new XYLayout());
	return fig;
}

protected void createEditPolicies() {
	super.createEditPolicies();
	installEditPolicy(VisualComponentsLayoutPolicy.LAYOUT_POLICY, new VisualComponentsLayoutPolicy());	// This is a special policy that just handles the size/position of visual components wrt/the figures. It does not handle changing size/position.
	createLayoutEditPolicy();
}

/**
 * Because java.awt.Container can vary its layout manager we need to use 
 * the correct layout input policy for the layout manager that is calculated by
 * a factory
 */
protected void createLayoutEditPolicy() {
	// Need to create the correct layout input policy depending upon the layout manager setting.
	EditPolicy layoutPolicy = null;
	// Get the layout input policy class from the layout policy factory 
	IBeanProxy containerProxy = getComponentProxy().getBeanProxy();
	if (containerProxy != null) {
		// a container was created. 
		ILayoutPolicyFactory lpFactory = BeanAwtUtilities.getLayoutPolicyFactoryFromLayoutManger(containerProxy, EditDomain.getEditDomain(this));
		if(lpFactory.getLayoutInputPolicyClass() != null){
			layoutPolicy = VisualUtilities.getLayoutPolicy(lpFactory.getLayoutInputPolicyClass(),getContainerPolicy());
		}
	}
	// If the LayoutPolicyFactory didn't specifiy a LayoutInputPolicy, use UnknownLayoutInputPolicy
	if (layoutPolicy == null) {
		layoutPolicy = new UnknownLayoutInputPolicy(getContainerPolicy());
	}
	
	removeEditPolicy(EditPolicy.LAYOUT_ROLE); // Get rid of old one, if any
	// Layout policies put figure decorations for things like grids so we should remove this
	installEditPolicy(EditPolicy.LAYOUT_ROLE, layoutPolicy);
}

protected List getModelChildren() {
	// Model children is the components feature.
	// However, this returns the constraint components, but we want to return instead
	// the components themselves. They are the "model" that gets sent to the createChild and
	// component edit part.
	List constraintChildren = (List) ((EObject) getModel()).eGet(sf_containerComponents);
	ArrayList children = new ArrayList(constraintChildren.size());
	Iterator itr = constraintChildren.iterator();
	while (itr.hasNext()) {
		EObject con = (EObject) itr.next();		
		IJavaInstance component = (IJavaInstance)con.eGet(sf_constraintComponent);	// Get the component out of the constraint
		if (component != null) {
			// It was added. It would be null if it couldn't be added for some reason
			children.add(component);
		}
	}
	return children;
}


private Adapter containerAdapter = new Adapter() {
	public void notifyChanged(Notification notification) {
		if (notification.getFeature() == sf_containerComponents)
			refreshChildren();
		else if (notification.getFeature() == sf_containerLayout)
			createLayoutEditPolicy();

	}

	public Notifier getTarget() {
		return null;
	}

	public void setTarget(Notifier newTarget) {
	}

	public boolean isAdapterForType(Object type) {
		return false;
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


private EReference 
	sf_containerLayout,
	sf_constraintComponent,
	sf_containerComponents;
	
protected EditPart createChild(Object model) {
	EditPart ep = super.createChild(model);
	if (ep instanceof ComponentGraphicalEditPart) {
		setPropertySource((ComponentGraphicalEditPart) ep, (EObject) model);
		((ComponentGraphicalEditPart) ep).setTransparent(true);	// So that it doesn't create an image, we subsume it here.
	}
	return ep;
}

protected void setPropertySource(ComponentGraphicalEditPart childEP, EObject child) {
	childEP.setPropertySource((IPropertySource) EcoreUtil.getRegisteredAdapter(InverseMaintenanceAdapter.getFirstReferencedBy(child, sf_constraintComponent), IPropertySource.class));	// This is the property source of the actual model which is part of the constraintComponent.
}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		
		ResourceSet rset = ((IJavaObjectInstance) model).eResource().getResourceSet();
		sf_containerLayout = JavaInstantiation.getReference(rset, JFCConstants.SF_CONTAINER_LAYOUT);
		sf_constraintComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_CONSTRAINT_COMPONENT);	
		sf_containerComponents = JavaInstantiation.getReference(rset, JFCConstants.SF_CONTAINER_COMPONENTS);		
	}

}

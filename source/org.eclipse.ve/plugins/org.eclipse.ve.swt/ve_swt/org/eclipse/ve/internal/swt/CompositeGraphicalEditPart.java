package org.eclipse.ve.internal.swt;
/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2002 - All Rights Reserved. 
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 */

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

import org.eclipse.ve.internal.cde.core.*;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.visual.*;

/**
 * ViewObject for the awt Container.
 * Creation date: (2/16/00 3:45:46 PM)
 * @author: Joe Winchester
 */
public class CompositeGraphicalEditPart extends ControlGraphicalEditPart {
	
	private EReference 
		sf_compositeLayout,
		sf_compositeControls;	

public CompositeGraphicalEditPart(Object model) {
	super(model);
}

protected ContainerPolicy getContainerPolicy() {
	return new CompositeContainerPolicy(EditDomain.getEditDomain(this));	// SWT standard Composite/Container Edit Policy
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
protected EditPart createChild(Object model) {
	EditPart ep = super.createChild(model);
	if (ep instanceof ControlGraphicalEditPart) {
		((ControlGraphicalEditPart) ep).setTransparent(true);	// So that it doesn't create an image, we subsume it here.
	}
	return ep;
}

/**
 * Because org.eclipse.swt.widgets.Composite can vary its layout manager we need to use 
 * the correct layout input policy for the layout manager that is calculated by
 * a factory
 */
protected void createLayoutEditPolicy() {

	EditPolicy layoutPolicy = null;
	CompositeProxyAdapter compositeBeanProxyAdapter = (CompositeProxyAdapter) BeanProxyUtilities.getBeanProxyHost((IJavaInstance)getModel());
	// See the layout of the composite to determine the edit policy
	IBeanProxy layoutBeanProxy = compositeBeanProxyAdapter.getLayoutBeanProxy();
	// If the layoutBeanProxy is null then we use the null layout edit policy
	if(layoutBeanProxy == null){
		layoutPolicy = new NullLayoutEditPolicy((VisualContainerPolicy)getContainerPolicy(),compositeBeanProxyAdapter.getClientBox());		
	} else {
		// Get the layoutPolicyFactory
		ILayoutPolicyFactory layoutPolicyFactory = VisualUtilities.getLayoutPolicyFactory(layoutBeanProxy.getTypeProxy(),EditDomain.getEditDomain(this));
		// If we have one then try to get the EditPolicy from it
		if(layoutPolicyFactory != null && layoutPolicyFactory.getLayoutInputPolicyClass() != null){
			layoutPolicy = VisualUtilities.getLayoutPolicy(layoutPolicyFactory.getLayoutInputPolicyClass(),getContainerPolicy());
		}
	}
	if(layoutPolicy != null){
		removeEditPolicy(EditPolicy.LAYOUT_ROLE); // Get rid of old one, if any
		//	Layout policies put figure decorations for things like grids so we should remove this
		installEditPolicy(EditPolicy.LAYOUT_ROLE, layoutPolicy);
	}
}
	
protected List getModelChildren() {
	return (List) ((EObject) getModel()).eGet(sf_compositeControls);
}
/**
 * When the controls relationship is updated refresh the children, and when the layout property
 * is updated recalculate the edit policy for the specific layout
 */
private Adapter containerAdapter = new Adapter() {
	public void notifyChanged(Notification notification) {
		if (notification.getFeature() == sf_compositeControls)
			refreshChildren();
		else if (notification.getFeature() == sf_compositeLayout)
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


public void setModel(Object model) {
	super.setModel(model);
	IJavaObjectInstance javaModel = (IJavaObjectInstance)model;
	if(javaModel.eResource() != null && javaModel.eResource().getResourceSet() != null) {
		ResourceSet rset = ((IJavaObjectInstance) model).eResource().getResourceSet();
		sf_compositeLayout = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_LAYOUT);
		sf_compositeControls = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_CONTROLS);		
	}	
}

}

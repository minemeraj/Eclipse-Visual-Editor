package org.eclipse.ve.internal.swt;
/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2002 - All Rights Reserved. 
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 */
import java.util.Collections;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.awt.IRectangleBeanProxy;
import org.eclipse.jem.internal.proxy.core.*;
/**
 * Layout Input policy for when a java.awt.Container's layout manager
 * is null (i.e. not set)
 */
public class NullLayoutEditPolicy extends XYLayoutEditPolicy {
	protected ContainerPolicy containerPolicy;		// Handles the containment functions
	protected NullLayoutPolicyHelper helper;	
	protected Rectangle clientBox;

/**
 * Create with the container policy for handling DiagramFigures.
 */
public NullLayoutEditPolicy(VisualContainerPolicy containerPolicy, Rectangle aClientBox) {
	this.containerPolicy = containerPolicy;
	helper = new NullLayoutPolicyHelper(containerPolicy);	
	clientBox = aClientBox;
}

public void activate() {
	super.activate();
	containerPolicy.setContainer(getHost().getModel());
}

public void deactivate() {
	super.deactivate();
	containerPolicy.setContainer(null);
}
/**
 * The child editpart is about to be added to the parent.
 * The child is an existing child that was orphaned from a previous parent.
 */
protected Command createAddCommand(EditPart childEditPart, Object constraint) {
	// We need to create a constraint and send this and the child over to the container policy.
	Object child = childEditPart.getModel();
	NullLayoutPolicyHelper.NullConstraint nullconst = new NullLayoutPolicyHelper.NullConstraint((Rectangle) constraint, true, true);	
	return helper.getAddChildrenCommand(Collections.singletonList(child), Collections.singletonList(nullconst), null);
}

protected Command getCreateCommand(CreateRequest aRequest) {
	Object child = aRequest.getNewObject();
	Object parent = getHost().getModel() ;
	Object constraint = translateToModelConstraint(getConstraintFor(aRequest));			
	
	NullLayoutPolicyHelper.NullConstraint nullconst = new NullLayoutPolicyHelper.NullConstraint((Rectangle) constraint, true, true);
	
	return helper.getCreateChildCommand(child, parent, nullconst, null);
}

protected Command getDeleteDependantCommand(Request aRequest) {
	Command deleteContributionCmd = containerPolicy.getCommand(aRequest);
	if ( deleteContributionCmd == null )
		return UnexecutableCommand.INSTANCE;	// It can't be deleted

	// Note: If there is any annotation, that will be deleted too by the
	// container policy, and that will then also delete all of the view info.
	// So we don't need to handle viewinfo here.
		
	return deleteContributionCmd;		
}

/**
 * getOrphanChildCommand: About to remove a child from the model
 * so that it can be added someplace else.
 *
 * Remove the constraints since it may not be appropriate in 
 * the new position. We need to use the Helper for this.
 */
protected Command getOrphanChildrenCommand(Request aRequest) {
	return helper.getOrphanChildrenCommand(ContainerPolicy.getChildren((GroupRequest) aRequest));
}

protected Command createChangeConstraintCommand(EditPart childEditPart, Object constraint, boolean moved, boolean resize) {
	return createChangeConstraintCommand(childEditPart.getModel(), constraint, moved, resize);
}

/**
 * Get the child constraint. Even though the model constraint is really a java object,
 * we are treating the model constraint as being a Rectangle in this policy.
 * It not converted to the appropriate object until absolutely needed.
 */
protected Object getChildConstraint(EditPart child) {
	IJavaObjectInstance childObject = (IJavaObjectInstance) child.getModel();
	IJavaObjectInstance constraint = (IJavaObjectInstance) childObject.eGet(JavaInstantiation.getSFeature(childObject, SWTConstants.SF_CONTROL_BOUNDS));
	if (constraint != null) {
		IBeanProxy rect = BeanProxyUtilities.getBeanProxy(constraint);
		return new Rectangle(getX(rect), getY(rect), getWidth(rect), getHeight(rect));
	}
	
	// So, size/location should be set. Use those instead.
	Rectangle rect = new Rectangle();
	IJavaObjectInstance loc = (IJavaObjectInstance) childObject.eGet(JavaInstantiation.getSFeature(childObject, SWTConstants.SF_CONTROL_LOCATION));
	if (loc != null) {
		IBeanProxy point = BeanProxyUtilities.getBeanProxy(loc);
		rect.setLocation(getX(point), getY(point));
	}
	IJavaObjectInstance size = (IJavaObjectInstance) childObject.eGet(JavaInstantiation.getSFeature(childObject, SWTConstants.SF_CONTROL_SIZE));
	if (size != null) {
		IBeanProxy sz = BeanProxyUtilities.getBeanProxy(size);
		rect.setSize(getX(sz), getY(sz));
	}
	return rect;	
}

/**
 * primChangeConstraintCommand: Create the command to change the constraint
 * to this new value. For null layout, the constraints are stored in the
 * bounds, or size/location. It will try to be smart about choosing what to 
 * update.
 */
protected Command createChangeConstraintCommand(Object child, Object constraint, boolean moved, boolean resize) {
	NullLayoutPolicyHelper.NullConstraint nullConstraint = new NullLayoutPolicyHelper.NullConstraint((Rectangle) constraint, moved, resize);
	return helper.getChangeConstraintCommand(Collections.singletonList(child), Collections.singletonList(nullConstraint));
}

/**
 * Convert layout constraint (container child positioning data)
 * to a model constraint. We're using Rectangle as the model constraint
 * too here. It will be converted to the correct type when the command
 * itself is executed.
 * The figure constraint is relative to the bounds of the composite, however the model constraint is relative to the origin of the
 * client area.  Therefore we need to subtract from the corner the origin of the client area 
 */
protected Object translateToModelConstraint(Object aFigureConstraint) {
	
	Rectangle figureConstraint = (Rectangle)aFigureConstraint;
	Rectangle result = new Rectangle(
		figureConstraint.x - clientBox.x,
		figureConstraint.y - clientBox.y,
		figureConstraint.width,
		figureConstraint.height);
	return result;
}

protected Object modelToFigureConstraint(Object modelConstraint) {
	if (modelConstraint instanceof IJavaInstance) {
		IBeanProxy proxy = BeanProxyUtilities.getBeanProxy((IJavaInstance) modelConstraint);
		if (proxy instanceof IRectangleBeanProxy) {
			IRectangleBeanProxy rect = (IRectangleBeanProxy) modelConstraint;
			return new Rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
		}
	}
	
	// Don't know what it is, so what do we do. Returning null just makes it not participate.
	return null;
}

protected void setConstraintToFigure(EditPart child, Rectangle figureConstraint) {
	// This is only called if zoom changed, but we are doing zoom, so this is never called.
}


protected boolean isChildResizeable(EditPart aChild) {
	return true;
}
private int getX(IBeanProxy anSWTRectangleBeanProxy){
	IFieldProxy xFieldProxy = anSWTRectangleBeanProxy.getTypeProxy().getFieldProxy("x");
	try{
		IIntegerBeanProxy xProxy = (IIntegerBeanProxy) getGetFieldMethodProxy(anSWTRectangleBeanProxy.getProxyFactoryRegistry()).invoke(xFieldProxy,anSWTRectangleBeanProxy);
		return xProxy.intValue();			
	} catch (ThrowableProxy exc) {
		exc.printStackTrace();
		return 0;
	}
}
private int getY(IBeanProxy anSWTRectangleBeanProxy){
	IFieldProxy xFieldProxy = anSWTRectangleBeanProxy.getTypeProxy().getFieldProxy("y");
	try{
		IIntegerBeanProxy xProxy = (IIntegerBeanProxy) getGetFieldMethodProxy(anSWTRectangleBeanProxy.getProxyFactoryRegistry()).invoke(xFieldProxy,anSWTRectangleBeanProxy);
		return xProxy.intValue();			
	} catch (ThrowableProxy exc) {
		exc.printStackTrace();
		return 0;
	}
}
private int getWidth(IBeanProxy anSWTRectangleBeanProxy){
	IFieldProxy xFieldProxy = anSWTRectangleBeanProxy.getTypeProxy().getFieldProxy("width");
	try{
		IIntegerBeanProxy xProxy = (IIntegerBeanProxy) getGetFieldMethodProxy(anSWTRectangleBeanProxy.getProxyFactoryRegistry()).invoke(xFieldProxy,anSWTRectangleBeanProxy);
		return xProxy.intValue();			
	} catch (ThrowableProxy exc) {
		exc.printStackTrace();
		return 0;
	}
}
private int getHeight(IBeanProxy anSWTRectangleBeanProxy){
	IFieldProxy xFieldProxy = anSWTRectangleBeanProxy.getTypeProxy().getFieldProxy("height");
	try{
		IIntegerBeanProxy xProxy = (IIntegerBeanProxy) getGetFieldMethodProxy(anSWTRectangleBeanProxy.getProxyFactoryRegistry()).invoke(xFieldProxy,anSWTRectangleBeanProxy);
		return xProxy.intValue();			
	} catch (ThrowableProxy exc) {
		exc.printStackTrace();
		return 0;
	}
}	
private IBeanTypeProxy environmentBeanTypeProxy;
private IMethodProxy getFieldMethodProxy; 
protected final IBeanTypeProxy getEnvironmentBeanTypeProxy(ProxyFactoryRegistry aProxyFactoryRegistry){
	if(environmentBeanTypeProxy == null){	
		environmentBeanTypeProxy = aProxyFactoryRegistry.getBeanTypeProxyFactory().getBeanTypeProxy("com.ibm.etools.jbcf.swt.targetvm.Environment"); //$NON-NLS-1$		
	}
	return environmentBeanTypeProxy;
}
protected final IMethodProxy getGetFieldMethodProxy(ProxyFactoryRegistry aProxyFactoryRegistry){
	if(getFieldMethodProxy == null){	
		getFieldMethodProxy = getEnvironmentBeanTypeProxy(aProxyFactoryRegistry).getMethodProxy("java.lang.reflect.field","java.lang.Object");		
	}
	return getFieldMethodProxy;
}
}

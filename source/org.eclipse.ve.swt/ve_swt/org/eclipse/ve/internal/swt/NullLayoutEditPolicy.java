/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;
import java.util.Collections;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.core.*;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;


/**
 * Layout Input policy for when a java.awt.Container's layout manager
 * is null (i.e. not set)
 */
public class NullLayoutEditPolicy extends XYLayoutEditPolicy {
	protected ContainerPolicy containerPolicy;		// Handles the containment functions
	protected NullLayoutPolicyHelper helper;	
	protected CompositeProxyAdapter compositeProxyAdapter;

/**
 * Create with the container policy for handling DiagramFigures.
 */
public NullLayoutEditPolicy(VisualContainerPolicy containerPolicy, CompositeProxyAdapter compositeProxyAdapter) {
	this.containerPolicy = containerPolicy;
	helper = new NullLayoutPolicyHelper(containerPolicy);	
	this.compositeProxyAdapter = compositeProxyAdapter;
}

/* (non-Javadoc)
 * @see org.eclipse.ve.internal.cde.core.XYLayoutEditPolicy#createGridFigure()
 */
protected GridFigure createGridFigure() {
	return new GridFigure(getGridController(), getZoomController()) {
		protected Rectangle getGridClientArea() {
			// We want to modify the border so that it is down to the client area. Don't want the grid drawing outside of this.
			Rectangle clientArea = compositeProxyAdapter.getClientArea();
			clientArea = (Rectangle) NullLayoutEditPolicy.this.modelToFigureConstraint(clientArea);
			IFigure hostFigure = getHostFigure();
			if (!hostFigure.isCoordinateSystem()) {
				// It expects absolute. Don't ask why this works! I don't know.
				clientArea.translate(hostFigure.getBounds().getTopLeft());
			}
			return clientArea;
		};	
	};
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
	return helper.getAddChildrenCommand(Collections.singletonList(child), Collections.singletonList(nullconst), null).getCommand();
}

protected Command getCreateCommand(CreateRequest aRequest) {
	Object child = aRequest.getNewObject();
	Object constraint = translateToModelConstraint(getConstraintFor(aRequest));			
	
	NullLayoutPolicyHelper.NullConstraint nullconst = new NullLayoutPolicyHelper.NullConstraint((Rectangle) constraint, true, true);
	
	return helper.getCreateChildCommand(child, nullconst, null).getCommand();
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
	return helper.getOrphanChildrenCommand(ContainerPolicy.getChildren((GroupRequest) aRequest)).getCommand();
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
	// Need to handle right to left. It can cause resizes to actually be a move and resize, or a move and resize to actually be a move.
	if (compositeProxyAdapter.isRightToLeft()) {
		// Just moved with no resize is ok. It is a move. It moved the entire rect, but didn't change its size.
		if (moved && resize) {
			// To get a move and resize means the left-hand side was moved. This would cause both a move (to move the upper-left to new
			// location) and a resize because the right-hand side is still where it was. In RTL land this is just a resize because
			// we only changed its size (since in RTL the origin is the upper-right, not the upper-left).
			moved = false;
		} else if (resize) {
			// To get resize without move means the right-hand side was moved. In RTL land this means move and resize because it means
			// the upper-right corner was changed.
			moved = true;
		}
	}
	NullLayoutPolicyHelper.NullConstraint nullConstraint = new NullLayoutPolicyHelper.NullConstraint((Rectangle) constraint, moved, resize);
	return helper.getChangeConstraintCommand(Collections.singletonList(child), Collections.singletonList(nullConstraint));
}


protected Object translateToModelConstraint(Object aFigureConstraint) {
	return aFigureConstraint instanceof Rectangle ? 
			(Object) LayoutPolicyHelper.mapFigureToModel(compositeProxyAdapter, getHostFigure(), ((Rectangle)aFigureConstraint).getCopy()) :
			LayoutPolicyHelper.mapFigureToModel(compositeProxyAdapter, getHostFigure(), ((Point)aFigureConstraint).getCopy());
}

/* (non-Javadoc)
 * @see org.eclipse.ve.internal.cde.core.XYLayoutEditPolicy#convertModelToDraw2D(java.lang.Object)
 */
protected Object convertModelToDraw2D(Object modelconstraint) {
	return modelconstraint;	// We use draw2d as the model too.
}

protected Object modelToFigureConstraint(Object modelConstraint) {
	if (modelConstraint instanceof IJavaInstance) {
		IBeanProxy proxy = BeanProxyUtilities.getBeanProxy((IJavaInstance) modelConstraint);
		if (proxy instanceof IRectangleBeanProxy) {
			IRectangleBeanProxy rect = (IRectangleBeanProxy) modelConstraint;
			return LayoutPolicyHelper.mapModelToFigure(compositeProxyAdapter, getHostFigure(), new Rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()));
		}
	} else if (modelConstraint instanceof Rectangle) {
		return LayoutPolicyHelper.mapModelToFigure(compositeProxyAdapter, getHostFigure(), ((Rectangle) modelConstraint).getCopy());
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
	IFieldProxy xFieldProxy = anSWTRectangleBeanProxy.getTypeProxy().getFieldProxy("x"); //$NON-NLS-1$
	try{
		IIntegerBeanProxy xProxy = (IIntegerBeanProxy) getGetFieldMethodProxy(anSWTRectangleBeanProxy.getProxyFactoryRegistry()).invoke(xFieldProxy,anSWTRectangleBeanProxy);
		return xProxy.intValue();			
	} catch (ThrowableProxy exc) {
		exc.printProxyStackTrace();
		return 0;
	}
}
private int getY(IBeanProxy anSWTRectangleBeanProxy){
	IFieldProxy xFieldProxy = anSWTRectangleBeanProxy.getTypeProxy().getFieldProxy("y"); //$NON-NLS-1$
	try{
		IIntegerBeanProxy xProxy = (IIntegerBeanProxy) getGetFieldMethodProxy(anSWTRectangleBeanProxy.getProxyFactoryRegistry()).invoke(xFieldProxy,anSWTRectangleBeanProxy);
		return xProxy.intValue();			
	} catch (ThrowableProxy exc) {
		exc.printProxyStackTrace();
		return 0;
	}
}
private int getWidth(IBeanProxy anSWTRectangleBeanProxy){
	IFieldProxy xFieldProxy = anSWTRectangleBeanProxy.getTypeProxy().getFieldProxy("width"); //$NON-NLS-1$
	try{
		IIntegerBeanProxy xProxy = (IIntegerBeanProxy) getGetFieldMethodProxy(anSWTRectangleBeanProxy.getProxyFactoryRegistry()).invoke(xFieldProxy,anSWTRectangleBeanProxy);
		return xProxy.intValue();			
	} catch (ThrowableProxy exc) {
		exc.printProxyStackTrace();
		return 0;
	}
}
private int getHeight(IBeanProxy anSWTRectangleBeanProxy){
	IFieldProxy xFieldProxy = anSWTRectangleBeanProxy.getTypeProxy().getFieldProxy("height"); //$NON-NLS-1$
	try{
		IIntegerBeanProxy xProxy = (IIntegerBeanProxy) getGetFieldMethodProxy(anSWTRectangleBeanProxy.getProxyFactoryRegistry()).invoke(xFieldProxy,anSWTRectangleBeanProxy);
		return xProxy.intValue();			
	} catch (ThrowableProxy exc) {
		exc.printProxyStackTrace();
		return 0;
	}
}	
private IBeanTypeProxy environmentBeanTypeProxy;
private IMethodProxy getFieldMethodProxy; 
protected final IBeanTypeProxy getEnvironmentBeanTypeProxy(ProxyFactoryRegistry aProxyFactoryRegistry){
	if(environmentBeanTypeProxy == null){	
		environmentBeanTypeProxy = aProxyFactoryRegistry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.swt.targetvm.Environment"); //$NON-NLS-1$		
	}
	return environmentBeanTypeProxy;
}
protected final IMethodProxy getGetFieldMethodProxy(ProxyFactoryRegistry aProxyFactoryRegistry){
	if(getFieldMethodProxy == null){	
		getFieldMethodProxy = getEnvironmentBeanTypeProxy(aProxyFactoryRegistry).getMethodProxy("java.lang.reflect.field","java.lang.Object");		 //$NON-NLS-1$ //$NON-NLS-2$
	}
	return getFieldMethodProxy;
}

}

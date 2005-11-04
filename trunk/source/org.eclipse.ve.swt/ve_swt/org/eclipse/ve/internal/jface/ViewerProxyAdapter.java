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
 *  $RCSfile: ViewerProxyAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2005-11-04 17:30:52 $ 
 */
package org.eclipse.ve.internal.jface;

import org.eclipse.core.runtime.*;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.ImplicitAllocation;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

import org.eclipse.ve.internal.swt.BeanSWTUtilities;
import org.eclipse.ve.internal.swt.UIThreadOnlyProxyAdapter;
 

/**
 * A basic Viewer Proxy adapter. It can be customized to allow
 * the control (getControl) to either be passed in on the constructor or have
 * the viewer itself construct the control. It also allows for specialization of
 * the control, such as for a TreeViewer it knows about the "getTree" method.
 * <p>
 * When constructed, the {@link #setInitializationData(IConfigurationElement, String, Object)} could
 * be called. The Object argument is a s String and will be the name of the property to use the "specialization". The
 * default if not called will be "control". For example, it would be "tree" for TreeViewer.
 * <p>
 * <b>Note:</b> This special feature has a very special purpose. It is used to determine if the control for the
 * viewer is created by the viewer or was passed in. The is done by checking the setting of this feature during
 * initialization and everytime it is changed. If the setting is not set, or if it is set and it is an implicit
 * allocation (@link ImplicitAllocation} and the parent setting of the allocation is this viewer and the feature
 * setting of the allocation is this special feature, then it is considered that the viewer created the control.
 * If is not an implicit, or it is an implicit for some other parent/feature, then it is considered to be passed
 * in on the constructor and not created by this viewer.
 * <p>
 * This is important to know because when the viewer is disposed, we need to dispose of the control too if the
 * viewer created it. We must not dispose it if the viewer did not create it.
 * @since 1.2.0
 */
public class ViewerProxyAdapter extends UIThreadOnlyProxyAdapter implements IExecutableExtension {


	private EStructuralFeature sf_control;
	private String controlFeatureName = "control";
	
	// Was the control created by the Viewer?
	// This is determined by the "control" setting and seeing if it is implicit allocation of the control property.
	// If it is , or there is no setting, then the control is owned by the viewer.
	// If is not an implicit allocation of the control property, then it is not owned by the viewer.
	// This field will be set on instantiation and on each apply so that we know what the state was.
	// We can't look at the setting at release time because by then the setting may of been canceled but
	// still what it was (because it actually can't change once instantiated).
	//
	// So we rely on the convention that the "control" feature will always be set at least once so that we can determine the type.
	private boolean ownsControl;	 

	public ViewerProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		if (newTarget != null)
			sf_control = ((EObject)newTarget).eClass().getEStructuralFeature(controlFeatureName);		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#primInstantiateDroppedPart(org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	protected IProxy primInstantiateDroppedPart(IExpression expression) throws AllocationException {
		// We are instantiating. We need to determine if we own the control or not. After instantiation we will follow the 
		// applied settings to determine this.
		IJavaInstance tree = (IJavaInstance) getEObject().eGet(sf_control);
		setOwnsControl(tree);
		return super.primInstantiateDroppedPart(expression);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.UIThreadOnlyProxyAdapter#primApplied(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int, boolean, org.eclipse.jem.internal.proxy.core.IExpression, boolean)
	 */
	protected void primApplied(EStructuralFeature feature, Object value, int index, boolean isTouch, IExpression expression, boolean testValidity) {
		if (feature == sf_control)
			setOwnsControl((IJavaInstance) value);
		super.primApplied(feature, value, index, isTouch, expression, testValidity);
	}

	/*
	 * Set if we own the tree or not.
	 * @param tree
	 * 
	 * @since 1.2.0
	 */
	private void setOwnsControl(IJavaInstance control) {
		if (control != null) {
			JavaAllocation alloc = control.getAllocation();
			if (alloc != null && alloc.isImplicit()) {
				ImplicitAllocation impAlloc = (ImplicitAllocation) alloc;
				ownsControl = impAlloc.getParent() == getTarget() && impAlloc.getFeature() == sf_control;
			} else
				ownsControl = false;	// If no alloc or not implicit, then we don't own it.
		} else
			ownsControl = true;	// Assume we own it.
	}
	
	
	protected void primPrimReleaseBeanProxy(IExpression expression) {
		// We need to physically release the control if we created the control. Otherwise it will never go away.
		if (isOwnsProxy() && isBeanProxyInstantiated() && ownsControl) {
			// Since the tree proxy can't change once instantiated, we can use the getBeanPropertyProxyValue call
			// to get the value.
			BeanSWTUtilities.invoke_WidgetDispose(getBeanPropertyProxyValue(sf_control, expression, ForExpression.ROOTEXPRESSION), expression, getModelChangeController());
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement, java.lang.String, java.lang.Object)
	 */
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		if (data instanceof String)
			controlFeatureName = (String) data;
	}

}

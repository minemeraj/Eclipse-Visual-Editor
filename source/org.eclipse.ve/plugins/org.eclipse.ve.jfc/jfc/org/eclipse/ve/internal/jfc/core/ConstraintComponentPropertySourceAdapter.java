/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: ConstraintComponentPropertySourceAdapter.java,v $
 *  $Revision: 1.10 $  $Date: 2005-08-24 23:38:10 $ 
 */
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.properties.PropertySourceAdapter;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;
import org.eclipse.ve.internal.java.rules.RuledWrapperedPropertyDescriptor;
import org.eclipse.ve.internal.java.visual.*;

import org.eclipse.jem.internal.proxy.core.IBeanProxy;
/**
 * Default PropertySourceAdapter for ConstraintComponent sources and subclasses.
 *
 * By default it will merge in the properties from the component with the constraint
 * property. If layout manager is null, or the layout manager is not
 * an instance of LayoutManager2, then the constraint property will not be
 * returned. If the layout manager is not null, then bounds/size/location are
 * hidden and not returned. When applying/retrieving properties, if they are for the "component" they
 * will forwarded over to the component.
 */

public class ConstraintComponentPropertySourceAdapter extends PropertySourceAdapter {
	
	protected IPropertySource componentPS;
	protected IPropertyDescriptor constraintDescriptor;
	protected EStructuralFeature sfConstraintConstraint;
	private boolean triedCreateComponentPS;
	
	
	/*
	 * @see Adapter#setTarget(ComponentManagerFeedbackControllerNotifier)
	 */
	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		
		sfConstraintConstraint = JavaInstantiation.getSFeature(((EObject) newTarget).eResource().getResourceSet(), JFCConstants.SF_CONSTRAINT_CONSTRAINT);
	}
	
	/*
	 * Need to merge in the properties of the component.
	 * We will have to wrapper any ISourced or ICommand descriptors because
	 * those will have the wrong source passed into them. We need to intercept
	 * and have the correct source (the component) passed into them.
	 *
	 * 
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		
		IBeanProxyHost containerProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) getEObject().eContainer());
		if (containerProxyHost == null)
			return new IPropertyDescriptor[0];	// We don't have a proxy host, really can't go on.
			
		boolean containerBad = !containerProxyHost.isBeanProxyInstantiated(); 
		IBeanProxy layoutManagerProxy =  !containerBad ? BeanAwtUtilities.invoke_getLayout(containerProxyHost.getBeanProxy()) : null;
		
		createComponentPS();		
		IPropertyDescriptor[] theirs = null;
		IPropertyDescriptor[] wrappedTheirs = null;
		int wi = 0;		
		if (componentPS != null) {
			theirs = componentPS.getPropertyDescriptors();
			
			wrappedTheirs = new IPropertyDescriptor[theirs.length];
			for (int i = 0; i<theirs.length; i++) {
				IPropertyDescriptor pd = theirs[i];
				if (layoutManagerProxy != null && pd.getId() instanceof EStructuralFeature) {
					// exclude bounds/size/location because we have a layout manager, or the container is bad.
					String fn = ((EStructuralFeature) pd.getId()).getName();
					if ("bounds".equals(fn) || "size".equals(fn) || "location".equals(fn)) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						continue;
				}
				wrappedTheirs[wi++] = new RuledWrapperedPropertyDescriptor(containerProxyHost.getBeanProxyDomain().getEditDomain(), componentPS, pd);
			}
		} else {
			theirs = new IPropertyDescriptor[0];
			wrappedTheirs = new IPropertyDescriptor[0];
		}
		
		// Now get our constraint property descriptor, if any.
		EditDomain domain = containerProxyHost.getBeanProxyDomain().getEditDomain();
		ILayoutPolicyFactory fact = !containerBad ? BeanAwtUtilities.getLayoutPolicyFactoryFromLayoutManger(layoutManagerProxy, domain) : new UnknownLayout2PolicyFactory();
		constraintDescriptor = fact.getConstraintPropertyDescriptor(sfConstraintConstraint);
		
		// Finally build the complete list.
		IPropertyDescriptor[] finalList = null;
		if (constraintDescriptor != null) {
			finalList = new IPropertyDescriptor[1 + wi];
			finalList[0] = constraintDescriptor;
			System.arraycopy(wrappedTheirs, 0, finalList, 1, wi);
		} else {
			if (wi != theirs.length) {
				// we've removed some, so make a smaller array
				finalList = new IPropertyDescriptor[wi];
				System.arraycopy(wrappedTheirs, 0, finalList, 0, wi);
			} else
				finalList = wrappedTheirs;
		}
		return finalList;
	}
		
	private void createComponentPS() {
		if (triedCreateComponentPS)
			return;
		triedCreateComponentPS = true;
		Resource cRes = getEObject().eResource();
		EObject component = cRes != null ? (EObject) getEObject().eGet(JavaInstantiation.getSFeature(getEObject().eResource().getResourceSet(), JFCConstants.SF_CONSTRAINT_COMPONENT)) : null;
		if (component != null)
			componentPS = (IPropertySource) EcoreUtil.getRegisteredAdapter(component, IPropertySource.class);
	}

	/*
	 * If one of ours, send it on up, else send it to the component.
	 */
	public Object getPropertyValue(Object feature) {
		if (feature == sfConstraintConstraint)
			return super.getPropertyValue(feature);
		createComponentPS();	
		return componentPS != null ? componentPS.getPropertyValue(feature) : null;
	}
	
	/*
	 * If one of ours, send it on up, else send it to the component.
	 */
	public boolean isPropertySet(Object feature) {
		if (feature == sfConstraintConstraint)
			return super.isPropertySet(feature);
		createComponentPS();	
		return componentPS != null ? componentPS.isPropertySet(feature) : false;
	}	

	/*
	 * If one of ours, send it on up, else send it to the component.
	 */
	public void resetPropertyValue(Object feature) {
		if (feature == sfConstraintConstraint){
			super.resetPropertyValue(feature);
			return;
		}
		createComponentPS();
		if (componentPS != null)
			componentPS.resetPropertyValue(feature);
	}	
	
	/*
	 * If one of ours, send it on up, else send it to the component.
	 */
	public void setPropertyValue(Object feature, Object val)  {
		if (feature == sfConstraintConstraint) {
			super.setPropertyValue(feature, val);
			return;
		}
		createComponentPS();
		if (componentPS != null)
			componentPS.setPropertyValue(feature, val);
	}	
}

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
/*
 * $RCSfile: BeanPropertySourceAdapter.java,v $ $Revision: 1.16 $ $Date: 2005-12-08 20:39:27 $
 */
package org.eclipse.ve.internal.java.core;

import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;
import org.eclipse.ve.internal.cde.properties.PropertySourceAdapter;

import org.eclipse.ve.internal.jcm.JCMPackage;

/**
 * Property Source adaptor for Beans.
 * 
 * @since 1.0.0
 */
public class BeanPropertySourceAdapter extends PropertySourceAdapter {
	
	/**
	 * Get the target as a bean.
	 * @return 
	 * @since 1.0.0
	 */
	public final IJavaInstance getBean() {
		return (IJavaInstance) getTarget();
	}

	/*
	 *  (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (super.equals(obj))
			return true;

		// Now also check to see if the bean proxies are equal. This could be if two different
		// mof targets, but the actual bean proxy on the VM is the same one. this occurs
		// because when we query property value, if not set in MOF, we then query the
		// live object. This can create a new bean proxy each time, but for the same bean.
		if (obj instanceof IPropertySource)
			obj = ((IPropertySource) obj).getEditableValue();
		if (obj instanceof EObject) {
			IBeanProxyHost otherProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) obj);
			if (otherProxyHost != null) {
				IBeanProxy otherProxy = otherProxyHost.getBeanProxy();
				if (otherProxy != null) {
					IBeanProxyHost myProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) target);
					if (myProxyHost != null)
						return otherProxy.equals(myProxyHost.getBeanProxy());
				}
			}
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object structuralFeature) {

		EStructuralFeature sf = (EStructuralFeature) structuralFeature;
		Object value = null;
		try {
			if (!getEObject().eIsSet(sf)) {
				// The value was not explicitly set in the EMF Object, so we will get the Bean Proxy Adaptor
				// and ask it for the live value from the real java bean over in the VM.
				IBeanProxyHost sourceHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) getTarget());
				value = sourceHost.getBeanPropertyValue(sf);
				if (value != null) {
					// Put a propertySource adapter onto it because one will be needed but this guy isn't in a resource set to get it.
					// Set it an IPropertySource so that we don't look it up again below.
					Resource res = getEObject().eResource();
					ResourceSet rset = res != null ? res.getResourceSet() : EMFEditDomainHelper.getResourceSet(sourceHost.getBeanProxyDomain()
							.getEditDomain()); // If the EObject is not yet contained, use the resource set from the proxy host.
					IPropertySource ps = (IPropertySource) EcoreUtil.getAdapterFactory(rset.getAdapterFactories(), IPropertySource.class).adapt(
							(Notifier) value, IPropertySource.class);
					if (ps != null)
						value = ps; // It has a property source adapter.
				}
			} else
				value = getEObject().eGet(sf);
		} catch (IllegalArgumentException e) {
			return null; 			// Feature not a feature of this property source, by IPropertySource definition this should return null.
		}
		
		if (!(value instanceof IPropertySource) && value instanceof EObject) {
			IPropertySource ps = (IPropertySource) EcoreUtil.getRegisteredAdapter((EObject) value, IPropertySource.class);
			return ps != null ? ps : value;
		} else
			return value; 
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.properties.PropertySourceAdapter#includeFeature(org.eclipse.emf.ecore.EStructuralFeature)
	 */
	protected boolean includeFeature(EStructuralFeature aFeature) {
		//first check if PropertyDecorator sets the feature to be hidden in property sheet
		PropertyDecorator propDecor = Utilities.getPropertyDecorator(aFeature);
		if (propDecor != null) {
			boolean propDecorFlag = !propDecor.isHidden() && (!propDecor.isSetDesignTime() || propDecor.isDesignTime());
			if (!propDecorFlag)
				return false;
		}

		//must call superclass method to check if the PropertyDescriptorAdatper sets the
		//feature to be hidden in property sheet
		return super.includeFeature(aFeature);

	}
	
	protected boolean shouldAllowAnnotationRename() {
		return getEObject().eContainingFeature() == JCMPackage.eINSTANCE.getMemberContainer_Members();	// In a members, has a name to rename.
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.properties.PropertySourceAdapter#getAllFeatures(org.eclipse.emf.ecore.EClass)
	 */
	protected List getAllFeatures(EClass cls) {
		return cls instanceof JavaClass ? ((JavaClass) cls).getAllProperties() : super.getAllFeatures(cls);
	}

	public boolean isPropertySet(Object feature) {
		if (super.isPropertySet(feature)) {
			Object value = ((EObject) target).eGet((EStructuralFeature) feature);
			if (value instanceof IJavaInstance)
				return !((IJavaInstance) value).isImplicitAllocation();	// Implicit's are not considered set. We set it only so that settings on implicits will all apply to the same physical object.
			else return true;
		} else
			return false;
	}

}

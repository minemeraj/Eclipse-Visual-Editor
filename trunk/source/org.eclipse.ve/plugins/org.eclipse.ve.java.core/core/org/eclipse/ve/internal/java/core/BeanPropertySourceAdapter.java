package org.eclipse.ve.internal.java.core;
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
 *  $RCSfile: BeanPropertySourceAdapter.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 16:16:38 $ 
 */
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.beaninfo.adapters.Utilities;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;
import org.eclipse.ve.internal.cde.properties.PropertySourceAdapter;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
/**
 * Property Source adaptor for Beans.
 * Creation date: (1/19/00 1:43:16 PM)
 * @author: Joe Winchester
 */
public class BeanPropertySourceAdapter extends PropertySourceAdapter {
/**
 * Compares two objects for equality. Returns a boolean that indicates
 * whether this object is equivalent to the specified object. This method
 * is used when an object is stored in a hashtable.
 * @param obj the Object to compare with
 * @return true if these Objects are equal; false otherwise.
 * @see java.util.Hashtable
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
		IBeanProxyHost otherProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance)obj);
		if (otherProxyHost != null) {
			IBeanProxy otherProxy = otherProxyHost.getBeanProxy();
			if (otherProxy != null) {
				IBeanProxyHost myProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance)target);
				if (myProxyHost != null)
					return otherProxy.equals(myProxyHost.getBeanProxy());
			}
		}
	}
	return false;
}
/**
 * Get The property value from the target and wrap as a property source if necessary.
 */
public Object getPropertyValue(Object structuralFeature) {
	
	Object value = null;
	EStructuralFeature sf = (EStructuralFeature) structuralFeature;
	if (!getEObject().eIsSet(sf)) {
		// The value was not explicitly set in the MOF Object, so we will get the Bean Proxy Adaptor
		// and ask it for the live value from the real java bean over in the VM.
		IBeanProxyHost sourceHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) getTarget());
		value = sourceHost.getBeanPropertyValue(sf);
		if (value != null) {
			// Put a propertySource adapter onto it because one will be needed but this guy isn't in a resource set to get it.
			// Set it an IPropertySource so that we don't look it up again below.
			Resource res = getEObject().eResource();
			ResourceSet rset = res != null ? res.getResourceSet() : EMFEditDomainHelper.getResourceSet(sourceHost.getBeanProxyDomain().getEditDomain());	// If the EObject is not yet contained, use the resource set from the proxy host.
			IPropertySource ps = (IPropertySource) EcoreUtil.getAdapterFactory(rset.getAdapterFactories(), IPropertySource.class).adapt((Notifier) value, IPropertySource.class);
			if (ps != null)
				value = ps;	// It has a property source adapter.
		}
	} else
		value = getEObject().eGet(sf);	
		
	if (!(value instanceof IPropertySource) && value instanceof EObject) {
		IPropertySource ps = (IPropertySource) EcoreUtil.getRegisteredAdapter((EObject) value, IPropertySource.class);
		return ps != null ? ps : value;
	} else
		return value;
}

/**
 * Bean method for filtering attributes.
 */
protected boolean includeFeature(EStructuralFeature aFeature) {
	//first check if PropertyDecorator sets the feature to be hidden in property sheet
	PropertyDecorator propDecor = Utilities.getPropertyDecorator((EModelElement)aFeature);
	if (propDecor != null) {
		boolean propDecorFlag =  !propDecor.isHidden() &&
			(!propDecor.isSetDesignTime() || propDecor.isDesignTime());		
		if (! propDecorFlag) return false;
	}	
	
	//must call superclass method to check if the PropertyDescriptorAdatper sets the 
	//feature to be hidden in property sheet			
	return super.includeFeature(aFeature);
				
}

	/**
	 * @see org.eclipse.ve.internal.cde.properties.PropertySourceAdapter#getAllFeatures(EClass)
	 */
	protected List getAllFeatures(EClass cls) {
		return cls instanceof JavaClass ? ((JavaClass) cls).getAllProperties() : super.getAllFeatures(cls);
	}

}

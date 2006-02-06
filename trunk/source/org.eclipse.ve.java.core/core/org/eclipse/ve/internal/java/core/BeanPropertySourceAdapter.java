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
 * $RCSfile: BeanPropertySourceAdapter.java,v $ $Revision: 1.17 $ $Date: 2006-02-06 17:14:38 $
 */
package org.eclipse.ve.internal.java.core;

import java.util.*;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;
import org.eclipse.ve.internal.cde.properties.PropertySourceAdapter;

import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.java.core.FactoryCreationData.MethodData;

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
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.properties.PropertySourceAdapter#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object feature, Object val) {
		if (val == null || val instanceof IJavaInstance) {
			// Assume feature is an EStructuralFeature. Test to see if this is a factory arg.
			Map factoryMap = getFactoryArgumentsMap();
			Integer argIndex = (Integer) factoryMap.get(((EStructuralFeature) feature).getName());
			ParseTreeAllocation parseTreeAllocation = (ParseTreeAllocation) getBean().getAllocation();
			PTMethodInvocation methodInvocation = (PTMethodInvocation) parseTreeAllocation.getExpression();
			if (argIndex != null) {
				// It is a factory arg. Test it.
				PTExpression newArgExpression = createArgumentExpression((IJavaInstance) val);
				if (newArgExpression != null) {
					methodInvocation.getArguments().set(argIndex.intValue(), newArgExpression);
					getBean().setAllocation(parseTreeAllocation); // Touch the allocation to cause a target VM refresh
					return;
				} 
			} else if (isFactoryType()) {
				// we are a factory type, but the property is not on the factory. See if we can expand.
				PTExpression newArgExpression = createArgumentExpression((IJavaInstance) val);
				if (newArgExpression != null) {
					if (expandToIncludeProperty(((EStructuralFeature) feature).getName(), newArgExpression, methodInvocation) != COULD_NOT_EXPAND) {
						getBean().setAllocation(parseTreeAllocation); // Touch the allocation to cause a target VM refresh
						return;
					}
				} 
			}
		} 
		
		// By default do normal. If we fall through and get here.
		setPropertyValueNoFactoryTest(feature, val);
	}
	
	/**
	 * Convert the new argument value into a PTExpression to be used as a factory argument expression.
	 * @param newArgumentValue
	 * @return the expression to use or <code>null</code> if couldn't be converted.
	 * 
	 * @since 1.2.0
	 */
	protected PTExpression createArgumentExpression(IJavaInstance newArgumentValue) {
		if (newArgumentValue != null) {
			EStructuralFeature containmentFeature = newArgumentValue.eContainmentFeature();
			if (containmentFeature == null || containmentFeature == JCMPackage.eINSTANCE.getMemberContainer_Properties()) {
				JavaAllocation alloc = newArgumentValue.getAllocation();
				if (alloc instanceof ParseTreeAllocation)
					return ((ParseTreeAllocation) alloc).getExpression();
				else if (alloc instanceof InitStringAllocation) {
					alloc = BeanPropertyDescriptorAdapter.createAllocation(((InitStringAllocation) alloc).getInitString());
					// If not a parsetree alloc then we can't assign it.
					if (alloc instanceof ParseTreeAllocation)
						return ((ParseTreeAllocation) alloc).getExpression();
				}
			} else {
				// It is object reference.
				return InstantiationFactory.eINSTANCE.createPTInstanceReference(newArgumentValue);
			}
		} else
			return InstantiationFactory.eINSTANCE.createPTNullLiteral();
		return null;
	
	}
	
	/**
	 * Flag from {@link #expandToIncludeProperty(String, PTExpression, PTMethodInvocation)} as a return value indicating that the
	 * method could not expand the call.
	 * @since 1.2.0
	 */
	protected final int COULD_NOT_EXPAND = -1;
	
	/**
	 * This will modify the factory method invocation to include the new property given by the name and the expression to use for the property. It may
	 * not be able to expand it. It will only increase by one argument count and check to see if all of the existing properties exist in the new one,
	 * and there are no "undefined" (i.e. non-property) arguments in either the old or the new. It can't be reliably expanded if there is not a one
	 * for one match, though they may be in a different order.
	 * 
	 * @param propertyName
	 *            the name of the property to expand to.
	 * @param propertyExpression
	 *            the property expression to be placed into the argument in the expanded call.
	 * @param factoryMethodExpression
	 *            the factory method invocation that will be updated.
	 * @return the index argument of the new setting or {@link #COULD_NOT_EXPAND} if it could not be done.
	 * @since 1.2.0
	 */
	protected int expandToIncludeProperty(String propertyName, PTExpression propertyExpression, PTMethodInvocation factoryMethodExpression) {
		FactoryCreationData fcdata = FactoryCreationData.getCreationData(factoryMethodExpression);
		if (fcdata != null) {
			MethodData methodData = fcdata.getMethodData(factoryMethodExpression.getName());
			if (methodData != null) {
				// See if the next higher method has the given property name, if it does we can go on to check that no existing properties are missing.
				EList arguments = factoryMethodExpression.getArguments();
				int currentSize = arguments.size();
				int newSize = currentSize+1;
				int argIndex = methodData.getArgIndex(propertyName, newSize);
				if (argIndex > -1) {
					String[] currentNames = methodData.getPropertyNames(currentSize);
					String[] newNames = methodData.getPropertyNames(newSize);
					int[] newPos = new int[currentNames.length];
nextName:			for (int i = 0; i < currentNames.length; i++) {
						String cName = currentNames[i];
						if (cName == null)
							return COULD_NOT_EXPAND;	// Can't expand. We have an undefined, so we don't where it should go in expansion.
						for (int j = 0; j < newNames.length; j++) {
							String nName = newNames[j];
							if (nName == null)
								return COULD_NOT_EXPAND;	// Can't expand. We have an undefined, so we don't know what should go there.
							if (cName.equals(nName)) {
								newPos[i] = j;
								continue nextName;
							}
						}
						return COULD_NOT_EXPAND;	// Got all of the way through new names and didn't find a match for the old name, so we can't throw the old one away.
					}
					List newArgs = Arrays.asList(new Object[newSize]);	// Build up into this new list.
					for (int i = 0; i < newPos.length; i++) {
						newArgs.set(newPos[i], arguments.get(i));
					}
					newArgs.set(argIndex, propertyExpression);
					arguments.clear();
					arguments.addAll(newArgs);
					return argIndex;
				}
			}
		}
		return COULD_NOT_EXPAND;
	}
	
	/**
	 * Called to non-factory setPropertyValue testing. Allows subclasses to avoid the factory method
	 * stuff if it needs to do it itself.
	 * @param feature
	 * 
	 * @since 1.2.0
	 */
	protected final void setPropertyValueNoFactoryTest(Object feature, Object value) {
		super.setPropertyValue(feature, value);
	}	

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.properties.PropertySourceAdapter#resetPropertyValue(java.lang.Object)
	 */
	public void resetPropertyValue(Object feature) {
		// Assume feature is an EStructuralFeature. Test to see if this is a factory arg.
		Map factoryMap = getFactoryArgumentsMap();
		Integer argIndex = (Integer) factoryMap.get(((EStructuralFeature) feature).getName());
		if (argIndex != null) {
			// It is a factory arg. Test it.
			ParseTreeAllocation parseTreeAllocation = (ParseTreeAllocation) getBean().getAllocation();
			PTMethodInvocation methodInvocation = (PTMethodInvocation)parseTreeAllocation.getExpression();
			methodInvocation.getArguments().set(argIndex.intValue(), InstantiationFactory.eINSTANCE.createPTNullLiteral());
			getBean().setAllocation(parseTreeAllocation);  // Touch the allocation to cause a target VM refresh			
		} else
			resetPropertyValueNoFactoryTest(feature);
	}
	
	/**
	 * Called to non-factory resetPropertyValue testing. Allows subclasses to avoid the factory method
	 * stuff if it needs to do it itself.
	 * @param feature
	 * 
	 * @since 1.2.0
	 */
	protected final void resetPropertyValueNoFactoryTest(Object feature) {
		super.resetPropertyValue(feature);
	}
	
	public boolean isPropertySet(Object feature) {
		// Assume feature is an EStructuralFeature. Test to see if this is a factory arg.
		Map factoryMap = getFactoryArgumentsMap();
		Integer argIndex = (Integer) factoryMap.get(((EStructuralFeature) feature).getName());
		if (argIndex != null) {
			// It is a factory arg. Test it.
			ParseTreeAllocation allocation = (ParseTreeAllocation) getBean().getAllocation();			
			PTMethodInvocation methodInvocation = (PTMethodInvocation) allocation.getExpression();
			return !(methodInvocation.getArguments().get(argIndex.intValue()) instanceof PTNullLiteral);

		} else
			return isPropertySetNoFactoryTest(feature);	// Not a factory arg, do normal testing.
	}
	
	/**
	 * Called to non-factory isPropertySet testing. Allows subclasses to avoid the factory method
	 * stuff if it needs to do it itself.
	 * @param feature
	 * @return
	 * 
	 * @since 1.2.0
	 */
	protected final boolean isPropertySetNoFactoryTest(Object feature) {
		if (super.isPropertySet(feature)) {
			Object value = ((EObject) target).eGet((EStructuralFeature) feature);
			if (value instanceof IJavaInstance)
				return !((IJavaInstance) value).isImplicitAllocation();	// Implicit's are not considered set. We set it only so that settings on implicits will all apply to the same physical object.
			else return true;
		} else
			return false;
	}
	
	// Map of property name to factory argument index. Used when factory allocation.
	private Map factoryArguments;
	// Is this a factory type allocation.
	private boolean factoryType;

	/**
	 * Return the map of property names to the factory creation method argument index (Integer). If the
	 * property name is not one of the factory args, then the value returned from the get will be null.
	 * @return
	 * 
	 * @since 1.2.0
	 */
	protected Map getFactoryArgumentsMap() {
		if (factoryArguments == null) {
			// See if a factory, and if so build up the map. First init to empty map so if any problems it will be empty.
			//TODO Currently don't handle factory type introspection going stale. Assuming that it won't change for a factory while this bean is live.
			factoryType = false;
			factoryArguments = Collections.EMPTY_MAP;
			JavaAllocation allocation = getBean().getAllocation();						
			if (allocation != null && allocation.isParseTree() && ((ParseTreeAllocation) allocation).getExpression() instanceof PTMethodInvocation) {
				PTMethodInvocation mi = (PTMethodInvocation) ((ParseTreeAllocation) allocation).getExpression();
				FactoryCreationData fcdata = FactoryCreationData.getCreationData(mi);
				if (fcdata != null) {
					MethodData methodData = fcdata.getMethodData(mi.getName());
					if (methodData != null) {
						factoryType = true;
						String[] propertyNames = methodData.getPropertyNames(mi.getArguments().size());
						if (propertyNames != null && propertyNames.length > 0) {
							factoryArguments = new HashMap(propertyNames.length);
							for (int i = 0; i < propertyNames.length; i++) {
								if (propertyNames[i] != null)
									factoryArguments.put(propertyNames[i], new Integer(i));
							}
						}
					}
				}				
			}
		}
		return factoryArguments;
	}
	
	/**
	 * Is the allocation a factory allocation.
	 * @return <code>true</code> if it is a factory allocation.
	 * 
	 * @since 1.2.0
	 */
	protected final boolean isFactoryType() {
		if (factoryArguments == null)
			getFactoryArgumentsMap();	// init the flag.
		return factoryType;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);
		// Need to handle allocation changed. If the allocation changed we need to clear the factory map.
		switch ( notification.getEventType()) {
			case Notification.SET:
			case Notification.UNSET: 
				if (((EStructuralFeature) notification.getFeature()).getName().equals(JavaInstantiation.ALLOCATION)) {
					factoryArguments = null;
				}
				break;			
		}
		
	}
}

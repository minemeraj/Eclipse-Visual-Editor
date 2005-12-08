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
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: BeanProxyUtilities.java,v $
 *  $Revision: 1.21 $  $Date: 2005-12-08 17:15:54 $ 
 */

import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPart;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.core.EditDomain;

public class BeanProxyUtilities {

	
	/**
	 * A settings list class that only returns Java types settings (i.e. JavaDataType or JavaClass).
	 */
	public static class JavaSettingsEList extends CDEUtilities.ESettingsEList {
		
		/**
		 * Constructor for JavaSettingsEList.
		 * @param eObject
		 * @param shared
		 */
		public JavaSettingsEList(EObject eObject, boolean shared) {
			super(eObject, shared);
		}

		/**
		 * Constructor for JavaSettingsEList.
		 * @param eObject
		 * @param eStructuralFeatures
		 * @param shared
		 */
		public JavaSettingsEList(EObject eObject, EStructuralFeature[] eStructuralFeatures, boolean shared) {
			super(eObject, eStructuralFeatures, shared);
		}
		
		public List basicList() {
			return new JavaSettingsEList(eObject, eStructuralFeatures, shared) {
				protected boolean resolve() {
					return false;
				}
			};
		}		

		/**
		 * @see org.eclipse.emf.ecore.util.EContentsEList#isIncluded(EStructuralFeature)
		 */
		protected boolean isIncluded(EStructuralFeature eStructuralFeature) {
			return eStructuralFeature.getEType() instanceof JavaHelpers &&  super.isIncluded(eStructuralFeature);
		}

	}
	
	/**
	 * Return the MOF JavaClass that wrappers the argument.  Set if this host owns the proxy.
	 * NOTE: This doesn't set the JavaAllocation. This needs to be done by caller since only caller
	 * knows the correct allocation.
	 * 
	 * This requires 4 steps
	 * 1 - Find the MOF class for the bean proxy
	 * 2 - Instantiate the MOF class
	 * 3 - Get the bean proxy adaptor for the instance
	 * 4 - Give the bean proxy adaptor the bean proxy
	 */
	public static IJavaInstance wrapperBeanProxy(IBeanProxy aBeanProxy, ResourceSet aResourceSet, String initString, boolean ownsProxy) {

		return wrapperBeanProxy(aBeanProxy, aResourceSet, ownsProxy, initString != null ? InstantiationFactory.eINSTANCE.createInitStringAllocation(initString) : null);
	}

	public static IJavaInstance wrapperBeanProxy(IBeanProxy aBeanProxy, ResourceSet aResourceSet, boolean ownsProxy, JavaAllocation alloc) {
		if (aBeanProxy == null)
			return null;
		
		// 1 - Get the mof class for this type of bean proxy.
		JavaHelpers javaType = getJavaType(aBeanProxy, aResourceSet);
		// The object could be a class or a primitive type
		if (!javaType.isPrimitive()) {
			JavaClass javaClass = (JavaClass) javaType;
			// TODO For now, if the class can't be found, then there is nothing we can do, so we will return null. This may change when we have our error handling for unfound types."); 
			if (javaClass.getKind() == TypeKind.UNDEFINED_LITERAL)
				return null;
		}
		// 2 - The factory creates the class.
		IJavaInstance javaInstance = (IJavaInstance) javaType.getEPackage().getEFactoryInstance().create(javaType);
		javaInstance.setAllocation(alloc);
		// 3 - The adaptor factory should be installed so just asking for the adaptor will create it
		IInternalBeanProxyHost beanProxyHost = (IInternalBeanProxyHost) getBeanProxyHost(javaInstance, aResourceSet);
		// 4 - Pass the bean proxy that we got in the argument to the proxy host
		beanProxyHost.setBeanProxy(aBeanProxy);
		beanProxyHost.setOwnsProxy(ownsProxy);
		return javaInstance;
	}
	
	/**
	 * Wrapper the bean as the expected type and not what it is really.
	 * @param aBeanProxy
	 * @param aResourceSet
	 * @param ownsProxy
	 * @param alloc
	 * @param javaType
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static IJavaInstance wrapperBeanProxy(IBeanProxy aBeanProxy, ResourceSet aResourceSet, boolean ownsProxy, JavaAllocation alloc, JavaHelpers javaType) {
		if (aBeanProxy == null)
			return null;
		
		// The object could be a class or a primitive type
		if (!javaType.isPrimitive()) {
			JavaClass javaClass = (JavaClass) javaType;
			// TODO For now, if the class can't be found, then there is nothing we can do, so we will return null. This may change when we have our error handling for unfound types."); 
			if (javaClass.getKind() == TypeKind.UNDEFINED_LITERAL)
				return null;
		}
		// 2 - The factory creates the class.
		IJavaInstance javaInstance = (IJavaInstance) javaType.getEPackage().getEFactoryInstance().create(javaType);
		javaInstance.setAllocation(alloc);
		// 3 - The adaptor factory should be installed so just asking for the adaptor will create it
		IInternalBeanProxyHost beanProxyHost = (IInternalBeanProxyHost) getBeanProxyHost(javaInstance, aResourceSet);
		// 4 - Pass the bean proxy that we got in the argument to the proxy host
		beanProxyHost.setBeanProxy(aBeanProxy);
		beanProxyHost.setOwnsProxy(ownsProxy);
		return javaInstance;
	}
	
	/**
	 * Get the JavaHelpers type from the beanproxy and resource set.
	 */
	public static JavaHelpers getJavaType(IBeanProxy aBeanProxy, ResourceSet aResourceSet) {
		String qualifiedClassName = aBeanProxy.getTypeProxy().getFormalTypeName();
		JavaHelpers javaType = JavaRefFactory.eINSTANCE.reflectType(qualifiedClassName, aResourceSet);
		return javaType;
	}
	
	/**
	 * In cases the bean proxy is an Object and we want to wrapper it as a primitive type,
	 * e.g. the java.beans.PropertyEditor returns an Integer but we want an int
	 * 
	 * Note: This will set the allocation because the init string was passed in.
	 */
	public static IJavaInstance wrapperBeanProxyAsPrimitiveType(
		IBeanProxy aBeanProxy,
		JavaDataType aType,
		ResourceSet aResourceSet,
		String anInitializationString) {
			
		return wrapperBeanProxyAsPrimitive(aBeanProxy, aType, aResourceSet, anInitializationString != null ? InstantiationFactory.eINSTANCE.createInitStringAllocation(anInitializationString) : null);
	}

	public static IJavaInstance wrapperBeanProxyAsPrimitive(
		IBeanProxy aBeanProxy,
		JavaDataType aType,
		ResourceSet aResourceSet,
		JavaAllocation alloc) {
		if (aBeanProxy == null)
			return null;
		// Integer
		IBeanProxy primitiveProxy = null;
		IStandardBeanProxyFactory proxyFactory = aBeanProxy.getProxyFactoryRegistry().getBeanProxyFactory();
		String typeName = aType.getQualifiedName();	// We're only interested if primitive, so don't need getQualifiedNameForReflection().
		if (typeName.equals("int")) { //$NON-NLS-1$
			primitiveProxy = proxyFactory.createBeanProxyWith(((INumberBeanProxy) aBeanProxy).intValue());
		} else if (typeName.equals("boolean")) { //$NON-NLS-1$
			primitiveProxy = proxyFactory.createBeanProxyWith(((IBooleanBeanProxy) aBeanProxy).booleanValue());
		} else if (typeName.equals("short")) { //$NON-NLS-1$
			primitiveProxy = proxyFactory.createBeanProxyWith(((INumberBeanProxy) aBeanProxy).shortValue());
		} else if (typeName.equals("long")) { //$NON-NLS-1$
			primitiveProxy = proxyFactory.createBeanProxyWith(((INumberBeanProxy) aBeanProxy).longValue());
		} else if (typeName.equals("float")) { //$NON-NLS-1$
			primitiveProxy = proxyFactory.createBeanProxyWith(((INumberBeanProxy) aBeanProxy).floatValue());
		} else if (typeName.equals("double")) { //$NON-NLS-1$
			primitiveProxy = proxyFactory.createBeanProxyWith(((INumberBeanProxy) aBeanProxy).doubleValue());
		} else if (typeName.equals("char")) { //$NON-NLS-1$
			primitiveProxy = proxyFactory.createBeanProxyWith(((ICharacterBeanProxy) aBeanProxy).charValue());
		} else if (typeName.equals("byte")) { //$NON-NLS-1$
			primitiveProxy = proxyFactory.createBeanProxyWith(((INumberBeanProxy) aBeanProxy).byteValue());
		}
		IJavaInstance javaInstance = (IJavaInstance) aType.getEPackage().getEFactoryInstance().create(aType);
		javaInstance.setAllocation(alloc);
		IInternalBeanProxyHost beanProxyHost = (IInternalBeanProxyHost) getBeanProxyHost(javaInstance, aResourceSet);
		beanProxyHost.setBeanProxy(primitiveProxy);
		beanProxyHost.setOwnsProxy(true);
		return javaInstance;
	}
	
	/**
	 * This method relies on the fact that there is a bean proxy adapter factory
	 * that we can get the proxy factory registry from
	 */
	public static ProxyFactoryRegistry getProxyFactoryRegistry(EditPart anEditPart) {
		IBeanProxyDomain proxyDomain = JavaEditDomainHelper.getBeanProxyDomain(EditDomain.getEditDomain(anEditPart));
		if (proxyDomain != null) {
			return proxyDomain.getProxyFactoryRegistry();
		} else {
			// TODO Is this really a good thing to do"); //$NON-NLS-1$
			if (JavaVEPlugin.isLoggingLevel(Level.WARNING))
				JavaVEPlugin.log("Unable to find a proxy factory registry for " + anEditPart, Level.WARNING); //$NON-NLS-1$
			return null;
		}
	}
	/** 
	 * Helper to apply the value to the source
	 * @throws NoSuchMethodException
	 * @deprecated This should not be used. Alway go through the IBeanProxyHost.
	 */
	public static void writeBeanFeature(PropertyDecorator aPropertyDecorator, IBeanProxy aSource, IBeanProxy aValue) throws ThrowableProxy, NoSuchMethodException {

		if(aPropertyDecorator.getWriteMethod() != null){
			Method method = aPropertyDecorator.getWriteMethod();
			ProxyFactoryRegistry registry = aSource.getProxyFactoryRegistry();
			IMethodProxy setFeatureMethodProxy = getMethodProxy(method, registry);
			setFeatureMethodProxy.invoke(aSource, aValue);
		} else if (aPropertyDecorator.getField() != null){
			setBeanField(aPropertyDecorator.getField().getName(),aSource,aValue);
		}
		
	}
	/** 
	 * Helper to set a field
	 */
	public static void setBeanField(String fieldName, IBeanProxy aSource, IBeanProxy aValue) {

		// Find the set method on the same VM as the source
		IFieldProxy aField = aSource.getTypeProxy().getFieldProxy(fieldName);
		try {
			aField.set(aSource, aValue);
		} catch (ThrowableProxy e) {
			// TODO "Field could not be set " + fieldName); //$NON-NLS-1$
			JavaVEPlugin.log(e, Level.WARNING);
		}

	}
	/** 
	 * Helper to get a field
	 */
	public static IBeanProxy getBeanField(String fieldName, IBeanProxy aSource) {

		// Find the set method on the same VM as the source
		IFieldProxy aField = aSource.getTypeProxy().getFieldProxy(fieldName);
		try {
			return aField.get(aSource);
		} catch (ThrowableProxy e) {
			// TODO "Field could not be retrieved " + fieldName); //$NON-NLS-1$
			JavaVEPlugin.log(e, Level.WARNING);
			return null;
		}

	}
	/**
	 * Get a method proxy for the getMethod on the property and invoke it on the source bean
	 * returning the result
	 * @throws NoSuchMethodException
	 * @deprecated This should not be used. Alway go through the IBeanProxyHost.
	 */
	public static IBeanProxy readBeanFeature(PropertyDecorator aPropertyDecorator, IBeanProxy aSource) throws ThrowableProxy, NoSuchMethodException {

		if (!aSource.isValid())
			return null;	// Not valid to read.

		if(aPropertyDecorator.getReadMethod() != null){
			Method method = aPropertyDecorator.getReadMethod();
			ProxyFactoryRegistry registry = aSource.getProxyFactoryRegistry();
			// 	Now get the invokable and invoke it.
			IMethodProxy getMethodProxy = getMethodProxy(method, registry);
			return getMethodProxy.invoke(aSource);
		} else if (aPropertyDecorator.getField() != null){
			Field field = aPropertyDecorator.getField();
			// Find the get method on the same VM as the source
			IFieldProxy aField = aSource.getTypeProxy().getFieldProxy(field.getName());
			return aField.get(aSource);
		}
		return null;
	}
	
	/**
	 * Return the method proxy from the given Method.
	 * @param aMethod the method to find the proxy for.
	 * @param aRegistry
	 * @return the method proxy.
	 * @throws NoSuchMethodException if method proxy could not be found.
	 * 
	 * @since 1.1.0
	 */
	public static IMethodProxy getMethodProxy(Method aMethod, ProxyFactoryRegistry aRegistry) throws NoSuchMethodException {
		// Get the name of the method and the arguments
		String methodName = aMethod.getName();
		List inputMethods = aMethod.getParameters();
		String[] methodParms = new String[inputMethods.size()];
		for (int i = 0; i < inputMethods.size(); i++) {
			JavaParameter parm = (JavaParameter) inputMethods.get(i);
			JavaHelpers jh = parm.getJavaType();
			methodParms[i] = jh.getQualifiedNameForReflection();
		}
		String className = aMethod.getContainingJavaClass().getQualifiedNameForReflection();
		// Now get the method proxy on the same VM as the source
		IMethodProxy result = aRegistry.getMethodProxyFactory().getMethodProxy(className, methodName, methodParms);
		if (result != null)
			return result;
		else
			throw new NoSuchMethodException(aMethod.getContainingJavaClass().getJavaName()+'.'+aMethod.getMethodElementSignature());
	}

	public static IProxyMethod getMethodProxy(IExpression expression, Method aMethod) throws NoSuchMethodException {
		// Get the name of the method and the arguments
		String methodName = aMethod.getName();
		List inputMethods = aMethod.getParameters();
		String[] methodParms;
		if (inputMethods.isEmpty())
			methodParms = null;
		else {
			methodParms = new String[inputMethods.size()];
			for (int i = 0; i < inputMethods.size(); i++) {
				JavaParameter parm = (JavaParameter) inputMethods.get(i);
				JavaHelpers jh = parm.getJavaType();
				methodParms[i] = jh.getQualifiedNameForReflection();
			}
		}
		String className = aMethod.getContainingJavaClass().getQualifiedNameForReflection();
		// Now get the method proxy from the expression.
		ProxyFactoryRegistry registry = expression.getRegistry();
		IProxyMethod result = registry.getMethodProxyFactory().getMethodProxy(expression, className, methodName, methodParms);
		if (result != null)
			return result;
		else
			throw new NoSuchMethodException(aMethod.getContainingJavaClass().getJavaName()+'.'+aMethod.getMethodElementSignature());
	}

	/**
	 * Get the field proxy from the given field.
	 * @param aField
	 * @param aRegistry
	 * @return the field proxy or <code>null</code> if not found.
	 * @throws NoSuchFieldException
	 * 
	 * @since 1.1.0
	 */
	public static IFieldProxy getFieldProxy(Field aField, ProxyFactoryRegistry aRegistry) throws NoSuchFieldException {

		// Get the name of the method and the arguments
		String fieldName = aField.getName();
		String className = aField.getContainingJavaClass().getQualifiedNameForReflection();
		// Now get the method proxy on the same VM as the source
		IFieldProxy result = aRegistry.getBeanTypeProxyFactory().getBeanTypeProxy(className).getFieldProxy(fieldName);
		if (result != null)
			return result;
		else
			throw new NoSuchFieldException(aField.getContainingJavaClass().getJavaName()+'.'+aField.getName());
	}
	
	/**
	 * Get proxy field for the given field and expression.
	 * @param expression
	 * @param aField
	 * @return
	 * @throws NoSuchFieldException
	 * 
	 * @since 1.1.0
	 */
	public static IProxyField getFieldProxy(IExpression expression, Field aField) throws NoSuchFieldException {
		// Get the name of the method and the arguments
		String fieldName = aField.getName();
		String className = aField.getContainingJavaClass().getQualifiedNameForReflection();
		// Now get the method proxy from the expression.
		ProxyFactoryRegistry registry = expression.getRegistry();
		IProxyField result = registry.getMethodProxyFactory().getFieldProxy(expression, className, fieldName);
		if (result != null)
			return result;
		else
			throw new NoSuchFieldException(aField.getContainingJavaClass().getJavaName()+'.'+fieldName);
	}
	
	public static IInvokable getInvokable(Method aMethod, ProxyFactoryRegistry aRegistry) {

		// Get the name of the method and the arguments
		String methodName = aMethod.getName();
		List inputMethods = aMethod.getParameters();
		String[] methodParms = new String[inputMethods.size()];
		for (int i = 0; i < inputMethods.size(); i++) {
			JavaParameter parm = (JavaParameter) inputMethods.get(i);
			JavaHelpers jh = parm.getJavaType();
			methodParms[i] = jh.getQualifiedNameForReflection();
		}
		String className = aMethod.getContainingJavaClass().getQualifiedNameForReflection();
		// Now get the method proxy on the same VM as the source
		return aRegistry.getMethodProxyFactory().getInvokable(className, methodName, methodParms);

	}	

	/**
	 * Static helper to get the bean proxy from a bean.  This method instantiates the bean as well to ensure
	 * of its existence
	 * Note that JavaClassInstance cannot have a method getBeanProxy() on it because this would mean that the beaninfo
	 * package would need knowledge of the adaptor package for BeanProxy which is the wrong order of
	 * pre-requisites.
	 *
	 * Note: this will only work for beans that already have a beanproxyhost attached, or if the bean is a setting.
	 * If it is not a setting, then there is no way to get the ResourceSet to get the factory to create the bean proxy host.
	 * If that is possible, then should use the getBeanProxy(abean, aresourceset).
	 */
	public static IBeanProxy getBeanProxy(IJavaInstance aBean) {
		return getBeanProxy(aBean, false);
	}
	
	/**
	 * This will get the bean proxy and instantiate if not instantiated. The flag <code>noInstantiateOnError</code>
	 * means if <code>true</code> then if already tried to be instantiated but has an instantiation error, then don't
	 * try to instantiate. Just return null.
	 * 
	 * @param aBean
	 * @param noInstantiateOnError
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public static IBeanProxy getBeanProxy(IJavaInstance aBean, boolean noInstantiateOnError) {
		if (aBean == null)
			return null;
		IInternalBeanProxyHost aBeanProxyHost = (IInternalBeanProxyHost) BeanProxyUtilities.getBeanProxyHost(aBean);
		if (aBeanProxyHost == null || (aBeanProxyHost.hasInstantiationErrors() && !noInstantiateOnError))
			return null;
		aBeanProxyHost.instantiateBeanProxy();
		return aBeanProxyHost.getBeanProxy();
	}

	/**
	 * Static helper to get the bean proxy from a bean.  This method instantiates the bean as well to ensure
	 * of its existence. It will use resource set because the bean may not yet be in a resource set.
	 * Note that JavaClassInstance cannot have a method getBeanProxy() on it because this would mean that the beaninfo
	 * package would need knowledge of the adaptor package for BeanProxy which is the wrong order of
	 * pre-requisites
	 */
	public static IBeanProxy getBeanProxy(IJavaInstance aBean, ResourceSet aResourceSet) {
		if (aBean == null)
			return null;
		IInternalBeanProxyHost aBeanProxyHost = (IInternalBeanProxyHost) BeanProxyUtilities.getBeanProxyHost(aBean, aResourceSet);
		if (!aBeanProxyHost.hasInstantiationErrors())
			aBeanProxyHost.instantiateBeanProxy();
		return aBeanProxyHost.getBeanProxy();
	}

	/**
	 * Static helper to get the bean proxy host ( its BeanProxyAdaptor) from a bean. 
	 * 
	 * Note: this will only work for beans that already have a beanproxyhost attached, or if the bean is a contained by a resource.
	 * If it is not a contained (by one of its parent containers), then there is no way to get the ResourceSet to get the factory to create the bean proxy host.
	 * If that is possible, then should use the getBeanProxy(abean, aresourceset).  
	 */
	public static IBeanProxyHost getBeanProxyHost(IJavaInstance aBean) {
		if (aBean == null) {
			return null;
		} else
			return (IBeanProxyHost) EcoreUtil.getRegisteredAdapter(aBean, IBeanProxyHost.BEAN_PROXY_TYPE);
	}
	/**
	 * Static helper to get the bean proxy host ( its BeanProxyAdaptor) from a bean
	 * This is used when aBean is not in any resource set.
	 */
	public static IBeanProxyHost getBeanProxyHost(IJavaInstance aBean, ResourceSet aResourceSet) {
		if (aBean == null) {
			return null;
		} else {
			IBeanProxyHost existing = (IBeanProxyHost) EcoreUtil.getExistingAdapter(aBean,IBeanProxyHost.BEAN_PROXY_TYPE);
			return existing != null
				? existing
				: (IBeanProxyHost) EcoreUtil.getAdapterFactory(aResourceSet.getAdapterFactories(), IBeanProxyHost.BEAN_PROXY_TYPE).adaptNew(
					aBean,
					IBeanProxyHost.BEAN_PROXY_TYPE);
		}
	}
	
	/**
	 * Get the beanproxy host, creating one if not already there. It will use the resource set out of the domain if the bean is not
	 * contained within a resource set that can find the bean proxy host adapter factory.
	 * @param aBean
	 * @param domain
	 * @return the host or <code>null</code> if aBean was null.
	 * 
	 * @since 1.1.0
	 */
	public static IBeanProxyHost getBeanProxyHost(IJavaInstance aBean, EditDomain domain) {
		if (aBean != null) {
			// The java value should be in a resource.
			IBeanProxyHost settingBean = (IBeanProxyHost) EcoreUtil.getRegisteredAdapter(aBean, IBeanProxyHost.BEAN_PROXY_TYPE);
			if (settingBean == null) {
				// It isn't in a resource, so we'll get it explicitly from our domain resource.
				ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
				AdapterFactory f = EcoreUtil.getAdapterFactory(rset.getAdapterFactories(), IBeanProxyHost.BEAN_PROXY_TYPE);
				settingBean = (IBeanProxyHost) f.adaptNew(aBean, IBeanProxyHost.BEAN_PROXY_TYPE);
			}
			return settingBean;
		}
		return null;
	}

	/**
	 * Return the unqualified name, e.g. java.awt.Frame returns Frame
	 */
	public static String getUnqualifiedClassName(String aQualifiedClassName) {

		int positionOfLastPeriod = aQualifiedClassName.lastIndexOf('.');
		return aQualifiedClassName.substring(positionOfLastPeriod + 1, aQualifiedClassName.length());

	}
	public static IStringBeanProxy invoke_getMessage(IBeanProxy anExceptionProxy) {

		IMethodProxy getMessageMethodProxy = anExceptionProxy.getTypeProxy().getMethodProxy("getMessage"); //$NON-NLS-1$
		try {
			IStringBeanProxy messageProxy = (IStringBeanProxy) getMessageMethodProxy.invoke(anExceptionProxy);
			return messageProxy;
		} catch (ThrowableProxy exc) {
			return null;
		}
	}
	/**
	 * Static helper to run the method on the receiver and return the result
	 */
	public static IBeanProxy invoke(IBeanProxy aReceiver, String aMessageName) {

		IMethodProxy aMethodProxy = aReceiver.getTypeProxy().getMethodProxy(aMessageName);
		try {
			return aMethodProxy.invoke(aReceiver);
		} catch (ThrowableProxy exc) {
			if (JavaVEPlugin.isLoggingLevel(Level.WARNING)) {
				JavaVEPlugin.log("Error invoking " + aMessageName, Level.WARNING); //$NON-NLS-1$
				JavaVEPlugin.log(exc, Level.WARNING);
			}
			return null;
		}
	}
}

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: VisualUtilities.java,v $
 *  $Revision: 1.1 $  $Date: 2004-01-27 16:36:04 $ 
 */
package org.eclipse.ve.internal.java.visual;

import java.lang.reflect.Constructor;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jem.internal.beaninfo.adapters.Utilities;
import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.internal.proxy.core.IBeanTypeProxy;
import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.ClassDescriptorDecoratorPolicy;
import org.eclipse.ve.internal.jcm.BeanDecorator;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 
/**
 * 
 * @since 1.0.0
 */
public class VisualUtilities {
	
	public static final String LAYOUT_POLICY_FACTORY_CLASSNAME_KEY = "org.eclipse.ve.internal.jfc.core.layoutpolicyfactoryclassnamekey"; //$NON-NLS-1$	
	
public static ILayoutPolicyFactory getLayoutPolicyFactory(IBeanTypeProxy layoutBeanTypeProxy, EditDomain editDomain){ 
		
	// The JavaClass decorator has the factory that can be used to create the edit policy
	// Go from the IBeanTypeProxy to the EClassifier representing the javaType
	ResourceSet rset = JavaEditDomainHelper.getResourceSet(editDomain);	
	EClassifier layoutManagerClass = Utilities.getJavaClass(layoutBeanTypeProxy, rset);
	return getLayoutPolicyFactory(layoutManagerClass,editDomain);

}
/**
 * @param classifier
 * @param editDomain
 * @return
 * 
 * @since 1.0.0
 */
public static ILayoutPolicyFactory getLayoutPolicyFactory(EClassifier layoutManagerClass, EditDomain editDomain) {
	// Get the decorator policy that is a decorator and find the layout policy factory 
	ClassDescriptorDecoratorPolicy policy = ClassDescriptorDecoratorPolicy.getPolicy(editDomain);
	BeanDecorator decr = (BeanDecorator) policy.findDecorator(layoutManagerClass, BeanDecorator.class, LAYOUT_POLICY_FACTORY_CLASSNAME_KEY);
	String layoutFactoryClassname = null;
	if (decr != null)
		layoutFactoryClassname = (String) decr.getKeyedValues().get(LAYOUT_POLICY_FACTORY_CLASSNAME_KEY);
	if (layoutFactoryClassname != null) {
		try {
			Class factoryClass = CDEPlugin.getClassFromString(layoutFactoryClassname);
			ILayoutPolicyFactory fact = (ILayoutPolicyFactory) factoryClass.newInstance();
			CDEPlugin.setInitializationData(fact, layoutFactoryClassname, null);
			return fact;
		} catch (ClassNotFoundException e) {
			JavaVEPlugin.getPlugin().getMsgLogger().log(new Status(IStatus.WARNING, JavaVEPlugin.getPlugin().getDescriptor().getUniqueIdentifier(), 0, "", e), MsgLogger.LOG_WARNING); //$NON-NLS-1$
		} catch (ClassCastException e) {
			JavaVEPlugin.getPlugin().getMsgLogger().log(new Status(IStatus.WARNING, JavaVEPlugin.getPlugin().getDescriptor().getUniqueIdentifier(), 0, "", e), MsgLogger.LOG_WARNING); //$NON-NLS-1$
		} catch (InstantiationException e) {
			JavaVEPlugin.getPlugin().getMsgLogger().log(new Status(IStatus.WARNING, JavaVEPlugin.getPlugin().getDescriptor().getUniqueIdentifier(), 0, "", e), MsgLogger.LOG_WARNING); //$NON-NLS-1$
		} catch (IllegalAccessException e) {
			JavaVEPlugin.getPlugin().getMsgLogger().log(new Status(IStatus.WARNING, JavaVEPlugin.getPlugin().getDescriptor().getUniqueIdentifier(), 0, "", e), MsgLogger.LOG_WARNING); //$NON-NLS-1$
		} catch (CoreException e) {
			JavaVEPlugin.getPlugin().getMsgLogger().log(new Status(IStatus.WARNING, JavaVEPlugin.getPlugin().getDescriptor().getUniqueIdentifier(), 0, "", e), MsgLogger.LOG_WARNING); //$NON-NLS-1$
		}
	}
	return null;
}
/**
 * @param policy
 * @return EditPolicy
 * Try to instantiate the layoutInputPolicyClass using first a constructor with an argument of the ContainerPolicy and if that fails
 * a null constructor
 * @since 1.0.0
 */
public static EditPolicy getLayoutPolicy(Class layoutInputPolicyClass, org.eclipse.ve.internal.cde.core.ContainerPolicy policy) {
	EditPolicy layoutPolicy =  null;
	try {
		// See if there is a ctor that takes an IContainerInputPolicyHelper, and if there is,
		// use that ctor with the helper assigned to this view object.
		Constructor ctor = null;
		try {
			ctor = layoutInputPolicyClass.getConstructor(new Class[] {org.eclipse.ve.internal.cde.core.ContainerPolicy.class});
			layoutPolicy = (EditPolicy) ctor.newInstance(new Object[] {policy});
		} catch (NoSuchMethodException e) {
			layoutPolicy = (EditPolicy) layoutInputPolicyClass.newInstance();
		}
	} catch (Throwable e) {
		JavaVEPlugin.log("Unable to create the layout policy", MsgLogger.LOG_WARNING); //$NON-NLS-1$
		JavaVEPlugin.log(e, MsgLogger.LOG_WARNING);
	}
	return layoutPolicy;
}
}

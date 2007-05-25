/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: VisualUtilities.java,v $
 *  $Revision: 1.15 $  $Date: 2007-05-25 04:18:47 $ 
 */
package org.eclipse.ve.internal.java.visual;

import java.util.logging.Level;

import org.eclipse.core.runtime.*;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.proxy.core.IBeanTypeProxy;
import org.eclipse.jem.util.logger.proxy.Logger;

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
	
	public static final String LAYOUT_POLICY_FACTORY_CLASSNAME_KEY = "org.eclipse.ve.internal.java.visual.layoutpolicyfactoryclassnamekey"; //$NON-NLS-1$	
	
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
	ClassDescriptorDecoratorPolicy policy;
	if(editDomain != null) {
		policy = ClassDescriptorDecoratorPolicy.getPolicy(editDomain);
	} else
		policy = new ClassDescriptorDecoratorPolicy();
	
	BeanDecorator decr = (BeanDecorator) policy.findDecorator(layoutManagerClass, BeanDecorator.class, LAYOUT_POLICY_FACTORY_CLASSNAME_KEY);
	String layoutFactoryClassname = null;
	if (decr != null)
		layoutFactoryClassname = (String)decr.getKeyedValues().get(LAYOUT_POLICY_FACTORY_CLASSNAME_KEY);
	if (layoutFactoryClassname != null) {
		try {
			Class<ILayoutPolicyFactory> factoryClass = CDEPlugin.getClassFromString(layoutFactoryClassname);
			ILayoutPolicyFactory fact = factoryClass.newInstance();
			CDEPlugin.setInitializationData(fact, layoutFactoryClassname, null);
			return fact;
		} catch (ClassNotFoundException e) {
			Logger logger = JavaVEPlugin.getPlugin().getLogger();
			if (logger.isLoggingLevel(Level.WARNING))
				logger.log(new Status(IStatus.WARNING, JavaVEPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", e), Level.WARNING); //$NON-NLS-1$
		} catch (ClassCastException e) {
			Logger logger = JavaVEPlugin.getPlugin().getLogger();
			if (logger.isLoggingLevel(Level.WARNING))
				logger.log(new Status(IStatus.WARNING, JavaVEPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", e), Level.WARNING); //$NON-NLS-1$
		} catch (InstantiationException e) {
			Logger logger = JavaVEPlugin.getPlugin().getLogger();
			if (logger.isLoggingLevel(Level.WARNING))
				logger.log(new Status(IStatus.WARNING, JavaVEPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", e), Level.WARNING); //$NON-NLS-1$
		} catch (IllegalAccessException e) {
			Logger logger = JavaVEPlugin.getPlugin().getLogger();
			if (logger.isLoggingLevel(Level.WARNING))
				logger.log(new Status(IStatus.WARNING, JavaVEPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", e), Level.WARNING); //$NON-NLS-1$
		} catch (CoreException e) {
			Logger logger = JavaVEPlugin.getPlugin().getLogger();
			if (logger.isLoggingLevel(Level.WARNING))
				logger.log(new Status(IStatus.WARNING, JavaVEPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", e), Level.WARNING); //$NON-NLS-1$
		}
	}
	return null;
}
}


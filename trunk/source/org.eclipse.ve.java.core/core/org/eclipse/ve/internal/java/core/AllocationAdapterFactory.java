package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: AllocationAdapterFactory.java,v $
 *  $Revision: 1.1 $  $Date: 2004-01-12 21:44:11 $ 
 */
 
import java.text.MessageFormat;
import java.util.HashMap;

import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

/**
 * The <code>AllocatationAdapterFactory</code> class is used to attach the appropriate IAllocationAdapter to the
 * targets.
 * <p>IAllocationAdapters are used to allocate the java proxy for the given IJavaInstance. 
 * <p>AdapterFactory's should be registered for each EMF package that has JavaAllocation
 * EMF subclasses in it. Each factory should only handle classes within that EMF package. It should not handle superclasses. Those 
 * superclasses will be handled by the registered factory for that package. These factories allow the extension
 * <p>If there is no factory registered for the package of the target JavaAllocation object, then no adapter is created.
 * <p>For instance, <code>BasicAllocationAdapterFactory</code> handles the standard default JavaAllocation subclasses in the
 * <code>org.eclipse.jem.internal.instantiation</code> EMF package, e.g. <code>InitStringAllocation</code>.
 * 
 * @see org.eclipse.jem.internal.instantiation.JavaAllocation
 * @see org.eclipse.ve.internal.java.core.BasicAllocationAdapterFactory
 * @since 1.0.0  
 */
public class AllocationAdapterFactory extends AdapterFactoryImpl {
	
	protected HashMap factoryMap = new HashMap(2);	// Map of package to factory.
	
	/**
	 * Register an adapter factory for the allocation for a package.
	 * 
	 * @param epackage Package the factory will adapt to.
	 * @param factory The factory (which must adapt to type IAllocationAdapter.class)
	 * @see IAllocationAdapter
	 */
	public void registerPackageFactory(EPackage epackage, AdapterFactory factory) {
		if (!factory.isFactoryForType(IAllocationAdapter.class))
			throw new IllegalArgumentException(MessageFormat.format("{0} factory not for type IAllocationAdapter.", new Object[] {factory.getClass()}));
		factoryMap.put(epackage, factory);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.AdapterFactory#isFactoryForType(java.lang.Object)
	 */
	public boolean isFactoryForType(Object type) {
		return type == IAllocationAdapter.class;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.AdapterFactory#adaptNew(org.eclipse.emf.common.notify.Notifier, java.lang.Object)
	 */
	public Adapter adaptNew(Notifier target, Object type) {
		if (!(target instanceof EObject))
			return null;	// Not an EObject, so we can't adapt it.
			
		AdapterFactory factory = (AdapterFactory) factoryMap.get(((EObject) target).eClass().getEPackage());			
		return factory != null ? factory.adaptNew(target, type) : null;
	}

}

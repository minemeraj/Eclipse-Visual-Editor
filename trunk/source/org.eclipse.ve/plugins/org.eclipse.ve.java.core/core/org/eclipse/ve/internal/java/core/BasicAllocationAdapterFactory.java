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
 *  $RCSfile: BasicAllocationAdapterFactory.java,v $
 *  $Revision: 1.1 $  $Date: 2004-01-12 21:44:11 $ 
 */
 
import org.eclipse.emf.common.notify.Adapter;

import org.eclipse.jem.internal.instantiation.util.InstantiationAdapterFactory;

/**
 * The basic allocation adapter factory. It handles the basic allocations.
 * <ul>
 *   <li> InitStringAllocation
 *   <li> ImplicitAllocation
 * </ul>
 * 
 * <p>It needs to be registered with the AllocationAdapterFactory in use. The method registerWithFactory can be used
 * to simplify things.
 * 
 * @see org.eclipse.ve.internal.java.core.AllocationAdapterFactory
 * @see org.eclipse.jem.internal.instantiation.InitStringAllocation
 * @see org.eclipse.jem.internal.instantiation.ImplicitAllocation
 * @see org.eclipse.ve.internal.java.core.IAllocationAdapter
 * 
 * @since 1.0.0
 */
public class BasicAllocationAdapterFactory extends InstantiationAdapterFactory {
	
	/**
	 * Create and register a <code>BasicAllocationAdapterFactory</code> with the <code>AllocationAdapterFactory</code>.
	 * 
	 * @param factory The factory to register with.
	 * @return The created factory.
	 * 
	 * @see AllocationAdapterFactory
	 */
	public static BasicAllocationAdapterFactory registerWithFactory(AllocationAdapterFactory factory) {
		BasicAllocationAdapterFactory newFactory = new BasicAllocationAdapterFactory();
		factory.registerPackageFactory(modelPackage, newFactory);
		return newFactory;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.AdapterFactory#isFactoryForType(java.lang.Object)
	 */
	public boolean isFactoryForType(Object object) {
		return object == IAllocationAdapter.class;	// Override to accept this instead of the default for superclass.
	}

	private static final InitStringAllocationAdapter INIT_STRING_ALLOCATION_ADAPTER = new InitStringAllocationAdapter();
	private static final ImplicitAllocationAdapter IMPLICIT_ALLOCATION_ADAPTER = new ImplicitAllocationAdapter();
	
	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.instantiation.util.InstantiationAdapterFactory#createInitStringAllocationAdapter()
	 */
	public Adapter createInitStringAllocationAdapter() {
		return INIT_STRING_ALLOCATION_ADAPTER;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.instantiation.util.InstantiationAdapterFactory#createImplicitAllocationAdapter()
	 */
	public Adapter createImplicitAllocationAdapter() {
		return IMPLICIT_ALLOCATION_ADAPTER;
	}

}

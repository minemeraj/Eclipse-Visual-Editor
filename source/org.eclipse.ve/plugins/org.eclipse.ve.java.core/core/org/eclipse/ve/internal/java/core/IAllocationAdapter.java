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
 *  $RCSfile: IAllocationAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2004-01-12 21:44:11 $ 
 */
 
import org.eclipse.emf.common.notify.Adapter;

import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

/**
 * The adapter for allocating the appropriate proxy.
 * <p>
 * The allocate method is called when allocation is required.
 * <p>
 * Typically these adapters can be singletons. The JavaAllocation is passed in on the request. This should cut down
 * on the memory usage.
 * 
 * @see org.eclipse.jem.internal.instantiation.JavaAllocation
 * @see org.eclipse.ve.internal.java.core.AllocationAdapterFactory
 * @since 1.0.0
 */
public interface IAllocationAdapter extends Adapter {
	
	/**
	 * Exception occurred during allocation. The exception was caught and logged to the logging file. It would then
	 * be wrappered in an <code>AllocationException</code> and rethrown. Any other RuntimeExceptions that
	 * are thrown will be passed on through.
	 */
	public static class AllocationException extends Exception {
		private final Throwable exc;
		
		/**
		 * Construct an <code>AllocationException</code>
		 * 
		 * @param exc The exception that is being wrappered.
		 */
		public AllocationException(Throwable exc) {
			this.exc = exc;
		}
		
		/**
		 * Get the wrappered exception.
		 * 
		 * @return The wrappered exception.
		 */
		public Throwable getWrapperedException() {
			return exc;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Throwable#getMessage()
		 */
		public String getMessage() {
			return exc.getMessage();
		}

	}
	
	/**
	 * This method performs the actual allocation and return the appropriate IBeanProxy.
	 * 
	 * <p>If any exceptions are thrown, the exceptions should already be logged by the Allocation adapter implementation.
	 * The problem is there is no way to know what all possible exceptions can be thrown. The list depends on the implementation.
	 * 
	 * @param allocation The allocation object that this adapter is on.  It is passed in rather than
	 * use the adapter's target so that the adapter can be a singleton.
	 * @param domain The domain to do the allocation within.
	 * @return The allocated proxy.
	 * @throws AllocationException
	 */
	public IBeanProxy allocate(JavaAllocation allocation, IBeanProxyDomain domain) throws AllocationException;

}

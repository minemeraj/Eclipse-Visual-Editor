/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IAllocationProcesser.java,v $
 *  $Revision: 1.3 $  $Date: 2004-08-27 15:34:10 $ 
 */
package org.eclipse.ve.internal.java.core;


import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

/**
 * The processer for allocating the appropriate proxy.
 * <p>
 * The allocate method is called when allocation is required.
 * <p>
 * The processer is associated with an IBeanProxyDomain. It can be retrieved from there. The processer cannot be
 * shared with other IBeanProxyDomains.
 * 
 * @see org.eclipse.jem.internal.instantiation.JavaAllocation
 * @since 1.0.0
 */
public interface IAllocationProcesser {
	
	/**
	 * Exception occurred during allocation. The exception was caught and logged to the logging file. It would then
	 * be wrappered in an <code>AllocationException</code> and rethrown. Any other RuntimeExceptions that
	 * are thrown will be passed on through.
	 */
	public static class AllocationException extends Exception {
		
		/**
		 * Construct an <code>AllocationException</code>
		 * 
		 * @param exc The exception that is being wrappered.
		 */
		public AllocationException(Throwable exc) {
			super(exc);
		}
				
		/* (non-Javadoc)
		 * @see java.lang.Throwable#getMessage()
		 */
		public String getMessage() {
			return getCause().getMessage();
		}

	}
	
	/**
	 * This method performs the actual allocation and return the appropriate IBeanProxy.
	 * 
	 * <p>If any exceptions are thrown, the exceptions should already be logged by the Allocation adapter implementation.
	 * The problem is there is no way to know what all possible exceptions can be thrown. The list depends on the implementation.
	 * 
	 * @param allocation The allocation object to allocate for. 
	 * @return The allocated proxy.
	 * @throws AllocationException
	 */
	public IBeanProxy allocate(JavaAllocation allocation) throws AllocationException;
	
	/**
	 * Set the domain to use with this processer. 
	 * @param domain The domain to use. 
	 * 
	 * @since 1.0.0
	 */
	public void setBeanProxyDomain(IBeanProxyDomain domain);

}

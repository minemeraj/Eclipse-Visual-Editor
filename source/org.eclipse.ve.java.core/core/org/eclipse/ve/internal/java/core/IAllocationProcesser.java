/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IAllocationProcesser.java,v $
 *  $Revision: 1.7 $  $Date: 2005-06-16 17:46:06 $ 
 */
package org.eclipse.ve.internal.java.core;


import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.PTExpression;
import org.eclipse.jem.internal.proxy.core.*;

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
	 * Exception occurred during allocation. The exception was caught. It would then
	 * be wrappered in an <code>AllocationException</code> and rethrown. Any other RuntimeExceptions that
	 * are thrown will be passed on through.
	 */
	public static class AllocationException extends Exception {
		
		/**
		 * Comment for <code>serialVersionUID</code>
		 * 
		 * @since 1.1.0
		 */
		private static final long serialVersionUID = -5219460831116485340L;

		/**
		 * Construct an <code>AllocationException</code>
		 * 
		 * @param exc The exception that is being wrappered.
		 */
		public AllocationException(Throwable exc) {
			super(exc);
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
	 * This method performs the actual allocation and return the appropriate IProxy.
	 * 
	 * <p>If any exceptions are thrown, the exceptions should already be logged by the Allocation adapter implementation.
	 * The problem is there is no way to know what all possible exceptions can be thrown. The list depends on the implementation.
	 * <p>
	 * This will use the given allocation to do the allocation.
	 *  
	 * @param allocation
	 * @param expression the expression to use. The expression will be valid after the call, even if AllocationException is thrown. In that case the
	 * state of the expression will be as it was at the start of the call.
	 * @return the expression proxy for allocation.
	 * @throws AllocationException
	 * 
	 * @since 1.1.0
	 */
	public IProxy allocate(JavaAllocation allocation, IExpression expression) throws AllocationException;
	
	/**
	 * This method performs the actual allocation and return the appropriate IProxy.
	 * 
	 * <p>If any exceptions are thrown, the exceptions should already be logged by the Allocation adapter implementation.
	 * The problem is there is no way to know what all possible exceptions can be thrown. The list depends on the implementation.
	 * <p>
	 * This will use the given expression to do the allocation.

	 * @param parseTreeExpression
	 * @param expression
	 * @return
	 * @throws AllocationException
	 * 
	 * @since 1.1.0
	 */
	public IProxy allocate(PTExpression parseTreeExpression, IExpression expression) throws AllocationException;
	
	/**
	 * Set the domain to use with this processer. 
	 * @param domain The domain to use. 
	 * 
	 * @since 1.0.0
	 */
	public void setBeanProxyDomain(IBeanProxyDomain domain);

}

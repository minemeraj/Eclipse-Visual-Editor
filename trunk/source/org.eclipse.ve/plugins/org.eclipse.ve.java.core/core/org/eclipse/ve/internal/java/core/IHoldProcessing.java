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
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: IHoldProcessing.java,v $
 *  $Revision: 1.3 $  $Date: 2005-02-15 23:23:54 $ 
 */

/**
 * Implemented by ContainerProxyAdapter and JTabbedPaneProxyAdapter. It allows these
 * adapters to be told to stop processing changes and when done to refresh. Each
 * adapter will determine what it stops listening to and what it refreshes when done.
 * 
 * This is a common interface to allow a simple command to be able to wrapper a bunch
 * of requests and hold during the processing.
 */
public interface IHoldProcessing {
	
	/**
	 * Increment and hold processing. Increment the number of holds
	 * in existence and if this is the first hold, start holding.
	 */
	public void holdProcessing();
	
	/**
	 * Decrement hold processing, and when count goes to zero, restart
	 * processing and refresh.
	 */
	public void resumeProcessing();

}

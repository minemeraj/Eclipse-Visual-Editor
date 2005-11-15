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

import org.eclipse.jem.internal.instantiation.JavaAllocation;

/*
 * $RCSfile: IJavaCellEditor2.java,v $ $Revision: 1.1 $ $Date: 2005-11-15 18:53:28 $
 */

/**
 * A java cell editor that can return a {@link JavaAllocation} instead of an init string.
 */
public interface IJavaCellEditor2 {

	/**
	 * Get the java allocation, or <code>null</code> if no allocation. 
	 * @return the java allocation or <code>null</code> if no allocation.
	 * 
	 * @since 1.2.0
	 */
	JavaAllocation getJavaAllocation();

}

/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  Created Oct 14, 2005 by Gili Mendel
 * 
 *  $RCSfile: IUpdatableFactory.java,v $
 *  $Revision: 1.2 $  $Date: 2005-10-18 17:30:52 $ 
 */

package org.eclipse.jface.binding;

/**
 * @since 3.2
 * 
 */
public interface IUpdatableFactory {

	/**
	 * Factory would determine what type of IUpdatable to created from the
	 * attribute.
	 * 
	 * @param object
	 * @param attribute
	 * @return the updatable
	 */
	IUpdatable createUpdatable(Object object, Object attribute);
}

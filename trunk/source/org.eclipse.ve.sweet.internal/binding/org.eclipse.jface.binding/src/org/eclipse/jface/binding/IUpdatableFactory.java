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
 *  $Revision: 1.1 $  $Date: 2005-10-17 23:06:30 $ 
 */
 
package org.eclipse.jface.binding;
 

public interface IUpdatableFactory {
	
		// Factory would determine what type of IUpdatable to created from the attribute.
		IUpdatable createUpdatable(Object object, Object attribute);
}

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
 *  $RCSfile: PropertySourceContributor.java,v $
 *  $Revision: 1.1 $  $Date: 2005-09-21 23:09:01 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.util.List;
 

public interface PropertySourceContributor {

	void contributePropertyDescriptors(List descriptorsList);

}

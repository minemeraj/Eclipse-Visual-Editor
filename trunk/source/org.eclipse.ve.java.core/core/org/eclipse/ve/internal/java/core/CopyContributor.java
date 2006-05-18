/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
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
 *  $RCSfile: CopyContributor.java,v $
 *  $Revision: 1.2 $  $Date: 2006-05-17 20:14:52 $ 
 */

import org.eclipse.jem.internal.instantiation.PTMethodInvocation;

public interface CopyContributor {

	void contributeToCopy(PTMethodInvocation factoryMethodCall);

}
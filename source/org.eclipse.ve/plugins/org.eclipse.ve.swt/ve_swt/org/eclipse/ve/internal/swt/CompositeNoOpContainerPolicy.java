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
/*
 * $RCSfile: CompositeNoOpContainerPolicy.java,v $ $Revision: 1.1 $ $Date: 2005-06-07 13:38:07 $
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.ve.internal.cde.core.EditDomain;

public class CompositeNoOpContainerPolicy extends CompositeContainerPolicy {
	
	public CompositeNoOpContainerPolicy(EditDomain anEditDomain){
		super(anEditDomain);
	}
	protected boolean isValidChild(Object child,EStructuralFeature containmentSF) {
		// No one can be dropped onto us
		return false;
	}
}	
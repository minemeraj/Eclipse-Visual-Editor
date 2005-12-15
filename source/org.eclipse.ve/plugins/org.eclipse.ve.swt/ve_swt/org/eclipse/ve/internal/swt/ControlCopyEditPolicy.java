/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ControlCopyEditPolicy.java,v $
 *  $Revision: 1.9 $  $Date: 2005-12-15 14:54:55 $ 
 */

package org.eclipse.ve.internal.swt;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.cde.core.EditDomain;

public class ControlCopyEditPolicy extends WidgetCopyEditPolicy {
	
	public ControlCopyEditPolicy(EditDomain anEditDomain) {
		super(anEditDomain);
	}
	
	protected void cleanup(IJavaInstance javaBeanToCopy) {

		super.cleanup(javaBeanToCopy);
		// Strip the bounds, size, location and layoutData from the primary object being copied
		IJavaInstance copiedObject = (IJavaInstance) copier.get(javaBeanToCopy);
		removeReferenceTo(copiedObject,"bounds",javaBeanToCopy); //$NON-NLS-1$
		removeReferenceTo(copiedObject,"size",javaBeanToCopy); //$NON-NLS-1$
		removeReferenceTo(copiedObject,"location",javaBeanToCopy); //$NON-NLS-1$
		removeReferenceTo(copiedObject,"layoutData",javaBeanToCopy);		 //$NON-NLS-1$
	}
	
}

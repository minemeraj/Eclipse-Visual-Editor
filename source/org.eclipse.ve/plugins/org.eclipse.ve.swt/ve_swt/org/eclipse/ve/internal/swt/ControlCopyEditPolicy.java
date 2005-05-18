/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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
 *  $Revision: 1.3 $  $Date: 2005-05-18 16:48:00 $ 
 */

package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

public class ControlCopyEditPolicy extends WidgetCopyEditPolicy {
	
	protected boolean shouldCopyFeature(EStructuralFeature feature, Object eObject) {
		if(feature != null && "allocation".equals(feature.getName())){ //$NON-NLS-1$
			return false;
		} else {
			return super.shouldCopyFeature(feature, eObject);
		}
	}
	
	protected void cleanup(IJavaInstance javaBeanToCopy, Copier aCopier) {

		super.cleanup(javaBeanToCopy, aCopier);
		// Strip the bounds, size, location and layoutData from the primary object being copied
		IJavaInstance copiedObject = (IJavaInstance) aCopier.get(javaBeanToCopy);
		removeReferenceTo(copiedObject,"bounds",aCopier); //$NON-NLS-1$
		removeReferenceTo(copiedObject,"size",aCopier); //$NON-NLS-1$
		removeReferenceTo(copiedObject,"location",aCopier); //$NON-NLS-1$
		removeReferenceTo(copiedObject,"layoutData",aCopier);		 //$NON-NLS-1$
	}
	
}

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
 *  $Revision: 1.4 $  $Date: 2005-05-27 12:49:22 $ 
 */

package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

public class ControlCopyEditPolicy extends WidgetCopyEditPolicy {
	
	protected boolean shouldCopyFeature(EStructuralFeature feature, Object eObject) {		
		if(feature != null){
			if ("allocation".equals(feature.getName()) //$NON-NLS-1$
			 || eObject instanceof EStructuralFeature){ // Implicit allocation points to the meta layer and we don't want to copy up into this
				return false;				
			}
		}
		return super.shouldCopyFeature(feature, eObject);
	}
	
	protected boolean shouldExpandFeature(EStructuralFeature feature, Object eObject) {
		if(eObject instanceof EStructuralFeature){
			return false;
		} 
		if(feature != null && feature.getName().equals("allocation")){
			return false;
		}
		return super.shouldExpandFeature(feature,eObject);
	}
	
	protected void cleanup(IJavaInstance javaBeanToCopy) {

		super.cleanup(javaBeanToCopy);
		// Strip the bounds, size, location and layoutData from the primary object being copied
		IJavaInstance copiedObject = (IJavaInstance) copier.get(javaBeanToCopy);
		removeReferenceTo(copiedObject,"bounds",copier); //$NON-NLS-1$
		removeReferenceTo(copiedObject,"size",copier); //$NON-NLS-1$
		removeReferenceTo(copiedObject,"location",copier); //$NON-NLS-1$
		removeReferenceTo(copiedObject,"layoutData",copier);		 //$NON-NLS-1$
	}
	
}

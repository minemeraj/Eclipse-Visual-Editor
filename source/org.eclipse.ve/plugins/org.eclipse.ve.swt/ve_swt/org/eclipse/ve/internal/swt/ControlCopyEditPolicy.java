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
 *  $Revision: 1.1 $  $Date: 2005-05-12 11:40:55 $ 
 */

package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaClass;

public class ControlCopyEditPolicy extends WidgetCopyEditPolicy {
	
	private JavaClass javaClass;
	private Copier copier;

	protected void cleanup(IJavaInstance javaBeanToCopy, Copier aCopier) {

		super.cleanup(javaBeanToCopy, copier);
		// Strip the bounds, size, location and layoutData from the primary object being copied
		javaClass = (JavaClass)javaBeanToCopy.getJavaType();
		copier = aCopier;
		removeReferenceTo(javaBeanToCopy,"bounds");
		removeReferenceTo(javaBeanToCopy,"size");
		removeReferenceTo(javaBeanToCopy,"location");
		removeReferenceTo(javaBeanToCopy,"layoutData");		
	}

	private void removeReferenceTo(EObject javaBeanToCopy, String featureName) {

		Object propertyValue = javaBeanToCopy.eGet(javaClass.getEStructuralFeature(featureName));
		if(propertyValue != null){
			copier.remove(propertyValue);
		}
		
	}
	
}

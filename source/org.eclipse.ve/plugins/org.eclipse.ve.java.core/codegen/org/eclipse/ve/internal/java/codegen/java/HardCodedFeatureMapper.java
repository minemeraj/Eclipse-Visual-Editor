/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: HardCodedFeatureMapper.java,v $
 *  $Revision: 1.1 $  $Date: 2004-08-20 15:58:42 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.Statement;

 

/**
 * 
 * @since 1.0.0
 */
public class HardCodedFeatureMapper extends AbstractFeatureMapper {
	
	String featureName ;
	
	public HardCodedFeatureMapper (String featureName, String methodName) {
		this.featureName = featureName;
		fMethodName = methodName;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper#getFeature(org.eclipse.jdt.core.dom.Statement)
	 */
	public EStructuralFeature getFeature(Statement expr) {
		if (fSF!=null) return fSF;
		
		EStructuralFeature sf = ((EObject)fRefObj).eClass().getEStructuralFeature(featureName) ;
		setFeature(sf);
		return fSF;
	}

}

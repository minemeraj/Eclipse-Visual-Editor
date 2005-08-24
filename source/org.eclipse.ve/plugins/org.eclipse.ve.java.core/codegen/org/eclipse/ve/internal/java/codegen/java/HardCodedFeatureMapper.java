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
 *  $RCSfile: HardCodedFeatureMapper.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:45 $ 
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

/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.codegen.java;
/*
 *  $RCSfile: IJavaFeatureMapper.java,v $
 *  $Revision: 1.7 $  $Date: 2004-08-27 15:34:09 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

/**
 *  An IFeatureMapper is a IExpressionDecoder helper which comes
 *  to provide java method to PropertyDecorator/SF mapping (and vice versa)
 */
public interface IJavaFeatureMapper  {
	
	public static String  CONSTRAINT_FEATURE_NAME = "constraints" ; //$NON-NLS-1$
	public static String  COMPONENT_FEATURE_NAME = "components" ; //$NON-NLS-1$
	
//  Given an expression, what is the SF.  The SF will be
//  calculated once, and cached from that point.	
public EStructuralFeature	getFeature(Statement expr) ;
public void  setFeature(EStructuralFeature sf) ;
public String getFeatureName() ;
public void setRefObject(IJavaInstance obj) ;
public IJavaInstance getRefObject() ;
public String getMethodName() ;
public String getIndexMethodName() ;
public String getReadMethodName() ;
public PropertyDecorator getDecorator() ;
public boolean isFieldFeature() ;

// Expresson priority will determine where new expressions will 
// be inserted in the code

public class VEexpressionPriority {	
    int priority;  // Expression "feature" level priority
    int index;     // In the case of same "feature", index  (e.g., z order of add(Foo)
    
    public VEexpressionPriority (int p, int i) {
    	priority = p;
    	index=i;    	
    }
	public int getProiority() {  		
		return priority;
	}
	public int getProiorityIndex() {  	
		return index;
	}
	public String toString() {
		if (priority<0) 
			return "[NO Priority]"; //$NON-NLS-1$
		else
			return "["+priority+", "+index+"]"; //$NON-NLS-1$
	}
}

/**
 * 
 * @param methodName  feature's method name
 * @return feature level priority
 * The VEexpresssionPriority will determine where an expression be inserted
 * in the code.
 * The feature level priority comes to force ordering between features.
 * e.g., setLayout()  will come before add(component)
 */
public int getFeaturePriority(String methodName);

// Higher Values will come first in the code ... 
// priority will be determined by FeatureMappers using
// getFeaturePriority()
// The following are generic, preCanned priorities
public static final int PRIORITY_DEFAULT =				10000;
//Add to a container should be at the end all default expression settings 
public static final int PRIORITY_ADD = 					PRIORITY_DEFAULT - 5000; 
// Constructor Expression, all at the top
public static final int PRIORITY_CONSTRUCTOR = 			100000;


public static final VEexpressionPriority NOPriority = new VEexpressionPriority(-1,-1);
public static final VEexpressionPriority DEFAULTPriority = new VEexpressionPriority(PRIORITY_DEFAULT,0);

}

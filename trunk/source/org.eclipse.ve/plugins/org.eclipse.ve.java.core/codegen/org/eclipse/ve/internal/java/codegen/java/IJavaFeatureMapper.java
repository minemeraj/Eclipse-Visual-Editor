package org.eclipse.ve.internal.java.codegen.java;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IJavaFeatureMapper.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.internal.compiler.ast.Statement;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

/**
 *  An IFeatureMapper is a IExpressionDecoder helper which comes
 *  to provide java method to PropertyDecorator/SF mapping (and vice versa)
 */
public interface IJavaFeatureMapper {
	
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
public int getPriorityIncrement(String methodName);

public static final int INTER_PRIORITY_GAP = 5;

// Higher Values will come first in the code
public static final int PRIORITY_DEFAULT = 0;
public static final int PRIORITY_CONSTRAINT_CHANGE = PRIORITY_DEFAULT + INTER_PRIORITY_GAP;
public static final int PRIORITY_ADD_CHANGE = PRIORITY_CONSTRAINT_CHANGE + INTER_PRIORITY_GAP;
public static final int PRIORITY_LAYOUT_CHANGE = PRIORITY_ADD_CHANGE + INTER_PRIORITY_GAP;
public static final int PRIORITY_IMPLICIT = PRIORITY_LAYOUT_CHANGE + INTER_PRIORITY_GAP;
public static final int PRIORITY_INIT_EXPR = Integer.MAX_VALUE;
}
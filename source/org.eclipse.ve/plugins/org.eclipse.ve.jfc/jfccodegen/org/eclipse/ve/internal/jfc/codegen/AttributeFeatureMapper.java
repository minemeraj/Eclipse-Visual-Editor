package org.eclipse.ve.internal.jfc.codegen;
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
 *  $RCSfile: AttributeFeatureMapper.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 23:13:34 $ 
 */
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.emf.common.util.URI;

import org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper;
import org.eclipse.ve.internal.java.codegen.java.PropertyFeatureMapper;
import org.eclipse.ve.internal.jfc.core.JFCConstants;

public class AttributeFeatureMapper extends PropertyFeatureMapper implements IJFCFeatureMapper {

/* The following features are not properties, and have special handling */
protected final static String hardCodeMethods[] = {
	IJFCFeatureMapper.CONSTRAINT_BOUND,
	IJFCFeatureMapper.CONSTRAINT_SIZE,
	IJFCFeatureMapper.LOCATION_NAME
};

protected final static URI hardCodedURI[] = {
	JFCConstants.SF_COMPONENT_BOUNDS,
	JFCConstants.SF_COMPONENT_SIZE,
	JFCConstants.SF_COMPONENT_LOCATION
};


/**
 * The assumption is that it is a vanilla attribute, and a PropertyDecorator exists
 */
protected boolean isHardCodedMethod (String method) {
	if (method==null) return false;
	for (int i = 0; i < hardCodeMethods.length; i++) {
		if (method.equals(hardCodeMethods[i])) return true ;
	}
	return false ;
}

protected void processHardCodedProperty(String method, Object bean) {
	if (method!=null && bean!=null && (bean instanceof IJavaObjectInstance)) {	
		for (int i = 0; i < hardCodeMethods.length; i++) {
			if (method.equals(hardCodeMethods[i])) {
				hardCode(JavaInstantiation.getSFeature((IJavaObjectInstance)bean,hardCodedURI[i])) ;
				return ;
			}
		}
	}	
}


/**
 * @see org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper#getPriorityIncrement(String)
 */
public int getPriorityIncrement(String methodType) {
	if (methodType.equals("setLayout")) //$NON-NLS-1$
		return IJavaFeatureMapper.PRIORITY_LAYOUT_CHANGE;
	if(methodType.equals("setModel")) //$NON-NLS-1$
		return 1;
	if(methodType.equals("setAutoCreateColumnsFromModel")) //$NON-NLS-1$
		return IJavaFeatureMapper.PRIORITY_ADD_CHANGE+1;
	return super.getPriorityIncrement(methodType);
}


}
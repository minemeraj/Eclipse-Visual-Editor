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
package org.eclipse.ve.internal.jfc.codegen;
/*
 *  $RCSfile: AttributeFeatureMapper.java,v $
 *  $Revision: 1.10 $  $Date: 2005-08-24 23:38:12 $ 
 */
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.java.codegen.java.PropertyFeatureMapper;

import org.eclipse.ve.internal.jfc.core.JFCConstants;

public class AttributeFeatureMapper extends PropertyFeatureMapper implements IJFCFeatureMapper {
	

/* The following features are not properties, and have special handling */
protected final static String hardCodeMethods[] = {
	IJFCFeatureMapper.CONSTRAINT_BOUND,
	IJFCFeatureMapper.CONSTRAINT_SIZE,
	IJFCFeatureMapper.LOCATION_NAME,
	IJFCFeatureMapper.LAYOUT_NAME,
	IJFCFeatureMapper.JTABLE_MODEL_NAME,
	IJFCFeatureMapper.JTABLE_AUTOCREATECOLUMNSFROMMODEL_NAME,
	IJFCFeatureMapper.JLIST_MODEL_NAME
};

protected final static URI hardCodedURI[] = {
	JFCConstants.SF_COMPONENT_BOUNDS,
	JFCConstants.SF_COMPONENT_SIZE,
	JFCConstants.SF_COMPONENT_LOCATION,
	JFCConstants.SF_CONTAINER_LAYOUT,
	JFCConstants.SF_JTABLE_MODEL,
	JFCConstants.SF_JTABLE_AUTOCREATECOLUMNSFROMMODEL,
	JFCConstants.SF_JLIST_MODEL
};


/**
 * The assumption is that it is a vanilla attribute, and a PropertyDecorator exists
 */
protected boolean isHardCodedMethod (String method, Object bean) {
	if (method==null) return false;
	for (int i = 0; i < hardCodeMethods.length; i++) {
		if (method.equals(hardCodeMethods[i])) {
			if(bean!=null && bean instanceof IJavaObjectInstance){
				IJavaObjectInstance joi = (IJavaObjectInstance) bean;
				JavaHelpers joiType = joi.getJavaType();
				EStructuralFeature methodSF = JavaInstantiation.getSFeature(joi, hardCodedURI[i]);
				if(		joiType!=null && methodSF!=null && 
						methodSF.eContainer()!=null && methodSF.eContainer() instanceof JavaHelpers){
					JavaHelpers methodClassType = (JavaHelpers) methodSF.eContainer();
					// If SF class and the passed in class are not the same - then continue searching. 
					if(methodClassType.isAssignableFrom(joiType))
						return true; 
					else
						continue;
				}
			}
			return true;
		}
	}
	return false ;
}

protected void processHardCodedProperty(String method, Object bean) {
	if (method!=null && bean!=null && (bean instanceof IJavaObjectInstance)) {	
		for (int i = 0; i < hardCodeMethods.length; i++) {
			if (method.equals(hardCodeMethods[i])) {
				IJavaObjectInstance joi = (IJavaObjectInstance) bean;
				JavaHelpers joiType = joi.getJavaType();
				EStructuralFeature methodSF = JavaInstantiation.getSFeature(joi, hardCodedURI[i]);
				if(		joiType!=null && methodSF!=null && 
						methodSF.eContainer()!=null && methodSF.eContainer() instanceof JavaHelpers){
					JavaHelpers methodClassType = (JavaHelpers) methodSF.eContainer();
					// If SF class and the passed in class are not the same - then continue searching. 
					if(methodClassType.isAssignableFrom(joiType)){
						hardCode(JavaInstantiation.getSFeature((IJavaObjectInstance)bean,hardCodedURI[i])) ;
						return ;
					}else{
						continue;
					}
				}
				hardCode(JavaInstantiation.getSFeature((IJavaObjectInstance)bean,hardCodedURI[i])) ;
				return ;
			}
		}
	}	
}


/**
 * @see org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper#getFeaturePriority(String)
 */
public int getFeaturePriority(String methodType) {
	if (methodType.equals(IJFCFeatureMapper.LAYOUT_NAME)) //$NON-NLS-1$
		return PRIORITY_LAYOUT;
	if(methodType.equals(IJFCFeatureMapper.JTABLE_MODEL_NAME)) //$NON-NLS-1$
		return PRIORITY_JTABLE_MODEL;
	if(methodType.equals(IJFCFeatureMapper.JTABLE_AUTOCREATECOLUMNSFROMMODEL_NAME)) //$NON-NLS-1$
		return PRIORITY_JTABLE_AUTOCREATECOLUMNSFROMMODEL;
	if (methodType.equals(IJFCFeatureMapper.ABSTRACTBUTTON_ACTION_NAME)) //$NON-NLS-1$
		return PRIORITY_ACTION;
	if (methodType.equals(IJFCFeatureMapper.COMPONENT_VISIBLE_NAME)) //$NON-NLS-1$
		return PRIORITY_VISIBLE;
	return super.getFeaturePriority(methodType);
}


}

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
package org.eclipse.ve.internal.java.codegen.java;
/*
 *  $RCSfile: PropertyFeatureMapper.java,v $
 *  $Revision: 1.13 $  $Date: 2005-09-16 13:34:47 $ 
 */
import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.beaninfo.core.Utilities;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.Method;
import org.eclipse.jem.internal.java.beaninfo.IIntrospectionAdapter;

public class PropertyFeatureMapper extends AbstractFeatureMapper {


static HashMap     fdecoratorCache = new HashMap() ;   
static public ResourceSet fdecoratorRSCache = null ;


private List primGetDecorators(EObject metaObject) {

	if (JavaVEPlugin.isLoggingLevel(Level.FINEST))
		JavaVEPlugin.log("AttributeFeatureMapper.primGetDecorators: " + ((EClass) metaObject).getName(), Level.FINEST); //$NON-NLS-1$
	EList properties = ((JavaClass) metaObject).getAllProperties();
	fdecoratorCache.put(metaObject, properties);
	Iterator itr = Utilities.getPropertiesIterator(properties);
	ArrayList list = new ArrayList();
	while (itr.hasNext()) {
		list.add(itr.next());
	}
	return list;
}

/**
 * We are going to catch the PropertyDecorators
 */
protected Iterator getPropertiesIterator(EObject obj) {
    
    EClass metaObject = obj.eClass() ;
    if (metaObject==null || metaObject.eResource()==null) return null ;
    ResourceSet rs ;
    if (obj.eResource() != null && obj.eResource().getResourceSet() != null)
       rs = obj.eResource().getResourceSet() ;
    else
       rs = metaObject.eResource().getResourceSet() ;
    if (rs == null) return null ;
    
	// It is possible that properties have changed on the same resource set (file savedAs etc)
	// In this case we need to reSet the cache.
	EList cachedProps = (EList) fdecoratorCache.get(metaObject);
	if (cachedProps != null) {	
	   EList properties = ((JavaClass)metaObject).getAllProperties() ;
	   if (cachedProps != properties)
	       fdecoratorRSCache = null ;
	}
     
    if (fdecoratorRSCache==null || rs != fdecoratorRSCache) {
        // Poor Man's cache at this point
    	if (JavaVEPlugin.isLoggingLevel(Level.FINE))
    		JavaVEPlugin.log("AttributeFeatureMapper.getPropertiesIterator():  *ClearingCache*", Level.FINE) ; //$NON-NLS-1$
        fdecoratorCache.clear();
        fdecoratorRSCache = rs ;
    }
    List decorators = null ;
    String handle = ((JavaClass)(metaObject)).getJavaName() ;
    decorators = (List) fdecoratorCache.get(handle) ;
    // This does not really do much, as it is cleared once BeanInfo recycles
    IIntrospectionAdapter ia = (IIntrospectionAdapter)EcoreUtil.getRegisteredAdapter(metaObject,IIntrospectionAdapter.ADAPTER_KEY);    
    if (decorators == null || ia==null || ia.isStale()) {  // Get them again.
       decorators = primGetDecorators(metaObject) ;
       fdecoratorCache.put(handle, decorators) ;       
    }
    if (decorators != null)
      return decorators.iterator() ;
    else
      return null ;
}

/**
 * Force a feature mapper to become sf
 */
protected void hardCode(EStructuralFeature sf) {
	fSF = sf ;
	fPD = Utilities.getPropertyDecorator(sf);	
}

/**
 * Enable extenders to filter methods
 */
protected boolean isHardCodedMethod (String method, Object bean) {
	return false ;
}

/**
 * Enable an extender to hard code a feature
 */
protected void processHardCodedProperty(String method, Object bean) {
}

public EStructuralFeature getImplicitFeature (Expression implicitExpression) {
	if (fSF != null)
		return fSF;
	if (fRefObj == null || implicitExpression == null)
		return null;
		

	List args = null;
	if (implicitExpression instanceof MethodInvocation)
		args = ((MethodInvocation) implicitExpression).arguments();
	
	fMethodName = getPropertyMethod(implicitExpression);

	if (isHardCodedMethod(fMethodName, fRefObj)) {
		processHardCodedProperty(fMethodName, fRefObj);
	}
	else {
		Iterator itr = getPropertiesIterator(fRefObj);
		// Find a write method that matches the one in the Expression.
		while (itr.hasNext()) {
			try {
				PropertyDecorator pd = (PropertyDecorator) itr.next();

				if (pd.getField() == null){
					Method m = pd.getReadMethod();
					if (m == null
							|| !m.getName().equals(fMethodName)
							|| args == null
							|| (m.listParametersWithoutReturn().length != args.size()))
						continue;

					//TODO: Need to check argument types
					fSF = (EStructuralFeature) pd.getEModelElement();
					fSFname = fSF.getName();
					fPD = pd;
					break;
				} else {
					if (pd.getField().getName().equals(fMethodName)){
						fSF = (EStructuralFeature) pd.getEModelElement();
						fSFname = fSF.getName();
						fPD = pd;
						fisMethod = false;						
						break;						
					}
				}
			} catch (Exception e) {
				if (JavaVEPlugin.isLoggingLevel(Level.WARNING))
					JavaVEPlugin.log("PropertyFeatureMapper.getFeature() : " + e, Level.WARNING); //$NON-NLS-1$
				continue;
			}

		}
	}

	if (fSF != null) {
		fSFname = fSF.getName();
	}
	return fSF;		
}

public EStructuralFeature getFeature (Statement settingStatement) {
	
	if (fSF != null)
		return fSF;
	if (fRefObj == null || settingStatement == null || 
	    !(settingStatement instanceof ExpressionStatement))
		return null;

	Expression expr = ((ExpressionStatement)settingStatement).getExpression();	
	getMethodName(settingStatement);

	List args = null;
	if (expr instanceof MethodInvocation)
		args = ((MethodInvocation) expr).arguments();

	if (isHardCodedMethod(fMethodName, fRefObj)) {
		processHardCodedProperty(fMethodName, fRefObj);
	}
	else {
		Iterator itr = getPropertiesIterator(fRefObj);
		// Find a write method that matches the one in the Expression.
		while (itr.hasNext()) {
			try {
				PropertyDecorator pd = (PropertyDecorator) itr.next();

				if (pd.getField() == null){
					Method m = pd.getWriteMethod();
					if (m == null
							|| !m.getName().equals(fMethodName)
							|| args == null
							|| (m.listParametersWithoutReturn().length != args.size()))
						continue;

					//TODO: Need to check argument types
					fSF = (EStructuralFeature) pd.getEModelElement();
					fSFname = fSF.getName();
					fPD = pd;
					break;
				} else {
					if (pd.getField().getName().equals(fMethodName)){
						fSF = (EStructuralFeature) pd.getEModelElement();
						fSFname = fSF.getName();
						fPD = pd;
						fisMethod = false;						
						break;						
					}
				}
			} catch (Exception e) {
				if (JavaVEPlugin.isLoggingLevel(Level.WARNING))
					JavaVEPlugin.log("AttributeFeatureMapper.getFeature() : " + e, Level.WARNING); //$NON-NLS-1$
				continue;
			}

		}
	}

	if (fSF != null) {
		fSFname = fSF.getName();
	}
	return fSF;	
}


public static void clearCache() {
	fdecoratorRSCache = null ;
	fdecoratorCache.clear();
}

}

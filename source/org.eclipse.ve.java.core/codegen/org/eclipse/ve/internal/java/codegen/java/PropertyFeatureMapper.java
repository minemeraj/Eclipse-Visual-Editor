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
 *  $RCSfile: PropertyFeatureMapper.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */
import java.util.*;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.internal.compiler.ast.*;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.beaninfo.adapters.Utilities;
import org.eclipse.jem.internal.core.MsgLogger;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

import org.eclipse.jem.internal.java.JavaClass;
import org.eclipse.jem.internal.java.Method;
import org.eclipse.jem.internal.java.beaninfo.IIntrospectionAdapter;

public class PropertyFeatureMapper extends AbstractFeatureMapper {


static HashMap     fdecoratorCache = new HashMap() ;   
static public ResourceSet fdecoratorRSCache = null ;


private List primGetDecorators(EObject metaObject) {
    
     JavaVEPlugin.log("AttributeFeatureMapper.primGetDecorators: "+((EClass) metaObject).getName(),org.eclipse.jem.internal.core.MsgLogger.LOG_FINEST) ; //$NON-NLS-1$
     EList properties = ((JavaClass)metaObject).getAllProperties() ;
	 fdecoratorCache.put(metaObject,properties) ;
     Iterator itr = Utilities.getPropertiesIterator(properties);
     ArrayList list = new ArrayList() ;
     while (itr.hasNext()) {
         list.add(itr.next()) ;
     }
     return list ;    
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
        JavaVEPlugin.log("AttributeFeatureMapper.getPropertiesIterator():  *ClearingCache*",org.eclipse.jem.internal.core.MsgLogger.LOG_FINE) ; //$NON-NLS-1$
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
protected boolean isHardCodedMethod (String method) {
	return false ;
}

/**
 * Enable an extender to hard code a feature
 */
protected void processHardCodedProperty(String method, Object bean) {
}

public EStructuralFeature getFeature (Statement expr) {
	
	if (fSF != null)
		return fSF;
	if (fRefObj == null || expr == null)
		return null;

	getMethodName(expr);

	Expression args[] = null;
	if (expr instanceof MessageSend)
		args = ((MessageSend) expr).arguments;

	if (isHardCodedMethod(fMethodName)) {
		processHardCodedProperty(fMethodName, fRefObj);
	}
	else {
		Iterator itr = getPropertiesIterator((EObject) fRefObj);
		// Find a write method that matches the one in the Expression.
		while (itr.hasNext()) {
			try {
				PropertyDecorator pd = (PropertyDecorator) itr.next();

				Method m = pd.getWriteMethod();
				if (m == null
					|| !m.getName().equals(fMethodName)
					|| args == null
					|| (m.listParametersWithoutReturn().length != args.length))
					continue;

				fSF = (EStructuralFeature) pd.getEModelElement();
				fSFname = fSF.getName();
				fPD = pd;
				break;
			} catch (Exception e) {
				JavaVEPlugin.log("AttributeFeatureMapper.getFeature() : " + e, MsgLogger.LOG_WARNING); //$NON-NLS-1$
				continue;
			}

		}
		if (fSF == null && fMethodName != null) {
			// Try to see if our write method is actually a public field access
			EList properties = ((JavaClass) fRefObj.getJavaType()).getAllProperties();
			for (itr = properties.iterator(); itr.hasNext();) {
				EStructuralFeature sf = (EStructuralFeature) itr.next();
				if (fMethodName.equals(sf.getName()) && isFieldAccess(sf)) {
					fSF = sf;
					fSFname = sf.getName();
					fisMethod = false;
					break;
				}
			}
		}
	}

	if (fSF != null) {
		fSFname = ((EStructuralFeature) fSF).getName();
	}
	return fSF;	
}


public static void clearCache() {
	fdecoratorRSCache = null ;
	fdecoratorCache.clear();
}

}
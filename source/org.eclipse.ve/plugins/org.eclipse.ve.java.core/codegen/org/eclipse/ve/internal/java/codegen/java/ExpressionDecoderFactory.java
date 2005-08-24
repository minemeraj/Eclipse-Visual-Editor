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
 *  $RCSfile: ExpressionDecoderFactory.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:30:45 $ 
 */


import java.util.HashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.emf.ClassDecoratorFeatureAccess;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

public class ExpressionDecoderFactory {
			

//  This is the decorator that holds which decoder to use
public static final URI    URIcodeGenHelperClass = URI.createURI("platform:/plugin/org.eclipse.ve.java.core/overrides/codegenHelpers.ecore#CodeGenHelperClass"); //$NON-NLS-1$
//  This is the .xmi source decoration key
public final static String SOURCE_DECORATOR_KEY 			= "codegen.CodeGenHelperClass" ; //$NON-NLS-1$
//  This is the Edit domain's key for this factory
public final static String CodeGenDecoderFactory_KEY 	= "org.eclipse.ve.internal.java.codegen.java.ExpressionDecoderFactory" ; //$NON-NLS-1$

// This is a default Attribute Decoder
public  Class defaultExpDecoder = 
        					org.eclipse.ve.internal.java.codegen.java.ObjectDecoder.class ; //$NON-NLS-1$

// This is a default Attribute Decoder
public static final Class defaultEventDecoder = 
        					org.eclipse.ve.internal.java.codegen.java.ObjectEventDecoder.class ; //$NON-NLS-1$

private HashMap fexpCache 	= new HashMap();
private HashMap fPropCache = new HashMap(); // Keep track of properties of meta
private EStructuralFeature fexpDecoderSF = null;
private HashMap feventCache = new HashMap();
private EStructuralFeature feventDecoderSF = null;
private ResourceSet fRS = null;


/**
 * CodeGen plugin adds annotations to the model denoting what decoders to use.
 * The "decoder" property of the anotation holds a string denoting the decoder's class
 */
protected EStructuralFeature getExpDecoderFeature() {	
	if (fexpDecoderSF != null) return fexpDecoderSF ;
		
	EClass cgHelperClass = (EClass) fRS.getEObject(URIcodeGenHelperClass, true) ; 
   	fexpDecoderSF = cgHelperClass.getEStructuralFeature("expDecoder") ; //$NON-NLS-1$
   	
   	return fexpDecoderSF ;
}
/**
 * CodeGen plugin adds annotations to the model denoting what decoders to use.
 * The "decoder" property of the anotation holds a string denoting the decoder's class
 */
protected EStructuralFeature getEventDecoderFeature() {	
	if (feventDecoderSF != null) return feventDecoderSF ;
		
	EClass cgHelperClass = (EClass) fRS.getEObject(URIcodeGenHelperClass, true) ; 
   	feventDecoderSF = cgHelperClass.getEStructuralFeature("eventDecoder") ; //$NON-NLS-1$
   	
   	return feventDecoderSF ;
}
/**
 *  Resolve the Exp Decoder
 */ 	
public IExpressionDecoder getExpDecoder(IJavaInstance obj) {

    
	IExpressionDecoder result = null;
	if (obj instanceof IJavaObjectInstance) {
		IJavaObjectInstance o = (IJavaObjectInstance) obj;
		EClassifier eClass = o.eClass();

        reSetCacheIfNeeded((JavaClass)eClass) ;
        
		// Avoid searching for decorator if possible
		Class decoderClass = (Class) fexpCache.get(eClass);
		if (decoderClass == null) {
			EAnnotation decr = ClassDecoratorFeatureAccess.getDecoratorWithFeature(eClass, SOURCE_DECORATOR_KEY, getExpDecoderFeature());
			String decoderName = (String) decr.eGet(getExpDecoderFeature());
			try {
				Class decoderClazz = CDEPlugin.getClassFromString(decoderName);
				if (decoderClazz != null) {
					Object dObj = decoderClazz.newInstance();
					if (dObj instanceof IExpressionDecoder) {
						result = (IExpressionDecoder) dObj;
						fexpCache.put(eClass,decoderClazz) ;
					}
				}

			}
			catch (Exception e) {
				org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
			}
		}
		else
		   try {
			result = (IExpressionDecoder) decoderClass.newInstance() ;
		   }
		   catch (Exception e) {
		   	  org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
		   }
	}
	if (result == null) {
		try {
			result = (IExpressionDecoder) defaultExpDecoder.newInstance();
		}
		catch (Exception e) {}
	}
	return result;

}	  


protected void	reSetCacheIfNeeded (JavaClass meta) {
	EList props = meta.getAllProperties() ;
	EList cProps = (EList) fPropCache.get(meta) ;
	if (cProps == null)
	   fPropCache.put(meta,props) ;
	else
	   if (cProps!=props) {
	   	// Properties have change, possibly because of hierarcy changes (class rename etc)
	   	feventCache.clear();
	   	fexpCache.clear();
	   	fPropCache.clear();
	   	fPropCache.put(meta, props) ;
	   }
}

/**
 *  Resolve the Exp Decoder
 */ 	
public IEventDecoder getEventDecoder(IJavaInstance obj) {

    
	IEventDecoder result = null;
	if (obj instanceof IJavaObjectInstance) {
		IJavaObjectInstance o = (IJavaObjectInstance) obj;
		EClassifier eClass = o.eClass();
		
		reSetCacheIfNeeded((JavaClass)eClass) ;

		// Avoid searching for decorator if possible
		Class decoderClass = (Class) feventCache.get(eClass);
		if (decoderClass == null) {
			EAnnotation decr = ClassDecoratorFeatureAccess.getDecoratorWithFeature(eClass, SOURCE_DECORATOR_KEY, getEventDecoderFeature());
			String decoderName = (String) decr.eGet(getEventDecoderFeature());
			try {
				Class decoderClazz = CDEPlugin.getClassFromString(decoderName);
				if (decoderClazz != null) {
					Object dObj = decoderClazz.newInstance();
					if (dObj instanceof IEventDecoder) {
						result = (IEventDecoder) dObj;
						feventCache.put(eClass,decoderClazz) ;
					}
				}

			}
			catch (Exception e) {
				org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
			}
		}
		else
		   try {
			result = (IEventDecoder) decoderClass.newInstance() ;
		   }
		   catch (Exception e) {
		   	  org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
		   }
	}
	if (result == null) {
		try {
			result = (IEventDecoder	) defaultEventDecoder.newInstance();
		}
		catch (Exception e) {}
	}
	return result;

}

public IExpressionDecoder getDefaultExpDecoder() {
	IExpressionDecoder d = null ;
	try {
		d =  (IExpressionDecoder) defaultExpDecoder.newInstance();
	}
	catch (Exception e) {}
	return d ;
}

public void setDefaultExpDecoder(Class d) {
	defaultExpDecoder = d ;
}
	  
public void	setResourceSet (ResourceSet rs) {
	if (fRS != rs) {
		fRS=rs ;
		// New resource set. Need to clear caches that refer to old set.
		feventCache.clear();
		fexpCache.clear();
		fPropCache.clear();
		fexpDecoderSF = feventDecoderSF = null;
	}	
}

}

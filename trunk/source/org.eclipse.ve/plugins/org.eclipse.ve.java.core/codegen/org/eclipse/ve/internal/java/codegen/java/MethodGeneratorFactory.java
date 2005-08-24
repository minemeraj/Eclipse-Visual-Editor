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
 *  $RCSfile: MethodGeneratorFactory.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:30:45 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.emf.ClassDecoratorFeatureAccess;

import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.util.*;



/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public class MethodGeneratorFactory {


	
		

//  This is the decorator that holds which method generator to use
		public static final URI    URIcodeGenHelperClass = URI.createURI("platform:/plugin/org.eclipse.ve.java.core/overrides/codegenHelpers.ecore#CodeGenHelperClass"); //$NON-NLS-1$
//  This is the .xmi source decoration key
		public final static String SOURCE_DECORATOR_KEY 			= "codegen.CodeGenHelperClass" ; //$NON-NLS-1$
//  This is the Edit domain's key for this factory
		public final static String CodeGenMethodGeneratorFactory_KEY 	= "org.eclipse.ve.internal.java.codegen.java.MethodGeneratorFactory" ; //$NON-NLS-1$

// This is a default Method Decoder
		public  Class defaultMethodGenerator = 
		org.eclipse.ve.internal.java.codegen.java.MethodTextGenerator.class ; //$NON-NLS-1$

		private HashMap fGeneratorsCache 	= new HashMap();
		private HashMap fPropCache = new HashMap(); // Keep track of properties of meta
		private EStructuralFeature fexpDecoderSF = null; // the Method Generator feature of the decorator
		private ResourceSet fRS = null;


		/**
		 * CodeGen plugin adds annotations to the model denoting what decoders to use.
		 * The "decoder" property of the anotation holds a string denoting the decoder's class
		 */
		protected EStructuralFeature getMethodGeneratorFeature() {	
			if (fexpDecoderSF != null) return fexpDecoderSF ;
			
			EClass cgHelperClass = (EClass) fRS.getEObject(URIcodeGenHelperClass, true) ; 
			fexpDecoderSF = cgHelperClass.getEStructuralFeature("methodGenerator") ; //$NON-NLS-1$
			
			return fexpDecoderSF ;
		}
		
		Object getInstance(Class c, EObject obj, IBeanDeclModel model) {
			try {
				Constructor cons = c.getConstructor(new Class[] { EObject.class, IBeanDeclModel.class }) ;
				return cons.newInstance(new Object[] { obj, model } ) ;
			} catch (Exception e) {}
			
			return null ;
		}
		
		/**
		 *  Resolve the Exp Decoder
		 */ 	
		public IMethodTextGenerator getMethodGenerator(IJavaInstance obj, IBeanDeclModel model) {

			
			IMethodTextGenerator result = null;
			if (obj instanceof IJavaObjectInstance) {
				IJavaObjectInstance o = (IJavaObjectInstance) obj;
				EClassifier eClass = o.eClass();

				reSetCacheIfNeeded((JavaClass)eClass) ;
				
				// Avoid searching for decorator if possible
				Class generatorClass = (Class) fGeneratorsCache.get(eClass);
				if (generatorClass == null) {
					EAnnotation decr = ClassDecoratorFeatureAccess.getDecoratorWithFeature(eClass, SOURCE_DECORATOR_KEY, getMethodGeneratorFeature());
					String generatorName = (String) decr.eGet(getMethodGeneratorFeature());
					try {
						Class genClass = CDEPlugin.getClassFromString(generatorName);
						if (genClass != null) {
							Object dObj = getInstance(genClass, obj, model) ;
							if (dObj != null && dObj instanceof IMethodTextGenerator) {
								result = (IMethodTextGenerator) dObj;
								fGeneratorsCache.put(eClass,genClass) ;
							}
						}

					}
					catch (Exception e) {
						org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
					}
				}
				else
					try {
                      Object ins = getInstance(generatorClass, obj, model) ;
                      if (ins != null && ins instanceof IMethodTextGenerator)
					       result = (IMethodTextGenerator) ins ;
				}
				catch (Exception e) {
					org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
				}
			}
			if (result == null) {
				try {
					result = (IMethodTextGenerator) defaultMethodGenerator.newInstance();
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
					fGeneratorsCache.clear();
					fPropCache.clear();
					fPropCache.put(meta, props) ;
				}
		}

		public IMethodTextGenerator getDefaultMethodGenerator() {
			IMethodTextGenerator mg = null ;
			try {
				mg =  (IMethodTextGenerator) defaultMethodGenerator.newInstance();
			}
			catch (Exception e) {}
			return mg ;
		}

		public void setDefaultExpDecoder(Class d) {
			defaultMethodGenerator = d ;
		}
		
		public void	setResourceSet (ResourceSet rs) {
			if (fRS != rs) {
				fRS=rs ;
				// New resource set. Need to clear caches that refer to old set.
	
				fGeneratorsCache.clear();
				fPropCache.clear();
			}	
		}



}

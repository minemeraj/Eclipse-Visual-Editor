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
package org.eclipse.ve.internal.cde.emf;
/*
 *  $RCSfile: ClassDecoratorFeatureAccess.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:48 $ 
 */

import org.eclipse.emf.ecore.*;

import org.eclipse.ve.internal.cdm.KeyedValueHolder;
/**
 * This class is a utility class to access decorators on the class
 * hierarchy.
 */

public class ClassDecoratorFeatureAccess {
	private ClassDecoratorFeatureAccess() {
		// Never instantiated.
	}

	/**
	 * This will return the decorator that has the requested feature set in it.
	 * If the feature is single, then isSet is used. If the feature is many, then
	 * if the list is not empty, then it is considered to be set.
	 * It will return null if no decorator with that feature set was found.
	 */
	public static EAnnotation getDecoratorWithFeature(EClassifier aClass, Class decoratorType, EStructuralFeature feature) {
		if (aClass == null)
			return null;

		ClassDecoratorTypeIterator classItr = new ClassDecoratorTypeIterator(aClass, decoratorType);

		// Now for each decorator, determine if the feature is set, return it if it is set.
		while (classItr.hasNext()) {
			EAnnotation dec = (EAnnotation) classItr.next();
			try {
				if (dec.eIsSet(feature))
					return dec;
			} catch (IllegalArgumentException e) {
				// This isn't a valid feature for this decorator, try next one.
			}
		}

		// None with a set feature was found.
		return null;
	}

	/**
	 * This will return the decorator (with source string matching) that has the requested feature set in it.
	 * If the feature is single, then isSet is used. If the feature is many, then
	 * if the list is not empty, then it is considered to be set.
	 * It will return null if no decorator with that feature set was found.
	 */
	public static EAnnotation getDecoratorWithFeature(EClassifier aClass, String decoratorSource, EStructuralFeature feature) {
		if (aClass == null)
			return null;

		ClassDecoratorSourceIterator classItr = new ClassDecoratorSourceIterator(aClass, decoratorSource);

		// Now for each decorator, determine if the feature is set, return it if it is set.
		while (classItr.hasNext()) {
			EAnnotation dec = (EAnnotation) classItr.next();
			try {
				if (dec.eIsSet(feature))
					return dec;
			} catch (IllegalArgumentException e) {
				// This isn't a valid feature for this decorator, try next one.
			}
		}

		// None with a set feature was found.
		return null;
	}	

	/**
	 * This will return the decorator that has the requested KeyedValue key set in it.
	 * It will return null if no decorator with that feature set was found.
	 */
	public static EAnnotation getDecoratorWithKeyedFeature(EClassifier aClass, Class decoratorType, String key) {
		if (aClass == null)
			return null;
		ClassDecoratorTypeIterator classItr = new ClassDecoratorTypeIterator(aClass, decoratorType);

		// Now for each decorator, determine if the feature is set, return it if it is set.
		while (classItr.hasNext()) {
			EAnnotation dec = (EAnnotation) classItr.next();
			if (dec instanceof KeyedValueHolder)
				if (((KeyedValueHolder) dec).getKeyedValues().containsKey(key))
					return dec;
		}

		// None with a set keyedValue was found.
		return null;
	}

}

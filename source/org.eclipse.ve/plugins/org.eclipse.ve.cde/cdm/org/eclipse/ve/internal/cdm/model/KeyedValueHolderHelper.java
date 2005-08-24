/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cdm.model;

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEMap;

import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.KeyedValueHolder;

/*
 *  $RCSfile: KeyedValueHolderHelper.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:50 $ 
 */
 
/**
 * This class is used to help out for KeyedValueHolder implementations that
 * don't inherit from KeyedValueHolderImpl or some other implementation of
 * KeyedValueHolder, i.e. the first KeyedValueHolder implementation in a
 * hierarchy. It is needed because there are some complicated things that need to be done,
 * and don't want to have to reimplement it in each implementer. 
 *
 */
public class KeyedValueHolderHelper {
	
	/*
	 * This all static, no instance is used.
	 */
	private KeyedValueHolderHelper() {
	}
	
	/**
	 * This should be used by the getKeyedValues() method. The method implementation should be:
	 * <code>
	 *	public EMap getKeyedValues() {
	 *		if (keyedValues == null) {
	 *			keyedValues = KeyedValueHolderHelper.createKeyedValuesEMap(this, DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__KEYED_VALUES);
	 *		}
	 *		return keyedValues;
	 *	} 
	 * </code>
	 * This example comes from CDE DecoratorsPackage, CleassDescriptorDecoratorImpl. 
	 * It is important that the generated tag is taken off of the comment before the getKeyedValues method so that
	 * EMF code generation won't wipe out your explicit changes.  
	 * @param holder The KeyedValuesHolder implementation instance
	 * @param keyedValuesFeatureID The feature id of the "keyedValues" feature from the holder implementation eClass.
	 * @return
	 */
	public static EMap createKeyedValuesEMap(KeyedValueHolder holder, int keyedValuesFeatureID) {
		return new EcoreEMap(CDMPackage.eINSTANCE.getMapEntry(), BasicEMap.Entry.class, (InternalEObject) holder, keyedValuesFeatureID);
	}
	
	/**
	 * The object returned from eObjectForURIFragmentSegment method if the fragment is not for a 
	 * keyed values. If this is returned (use identity test (==)) then continue the eObjectForURIFragmentSegment with
	 * the super class.   
	 */
	public static final EObject NOT_KEYED_VALUES_FRAGMENT = EcoreFactory.eINSTANCE.createEObject();
	
	/**
	 * A path element for looking for a particular keyed value using a string key, use a path fragment of the format:
	 * 	<code>*keyvalue</code>
	 * The asteric indicates search by keyedValues. "keyvalue" is whatever the string key that is being searched for. 
	 * 
	 * This should be used by the eObjectForURIFragmentSegment(String)  method. The method implementation should be:
	 * <code> 
	 *	public EObject eObjectForURIFragmentSegment(String uriFragmentSegment) {
	 *		EObject eo = KeyedValueHolderHelper.eObjectForURIFragmentSegment(this, uriFragmentSegment);
	 *		return eo == KeyedValueHolderHelper.NOT_KEYED_VALUES_FRAGMENT ? super.eObjectForURIFragmentSegment(uriFragmentSegment) : eo;
	 *	} 
	 * </code>
	 * This example comes from CDE DecoratorsPackage, CleassDescriptorDecoratorImpl. 
	 * @param holder The KeyedValuesHolder implementation instance
	 * @param uriFragmentSegment The fragment to search for.
	 * @return	<code>null</code> - Key not found, return null. 
	 *	 		<code>NOT_KEYED_VALUES_FRAGMENT</code> - Not searching for keyed value, continue with super.
	 *			anything else - The found value for the path. 
	 */
	public static EObject eObjectForURIFragmentSegment(KeyedValueHolder holder, String uriFragmentSegment) {
		if (uriFragmentSegment.length() <= 1 || uriFragmentSegment.charAt(0) != '*')
			return NOT_KEYED_VALUES_FRAGMENT;	// Doesn't start with "*", so not a keyed value search.
		else {
			int entryNdx = holder.getKeyedValues().indexOfKey(uriFragmentSegment.substring(1));	// Path only works with string keys.  	
		  return entryNdx != -1 ? (EObject) holder.getKeyedValues().get(entryNdx) : null;
		}
	}

}

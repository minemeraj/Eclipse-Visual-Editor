/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: SweetHelper.java,v $
 *  $Revision: 1.1 $  $Date: 2004-03-08 14:50:01 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;
import java.util.*;
 
/**
 * Helper class to combine style bits into a BeanDescriptor based on a set of rules
 * @since 1.0.0
 */
public class SweetHelper {
	
	public static final String SWEET_STYLEBITS = "SWEET_STYLEBITS";
/**
 * Combine all of the style bits from the descriptor's classes superclass into the descriptor
 * This is used in most cases of Control
 */	
public static void mergeAllSuperclassStyleBits(BeanDescriptor descriptor){
	
	Map styleBitsMap = getStyleBitsMap(descriptor);
	// Get the style bits from the superclass
	Class superclass = descriptor.getBeanClass().getSuperclass();
	while(superclass != java.lang.Object.class){
		try {
			BeanInfo superclassBeanInfo = Introspector.getBeanInfo(superclass,superclass.getSuperclass());
			mergeStyleBits(styleBitsMap,superclassBeanInfo);
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		superclass = superclass.getSuperclass();		
	}	
	setStyleBits(descriptor,styleBitsMap);
	
}

private static void setStyleBits(BeanDescriptor descriptor, Map styleBitsMap) {
	// Turn the style bits back into an array
	Object[][] newStyleBits = new Object[styleBitsMap.size()][2];
	Iterator iter = styleBitsMap.keySet().iterator();
	int index = 0;
	while (iter.hasNext()) {
		Object styleName = iter.next();
		newStyleBits[index][0] = styleName;
		newStyleBits[index][1] = styleBitsMap.get(styleName);
		index++;
	}
	descriptor.setValue(SWEET_STYLEBITS,newStyleBits);	
}


private static void mergeStyleBits(Map styleBitsMap, BeanInfo superclassBeanInfo) {
	Object[][] superclassStyleBits = (Object[][])superclassBeanInfo.getBeanDescriptor().getValue(SWEET_STYLEBITS);
	if(superclassStyleBits == null) {
		return;
	}	
	for (int i = 0; i < superclassStyleBits.length; i++) {
		String canonnicalName = (String)superclassStyleBits[i][0];
		Object[] styleBits = (Object[])superclassStyleBits[i][1];
		// If the cannonical name doesn't exist then add this to the map
		if(styleBitsMap.get(canonnicalName ) == null){
			styleBitsMap.put(canonnicalName,styleBits);
		} else {
			// Merge into the existing set of style bits
			Object[] existingBits = (Object[])styleBitsMap.get(canonnicalName);
			Object[] newBits = new Object[existingBits.length + styleBits.length];
			System.arraycopy(existingBits,0,newBits,0,existingBits.length);
			System.arraycopy(styleBits,0,newBits,existingBits.length + 1,styleBits.length);
			styleBitsMap.put(canonnicalName,newBits);
		}
	}
}

private static Map getStyleBitsMap(BeanDescriptor descriptor) {
	Object[] [] currentStyleBits = (Object[][])descriptor.getValue(SWEET_STYLEBITS);
	Map styleBitsMap = new HashMap(currentStyleBits.length + 5);
	for (int i = 0; i < currentStyleBits.length; i++) {
		styleBitsMap.put(currentStyleBits[i][0],currentStyleBits[i][1]);
	}
	return styleBitsMap;
}
/**
 * Combine all of the style bits from the names classes into the descriptor
 * This is used for scenarios such as Table which inherits from Composite but does not want to inherit its style bits 
 */	
public static void mergeNamedSuperclassStyleBits(BeanDescriptor descriptor, Class[] namedClasses){
	
	Map styleBitsMap = getStyleBitsMap(descriptor);
	for (int i = 0; i < namedClasses.length; i++) {
		try {
			BeanInfo namedBeanInfo = Introspector.getBeanInfo(namedClasses[i],namedClasses[i].getSuperclass());
			mergeStyleBits(styleBitsMap,namedBeanInfo);
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}
	setStyleBits(descriptor,styleBitsMap);	
}

public static void mergeNamedSuperclassStyleBits(BeanDescriptor descriptor, Class namedClass, String[] styleBits){
	
}
	
public static void main(String[] args) {
	
	// Test
	Introspector.setBeanInfoSearchPath(new String[] {"org.eclipse.swt.widgets.beaninfo"});
	mergeAllSuperclassStyleBits(new ButtonBeanInfo().getBeanDescriptor());
	
}

}

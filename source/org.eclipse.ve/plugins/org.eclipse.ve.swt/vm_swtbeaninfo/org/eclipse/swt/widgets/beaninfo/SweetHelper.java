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
 *  $Revision: 1.2 $  $Date: 2004-03-09 00:07:48 $ 
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
public static void mergeSuperclassStyleBits(BeanDescriptor descriptor){
	
	Map styleBitsMap = getStyleBitsMap(descriptor);
	// Get the style bits from the superclass
	Class superclass = descriptor.getBeanClass().getSuperclass();
	try {
		BeanInfo superclassBeanInfo = Introspector.getBeanInfo(superclass,superclass.getSuperclass());
		while(superclassBeanInfo.getBeanDescriptor().getValue(SWEET_STYLEBITS) == null){
			superclassBeanInfo = Introspector.getBeanInfo(superclass.getSuperclass(),superclass.getSuperclass().getSuperclass()); 			 
		}
		mergeStyleBits(styleBitsMap,superclassBeanInfo);
	} catch (IntrospectionException e) {
		e.printStackTrace();
	}	
	setStyleBits(descriptor,styleBitsMap);
	
}

private static void setStyleBits(BeanDescriptor descriptor, Map styleBitsMap) {
	// Turn the style bits back into an array
	Object[][] newStyleBits = new Object[styleBitsMap.size()][4];
	Iterator iter = styleBitsMap.keySet().iterator();
	int index = 0;
	while (iter.hasNext()) {
		Object styleName = iter.next();
		newStyleBits[index][0] = styleName;
		Object[] nameInitStringValues = (Object[])styleBitsMap.get(styleName);
 		System.arraycopy(nameInitStringValues,0,newStyleBits[index],1,3);
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
		String displayName = (String)superclassStyleBits[i][1];
		Boolean expert = (Boolean) superclassStyleBits[i][2];
		Object[] values = (Object[]) superclassStyleBits[i][3];
		// If the cannonical name doesn't exist then add this to the map
		if(styleBitsMap.get(canonnicalName ) == null){
			styleBitsMap.put(canonnicalName,new Object[] {displayName,expert,values});
		} else {
			// Merge into the existing set of style bits
			// TODO
		}
	}
}

private static Map getStyleBitsMap(BeanDescriptor descriptor) {
	Object[] [] currentStyleBits = (Object[][])descriptor.getValue(SWEET_STYLEBITS);
	Map styleBitsMap = new HashMap(currentStyleBits.length + 5);
	for (int i = 0; i < currentStyleBits.length; i++) {
		styleBitsMap.put(currentStyleBits[i][0], new Object[] { currentStyleBits[i][1] , currentStyleBits[i][2] , currentStyleBits [i][3]});
	}
	return styleBitsMap;
}
/**
 * Combine all of the style bits from the names classes into the descriptor
 * This is used for scenarios such as Table which inherits from Composite but does not want to inherit its style bits 
 */	
public static void mergeStyleBits(BeanDescriptor descriptor, Class[] namedClasses){
	
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

public static void mergeNamedStyleBits(BeanDescriptor descriptor, Class namedClass, String[] styleBits){
	
}
	
public static void main(String[] args) {
	
	// Test
	Introspector.setBeanInfoSearchPath(new String[] {"org.eclipse.swt.widgets.beaninfo"});
	mergeSuperclassStyleBits(new TableBeanInfo().getBeanDescriptor());
	
}

}

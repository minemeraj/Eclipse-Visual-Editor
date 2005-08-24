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
package org.eclipse.ve.examples.java.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import org.eclipse.ve.examples.java.vm.TestBean;
public class TestBeanBeanInfo extends SimpleBeanInfo {
	
public BeanDescriptor getBeanDescriptor(){
	  BeanDescriptor result = new BeanDescriptor(TestBean.class);
	  result.setValue("ICON_COLOR_16x16_URL",getClass().getResource("test.gif").toExternalForm());
	  return result;
}
	
public PropertyDescriptor[] getPropertyDescriptors(){

	String[] propertyNameArray = {
		"trueOrFalse",
		"color",
		"colorPropEditor",
		"string",
		"enumeration",
		"hidden",
		"internalName",
		"rect",
		"integer",
		"integerClass",
		"boolean",
		"booleanClass",
		"bigDecimal",
		"bigInteger",
		"byte",
		"byteClass",
		"double",
		"doubleClass",
		"float",
		"floatClass",
		"longClass",
		"long",
		"short",
		"shortClass",
		"even",
		"day",
		"month"
	};
				
	PropertyDescriptor[] propertyArray = new PropertyDescriptor[propertyNameArray.length];
	try{ 
		for (int index = 0; index < propertyNameArray.length; index++) {
			String name = propertyNameArray[index];
			propertyArray[index] = new PropertyDescriptor(name, TestBean.class);

			if (name.equals("even")) {
				propertyArray[index].setPropertyEditorClass(OnlyEvenIntegerPropertyEditor.class);
			};
			if (name.equals("day")) {
				propertyArray[index].setPropertyEditorClass(DayPropertyEditor.class);
			};			
			if (name.equals("month")) {
				propertyArray[index].setPropertyEditorClass(MonthPropertyEditor.class);
			};						
			if (name.equals("enumeration")){
				propertyArray[index].setValue(
					"enumerationValues",
					new Object[] {
		      			"Zero", new Integer(0), "0",
		      			"One", new Integer(1), "1",								
		      			"Two", new Integer(2), "2",
		      			"Three", new Integer(3), "3",			
		      			"Four", new Integer(4), "4",			
		      			"Five", new Integer(5), "5",			
		      			"Six", new Integer(6), "6",			
		      			"Seven", new Integer(7), "7",			
		      			"Eight", new Integer(8), "8",			
		      			"Nine", new Integer(9), "9"
					});
			};
			if (name.equals("hidden"))
				propertyArray[index].setHidden(true);
		};
	} catch ( Exception exc ){
		System.out.println("TestBeanBeanInfo failed to create property descriptors");
		exc.printStackTrace();
	}
	return propertyArray;	
}	
}

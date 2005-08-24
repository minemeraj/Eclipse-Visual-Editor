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

import java.beans.*;

import org.eclipse.ve.examples.java.vm.Area;
/**
 * Insert the type's description here.
 * Creation date: (07/11/00 10:16:39 AM)
 * @author: Joe Winchester
 */
public class AreaBeanInfo extends SimpleBeanInfo {
/**
 * Get the bean descriptor with the customizer 
 */
public BeanDescriptor getBeanDescriptor(){
	  BeanDescriptor result = new BeanDescriptor(Area.class,AreaCustomizer.class);
	  result.setValue("EXPLICIT_PROPERTY_CHANGE",Boolean.TRUE);
	  return result;
}
/**
 * Return the property descriptors for this bean.
 * There are two special ones, Shape which is typed as int but has a combo box
 * editor that returns the valid shapes and evenNumber which is an int but has a property editor
 * that is Text but throws IllegalArgumentExceptions if the number if odd
 */
public PropertyDescriptor[] getPropertyDescriptors() 
{
	int index = 0;
	
	try {
		String[] propertyNameArray = {
			"fillColor",
			"borderWidth",
			"continent",
			"evenNumber",
			"month",
			"day",
			"shape",
			"font",
			"object"
		};
				
		PropertyDescriptor[] propertyArray = new PropertyDescriptor[propertyNameArray.length];

		for (index = 0; index < propertyNameArray.length; index++) {
			String name = propertyNameArray[index];
			propertyArray[index] = new PropertyDescriptor(name, Area.class);

			if (name.equals("shape")) {
				propertyArray[index].setPropertyEditorClass(ShapeTagsPropertyEditor.class);
			} else if (name.equals("evenNumber")) {
				propertyArray[index].setPropertyEditorClass(OnlyEvenIntegerPropertyEditor.class);
			} else if (name.equals("day")) {
				propertyArray[index].setPropertyEditorClass(DayPropertyEditor.class);
			} else if (name.equals("font")) {
				propertyArray[index].setPropertyEditorClass(sun.beans.editors.FontEditor.class);
			} else if (name.equals("fillColor")) {
				propertyArray[index].setPropertyEditorClass(sun.beans.editors.ColorEditor.class);
			}
		}	
		// Shape has another property for the same getShape and setShape
		// that has a special editor that is written in AWT
//		PropertyDescriptor shapeCustomDescriptor = new PropertyDescriptor(
//			"shapeCustom",
//			Area.class.getMethod("getShape",null),
//			Area.class.getMethod("setShape",new Class[] { Integer.TYPE })	
//		);
//		shapeCustomDescriptor.setPropertyEditorClass(ShapeCustomPropertyEditor.class);
//		propertyArray[8] = shapeCustomDescriptor;		
		// Shape has another property for the same getShape and setShape
//		// that has a special editor that is written in Swing
//		PropertyDescriptor shapeJCustomDescriptor = new PropertyDescriptor(
//			"shapeJCustom",
//			Area.class.getMethod("getShape",null),
//			Area.class.getMethod("setShape",new Class[] { Integer.TYPE })	
//		);
//		shapeJCustomDescriptor.setPropertyEditorClass(ShapeJCustomPropertyEditor.class);
//		propertyArray[9] = shapeJCustomDescriptor;			
		return propertyArray;
			
	} catch (Exception ie) {
		System.out.println("getPropertyDescriptors IntrospectionException");
		System.out.println(ie.getMessage());
		ie.printStackTrace();
		return null;
	}         
}
}

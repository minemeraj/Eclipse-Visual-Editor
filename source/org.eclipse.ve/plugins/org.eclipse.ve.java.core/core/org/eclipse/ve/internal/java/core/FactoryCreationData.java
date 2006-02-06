/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: FactoryCreationData.java,v $
 *  $Revision: 1.1 $  $Date: 2006-02-06 17:14:38 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.*;

import org.eclipse.jem.beaninfo.common.IBaseBeanInfoConstants;
import org.eclipse.jem.internal.beaninfo.BeanDecorator;
import org.eclipse.jem.internal.beaninfo.common.FeatureAttributeValue;
import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.PTMethodInvocation;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaRefFactory;
 

/**
 * Factory Creation data. This will be the internal data of a {@link FeatureAttributeValue#getInternalValue()} that has
 * the key of {@link IBaseBeanInfoConstants#FACTORY_CREATION}. It will be on the {@link BeanDecorator} for the factory type.
 * <p>
 * @since 1.2.0
 */
public class FactoryCreationData {
	protected String initString;
	protected boolean shared;
	protected boolean onFreeform;
	protected Map methodToData;	// Map of the factory method name to factory method data.
	
	protected FactoryCreationData(Map methodToData, String initString, boolean shared, boolean onFreeform) {
		this.methodToData = methodToData;
		this.initString = initString;
		this.shared = shared;
		this.onFreeform = onFreeform;
	}
	
	/**
	 * The method data for a method.
	 * 
	 * @since 1.2.0
	 */
	public static class MethodData {
		/**
		 * The name of the class that is the created type returned from the method.
		 */
		private String createdClassname;
		private boolean isStatic;
		private String[][] propertyNames;
		
		/**
		 * Create it.
		 * @param createdClassname
		 * @param propertyNames
		 * 
		 * @since 1.2.0
		 */
		protected MethodData(String createdClassname, boolean isStatic, String[][] propertyNames) {
			this.createdClassname = createdClassname;
			this.isStatic = isStatic;
			this.propertyNames = propertyNames;
		}
		
		/**
		 * Get the created classname.
		 * @return
		 * 
		 * @since 1.2.0
		 */
		public String getCreatedClassname() {
			return createdClassname;
		}
		
		/**
		 * Return the property name for the given arg index, for the
		 * factory with the given total number of args. 
		 * @param argNum argument index number to get the property name for. It must be less than totalNumArgs
		 * @param totalNumArgs total number of arguments for the given creation method. This is used to select the property names list.
		 * @return the property name of <code>null</code> if the given index is not a property.
		 * 
		 * @since 1.2.0
		 */
		public String getPropertyName(int argNum, int totalNumArgs) {
			for (int i = 0; i < propertyNames.length; i++) {
				String[] names = propertyNames[i];
				if (names.length != totalNumArgs)
					continue;
				return names[argNum];
			}
			return null;
		}
		
		/**
		 * Return the array of property names for the method with the given total number of args.
		 * @param totalNumArgs
		 * @return the array of property names (in index order of the arguments in factory method) or <code>null</code> if there is no property names for that method with that number of args.
		 * This array must not be modified. It is not a copy.
		 * 
		 * @since 1.2.0
		 */
		public String[] getPropertyNames(int totalNumArgs) {
			for (int i = 0; i < propertyNames.length; i++) {
				String[] names = propertyNames[i];
				if (names.length != totalNumArgs)
					continue;
				return names;
			}
			return null;			
		}
		
		/**
		 * Return the argument index for the given property name, or <code>null</code> if property is not one of the arguments.
		 * @param propertyName name of property to search for.
		 * @param totalNumArgs total number of arguments for the given creation method. This is used to select the property names list.
		 * @return the index of the property name in the arguments of the method, or <code>-1</code> if not a property in the method.
		 * 
		 * @since 1.2.0
		 */
		public int getArgIndex(String propertyName, int totalNumArgs) {
			for (int i = 0; i < propertyNames.length; i++) {
				String[] names = propertyNames[i];
				if (names.length != totalNumArgs)
					continue;
				for (int j = 0; j < names.length; j++) {
					if (propertyName.equals(names[j]))
						return j;
				}
			}
			return -1;
		}

		/**
		 * Returns <code>true</code> if the method call is a static call.
		 * @return Returns the isStatic.
		 * 
		 * @since 1.2.0
		 */
		public boolean isStatic() {
			return isStatic;
		}
	}
	
	/**
	 * Return the method data for the given creation method name.
	 * @param createMethodName
	 * @return the method data or <code>null</code> if not a creation method.
	 * 
	 * @since 1.2.0
	 */
	public MethodData getMethodData(String createMethodName) {
		return (MethodData) methodToData.get(createMethodName);
	}
	
	/**
	 * Return the factory creation data, if any, for the receiver type in the factory method invocation.
	 * This is a help method to handle if the receiver could be either an instance ref (i.e. a non bean) or
	 * is a static class reference.
	 * @param methodInvocation the method invocation. <b>Note:</b> The invocation must be connected into a Resource that is in a ResourceSet that is connected to the JEM java model.
	 * @return the creation data or <code>null</code> if the type is not a factory.
	 * 
	 * @see #getCreationData(JavaHelpers)
	 * @since 1.2.0
	 */
	public static FactoryCreationData getCreationData(PTMethodInvocation methodInvocation) {
		PTExpression receiver = methodInvocation.getReceiver();
		JavaHelpers recvType; 
		if (receiver instanceof PTInstanceReference) {
			recvType = ((PTInstanceReference) receiver).getReference().getJavaType();
		} else if (receiver instanceof PTName) {
			recvType = JavaRefFactory.eINSTANCE.reflectType(((PTName) receiver).getName(), receiver);	// It is a static class
		} else
			return null;	// Don't think it is a factory because don't know the receiver.
		return getCreationData(recvType);
	}
	/**
	 * Return the factory creation data, if any for the given java type.
	 * @param jType the factory type.
	 * @return the creation data or <code>null</code> if the type is not a factory.
	 * 
	 * @since 1.2.0
	 */
	public static FactoryCreationData getCreationData(JavaHelpers jType) {
		FeatureAttributeValue factoryAttributeValue = BeanUtilities.getSetBeanDecoratorFeatureAttributeValue(jType, IBaseBeanInfoConstants.FACTORY_CREATION);
		if (factoryAttributeValue == null)
			return null;
		else if (factoryAttributeValue.getInternalValue() != null)
			return (FactoryCreationData) factoryAttributeValue.getInternalValue();
		else {
			Object value = factoryAttributeValue.getValue();
			if (value instanceof Object[][]) {
				Object[][] mvalues = (Object[][]) value;
				if (mvalues.length > 1) {
					Object[] classData = mvalues[0];
					if (classData.length >= 3 && (classData[0] == null || classData[0] instanceof String) && classData[1] instanceof Boolean && classData[2] instanceof Boolean) {
						String initString = (String) classData[0];
						boolean shared = ((Boolean) classData[1]).booleanValue();
						boolean onFreefrom = ((Boolean) classData[2]).booleanValue();
						Map methodMap = new HashMap(mvalues.length);
						for (int i = 0; i < mvalues.length; i++) {
							Object[] mvalue = mvalues[i];
							if (mvalue.length < 3)
								continue; // Not a valid entry.
							if (!(mvalue[0] instanceof String) || !(mvalue[1] instanceof String) || !(mvalue[2] instanceof Boolean))
								continue; // Not valid method name or creation class name or static.
							List lpropertyNames = new ArrayList(mvalue.length - 2);
							for (int j = 2; j < mvalue.length; j++) {
								if (!(mvalue[j] instanceof Object[]))
									continue; // Not a valid list parms.
								Object[] objects = (Object[]) mvalue[j];
								String[] methodPropertyNames = new String[objects.length];
								for (int k = 0; k < objects.length; k++) {
									if (objects[k] instanceof String)
										methodPropertyNames[k] = (String) objects[k];
									else
										methodPropertyNames[k] = null;
								}
								lpropertyNames.add(methodPropertyNames);
							}
							methodMap.put(mvalue[0], new MethodData((String) mvalue[1], ((Boolean) mvalue[2]).booleanValue(), (String[][]) lpropertyNames.toArray(new String[lpropertyNames
									.size()][])));
						}
						if (!methodMap.isEmpty()) {
							FactoryCreationData data = new FactoryCreationData(methodMap, initString, shared, onFreefrom);
							factoryAttributeValue.setInternalValue(data);
							return data;
						}
					}					
				}				
			}
		}
		
		// Some problem, return an empty one.
		FactoryCreationData data = new FactoryCreationData(Collections.EMPTY_MAP, null, false, false);
		factoryAttributeValue.setInternalValue(data);
		return data;
	}

	/**
	 * The init string to use to create the toolkit if needed.
	 * @return Returns the initString. <code>null</code> if not needed or to use default constructor.
	 * 
	 * @since 1.2.0
	 */
	public String getInitString() {
		return initString;
	}

	/**
	 * Is this toolkit shared? <code>true</code> if it should use the toolkit that the parent is using, or if not using a toolkit
	 * it should use an existing one of the exact same type (prompting if there is more than one), or create one if none found.
	 * <p>
	 * Use <code>false</code> if the toolkit should be created for each component. This would be used for something that is more
	 * like a controller of a component than a toolkit.
	 * @return Returns the shared state.
	 * 
	 * @since 1.2.0
	 */
	public boolean isShared() {
		return shared;
	}
	
	/**
	 * Return whether instances of the factory when it is created should be forced to be GLOBAL_GLOBAL and on the free form.
	 * @return <code>true</code> if factory should be on the freeform, <code>false</code> to do default location determination.
	 * 
	 * @since 1.2.0
	 */
	public boolean isOnFreeform() {
		return onFreeform;
	}
}

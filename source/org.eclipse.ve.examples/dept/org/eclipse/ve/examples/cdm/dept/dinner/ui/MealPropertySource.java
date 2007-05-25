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
package org.eclipse.ve.examples.cdm.dept.dinner.ui;
/*
 *  $RCSfile: MealPropertySource.java,v $
 *  $Revision: 1.5 $  $Date: 2007-05-25 04:13:08 $ 
 */
 
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.Diagram;
import org.eclipse.ve.internal.propertysheet.StringPropertyDescriptor;

/**
 * @author richkulp
 *
 * The property source for the Meal (which is a diagram).
 */
public class MealPropertySource implements IPropertySource {
	protected Diagram meal;
	protected IPropertyDescriptor[] descriptors;
	protected static final String MEAL = "meal";

	public MealPropertySource(Diagram meal) {
		this.meal = meal;
	}

	public Object getEditableValue() {
		return meal;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (descriptors == null) {
			descriptors = new IPropertyDescriptor[] { 
				new StringPropertyDescriptor(MEAL, "meal name"),
				new StringPropertyDescriptor(DinnerConstants.COMPANY_URL, "company url")};
			((StringPropertyDescriptor) descriptors[0]).setNullInvalid(true);
			((StringPropertyDescriptor) descriptors[1]).setNullInvalid(true);			
		};

		return descriptors;
	}

	public Object getPropertyValue(Object propertyKey) {
		if (DinnerConstants.COMPANY_URL.equals(propertyKey)) {
			Object kv = meal.getKeyedValues().get(DinnerConstants.COMPANY_URL);
			if (kv != null)
				return kv;
		} else if (MEAL.equals(propertyKey))
			return meal.getName();
		return null;
	}

	public boolean isPropertySet(Object propertyKey) {
		if (DinnerConstants.COMPANY_URL.equals(propertyKey)) {
			return meal.getKeyedValues().get(DinnerConstants.COMPANY_URL) != null;
		} else if (MEAL.equals(propertyKey))
			return meal.eIsSet(CDMPackage.eINSTANCE.getDiagram_Name());
		return false;
	}

	public void resetPropertyValue(Object propertyKey) {
		if (DinnerConstants.COMPANY_URL.equals(propertyKey)) {
			meal.getKeyedValues().removeKey(DinnerConstants.COMPANY_URL);
		} else if (MEAL.equals(propertyKey))
			meal.setName(null);
	}

	public void setPropertyValue(Object propertyKey, Object value) {
		if (value instanceof String) {
			if (DinnerConstants.COMPANY_URL.equals(propertyKey)) {
				EStringToStringMapEntryImpl newKV =
					(EStringToStringMapEntryImpl) EcoreFactory.eINSTANCE.create(EcorePackage.eINSTANCE.getEStringToStringMapEntry());
				newKV.setKey((String)propertyKey);
				newKV.setValue((String)value);
				int keyPos = meal.getKeyedValues().indexOfKey(propertyKey);
				if (keyPos != -1)
					meal.getKeyedValues().set(keyPos, newKV);
				else
					meal.getKeyedValues().add(newKV);
			} else if (MEAL.equals(propertyKey))
				meal.setName((String) value);
		}
	}

}

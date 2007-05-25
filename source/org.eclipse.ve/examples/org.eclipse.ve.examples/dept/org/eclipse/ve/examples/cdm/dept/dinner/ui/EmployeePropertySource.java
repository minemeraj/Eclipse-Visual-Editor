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
package org.eclipse.ve.examples.cdm.dept.dinner.ui;
/*
 *  $RCSfile: EmployeePropertySource.java,v $
 *  $Revision: 1.5 $  $Date: 2007-05-25 04:13:09 $ 
 */

import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cdm.DiagramFigure;
import org.eclipse.ve.internal.propertysheet.StringPropertyDescriptor;
/**
 * This is the property source for an Entree.
 */
public class EmployeePropertySource implements IPropertySource {
	protected DiagramFigure employee;
	protected IPropertyDescriptor[] descriptors;

	public EmployeePropertySource(DiagramFigure employee) {
		this.employee = employee;
	}

	public Object getEditableValue() {
		return employee;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (descriptors == null) {
			descriptors = new IPropertyDescriptor[] { new StringPropertyDescriptor(DinnerConstants.EMPLOYEE, "employee name")};
			((StringPropertyDescriptor) descriptors[0]).setNullInvalid(true);
		};

		return descriptors;
	}

	public Object getPropertyValue(Object propertyKey) {
		if (DinnerConstants.EMPLOYEE.equals(propertyKey)) {
			Object kv = employee.getKeyedValues().get(DinnerConstants.EMPLOYEE);
			if (kv != null)
				return kv;
		}
		return null;
	}

	public boolean isPropertySet(Object propertyKey) {
		if (DinnerConstants.EMPLOYEE.equals(propertyKey)) {
			return employee.getKeyedValues().get(DinnerConstants.EMPLOYEE) != null;
		}
		return false;
	}

	public void resetPropertyValue(Object propertyKey) {
		if (DinnerConstants.EMPLOYEE.equals(propertyKey)) {
			employee.getKeyedValues().removeKey(DinnerConstants.EMPLOYEE);
		}
	}

	public void setPropertyValue(Object propertyKey, Object value) {
		if (value instanceof String && DinnerConstants.EMPLOYEE.equals(propertyKey)) {
			EStringToStringMapEntryImpl newKV =
				(EStringToStringMapEntryImpl) EcoreFactory.eINSTANCE.create(EcorePackage.eINSTANCE.getEStringToStringMapEntry());
			newKV.setKey((String)propertyKey);
			newKV.setValue((String)value);
			int keyPos = employee.getKeyedValues().indexOfKey(propertyKey);
			if (keyPos != -1)
				employee.getKeyedValues().set(keyPos, newKV);
			else
				employee.getKeyedValues().add(newKV);
		}
	}

}

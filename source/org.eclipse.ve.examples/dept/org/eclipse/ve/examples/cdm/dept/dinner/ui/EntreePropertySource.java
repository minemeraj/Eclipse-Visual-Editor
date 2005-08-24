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
 *  $RCSfile: EntreePropertySource.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:16:43 $ 
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
public class EntreePropertySource implements IPropertySource {
	protected DiagramFigure entree;
	protected IPropertyDescriptor[] descriptors;

	public EntreePropertySource(DiagramFigure entree) {
		this.entree = entree;
	}

	public Object getEditableValue() {
		return entree;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (descriptors == null) {
			descriptors = new IPropertyDescriptor[] { new StringPropertyDescriptor(DinnerConstants.FOOD, "entree type")};
			((StringPropertyDescriptor) descriptors[0]).setNullInvalid(true);
		};

		return descriptors;
	}

	public Object getPropertyValue(Object propertyKey) {
		if (DinnerConstants.FOOD.equals(propertyKey)) {
			Object kv = entree.getKeyedValues().get(DinnerConstants.FOOD);
			if (kv != null)
				return kv;
		}
		return null;
	}

	public boolean isPropertySet(Object propertyKey) {
		if (DinnerConstants.FOOD.equals(propertyKey)) {
			return entree.getKeyedValues().get(DinnerConstants.FOOD) != null;
		}
		return false;
	}

	public void resetPropertyValue(Object propertyKey) {
		if (DinnerConstants.FOOD.equals(propertyKey)) {
			entree.getKeyedValues().removeKey(DinnerConstants.FOOD);
		}
	}

	public void setPropertyValue(Object propertyKey, Object value) {
		if (value instanceof String && DinnerConstants.FOOD.equals(propertyKey)) {
			EStringToStringMapEntryImpl newKV =
				(EStringToStringMapEntryImpl) EcoreFactory.eINSTANCE.create(EcorePackage.eINSTANCE.getEStringToStringMapEntry());
			newKV.setKey(propertyKey);
			newKV.setValue(value);
			int keyPos = entree.getKeyedValues().indexOfKey(propertyKey);
			if (keyPos != -1)
				entree.getKeyedValues().set(keyPos, newKV);
			else
				entree.getKeyedValues().add(newKV);
		}
	}

}

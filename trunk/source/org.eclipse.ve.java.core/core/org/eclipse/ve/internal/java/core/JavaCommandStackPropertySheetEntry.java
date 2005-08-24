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
/*
 *  $RCSfile: JavaCommandStackPropertySheetEntry.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:30:45 $ 
 */
package org.eclipse.ve.internal.java.core;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.ModelChangeController;
import org.eclipse.ve.internal.cde.properties.ModelChangeControllerPropertySheetEntry;

import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.java.rules.RuledPropertySetCommand;
import org.eclipse.ve.internal.java.rules.RuledRestoreDefaultPropertyCommand;

import org.eclipse.ve.internal.propertysheet.IDescriptorPropertySheetEntry;

/**
 * This is used in the JBCF to handle JBCF entries appropriately.
 * @version 	1.0
 * @author
 */
public class JavaCommandStackPropertySheetEntry extends ModelChangeControllerPropertySheetEntry {

	
	
	protected EditDomain domain;
	/**
	 * Constructor for JBCFCommandStackPropertySheetEntr.
	 * @param stack
	 * @param parent
	 * @param provider
	 */
	public JavaCommandStackPropertySheetEntry(
		EditDomain domain,
		CommandStack stack,
		JavaCommandStackPropertySheetEntry parent,
		IPropertySourceProvider provider) {
		super((ModelChangeController) domain.getData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY), stack, parent, provider);
		this.domain = domain;
	}

	/**
	 * @see AbstractPropertySheetEntry#primFillValues(Object, Object[])
	 */
	protected void primFillValues(Object newEditValue, Object[] valuesArray) {
		// If the length is only one, don't bother we duplicating checks
		if (valuesArray.length > 1 && newEditValue instanceof EObject) {
			if (fDescriptors[0].getId() instanceof EReference) {
				EReference sf = (EReference) fDescriptors[0].getId();
				EObject newValue = (EObject) newEditValue;
				// If the feature is containment, or if the EObject is not contained, or is contained as a <properties>,
				// then the new value should be cloned and become a <properties> for each setting.
				if (sf.isContainment() || newValue.eContainer() == null || newValue.eContainmentFeature() == JCMPackage.eINSTANCE.getMemberContainer_Properties()) {
					valuesArray[0] = newEditValue;
					for (int i=1; i<valuesArray.length; i++) {
						valuesArray[i] = EcoreUtil.copy((EObject) newEditValue);	// Create a copy
					}
					return;
				}
			}
		}
		
		// Any other types are just used.
		super.primFillValues(newEditValue, valuesArray);	// Standard fill is appropriate
	}
	
	/**
	 * Create a property sheet entry of the desired type.
	 * Use the provider passed in.
	 *
	 * It is used to create children of this current entry.
	 */
	protected IDescriptorPropertySheetEntry createPropertySheetEntry(IPropertySourceProvider provider) {
		return new JavaCommandStackPropertySheetEntry(domain, fStack, this, provider);
	}	


	/**
	 * @see org.eclipse.ve.internal.propertysheet.CommandStackPropertySheetEntry#createRestorePropertyCommand(IPropertySource, String, Object)
	 */
	protected Command createRestorePropertyCommand(
		IPropertySource propertySource,
		String resetString,
		Object descriptorID) {
		return new RuledRestoreDefaultPropertyCommand(resetString, domain, propertySource, descriptorID);
	}

	/**
	 * @see org.eclipse.ve.internal.propertysheet.CommandStackPropertySheetEntry#createSetPropertyCommand(IPropertySource, String, Object, Object)
	 */
	protected Command createSetPropertyCommand(IPropertySource propertySource, String applyString, Object descriptorID, Object value) {
		return new RuledPropertySetCommand(applyString, domain, propertySource, descriptorID, value);
	}

}

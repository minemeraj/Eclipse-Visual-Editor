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
 *  $RCSfile: EnsureRequiredImplicitCommand.java,v $
 *  $Revision: 1.1 $  $Date: 2006-02-06 17:14:38 $ 
 */
package org.eclipse.ve.internal.java.core;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.beaninfo.common.IBaseBeanInfoConstants;
import org.eclipse.jem.internal.beaninfo.common.FeatureAttributeValue;
import org.eclipse.jem.internal.instantiation.ImplicitAllocation;
import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;
 

/**
 * Ensure that there is the required implicits (if any) for the object. 
 * @since 1.2.0
 */
public class EnsureRequiredImplicitCommand extends CommandWrapper {

	private final IJavaInstance target;
	private final EditDomain domain;

	/**
	 * Create the command with the given target.
	 * 
	 * @since 1.2.0
	 */
	public EnsureRequiredImplicitCommand(IJavaInstance target, EditDomain domain) {
		super();
		this.target = target;
		this.domain = domain;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper#prepare()
	 */
	protected boolean prepare() {
		return target != null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper#execute()
	 */
	public void execute() {
		// See if we have any required implicits.
		FeatureAttributeValue val = BeanUtilities.getSetBeanDecoratorFeatureAttributeValue(target.getJavaType(),
				IBaseBeanInfoConstants.REQUIRED_IMPLICIT_PROPERTIES);
		if (val != null) { 
			ResourceSet rset = EMFEditDomainHelper.getResourceSet(domain);
			RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
			Object fval = val.getValue();
			if (fval instanceof String)
				processRequired((String) fval, target, cbld, rset);
			else if (fval instanceof String[]) {
				String[] requireds = (String[]) fval;
				for (int i = 0; i < requireds.length; i++) {
					processRequired(requireds[i], target, cbld, rset);
				}
			}
			if (!cbld.isEmpty()) {
				command = cbld.getCommand();
				command.execute();
			}
		}
	}
	
	private void processRequired(String featureName, IJavaInstance target, CommandBuilder cbld, ResourceSet rset) {
		EStructuralFeature reqFeature = target.getJavaType().getEStructuralFeature(featureName);
		if (reqFeature != null && !target.eIsSet(reqFeature)) {
			ImplicitAllocation allocation = InstantiationFactory.eINSTANCE.createImplicitAllocation(target, reqFeature);
			IJavaInstance reImpSetting = BeanUtilities.createJavaObject((JavaHelpers) reqFeature.getEType(), rset, allocation);
			cbld.applyAttributeSetting(target, reqFeature, reImpSetting);
		}
	}

}

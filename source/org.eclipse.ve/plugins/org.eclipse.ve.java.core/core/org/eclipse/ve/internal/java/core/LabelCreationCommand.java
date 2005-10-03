/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: LabelCreationCommand.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-03 19:20:57 $ 
 */
package org.eclipse.ve.internal.java.core;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;
 

/**
 * A command to use by IContainmentHandlers to set the label/text for a control if not already
 * set. This MUST be called only on creation and BEFORE the label is added to the model. Otherwise
 * the location of child and text would be incorrect.
 * 
 * @since 1.2.0
 */
public class LabelCreationCommand extends CommandWrapper {

	private final IJavaObjectInstance model;
	private final String labelSFname;
	private final String labelToUse;
	private final EditDomain domain;

	/**
	 * Create the command with the required args.
	 * @param model model to use
	 * @param labelSFname name of the feature of the model to set
	 * @param labelToUse the label to use.
	 * @param domain domain to use
	 * 
	 * @since 1.2.0
	 */
	public LabelCreationCommand(IJavaObjectInstance model, String labelSFname, String labelToUse, EditDomain domain) {
		super();
		this.model = model;
		this.labelSFname = labelSFname;
		this.labelToUse = labelToUse;
		this.domain = domain;
	}
	
	protected boolean prepare() {
		return true;
	}
	
	public void execute() {
		EStructuralFeature sf = model.eClass().getEStructuralFeature(labelSFname);
		if (sf != null && !model.eIsSet(sf)) {
			ResourceSet resourceSet = EMFEditDomainHelper.getResourceSet(domain);
			IJavaInstance newCLabel = BeanUtilities.createString(resourceSet, labelToUse);
			CommandBuilder cb = new CommandBuilder();
			cb.applyAttributeSetting(model, sf, newCLabel);
			command = cb.getCommand();
			command.execute();
		}
	}

}

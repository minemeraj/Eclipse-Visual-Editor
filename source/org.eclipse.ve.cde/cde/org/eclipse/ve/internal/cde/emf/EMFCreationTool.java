package org.eclipse.ve.internal.cde.emf;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EMFCreationTool.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:07 $ 
 */

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;

import org.eclipse.ve.internal.cde.core.CDECreationTool;
import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator;

/**
 * CDE Creation Tool. It exposes the EditDomain and
 * if a factory implements IDomainedFactory, it will set
 * the domain into the factory when activated.
 * @version 	1.0
 * @author
 */
public class EMFCreationTool extends CDECreationTool {

	public static String CREATION_POLICY_KEY = "org.eclipse.ve.internal.cde.core.creationtool.policy"; //$NON-NLS-1$
	protected CreationPolicy fCreationPolicy;
	protected boolean fHasLookedupCreationPolicy = false;

	/**
	 * Constructor for EMFCreationTool.
	 */
	public EMFCreationTool() {
	}
	/**
	 * Constructor for EMFCreationTool.
	 * @param arg0
	 */
	public EMFCreationTool(CreationFactory factory) {
		super(factory);
	}

	/**
	 * Return the edit domain.
	 */
	public org.eclipse.gef.EditDomain getDomain() {
		return super.getDomain();
	}

	/**
	 * @see Tool#activate()
	 */
	public void activate() {
		super.activate();

		//Frog

		CreationFactory fact = getFactory();
		if (fact instanceof IDomainedFactory)
			 ((IDomainedFactory) fact).setEditDomain((EditDomain) getDomain());
	}

	protected Command getCommand() {
		Command result = super.getCommand();
		if (result != null) {
			Object type = getFactory().getObjectType();
			// There is an API where an implementor of EMFCreationTool.CreationPolicy can register themselves
			// on a decorator with a key
			// The purpose of this is so that people can decorate their classes with CreationPolicy
			// that can put commands in front of and after the set of commands that are called on creation
			// This is cach'd for speed of lookup
			if (fCreationPolicy == null && !fHasLookedupCreationPolicy) {
				if (type instanceof EClassifier) {
					ClassDescriptorDecorator decorator =
						(ClassDescriptorDecorator) ClassDecoratorFeatureAccess.getDecoratorWithKeyedFeature(
							(EClassifier) type,
							ClassDescriptorDecorator.class,
							CREATION_POLICY_KEY);
					if (decorator != null) {
						String creationPolicyClassName = (String) decorator.getKeyedValues().get(CREATION_POLICY_KEY);
						// The class name may be from another plugin so we must instantiate it correctly
						try {
							fCreationPolicy = (CreationPolicy) CDEPlugin.createInstance(null, creationPolicyClassName);
						} catch (Exception exc) {
							// If the class can't be created then just drop down and let the regular command be returned
						}
					}
				}
				fHasLookedupCreationPolicy = true;
			}
			if (fCreationPolicy != null) {
				return fCreationPolicy.getCommand(result, (EditDomain) getDomain(), getCreateRequest());
			} else {
				return result;
			}
		}
		return null;
	}

	public interface CreationPolicy {
		public Command getCommand(Command aCommand, EditDomain domain, CreateRequest createRequest);
		/**
		 *  Optionally overides the default null constructor super string.
		 *  e.g., super(new Arg1()) 
		 *  @return null if no overide is needed, String if an overide exists
		 */
		public String getDefaultSuperString(EClass superClass);
	}

}

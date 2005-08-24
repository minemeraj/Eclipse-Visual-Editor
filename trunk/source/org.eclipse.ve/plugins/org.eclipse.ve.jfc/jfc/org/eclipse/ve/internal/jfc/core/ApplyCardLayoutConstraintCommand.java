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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: ApplyCardLayoutConstraintCommand.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:09 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.ve.internal.cde.core.AnnotationLinkagePolicy;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;
import org.eclipse.ve.internal.cdm.Annotation;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;

import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;

/**
 * @author pwalker
 *
 * Special command for applying a constraint to a component dropped onto
 * a container with a java.awt.CardLayout.
 * 
 * Since a String constraint is required for a CardLayout, we use 
 * the 'name' property, if it is set, in the proxy adapter. If not, we create
 * this command so we can set the name with a unique name which is not available
 * when we first construct it... only at execution time.
 */
public class ApplyCardLayoutConstraintCommand extends CommandWrapper {
	protected EObject fConstraintComponent, fComponent;
	protected ResourceSet fResourceSet;
	protected EditDomain fDomain;

	/**
	 * Constructor for ApplyCardLayoutConstraintCommand.
	 */
	public ApplyCardLayoutConstraintCommand(EObject constraintComponent, EObject component, EditDomain domain) {
		super();
		fConstraintComponent = constraintComponent;
		fComponent = component;
		fDomain = domain;
		fResourceSet = JavaEditDomainHelper.getResourceSet(domain);
	}

	protected boolean prepare() {
		// Need to override prepare because prepare expects to have a command
		// create, and at the time of prepare being called, we don't have a command yet.
		return fConstraintComponent != null && fComponent != null && fDomain != null && fResourceSet != null;
	}
	public void execute() {
		RuledCommandBuilder cb = new RuledCommandBuilder(fDomain);
		EStructuralFeature sfName =
			JavaInstantiation.getSFeature((IJavaObjectInstance) fComponent, JFCConstants.SF_COMPONENT_NAME),
			sfConstraintConstraint =
				JavaInstantiation.getSFeature(fResourceSet, JFCConstants.SF_CONSTRAINT_CONSTRAINT);
		String cardname = null;
		if (!fComponent.eIsSet(sfName)) {
			// Since 'name' is not set, we need to set it so the cardlayout will use it
			// by default for it's constraint. We'll get this name from the annotation.
			IJavaObjectInstance stringObject = null;
			AnnotationLinkagePolicy policy = fDomain.getAnnotationLinkagePolicy();
			Annotation ann = policy.getAnnotation(fComponent);
			if (ann != null) {
				cardname = (String) ann.getKeyedValues().get(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
			}
			// Found a name
			if (cardname != null)
				stringObject = BeanUtilities.createString(fResourceSet, cardname);
			else {
				// Either didn't find the annotation or didn't find annotation name... so create one
				String classname = ((IJavaObjectInstance) fComponent).getJavaType().getJavaName();
				int index = classname.lastIndexOf("."); //$NON-NLS-1$
				if (index != -1) 
					classname = classname.substring(index + 1);
				if (classname.length() > 1)
					classname = Character.toLowerCase(classname.charAt(0)) + classname.substring(1);
				else 
					classname = classname.toLowerCase();
				cardname = NameInCompositionPropertyDescriptor.getUniqueNameInComposition(fDomain, classname);
			}
			// Need to apply the name property and than unset the dummy constraint that was set earlier.
			cb.applyAttributeSetting(fComponent, sfName, stringObject);
		}
		cb.cancelAttributeSetting(fConstraintComponent, sfConstraintConstraint);
		command = cb.getCommand();
		command.execute();
	}
}

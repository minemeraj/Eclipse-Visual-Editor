package org.eclipse.ve.internal.java.rules;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: DefaultPropertyRule.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

/**
 * Default JBCF implementation of the Property Rule.
 */
public class DefaultPropertyRule implements IPropertyRule {

	/**
	 * @see org.eclipse.ve.internal.java.rules.IPropertyRule#preSet(EditDomain, EObject, EObject)
	 */
	public Command preSet(EditDomain domain, EObject target, EObject newValue, EReference feature) {
		if (newValue != null) {
			// So as not to waste time doing any verifications and walking children
			// we just return a special command to do it.
			return new DefaultPreSetCommand(domain, target, newValue, feature);
		} else
			return null;
	}

	/**
	 * @see org.eclipse.ve.internal.java.rules.IPropertyRule#postSet(EObject)
	 */
	public Command postSet(EditDomain domain, EObject oldValue) {
		if (oldValue != null) {
			// So as not to waste time doing any verifications and walking children
			// we just return a special command to do it.
			return new DefaultPostSetCommand(domain, oldValue);
		} else
			return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.rules.IRule#setRegistry(org.eclipse.ve.internal.cde.rules.IRuleRegistry)
	 */
	public void setRegistry(IRuleRegistry registry) {
		// no-op. We don't care.
	}

}

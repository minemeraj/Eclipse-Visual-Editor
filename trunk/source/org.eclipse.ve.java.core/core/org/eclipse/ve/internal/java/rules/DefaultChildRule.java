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
 *  $RCSfile: DefaultChildRule.java,v $
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
public class DefaultChildRule implements IChildRule {

	/**
	 * @see org.eclipse.ve.internal.java.rules.IChildRule#preCreateChild(EditDomain, EObject, EObject, EReference)
	 */
	public Command preCreateChild(EditDomain domain, EObject target, EObject newChild, EReference feature) {
		if (newChild != null) {
			// So as not to waste time doing any verifications and walking children
			// we just return a special command to do it.
			return new DefaultPreSetCommand(domain, target, newChild, feature);
		} else
			return null;
	}

	/**
	 * @see org.eclipse.ve.internal.java.rules.IChildRule#postDeleteChild(EditDomain, EObject)
	 */
	public Command postDeleteChild(EditDomain domain, EObject oldChild) {
		if (oldChild != null) {
			// So as not to waste time doing any verifications and walking children
			// we just return a special command to do it.
			return new DefaultPostSetCommand(domain, oldChild);
		} else
			return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.rules.IRule#setRegistry(org.eclipse.ve.internal.cde.rules.IRuleRegistry)
	 */
	public void setRegistry(IRuleRegistry registry) {
		// Tno-op. We don't care.

	}

}

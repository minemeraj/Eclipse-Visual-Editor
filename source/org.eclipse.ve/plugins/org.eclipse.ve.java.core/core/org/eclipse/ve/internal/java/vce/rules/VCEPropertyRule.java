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
package org.eclipse.ve.internal.java.vce.rules;
/*
 *  $RCSfile: VCEPropertyRule.java,v $
 *  $Revision: 1.4 $  $Date: 2005-10-03 19:20:57 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

import org.eclipse.ve.internal.java.rules.IPropertyRule;

/**
 * Default VCE Property Rule.
 */
public class VCEPropertyRule implements IPropertyRule {

	/**
	 * @see org.eclipse.ve.internal.java.vce.rules.IPropertyRule#preSet(EditDomain, EObject, EObject, EReference)
	 */
	public Command preSet(EditDomain domain, EObject target, EObject newValue, EReference feature) {
		return new VCEPreSetCommand(domain, target, newValue, feature);
	}
	
	/**
	 * @see org.eclipse.ve.internal.java.vce.rules.IPropertyRule#postSet(EObject)
	 */
	public Command postSet(EditDomain domain, EObject oldValue) {
		if (oldValue != null) {
			// So as not to waste time doing any verifications and walking children
			// we just return a special command to do it. Since if child is null there is nothing to handle.
			return new VCEPostSetCommand(domain, oldValue, false);
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

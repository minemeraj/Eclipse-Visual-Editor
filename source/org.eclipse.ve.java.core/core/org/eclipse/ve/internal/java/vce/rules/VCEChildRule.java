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
 *  $RCSfile: VCEChildRule.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:48 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.rules.IRuleRegistry;
import org.eclipse.ve.internal.java.rules.IChildRule;

/**
 * @author richkulp
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class VCEChildRule implements IChildRule {
	
	/**
	 * @see org.eclipse.ve.internal.java.vce.rules.IChildRule#preCreateChild(EditDomain, EObject, EObject, EReference)
	 */
	public Command preCreateChild(EditDomain domain, EObject target, EObject newChild, EReference feature) {
		return new VCEPreSetCommand(domain, target, newChild, feature);
	}
	
	/**
	 * @see org.eclipse.ve.internal.java.vce.rules.IPropertyRule#postSet(EObject)
	 */
	public Command postDeleteChild(EditDomain domain, EObject oldValue) {
		if (oldValue != null) {
			// So as not to waste time doing any verifications and walking children
			// we just return a special command to do it. Since if child is null there is nothing to handle.
			return new VCEPostSetCommand(domain, oldValue, true);
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

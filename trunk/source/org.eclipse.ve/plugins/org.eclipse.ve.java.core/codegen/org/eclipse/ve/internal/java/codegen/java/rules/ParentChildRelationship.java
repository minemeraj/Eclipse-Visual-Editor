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
/*
 *  $RCSfile: ParentChildRelationship.java,v $
 *  $Revision: 1.4 $  $Date: 2005-10-03 19:20:57 $ 
 */
package org.eclipse.ve.internal.java.codegen.java.rules;

import org.eclipse.emf.ecore.EReference;

import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

/**
 * @author gmendel
 */
public class ParentChildRelationship implements IParentChildRelationship {

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.IParentChildRelationship#isChildRelationShip(org.eclipse.emf.ecore.EReference)
	 */
	public boolean isChildRelationShip(EReference feature) {
		if (feature.isContainment()) return true;
		else	
		  return org.eclipse.ve.internal.java.vce.rules.VCEPostSetCommand.isChildRelationShip(feature);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.rules.IRule#setRegistry(org.eclipse.ve.internal.cde.rules.IRuleRegistry)
	 */
	public void setRegistry(IRuleRegistry registry) {
		// no-op. We don't care.
	}
}

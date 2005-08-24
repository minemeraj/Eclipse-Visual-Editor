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
package org.eclipse.ve.internal.cde.rules;
/*
 *  $RCSfile: IRuleRegistry.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:51 $ 
 */

/**
 * Registry of rules to allow variations in model manipulations.
 * This registry is in the EditDomain. The contents of the registry
 * is completely undetermined. Each application knows what rules
 * it needs. Each application will determine what registry is
 * stored with the EditDomain.
 * 
 * CDE defines a few common rules. See the EditDomain comments
 * for what rules CDE expects to be there.
 */
public interface IRuleRegistry {

	/**
	 * Return a specific rule, keyed by String ID. The actual
	 * object returned is application specific.
	 */
	public IRule getRule(String id);

}

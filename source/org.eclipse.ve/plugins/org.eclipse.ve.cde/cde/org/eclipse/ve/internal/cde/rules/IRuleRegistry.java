package org.eclipse.ve.internal.cde.rules;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IRuleRegistry.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:18:01 $ 
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

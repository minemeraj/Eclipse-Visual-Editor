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
package org.eclipse.ve.internal.java.rules;
/*
 *  $RCSfile: DefaultRuleRegistry.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:48 $ 
 */

import org.eclipse.ve.internal.cde.rules.IRule;
import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

/**
 * Default JBCF Rule Registry
 */
public class DefaultRuleRegistry implements IRuleRegistry {

	protected static final IPropertyRule PROPERTY_RULE = new DefaultPropertyRule();
	protected static final IChildRule CHILD_RULE = new DefaultChildRule();
	
	/**
	 * @see org.eclipse.ve.internal.cde.rules.IRuleRegistry#getRule(String)
	 */
	public IRule getRule(String id) {
		if (IPropertyRule.RULE_ID.equals(id))
			return PROPERTY_RULE;
		else if (IChildRule.RULE_ID.equals(id))
			return CHILD_RULE;
		else
			return null;
			
	}

}

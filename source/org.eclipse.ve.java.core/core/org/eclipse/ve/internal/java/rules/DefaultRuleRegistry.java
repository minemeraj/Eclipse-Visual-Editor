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
 *  $RCSfile: DefaultRuleRegistry.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
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

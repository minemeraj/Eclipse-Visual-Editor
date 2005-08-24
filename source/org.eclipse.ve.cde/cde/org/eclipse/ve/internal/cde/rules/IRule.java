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
 * Created on Sep 4, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.eclipse.ve.internal.cde.rules;

/**
 * @author richkulp
 *
 * A rule in the registry.
 */
public interface IRule {
	
	/**
	 * Set the registry that this rule is in. If it doesn't matter
	 * to this rule, then it can be ignored.
	 * @param registry
	 */
	public void setRegistry(IRuleRegistry registry);

}

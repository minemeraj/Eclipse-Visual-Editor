/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IEventMethodParsingRule.java,v $
 *  $Revision: 1.4 $  $Date: 2005-02-15 23:28:35 $ 
 */
package org.eclipse.ve.internal.java.codegen.java.rules;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import org.eclipse.ve.internal.cde.rules.IRule;

/**
 * @author gmendel
 */
public interface IEventMethodParsingRule extends IRule {

	static final String 	RULE_ID = "ruleEventMethodParsing" ;	 //$NON-NLS-1$

	public boolean ignoreAnonymousEventMethod (MethodDeclaration m) ;

}

/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IEventMethodParsingRule.java,v $
 *  $Revision: 1.3 $  $Date: 2004-08-27 15:34:10 $ 
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

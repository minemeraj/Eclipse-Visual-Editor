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
 *  $RCSfile: IEventProcessingRule.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */
package org.eclipse.ve.internal.java.codegen.java.rules;

import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.ve.internal.cde.rules.IRule;

import org.eclipse.ve.internal.java.codegen.model.BeanPart;

/**
 * @author Gili Mendel
 *
 */
public interface IEventProcessingRule extends IRule {

    static String 	RULE_ID = "ruleEventProcessing" ;	 //$NON-NLS-1$
    /**
     * Beyond the init JCMMethod, should the given method be parsed for events
     * for a given bean part, bp.   e.g., initConnections()
     */
	boolean  parseForEvents (AbstractMethodDeclaration method, BeanPart bp) ;	
	boolean  isEventInitMethodSigniture (String methodName) ;

}

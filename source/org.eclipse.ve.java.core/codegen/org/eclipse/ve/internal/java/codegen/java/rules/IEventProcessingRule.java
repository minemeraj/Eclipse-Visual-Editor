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
 *  $RCSfile: IEventProcessingRule.java,v $
 *  $Revision: 1.4 $  $Date: 2005-02-15 23:28:35 $ 
 */
package org.eclipse.ve.internal.java.codegen.java.rules;

import org.eclipse.jdt.core.dom.MethodDeclaration;

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
	boolean  parseForEvents (MethodDeclaration method, BeanPart bp) ;	
	boolean  isEventInitMethodSigniture (String methodName) ;

}

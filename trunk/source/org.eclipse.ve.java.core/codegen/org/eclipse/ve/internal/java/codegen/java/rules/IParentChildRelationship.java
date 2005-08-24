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
 *  $RCSfile: IParentChildRelationship.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:48 $ 
 */
package org.eclipse.ve.internal.java.codegen.java.rules;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.ve.internal.cde.rules.IRule;

/**
 * @author gmendel
 */
public interface IParentChildRelationship extends IRule {
	
	static String RULE_ID = "ruleParentChild" ;	 //$NON-NLS-1$
	
	public boolean isChildRelationShip (EReference feature) ;
}

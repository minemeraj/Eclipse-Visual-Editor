/*
 * Created on May 12, 2003
 * by gmendel
 *
*******************************************************************************
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
 *  $RCSfile: IParentChildRelationship.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:28:35 $ 
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

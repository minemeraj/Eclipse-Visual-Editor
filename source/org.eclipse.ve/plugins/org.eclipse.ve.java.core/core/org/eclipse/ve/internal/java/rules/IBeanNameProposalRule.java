/*
 * Created on Jul 2, 2003
 * by gmendel
 *
*******************************************************************************
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
 *  $RCSfile: IBeanNameProposalRule.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */
package org.eclipse.ve.internal.java.rules;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ve.internal.cde.rules.IRule;

/**
 * @author gmendel
 */
public interface IBeanNameProposalRule extends IRule {
	
	public static final String RULE_ID = "ruleBeanNameProposal";

    /**
     * Return a default name proposal for a given type
     * @param The type of the bean (e.g., javax.swing.JPanel)
     * @param args  For a JVE, it will typically hold the IType involved
     * @param rs
     * @return default name proposal
     */
	public String getProspectInstanceVariableName(String typeName, Object[] args,  ResourceSet rs) ;
}

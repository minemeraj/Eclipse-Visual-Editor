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
 *  $RCSfile: IBeanNameProposalRule.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:30:48 $ 
 */
package org.eclipse.ve.internal.java.rules;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ve.internal.cde.rules.IRule;

/**
 * @author gmendel
 */
public interface IBeanNameProposalRule extends IRule {
	
	public static final String RULE_ID = "ruleBeanNameProposal"; //$NON-NLS-1$

    /**
     * Return a default name proposal for a given type
     * @param The type of the bean (e.g., javax.swing.JPanel)
     * @param args  For a JVE, it will typically hold the IType involved
     * @param rs
     * @return default name proposal
     */
	public String getProspectInstanceVariableName(String typeName, Object[] args,  ResourceSet rs) ;
}

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
 *  $RCSfile: EventProcessingRule.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:30:48 $ 
 */
package org.eclipse.ve.internal.java.codegen.java.rules;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

import org.eclipse.ve.internal.java.codegen.model.BeanPart;

/**
 * @author Gili Mendel
 *
 */
public class EventProcessingRule implements IEventProcessingRule {
	
	public final static String EVENT_INIT_METHOD = "initConnections" ; //$NON-NLS-1$

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.IEvenProcessingRule#parseForEvents(AbstractMethodDeclaration, BeanPart)
	 */
	public boolean parseForEvents(MethodDeclaration method, BeanPart bp) {
        // Parse this method for events, if it is the init method, or if it is a initConnection JCMMethod.		                                   
		if (EVENT_INIT_METHOD.equals(method.getName().getIdentifier())) { 
			if (method.parameters().size() == 0)
			  return true ;
		}	
		else if (bp.isInitMethod(method))
		    return true ;
	    return false ;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.IEventProcessingRule#isEventInitMethodSigniture(java.lang.String)
	 */
	public boolean isEventInitMethodSigniture(String methodName) {
		return EVENT_INIT_METHOD.equals(methodName) ;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.rules.IRule#setRegistry(org.eclipse.ve.internal.cde.rules.IRuleRegistry)
	 */
	public void setRegistry(IRuleRegistry registry) {
		// no-op. We don't care.
	}	

}

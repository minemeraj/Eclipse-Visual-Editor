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
 *  $RCSfile: EventMethodParsingRule.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:30:48 $ 
 */
package org.eclipse.ve.internal.java.codegen.java.rules;


import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

/**
 * @author gmendel
 */
public class EventMethodParsingRule implements IEventMethodParsingRule {

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.IEventMethodParsingRule#ignoreAnonymousEventMethod(org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration)
	 */
	public boolean ignoreAnonymousEventMethod(MethodDeclaration m) {
		if (m.getBody() == null) {
			return true ;
		}
		else {
			boolean result = true ;
			for (int i = 0; i < m.getBody().statements().size(); i++) {
				if (!(m.getBody().statements().get(i) instanceof EmptyStatement)) {
					result = false ;
					break ;
				}
			}
			return result ;		
		}	
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.rules.IRule#setRegistry(org.eclipse.ve.internal.cde.rules.IRuleRegistry)
	 */
	public void setRegistry(IRuleRegistry registry) {
		// no-op. We don't care.
	}	

}

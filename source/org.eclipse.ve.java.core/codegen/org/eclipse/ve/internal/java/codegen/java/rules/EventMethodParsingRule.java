/*
 * Created on Apr 3, 2003
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
 *  $RCSfile: EventMethodParsingRule.java,v $
 *  $Revision: 1.2 $  $Date: 2004-03-05 23:18:38 $ 
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

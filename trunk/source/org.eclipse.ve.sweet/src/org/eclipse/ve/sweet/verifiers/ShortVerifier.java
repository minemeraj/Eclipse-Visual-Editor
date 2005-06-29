/*
 * Copyright (C) 2005 db4objects Inc.  http://www.db4o.com
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     db4objects - Initial API and implementation
 */
package org.eclipse.ve.sweet.verifiers;

import org.eclipse.ve.sweet.verifier.IVerifier;


/**
 * DoubleVerifier.  Verify data input for Doubles
 *
 * @author djo
 */
public class ShortVerifier implements IVerifier {
    
	/* (non-Javadoc)
	 * @see org.eclipse.jface.binding.verifier.IVerifier#verifyFragment(java.lang.String)
	 */
	public boolean verifyFragment(String fragment) {
		return fragment.matches("\\-?[0-9]*");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.binding.verifier.IVerifier#verifyFullValue(java.lang.String)
	 */
	public boolean verifyFullValue(String value) {
        try {
            Short.parseShort(value);
            return true;
        } catch (Throwable t) {
        	return false;
        }
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.binding.verifier.IVerifier#getHint()
	 */
	public String getHint() {
		return "Please enter a number between " + Short.MIN_VALUE + " and " + Short.MAX_VALUE + ".";
	}

}

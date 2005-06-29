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
package org.eclipse.ve.sweet.verifiers.reusable;

import org.eclipse.ve.sweet.verifier.IVerifier;

/**
 * ReadOnlyVerifier.  The verifier that is used for read-only fields.
 *
 * @author djo
 */
public class ReadOnlyVerifier implements IVerifier {
    
    private static ReadOnlyVerifier singleton = null;
    
    public static ReadOnlyVerifier getDefault() {
        if (singleton == null) {
            singleton = new ReadOnlyVerifier();
        }
        return singleton;
    }

	/* (non-Javadoc)
	 * @see org.eclipse.jface.binding.verifier.IVerifier#verifyFragment(java.lang.String)
	 */
	public boolean verifyFragment(String fragment) {
		// No changes are allowed
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.binding.verifier.IVerifier#verifyFullValue(java.lang.String)
	 */
	public boolean verifyFullValue(String value) {
        // But the current value is accepted
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.binding.verifier.IVerifier#getHint()
	 */
	public String getHint() {
		return "No changes are allowed in this field";
	}

}

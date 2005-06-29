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
package org.eclipse.ve.sweet.verifier;

/**
 * IVerifier.  Verify data entry for some value or data type.
 */
public interface IVerifier {
    public boolean verifyFragment(String fragment);
    
	public boolean verifyFullValue(String value);
    
    public String getHint();
}

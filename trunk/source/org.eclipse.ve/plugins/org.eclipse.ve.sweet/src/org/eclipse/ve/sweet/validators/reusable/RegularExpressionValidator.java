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
package org.eclipse.ve.sweet.validators.reusable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.ve.sweet.validator.IValidator;


/**
 * RegularExpressionVerifier.  A Verifier that uses regular expressions to
 * specify verification rules.
 *
 * @author djo
 */
public class RegularExpressionValidator implements IValidator {
    
    private Pattern fragmentRegex;
    private Pattern fullValueRegex;
    private String hint;
    
	/**
     * Constructor RegularExpressionVerifier.
     * 
     * Verify input using regulare expressions.
     * 
	 * @param fragmentRegex
	 * @param fullValueRegex
	 * @param hint
	 */
	public RegularExpressionValidator(String fragmentRegex,
			String fullValueRegex, String hint) {
		super();
		this.fragmentRegex = Pattern.compile(fragmentRegex);
		this.fullValueRegex = Pattern.compile(fullValueRegex);
		this.hint = hint;
	}
    
	/* (non-Javadoc)
	 * @see org.eclipse.jface.binding.verifier.IVerifier#verifyFragment(java.lang.String)
	 */
	public String isValidPartialInput(String fragment) {
        Matcher matcher = fragmentRegex.matcher(fragment);
		if (matcher.find())
            return null;
        else
            return hint;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellEditorValidator#isValid(java.lang.Object)
	 */
	public String isValid(Object value) {
        String stringValue = (String) value;
        Matcher matcher = fullValueRegex.matcher(stringValue);
        if (matcher.find())
            return null;
        else
            return hint;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.binding.verifier.IVerifier#getHint()
	 */
	public String getHint() {
		return hint;
	}

}

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
package org.eclipse.ve.sweet.validator;

import java.util.Date;
import java.util.HashMap;

import org.eclipse.ve.sweet.validators.ByteValidator;
import org.eclipse.ve.sweet.validators.DateValidator;
import org.eclipse.ve.sweet.validators.DoubleValidator;
import org.eclipse.ve.sweet.validators.FloatValidator;
import org.eclipse.ve.sweet.validators.IntValidator;
import org.eclipse.ve.sweet.validators.LongValidator;
import org.eclipse.ve.sweet.validators.ShortValidator;
import org.eclipse.ve.sweet.validators.reusable.ReadOnlyValidator;
import org.eclipse.ve.sweet.validators.reusable.RegularExpressionValidator;


/**
 * Verifier.  The base verifier from which all verifiers can be found.
 *
 * @author djo
 */
public class ValidatorRegistry {
	private static HashMap validators;
    
    /**
     * Associate a particular verifier with a particular Java class.
     * 
     * @param klass
     * @param verifier
     */
    public static void associate(String klass, IValidator verifier) {
        validators.put(klass, verifier);
    }
    
    /**
     * Return an IVerifier for a specific class.
     * 
     * @param klass The Class to verify
     * @return An appropriate IVerifier
     */
    public static IValidator get(String klass) {
        IValidator result = (IValidator) validators.get(klass);
        if (result == null) {
            return ReadOnlyValidator.getDefault();
        }
        return result;
    }
    
    static {
        validators = new HashMap();
        
        // Standalone validators here...
        associate(Integer.TYPE.getName(), new IntValidator());
        associate(Byte.TYPE.getName(), new ByteValidator());
        associate(Short.TYPE.getName(), new ShortValidator());
        associate(Long.TYPE.getName(), new LongValidator());
        associate(Float.TYPE.getName(), new FloatValidator());
        associate(Double.TYPE.getName(), new DoubleValidator());
        
        associate(Integer.class.getName(), new IntValidator());
        associate(Byte.class.getName(), new ByteValidator());
        associate(Short.class.getName(), new ShortValidator());
        associate(Long.class.getName(), new LongValidator());
        associate(Float.class.getName(), new FloatValidator());
        associate(Double.class.getName(), new DoubleValidator());
        associate(Date.class.getName(), new DateValidator());
        
        // Regex-implemented validators here...
        associate(Character.TYPE.getName(), new RegularExpressionValidator(
                "^.$|^$", ".", "Please type a character"));
        associate(Character.class.getName(), new RegularExpressionValidator(
                "^.$|^$", ".", "Please type a character"));
        associate(Boolean.TYPE.getName(), new RegularExpressionValidator(
                "^$|Y$|^y$|^Ye$|^ye$|^Yes$|^yes$|^T$|^t$|^Tr$|^tr$|^Tru$|^tru$|^True$|^true$|^N$|^n$|^No$|^no$|^F$|^f$|^Fa$|^fa$|^Fal$|^fal$|^Fals$|^fals$|^False$|^false$", 
                "Yes$|^yes$|^No$|^no$|^True$|^true$|^False$|^false", 
                "Please type \"Yes\", \"No\", \"True\", or \"False\""));
        associate(Boolean.class.getName(), new RegularExpressionValidator(
                "^$|Y$|^y$|^Ye$|^ye$|^Yes$|^yes$|^T$|^t$|^Tr$|^tr$|^Tru$|^tru$|^True$|^true$|^N$|^n$|^No$|^no$|^F$|^f$|^Fa$|^fa$|^Fal$|^fal$|^Fals$|^fals$|^False$|^false$", 
                "Yes$|^yes$|^No$|^no$|^True$|^true$|^False$|^false", 
                "Please type \"Yes\", \"No\", \"True\", or \"False\""));
        associate(String.class.getName(), new RegularExpressionValidator("^.*$", "^.*$", ""));
    }
}

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

import java.util.HashMap;

import org.eclipse.ve.sweet.verifiers.ByteVerifier;
import org.eclipse.ve.sweet.verifiers.DoubleVerifier;
import org.eclipse.ve.sweet.verifiers.FloatVerifier;
import org.eclipse.ve.sweet.verifiers.IntVerifier;
import org.eclipse.ve.sweet.verifiers.LongVerifier;
import org.eclipse.ve.sweet.verifiers.ShortVerifier;
import org.eclipse.ve.sweet.verifiers.reusable.ReadOnlyVerifier;
import org.eclipse.ve.sweet.verifiers.reusable.RegularExpressionVerifier;


/**
 * Verifier.  The base verifier from which all verifiers can be found.
 *
 * @author djo
 */
public class Verifier {
	private static HashMap verifiers;
    
    /**
     * Associate a particular verifier with a particular Java class.
     * 
     * @param klass
     * @param verifier
     */
    public static void associate(String klass, IVerifier verifier) {
        verifiers.put(klass, verifier);
    }
    
    /**
     * Return an IVerifier for a specific class.
     * 
     * @param klass The Class to verify
     * @return An appropriate IVerifier
     */
    public static IVerifier get(String klass) {
        IVerifier result = (IVerifier) verifiers.get(klass);
        if (result == null) {
            return ReadOnlyVerifier.getDefault();
        }
        return result;
    }
    
    static {
        verifiers = new HashMap();
        
        // Standalone verifiers here...
        associate(Integer.TYPE.getName(), new IntVerifier());
        associate(Byte.TYPE.getName(), new ByteVerifier());
        associate(Short.TYPE.getName(), new ShortVerifier());
        associate(Long.TYPE.getName(), new LongVerifier());
        associate(Float.TYPE.getName(), new FloatVerifier());
        associate(Double.TYPE.getName(), new DoubleVerifier());
        
        associate(Integer.class.getName(), new IntVerifier());
        associate(Byte.class.getName(), new ByteVerifier());
        associate(Short.class.getName(), new ShortVerifier());
        associate(Long.class.getName(), new LongVerifier());
        associate(Float.class.getName(), new FloatVerifier());
        associate(Double.class.getName(), new DoubleVerifier());
        
        // Regex-implemented verifiers here...
        associate(Character.TYPE.getName(), new RegularExpressionVerifier(
                "^.$|^$", ".", "Please type a character"));
        associate(Character.class.getName(), new RegularExpressionVerifier(
                "^.$|^$", ".", "Please type a character"));
        associate(Boolean.TYPE.getName(), new RegularExpressionVerifier(
                "^$|Y$|^y$|^Ye$|^ye$|^Yes$|^yes$|^T$|^t$|^Tr$|^tr$|^Tru$|^tru$|^True$|^true$|^N$|^n$|^No$|^no$|^F$|^f$|^Fa$|^fa$|^Fal$|^fal$|^Fals$|^fals$|^False$|^false$", 
                "Yes$|^yes$|^No$|^no$|^True$|^true$|^False$|^false", 
                "Please type \"Yes\", \"No\", \"True\", or \"False\""));
        associate(Boolean.class.getName(), new RegularExpressionVerifier(
                "^$|Y$|^y$|^Ye$|^ye$|^Yes$|^yes$|^T$|^t$|^Tr$|^tr$|^Tru$|^tru$|^True$|^true$|^N$|^n$|^No$|^no$|^F$|^f$|^Fa$|^fa$|^Fal$|^fal$|^Fals$|^fals$|^False$|^false$", 
                "Yes$|^yes$|^No$|^no$|^True$|^true$|^False$|^false", 
                "Please type \"Yes\", \"No\", \"True\", or \"False\""));
        associate(String.class.getName(), new RegularExpressionVerifier("^.*$", "^.*$", ""));
    }
}

/*
 * Copyright (C) 2005 db4objects Inc.  http://www.db4o.com
 */
package org.eclipse.ve.sweet.test;

import org.eclipse.ve.sweet.verifier.IVerifier;
import org.eclipse.ve.sweet.verifiers.reusable.RegularExpressionVerifier;


public class Person {
    String name;
    int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public IVerifier getAgeVerifier() {
        return new RegularExpressionVerifier("^[0-9]*$", "^[0-9]{1,3}$", 
                "Please enter an age between 0 and 999");
    }
}


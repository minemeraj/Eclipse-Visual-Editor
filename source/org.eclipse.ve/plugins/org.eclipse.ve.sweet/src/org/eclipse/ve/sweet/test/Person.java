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
package org.eclipse.ve.sweet.test;

import org.eclipse.ve.sweet.validator.IValidator;
import org.eclipse.ve.sweet.validators.reusable.RegularExpressionValidator;


public class Person {
    String name;
    int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public IValidator getAgeVerifier() {
        return new RegularExpressionValidator("^[0-9]*$", "^[0-9]{1,3}$", 
                "Please enter an age between 0 and 999");
    }
    
    public String getName(){
    	return name;
    }
    public void setName(String aName){
    	name = aName;
    }
    public int getAge(){
    	return age;
    }
    public void setAge(int anAge){
    	age = anAge;
    }
    
}


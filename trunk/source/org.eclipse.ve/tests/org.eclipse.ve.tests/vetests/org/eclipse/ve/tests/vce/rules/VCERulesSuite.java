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
package org.eclipse.ve.tests.vce.rules;
/*
 *  $RCSfile: VCERulesSuite.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:54:15 $ 
 */
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Gili Mendel
 * 
 * This cass can be used as a TestSuite, or a Platform Runnable
 *
 */

public class VCERulesSuite extends TestSuite {
	// Testa cases to be include in the suite
	private static Class testsList[] = { PreSetTest.class, PostSetTest.class } ;
	public static String pkgName = "org.eclipse.ve.tests.vce.rules" ;
    
	/**
	 * Constructor for CodegenRulesSuite.
	 */
	public VCERulesSuite() {
		super();
		populateSuite() ;
	}

	/**
	 * Constructor for CodegenRulesSuite.
	 * @param theClass
	 */
	public VCERulesSuite(Class theClass) {
		super(theClass);
		populateSuite() ;
	}

	/**
	 * Constructor for CodegenRulesSuite.
	 * @param name
	 */
	public VCERulesSuite(String name) {
		super(name);
		populateSuite() ;
	}

    private void populateSuite () {
    	for (int i=0; i<testsList.length; i++)
    	  addTestSuite(testsList[i]) ;
    }
    
	public void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		return new VCERulesSuite("Test for: "+pkgName);
	}

}

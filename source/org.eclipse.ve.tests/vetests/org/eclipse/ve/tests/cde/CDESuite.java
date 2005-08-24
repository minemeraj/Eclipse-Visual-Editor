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
package org.eclipse.ve.tests.cde;
/*
 *  $RCSfile: CDESuite.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:54:15 $ 
 */
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author jmyers
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CDESuite extends TestSuite {

	// Test cases to be include in the suite
	private static Class testsList[] = {
		InverseMaintenanceAdapterSharedBeforePropagationTest.class,
		InverseMaintenanceAdapterSharedAfterPropagationTest.class,
		InverseMaintenanceAdapterContainedBeforePropagate.class,
		InverseMaintenanceAdapterContainedAfterPropagate.class,
		ResourceInverseMaintenanceAdapterTest.class
		                               } ;
	public static String pkgName = "org.eclipse.ve.tests.cde" ;
	    
	/**
	 * Constructor for CDESuite.
	 */
	public CDESuite() {
		super();
		populateSuite() ;
	}

	/**
	 * Constructor for CDESuite.
	 * @param theClass
	 */
	public CDESuite(Class theClass) {
		super(theClass);
		populateSuite() ;
	}

	/**
	 * Constructor for CDESuite.
	 * @param name
	 */
	public CDESuite(String name) {
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
		return new CDESuite("Test for: "+pkgName);
	}
}

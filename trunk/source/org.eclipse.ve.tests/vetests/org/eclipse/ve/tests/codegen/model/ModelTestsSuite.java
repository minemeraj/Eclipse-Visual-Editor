/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on Apr 26, 2004 by Gili Mendel
 */
package org.eclipse.ve.tests.codegen.model;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Gili Mendel
 *
 */
public class ModelTestsSuite extends TestSuite {
	// Testa cases to be include in the suite
	private static Class testsList[] = { MethodSortTests.class };
	public static String pkgName = "org.eclipse.ve.tests.codegen.model";

	/**
	 * Constructor for CodegenRulesSuite.
	 */
	public ModelTestsSuite() {
		super();
		populateSuite();
	}

	/**
	 * Constructor for CodegenRulesSuite.
	 * @param theClass
	 */
	public ModelTestsSuite(Class theClass) {
		super(theClass);
		populateSuite();
	}

	/**
	 * Constructor for CodegenRulesSuite.
	 * @param name
	 */
	public ModelTestsSuite(String name) {
		super(name);
		populateSuite();
	}

	private void populateSuite() {
		for (int i = 0; i < testsList.length; i++)
			addTestSuite(testsList[i]);
	}

	public void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		return new ModelTestsSuite("Test for: " + pkgName);
	}
	
	
	
}

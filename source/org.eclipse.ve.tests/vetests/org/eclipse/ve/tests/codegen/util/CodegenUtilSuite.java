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
package org.eclipse.ve.tests.codegen.util;
/*
 *  $RCSfile: CodegenUtilSuite.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:54:15 $ 
 */
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Gili Mendel
 * 
 * This cass can be used as a TestSuite, or a Platform Runnable
 */

public class CodegenUtilSuite extends TestSuite {

	private static Class testsList[] =
		{
			FieldTest1.class,
			FieldTest2.class,
			FieldTest3.class,
			FieldTest4.class,
			FieldTest5.class,
			FieldTest6.class,
			FieldTest7.class,
			FieldTest8.class,
			FieldTest9.class,
			FieldTest10.class };
	//	private static Class testsList[] = { FieldTest6.class,  } ;
	public static String pkgName = "org.eclipse.ve.tests.codegen.util";

	/**
	 * Constructor for CodegenRulesSuite.
	 */
	public CodegenUtilSuite() {
		super();
		populateSuite();
	}

	/**
	 * Constructor for CodegenRulesSuite.
	 * @param theClass
	 */
	public CodegenUtilSuite(Class theClass) {
		super(theClass);
		populateSuite();
	}

	/**
	 * Constructor for CodegenRulesSuite.
	 * @param name
	 */
	public CodegenUtilSuite(String name) {
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
		return new CodegenUtilSuite("Test for: " + pkgName);
	}
}

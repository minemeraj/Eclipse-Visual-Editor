package org.eclipse.ve.tests.codegen.util;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CodegenUtilSuite.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:38:46 $ 
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

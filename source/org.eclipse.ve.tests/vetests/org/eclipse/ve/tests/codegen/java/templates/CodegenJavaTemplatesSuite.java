package org.eclipse.ve.tests.codegen.java.templates;
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
 *  $RCSfile: CodegenJavaTemplatesSuite.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:38:46 $ 
 */
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Gili Mendel
 * 
 * This cass can be used as a TestSuite, or a Platform Runnable
 */

public class CodegenJavaTemplatesSuite extends TestSuite {

	private static Class testsList[] = { TemplateObjectFactoryTest.class };
	public static String pkgName = "org.eclipse.ve.tests.codegen.java.templates";

	/**
	 * Constructor for CodegenRulesSuite.
	 */
	public CodegenJavaTemplatesSuite() {
		super();
		populateSuite();
	}

	/**
	 * Constructor for CodegenRulesSuite.
	 * @param theClass
	 */
	public CodegenJavaTemplatesSuite(Class theClass) {
		super(theClass);
		populateSuite();
	}

	/**
	 * Constructor for CodegenRulesSuite.
	 * @param name
	 */
	public CodegenJavaTemplatesSuite(String name) {
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
		return new CodegenJavaTemplatesSuite("Test for: " + pkgName);
	}
}

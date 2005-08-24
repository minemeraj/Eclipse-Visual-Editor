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
package org.eclipse.ve.tests;
/*
 *  $RCSfile: AllSuites.java,v $
 *  $Revision: 1.11 $  $Date: 2005-08-24 23:54:15 $ 
 */

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.ve.tests.cde.CDESuite;
import org.eclipse.ve.tests.codegen.java.rules.CodegenRulesSuite;
import org.eclipse.ve.tests.codegen.java.templates.CodegenJavaTemplatesSuite;
import org.eclipse.ve.tests.codegen.model.ModelTestsSuite;
import org.eclipse.ve.tests.codegen.resolve.ResolveSuite;
import org.eclipse.ve.tests.codegen.util.CodegenUtilSuite;
import org.eclipse.ve.tests.vce.rules.VCERulesSuite;

public class AllSuites extends TestSuite {
	// Testa cases to be include in the suite
	private static Class suitesList[] =
		{ CDESuite.class, CodegenRulesSuite.class, CodegenJavaTemplatesSuite.class, CodegenUtilSuite.class, VCERulesSuite.class, ModelTestsSuite.class,
			ResolveSuite.class};

	public static String pkgName = "Visual Editor jUnit Test Suite";

	/**
	 * Constructor for AllSuites.
	 */
	public AllSuites() {
		super();
		populateSuite();
	}

	/**
	 * Constructor for AllSuites.
	 * @param theClass
	 */
	public AllSuites(Class theClass) {
		super(theClass);
		populateSuite();
	}

	/**
	 * Constructor for AllSuites.
	 * @param name
	 */
	public AllSuites(String name) {
		super(name);
		populateSuite();
	}

	private void populateSuite() {
		for (int i = 0; i < suitesList.length; i++)
			try {
				Test ts = (Test) suitesList[i].newInstance();
				addTest(ts);
			} catch (Exception e) {
			}
	}

	public static Test suite() {
		return new AllSuites(pkgName);
	}

}

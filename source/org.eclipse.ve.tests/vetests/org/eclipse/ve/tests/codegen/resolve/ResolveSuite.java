/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ResolveSuite.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:54:15 $ 
 */
package org.eclipse.ve.tests.codegen.resolve;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import org.eclipse.jem.tests.JavaProjectUtil;

import org.eclipse.ve.tests.VETestsPlugin;

/**
 * Test the resolve types suite.
 * 
 * @since 1.0.0
 */
public class ResolveSuite extends TestSetup {

	// Test cases to be include in the suite
	private static final Class testsList[] = {TestSimpleDefaultPackageNames.class, TestSimpleNames.class, TestComplexNames.class, TestRefreshSuper.class,
			TestRefreshProject.class, TestRefreshClass.class, TestChangeSuper.class, TestImportChangesAddContainer.class, TestImportChangesRemoveContainer.class,
			TestImportChangesAddToExisting.class, TestImportChangesRemoveFromExisting.class, TestImportChanges.class, TestImportRevert.class, TestBlastReplace.class};

	public static Test suite() {
		return new ResolveSuite();
	}
	
	/**
	 * Constructor for BeanInfoSuite.
	 */
	public ResolveSuite(String name) {
		super(new TestSuite(name) {

			{
				for (int i = 0; i < testsList.length; i++) {
					addTestSuite(testsList[i]);
				}
			}

		});
	}

	public ResolveSuite() {
		this("Test Codegen Resolve Suite");
	}

	public static final String RESOLVE_TESTDATA_PROJECT = "ResolveTestCases";
	public static final String RESOLVE_TESTDATA2_PROJECT = "ResolveTestCases2";

	private boolean oldAutoBuildingState; // autoBuilding state before we started.

	protected void setUp() throws Exception {
		System.out.println("-- Initializing the Codegen Resolve test data --"); //$NON-NLS-1$
		oldAutoBuildingState = JavaProjectUtil.setAutoBuild(true);
		String[] zipPaths = new String[2];
		zipPaths[0] = Platform.asLocalURL(VETestsPlugin.getPlugin().getBundle().getEntry("resources/testdata/resolvetests.zip")).getFile();
		zipPaths[1] = Platform.asLocalURL(VETestsPlugin.getPlugin().getBundle().getEntry("resources/testdata/resolvetests2.zip")).getFile();
		IProject[] projects = JavaProjectUtil.importProjects(new String[] { RESOLVE_TESTDATA_PROJECT, RESOLVE_TESTDATA2_PROJECT}, zipPaths);
		assertNotNull(projects[0]);
		assertNotNull(projects[1]);
		JavaProjectUtil.waitForAutoBuild();
		System.out.println("-- Data initialized --"); //$NON-NLS-1$
	}

	protected void tearDown() throws Exception {
		ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {

			public void run(IProgressMonitor monitor) throws CoreException {
				JavaProjectUtil.deleteProject(JavaProjectUtil.getProject(RESOLVE_TESTDATA_PROJECT));
				JavaProjectUtil.deleteProject(JavaProjectUtil.getProject(RESOLVE_TESTDATA2_PROJECT));
			}
		}, ResourcesPlugin.getWorkspace().getRoot(), 0, null);

		JavaProjectUtil.setAutoBuild(oldAutoBuildingState);
	}

}

/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.tests.perf;

import junit.framework.Test;
import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.*;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.part.FileEditorInput;

public class Scenario179 extends TestCase implements Test {
	private static final String TEST_FILENAME = "test/UserAdmin.java";

	private IWorkbenchWindow getWorkbenchWindow() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		return workbench.getActiveWorkbenchWindow();
	}

	public void openJavaPerspective() {
		System.out.println("-- open Java perspective --");		
		try {
			PlatformUI.getWorkbench().showPerspective(
					"org.eclipse.jdt.ui.JavaPerspective", getWorkbenchWindow());
			PlatformUI
					.getWorkbench()
					.getPerspectiveRegistry()
					.setDefaultPerspective("org.eclipse.jdt.ui.JavaPerspective");
		} catch (WorkbenchException e) {
			e.printStackTrace();
		}
	}

	protected void setUp() throws Exception {
		super.setUp();
		closeAllPerspectives();
		openJavaPerspective();
		PerfSuite.waitFor(10000);
	}

	public void closeAllPerspectives() {
		IWorkbenchAction action = ActionFactory.CLOSE_ALL_PERSPECTIVES
				.create(getWorkbenchWindow());
		action.run();
	}

	public void closeAllEditors() {
		IWorkbenchAction action = ActionFactory.CLOSE_ALL
				.create(getWorkbenchWindow());
		action.run();
	}

	public void testUserAdminOpen() {
		long start;
		
		start = System.currentTimeMillis();
		openJVE(TEST_FILENAME);
		PerfSuite.waitFor(119, 60000);
		System.err.println("-- editor opened in " + (System.currentTimeMillis() - start) + "ms --");
		
		PerfSuite.waitFor(10000);		
		closeAllEditors();
		PerfSuite.waitFor(5000);		
		
		start = System.currentTimeMillis();		
		openJVE(TEST_FILENAME);
		PerfSuite.waitFor(119, 60000);
		System.err.println("-- editor opened in " + (System.currentTimeMillis() - start) + "ms --");
		
		PerfSuite.waitFor(5000);		
		closeAllEditors();
		PerfSuite.waitFor(5000);		
		
		closeOpenTestProject();
		PerfSuite.waitFor(10000);		
		
		start = System.currentTimeMillis();		
		openJVE(TEST_FILENAME);
		PerfSuite.waitFor(119, 60000);
		System.err.println("-- editor opened in " + (System.currentTimeMillis() - start) + "ms --");
		
		PerfSuite.waitFor(10000);		
		closeAllEditors();
		PerfSuite.waitFor(5000);		
	}

	private void closeOpenTestProject() {
		System.out.println("-- close and reopen project --");		
		try {
			IProject project = PerfSuite.getTestProject();
			project.close(null);
			project.open(null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void openJVE(String member) {
		openEditor(member, "org.eclipse.ve.internal.java.codegen.editorpart.JavaVisualEditor");
	}
	private void openEditor(String member, String id) {
		System.out.println("-- open editor on " + member + " --");
		IProject project = PerfSuite.getTestProject();
		IEditorInput ei = new FileEditorInput((IFile) project
				.findMember(member));
		try {
			getWorkbenchWindow()
					.getActivePage()
					.openEditor(
							ei,
							id,
							true);
		} catch (PartInitException e) {
			fail("Unable to open editor " + e + " " + id);
		}
	}
	
	
	
}

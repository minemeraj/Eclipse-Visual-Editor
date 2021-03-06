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

import org.eclipse.jem.util.PerformanceMonitorUtil;

public class Scenario179JDT extends TestCase implements Test {
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
		PerfSuite.waitFor(8000);
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

	public void testSimpleJavaEditor1() {
		runSimpleJavaEditor();
	}
	public void runSimpleJavaEditor() {
		long start;
		
		for (int i = 0; i < 3; i++) {
			start = System.currentTimeMillis();
			PerformanceMonitorUtil.getMonitor().snapshot(100);
			openJavaEditor(TEST_FILENAME);
			PerformanceMonitorUtil.getMonitor().snapshot(101);
			System.err.println("-- Java editor opened in " + (System.currentTimeMillis() - start) + "ms --");			
			PerfSuite.waitFor(4000);
			closeAllEditors();
			if (i == 1) { // second time 
				closeOpenTestProject();
				PerfSuite.waitFor(4000);
			} else {
				PerfSuite.waitFor(2000);
			}
		}
		PerfSuite.waitFor(5000);		
	}
	
	private void closeOpenTestProject() {
		System.out.println("-- close and reopen project --");		
		try {
			IProject project = PerfSuiteJDT.getTestProject();
			project.close(null);
			project.open(null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void openJavaEditor(String member) {
		openEditor(member, "org.eclipse.jdt.ui.CompilationUnitEditor");
	}
	private void openEditor(String member, String id) {
		System.out.println("-- open editor on " + member + " --");
		IProject project = PerfSuiteJDT.getTestProject();
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

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.tests.perf;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.*;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.activities.ICategoryActivityBinding;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;
import org.eclipse.ui.internal.*;
import org.eclipse.ui.internal.ide.IDEInternalPreferences;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;

import org.eclipse.jem.tests.JavaProjectUtil;
import org.eclipse.jem.util.PerformanceMonitorUtil;
import org.eclipse.jem.util.PerformanceMonitorUtil.PerformanceEvent;

import org.eclipse.ve.tests.VETestsPlugin;

/**
 * Test the performance types suite.
 * 
 * @since 1.0.0
 */
public class PerfSuite extends TestSetup {

	private static final boolean RECREATE_TESTDATA_PROJECT = true;

	private static final boolean DELETE_TESTDATA_PROJECT = true;

	public static PerfSuite instance;

	// Test cases to be include in the suite
	private static final Class testsList[] = { Scenario179.class};

	public static final String TESTDATA_PROJECT = "PerformanceTest";

	public static Test suite() {
		return (instance = new PerfSuite());
	}

	public static IProject getTestProject() {
		return instance.getProject(PerfSuite.TESTDATA_PROJECT);
	}

	/**
	 * Constructor for BeanInfoSuite.
	 */
	public PerfSuite(String name) {
		super(new TestSuite(name) {

			{
				for (int i = 0; i < testsList.length; i++) {
					addTestSuite(testsList[i]);
				}
			}

		});
	}

	public PerfSuite() {
		this("Performance Test Suite");
		instance = this;
	}

	private boolean oldAutoBuildingState; // autoBuilding state before we

	// started.

	protected void setUp() throws Exception {
		if (RECREATE_TESTDATA_PROJECT) {
			System.out.println("-- Initializing the performance test data --"); //$NON-NLS-1$
			oldAutoBuildingState = JavaProjectUtil.setAutoBuild(true);
			String[] zipPaths = new String[1];
			zipPaths[0] = Platform.asLocalURL(VETestsPlugin.getPlugin().getBundle().getEntry("resources/testdata/" + TESTDATA_PROJECT + ".zip"))
					.getFile();
			IProject[] projects = JavaProjectUtil.importProjects(new String[] { TESTDATA_PROJECT}, zipPaths);
			assertNotNull(projects[0]);
			JavaProjectUtil.waitForAutoBuild();
			System.out.println("-- Data initialized --"); //$NON-NLS-1$
			getProject(TESTDATA_PROJECT);
		}

		disablePromptOnExit();
		disableStartupPlugins();
		enableActivities("org.eclipse.categories.javaCategory");
	}

	protected void disablePromptOnExit() {
		IDEWorkbenchPlugin.getDefault().getPluginPreferences().setValue(IDEInternalPreferences.EXIT_PROMPT_ON_CLOSE_LAST_WINDOW, false);
		IDEWorkbenchPlugin.getDefault().savePluginPreferences();
	}

	protected void disableStartupPlugins() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint(PlatformUI.PLUGIN_ID, IWorkbenchConstants.PL_STARTUP);
		IExtension[] extensions = point.getExtensions();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < extensions.length; ++i) {
			IExtension extension = extensions[i];
			sb.append(extension.getNamespace());
			sb.append(";");
		}
		WorkbenchPlugin.getDefault().getPluginPreferences().setValue(IPreferenceConstants.PLUGINS_NOT_ACTIVATED_ON_STARTUP, sb.toString());
	}

	protected void enableActivities(String categoryId) {
		WorkbenchPlugin.getDefault().getPluginPreferences().setValue(IPreferenceConstants.SHOULD_PROMPT_FOR_ENABLEMENT, false);
		WorkbenchPlugin.getDefault().savePluginPreferences();
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchActivitySupport activitySupport = workbench.getActivitySupport();
		Set activityBindings = activitySupport.getActivityManager().getCategory(categoryId).getCategoryActivityBindings();
		Set activityIds = new HashSet();
		for (Iterator it = activityBindings.iterator(); it.hasNext();)
			activityIds.add(((ICategoryActivityBinding) it.next()).getActivityId());
		activitySupport.setEnabledActivityIds(activityIds);
	}

	protected void joinAutoBuild() throws InterruptedException, InvocationTargetException {
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(null);
		dialog.run(true, false, new IRunnableWithProgress() {

			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				try {
					Platform.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, monitor);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		});
	}

	protected void tearDown() throws Exception {
		if (DELETE_TESTDATA_PROJECT) {
			ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {

				public void run(IProgressMonitor monitor) throws CoreException {
					JavaProjectUtil.deleteProject(JavaProjectUtil.getProject(TESTDATA_PROJECT));
				}
			}, ResourcesPlugin.getWorkspace().getRoot(), 0, null);
		}
		JavaProjectUtil.setAutoBuild(oldAutoBuildingState);

		joinAutoBuild();
				if (PerformanceMonitorUtil.getMonitor().upload("Scenario 179 " + DateFormat.getDateInstance().format(new Date()))) {
					System.out.println("-- uploaded successfully -- ");
				}
		System.out.println("-- performance suite complete --");
	}

	public IProject getProject(String projectName) {
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProject project = workspace.getRoot().getProject(projectName);
		assertTrue(project.exists());
		if (!project.isOpen()) {
			try {
				project.open(null);
			} catch (CoreException e) {
				fail("Could not open project " + projectName + e);
			}
		}
		try {
			project.refreshLocal(IProject.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			fail("Unable to refresh project <" + projectName + ">");
		}
		return project;
	}

	static public void waitFor(long timeToWait) {
		final long _timeToWait = timeToWait;
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				long start = System.currentTimeMillis();
				long progress = System.currentTimeMillis() + 10000;
				Display display = Display.getDefault();
				while (System.currentTimeMillis() - start < _timeToWait) {
					if (System.currentTimeMillis() > progress) {
						progress = System.currentTimeMillis() + 10000;
					}
					if (!display.readAndDispatch())
						display.sleep();
				}
			}
		});
	}

	static public void waitFor(int step, long timeout) {
		PerformanceListener pl = new PerformanceListener(step);
		PerformanceMonitorUtil.getMonitor().addPerformanceListener(pl);
		long end = System.currentTimeMillis() + timeout;
		while (!pl.done && timeout != 0 && end > System.currentTimeMillis()) {
			waitFor(200);
		}
		PerformanceMonitorUtil.getMonitor().removePerformanceListener(pl);
	}

	static public class PerformanceListener implements PerformanceMonitorUtil.PerformanceListener {

		public boolean done = false;

		public int step = 0;

		public PerformanceListener(int step) {
			this.step = step;
		}

		public void snapshot(PerformanceEvent event) {
			if (event.step == step)
				done = true;
		}
	}

}
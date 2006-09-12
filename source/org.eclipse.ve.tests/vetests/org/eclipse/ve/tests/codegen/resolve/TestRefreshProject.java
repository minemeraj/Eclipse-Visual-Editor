/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TestRefreshProject.java,v $
 *  $Revision: 1.6 $  $Date: 2006-09-12 18:45:40 $ 
 */
package org.eclipse.ve.tests.codegen.resolve;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;

import org.eclipse.ve.internal.java.core.TypeResolver;
 

/**
 * Test refresh of the project.
 * @since 1.0.0
 */
public class TestRefreshProject extends BaseResolveTestCase {

	private static final Path CLASSPATH = new Path(".classpath");
	private static final Path CLASSPATH_COPY = new Path("classpathcopy");
	private IJavaProject javaProject;
	
	public void setUp() throws Exception {
		super.setUp();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(ResolveSuite.RESOLVE_TESTDATA_PROJECT+"/src/test2/InTest2.java"));
		ICompilationUnit cu = JavaCore.createCompilationUnitFrom(file);
		
		// Kludge, make a copy of the .classpath file so that we can restore it on teardown. This is so that
		// it is set up correctly for other tests.
		javaProject = cu.getJavaProject();
		javaProject.getProject().getFile(CLASSPATH).copy(CLASSPATH_COPY, true, new NullProgressMonitor());
		
		resolver = new TypeResolver(cu.getImports(), cu.getType("InTest2"));
	}
	
	public void testRemoveProjectReference() throws JavaModelException {
		resolveType("FromTest2");
		
		// Now let's remove the pre-req to the other project.
		IClasspathEntry[] rawPath = javaProject.getRawClasspath();
		IClasspathEntry[] newPath = new IClasspathEntry[rawPath.length-1];
		IPath test2Path = new Path("/ResolveTestCases2");
		int nx = 0;
		for (int i = 0; i < rawPath.length; i++) {
			if (rawPath[i].getEntryKind() == IClasspathEntry.CPE_PROJECT) {
				if (test2Path.equals(rawPath[i].getPath()))
					continue;
			}
			newPath[nx++] = rawPath[i];
		}
		
		javaProject.setRawClasspath(newPath, new NullProgressMonitor());
		
		// Now we should not get an answer to resolve type.
		assertNull(resolver.resolveType("FromTest2"));
	}
	

	public void testCloseProjectReference() throws CoreException {
		resolveType("FromTest2");
		// We will close the other project. This will be a remove.
		ResourcesPlugin.getWorkspace().getRoot().getProject(ResolveSuite.RESOLVE_TESTDATA2_PROJECT).close(new NullProgressMonitor());
		// Now we should not get an answer to resolve type.
		assertNull(resolver.resolveType("FromTest2"));
	}
	
	public void testRemovePkg() throws JavaModelException {
		resolveType("PkgToDeleteClass");
		
		// Now remove the package that the class is in. See if it is now unresolved.
		javaProject.findPackageFragment(new Path("/ResolveTestCases/src/pkgToDelete")).delete(true, new NullProgressMonitor());
		
		assertNull(resolver.resolveType("PkgToDeleteClass"));
		
		// note: I don't fix this in the teardown because this package is only for this test.
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.tests.codegen.resolve.BaseResolveTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();		
		// Now put the classpath back the way it was.
		ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {

			public void run(IProgressMonitor monitor) throws CoreException {
				javaProject.getProject().getFile(CLASSPATH).delete(true, monitor);
				javaProject.getProject().getFile(CLASSPATH_COPY).copy(CLASSPATH, true, new NullProgressMonitor());
				javaProject.getProject().getFile(CLASSPATH_COPY).delete(true, monitor);
				IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(ResolveSuite.RESOLVE_TESTDATA2_PROJECT);
				project.open(new NullProgressMonitor());
				ResourcesPlugin.getWorkspace().build(IncrementalProjectBuilder.INCREMENTAL_BUILD, new NullProgressMonitor());
			}
		}, new NullProgressMonitor());
	}
	
}

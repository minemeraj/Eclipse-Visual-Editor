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
 *  $RCSfile: TestSimpleDefaultPackageNames.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:54:15 $ 
 */
package org.eclipse.ve.tests.codegen.resolve;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.*;

import org.eclipse.ve.internal.java.codegen.util.TypeResolver;
 

/**
 * Test Simple Names. i.e. resolve with just one non-qualified name. This one tests where main type is in default package.
 * @since 1.0.0
 */
public class TestSimpleDefaultPackageNames extends BaseResolveTestCase {
	
	public void setUp() throws Exception {
		super.setUp();	
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(ResolveSuite.RESOLVE_TESTDATA_PROJECT+"/src/DefClass.java"));
		ICompilationUnit cu = JavaCore.createCompilationUnitFrom(file);
		resolver = new TypeResolver(cu.getImports(), cu.getType("DefClass"));
	}
	
	public void tearDown() {
		resolver.dispose();
	}
		
	public void testSamePackage() throws JavaModelException {
		assertEquals("DefClass1", resolveType("DefClass1").getName());
	}
	
	public void testSamePackageName() throws JavaModelException {
		assertEquals("DefClass1", resolveType(createName("DefClass1")).getName());
	}	
		
}

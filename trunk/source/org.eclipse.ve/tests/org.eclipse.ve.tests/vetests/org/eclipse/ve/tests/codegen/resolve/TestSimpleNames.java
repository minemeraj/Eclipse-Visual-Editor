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
 *  $RCSfile: TestSimpleNames.java,v $
 *  $Revision: 1.5 $  $Date: 2006-05-17 20:16:07 $ 
 */
package org.eclipse.ve.tests.codegen.resolve;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.*;

import org.eclipse.ve.internal.java.core.TypeResolver;
 

/**
 * Test Simple Names. i.e. resolve with just one non-qualified name. This one tests where main type is in a package.
 * @since 1.0.0
 */
public class TestSimpleNames extends BaseResolveTestCase {
	
	public void setUp() throws Exception {
		super.setUp();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(ResolveSuite.RESOLVE_TESTDATA_PROJECT+"/src/pkg/base/BaseClass.java"));
		ICompilationUnit cu = JavaCore.createCompilationUnitFrom(file);
		resolver = new TypeResolver(cu.getImports(), cu.getType("BaseClass"));
	}
	
	public void testSameClass() throws JavaModelException {
		assertEquals("pkg.base.BaseClass", resolveType("BaseClass").getName());
	}
	
	public void testLocalInnerClass() throws JavaModelException {
		assertEquals("pkg.base.BaseClass$Inner0", resolveType("Inner0").getName());
	}
	
	public void testSuperInnerClass() throws JavaModelException {
		assertEquals("pkg2.SuperClass$InnerSuper", resolveType("InnerSuper").getName());
	}

	public void testSuperInterfaceInnerClass() throws JavaModelException {
		assertEquals("pkg2.Interface$InterfaceInner0", resolveType("InterfaceInner0").getName());
	}
	
	public void testFullClassImport() throws JavaModelException {
		assertEquals("pkg1.sub1.FullImport0", resolveType("FullImport0").getName());
	}
	
	public void testSamePackage() throws JavaModelException {
		assertEquals("pkg.base.SamePackage0", resolveType("SamePackage0").getName());
	}
	
	public void testClassFromPackageImport() throws JavaModelException {
		assertEquals("pkg1.sub1.sub2.StarSub2", resolveType("StarSub2").getName());		
	}
	
	public void testInnerFromClassImport() throws JavaModelException {
		assertEquals("pkg2.StarType0$InnerStarType0", resolveType("InnerStarType0").getName());
	}
	
	public void testJavaLangClass() throws JavaModelException {
		assertEquals("java.lang.Number", resolveType("Number").getName());
	}

	public void testSameClassName() throws JavaModelException {
		assertEquals("pkg.base.BaseClass", resolveType(createName("BaseClass")).getName());
	}
	
	public void testLocalInnerClassName() throws JavaModelException {
		assertEquals("pkg.base.BaseClass$Inner0", resolveType(createName("Inner0")).getName());
	}
	
	public void testSuperInnerClassName() throws JavaModelException {
		assertEquals("pkg2.SuperClass$InnerSuper", resolveType(createName("InnerSuper")).getName());
	}

	public void testSuperInterfaceInnerClassName() throws JavaModelException {
		assertEquals("pkg2.Interface$InterfaceInner0", resolveType(createName("InterfaceInner0")).getName());
	}
	
	public void testFullClassImportName() throws JavaModelException {
		assertEquals("pkg1.sub1.FullImport0", resolveType(createName("FullImport0")).getName());
	}
	
	public void testSamePackageName() throws JavaModelException {
		assertEquals("pkg.base.SamePackage0", resolveType(createName("SamePackage0")).getName());
	}
	
	public void testClassFromPackageImportName() throws JavaModelException {
		assertEquals("pkg1.sub1.sub2.StarSub2", resolveType(createName("StarSub2")).getName());		
	}
	
	public void testInnerFromClassImportName() throws JavaModelException {
		assertEquals("pkg2.StarType0$InnerStarType0", resolveType(createName("InnerStarType0")).getName());
	}
	
	public void testJavaLangClassName() throws JavaModelException {
		assertEquals("java.lang.Number", resolveType(createName("Number")).getName());
	}

}
